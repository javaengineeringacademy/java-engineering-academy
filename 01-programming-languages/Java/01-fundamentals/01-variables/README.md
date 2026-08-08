# Variables & Data Types

Variables are containers for storing data. Think of them as labeled boxes — you put something in, give it a name, and refer to that name later.

---

## The Eight Primitive Types

Java has eight basic (primitive) data types. These are the building blocks.

### Whole Numbers

**byte** — stores numbers from -128 to 127. Uses 1 byte of memory.

```java
byte temperature = 35;
byte negative = -50;
```

Use `byte` when you're tight on memory and the number fits. Realistically, you'll use `int` most of the time.

**short** — stores numbers from -32,768 to 32,767. Uses 2 bytes.

```java
short year = 2024;
short population = 25000;
```

**int** — the workhorse. Stores roughly -2 billion to 2 billion. Uses 4 bytes.

```java
int age = 28;
int salary = 75000;
int numberOfStudents = 350;
```

If you're storing a whole number, start with `int`. Switch to something else only if you have a good reason.

**long** — for numbers bigger than 2 billion. Uses 8 bytes. Add an `L` at the end.

```java
long worldPopulation = 8_000_000_000L;
long distanceToSun = 150_000_000L;
```

The underscore is optional — it just makes large numbers easier to read.

### Decimal Numbers

**float** — single precision. Uses 4 bytes. Add an `F` at the end.

```java
float price = 19.99F;
float interestRate = 7.5F;
```

**double** — double precision. Uses 8 bytes. This is the default for decimals.

```java
double pi = 3.14159;
double bigNumber = 1_000_000.50;
```

Use `double` unless you have a specific reason to use `float`. The precision difference matters in most real-world applications.

### True or False

**boolean** — only two values: `true` or `false`.

```java
boolean isStudent = true;
boolean hasGraduated = false;
```

Simple, but incredibly powerful. Every if-statement and loop depends on booleans.

### Single Character

**char** — a single character. Uses single quotes.

```java
char grade = 'A';
char newline = '\n';
char copyright = '\u00A9';
```

Note: `char` uses single quotes (`'A'`), while `String` uses double quotes (`"A"`). This trips up every beginner at least once.

---

## Reference Types

Everything else in Java is a reference type. These are objects, and they're more flexible than primitives.

### String

Text is everywhere in programming. Java uses the `String` class for text.

```java
String name = "Pooja";
String greeting = "Hello, " + name + "!";
String empty = "";
```

Strings are **immutable** — once created, they can't be changed. When you "modify" a string, you're actually creating a new one.

```java
String original = "Hello";
String modified = original + " World";  // original is still "Hello"
```

### Arrays

We'll cover arrays in detail in [Topic 05](../05-arrays/). For now, just know they exist.

```java
int[] numbers = {1, 2, 3, 4, 5};
String[] names = {"Alice", "Bob", "Charlie"};
```

---

## Declaring and Initializing

You can declare a variable and assign a value in one line, or do it separately.

```java
// Declare and initialize
int age = 25;
String name = "Pooja";

// Declare first, initialize later
int score;
score = 100;

// Multiple declarations of the same type
int x, y, z;
x = 10;
y = 20;
z = 30;
```

---

## Naming Rules

Java has strict rules for variable names:

- Must start with a letter, underscore (`_`), or dollar sign (`$`)
- Can contain letters, digits, underscores, and dollar signs
- Case-sensitive (`age` and `Age` are different variables)
- Cannot use reserved words (`int`, `class`, `public`, etc.)

```java
// Valid names
int age;
int _count;
int $price;
int studentAge2;

// Invalid names
// int 2age;      // can't start with a digit
// int my-age;    // can't use hyphens
// int class;     // can't use reserved words
```

### Naming Conventions

Java follows conventions that most developers expect:

```java
// Variables and methods: camelCase
int studentAge;
String firstName;
boolean isLoggedIn;

// Classes: PascalCase
public class StudentAccount { }
public class BankTransaction { }

// Constants: UPPER_SNAKE_CASE
public static final int MAX_RETRY_COUNT = 3;
public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mydb";
```

---

## Type Casting

Sometimes you need to convert one type to another. Java handles this in two ways.

### Widening (Automatic)

Going from a smaller type to a larger type happens automatically.

```java
int myInt = 9;
double myDouble = myInt;  // int → double, no problem

byte myByte = 10;
int myInt = myByte;       // byte → int, automatic
```

### Narrowing (Manual)

Going from a larger type to a smaller type requires an explicit cast. You're telling Java "I know what I'm doing."

```java
double myDouble = 9.78;
int myInt = (int) myDouble;  // drops the decimal → 9

int myInt = 256;
byte myByte = (byte) myInt;  // overflows → 0
```

The narrowing example with `byte` is a good reminder — casting can lose data. Be careful.

---

## Constants

Use `final` when a value should never change.

```java
final double PI = 3.14159;
final int MAX_LOGIN_ATTEMPTS = 5;
final String APP_NAME = "MyApplication";

// PI = 3.14;  // This won't compile — you can't change a final variable
```

Constants make your code clearer and prevent accidental changes.

---

## Common Mistakes

**Forgetting to initialize:**
```java
int score;
System.out.println(score);  // won't compile — score might not have a value
```

**Integer division surprise:**
```java
int result = 7 / 2;  // result is 3, not 3.5
double better = 7.0 / 2;  // result is 3.5
```

**Confusing = with ==:**
```java
int a = 5;       // assignment (sets the value)
if (a == 5) { }  // comparison (checks if equal)
```

---

## Practice

1. Create variables of each primitive type and print them
2. Try casting a `double` to an `int` — what happens to the decimal?
3. What's the result of `int x = 5 / 2`? Now try `double x = 5.0 / 2`
4. Create a constant for your favorite number and try to change it

---

**Next:** [02-Operators](../02-operators/)
