package sap.ass01.presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class UserGUI extends JFrame implements ActionListener, UserGUICallback {
    // TODO: ha senso che l'utente possa vedere in tempo reale il suo credito che scende e la batteria della bici che sta usando.
    private JButton startRideButton;
    private JLabel userCreditLabel;

    public UserGUI(){
        setup();
    }

    protected void setup() {
        setTitle("USER GUI");        
        setSize(800,300);
        setResizable(false);
        
        setLayout(new BorderLayout());

		startRideButton = new JButton("Start Ride");
		startRideButton.addActionListener(this);

        userCreditLabel = new JLabel("Credit: 10000000000000000");
		
		JPanel topPanel = new JPanel();		
		topPanel.add(startRideButton);	
        topPanel.add(userCreditLabel);
	    add(topPanel,BorderLayout.NORTH);

	    	    		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}
		});
    }

    public void display() {
    	SwingUtilities.invokeLater(() -> {
    		this.setVisible(true);
    	});
    }

    public void startNewRide(String userId, String bikeId) {
    	/*rideId++; 	 
    	String idRide = "ride-";
    	
    	var b = bikes.get(bikeId);
    	var u = users.get(userId);
    	var ride = new Ride(idRide, u, b);
    	b.updateState(EBike.EBikeState.IN_USE);
    	rides.put(idRide, ride);
    	ride.start(this);
        
        log("started new Ride " + ride);       */ 
    }

    public void endRide(String rideId) {
    	/*var r = rides.get(rideId);
    	r.end();
    	rides.remove(rideId);*/
    }
    
    //public Enumeration<EBike> getEBikes(){
    	//return bikes.elements();
    //}
        

    @Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.startRideButton) {
	        JDialog d = new RideDialog(this);
	        d.setVisible(true);
        }
	}

	private void log(String msg) {
		System.out.println("[EBikeApp] " + msg);
	}
	
	public static void main(String[] args) {
		var w = new UserGUI();
		w.display();
	}

	@Override
	public void notifyBikeStateChanged(String bikeID, String state, int batteryLevel) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyBikeStateChanged'");
	}

	@Override
	public void notifyUserCreditRecharged(String userID, int credits) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyUserCreditRecharged'");
	}

	@Override
	public void notifyRideStepDone(String userID, String bikeID, double x, double y, int batteryLevel, int userCredits,
			boolean rideEnded) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyRideStepDone'");
	}

}
