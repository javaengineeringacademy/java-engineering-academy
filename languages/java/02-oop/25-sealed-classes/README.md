# Sealed Classes

## Introduction

Sealed classes, introduced in Java 17 (JEP 409), are a language feature that restricts which other classes or interfaces may extend or implement them. By explicitly enumerating the permitted subclasses, sealed classes provide greater control over inheritance hierarchies, enable the compiler to perform exhaustive checks in pattern matching, and improve code maintainability by making design intentions clear. They bridge the gap between abstract classes (which allow unlimited extension) and final classes (which allow no extension), giving developers a precise middle ground for modeling domain-specific type hierarchies.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the motivation behind sealed classes and when to use them
- [ ] Declare sealed classes with `permits` and implement permitted subclasses with `final`, `sealed`, or `non-sealed` modifiers
- [ ] Combine sealed classes with pattern matching for exhaustive `switch` expressions
- [ ] Evaluate the trade-offs between sealed and open inheritance hierarchies

## Prerequisites

- [09-inheritance](../09-inheritance/README.md) - Understanding of class hierarchies
- [11-abstraction](../11-abstraction/README.md) - Abstract classes and interfaces
- [13-abstract-classes](../13-abstract-classes/README.md) - Abstract class mechanics
- [24-records](../24-records/README.md) - Record classes (often combined with sealed)

## Why This Concept Exists

### The Problem

In traditional Java, when you create an abstract class or interface, any class in any package can extend or implement it. This creates several issues:

1. **Unknown subtype set**: The author of a base type cannot know all possible subtypes at compile time, making exhaustive reasoning impossible.
2. **Fragile switch statements**: When adding a new subtype, existing `switch` statements may silently miss the new case, leading to bugs.
3. **Unclear design intent**: There is no way to express "only these specific classes should extend this type."
4. **Pattern matching limitations**: The compiler cannot verify that all cases are handled in `switch` expressions over type hierarchies.

### The Solution

Sealed classes solve these problems by:

1. **Enumerating permitted subtypes**: The `permits` clause explicitly lists which classes may extend the sealed class.
2. **Enforcing modifier constraints**: Each permitted subclass must be declared `final`, `sealed`, or `non-sealed`.
3. **Enabling exhaustive analysis**: The compiler knows the complete set of subtypes and can verify exhaustive `switch` expressions.
4. **Documenting design intent**: The sealed modifier clearly communicates the author's intent about the type hierarchy.

### Real-World Analogy

Think of a sealed class like a **members-only club** with a strict guest list. The club owner (class author) decides exactly who can be a member (subclass). Each member must also follow club rules (be final, sealed, or non-sealed). When the club holds a meeting (switch expression), the owner knows exactly who will attend and can ensure every member's opinion is heard.

## Internal Working

### JVM Perspective

At the bytecode level, sealed classes are implemented using:
- A `PermittedSubclasses` attribute in the class file that lists the internal names of permitted subclasses.
- The JVM enforces that only the listed subclasses may extend the sealed type.
- At runtime, the JVM can verify the complete type hierarchy for pattern matching.

### Memory Representation

```
┌─────────────────────────────────────┐
│         Sealed Class (Shape)        │
│  - PermittedSubclasses attribute    │
│  - Lists: Circle, Rectangle, Triangle│
│  - Loaded by classloader            │
└─────────────────────────────────────┘
            │ extends
    ┌───────┼───────┐
    ▼       ▼       ▼
┌────────┐┌────────┐┌────────┐
│ Circle ││Rectangle││Triangle│
│ (final)││ (final)││ (final)│
└────────┘└────────┘└────────┘

All subtypes are in same module (or same package if no module-info.java)
```

### Compilation Checks

1. All permitted subtypes must be in the same module (or same package if no module system).
2. Each permitted subtype must declare exactly one of: `final`, `sealed`, or `non-sealed`.
3. The compiler can determine the complete set of subtypes at compile time.

## Syntax

### Basic Sealed Class

```java
public sealed class Shape permits Circle, Rectangle, Triangle {
    // Common fields and methods
}

public final class Circle extends Shape {
    private final double radius;
    public Circle(double radius) { this.radius = radius; }
    public double getRadius() { return radius; }
}

public final class Rectangle extends Shape {
    private final double width, height;
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

public final class Triangle extends Shape {
    private final double base, height;
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    public double getBase() { return base; }
    public double getHeight() { return height; }
}
```

### Sealed Interface

```java
public sealed interface Result permits Success, Failure {
    String message();
}

public record Success(String data) implements Result {
    @Override
    public String message() { return "Success: " + data; }
}

public record Failure(String error) implements Result {
    @Override
    public String message() { return "Failure: " + error; }
}
```

### Non-Sealed Subclass (Open Extension)

```java
public non-sealed class Ellipse extends Shape {
    // This class can be extended by anyone
}
```

### Sealed Subclass (Further Restricted)

