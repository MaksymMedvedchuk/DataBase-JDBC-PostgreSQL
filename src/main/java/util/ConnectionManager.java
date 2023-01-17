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
        try {
            return DriverManager.getConnection(PropertyReader.getKey(PropertiesKeys.URL.getKey()),
                    PropertyReader.getKey(PropertiesKeys.USER_NAME.getKey()),
                    PropertyReader.getKey(PropertiesKeys.PASSWORD.getKey())); //DriverManager клас для керування драйверами jdbc
            //getConnection встановлює зєднаня з БД по URL
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
