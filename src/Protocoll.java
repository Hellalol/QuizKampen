import java.util.Arrays;

public class Protocoll {

    private static final int BEGIN = 0;
    private static final int QUESTION = 1;
    private static final int VALIDATION = 2;
    private static final int QUIT = 3;



    Protocoll(){}

    public String[] infoFromServer2() {
        return infoFromServer2;
    }

    public String getInfo(String[] info,int i) {
        return info[i];
    }

    private static final int NUMRIDDLES = 5;

    private int state = BEGIN;
    private int currentRiddle = 0;

    public String[] infoFromServer2 =
             {"Sony Playstation blev Nintendos största konkurrent efter misslyckad samarbete. " +
            "Men när släpptes Sony Playstation sin första spelkonsol?"};



    public  String[] answerArray = {"1997","1991","2011","1994","3"

    };

    int correctAnswerIndex = Integer.parseInt(answerArray[4]);

    public String processInput(String theInput) {
        String theOutput = "";
        if (state == QUESTION) {
            theOutput = infoFromServer2[currentRiddle];
            state = VALIDATION;
        } else if (theInput.equalsIgnoreCase("No")
                || theInput.equalsIgnoreCase("N")) {
            theOutput = "Bye";
            state = QUIT;
        } else if (state == VALIDATION) {
            if (theInput.equalsIgnoreCase(answerArray[correctAnswerIndex])) {
                System.out.println("CORRECT ANSWER");
                theOutput = answerArray[correctAnswerIndex]
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