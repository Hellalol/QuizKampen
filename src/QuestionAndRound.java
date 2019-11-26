import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class QuestionAndRound {
    protected Properties properties;
    protected int questionAmount;
    protected int roundAmount;


    public QuestionAndRound(String propertiesFile) {
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(propertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        questionAmount = Integer.parseInt(properties.getProperty("question"));
        roundAmount = Integer.parseInt(properties.getProperty("round"));
    }

    public int getQuestionAmount() {
        return questionAmount;
    }

    public int getRoundAmount() {
        return roundAmount;
    }

}
