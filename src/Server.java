import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server extends Thread {
    Socket socket;
    Server opponent;
    String Playername;
    ObjectOutputStream pw;
    ObjectInputStream in;
    int points;
    public Server(Socket socket){
        this.socket = socket;
        this.Playername = Playername;
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

    public void setPlayername(String playername) throws IOException {
        Playername = playername;
        pw.writeObject(playername);
    }


    @Override
    public void run() {
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

    }
}