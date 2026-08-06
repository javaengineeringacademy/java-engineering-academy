package academy.javaengineering.senior.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcDemo {

    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("=== Database Connection ===");
            System.out.println("Connected to: " + conn.getMetaData().getDatabaseProductName());
            System.out.println();

            System.out.println("=== Statement (DDL) ===");
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
                System.out.println("Table 'users' created successfully");
            }

            System.out.println();
            System.out.println("=== PreparedStatement (Insert) ===");
            String insertSql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, "John Doe");
                pstmt.setString(2, "john@example.com");
                int rows = pstmt.executeUpdate();
                System.out.println("Inserted " + rows + " row(s)");

                pstmt.setString(1, "Jane Smith");
                pstmt.setString(2, "jane@example.com");
                rows = pstmt.executeUpdate();
                System.out.println("Inserted " + rows + " row(s)");
            }

            System.out.println();
            System.out.println("=== ResultSet (Query) ===");
            String querySql = "SELECT id, name, email FROM users WHERE name LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySql)) {
                pstmt.setString(1, "%John%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        System.out.printf("User: id=%d, name=%s, email=%s%n", id, name, email);
                    }
                }
            }

            System.out.println();
            System.out.println("=== Batch Operations ===");
            String batchSql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(batchSql)) {
                List<String[]> users = List.of(
                    new String[]{"Alice Brown", "alice@example.com"},
                    new String[]{"Bob Wilson", "bob@example.com"},
                    new String[]{"Charlie Davis", "charlie@example.com"}
                );

                conn.setAutoCommit(false);
                for (String[] user : users) {
                    pstmt.setString(1, user[0]);
                    pstmt.setString(2, user[1]);
                    pstmt.addBatch();
                }
                int[] results = pstmt.executeBatch();
                conn.commit();
                System.out.println("Batch inserted " + results.length + " rows");
                conn.setAutoCommit(true);
            }

            System.out.println();
            System.out.println("=== Transaction Management ===");
            conn.setAutoCommit(false);
            try {
                String updateSql = "UPDATE users SET email = ? WHERE name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setString(1, "john.doe@newdomain.com");
                    pstmt.setString(2, "John Doe");
                    pstmt.executeUpdate();
                }
                conn.commit();
                System.out.println("Transaction committed successfully");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction rolled back: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }

            System.out.println();
            System.out.println("=== Final Data ===");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                rs.next();
                System.out.println("Total users: " + rs.getInt(1));
            }
        }
    }
}
