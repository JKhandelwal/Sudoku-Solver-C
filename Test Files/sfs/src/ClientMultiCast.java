import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.*;

/**
 * Class to run the client MultiCasting for the client
 */
public class ClientMultiCast extends Thread {
    private static MulticastSocket s;
    private static InetAddress address;
    private static HashMap<String,Date> received = new HashMap<>();

    /**
     * Starts the Thread to run the MultiCasting
     */
    public void run() {
        try {
            //Gets the IP address of the program.
            address = InetAddress.getByName("228.5.6.7");
            //Joins the MultiCast group.
            s = new MulticastSocket(2345);
            s.joinGroup(address);
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shutdown  function to close cleanly
     */
    public static void close() {
        try {
            s.leaveGroup(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        s.close();
    }

    /**
     * Receives the UDP packets.
     */
    private static void receive() {
    int counter = 0;
    try {
        while (true) {
            //Gets the DataGram socket
            byte[] buf = new byte[64];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            s.receive(recv);
            //Since the server part of this program also sends the UDP packet, this code filters out the
            //The users own IP
            Date d1 = new Date();
            if (!(new String(recv.getData())).trim().equals(InetAddress.getLocalHost().getHostAddress())) {
                received.put((new String(recv.getData())).trim(), d1);
            }
            Thread.sleep(1000);
            counter++;
            if (counter == 5)  {
                /*
                Every 5 seconds, there is a sweep through the list of stored IP addresses
                The time that the packets arrived is stored, and since the IP is sent every 5 seconds,
                if the program has not received an IP from a Server for 20 seconds or 4 cycles, it removes it
                from the list of known IPs.
                 */
                for (String s: received.keySet()){
                    Date currDate = new Date();
                    if ((((currDate.getTime()) - (received.get(s).getTime()))/1000) > 20){
                        received.remove(s);
                    }
                }
                counter = 0;
            }
        }
    } catch(Exception e) {
        System.out.println(e.getMessage());
    }
}

    /**
     *Returns the Hash Map containing the list of IPs open on the network
     * @return the hash map of the known IP's on the network
     */
    public static HashMap<String,Date> getUsers() {
        return received;
    }
}
