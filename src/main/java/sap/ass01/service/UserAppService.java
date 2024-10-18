package sap.ass01.service;

import java.rmi.RemoteException;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.UserCallback;
import sap.ass01.businessLogic.UserInfo;

/**
 * Application service interface for user clients.
 */
public interface UserAppService extends AppService {
    /**
     * Creates a new user.
     * @param userID the user's id
     * @param credits the user's initial credits
     * @return the info about the new user
     * @throws RemoteException
     * @throws IllegalArgumentException if a user with the same id already exists
     * @throws RepositoryException
     */
    UserInfo createUser(String userID, int credits) throws RemoteException, IllegalArgumentException, RepositoryException;

    /**
     * Starts a new ride.
     * @param userID the user's id
     * @param bikeID the bike's id
     * @return the new ride's information
     * @throws RemoteException
     * @throws IllegalArgumentException if the user or the bike doesn't exist
     * @throws IllegalStateException if the bike is unavailable in any way
     */
    RideInfo beginRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException, IllegalStateException, RepositoryException;

    /**
     * Ends an active ride.
     * @param userID the user's id
     * @param bikeID the bike's id
     * @throws RemoteException
     * @throws IllegalArgumentException if the user is not in a ride with the given bike
     * @throws RepositoryException 
     */
    void endRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException, RepositoryException;

    /**
     * Adds credits to a user
     * @param userID the user's id
     * @param credits the amount of credits
     * @throws RemoteException
     * @throws IllegalArgumentException if the credits number is invalid
     * @throws RepositoryException
     */
    void addCredits(String userID, int credits) throws RemoteException, IllegalArgumentException, RepositoryException;

    /**
     * Registers a user service for events concerning a particular user.
     * @param userID the user's id
     * @param callback callback to the user service
     * @return the info regarding the user
     * @throws RemoteException
     * @throws IllegalArgumentException if the user doesn't exist
     */
    UserInfo registerUser(String userID, UserCallback callback) throws RemoteException, IllegalArgumentException;
}