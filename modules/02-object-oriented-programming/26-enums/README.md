# Enums

## Introduction

An enum (enumeration) in Java is a special data type that represents a fixed set of constants. Unlike regular classes, enums are implicitly `java.lang.Enum` subclasses and provide type-safe alternatives to integer-based or string-based constants. Enums are first-class citizens in Java — they can have fields, constructors, methods, and implement interfaces. Introduced in Java 5, enums eliminate the errors that arise from using raw numeric or string values and make code self-documenting. Every enum constant is an implicitly `public static final` instance of the enum type, and because enums are backed by the JVM as full classes, they are thread-safe, serializable, and can participate in sophisticated design patterns like Singleton and Strategy.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Define and use Java enums with fields, constructors, and methods
- [ ] Compare enums using `==` and understand why `equals()` should be avoided
- [ ] Leverage built-in enum methods like `values()`, `valueOf()`, and `ordinal()`
- [ ] Apply enums in `switch` statements and implement the Strategy pattern using enum constants

## Prerequisites

- [02-classes](../02-classes/) — Enums are implicitly classes
- [05-methods](../05-methods/) — Enums can define instance and static methods
- [08-encapsulation](../08-encapsulation/) — Enums encapsulate constant-related data
- [12-interfaces](../12-interfaces/) — Enums can implement interfaces

## Why This Concept Exists

### The Problem

Before enums, Java developers used `int` constants or `String` constants to represent fixed sets of values:

```java
public class TrafficLight {
    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final int GREEN = 2;
}
```

This approach is fragile — the compiler cannot prevent passing invalid integers, there is no type safety, no namespace, and no easy way to iterate over all valid values. A method accepting `int color` will happily accept `999` with no compile-time error.

### The Solution

Enums solve all of these problems by creating a named type with a fixed set of instances. The compiler enforces that only valid enum constants can be used, provides iteration via `values()`, and allows attaching data and behavior to each constant.

### Real-World Analogy

Think of an enum like a deck of playing cards. A deck always has exactly 52 cards across 4 suits — no more, no less. You cannot create a fifth suit or invent a card that doesn't exist. Similarly, an enum restricts the set of possible values to a known, finite collection.

## Internal Working

### JVM Perspective

When the compiler encounters an enum declaration, it generates a class that extends `java.lang.Enum<E>`. The bytecode for:

```java
enum Color { RED, GREEN, BLUE }
```

roughly corresponds to:

```java
public final class Color extends Enum<Color> {
    public static final Color RED = new Color("RED", 0);
    public static final Color GREEN = new Color("GREEN", 1);
    public static final Color BLUE = new Color("BLUE", 2);

    private Color(String name, int ordinal) {
        super(name, ordinal);
    }

    public static Color[] values() {
        return (Color[]) $VALUES.clone();
    }

    public static Color valueOf(String name) {
        return (Color) Enum.valueOf(Color.class, name);
    }

    private static final Color[] $VALUES = { RED, GREEN, BLUE };
}
```

### Memory Representation

Each enum constant is a singleton object allocated on the heap. The `$VALUES` array holds all constants in declaration order. The `name()` method returns the constant's declared name, and `ordinal()` returns its position (0-indexed). Because enum instances are cached by the JVM, `==` comparison is reliable and preferred over `equals()`.

## Syntax

```java
// Basic enum declaration
enum Planet {
    MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE
}

// Enum with fields, constructor, and methods
enum PlanetWithMass {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6);

    private final double mass;   // in kilograms
    private final double radius; // in meters

    PlanetWithMass(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    private static final double G = 6.67300E-11;

    double surfaceGravity() {
        return G * mass / (radius * radius);
    }

    double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
}

// Enum implementing an interface
enum Operation implements DoubleBinaryOperator {
    ADD {
        @Override
        public double applyAsDouble(double left, double right) {
            return left + right;
        }
    },
    SUBTRACT {
        @Override
        public double applyAsDouble(double left, double right) {
            return left - right;
        }
    };
}

// Using enum in switch
switch (color) {
    case RED    -> System.out.println("Stop");
    case YELLOW -> System.out.println("Caution");
    case GREEN  -> System.out.println("Go");
}
```

## Easy Examples

### Example 1: Basic Enum — Day of the Week

**Problem Statement**: Represent the seven days of the week and print each day's index and name.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

