package sap.ass01.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.UserInfo;

public interface AppService extends Remote {
    List<UserInfo> getUsers() throws RemoteException;
    
    List<EBikeInfo> getEBikes() throws RemoteException;
}
