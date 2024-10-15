package sap.ass01.presentation;

import sap.ass01.businessLogic.EBike.EBikeState;

public interface UserGUICallback {
    void notifyBikeStateChanged(String bikeID, EBikeState state, double x, double y, int batteryLevel);

    void notifyUserCreditRecharged(String userID, int credits);

    void notifyRideStepDone(String rideId, double x, double y, int batteryLevel, int userCredits, boolean rideEnded);
}