public class DayOfWeek {
    public static void main(String[] args) {
        Day today = Day.WEDNESDAY;
        System.out.println("Today is " + today);
        System.out.println("Ordinal: " + today.ordinal());
        System.out.println("Name: " + today.name());

        System.out.println("\nAll days:");
        for (Day day : Day.values()) {
            System.out.println(day.ordinal() + " -> " + day.name());
        }
    }
}
```

**Expected Output**:
```
Today is WEDNESDAY
Ordinal: 2
Name: WEDNESDAY

All days:
0 -> MONDAY
1 -> TUESDAY
2 -> WEDNESDAY
3 -> THURSDAY
4 -> FRIDAY
5 -> SATURDAY
6 -> SUNDAY
```

**Best Practices**:
- Use `==` to compare enum constants, not `equals()`
- Prefer `name()` for display and `valueOf()` for parsing
- Use `values()` to iterate over all constants

### Example 2: Enum with Fields and Constructor

**Problem Statement**: Create a `Season` enum that stores average temperatures and can determine if a given temperature belongs to that season.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

enum Season {
    SPRING("Spring", 15.0, 20.0),
    SUMMER("Summer", 25.0, 35.0),
    AUTUMN("Autumn", 10.0, 18.0),
    WINTER("Winter", -5.0, 5.0);

    private final String displayName;
    private final double minTemp;
    private final double maxTemp;

    Season(String displayName, double minTemp, double maxTemp) {
        this.displayName = displayName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getDisplayName() { return displayName; }
    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }

    public boolean containsTemp(double temp) {
        return temp >= minTemp && temp <= maxTemp;
    }
}

public class SeasonDemo {
    public static void main(String[] args) {
        double temp = 28.0;
        for (Season s : Season.values()) {
            if (s.containsTemp(temp)) {
                System.out.printf("%.1f°C belongs to %s (range: %.1f–%.1f)%n",
                        temp, s.getDisplayName(), s.getMinTemp(), s.getMaxTemp());
            }
        }
    }
}
```

**Expected Output**:
```
28.0°C belongs to Summer (range: 25.0–35.0)
```

**Best Practices**:
- Use all-caps constant names with underscores for readability
- Make enum fields `private final` to ensure immutability
- Provide getter methods instead of exposing fields directly

### Example 3: Enum in a Switch Statement

**Problem Statement**: Use a `TrafficLight` enum in a switch to determine the action for each signal color.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

enum TrafficLight {
    RED, YELLOW, GREEN
}

public class TrafficLightDemo {
    public static void main(String[] args) {
        TrafficLight signal = TrafficLight.YELLOW;
        String action = getAction(signal);
        System.out.println(signal + " -> " + action);
    }

    static String getAction(TrafficLight light) {
        return switch (light) {
            case RED -> "Stop the car";
            case YELLOW -> "Slow down";
            case GREEN -> "Proceed";
        };
    }
}
```

**Expected Output**:
```
YELLOW -> Slow down
```

**Best Practices**:
- Always handle all enum constants in a switch to avoid missing cases
- Use the enhanced switch expression (Java 14+) for cleaner code
- Use `default` only when the enum might be extended with new constants at runtime

## Medium Examples

### Example 1: Enum with Abstract Method — Strategy Pattern

**Problem Statement**: Implement a calculator where each operation (ADD, SUBTRACT, MULTIPLY, DIVIDE) is an enum constant with its own `apply` method.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

enum CalculatorOperation {
    ADD {
        @Override
        public double apply(double a, double b) {
            return a + b;
        }
    },
    SUBTRACT {
        @Override
        public double apply(double a, double b) {
            return a - b;
        }
    },
    MULTIPLY {
        @Override
        public double apply(double a, double b) {
            return a * b;
        }
    },
    DIVIDE {
        @Override
        public double apply(double a, double b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
    };

    public abstract double apply(double a, double b);
}

public class CalculatorDemo {
    public static void main(String[] args) {
        double x = 10, y = 3;
        for (CalculatorOperation op : CalculatorOperation.values()) {
            System.out.printf("%.1f %s %.1f = %.2f%n",
                    x, op.name(), y, op.apply(x, y));
        }
    }
}
```

**Expected Output**:
```
10.0 ADD 3.0 = 13.00
10.0 SUBTRACT 3.0 = 7.00
10.0 MULTIPLY 3.0 = 30.00
10.0 DIVIDE 3.0 = 3.33
```

**Code Walkthrough**: Each enum constant overrides the abstract `apply` method. This is the Strategy pattern implemented cleanly with enums — each constant IS a strategy. The loop demonstrates polymorphic dispatch on enum constants.

