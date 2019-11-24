import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class QuestionAndRound {
    private Properties properties;
    private int questionAmount;
    private int roundAmount;


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
