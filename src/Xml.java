import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class Xml {

    Properties properties;
    Map<String,String> all = new LinkedHashMap<>();


    Xml(){
        properties = new Properties();

        getInfoFromAllXML();
        //By send "category1" as argument to get all 4 questions under category1
        //infoFromServer2 = getQuestionsByCategory("category1");
        //System.out.println(infoFromServer2);
        //answerArray = getAnswersByQuestion("Sony Playstation blev Nintendos största konkurrent efter misslyckad samarbete. " +
        //"Men när släpptes Sony Playstation sin första spelkonsol?");
        //answerArray = getAnswersByQuestion("question2_2");
        //System.out.println("answerArray is:"+answerArray+answerArray.size());

    }

    public static void main(String[] args) {

        List<Question> baba = new ArrayList<>();
        Xml e = new Xml();
        String questionSpel1 = e.getQuestionsByCategory("category1").get(0);
        Question q = new Question(e.getAnswersByQuestion(questionSpel1).get(0),
                e.getAnswersByQuestion(questionSpel1).get(1),
                e.getAnswersByQuestion(questionSpel1).get(2),
                e.getAnswersByQuestion(questionSpel1).get(3),
                e.getAnswersByQuestion(questionSpel1).get(4),
                e.getAnswersByQuestion(questionSpel1).get(4)
        );
        baba.add(q);
        System.out.println(baba.get(0).getQuestion());
        System.out.println(baba.get(0).getAnswerOne());
        System.out.println(baba.get(0).getAnswerTwo());
        System.out.println(baba.get(0).getAnswerThree());
        System.out.println(baba.get(0).getAnswerFour());
        System.out.println("Correct answer is " + baba.get(0).getCorretAnswer());
    }

    //To get all questions under one category
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

//    //Random the answers and show the right answer index
//    void randomAnswers(List<String> answers){
//        String rightAnswer = answers.get(answers.size()-1);
//        System.out.println("right answer: "+rightAnswer);
//        for (int i = answers.size()-1; i >=1; i--) {
//            //for (int i = answers.size()-1; i >=0; i--) {
//            int random = (int) (Math.random()*i)+1;
//            //int random = (int) (Math.random()*(i+1));
//            String temp = answers.get(i);
//            answers.set(i,answers.get(random));
//            answers.set(random,temp);
//        }
//        System.out.println("answers after random: "+answers);
//        for (int i = 0; i < answers.size() ; i++) {
//            if(answers.get(i).equals(rightAnswer)){
//                answers.add(""+(i-1));
//                //answers.add(""+i);
//                System.out.println("right index: "+i);
//                break;
//            }
//        }
//        System.out.println("answers :"+answers);
//    }


    //Store all categories, questions and answers into a Map
    void getInfoFromAllXML(){
        InputStream  in;
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