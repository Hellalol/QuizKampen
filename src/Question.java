
import java.io.Serializable;

public class Question implements Serializable {
    static final long serialVersionUID = 42L;
    protected String question;
    protected String answerOne;
    protected String answerTwo;
    protected String answerThree;
    protected String answerFour;
    protected String correctAnswer;
    //ServerProtocoll pro = new ServerProtocoll();

    public Question(){};

    public Question(String question, String answerOne, String answerTwo, String answerThree, String answerFour, String correctAnswer) {
        this.question = question;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public String getAnswerFour() {
        return answerFour;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return question+"："+answerOne+"，"+answerTwo+"， "+answerThree+"， "+answerFour+"-->"+correctAnswer;
    }
}