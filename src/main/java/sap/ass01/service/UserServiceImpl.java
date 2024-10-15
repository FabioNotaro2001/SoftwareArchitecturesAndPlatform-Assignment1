package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.List;

import sap.ass01.businessLogic.EBike.EBikeState;
import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.P2d;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.presentation.AdminGUICallback;
import sap.ass01.presentation.UserGUICallback;

public class UserServiceImpl implements UserService {
    private UserAppService userAppService;
    private UserGUICallback userGUICallback;

    public UserServiceImpl(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Override
    public void registerGUI(UserGUICallback callback) {
        this.userGUICallback = callback;
    }

    @Override
    public List<String> getUsers() throws RemoteException {
        return this.userAppService.getUsers().stream().map(u -> u.userID()).toList();
    }

    @Override
    public UserInfo logAsUser(String userId) throws RemoteException, IllegalArgumentException {
        return this.userAppService.registerUser(userId, this);
    }

    @Override
    public UserInfo createUser(String userId, int credits) throws RemoteException, IllegalArgumentException, RepositoryException {
        return this.userAppService.createUser(userId, credits);
    }

    @Override
    public List<EBikeInfo> getBikes() throws RemoteException {
        return this.userAppService.getEBikes();
    }

    @Override
    public RideInfo beginRide(String userId, String bikeId) throws RemoteException, IllegalArgumentException {
        return this.userAppService.beginRide(userId, bikeId);
    }

    @Override
    public boolean endRide(String userId, String bikeId) throws RemoteException {
        try {
            this.userAppService.endRide(userId, bikeId);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean rechargeCredit(String userId, int credits) throws RemoteException, RepositoryException {
        try {
            this.userAppService.addCredits(userId, credits);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public void notifyBikeStateChanged(String bikeID, EBikeState newState, double x, double y, int batteryLevel) throws RemoteException {
        this.userGUICallback.notifyBikeStateChanged(bikeID, newState, x, y, batteryLevel);
    }

    @Override
    public void notifyUserCreditRecharged(String userID, int credits) throws RemoteException {
        this.userGUICallback.notifyUserCreditRecharged(userID, credits);
    }

    @Override
    public void notifyRideStepDone(String rideId, P2d bikePos, int batteryLevel, int userCredits, boolean rideEnded) throws RemoteException {
        this.userGUICallback.notifyRideStepDone(rideId, bikePos.x(), bikePos.y(), batteryLevel, userCredits, rideEnded);
    }
}
