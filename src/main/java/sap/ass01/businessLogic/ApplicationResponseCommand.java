package sap.ass01.businessLogic;

import java.util.List;

public enum ApplicationResponseCommand {
    USERS("USERS", -1),                         // list of users
    BIKES("BIKES", -1),                         // list of bikes
    // User-only responses
    RIDEINFO("RIDEINFO", -1),                   // list of ride info       TODO: include la data di inizio di ogni ride?
    SUCCESS("SUCCESS", 0),
    FAIL("FAIL", 0),
    // Admin-only responses
    RIDEENDED("ENDRIDE", 1),            // bike id          TODO: data di fine?
    ADDEDCREDITS("ADDEDCREDITS", 2);    // user id + 
    
    private final String commandName;
    private final int argsNum;

    private ApplicationResponseCommand(String commandName, int argsNum) {
        this.commandName = commandName;
        this.argsNum = argsNum;
    }

    public String commandName() { return this.commandName; }

    public int argsNum() { return this.argsNum; }

    public ApplicationResponse toApplicationResponse(List<?> arguments) {
        return new ApplicationResponse(this, arguments);
    }
}
