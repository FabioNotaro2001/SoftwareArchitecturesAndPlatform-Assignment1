package sap.ass01.clean.service;

import sap.ass01.clean.businessLogic.EBike.EBikeState;

/**
 * Interface for callback methods that notify the GUI about user-related events.
 */
public interface UserGUICallback {
    /**
     * Notifies the GUI when the state of a bike has changed.
     * 
     * @param bikeID       The ID of the bike whose state has changed.
     * @param state        The new state of the bike.
     * @param x            The new x-coordinate of the bike's position.
     * @param y            The new y-coordinate of the bike's position.
     * @param batteryLevel The current battery level of the bike.
     */
    void notifyBikeStateChanged(String bikeID, EBikeState state, double x, double y, int batteryLevel);

    /**
     * Notifies the GUI that a user's credits have been recharged.
     * 
     * @param userID The ID of the user whose credits have been recharged.
     * @param credits The amount of credits added to the user's account.
     */
    void notifyUserCreditRecharged(String userID, int credits);

    /**
     * Notifies the GUI that a step in a ride has been completed.
     * 
     * @param rideId       The ID of the ride that has progressed.
     * @param x            The current x-coordinate of the bike's position.
     * @param y            The current y-coordinate of the bike's position.
     * @param batteryLevel The current battery level of the bike.
     * @param userCredits  The remaining credits of the user after the ride step.
     */
    void notifyRideStepDone(String rideId, double x, double y, int batteryLevel, int userCredits);
}
