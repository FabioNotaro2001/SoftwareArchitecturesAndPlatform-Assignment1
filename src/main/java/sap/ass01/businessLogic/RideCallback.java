package sap.ass01.businessLogic;

import java.rmi.Remote;

public interface RideCallback extends Remote {
    void notifyClient(String userID, String bikeID, P2d bikePos, int batteryLevel, int userCredits);
}
