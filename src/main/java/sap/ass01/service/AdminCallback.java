package sap.ass01.service;

import java.rmi.RemoteException;

/**
 * Interface for notifying about admin events.
 */
public interface AdminCallback extends UserCallback {
    /**
     * Sends a notification for an event regarding the creation of a new user
     * @param userID the user's id
     * @param credits the user's credit
     */
    void notifyUserCreated(String userID, int credits) throws RemoteException;
}