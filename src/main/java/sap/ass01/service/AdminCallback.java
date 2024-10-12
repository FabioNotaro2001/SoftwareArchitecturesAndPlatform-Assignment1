package sap.ass01.service;

public interface AdminCallback extends UserCallback {
    void notifyUserCreated(String userID, int credits);
}