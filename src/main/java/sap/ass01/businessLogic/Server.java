package sap.ass01.businessLogic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Server extends Remote {
    List<User> getUsers() throws RemoteException;
    
    List<EBike> getEBikes() throws RemoteException;

    void registerForBikeUpdates(BikeUpdateCallback callback);
}