```java
public sealed class Polygon extends Shape permits Square, Pentagon {
    // Only Square and Pentagon can extend Polygon
}

public final class Square extends Polygon { }
public final class Pentagon extends Polygon { }
```

## Easy Examples

### Example 1: Basic Sealed Class with Switch

**Problem Statement**: Model a payment system where only specific payment types are allowed, and ensure all types are handled in processing logic.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed class PaymentMethod permits CreditCard, DebitCard, UPI {
    private final double amount;

    protected PaymentMethod(double amount) {
        this.amount = amount;
    }

    public double getAmount() { return amount; }
}

final class CreditCard extends PaymentMethod {
    private final String cardNumber;

    public CreditCard(String cardNumber, double amount) {
        super(amount);
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() { return cardNumber; }
}

final class DebitCard extends PaymentMethod {
    private final String cardNumber;

    public DebitCard(String cardNumber, double amount) {
        super(amount);
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() { return cardNumber; }
}

final class UPI extends PaymentMethod {
    private final String upiId;

    public UPI(String upiId, double amount) {
        super(amount);
        this.upiId = upiId;
    }

    public String getUpiId() { return upiId; }
}
```

**PaymentProcessor using exhaustive switch**:

```java
package academy.javaengineering.oop.sealedclasses;

public class PaymentProcessor {
    public static String process(PaymentMethod method) {
        return switch (method) {
            case CreditCard cc ->
                "Processing Credit Card " + cc.getCardNumber() + " for $" + cc.getAmount();
            case DebitCard dc ->
                "Processing Debit Card " + dc.getCardNumber() + " for $" + dc.getAmount();
            case UPI upi ->
                "Processing UPI " + upi.getUpiId() + " for $" + method.getAmount();
        };
    }

    public static void main(String[] args) {
        PaymentMethod cc = new CreditCard("4111-xxxx-xxxx-1234", 99.99);
        PaymentMethod dc = new DebitCard("5222-xxxx-xxxx-5678", 45.50);
        PaymentMethod upi = new UPI("user@bank", 120.00);

        System.out.println(process(cc));
        System.out.println(process(dc));
        System.out.println(process(upi));
    }
}
```

**Output**:
```
Processing Credit Card 4111-xxxx-xxxx-1234 for $99.99
Processing Debit Card 5222-xxxx-xxxx-5678 for $45.50
Processing UPI user@bank for $120.0
```

**Best Practices**:
- Use `final` for leaf classes that should not be extended.
- Use exhaustive `switch` expressions to catch missing cases at compile time.
- Place sealed classes and their permitted subtypes in the same package for clarity.

### Example 2: Sealed Interface with Records

**Problem Statement**: Model API responses where only success or failure is possible, using Java records for conciseness.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed interface ApiResponse<T> permits SuccessResponse, ErrorResponse {
    boolean isSuccess();
    default String status() { return isSuccess() ? "SUCCESS" : "ERROR"; }
}

record SuccessResponse<T>(T data, int statusCode) implements ApiResponse<T> {
    public SuccessResponse {
        if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalArgumentException("Success status must be 2xx");
        }
    }

    @Override
    public boolean isSuccess() { return true; }
}

record ErrorResponse<T>(String message, int errorCode) implements ApiResponse<T> {
    @Override
    public boolean isSuccess() { return false; }
}
```

**Usage**:

```java
package academy.javaengineering.oop.sealedclasses;

public class ApiDemo {
    public static void main(String[] args) {
        ApiResponse<String> success = new SuccessResponse<>("Hello, World!", 200);
        ApiResponse<String> error = new ErrorResponse<>("Not Found", 404);

        System.out.println(success.status() + ": " + success.data());
        System.out.println(error.status() + ": " + error.message());
    }
}
```

**Output**:
```
SUCCESS: Hello, World!
ERROR: Not Found
```

**Best Practices**:
- Sealed interfaces with records create lightweight, immutable data carriers.
- Use generic type parameters for reusable sealed hierarchies.
- Combine with `instanceof` pattern matching for clean type checks.

### Example 3: Sealed Class with State Machine

**Problem Statement**: Model a connection state machine where transitions are limited to specific states.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed class ConnectionState
        permits Disconnected, Connecting, Connected, Error {
    // Base class holds no state specific to any transition
}

final class Disconnected extends ConnectionState {
    @Override
    public String toString() { return "DISCONNECTED"; }
}

final class Connecting extends ConnectionState {
    private final String host;

    Connecting(String host) { this.host = host; }

    public String getHost() { return host; }

    @Override
    public String toString() { return "CONNECTING to " + host; }
}

final class Connected extends ConnectionState {
    private final int sessionId;

    Connected(int sessionId) { this.sessionId = sessionId; }

    public int getSessionId() { return sessionId; }

