package sap.ass01.clean.businessLogic; 

import java.rmi.RemoteException; 

/**
 * Interface for notifying about admin events.
 */
public interface AdminCallback extends UserCallback {
    /**
     * Sends a notification for an event regarding the creation of a new user.
     * @param userID the user's id. Represents the unique identifier of the user.
     * @param credits the user's credit. Represents the initial credits assigned to the new user.
     * @throws RemoteException if there is a problem with remote communication.
     */
    void notifyUserCreated(String userID, int credits) throws RemoteException; 

    /**
     * Sends a notification for an event regarding the start or end of a ride.
     * @param rideInfo the ride's information. Contains details about the ride, including its status and participants.
     * @throws RemoteException if there is a problem with remote communication.
     */
    void notifyRideUpdate(RideInfo rideInfo) throws RemoteException; 
}
