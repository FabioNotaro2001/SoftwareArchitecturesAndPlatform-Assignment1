package sap.ass01.businessLogic;

import java.util.List;

/**
 * 
 * Outbound Port
 * 
 */
public interface RepositoryInterface {

	void saveEBike(EBike eBike) throws RepositoryException;
	void saveUser(User user) throws RepositoryException;
	List<User> getUsers() throws RepositoryException;
	User getUserByID(String id) throws RepositoryException;
	List<EBike> getEBikes() throws RepositoryException;
	EBike getEBikeByID(String id) throws RepositoryException;

}
