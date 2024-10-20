package sap.ass01.clean.domain;

import java.io.Serializable;

/**
 * Represents information about a ride in the system.
 * This record holds details such as the ride ID, user ID, bike ID, and the ongoing status of the ride.
 */
public record RideInfo(String rideId, String userID, String bikeID, boolean ongoing) implements Serializable {
    /**
     * Updates the ongoing status of the ride and returns a new RideInfo object.
     * @param isOngoing The new ongoing status of the ride.
     * @return A new RideInfo object with the updated ongoing status.
     */
    public RideInfo setOngoing(boolean isOngoing) {
        return new RideInfo(rideId, userID, bikeID, isOngoing); 
    }
}
