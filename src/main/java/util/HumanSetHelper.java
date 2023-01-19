package util;

import entity.Human;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class HumanSetHelper {
    //todo change method signature to map and move to dao
   /* public void setHuman(Collection<Human> list, ResultSet resultSet) throws SQLException {
        Human human = new Human();
        human.setFirstName(resultSet.getString("first_name"));
        human.setSecondName(resultSet.getString("second_name"));
        human.setBirthday(resultSet.getDate("birthday"));
        list.add(human);
        Очевидно, що метод, який не має значення, що повертається, повинен мати якийсь побічний ефект, який виправдовує
        його існування. Набір методів є прикладом - побічним ефектом є зміна внутрішнього стану об'єкта. Що метод повинен щось вертати?
    }*/

    public Human map(ResultSet resultSet) throws SQLException {
        Human human = new Human();
        human.setId(resultSet.getLong("id"));
        human.setFirstName(resultSet.getString("first_name"));
        human.setSecondName(resultSet.getString("second_name"));
        human.setBirthday(resultSet.getDate("birthday"));
        return human;
    }
}
