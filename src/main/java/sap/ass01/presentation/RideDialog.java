package sap.ass01.presentation;

import javax.swing.*;
import sap.ass01.service.UserService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

/**
 * Adapted from AddEBikeDialog
 * 
 */
public class RideDialog extends JDialog {
    private JComboBox<String> bikesComboBox;
    private JTextField userName;
    private JButton startButton;
    private JButton cancelButton;
    private UserGUI fatherUserGUI;
    private String userRiding;
    private List<String> availableBikes;
    private String bikeSelectedID;
    private UserService userService;

    public RideDialog(UserGUI fatherUserGUI, UserService userService) throws RemoteException {
        super(fatherUserGUI, "Start Riding an EBike", true);
        this.userService = userService;
        this.availableBikes = this.userService.getAvailableBikes().stream().map(b -> b.bikeID()).toList();
        initializeComponents();
        setupLayout();
        pack();
        addEventHandlers();
        setLocationRelativeTo(fatherUserGUI);
        this.fatherUserGUI = fatherUserGUI;
    }

    private void initializeComponents() {
        bikesComboBox = new JComboBox<String>(new Vector<>(this.availableBikes));
        userName = new JTextField(15);
        startButton = new JButton("Start Riding");
        cancelButton = new JButton("Cancel");
    }

    private void setupLayout() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("E-Bike to ride:"));
        inputPanel.add(bikesComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventHandlers() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bikeSelectedID =  bikesComboBox.getSelectedItem().toString();
	            userRiding = userName.getText();
	            cancelButton.setEnabled(false);
                try {
                    fatherUserGUI.setLaunchedRide(userService.beginRide(userRiding, bikeSelectedID));
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
	            dispose();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
