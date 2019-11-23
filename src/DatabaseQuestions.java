import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DatabaseQuestions {

    Properties properties;
    Map<String,String> all = new LinkedHashMap<>();

    protected List<Question> spelCategory = new LinkedList<>();
    protected List<Question> sportCategory = new LinkedList<>();
    protected List<Question> javaCategory = new LinkedList<>();
    protected List<Question> teknikCategory = new LinkedList<>();

    DatabaseQuestions() {
        properties = new Properties();
        getInfoFromAllXML();

        String questionsport1 = getQuestionsByCategory("category2").get(0);
        Question answersAndQuestionsSport1 = new Question(getAnswersByQuestion(questionsport1).get(0),
                getAnswersByQuestion(questionsport1).get(1),
                getAnswersByQuestion(questionsport1).get(2),
                getAnswersByQuestion(questionsport1).get(3),
                getAnswersByQuestion(questionsport1).get(4),
                getAnswersByQuestion(questionsport1).get(4)
        );

        String questionsport2 = getQuestionsByCategory("category2").get(1);
        Question answersAndQuestionsSport2 = new Question(getAnswersByQuestion(questionsport2).get(0),
                getAnswersByQuestion(questionsport2).get(1),
                getAnswersByQuestion(questionsport2).get(2),
                getAnswersByQuestion(questionsport2).get(3),
                getAnswersByQuestion(questionsport2).get(4),
                getAnswersByQuestion(questionsport2).get(4));

        String questionsport3 = getQuestionsByCategory("category2").get(2);
        Question answersAndQuestionsSport3 = new Question(getAnswersByQuestion(questionsport3).get(0),
                getAnswersByQuestion(questionsport3).get(1),
                getAnswersByQuestion(questionsport3).get(2),
                getAnswersByQuestion(questionsport3).get(3),
                getAnswersByQuestion(questionsport3).get(4),
                getAnswersByQuestion(questionsport3).get(4));

        String questionsport4 = getQuestionsByCategory("category2").get(3);
        Question answersAndQuestionsSport4 = new Question(getAnswersByQuestion(questionsport4).get(0),
                getAnswersByQuestion(questionsport4).get(1),
                getAnswersByQuestion(questionsport4).get(2),
                getAnswersByQuestion(questionsport4).get(3),
                getAnswersByQuestion(questionsport4).get(4),
                getAnswersByQuestion(questionsport4).get(4));

        sportCategory.add(answersAndQuestionsSport1);
        sportCategory.add(answersAndQuestionsSport2);
        sportCategory.add(answersAndQuestionsSport3);
        sportCategory.add(answersAndQuestionsSport4);


        // SPEL
        String questionSpel1 = getQuestionsByCategory("category1").get(0);
        Question answersAndQuestionsSpel1 = new Question(getAnswersByQuestion(questionSpel1).get(0),

                getAnswersByQuestion(questionSpel1).get(1),
                getAnswersByQuestion(questionSpel1).get(2),
                getAnswersByQuestion(questionSpel1).get(3),
                getAnswersByQuestion(questionSpel1).get(4),
                getAnswersByQuestion(questionSpel1).get(4)
        );

        String questionSpel2 = getQuestionsByCategory("category1").get(1);
        Question answersAndQuestionsSpel2 = new Question(getAnswersByQuestion(questionSpel2).get(0),
                getAnswersByQuestion(questionSpel2).get(1),
                getAnswersByQuestion(questionSpel2).get(2),
                getAnswersByQuestion(questionSpel2).get(3),
                getAnswersByQuestion(questionSpel2).get(4),
                getAnswersByQuestion(questionSpel1).get(4));

        String questionSpel3 = getQuestionsByCategory("category1").get(2);
        Question answersAndQuestionsSpel3 = new Question(getAnswersByQuestion(questionSpel3).get(0),
                getAnswersByQuestion(questionSpel3).get(1),
                getAnswersByQuestion(questionSpel3).get(2),
                getAnswersByQuestion(questionSpel3).get(3),
                getAnswersByQuestion(questionSpel3).get(4),
                getAnswersByQuestion(questionSpel1).get(4));

        String questionSpel4 = getQuestionsByCategory("category1").get(3);
        Question answersAndQuestionsSpel4 = new Question(getAnswersByQuestion(questionSpel4).get(0),
                getAnswersByQuestion(questionSpel4).get(1),
                getAnswersByQuestion(questionSpel4).get(2),
                getAnswersByQuestion(questionSpel4).get(3),
                getAnswersByQuestion(questionSpel4).get(4),
                getAnswersByQuestion(questionSpel1).get(4));



        spelCategory.add(answersAndQuestionsSpel1);
        spelCategory.add(answersAndQuestionsSpel2);
        spelCategory.add(answersAndQuestionsSpel3);
        spelCategory.add(answersAndQuestionsSpel4);


    }
    public List<Question> loadCategorylistSpel(){
        return this.spelCategory;
    }

    List<String> getQuestionsByCategory(String category){
        List<String> questions = new ArrayList<>();
        for(Map.Entry<String,String> entry : all.entrySet()){
            if(entry.getValue().equals(category)){
                questions.add(entry.getKey());
            }
        }
        return questions;
    }

    //To get all questions under one question
    List<String> getAnswersByQuestion(String question){
        List<String> answers = new ArrayList<>();
        answers.add(question);
        for(Map.Entry<String,String> entry : all.entrySet()){
            if(entry.getValue().equals(question)){
                answers.add(entry.getKey());
            }
        }
        return answers;
    }

    //Random the answers and show the right answer index
    void randomAnswers(List<String> answers){
        String rightAnswer = answers.get(answers.size()-1);
        System.out.println("right answer: "+rightAnswer);
        for (int i = answers.size()-1; i >=1; i--) {
            //for (int i = answers.size()-1; i >=0; i--) {
            int random = (int) (Math.random()*i)+1;
            //int random = (int) (Math.random()*(i+1));
            String temp = answers.get(i);
            answers.set(i,answers.get(random));
            answers.set(random,temp);
        }
        System.out.println("answers after random: "+answers);
        for (int i = 0; i < answers.size() ; i++) {
            if(answers.get(i).equals(rightAnswer)){
                answers.add(""+(i-1));
                //answers.add(""+i);
                System.out.println("right index: "+i);
                break;
            }
        }
        System.out.println("answers :"+answers);
    }

    //Store all categories, questions and answers into a Map
    void getInfoFromAllXML(){
        InputStream in;
        for (int i = 1; i<=4 ; i++) {
            for(int j=1; j<=4 ; j++ ){
                in = getClass().getResourceAsStream("question"+i+"_"+j+".xml");
                try {
                    properties.loadFromXML(in);
                    String category = properties.getProperty("category"+i);
                    String question = properties.getProperty("question"+i+"_"+j);
                    String answer1 = properties.getProperty("answer"+i+"_"+j+"_1");
                    String answer2 = properties.getProperty("answer"+i+"_"+j+"_2");
                    String answer3 = properties.getProperty("answer"+i+"_"+j+"_3");
                    String rightAnswer = properties.getProperty("question"+i+"_"+j+"_right");
                    all.put(question,category);
                    all.put(answer1,question);
                    all.put(answer2,question);
                    all.put(answer3,question);
                    all.put(rightAnswer,question);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    List<Question> getCategoryList(String category){
        List<Question> tempList = new LinkedList<>();
        if (category.equalsIgnoreCase("spel"))
            tempList = spelCategory;
        else if (category.equalsIgnoreCase("sport"))
            tempList = sportCategory;
        else if (category.equalsIgnoreCase("java")){
            tempList = javaCategory;
        }else if(category.equalsIgnoreCase("teknik")){
            tempList = teknikCategory;
        }
        return tempList;
    }

    public static void main(String[] args) {
        DatabaseQuestions ad= new DatabaseQuestions();

        System.out.println(ad.getCategoryList("spel").get(0).getQuestion());

    }
}
