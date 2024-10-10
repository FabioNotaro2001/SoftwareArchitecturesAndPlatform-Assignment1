package sap.ass01.businessLogic;

import java.util.List;
import java.util.concurrent.Future;

public interface UserBusinessLogic { // FIXME
    List<User> getUsers();

    List<EBike> getEBikes();

    Future<Ride> beginRide(User u, EBike bike);

    Future<Void> endRide(Ride ride);

    Future<Void> addCredits(User u, int credits);
}