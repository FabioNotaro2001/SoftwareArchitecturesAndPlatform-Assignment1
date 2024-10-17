package sap.ass01.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.ServerImpl;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.businessLogic.EBike.EBikeState;
import sap.ass01.persistence.MyRepoPersistence;
import sap.ass01.service.AppServiceImpl;
import sap.ass01.service.UserService;
import sap.ass01.service.UserServiceImpl;

public class UserGUI extends JFrame implements ActionListener, UserGUICallback {
    // TODO: ha senso che l'utente possa vedere in tempo reale il suo credito che scende e la batteria della bici che sta usando.

    private UserService userService;
    
    private JButton startRideButton;
    private JButton endRideButton;
    private JLabel userCreditLabel;
    private JTextField creditRechargeTextField;
    private JButton creditRechargeButton;
    
    // Nuovi componenti
    private JButton loginButton;
    private JButton registerUserButton;
    private JComboBox<String> userDropdown;

    // CardLayout per gestire i pannelli
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private UserInfo userConnected;

    private RideInfo launchedRide;

    public UserGUI(UserService userService) throws RemoteException {
        this.userService = userService;
        setupModel();
        setupView();
    }

    protected void setupModel() {
        this.userService.registerGUI(this);
    }

    protected void setupView() throws RemoteException {
        setTitle("USER GUI");        
        setSize(800,300);
        setResizable(false);
        setLayout(new BorderLayout());

        // Inizializzazione di CardLayout e pannelli principali
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Pannello iniziale (selezione utente e registrazione)
        JPanel userSelectionPanel = new JPanel();

        // Menu a tendina con nomi di persone
        userDropdown = new JComboBox<>(new Vector<>(userService.getUsers()));
        loginButton = new JButton("LOGIN");
        registerUserButton = new JButton("NEW USER");

        // Assegna ActionListener
        loginButton.addActionListener(this);
        registerUserButton.addActionListener(this);

        // Aggiungi componenti al pannello di selezione utente
        userSelectionPanel.add(userDropdown);
        userSelectionPanel.add(loginButton);
        userSelectionPanel.add(registerUserButton);

        // Pannello successivo (Start Ride e credito utente)
        JPanel ridePanel = new JPanel();

        startRideButton = new JButton("Start Ride");
        startRideButton.addActionListener(this);

        endRideButton = new JButton("End Ride");
        endRideButton.addActionListener(this);
        endRideButton.setEnabled(false);

        creditRechargeButton = new JButton("RECHARGE");
        creditRechargeButton.addActionListener(this);
        creditRechargeTextField = new JTextField();
        creditRechargeTextField.setColumns(2);

        userCreditLabel = new JLabel("Credit: ");

        // Aggiungi componenti al pannello del ride
        ridePanel.add(startRideButton);
        ridePanel.add(endRideButton);
        ridePanel.add(userCreditLabel);
        ridePanel.add(creditRechargeTextField);
        ridePanel.add(creditRechargeButton);

        // Aggiungi entrambi i pannelli al mainPanel gestito da CardLayout
        mainPanel.add(userSelectionPanel, "UserSelection");
        mainPanel.add(ridePanel, "RidePanel");

        // Mostra inizialmente il pannello di selezione utente
        cardLayout.show(mainPanel, "UserSelection");

        // Aggiungi il mainPanel alla finestra
        add(mainPanel, BorderLayout.CENTER);

        // Gestore per la chiusura della finestra
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(-1);
            }
        });

        pack();
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }
        

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startRideButton) {
            this.startRideButton.setEnabled(false);
            JDialog d;
            try {
                d = new RideDialog(this, this.userConnected.userID(), this.userService);
                d.setVisible(true);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == creditRechargeButton){
            try {
                this.userService.rechargeCredit(this.userConnected.userID(), Integer.parseInt(creditRechargeTextField.getText()));
                userCreditLabel.setText("Credit: " + this.userConnected.credits());
            } catch (NumberFormatException | RemoteException | RepositoryException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == endRideButton){
            try {
                this.userService.endRide(launchedRide.userID(), launchedRide.bikeID());
            } catch (RemoteException | RepositoryException e1) {
                e1.printStackTrace();
            }
            this.startRideButton.setEnabled(true);
            this.endRideButton.setEnabled(false);
        }else if (e.getSource() == loginButton) {
            // Salva l'utente selezionato nella variabile userConnected
            try {
                this.userConnected = this.userService.logAsUser((String) userDropdown.getSelectedItem());
                userCreditLabel.setText("Credit: " + this.userConnected.credits());
                this.pack();
            } catch (RemoteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // Mostra il pannello successivo (Ride Panel) dopo la selezione dell'utente
            cardLayout.show(mainPanel, "RidePanel");
        } else if (e.getSource() == registerUserButton) {
            // Mostra una JDialog per registrare un nuovo utente
            JDialog registerDialog = new JDialog(this, "CREATE NEW USER", true);
            registerDialog.setSize(300, 150);
            registerDialog.setLayout(new GridLayout(3, 1));

            // Aggiungi un campo di testo per l'inserimento del nuovo nome utente
            JTextField newUserField = new JTextField();
            JButton confirmButton = new JButton("REGISTER");

            confirmButton.addActionListener(ev -> {
                UserInfo newUserToBeChecked;
                String newUser = newUserField.getText();
                if (!newUser.isEmpty()) {
                    try {
                        newUserToBeChecked = this.userService.createUser(newUser, 100);
                        if (newUserToBeChecked == null){
                            JOptionPane.showMessageDialog(this, "Error when attempting to create your account", "ERROR", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (RemoteException | RepositoryException e1) {
                        e1.printStackTrace();
                    }

                    userDropdown.addItem(newUser);
                    
                    registerDialog.dispose();
                }
            });

            // Aggiungi i componenti alla dialog
            registerDialog.add(new JLabel("INSERT USER-ID:"));
            registerDialog.add(newUserField);
            registerDialog.add(confirmButton);

            // Mostra la dialog
            registerDialog.setVisible(true);
        }
    }
    
    public static void main(String[] args) throws RepositoryException, RemoteException {
		var w = new UserGUI(new UserServiceImpl(new AppServiceImpl(new ServerImpl(new MyRepoPersistence()))));
        w.display();
	}

	@Override
	public void notifyBikeStateChanged(String bikeID, EBikeState state, double x, double y, int batteryLevel) {
        if (launchedRide == null || launchedRide.bikeID() != bikeID) {
            return;
        }

        switch (state) {
            case AVAILABLE:
                if (userConnected.credits() <= 0) {
                    System.out.println("Ride ended due to lack of credits.");
                }
                this.launchedRide = null;

                this.startRideButton.setEnabled(true);
                this.endRideButton.setEnabled(false);
                break;
            case MAINTENANCE:
                System.out.println("Bike ran out of battery.");
                this.launchedRide = null;
                
                this.startRideButton.setEnabled(true);
                this.endRideButton.setEnabled(false);
                break;
            default:
                break;
        }
	}

	@Override
	public void notifyUserCreditRecharged(String userID, int credits) {
		this.userConnected = new UserInfo(userID, credits);
        this.userCreditLabel.setText("Credits: " + credits);
        pack();
	}

	@Override
	public void notifyRideStepDone(String rideId, double x, double y, int batteryLevel, int userCredits) {
		System.out.println("Bike moving, pos: (" + x + ", " + y + "), battery level: " + batteryLevel + ", credits left: " + userCredits);
        this.userConnected = new UserInfo(this.userConnected.userID(), userCredits);
        this.userCreditLabel.setText("Credits: " + userCredits);
        pack();
	}

    public void setLaunchedRide(RideInfo newRide){
        this.launchedRide = newRide;
        this.endRideButton.setEnabled(true);
    }
}
