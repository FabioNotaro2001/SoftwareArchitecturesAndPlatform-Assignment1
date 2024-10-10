package sap.ass01.businessLogic;

import java.rmi.RemoteException;

public interface UserServer extends Server {
    User createUser(String userID, int credits) throws RemoteException, IllegalArgumentException, RepositoryException;

    Ride beginRide(String userID, String bikeID, RideCallback callback) throws RemoteException, IllegalArgumentException;

    void endRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException;

    void addCredits(String userID, int credits) throws RemoteException, RepositoryException;
}