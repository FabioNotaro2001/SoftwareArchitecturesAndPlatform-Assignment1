package sap.ass01.service;

import sap.ass01.businessLogic.RideInfo;

public interface AdminGUICallback extends UserGUICallback{
    void notifyUserCreated(String userID, int credits);
    void notifyRideUpdate(RideInfo rideInfo);
}
