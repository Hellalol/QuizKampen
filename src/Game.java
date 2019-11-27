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
    int opponentPoints;

    int totalPoints;
    int totalPointsOpponent;

    Database database = new Database();
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
                    //Server sends the 4 Categories and jump to next state
                    sendCategories();
                    //checkIfGameHasEnded();
                    currentState = ASKING_QUESTIONS;

                } else if (currentState == ASKING_QUESTIONS) {
                    System.out.println("ASKING_QUESTIONS");
                    //Read category chosen by the player
                    Object sel = nuvarandeSpelare.ois.readObject();
                    String selectedCategory = (String) sel;
                    System.out.println("Chosen category is: "+selectedCategory);
                    //Get all questions under one category
                    spel = getCategoryList(selectedCategory);
                    //Send questions to client and keep on collecting score
                    sendQuestion(spel);
                    System.out.println("received points is: " + points);
                    currentState = SWITCH_PLAYER;

                } else if (currentState == SWITCH_PLAYER) {
                    System.out.println("SWITCH_PLAYER");
                    //Swap player
                    nuvarandeSpelare.oos.writeObject("Andra spelarens tur!");
                    nuvarandeSpelare = nuvarandeSpelare.opponent;
                    opponentPoints = points;
                    points = 0;
                    sendQuestion(spel);
                    //server sends the opponentPoints to the new current player
                    nuvarandeSpelare.oos.writeObject("RoundScore" + opponentPoints);
                    //server sends the updated new points to the previous player
                    nuvarandeSpelare.opponent.oos.writeObject("RoundScore" + points);
                    System.out.println("One round is done, send my points: " + points);
                    //Send questions to client and get client's answer
                    checkIfGameHasEnded();
                    //currentState = SELECTING_CATEGORY;

                    //Send points......
                } else if (currentState == ALL_QUESTIONS_ANSWERED) {
                    //Send totalPoints to opponent
                    //totalPoints = Integer.parseInt((String)nuvarandeSpelare.ois.readObject());
                    totalPoints = (Integer)nuvarandeSpelare.ois.readObject();
                    totalPointsOpponent = (Integer) nuvarandeSpelare.opponent.ois.readObject();
                    System.out.println("Server totalPoints: "+totalPoints+" "+totalPointsOpponent);

                    //totalPointsOpponent = (Integer) nuvarandeSpelare.opponent.ois.readObject();
                    nuvarandeSpelare.opponent.oos.writeObject(totalPoints);
                    nuvarandeSpelare.oos.writeObject(totalPointsOpponent);
                    System.out.println("-->" + totalPoints);
                    System.out.println("-->" + totalPointsOpponent);
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
            tempList = database.spelCategory;
        else if (input.equalsIgnoreCase("sport"))
            tempList = database.sportCategory;
        else if (input.equalsIgnoreCase("java")){
            tempList = database.javaCategory;
        }else if(input.equalsIgnoreCase("teknik")){
            tempList = database.teknikCategory;
        }
        return tempList;
    }

    //Send the 4 Categories and jump to next state
    private void sendCategories() throws IOException {
        nuvarandeSpelare.oos.writeObject(database.categories);
        categoryCounter++;
        System.out.println("Current round:"+categoryCounter+" of "+roundAmount);
    }

    private void checkIfGameHasEnded() throws IOException {
        if (categoryCounter == roundAmount){
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
            //Read player's points
            points = Integer.parseInt((String)nuvarandeSpelare.ois.readObject());
            System.out.println("Current player's points: "+points);
            counter++;
        }
    }
}