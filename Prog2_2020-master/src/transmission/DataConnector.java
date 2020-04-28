package transmission;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DataConnector implements DataConnection {
    ServerSocket serversocket;
    Socket clientsocket;
    /**
     * Create client side - open connection to address / port
     * @param address
     */
    public DataConnector(String address, int port) throws IOException {
        clientsocket = new Socket(address, port);
    }

    /**
     * Create server side - open port on this port and wait for one client
     * @param port
     */
    public DataConnector(int port) throws IOException {
        serversocket = new ServerSocket(port);
    }

    @Override
    public DataInputStream getDataInputStream() throws IOException {
        Socket s  = serversocket.accept(); // should be first accepted before starting the connection
        DataInputStream is = new DataInputStream(s.getInputStream());
        return is;
    }

    @Override
    public DataOutputStream getDataOutputStream() throws IOException {
        DataOutputStream os = new DataOutputStream(clientsocket.getOutputStream());
        return os;
    }
}
