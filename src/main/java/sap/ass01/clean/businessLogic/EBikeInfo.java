package sap.ass01.clean.businessLogic;

import java.io.Serializable;
import sap.ass01.clean.businessLogic.EBike.EBikeState; 

/**
 * A record class that encapsulates information about an electric bike (ebike).
 * This class is used to transfer data related to an ebike in a structured manner.
 */
public record EBikeInfo(
    String bikeID,         // The unique identifier for the ebike.
    EBikeState state,     // The current state of the ebike (e.g., available, in use, etc.).
    P2d loc,              // The current location of the ebike represented as a 2D point.
    V2d direction,        // The direction in which the ebike is facing, represented as a 2D vector.
    double speed,         // The current speed of the ebike.
    int batteryLevel      // The current battery level of the ebike as a percentage.
) implements Serializable {
    
}
