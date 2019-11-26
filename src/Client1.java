import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client1 extends JFrame implements Runnable {

    Thread thread=new Thread(this);
    JButton[] buttons=new JButton[4];
    JPanel buttonsPanel=new JPanel();
    JTextArea showQuestion = new JTextArea(15,100);
    ObjectOutputStream pw;
    ObjectInputStream in;
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    Socket socket;
    Question questionFromServer;
    Category categoryFromServer;
    int points;
    int pointsOpponent;
    String rightAnswer;
    int round = 1;

    public Client1(){
        String namn=JOptionPane.showInputDialog("Skriv ditt namn!");
        if(namn == null)
            System.exit(0);
        //  String categoy=JOptionPane.showInputDialog("Choose category");
        setLayout(new BorderLayout());
        try {
            socket=new Socket("localhost",12345);
            pw=new ObjectOutputStream(socket.getOutputStream());
            in=new ObjectInputStream(socket.getInputStream());
            pw.writeObject(namn);
            setTitle(namn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i]= new JButton("Vänta på andra spelaren");
            buttons[i].addActionListener(sendAnswer);
            buttons[i].setBackground(Color.white);
            buttons[i].setBorder(border);
            buttons[i].setFont(new Font("arial",Font.PLAIN,20));
            buttonsPanel.setLayout(new GridLayout(2,2,16,16));
            buttonsPanel.add(buttons[i]);
        }
        setLocationRelativeTo(null);
        showQuestion.setWrapStyleWord(true);
        showQuestion.setLineWrap(true);
        showQuestion.setFont(new Font("arial",Font.PLAIN,20));
        add(showQuestion,BorderLayout.NORTH);
        add(buttonsPanel,BorderLayout.SOUTH);
        showQuestion.setBorder(border);

        setSize(500,485);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        thread.start();
    }


    @Override
    public void run() {
        try {
            Object data;
            while((data=in.readObject())!=null){
                if(data instanceof Question) {
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    questionFromServer = (Question) data;

                    for (int i = 0; i < buttons.length; i++) {
                        //buttons[i].addActionListener(sendAnswer);
                        buttons[i].setBackground(Color.white);
                        buttons[i].setForeground(Color.BLUE);
                        buttons[i].setBorder(border);
                        buttons[i].setFont(new Font("arial", Font.PLAIN, 20));
                        buttonsPanel.setLayout(new GridLayout(2, 2, 16, 16));
                        buttonsPanel.add(buttons[i]);
                    }
                    showQuestion.setText(questionFromServer.getQuestion());
                    buttons[0].setText(questionFromServer.getAnswerOne());
                    //System.out.println(questionFromServer.getAnswerOne());
                    buttons[1].setText(questionFromServer.getAnswerTwo());
                    //System.out.println(questionFromServer.getAnswerTwo());
                    buttons[2].setText(questionFromServer.getAnswerThree());
                    //System.out.println(questionFromServer.getAnswerThree());
                    buttons[3].setText(questionFromServer.getAnswerFour());
                    //System.out.println(questionFromServer.getAnswerFour());
                    System.out.println("correct answer: "+questionFromServer.getCorrectAnswer());
                    rightAnswer = questionFromServer.getCorrectAnswer();
                    //System.out.println("Right answer in receive: "+rightAnswer);
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setEnabled(true);
                    }
                }
                else if(data instanceof Category){
                    categoryFromServer = (Category) data;
                    showQuestion.setText(categoryFromServer.getChooseCat());
                    buttons[0].setText(categoryFromServer.getCat1());
                    buttons[1].setText(categoryFromServer.getCat2());
                    buttons[2].setText(categoryFromServer.getCat3());
                    buttons[3].setText(categoryFromServer.getCat4());
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setEnabled(true);
                        buttons[i].setForeground(Color.blue);
                    }
                }
                else if(data instanceof String){
                    String stringFromServer = (String) data;
                    //setTitle(stringFromServer);
                    System.out.println(stringFromServer);
                    if(((String) data).startsWith("RoundScore")){
                        pointsOpponent = Integer.parseInt(((String) data).substring(10));
                        System.out.println("pointsOpponent is: "+pointsOpponent);
                        //showQuestion.setText(points +" : "+pointsOpponent);
                        JOptionPane.showMessageDialog(this,"Current result:\n" + points + " : " + pointsOpponent,"Round "+ round + " is done",JOptionPane.INFORMATION_MESSAGE);
                        round++;
                        for(JButton btn:buttons){
                            btn.setText("");
                        }
                    }
                    //When game is over send points
                    if(((String) data).equals("Gameover")){
                        pw.writeObject(""+points);
                        System.out.println("Send: "+points);
                        //pointsOpponent = Integer.parseInt((String)in.readObject());

                        if(points > pointsOpponent)
                            showQuestion.setText("Game is over\nFinal Result:\n"+ points +" : "+pointsOpponent + "\nYou are winner!");
                        if(points < pointsOpponent)
                            showQuestion.setText("Game is over\nFinal Result:\n"+ points +" : "+pointsOpponent + "\nYou lose!");
                        if(points == pointsOpponent)
                            showQuestion.setText("Game is over\nFinal Result:\n"+ points +" : "+pointsOpponent + "\nEven!");
                        for(JButton btn:buttons){
                            btn.setText(null);
                        }
                    }
                    if(((String) data).startsWith("Other")){
                        showQuestion.setText((String) data);
                        for (int i = 0; i < buttons.length; i++) {
                            buttons[i].setEnabled(false);
                        }
                    }else
                        for (int i = 0; i < buttons.length; i++) {
                            buttons[i].setEnabled(true);
                        }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ActionListener sendAnswer = e -> {
        System.out.println("button click runs");
        JButton button=(JButton) e.getSource();
        System.out.println("Pressed button is: " + button.getText());
        System.out.println("right answer is: " + rightAnswer);
        //client chose right answer
        if(rightAnswer != null) {
            if (button.getText().equals(rightAnswer)) {
                //button.setBackground(Color.green);
                button.setForeground(Color.green);
                points++;
                System.out.println("Client point: " + points);
                clientSendPoints();
                System.out.println("Right answer is: " + rightAnswer);
            }//Client chose wrong answer
            else {
                //button.setBackground(Color.red);
                button.setForeground(Color.red);
                for (JButton btn : buttons) {
                    if (btn.getText().equals(rightAnswer)) {
                        //btn.setBackground(Color.green);
                        btn.setForeground(Color.green);
                        break;
                    }
                }
                System.out.println("Client point: " + points);
                clientSendPoints();
                rightAnswer = null;
            }
        }
        else{
            try {
                pw.writeObject(button.getText());
                System.out.println("client sends: "+button.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        new Client1();
    }

    void clientSendPoints(){
        try {
            //send player's points
            pw.writeObject(""+points);
            System.out.println("Player's points on client side: "+points);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}