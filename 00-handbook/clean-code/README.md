# Clean Code

Comprehensive guide to writing clean, readable, and maintainable code with meaningful names, functions, comments, formatting, objects, and error handling.

---

## Table of Contents

1. [Overview](#overview)
2. [Meaningful Names](#meaningful-names)
3. [Functions](#functions)
4. [Comments](#comments)
5. [Formatting](#formatting)
6. [Objects and Data Structures](#objects-and-data-structures)
7. [Error Handling](#error-handling)
8. [Unit Tests](#unit-tests)
9. [Best Practices](#best-practices)
10. [Common Mistakes](#common-mistakes)
11. [Key Takeaways](#key-takeaways)

---

## Overview

Clean code is code that is easy to understand, easy to change, and easy to maintain. It's code that reads like well-written prose and communicates its intent clearly.

### Why Clean Code Matters

- **Readability**: Others can understand it quickly
- **Maintainability**: Easier to fix bugs and add features
- **Collaboration**: Team can work together effectively
- **Reduced bugs**: Fewer misunderstandings and errors
- **Professional pride**: Writing code you're proud of

### The Boy Scout Rule

"Leave the code cleaner than you found it."

- Refactor when you see messy code
- Improve naming and structure
- Add tests for untested code
- Remove dead code

---

## Meaningful Names

### Use Intention-Revealing Names

**Bad**
```java
int d; // elapsed time in days
```

**Good**
```java
int elapsedTimeInDays;
```

### Avoid Disinformation

**Bad**
```java
List<User> accountList; // It's not a List
String nameTable; // It's not a Table
```

**Good**
```java
List<User> accounts;
String name;
```

### Make Meaningful Distinctions

**Bad**
```java
public void copyCharacters(char[] source, char[] target) {
    // ...
}

// What's the difference?
copyCharacters(sourceArray, destinationArray);
copyCharacters(sourceData, destinationInfo);
```

**Good**
```java
public void copyCharacters(char[] source, char[] target) {
    // ...
}

// Clear distinction
copyCharacters(sourceCharacters, targetCharacters);
copyCharacters(sourceBuffer, targetBuffer);
```

### Use Pronounceable Names

**Bad**
```java
String genymdhms; // generation date, hour, minute, second
```

**Good**
```java
String generationTimestamp;
```

### Use Searchable Names

**Bad**
```java
// Magic number
if (status == 4) {
    // ...
}

// Hard to search
for (int i = 0; i < list.size(); i++) {
    // ...
}
```

**Good**
```java
// Named constant
if (status == STATUS_COMPLETED) {
    // ...
}

// Searchable
for (int index = 0; index < list.size(); index++) {
    // ...
}
```

### Avoid Encodings

**Bad**
```java
String m_dsc; // What does m_ mean?
String iScore; // What does i mean?
```

**Good**
```java
String memberDescription;
int score;
```

---

## Functions

### Small

**Rule**: Functions should be small. Very small.

**Bad**
```java
public void processOrder(Order order) {
    // 100 lines of code
    // Validation
    // Calculation
    // Database operations
    // Email sending
    // Logging
    // Error handling
}
```

**Good**
```java
public void processOrder(Order order) {
    validateOrder(order);
    double total = calculateTotal(order);
    processPayment(order.getCustomer(), total);
    sendConfirmationEmail(order);
    logOrderProcessed(order);
}
```

### Do One Thing

**Rule**: Functions should do one thing. Do it well. Do it only.

**Bad**
```java
public void processOrder(Order order) {
    // Does many things
    validateOrder(order);
    calculateTotal(order);
    processPayment(order.getCustomer(), total);
    sendEmail(order);
    logOrder(order);
    updateInventory(order);
    notifyWarehouse(order);
}
```

**Good**
```java
public void processOrder(Order order) {
    validateOrder(order);
    processPayment(order);
    notifyStakeholders(order);
}

private void validateOrder(Order order) {
    // Only validation
}

private void processPayment(Order order) {
    // Only payment processing
}

private void notifyStakeholders(Order order) {
    // Only notifications
}
```

### One Level of Abstraction per Function

**Bad**
```java
public void processOrder(Order order) {
    validateOrder(order);
    double total = calculateTotal(order);
    PaymentResult result = paymentGateway.charge(order.getCustomer(), total);
    if (result.isSuccessful()) {
        order.setStatus(Status.PROCESSED);
        userRepository.save(order.getCustomer());
        emailService.sendConfirmation(order);
        logger.info("Order processed: " + order.getId());
    } else {
        order.setStatus(Status.FAILED);
        logger.error("Payment failed for order: " + order.getId());
    }
}
```

**Good**
```java
public void processOrder(Order order) {
    validateOrder(order);
    processPayment(order);
    updateOrderStatus(order);
    notifyCustomer(order);
}

private void validateOrder(Order order) {
    // Only validation
}

private void processPayment(Order order) {
    // Only payment processing
}

private void updateOrderStatus(Order order) {
    // Only status update
}

private void notifyCustomer(Order order) {
    // Only notification
}
```

### Switch Statements

**Bad**
```java
public double calculatePay(Employee employee) {
    switch (employee.getType()) {
        case ENGINEERING:
            return employee.getBasePay() + employee.getBonus();
        case SALES:
            return employee.getBasePay() + employee.getCommission();
        case MANAGER:
            return employee.getBasePay() + employee.getBonus() + employee.getStockOptions();
        default:
            return employee.getBasePay();
    }
}
```

**Good**
```java
public double calculatePay(Employee employee) {
    return employee.calculatePay();
}

// In Employee class
public abstract double calculatePay();

// In subclasses
public class EngineeringEmployee extends Employee {
    @Override
    public double calculatePay() {
        return getBasePay() + getBonus();
    }
}
```

### Use Descriptive Names

**Bad**
```java
public void doWork() {
    // What work?
}

public void handleData() {
    // What data? What handling?
}
```

**Good**
```java
public void processOrder() {
    // Clear what work
}

public void validateUserInput() {
    // Clear what data and handling
}
```

---

## Comments

### Explain Why, Not What

**Bad**
```java
// Increment counter
count++;

// Check if user is active
if (user.isActive()) {
    // ...
}
```

**Good**
```java
// We increment the counter to track the number of attempts
// because we need to limit retries to prevent abuse
count++;

// We check if the user is active because inactive users
// should not be able to access protected resources
if (user.isActive()) {
    // ...
}
```

### Good Comments

**Intent Comments**
```java
// This is a temporary workaround until the API team fixes the bug
// in the rate limiting logic. See JIRA-1234.
String workaround = "fixed-value";
```

**Clarification Comments**
```java
// The result is in milliseconds, not seconds
long result = calculateDuration();
```

**Warning of Consequences**
```java
// WARNING: This method modifies the original list
// Callers should make a copy if they need the original
public void sortList(List<String> list) {
    Collections.sort(list);
}
```

### Bad Comments

**Redundant Comments**
```java
// Increment counter
count++;

// Return the result
return result;
```

**Misleading Comments**
```java
// This method adds two numbers (but it actually multiplies)
public int add(int a, int b) {
    return a * b;
}
```

**Mandatory Comments**
```java
// This comment is required by the coding standards
// but doesn't add any value
public void method() {
    // ...
}
```

---

## Formatting

### Vertical Formatting

**File Size**
- Keep files small
- Aim for under 200 lines
- Split large files into smaller ones

**Section Spacing**
```java
public class UserService {
    
    // Constants
    private static final int MAX_RETRY_COUNT = 3;
    
    // Fields
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    // Constructor
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    
    // Public methods
    public User createUser(String email) {
        // ...
    }
    
    // Private methods
    private void validateEmail(String email) {
        // ...
    }
}
```

### Horizontal Formatting

**Line Length**
- Keep lines under 80-100 characters
- Break long lines logically
- Use consistent indentation

**Spacing**
```java
// Good: Space around operators
int result = a + b;

// Good: Space after keywords
if (condition) {
    // ...
}

// Good: No space inside parentheses
public void method(int a, int b) {
    // ...
}
```

### Horizontal Rules

**Use Horizontal Rules to Separate Sections**
```java
public class UserService {
    
    // ============================================
    // Constants
    // ============================================
    
    private static final int MAX_RETRY_COUNT = 3;
    
    // ============================================
    // Fields
    // ============================================
    
    private final UserRepository userRepository;
    
    // ============================================
    // Constructor
    // ============================================
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // ============================================
    // Public Methods
    // ============================================
    
    public User createUser(String email) {
        // ...
    }
    
    // ============================================
    // Private Methods
    // ============================================
    
    private void validateEmail(String email) {
        // ...
    }
}
```

---

## Objects and Data Structures

### Data Abstraction

**Bad**
```java
public class Point {
    public double x;
    public double y;
}
```

**Good**
```java
public class Point {
    private double x;
    private double y;
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}
```

### Data/Object Anti-Symmetry

**Data Structures**
```java
// Data structures expose data and have no functions
public class Point {
    public double x;
    public double y;
}

// Usage
Point point = new Point();
point.x = 10;
point.y = 20;
```

**Objects**
```java
// Objects hide data and expose functions
public class Point {
    private double x;
    private double y;
    
    public double distanceTo(Point other) {
        return Math.sqrt(
            Math.pow(this.x - other.x, 2) +
            Math.pow(this.y - other.y, 2)
        );
    }
}

// Usage
Point point1 = new Point(10, 20);
Point point2 = new Point(30, 40);
double distance = point1.distanceTo(point2);
```

### The Law of Demeter

**Bad**
```java
// Violates Law of Demeter
customer.getOrder().getItems().get(0).getPrice();
```

**Good**
```java
// Follows Law of Demeter
customer.getFirstItemPrice();
```

---

## Error Handling

### Use Exceptions Rather Than Return Codes

**Bad**
```java
public int processOrder(Order order) {
    if (order == null) {
        return -1;
    }
    if (order.getItems().isEmpty()) {
        return -2;
    }
    if (order.getTotal() <= 0) {
        return -3;
    }
    // Process order
    return 0;
}
```

**Good**
```java
public void processOrder(Order order) {
    validateOrder(order);
    // Process order
}

private void validateOrder(Order order) {
    if (order == null) {
        throw new IllegalArgumentException("Order cannot be null");
    }
    if (order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must contain items");
    }
    if (order.getTotal() <= 0) {
        throw new IllegalArgumentException("Order total must be positive");
    }
}
```

### Write Your Own Error Messages

**Bad**
```java
throw new Exception("Error");
throw new IllegalArgumentException("Invalid");
```

**Good**
```java
throw new IllegalArgumentException(
    "Order total must be positive, but was: " + order.getTotal()
);
```

### Define Exception Classes in Terms of a Caller's Needs

**Bad**
```java
public class DatabaseException extends Exception {
    // Too generic
}
```

**Good**
```java
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String email) {
        super("User already exists with email: " + email);
    }
}
```

### Don't Return Null

**Bad**
```java
public User findUser(String email) {
    if (userExists(email)) {
        return getUser(email);
    }
    return null; // Caller must check for null
}

// Usage
User user = findUser("john@example.com");
if (user != null) { // Easy to forget
    // ...
}
```

**Good**
```java
public Optional<User> findUser(String email) {
    if (userExists(email)) {
        return Optional.of(getUser(email));
    }
    return Optional.empty();
}

// Usage
findUser("john@example.com")
    .ifPresent(user -> {
        // ...
    });
```

### Don't Pass Null

**Bad**
```java
public void processOrder(Order order, User user) {
    if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
    }
    // Process order
}

// Easy to pass null
processOrder(order, null); // Compiles but fails at runtime
```

**Good**
```java
public void processOrder(Order order, User user) {
    Objects.requireNonNull(order, "Order cannot be null");
    Objects.requireNonNull(user, "User cannot be null");
    // Process order
}

// Compile-time protection (with @NonNull annotations)
public void processOrder(@NonNull Order order, @NonNull User user) {
    // Process order
}
```

---

## Unit Tests

### The Three Laws of TDD

1. **First Law**: You may not write production code until you have written a failing unit test.
2. **Second Law**: You may not write more of a unit test than is sufficient to fail, and not compiling is failing.
3. **Third Law**: You may not write more production code than is sufficient to pass the currently failing test.

### Keep Tests Clean

**Bad**
```java
@Test
public void testProcessOrder() {
    // Complex test that's hard to understand
    Order order = new Order();
    order.setItems(Arrays.asList(new Item("Product A", 10.0)));
    order.setCustomer(new Customer("John", "john@example.com"));
    OrderConfirmation confirmation = orderService.processOrder(order);
    assertNotNull(confirmation);
    assertEquals(10.0, confirmation.getTotal(), 0.01);
    assertEquals("John", confirmation.getCustomerName());
    assertEquals("john@example.com", confirmation.getCustomerEmail());
    assertEquals(Status.PROCESSED, confirmation.getStatus());
}
```

**Good**
```java
@Test
void shouldProcessOrderSuccessfully() {
    // Arrange
    Order order = createValidOrder();
    
    // Act
    OrderConfirmation confirmation = orderService.processOrder(order);
    
    // Assert
    assertThat(confirmation).isNotNull();
    assertThat(confirmation.getTotal()).isEqualTo(10.0);
    assertThat(confirmation.getStatus()).isEqualTo(Status.PROCESSED);
}

private Order createValidOrder() {
    Order order = new Order();
    order.setItems(List.of(new Item("Product A", 10.0)));
    order.setCustomer(new Customer("John", "john@example.com"));
    return order;
}
```

### Clean Tests

**FIRST Principles**
- **Fast**: Tests should run quickly
- **Independent**: Tests should not depend on each other
- **Repeatable**: Tests should produce the same results every time
- **Self-Validating**: Tests should have a clear pass/fail output
- **Timely**: Tests should be written in a timely manner

---

## Best Practices

### Daily Practices

1. **Write code as if the next person is a violent psychopath**: Make it clear
2. **Refactor continuously**: Leave code cleaner than you found it
3. **Write tests first**: TDD guides design
4. **Use meaningful names**: Code should be self-documenting
5. **Keep functions small**: One function, one responsibility

### Code Review Practices

1. **Review for clarity**: Can others understand it?
2. **Check for duplication**: Is code repeated?
3. **Verify error handling**: Are errors handled properly?
4. **Ensure tests exist**: Is the code tested?
5. **Look for naming issues**: Are names meaningful?

### Refactoring Practices

1. **Extract methods**: Break large functions into smaller ones
2. **Rename variables**: Use meaningful names
3. **Remove duplication**: Apply DRY principle
4. **Simplify conditionals**: Reduce complexity
5. **Add comments**: Explain why, not what

---

## Common Mistakes

### Naming Mistakes

1. **Single letter names**: Except for loop counters
2. **Abbreviations**: cnt, usr, msg
3. **Hungarian notation**: strName, intCount
4. **Misleading names**: data, info, thing
5. **Inconsistent naming**: Mixing conventions

### Function Mistakes

1. **Too large**: More than 20-30 lines
2. **Multiple responsibilities**: Doing more than one thing
3. **Too many parameters**: More than 3-4 parameters
4. **Deep nesting**: More than 2-3 levels
5. **Side effects**: Doing more than stated

### Comment Mistakes

1. **Redundant comments**: Explaining what code does
2. **Misleading comments**: Comments that are wrong
3. **Mandatory comments**: Comments that don't add value
4. **Outdated comments**: Comments that no longer apply
5. **Commented-out code**: Should be removed

### Error Handling Mistakes

1. **Swallowing exceptions**: Catching and ignoring
2. **Returning null**: Forces null checks
3. **Passing null**: Can cause NPEs
4. **Generic exceptions**: Too broad exception types
5. **Poor error messages**: Not explaining what went wrong

---

## Key Takeaways

1. **Clean code is readable**: Others can understand it quickly
2. **Meaningful names**: Code should be self-documenting
3. **Small functions**: One function, one responsibility
4. **Good comments**: Explain why, not what
5. **Consistent formatting**: Follow style guide
6. **Proper error handling**: Use exceptions, not return codes
7. **Test your code**: TDD guides design
8. **Refactor continuously**: Leave code cleaner than you found it

---

## Additional Resources

- [Naming Conventions](../naming-conventions/README.md) - Naming guidelines
- [Code Style Guide](../code-style-guide/README.md) - Formatting guidelines
- [Coding Standards](../coding-standards/README.md) - Overall standards
- [Engineering Principles](../engineering-principles/README.md) - Core principles
- [Books](../books/README.md) - Recommended reading

---

*Last Updated: August 2026*
