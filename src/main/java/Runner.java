import dao.HumanDao;
import dao.LetterDao;
import entity.Human;
import entity.Letter;

import java.sql.Date;
import java.sql.SQLException;

public class Runner {

    public static void main(String[] args) throws SQLException {
        HumanDao humanDao = new HumanDao();
        LetterDao letterDao = new LetterDao();
//        System.out.println(humanDao.findByShortestLettersLength());
//        System.out.println("-----------------------------------------------------------------------------------------");
//        List<Human> humans = humanDao.findAllWithReceivedAndSentLettersQuantity();
//        for (Human human : humans) {
//            System.out.println(human + " sent = " + human.getSentLettersCount() + ", received = " + human.getReceivedLettersCount());
//        }
//        System.out.println(humanDao.findByLetterTopic("Java"));
//        System.out.println("-----------------------------------------------------------------------------------------");
//        System.out.println(humanDao.findNotByLetterTopic("Java"));
//        System.out.println("-----------------------------------------------------------------------------------------");
//        System.out.println(humanDao.findEntityById(8));
//        System.out.println("-----------------------------------------------------------------------------------------");

        Human human = new Human();
        human.setFirstName("Ruslan");
        human.setSecondName("Gavrutenko");
        human.setBirthday(new Date(294019200000L));
        Long checkHuman = humanDao.fetchIdHuman(human);
        System.out.println(checkHuman);
        System.out.println(humanDao.insertOrGetFromDb(human));

        Letter letter = new Letter();
        letter.setTopic("Go");
        letter.setLetterText("I'm studing go");
        letter.setShippingDate(new Date(1673913600000L));
        letter.setSender(humanDao.insertOrGetFromDb(human));
        Long checkLetter = letterDao.fetchIdLetter(letter);
        System.out.println(checkLetter);

        System.out.println(letterDao.insertOrGetFromDB(letter));


    }
}







