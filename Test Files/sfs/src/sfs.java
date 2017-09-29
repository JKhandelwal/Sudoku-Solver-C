import java.io.File;

/**
 * Main Class which Starts the Program, if arguments are provided, they form the upload
 * and download directories, otherwise the directories would be the same as the ones that the program is set up in
 * The server is set up in a different thread to the main client part of the program.
 */
public class sfs {

    public static void main(String[] args){
        if ((args.length != 2) && (args.length != 0)){
            System.out.println("Invalid arguments");
        }else{
            if (args.length == 0){
                Server s = new Server(".");
                s.start();
                Client c = new Client(".");
            }else{
                File folder1 = new File(args[0]);
                File folder2 = new File(args[1]);
                if (folder1.exists() && folder2.exists()) {
                    Server s = new Server(args[0]);
                    s.start();
                    Client c = new Client(args[1]);
                }else System.out.println("Invalid Directory Names");
            }
        }

    }
}
