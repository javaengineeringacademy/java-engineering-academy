# Autoboxing Exercises

## Exercise 1: Basic Autoboxing
Write a program that demonstrates autoboxing for all primitive types (byte, short, int, long, float, double, char, boolean).

## Exercise 2: Integer Cache
Write a program that tests the Integer cache boundary conditions. Test values -128, -129, 127, 128 and verify whether `==` returns true or false.

## Exercise 3: Collection Operations
Create an ArrayList of Integer values and perform the following operations:
- Add values using autoboxing
- Retrieve values using unboxing
- Calculate the sum of all elements
- Find the maximum value

## Exercise 4: Null Safety
Write a method that safely unboxes an Integer parameter, returning a default value if the parameter is null.

## Exercise 5: Performance Comparison
Write a program comparing the performance of using Integer vs int in a loop of 1,000,000 iterations.

## Exercise 6: Method Overloading
Create a class with overloaded methods that accept both int and Integer parameters. Determine which method gets called in different scenarios.

## Exercise 7: Autoboxing in Expressions
Predict the output of the following code and verify your answer:
```java
Integer a = 100;
Integer b = 200;
Integer c = a + b;
System.out.println(c);
```

## Exercise 8: Wrapper Class Methods
Use Integer wrapper class methods to:
- Convert a String to an Integer
- Convert an Integer to a String
- Get the binary representation of a number
- Get the maximum value of an Integer

## Exercise 9: Boolean Autoboxing
Test autoboxing behavior with Boolean values. Verify that Boolean.TRUE == Boolean.TRUE returns true.

## Exercise 10: Character Autoboxing
Test autoboxing behavior with Character values. Verify the cache range for Character (0-127).
