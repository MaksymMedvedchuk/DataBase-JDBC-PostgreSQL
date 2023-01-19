import dao.HumanDao;
import entity.Human;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws SQLException {
        HumanDao humanDao = new HumanDao();
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
//
        Human human = new Human();
        human.setFirstName("Ruslan");
        human.setSecondName("Gavrutenko");
        human.setBirthday(new Date(294019200000L));
        Long checkHuman = humanDao.fetchId(human);
        System.out.println(checkHuman);

        humanDao.insertOrGetFromDb(human);


    }
}







