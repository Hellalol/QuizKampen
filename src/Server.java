<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
=======
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ServerSocket;
>>>>>>> e6b1e1fa943c30b5efacdf538abf6bb746313df2
import java.net.Socket;
import java.util.StringTokenizer;

public class Server extends Thread {
    Socket socket;
    Server opponent;
<<<<<<< HEAD
    String playerName;
    PrintWriter out;
    BufferedReader in;
    String round_amount ;
    String question_amount;
    boolean serverIsRunning = true;
    //to check whether to show rounds and questions configuration frame
    boolean firstPlayer;
    public Server(Socket socket,boolean firstPlayer){
        this.socket = socket;
        this.firstPlayer = firstPlayer;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            //Get this client's name
            this.playerName = receive();

            System.out.println("player name is:"+ playerName);
=======
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
>>>>>>> e6b1e1fa943c30b5efacdf538abf6bb746313df2

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Exchange players' name to each other
    public void setOpponent(Server opponent) {
<<<<<<< HEAD
        if(opponent!=null) {
            this.opponent = opponent;
            sendToOpponent("OpponentName@"+this.playerName+"@"+firstPlayer);
        }
    }
    void sendToOpponent(String msg){
        opponent.send(msg);
=======
        this.opponent = opponent;
    }

    public void setPlayername(String playername) throws IOException {
        Playername = playername;
        pw.writeObject(playername);
>>>>>>> e6b1e1fa943c30b5efacdf538abf6bb746313df2
    }


    @Override
    public void run() {
<<<<<<< HEAD
        while(serverIsRunning) {
            String msg = receive();
            if(opponent!=null)
                sendToOpponent(msg);
        }
    }

    String receive (){
        String msg = "";
        try {
            msg = in.readLine();
            if(msg == null){
                System.out.println(playerName +" shut down.");
                serverIsRunning = false;
                socket.close();
            }
            else if(msg.contains("@")){
                StringTokenizer st = new StringTokenizer(msg,"@");
                switch (st.nextToken()){
                    case "R&Q":
                        round_amount = st.nextToken();
                        question_amount = st.nextToken();
                        System.out.println("Round and Question amount: "+ round_amount+" "+question_amount );
                        break;

                }
            }
            System.out.println(playerName +" Server receive: "+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
=======
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
>>>>>>> e6b1e1fa943c30b5efacdf538abf6bb746313df2

    void send(String msg){
        out.println(msg);
        System.out.println("Server sends: "+msg);
    }

}