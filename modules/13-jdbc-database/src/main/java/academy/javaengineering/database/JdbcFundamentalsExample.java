package academy.javaengineering.database;

import java.sql.*;

/**
 * JDBC Fundamentals - Connection, Statement, ResultSet, CRUD.
 */
public class JdbcFundamentalsExample {

    public static class DatabaseConfig {
        private final String url;
        private final String username;
        private final String password;

        public DatabaseConfig(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }
    }

    public static class UserRepository {
        private final DatabaseConfig config;

        public UserRepository(DatabaseConfig config) {
            this.config = config;
        }

        public void createTable() throws SQLException {
            String sql = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100))";
            try (Connection conn = config.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }

        public void insert(String id, String name, String email) throws SQLException {
            String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
            try (Connection conn = config.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(id));
                pstmt.setString(2, name);
                pstmt.setString(3, email);
                pstmt.executeUpdate();
            }
        }

        public ResultSet findAll() throws SQLException {
            Connection conn = config.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM users");
        }
    }

    public static void main(String[] args) {
        System.out.println("JDBC Fundamentals - Requires database connection");
    }
}
