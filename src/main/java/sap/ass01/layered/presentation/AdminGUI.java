package sap.ass01.layered.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import sap.ass01.layered.businessLogic.EBikeInfo;
import sap.ass01.layered.businessLogic.P2d;
import sap.ass01.layered.businessLogic.RideInfo;
import sap.ass01.layered.businessLogic.UserInfo;
import sap.ass01.layered.businessLogic.V2d;
import sap.ass01.layered.businessLogic.EBike.EBikeState;
import sap.ass01.layered.service.AdminGUICallback;
import sap.ass01.layered.service.AdminService;

/**
 * Admin GUI for managing E-Bikes, users, and rides.
 */
public class AdminGUI extends JFrame implements ActionListener, AdminGUICallback {
	private AdminService adminService; // Service for handling admin operations.

	private VisualiserPanel centralPanel; // Panel for visualizing bike locations.
    private JButton addEBikeButton; // Button to add a new E-Bike.
	private Map<String, UserInfo> users = new HashMap<>(); // Map of users.
	private Map<String, EBikeInfo> bikes = new HashMap<>(); // Map of E-Bikes.
	private Map<String, RideInfo> rides = new HashMap<>(); // Map of rides.
	
	private JList<String> usersList; // List component for displaying users.
	private JList<String> bikesList; // List component for displaying bikes.
	private JList<String> ridesList; // List component for displaying rides.

	private DefaultListModel<String> usersModel; // Model for user list.
	private DefaultListModel<String> bikesModel; // Model for bike list.
	private DefaultListModel<String> ridesModel; // Model for ride list.

    public AdminGUI(AdminService adminService) throws RemoteException {
		this.adminService = adminService; // Initialize admin service.
        setupModel(); // Setup data models.
        setupView(); // Setup the GUI layout.
    }
    
    protected void setupModel() throws RemoteException {
        // Populate bikes, users, and rides from the admin service.
        this.adminService.getEBikes().forEach(x -> bikes.put(x.bikeID(), x));
		this.adminService.getUsers().forEach(u -> users.put(u.userID(), u));
		this.adminService.getRides().forEach(r -> rides.put(r.bikeID(), r));
		this.adminService.registerGUI(this); // Register the GUI with the admin service.
    }

    protected void setupView() {
        setTitle("ADMIN GUI");        
        setSize(1000,600); // Set the size of the window.
        setResizable(false); // Prevent resizing of the window.
        
        setLayout(new BorderLayout()); // Set layout for the frame.

		addEBikeButton = new JButton("Add EBike"); // Create add bike button.
		addEBikeButton.addActionListener(this); // Add action listener.
		
		JPanel topPanel = new JPanel(); // Panel for top components.
		topPanel.add(addEBikeButton);	
	    add(topPanel, BorderLayout.NORTH); // Add top panel to the north.

        centralPanel = new VisualiserPanel(800, 500, this); // Create visualizer panel.
	    add(centralPanel, BorderLayout.CENTER); // Add visualizer to the center.
	    	    		
		addWindowListener(new WindowAdapter() { // Handle window close events.
			public void windowClosing(WindowEvent ev) {
				System.exit(-1); // Exit the application.
			}
		});

		JPanel eastPanel = new JPanel(); // Panel for side components.
		eastPanel.setLayout(new GridLayout(3, 1)); // Use grid layout for the side panel.
		eastPanel.setPreferredSize(new Dimension(300, 500)); // Set preferred size for the side panel.
		
		this.usersModel = getUsersModel(); // Get user list model.
		this.bikesModel = getBikesModel(); // Get bike list model.
		this.ridesModel = getRidesModel(); // Get ride list model.
		this.usersList = new JList<>(usersModel); // Create user list.
		this.bikesList = new JList<>(bikesModel); // Create bike list.
		this.ridesList = new JList<>(ridesModel); // Create ride list.
		
		// Add lists to the east panel within scroll panes.
		eastPanel.add(new JScrollPane(usersList));
		eastPanel.add(new JScrollPane(bikesList));
		eastPanel.add(new JScrollPane(ridesList));
		add(eastPanel, BorderLayout.EAST); // Add east panel to the right.
    }

	private void addOrReplaceRide(RideInfo info) {
		// Add or replace a ride in the rides map and update the model.
		var old = rides.put(info.rideId(), info);
		if (old == null) {
			ridesModel.addElement(info.toString()); // Add new ride to model.
		} else {
			ridesModel.clear(); // Clear model and refresh.
			ridesModel.addAll(rides.values().stream().map(RideInfo::toString).toList());	
		}
	}

	private void addOrReplaceUser(UserInfo info) {
		// Add or replace a user in the users map and update the model.
		var old = users.put(info.userID(), info);
		if (old == null) {
			usersModel.addElement(info.toString()); // Add new user to model.
		} else {
			usersModel.clear(); // Clear model and refresh.
			usersModel.addAll(users.values().stream().map(UserInfo::toString).toList());
		}
	}

	private void addOrReplaceEBike(EBikeInfo info) {
		// Add or replace an E-Bike in the bikes map and update the model.
		var old = bikes.put(info.bikeID(), info);
		if (old == null) {
			bikesModel.addElement(info.toString()); // Add new bike to model.
		} else {
			bikesModel.clear(); // Clear model and refresh.
			bikesModel.addAll(bikes.values().stream().map(EBikeInfo::toString).toList());	
		}
	}

	private void removeRide(String rideId) {
		// Remove a ride by its ID and refresh the model.
		rides.remove(rideId);
		ridesModel.clear();
		ridesModel.addAll(rides.values().stream().map(RideInfo::toString).toList());
	}