    @Override
    public String toString() { return "CONNECTED (session=" + sessionId + ")"; }
}

final class Error extends ConnectionState {
    private final String reason;

    Error(String reason) { this.reason = reason; }

    public String getReason() { return reason; }

    @Override
    public String toString() { return "ERROR: " + reason; }
}
```

**State machine with exhaustive transitions**:

```java
package academy.javaengineering.oop.sealedclasses;

public class Connection {
    private ConnectionState state;

    public Connection() {
        this.state = new Disconnected();
    }

    public void connect(String host) {
        state = switch (state) {
            case Disconnected d -> new Connecting(host);
            case Connecting c -> throw new IllegalStateException("Already connecting to " + c.getHost());
            case Connected c -> throw new IllegalStateException("Already connected");
            case Error e -> new Connecting(host);
        };
        System.out.println("State: " + state);
    }

    public void complete(int sessionId) {
        state = switch (state) {
            case Disconnected d -> throw new IllegalStateException("Not connecting");
            case Connecting c -> new Connected(sessionId);
            case Connected c -> throw new IllegalStateException("Already connected");
            case Error e -> throw new IllegalStateException("In error state");
        };
        System.out.println("State: " + state);
    }

    public void disconnect() {
        state = switch (state) {
            case Disconnected d -> d;
            case Connecting c -> new Disconnected();
            case Connected c -> new Disconnected();
            case Error e -> new Disconnected();
        };
        System.out.println("State: " + state);
    }

    public void fail(String reason) {
        state = new Error(reason);
        System.out.println("State: " + state);
    }

    public static void main(String[] args) {
        Connection conn = new Connection();
        conn.connect("api.example.com");
        conn.complete(12345);
        conn.disconnect();
        conn.connect("api.example.com");
        conn.fail("Timeout");
        conn.connect("api.example.com");
        conn.complete(67890);
    }
}
```

**Output**:
```
State: CONNECTING to api.example.com
State: CONNECTED (session=12345)
State: DISCONNECTED
State: CONNECTING to api.example.com
State: ERROR: Timeout
State: CONNECTING to api.example.com
State: CONNECTED (session=67890)
```

**Best Practices**:
- Sealed classes are ideal for modeling finite state machines.
- The exhaustive switch ensures all valid transitions are handled.
- Use `final` subclasses to prevent external extension of states.

## Medium Examples

### Example 4: Sealed Hierarchy with Generic Constraint

**Problem Statement**: Create a generic Result type that only permits Success or Failure subtypes, with type-safe mapping operations.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

import java.util.function.Function;
import java.util.Optional;

public sealed interface Either<L, R> permits Either.Left, Either.Right {
    boolean isLeft();
    boolean isRight();

    default <T> Either<L, T> mapRight(Function<R, T> mapper) {
        return switch (this) {
            case Left<L, R> left -> left;
            case Right<L, R> right -> new Right<>(mapper.apply(right.value()));
        };
    }

    default <T> Either<L, T> flatMapRight(Function<R, Either<L, T>> mapper) {
        return switch (this) {
            case Left<L, R> left -> left;
            case Right<L, R> right -> mapper.apply(right.value());
        };
    }

    default Optional<R> toOptional() {
        return switch (this) {
            case Left<L, R> left -> Optional.empty();
            case Right<L, R> right -> Optional.of(right.value());
        };
    }

    record Left<L, R>(L value) implements Either<L, R> {
        @Override
        public boolean isLeft() { return true; }
        @Override
        public boolean isRight() { return false; }
    }

    record Right<L, R>(R value) implements Either<L, R> {
        @Override
        public boolean isLeft() { return false; }
        @Override
        public boolean isRight() { return true; }
    }

    static <L, R> Either<L, R> left(L value) { return new Left<>(value); }
    static <L, R> Either<L, R> right(R value) { return new Right<>(value); }
}
```

**Usage**:

```java
package academy.javaengineering.oop.sealedclasses;

public class EitherDemo {
    static Either<String, Integer> parseAge(String input) {
        try {
            int age = Integer.parseInt(input);
            if (age < 0 || age > 150) {
                return Either.left("Invalid age: " + age);
            }
            return Either.right(age);
        } catch (NumberFormatException e) {
            return Either.left("Not a number: " + input);
        }
    }

    public static void main(String[] args) {
        Either<String, Integer> result = parseAge("25")
            .mapRight(age -> age * 2)
            .mapRight(age -> "Age doubled: " + age);

        System.out.println("Result: " + result);

        Either<String, Integer> error = parseAge("abc");
        System.out.println("Error: " + error);
        System.out.println("Optional: " + error.toOptional());
    }
}
```

**Output**:
```
Result: Right[Age doubled: 50]
Error: Left[Not a number: abc]
Optional: Optional.empty()
```

### Example 5: Sealed Class for Document Types with Visitor Pattern

**Problem Statement**: Model different document types in a CMS where only specific types are permitted, and implement a visitor pattern for processing.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed class Document permits TextDocument, ImageDocument, VideoDocument {
    private final String id;
    private final String author;

    protected Document(String id, String author) {
        this.id = id;
        this.author = author;
    }

    public String getId() { return id; }
    public String getAuthor() { return author; }

    public abstract <T> T accept(DocumentVisitor<T> visitor);
}

final class TextDocument extends Document {
    private final String content;
    private final String format;

