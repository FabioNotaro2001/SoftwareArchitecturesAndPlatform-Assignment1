package sap.ass01.presentation;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import sap.ass01.bbom.AddEBikeDialog;
import sap.ass01.bbom.AddUserDialog;
import sap.ass01.businessLogic.EBike;
import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.P2d;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.User;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.service.AdminService;

public class AdminGUI extends JFrame implements ActionListener, AdminGUICallback {
    private VisualiserPanel centralPanel;
    private JButton addUserButton, addEBikeButton;
	private AdminService adminService;
	private Map<String, UserInfo> users = new HashMap<>();
	private Map<String, EBikeInfo> bikes = new HashMap<>();
	private Map<String, RideInfo> rides = new HashMap<>();
	
	private JList<String> usersList;
	private JList<String> bikesList;
	private JList<String> ridesList;

    public AdminGUI(AdminService adminService){
        setupView();
        setupModel();
		this.adminService = adminService;
    }

    
    protected void setupModel() {
        this.addUser("u1");
        this.addEBike("b1", new P2d(0,0));
    }

    protected void setupView() {
        setTitle("ADMIN GUI");        
        setSize(1000,600);
        setResizable(false);
        
        setLayout(new BorderLayout());

		addUserButton = new JButton("Add User");
		addUserButton.addActionListener(this);

		addEBikeButton = new JButton("Add EBike");
		addEBikeButton.addActionListener(this);
		
		JPanel topPanel = new JPanel();
		topPanel.add(addUserButton);		
		topPanel.add(addEBikeButton);	
	    add(topPanel,BorderLayout.NORTH);

        centralPanel = new VisualiserPanel(800,500,this);
	    add(centralPanel,BorderLayout.CENTER);
	    	    		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}
		});

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(3, 1));


		DefaultListModel<String> usersModel = new DefaultListModel<>();
		usersModel.addAll(users.values().stream().map(UserInfo::toString).toList());
		DefaultListModel<String> bikesModel = new DefaultListModel<>();
		bikesModel.addAll(bikes.values().stream().map(EBikeInfo::toString).toList());
		DefaultListModel<String> ridesModel = new DefaultListModel<>();
		ridesModel.addAll(rides.values().stream().map(RideInfo::toString).toList());
		this.usersList = new JList<String>(usersModel);
		this.bikesList = new JList<String>(bikesModel);
		this.ridesList = new JList<String>(ridesModel);

		eastPanel.add(new JScrollPane(usersList));
		eastPanel.add(new JScrollPane(bikesList));
		eastPanel.add(new JScrollPane(ridesList));
		add(eastPanel, BorderLayout.EAST);		
    }

    public void display() {
    	SwingUtilities.invokeLater(() -> {
    		this.setVisible(true);
    	});
    }
        
    public void addEBike(String id, P2d loc) {
    	EBike bike = new EBike(id);
    	bike.updateLocation(loc);
    	log("added new EBike " + bike);
    	centralPanel.refresh();
    }

    public void addUser(String id) {
    	User user = new User(id);
    	user.rechargeCredit(100);
    	log("added new User " + user);
    	centralPanel.refresh();
    }
    
    public void refreshView() {
    	centralPanel.refresh();
    }  

    @Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addEBikeButton) {
	        JDialog d = new AddEBikeDialog(this);
	        d.setVisible(true);
        } else if (e.getSource() == this.addUserButton) {
		    JDialog d = new AddUserDialog(this);
		    d.setVisible(true);
        }
	}

	private void log(String msg) {
		System.out.println("[EBikeApp] " + msg);
	}
    
    public static class VisualiserPanel extends JPanel {
        private long dx;
        private long dy;
        private AdminGUI app;
        
        public VisualiserPanel(int w, int h, AdminGUI app){
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
            this.app = app;
        }

        public void paint(Graphics g){
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    		          RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
    		          RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0,0,this.getWidth(),this.getHeight());
        }
        
        public void refresh(){
            repaint();
        }
    }

	
	
	public static void main(String[] args) {
		var w = new AdminGUI(null);
		w.display();
	}


	@Override
	public void notifyBikeStateChanged(String bikeID, String state, int batteryLevel) {

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


	@Override
	public void notifyUserCreated(String userID, int credits) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyUserCreated'");
	}
	
}
