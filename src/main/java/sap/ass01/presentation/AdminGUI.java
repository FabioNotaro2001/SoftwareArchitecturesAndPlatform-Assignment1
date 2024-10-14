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
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.P2d;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.ServerImpl;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.businessLogic.V2d;
import sap.ass01.businessLogic.EBike.EBikeState;
import sap.ass01.persistence.MyRepoPersistence;
import sap.ass01.service.AdminService;
import sap.ass01.service.AdminServiceImpl;
import sap.ass01.service.AppServiceImpl;

public class AdminGUI extends JFrame implements ActionListener, AdminGUICallback {
    private VisualiserPanel centralPanel;
    private JButton addEBikeButton;
	private AdminService adminService;
	private Map<String, UserInfo> users = new HashMap<>();
	private Map<String, EBikeInfo> bikes = new HashMap<>();
	private Map<String, RideInfo> rides = new HashMap<>();
	
	private JList<String> usersList;
	private JList<String> bikesList;
	private JList<String> ridesList;

	private DefaultListModel<String> usersModel;
	private DefaultListModel<String> bikesModel;
	private DefaultListModel<String> ridesModel;

    public AdminGUI(AdminService adminService) throws RemoteException{
		this.adminService = adminService;
        setupModel();
        setupView();
    }
    
    protected void setupModel() throws RemoteException {
        this.adminService.getEBikes().forEach(x -> bikes.put(x.bikeID(), x));
		this.adminService.getUsers().forEach(u -> users.put(u.userID(), u));
		this.adminService.getRides().forEach(r -> rides.put(r.bikeID(), r));
		this.adminService.registerGUI(this);
    }

    protected void setupView() {
        setTitle("ADMIN GUI");        
        setSize(1000,600);
        setResizable(false);
        
        setLayout(new BorderLayout());

		addEBikeButton = new JButton("Add EBike");
		addEBikeButton.addActionListener(this);
		
		JPanel topPanel = new JPanel();		
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

		this.usersModel = getUsersModel();
		this.bikesModel = getBikesModel();
		this.ridesModel = getRidesModel();
		this.usersList = new JList<String>(usersModel);
		this.bikesList = new JList<String>(bikesModel);
		this.ridesList = new JList<String>(ridesModel);

		eastPanel.add(new JScrollPane(usersList));
		eastPanel.add(new JScrollPane(bikesList));
		eastPanel.add(new JScrollPane(ridesList));
		add(eastPanel, BorderLayout.EAST);		
    }

	// TODO: NON FARE SALTI CON USERINFO E RIDEINFO
	private void addRide(RideInfo info){
		rides.put(info.id(), info);
		ridesModel.addElement(info.toString());
	}

	private void removeRide(RideInfo info){
		rides.remove(info.id());
		ridesModel.clear();
		ridesModel.addAll(rides.values().stream().map(RideInfo::toString).toList());
	}

	private void addUser(UserInfo info){
		users.put(info.userID(), info);
		usersModel.addElement(info.toString());
	}

	private void removeUser(UserInfo info){
		users.remove(info.userID());
		usersModel.clear();
		usersModel.addAll(users.values().stream().map(UserInfo::toString).toList());
	}

	private void addEBike(EBikeInfo info){
		bikes.put(info.bikeID(), info);
		bikesModel.addElement(info.toString());
	}

	private void removeEBike(EBikeInfo info){
		bikes.remove(info.bikeID());
		bikesModel.clear();
		bikesModel.addAll(bikes.values().stream().map(EBikeInfo::toString).toList());
	}


	private DefaultListModel<String> getRidesModel() {
		DefaultListModel<String> ridesModel = new DefaultListModel<>();
		ridesModel.addAll(rides.values().stream().map(RideInfo::toString).toList());
		return ridesModel;
	}


	private DefaultListModel<String> getBikesModel() {
		DefaultListModel<String> bikesModel = new DefaultListModel<>();
		bikesModel.addAll(bikes.values().stream().map(EBikeInfo::toString).toList());
		return bikesModel;
	}


	private DefaultListModel<String> getUsersModel() {
		DefaultListModel<String> usersModel = new DefaultListModel<>();
		usersModel.addAll(users.values().stream().map(UserInfo::toString).toList());
		return usersModel;
	}

    public void display() {
    	SwingUtilities.invokeLater(() -> {
    		this.setVisible(true);
    	});
    }
    
    public void refreshView() {
    	centralPanel.refresh();
    }  

    @Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addEBikeButton) {
	        JDialog d = new AddEBikeDialog(this, adminService);
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
            dx = w/2;
            dy = h/2;
            this.app = app;
        }

        public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0,0,this.getWidth(),this.getHeight());
			//g2.drawOval(0, 0, 100, 100);

			app.bikes.values().forEach(b -> {
				var p = b.loc();
    			int x0 = (int)(p.x());
		        int y0 = (int)(p.y());
		        g2.drawOval(x0 ,y0,10,10);
		        g2.drawString(b.bikeID(), x0, y0 + 35);
			});
        }
        
        public void refresh(){
            repaint();
        }
    }
	
	public static void main(String[] args) throws RemoteException, RepositoryException {
		var w = new AdminGUI(new AdminServiceImpl(new AppServiceImpl(new ServerImpl(new MyRepoPersistence()))));
		w.display();
	}


	@Override
	public void notifyBikeStateChanged(String bikeID, String state, double x, double y, int batteryLevel) {
		EBikeInfo eBikeInfo = null;
		var bike = this.bikes.get(bikeID);
		if(bike == null){
			eBikeInfo = new EBikeInfo(bikeID, EBikeState.valueOf(state), new P2d(x, y), new V2d(-1,0), 0, batteryLevel);
		} else {
			eBikeInfo = new EBikeInfo(bikeID, EBikeState.valueOf(state), new P2d(x, y), bike.direction(), y, batteryLevel);
		}
		bikes.put(bikeID, eBikeInfo); 
		addEBike(eBikeInfo);
		centralPanel.refresh();
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
