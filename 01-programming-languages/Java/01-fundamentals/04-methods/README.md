# Methods

Methods are blocks of code that do something. They take input, do work, and optionally return output. Without methods, every program would be one long, unorganized mess.

---

## Your First Method

Every Java program starts with a `main` method. But you'll write many more.

```java
public class MethodDemo {
    // This is a method
    public static void sayHello() {
        System.out.println("Hello!");
    }

    public static void main(String[] args) {
        sayHello();  // call the method
        sayHello();  // call it again — reuse!
    }
}
```

The word `static` means you can call this method without creating an object. We'll cover that properly in OOP.

---

## Methods with Parameters

Parameters let you pass data into a method.

```java
public static void greet(String name) {
    System.out.println("Hello, " + name + "!");
}

public static void main(String[] args) {
    greet("Pooja");   // Hello, Pooja!
    greet("Alice");   // Hello, Alice!
}
```

### Multiple Parameters

```java
public static int add(int a, int b) {
    return a + b;
}

public static void main(String[] args) {
    int result = add(3, 5);  // result is 8
    System.out.println(result);
}
```

---

## Methods with Return Values

A method can send a value back to whoever called it using `return`.

```java
public static double calculateArea(double length, double width) {
    return length * width;
}

public static void main(String[] args) {
    double area = calculateArea(5.0, 3.0);  // area is 15.0
    System.out.println("Area: " + area);
}
```

### Return Type Must Match

If a method says it returns an `int`, it must return an `int`. No exceptions.

```java
public static int getNumber() {
    return 42;        // correct — returns an int
    // return "42";   // won't compile — wrong type
}
```

### void — No Return

If a method doesn't return anything, use `void`.

```java
public static void printLine() {
    System.out.println("───────────────────");
    // no return statement needed
}
```

---

## Parameter Passing

Java passes everything by value. For primitives, it copies the value. For objects, it copies the reference (but the object itself isn't copied).

### Primitives: Copy of the Value

```java
public static void changeNumber(int x) {
    x = 100;  // only changes the local copy
}

public static void main(String[] args) {
    int num = 5;
    changeNumber(num);
    System.out.println(num);  // still 5 — the original didn't change
}
```

### Objects: Copy of the Reference

```java
public static void changeName(StringBuilder sb) {
    sb.append(" World");  // modifies the original object
}

public static void main(String[] args) {
    StringBuilder greeting = new StringBuilder("Hello");
    changeName(greeting);
    System.out.println(greeting);  // "Hello World" — the object was modified
}
```

---

## Method Overloading

You can have multiple methods with the same name but different parameters.

```java
public static int add(int a, int b) {
    return a + b;
}

public static double add(double a, double b) {
    return a + b;
}

public static String add(String a, String b) {
    return a + b;
}

public static void main(String[] args) {
    System.out.println(add(3, 5));        // 8 (calls int version)
    System.out.println(add(3.5, 2.1));    // 5.6 (calls double version)
    System.out.println(add("Hi ", "Mom")); // "Hi Mom" (calls String version)
}
```

Java picks the right method based on the argument types. This is one of the most useful features in the language.

---

## Static vs Instance Methods

For now, all your methods will be `static`. This means you can call them without creating an object.

```java
// Static method — called on the class
Math.sqrt(25);  // 5.0

// Instance method — called on an object (we'll learn this in OOP)
// String s = "hello";
// s.toUpperCase();  // "HELLO"
```

When you see `static`, think "this belongs to the class, not to a specific object."

---

## Naming Methods

Follow these conventions:

```java
// camelCase, verb first
public static void calculateTotal() { }
public static boolean isValid() { }
public static String getUserName() { }
public static int findMax(int a, int b) { }

// Names should describe what the method does
// Good: calculateAverage, isPrime, formatCurrency
// Bad: calc, doStuff, method1, process
```

---

## Real-World Example: Temperature Converter

```java
public class TemperatureConverter {
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }

    public static String describe(double celsius) {
        if (celsius > 30) {
            return "Hot";
        } else if (celsius > 20) {
            return "Warm";
        } else if (celsius > 10) {
            return "Cool";
        } else {
            return "Cold";
        }
    }

    public static void main(String[] args) {
        double boiling = celsiusToFahrenheit(100);
        System.out.println("Boiling point: " + boiling + "°F");  // 212.0°F

        double bodyTemp = fahrenheitToCelsius(98.6);
        System.out.println("Body temp: " + bodyTemp + "°C");    // 37.0°C

        System.out.println("It's " + describe(25) + " outside"); // It's Warm outside
    }
}
```

This example shows methods calling other methods, returning values, and keeping code organized.

---

## Common Mistakes

**Forgetting return:**
```java
public static int add(int a, int b) {
    int sum = a + b;
    // return sum;  // won't compile — method promises to return int
}
```

**Returning the wrong type:**
```java
public static int getLength(String s) {
    return s.length();  // correct — length() returns int
    // return s;        // won't compile — can't return String as int
}
```

**Using the wrong parameter types:**
```java
public static int add(int a, int b) { return a + b; }

// add(3.5, 2.1);  // won't compile — doubles aren't ints
add(3, 5);          // correct
```

---

## Practice

1. Write a method that takes two numbers and returns the larger one
2. Write a method that checks if a string is a palindrome (reads the same backward)
3. Overload a `print` method to handle `int`, `double`, and `String` differently
4. Build a simple calculator with `add`, `subtract`, `multiply`, `divide` methods

---

**Previous:** [03-Control Flow](../03-control-flow/)
**Next:** [05-Arrays](../05-arrays/)
