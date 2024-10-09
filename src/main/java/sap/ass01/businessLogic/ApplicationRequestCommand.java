package sap.ass01.businessLogic;

import java.util.List;

public enum ApplicationRequestCommand {
    GETUSERS("GETUSERS", 0),
    GETBIKES("GETBIKES", 0),
    BEGINRIDE("BEGINRIDE", 2),      // user id + bike id
    ENDRIDE("ENDRIDE", 1),          // user id
    ADDCREDITS("ADDCREDITS", 2);    // user id + credits
    
    private final String commandName;
    private final int argsNum;

    private ApplicationRequestCommand(String commandName, int argsNum) {
        this.commandName = commandName;
        this.argsNum = argsNum;
    }

    public String commandName() { return this.commandName; }

    public int argsNum() { return this.argsNum; }

    public ApplicationRequest toApplicationRequest(List<?> arguments) {
        return new ApplicationRequest(this, arguments);
    }
}
