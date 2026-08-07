# Pass by Value Exercises

Complete the following exercises to test your understanding of Java's pass-by-value mechanism.

## Exercise 1: Primitive Modification

Write a method that attempts to double a primitive value passed to it. Predict whether the original value will change.

```java
public class Exercise1 {
    public static void main(String[] args) {
        int num = 5;
        doubleValue(num);
        System.out.println("Original value: " + num);
        // What will this print?
    }

    // Write your method here
    
}