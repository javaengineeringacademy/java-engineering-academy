# Bean Validation

## Comprehensive Guide to Jakarta Bean Validation

Bean Validation provides a standardized way to validate Java objects using annotations. This guide covers annotations, constraints, and custom validators.

---

## Table of Contents

1. [Bean Validation Overview](#bean-validation-overview)
2. [Built-in Constraints](#built-in-constraints)
3. [Complex Constraints](#complex-constraints)
4. [Validation Groups](#validation-groups)
5. [Custom Validators](#custom-validators)
6. [Best Practices](#best-practices)

---

## Bean Validation Overview

### Dependencies

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Manual -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

### Basic Usage

```java
@RestController
public class UserController {

    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }
}
```

---

## Built-in Constraints

### String Constraints

```java
public class UserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String displayName;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only alphanumeric allowed")
    private String alphanumeric;
}
```

### Numeric Constraints

```java
public class ProductRequest {

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    @DecimalMax(value = "999999.99", message = "Price too high")
    private BigDecimal price;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 1, message = "Minimum quantity is 1")
    @Max(value = 1000, message = "Maximum quantity is 1000")
    private Integer orderQuantity;
}
```

### Date/Time Constraints

```java
public class EventRequest {

    @NotNull(message = "Start time is required")
    @Past(message = "Start time must be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull
    @FutureOrPresent(message = "Registration cannot be in the past")
    private LocalDate registrationDeadline;
}
```

### Collection Constraints

```java
public class OrderRequest {

    @NotEmpty(message = "At least one item required")
    private List<OrderItem> items;

    @Size(min = 1, max = 10, message = "1-10 items allowed")
    private List<String> tags;

    @Valid
    private List<@NotNull OrderItem> validatedItems;
}
```

---

## Complex Constraints

### Null Safety

```java
public class DocumentRequest {

    @Null(message = "ID must be null for creation")
    private Long id;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Content is required")
    private String content;
}
```

### Conditional Validation

```java
public class PaymentRequest {

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Card number required for card payments",
        groups = CardPaymentGroup.class)
    @Pattern(regexp = "^\\d{16}$", message = "Invalid card number",
        groups = CardPaymentGroup.class)
    private String cardNumber;
}
```

### Custom Constraints

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "Invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.matches("^\\+?[1-9]\\d{1,14}$");
    }
}
```

---

## Validation Groups

### Group Definition

```java
public interface CreateValidation {}
public interface UpdateValidation {}
public interface DeleteValidation {}
```

### Usage

```java
public class UserRequest {

    @Null(groups = UpdateValidation.class)
    @NotNull(groups = CreateValidation.class)
    private Long id;

    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class})
    private String name;
}

@RestController
public class UserController {

    @PostMapping("/users")
    public ResponseEntity<User> create(
            @Validated(CreateValidation.class)
            @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @Validated(UpdateValidation.class)
            @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }
}
```

---

## Custom Validators

### Simple Validator

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default "Username already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

@Component
public class UniqueUsernameValidator
        implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) return true;
        return !userRepository.existsByUsername(username);
    }
}
```

### Cross-Field Validator

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {
    String message() default "End date must be after start date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class DateRangeValidator
        implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraint) {
        this.startDateField = "startDate";
        this.endDateField = "endDate";
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalDateTime startDate = (LocalDateTime) getFieldValue(value, startDateField);
        LocalDateTime endDate = (LocalDateTime) getFieldValue(value, endDateField);

        if (startDate == null || endDate == null) return true;
        return endDate.isAfter(startDate);
    }
}
```

---

## Best Practices

### 1. Use Message Keys

```java
// Instead of hardcoded messages
@NotBlank(message = "{user.name.required}")
private String name;

// messages.properties
user.name.required=Name is required
user.email.invalid=Email must be valid
```

### 2. Validate Early

```java
@Service
public class UserService {

    public User create(CreateUserRequest request) {
        // Validation happens before this method is called
        // via @Valid on controller parameter
        return userRepository.save(mapToEntity(request));
    }
}
```

### 3. Use Groups for Complex Flows

```java
public interface Step1Validation {}
public interface Step2Validation {}
public interface Step3Validation {}

// Multi-step wizard
@PostMapping("/wizard/step1")
public ResponseEntity<Void> step1(
        @Validated(Step1Validation.class) @RequestBody Step1Request request) { }

@PostMapping("/wizard/step2")
public ResponseEntity<Void> step2(
        @Validated(Step2Validation.class) @RequestBody Step2Request request) { }
```

### 4. Handle Validation Errors

```java
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        List<FieldError> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> new FieldError(e.getField(), e.getDefaultMessage()))
            .collect(Collectors.toList());

        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", errors));
    }
}
```

---

## Further Reading

- [Jakarta Bean Validation](https://beanvalidation.org/)
- [Hibernate Validator](https://hibernate.org/validator/)
- [Spring Validation](https://docs.spring.io/spring-framework/reference/core/validation.html)
