package sap.ass01.main;

import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.ServerImpl;
import sap.ass01.persistence.MyRepoPersistence;
import sap.ass01.presentation.UserGUI;
import sap.ass01.service.AdminAppService;
import sap.ass01.service.AdminCallback;
import sap.ass01.service.AdminService;
import sap.ass01.service.AdminServiceImpl;
import sap.ass01.service.AppServiceImpl;
import sap.ass01.service.UserAppService;
import sap.ass01.service.UserCallback;
import sap.ass01.service.UserService;
import sap.ass01.service.UserServiceImpl;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MainUser {
	private MainUser() {
	}

	public static void main(String[] args) throws RepositoryException, RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry();
		UserAppService userAppService = (UserAppService) registry.lookup(AppServiceImpl.USER_SERVER_NAME);

		UserService userService = new UserServiceImpl(userAppService);

		UnicastRemoteObject.exportObject(userService, 0);

		var w = new UserGUI(userService);
		w.display();
	}
}
