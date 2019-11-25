
import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("--Server running--");
        try {
            while (true) {
                Game game =new Game();
                //Spelare 1
                Server playerOne
                        = new Server(listener.accept(),"Player 1", game);
                Object obj = playerOne.ois.readObject();
                String playerOneName=(String) obj;
                playerOne.setPlayername(playerOneName);
                System.out.println("--" + playerOne.getPlayername() + " has connected--");
                System.out.println("--Socket " + playerOne.socket);

                game.setNuvarandeSpelare(playerOne);

                //Spelare 2
                Server playerTwo
                        = new Server(listener.accept(), "Player 2", game);
                Object obj1 = playerTwo.ois.readObject();
                String playerTwoName=(String) obj1;
                playerTwo.setPlayername(playerTwoName);
                System.out.println("--" + playerTwo.getPlayername() + " has connected--");
                System.out.println("--Socket " + playerTwo.socket);


                playerOne.setOpponent(playerTwo);
                playerTwo.setOpponent(playerOne);

                game.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}