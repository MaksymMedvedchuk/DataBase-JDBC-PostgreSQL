package dao;

import entity.Human;
import entity.Letter;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class GenericDao {
    private final static String SQL_HUMAN_LETTER_REFERENCES = "INSERT INTO humans_letters(receiver_id, letter_id) " +
            "VALUES (?, ?)";
    private final HumanDao humanDao = new HumanDao();
    private final LetterDao letterDao = new LetterDao();

    public void sendLetterToAll(Human sender, Letter letter) {
        Connection connection = ConnectionManager.openConnectionForTransaction();
        try {
            sender = humanDao.insertOrGetFromDbInTransaction(sender, connection);
            if (letter.getId() == null) {
                letter = letterDao.insertInTransaction(letter, connection);
                List<Human> receiverList = humanDao.findByIdNotInTransaction(sender.getId(), connection);
                for (Human receiver : receiverList) {
                    insertHumanLetterLinkInTransaction(receiver, letter, connection);
                }
            }
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ignore) {
                System.out.println("rollback exception ignore");
            }
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (Exception ignore) {
                System.out.println("connection.close exception ignore");
            }
        }
    }

    private void insertHumanLetterLinkInTransaction(Human receiver, Letter letter, Connection connection) {
        if (connection == null) {
            connection = ConnectionManager.openConnection();
        }
        try (PreparedStatement insertHumanLetterLink = connection.prepareStatement(SQL_HUMAN_LETTER_REFERENCES)) {
            insertHumanLetterLink.setLong(1, receiver.getId());
            insertHumanLetterLink.setLong(2, letter.getId());

            insertHumanLetterLink.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            try {
                if (connection.getAutoCommit()) connection.close();
            } catch (SQLException e) {
                System.out.println("Connection close");
            }
        }
    }
}






