package academy.javaengineering.oop.`08-method-overriding`;

/**
 * Demonstrates method overriding, {@code @Override}, and covariant return types.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>{@code @Override} annotation for compile-time safety</li>
 *   <li>Covariant return types (narrowing return types)</li>
 *   <li>Calling {@code super} methods from overridden methods</li>
 *   <li>Covariant return types with generics</li>
 *   <li>Override rules: same name, same or compatible signature</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class MethodOverridingExample {

    /**
     * Base repository class with overridable methods.
     */
    public static class BaseRepository<T> {
        protected final String tableName;

        protected BaseRepository(String tableName) {
            this.tableName = tableName;
        }

        /**
         * Generates a find-all query. Subclasses override to customize.
         */
        public String findAllQuery() {
            return "SELECT * FROM %s".formatted(tableName);
        }

        /**
         * Returns the entity type name. Overridden to return specific types.
         */
        public String getEntityName() {
            return "Entity";
        }

        /**
         * Creates a default instance. Overridden with covariant return.
         */
        public T createDefault() {
            return null;
        }

        /**
         * Hook method for customizing SQL generation.
         */
        protected String sqlDialect() {
            return "standard";
        }

        public String describe() {
            return "%s repository [%s] - %s".formatted(getEntityName(), tableName, sqlDialect());
        }
    }

    /**
     * User repository with overridden methods and covariant return.
     */
    public static class UserRepository extends BaseRepository<User> {

        public UserRepository() {
            super("users");
        }

        @Override
        public String findAllQuery() {
            return super.findAllQuery() + " WHERE active = true"; // Extend parent
        }

        @Override
        public String getEntityName() { return "User"; }

        @Override
        public User createDefault() { // Covariant return: User instead of Entity
            return new User(0, "new_user@example.com", false);
        }

        @Override
        protected String sqlDialect() { return "PostgreSQL"; }
    }

    /**
     * Product repository with different overriding strategy.
     */
    public static class ProductRepository extends BaseRepository<Product> {

        public ProductRepository() {
            super("products");
        }

        @Override
        public String findAllQuery() {
            return "SELECT id, name, price FROM %s WHERE deleted = false".formatted(tableName);
        }

        @Override
        public String getEntityName() { return "Product"; }

        @Override
        public Product createDefault() {
            return new Product(0, "Untitled", 0.0);
        }

        @Override
        protected String sqlDialect() { return "MySQL"; }

        /** Additional method not in parent. */
        public String findByNameQuery(String namePattern) {
            return findAllQuery() + " AND name LIKE '%" + namePattern + "%'";
        }
    }

    /**
     * Audit log repository demonstrating full override chain.
     */
    public static class AuditLogRepository extends BaseRepository<AuditLog> {

        public AuditLogRepository() {
            super("audit_logs");
        }

        @Override
        public String findAllQuery() {
            return "SELECT * FROM %s ORDER BY created_at DESC".formatted(tableName);
        }

        @Override
        public String getEntityName() { return "AuditLog"; }

        @Override
        public AuditLog createDefault() {
            return new AuditLog(0, "SYSTEM", "default");
        }

        @Override
        protected String sqlDialect() { return "PostgreSQL"; }
    }

    // ==================== Covariant Return Types ====================

    public static class User {
        private final long id;
        private final String email;
        private final boolean active;

        public User(long id, String email, boolean active) {
            this.id = id;
            this.email = email;
            this.active = active;
        }

        public long getId() { return id; }
        public String getEmail() { return email; }
        public boolean isActive() { return active; }

        @Override
        public String toString() {
            return "User{id=%d, email='%s', active=%s}".formatted(id, email, active);
        }
    }

    public static class Product {
        private final long id;
        private final String name;
        private final double price;

        public Product(long id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() {
            return "Product{id=%d, name='%s', price=%.2f}".formatted(id, name, price);
        }
    }

    public static class AuditLog {
        private final long id;
        private final String action;
        private final String details;

        public AuditLog(long id, String action, String details) {
            this.id = id;
            this.action = action;
            this.details = details;
        }

        public long getId() { return id; }
        public String getAction() { return action; }

        @Override
        public String toString() {
            return "AuditLog{id=%d, action='%s'}".formatted(id, action);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Method Overriding Demo ===\n");

        // Polymorphic usage - overridden methods called dynamically
        System.out.println("--- Overridden Query Generation ---");
        BaseRepository<?>[] repositories = {
                new UserRepository(),
                new ProductRepository(),
                new AuditLogRepository()
        };

        for (BaseRepository<?> repo : repositories) {
            System.out.println("\n" + repo.describe());
            System.out.println("  Query: " + repo.findAllQuery());
            System.out.println("  Default: " + repo.createDefault());
        }

        // Covariant return types
        System.out.println("\n--- Covariant Return Types ---");
        UserRepository userRepo = new UserRepository();
        ProductRepository productRepo = new ProductRepository();

        User defaultUser = userRepo.createDefault(); // Returns User, not Entity
        Product defaultProduct = productRepo.createDefault(); // Returns Product

        System.out.println("Default User: " + defaultUser);
        System.out.println("Default Product: " + defaultProduct);

        // Super call demonstration
        System.out.println("\n--- Super Method Calls ---");
        System.out.println("UserRepository findAll:");
        System.out.println("  Without super: SELECT * FROM users");
        System.out.println("  With super:    " + userRepo.findAllQuery());

        System.out.println("\nProductRepository findAll:");
        System.out.println("  Without super: SELECT * FROM products");
        System.out.println("  With super:    " + productRepo.findAllQuery());
    }
}
