package streamMachine;

import java.io.*;

public class WriteAndReadDataSet {
    public static void main(String[] args) throws IOException {
        // three example data sets
        String sensorName = "MyGoodOldSensor"; // does not change

        long timeStamps[] = new long[3];
        timeStamps[0] = System.currentTimeMillis();
        timeStamps[1] = timeStamps[0] + 1; // milli sec later
        timeStamps[2] = timeStamps[1] + 1000; // second later

        float[][] values = new float[3][];
        // 1st measure .. just one value
        float[] valueSet = new float[1];
        values[0] = valueSet;
        valueSet[0] = (float) 1.5; // example value 1.5 degrees

        // 2nd measure .. just three values
        valueSet = new float[3];
        values[1] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        // 3rd measure .. two values
        valueSet = new float[2];
        values[2] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;

        // write three data set into a file
        String filename = "testfile.txt";
        OutputStream fileOutputStream = new FileOutputStream(filename);

        DataOutputStream dos = new DataOutputStream(fileOutputStream);

        dos.writeInt(3);

        for(int number = 0; number < 3; number++) {
            // sensorname
            dos.writeUTF(sensorName);

            // timestamp
            dos.writeLong(timeStamps[number]);

            // values
            dos.writeInt(values[number].length);
            for (int i = 0; i < values[number].length; i++) {
                dos.writeFloat(values[number][i]);
            }
        }

        // read data from file and print to System.out
        InputStream is = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(is);

        int numberSets = dis.readInt();
        System.out.println("number data sets == " + numberSets);

        while(numberSets-- > 0) {
            // sensorname
            String sensorNameReceived = dis.readUTF();
            System.out.println("sensorname == " + sensorNameReceived);

            // timestamp
            long timeStampReceived = dis.readLong();
            System.out.println("timeStampReceived == " + timeStampReceived);

            // values
            int numberReceived = dis.readInt();
            float[] valuesReceived = new float[numberReceived];
            for (int i = 0; i < numberReceived; i++) {
                valuesReceived[i] = dis.readFloat();
                System.out.print("value[" + i + "]" + valuesReceived[i]);
            }
            System.out.println(" ");
        }
    }
}
