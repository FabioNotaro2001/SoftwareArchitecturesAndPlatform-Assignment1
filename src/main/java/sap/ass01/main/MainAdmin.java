package sap.ass01.main;

import sap.ass01.businessLogic.AdminCallback;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.presentation.AdminGUI;
import sap.ass01.service.AdminAppService;
import sap.ass01.service.AdminService;
import sap.ass01.service.AdminServiceImpl;
import sap.ass01.service.AppServiceImpl;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

// Main class for launching the Admin interface of the application.
public class MainAdmin {
  
  // Private constructor to prevent instantiation.
  private MainAdmin() {
  }

  // Entry point for the Admin application.
  public static void main(String[] args) throws RemoteException, RepositoryException, NotBoundException {
    // Obtain the RMI registry.
    Registry registry = LocateRegistry.getRegistry();

    // Lookup the AdminAppService in the RMI registry.
    AdminAppService adminAppService = (AdminAppService) registry.lookup(AppServiceImpl.ADMIN_SERVER_NAME);
    
    // Create an instance of AdminService using the retrieved AdminAppService.
    AdminService adminService = new AdminServiceImpl(adminAppService); 

    // Export the AdminService object for remote calls.
    AdminCallback adminServiceStub = (AdminCallback) UnicastRemoteObject.exportObject((AdminCallback) adminService, 0);

    // Register the Admin service with the AdminAppService.
    adminAppService.registerAdmin(adminServiceStub);

    // Initialize the Admin GUI and display it.
    var w = new AdminGUI(adminService);
    w.display();
  }
}
