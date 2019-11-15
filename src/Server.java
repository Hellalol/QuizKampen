import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server implements Runnable {
    int portNumber = 12345;
    //all info ska komma in i en array i detta formatet


    //svar och rätt index i Buttonarrayen parsas in i en mindre array och en variabel

    //int correctAnswerIndex = Integer.parseInt(infoFromServer2[5]);

    public Server() throws IOException {

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine, outPutLine;

            //Initiate conversation with client
            Protocoll prot = new Protocoll();
            outPutLine = prot.processInput(null);
            out.println(outPutLine);

            while ((inputLine = in.readLine()) != null){
                outPutLine = prot.processInput(inputLine);
                out.println(outPutLine);
                if (outPutLine.equalsIgnoreCase("Bye"))
                    break;
            }
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {

    }
    public static void main(String[] args) throws IOException {
        Server s = new Server();
    }
}