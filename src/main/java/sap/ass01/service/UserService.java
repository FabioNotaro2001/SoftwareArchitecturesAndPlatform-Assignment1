package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.List;

import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.presentation.UserGUICallback;

/**
 * Interface for the user GUI service.
 */
public interface UserService extends UserCallback {
    /**
     * Registers the GUI so that application events can be forwarded.
     * @param callback callback to the user GUI
     */
    void registerGUI(UserGUICallback callback);

    /**
     * Returns all available users.
     * @return a list of users information
     * @throws RemoteException
     */
    List<String> getUsers() throws RemoteException;

    /**
     * Attempts to log in as an existing user. 
     * @param userId the user's id
     * @return the user's information if successful, {@code null} otherwise
     * @throws RemoteException
     */
    UserInfo logAsUser(String userId) throws RemoteException;

    /**
     * Attempts to create and log in as a new user.
     * @param userId the user's id
     * @param credits the user's initial credits
     * @return the new user's information if successful, {@code null} otherwise
     * @throws RemoteException
     * @throws RepositoryException
     */
    UserInfo createUser(String userId, int credits) throws RemoteException, RepositoryException;

    /**
     * Returns all available ebikes.
     * @return a list of ebike information
     * @throws RemoteException
     */
    List<EBikeInfo> getAvailableBikes() throws RemoteException;

    /**
     * Attempts to start a ride.
     * @param userId the user's id
     * @param bikeId the bike's id
     * @return the new ride's information if successful, {@code null} otherwise
     * @throws RemoteException
     */
    RideInfo beginRide(String userId, String bikeId) throws RemoteException;
    
    /**
     * Attempts to end a ride.
     * @param userId the user's id
     * @param bikeId the bike's id
     * @return {@code true} if successful, {@code false} otherwise
     * @throws RemoteException
     */
    boolean endRide(String userId, String bikeId) throws RemoteException;

    /**
     * Attempts to recharge a user's credit.
     * @param userId the user's id
     * @param credits the amount of credit
     * @return {@code true} if successful, {@code false} otherwise
     * @throws RemoteException
     * @throws RepositoryException
     */
    boolean rechargeCredit(String userId, int credits) throws RemoteException, RepositoryException;
}
