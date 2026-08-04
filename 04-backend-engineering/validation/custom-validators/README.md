# Custom Validators

## Comprehensive Guide to Custom Bean Validation

Custom validators extend Bean Validation to enforce domain-specific rules. This guide covers field, class, and cross-field validators.

---

## Table of Contents

1. [Custom Constraint Annotations](#custom-constraint-annotations)
2. [Validator Implementation](#validator-implementation)
3. [Cross-Field Validation](#cross-field-validation)
4. [Database Validation](#database-validation)
5. [Best Practices](#best-practices)

---

## Custom Constraint Annotations

### Simple Field Validator

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailDomainValidator.class)
public @interface EmailDomain {
    String message() default "Invalid email domain";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedDomains() default {};
}
```

### Implementation

```java
public class EmailDomainValidator
        implements ConstraintValidator<EmailDomain, String> {

    private List<String> allowedDomains;

    @Override
    public void initialize(EmailDomain constraint) {
        this.allowedDomains = Arrays.asList(constraint.allowedDomains());
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) return true;

        String domain = email.substring(email.indexOf('@') + 1);
        return allowedDomains.contains(domain);
    }
}
```

### Usage

```java
public class ContactRequest {

    @EmailDomain(allowedDomains = {"company.com", "partner.com"})
    private String email;
}
```

---

## Validator Implementation

### Complex Field Validator

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {
    String message() default "Password is not strong enough";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class StrongPasswordValidator
        implements ConstraintValidator<StrongPassword, String> {

    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%^&*]");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return true;

        boolean hasUppercase = UPPERCASE.matcher(password).find();
        boolean hasLowercase = LOWERCASE.matcher(password).find();
        boolean hasDigit = DIGIT.matcher(password).find();
        boolean hasSpecial = SPECIAL.matcher(password).find();
        boolean hasMinLength = password.length() >= 8;

        return hasUppercase && hasLowercase && hasDigit &&
               hasSpecial && hasMinLength;
    }
}
```

### Validator with Spring Dependencies

```java
@Component
public class UniqueEmailValidator
        implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) return true;
        return !userRepository.existsByEmail(email);
    }
}
```

---

## Cross-Field Validation

### Class-Level Validator

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {
    String message() default "Invalid date range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String startDate();
    String endDate();
}

public class DateRangeValidator
        implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraint) {
        this.startDateField = constraint.startDate();
        this.endDateField = constraint.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object startDate = BeanUtils.getProperty(value, startDateField);
            Object endDate = BeanUtils.getProperty(value, endDateField);

            if (startDate == null || endDate == null) return true;

            if (startDate instanceof LocalDateTime start &&
                endDate instanceof LocalDateTime end) {
                return end.isAfter(start);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

### Password Confirmation Validator

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {
    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class PasswordMatchValidator
        implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object password = BeanUtils.getProperty(value, "password");
            Object confirmPassword = BeanUtils.getProperty(value, "confirmPassword");

            if (password == null || confirmPassword == null) return true;

            return password.equals(confirmPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
```

### Usage

```java
@PasswordMatch
public class ChangePasswordRequest {
    private String password;
    private String confirmPassword;
}

@ValidDateRange(startDate = "startDate", endDate = "endDate")
public class EventRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
```

---

## Database Validation

### Async Database Validator

```java
@Component
public class AsyncUniqueValidator implements ConstraintValidator<AsyncUnique, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) return true;

        return userRepository.findByEmail(email).isEmpty();
    }
}
```

### Conditional Database Validation

```java
@Component
public class ConditionalUniqueValidator
        implements ConstraintValidator<ConditionalUnique, String> {

    private final UserRepository userRepository;
    private boolean checkOnUpdate;

    @Override
    public void initialize(ConditionalUnique constraint) {
        this.checkOnUpdate = constraint.checkOnUpdate();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) return true;

        // Skip check for new records
        if (!checkOnUpdate) {
            return true;
        }

        return userRepository.findByEmail(email).isEmpty();
    }
}
```

---

## Best Practices

### 1. Keep Validators Simple

```java
// Good - Single responsibility
@Email
private String email;

@NotBlank
private String name;

// Bad - Overly complex
@CustomValidator(
    rules = {"email", "phone", "address", "name"},
    message = "Invalid contact"
)
private String contact;
```

### 2. Provide Clear Messages

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhoneValidator.class)
public @interface ValidPhone {
    String message() default "Phone number must be in format: +{country code}{number}";
    // ...
}
```

### 3. Handle Null Values

```java
@Override
public boolean isValid(String value, ConstraintValidatorContext context) {
    // Let @NotNull handle null values
    if (value == null) return true;

    // Your validation logic
    return isValidFormat(value);
}
```

### 4. Test Validators

```java
@Test
void testStrongPassword() {
    StrongPasswordValidator validator = new StrongPasswordValidator();

    assertTrue(validator.isValid("StrongP@ss1", null));
    assertFalse(validator.isValid("weak", null));
    assertFalse(validator.isValid("nouppercase1!", null));
    assertFalse(validator.isValid("NOLOWERCASE1!", null));
}
```

### 5. Use Groups When Needed

```java
public interface CreateValidation {}
public interface UpdateValidation {}

public class UserRequest {
    @Null(groups = UpdateValidation.class)
    @NotNull(groups = CreateValidation.class)
    private Long id;

    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class})
    private String name;
}
```

---

## Further Reading

- [Bean Validation Specification](https://beanvalidation.org/)
- [Hibernate Validator](https://hibernate.org/validator/)
- [Custom Constraints](https://docs.jboss.org/hibernate/validator/reference/en-US/html_single/#custom-constraints)
