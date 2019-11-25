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

    public Client1(){
        String namn=JOptionPane.showInputDialog("Skriv ditt namn!");
        if(namn == null)
            System.exit(0);
        setLayout(new BorderLayout());
        try {
            socket=new Socket("localhost",12345);
            pw=new ObjectOutputStream(socket.getOutputStream());
            in=new ObjectInputStream(socket.getInputStream());
            pw.writeObject(namn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i]= new JButton("Vänta på andra spelaren");
            buttons[i].addActionListener(sendcorrectAnswer);
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
        showQuestion.setEditable(false);

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
                    showQuestion.setText(questionFromServer.getQuestion());
                    buttons[0].setText(questionFromServer.getAnswerOne());
                    buttons[1].setText(questionFromServer.getAnswerTwo());
                    buttons[2].setText(questionFromServer.getAnswerThree());
                    buttons[3].setText(questionFromServer.getAnswerFour());
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
                    }
                }
                else if(data instanceof String){
                    String stringFromServer = (String) data;
                    //setTitle(stringFromServer);
                    showQuestion.setText(stringFromServer);
                    System.out.println(stringFromServer);
                    if(((String) data).startsWith("Other")){
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

    ActionListener sendSelecetedAlternativ= e->{
        JButton button=(JButton) e.getSource();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
        }
        try {
            pw.writeObject(button.getText());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    };

    ActionListener sendcorrectAnswer = e -> {
        JButton button=(JButton) e.getSource();
        try {
            pw.writeObject(button.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    };



    public static void main(String[] args) {
        new Client1();
    }
}