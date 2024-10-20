package sap.ass01.clean.domain;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * [OUTBOUND PORT].
 * Interface for notifying about user events related to ebikes.
 * This interface is designed to be used in a remote method invocation (RMI) context.
 * Implementing classes must provide functionality for handling notifications of 
 * bike state changes, user credit updates, and ride steps.
 */
public interface UserCallback extends Remote {
    /**
     * Sends a notification for an event regarding an update to the state of an ebike.
     * This includes events such as bike creation, state changes, and battery level changes.
     * @param bikeID The ebike's unique identifier.
     * @param newState The ebike's (eventual) new state.
     * @param x The ebike's x position.
     * @param y The ebike's y position.
     * @param batteryLevel The ebike's (eventual) new battery level.
     * @throws RemoteException If a remote communication error occurs.
     */
    void notifyBikeStateChanged(String bikeID, EBike.EBikeState newState, double x, double y, int batteryLevel) throws RemoteException; 

    /**
     * Sends a notification for an event regarding a user's credit update.
     * This is used to inform the user about any changes to their credit balance.
     * @param userID The user's unique identifier.
     * @param credits The updated credit value.
     * @throws RemoteException If a remote communication error occurs.
     */
    void notifyUserCreditRecharged(String userID, int credits) throws RemoteException;

    /**
     * Sends a notification for an event regarding a ride step.
     * This is used to update the user on the progress of their ride.
     * @param rideId The ride's unique identifier.
     * @param bikePos The ebike's current position represented as a 2D point.
     * @param batteryLevel The ebike's current battery level.
     * @param userCredits The user's current credit balance.
     * @throws RemoteException If a remote communication error occurs.
     */
    void notifyRideStepDone(String rideId, P2d bikePos, int batteryLevel, int userCredits) throws RemoteException;
}
