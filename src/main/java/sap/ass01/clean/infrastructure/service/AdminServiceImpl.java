package sap.ass01.clean.infrastructure.service;

import java.rmi.RemoteException;
import java.util.List;
import sap.ass01.clean.domain.EBikeInfo;
import sap.ass01.clean.domain.P2d;
import sap.ass01.clean.domain.RepositoryException;
import sap.ass01.clean.domain.RideInfo;
import sap.ass01.clean.domain.UserInfo;
import sap.ass01.clean.domain.EBike.EBikeState;

public class AdminServiceImpl implements AdminService{
    private transient AdminAppService adminAppService;
    private transient AdminGUICallback adminGUICallback;

    public AdminServiceImpl(AdminAppService adminAppService) throws RemoteException{
        this.adminAppService = adminAppService;
    }

    public void init() throws RemoteException {
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
    public void notifyRideUpdate(RideInfo rideInfo) {
        this.adminGUICallback.notifyRideUpdate(rideInfo);
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
