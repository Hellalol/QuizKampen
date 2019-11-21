import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        boolean firstPlayer = true;
        System.out.println("-----Server started-----");
        try {
            while (true) {
                Socket socket1 = listener.accept();
                System.out.println("-----------player one client connected--------");
                Server playerOne = new Server(socket1,firstPlayer);
                playerOne.start();
                System.out.println("----Player one started----");

                firstPlayer = !firstPlayer;

                Socket socket2 = listener.accept();
                System.out.println("-----------player two client connected--------");
                Server playerTwo = new Server(socket2,firstPlayer);
                playerTwo.start();
                System.out.println("----Player two started----");

                firstPlayer = !firstPlayer;
                playerOne.setOpponent(playerTwo);
                playerTwo.setOpponent(playerOne);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        listener.close();
    }
}
