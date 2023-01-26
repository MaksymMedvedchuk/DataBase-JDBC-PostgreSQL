package dao;

import entity.Human;
import entity.Letter;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GenericDao {
    private final static String SQL_HUMAN_LETTER_REFERENCES = "INSERT INTO humans_letters(receiver_id, letter_id) " +
            "VALUES (?, ?)";
    private final static String SQL_HUMAN_ID_NOT_EQUALS = "SELECT DISTINCT humans.id, first_name, second_name, birthday " +
            "FROM humans JOIN letters l ON humans.id = l.sender WHERE sender <> ?";
    private final HumanDao humanDao = new HumanDao();
    private final LetterDao letterDao = new LetterDao();

    public void sendLetterToAll(Human sender, Letter letter) {
        Connection connection = ConnectionManager.openConnectionForTransaction();
        try {
            //todo якщо мені потрібно не в транзакції, я викликаю insertOrGetFromDb?
            sender = humanDao.insertOrGetFromDbInTransaction(sender, connection);
            if (letter.getId() == null) {
                //todo insertInTransaction
                letter = letterDao.insert(letter);
                List<Human> receiverList = humanDao.findByIdNot(sender.getId());
                for (Human receiver : receiverList) {
                    //todo insertHumanLetterLinkInTransaction
                    insertHumanLetterLink(receiver, letter);
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

    /*public void sendLetterToAll(Human sender, Letter letter) {
        sender = humanDao.insertOrGetFromDb(sender);
        if (letter.getId() == null) {
            letter = letterDao.insert(letter);
        }
        insert(sender, letter);
    }

    private void insert(Human sender, Letter letter) {
        Connection connection = ConnectionManager.openConnection();
        try (PreparedStatement receiversStatement = connection.prepareStatement(SQL_HUMAN_ID_NOT_EQUALS);
             PreparedStatement insertHumanLetterLink = connection.prepareStatement(SQL_HUMAN_LETTER_REFERENCES)) {
            //todo transaction
            connection.setAutoCommit(false);
            receiversStatement.setLong(1, sender.getId());
            ResultSet receiverResultSet = receiversStatement.executeQuery();
            List<Human> receiverList = new ArrayList<>();
            while (receiverResultSet.next()) {
                receiverList.add(humanDao.map(receiverResultSet));
            }
            for (Human receiver : receiverList) {
                insertHumanLetterLink.setLong(1, receiver.getId());
                insertHumanLetterLink.setLong(2, letter.getId());

                insertHumanLetterLink.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ex) {
                //Log.debug("Connection.rollback() failure. Ignoring")
            }
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                //Log.debug("Connection.rollback() failure. Ignoring")
            }
        }
    }*/

    private void insertHumanLetterLink(Human receiver, Letter letter) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement insertHumanLetterLink = connection.prepareStatement(SQL_HUMAN_LETTER_REFERENCES)) {
                insertHumanLetterLink.setLong(1, receiver.getId());
                insertHumanLetterLink.setLong(2, letter.getId());
                insertHumanLetterLink.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

//    public void sendLetterToAll(Human sender, Letter letter) {
//        List<Human> senderList = new ArrayList<>();
//        //todo insert sender or fetch id
//        sender = humanDao.insertOrGetFromDb(sender);
//        //todo insert letter if it doesn't have id
//        if (letter.getId() == null) {
//            letter = letterDao.insert(letter);
//        } else letter = letterDao.getFromDB(letter);
//        for (Human human : humanDao.getAll()) {
//            if (!human.getId().equals(sender.getId())) senderList.add(human);
//        }
//        for (Human receiverHuman : senderList) {
//            insertLetterAndReceiver(letter, receiverHuman);
//        }
//    }
//      //todo insert records to linkage table for every receiver_id = (select id from humans where id <> sender.id )
//    private void insertLetterAndReceiver(Letter letter, Human receiverHuman) {
//        try (Connection connection = ConnectionManager.openConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SQL_HUMAN_LETTER_REFERENCES)) {
//            preparedStatement.setLong(1, receiverHuman.getId());
//            preparedStatement.setLong(2, letter.getId());
//
//            preparedStatement.executeUpdate();
//
//        } catch (Exception e) {
//            throw new DaoException(e);
//        }
//    }


}






