

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
    JFrame chooseRoundAndQuestionAmount = new JFrame("Choose rounds and questions");
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

    String question_amount ="+";
    String round_amount ="-";
    int points;
    public Client1(){
        String namn=JOptionPane.showInputDialog("Skriv ditt namn");
      //  String categoy=JOptionPane.showInputDialog("Choose category");
        setLayout(new BorderLayout());
        try {
            socket=new Socket("localhost",12345);
            pw=new ObjectOutputStream(socket.getOutputStream());
            in=new ObjectInputStream(socket.getInputStream());
            pw.writeObject(namn);
            pw.flush();
            //For user to choose amount of questions and rounds
            chooseRoundAndQuestionAmount.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i]=new JButton(i+"");
            buttons[i].addActionListener(sendcorretAnswer);
            buttonsPanel.add(buttons[i]);
        }


        add(question,BorderLayout.NORTH);
        add(buttonsPanel,BorderLayout.SOUTH);


        setSize(500,500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        thread.start();

        //chooseRoundAndQuestionAmount frame handling

        chooseRoundAndQuestionAmount.setSize(500,500);
        chooseRoundAndQuestionAmount.setDefaultCloseOperation(EXIT_ON_CLOSE);

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
        chooseRoundAndQuestionAmount.add(option_zone,BorderLayout.NORTH);
        chooseRoundAndQuestionAmount.add(btn_confirm,BorderLayout.SOUTH);

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
            try {
                String message = "R&Q@"+round_amount+"@"+question_amount;
                pw.writeObject(message);
                System.out.println(message);
                pw.flush();

                //Show this frame after confirm button
                chooseRoundAndQuestionAmount.setVisible(false);
                setVisible(true);


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }
    Question questionFromServer;


    @Override
    public void run() {

        try {
                Object message;
                while((message=in.readObject())!=null){
                   if(message instanceof Question) {
                       questionFromServer = (Question) message;
                       question.setText(questionFromServer.getQuestion());
                       buttons[0].setText(questionFromServer.getAnswerOne());
                       buttons[1].setText(questionFromServer.getAnswerTwo());
                       buttons[2].setText(questionFromServer.getAnswerThree());
                       buttons[3].setText(questionFromServer.getAnswerFour());
                       for (int i = 0; i < buttons.length; i++) {
                           buttons[i].setEnabled(true);
                       }

                   }

                  else if(message instanceof String){
                       String nnn=(String) message;
                       setTitle(nnn);
                       System.out.println(nnn);
                      if(((String) message).startsWith("Other")){
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

    ActionListener sendcorretAnswer = e -> {
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
