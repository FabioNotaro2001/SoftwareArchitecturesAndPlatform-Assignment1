package sap.ass01.businessLogic;

import java.util.List;

// TODO: cambiare gli oggetti restituiti, tipo User, con dei record per i loro dati.
public interface Server {
    public List<UserInfo> getUsers();

    public UserInfo getUserByID(String userID) throws IllegalArgumentException;

    public List<EBikeInfo> getEBikes();

    public EBikeInfo getEBikeByID(String bikeID) throws IllegalArgumentException;

    public EBikeInfo addEBike(String bikeID) throws RepositoryException;

    public void removeEBike(String bikeID) throws IllegalStateException, RepositoryException;

    public RideInfo beginRide(String userID, String bikeID) throws IllegalArgumentException;

    public void endRide(String userID, String bikeID) throws IllegalArgumentException;

    public void addCredits(String userID, int credits) throws RepositoryException;

    public UserInfo createUser(String userID, int credits) throws IllegalArgumentException, RepositoryException;
}
