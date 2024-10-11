package sap.ass01.service;

import java.rmi.RemoteException;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.UserInfo;

public interface UserAppService extends AppService {
    UserInfo createUser(String userID, int credits) throws RemoteException, IllegalArgumentException, RepositoryException;

    RideInfo beginRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException;

    void endRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException;

    void addCredits(String userID, int credits) throws RemoteException, RepositoryException;

    void registerUser(String userID, UserCallback callback);
}