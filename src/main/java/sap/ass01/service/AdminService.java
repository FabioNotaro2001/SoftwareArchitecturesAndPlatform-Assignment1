package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.List;

import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.presentation.AdminGUICallback;

public interface AdminService extends AdminCallback {
    void registerGUI(AdminGUICallback callback);

    boolean addEBike(String bikeID, double x, double y) throws RemoteException, RepositoryException;

    boolean removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException;

    List<RideInfo> getRides() throws RemoteException;

    List<EBikeInfo> getEBikes() throws RemoteException;

    List<UserInfo> getUsers() throws RemoteException;
}
