package dao;

import entity.Human;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HumanDao {

    private final static String SQL_SHORTEST_LENGTH_LETTERS = "SELECT sum(length(letter_text)) letters_sum, h.id, h.first_name, " +
            "h.second_name, h.birthday FROM letters l JOIN humans h ON h.id = l.sender GROUP BY h.id " +
            "ORDER BY letters_sum LIMIT 1";
    private final static String SQL_WITH_GIVEN_TOPIC = "SELECT humans.id, first_name, second_name, birthday, topic, receiver_id " +
            "FROM humans JOIN humans_letters hl on humans.id = hl.receiver_id JOIN letters l on l.id = hl.letter_id WHERE topic = ?";
    private final static String SQL_WITH_GIVEN_TOPIC_NOT = "SELECT humans.id, first_name, second_name, birthday FROM humans\n" +
            "WHERE humans.id NOT IN (SELECT receiver_id FROM humans_letters WHERE letter_id IN (SELECT id FROM letters WHERE topic = ?))";
    private final static String SQL_RECEIVED_AND_SENT_LETTERS_QUANTITY = "SELECT humans.id, first_name, second_name, birthday, " +
            "(SELECT count(letters.id) sent FROM letters WHERE sender = humans.id), " +
            "(SELECT count(humans_letters.id) received FROM humans_letters WHERE receiver_id = humans.id) FROM humans";
    private final static String SQL_INSERT_HUMAN = "INSERT INTO humans (first_name, second_name, birthday) VALUES (?, ?, ?)";
    private final static String SQL_CHECK_HUMAN_ID = "SELECT id FROM humans WHERE first_name = ? AND second_name = ? AND birthday = ?";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM humans WHERE id = ?";
    private final static String SQL_GET_ALL = "SELECT * FROM humans";
    private final static String SQL_FIND_BY_ID_NOT_EQUALS = "SELECT DISTINCT humans.id, first_name, second_name, birthday " +
            "FROM humans JOIN letters l ON humans.id = l.sender WHERE sender <> ?";


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

    public List<Human> findByLetterTopicNot(String topic) {
        List<Human> humanList = new ArrayList<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_WITH_GIVEN_TOPIC_NOT)) {
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

    public List<HumanWithReceivedAndSentLetters> findAllWithReceivedAndSentLettersQuantity() {
        List<HumanWithReceivedAndSentLetters> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_RECEIVED_AND_SENT_LETTERS_QUANTITY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Human human = map(resultSet);
                HumanWithReceivedAndSentLetters humanWithReceivedAndSentLetters = new HumanWithReceivedAndSentLetters(human,
                        resultSet.getInt("sent"), resultSet.getInt("received"));
                result.add(humanWithReceivedAndSentLetters);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    public Human insertOrGetFromDb(Human human) {
        return insertOrGetFromDb(human, null);
    }

    public Human insertOrGetFromDbInTransaction(Human human, Connection connection) {
        return insertOrGetFromDb(human, connection);
    }

    private Human insertOrGetFromDb(Human human, Connection connection) {
        Long id = fetchIdInTransaction(human, connection);
        if (id != null) {
            human.setId(id);
            return human;
        }
        return insertInTransaction(human, connection);
    }

    public Long fetchId(Human human) {
        return fetchId(human, null);
    }

    public Long fetchIdInTransaction(Human human, Connection connection) {
        return fetchId(human, connection);
    }

    private Long fetchId(Human human, Connection connection) {
        if (connection == null) {
            connection = ConnectionManager.openConnection();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_HUMAN_ID)) {
            preparedStatement.setString(1, human.getFirstName());
            preparedStatement.setString(2, human.getSecondName());
            preparedStatement.setDate(3, human.getBirthday());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            try {
                if (connection.getAutoCommit()) connection.close();
            } catch (SQLException e) {
                System.out.println("Connection close");
            }
        }
        return null;
    }

    public Human insert(Human human) {
        return insert(human, null);
    }

    public Human insertInTransaction(Human human, Connection connection) {
        return insert(human, connection);
    }

    private Human insert(Human human, Connection connection) {
        if (connection == null) {
            connection = ConnectionManager.openConnection();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_HUMAN, Statement.RETURN_GENERATED_KEYS)) {
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
        } finally {
            try {
                if (connection.getAutoCommit()) connection.close();
            } catch (Exception e) {
                System.out.println("connection close exception ignore");
            }
        }
        return human;
    }

    public Human findById(int id) {
        Human human = new Human();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                human.setFirstName(resultSet.getString("first_name"));
                human.setSecondName(resultSet.getString("second_name"));
                human.setBirthday(resultSet.getDate("birthday"));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return human;
    }

    public List<Human> findByIdNot(Long id) {
        return findByIdNot(id, null);
    }

    public List<Human> findByIdNotInTransaction(Long id, Connection connection) {
        return findByIdNot(id, connection);
    }

    private List<Human> findByIdNot(Long id, Connection connection) {
        if (connection == null) {
            connection = ConnectionManager.openConnection();
        }
        List<Human> humanList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID_NOT_EQUALS)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                humanList.add(map(resultSet));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            try {
                if (connection.getAutoCommit()) connection.close();
            } catch (SQLException e) {
                System.out.println("Connection close");
            }
        }
        return humanList;
    }

    public List<Human> getAll() {
        List<Human> humanList = new ArrayList<>();
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                humanList.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return humanList;
    }

    public static class HumanWithReceivedAndSentLetters {
        private final Human human;
        private final Integer sentLettersCount;
        private final Integer receivedLettersCount;

        public HumanWithReceivedAndSentLetters(Human human, Integer sentLettersCount, Integer receivedLettersCount) {
            this.human = human;
            this.sentLettersCount = sentLettersCount;
            this.receivedLettersCount = receivedLettersCount;
        }

        public Human getHuman() {
            return human;
        }

        public Integer getSentLettersCount() {
            return sentLettersCount;
        }

        public Integer getReceivedLettersCount() {
            return receivedLettersCount;
        }

        @Override
        public String toString() {
            return human + " " + "sent" + " " + sentLettersCount + " " + "received" + " "
                    + receivedLettersCount;
        }
    }
}