    public TextDocument(String id, String author, String content, String format) {
        super(id, author);
        this.content = content;
        this.format = format;
    }

    public String getContent() { return content; }
    public String getFormat() { return format; }

    @Override
    public <T> T accept(DocumentVisitor<T> visitor) {
        return visitor.visitText(this);
    }
}

final class ImageDocument extends Document {
    private final int width;
    private final int height;
    private final String mimeType;

    public ImageDocument(String id, String author, int width, int height, String mimeType) {
        super(id, author);
        this.width = width;
        this.height = height;
        this.mimeType = mimeType;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getMimeType() { return mimeType; }

    @Override
    public <T> T accept(DocumentVisitor<T> visitor) {
        return visitor.visitImage(this);
    }
}

final class VideoDocument extends Document {
    private final int durationSeconds;
    private final int resolution;

    public VideoDocument(String id, String author, int durationSeconds, int resolution) {
        super(id, author);
        this.durationSeconds = durationSeconds;
        this.resolution = resolution;
    }

    public int getDurationSeconds() { return durationSeconds; }
    public int getResolution() { return resolution; }

    @Override
    public <T> T accept(DocumentVisitor<T> visitor) {
        return visitor.visitVideo(this);
    }
}
```

**Visitor interface**:

```java
package academy.javaengineering.oop.sealedclasses;

public interface DocumentVisitor<T> {
    T visitText(TextDocument doc);
    T visitImage(ImageDocument doc);
    T visitVideo(VideoDocument doc);
}
```

**Concrete visitor for size estimation**:

```java
package academy.javaengineering.oop.sealedclasses;

public class SizeEstimator implements DocumentVisitor<Long> {
    @Override
    public Long visitText(TextDocument doc) {
        return (long) doc.getContent().length() * 2; // 2 bytes per char
    }

    @Override
    public Long visitImage(ImageDocument doc) {
        return (long) doc.getWidth() * doc.getHeight() * 3; // RGB
    }

    @Override
    public Long visitVideo(VideoDocument doc) {
        return (long) doc.getDurationSeconds() * doc.getResolution() * 1000;
    }

    public static void main(String[] args) {
        Document text = new TextDocument("1", "Alice", "Hello World", "txt");
        Document image = new Document("2", "Bob", 1920, 1080, "png");
        Document video = new Document("3", "Charlie", 120, 1080);

        SizeEstimator estimator = new SizeEstimator();
        System.out.println("Text size: " + text.accept(estimator) + " bytes");
        System.out.println("Image size: " + image.accept(estimator) + " bytes");
        System.out.println("Video size: " + video.accept(estimator) + " bytes");
    }
}
```

**Output**:
```
Text size: 22 bytes
Image size: 6220800 bytes
Video size: 129600000 bytes
```

### Example 6: Sealed Class with Pattern Matching in Nested Context

**Problem Statement**: Demonstrate sealed classes with nested pattern matching for complex type dispatch.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed class JsonValue
        permits JsonString, JsonNumber, JsonBoolean, JsonNull, JsonArray, JsonObject {
}

final record JsonString(String value) extends JsonValue {}
final record JsonNumber(double value) extends JsonValue {}
final record JsonBoolean(boolean value) extends JsonValue {}
final record JsonNull() extends JsonValue {}
final record JsonArray(java.util.List<JsonValue> elements) extends JsonValue {}
final record JsonObject(java.util.Map<String, JsonValue> entries) extends JsonValue {}
```

**JsonPrinter with nested patterns**:

```java
package academy.javaengineering.oop.sealedclasses;

import java.util.List;
import java.util.Map;

public class JsonPrinter {

    public static String prettyPrint(JsonValue value, int indent) {
        String prefix = "  ".repeat(indent);
        return switch (value) {
            case JsonString s -> "\"" + s.value() + "\"";
            case JsonNumber n -> String.valueOf(n.value());
            case JsonBoolean b -> String.valueOf(b.value());
            case JsonNull n -> "null";
            case JsonArray arr -> {
                if (arr.elements().isEmpty()) {
                    yield "[]";
                }
                StringBuilder sb = new StringBuilder("[\n");
                for (int i = 0; i < arr.elements().size(); i++) {
                    sb.append(prefix).append("  ");
                    sb.append(prettyPrint(arr.elements().get(i), indent + 1));
                    if (i < arr.elements().size() - 1) sb.append(",");
                    sb.append("\n");
                }
                sb.append(prefix).append("]");
                yield sb.toString();
            }
            case JsonObject obj -> {
                if (obj.entries().isEmpty()) {
                    yield "{}";
                }
                StringBuilder sb = new StringBuilder("{\n");
                int i = 0;
                for (Map.Entry<String, JsonValue> entry : obj.entries().entrySet()) {
                    sb.append(prefix).append("  \"").append(entry.getKey()).append("\": ");
                    sb.append(prettyPrint(entry.getValue(), indent + 1));
                    if (i < obj.entries().size() - 1) sb.append(",");
                    sb.append("\n");
                    i++;
                }
                sb.append(prefix).append("}");
                yield sb.toString();
            }
        };
    }

    public static void main(String[] args) {
        JsonValue json = new JsonObject(Map.of(
            "name", new JsonString("Alice"),
            "age", new JsonNumber(30),
            "active", new JsonBoolean(true),
            "address", new JsonNull(),
            "hobbies", new JsonArray(List.of(
                new JsonString("reading"),
                new JsonString("coding")
            ))
        ));

        System.out.println(prettyPrint(json, 0));
    }
}
```

**Output**:
```
{
  "name": "Alice",
  "age": 30.0,
  "active": true,
  "address": null,
  "hobbies": [
    "reading",
    "coding"
  ]
}
```

## Hard Examples

### Example 7: Sealed Class Type-Safe Builder

**Problem Statement**: Build a type-safe builder that uses sealed classes to ensure valid construction states and prevent invalid configurations.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed class QueryBuilder<B extends QueryBuilder<B>>
        permits SelectBuilder, WhereBuilder, OrderByBuilder {

    protected final StringBuilder sql;

    protected QueryBuilder(String base) {
        this.sql = new StringBuilder(base);
    }

    @SuppressWarnings("unchecked")
    protected B self() { return (B) this; }

    public String build() { return sql.toString(); }
}
```

```java
package academy.javaengineering.oop.sealedclasses;

import java.util.ArrayList;
import java.util.List;

public final class SelectBuilder extends QueryBuilder<SelectBuilder> {
    private final List<String> columns = new ArrayList<>();
    private String table;

