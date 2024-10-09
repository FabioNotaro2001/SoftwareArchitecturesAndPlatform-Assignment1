package sap.ass01.businessLogic;

import java.util.List;
import java.util.Optional;

/**
 * 
 * Outbound Port
 * 
 */
public interface RepositoryInterface {

	void saveEBike(EBike eBike) throws RepositoryException;
	void saveUser(User user) throws RepositoryException;
	List<User> getUsers() throws RepositoryException;
	Optional<User> getUserByID(String id) throws RepositoryException;
	List<EBike> getEBikes() throws RepositoryException;
	Optional<EBike> getEBikeByID(String id) throws RepositoryException;


}
