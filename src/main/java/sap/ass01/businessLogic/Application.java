package sap.ass01.businessLogic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private ServerSocket serverSocket;
    
    public Application(int port, RepositoryInterface repo) throws UnknownHostException, IOException {
        this.serverSocket = new ServerSocket(port);
    
        int rideCounter = 0;
        List<Ride> rides = new ArrayList<>();
        
        // TODO: monitor per rideCounter e rides, da condividere con i thread

        while(true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                new ClientHandlerThread(socket, repo).start();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
        }
        
    }
}
