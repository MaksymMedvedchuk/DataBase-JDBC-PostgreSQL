import javax.xml.transform.Result;
import java.sql.*;

public class JdbcRunner {

    public static void main(String[] args) {
        try (Connection connection = ConnectionManager.openConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM human");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





