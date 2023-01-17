import dao.HumanDao;

public class Runner {

    public static void main(String[] args) {
        HumanDao humanDao = new HumanDao();
        System.out.println(humanDao.findShortestLengthLetters());
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(humanDao.printUsersWithGivenTopic("Java"));
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(humanDao.printUsersWithNotGivenTopic("Java"));
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}







