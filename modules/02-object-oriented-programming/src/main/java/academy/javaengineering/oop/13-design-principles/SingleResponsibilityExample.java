package academy.javaengineering.oop.`13-design-principles`;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Demonstrates the Single Responsibility Principle (SRP) with real code.
 *
 * <p>SRP states that a class should have only one reason to change - it should
 * have only one job or responsibility. This promotes loose coupling and high cohesion.</p>
 *
 * <h3>Before SRP (violated):</h3>
 * <p>A single class handles user management, persistence, and email notifications.</p>
 *
 * <h3>After SRP (adhered):</h3>
 * <p>Separate classes for user management, persistence, and notifications.</p>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SingleResponsibilityExample {

    // ==================== VIOLATION: Single class with multiple responsibilities ====================

    /**
     * BAD EXAMPLE: God class violating SRP.
     * Reasons to change: user logic, persistence, email, validation.
     */
    @SuppressWarnings("unused")
    public static class UserManager {
        private final List<String[]> users = new ArrayList<>();

        /** Responsibility 1: User creation and validation. */
        public boolean createUser(String name, String email) {
            if (name == null || name.isBlank()) return false;
            if (email == null || !email.contains("@")) return false;
            users.add(new String[]{name, email});
            sendWelcomeEmail(email, name); // Mixed responsibility!
            saveToDatabase(); // Mixed responsibility!
            return true;
        }

        /** Responsibility 2: Email sending - NOT related to user management. */
        private void sendWelcomeEmail(String email, String name) {
            System.out.println("[EMAIL] Sending welcome to " + name + " at " + email);
        }

        /** Responsibility 3: Database persistence - NOT related to user management. */
        private void saveToDatabase() {
            System.out.println("[DB] Saving to database...");
        }

        /** Responsibility 4: Reporting - NOT related to user management. */
        public String generateReport() {
            return "User count: " + users.size();
        }
    }

    // ==================== SOLUTION: Separate classes for each responsibility ====================

    /**
     * Responsibility 1: User domain entity and business logic only.
     */
    public static class User {
        private final long id;
        private final String name;
        private final String email;

        public User(long id, String name, String email) {
            this.id = id;
            this.name = Objects.requireNonNull(name);
            this.email = Objects.requireNonNull(email);
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }

        @Override
        public String toString() {
            return "User{id=%d, name='%s', email='%s'}".formatted(id, name, email);
        }
    }

    /**
     * Responsibility 2: User validation logic.
     */
    public static class UserValidator {
        public ValidationResult validate(User user) {
            List<String> errors = new ArrayList<>();
            if (user.getName() == null || user.getName().isBlank()) {
                errors.add("Name is required");
            }
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                errors.add("Valid email is required");
            }
            return new ValidationResult(errors);
        }

        public static class ValidationResult {
            private final List<String> errors;

            public ValidationResult(List<String> errors) {
                this.errors = List.copyOf(errors);
            }

            public boolean isValid() { return errors.isEmpty(); }
            public List<String> getErrors() { return errors; }
        }
    }

    /**
     * Responsibility 3: User persistence (repository pattern).
     */
    public static class UserRepository {
        private final Map<Long, User> store = new java.util.HashMap<>();
        private long nextId = 1;

        public User save(User user) {
            User saved = new User(nextId++, user.getName(), user.getEmail());
            store.put(saved.getId(), saved);
            return saved;
        }

        public User findById(long id) { return store.get(id); }
        public List<User> findAll() { return new ArrayList<>(store.values()); }
        public long count() { return store.size(); }
    }

    /**
     * Responsibility 4: Email notification service.
     */
    public static class EmailNotificationService {
        private final List<String> sentEmails = new ArrayList<>();

        public void sendWelcomeEmail(User user) {
            String message = "Welcome, %s! Your account is ready.".formatted(user.getName());
            send(user.getEmail(), "Welcome!", message);
        }

        public void send(String to, String subject, String body) {
            sentEmails.add("To: %s | Subject: %s | Body: %s".formatted(to, subject, body));
            System.out.println("  [EMAIL] Sent to %s: %s".formatted(to, subject));
        }

        public int getSentCount() { return sentEmails.size(); }
    }

    /**
     * Responsibility 5: User registration service (orchestrator).
     * Coordinates between the separate concerns.
     */
    public static class UserRegistrationService {
        private final UserValidator validator;
        private final UserRepository repository;
        private final EmailNotificationService emailService;

        public UserRegistrationService(UserValidator validator,
                                       UserRepository repository,
                                       EmailNotificationService emailService) {
            this.validator = validator;
            this.repository = repository;
            this.emailService = emailService;
        }

        /**
         * Orchestrates user registration using single-responsibility components.
         *
         * @param name  user's name
         * @param email user's email
         * @return registration result
         */
        public RegistrationResult register(String name, String email) {
            User user = new User(0, name, email);

            // Validate
            UserValidator.ValidationResult validation = validator.validate(user);
            if (!validation.isValid()) {
                return RegistrationResult.failure(validation.getErrors());
            }

            // Persist
            User saved = repository.save(user);

            // Notify
            emailService.sendWelcomeEmail(saved);

            return RegistrationResult.success(saved);
        }

        public static class RegistrationResult {
            private final boolean success;
            private final User user;
            private final List<String> errors;

            private RegistrationResult(boolean success, User user, List<String> errors) {
                this.success = success;
                this.user = user;
                this.errors = errors;
            }

            public static RegistrationResult success(User user) {
                return new RegistrationResult(true, user, List.of());
            }

            public static RegistrationResult failure(List<String> errors) {
                return new RegistrationResult(false, null, errors);
            }

            public boolean isSuccess() { return success; }
            public User getUser() { return user; }
            public List<String> getErrors() { return errors; }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Single Responsibility Principle Demo ===\n");

        // Create single-responsibility components
        UserValidator validator = new UserValidator();
        UserRepository repository = new UserRepository();
        EmailNotificationService emailService = new EmailNotificationService();
        UserRegistrationService service = new UserRegistrationService(validator, repository, emailService);

        // Successful registration
        System.out.println("--- Successful Registration ---");
        UserRegistrationService.RegistrationResult result1 =
                service.register("Alice Johnson", "alice@example.com");
        System.out.println("Success: " + result1.isSuccess());
        System.out.println("User: " + result1.getUser());

        UserRegistrationService.RegistrationResult result2 =
                service.register("Bob Smith", "bob@example.com");
        System.out.println("Success: " + result2.isSuccess());
        System.out.println("User: " + result2.getUser());

        // Failed registration (validation)
        System.out.println("\n--- Failed Registration (Validation) ---");
        UserRegistrationService.RegistrationResult result3 =
                service.register("", "invalid-email");
        System.out.println("Success: " + result3.isSuccess());
        System.out.println("Errors: " + result3.getErrors());

        // Repository state
        System.out.println("\n--- Repository State ---");
        System.out.println("Total users: " + repository.count());
        System.out.println("All users: " + repository.findAll());

        // Email service state
        System.out.println("\n--- Email Service ---");
        System.out.println("Emails sent: " + emailService.getSentCount());
    }
}
