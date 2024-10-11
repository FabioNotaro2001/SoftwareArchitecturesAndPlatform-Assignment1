package sap.ass01.businessLogic;

import java.util.Date;
import java.util.Optional;
import sap.ass01.bbom.RideSimulation;
import sap.ass01.bbom.RideSimulationControlPanel;
import sap.ass01.presentation.AdminGUI;

public class Ride {
	private Date startedDate;
	private Optional<Date> endDate;
	private User user;
	private EBike ebike;
	private boolean ongoing;
	private String id;
	private RideSimulation rideSimulation;
	
	public Ride(String id, User user, EBike ebike) {
		this.id = id;
		this.startedDate = new Date();
		this.endDate = Optional.empty();
		this.user = user;
		this.ebike = ebike;
	}
	
	public synchronized String getId() {
		return id;
	}

	public void start(AdminGUI app) { // FIXME: useless for user and admin
		ongoing = true;
        rideSimulation = new RideSimulation(this, user, app);
        RideSimulationControlPanel ridingWindow = new RideSimulationControlPanel(this, app);
        ridingWindow.display();
        rideSimulation.start();
	}
	
	public void end() {				// FIXME: useless for user and admin
		endDate = Optional.of(new Date());
		ongoing = false;
		rideSimulation.stopSimulation();
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public boolean isOngoing() {	// FIXME: useless for user and admin
		return this.ongoing;
	}
	
	public Optional<Date> getEndDate() {	// FIXME: useless for user and admin
		return endDate;
	}

	public User getUser() {
		return user;
	}

	public EBike getEBike() {
		return ebike;
	}

	public RideInfo getInfo(){
		return new RideInfo(this.id, this.user.getId(), this.ebike.getId(), this.ongoing);
	}
	
	public String toString() {
		return "{ id: " + this.id + ", user: " + user.getId() + ", bike: " + ebike.getId() + " }";
	}
}
