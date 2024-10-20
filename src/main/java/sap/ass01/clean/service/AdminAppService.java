package sap.ass01.clean.service;

import java.rmi.RemoteException;
import java.util.List;
import sap.ass01.clean.businessLogic.AdminCallback;
import sap.ass01.clean.businessLogic.P2d;
import sap.ass01.clean.businessLogic.RepositoryException;
import sap.ass01.clean.businessLogic.RideInfo;

/**
 * Application service interface for admin clients.
 */
public interface AdminAppService extends AppService {
    /**
     * Registers an admin service for events.
     * @param callback callback to the admin service
     * @throws RemoteException
     */
    void registerAdmin(AdminCallback callback) throws RemoteException;

    /**
     * Adds a new ebike.
     * @param bikeID the ebike's id
     * @param pos the ebike's initial position
     * @throws RemoteException
     * @throws RepositoryException
     */
    void addEBike(String bikeID, P2d pos) throws RemoteException, RepositoryException;

    /**
     * Deletes an existing ebike.
     * @param bikeID the ebike's id
     * @throws RemoteException
     * @throws IllegalStateException if the ebike is currently in use
     * @throws RepositoryException
     */
    void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException;

    /**
     * Returns all active rides.
     * @return a list of ride information
     * @throws RemoteException
     */
    List<RideInfo> getRides() throws RemoteException;
}
