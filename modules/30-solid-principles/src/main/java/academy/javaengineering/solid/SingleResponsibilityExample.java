package academy.javaengineering.solid;

/**
 * Demonstrates Single Responsibility Principle (SRP).
 * A class should have only one reason to change.
 */
public class SingleResponsibilityExample {

    // Bad: Class has multiple responsibilities
    static class UserManager {
        public void createUser(String name) {
            // Creating user
            validateUser(name);
            saveUserToDatabase(name);
            sendWelcomeEmail(name);
        }

        private void validateUser(String name) {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
        }

        private void saveUserToDatabase(String name) {
            System.out.println("Saving user: " + name);
        }

        private void sendWelcomeEmail(String name) {
            System.out.println("Sending welcome email to: " + name);
        }
    }

    // Good: Each class has single responsibility
    static class UserValidator {
        public void validate(String name) {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
        }
    }

    static class UserRepository {
        public void save(String name) {
            System.out.println("Saving user: " + name);
        }
    }

    static class UserEmailService {
        public void sendWelcomeEmail(String name) {
            System.out.println("Sending welcome email to: " + name);
        }
    }

    static class UserService {
        private final UserValidator validator;
        private final UserRepository repository;
        private final UserEmailService emailService;

        public UserService(UserValidator validator, UserRepository repository, 
                          UserEmailService emailService) {
            this.validator = validator;
            this.repository = repository;
            this.emailService = emailService;
        }

        public void createUser(String name) {
            validator.validate(name);
            repository.save(name);
            emailService.sendWelcomeEmail(name);
        }
    }
}
