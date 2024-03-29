import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Database {

    Properties properties;
    Map<String,String> all = new LinkedHashMap<>();
    protected Category categories = new Category("Välj kategori!","spel","sport","blandade frågor","java");
    protected List<String> allCategories = new LinkedList<>();
    protected List<Question> spelCategory = new LinkedList<>();
    protected List<Question> sportCategory = new LinkedList<>();
    protected List<Question> javaCategory = new LinkedList<>();
    protected List<Question> blandadeFrågor = new LinkedList<>();

    Database() {
        properties = new Properties();
        getInfoFromAllXML();

        String questionBlandade1 =getQuestionsByCategory("category3").get(0);
        Question answersAndQuestionsBlandade1 = new Question(getAnswersByQuestion(questionBlandade1).get(0),
                getAnswersByQuestion(questionBlandade1).get(4),
                getAnswersByQuestion(questionBlandade1).get(3),
                getAnswersByQuestion(questionBlandade1).get(2),
                getAnswersByQuestion(questionBlandade1).get(1),
                getAnswersByQuestion(questionBlandade1).get(4));

        String questionBlandade2 =getQuestionsByCategory("category3").get(1);
        Question answersAndQuestionsBlandade2 = new Question(getAnswersByQuestion(questionBlandade2).get(0),
                getAnswersByQuestion(questionBlandade2).get(3),
                getAnswersByQuestion(questionBlandade2).get(1),
                getAnswersByQuestion(questionBlandade2).get(4),
                getAnswersByQuestion(questionBlandade2).get(2),
                getAnswersByQuestion(questionBlandade2).get(4));

        String questionBlandade3 =getQuestionsByCategory("category3").get(2);
        Question answersAndQuestionsBlandade3 = new Question(getAnswersByQuestion(questionBlandade3).get(0),
                getAnswersByQuestion(questionBlandade3).get(1),
                getAnswersByQuestion(questionBlandade3).get(3),
                getAnswersByQuestion(questionBlandade3).get(4),
                getAnswersByQuestion(questionBlandade3).get(2),
                getAnswersByQuestion(questionBlandade3).get(4));

        String questionBlandade4 =getQuestionsByCategory("category3").get(3);
        Question answersAndQuestionsBlandade4 = new Question(getAnswersByQuestion(questionBlandade4).get(0),
                getAnswersByQuestion(questionBlandade4).get(1),
                getAnswersByQuestion(questionBlandade4).get(2),
                getAnswersByQuestion(questionBlandade4).get(3),
                getAnswersByQuestion(questionBlandade4).get(4),
                getAnswersByQuestion(questionBlandade4).get(4));

        blandadeFrågor.add(answersAndQuestionsBlandade1);
        blandadeFrågor.add(answersAndQuestionsBlandade2);
        blandadeFrågor.add(answersAndQuestionsBlandade3);
        blandadeFrågor.add(answersAndQuestionsBlandade4);

        String questionJava1 =getQuestionsByCategory("category4").get(0);
        Question answersAndQuestionsJava1 = new Question(getAnswersByQuestion(questionJava1).get(0),
                getAnswersByQuestion(questionJava1).get(2),
                getAnswersByQuestion(questionJava1).get(1),
                getAnswersByQuestion(questionJava1).get(4),
                getAnswersByQuestion(questionJava1).get(3),
                getAnswersByQuestion(questionJava1).get(4));

        String questionJava2 =getQuestionsByCategory("category4").get(1);
        Question answersAndQuestionsJava2 = new Question(getAnswersByQuestion(questionJava2).get(0),
                getAnswersByQuestion(questionJava2).get(2),
                getAnswersByQuestion(questionJava2).get(4),
                getAnswersByQuestion(questionJava2).get(3),
                getAnswersByQuestion(questionJava2).get(1),
                getAnswersByQuestion(questionJava2).get(4));

        String questionJava3 =getQuestionsByCategory("category4").get(2);
        Question answersAndQuestionsJava3 = new Question(getAnswersByQuestion(questionJava3).get(0),
                getAnswersByQuestion(questionJava3).get(2),
                getAnswersByQuestion(questionJava3).get(3),
                getAnswersByQuestion(questionJava3).get(4),
                getAnswersByQuestion(questionJava3).get(1),
                getAnswersByQuestion(questionJava3).get(4));

        String questionJava4 =getQuestionsByCategory("category4").get(3);
        Question answersAndQuestionsJava4 = new Question(getAnswersByQuestion(questionJava4).get(0),
                getAnswersByQuestion(questionJava4).get(4),
                getAnswersByQuestion(questionJava4).get(3),
                getAnswersByQuestion(questionJava4).get(2),
                getAnswersByQuestion(questionJava4).get(1),
                getAnswersByQuestion(questionJava4).get(4));

        javaCategory.add(answersAndQuestionsJava1);
        javaCategory.add(answersAndQuestionsJava2);
        javaCategory.add(answersAndQuestionsJava3);
        javaCategory.add(answersAndQuestionsJava4);

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
                getAnswersByQuestion(questionsport2).get(2),
                getAnswersByQuestion(questionsport2).get(3),
                getAnswersByQuestion(questionsport2).get(4),
                getAnswersByQuestion(questionsport2).get(1),
                getAnswersByQuestion(questionsport2).get(4));

        String questionsport3 = getQuestionsByCategory("category2").get(2);
        Question answersAndQuestionsSport3 = new Question(getAnswersByQuestion(questionsport3).get(0),
                getAnswersByQuestion(questionsport3).get(3),
                getAnswersByQuestion(questionsport3).get(1),
                getAnswersByQuestion(questionsport3).get(4),
                getAnswersByQuestion(questionsport3).get(2),
                getAnswersByQuestion(questionsport3).get(4));

        String questionsport4 = getQuestionsByCategory("category2").get(3);
        Question answersAndQuestionsSport4 = new Question(getAnswersByQuestion(questionsport4).get(0),
                getAnswersByQuestion(questionsport4).get(4),
                getAnswersByQuestion(questionsport4).get(1),
                getAnswersByQuestion(questionsport4).get(3),
                getAnswersByQuestion(questionsport4).get(2),
                getAnswersByQuestion(questionsport4).get(4));

        sportCategory.add(answersAndQuestionsSport1);
        sportCategory.add(answersAndQuestionsSport2);
        sportCategory.add(answersAndQuestionsSport3);
        sportCategory.add(answersAndQuestionsSport4);

        allCategories.add(categories.getChooseCat());
        allCategories.add(categories.getCat1());
        allCategories.add(categories.getCat2());
        allCategories.add(categories.getCat3());
        allCategories.add(categories.getCat4());


        // SPEL

        String questionSpel1 = getQuestionsByCategory("category1").get(0);
        Question answersAndQuestionsSpel1 = new Question(getAnswersByQuestion(questionSpel1).get(0),
                getAnswersByQuestion(questionSpel1).get(4),
                getAnswersByQuestion(questionSpel1).get(3),
                getAnswersByQuestion(questionSpel1).get(2),
                getAnswersByQuestion(questionSpel1).get(1),
                getAnswersByQuestion(questionSpel1).get(4)
        );

        String questionSpel2 = getQuestionsByCategory("category1").get(1);
        Question answersAndQuestionsSpel2 = new Question(getAnswersByQuestion(questionSpel2).get(0),
                getAnswersByQuestion(questionSpel2).get(3),
                getAnswersByQuestion(questionSpel2).get(1),
                getAnswersByQuestion(questionSpel2).get(2),
                getAnswersByQuestion(questionSpel2).get(4),
                getAnswersByQuestion(questionSpel2).get(4));

        String questionSpel3 = getQuestionsByCategory("category1").get(2);
        Question answersAndQuestionsSpel3 = new Question(getAnswersByQuestion(questionSpel3).get(0),
                getAnswersByQuestion(questionSpel3).get(2),
                getAnswersByQuestion(questionSpel3).get(3),
                getAnswersByQuestion(questionSpel3).get(4),
                getAnswersByQuestion(questionSpel3).get(1),
                getAnswersByQuestion(questionSpel3).get(4));

        String questionSpel4 = getQuestionsByCategory("category1").get(3);
        Question answersAndQuestionsSpel4 = new Question(getAnswersByQuestion(questionSpel4).get(0),
                getAnswersByQuestion(questionSpel4).get(2),
                getAnswersByQuestion(questionSpel4).get(1),
                getAnswersByQuestion(questionSpel4).get(4),
                getAnswersByQuestion(questionSpel4).get(3),
                getAnswersByQuestion(questionSpel4).get(4));


        spelCategory.add(answersAndQuestionsSpel1);
        spelCategory.add(answersAndQuestionsSpel2);
        spelCategory.add(answersAndQuestionsSpel3);
        spelCategory.add(answersAndQuestionsSpel4);



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

    Category mixedCategories(){
        int r = (int) (Math.random() * 4);

        switch (r){
            case 1:
                categories = new Category("Välj kategori!","spel","sport","blandade frågor","java");
            case 2:
                categories = new Category("Välj kategori!","sport","spel","java","blandade frågor");
            case 3:
                categories = new Category("Välj kategori!","java","blandade frågor","spel","sport");
            case 4:
                categories = new Category("Välj kategori!","blandade frågor","java","sport","spel");
        }
        return categories;
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
}
