package sap.ass01.layered.service;

import sap.ass01.layered.businessLogic.EBikeInfo;
import sap.ass01.layered.businessLogic.P2d;
import sap.ass01.layered.businessLogic.RepositoryException;
import sap.ass01.layered.businessLogic.RideInfo;
import sap.ass01.layered.businessLogic.Server;
import sap.ass01.layered.businessLogic.UserInfo;
import sap.ass01.layered.businessLogic.V2d;
import sap.ass01.layered.businessLogic.EBike.EBikeState;

/**
 * Thread responsible for managing the ride simulation of an e-bike.
 * It updates the bike's location, battery level, and user credits
 * while handling interactions with the server.
 */
public class RideThread extends Thread {
    private RideThreadObserver observer; // Observer to notify about ride updates
    private Server serverBL; // Business logic server for e-bikes and users
    private RideInfo rideInfo; // Information about the current ride
    private boolean ongoing; // Flag to indicate if the ride is ongoing

    /**
     * Constructs a new RideThread.
     * 
     * @param observer The observer to notify about ride events.
     * @param serverBL The server to interact with the business logic.
     * @param ride     The ride information.
     */
    public RideThread(RideThreadObserver observer, Server serverBL, RideInfo ride) {
        this.observer = observer; // Assigning the observer
        this.serverBL = serverBL; // Assigning the server
        this.rideInfo = ride; // Assigning the ride information
    }

    /**
     * The main method that runs the ride process in a separate thread.
     */
    @Override
    public void run() {
        // Retrieve the bike and user information from the server.
        EBikeInfo bikeInfo = serverBL.getEBikeByID(rideInfo.bikeID());
        UserInfo userInfo = serverBL.getUserByID(rideInfo.userID());
        this.ongoing = true; // Start the ride process.

        // Variables to track time for various actions.
        var lastTimeDecreasedCredit = System.currentTimeMillis();
        var lastTimeChangedDir = System.currentTimeMillis();
        var lastTimeBatteryDecreased = System.currentTimeMillis();

        // Ride loop continues while bike has battery and user has credits.
        while (bikeInfo.batteryLevel() > 0 && userInfo.credits() > 0) {
            synchronized (this) {
                // Check if the ride is ongoing; if not, end the ride.
                if (!ongoing) {
                    try {
                        bikeInfo = serverBL.getEBikeByID(rideInfo.bikeID());
                        serverBL.updateEBike(new EBikeInfo(bikeInfo.bikeID(), bikeInfo.state(), bikeInfo.loc(), bikeInfo.direction(), 0.0, bikeInfo.batteryLevel()));
                    } catch (IllegalArgumentException | RepositoryException e) {
                        e.printStackTrace(); // Handle exceptions.
                    }
                    return; // Exit if the ride is no longer ongoing.
                }
            }

            // Get current location and direction of the bike.
            var l = bikeInfo.loc();
            var d = bikeInfo.direction();
            var s = 1; // Speed of the bike

            // Calculate new location based on direction.
            var newLoc = l.sum(d.mul(s));
            var newDir = d; // Keep current direction.
            var newBatteryLevel = bikeInfo.batteryLevel(); // Battery level.
            var newUserCredits = userInfo.credits(); // User credits.

            // Handle boundary conditions for bike's location.
            if (newLoc.x() > 200 || newLoc.x() < -200) {
                newDir = new V2d(-d.x(), d.y()); // Reverse direction on x-axis.
                if (newLoc.x() > 200) {
                    newLoc = new P2d(200, l.y()); // Set location at right boundary.
                } else {
                    newLoc = new P2d(-200, l.y()); // Set location at left boundary.
                }
            }
            if (newLoc.y() > 200 || newLoc.y() < -200) {
                newDir = new V2d(d.x(), -d.y()); // Reverse direction on y-axis.
                if (newLoc.y() > 200) {
                    newLoc = new P2d(l.x(), 200); // Set location at upper boundary.
                } else {
                    newLoc = new P2d(l.x(), -200); // Set location at lower boundary.
                }
            }

            // Change direction randomly every 500 milliseconds.
            var elapsedTimeSinceLastChangeDir = System.currentTimeMillis() - lastTimeChangedDir;
            if (elapsedTimeSinceLastChangeDir > 500) {
                double angle = Math.random() * 60 - 30; // Random angle between -30 and 30 degrees.
                newDir = d.rotate(angle); // Rotate direction.
                lastTimeChangedDir = System.currentTimeMillis(); // Reset timer.
            }

            // Update user credits every 2000 milliseconds.
            var elapsedTimeSinceLastDecredit = System.currentTimeMillis() - lastTimeDecreasedCredit;
            if (elapsedTimeSinceLastDecredit > 2000) {
                try {
                    serverBL.decreaseCredits(userInfo.userID(), 1); // Decrease user credits.
                    newUserCredits--; // Update local credits variable
                } catch (IllegalArgumentException | RepositoryException e) {
                    e.printStackTrace(); 
                    break; // Exit if an error occurs
                }
                lastTimeDecreasedCredit = System.currentTimeMillis(); // Reset timer.
            }

            // Decrease battery level every 1500 milliseconds.
            var elapsedTimeSinceLastBatteryDecreased = System.currentTimeMillis() - lastTimeBatteryDecreased;
            if (elapsedTimeSinceLastBatteryDecreased > 1500) {
                newBatteryLevel--; 
                lastTimeBatteryDecreased = System.currentTimeMillis(); // Reset timer.
            }

            // Notify observer about the current ride status.
            this.observer.rideStepDone(bikeInfo.bikeID(), rideInfo.rideId(), newLoc, newBatteryLevel, newUserCredits);

            try {
                // Update bike information on the server
                serverBL.updateEBike(new EBikeInfo(bikeInfo.bikeID(), bikeInfo.state(), newLoc, newDir, 1.0, newBatteryLevel));
            } catch (IllegalArgumentException | RepositoryException e) {
                e.printStackTrace(); 
                break; // Exit on error.
            }

            // Sleep for a short duration to simulate time passing in the ride.
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
                ex.printStackTrace(); 
                break; 
            }

            // Refresh bike and user information for the next iteration.
            bikeInfo = serverBL.getEBikeByID(rideInfo.bikeID());
            userInfo = serverBL.getUserByID(rideInfo.userID());
        }

        // Update bike state upon ending the ride.
        try {
            serverBL.updateEBike(new EBikeInfo(bikeInfo.bikeID(), bikeInfo.batteryLevel() > 0 ? EBikeState.AVAILABLE : EBikeState.MAINTENANCE, bikeInfo.loc(), bikeInfo.direction(), 0.0, bikeInfo.batteryLevel()));
        } catch (IllegalArgumentException | RepositoryException e) {
            e.printStackTrace(); 
        }

        // End the ride in the server.
        try {
            serverBL.endRide(userInfo.userID(), bikeInfo.bikeID());
        } catch (IllegalArgumentException | RepositoryException e) {
            e.printStackTrace(); 
        }

        // Notify observer that the ride has ended.
        observer.rideEnded(rideInfo.rideId(), rideInfo.userID(), bikeInfo.bikeID(), bikeInfo.batteryLevel() > 0 ? EBikeState.AVAILABLE : EBikeState.MAINTENANCE, 
                                    bikeInfo.loc().x(), bikeInfo.loc().y(), bikeInfo.batteryLevel());
    }

    /**
     * Method to stop the ride.
     * This method can be called to end the ongoing ride.
     */
    public synchronized void endRide() {
        this.ongoing = false; 
    }
}
