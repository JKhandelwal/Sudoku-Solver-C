import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Runs the server
 */
public class Server extends Thread {
	private static ServerSocket serverSocket;
	private static final int port = 12345;
	private static String uploadDir = "";
	private static ServerMulticast s;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Sets the upload Directory
	 * @param Dir upload directory
	 */
	public Server(String Dir) {
		this.uploadDir = Dir;
	}

	/**
	 * Starts the Server on the default port 12345,
	 * Then starts the MultiCast socket
	 */
	public void run() {

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server - Your server has started on port " + port + " with directory " + uploadDir);
			s = new ServerMulticast();
			s.start();
			while (true) {
				//Accepts the connection and starts a connection handler thread to manage that client.
				Socket connectionSocket = serverSocket.accept();
				System.out.println("Server - Accepted the connection to " + connectionSocket.getInetAddress());
				transferFiles tf = new transferFiles(connectionSocket,uploadDir);
				tf.start();
			}
		} catch (IOException e) {
			System.out.println("Server could not start - Errors" + e.getMessage());
		}
	}

}