package sap.ass01.businessLogic;

import java.util.List;
import java.util.concurrent.Future;

public class MyUserBusinessLogic implements UserBusinessLogic {

    @Override
    public Future<List<User>> getUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsers'");
    }

    @Override
    public Future<List<EBike>> getEBikes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEBikes'");
    }

    @Override
    public Future<Ride> beginRide(User u, EBike bike) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'beginRide'");
    }

    @Override
    public Future<Void> endRide(Ride ride) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endRide'");
    }

    @Override
    public Future<Void> addCredits(User u, int credits) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCredits'");
    }

}
