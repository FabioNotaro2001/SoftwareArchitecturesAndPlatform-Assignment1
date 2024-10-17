package sap.ass01.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sap.ass01.businessLogic.EBike;
import sap.ass01.businessLogic.P2d;

/**
 * Interface for notifying about user events.  
 */
public interface UserCallback extends Remote {
    /**
     * Sends a notification for an event regarding an update to the state of an ebike (bike created, state change, battery level charge).
     * @param bikeID the ebike's id
     * @param newState the ebike's (eventual) new state 
     * @param x the ebike's x position
     * @param y the ebike's y position
     * @param batteryLevel the ebike's (eventual) new battery level
     * @throws RemoteException
     */
    void notifyBikeStateChanged(String bikeID, EBike.EBikeState newState, double x, double y, int batteryLevel) throws RemoteException; // TODO x, y => pos?

    /**
     * Sends a notification for an event regarding a user's credit update.
     * @param userID the user's id
     * @param credits the updated credit value
     * @throws RemoteException
     */
    void notifyUserCreditRecharged(String userID, int credits) throws RemoteException;

    /**
     * Sends a notification for an event regarding a ride step.
     * @param rideId the ride's id
     * @param bikePos the ebike's position
     * @param batteryLevel the ebike's battery level
     * @param userCredits the user's current credit
     * @throws RemoteException
     */
    void notifyRideStepDone(String rideId, P2d bikePos, int batteryLevel, int userCredits) throws RemoteException;
}
