package dao;

import entity.Letter;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.*;

public class LetterDao {

    public final static String SQL_HUMAN_LETTER_REFERENCES = "INSERT INTO humans_letters(letter_id, receiver_id) " +
            "VALUES (?, ?)";
    private final static String SQL_INSERT_LETTER = "INSERT INTO letters(topic, letter_text, shipping_date, sender) " +
            "VALUES (?, ?, ?, ?)";
    private final static String SQL_CHECK_LETTER_ID = "SELECT id FROM letters WHERE topic = ? AND letter_text = ? AND shipping_date = ?";

    public Letter insert(Letter letter) {
        return insert(letter, null);
    }

    public Letter insertInTransaction(Letter letter, Connection connection) {
        return insert(letter, connection);
    }

    private Letter insert(Letter letter, Connection connection) {
        if (connection == null) {
            connection = ConnectionManager.openConnection();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_LETTER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, letter.getTopic());
            preparedStatement.setString(2, letter.getLetterText());
            preparedStatement.setDate(3, letter.getShippingDate());
            preparedStatement.setLong(4, letter.getSender().getId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                letter.setId(generatedKeys.getLong("id"));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            try {
                if (connection.getAutoCommit())
                    connection.close();
            } catch (Exception e) {
                System.out.println("Connection close");
            }
        }
        return letter;
    }

    public Long fetchLetterId(Letter letter) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_LETTER_ID)) {
            preparedStatement.setString(1, letter.getTopic());
            preparedStatement.setString(2, letter.getLetterText());
            preparedStatement.setDate(3, letter.getShippingDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    public Letter getFromDB(Letter letter) {
        Long id = fetchLetterId(letter);
        if (id != null) {
            letter.setId(id);
        }
        return letter;
    }
}
