package sap.ass01.clean.service;

import sap.ass01.clean.businessLogic.RideInfo;

/**
 * An interface for admin GUI callbacks that extends user GUI callbacks.
 * 
 * This interface provides additional callback methods specific to admin functionalities, 
 * allowing the admin GUI to respond to events related to user and ride management.
 */
public interface AdminGUICallback extends UserGUICallback {

    /**
     * Notifies the admin GUI that a new user has been created.
     * 
     * @param userID the ID of the newly created user
     * @param credits the initial amount of credits assigned to the user
     */
    void notifyUserCreated(String userID, int credits);

    /**
     * Notifies the admin GUI about updates to a ride.
     * 
     * @param rideInfo the updated information of the ride
     */
    void notifyRideUpdate(RideInfo rideInfo);
}
