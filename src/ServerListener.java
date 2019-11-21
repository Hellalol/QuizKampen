import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

public class ServerListener implements Serializable {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        try {
            while (true) {
                Server playerOne
                        = new Server(listener.accept(), "Player One");
                Server playerTwo
                        = new Server(listener.accept(),"Player Two");
                playerOne.setOpponent(playerOne);
                playerTwo.setOpponent(playerTwo);
                playerOne.start();
                playerTwo.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}