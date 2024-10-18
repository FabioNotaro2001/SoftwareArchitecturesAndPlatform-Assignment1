package sap.ass01.businessLogic;

import java.io.Serializable;

public record RideInfo(String rideId, String userID, String bikeID, boolean ongoing) implements Serializable {

}
