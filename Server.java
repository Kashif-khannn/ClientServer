import java.net.*;
import java.io.*;
import java.util.* ;

public class Server {
	protected static List<List<String>> database;
    public static void main(String[] args) throws IOException {
        database = new ArrayList<List<String>>();

        int port = new Integer(args[0]).intValue();

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        Socket clientSocket = null;
       
        while (true) {
            // Listen for a TCP connection request.
            try {
                clientSocket = serverSocket.accept();
                BibliographyProtocol request = new BibliographyProtocol(clientSocket,database);
                  // Create a new thread to process the request.
            Thread thread = new Thread(request);
            
            // Start the thread.
            thread.start();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }catch (Exception e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            
        }

       
    }
}