package sap.ass01.presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class UserGUI extends JFrame implements ActionListener, UserGUICallback {
    // TODO: ha senso che l'utente possa vedere in tempo reale il suo credito che scende e la batteria della bici che sta usando.
	private JButton startRideButton;
	private JButton endRideButton;
    private JLabel userCreditLabel;
	private JTextField creditRechargeTextField;
	private JButton creditRechargeButton;
    
    // Nuovi componenti
    private JButton userRegisteredButton;
    private JButton registerUserButton;
    private JComboBox<String> userDropdown;
    
    // Variabile per memorizzare l'utente selezionato
    private String userConnected;

    // CardLayout per gestire i pannelli
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public UserGUI(){
        setup();
    }

    protected void setup() {
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
        userDropdown = new JComboBox<>(new String[] {"Mario Rossi", "Luca Bianchi", "Giulia Verdi", "Anna Neri"});
        userRegisteredButton = new JButton("Utente Registrato");
        registerUserButton = new JButton("Registra Utente");

        // Assegna ActionListener
        userRegisteredButton.addActionListener(this);
        registerUserButton.addActionListener(this);

        // Aggiungi componenti al pannello di selezione utente
        userSelectionPanel.add(userDropdown);
        userSelectionPanel.add(userRegisteredButton);
        userSelectionPanel.add(registerUserButton);

        // Pannello successivo (Start Ride e credito utente)
        JPanel ridePanel = new JPanel();

        startRideButton = new JButton("Start Ride");
        startRideButton.addActionListener(this);

		endRideButton = new JButton("End Ride");
		endRideButton.addActionListener(this);
		endRideButton.setEnabled(false);

		creditRechargeButton = new JButton("RICARICA");
		creditRechargeButton.addActionListener(this);
		creditRechargeTextField = new JTextField();
		creditRechargeTextField.setColumns(2);

        userCreditLabel = new JLabel("Credit: 10000000000000000");

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
        if (e.getSource() == startRideButton) {
			this.endRideButton.setEnabled(true);
			this.startRideButton.setEnabled(false);
            JDialog d = new RideDialog(this);
            d.setVisible(true);
        } else if (e.getSource() == creditRechargeButton){
			System.out.println(creditRechargeTextField.getText());
		}else if (e.getSource() == endRideButton){
			this.startRideButton.setEnabled(true);
			this.endRideButton.setEnabled(false);
			System.out.println("La corsa è finita andate in pace!");
		}else if (e.getSource() == userRegisteredButton) {
            // Salva l'utente selezionato nella variabile userConnected
            userConnected = (String) userDropdown.getSelectedItem();
            System.out.println("Utente connesso: " + userConnected);

            // Mostra il pannello successivo (Ride Panel) dopo la selezione dell'utente
            cardLayout.show(mainPanel, "RidePanel");
        } else if (e.getSource() == registerUserButton) {
            // Mostra una JDialog per registrare un nuovo utente
            JDialog registerDialog = new JDialog(this, "Registra Nuovo Utente", true);
            registerDialog.setSize(300, 150);
            registerDialog.setLayout(new BorderLayout());

            // Aggiungi un campo di testo per l'inserimento del nuovo nome utente
            JTextField newUserField = new JTextField();
            JButton confirmButton = new JButton("Conferma");

            confirmButton.addActionListener(ev -> {
                String newUser = newUserField.getText();
                if (!newUser.isEmpty()) {
                    userDropdown.addItem(newUser);
                    registerDialog.dispose();
                    System.out.println("Nuovo utente registrato: " + newUser);
                }
            });

            // Aggiungi i componenti alla dialog
            registerDialog.add(new JLabel("Inserisci nome utente:"), BorderLayout.NORTH);
            registerDialog.add(newUserField, BorderLayout.CENTER);
            registerDialog.add(confirmButton, BorderLayout.SOUTH);

            // Mostra la dialog
            registerDialog.setVisible(true);
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
	public void notifyBikeStateChanged(String bikeID, String state, double x, double y, int batteryLevel) {
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
