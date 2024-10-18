package sap.ass01.businessLogic;

import java.io.Serializable;

/**
 * Represents user information in the system.
 * This record holds the user ID and the number of credits available to the user.
 * Records provide a concise way to create immutable data classes in Java.
 */
public record UserInfo(String userID, int credits) implements Serializable {
    
}
