# Control Flow

Control flow is how your program decides what to do and when. Without it, every program would just run top to bottom, line by line, with no decisions and no repetition.

---

## if-else Statements

The basic building block. If a condition is true, do something. Otherwise, do something else.

```java
int temperature = 32;

if (temperature > 30) {
    System.out.println("It's hot outside");
} else if (temperature > 20) {
    System.out.println("Nice weather");
} else if (temperature > 10) {
    System.out.println("A bit chilly");
} else {
    System.out.println("It's cold");
}
```

### One-Line if

If the body is a single statement, you can skip the braces. But don't.

```java
// Don't do this
if (age >= 18) System.out.println("Adult");

// Do this instead
if (age >= 18) {
    System.out.println("Adult");
}
```

The braced version is safer. If you later add another line, you won't accidentally leave it outside the `if`.

### Nested if

You can put if-statements inside other if-statements. But keep it shallow.

```java
if (age >= 18) {
    if (hasTicket) {
        System.out.println("Welcome!");
    } else {
        System.out.println("You need a ticket");
    }
} else {
    System.out.println("Must be 18 or older");
}
```

When nesting gets deep, consider combining conditions:

```java
// Instead of three levels of nesting:
if (age >= 18 && hasTicket && isNotBanned) {
    System.out.println("Welcome!");
}
```

---

## switch Statement

Use `switch` when you're comparing one value against many fixed options.

```java
String day = "Monday";

switch (day) {
    case "Monday":
        System.out.println("Start of the work week");
        break;
    case "Friday":
        System.out.println("Almost weekend!");
        break;
    case "Saturday":
    case "Sunday":
        System.out.println("Weekend!");
        break;
    default:
        System.out.println("Midweek");
        break;
}
```

### Don't Forget break

Without `break`, Java "falls through" to the next case. Sometimes this is useful, usually it's a bug.

```java
// Intentional fall-through
case "Saturday":
case "Sunday":
    System.out.println("Weekend!");
    break;

// Unintentional fall-through (bug)
case "Monday":
    System.out.println("Start of week");
    // missing break — Tuesday code also runs!
case "Tuesday":
    System.out.println("Second day");
    break;
```

### Switch Expressions (Java 14+)

Modern Java lets you write switch as an expression. Cleaner, no break needed.

```java
String result = switch (day) {
    case "Monday" -> "Start of week";
    case "Friday" -> "Almost weekend!";
    case "Saturday", "Sunday" -> "Weekend!";
    default -> "Midweek";
};
```

This is the preferred way in modern Java.

---

## for Loop

Use `for` when you know how many times to repeat something.

```java
// Basic for loop
for (int i = 0; i < 5; i++) {
    System.out.println("i is " + i);
}
// Prints: 0, 1, 2, 3, 4

// Counting backwards
for (int i = 10; i > 0; i--) {
    System.out.println(i);
}
// Prints: 10, 9, 8, ..., 1

// Step by 2
for (int i = 0; i < 20; i += 2) {
    System.out.println(i);
}
// Prints: 0, 2, 4, ..., 18
```

### Loop Variables

The variable declared in a `for` loop only exists inside the loop.

```java
for (int i = 0; i < 5; i++) {
    // i exists here
}
// System.out.println(i);  // won't compile — i doesn't exist here
```

---

## Enhanced for Loop (for-each)

When you're iterating over an array or collection, the for-each loop is cleaner.

```java
int[] numbers = {10, 20, 30, 40, 50};

// Regular for loop
for (int i = 0; i < numbers.length; i++) {
    System.out.println(numbers[i]);
}

// For-each loop (cleaner)
for (int num : numbers) {
    System.out.println(num);
}
```

Use for-each when you don't need the index. Use regular `for` when you do.

---

## while Loop

Use `while` when you don't know how many times to loop — you just know the condition.

```java
int count = 0;
while (count < 5) {
    System.out.println("Count is " + count);
    count++;
}
```

### Be Careful: Infinite Loops

If the condition never becomes false, the loop runs forever.

```java
// Bug: count never increases
while (count < 5) {
    System.out.println("Count is " + count);
    // forgot to increment count!
}

// Always make sure something inside the loop changes the condition
```

---

## do-while Loop

Like `while`, but the code runs at least once — the condition is checked at the end.

```java
int number;

do {
    System.out.print("Enter a positive number: ");
    number = scanner.nextInt();
} while (number <= 0);
```

The `do-while` is perfect for input validation — you always want to ask at least once.

---

## break and continue

### break — Exit the Loop

```java
for (int i = 0; i < 100; i++) {
    if (i == 5) {
        break;  // exits the loop entirely
    }
    System.out.println(i);
}
// Prints: 0, 1, 2, 3, 4
```

### continue — Skip to Next Iteration

```java
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) {
        continue;  // skips the rest of this iteration
    }
    System.out.println(i);
}
// Prints: 1, 3, 5, 7, 9 (only odd numbers)
```

---

## Real-World Example: Guessing Game

```java
import java.util.Scanner;
import java.util.Random;

public class GuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int target = random.nextInt(100) + 1;  // 1 to 100
        int guess;
        int attempts = 0;

        do {
            System.out.print("Guess a number (1-100): ");
            guess = scanner.nextInt();
            attempts++;

            if (guess < target) {
                System.out.println("Too low!");
            } else if (guess > target) {
                System.out.println("Too high!");
            } else {
                System.out.println("Correct! Took " + attempts + " attempts.");
            }
        } while (guess != target);

        scanner.close();
    }
}
```

This combines `do-while`, `if-else`, and variables — everything from the first two topics.

---

## Common Mistakes

**Off-by-one errors:**
```java
// This prints 0-9, not 0-10
for (int i = 0; i <= 10; i++) { }  // if you want 0-10, use <=
for (int i = 0; i < 10; i++) { }   // if you want 0-9, use <
```

**Infinite loops:**
```java
int i = 0;
while (i < 10) {
    System.out.println(i);
    // forgot i++ → infinite loop
}
```

**Missing break in switch:**
```java
case "A":
    System.out.println("Excellent");
    // missing break → falls through to case "B"
case "B":
    System.out.println("Good");
    break;
```

---

## Practice

1. Print all even numbers from 1 to 50
2. Write a program that finds the largest of three numbers
3. Build a simple calculator that runs until the user types "quit"
4. Print the multiplication table for a given number

---

**Previous:** [02-Operators](../02-operators/)
**Next:** [04-Methods](../04-methods/)
