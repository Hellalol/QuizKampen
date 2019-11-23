import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatabaseQuestions {

    Xml xmlInfo = new Xml();

    protected List<Question> spelCategory = new LinkedList<>();
    protected List<Question> sport = new LinkedList<>();
    protected List<Question> javaa = new LinkedList<>();
    protected List<Question> teknik = new LinkedList<>();

    DatabaseQuestions() {

        String questionSpel1 = xmlInfo.getQuestionsByCategory("category1").get(0);
        Question answersAndQuestionsSpel1 = new Question(xmlInfo.getAnswersByQuestion(questionSpel1).get(0),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(1),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(2),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(3),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(4),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(4)
        );

        String questionSpel2 = xmlInfo.getQuestionsByCategory("category1").get(1);
        Question answersAndQuestionsSpel2 = new Question(xmlInfo.getAnswersByQuestion(questionSpel2).get(0),
                xmlInfo.getAnswersByQuestion(questionSpel2).get(1),
                xmlInfo.getAnswersByQuestion(questionSpel2).get(2),
                xmlInfo.getAnswersByQuestion(questionSpel2).get(3),
                xmlInfo.getAnswersByQuestion(questionSpel2).get(4),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(4));

        String questionSpel3 = xmlInfo.getQuestionsByCategory("category1").get(2);
        Question answersAndQuestionsSpel3 = new Question(xmlInfo.getAnswersByQuestion(questionSpel3).get(0),
                xmlInfo.getAnswersByQuestion(questionSpel3).get(1),
                xmlInfo.getAnswersByQuestion(questionSpel3).get(2),
                xmlInfo.getAnswersByQuestion(questionSpel3).get(3),
                xmlInfo.getAnswersByQuestion(questionSpel3).get(4),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(4));
        String questionSpel4 = xmlInfo.getQuestionsByCategory("category1").get(3);
        Question answersAndQuestionsSpel4 = new Question(xmlInfo.getAnswersByQuestion(questionSpel4).get(0),
                xmlInfo.getAnswersByQuestion(questionSpel4).get(1),
                xmlInfo.getAnswersByQuestion(questionSpel4).get(2),
                xmlInfo.getAnswersByQuestion(questionSpel4).get(3),
                xmlInfo.getAnswersByQuestion(questionSpel4).get(4),
                xmlInfo.getAnswersByQuestion(questionSpel1).get(4));


        spelCategory.add(answersAndQuestionsSpel1);
        spelCategory.add(answersAndQuestionsSpel2);
        spelCategory.add(answersAndQuestionsSpel3);
        spelCategory.add(answersAndQuestionsSpel4);

        System.out.println(spelCategory.get(0).getQuestion());

    }
    public List<Question> loadCategorylistSpel(){
        return this.spelCategory;
    }

    public static void main(String[] args) {
        DatabaseQuestions db = new DatabaseQuestions();
    }
}
