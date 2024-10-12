package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sap.ass01.businessLogic.*;
import sap.ass01.businessLogic.EBike.EBikeState;

public class AppServiceImpl implements AdminAppService, UserAppService {
    private static final String USER_SERVER_NAME = "userServer";
    private static final String ADMIN_SERVER_NAME = "adminServer";

    private transient List<AdminCallback> adminListeners;
    private transient Map<String, UserCallback> userListeners;  // userID -> callback.
    private transient Map<String, UserCallback> ridesListeners; // bikeID -> callback.

    private Server serverBL;

    public AppServiceImpl(Server serverBL){
        this.serverBL = serverBL;
        this.adminListeners = new ArrayList<>();
        this.userListeners = new HashMap<>();
        this.ridesListeners = new HashMap<>();
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
    public RideInfo beginRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException {
        RideInfo rideBegun = this.serverBL.beginRide(userID, bikeID);
        EBikeInfo bikeUsed = this.serverBL.getEBikeByID(bikeID);

        this.adminListeners.forEach(a -> a.notifyBikeStateChanged(bikeID, bikeUsed.state(), bikeUsed.batteryLevel()));
        this.userListeners.values().forEach(u -> u.notifyBikeStateChanged(bikeID,  bikeUsed.state(), bikeUsed.batteryLevel()));

        this.ridesListeners.put(bikeID, this.userListeners.get(userID));
        return rideBegun;
    }

    @Override
    public void endRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException {
        this.serverBL.endRide(userID, bikeID);
        EBikeInfo bikeUsed = this.serverBL.getEBikeByID(bikeID);

        this.adminListeners.forEach(a -> a.notifyBikeStateChanged(bikeID, bikeUsed.state(), bikeUsed.batteryLevel()));
        this.userListeners.values().forEach(u -> u.notifyBikeStateChanged(bikeID,  bikeUsed.state(), bikeUsed.batteryLevel()));

        this.ridesListeners.remove(bikeID);
    }

    @Override
    public void addCredits(String userID, int credits) throws RemoteException, RepositoryException {
        this.serverBL.addCredits(userID, credits);
        int creditUpdated = this.serverBL.getUserByID(userID).credits();

        this.adminListeners.forEach(a -> a.notifyUserCreditRecharged(userID, creditUpdated));
        this.userListeners.get(userID).notifyUserCreditRecharged(userID, creditUpdated);
    }

    @Override
    public void registerUser(String userID, UserCallback callback) {
        this.userListeners.put(userID, callback);
    }

    @Override
    public void registerAdmin(AdminCallback callback) throws RemoteException {
        this.adminListeners.add(callback);
    }

    @Override
    public void addEBike(String bikeID) throws RemoteException, RepositoryException {
        EBikeInfo bikeCreated = this.serverBL.addEBike(bikeID);
        this.adminListeners.forEach(a -> a.notifyBikeStateChanged(bikeID, bikeCreated.state(), bikeCreated.batteryLevel()));
        this.userListeners.values().forEach(u -> u.notifyBikeStateChanged(bikeID, bikeCreated.state(), bikeCreated.batteryLevel()));
    }

    @Override
    public void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException {
        this.serverBL.removeEBike(bikeID);
        this.adminListeners.forEach(a -> a.notifyBikeStateChanged(bikeID, EBikeState.DISMISSED, 0));
        this.userListeners.values().forEach(u -> u.notifyBikeStateChanged(bikeID, EBikeState.DISMISSED, 0));
    }

    @Override
    public List<RideInfo> getRides() throws RemoteException {
        return this.serverBL.getRides();
    }
}