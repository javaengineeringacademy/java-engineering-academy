package academy.javaengineering.oop.abstraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates abstraction through abstract classes and interfaces.
 *
 * <p>Abstraction hides implementation details and exposes only the essential
 * features of an object. It's achieved through abstract classes (partial
 * implementation) and interfaces (contract specification).</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Abstract classes: partial implementation, cannot be instantiated</li>
 *   <li>Abstract methods: declared without body, must be implemented by subclasses</li>
 *   <li>Interfaces: pure contracts with default methods</li>
 *   <li>Multiple interface implementation</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class AbstractionExample {

    // ==================== Abstract Classes ====================

    /**
     * Abstract notification channel - provides common behavior, enforces contract.
     */
    public static abstract class NotificationChannel {
        protected final String channelName;
        protected boolean enabled;

        protected NotificationChannel(String channelName) {
            this.channelName = channelName;
            this.enabled = true;
        }

        public String getChannelName() { return channelName; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        /**
         * Template method - defines the algorithm skeleton.
         * Subclasses implement the abstract steps.
         */
        public final String sendNotification(String recipient, String message) {
            if (!enabled) {
                return "[%s] Channel disabled, notification queued".formatted(channelName);
            }
            if (!validateRecipient(recipient)) {
                return "[%s] Invalid recipient: %s".formatted(channelName, recipient);
            }
            String formatted = formatMessage(message);
            String result = deliver(recipient, formatted);
            logDelivery(recipient);
            return result;
        }

        /** Subclasses must implement recipient validation. */
        protected abstract boolean validateRecipient(String recipient);

        /** Subclasses must implement message formatting. */
        protected abstract String formatMessage(String message);

        /** Subclasses must implement the actual delivery mechanism. */
        protected abstract String deliver(String recipient, String formattedMessage);

        /** Hook method with default implementation - subclasses can override. */
        protected void logDelivery(String recipient) {
            // Default no-op logging
        }
    }

    public static class EmailNotification extends NotificationChannel {
        private final String smtpServer;

        public EmailNotification(String smtpServer) {
            super("EMAIL");
            this.smtpServer = smtpServer;
        }

        @Override
        protected boolean validateRecipient(String recipient) {
            return recipient != null && recipient.contains("@");
        }

        @Override
        protected String formatMessage(String message) {
            return "<html><body><p>%s</p></body></html>".formatted(message);
        }

        @Override
        protected String deliver(String recipient, String formattedMessage) {
            return "[EMAIL via %s] To: %s | Content: %s".formatted(smtpServer, recipient, formattedMessage);
        }
    }

    public static class SmsNotification extends NotificationChannel {
        private final int maxSmsLength;

        public SmsNotification(int maxSmsLength) {
            super("SMS");
            this.maxSmsLength = maxSmsLength;
        }

        @Override
        protected boolean validateRecipient(String recipient) {
            return recipient != null && recipient.matches("\\+?\\d{7,15}");
        }

        @Override
        protected String formatMessage(String message) {
            return message.length() > maxSmsLength
                    ? message.substring(0, maxSmsLength - 3) + "..."
                    : message;
        }

        @Override
        protected String deliver(String recipient, String formattedMessage) {
            return "[SMS] To: %s | Text: %s".formatted(recipient, formattedMessage);
        }

        @Override
        protected void logDelivery(String recipient) {
            // SMS requires delivery confirmation logging
        }
    }

    // ==================== Interfaces ====================

    /**
     * Interface defining a repository contract.
     */
    public interface Repository<T, ID> {
        T findById(ID id);
        List<T> findAll();
        void save(T entity);
        void delete(ID id);
        long count();

        /** Default method for bulk operations. */
        default void saveAll(List<T> entities) {
            entities.forEach(this::save);
        }
    }

    /**
     * Auditing capability - mixable via interface.
     */
    public interface Auditable {
        String getAuditId();
        default String auditEntry() {
            return "[AUDIT] Entity: %s at %s".formatted(getAuditId(), java.time.LocalDateTime.now());
        }
    }

    /**
     * Enterprise customer entity implementing multiple interfaces.
     */
    public static class Customer implements Auditable {
        private final long id;
        private String name;

        public Customer(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @Override
        public String getAuditId() { return "Customer#" + id; }
    }

    /**
     * In-memory repository implementing the Repository contract.
     */
    public static class InMemoryCustomerRepository implements Repository<Customer, Long>, Auditable {
        private final java.util.Map<Long, Customer> store = new java.util.HashMap<>();

        @Override
        public Customer findById(Long id) { return store.get(id); }

        @Override
        public List<Customer> findAll() { return new ArrayList<>(store.values()); }

        @Override
        public void save(Customer entity) { store.put(entity.getId(), entity); }

        @Override
        public void delete(Long id) { store.remove(id); }

        @Override
        public long count() { return store.size(); }

        @Override
        public String getAuditId() { return "CustomerRepository"; }
    }

    public static void main(String[] args) {
        System.out.println("=== Abstraction Demo ===\n");

        // Abstract class usage
        System.out.println("--- Abstract Class: Notification Channels ---");
        NotificationChannel email = new EmailNotification("smtp.enterprise.com");
        NotificationChannel sms = new SmsNotification(160);

        System.out.println(email.sendNotification("alice@company.com", "Your order has shipped!"));
        System.out.println(email.sendNotification("invalid-email", "Test message"));
        System.out.println(sms.sendNotification("+15551234567", "Your OTP is 4829"));
        System.out.println(sms.sendNotification("not-a-phone", "Test"));
        System.out.println(email.sendNotification("bob@company.com", "Hello from the enterprise system!"));

        // Interface usage
        System.out.println("\n--- Interface: Repository Pattern ---");
        InMemoryCustomerRepository repo = new InMemoryCustomerRepository();

        repo.save(new Customer(1, "Alice Johnson"));
        repo.save(new Customer(2, "Bob Smith"));
        repo.save(new Customer(3, "Carol White"));

        System.out.println("Customer count: " + repo.count());
        System.out.println("Find #1: " + repo.findById(1L).getName());
        System.out.println("Audit: " + repo.auditEntry());

        repo.delete(2L);
        System.out.println("After delete: count=" + repo.count());

        // Bulk operation via default method
        System.out.println("\n--- Default Methods ---");
        List<Customer> bulk = List.of(
                new Customer(10, "Bulk A"),
                new Customer(11, "Bulk B")
        );
        repo.saveAll(bulk);
        System.out.println("After bulk save: count=" + repo.count());

        // Multiple interface implementation
        System.out.println("\n--- Multiple Interface Implementation ---");
        Customer customer = repo.findById(1L);
        System.out.println("Entity: " + customer.getName());
        System.out.println("Audit: " + customer.auditEntry());
    }
}
