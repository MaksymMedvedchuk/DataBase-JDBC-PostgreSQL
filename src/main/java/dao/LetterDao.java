package dao;

import entity.Letter;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.*;

public class LetterDao {

    public final static String SQL_HUMAN_LETTER_REFERENCES = "INSERT INTO humans_letters(letter_id, receiver_id) " +
            "VALUES (?, ?) ";
    private final static String SQL_INSERT_LETTER = "INSERT INTO letters(topic, letter_text, shipping_date, sender) " +
            "VALUES (?, ?, ?, ?)";
    private final static String SQL_CHECK_LETTER_ID = "SELECT id FROM letters WHERE topic = ? AND letter_text = ? AND shipping_date = ?";

    public Letter insert(Letter letter) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_LETTER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, letter.getTopic());
            preparedStatement.setString(2, letter.getLetterText());
            preparedStatement.setDate(3, letter.getShippingDate());
            preparedStatement.setObject(4, letter.getSender().getId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                letter.setId(generatedKeys.getLong("id"));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return letter;
    }

    public Long fetchIdLetter(Letter letter) {
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

    public Letter insertOrGetFromDB(Letter letter) {
        Long idLetter = fetchIdLetter(letter);
        if (idLetter != null) {
            letter.setId(idLetter);
            return letter;
        }
        return insert(letter);
    }


    public void sendLetterWithGivenTopicToAll(Letter letter) {
        try (Connection connection = ConnectionManager.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_HUMAN_LETTER_REFERENCES, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, insertOrGetFromDB(letter).getId());
            preparedStatement.setObject(2, letter.getReceivers().get(Integer.parseInt("id")));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){

            }



        } catch (Exception e) {
            throw new DaoException(e);
        }

    }


}
