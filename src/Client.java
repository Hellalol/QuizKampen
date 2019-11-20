import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

class Client extends JFrame implements ActionListener,Runnable {
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

    Thread thread ;
    Protocoll pro = new Protocoll();

    BufferedReader in;
    PrintWriter out;

    String username;
    boolean isConnected = false;
    boolean clientIsRunning = true;

    boolean sendQuestionAndRoundNumber = false;
    String question_amount ="2";
    String round_amount ="2";
    String message;

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

        r_2.addActionListener(this);
        r_3.addActionListener(this);
        r_4.addActionListener(this);
        q_2.addActionListener(this);
        q_3.addActionListener(this);
        q_4.addActionListener(this);

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


        categoryFrame.setSize(600,600);
        categoryFrame.setTitle("Category");
        categoryFrame.setLocationRelativeTo(null);
        categoryFrame.setVisible(false);

        //Use btn-start to start
        btn_start.addActionListener(l->{
            if(!isConnected) {
                username = name_txt.getText().trim();
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
                loginFrame.setVisible(false);

                this.setVisible(true);
            }
        });
        btn_confirm.addActionListener(l->{
            sendQuestionAndRoundNumber = true;
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
        if(e.getSource().equals("r_2")){
            round_amount = "2";
            System.out.println("Round amount is:"+round_amount);
        }else if(e.getSource().equals("r_3")){
            round_amount = "3";
            System.out.println("Round amount is:"+round_amount);
        }else if(e.getSource().equals("r_4")){
            round_amount = "4";
            System.out.println("Round amount is:"+round_amount);
        }

        if(e.getSource().equals("q_2")){
            question_amount = "2";
            System.out.println("Question amount is:"+question_amount);
        }else if(e.getSource().equals("q_3")){
            question_amount = "3";
            System.out.println("Question amount is:"+question_amount);
        }else if(e.getSource().equals("q_4")){
            question_amount = "4";
            System.out.println("Question amount is:"+question_amount);
        }
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

    class SendMessage implements Runnable{
        private Socket socket;
        private String username;

        public SendMessage(Socket socket,String username) {
            this.username = username;
            message = username;
            sendMessage(message);
            this.socket = socket;
        }

        @Override
        public void run() {
        while(clientIsRunning&&!message.equals(""));
          {
                    //msg for question amount and round amount
                    if(sendQuestionAndRoundNumber){
                        message = "R&Q@"+round_amount+"@"+question_amount;
                        //initial = false;
                    }
                    sendMessage(message);
                    message="";
           }
        }

        void sendMessage(String msg){
            out.println(msg);
            System.out.println("Client "+ username +" has sent:"+msg);
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
                            if(st.nextToken().equals("false"))
                                //initial = false;
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