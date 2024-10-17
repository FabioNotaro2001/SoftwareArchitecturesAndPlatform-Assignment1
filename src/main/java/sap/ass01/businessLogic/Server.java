package sap.ass01.businessLogic;

import java.util.List;

public interface Server {
    public List<UserInfo> getUsers();

    public UserInfo getUserByID(String userID) throws IllegalArgumentException;

    public List<EBikeInfo> getEBikes();

    public EBikeInfo getEBikeByID(String bikeID) throws IllegalArgumentException;

    public EBikeInfo updateEBike(EBikeInfo bikeInfo) throws IllegalArgumentException, RepositoryException;

    public EBikeInfo addEBike(String bikeID, P2d pos) throws RepositoryException;

    public void removeEBike(String bikeID) throws IllegalStateException, RepositoryException;

    public List<RideInfo> getRides();

    public RideInfo beginRide(String userID, String bikeID) throws IllegalArgumentException, RepositoryException;

    public void endRide(String userID, String bikeID) throws IllegalArgumentException, RepositoryException;

    public UserInfo createUser(String userID, int credits) throws IllegalArgumentException, RepositoryException;

    public void addCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException;
    
    public void decreaseCredits(String userID, int credits) throws IllegalArgumentException, RepositoryException;
}
