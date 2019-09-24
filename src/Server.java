import java.net.DatagramSocket;
import java.net.MulticastSocket;

import se.miun.models.Constants;

public class Server {
    DatagramSocket datagramSocket = null;

    public static void main(String[] args) {
        Server server = new Server();
    }
    
    public Server() {
        try {
            datagramSocket = new MulticastSocket(Constants.serverPort);
        } catch(Exception e) {
            System.out.print("error creating multicast socket, stacktrace:" + e.getStackTrace());
        }

    }

}