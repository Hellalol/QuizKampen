import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server extends Thread {
    Socket socket;
    Server opponent;
    String Playername;
    public Server(Socket socket, String Playername){
        this.socket = socket;
        this.Playername = Playername;
    }
    public void setOpponent(Server opponent) {
        this.opponent = opponent;

    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        ) {
            String inputLine, outPutLine;

            //Initiate conversation with client

            Protocoll prot = new Protocoll();
            outPutLine = prot.processInput(null, this);
            out.println(outPutLine);

            while ((inputLine = in.readLine()) != null) {
                outPutLine = prot.processInput(inputLine,this);
                out.println(outPutLine);
            }
        } catch (IOException e) {

        }

    }
}