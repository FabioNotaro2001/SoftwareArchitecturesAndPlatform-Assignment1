package sap.ass01.businessLogic;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sap.ass01.businessLogic.EBike.EBikeState;

// TODO: batteria delle bici deve ricaricarsi (thread?)

public class ServerImpl implements Server {
    private RepositoryInterface repository;

    private List<User> users;
    private List<EBike> bikes;
    private int rideId = 0;
    private List<Ride> rides;

    public ServerImpl(RepositoryInterface repository) throws RepositoryException {
        this.repository = repository;
        this.users = this.repository.getUsers();
        this.bikes = this.repository.getEBikes();
        this.rides = new ArrayList<>();
    }

    @Override
    public List<UserInfo> getUsers() {
        return this.users.stream().map(User::getInfo).toList();
    }

    @Override
    public List<EBikeInfo> getEBikes()  {
        return this.bikes.stream().map(EBike::getInfo).toList();
    }

    @Override
    public EBikeInfo addEBike(String bikeID, P2d pos) throws RepositoryException {
        EBike bike = new EBike(bikeID, pos);
        this.bikes.add(bike);
        this.repository.saveEBike(bike);
        return bike.getInfo();
    }

    @Override
    public void removeEBike(String bikeID) throws IllegalStateException, RepositoryException {
        if (this.rides.stream().anyMatch(r -> r.getEBike().getId().equals(bikeID))) {
            throw new IllegalStateException("Bike currently in use");
        }
        this.bikes.removeIf(b -> b.getId().equals(bikeID));
    }

    @Override
    public RideInfo beginRide(String userID, String bikeID) throws RepositoryException, IllegalArgumentException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        var bikeOpt = this.bikes.stream().filter(b -> b.getId().equals(bikeID)).findFirst();
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }
        if (bikeOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid bike id");
        }

        var user = userOpt.get();
        var bike = bikeOpt.get();
        bike.updateState(EBikeState.IN_USE);
        updateEBike(bike.getInfo());

        var ride = new Ride(String.valueOf(rideId++), user, bike);
        this.rides.add(ride);

        return ride.getInfo();
    }

    @Override
    public void endRide(String userID, String bikeID) throws IllegalArgumentException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        var bikeOpt = this.bikes.stream().filter(b -> b.getId().equals(bikeID)).findFirst();
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }
        if (bikeOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid bike id");
        }

        var user = userOpt.get();
        var bike = bikeOpt.get();
        bike.updateState(bike.getBatteryLevel() > 0 ? EBikeState.AVAILABLE : EBikeState.MAINTENANCE);
        bike.updateSpeed(0);
        updateEBike(bike.getInfo());

        var rideOpt = this.rides.stream().filter(r -> r.getEBike() == bike).findFirst();
        if (rideOpt.isEmpty()) {
            throw new IllegalArgumentException("No ride found with the given bike");
        }
        
        var ride = rideOpt.get();
        if(ride.getUser() != user) {
            throw new IllegalArgumentException("The given user is not riding the bike");
        }
    }

    @Override
    public void addCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }

        var user = userOpt.get();
        user.rechargeCredit(credits);
        this.repository.saveUser(user);
    }

    @Override
    public void decreaseCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }

        var user = userOpt.get();
        user.decreaseCredit(credits);
        this.repository.saveUser(user);
    }

    @Override
    public UserInfo createUser(String userID, int credits) throws IllegalArgumentException, RepositoryException {
        if (users.stream().anyMatch(u -> u.getId() == userID)) {
            throw new IllegalArgumentException("User with given id already exists");
        }
        
        var user = new User(userID, credits);
        this.users.add(user);
        this.repository.saveUser(user);

        return user.getInfo();
    }

    @Override
    public UserInfo getUserByID(String userID) throws IllegalArgumentException {
        Optional<User> targetUser = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        if (targetUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID!");
        } 
        return targetUser.get().getInfo();
    }

    @Override
    public EBikeInfo getEBikeByID(String bikeID) throws IllegalArgumentException {
        Optional<EBike> targetBike = this.bikes.stream().filter(b -> b.getId().equals(bikeID)).findFirst();
        if (targetBike.isEmpty()) {
            throw new IllegalArgumentException("Invalid bike ID!");
        } 
        return targetBike.get().getInfo();
    }
    // TODO: i thread vanno nel business logic o nel service? Rifletterci. Forse ha senso metterli nel service.

    @Override
    public List<RideInfo> getRides() {
        return this.rides.stream().map(Ride::getInfo).toList();
    }

    @Override
    public EBikeInfo updateEBike(EBikeInfo bikeInfo) throws IllegalArgumentException, RepositoryException {
        var bikeOpt = bikes.stream().filter(b -> b.getId() == bikeInfo.bikeID()).findFirst();
        if (bikeOpt == null) {
            throw new IllegalArgumentException("Bike not found.");
        }
        var bike = bikeOpt.get();
        bike.updateState(bikeInfo.state());
        bike.updateLocation(bikeInfo.loc());
        bike.updateDirection(bikeInfo.direction());
        bike.updateSpeed(bikeInfo.speed());
        bike.decreaseBatteryLevel(bike.getBatteryLevel() - bikeInfo.batteryLevel());

        this.repository.saveEBike(bike);

        return bike.getInfo();
    }
}
