package sap.ass01.service;

import java.rmi.RemoteException;

import sap.ass01.businessLogic.RideInfo;

/**
 * Interface for notifying about admin events.
 */
public interface AdminCallback extends UserCallback {
    /**
     * Sends a notification for an event regarding the creation of a new user.
     * @param userID the user's id
     * @param credits the user's credit
     */
    void notifyUserCreated(String userID, int credits) throws RemoteException;

    /**
     * Sends a notification for an event regarding the start/end of a ride.
     * @param rideInfo the ride's information
     */
    void notifyRideUpdate(RideInfo rideInfo) throws RemoteException;
}