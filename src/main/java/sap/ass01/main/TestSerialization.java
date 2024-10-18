package sap.ass01.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;

import sap.ass01.businessLogic.EBike.EBikeState;
import sap.ass01.businessLogic.EBikeInfo;
import sap.ass01.businessLogic.P2d;
import sap.ass01.businessLogic.V2d;

public class TestSerialization {
    public static void main(String[] args) throws IOException {    
        var list = Stream.iterate(0, i -> i + 1).limit(5).map(i -> new EBikeInfo("a", EBikeState.AVAILABLE, new P2d(0, 0), new V2d(0, 0), 0.0, 0)).toList();

        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(list);
    }
}
