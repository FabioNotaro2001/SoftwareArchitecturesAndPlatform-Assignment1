package sap.ass01.businessLogic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class ClientHandlerThread extends Thread {
    private Socket socket;
    private RepositoryInterface repo;

    public ClientHandlerThread(Socket clientSocket, RepositoryInterface repo) { // TODO: aggiunta parametro monitor
        this.socket = clientSocket;
        this.repo = repo;
    }

    public void run() {
        DataInputStream in;
        try {
            in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        DataOutputStream out;
        try {
            out = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String line = "";

        Gson gson = new Gson();

        while (true)
        {
            try
            {
                line = in.readUTF();
                var req = gson.fromJson(line, ApplicationRequest.class);
                
                switch (req.command()) {
                    case GETBIKES: {
                        var bikes = repo.getEBikes();
                        var json = gson.toJson(ApplicationResponseCommand.BIKES.toApplicationResponse(bikes));
                        out.writeUTF(json);
                        out.flush();

                        break;
                    }
                    case GETUSERS: {
                        var users = repo.getUsers();
                        var json = gson.toJson(ApplicationResponseCommand.USERS.toApplicationResponse(users));
                        out.writeUTF(json);
                        out.flush();

                        break;
                    }
                    case BEGINRIDE: {
                        var userID = (String)req.arguments().get(0);
                        var bikeID = (String)req.arguments().get(1);

                        var userOpt = repo.getUserByID(userID);
                        var bikeOpt = repo.getEBikeByID(bikeID);

                        if (userOpt.isEmpty() || bikeOpt.isEmpty() //||
                                //rides.stream().filter(r -> r.getUser().getId() == userID).findAny().isPresent() || 
                                /*rides.stream().filter(r -> r.getEBike().getId() == bikeID).findAny().isPresent()*/) {
                            var json = gson.toJson(ApplicationResponseCommand.FAIL.toApplicationResponse(List.of()));
                            out.writeUTF(json);
                            out.flush();
                            break;
                        }

                        // rideCounter++; 
                        // var ride = new Ride(String.valueOf(rideCounter), userOpt.get(), bikeOpt.get());
                        // rides.add(ride);

                        var json = gson.toJson(ApplicationResponseCommand.SUCCESS.toApplicationResponse(List.of()));
                        out.writeUTF(json);
                        out.flush();
                        
                        // TODO: thread o qualcosa che esegue la simulazione della Ride, inviando periodicamente a qualsiasi admin le informazioni sulle ride

                        break;
                    }
                    case ENDRIDE: {
                        
                        break;
                    }
                    case ADDCREDITS: {

                        break;
                    }
                    default: {

                        break;
                    }
                    
                }
            }
            catch(IllegalArgumentException | RepositoryException e) {
                System.out.println(e);
            }
            catch(IOException i)
            {
                System.out.println(i);
                break;
            }
        }
    }
}
