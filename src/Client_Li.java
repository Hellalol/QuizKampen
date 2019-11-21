import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

class Client_Li extends JFrame implements ActionListener,Runnable {



    JButton btn_start = new JButton("Connect");
    JLabel name = new JLabel("name",SwingConstants.CENTER);
    JTextField name_txt = new JTextField(6);
    JLabel questions = new JLabel("Questions");
    JPanel gameInfo = new JPanel();
    JPanel display = new JPanel();
    JPanel score = new JPanel();
    JPanel userInfo = new JPanel();

    JLabel user1 = new JLabel("user1",SwingConstants.CENTER);
    JLabel separator1 = new JLabel(" : ",SwingConstants.CENTER);
    JLabel separator2 = new JLabel(" : ",SwingConstants.CENTER);
    JLabel separator3 = new JLabel(" : ",SwingConstants.CENTER);
    JLabel user2 = new JLabel("user2",SwingConstants.CENTER);

    JLabel score_user1_round1 = new JLabel("0",SwingConstants.CENTER);
    JLabel score_user2_round1 = new JLabel("0",SwingConstants.CENTER);
    JLabel score_user1_round2 = new JLabel("0",SwingConstants.CENTER);
    JLabel score_user2_round2 = new JLabel("0",SwingConstants.CENTER);


    JPanel panel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JTextArea area = new JTextArea();
    Color colorbutton = new Color(13, 199, 253);
    private static JButton[] buttons = new JButton[4];
    int toPort = 12345;

    String hostName = "localhost";
    Socket socket ;

    Thread thread ;
    Protocoll pro = new Protocoll();

    BufferedReader in;
    PrintWriter out;

    String username;
    boolean isConnected = false;
    boolean clientIsRunning = true;
    String message = null;

    public Client_Li() throws IOException {

        setSize(400, 439);
        add(panel);
        userInfo.setLayout(new GridLayout(1,3));
        userInfo.add(name);
        userInfo.add(name_txt);
        userInfo.add(btn_start);

        display.setLayout(new GridLayout(1,3));
        display.add(user1);
        display.add(separator1);
        display.add(user2);

        score.setLayout(new GridLayout(2,3));
        score.add(score_user1_round1);
        score.add(separator2);
        score.add(score_user2_round1);
        score.add(score_user1_round2);
        score.add(separator3);
        score.add(score_user2_round2);

        gameInfo.setLayout(new GridLayout(3,0));
        gameInfo.add(userInfo);
        gameInfo.add(display);
        gameInfo.add(score);

        panel.setSize(400, 400);
        panel.setLayout(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);
        area.setEditable(false);
        area.setLineWrap(true);
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.setSize(400, 200);
        panel.add(gameInfo,BorderLayout.NORTH);
        panel.add(addButtons(), BorderLayout.SOUTH);
        area.setText(pro.infoFromServer2[0]);
        area.setVisible(true);
        buttonPanel.setVisible(true);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        //Use btn-start to start
        btn_start.addActionListener(l->{
            if(!isConnected) {
                username = name_txt.getText().trim();
                //Change user1 name and
                this.setTitle(username);
                user1.setText(username);
                try {
                    socket = new Socket(hostName, toPort);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                thread = new Thread(this);
                thread.start();
                isConnected = true;
                //sendMessage(username);
            }else {
                JOptionPane.showMessageDialog(this,"You have already logged in");
            }
        });
    }

    public JPanel addButtons(){

        //Knapparna målas upp en for-loop
        JPanel buttonGridArray = new JPanel();
        buttons = new JButton[4];
        for (int i = 0; i < 4 ; i++) {
            buttons[i] = new JButton();
            buttonGridArray.setLayout(new GridLayout(2,2));
            buttons[i].setPreferredSize(new Dimension(500,100));
            buttons[i].putClientProperty("column", i);
            buttons[i].addActionListener(this);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colorbutton);
            buttons[i].setText(pro.answerArray[i]);
            buttonGridArray.add(buttons[i]);
        }
        return buttonGridArray;
    }


    @Override
    public void run() {
        Thread sender = new Thread(new SendMessage(socket, username));
        sender.start();
        Thread receiver = new Thread(new ReceiveMessage(socket));
        receiver.start();
        System.out.println("I am run!!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton buttonPressed = (JButton) e.getSource();
        for (int i = 0; i < 4 ; i++) {
            if(buttons[i] == e.getSource()){
                if ((int)buttonPressed.getClientProperty("column") == pro.correctAnswerIndex){
                    buttons[i].setBackground(Color.green);
                   JOptionPane.showMessageDialog(null,"RÄTT");

                }else
                    buttons[i].setBackground(Color.red);
                    JOptionPane.showMessageDialog(null,"FEL");

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Client_Li();
    }

    class SendMessage implements Runnable{
        private Socket socket;
        private String username;

        public SendMessage(Socket socket,String username) {
            this.username = username;
            message = username;
            //sendMessage(username);
            System.out.println("Before send name");
            sendMessage();
            System.out.println("After send name");
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Message is: "+message);
         while(clientIsRunning && message!=null)
            {
                    System.out.println("Send in Send Thread run! and message is: "+ message);
                    //msg for question amount and round amount
                  /*  String msg = "";
                    sendMessage(msg);*/
                  sendMessage();
           }
        }

        void sendMessage(){
            out.println(message);
            System.out.println("Client "+ username +" has sent:"+message);
            message = null;
            System.out.println("Current message value : "+message);
        }

    }

    class ReceiveMessage implements Runnable{
        private Socket socket;

        public ReceiveMessage(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while(clientIsRunning) {
                receiveMessage();
            }
        }

        String receiveMessage(){
            String msg = "";
            try {
                msg = in.readLine();
                if(msg == null){
                    System.out.println("Server shut down");
                    clientIsRunning = false;
                    in.close();
                    out.close();
                    socket.close();
                }else if(msg.contains("@")){
                    StringTokenizer st = new StringTokenizer(msg,"@");
                    switch (st.nextToken()){
                        case "OpponentName":
                            msg = st.nextToken();
                            user2.setText(msg);
                            break;
                        default:
                            System.out.println("Client receive unmatched msg ");
                    }
                }else {
                    System.out.println("Client receives: "+msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }
    }
}