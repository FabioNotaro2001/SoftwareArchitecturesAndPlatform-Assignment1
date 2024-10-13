package sap.ass01.service;

import java.rmi.Remote;
import sap.ass01.businessLogic.EBike;
import sap.ass01.businessLogic.P2d;

public interface UserCallback extends Remote {
    void notifyBikeStateChanged(String bikeID, EBike.EBikeState newState, double x, double y, int batteryLevel);

    void notifyUserCreditRecharged(String userID, int credits);

    void notifyRideStepDone(String userID, String bikeID, P2d bikePos, int batteryLevel, int userCredits, boolean rideEnded);
}
