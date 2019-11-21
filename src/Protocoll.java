import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Protocoll {

    private static final int BEGIN = 0;
    private static final int QUESTION = 1;
    private static final int VALIDATION = 2;
    private static final int QUIT = 3;
    Properties properties;
    Map<String,String> all = new LinkedHashMap<>();
    List<String> infoFromServer2 = new ArrayList<>();
    List<String> answerArray = new ArrayList<>();
    int correctAnswerIndex;

    public static void main(String[] args) {
       new Protocoll();
    }
    Protocoll(){
        properties = new Properties();
        getInfoFromAllXML();
        //By send "category1" as argument to get all 4 questions under category1
        infoFromServer2 = getQuestionsByCategory("category1");
        System.out.println(infoFromServer2);
       // answerArray = getAnswersByQuestion("Sony Playstation blev Nintendos största konkurrent efter misslyckad samarbete. " +
       //         "Men när släpptes Sony Playstation sin första spelkonsol?");
        answerArray = getAnswersByQuestion("question2_2");
        System.out.println("answerArray is:"+answerArray+answerArray.size());
        correctAnswerIndex = Integer.parseInt(answerArray.get(4));
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
        //answers.add(question);
        for(Map.Entry<String,String> entry : all.entrySet()){
            if(entry.getValue().equals(question)){
                System.out.println(entry.getKey());
                answers.add(entry.getKey());
            }
        }
        randomAnswers(answers);
        return answers;
    }

    //Random the answers and show the right answer index
    void randomAnswers(List<String> answers){
        String rightAnswer = answers.get(answers.size()-1);
        System.out.println("right answer: "+rightAnswer);
        //for (int i = answers.size()-1; i >=1; i--) {
        for (int i = answers.size()-1; i >=0; i--) {
            //int random = (int) (Math.random()*i)+1;
            int random = (int) (Math.random()*(i+1));
            String temp = answers.get(i);
            answers.set(i,answers.get(random));
            answers.set(random,temp);
        }
        System.out.println("answers after random: "+answers);
        for (int i = 0; i < answers.size() ; i++) {
            if(answers.get(i).equals(rightAnswer)){
                //answers.add(""+(i-1));
                answers.add(""+i);
                System.out.println("right index: "+i);
                break;
            }
        }
        System.out.println("answers :"+answers);
    }


    //Store all categories, questions and answers into a Map
    void getInfoFromAllXML(){
        InputStream  in = null;
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
                    System.out.println(category+" "+question+" "+answer1+" "+answer2+" "+answer3+" "+rightAnswer);
                    System.out.println();
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


    public List<String> infoFromServer2(String categoryName) {
        infoFromServer2 = getQuestionsByCategory("category1");
        return infoFromServer2;
    }

    public List<String> getAnswerArray(String questionName){
        answerArray = getAnswersByQuestion("Sony Playstation blev Nintendos största konkurrent efter misslyckad samarbete. " +
                "Men när släpptes Sony Playstation sin första spelkonsol?");
        return answerArray;
    }

    public String getInfo(String[] info,int i) {
        return info[i];
    }


    private static final int NUMRIDDLES = 5;

    private int state = BEGIN;
    private int currentRiddle = 0;

    //int correctAnswerIndex = Integer.parseInt(answerArray[4]);

    public String processInput(String theInput) {
        String theOutput = "";
        if (state == QUESTION) {
            theOutput = infoFromServer2.get(currentRiddle);
            state = VALIDATION;
        } else if (state == VALIDATION) {
            if (theInput.equalsIgnoreCase(answerArray.get(correctAnswerIndex))) {
                System.out.println("CORRECT ANSWER");
                theOutput = answerArray.get(correctAnswerIndex)
                        + " was the correct answer! Want another? (Yes/No)";
                currentRiddle++;
            } else {
                theOutput = "Wrong answer, try again? (Yes/No)";
                System.out.println("WRONG ANSWER");
            }
            state = QUESTION;
        }
        return theOutput;
    }
}