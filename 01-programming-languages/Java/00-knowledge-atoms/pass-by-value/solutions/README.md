# Pass by Value Solutions

Here are the solutions to the exercises.

## Exercise 1: Primitive Modification

The original value will NOT change. Primitives are passed by value (a copy is made).

```java
public class Exercise1 {
    public static void main(String[] args) {
        int num = 5;
        System.out.println("Before method call: " + num);
        
        doubleValue(num);
        
        System.out.println("After method call: " + num);
    }

    public static void doubleValue(int x) {
        x = x * 2;
        System.out.println("Inside method: " + x);
    }
}