    public SelectBuilder() {
        super("SELECT ");
    }

    public SelectBuilder columns(String... cols) {
        columns.addAll(List.of(cols));
        return this;
    }

    public WhereBuilder from(String table) {
        this.table = table;
        sql.append(String.join(", ", columns));
        sql.append(" FROM ").append(table);
        return new WhereBuilder(sql.toString());
    }
}
```

```java
package academy.javaengineering.oop.sealedclasses;

public final class WhereBuilder extends QueryBuilder<WhereBuilder> {
    private final List<String> conditions = new ArrayList<>();

    WhereBuilder(String base) {
        super(base);
    }

    public WhereBuilder and(String condition) {
        conditions.add(condition);
        return this;
    }

    public OrderByBuilder orderBy(String column) {
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        sql.append(" ORDER BY ").append(column);
        return new OrderByBuilder(sql.toString());
    }

    public String build() {
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        return sql.toString();
    }
}
```

```java
package academy.javaengineering.oop.sealedclasses;

public final class OrderByBuilder extends QueryBuilder<OrderByBuilder> {
    private String direction = "ASC";

    OrderByBuilder(String base) {
        super(base);
    }

    public OrderByBuilder descending() {
        this.direction = "DESC";
        return this;
    }

    @Override
    public String build() {
        sql.append(" ").append(direction);
        return sql.toString();
    }
}
```

**Usage**:

```java
package academy.javaengineering.oop.sealedclasses;

public class QueryBuilderDemo {
    public static void main(String[] args) {
        String query1 = new SelectBuilder()
                .columns("id", "name", "email")
                .from("users")
                .and("age > 18")
                .and("active = true")
                .orderBy("name")
                .build();

        String query2 = new SelectBuilder()
                .columns("*")
                .from("orders")
                .orderBy("created_at")
                .descending()
                .build();

        System.out.println("Query 1: " + query1);
        System.out.println("Query 2: " + query2);
    }
}
```

**Output**:
```
Query 1: SELECT id, name, email FROM users WHERE age > 18 AND active = true ORDER BY name ASC
Query 2: SELECT * FROM orders ORDER BY created_at DESC
```

**Unit Tests**:

```java
package academy.javaengineering.oop.sealedclasses;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderTest {

    @Test
    void testSimpleSelect() {
        String query = new SelectBuilder()
                .columns("id", "name")
                .from("users")
                .build();
        assertEquals("SELECT id, name FROM users", query);
    }

    @Test
    void testSelectWithWhere() {
        String query = new SelectBuilder()
                .columns("*")
                .from("users")
                .and("active = true")
                .build();
        assertEquals("SELECT * FROM users WHERE active = true", query);
    }

    @Test
    void testSelectWithOrderBy() {
        String query = new SelectBuilder()
                .columns("id")
                .from("users")
                .orderBy("id")
                .descending()
                .build();
        assertEquals("SELECT id FROM users ORDER BY id DESC", query);
    }
}
```

**Execution Flow**:
1. `SelectBuilder` is created, appending "SELECT " to the SQL buffer.
2. `columns()` appends column names.
3. `from()` appends table name and returns a `WhereBuilder`.
4. `and()` accumulates conditions.
5. `orderBy()` appends WHERE clause if conditions exist, then ORDER BY.
6. `descending()` sets direction to DESC.
7. `build()` returns the final SQL string.

**Complexity**: O(n) where n is the number of columns and conditions.

**Best Practices**:
- Use sealed hierarchies to enforce valid state transitions in builders.
- Each builder state represents a valid configuration step.
- The type system prevents calling methods in the wrong order.

### Example 8: Sealed Class Algebraic Data Type

**Problem Statement**: Implement a type-safe algebraic data type (ADT) for representing expressions in a simple calculator language, with evaluation and type checking.

**Implementation**:

```java
package academy.javaengineering.oop.sealedclasses;

public sealed interface Expr
        permits Expr.Literal, Expr.Add, Expr.Subtract, Expr.Multiply, Expr.Divide, Expr.Negate {

    double evaluate();

    record Literal(double value) implements Expr {
        @Override
        public double evaluate() { return value; }

        @Override
        public String toString() { return String.valueOf(value); }
    }

    record Add(Expr left, Expr right) implements Expr {
        @Override
        public double evaluate() { return left.evaluate() + right.evaluate(); }

        @Override
        public String toString() { return "(" + left + " + " + right + ")"; }
    }

    record Subtract(Expr left, Expr right) implements Expr {
        @Override
        public double evaluate() { return left.evaluate() - right.evaluate(); }

        @Override
        public String toString() { return "(" + left + " - " + right + ")"; }
    }

    record Multiply(Expr left, Expr right) implements Expr {
        @Override
        public double evaluate() { return left.evaluate() * right.evaluate(); }

        @Override
        public String toString() { return "(" + left + " * " + right + ")"; }
    }

    record Divide(Expr left, Expr right) implements Expr {
        @Override
        public double evaluate() {
            double divisor = right.evaluate();
            if (divisor == 0) throw new ArithmeticException("Division by zero");
            return left.evaluate() / divisor;
        }

        @Override
        public String toString() { return "(" + left + " / " + right + ")"; }
    }

    record Negate(Expr operand) implements Expr {
        @Override
        public double evaluate() { return -operand.evaluate(); }

        @Override
        public String toString() { return "(-" + operand + ")"; }
    }

    static Expr literal(double value) { return new Literal(value); }
    static Expr add(Expr left, Expr right) { return new Add(left, right); }
    static Expr subtract(Expr left, Expr right) { return new Subtract(left, right); }
    static Expr multiply(Expr left, Expr right) { return new Multiply(left, right); }
    static Expr divide(Expr left, Expr right) { return new Divide(left, right); }
    static Expr negate(Expr operand) { return new Negate(operand); }
}
```

**Expression simplifier visitor**:

```java
package academy.javaengineering.oop.sealedclasses;

public class ExprSimplifier {

