import entity.Test;
import util.ConnectionManager;

import java.sql.*;

public class TestSQL {
    String SQL_TABLE = "INSERT INTO test (name_test, surname_test) VALUES (?, ?)";
    private final static String SQL_CHECK_TEST_ID = "SELECT id FROM test WHERE name_test = ? AND surname_test = ?";


    public static void main(String[] args) {
        TestSQL testSQL = new TestSQL();
        Test test = new Test();
        test.setNameTest("Vov");
        test.setSurnameTest("Zelehsk");
        Test save = testSQL.save(test);
        System.out.println(save);




    }

    public Test save(Test test) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_TABLE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, test.getNameTest());
            preparedStatement.setString(2, test.getSurnameTest());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                test.setId(generatedKeys.getLong("id"));
            }
            return test;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public Test check(Test test){
//        try (Connection connection = ConnectionManager.openConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_TEST_ID)) {
//            preparedStatement.setString(1, test.getNameTest());
//            preparedStatement.setString(2, test.getSurnameTest());
//            ResultSet resultSet= preparedStatement.executeQuery();
//            while (resultSet.next()){
//                Test test1 = new Test();
//                test1.setId(resultSet.getLong("id"));
//                System.out.println("Exist");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }



}

