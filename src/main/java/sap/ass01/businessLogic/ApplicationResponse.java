package sap.ass01.businessLogic;

import java.util.List;

public record ApplicationResponse(ApplicationResponseCommand command, List<?> arguments) {
    
    public ApplicationResponse {
        if (command.argsNum() != -1 && command.argsNum() != arguments.size()) {
            throw new IllegalArgumentException("The number of arguments doesn't match with the number specified for the command.");
        }
    }
}
