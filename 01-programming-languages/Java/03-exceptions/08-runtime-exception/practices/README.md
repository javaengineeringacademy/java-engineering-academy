# Exercises: RuntimeException in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Null Check

### Problem

Write a method `greet(String name)` that throws `NullPointerException` with message `"Name cannot be null"` if name is null, and prints `"Hello, <name>!"` otherwise.

### Starter Code

```java
public class Exercise1 {
    static void greet(String name) {
        // TODO: Check for null, throw NullPointerException with message
    }

    public static void main(String[] args) {
        greet("Alice");
        greet(null);
    }
}
```

### Expected Output

```
Hello, Alice!
Exception in thread "main" java.lang.NullPointerException: Name cannot be null
```

### Hints

1. Check `if (name == null)` first.
2. Throw `new NullPointerException("Name cannot be null")`.
3. Print the greeting if name is not null.

---

## Exercise 2: Custom Unchecked Exception

### Problem

Create a custom unchecked exception `InvalidAgeException` and a method `validateAge(int age)` that throws it with a descriptive message.

### Starter Code

```java
public class Exercise2 {
    // TODO: Create InvalidAgeException extending RuntimeException

    static void validateAge(int age) {
        // TODO: Validate age is between 0 and 150
    }

    public static void main(String[] args) {
        validateAge(25);
        validateAge(-5);
    }
}
```

### Expected Output

```
Age 25 is valid
Exception in thread "main" InvalidAgeException: Invalid age: -5
```

### Hints

1. Extend `RuntimeException`.
2. Include a message constructor.
3. Check `age < 0 || age > 150` and throw with the actual value.

---

## Exercise 3: Try-Catch Recovery

### Problem

Write a method `safeParse(String input)` that returns the parsed integer or -1 if parsing fails.

### Starter Code

```java
public class Exercise3 {
    static int safeParse(String input) {
        // TODO: Parse input, catch NumberFormatException, return -1
    }

    public static void main(String[] args) {
        System.out.println("42: " + safeParse("42"));
        System.out.println("abc: " + safeParse("abc"));
    }
}
```

### Expected Output

```
42: 42
abc: -1
```

### Hints

1. Use `Integer.parseInt(input)`.
2. Catch `NumberFormatException`.
3. Return -1 in the catch block.