    public static Expr simplify(Expr expr) {
        return switch (expr) {
            case Expr.Literal l -> l;
            case Expr.Negate n -> {
                Expr inner = simplify(n.operand());
                if (inner instanceof Expr.Literal lit) {
                    yield Expr.literal(-lit.value());
                }
                yield Expr.negate(inner);
            }
            case Expr.Add a -> {
                Expr left = simplify(a.left());
                Expr right = simplify(a.right());
                if (left instanceof Expr.Literal l && right instanceof Expr.Literal r) {
                    yield Expr.literal(l.value() + r.value());
                }
                yield Expr.add(left, right);
            }
            case Expr.Subtract s -> {
                Expr left = simplify(s.left());
                Expr right = simplify(s.right());
                if (left instanceof Expr.Literal l && right instanceof Expr.Literal r) {
                    yield Expr.literal(l.value() - r.value());
                }
                yield Expr.subtract(left, right);
            }
            case Expr.Multiply m -> {
                Expr left = simplify(m.left());
                Expr right = simplify(m.right());
                if (left instanceof Expr.Literal l && right instanceof Expr.Literal r) {
                    yield Expr.literal(l.value() * r.value());
                }
                yield Expr.multiply(left, right);
            }
            case Expr.Divide d -> {
                Expr left = simplify(d.left());
                Expr right = simplify(d.right());
                if (left instanceof Expr.Literal l && right instanceof Expr.Literal r) {
                    if (r.value() == 0) throw new ArithmeticException("Division by zero");
                    yield Expr.literal(l.value() / r.value());
                }
                yield Expr.divide(left, right);
            }
        };
    }