**Alternative Solution**: You could use a `Map<CalculatorOperation, BinaryOperator<Double>>` with lambda expressions, but the enum approach is more self-contained and leverages the type system.

### Example 2: Enum Implementing an Interface

**Problem Statement**: Create a `FileStatus` enum that implements a `Describable` interface so each status provides its own description.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

interface Describable {
    String describe();
}

enum FileStatus implements Describable {
    CREATED("File has been created and is ready for writing") {
        @Override
        public boolean canWrite() { return true; }
    },
    OPEN("File is currently open for reading or writing") {
        @Override
        public boolean canWrite() { return true; }
    },
    CLOSED("File has been closed and cannot be accessed") {
        @Override
        public boolean canWrite() { return false; }
    },
    CORRUPTED("File is damaged and may contain invalid data") {
        @Override
        public boolean canWrite() { return false; }
    };

    private final String description;

    FileStatus(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return description;
    }

    public abstract boolean canWrite();
}

public class FileStatusDemo {
    public static void main(String[] args) {
        for (FileStatus status : FileStatus.values()) {
            System.out.printf("%-12s | Write: %-5s | %s%n",
                    status.name(), status.canWrite(), status.describe());
        }
    }
}
```

**Expected Output**:
```
CREATED      | Write: true  | File has been created and is ready for writing
OPEN         | Write: true  | File is currently open for reading or writing
CLOSED       | Write: false | File has been closed and cannot be accessed
CORRUPTED    | Write: false | File is damaged and may contain invalid data
```

**Alternative Solution**: Using a sealed interface with records (Java 17+) could provide similar exhaustiveness checks, but enums remain the idiomatic choice for fixed constants with behavior.

### Example 3: EnumSet and EnumMap

**Problem Statement**: Use `EnumSet` to efficiently track which permissions a user has, and `EnumMap` to associate each permission with a human-readable description.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

enum Permission {
    READ, WRITE, EXECUTE, DELETE, ADMIN
}

public class PermissionDemo {
    public static void main(String[] args) {
        EnumMap<Permission, String> descriptions = new EnumMap<>(Permission.class);
        descriptions.put(Permission.READ, "View files");
        descriptions.put(Permission.WRITE, "Modify files");
        descriptions.put(Permission.EXECUTE, "Run programs");
        descriptions.put(Permission.DELETE, "Remove files");
        descriptions.put(Permission.ADMIN, "Full system access");

        Set<Permission> userPerms = EnumSet.of(Permission.READ, Permission.WRITE);

        System.out.println("User permissions:");
        for (Permission p : userPerms) {
            System.out.printf("  %-10s -> %s%n", p, descriptions.get(p));
        }

        Set<Permission> adminPerms = EnumSet.allOf(Permission.class);
        System.out.println("\nAdmin has all permissions: " + adminPerms);
        System.out.println("Admin can write? " + adminPerms.contains(Permission.WRITE));
    }
}
```

**Expected Output**:
```
User permissions:
  READ       -> View files
  WRITE      -> Modify files

Admin has all permissions: [READ, WRITE, EXECUTE, DELETE, ADMIN]
Admin can write? true
```

**Code Walkthrough**: `EnumSet` is backed by a bit vector, making `contains()` and `addAll()` operations O(1). `EnumMap` uses an array indexed by ordinal, providing faster lookups than `HashMap`. Both are optimized specifically for enum keys.

## Hard Examples

### Example 1: Enum-Based State Machine

**Problem Statement**: Implement a vending machine state machine using an enum where each state defines its valid transitions and behavior.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

import java.util.EnumMap;
import java.util.Map;

enum VendingState {
    IDLE {
        @Override
        public VendingState next(VendingEvent event) {
            return switch (event) {
                case INSERT_COIN -> HAS_MONEY;
                default -> this;
            };
        }
        @Override
        public String action() { return "Insert coin to start"; }
    },
    HAS_MONEY {
        @Override
        public VendingState next(VendingEvent event) {
            return switch (event) {
                case SELECT_ITEM -> ITEM_SELECTED;
                case RETURN_COIN -> IDLE;
                default -> this;
            };
        }
        @Override
        public String action() { return "Select an item"; }
    },
    ITEM_SELECTED {
        @Override
        public VendingState next(VendingEvent event) {
            return switch (event) {
                case DISPENSE -> DISPENSING;
                case RETURN_COIN -> IDLE;
                default -> this;
            };
        }
        @Override
        public String action() { return "Confirm purchase or return coin"; }
    },
    DISPENSING {
        @Override
        public VendingState next(VendingEvent event) {
            return switch (event) {
                case DISPENSE_COMPLETE -> IDLE;
                default -> this;
            };
        }
        @Override
        public String action() { return "Dispensing item..."; }
    },
    OUT_OF_STOCK {
        @Override
        public VendingState next(VendingEvent event) {
            return switch (event) {
                case RESTOCK -> IDLE;
                default -> this;
            };
        }
        @Override
        public String action() { return "Machine is out of stock"; }
    };

