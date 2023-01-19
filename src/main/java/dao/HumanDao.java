package dao;

import entity.Human;
import exception.DaoException;
import util.ConnectionManager;
import util.HumanSetHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HumanDao {

    private final static String SQL_SHORTEST_LENGTH_LETTERS = "SELECT sum(length(letter_text)) letters_sum, h.id, h.first_name, " +
            "h.second_name, h.birthday FROM letters l JOIN humans h ON h.id = l.sender GROUP BY h.id " +
            "ORDER BY letters_sum LIMIT 1";
    private final static String SQL_WITH_GIVEN_TOPIC = "SELECT humans.id, first_name, second_name, birthday, topic, receiver_id " +
            "FROM humans JOIN humans_letters hl on humans.id = hl.receiver_id JOIN letters l on l.id = hl.letter_id WHERE topic = ?";
    private final static String SQL_WITH_NOT_GIVEN_TOPIC = "SELECT humans.id, first_name, second_name, birthday, topic, receiver_id " +
            "FROM humans JOIN humans_letters hl on humans.id = hl.receiver_id JOIN letters l on l.id = hl.letter_id WHERE topic <> ?";
    private final static String SQL_RECEIVED_AND_SENT_LETTERS_QUANTITY = "SELECT  humans.id, first_name, second_name, birthday," +
            " count(DISTINCT l.id) sent, count(DISTINCT hl.id) received FROM humans JOIN letters l ON humans.id = l.sender JOIN humans_letters " +
            "hl ON hl.receiver_id = humans.id GROUP BY humans.id";
    private final static String SQL_INSERT_HUMAN = "INSERT INTO humans (first_name, second_name, birthday) VALUES (?, ?, ?)";
    private final static String SQL_CHECK_HUMAN_ID = "SELECT id FROM humans WHERE first_name = ? AND second_name = ? AND birthday = ?";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM humans WHERE id = ?";
    private final HumanSetHelper helper = new HumanSetHelper();

    public Human findByShortestLettersLength() {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SHORTEST_LENGTH_LETTERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return map(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return new Human();
    }

    public Human map(ResultSet resultSet) {
        Human human = new Human();
        try {
            human.setId(resultSet.getLong("id"));
            human.setFirstName(resultSet.getString("first_name"));
            human.setSecondName(resultSet.getString("second_name"));
            human.setBirthday(resultSet.getDate("birthday"));
            return human;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Human> findByLetterTopic(String topic) {
        List<Human> humanList = new ArrayList<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_WITH_GIVEN_TOPIC)) {
            preparedStatement.setString(1, topic);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                humanList.add(map(resultSet));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return humanList;
    }

    //todo change signature like other methods
    public Set<Human> findNotByLetterTopic(String topic) {
        Set<Human> humanList = new HashSet<>();
        try (Connection connection = ConnectionManager.openConnection();//чи не повинні бути окремі методи на зєднанн і результат БД?
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_WITH_NOT_GIVEN_TOPIC)) {
            preparedStatement.setString(1, topic);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                humanList.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return humanList;
    }

    public List<Human> findAllWithReceivedAndSentLettersQuantity() {
        List<Human> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_RECEIVED_AND_SENT_LETTERS_QUANTITY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Human human = map(resultSet);
                human.setSentLettersCount(resultSet.getInt("sent"));//при дебазі тут в human нічого не сетиться, це через те що toString для них не перевизначений!!!???
                human.setReceivedLettersCount(resultSet.getInt("received"));
                result.add(human);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    public Human insertOrGetFromDb(Human human) {
        Long id = fetchId(human);
        if (id != null) {
            human.setId(id);
            return human;
        }
        return insert(human);
    }

    public Long fetchId(Human human) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_HUMAN_ID)) {
            preparedStatement.setString(1, human.getFirstName());
            preparedStatement.setString(2, human.getSecondName());
            preparedStatement.setDate(3, human.getBirthday());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    public Human insert(Human human) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_HUMAN, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, human.getFirstName());
            preparedStatement.setString(2, human.getSecondName());
            preparedStatement.setDate(3, human.getBirthday());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                human.setId(generatedKeys.getLong("id"));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return human;
    }

    public Human findEntityById(int id) {
        Human human = new Human();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                human.setFirstName(resultSet.getString("first_name"));
                human.setSecondName(resultSet.getString("second_name"));
                human.setBirthday(resultSet.getDate("birthday"));
            } else System.out.println("There is no such id in the table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return human;
    }
}


