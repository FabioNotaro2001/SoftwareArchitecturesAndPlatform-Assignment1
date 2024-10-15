package sap.ass01.presentation;

public interface AdminGUICallback extends UserGUICallback{ // TODO: serve distinguere tra callback GUI e service, quando hanno praticamente gli stessi metodi?
    void notifyUserCreated(String userID, int credits);
}
