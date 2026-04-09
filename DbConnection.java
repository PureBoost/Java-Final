// Database connection helper for PostgreSQL.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/qap4";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    private DbConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
