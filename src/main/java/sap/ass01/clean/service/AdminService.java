package sap.ass01.clean.service;

import java.rmi.RemoteException;
import java.util.List;
import sap.ass01.clean.businessLogic.AdminCallback;
import sap.ass01.clean.businessLogic.EBikeInfo;
import sap.ass01.clean.businessLogic.RepositoryException;
import sap.ass01.clean.businessLogic.RideInfo;
import sap.ass01.clean.businessLogic.UserInfo;

/**
 * Interface for the admin GUI service.
 */
public interface AdminService extends AdminCallback {
    /**
     * Registers the GUI so that application events can be forwarded. 
     * @param callback callback to the admin GUI
     */
    void registerGUI(AdminGUICallback callback) throws RemoteException;

    /**
     * Attempts to add a new ebike.
     * @param bikeID the ebike's id
     * @param x the ebike's x position
     * @param y the ebike's y position
     * @return {@code true} if the addition is successful, {@code false} otherwise
     * @throws RemoteException
     * @throws RepositoryException
     */
    boolean addEBike(String bikeID, double x, double y) throws RemoteException, RepositoryException; 

    /**
     * Attempts to remove an ebike.
     * @param bikeID the ebike's id
     * @return {@code true} if the removal is successful, {@code false} otherwise
     * @throws RemoteException
     * @throws RepositoryException
     */
    boolean removeEBike(String bikeID) throws RemoteException, RepositoryException;

    /**
     * Returns all available rides
     * @return a list of ride information
     * @throws RemoteException
     */
    List<RideInfo> getRides() throws RemoteException;

    /**
     * Returns all available ebikes.
     * @return a list of ebike information
     * @throws RemoteException
     */
    List<EBikeInfo> getEBikes() throws RemoteException;

    /**
     * Returns all available users.
     * @return a list of user information
     * @throws RemoteException
     */
    List<UserInfo> getUsers() throws RemoteException;
}
