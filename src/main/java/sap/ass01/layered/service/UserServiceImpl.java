package sap.ass01.layered.service;

import java.rmi.RemoteException;
import java.util.List;

import sap.ass01.layered.businessLogic.EBikeInfo;
import sap.ass01.layered.businessLogic.P2d;
import sap.ass01.layered.businessLogic.RepositoryException;
import sap.ass01.layered.businessLogic.RideInfo;
import sap.ass01.layered.businessLogic.UserInfo;
import sap.ass01.layered.businessLogic.EBike.EBikeState;

/**
 * [SERVICE LAYER][UserService]<-[PRESENTATION LAYER].
 * Implementation of the UserService interface, providing user-related operations.
 */
public class UserServiceImpl implements UserService {
    // Reference to the UserAppService, which handles business logic related to users.
    private transient UserAppService userAppService;
    
    // Callback reference to communicate with the user GUI.
    private transient UserGUICallback userGUICallback;

    /**
     * Constructor to initialize UserServiceImpl with the UserAppService instance.
     * 
     * @param userAppService the application service that provides user operations
     */
    public UserServiceImpl(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Override
    public void registerGUI(UserGUICallback callback) {
        // Register the GUI callback for receiving notifications about user-related events.
        this.userGUICallback = callback;
    }

    @Override
    public List<String> getUsers() throws RemoteException {
        // Retrieve the list of users and return their IDs.
        return this.userAppService.getUsers().stream().map(u -> u.userID()).toList();
    }

    @Override
    public UserInfo logAsUser(String userId) throws RemoteException, IllegalArgumentException {
        // Attempt to log in the user with the provided ID.
        return this.userAppService.registerUser(userId, this);
    }

    @Override
    public UserInfo createUser(String userId, int credits) throws RemoteException, IllegalArgumentException, RepositoryException {
        // Create a new user with the specified ID and initial credits.
        return this.userAppService.createUser(userId, credits);
    }

    @Override
    public List<EBikeInfo> getAvailableBikes() throws RemoteException {
        // Retrieve all available e-bikes from the user application service.
        return this.userAppService.getEBikes().stream().filter(b -> b.state() == EBikeState.AVAILABLE).toList();
    }

    @Override
    public RideInfo beginRide(String userId, String bikeId) throws RemoteException, IllegalArgumentException, IllegalStateException, RepositoryException {
        // Attempt to start a ride for the user with the specified bike.
        return this.userAppService.beginRide(userId, bikeId);
    }

    @Override
    public boolean endRide(String userId, String bikeId) throws RemoteException, RepositoryException {
        try {
            // Attempt to end the ride for the specified user and bike.
            this.userAppService.endRide(userId, bikeId);
        } catch (IllegalArgumentException e) {
            // If an IllegalArgumentException is thrown, the ride could not be ended successfully.
            return false;
        }
        // Return true if the ride ended successfully.
        return true;
    }

    @Override
    public boolean rechargeCredit(String userId, int credits) throws RemoteException, RepositoryException {
        try {
            // Attempt to add credits to the specified user's account.
            this.userAppService.addCredits(userId, credits);
        } catch (IllegalArgumentException e) {
            // If an IllegalArgumentException is thrown, the recharge was unsuccessful.
            return false;
        }
        // Return true if the credits were recharged successfully.
        return true;
    }

    @Override
    public void notifyBikeStateChanged(String bikeID, EBikeState newState, double x, double y, int batteryLevel) throws RemoteException {
        // Notify the GUI about a change in the state of a bike.
        this.userGUICallback.notifyBikeStateChanged(bikeID, newState, x, y, batteryLevel);
    }

    @Override
    public void notifyUserCreditRecharged(String userID, int credits) throws RemoteException {
        // Notify the GUI that the user's credits have been recharged.
        this.userGUICallback.notifyUserCreditRecharged(userID, credits);
    }

    @Override
    public void notifyRideStepDone(String rideId, P2d bikePos, int batteryLevel, int userCredits) throws RemoteException {
        // Notify the GUI that a step in the ride has been completed.
        this.userGUICallback.notifyRideStepDone(rideId, bikePos.x(), bikePos.y(), batteryLevel, userCredits);
    }
}
