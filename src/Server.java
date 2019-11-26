import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread {
   protected Socket socket;
   protected Server opponent;
   protected String playername;
   protected  ObjectOutputStream oos;
   protected ObjectInputStream ois;

    public Server(Socket socket, String name){
        this.socket = socket;
        this.playername = name;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setOpponent(Server opponent) {
        this.opponent = opponent;
    }

    public void setPlayername(String playername) throws IOException {
        this.playername = playername;
       oos.writeObject(playername);
    }
}
