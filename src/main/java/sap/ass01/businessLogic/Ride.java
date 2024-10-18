package sap.ass01.businessLogic;

import java.util.Date;
import java.util.Optional;

public class Ride {
	private Date startedDate;
	private Optional<Date> endDate;
	private User user;
	private EBike ebike;
	private String id;
	
	public Ride(String id, User user, EBike ebike) {
		this.id = id;
		this.startedDate = new Date();
		this.endDate = Optional.empty();
		this.user = user;
		this.ebike = ebike;
	}
	
	public String getId() {
		return id;
	}

	public Date getStartedDate() {
		return startedDate;
	}
	
	public Optional<Date> getEndDate() {
		return endDate;
	}

	public User getUser() {
		return user;
	}

	public EBike getEBike() {
		return ebike;
	}

	public RideInfo getInfo(){
		return new RideInfo(this.id, this.user.getId(), this.ebike.getId(), true);
	}
	
	public String toString() {
		return "{ id: " + this.id + ", user: " + user.getId() + ", bike: " + ebike.getId() + " }";
	}
}
