package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private ConnectionManager() {
    }

    public static Connection openConnection() {
        return openConnection(true);
    }

    public static Connection openConnectionForTransaction() {
        return openConnection(false);
    }

    public static Connection openConnection(boolean isAutocommitOn) {
        try {
            Connection connection = DriverManager.getConnection(PropertyReader.getKey(PropertiesKeys.URL.getKey()),
                    PropertyReader.getKey(PropertiesKeys.USER_NAME.getKey()),
                    PropertyReader.getKey(PropertiesKeys.PASSWORD.getKey()));
            connection.setAutoCommit(isAutocommitOn);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
