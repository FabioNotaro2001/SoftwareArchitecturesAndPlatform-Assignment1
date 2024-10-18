package sap.ass01.businessLogic; 

import java.util.List; 

/**
 * Interface representing the server functionality for managing users and e-bikes.
 */
public interface Server {
    /**
     * Retrieves a list of all users.
     * @return a list of UserInfo objects representing all users.
     */
    public List<UserInfo> getUsers(); 

    /**
     * Retrieves user information by user ID.
     * @param userID the unique identifier of the user. Must not be null or empty.
     * @return UserInfo object representing the user.
     * @throws IllegalArgumentException if the userID is invalid.
     */
    public UserInfo getUserByID(String userID) throws IllegalArgumentException; 

    /**
     * Retrieves a list of all e-bikes.
     * @return a list of EBikeInfo objects representing all e-bikes.
     */
    public List<EBikeInfo> getEBikes(); 

    /**
     * Retrieves e-bike information by bike ID.
     * @param bikeID the unique identifier of the e-bike. Must not be null or empty.
     * @return EBikeInfo object representing the e-bike.
     * @throws IllegalArgumentException if the bikeID is invalid.
     */
    public EBikeInfo getEBikeByID(String bikeID) throws IllegalArgumentException; 

    /**
     * Updates information for a given e-bike.
     * @param bikeInfo the new information for the e-bike. Must not be null.
     * @return the updated EBikeInfo object.
     * @throws IllegalArgumentException if the bikeInfo is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public EBikeInfo updateEBike(EBikeInfo bikeInfo) throws IllegalArgumentException, RepositoryException; 

    /**
     * Adds a new e-bike to the system at a specified position.
     * @param bikeID the unique identifier for the new e-bike. Must not be null or empty.
     * @param pos the position where the e-bike will be added. Must not be null.
     * @return the newly created EBikeInfo object.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public EBikeInfo addEBike(String bikeID, P2d pos) throws RepositoryException;

    /**
     * Removes an e-bike from the system by bike ID.
     * @param bikeID the unique identifier of the e-bike to be removed. Must not be null or empty.
     * @throws IllegalStateException if the e-bike cannot be removed.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public void removeEBike(String bikeID) throws IllegalStateException, RepositoryException; 

    /**
     * Retrieves a list of all rides.
     * @return a list of RideInfo objects representing all rides.
     */
    public List<RideInfo> getRides(); // Method to get all rides.

    /**
     * Retrieves the current ride information for a specific user.
     * @param userID the unique identifier of the user. Must not be null or empty.
     * @return RideInfo object representing the user's current ride.
     * @throws IllegalArgumentException if the userID is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public RideInfo getRideForUser(String userID) throws IllegalArgumentException, RepositoryException; 

    /**
     * Begins a ride for a specified user and e-bike.
     * @param userID the unique identifier of the user. Must not be null or empty.
     * @param bikeID the unique identifier of the e-bike. Must not be null or empty.
     * @return RideInfo object representing the started ride.
     * @throws IllegalArgumentException if either the userID or bikeID is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public RideInfo beginRide(String userID, String bikeID) throws IllegalArgumentException, RepositoryException; 

    /**
     * Ends a ride for a specified user and e-bike.
     * @param userID the unique identifier of the user. Must not be null or empty.
     * @param bikeID the unique identifier of the e-bike. Must not be null or empty.
     * @return RideInfo object representing the ended ride.
     * @throws IllegalArgumentException if either the userID or bikeID is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public RideInfo endRide(String userID, String bikeID) throws IllegalArgumentException, RepositoryException; 

    /**
     * Creates a new user with a specified ID and initial credits.
     * @param userID the unique identifier for the new user. Must not be null or empty.
     * @param credits the initial credits assigned to the new user.
     * @return UserInfo object representing the created user.
     * @throws IllegalArgumentException if the userID is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public UserInfo createUser(String userID, int credits) throws IllegalArgumentException, RepositoryException; 

    /**
     * Adds credits to an existing user.
     * @param userID the unique identifier of the user. Must not be null or empty.
     * @param credits the amount of credits to add to the user.
     * @throws IllegalArgumentException if the userID is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public void addCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException; 
    
    /**
     * Decreases credits for an existing user.
     * @param userID the unique identifier of the user. Must not be null or empty.
     * @param credits the amount of credits to decrease for the user.
     * @throws IllegalArgumentException if the userID is invalid.
     * @throws RepositoryException if there is an issue with data storage.
     */
    public void decreaseCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException; 
}
