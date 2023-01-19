package dao;

import entity.Letter;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LetterDao {

    public final static String SQL_HUMAN_LETTER_REFERENCES = "INSERT INTO humans_letters(letter_id, receiver_id) " +
            "VALUES (?, ?) ";
    private final static String SQL_INSERT_LETTER = "INSERT INTO letters(topic, letter_text, shipping_date, sender) " +
            "VALUES (?, ?, ?, ?)";

    public Letter insert(Letter letter) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_LETTER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, letter.getTopic());
            preparedStatement.setString(2, letter.getLetterText());
            preparedStatement.setDate(3, letter.getShippingDate());
            preparedStatement.setObject(4, letter.getSender());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                letter.setId(generatedKeys.getLong("id"));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return letter;
    }


}
