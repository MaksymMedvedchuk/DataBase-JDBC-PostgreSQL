import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String PASSWORD = "3346";
    private static final String USERNAME = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/Letters";

    private ConnectionManager() {
    }

    public static Connection openConnection() { //Connection клас для вікриття зєднання з БД
        try {
           return DriverManager.getConnection(URL, USERNAME, PASSWORD); //DriverManager клас длч керування драйверами jdbc
            //getConnection встановлює зєднаня з БД по URL
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
