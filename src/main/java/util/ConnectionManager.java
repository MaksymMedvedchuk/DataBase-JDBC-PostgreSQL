package util;

import util.PropertiesKeys;
import util.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private ConnectionManager() {
    }

    public static Connection openConnection() { //Connection клас для вікриття зєднання з БД
        return openConnection(true);
    }

    public static Connection openConnectionForTransaction() { //Connection клас для вікриття зєднання з БД
        return openConnection(false);
    }

    public static Connection openConnection(boolean isAutocommitOn) { //Connection клас для вікриття зєднання з БД
        try {
            Connection connection = DriverManager.getConnection(PropertyReader.getKey(PropertiesKeys.URL.getKey()),
                    PropertyReader.getKey(PropertiesKeys.USER_NAME.getKey()),
                    PropertyReader.getKey(PropertiesKeys.PASSWORD.getKey())); //DriverManager клас для керування драйверами jdbc
            connection.setAutoCommit(isAutocommitOn);
            return connection;
            //getConnection встановлює зєднаня з БД по URL
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
