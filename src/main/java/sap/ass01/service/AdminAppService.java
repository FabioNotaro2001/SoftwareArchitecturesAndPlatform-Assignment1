package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.List;

import sap.ass01.businessLogic.P2d;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;

public interface AdminAppService extends AppService {
    void registerAdmin(AdminCallback callback) throws RemoteException;

    void addEBike(String bikeID, P2d pos) throws RemoteException, RepositoryException;

    void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException;

    List<RideInfo> getRides() throws RemoteException;
}
