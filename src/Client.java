import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;

class Client extends JFrame implements ActionListener,Runnable {

    // Swing stuff
    JPanel panel = new JPanel();
    JTextArea area = new JTextArea();
    JPanel buttonPanel = new JPanel();
    JPanel welcomePanel = new JPanel();
    JTextField userNameInput = new JTextField();
    JTextField roundsInput = new JTextField();
    JTextField questionsInput = new JTextField();
    JLabel welcomeText = new JLabel("QUIZGAME");
    JLabel userName = new JLabel("name: ");
    JLabel rounds = new JLabel("rounds: ");
    JLabel questions = new JLabel("Questions:");
    JButton connect = new JButton("CONNECT");
    JButton exit = new JButton("EXIT");
    Color colorbutton = new Color(13, 199, 253);
    private static JButton[] buttons = new JButton[4];


    // Server stuff
    int toPort = 12345;
    String hostName = Inet4Address.getLocalHost().getHostAddress();
    Socket socket = new Socket(hostName, toPort);
    Thread thread = new Thread(this);
    Protocoll pro = new Protocoll();

    public Client() throws IOException {
        this.thread.start();
        setSize(400, 200);
        add(panel);
        panel.setLayout(new BorderLayout());
        panel.setSize(400, 4);
        panel.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new GridLayout(4,2));
        buttonPanel.add(userName);
        buttonPanel.add(userNameInput);
        buttonPanel.add(rounds);
        buttonPanel.add(roundsInput);
        buttonPanel.add(questions);
        buttonPanel.add(questionsInput);
        buttonPanel.add(connect);
        buttonPanel.add(exit);
        add(welcomePanel,BorderLayout.NORTH);
        welcomePanel.add(welcomeText);
        welcomeText.setFont(new Font("arial", Font.PLAIN,40));

        setLocationRelativeTo(null);
        setVisible(true);

        connect.addActionListener(l ->{
            openCategoryChooserWindow();
            dispose();
        });


        exit.addActionListener(l ->{
            System.exit(0);
        });





    }

    public JPanel addQuestionButtons(){

        JPanel questionGridArray = new JPanel();
        buttons = new JButton[4];
        for (int i = 0; i < 4 ; i++) {
            buttons[i] = new JButton();
            questionGridArray.setLayout(new GridLayout(2,2));
            buttons[i].setPreferredSize(new Dimension(500,100));
            buttons[i].putClientProperty("column", i);
            buttons[i].addActionListener(this);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colorbutton);
            buttons[i].setText(pro.answerArray[i]);
            questionGridArray.add(buttons[i]);
        }
        return questionGridArray;
    }

    public JPanel addCategoryButtons(){

        JPanel categoryGridArray = new JPanel();
        buttons = new JButton[4];
        for (int i = 0; i < 4 ; i++) {
            buttons[i] = new JButton();
            categoryGridArray.setLayout(new GridLayout(2,2));
            buttons[i].setPreferredSize(new Dimension(500,100));
            buttons[i].putClientProperty("column", i);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colorbutton);
            buttons[i].setText("TEXT");
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int j = 0; j < 4; j++) {
                        if (buttons[j] == e.getSource()){

                        }
                    }
                }
            });
            categoryGridArray.add(buttons[i]);
        }
        return categoryGridArray;
    }


    public JFrame openQuestionWindow(){

        JFrame questionMainWindow = new JFrame();
        JPanel questionMainPanel = new JPanel();
        JPanel qbuttonPanel = new JPanel();


        questionMainWindow.add(questionMainPanel);
        questionMainPanel.setLayout(new BorderLayout());
        questionMainPanel.add(area, BorderLayout.CENTER);
        area.setEditable(false);
        area.setLineWrap(true);
        qbuttonPanel.setLayout(new GridLayout(2, 2));
        qbuttonPanel.setSize(400, 200);
        questionMainPanel.add(addQuestionButtons(), BorderLayout.SOUTH);
        area.setText(pro.infoFromServer2[0]);
        setLocationRelativeTo(null);
        area.setVisible(true);
        qbuttonPanel.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        return questionMainWindow;
    }

    public JFrame openCategoryChooserWindow(){

        JFrame categoryMainWindow = new JFrame();
        JPanel categoryMainPanel = new JPanel();
        JPanel textPanelNorth = new JPanel();
        JLabel textNorth = new JLabel("CATEGORY");

        categoryMainPanel.setLayout(new BorderLayout());
        categoryMainPanel.add(addCategoryButtons(),BorderLayout.CENTER);
        categoryMainPanel.add(textPanelNorth, BorderLayout.NORTH);
        textPanelNorth.add(textNorth);
        textNorth.setFont(new Font("Arial", Font.PLAIN,30));
        categoryMainWindow.add(categoryMainPanel);
        categoryMainWindow.setSize(400,500);
        categoryMainWindow.setLocationRelativeTo(null);
        categoryMainWindow.setVisible(true);


        return categoryMainWindow;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream()));
        ) {
            String message;
            String title = in.readLine();
            setTitle(title);

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