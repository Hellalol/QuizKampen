import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements Runnable {


    Thread thread=new Thread(this);
    JButton[] buttons=new JButton[4];
    JPanel buttonsPanel=new JPanel();
    JTextArea showQuestion = new JTextArea("Vänta på motståndare!",8,100);
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    Socket socket;
    Question questionFromServer;
    Category categoryFromServer;
    int points;
    int pointsOpponent;
    String rightAnswer;
    int round = 1;

    public Client(){
        String namn=JOptionPane.showInputDialog("Skriv ditt namn!");
        if(namn == null)
            System.exit(0);
        //  String categoy=JOptionPane.showInputDialog("Choose category");
        setLayout(new BorderLayout());
        setSize(500,317);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            socket=new Socket("localhost",12345);
            oos =new ObjectOutputStream(socket.getOutputStream());
            ois =new ObjectInputStream(socket.getInputStream());
            oos.writeObject(namn);
            oos.flush();
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
        setVisible(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Object dataFromServer;
            while((dataFromServer= ois.readObject())!=null){
                if(dataFromServer instanceof Question) {
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    questionFromServer = (Question) dataFromServer;

                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setBackground(Color.white);
                        buttons[i].setForeground(Color.BLACK);
                        buttons[i].setBorder(border);
                        buttons[i].setFont(new Font("arial", Font.PLAIN, 20));
                        buttonsPanel.setLayout(new GridLayout(2, 2, 16, 16));
                        buttonsPanel.add(buttons[i]);
                    }
                    showQuestion.setText(questionFromServer.getQuestion());
                    buttons[0].setText(questionFromServer.getAnswerOne());
                    buttons[1].setText(questionFromServer.getAnswerTwo());
                    buttons[2].setText(questionFromServer.getAnswerThree());
                    buttons[3].setText(questionFromServer.getAnswerFour());
                    System.out.println("correct answer: "+questionFromServer.getCorrectAnswer());
                    rightAnswer = questionFromServer.getCorrectAnswer();
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setEnabled(true);
                    }
                }
                else if(dataFromServer instanceof Category){
                    categoryFromServer = (Category) dataFromServer;
                    showQuestion.setText(categoryFromServer.getChooseCat());
                    buttons[0].setText(categoryFromServer.getCat1());
                    buttons[1].setText(categoryFromServer.getCat2());
                    buttons[2].setText(categoryFromServer.getCat3());
                    buttons[3].setText(categoryFromServer.getCat4());
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setEnabled(true);
                        buttons[i].setBackground(Color.white);
                    }
                }
                else if(dataFromServer instanceof String){
                    String stringFromServer = (String) dataFromServer;
                    //setTitle(stringFromServer);
                    System.out.println(stringFromServer);
                    if(((String) dataFromServer).startsWith("RoundScore")){
                        pointsOpponent = Integer.parseInt(((String) dataFromServer).substring(10));
                        System.out.println("pointsOpponent is: "+pointsOpponent);
                        //showQuestion.setText(points +" : "+pointsOpponent);
                        JOptionPane.showMessageDialog(this,"Current result:\n" + points + " : " + pointsOpponent,"Round "+ round + " is done",JOptionPane.INFORMATION_MESSAGE);
                        round++;
                        for(JButton btn:buttons){
                            btn.setText("");
                        }
                    }

                    //When game is over send points
                    if(((String) dataFromServer).equals("Gameover")){
                        oos.writeObject(""+points);
                        System.out.println("Send: "+points);
                        //pointsOpponent = Integer.parseInt((String)in.readObject());
                        if(points > pointsOpponent)
                            showQuestion.setText("Servers over\nFinal Result:\n"+ points +" : "+pointsOpponent + "\nYou are winner!");
                        if(points < pointsOpponent)
                            showQuestion.setText("Servers over\nFinal Result:\n"+ points +" : "+pointsOpponent + "\nYou lose!");
                        if(points == pointsOpponent)
                            showQuestion.setText("Servers over\nFinal Result:\n"+ points +" : "+pointsOpponent + "\nEven!");
                        for(JButton btn:buttons){
                            btn.setText("");
                        }
                    }
                    if(((String) dataFromServer).startsWith("Other")){
                        showQuestion.setText((String) dataFromServer);
                        for (int i = 0; i < buttons.length; i++) {
                            buttons[i].setEnabled(false);
                            buttons[i].setBackground(Color.white);

                        }
                    }else
                        for (int i = 0; i < buttons.length; i++) {
                            buttons[i].setEnabled(true);
                            buttons[i].setBackground(Color.white);
                        }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ActionListener sendAnswer = e -> {
        JButton button=(JButton) e.getSource();
        //client chose right answer
        if(rightAnswer != null) {
            if (button.getText().equals(rightAnswer)) {
                button.setBackground(Color.green);
                points++;
                clientSendPoints();
            }//Client chose wrong answer
            else {
                button.setBackground(Color.red);
                for (JButton btn : buttons) {
                    if (btn.getText().equals(rightAnswer)) {
                        btn.setBackground(Color.green);
                        break;
                    }
                }
                clientSendPoints();
                rightAnswer = null;
            }
        }
        else{
            try {
                oos.writeObject(button.getText());
                oos.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        new Client();
    }

    void clientSendPoints(){
        try {
            //send player's points
            oos.writeObject(""+points);
            oos.flush();
            System.out.println("Player's points on client side: "+points);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}