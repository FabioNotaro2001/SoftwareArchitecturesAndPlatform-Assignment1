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

public class AdminServiceImpl implements AdminService{
    private AdminAppService adminAppService;
    private AdminGUICallback adminGUICallback;

    public AdminServiceImpl(AdminAppService adminAppService) throws RemoteException{
        this.adminAppService = adminAppService;
        this.adminAppService.registerAdmin(this);
    }

    @Override
    public void notifyUserCreated(String userID, int credits) {
        this.adminGUICallback.notifyUserCreated(userID, credits);
    }

    @Override
    public void notifyBikeStateChanged(String bikeID, EBikeState newState, double x, double y, int batteryLevel) {
        this.adminGUICallback.notifyBikeStateChanged(bikeID, newState, x, y, batteryLevel);
    }

    @Override
    public void notifyUserCreditRecharged(String userID, int credits) {
        this.adminGUICallback.notifyUserCreditRecharged(userID, credits);
    }

    @Override
    public void notifyRideStepDone(String rideId, P2d bikePos, int batteryLevel, int userCredits) {
        this.adminGUICallback.notifyRideStepDone(rideId, bikePos.x(), bikePos.y(), batteryLevel, userCredits);
    }

    @Override
    public void registerGUI(AdminGUICallback callback) {
        this.adminGUICallback = callback;
    }

    @Override
    public boolean addEBike(String bikeID, double x, double y) throws RemoteException, RepositoryException {
        try {
            this.adminAppService.addEBike(bikeID, new P2d(x, y));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException {
        try {
            this.adminAppService.removeEBike(bikeID);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<RideInfo> getRides() throws RemoteException {
        return this.adminAppService.getRides();
    }

    @Override
    public List<EBikeInfo> getEBikes() throws RemoteException {
       return this.adminAppService.getEBikes();
    }

    @Override
    public List<UserInfo> getUsers() throws RemoteException {
        return this.adminAppService.getUsers();
    }
}
