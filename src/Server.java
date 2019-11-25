
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread {
    Socket socket;
    Server opponent;
    String playername;
    Game game;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int points;


    public Server(Socket socket, String name, Game game){
        this.socket = socket;
        this.playername = name;
        this.game = game;


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


// @Override
//public void run() {
//        try (
//
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(socket.getInputStream()));
//        ) {
//            String inputLine, outPutLine;
//
//            //Initiate conversation with client
//
//            ServerProtocoll prot = new ServerProtocoll();
//            outPutLine = prot.processInput(null, this);
//            out.println(outPutLine);
//
//            while ((inputLine = in.readLine()) != null) {
//                outPutLine = prot.processInput(inputLine,this);
//                out.println(outPutLine);
//            }
//        } catch (IOException e) {
//
//        }
