# Quiz: Abstraction

## Multiple Choice Questions

1. What is abstraction in OOP?
   - A) Showing all implementation details
   - B) Hiding complex implementation and showing only essential features
   - C) Creating multiple objects
   - D) Using inheritance

2. Which two ways can abstraction be achieved in Java?
   - A) Classes and objects
   - B) Abstract classes and interfaces
   - C) Methods and variables
   - D) Constructors and destructors

3. What is an abstract method?
   - A) A method with a body
   - B) A method without a body
   - C) A static method
   - D) A final method

4. Can an abstract class be instantiated?
   - A) Yes, always
   - B) No, never
   - C) Only if it has a constructor
   - D) Only in main method

5. What happens if a class doesn't implement all abstract methods?
   - A) It compiles fine
   - B) The class must also be declared abstract
   - C) Compilation error
   - D) Runtime exception

## True/False Questions

6. An abstract class can have non-abstract methods.
   - True / False

7. An abstract class can have constructors.
   - True / False

8. An interface can have instance variables.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
abstract class Shape {
    abstract double area();
    void display() {
        System.out.println("Area: " + area());
    }
}
class Rectangle extends Shape {
    double width, height;
    Rectangle(double w, double h) { width = w; height = h; }
    double area() { return width * height; }
}
public class Main {
    public static void main(String[] args) {
        Shape s = new Rectangle(5, 3);
        s.display();
    }
}
```

10. What is the output of the following code?
```java
abstract class Vehicle {
    abstract void start();
    void stop() {
        System.out.println("Vehicle stopped");
    }
}
class Bike extends Vehicle {
    void start() {
        System.out.println("Bike started with key");
    }
}
public class Main {
    public static void main(String[] args) {
        Vehicle v = new Bike();
        v.start();
        v.stop();
    }
}
```

---

## Answers

1. B) Hiding complex implementation and showing only essential features
2. B) Abstract classes and interfaces
3. B) A method without a body
4. B) No, never
5. B) The class must also be declared abstract
6. True
7. True
8. False (interfaces have constants, not instance variables)
9. Area: 15.0
10. Bike started with key
Vehicle stopped