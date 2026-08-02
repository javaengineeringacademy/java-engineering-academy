package academy.javaengineering.database;

import java.sql.*;

/**
 * PreparedStatement - Parameterized Queries, Batch Processing.
 */
public class PreparedStatementExample {

    public static class BatchProcessor {
        private final String url;

        public BatchProcessor(String url) { this.url = url; }

        public void batchInsert(String[][] records) throws SQLException {
            String sql = "INSERT INTO products (id, name, price) VALUES (?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                for (String[] record : records) {
                    pstmt.setInt(1, Integer.parseInt(record[0]));
                    pstmt.setString(2, record[1]);
                    pstmt.setDouble(3, Double.parseDouble(record[2]));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("PreparedStatement Example - Requires database connection");
    }
}
