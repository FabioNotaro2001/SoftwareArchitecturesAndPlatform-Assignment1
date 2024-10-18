package sap.ass01.main;

import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.ServerImpl;
import sap.ass01.persistence.MyRepoPersistence;
import sap.ass01.presentation.AdminGUI;
import sap.ass01.service.AdminAppService;
import sap.ass01.service.AdminCallback;
import sap.ass01.service.AdminService;
import sap.ass01.service.AdminServiceImpl;
import sap.ass01.service.AppServiceImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class MainAdmin {
  private MainAdmin() {
  }

  public static void main(String[] args) throws RemoteException, RepositoryException, NotBoundException {
    Registry registry = LocateRegistry.getRegistry();
    AdminAppService adminAppService = (AdminAppService) registry.lookup(AppServiceImpl.ADMIN_SERVER_NAME);
    
    AdminService adminService = new AdminServiceImpl(adminAppService); 

    AdminCallback adminServiceStub = (AdminCallback) UnicastRemoteObject.exportObject((AdminCallback) adminService, 0);

    adminAppService.registerAdmin(adminServiceStub);

    var w = new AdminGUI(adminService);
    w.display();
  }
}
