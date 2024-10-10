package sap.ass01.businessLogic;

import java.rmi.RemoteException;

public interface AdminServer extends Server {
    void registerForRidesUpdates(RideCallback callback) throws RemoteException;

    void addEBike(String bikeID) throws RemoteException, RepositoryException;

    void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException;
}
