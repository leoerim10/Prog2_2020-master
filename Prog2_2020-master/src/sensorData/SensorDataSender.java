package sensorData;

import streamMachine.PersistenceException;
import transmission.DataConnection;

import java.io.*;

public class SensorDataSender {
    private final DataConnection connection;

    public SensorDataSender(DataConnection connection) {

        this.connection = connection;
    }

    public void sendData(String name, long time, float[] values, DataConnection c) throws PersistenceException, IOException { //data connection to start the connection first before it starts to send data
        // test of invalid parameters
        if(time < 0 || values == null || values.length < 1) {
            throw new PersistenceException("parameters are valid, values null or negative time");
        }
        try {
            DataOutputStream dos = c.getDataOutputStream();//as explained above
            dos.writeUTF(name);// sensorname
            dos.writeLong(time);//time
            // values
            dos.writeInt(values.length);
                for (int i = 0; i < values.length; i++) {
                dos.writeFloat(values[i]);
                }
            dos.close();

         } catch (FileNotFoundException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }


}
