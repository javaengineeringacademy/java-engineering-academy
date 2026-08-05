# Method Overloading in Java

## Overview
Method overloading allows multiple methods with the same name but different parameter lists in the same class.

## When to Use
- To provide multiple ways to call a method with different parameter types
- To create flexible APIs
- When methods perform similar operations with different inputs

## Code Example
See `src/main/java/academy/javaengineering/oop/methodoverloading/Calculator.java`

```java
Calculator calc = new Calculator();
calc.add(2, 3);          // int version
calc.add(2.5, 3.5);      // double version
calc.add("A", "B");       // String version
calc.add(1, 2, 3, 4, 5); // varargs version
```

## Common Mistakes
1. Confusing overloading with overriding
2. Ambiguous overloads causing compilation errors
3. Using overloading when overriding is needed
4. Not considering autoboxing behavior

## Interview Questions
1. What is the difference between overloading and overriding?
2. Can you overload by return type only?
3. How does Java resolve overloaded methods?
4. What are the rules for method signature matching?
5. Can constructors be overloaded?
