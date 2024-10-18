package sap.ass01.businessLogic;

import java.io.Serializable;

public record UserInfo(String userID, int credits) implements Serializable {

}
