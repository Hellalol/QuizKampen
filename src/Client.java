import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements Runnable, ActionListener {
    Protocoll pro = new Protocoll();
    JPanel panel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JTextArea area = new JTextArea();
    Color colorbutton = new Color(30, 29, 37);
    Color colorarea = new Color(13, 199, 253);
    int toPort = 12345;
    String hostName = "172.20.201.51";
    Socket socket = new Socket(hostName, toPort);
    Thread thread = new Thread(this);
    private static JButton[] buttons = new JButton[4];

    public Client() throws IOException {

        Server chatServer = new Server();
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
        area.setBackground(colorarea);

        setLocationRelativeTo(null);

        area.setVisible(true);
        buttonPanel.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        this.thread.start();


    }

    public JPanel addButtons(){
        JPanel grid = new JPanel();
        buttons = new JButton[4];
        for (int i = 0; i < 4 ; i++) {
            buttons[i] = new JButton();
            grid.setLayout(new GridLayout(2,2));
            buttons[i].setPreferredSize(new Dimension(500,100));
            buttons[i].addActionListener(this);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colorbutton);
            buttons[i].setText(pro.getInfo(pro.answers,i));
            grid.add(buttons[i]);
        }
        return grid;
    }
    public String getRiddle(String[] riddles, int i){
        return riddles[i];
    }

    public static void main(String[] args) throws IOException {
        Client cc = new Client();
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
        for (int i = 0; i < 4 ; i++) {
            if(buttons[i] == e.getSource()){
                if(buttons[i].getText().equalsIgnoreCase("")) {
                    buttons[i].setBackground(Color.GREEN);
                }
            }

        }
    }
}