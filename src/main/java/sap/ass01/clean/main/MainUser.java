package sap.ass01.clean.main;

import sap.ass01.clean.domain.RepositoryException;
import sap.ass01.clean.infrastructure.gui.UserGUI;
import sap.ass01.clean.infrastructure.service.AppServiceImpl;
import sap.ass01.clean.infrastructure.service.UserAppService;
import sap.ass01.clean.infrastructure.service.UserService;
import sap.ass01.clean.infrastructure.service.UserServiceImpl;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MainUser {
    // Private constructor to prevent instantiation.
    private MainUser() {
    }

    public static void main(String[] args) throws RepositoryException, RemoteException, NotBoundException {
        // Locate the RMI registry.
        Registry registry = LocateRegistry.getRegistry();

        // Look up the UserAppService from the RMI registry.
        UserAppService userAppService = (UserAppService) registry.lookup(AppServiceImpl.USER_SERVER_NAME);

        // Create an instance of UserServiceImpl which implements the business logic.
        UserService userService = new UserServiceImpl(userAppService);

        // Export the userService object for RMI calls.
        UnicastRemoteObject.exportObject(userService, 0);

        // Initialize the User GUI with the UserService.
        var w = new UserGUI(userService);
        
        // Display the User GUI.
        w.display();
    }
}
