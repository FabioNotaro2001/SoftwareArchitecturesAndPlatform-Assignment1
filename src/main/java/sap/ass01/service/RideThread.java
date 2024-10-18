package sap.ass01.service;

import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.P2d;
import sap.ass01.businessLogic.RepositoryException;
import sap.ass01.businessLogic.RideInfo;
import sap.ass01.businessLogic.Server;
import sap.ass01.businessLogic.UserInfo;
import sap.ass01.businessLogic.V2d;
import sap.ass01.businessLogic.EBike.EBikeState;

public class RideThread extends Thread {
    RideThreadObserver observer;
    Server serverBL;
    RideInfo rideInfo;
    boolean ongoing;

    public RideThread(RideThreadObserver observer, Server serverBL, RideInfo ride) {
        this.observer = observer;
        this.serverBL = serverBL;
        this.rideInfo = ride;
    }

    @Override
    public void run() {
        EBikeInfo bikeInfo = serverBL.getEBikeByID(rideInfo.bikeID());
        UserInfo userInfo = serverBL.getUserByID(rideInfo.userID());
        this.ongoing = true;

        var lastTimeDecreasedCredit = System.currentTimeMillis();
		var lastTimeChangedDir = System.currentTimeMillis();
        var lastTimeBatteryDecreased = System.currentTimeMillis();

        while (bikeInfo.batteryLevel() > 0 && userInfo.credits() > 0) {
            synchronized (this) {
                if (!ongoing) {
                    try {
                        bikeInfo = serverBL.getEBikeByID(rideInfo.bikeID());
                        serverBL.updateEBike(new EBikeInfo(bikeInfo.bikeID(), bikeInfo.state(), bikeInfo.loc(), bikeInfo.direction(), 0.0, bikeInfo.batteryLevel()));
                    } catch (IllegalArgumentException | RepositoryException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            var l = bikeInfo.loc();
			var d = bikeInfo.direction();
			var s = 1;

            var newLoc = l.sum(d.mul(s));
            var newDir = d;
            var newBatteryLevel = bikeInfo.batteryLevel();
            var newUserCredits = userInfo.credits();

			if (newLoc.x() > 200 || newLoc.x() < -200) {
				newDir = new V2d(-d.x(), d.y());
				if (newLoc.x() > 200) {
					newLoc = new P2d(200, l.y()); // FIXME: dubbio
				} else {
					newLoc = new P2d(-200, l.y());  // FIXME: dubbio
				}
			};
			if (newLoc.y() > 200 || newLoc.y() < -200) {
				newDir = new V2d(d.x(), -d.y());
				if (newLoc.y() > 200) {
					newLoc = new P2d(l.x(), 200);   // FIXME: dubbio
				} else {
					newLoc = new P2d(l.x(), -200);  // FIXME: dubbio
				}
			};
			
			/* change dir randomly */
			
			var elapsedTimeSinceLastChangeDir = System.currentTimeMillis() - lastTimeChangedDir;
			if (elapsedTimeSinceLastChangeDir > 500) {
				double angle = Math.random()*60 - 30;
				newDir = d.rotate(angle);
				elapsedTimeSinceLastChangeDir = System.currentTimeMillis();
			}
			
			
			/* update credit */
			
			var elapsedTimeSinceLastDecredit = System.currentTimeMillis() - lastTimeDecreasedCredit;
			if (elapsedTimeSinceLastDecredit > 2000) {
                try {
                    serverBL.decreaseCredits(userInfo.userID(), 1);
                    newUserCredits--;
                } catch (IllegalArgumentException | RepositoryException e) {
                    e.printStackTrace();
                    break;
                }
				lastTimeDecreasedCredit = System.currentTimeMillis();
			}

            /* Decrease battery level */
            var elapsedTimeSinceLastBatteryDecreased = System.currentTimeMillis() - lastTimeBatteryDecreased;
            if (elapsedTimeSinceLastBatteryDecreased > 1500) {
                newBatteryLevel--;
                lastTimeBatteryDecreased = System.currentTimeMillis();
            }

            /* send notifications */
            this.observer.rideStepDone(bikeInfo.bikeID(), rideInfo.rideId(), newLoc, newBatteryLevel, newUserCredits);

            try {
                serverBL.updateEBike(new EBikeInfo(bikeInfo.bikeID(), bikeInfo.state(), newLoc, newDir, 1.0, newBatteryLevel));
            } catch (IllegalArgumentException | RepositoryException e) {
                e.printStackTrace();
                break;
            }

			try {
				Thread.sleep(500);
			} catch (Exception ex) {
                ex.printStackTrace();
                break;                
            }

            bikeInfo = serverBL.getEBikeByID(rideInfo.bikeID());
            userInfo = serverBL.getUserByID(rideInfo.userID());
        }

        try {
            serverBL.updateEBike(new EBikeInfo(bikeInfo.bikeID(), bikeInfo.batteryLevel() > 0 ? EBikeState.AVAILABLE : EBikeState.MAINTENANCE, bikeInfo.loc(), bikeInfo.direction(), 0.0, bikeInfo.batteryLevel()));
        } catch (IllegalArgumentException | RepositoryException e) {
            e.printStackTrace();
        }

        try {
            serverBL.endRide(userInfo.userID(), bikeInfo.bikeID());
        } catch (IllegalArgumentException | RepositoryException e) {
            e.printStackTrace();
        }
        observer.bikeStateChanged(bikeInfo.bikeID(), bikeInfo.batteryLevel() > 0 ? EBikeState.AVAILABLE : EBikeState.MAINTENANCE, bikeInfo.loc().x(), bikeInfo.loc().y(), bikeInfo.batteryLevel());
    }

    public synchronized void endRide() {
        this.ongoing = false;
    }
}
