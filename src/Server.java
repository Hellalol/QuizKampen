import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server extends Thread implements Serializable {

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
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            String inputLine, outPutLine;
            //Initiate conversation with client
            Protocoll prot = new Protocoll();
            outPutLine = prot.processInput(null, this);
            out.writeObject(outPutLine);

            while ((inputLine = in.readLine()) != null) {
                outPutLine = prot.processInput(inputLine,this);
                out.writeObject(outPutLine);
            }
        } catch (IOException e) {

        }

    }
}
