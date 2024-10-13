package sap.ass01.presentation;

// TODO: pensare se bikeId e userId sono superflui.
public interface UserGUICallback {
    void notifyBikeStateChanged(String bikeID, String state, double x, double y, int batteryLevel);

    void notifyUserCreditRecharged(String userID, int credits);

    void notifyRideStepDone(String userID, String bikeID, double x, double y, int batteryLevel, int userCredits, boolean rideEnded);
}
