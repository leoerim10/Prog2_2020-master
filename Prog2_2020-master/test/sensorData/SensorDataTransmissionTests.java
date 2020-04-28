package sensorData;

import streamMachine.PersistenceException;
import streamMachine.SensorDataSet;
import streamMachine.StreamMachine;
import org.junit.Assert;
import org.junit.Test;
import streamMachine.StreamMachineFS;
import transmission.DataConnection;
import transmission.DataConnector;

import java.io.DataOutputStream;
import java.io.IOException;

public class SensorDataTransmissionTests {
    private static final int PORTNUMBER = 9876;

    @Test
    public void gutTransmissionTest() throws IOException, PersistenceException {
        // create example data set
        String sensorName = "MyGoodOldSensor"; // does not change
        long timeStamp = System.currentTimeMillis();
        float[] valueSet = new float[3];
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                              receiver side                                        //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // create storage
        StreamMachineFS dataStorage = new StreamMachineFS(sensorName);

        // create connections
        DataConnection receiverConnection = new DataConnector(PORTNUMBER);

        // create receiver
        SensorDataReceiver sensorDataReceiver = new SensorDataReceiver(receiverConnection, dataStorage);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                              sender side                                          //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // create connections
        DataConnection senderConnection = new DataConnector("localhost", PORTNUMBER);

        // create sender
        SensorDataSender sensorDataSender = new SensorDataSender(senderConnection);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //                               execute communication and test                                      //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // send data with TCP
        sensorDataSender.sendData(sensorName, timeStamp, valueSet, senderConnection);

        // test if stored
        sensorDataReceiver.storeData(receiverConnection);
        StreamMachine dataStorageReceived = sensorDataReceiver.getStorage();


        String sensorNameReceived = dataStorageReceived.getSensorName();
        long timeStampReceived = dataStorageReceived.getDataSet(dataStorageReceived.size() - 1).getTime(); // to get the exact same time. -1 because array index starts with o but the size goes with 1.

        // test
        Assert.assertEquals(sensorName, sensorNameReceived);
        Assert.assertEquals(timeStamp, timeStampReceived);
    }
}
