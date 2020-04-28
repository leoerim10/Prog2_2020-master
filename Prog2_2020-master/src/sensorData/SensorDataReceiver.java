package sensorData;

import streamMachine.PersistenceException;
import streamMachine.StreamMachine;
import transmission.DataConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SensorDataReceiver {
    private final DataConnection connection;
    private final StreamMachine storage;

    public SensorDataReceiver(DataConnection connection, StreamMachine storage) {
        this.connection = connection;
        this.storage = storage;
    }

    public void storeData(DataConnection c) throws PersistenceException {
        try {
            DataInputStream dis = c.getDataInputStream();// start the connection
            String sensor_Name = dis.readUTF();// sensorname
            long time = dis.readLong();//time
            //values
            int number_Received = dis.readInt();
            float[] values_Received = new float[number_Received];
            for (int i = 0; i < number_Received; i++) {
                values_Received[i] = dis.readFloat();
            }
            storage.saveData(time, values_Received);
            dis.close();

        } catch (FileNotFoundException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    StreamMachine getStorage() throws IOException, PersistenceException { // returns the stored datasets
        return storage;
    }



}

