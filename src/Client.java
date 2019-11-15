import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Arrays;

class GUI extends JFrame implements ActionListener {

    String[] info = {"När släpptes SNES?","2011","1991","1984","1990","1"};
    String[] svarArray = Arrays.copyOfRange(info, 1, 5);
    int correctAnswer = Integer.parseInt(info[5]);


    JPanel panel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JTextArea area = new JTextArea();
    Color colorarea = new Color(30, 29, 37);
    Color colorbutton = new Color(13, 199, 253);
    private static JButton[] buttons = new JButton[4];

    public GUI() {
        setSize(400, 439);
        add(panel);
        panel.setSize(400, 400);
        panel.setLayout(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);
        area.setEditable(false);
        area.setLineWrap(true);
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.setSize(400, 200);
        panel.add(addButtons(), BorderLayout.SOUTH);
        area.setText(info[0]);
        setLocationRelativeTo(null);
        area.setVisible(true);
        buttonPanel.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }

    public JPanel addButtons(){
        JPanel grid = new JPanel();
        buttons = new JButton[4];
        for (int i = 0; i < 4 ; i++) {
            buttons[i] = new JButton();
            grid.setLayout(new GridLayout(2,2));
            buttons[i].setPreferredSize(new Dimension(500,100));
            buttons[i].putClientProperty("column", i);
            buttons[i].addActionListener(this);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colorbutton);
            buttons[i].setText(svarArray[i]);
            grid.add(buttons[i]);
        }
        return grid;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton btn = (JButton) e.getSource();
        System.out.println("clicked column " + btn.getClientProperty("column"));

        for (int i = 0; i < 4 ; i++) {
            if(buttons[i] == e.getSource()){
                if ((int)btn.getClientProperty("column") == correctAnswer){
                    buttons[i].setBackground(Color.green);
                    //Skickar info till server RÄTT
                }else
                    buttons[i].setBackground(Color.red);
                //Skickar info till server FEL

            }

        }
    }

    public static void main(String[] args) throws IOException {
        GUI cc = new GUI();
    }
}