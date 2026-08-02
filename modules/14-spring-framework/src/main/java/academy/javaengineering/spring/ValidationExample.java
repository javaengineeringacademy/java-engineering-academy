package academy.javaengineering.spring;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation - @Valid, @NotNull, Custom Validators.
 */
public class ValidationExample {

    public interface Validator<T> {
        List<String> validate(T value);
    }

    public static class EmailValidator implements Validator<String> {
        @Override
        public List<String> validate(String email) {
            List<String> errors = new ArrayList<>();
            if (email == null) errors.add("Email cannot be null");
            else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) errors.add("Invalid email format");
            return errors;
        }
    }

    public static class User {
        private final String name;
        private final String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    public static void main(String[] args) {
        EmailValidator validator = new EmailValidator();
        System.out.println("Valid: " + validator.validate("test@test.com"));
        System.out.println("Invalid: " + validator.validate("invalid"));
        System.out.println("Null: " + validator.validate(null));
    }
}
