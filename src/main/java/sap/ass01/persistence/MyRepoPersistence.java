package sap.ass01.persistence;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonObject;
import sap.ass01.businessLogic.*;
import sap.ass01.businessLogic.EBike.EBikeState;
import sap.ass01.presentation.UserGUI;

public class MyRepoPersistence implements RepositoryInterface {

	private String PATH_USER = "users";
	private String PATH_EBIKE = "ebikes";
	
	private String dbaseFolder;
	
	public MyRepoPersistence() {
		this.dbaseFolder =  "database";
	}

	public void init() {
		makeDir(dbaseFolder);
		makeDir(dbaseFolder + File.separator + PATH_USER);
		makeDir(dbaseFolder + File.separator + PATH_EBIKE);
	}	

	private void saveObj(String db, String id, JsonObject obj) throws RepositoryException {
		try {									
			FileWriter fw = new FileWriter(dbaseFolder + File.separator + db + File.separator + id + ".json");
			java.io.BufferedWriter wr = new BufferedWriter(fw);	
		
			wr.write(obj.encodePrettily());
			wr.flush();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RepositoryException();
		}
	}
	
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

	@Override
	public void saveEBike(EBike eBike) throws RepositoryException {
		JsonObject obj = new JsonObject();
		obj.put("ID", eBike.getId());
		obj.put("STATE", eBike.getState().toString());
		obj.put("LOC_X", eBike.getLocation().x());
		obj.put("LOC_Y", eBike.getLocation().y());
		obj.put("DIR_X", eBike.getDirection().x());
		obj.put("DIR_Y", eBike.getDirection().y());
		obj.put("SPEED", eBike.getSpeed());
		obj.put("BATTERY", eBike.getBatteryLevel());
		this.saveObj(PATH_EBIKE, eBike.getId(), obj);
	}

	@Override
	public void saveUser(User user) throws RepositoryException {
		JsonObject obj = new JsonObject();
		obj.put("ID", user.getId());
		obj.put("CREDIT", user.getCredit());
		this.saveObj(PATH_USER, user.getId(), obj);
	}

	@Override
	public List<User> getUsers() throws RepositoryException {
		List<User> users = new ArrayList<>();
		File userDir = new File(dbaseFolder + File.separator + PATH_USER);
		
		if (userDir.exists() && userDir.isDirectory()) {
			File[] userFiles = userDir.listFiles((dir, name) -> name.endsWith(".json"));
			
			if (userFiles != null) {
				for (File userFile : userFiles) {
					try {
						String content = new String(Files.readAllBytes(userFile.toPath()));
						JsonObject obj = new JsonObject(content);
						User user = new User(obj.getString("ID"), obj.getInteger("CREDIT"));
						users.add(user);
					} catch (IOException e) {
						throw new RepositoryException();
					}
				}
			}
		}
		return users;
	}

	@Override
	public User getUserByID(String id) throws RepositoryException {
		File userFile = new File(dbaseFolder + File.separator + PATH_USER + File.separator + id + ".json");
		if (userFile.exists()) {
			try {
				String content = new String(Files.readAllBytes(userFile.toPath()));
				JsonObject obj = new JsonObject(content);
				return new User(obj.getString("ID"), obj.getInteger("CREDIT"));
			} catch (IOException e) {
				throw new RepositoryException();
			}
		} else {
			throw new RepositoryException();
		}
	}

	@Override
	public List<EBike> getEBikes() throws RepositoryException {
		List<EBike> ebikes = new ArrayList<>();
		File ebikeDir = new File(dbaseFolder + File.separator + PATH_EBIKE);
		
		if (ebikeDir.exists() && ebikeDir.isDirectory()) {
			File[] ebikeFiles = ebikeDir.listFiles((dir, name) -> name.endsWith(".json"));
			
			if (ebikeFiles != null) {
				for (File ebikeFile : ebikeFiles) {
					try {
						String content = new String(Files.readAllBytes(ebikeFile.toPath()));
						JsonObject obj = new JsonObject(content);
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
						throw new RepositoryException();
					}
				}
			}
		}
		return ebikes;
	}

	@Override
	public EBike getEBikeByID(String id) throws RepositoryException {
		File ebikeFile = new File(dbaseFolder + File.separator + PATH_EBIKE + File.separator + id + ".json");
		if (ebikeFile.exists()) {
			try {
				String content = new String(Files.readAllBytes(ebikeFile.toPath()));
				JsonObject obj = new JsonObject(content);
				return new EBike(
					obj.getString("ID"),
					EBikeState.valueOf(obj.getString("STATE")),
					new P2d(obj.getDouble("LOC_X"), obj.getDouble("LOC_Y")),
					new V2d(obj.getDouble("DIR_X"), obj.getDouble("DIR_Y")),
					obj.getDouble("SPEED"),
					obj.getInteger("BATTERY")
				);
			} catch (IOException e) {
				throw new RepositoryException();
			}
		} else {
			throw new RepositoryException();
		}
	}

	public static void main(String[] args) throws RepositoryException {
		MyRepoPersistence pippo = new MyRepoPersistence();
		pippo.init();
		User usr = new User("HelloAMICO", 120);
		User usr2 = new User("HelloAMICO2", 90);
		pippo.saveUser(usr);
		pippo.saveUser(usr2);
		EBike eBike = new EBike("CIAO");
		pippo.saveEBike(eBike);
		System.out.println(pippo.getEBikeByID("CIAO"));
		System.out.println(pippo.getUsers());
	}
}
