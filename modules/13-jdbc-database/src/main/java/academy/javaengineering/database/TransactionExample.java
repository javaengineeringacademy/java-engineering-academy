package academy.javaengineering.database;

import java.sql.*;

/**
 * Transaction Management - ACID, Commit, Rollback, Savepoints.
 */
public class TransactionExample {

    public static class TransferService {
        private final String url;

        public TransferService(String url) { this.url = url; }

        public void transfer(int fromAccount, int toAccount, double amount) throws SQLException {
            String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
            String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

            try (Connection conn = DriverManager.getConnection(url)) {
                conn.setAutoCommit(false);
                Savepoint savepoint = null;
                try {
                    try (PreparedStatement debit = conn.prepareStatement(debitSql)) {
                        debit.setDouble(1, amount);
                        debit.setInt(2, fromAccount);
                        debit.executeUpdate();
                    }
                    savepoint = conn.setSavepoint("before_credit");
                    try (PreparedStatement credit = conn.prepareStatement(creditSql)) {
                        credit.setDouble(1, amount);
                        credit.setInt(2, toAccount);
                        credit.executeUpdate();
                    }
                    conn.commit();
                } catch (SQLException e) {
                    if (savepoint != null) conn.rollback(savepoint);
                    conn.rollback();
                    throw e;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Transaction Example - Requires database connection");
    }
}
