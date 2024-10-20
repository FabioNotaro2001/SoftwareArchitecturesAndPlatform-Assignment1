package sap.ass01.clean.infrastructure.repo;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.vertx.core.json.JsonObject;
import sap.ass01.clean.domain.*;
import sap.ass01.clean.domain.EBike.EBikeState;

/**
 * MyRepoPersistence handles the persistence of data related to users and eBikes,
 * storing them in JSON format. This class implements the RepositoryInterface and provides
 * methods for saving and retrieving user and eBike information from a JSON-based storage.
 */
public class MyRepoAdapter implements RepositoryInterface {
	private String PATH_USER = "users";    // Path for user data storage.
	private String PATH_EBIKE = "ebikes";  // Path for eBike data storage.
	private String dbaseFolder;            // Base folder for storing database files.
	
	// Constructor initializing the base folder for database files.
	public MyRepoAdapter() {
		this.dbaseFolder =  "./database";  // Default path for the database folder.
	}

	/**
	 * Initializes the storage system by creating necessary directories.
	 */
	public void init() {
		makeDir(dbaseFolder);  // Create the base folder if not exists.
		makeDir(dbaseFolder + File.separator + PATH_USER);   // Create user data directory.
		makeDir(dbaseFolder + File.separator + PATH_EBIKE);  // Create eBike data directory.
	}	

	/**
	 * Saves a given JsonObject (representing a user or an eBike) to a file.
	 * @param db  Directory path where to store the object (user or eBike).
	 * @param id  The ID of the object (used to name the file).
	 * @param obj The JsonObject to save.
	 * @throws RepositoryException in case of any file operation failure.
	 */
	private void saveObj(String db, String id, JsonObject obj) throws RepositoryException {
		try {
			// Open a file for writing (create a new one if it doesn't exist).
			FileWriter fw = new FileWriter(dbaseFolder + File.separator + db + File.separator + id + ".json");
			java.io.BufferedWriter wr = new BufferedWriter(fw);	
		
			// Write the JsonObject as a file.
			wr.write(obj.encodePrettily());
			wr.flush();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RepositoryException();  // Custom exception for repository errors.
		}
	}
	
