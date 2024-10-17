package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sap.ass01.businessLogic.*;
import sap.ass01.businessLogic.EBike.EBikeState;

public class AppServiceImpl implements AdminAppService, UserAppService, RideThreadObserver {
    private static final String USER_SERVER_NAME = "userServer";
    private static final String ADMIN_SERVER_NAME = "adminServer";

    private transient List<AdminCallback> adminListeners;
    private transient Map<String, UserCallback> userListeners;  // userID -> callback.
    private transient Map<String, UserCallback> ridesListeners; // bikeID -> callback.

    private Server serverBL;

    private Executor bikesExecutor;
    private Map<String, RideThread> rideThreads; // userID -> thread 

    public AppServiceImpl(Server serverBL){
        this.serverBL = serverBL;
        this.adminListeners = new ArrayList<>();
        this.userListeners = new HashMap<>();
        this.ridesListeners = new HashMap<>();
        this.bikesExecutor = Executors.newCachedThreadPool();
        this.rideThreads = new HashMap<>();
    }

    @Override
    public List<UserInfo> getUsers() throws RemoteException {
        return this.serverBL.getUsers();
    }

    @Override
    public List<EBikeInfo> getEBikes() throws RemoteException {
        return this.serverBL.getEBikes();
    }

    @Override
    public UserInfo createUser(String userID, int credits)
            throws RemoteException, IllegalArgumentException, RepositoryException {
        UserInfo userCreated = this.serverBL.createUser(userID, credits);
        this.adminListeners.forEach(a -> a.notifyUserCreated(userID, credits));
        return userCreated;
    }

    @Override
    public RideInfo beginRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException, RepositoryException {
        RideInfo rideBegun = this.serverBL.beginRide(userID, bikeID);
        EBikeInfo bikeUsed = this.serverBL.getEBikeByID(bikeID);

        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyBikeStateChanged(bikeID, bikeUsed.state(), bikeUsed.loc().x(), bikeUsed.loc().y(), bikeUsed.batteryLevel())));
        this.userListeners.values().forEach(LambdaUtil.wrap(u -> u.notifyBikeStateChanged(bikeID,  bikeUsed.state(), bikeUsed.loc().x(), bikeUsed.loc().y(), bikeUsed.batteryLevel())));

        this.ridesListeners.put(bikeID, this.userListeners.get(userID));

        var thread = new RideThread(this, this.serverBL, rideBegun);
        bikesExecutor.execute(thread);
        rideThreads.put(userID, thread);

        return rideBegun;
    }

    @Override
    public void endRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException, RepositoryException {
        this.serverBL.endRide(userID, bikeID);
        EBikeInfo bikeUsed = this.serverBL.getEBikeByID(bikeID);

        rideThreads.remove(userID).endRide();

        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyBikeStateChanged(bikeID, bikeUsed.state(), bikeUsed.loc().x(), bikeUsed.loc().y(), bikeUsed.batteryLevel())));
        this.userListeners.values().forEach(LambdaUtil.wrap(u -> u.notifyBikeStateChanged(bikeID,  bikeUsed.state(), bikeUsed.loc().x(), bikeUsed.loc().y(), bikeUsed.batteryLevel())));

        this.ridesListeners.remove(bikeID);
    }

    @Override
    public void addCredits(String userID, int credits) throws RemoteException, RepositoryException {
        this.serverBL.addCredits(userID, credits);
        int creditUpdated = this.serverBL.getUserByID(userID).credits();

        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyUserCreditRecharged(userID, creditUpdated)));
        this.userListeners.get(userID).notifyUserCreditRecharged(userID, creditUpdated);
    }

    @Override
    public UserInfo registerUser(String userID, UserCallback callback) throws RemoteException, IllegalArgumentException {
        var userInfo = this.serverBL.getUserByID(userID);
        this.userListeners.put(userID, callback);
        return userInfo;
    }

    @Override
    public void registerAdmin(AdminCallback callback) throws RemoteException {
        this.adminListeners.add(callback);
    }

    @Override
    public void addEBike(String bikeID, P2d pos) throws RemoteException, RepositoryException {
        EBikeInfo bikeCreated = this.serverBL.addEBike(bikeID, pos);
        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyBikeStateChanged(bikeID, bikeCreated.state(), pos.x(), pos.y(), bikeCreated.batteryLevel())));
        this.userListeners.values().forEach(LambdaUtil.wrap(u -> u.notifyBikeStateChanged(bikeID, bikeCreated.state(), pos.x(), pos.y(), bikeCreated.batteryLevel())));
    }

    @Override
    public void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException {
        this.serverBL.removeEBike(bikeID);
        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyBikeStateChanged(bikeID, EBikeState.DISMISSED, 0, 0, 0)));
        this.userListeners.values().forEach(LambdaUtil.wrap(u -> u.notifyBikeStateChanged(bikeID, EBikeState.DISMISSED, 0, 0, 0)));
    }

    @Override
    public List<RideInfo> getRides() throws RemoteException {
        return this.serverBL.getRides();
    }

    @Override
    public void bikeStateChanged(String bikeID, EBikeState newState, double x, double y, int batteryLevel) {
        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyBikeStateChanged(bikeID, newState, x, y, batteryLevel)));
        this.userListeners.values().forEach(LambdaUtil.wrap(u -> u.notifyBikeStateChanged(bikeID, newState, x, y, batteryLevel)));
    }

    @Override
    public void rideStepDone(String bikeID, String rideID, P2d bikePos, int batteryLevel, int userCredits) {
        this.adminListeners.forEach(LambdaUtil.wrap(a -> a.notifyRideStepDone(rideID, bikePos, batteryLevel, userCredits)));
        try {
            this.ridesListeners.get(bikeID).notifyRideStepDone(rideID, bikePos, batteryLevel, userCredits);
        } catch (RemoteException e) {   // Unreachable GUIs are removed.
            System.out.println("Unreachable user GUI.");
            this.ridesListeners.remove(bikeID);
        }
    }
}