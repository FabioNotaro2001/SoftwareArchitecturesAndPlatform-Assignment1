package sap.ass01.businessLogic;

import java.rmi.Remote;

public interface BikeUpdateCallback extends Remote {
    void notifyClient(String bikeID, EBike.EBikeState newState, int batteryLevel);
}

// FIXME: unica interfaccia admin per aggiunta di bici e utenti, aggiornamenti stato bici, aggiornamenti stato utente e aggiornamenti stato ride

// FIXME: interfaccia utente per aggiornamenti stato bici, aggiornamenti stato utente e stato sua ride