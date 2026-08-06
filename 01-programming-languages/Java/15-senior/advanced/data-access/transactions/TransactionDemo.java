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
            // SERIALIZABLE is the safest isolation level for transfers.
            // It acquires range locks that prevent concurrent transactions
            // from reading or modifying the affected rows until this transaction
            // completes, eliminating race conditions like double-spending.
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            try {
                transferMoney(conn, 1, 2, 100);
                // If transferMoney throws (e.g., destination account missing),
                // we never reach commit() — the rollback below undoes the debit.
                conn.commit();
                System.out.println("Transfer completed successfully");
            } catch (SQLException e) {
                // Rollback restores all rows to their pre-transaction state.
                // Even though debit succeeded inside transferMoney, the rollback
                // undoes it — this is the atomicity guarantee of ACID.
                conn.rollback();
                System.out.println("Transfer failed: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            }

            System.out.println();
            System.out.println("=== Transaction with Rollback ===");
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            try {
                String updateSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setDouble(1, 1000000);
                    pstmt.setInt(2, 1);
                    pstmt.executeUpdate();
                }
                // Commit would succeed, but this is a demonstration of rollback.
                // In practice, the business logic would reject this before commit.
                conn.rollback();
                System.out.println("Rollback triggered: debited $1,000,000 from Alice, then rolled back");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback triggered: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            }

            System.out.println();
            System.out.println("=== Savepoints ===");
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            try {
                String sql1 = "UPDATE accounts SET balance = balance + 50 WHERE id = 1";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql1);
                }

                java.sql.Savepoint savepoint = conn.setSavepoint("AFTER_FIRST_UPDATE");
                System.out.println("Savepoint created after first update");

                String sql2 = "UPDATE accounts SET balance = balance + 50 WHERE id = 1";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql2);
                }

                // Rollback to savepoint undoes only the second update.
                // The first update (+50) is preserved — partial rollback within
                // a transaction, useful when later steps fail but earlier steps
                // should remain.
                conn.rollback(savepoint);
                System.out.println("Rolled back to savepoint (second update undone)");

                conn.commit();
                System.out.println("Transaction committed with only the first update applied");
            } finally {
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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

    // Transfers money between two accounts within the current transaction.
    // The caller MUST manage the transaction (setAutoCommit(false), commit/rollback).
    //
    // Transaction flow:
    //   1. Debit: deduct amount from source (fails if insufficient funds or account missing)
    //   2. Credit: add amount to destination (fails if destination account missing)
    //   3. Caller commits on success, or rolls back on any failure (undoing both steps)
    //
    // This guarantees atomicity — money is never created or destroyed,
    // only moved. If any step fails, both operations are undone.
    private static void transferMoney(Connection conn, int from, int to, double amount) throws SQLException {
        // Debit SQL: only deducts if sufficient balance exists (prevents overdraft).
        // The WHERE clause acts as an application-level check — if balance is too low,
        // zero rows are affected and we throw an exception before attempting the credit.
        String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";

        // Credit SQL: adds amount to destination. We must verify exactly 1 row was
        // affected — if the destination account doesn't exist, the UPDATE silently
        // affects 0 rows and money would vanish on commit.
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try (PreparedStatement debitStmt = conn.prepareStatement(debitSql);
             PreparedStatement creditStmt = conn.prepareStatement(creditSql)) {

            // Step 1: Debit source account
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, from);
            debitStmt.setDouble(3, amount);
            int debitRows = debitStmt.executeUpdate();

            if (debitRows == 0) {
                // Either account doesn't exist or insufficient funds.
                // Throwing here means the caller's rollback will undo nothing (debit never happened),
                // which is correct — the transaction is still atomic.
                throw new SQLException("Transfer failed: insufficient funds or source account " + from + " not found");
            }

            // Step 2: Credit destination account
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, to);
            int creditRows = creditStmt.executeUpdate();

            if (creditRows == 0) {
                // Destination account doesn't exist. This is the critical check:
                // without it, the caller would commit successfully and money would
                // disappear. Throwing here forces the caller to rollback, restoring
                // the debit that already occurred.
                throw new SQLException("Transfer failed: destination account " + to + " not found");
            }
        }
        // If we reach here, both operations succeeded. The caller should now commit.
        // If an exception was thrown at any point above, the caller's rollback undoes
        // all changes made in this transaction, including the debit.
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
