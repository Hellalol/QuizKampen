import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    String hostName = "172.20.201.252";
    int portNumber = 44444;


    public Client() throws IOException {
        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {

            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {

                System.out.println("Server: " + fromServer);
                if (fromServer.equalsIgnoreCase("Bye"))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {
        Client c = new Client();
    }
}