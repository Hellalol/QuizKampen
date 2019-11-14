public class Question {

    private String question;
    private String answerOne;
    private String answerTwo;
    private String answerThree;
    private String answerFour;
    private int corretAnswer;
    Protocoll pro = new Protocoll();

    public Question(String question, String answerOne, String answerTwo, String answerThree, String answerFour, int corretAnswer) {
        this.question = question;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
        this.corretAnswer = corretAnswer;
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

    public int getCorretAnswer() {
        return corretAnswer;
    }
}
