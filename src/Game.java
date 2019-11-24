
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Game extends Thread {

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_QUESTIONS = 1;
    private static final int SWITCH_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    int currentState = SELECTING_CATEGORY;
    int categoryCounter = 0;
    Server nuvarandeSpelare;

    // TODO: 2019-11-24 This is where we need Li's round and question "amountchooser"
    int questionAmount = 4;
    int roundAmount = 4;


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
                    System.out.println("SELECTING_CATEGORY");
                    nuvarandeSpelare.opponent.oos.writeObject("Other player is choosing category ?");
                    sendCategories();
                    checkIfGameHasEnded();
                    currentState = ASKING_QUESTIONS;

                } else if (currentState == ASKING_QUESTIONS) {
                    System.out.println("ASKING_QUESTIONS");
                    Object sel = nuvarandeSpelare.ois.readObject();
                    String selectedCategory = (String) sel;
                    System.out.println(selectedCategory);
                    spel = getCategoryList(selectedCategory);
                    sendQuestion(spel);
                    currentState = SWITCH_PLAYER;

                } else if (currentState == SWITCH_PLAYER) {
                    System.out.println("SWITCH_PLAYER");
                    nuvarandeSpelare = nuvarandeSpelare.opponent;
                    sendQuestion(spel);

                    currentState = SELECTING_CATEGORY;
                } else if (currentState == ALL_QUESTIONS_ANSWERED) {

                    // TODO: 2019-11-24 for each round add points to a list??
                    // todo make it so that it shows in the end of the game.
                    //nuvarandeSpelare.showScores();
                    //nuvarandeSpelare.opponent.showScores();

                    System.out.println("ALL_QUESTIONS_ANSWERED");

                    //Send scores
                }

            }

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

    private void sendCategories() throws IOException {
        nuvarandeSpelare.oos.writeObject(dbq.categories);
        categoryCounter++;
        System.out.println(categoryCounter);
    }

    private void checkIfGameHasEnded(){
        if (categoryCounter == roundAmount)
            currentState = ALL_QUESTIONS_ANSWERED;

    }

    private void sendQuestion(List<Question> list) throws IOException, ClassNotFoundException {
        int counter = 0;
        Object obj;
        while (counter < questionAmount) {
            nuvarandeSpelare.oos.writeObject(list.get(counter));
            obj = nuvarandeSpelare.ois.readObject();
            String answer = (String) obj;
            counter++;


        }
    }
}