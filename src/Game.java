import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Game extends Thread {

    private static final int SELECTING_CATEGORY = 0;
    private static final int ASKING_QUESTIONS = 1;
    private static final int SWITCH_PLAYER = 2;
    private static final int ALL_QUESTIONS_ANSWERED = 3;
    int currentState = SELECTING_CATEGORY;

    Server nuvarandeSpelare;
    private int currentRound = 0;

    List<Question> spel = new LinkedList<>();
    public void setNuvarandeSpelare(Server nuvarandeSpelare) {
        this.nuvarandeSpelare = nuvarandeSpelare;
    }
    Xml xml = new Xml();
    @Override
    public void run() {
        String questionSpel1 = xml.getQuestionsByCategory("category1").get(0);
        Question q = new Question(xml.getAnswersByQuestion(questionSpel1).get(0),
                xml.getAnswersByQuestion(questionSpel1).get(1),
                xml.getAnswersByQuestion(questionSpel1).get(2),
                xml.getAnswersByQuestion(questionSpel1).get(3),
                xml.getAnswersByQuestion(questionSpel1).get(4),
                xml.getAnswersByQuestion(questionSpel1).get(4)
        );
        String questionSpel2 = xml.getQuestionsByCategory("category1").get(1);
        Question qq =new Question(xml.getAnswersByQuestion(questionSpel2).get(0),
                xml.getAnswersByQuestion(questionSpel2).get(1),
                xml.getAnswersByQuestion(questionSpel2).get(2),
                xml.getAnswersByQuestion(questionSpel2).get(3),
                xml.getAnswersByQuestion(questionSpel2).get(4),
                "e");
        String questionSpel3 = xml.getQuestionsByCategory("category1").get(2);
        Question qqq = new Question(xml.getAnswersByQuestion(questionSpel3).get(0),
                xml.getAnswersByQuestion(questionSpel3).get(1),
                xml.getAnswersByQuestion(questionSpel3).get(2),
                xml.getAnswersByQuestion(questionSpel3).get(3),
                xml.getAnswersByQuestion(questionSpel3).get(4),
                "e");
        String questionSpel4 = xml.getQuestionsByCategory("category1").get(3);
        Question qqqq = new Question(xml.getAnswersByQuestion(questionSpel4).get(0),
                xml.getAnswersByQuestion(questionSpel4).get(1),
                xml.getAnswersByQuestion(questionSpel4).get(2),
                xml.getAnswersByQuestion(questionSpel4).get(3),
                xml.getAnswersByQuestion(questionSpel4).get(4),
                "e");


        spel.add(q);
        spel.add(qq);
        spel.add(qqq);
        spel.add(qqqq);
        try {
            while (true) {

                if (currentState == SELECTING_CATEGORY){
                    nuvarandeSpelare.opponent.oos.writeObject("Other player is choosing category ?");
                    Object sel = nuvarandeSpelare.ois.readObject();
                    String selectedCategory= (String) sel;
                    System.out.println("Cat "+selectedCategory);
                    currentState = ASKING_QUESTIONS;

                }else if (currentState == ASKING_QUESTIONS){
                    sendQuestion(spel);
                    System.out.println("Atef");
                    currentState = SWITCH_PLAYER;


                }else if (currentState == SWITCH_PLAYER){
                    nuvarandeSpelare=nuvarandeSpelare.opponent;
                    sendQuestion(spel);

                    if (currentRound > 0){
                        currentState = ALL_QUESTIONS_ANSWERED;
                    }
                }else if (currentState == ALL_QUESTIONS_ANSWERED){
                    JOptionPane.showMessageDialog(null,"YOU WIN");
                }



            }


            //TODO : fix the database
            //-Skapa en metod som tar in en String och returnerar en lista i databasen/hÃ¤r
            // - questions=database.getQuestionLisT(selectedCategory);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    private void choosingCategory() throws IOException, ClassNotFoundException {
//        nuvarandeSpelare.oos.writeObject("Choose category :");
//        String category = (String) nuvarandeSpelare.ois.readObject();
//        nuvarandeSpelare.game.
//    }

    public synchronized void selectCategory(String categoryName) {

        currentRound++;
    }

    private void sendQuestion(List<Question> list) throws IOException, ClassNotFoundException {
        int  counter = 0;
        Object obj;
        while (counter < 2) {
            System.out.println(counter+" before");
            nuvarandeSpelare.oos.writeObject(list.get(counter));
            obj = nuvarandeSpelare.ois.readObject();
            String answer = (String) obj;
            System.out.println(answer);
            counter++;
            System.out.println("after "+counter);

        }
    }
}