
import org.w3c.dom.ls.LSOutput;

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
    private QuestionAndRound questionAndRound = new QuestionAndRound("roundAndQuestions.properties");


    int questionAmount = questionAndRound.getQuestionAmount();
    int roundAmount = questionAndRound.getRoundAmount();

    int points;

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
                    //Player 1 sever sends the 4 Categories and jump to next state
                    sendCategories();
                    checkIfGameHasEnded();
                    //currentState = ASKING_QUESTIONS;

                } else if (currentState == ASKING_QUESTIONS) {
                    System.out.println("ASKING_QUESTIONS");
                    //Read category chosen by player1
                    Object sel = nuvarandeSpelare.ois.readObject();
                    String selectedCategory = (String) sel;
                    System.out.println("Chosen category is: "+selectedCategory);
                    //Get all questions under one category
                    spel = getCategoryList(selectedCategory);
                    //Send questions to client and get client's answer
                    sendQuestion(spel);
                    currentState = SWITCH_PLAYER;

                } else if (currentState == SWITCH_PLAYER) {
                    System.out.println("SWITCH_PLAYER");
                    //categoryCounter = 0;
                    points = Integer.parseInt((String)nuvarandeSpelare.ois.readObject());
                    System.out.println("One round is done, to send points: " + points);
                    nuvarandeSpelare.opponent.oos.writeObject("RoundScore"+points);
                    nuvarandeSpelare = nuvarandeSpelare.opponent;
                    //Send questions to client and get client's answer
                    checkIfGameHasEnded();
                    //currentState = SELECTING_CATEGORY;

                    //Send points......
                } else if (currentState == ALL_QUESTIONS_ANSWERED) {
                    points = Integer.parseInt((String)nuvarandeSpelare.ois.readObject());
                    System.out.println("-->" + points);
                    nuvarandeSpelare.opponent.oos.writeObject(""+points);
                    System.out.println("ALL_QUESTIONS_ANSWERED");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

        //Get all questions under one category
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

    //Send the 4 Categories and jump to next state
    private void sendCategories() throws IOException {
        nuvarandeSpelare.oos.writeObject(dbq.categories);
        categoryCounter++;
        System.out.println("Current round:"+categoryCounter+" of "+roundAmount);
    }

    private void checkIfGameHasEnded() throws IOException {
        if (categoryCounter > roundAmount){
            nuvarandeSpelare.oos.writeObject("Gameover");
            nuvarandeSpelare.opponent.oos.writeObject("Gameover");
            currentState = ALL_QUESTIONS_ANSWERED;
        } else if(currentState == ASKING_QUESTIONS) {
            System.out.println("!it runs here!");
            currentState = SWITCH_PLAYER;
        }else if (currentState == SELECTING_CATEGORY) {
            System.out.println("!!it runs here!!");
            currentState = ASKING_QUESTIONS;
        } else if (currentState == SWITCH_PLAYER) {
            System.out.println("!!!it runs here!!!");
            currentState = SELECTING_CATEGORY;
        }
    }

    //player1 server send questions to player1's client
    private void sendQuestion(List<Question> list) throws IOException, ClassNotFoundException {
        int counter = 0;
        System.out.println("question amount is: " + questionAmount);
        while (counter < questionAmount) {
            nuvarandeSpelare.oos.writeObject(list.get(counter));
            //Read player1's points
            points += Integer.parseInt((String)nuvarandeSpelare.ois.readObject());
            counter++;
        }
    }
}