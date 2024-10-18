package sap.ass01.businessLogic;

import java.io.Serializable;

public record RideInfo(String rideId, String userID, String bikeID, boolean ongoing) implements Serializable {
    public RideInfo setOngoing(boolean isOngoing) {
        return new RideInfo(rideId, userID, bikeID, isOngoing);
    }
}
