package sap.ass01.layered.persistence;

import java.util.List;
import java.util.Optional;
import sap.ass01.layered.businessLogic.EBike;
import sap.ass01.layered.businessLogic.RepositoryException;
import sap.ass01.layered.businessLogic.User;

/**
 * [PERSISTENCE LAYER][RepositoryInterface][BUSINESS LOGIC LAYER].
 * Interface for the repository that handles data persistence operations.
 * This interface defines methods for saving and retrieving ebikes and users.
 */
public interface RepositoryInterface {
    /**
     * Saves an ebike object to the persistence layer.
     * @param eBike The ebike object to be saved.
     * @throws RepositoryException if an error occurs during the save operation.
     */
    void saveEBike(EBike eBike) throws RepositoryException; 

    /**
     * Saves a user object to the persistence layer.
     * @param user The user object to be saved.
     * @throws RepositoryException if an error occurs during the save operation.
     */
    void saveUser(User user) throws RepositoryException; 

    /**
     * Retrieves a list of all users from the persistence layer.
     * @return A list of User objects.
     * @throws RepositoryException if an error occurs during the retrieval operation.
     */
    List<User> getUsers() throws RepositoryException; 

    /**
     * Retrieves a user by their unique identifier.
     * @param id The unique identifier of the user to be retrieved.
     * @return An Optional containing the User object if found; otherwise, empty.
     * @throws RepositoryException if an error occurs during the retrieval operation.
     */
    Optional<User> getUserByID(String id) throws RepositoryException; 

    /**
     * Retrieves a list of all ebikes from the persistence layer.
     * @return A list of EBike objects.
     * @throws RepositoryException if an error occurs during the retrieval operation.
     */
    List<EBike> getEBikes() throws RepositoryException; 

    /**
     * Retrieves an ebike by its unique identifier.
     * @param id The unique identifier of the ebike to be retrieved.
     * @return An Optional containing the EBike object if found; otherwise, empty.
     * @throws RepositoryException if an error occurs during the retrieval operation.
     */
    Optional<EBike> getEBikeByID(String id) throws RepositoryException; 
}
