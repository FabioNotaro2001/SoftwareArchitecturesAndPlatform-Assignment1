package sap.ass01.businessLogic;

public class User {

	private String id;
	private int credit;
	
	public User(String id) {
		this.id = id;
		this.credit = 0;
	}

	public User(String id, int credit) {
		this.id = id;
		this.credit = credit;
	}
	
	public String getId() {
		return id;
	}
	
	public int getCredit() {
		return credit;
	}
	
	public void rechargeCredit(int deltaCredit) { // FIXME: useless for user and admin
		credit += deltaCredit;
	}
	
	public void decreaseCredit(int amount) {	// FIXME: useless for user and admin
		credit -= amount;
		if (credit < 0) {
			credit = 0;
		}
	}
	
	public String toString() {
		return "{ id: " + id + ", credit: " + credit + " }";
	}

	
}