    public abstract VendingState next(VendingEvent event);
    public abstract String action();
}

enum VendingEvent {
    INSERT_COIN, SELECT_ITEM, RETURN_COIN, DISPENSE, DISPENSE_COMPLETE, RESTOCK
}

class VendingMachine {
    private VendingState currentState = VendingState.IDLE;

    public void handleEvent(VendingEvent event) {
        VendingState next = currentState.next(event);
        System.out.printf("  Event: %-16s | %s -> %s | %s%n",
                event, currentState, next, next.action());
        currentState = next;
    }

    public VendingState getState() { return currentState; }
}

public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        System.out.println("=== Normal Flow ===");
        vm.handleEvent(VendingEvent.INSERT_COIN);
        vm.handleEvent(VendingEvent.SELECT_ITEM);
        vm.handleEvent(VendingEvent.DISPENSE);
        vm.handleEvent(VendingEvent.DISPENSE_COMPLETE);

        System.out.println("\n=== Return Coin Flow ===");
        vm.handleEvent(VendingEvent.INSERT_COIN);
        vm.handleEvent(VendingEvent.RETURN_COIN);
    }
}
```

**Unit Tests**:

```java
package academy.javaengineering.oop.enums;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VendingMachineTest {
    @Test
    void testIdleToHasMoney() {
        VendingMachine vm = new VendingMachine();
        assertEquals(VendingState.IDLE, vm.getState());
        vm.handleEvent(VendingEvent.INSERT_COIN);
        assertEquals(VendingState.HAS_MONEY, vm.getState());
    }

    @Test
    void testReturnCoinFromHasMoney() {
        VendingMachine vm = new VendingMachine();
        vm.handleEvent(VendingEvent.INSERT_COIN);
        vm.handleEvent(VendingEvent.RETURN_COIN);
        assertEquals(VendingState.IDLE, vm.getState());
    }

    @Test
    void testFullDispenseCycle() {
        VendingMachine vm = new VendingMachine();
        vm.handleEvent(VendingEvent.INSERT_COIN);
        vm.handleEvent(VendingEvent.SELECT_ITEM);
        vm.handleEvent(VendingEvent.DISPENSE);
        vm.handleEvent(VendingEvent.DISPENSE_COMPLETE);
        assertEquals(VendingState.IDLE, vm.getState());
    }
}
```

**Execution Flow**: The state machine starts in IDLE. Each event triggers a transition based on the current state's `next()` method. The switch expression in each state's implementation ensures exhaustiveness — the compiler warns if any event is unhandled.

**Complexity**: O(1) per state transition. The enum state pattern avoids complex conditional chains and makes adding new states or events straightforward.

**Best Practices**:
- Encapsulate transition logic within each enum constant
- Use the State pattern with enums to keep transition tables explicit
- Document valid events for each state

### Example 2: Enum with Instance-Specific Behavior Using Abstract Methods

**Problem Statement**: Implement a file parser factory where each enum constant knows how to parse its specific file format and validate the parsed data.

**Implementation**:

```java
package academy.javaengineering.oop.enums;

import java.util.Arrays;
import java.util.List;

enum FileType {
    CSV {
        @Override
        public List<String> parse(String raw) {
            return Arrays.asList(raw.split(","));
        }

        @Override
        public boolean validate(List<String> data) {
            return data.stream().allMatch(s -> !s.trim().isEmpty());
        }

        @Override
        public String format() { return "Comma-Separated Values"; }
    },
    TSV {
        @Override
        public List<String> parse(String raw) {
            return Arrays.asList(raw.split("\t"));
        }

        @Override
        public boolean validate(List<String> data) {
            return data.size() >= 2;
        }

        @Override
        public String format() { return "Tab-Separated Values"; }
    },
    PIPE {
        @Override
        public List<String> parse(String raw) {
            return Arrays.asList(raw.split("\\|"));
        }

        @Override
        public boolean validate(List<String> data) {
            return data.stream().noneMatch(String::isEmpty);
        }

        @Override
        public String format() { return "Pipe-Separated Values"; }
    };

