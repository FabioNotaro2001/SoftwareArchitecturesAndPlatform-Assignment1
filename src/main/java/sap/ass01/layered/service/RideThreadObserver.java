package sap.ass01.layered.service;

import sap.ass01.layered.businessLogic.EBike;
import sap.ass01.layered.businessLogic.P2d;

/**
 * Observer interface for monitoring ride events.
 * Implementations of this interface can receive notifications
 * about ride progress and completion.
 */
public interface RideThreadObserver {
    /**
     * Called when a ride has ended.
     * 
     * @param rideId      The unique identifier for the completed ride.
     * @param userID      The unique identifier for the user who completed the ride.
     * @param bikeID      The unique identifier for the bike used in the ride.
     * @param newState    The new state of the bike after the ride has ended.
     * @param x           The final x-coordinate of the bike's position.
     * @param y           The final y-coordinate of the bike's position.
     * @param batteryLevel The remaining battery level of the bike after the ride.
     */
    void rideEnded(String rideId, String userID, String bikeID, EBike.EBikeState newState, double x, double y, int batteryLevel);

    /**
     * Called when a ride step has been completed.
     * 
     * @param bikeID      The unique identifier for the bike being ridden.
     * @param rideID      The unique identifier for the current ride.
     * @param bikePos     The current position of the bike as a P2d object.
     * @param batteryLevel The remaining battery level of the bike at this step.
     * @param userCredits  The user's current credit balance after the ride step.
     */
    void rideStepDone(String bikeID, String rideID, P2d bikePos, int batteryLevel, int userCredits);
}
