# Quiz: Dependency

## Multiple Choice Questions

1. What is dependency in Java?
   - A) A relationship where one class depends on another to function
   - B) A permanent "has-a" relationship
   - C) An "is-a" relationship
   - D) A relationship where both classes are equal

2. When does dependency typically occur?
   - A) When a class is instantiated in another class's field
   - B) When a class is used as a method parameter or local variable
   - C) When a class inherits from another
   - D) When two classes implement the same interface

3. Which is the weakest form of relationship in UML?
   - A) Inheritance
   - B) Composition
   - C) Aggregation
   - D) Dependency

4. How is dependency usually represented in code?
   - A) As a field of the class
   - B) As a method parameter or local variable
   - C) As a constructor argument stored in a field
   - D) As a superclass

5. What does reducing dependency between classes improve?
   - A) Execution speed
   - B) Maintainability and testability
   - C) Memory usage
   - D) Number of classes

## True/False Questions

6. Dependency is a stronger relationship than association.
   - True / False

7. A class that uses another class only in a method has a dependency relationship.
   - True / False

8. Dependency injection is a technique to reduce hard-coded dependencies.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Logger {
    void log(String msg) { System.out.println("LOG: " + msg); }
}
class Service {
    void processData(Logger logger, String data) {
        logger.log("Processing: " + data);
        System.out.println("Done");
    }
}
class Test {
    public static void main(String[] args) {
        Service s = new Service();
        Logger l = new Logger();
        s.processData(l, "orders");
    }
}
```

10. What will this code print?
```java
class MathHelper {
    static int square(int n) { return n * n; }
}
class App {
    int[] values;
    App(int[] values) { this.values = values; }
    int sumOfSquares() {
        int sum = 0;
        for (int v : values)
            sum += MathHelper.square(v);
        return sum;
    }
}
class Test {
    public static void main(String[] args) {
        App app = new App(new int[]{2, 3, 4});
        System.out.println("Sum: " + app.sumOfSquares());
    }
}
```

## Answers

1. A
2. B - Temporary usage creates dependency, not permanent reference
3. D
4. B
5. B
6. False - Dependency is weaker than association
7. True
8. True
9. Output:
```
LOG: Processing: orders
Done
```
10. Output:
```
Sum: 29
```
(2^2 + 3^2 + 4^2 = 4 + 9 + 16 = 29)
