package academy.javaengineering.senior.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionDemo {

    private static final String DB_URL = "jdbc:h2:mem:transactiondb;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, "sa", "")) {
            setupTables(conn);

            System.out.println("=== ACID Properties ===");
            System.out.println("Atomicity: All or nothing");
            System.out.println("Consistency: Valid state transitions");
            System.out.println("Isolation: Concurrent transactions don't interfere");
            System.out.println("Durability: Committed changes persist");
            System.out.println();

            System.out.println("=== Transaction Isolation Levels ===");
            System.out.println("READ_UNCOMMITTED: Dirty reads allowed");
            System.out.println("READ_COMMITTED: No dirty reads");
            System.out.println("REPEATABLE_READ: Consistent reads in transaction");
            System.out.println("SERIALIZABLE: Full isolation, highest overhead");
            System.out.println();

            System.out.println("=== Simple Transaction (Commit) ===");
            conn.setAutoCommit(false);
            try {
                transferMoney(conn, 1, 2, 100);
                conn.commit();
                System.out.println("Transfer completed successfully");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transfer failed: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }

            System.out.println();
            System.out.println("=== Transaction with Rollback ===");
            conn.setAutoCommit(false);
            try {
                String updateSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setDouble(1, 1000000);
                    pstmt.setInt(2, 1);
                    pstmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback triggered: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }

            System.out.println();
            System.out.println("=== Savepoints ===");
            conn.setAutoCommit(false);
            try {
                String sql1 = "UPDATE accounts SET balance = balance + 50 WHERE id = 1";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql1);
                }

                java.sql.Savepoint savepoint = conn.setSavepoint("AFTER_FIRST_UPDATE");
                System.out.println("Savepoint created");

                String sql2 = "UPDATE accounts SET balance = balance + 50 WHERE id = 1";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql2);
                }

                conn.rollback(savepoint);
                System.out.println("Rolled back to savepoint");

                conn.commit();
                System.out.println("Transaction committed with partial changes");
            } finally {
                conn.setAutoCommit(true);
            }

            System.out.println();
            System.out.println("=== Propagation Concepts ===");
            System.out.println("REQUIRED: Join existing or create new");
            System.out.println("REQUIRES_NEW: Always create new");
            System.out.println("SUPPORTS: Join existing or run non-transactional");
            System.out.println("NOT_SUPPORTED: Run non-transactional");
            System.out.println("MANDATORY: Must have existing transaction");
            System.out.println("NEVER: Must not have transaction");
            System.out.println("NOTHING: Do not participate");

            printAccountBalances(conn);
        }
    }

    private static void setupTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE accounts (" +
                "id INT PRIMARY KEY," +
                "name VARCHAR(100)," +
                "balance DECIMAL(15,2)" +
                ")");
            stmt.executeUpdate("INSERT INTO accounts VALUES (1, 'Alice', 1000.00)");
            stmt.executeUpdate("INSERT INTO accounts VALUES (2, 'Bob', 500.00)");
        }
    }

    private static void transferMoney(Connection conn, int from, int to, double amount) throws SQLException {
        String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try (PreparedStatement debitStmt = conn.prepareStatement(debitSql);
             PreparedStatement creditStmt = conn.prepareStatement(creditSql)) {

            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, from);
            debitStmt.setDouble(3, amount);
            int affected = debitStmt.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Insufficient funds or account not found");
            }

            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, to);
            creditStmt.executeUpdate();
        }
    }

    private static void printAccountBalances(Connection conn) throws SQLException {
        System.out.println();
        System.out.println("=== Final Balances ===");
        try (Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT name, balance FROM accounts")) {
            while (rs.next()) {
                System.out.printf("%s: $%.2f%n", rs.getString("name"), rs.getDouble("balance"));
            }
        }
    }
}