	private void removeEBike(String bikeId) {
		// Remove an E-Bike by its ID and refresh the model.
		bikes.remove(bikeId);
		bikesModel.clear();
		bikesModel.addAll(bikes.values().stream().map(EBikeInfo::toString).toList());
	}

	private DefaultListModel<String> getRidesModel() {
		// Create a model for the rides list.
		DefaultListModel<String> ridesModel = new DefaultListModel<>();
		ridesModel.addAll(rides.values().stream().map(RideInfo::toString).toList());
		return ridesModel;
	}

	private DefaultListModel<String> getBikesModel() {
		// Create a model for the bikes list.
		DefaultListModel<String> bikesModel = new DefaultListModel<>();
		bikesModel.addAll(bikes.values().stream().map(EBikeInfo::toString).toList());
		return bikesModel;
	}

	private DefaultListModel<String> getUsersModel() {
		// Create a model for the users list.
		DefaultListModel<String> usersModel = new DefaultListModel<>();
		usersModel.addAll(users.values().stream().map(UserInfo::toString).toList());
		return usersModel;
	}

    public void display() {
    	// Show the admin GUI on the Event Dispatch Thread.
    	SwingUtilities.invokeLater(() -> {
    		this.setVisible(true);
    	});
    }
    
    public void refreshView() {
    	// Refresh the central visualizer panel.
    	centralPanel.refresh();
    }  

    @Override
	public void actionPerformed(ActionEvent e) {
        // Handle action events.
        if (e.getSource() == this.addEBikeButton) {
	        JDialog d = new AddEBikeDialog(this, adminService); // Open dialog to add new E-Bike.
	        d.setVisible(true);
        }
	}
    
    public static class VisualiserPanel extends JPanel {
        private long dx; // X offset for rendering.
        private long dy; // Y offset for rendering.
        private AdminGUI app; // Reference to the main AdminGUI.
        
        public VisualiserPanel(int w, int h, AdminGUI app) {
            setSize(w, h); // Set the size of the panel.
            dx = w / 2; // Center x offset.
            dy = h / 2; // Center y offset.
            this.app = app; // Reference to the main application.
        }

        public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
    		
    		// Enable anti-aliasing and high-quality rendering.
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0, 0, this.getWidth(), this.getHeight()); // Clear the panel.

			// Render each E-Bike on the visualizer.
			app.bikes.values().forEach(b -> {
				var p = b.loc(); // Get the location of the bike.
    			int x0 = (int) (dx + p.x()); // Calculate screen x coordinate.
		        int y0 = (int) (dy - p.y()); // Calculate screen y coordinate.
		        g2.drawOval(x0, y0, 10, 10); // Draw bike as a circle.
		        g2.drawString(b.bikeID(), x0, y0 + 35); // Display bike ID.
			});
        }
        
        public void refresh() {
            // Refresh the visualizer panel.
            repaint();
        }
    }

	@Override
	public void notifyBikeStateChanged(String bikeID, EBikeState state, double x, double y, int batteryLevel) {
		EBikeInfo eBikeInfo = null;
		var bike = this.bikes.get(bikeID); // Get existing bike info.

		if (state == EBikeState.DISMISSED) {
			removeEBike(bikeID); // Remove bike if dismissed.
		} else {	
			// Create or update E-Bike info.
			if (bike == null) {
				eBikeInfo = new EBikeInfo(bikeID, state, new P2d(x, y), new V2d(-1, 0), 0.0, batteryLevel);
			} else {
				eBikeInfo = new EBikeInfo(bikeID, state, new P2d(x, y), bike.direction(), bike.speed(), batteryLevel);
			}
			addOrReplaceEBike(eBikeInfo); // Update the bike list model.
		}

		// Remove ongoing ride if bike is not in use.
		if (state != EBikeState.IN_USE) {
			var ride = rides.values().stream().filter(r -> r.bikeID().equals(bikeID)).findFirst();
			if (ride.isPresent()) {
				removeRide(ride.get().rideId()); // Remove the ride from the model.
			}
		}

		centralPanel.refresh(); // Refresh the visualizer panel.
	}

	@Override
	public void notifyUserCreditRecharged(String userID, int credits) {
		var newUserInfo = new UserInfo(userID, credits); // Create new user info.
		addOrReplaceUser(newUserInfo); // Update the user list model.
	}

	@Override
	public void notifyRideUpdate(RideInfo rideInfo) {
		// Handle ride updates based on ongoing status.
		if (rideInfo.ongoing()) {
			addOrReplaceRide(rideInfo); // Update the ride model.
		} else {
			removeRide(rideInfo.rideId()); // Remove the ride if completed.
		}
	}

	@Override
	public void notifyRideStepDone(String rideId, double x, double y, int batteryLevel, int userCredits) {
		// Update ride step information.
		var ride = rides.get(rideId);
		if (ride == null) {
			return; // Exit if ride not found.
		}

		var bike = bikes.get(ride.bikeID()); // Get the corresponding bike.

		// Update user credits
		var newUser = new UserInfo(ride.userID(), userCredits);
		addOrReplaceUser(newUser); // Update user list model.

		// Update bike information based on the ride.
		var newBike = new EBikeInfo(ride.bikeID(), bike.state(), new P2d(x, y), bike.direction(), bike.speed(), batteryLevel);
		addOrReplaceEBike(newBike); // Update bike list model.

		centralPanel.refresh(); // Refresh the visualizer panel.
	}

	@Override
	public void notifyUserCreated(String userID, int credits) {
		// Notify when a new user is created and update the user model.
		addOrReplaceUser(new UserInfo(userID, credits));
	}
}
