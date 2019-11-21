

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client1 extends JFrame implements Runnable {
    Thread thread=new Thread(this);
    JLabel question=new JLabel("Question");
    JButton[] buttons=new JButton[4];
    JPanel buttonsPanel=new JPanel();
    ObjectOutputStream pw;
    ObjectInputStream in;
    Socket socket;
    public Client1(){
        String namn=JOptionPane.showInputDialog("Skriv ditt namn");
      //  String categoy=JOptionPane.showInputDialog("Choose category");
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
            buttons[i]=new JButton(i+"");
            buttons[i].addActionListener(sendSelecetedAlternativ);
            buttonsPanel.add(buttons[i]);
        }

        add(question,BorderLayout.NORTH);
        add(buttonsPanel,BorderLayout.SOUTH);

        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        thread.start();

    }
    Question questionFromServer;
    @Override
    public void run() {

        try {
                Object message;
                while((message=in.readObject())!=null){
                   if(message instanceof Question){
                       questionFromServer=(Question)  message;
                       question.setText(questionFromServer.getQuestion());
                       buttons[0].setText(questionFromServer.getAnswerOne());
                       buttons[1].setText(questionFromServer.getAnswerTwo());
                       buttons[2].setText(questionFromServer.getAnswerThree());
                       buttons[3].setText(questionFromServer.getAnswerFour());
                   }else if(message instanceof String){
                       String nnn=(String) message;
                       setTitle(nnn);
                       System.out.println(nnn);

                   }

                }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ActionListener sendSelecetedAlternativ= e->{
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
