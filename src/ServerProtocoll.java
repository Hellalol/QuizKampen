import java.util.Arrays;

public class ServerProtocoll {

    private static final int BEGIN = 0;
    private static final int CHOOSECATEGORY = 1;
    private static final int CHOOSECATEGORYWITHRESULTS = 2;
    private static final int QUESTIONING = 3;
    private static final int QUESTIONSANSWERED = 4;
    private static final int SHOWRESULT = 5;



    ServerProtocoll(){}

    private int state = BEGIN;

    public  String[] answerArray = {"Sony Playstation blev Nintendos största konkurrent efter misslyckad samarbete.\n" +
            " Men när släpptes Sony Playstation sin första spelkonsol?","1997","1991","2011","1994","3"};

    int correctAnswerIndex = Integer.parseInt(answerArray[4]);

    public String processInput(String theInput, Server server) {
        String theOutput = "";
        if(state == BEGIN) {
            theOutput = server.Playername;
            state = CHOOSECATEGORY;
        }
        else if (state == CHOOSECATEGORY) {
            // Hämtar kategorier från XMLen
            theOutput = answerArray[1];

            state = QUESTIONING;


        } else if(state == CHOOSECATEGORYWITHRESULTS) {



        }else if (state == QUESTIONING) {

            // Använder sig utav antalet av frågor och rundor som defineras i början.
            // LOOPAR TILLS ALLA FRÅGOR ÄR KLARA

            for (int i = 0; i < 2; i++) {

            }

            state = QUESTIONSANSWERED;
        } else if(state == QUESTIONSANSWERED){
            // Frågor klara, åter till


        }else if(state == SHOWRESULT){

        }
        return theOutput;
    }
}