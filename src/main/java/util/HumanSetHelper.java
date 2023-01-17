package util;

import entity.Humans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class HumanSetHelper {

    public void setHuman(Collection<Humans> list, ResultSet resultSet) throws SQLException {
        Humans human = new Humans();
        human.setFirstName(resultSet.getString("first_name"));
        human.setSecondName(resultSet.getString("second_name"));
        human.setBirthday(resultSet.getDate("birthday"));
        list.add(human);
    }
}
