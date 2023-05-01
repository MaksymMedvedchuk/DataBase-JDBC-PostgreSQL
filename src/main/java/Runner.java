import dao.GenericDao;
import dao.HumanDao;
import entity.Human;
import entity.Letter;

import java.sql.Date;
import java.util.List;

public class Runner {
    private static final GenericDao genericDao = new GenericDao();

    public static void main(String[] args) {
        HumanDao humanDao = new HumanDao();
        System.out.println(humanDao.findByShortestLettersLength());
        System.out.println("-----------------------------------------------------------------------------------------");
        List<HumanDao.HumanWithReceivedAndSentLetters> human = humanDao.findAllWithReceivedAndSentLettersQuantity();
        for (HumanDao.HumanWithReceivedAndSentLetters humanWithReceivedAndSentLetters : human) {
            System.out.println(humanWithReceivedAndSentLetters);
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(humanDao.findByLetterTopic("Java"));
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(humanDao.findByLetterTopicNot("Java"));

//        System.out.println("-----------------------------------------------------------------------------------------");
//        Human sender = new Human("Oleksandr", "Kislinsky", new Date(637891200000L));
//        Letter letter = new Letter("Delphi", "I'm studing Delphi", new Date(1674000000000L), sender);
//        genericDao.sendLetterToAll(sender, letter);
    }
}







