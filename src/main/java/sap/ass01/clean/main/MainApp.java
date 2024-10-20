package sap.ass01.clean.main;

import sap.ass01.clean.domain.RepositoryException;
import sap.ass01.clean.domain.ServerImpl;
import sap.ass01.clean.infrastructure.repo.MyRepoAdapter;
import sap.ass01.clean.infrastructure.service.AppService;
import sap.ass01.clean.infrastructure.service.AppServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

// Main application class for starting the server.
public class MainApp {

    // Private constructor to prevent instantiation.
    private MainApp() {
    }

    // Entry point of the application. Remember to run rmiregistry on /target/classes first!!!!!!
    public static void main(String[] args) throws RepositoryException {
        try {
            // Initialize the server with the repository persistence implementation.
            ServerImpl server = new ServerImpl(new MyRepoAdapter());

            // Create the application service using the server instance.
            AppService service = new AppServiceImpl(server);

            // Export the service object for remote method calls.
            AppService appServiceStub = (AppService) UnicastRemoteObject.exportObject(service, 0);
            
            // Get the RMI registry.
            Registry registry = LocateRegistry.getRegistry();

            // Rebind the service name to the exported service object in the RMI registry.
            registry.rebind(AppServiceImpl.USER_SERVER_NAME, appServiceStub);
            registry.rebind(AppServiceImpl.ADMIN_SERVER_NAME, appServiceStub);

            // Log message indicating that the server is created and ready.
            System.out.println("Server created.");
        } catch (RemoteException e) {
            // Handle exceptions related to remote method calls.
            e.printStackTrace();
        }
    }   
}