    public abstract List<String> parse(String raw);
    public abstract boolean validate(List<String> data);
    public abstract String format();

    public List<String> safeParse(String raw) {
        List<String> result = parse(raw);
        if (!validate(result)) {
            throw new IllegalArgumentException(
                    "Invalid " + format() + " data: " + raw);
        }
        return result;
    }
}

public class FileParserDemo {
    public static void main(String[] args) {
        String csvLine = "John,Doe,30,Engineer";
        String tsvLine = "John\tDoe\t30\tEngineer";
        String pipeLine = "John|Doe|30|Engineer";

        for (FileType type : FileType.values()) {
            String raw = switch (type) {
                case CSV -> csvLine;
                case TSV -> tsvLine;
                case PIPE -> pipeLine;
            };
            List<String> parsed = type.safeParse(raw);
            System.out.printf("%-6s -> %s%n", type.format(), parsed);
        }
    }
}
```

**Execution Flow**: Each enum constant defines how it splits and validates data. The template method `safeParse()` handles the common pattern of parse-then-validate, while subclasses provide the specific logic.

**Complexity**: O(n) where n is the length of the input string.

**Best Practices**:
- Use template methods in enums for common patterns
- Validate in the template method, not in each constant
- Keep enum constants focused on a single responsibility

## Exercises

### Easy

1. Create a `Color` enum with RED, GREEN, BLUE, and YELLOW. Write a method that takes a `Color` and returns its hex code (e.g., RED -> "#FF0000").

2. Create a `Month` enum with all 12 months. Write a method that returns how many days each month has (assume non-leap year).

3. Write a program that uses `EnumSet` to represent a set of weekdays and prints whether today is a weekend or weekday.

### Medium

4. Create a `Direction` enum (NORTH, SOUTH, EAST, WEST) with a `turnRight()` and `turnLeft()` method that returns the new direction after a 90-degree turn.

5. Implement a `LogLevel` enum (DEBUG, INFO, WARN, ERROR) that stores a numeric severity level and can filter log messages below a threshold.

6. Build a `Currency` enum with exchange rates to USD. Add a `convertTo(Currency target, double amount)` method.

### Hard

7. Implement an enum-based finite state machine for a simple ATM with states: IDLE, CARD_INSERTED, PIN_ENTERED, TRANSACTION_IN_PROGRESS, CARD_EJECTED. Handle events: INSERT_CARD, ENTER_PIN, SELECT_WITHDRAWAL, SELECT_DEPOSIT, EJECT_CARD, CANCEL.

8. Create an enum `CachePolicy` with constants LRU, LFU, FIFO, and RANDOM. Each constant should implement an eviction strategy for a simple in-memory cache.

9. Design an enum `HttpStatus` that wraps HTTP status codes (200, 301, 404, 500, etc.) with fields for code, message, and category (SUCCESS, REDIRECT, CLIENT_ERROR, SERVER_ERROR).

## Interview Questions

### Easy

1. **What is the difference between an enum and a class in Java?**
   An enum is implicitly a subclass of `java.lang.Enum`. Its instances are pre-defined constants. You cannot create new instances at runtime (except via deserialization). Enums cannot extend other classes but can implement interfaces.

2. **Why should you use `==` instead of `equals()` to compare enum constants?**
   Enum constants are singletons guaranteed by the JVM. Using `==` is both faster and null-safe, whereas `equals()` could be overridden or throw a NullPointerException.

3. **What does the `values()` method return?**
   It returns an array of all enum constants in the order they are declared. The array is a clone, so modifying it does not affect the enum.

### Intermediate

4. **Can an enum have abstract methods?**
   Yes. If an enum has an abstract method, each constant must provide an implementation. This is useful for the Strategy pattern where each constant behaves differently.

5. **How does `valueOf()` work and what exception does it throw?**
   `Enum.valueOf(Class, String)` returns the enum constant with the specified name. It throws `IllegalArgumentException` if the name doesn't match any constant.

6. **Can enums implement interfaces?**
   Yes. An enum can implement one or more interfaces. Since enums cannot extend classes, this is the only way to achieve polymorphism with enums.

### Hard

7. **How are enums handled during serialization? Java guarantees a single instance. Explain the mechanism.**
   Java serialization for enums uses `readResolve()`. The `Enum` class defines `readResolve()` which replaces the deserialized object with the existing enum constant by calling `Enum.valueOf()`. This ensures singleton semantics across serialization/deserialization cycles.

8. **What are the memory implications of using enums with many constants?**
   Each enum constant is a full object with fields, a name string, and an ordinal. For thousands of constants, this can consume significant heap space. The `$VALUES` array holds references to all constants, preventing garbage collection. Use `EnumSet`/`EnumMap` for collections to minimize overhead — they use bit vectors instead of `HashSet`/`HashMap` overhead.

## Common Pitfalls

### 1. Using `equals()` Instead of `==`

**Wrong**:
```java
if (color.equals(Color.RED)) {
    System.out.println("It's red");
}
```

**Right**:
```java
if (color == Color.RED) {
    System.out.println("It's red");
}
```

`==` is null-safe and faster. `equals()` could be overridden by a malicious deserialization attack.

### 2. Not Handling All Cases in Switch

**Wrong**:
```java
String getMessage(TrafficLight light) {
    return switch (light) {
        case RED -> "Stop";
        case YELLOW -> "Caution";
        // Missing GREEN — compiler warning or runtime issue
    };
}
```

**Right**:
```java
String getMessage(TrafficLight light) {
    return switch (light) {
        case RED -> "Stop";
        case YELLOW -> "Caution";
        case GREEN -> "Go";
    };
}
```

Always handle all constants. The compiler will warn about missing cases in switch expressions.

### 3. Using Mutable Fields in Enums

**Wrong**:
```java
enum MutableEnum {
    INSTANCE;
    private int counter = 0;

