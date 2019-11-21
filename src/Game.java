import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Game extends Thread {

    Server nuvarandeSpelare;
    int rounds;
    List<Question> questions;
    public void setNuvarandeSpelare(Server nuvarandeSpelare) {
        this.nuvarandeSpelare = nuvarandeSpelare;
    }
    DatabaseQuestions database=new DatabaseQuestions();
    @Override
    public void run() {
         Question q=new Question("Hur gammal är Fazli: ","18","19","20","21","21");
        try {
            while (true) {
                Object sel=nuvarandeSpelare.in.readObject();
                String selectedCategory=(String)sel;
                System.out.println(selectedCategory);

                nuvarandeSpelare.pw.writeObject(q);
                System.out.println(q.getQuestion());
                nuvarandeSpelare=nuvarandeSpelare.opponent;
            }


            //TODO : fix the database
            //-Skapa en metod som tar in en String och returnerar en lista i databasen/här
            // - questions=database.getQuestionLisT(selectedCategory);





        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
