import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * This class runs the multicast socket for the server, sending the IP address of the server out.
 */
public class ServerMulticast extends Thread{
    private DatagramPacket serverIP;
    private static MulticastSocket s;
    private static InetAddress group;

    /**
     * Runner method for the separate multicast thread
     */
    public void run() {
        startMultiCast();
    }

    /**
     * Starts the multicast packet sending infinitely, as long as the thread runs.
     */
    public void startMultiCast(){
        try {
            String stringIP = InetAddress.getLocalHost().getHostAddress();
            //IP 228.5.6.7 and port 2345 was chosen for the MultiCast group.
            group = InetAddress.getByName("228.5.6.7");
            s = new MulticastSocket(2345);
            s.joinGroup(group);
            //Constructs the Datagram packet
            serverIP = new DatagramPacket(stringIP.getBytes(), stringIP.length(), group, 2345);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            try {
                //Sends the UDP packet every 5 seconds.
                s.send(serverIP);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
