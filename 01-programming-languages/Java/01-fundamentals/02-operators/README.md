# Operators

Operators are symbols that perform operations on values. You've already seen assignment (`=`) and addition (`+`). This topic covers all the operators you'll use daily.

---

## Arithmetic Operators

The math operators. Straightforward.

```java
int a = 10;
int b = 3;

System.out.println(a + b);   // 13  (addition)
System.out.println(a - b);   // 7   (subtraction)
System.out.println(a * b);   // 30  (multiplication)
System.out.println(a / b);   // 3   (division — notice it truncates)
System.out.println(a % b);   // 1   (modulus — remainder after division)
```

The modulus operator is more useful than it looks. Use it to check if a number is even:

```java
int number = 7;
if (number % 2 == 0) {
    System.out.println("Even");
} else {
    System.out.println("Odd");
}
```

### Watch Out: Integer Division

When you divide two integers, Java throws away the decimal part.

```java
int a = 7;
int b = 2;
System.out.println(a / b);   // 3, not 3.5

// To get the decimal:
double result = (double) a / b;  // 3.5
// OR
double result = 7.0 / 2;        // 3.5
```

---

## Increment and Decrement

Adding or subtracting 1 is so common that Java has shorthand operators.

```java
int count = 5;

count++;    // same as count = count + 1 → now 6
count--;    // same as count = count - 1 → now 5
```

### Prefix vs Postfix

The position of `++` matters when used in an expression:

```java
int a = 5;
int b = a++;   // b is 5, then a becomes 6 (postfix: use first, then increment)

int c = 5;
int d = ++c;   // c becomes 6 first, then d is 6 (prefix: increment first, then use)
```

In practice, most developers just use `count++` on its own line and don't mix it with assignments. The prefix/postfix distinction rarely matters in real code.

---

## Relational Operators

These compare two values and return `true` or `false`.

```java
int x = 10;
int y = 20;

System.out.println(x == y);   // false (equal to)
System.out.println(x != y);   // true  (not equal to)
System.out.println(x > y);    // false (greater than)
System.out.println(x < y);    // true  (less than)
System.out.println(x >= 10);  // true  (greater than or equal to)
System.out.println(x <= 5);   // false (less than or equal to)
```

### Common Mistake: = vs ==

This is the #1 beginner mistake. Single `=` assigns. Double `==` compares.

```java
int a = 5;        // assignment: sets a to 5
if (a == 5) { }   // comparison: checks if a equals 5
```

Java won't let you use `=` inside an `if` statement, so you'll catch this error at compile time. But it's worth internalizing now.

---

## Logical Operators

Used to combine boolean expressions.

```java
boolean a = true;
boolean b = false;

System.out.println(a && b);   // false (AND — both must be true)
System.out.println(a || b);   // true  (OR — at least one must be true)
System.out.println(!a);       // false (NOT — flips the value)
```

### Short-Circuit Evaluation

Java stops evaluating as soon as it knows the answer:

```java
// If x is 0, Java never calls dangerousMethod() because false && anything is false
if (x != 0 && dangerousMethod() / x > 0) { }

// If the first condition is true, Java never checks the second
if (isValid || expensiveCheck()) { }
```

This is a feature, not a bug. It prevents unnecessary computation and potential errors.

### Combining Conditions

```java
int age = 25;
boolean hasTicket = true;

if (age >= 18 && hasTicket) {
    System.out.println("Welcome!");
}

if (age < 13 || age > 65) {
    System.out.println("Discounted rate");
}

if (!(age >= 18)) {
    System.out.println("Must be 18 or older");
}
```

---

## Assignment Operators

Shorthand operators that combine arithmetic with assignment.

```java
int x = 10;

x += 5;    // x = x + 5  → 15
x -= 3;    // x = x - 3  → 12
x *= 2;    // x = x * 2  → 24
x /= 4;    // x = x / 4  → 6
x %= 4;    // x = x % 4  → 2
```

These are just shortcuts. They don't do anything you couldn't do with the long form, but they make code more concise.

---

## Ternary Operator

A compact if-else in a single line.

```java
int age = 20;
String status = (age >= 18) ? "Adult" : "Minor";

// Same as:
String status;
if (age >= 18) {
    status = "Adult";
} else {
    status = "Minor";
}
```

Use ternary for simple assignments. If the logic is complex, an `if-else` block is clearer.

---

## Operator Precedence

When you write `3 + 4 * 5`, Java doesn't just go left to right. Multiplication happens before addition.

```
3 + 4 * 5  →  3 + 20  →  23
```

Here's the full precedence (from highest to lowest):

| Precedence | Operators | Associativity |
|------------|-----------|---------------|
| 1 | `()` | Left to right |
| 2 | `!` `++` `--` | Right to left |
| 3 | `*` `/` `%` | Left to right |
| 4 | `+` `-` | Left to right |
| 5 | `<` `<=` `>` `>=` | Left to right |
| 6 | `==` `!=` | Left to right |
| 7 | `&&` | Left to right |
| 8 | `\|\|` | Left to right |
| 9 | `=` `+=` `-=` etc. | Right to left |

When in doubt, use parentheses. They make your intent clear and have zero performance cost.

```java
// Hard to read
int result = a + b * c - d / e;

// Clear
int result = (a + (b * c)) - (d / e);
```

---

## Common Mistakes

**Confusing `&&` with `&`:**
```java
// && is short-circuit (stops early)
// & is bitwise AND (always evaluates both)
// Always use && for boolean logic
if (x != null && x.length() > 0) { }
```

**Forgetting operator precedence:**
```java
// What does this print?
System.out.println(2 + 3 * 4);  // 14, not 20
```

**Using `=` instead of `==`:**
```java
if (x = 5) { }   // won't compile — assignment isn't a boolean
if (x == 5) { }  // correct comparison
```

---

## Practice

1. What's the result of `17 % 5`? Calculate it by hand, then verify.
2. Write a program that checks if a number is positive, negative, or zero.
3. Use the ternary operator to assign "Even" or "Odd" to a variable.
4. What's `true && false || true`? Think about it before running the code.

---

**Previous:** [01-Variables](../01-variables/)
**Next:** [03-Control Flow](../03-control-flow/)
