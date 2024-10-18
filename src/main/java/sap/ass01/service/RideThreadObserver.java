package sap.ass01.service;

import sap.ass01.businessLogic.EBike;
import sap.ass01.businessLogic.P2d;

public interface RideThreadObserver {
    void rideEnded(String rideId, String userID, String bikeID, EBike.EBikeState newState, double x, double y, int batteryLevel);

    void rideStepDone(String bikeID, String rideID, P2d bikePos, int batteryLevel, int userCredits);
}
