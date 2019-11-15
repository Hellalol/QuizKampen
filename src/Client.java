import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

class Client extends JFrame implements ActionListener,Runnable {

//    //all info ska komma in i en array i detta formatet
//    String[] infoFromServer2 = {"Sony Playstation blev Nintendos största konkurrent efter misslyckad samarbete. " +
//            "Men när släpptes Sony Playstation sin första spelkonsol?","1997","1991","2011","1994","3"};
//
//    //svar och rätt index i Buttonarrayen parsas in i en mindre array och en variabel
//    String[] answerArray = Arrays.copyOfRange(infoFromServer2, 1, 5);
//    int correctAnswerIndex = Integer.parseInt(infoFromServer2[5]);

    JPanel panel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JTextArea area = new JTextArea();
    Color colorbutton = new Color(13, 199, 253);
    private static JButton[] buttons = new JButton[4];
    int toPort = 12345;
    String hostName = "172.20.201.86";
    Socket socket = new Socket("localhost", toPort);
    Thread thread = new Thread(this);
    Protocoll pro = new Protocoll();

    public Client() throws IOException {

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
        area.setText(pro.infoFromServer2[0]);
        setLocationRelativeTo(null);
        area.setVisible(true);
        buttonPanel.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        this.thread.start();
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
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream()));
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                area.append(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Client cc = new Client();
    }
}