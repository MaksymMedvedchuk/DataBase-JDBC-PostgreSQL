package dao;

import entity.Humans;
import exception.DaoException;
import util.ConnectionManager;
import util.HumanSetHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HumanDao {

    private final static String SQL_SHORTEST_LENGTH_LETTERS = "SELECT sum(length(letter_text)) letters_sum, h.first_name, " +
            "h.second_name, h.birthday FROM letters l JOIN humans h ON h.id = l.sender GROUP BY h.first_name, h.second_name, h.birthday " +
            "ORDER BY letters_sum LIMIT 1";
    private final static String SQL_WITH_GIVEN_TOPIC = "SELECT first_name, second_name, birthday, topic " +
            "FROM humans JOIN letters l on humans.id = l.sender " +
            "WHERE topic = ?";
    private final static String SQL_WITH_NOT_GIVEN_TOPIC = "SELECT DISTINCT first_name, second_name, birthday, topic " +
            "FROM humans JOIN letters l on humans.id = l.sender " +
            "WHERE NOT topic = ?";
    private final HumanSetHelper helper = new HumanSetHelper();

    public Humans findShortestLengthLetters() {
        Humans human = new Humans();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SHORTEST_LENGTH_LETTERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                addHuman(human, resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return human;
    }

    private void addHuman(Humans human, ResultSet resultSet) {
        try {
            human.setFirstName(resultSet.getString("first_name"));
            human.setSecondName(resultSet.getString("second_name"));
            human.setBirthday(resultSet.getDate("birthday"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Humans> printUsersWithGivenTopic(String topic) {
        List<Humans> humansList = new ArrayList<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_WITH_GIVEN_TOPIC)) {
            preparedStatement.setString(1, topic);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                helper.setHuman(humansList, resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return humansList;
    }

    public Set<Humans> printUsersWithNotGivenTopic(String topic) {
        Set<Humans> humansSet = new HashSet<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_WITH_NOT_GIVEN_TOPIC)) {
            preparedStatement.setString(1, topic);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                helper.setHuman(humansSet, resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return humansSet;
    }


//   public List<Humans> printReceivedAndSentLettersQuantity(){
//        List<Humans> humansList = new ArrayList<>();
//       Connection connection = ConnectionManager.openConnection();
//   }
}