    public void increment() {
        counter++; // Thread-unsafe, breaks singleton semantics
    }
}
```

**Right**:
```java
enum ImmutableEnum {
    INSTANCE;
    private final int value;

    ImmutableEnum(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
```

Enum constants are shared. Mutable state in enums creates race conditions and violates the immutable constant contract.

## Best Practices

1. **Use enums instead of int/string constants** — They provide type safety, namespace isolation, and can hold related data and behavior.
2. **Compare with `==`** — Enum constants are singletons; `==` is null-safe, faster, and conventional.
3. **Keep enums immutable** — Never add mutable instance fields. If state must change, use a separate data structure keyed by the enum.
4. **Use `EnumSet` and `EnumMap`** — These are optimized for enum keys and outperform `HashSet`/`HashMap` in both speed and memory.
5. **Prefer `switch` expressions over if-else chains** — Switch expressions on enums are exhaustive and the compiler enforces completeness.

## Real World Usage

### How Spring Uses Enums

Spring Framework uses enums extensively. `HttpStatus` represents HTTP response codes. `TransactionDefinition.Propagation` defines transaction propagation behaviors. `AnnotationConfigApplicationContext` lifecycle states are managed via enums. Spring Security uses `SecurityFilterChain` with enum-based configuration.

### How JDK Uses Enums

The JDK uses enums in `TimeUnit` (SECONDS, MILLISECONDS, etc.), `Thread.State` (RUNNABLE, BLOCKED, etc.), `Character.UnicodeScript`, `StandardCharsets`, and `JVM` flag constants. `java.lang.Thread` uses enum `State` to track thread lifecycle.

### How Hibernate Uses Enums

Hibernate maps Java enums to database columns using `@Enumerated(EnumType.STRING)` or `@Enumerated(EnumType.ORDINAL)`. `CacheModeType`, `FlushModeType`, and `GenerationType` are all enums that control ORM behavior.

### Enterprise Usage

In enterprise applications, enums power state machines (order status: PENDING, CONFIRMED, SHIPPED, DELIVERED), configuration flags (feature toggles), error categorization, and role-based access control (USER, ADMIN, SUPER_ADMIN).

## References

- [Oracle — Java Enums Tutorial](https://docs.oracle.com/en/java/javase/21/java/javaOO/enum.html)
- [Effective Java, Item 34: Use enums instead of int constants](https://books.google.com/books?id=BIoul6j2KcIC)
- [Baeldung — Java Enums](https://www.baeldung.com/java-enum)
- [Javadoc — java.lang.Enum](https://docs.oracle.com/en/java/javase/21/docs/api/java/lang/Enum.html)

## Summary

- Enums are implicitly subclasses of `java.lang.Enum` and represent fixed sets of constants
- Each constant is a singleton — use `==` for comparison
- Enums can have fields, constructors, abstract methods, and implement interfaces
- `EnumSet` and `EnumMap` provide optimized collections for enum keys
- Enums eliminate the pitfalls of raw int/string constants and are a cornerstone of type-safe Java design

**Next Step**: [27-inner-classes](../27-inner-classes/)
