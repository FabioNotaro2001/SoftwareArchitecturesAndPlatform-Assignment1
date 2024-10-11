package sap.ass01.businessLogic;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sap.ass01.persistence.MyRepoPersistence;

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
        return this.users.stream().map(u -> u.getInfo()).toList();
    }

    @Override
    public List<EBikeInfo> getEBikes()  {
        return this.bikes.stream().map(b -> b.getInfo()).toList();
    }

    @Override
    public EBikeInfo addEBike(String bikeID) throws RepositoryException {
        EBike bike = new EBike(bikeID);
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
    public RideInfo beginRide(String userID, String bikeID) throws IllegalArgumentException {
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

        var ride = new Ride(String.valueOf(rideId++), user, bike);
        this.rides.add(ride);

        // TODO: start ride in thread
        // ride.start(null);

        return ride.getInfo();
    }

    @Override
    public void endRide(String userID, String bikeID) throws IllegalArgumentException {
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

        var rideOpt = this.rides.stream().filter(r -> r.getEBike() == bike).findFirst();
        if (rideOpt.isEmpty()) {
            throw new IllegalArgumentException("No ride found with the given bike");
        }
        
        var ride = rideOpt.get();
        if(ride.getUser() != user) {
            throw new IllegalArgumentException("The given user is not riding the bike");
        }

        // TODO: stop ride
        // ride.end();
    }

    @Override
    public void addCredits(String userID, int credits) throws RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }

        var user = userOpt.get();
        user.rechargeCredit(credits);
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

        //TODO: notify admins of user creation

        return user.getInfo();
    }

    public static void main(String[] args) throws RepositoryException {
        try {
            ServerImpl server = new ServerImpl(new MyRepoPersistence());

            // Esporta l'oggetto server per renderlo disponibile alle chiamate remote.
            //UserAppService userServerStub = (UserAppService) UnicastRemoteObject.exportObject(server, 0);
            //AdminAppService adminServerStub = (AdminAppService) UnicastRemoteObject.exportObject(server, 0);
            // Ottiene il registro RMI.
            Registry registry = LocateRegistry.getRegistry();

            // Associa (rebind) il nome del server al serverStub nel registro RMI.
            //registry.rebind(USER_SERVER_NAME, userServerStub);
            //registry.rebind(ADMIN_SERVER_NAME, adminServerStub);
            System.out.println("Server created.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    // TODO: i thread vanno nel business logic o nel service? Rifletterci.
}
