package academy.javaengineering.oop.`10-final-keyword`;

import java.util.Objects;

/**
 * Demonstrates the {@code final} keyword for variables, methods, and classes.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>{@code final} variables: constants, cannot be reassigned</li>
 *   <li>{@code final} methods: cannot be overridden</li>
 *   <li>{@code final} classes: cannot be extended</li>
 *   <li>{@code final} parameters: cannot be reassigned in method body</li>
 *   <li>Immutable objects using {@code final} fields</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class FinalExample {

    // ==================== Final Variables ====================

    /** Class-level constants. */
    public static final String APP_NAME = "Enterprise Suite";
    public static final int MAX_RETRY_COUNT = 3;
    public static final double TAX_RATE = 0.0825;

    /**
     * Configuration using final fields for immutability.
     */
    public static class DatabaseConfig {
        private final String host;
        private final int port;
        private final String database;
        private final String username;
        private final boolean useSsl;

        public DatabaseConfig(String host, int port, String database,
                              String username, boolean useSsl) {
            this.host = Objects.requireNonNull(host);
            this.port = port;
            this.database = Objects.requireNonNull(database);
            this.username = Objects.requireNonNull(username);
            this.useSsl = useSsl;
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getDatabase() { return database; }
        public String getUsername() { return username; }
        public boolean isUseSsl() { return useSsl; }

        public String getConnectionString() {
            return "jdbc:postgresql://%s:%d/%s?ssl=%s&user=%s".formatted(
                    host, port, database, useSsl, username);
        }

        @Override
        public String toString() {
            return "DatabaseConfig{host='%s', port=%d, db='%s'}".formatted(host, port, database);
        }
    }

    // ==================== Final Methods ====================

    /**
     * Base service with final methods that cannot be overridden.
     */
    public static class BaseService {
        private final String serviceName;

        protected BaseService(String serviceName) {
            this.serviceName = serviceName;
        }

        /** Final method - subclasses cannot override this. */
        public final String getServiceName() { return serviceName; }

        /** Final method - security-critical, must not be overridden. */
        public final boolean authorize(String user, String action) {
            if (user == null || action == null) return false;
            return auditAccess(user, action);
        }

        /** Protected hook for subclasses to customize audit behavior. */
        protected boolean auditAccess(String user, String action) {
            return true; // Default: allow all
        }

        /** Non-final method - can be overridden. */
        public String getStatus() { return "RUNNING"; }
    }

    public static class SecureService extends BaseService {
        public SecureService() {
            super("SecureService");
        }

        // Cannot override getServiceName() or authorize() - they are final

        @Override
        protected boolean auditAccess(String user, String action) {
            // Custom audit logic
            return !user.startsWith("blocked_");
        }

        @Override
        public String getStatus() { return "SECURE"; }
    }

    // ==================== Final Classes ====================

    /** Final class - cannot be extended. */
    public static final class Money {
        private final double amount;
        private final String currency;

        public Money(double amount, String currency) {
            this.amount = amount;
            this.currency = Objects.requireNonNull(currency);
        }

        public double getAmount() { return amount; }
        public String getCurrency() { return currency; }

        public Money add(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("Currency mismatch");
            }
            return new Money(amount + other.amount, currency);
        }

        public Money multiply(double factor) {
            return new Money(amount * factor, currency);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Money other)) return false;
            return Double.compare(amount, other.amount) == 0 && currency.equals(other.currency);
        }

        @Override
        public int hashCode() { return Objects.hash(amount, currency); }

        @Override
        public String toString() {
            return "%.2f %s".formatted(amount, currency);
        }
    }

    /** Final class - string representation is immutable by design. */
    public static final class EmailAddress {
        private final String localPart;
        private final String domain;

        public EmailAddress(String localPart, String domain) {
            this.localPart = Objects.requireNonNull(localPart);
            this.domain = Objects.requireNonNull(domain);
        }

        public String getLocalPart() { return localPart; }
        public String getDomain() { return domain; }
        public String full() { return localPart + "@" + domain; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EmailAddress other)) return false;
            return localPart.equals(other.localPart) && domain.equals(other.domain);
        }

        @Override
        public int hashCode() { return Objects.hash(localPart, domain); }

        @Override
        public String toString() { return full(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Final Keyword Demo ===\n");

        // Final variables (constants)
        System.out.println("--- Final Variables (Constants) ---");
        System.out.println("App: " + APP_NAME);
        System.out.println("Max retries: " + MAX_RETRY_COUNT);
        System.out.printf("Tax rate: %.4f%%%n", TAX_RATE * 100);

        // Cannot reassign final variables:
        // APP_NAME = "New Name"; // Compilation error!

        // Final fields - immutable objects
        System.out.println("\n--- Immutable Objects (Final Fields) ---");
        DatabaseConfig config = new DatabaseConfig(
                "db.enterprise.com", 5432, "production", "admin", true);
        System.out.println("Config: " + config);
        System.out.println("Connection: " + config.getConnectionString());

        // Cannot modify final fields:
        // config.host = "new-host"; // Compilation error!

        // Final methods
        System.out.println("\n--- Final Methods ---");
        SecureService service = new SecureService();
        System.out.println("Service: " + service.getServiceName());
        System.out.println("Status: " + service.getStatus());
        System.out.println("Authorize admin: " + service.authorize("admin", "read"));
        System.out.println("Authorize blocked: " + service.authorize("blocked_user", "write"));

        // Final class - cannot be extended
        System.out.println("\n--- Final Classes ---");
        Money price = new Money(29.99, "USD");
        Money tax = price.multiply(0.0825);
        Money total = price.add(tax);

        System.out.println("Price: " + price);
        System.out.println("Tax:   " + tax);
        System.out.println("Total: " + total);

        EmailAddress email = new EmailAddress("admin", "enterprise.com");
        System.out.println("Email: " + email);
        System.out.println("Equal: " + email.equals(new EmailAddress("admin", "enterprise.com")));

        // Cannot extend final classes:
        // class ExtendedMoney extends Money {} // Compilation error!
    }
}
