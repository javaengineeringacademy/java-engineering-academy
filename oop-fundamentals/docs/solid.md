# SOLID Principles

| Principle | Definition | Violation Example |
|-----------|------------|-------------------|
| **S**ingle Responsibility | Class has one reason to change | God class doing everything |
| **O**pen/Closed | Open for extension, closed for modification | `if (type == A) ... else if (type == B)` |
| **L**iskov Substitution | Subtype must be substitutable for base | Square extends Rectangle (can't set width/height independently) |
| **I**nterface Segregation | Many specific interfaces > one general | `IShape` with `draw()`, `resize()`, `rotate()` for all |
| **D**ependency Inversion | Depend on abstractions, not concretions | `OrderService` depends on `PaymentProcessor` interface, not `StripeProcessor` |

## S - Single Responsibility
A class should have one, and only one, reason to change.

**Violation:**
```java
class UserManager {
    public void saveUser(User user) { ... }
    public void sendEmail(User user) { ... }
    public void generateReport() { ... }
    public void backupDatabase() { ... }
}
```

**Fix:** Separate into `UserRepository`, `EmailService`, `ReportGenerator`, `BackupService`

## O - Open/Closed
Open for extension, closed for modification.

**Violation:**
```java
if (type == A) { ... } else if (type == B) { ... }
```

**Fix:** Use polymorphism - add new types by creating new classes, not modifying existing code.

## L - Liskov Substitution
Subtypes must be substitutable for their base types.

**Violation:** Square extends Rectangle
```java
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(10);  // Square ignores height!
assert r.getWidth() == 5;  // FAILS
```

**Fix:** Use composition or separate interfaces.

## I - Interface Segregation
Many specific interfaces > one general interface.

**Violation:** Fat interface
```java
interface IShape {
    void draw();
    void resize();      // Circle doesn't need
    void rotate();      // Circle doesn't need
    void fill();        // Line doesn't need
}
```

**Fix:** Segregate into `Drawable`, `Resizable`, `Rotatable`, `Fillable`

## D - Dependency Inversion
Depend on abstractions, not concretions.

**Violation:**
```java
class OrderService {
    private StripeProcessor processor = new StripeProcessor();
}
```

**Fix:**
```java
class OrderService {
    private final PaymentProcessor processor;  // Interface
    
    public OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }
}
```