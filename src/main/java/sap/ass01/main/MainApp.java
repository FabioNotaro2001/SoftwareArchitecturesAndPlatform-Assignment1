package sap.ass01.main;

import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.ServerImpl;
import sap.ass01.persistence.MyRepoPersistence;
import sap.ass01.service.AppService;
import sap.ass01.service.AppServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;


public class MainApp {

    private MainApp(){
    }

    public static void main(String[] args) throws RepositoryException {
        try {
            ServerImpl server = new ServerImpl(new MyRepoPersistence());
            AppService service = new AppServiceImpl(server);

            // Esporta l'oggetto server per renderlo disponibile alle chiamate remote.
            AppService appServiceStub = (AppService) UnicastRemoteObject.exportObject(service, 0);
            // Ottiene il registro RMI.
            Registry registry = LocateRegistry.getRegistry();

            // Associa (rebind) il nome del server al serverStub nel registro RMI.
            registry.rebind(AppServiceImpl.USER_SERVER_NAME, appServiceStub);
            registry.rebind(AppServiceImpl.ADMIN_SERVER_NAME, appServiceStub);
            System.out.println("Server created.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }   
}