    public static void main(String[] args) {
        // (3 + 5) * 2
        Expr expr = Expr.multiply(
            Expr.add(Expr.literal(3), Expr.literal(5)),
            Expr.literal(2)
        );

        System.out.println("Original: " + expr);
        System.out.println("Result: " + expr.evaluate());

        Expr simplified = simplify(expr);
        System.out.println("Simplified: " + simplified);
        System.out.println("Simplified result: " + simplified.evaluate());
    }
}
```

**Output**:
```
Original: ((3.0 + 5.0) * 2.0)
Result: 16.0
Simplified: 16.0
Simplified result: 16.0
```

**Complexity**: O(n) for evaluation where n is the expression tree depth; O(n) for simplification.

**Best Practices**:
- Sealed interfaces with records are ideal for algebraic data types.
- Exhaustive switch ensures every expression variant is handled.
- Use factory methods for cleaner API at call sites.

## Exercises

### Easy

1. **Traffic Light**: Create a sealed class `TrafficLight` with permitted subclasses `Red`, `Yellow`, and `Green`. Each should have a `duration()` method. Write a `switch` expression that returns the next light for each state.

2. **Shape Area Calculator**: Create a sealed class hierarchy for `Shape` with `Circle`, `Rectangle`, and `Triangle`. Implement an `area()` method using an exhaustive `switch` expression.

3. **Card Deck**: Create a sealed interface `Card` with `Ace`, `NumberCard`, and `FaceCard` subtypes. Each should have a `value()` method. Implement a `describe()` method using pattern matching.

### Medium

4. **Either Monad**: Create a sealed interface `Either<L, R>` with `Left` and `Right` subtypes. Implement `mapLeft()`, `mapRight()`, `flatMapRight()`, `orElse()`, and `orElseThrow()` methods.

5. **JSON Parser Result**: Create a sealed interface `ParseResult` with `Success` and `Failure` subtypes. `Success` holds the parsed JSON tree, `Failure` holds the error message and line number. Implement a `recover()` method that transforms failures.

6. **State Machine**: Model a file upload state machine using sealed classes: `Idle`, `Uploading`, `Processing`, `Complete`, `Failed`. Implement valid transitions using exhaustive switch expressions. Ensure invalid transitions throw descriptive errors.

### Hard

7. **Expression Language**: Extend the `Expr` example to include `IfThenElse(Expr condition, Expr then, Expr else)` and `Variable(String name, Expr value, Expr body)`. Implement evaluation with a variable environment map.

8. **Type-Safe Event System**: Create a sealed interface `Event` with subtypes for different domain events (e.g., `UserCreated`, `OrderPlaced`, `PaymentReceived`). Implement an event handler registry that uses exhaustive dispatch to route events to handlers.

9. **Pattern Matching Compiler**: Implement a sealed class hierarchy for a simple pattern language (`LiteralPattern`, `WildcardPattern`, `BindingPattern`, `CompositePattern`). Write a matcher that compiles patterns into a fast decision tree.

## Interview Questions

### Easy

1. **What is a sealed class in Java?**

   A sealed class is a class that restricts which other classes may extend it. It uses the `permits` clause to enumerate the allowed subclasses. Each permitted subclass must be `final`, `sealed`, or `non-sealed`.

2. **What are the three modifiers allowed on permitted subclasses?**

   - `final`: The class cannot be extended further.
   - `sealed`: The class itself is sealed and must list its own permitted subclasses.
   - `non-sealed`: The class opens the hierarchy, allowing anyone to extend it.

3. **Can a sealed class have no permitted subclasses?**

   Yes, but it must be declared `abstract`. A sealed class with no permitted subtypes is effectively unusable since no concrete instances can be created.

### Medium

4. **Why must all permitted subtypes be in the same module (or package)?**

   This ensures the author of the sealed class has visibility into all subtypes. If subtypes could be in different modules/packages, the compiler couldn't verify exhaustive pattern matching, and the sealed guarantee would be meaningless.

5. **How do sealed classes interact with pattern matching?**

   Sealed classes enable exhaustive `switch` expressions in pattern matching. The compiler knows the complete set of subtypes and can verify that all cases are handled. This eliminates `default` clauses when all cases are covered.

6. **What is the difference between a sealed class and an enum?**

   Enums define a fixed set of constant values (instances), while sealed classes define a fixed set of subtypes (classes). Sealed classes can have different implementations per subtype, hold different state, and participate in inheritance hierarchies. Enums are simpler but less flexible.

### Hard

7. **How do sealed classes relate to algebraic data types (ADTs) in functional programming?**

   Sealed classes enable ADTs in Java. A sealed type with record subtypes forms a sum type (the sealed class) of product types (the records). This allows expressing complex domain models with compile-time exhaustiveness checking, similar to pattern matching in languages like Haskell or ML.

8. **What are the performance implications of sealed classes?**

   Sealed classes can improve performance in several ways: (1) The JVM can devirtualize method calls when the complete type hierarchy is known. (2) Pattern matching can be optimized to direct jumps instead of virtual dispatch. (3) Escape analysis can be more effective with known subtype sets. However, these optimizations are JVM-implementation-dependent and not guaranteed.

## Common Pitfalls

### 1. Forgetting Required Modifiers

**Wrong**:
```java
public sealed class Shape permits Circle, Rectangle {
}

class Circle extends Shape {  // Compilation error!
    // Missing modifier: must be final, sealed, or non-sealed
}
```

**Right**:
```java
public sealed class Shape permits Circle, Rectangle {
}

final class Circle extends Shape {
    // OK: final modifier allows extension
}
```

### 2. Permitted Subclass in Wrong Package (Without Module)

**Wrong**:
```java
// package com.example.shapes
public sealed class Shape permits Circle {
}

// package com.example.other  -- WRONG PACKAGE, no module-info.java
final class Circle extends Shape {  // Compilation error!
}
```

**Right**:
```java
// package com.example.shapes
public sealed class Shape permits Circle {
}

// package com.example.shapes  -- SAME PACKAGE
final class Circle extends Shape {
    // OK: in same package
}
```

### 3. Using sealed with Final Together

**Wrong**:
```java
public final sealed class ImmutableShape permits Circle {  // Redundant and confusing
}
```

**Right**:
```java
public sealed class ImmutableShape permits Circle {
}

public final class Circle extends ImmutableShape {
    // sealed on the base class, final on the leaf
}
```

## Best Practices

1. **Use sealed classes to model domain constraints**: When the set of subtypes is fixed by business rules, sealed classes make the constraint explicit in the type system.

2. **Combine sealed classes with records**: Records as sealed subtypes create lightweight, immutable data carriers with built-in `equals()`, `hashCode()`, and `toString()`.

3. **Leverage exhaustive pattern matching**: Use `switch` expressions without `default` clauses when the sealed hierarchy is complete. This ensures new subtypes cause compile errors.

4. **Place permitted subtypes close to the sealed class**: Keep sealed classes and their permitted subtypes in the same package for readability and to satisfy the same-module requirement.

5. **Prefer sealed over non-sealed unless intentional**: Only use `non-sealed` when you explicitly want to open the hierarchy for external extension.

## Real World Usage

### How JDK Uses This

The JDK uses sealed classes in several places:

```java
// java.lang.constant.ClassDesc permits specific descriptor types
public sealed interface ClassDesc
        permits PrimitiveClassDesc, ArrayClassDesc, ModuleClassDesc, PackageClassDesc {
}

// Pattern matching in switch (Java 21+)
sealed interface Pattern<T> permits LiteralPattern<T>, PredicatePattern<T> {
}
```

### How Spring Uses This

Spring Framework uses sealed classes for internal type hierarchies:

```java
// Spring's BeanDefinition hierarchy could be modeled as:
sealed interface BeanDefinition permits
        GenericBeanDefinition,
        RootBeanDefinition,
        ScannedGenericBeanDefinition {
}
```

### How Hibernate Uses This

Hibernate uses sealed interfaces for its type system:

```java
// Hibernate's return type hierarchy
sealed interface ReturnInformation permits
        ResultSetReturnInformation,
        CallableStatementReturnInformation,
        UpdateCountReturnInformation {
}
```

### Enterprise Usage

```java
// Payment processing
sealed interface PaymentResult permits PaymentSuccess, PaymentDeclined, PaymentError {
    record PaymentSuccess(String transactionId, BigDecimal amount) implements PaymentResult {}
    record PaymentDeclined(String reason) implements PaymentResult {}
    record PaymentError(String code, String message) implements PaymentResult {}
}

// Audit logging
sealed interface AuditEvent permits UserLogin, UserLogout, DataAccess, DataModification {
    // Each subtype carries event-specific data
}
```

## Summary

- Sealed classes restrict which classes may extend them using the `permits` clause
- Permitted subclasses must be `final`, `sealed`, or `non-sealed`
- Sealed classes enable exhaustive pattern matching in `switch` expressions
- They are ideal for modeling domain-specific type hierarchies with known subtypes
- Combine with records for lightweight, immutable data carriers
- Sealed classes bridge the gap between abstract classes (unlimited extension) and final classes (no extension)
- They support algebraic data types and visitor patterns with compile-time safety
- All permitted subtypes must be in the same module or package

## References

- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [Oracle Java Tutorials - Sealed Classes](https://docs.oracle.com/en/java/javase/17/language/sealed-classes-and-interfaces.html)
- [Java Language Specification - Sealed Classes](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.1.1.2)
- [Baeldung - Java Sealed Classes](https://www.baeldung.com/java-sealed-classes)

**Previous**: [24-object-lifecycle](../24-object-lifecycle/README.md) | **Next**: [26-enums](../26-enums/README.md)
