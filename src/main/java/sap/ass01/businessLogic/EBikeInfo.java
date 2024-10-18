package sap.ass01.businessLogic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sap.ass01.businessLogic.EBike.EBikeState;

public record EBikeInfo(String bikeID, EBikeState state, P2d loc, V2d direction, double speed, int batteryLevel) implements Serializable {

}