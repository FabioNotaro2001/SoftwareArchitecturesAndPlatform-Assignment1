package sap.ass01.businessLogic;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sap.ass01.persistence.MyRepoPersistence;

public class ServerImpl implements UserServer, AdminServer {
    
    private static final String USER_SERVER_NAME = "userServer";
    private static final String ADMIN_SERVER_NAME = "adminServer";

    private transient RepositoryInterface repository;

    private transient List<RideCallback> adminListeners;
    private transient Map<String, RideCallback> userListeners; // bikeID -> callback
    private transient List<BikeUpdateCallback> bikeUpdateListeners;

    private List<User> users;
    private List<EBike> bikes;
    private transient int rideId = 0;
    private transient List<Ride> rides;

    public ServerImpl(RepositoryInterface repository) throws RepositoryException {
        this.repository = repository;
        this.adminListeners = new ArrayList<>();
        this.userListeners = new HashMap<>();
        this.bikeUpdateListeners = new ArrayList<>();

        this.users = this.repository.getUsers();
        this.bikes = this.repository.getEBikes();
        this.rides = new ArrayList<>();
    }

    @Override
    public synchronized List<User> getUsers() throws RemoteException {
        return List.copyOf(this.users);
    }

    @Override
    public synchronized List<EBike> getEBikes() throws RemoteException {
        return List.copyOf(this.bikes);
    }

    @Override
    public void registerForBikeUpdates(BikeUpdateCallback callback) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerForBikeUpdates'");
    }

    @Override
    public synchronized void registerForRidesUpdates(RideCallback callback) throws RemoteException {
        this.adminListeners.add(callback);
    }

    @Override
    public synchronized void addEBike(String bikeID) throws RemoteException, RepositoryException {
        EBike bike = new EBike(bikeID);
        this.bikes.add(bike);
        this.repository.saveEBike(bike);
    }

    @Override
    public synchronized void removeEBike(String bikeID) throws RemoteException, IllegalStateException, RepositoryException {
        if (this.rides.stream().anyMatch(r -> r.getEBike().getId().equals(bikeID))) {
            throw new IllegalStateException("Bike currently in use");
        }
        this.bikes.removeIf(b -> b.getId().equals(bikeID));
    }

    @Override
    public synchronized Ride beginRide(String userID, String bikeID, RideCallback callback) throws RemoteException, IllegalArgumentException {
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
        this.userListeners.put(bikeID, callback);

        // FIXME: maybe turn into separate method?
        callback.notifyClient(userID, bikeID, bike.getLocation(), bike.getBatteryLevel(), user.getCredit());
        this.adminListeners.forEach(c -> c.notifyClient(userID, bikeID, bike.getLocation(), bike.getBatteryLevel(), user.getCredit()));

        // TODO: start ride in thread
        // ride.start(null);

        return ride;
    }

    @Override
    public synchronized void endRide(String userID, String bikeID) throws RemoteException, IllegalArgumentException {
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

        bikeUpdateListeners.forEach(c -> c.notifyClient(bikeID, bike.getState(), bike.getBatteryLevel()));
    }

    @Override
    public synchronized void addCredits(String userID, int credits) throws RemoteException, RepositoryException {
        var userOpt = this.users.stream().filter(u -> u.getId().equals(userID)).findFirst();
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id");
        }

        var user = userOpt.get();
        user.rechargeCredit(credits);
        this.repository.saveUser(user);
    }

    @Override
    public User createUser(String userID, int credits) throws RemoteException, IllegalArgumentException, RepositoryException {
        if (users.stream().anyMatch(u -> u.getId() == userID)) {
            throw new IllegalArgumentException("User with given id already exists");
        }
        
        var user = new User(userID, credits);
        this.users.add(user);
        this.repository.saveUser(user);

        //TODO: notify admins of user creation

        return user;
    }

    public static void main(String[] args) throws RepositoryException {
        try {
            ServerImpl server = new ServerImpl(new MyRepoPersistence());

            // Esporta l'oggetto server per renderlo disponibile alle chiamate remote.
            UserServer userServerStub = (UserServer) UnicastRemoteObject.exportObject(server, 0);
            AdminServer adminServerStub = (AdminServer) UnicastRemoteObject.exportObject(server, 0);
            // Ottiene il registro RMI.
            Registry registry = LocateRegistry.getRegistry();

            // Associa (rebind) il nome del server al serverStub nel registro RMI.
            registry.rebind(USER_SERVER_NAME, userServerStub);
            registry.rebind(ADMIN_SERVER_NAME, adminServerStub);
            System.out.println("Server created.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
