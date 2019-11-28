import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements Runnable {

    Thread thread=new Thread(this);

    JButton[] buttons=new JButton[4];
    JPanel buttonsPanel=new JPanel();
    JPanel showQuestionPanel = new JPanel(new GridLayout(1,2,10,0));
    JTextArea showQuestion = new JTextArea("Vänta på motståndare!",8,100);
    JTextArea showResults = new JTextArea();
    ObjectOutputStream pw;
    ObjectInputStream in;
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    Socket socket;
    Question questionFromServer;
    Category categoryFromServer;
    int points;
    int pointsOpponent;
    int finalScorePlayer1;
    int finalScorePlayer2;
    String rightAnswer;
    int round = 1;

    public Client(){
        String namn=JOptionPane.showInputDialog("Skriv ditt namn!");
        if(namn == null)
            System.exit(0);
        //  String categoy=JOptionPane.showInputDialog("Choose category");
        setLayout(new BorderLayout());
        setSize(550,285);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
            buttons[i]= new JButton();
            buttons[i].setVisible(false);
            buttons[i].addActionListener(sendAnswer);
            buttons[i].setBackground(Color.white);
            buttons[i].setBorder(border);
            buttons[i].setFont(new Font("arial",Font.PLAIN,20));
            buttonsPanel.setLayout(new GridLayout(2,2,10,10));
            buttonsPanel.add(buttons[i]);
        }
        setLocationRelativeTo(null);
        showQuestion.setWrapStyleWord(true);
        showQuestion.setLineWrap(true);
        showQuestion.setFont(new Font("arial",Font.PLAIN,15));
        add(showQuestionPanel,BorderLayout.NORTH);
        showQuestionPanel.add(showQuestion);
        showQuestionPanel.add(showResults);
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(buttonsPanel,BorderLayout.SOUTH);
        showResults.setBorder(border);
        showQuestionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        showResults.setFont(new Font("arial", Font.PLAIN, 15));
        showQuestion.setBorder(border);

        setVisible(true);
        thread.start();
    }


    @Override
    public void run() {
        try {
            Object dataFromServer;
            while((dataFromServer = in.readObject()) != null){
                if(dataFromServer instanceof Question) {
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    questionFromServer = (Question) dataFromServer;

                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setBackground(Color.white);
                        buttons[i].setForeground(Color.black);
                        buttons[i].setBorder(border);
                        buttons[i].setFont(new Font("arial", Font.PLAIN, 20));
                        buttonsPanel.setLayout(new GridLayout(2, 2, 10, 16));
                        buttonsPanel.add(buttons[i]);
                    }
                    showQuestion.setText(questionFromServer.getQuestion());
                    makeButtonsVisible();
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
                    makeButtonsVisible();
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
                    System.out.println(stringFromServer);
                    if(((String) dataFromServer).startsWith("RoundScore")){
                        pointsOpponent = Integer.parseInt(((String) dataFromServer).substring(10));
                        System.out.println("pointsOpponent is: "+pointsOpponent);
                        //showQuestion.setText(points +" : "+pointsOpponent);
                        JOptionPane.showMessageDialog(this,
                                "Current result:\n" + points + " : " + pointsOpponent,
                                "Round "+ round + " is done",
                                JOptionPane.INFORMATION_MESSAGE);
                        showResults.append("runda " + round + ": "+ "[ " + points + " | " + pointsOpponent + " ]\n");
                        finalScorePlayer1 += points;
                        finalScorePlayer2 += pointsOpponent;
                        points = 0;
                        pointsOpponent = 0;
                        round++;
                        for(JButton btn:buttons){
                            btn.setText("");
                        }
                    }

                    if(((String) dataFromServer).startsWith("Andra")){
                        showQuestion.setText((String) dataFromServer);
                        if (showQuestion.getText().equals("Andra spelarens tur!")){
                            for (int i = 0; i < buttons.length ; i++) {
                                buttons[i].setVisible(false);
                            }
                        }
                    }

                    //When game is over send points
                    if(((String) dataFromServer).equals("Gameover")){
                        pw.writeObject(""+points);
                        pw.flush();
                        if(finalScorePlayer1 > finalScorePlayer2)
                            showQuestion.setText("Game is over\nFinal Result:\n"+ finalScorePlayer1 +" : "+finalScorePlayer2 + "\nYou are winner!");
                        if(finalScorePlayer1 < finalScorePlayer2)
                            showQuestion.setText("Game is over\nFinal Result:\n"+ finalScorePlayer1 +" : "+finalScorePlayer2 + "\nYou lose!");
                        if(finalScorePlayer1 == finalScorePlayer2)
                            showQuestion.setText("Game is over\nFinal Result:\n"+ finalScorePlayer1 +" : "+finalScorePlayer2 + "\nEven!");
                        for(JButton btn:buttons){
                            btn.setVisible(false);
                        }
                    }
                    if(((String) dataFromServer).startsWith("Other")){
                        showQuestion.setText((String) dataFromServer);
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
        JButton button=(JButton) e.getSource();
        //client chose right answer
        if(rightAnswer != null) {
            if (button.getText().equals(rightAnswer)) {
                button.setBackground(Color.green);
                points++;
            }//Client chose wrong answer
            else {
                button.setBackground(Color.red);
                for (JButton btn : buttons) {
                    if (btn.getText().equals(rightAnswer)) {
                        btn.setBackground(Color.green);
                        break;
                    }
                }
            }
            clientSendPoints();
            rightAnswer = null;

        }
        else{
            try {
                pw.writeObject(button.getText());
                pw.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };


    void makeButtonsVisible(){
        buttons[0].setVisible(true);
        buttons[1].setVisible(true);
        buttons[2].setVisible(true);
        buttons[3].setVisible(true);
    }

    public static void main(String[] args) {
        new Client();
    }

    void clientSendPoints(){
        try {
            //send player's points
            pw.writeObject(""+points);
            pw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}