import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Connection Handler for a client connecting to the server
 */
public class transferFiles extends Thread {
	private Socket connectionSocket;
	private InputStream is;
	private OutputStream os;
	private PrintWriter pw;
	private String fileName;
	private BufferedReader br;
    private String uploadDir;
    private BufferedInputStream bis;

	/**
	 * Initialises the input and output streams, along with the printwriters and buffered readers which
	 * interact with the client.
	 * @param connectionSocket the connection socket which interacts with the client
	 * @param Dir the Upload directory for the files
	 */
	public transferFiles(Socket connectionSocket,String Dir) {
		this.connectionSocket = connectionSocket;
        this.uploadDir = Dir;
        try {
			this.is = connectionSocket.getInputStream();
			this.os = connectionSocket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()), true);
		} catch (IOException e) {
			System.out.println("Server - Errors Occurred:" + e.getMessage());
		}
	}

	/**
	 * Runs the connection handler
	 */
	public void run() {
		System.out.println("Server - new ConnectionHandler thread started .... ");
		try {
			sendFile();
		} catch (Exception e) {
			System.out.println("Server - ConnectionHandler:run " + e.getMessage());
			cleanup();
		}
	}

	/**
	 * Sends as many files across to the client as the client wants, though only in the place where it is
	 */
	private void sendFile() {
        String lineCheck = "Y";
		try {

			while (lineCheck.equals("Y")) {
				//Sends a list of files in the specified directory to the client.
			    pw.println("Here is a list of files in the allowed directory: ");
				//Algorithm found from Stack overflow
				// www.stackoverflow/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
				File folder = new File(uploadDir);
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						pw.println(listOfFiles[i].getName());
					}
				}
                pw.println("Please enter the name of the file you wish to access:");
				pw.println("End of list of files");
				//Gets the name of the file the client wishes to get.
				fileName = br.readLine();
				if (fileName.equals("exit")) break;
				try {
					//Alogirthm adapted from from one found online
					//http://www.rgagnon.com/javadetails/java-0542.html
					//Tries to find the file
					File fileDir = new File(uploadDir + fileName);
					//sets the byte array to be the length of the file
					byte [] byteArray  = new byte [(int)(fileDir).length()];
					//Reads the file using a buffered input stream
					bis = new BufferedInputStream(new FileInputStream(fileDir));
					bis.read(byteArray,0,byteArray.length);
					//Sends the length of the byte array containing the file to the client
					pw.println(byteArray.length);
					System.out.println("Transferring: " + fileName + " of length (" + byteArray.length + " bytes) to " +connectionSocket.getInetAddress());
					//Writes the file to the output stream.
					os.write(byteArray,0,byteArray.length);
					os.flush();

				} catch (IOException e) {
					//If the file is not found, then a suitable error message is sent to the client
					pw.println("File " + fileName + " not found.");
				}
				//Allows the client to be able to access more files while keeping the connection alive.
				pw.println("Do you want to access more files? Y or N");
                lineCheck = br.readLine();
			}
			cleanup();
		} catch (IOException e) {
			System.out.println("Server - Errors: " + e.getMessage());
            cleanup();
		}

	}

	/**
	 * Cleanup method to close the thread and close the socket to the user.
	 */
	private void cleanup() {
		System.out.println("Server - Connection Closed to..." + connectionSocket.getInetAddress());
		try {
			if (bis != null) bis.close();
			if (br != null) br.close();
			if (os != null) os.close();
			if (is != null) is.close();
			if (pw != null) pw.close();
			if (connectionSocket != null) connectionSocket.close();
		} catch (IOException ioe) {
			System.out.println("Server " + ioe.getMessage());
		}
	}
}