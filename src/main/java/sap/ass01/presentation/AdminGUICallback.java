package sap.ass01.presentation;

public interface AdminGUICallback extends UserGUICallback{
    void notifyUserCreated(String userID, int credits);
}
