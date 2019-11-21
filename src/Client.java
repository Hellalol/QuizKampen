import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

class Client extends JFrame {
    //Log in frame
    JFrame loginFrame = new JFrame();
    JPanel loginPanel = new JPanel();
    JPanel welcome_panel = new JPanel();
    JLabel welcome = new JLabel("Welcome");
    JLabel name = new JLabel("username:",SwingConstants.CENTER);
    JButton btn_start = new JButton("Connect");
    JTextField name_txt = new JTextField(6);

    //play game frame
    JFrame questionFrame = new JFrame();


    //choose category frame
    JFrame categoryFrame = new JFrame();


    JPanel round_group_panel = new JPanel();
    JPanel question_group_panel = new JPanel();

    JPanel option_zone = new JPanel();
    JButton btn_confirm = new JButton("confirm");
    JLabel rounds = new JLabel("Rounds");
    ButtonGroup round_group = new ButtonGroup();
    JRadioButton r_2 = new JRadioButton("2",true);

    JRadioButton r_3 = new JRadioButton("3");
    JRadioButton r_4 = new JRadioButton("4");

    ButtonGroup question_group = new ButtonGroup();
    JLabel questions_ = new JLabel("Questions");
    JRadioButton q_2 = new JRadioButton("2",true);
    JRadioButton q_3 = new JRadioButton("3");
    JRadioButton q_4 = new JRadioButton("4");





    Color colorbutton = new Color(13, 199, 253);
    private static JButton[] buttons = new JButton[4];
    int toPort = 12345;

    String hostName = "localhost";
    Socket socket ;

    Protocoll pro = new Protocoll();

    BufferedReader in;
    PrintWriter out;

    String username;
    boolean clientIsRunning = true;

    String question_amount ="+";
    String round_amount ="-";
    volatile String  message;

    Thread sender;
    Thread receiver;

    public Client() throws IOException {
        //Log in frame
        loginFrame.setSize(600,600);
        loginFrame.setTitle("Log in");
        loginFrame.setLocationRelativeTo(null);
        welcome_panel.add(welcome);
        loginFrame.add(welcome_panel,BorderLayout.NORTH);
        loginPanel.add(name);
        loginPanel.add(name_txt);
        loginPanel.add(btn_start);
        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        //play game frame
        questionFrame.setSize(600,600);
        questionFrame.setTitle("To answer questions");
        questionFrame.setLocationRelativeTo(null);
        setSize(400, 439);

        //choose question and round amount
        option_zone.setLayout(new GridLayout(2,2));
        round_group_panel.setLayout(new GridLayout(1,3));
        round_group.add(r_2);
        round_group.add(r_3);
        round_group.add(r_4);
        round_group_panel.add(r_2);
        round_group_panel.add(r_3);
        round_group_panel.add(r_4);

        question_group_panel.setLayout(new GridLayout(1,3));
        question_group.add(q_2);
        question_group.add(q_3);
        question_group.add(q_4);
        question_group_panel.add(q_2);
        question_group_panel.add(q_3);
        question_group_panel.add(q_4);
        option_zone.add(rounds);
        option_zone.add(round_group_panel);
        option_zone.add(questions_);
        option_zone.add(question_group_panel);
        add(option_zone,BorderLayout.NORTH);
        add(btn_confirm,BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginFrame.setVisible(true);

        //Category frame to choose category type
        categoryFrame.setSize(600,600);
        categoryFrame.setTitle("Category");
        categoryFrame.setLocationRelativeTo(null);
        categoryFrame.setVisible(false);

        //Use btn-start to start
        btn_start.addActionListener(l->{
            username = name_txt.getText().trim();
            try {
                socket = new Socket(hostName, toPort);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Start receive thread
            receiver = new Thread(new ReceiveMessage(socket));
            receiver.setName("Receive Thread" );
            receiver.start();

            //Start send Sender thread
            sender = new Thread(new SendMessage(socket, username));
            sender.setName("Send Thread");
            sender.start();

            loginFrame.setVisible(false);
            this.setVisible(true);
        });

        btn_confirm.addActionListener(l->{
            if(r_2.isSelected())
                round_amount = "2";
            if(r_3.isSelected())
                round_amount = "3";
            if(r_4.isSelected())
                round_amount = "4";
            if(q_2.isSelected())
                question_amount = "2";
            if(q_3.isSelected())
                question_amount = "3";
            if(q_4.isSelected())
                question_amount = "4";
            System.out.println("Sender thead alive: "+sender.isAlive());
            message = "R&Q@"+round_amount+"@"+question_amount;
            System.out.println(message);
        });
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

    //Send thread
    class SendMessage implements Runnable{
        private Socket socket;
        private String username;
        

        public SendMessage(Socket socket,String username) {
            this.socket = socket;
            this.username = username;
            //Send username
            message = username;

        }

        @Override
        public void run() {
            while(clientIsRunning) {
                //When message is not null, client send the message
                if(message!=null) {
                    System.out.println("inside: "+message);
                    sendMessage();
                }
            }
        }

        void sendMessage(){
            out.println(message);
            System.out.println("Client "+ username +" has sent:"+message);
            //After the message is sent, make message as null again
            message = null;
        }
    }

    //Receive thread
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
                            break;
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