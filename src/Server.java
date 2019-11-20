import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server extends Thread {
    Socket socket;
    Server opponent;
    String playerName;
    PrintWriter out;
    BufferedReader in;
    String round_amount ;
    String question_amount;
    boolean serverIsRunning = true;
    public Server(Socket socket){
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            //Get this client's name
            this.playerName = receive();

            System.out.println("player name is:"+ playerName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Exchange players' name to each other
    public void setOpponent(Server opponent) {
        if(opponent!=null) {
            this.opponent = opponent;
            sendToOpponent("OpponentName@"+this.playerName+"@"+false);
        }
    }
    void sendToOpponent(String msg){
        opponent.send(msg);
    }

    @Override
    public void run() {
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
            if(msg.contains("@")){
                StringTokenizer st = new StringTokenizer(msg,"@");
                switch (st.nextToken()){
                    case "R&Q":
                        round_amount = st.nextToken();
                        question_amount = st.nextToken();

                        System.out.println("Round and Question amount: "+ round_amount+" "+question_amount );
                        break;

                }
            }
            if(msg == null){
                System.out.println(playerName +" shut down.");
                serverIsRunning = false;
                socket.close();
            }
            System.out.println(playerName +" Server receive: "+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    void send(String msg){
        out.println(msg);
    }


}