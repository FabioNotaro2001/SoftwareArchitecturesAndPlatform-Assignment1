package sap.ass01.service;

// TODO: probabilmente ha bisogno di un altro nome perchè dovrà fare diverse cose, tra cui avere metodi sia per i callback delle bici, cghe degliutenti aggiunti ecc e deve implementare RideCallback.
public interface AdminCallback extends UserCallback {
    void notifyUserCreated(String userID, int credits);
}

// FIXME: unica interfaccia admin per aggiunta di bici e utenti, aggiornamenti stato bici, aggiornamenti stato utente e aggiornamenti stato ride

// FIXME: interfaccia utente per aggiornamenti stato bici, aggiornamenti stato utente e stato sua ride