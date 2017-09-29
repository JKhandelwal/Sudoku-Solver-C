import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
public class Client {

	private Socket socket;
	private String host;
    //The port chosen is 12345
	private static final int port = 12345;
	private BufferedReader br;
    private BufferedReader brIn;
	private PrintWriter pw;
	private InputStream is;
	private String downloadDir = "";
    private BufferedOutputStream bos;
	private ClientMultiCast c = new ClientMultiCast();

	/**
	 * The main class which starts the Client Running
	 *@param Dir The download directory
	 */
	public Client(String Dir) {
		//Adds an exit string to allow the user to close the program whenever they wish
		System.out.println("Exit string is: exit");
		this.downloadDir = Dir;
		brIn =  new BufferedReader(new InputStreamReader(System.in));
		c.start();
       try{
		   //Added to allow the Server to be able to catch up with the client.
           Thread.sleep(100);
       }catch (java.lang.InterruptedException e){
            System.out.println(e.getMessage());
        }
		try {
			String repeatInput = "Y";
			//String Input;
			while (repeatInput.equals("Y")) {
				//This allows the user to be able to access a different server
                //After accessing the files on this server.
				findUsers();
				setIP();
				System.out.println("Would you like to access more files on another server? Y or N");
			 	repeatInput = (brIn.readLine()).trim();
				while (!((repeatInput.equals("Y")) || (repeatInput.equals("N")) || (repeatInput.equals("exit")))){
					System.out.println("Invalid input please re-enter");
					repeatInput = (brIn.readLine()).trim();
				}
			}
			//Cleanup methods to allow the program to close properly
			cleanup();
			c.close();
			//Server.shutDown();
			System.out.println("Closing program");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Prints out a list of Users sharing files on the network
     */
	public void findUsers(){
		HashMap<String,Date> users = ClientMultiCast.getUsers();
		if (users.size() == 0){
			System.out.println("No users are currently on the network");
		}else{
			System.out.println("Here is a list of IP addresses you can connect to on the local network:");
		for (String s: users.keySet()){
			System.out.println(s);
		}
		}
	}

    /**
     * Asks the user which IP address they wish to connect to
     */
	private void setIP(){
        System.out.println("Enter an IP address to connect to ");
        try{
            String enteredHost;
            enteredHost = (brIn.readLine()).trim();
            while(enteredHost.length() == 0){
                System.out.println("You must enter an IP address, Please re-enter");
                enteredHost = (brIn.readLine()).trim();
            }
            if (enteredHost.equals("exit")) {
				System.out.println("Closing program");
				cleanup();
				c.close();
            	System.exit(0);
			}
            this.host = enteredHost;
            runClient();
        } catch (IOException e){
			System.out.println(e.getMessage());

        }
    }

    /**
     * This method connects to the IP address provided, on the default port 12345
     */
	private void runClient() {
		try {
			this.socket = new Socket(host, port);
			System.out.println("Client connected to " + host + " on port " + port + ".");
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			is = socket.getInputStream();
            //If successful in connecting, allows the user to read files.
			readFiles();
		} catch (Exception e) {
			System.out.println("Errors: Connection to " + host + " on port " + port + ". " + e.getMessage());
			//cleanup();
		}
	}

    /**
     * This method allows the user to read files from the server repeatedly until they wish to stop.
     */
	private void readFiles() {
		br = new BufferedReader(new InputStreamReader(is));
		//FileWriter fw = null;
		boolean validFile;
		String line = "Y";
		String line3;
		int byteArraySize;
		String fileToReceive;

		try {
            //Input Validation
			while (line.equals("Y")) {
				validFile = true;
				while (!(line = br.readLine()).equals("End of list of files")) {
					System.out.println(line);
				}
				//Gets the file the user wished to access from the terminal
				line = (brIn.readLine()).trim();
				String fileName = line;
				pw.println(line);
				pw.flush();
				if (line.equals("exit")) {
					System.out.println("Closing program");
					c.close();
					cleanup();
					if (brIn != null)brIn.close();
					System.exit(0);
				} else {
                    //Gets either the size of the byte array or a statement saying that the file has not been found
					line3 = br.readLine();
					String text = line3;
					if (text.equals("File " + fileName + " not found.")) {
						System.out.println(text);
						validFile = false;
					} else {

						//Reading algorithm for files adapted from:
						//http://www.rgagnon.com/javadetails/java-0542.html
						byteArraySize = (Integer.parseInt(line3));
						fileToReceive = (downloadDir + fileName);
						int bytesRead;
						int current;

						// receive file
						byte[] clientByteArray = new byte[byteArraySize];
						try {
                            //Try to create the file in the correct directory
							bos = new BufferedOutputStream(new FileOutputStream(fileToReceive));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						bytesRead = is.read(clientByteArray, 0, clientByteArray.length);
						current = bytesRead;
                        //Read as many bytes as there are in the array, and move on.
						for (int counter = 0; counter < clientByteArray.length + 1; counter++) {
							bytesRead = is.read(clientByteArray, current, (clientByteArray.length - current));
							if (bytesRead >= 0) current += bytesRead;
						}
                        //Write the file to the directory
						bos.write(clientByteArray, 0, current);
						bos.flush();
					}
						if (validFile == true) System.out.println("File " + fileName + " transferred to directory " + downloadDir);
                        //Allows the user to be able to download many files from the Server without closing the connection
						line = br.readLine();
						System.out.println(line);
						line = (brIn.readLine()).trim();
						while (!(line.equals("Y") || (line.equals("N")) || (line.equals("exit")))) {
							System.out.println("Invalid String Re-enter Y or N");
							line = (brIn.readLine()).trim();
						}
						pw.println(line);
						pw.flush();
						if (line.equals("exit")) {
							System.out.println("Closing program");
							c.close();
							cleanup();
							if (brIn != null)brIn.close();
							System.exit(0);
						}
				}
			}
			System.out.println("Closing connection to server... ");
			cleanup();
		} catch (IOException e) {
			System.out.println("Client - Errors Occurred: " + e.getMessage());
			cleanup();
		}
	}

    /**
     * Cleanup method for closing the connection smoothly to the server
     */
	private void cleanup() {
		try {
			if (bos != null) bos.close();
			if (pw != null) pw.close();
			if (is != null) is.close();
			if (br != null) br.close();
			if (socket != null) socket.close();
		} catch (IOException ioe) {
			System.out.println("Errors: " + ioe.getMessage());
		}
	}

}
