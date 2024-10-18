package sap.ass01.service;

import java.rmi.RemoteException;
import java.util.List;
import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.UserCallback;
import sap.ass01.businessLogic.UserInfo;

/**
 * [SERVICE LAYER][UserService]<-[PRESENTATION LAYER].
 * Interface for the user GUI service, providing methods for user operations.
 */
public interface UserService extends UserCallback {
    /**
     * Registers the GUI callback so that application events can be forwarded to it.
     * 
     * @param callback callback to the user GUI
     * @throws RemoteException if a remote communication error occurs
     */
    void registerGUI(UserGUICallback callback) throws RemoteException;

    /**
     * Retrieves all available users in the system.
     * 
     * @return a list of user information (user IDs)
     * @throws RemoteException if a remote communication error occurs
     */
    List<String> getUsers() throws RemoteException;

    /**
     * Attempts to log in as an existing user using their user ID.
     * 
     * @param userId the user's id
     * @return the user's information if successful, {@code null} otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    UserInfo logAsUser(String userId) throws RemoteException;

    /**
     * Attempts to create a new user and log in at the same time.
     * 
     * @param userId the user's id
     * @param credits the user's initial credits
     * @return the new user's information if successful, {@code null} otherwise
     * @throws RemoteException if a remote communication error occurs
     * @throws RepositoryException if there is an issue with the repository
     */
    UserInfo createUser(String userId, int credits) throws RemoteException, RepositoryException;

    /**
     * Retrieves all available e-bikes in the system.
     * 
     * @return a list of e-bike information
     * @throws RemoteException if a remote communication error occurs
     */
    List<EBikeInfo> getAvailableBikes() throws RemoteException;

    /**
     * Attempts to start a ride with the specified bike.
     * 
     * @param userId the user's id
     * @param bikeId the bike's id
     * @return the new ride's information if successful, {@code null} otherwise
     * @throws RemoteException if a remote communication error occurs
     * @throws IllegalArgumentException if the user ID or bike ID is invalid
     * @throws IllegalStateException if the ride cannot be started (e.g., bike is unavailable)
     * @throws RepositoryException if there is an issue with the repository
     */
    RideInfo beginRide(String userId, String bikeId) throws RemoteException, IllegalArgumentException, IllegalStateException, RepositoryException;

    /**
     * Attempts to end a ride with the specified bike.
     * 
     * @param userId the user's id
     * @param bikeId the bike's id
     * @return {@code true} if the ride ended successfully, {@code false} otherwise
     * @throws RemoteException if a remote communication error occurs
     * @throws RepositoryException if there is an issue with the repository
     */
    boolean endRide(String userId, String bikeId) throws RemoteException, RepositoryException;

    /**
     * Attempts to recharge a user's credits.
     * 
     * @param userId the user's id
     * @param credits the amount of credit to be added
     * @return {@code true} if successful, {@code false} otherwise
     * @throws RemoteException if a remote communication error occurs
     * @throws RepositoryException if there is an issue with the repository
     */
    boolean rechargeCredit(String userId, int credits) throws RemoteException, RepositoryException;
}