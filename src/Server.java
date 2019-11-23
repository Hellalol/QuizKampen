import javax.print.DocFlavor;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server extends Thread {
    Socket socket;
    Server opponent;
    String playerName;
    ObjectOutputStream pw;
    ObjectInputStream in;


    int points;
    public Server(Socket socket){
        this.socket = socket;
        this.playerName = playerName;
        try {
            pw=new ObjectOutputStream(socket.getOutputStream());
            in =new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setOpponent(Server opponent) {
        this.opponent = opponent;
    }

    public void setPlayerName(String playerName) throws IOException {
        this.playerName = playerName;
        pw.writeObject(playerName);
    }


    @Override
    public void run() {

    }
}