package sap.ass01.layered.businessLogic; 

import java.util.ArrayList; 
import java.util.List; 
import java.util.Optional;

import sap.ass01.layered.businessLogic.EBike.EBikeState; 

/**
 * Implementation of the Server interface for managing users, e-bikes, and rides.
 */
public class ServerImpl implements Server {
    private RepositoryInterface repository; // Repository for data storage and retrieval.
    private List<User> users; // List of users.
    private List<EBike> bikes; // List of e-bikes.
    private int rideId = 0; // Counter for unique ride IDs.
    private List<Ride> rides; // List of ongoing rides.

    /**
     * Constructor for ServerImpl, initializes users, bikes, and rides from the repository.
     * @param repository the repository to use for data storage.
     * @throws RepositoryException if there is an issue retrieving users or bikes.
     */
    public ServerImpl(RepositoryInterface repository) throws RepositoryException {
        this.repository = repository;
        this.users = this.repository.getUsers();
        this.bikes = this.repository.getEBikes();
        this.rides = new ArrayList<>(); 
    }

    // Convert users to UserInfo objects.
    @Override
    public List<UserInfo> getUsers() {
        return this.users.stream().map(User::getInfo).toList(); 
    }

    // Convert bikes to EBikeInfo objects.
    @Override
    public List<EBikeInfo> getEBikes() {
        return this.bikes.stream().map(EBike::getInfo).toList(); 
    }

    @Override
    public EBikeInfo addEBike(String bikeID, P2d pos) throws RepositoryException {
        EBike bike = new EBike(bikeID, EBikeState.AVAILABLE, pos, new V2d(1, 0), 0, 100); // Create new e-bike.
        this.bikes.add(bike); // Add e-bike to the local list.
        this.repository.saveEBike(bike); // Save e-bike to the repository.
        return bike.getInfo(); // Return e-bike info.
    }

    @Override
    public void removeEBike(String bikeID) throws IllegalStateException, RepositoryException {
        // Check if the bike is currently in use in any ride.
        if (this.rides.stream().anyMatch(r -> r.getEBike().getId().equals(bikeID))) {
            throw new IllegalStateException("Bike currently in use");
        }
        this.bikes.removeIf(b -> b.getId().equals(bikeID)); // Remove bike from local list.
    }

    @Override
    public RideInfo beginRide(String userID, String bikeID) throws RepositoryException, IllegalArgumentException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst(); // Find user.
        var bikeOpt = this.bikes.stream().filter(b -> b.getId().equals(bikeID)).findFirst(); // Find bike.
        
        // Validate user and bike.
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }
        if (bikeOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid bike id");
        }

        var user = userOpt.get(); // Get user.
        var bike = bikeOpt.get(); // Get bike.
        bike.updateState(EBikeState.IN_USE); // Update bike state to IN_USE.
        updateEBike(bike.getInfo()); // Persist bike state change.

        var ride = new Ride(String.valueOf(rideId++), user, bike); // Create a new ride.
        this.rides.add(ride); // Add ride to the ongoing rides list.

        return ride.getInfo(); // Return ride info.
    }

    @Override
    public RideInfo endRide(String userID, String bikeID) throws IllegalArgumentException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst(); // Find user.
        var bikeOpt = this.bikes.stream().filter(b -> b.getId().equals(bikeID)).findFirst(); // Find bike.

        // Validate user and bike.
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }
        if (bikeOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid bike id");
        }

        var user = userOpt.get(); // Get user.
        var bike = bikeOpt.get(); // Get bike.

        var rideOpt = this.rides.stream().filter(r -> r.getEBike().equals(bike)).findFirst(); // Find ongoing ride.
        if (rideOpt.isEmpty()) {
            throw new IllegalArgumentException("No ride found with the given bike");
        }
        
        var ride = rideOpt.get(); // Get ride.
        if (ride.getUser() != user) {
            throw new IllegalArgumentException("The given user is not riding the bike");
        }

        // Update bike state based on battery level.
        bike.updateState(bike.getBatteryLevel() > 0 ? EBikeState.AVAILABLE : EBikeState.MAINTENANCE);
        bike.updateSpeed(0); // Stop the bike's speed.
        updateEBike(bike.getInfo()); // Persist bike state change.

        this.rides.remove(ride); // Remove ride from ongoing rides list.
        return ride.getInfo().setOngoing(false); // Return ride info as no longer ongoing.
    }

    @Override
    public void addCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst(); // Find user.
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }

        var user = userOpt.get(); // Get user.
        user.rechargeCredit(credits); // Recharge user credits.
        this.repository.saveUser(user); // Persist user changes.
    }

    @Override
    public void decreaseCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst(); // Find user.
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }

        var user = userOpt.get(); // Get user.
        user.decreaseCredit(credits); // Decrease user credits.
        this.repository.saveUser(user); // Persist user changes.
    }

    @Override
    public UserInfo createUser(String userID, int credits) throws IllegalArgumentException, RepositoryException {
        // Check if a user with the given ID already exists.
        if (users.stream().anyMatch(u -> u.getId().equals(userID))) {
            throw new IllegalArgumentException("User with given id already exists");
        }
        
        var user = new User(userID, credits); // Create new user.
        this.users.add(user); // Add user to the local list.
        this.repository.saveUser(user); // Persist user to the repository.

        return user.getInfo(); // Return user info.
    }

    @Override
    public UserInfo getUserByID(String userID) throws IllegalArgumentException {
        Optional<User> targetUser = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst(); // Find user by ID.
        if (targetUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID!");
        } 
        return targetUser.get().getInfo(); // Return user info.
    }

    @Override
    public EBikeInfo getEBikeByID(String bikeID) throws IllegalArgumentException {
        Optional<EBike> targetBike = this.bikes.stream().filter(b -> b.getId().equals(bikeID)).findFirst(); // Find bike by ID.
        if (targetBike.isEmpty()) {
            throw new IllegalArgumentException("Invalid bike ID!");
        } 
        return targetBike.get().getInfo(); // Return bike info.
    }

    @Override
    public List<RideInfo> getRides() {
        return this.rides.stream().map(Ride::getInfo).toList(); // Return a list of ongoing rides.
    }

    @Override
    public RideInfo getRideForUser(String userID) throws IllegalArgumentException, RepositoryException {
        var ride = this.rides.stream().filter(r -> r.getUser().getId().equals(userID)).findFirst(); // Find ride for user.
        if (ride.isEmpty()) {
            throw new IllegalArgumentException("Ride for user '" + userID + "' does not exist");
        }
        return ride.get().getInfo(); // Return ride info.
    }

    @Override
    public EBikeInfo updateEBike(EBikeInfo bikeInfo) throws IllegalArgumentException, RepositoryException {
        var bikeOpt = bikes.stream().filter(b -> b.getId().equals(bikeInfo.bikeID())).findFirst(); // Find bike by ID.
        if (bikeOpt.isEmpty()) {
            throw new IllegalArgumentException("Bike not found.");
        }
        var bike = bikeOpt.get(); // Get bike.

        // Update bike properties.
        bike.updateState(bikeInfo.state());
        bike.updateLocation(bikeInfo.loc());
        bike.updateDirection(bikeInfo.direction());
        bike.updateSpeed(bikeInfo.speed());
        bike.decreaseBatteryLevel(bike.getBatteryLevel() - bikeInfo.batteryLevel());

        this.repository.saveEBike(bike); // Persist updated bike.

        return bike.getInfo(); // Return updated bike info.
    }
}