	/**
	 * Creates a directory if it does not already exist.
	 * @param name Name (path) of the directory to create.
	 */
	private void makeDir(String name) {
		try {
			File dir = new File(name);
			if (!dir.exists()) {
				dir.mkdir();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Saves an eBike object as a file by converting it to a JSON format.
	 * @param eBike The eBike to save.
	 * @throws RepositoryException if saving fails.
	 */
	@Override
	public void saveEBike(EBike eBike) throws RepositoryException {
		// Create a JsonObject representing the eBike's data.
		JsonObject obj = new JsonObject();
		obj.put("ID", eBike.getId());
		obj.put("STATE", eBike.getState().toString());
		obj.put("LOC_X", eBike.getLocation().x());
		obj.put("LOC_Y", eBike.getLocation().y());
		obj.put("DIR_X", eBike.getDirection().x());
		obj.put("DIR_Y", eBike.getDirection().y());
		obj.put("SPEED", eBike.getSpeed());
		obj.put("BATTERY", eBike.getBatteryLevel());

		// Save the JsonObject to a file under the eBike path.
		this.saveObj(PATH_EBIKE, eBike.getId(), obj);
	}

	/**
	 * Saves a User object as a file by converting it to a JSON format.
	 * @param user The user to save.
	 * @throws RepositoryException if saving fails.
	 */
	@Override
	public void saveUser(User user) throws RepositoryException {
		// Create a JsonObject representing the user's data.
		JsonObject obj = new JsonObject();
		obj.put("ID", user.getId());
		obj.put("CREDIT", user.getCredit());

		// Save the JsonObject to a file under the user path.
		this.saveObj(PATH_USER, user.getId(), obj);
	}

	/**
	 * Retrieves all users stored in the database.
	 * @return A list of User objects.
	 * @throws RepositoryException if reading fails.
	 */
	@Override
	public List<User> getUsers() throws RepositoryException {
		List<User> users = new ArrayList<>();
		File userDir = new File(dbaseFolder + File.separator + PATH_USER);  // User data directory.
		
		// Check if the directory exists and contains files.
		if (userDir.exists() && userDir.isDirectory()) {
			File[] userFiles = userDir.listFiles((dir, name) -> name.endsWith(".json"));  // Filter .json files
			
			if (userFiles != null) {
				for (File userFile : userFiles) {
					try {
						// Read file content and convert it to JsonObject.
						String content = new String(Files.readAllBytes(userFile.toPath()));
						JsonObject obj = new JsonObject(content);
						
						// Create a User object from the JsonObject.
						User user = new User(obj.getString("ID"), obj.getInteger("CREDIT"));
						users.add(user);
					} catch (IOException e) {
						throw new RepositoryException();  // Handle file read error.
					}
				}
			}
		}
		return users;  // Return the list of users.
	}

	/**
	 * Retrieves a user by ID from the database.
	 * @param id The ID of the user to retrieve.
	 * @return An Optional containing the user if found, otherwise empty.
	 * @throws RepositoryException if reading fails.
	 */
	@Override
	public Optional<User> getUserByID(String id) throws RepositoryException {
		File userFile = new File(dbaseFolder + File.separator + PATH_USER + File.separator + id + ".json");

		if (!userFile.exists()) {
			return Optional.empty();  
		} else {
			try {
				// Read file content and convert to JsonObject.
				String content = new String(Files.readAllBytes(userFile.toPath()));
				JsonObject obj = new JsonObject(content);
				
				// Return a User object wrapped in an Optional.
				return Optional.of(new User(obj.getString("ID"), obj.getInteger("CREDIT")));
			} catch (IOException e) {
				throw new RepositoryException();  // Handle file read error.
			}
		}
	}

	/**
	 * Retrieves all eBikes stored in thedatabase.
	 * @return A list of EBike objects.
	 * @throws RepositoryException if reading fails.
	 */
	@Override
	public List<EBike> getEBikes() throws RepositoryException {
		List<EBike> ebikes = new ArrayList<>();
		File ebikeDir = new File(dbaseFolder + File.separator + PATH_EBIKE);  // eBike data directory.
		
		// Check if the directory exists and contains files.
		if (ebikeDir.exists() && ebikeDir.isDirectory()) {
			File[] ebikeFiles = ebikeDir.listFiles((dir, name) -> name.endsWith(".json"));  // Filter .json files.
			
			if (ebikeFiles != null) {
				for (File ebikeFile : ebikeFiles) {
					try {
						// Read file content and convert it to JsonObject.
						String content = new String(Files.readAllBytes(ebikeFile.toPath()));
						JsonObject obj = new JsonObject(content);
						
						// Create an EBike object from the JsonObject.
						EBike ebike = new EBike(
							obj.getString("ID"),
							EBikeState.valueOf(obj.getString("STATE")),
							new P2d(obj.getDouble("LOC_X"), obj.getDouble("LOC_Y")),
							new V2d(obj.getDouble("DIR_X"), obj.getDouble("DIR_Y")),
							obj.getDouble("SPEED"),
							obj.getInteger("BATTERY")
						);
						ebikes.add(ebike);
					} catch (IOException e) {
						throw new RepositoryException();  // Handle file read error.
					}
				}
			}
		}
		return ebikes;  // Return the list of eBikes.
	}

	/**
	 * Retrieves an eBike by ID from the database.
	 * @param id The ID of the eBike to retrieve.
	 * @return An Optional containing the eBike if found, otherwise empty.
	 * @throws RepositoryException if reading fails.
	 */
	@Override
	public Optional<EBike> getEBikeByID(String id) throws RepositoryException {
		File ebikeFile = new File(dbaseFolder + File.separator + PATH_EBIKE + File.separator + id + ".json");

		// Check if the file exists.
		if (!ebikeFile.exists()) {
			return Optional.empty();
		} else {
			try {
				// Read file content and convert to JsonObject.
				String content = new String(Files.readAllBytes(ebikeFile.toPath()));
				JsonObject obj = new JsonObject(content);

				// Return an EBike object wrapped in an Optional.
				return Optional.of(new EBike(
					obj.getString("ID"),
					EBikeState.valueOf(obj.getString("STATE")),
					new P2d(obj.getDouble("LOC_X"), obj.getDouble("LOC_Y")),
					new V2d(obj.getDouble("DIR_X"), obj.getDouble("DIR_Y")),
					obj.getDouble("SPEED"),
					obj.getInteger("BATTERY")
				));
			} catch (IOException e) {
				throw new RepositoryException();  // Handle file read error.
			}
		}
	}
}
