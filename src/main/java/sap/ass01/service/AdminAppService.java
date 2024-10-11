package sap.ass01.service;

import java.rmi.RemoteException;
import sap.ass01.businessLogic.RepositoryException;

public interface AdminAppService extends AppService {
    void registerAdmin(AdminCallback callback) throws RemoteException;

    void addEBike(String bikeID) throws RemoteException, RepositoryException;

    void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException;
}
