import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Game extends Thread {

    Server nuvarandeSpelare;
    int questionAmount;
    int roundAmount;
    List<Question> ålder =new LinkedList<>();
    public void setNuvarandeSpelare(Server nuvarandeSpelare) {
        this.nuvarandeSpelare = nuvarandeSpelare;
    }
    DatabaseQuestions database=new DatabaseQuestions();
    @Override
    public void run() {
         Question q=new Question("Hur gammal är Fazli: ","18","19","20","21","21");
         Question qq=new Question("Hur gammal är atef: ","18","19","30","21","30");
         ålder.add(q);
         ålder.add(qq);
        try {
            while (true) {
                nuvarandeSpelare.opponent.pw.writeObject("Other player is choosing category ?");
                Object sel=nuvarandeSpelare.in.readObject();
                if(sel instanceof String){
                    if(((String) sel).contains("@")){
                        StringTokenizer st = new StringTokenizer((String)sel, "@");
                        switch (st.nextToken()) {
                            //To get roundAmount and questionAmount from client
                            case "R&Q":
                                roundAmount = Integer.parseInt(st.nextToken());
                                questionAmount = Integer.parseInt(st.nextToken());
                                System.out.println(nuvarandeSpelare.playerName+"has chosen rounds as: "+roundAmount + " and questions as: " + questionAmount);
                                break;
                        }
                    }else {
                        String selectedCategory=(String)sel;
                        System.out.println("Cat "+selectedCategory);
                        sendQuestion(ålder);
                        System.out.println("Atef");
                        nuvarandeSpelare=nuvarandeSpelare.opponent;
                        sendQuestion(ålder);
                    }
                }
            }


            //TODO : fix the database
            //-Skapa en metod som tar in en String och returnerar en lista i databasen/här
            // - questions=database.getQuestionLisT(selectedCategory);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendQuestion(List<Question> list) throws IOException, ClassNotFoundException {
       int  counter=0;
       Object obj;
        while (counter<2) {
            System.out.println(counter+" before");
            nuvarandeSpelare.pw.writeObject(list.get(counter));
            obj=nuvarandeSpelare.in.readObject();
            String answer=(String) obj;
            System.out.println(answer);
            counter++;
            System.out.println("after "+counter);

        }
    }
}
