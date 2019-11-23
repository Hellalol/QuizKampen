import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Game extends Thread {

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_QUESTIONS = 1;
    private static final int SWITCH_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    int currentState = SELECTING_CATEGORY;

    Server nuvarandeSpelare;
    int questionAmount;
    int roundAmount;
    DatabaseQuestions dbq = new DatabaseQuestions();

    List<Question> spel = new LinkedList<>();

    public void setNuvarandeSpelare(Server nuvarandeSpelare) {
        this.nuvarandeSpelare = nuvarandeSpelare;
    }

    @Override
    public void run() {
        try {
            while (true) {


                if (currentState == SELECTING_CATEGORY) {
                    nuvarandeSpelare.opponent.oos.writeObject("Other player is choosing category ?");
                    Object sel = nuvarandeSpelare.ois.readObject();
                    String selectedCategory = (String) sel;
                    spel = getCategoryList(selectedCategory);
                    currentState = ASKING_QUESTIONS;

                } else if (currentState == ASKING_QUESTIONS) {
                    sendQuestion(spel);
                    System.out.println("Atef");
                    currentState = SWITCH_PLAYER;
                } else if (currentState == SWITCH_PLAYER) {
                    nuvarandeSpelare = nuvarandeSpelare.opponent;
                    sendQuestion(spel);
                    currentState= SELECTING_CATEGORY;


                } else if (currentState == ALL_QUESTIONS_ANSWERED) {
                    JOptionPane.showMessageDialog(null, "YOU WIN");
                }

            }


            //TODO : fix the database
            //-Skapa en metod som tar in en String och returnerar en lista i databasen/hÃ¤r
            // - questions=database.getQuestionLisT(selectedCategory);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Question> getCategoryList(String input){
        List<Question> tempList = new LinkedList<>();
        if (input.equalsIgnoreCase("spel"))
            tempList = dbq.spelCategory;
        else if (input.equalsIgnoreCase("sport"))
            tempList = dbq.sportCategory;
        else if (input.equalsIgnoreCase("java")){
            tempList = dbq.javaCategory;
        }else if(input.equalsIgnoreCase("teknik")){
            tempList = dbq.teknikCategory;
        }
        return tempList;
    }


    private void sendQuestion(List<Question> list) throws IOException, ClassNotFoundException {
        int counter = 0;
        Object obj;
        while (counter < 4) {
            System.out.println(counter + " before");
            nuvarandeSpelare.oos.writeObject(list.get(counter));
            obj = nuvarandeSpelare.ois.readObject();
            String answer = (String) obj;
            System.out.println(answer);
            counter++;
            System.out.println("after " + counter);

        }
    }
}