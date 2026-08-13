# Quiz: Inheritance

## Multiple Choice Questions

1. What is inheritance in OOP?
   - A) Creating new objects
   - B) A mechanism where a child class acquires properties of a parent class
   - C) Hiding data
   - D) Defining methods

2. Which keyword is used to inherit a class in Java?
   - A) implements
   - B) extends
   - C) inherits
   - D) super

3. What is the parent class also called?
   - A) Child class
   - B) Subclass
   - C) Superclass
   - D) Derived class

4. How many classes can a Java class extend?
   - A) Unlimited
   - B) Two
   - C) Three
   - D) One

5. What is the topmost class in Java's inheritance hierarchy?
   - A) Main
   - B) Object
   - C) Class
   - D) Base

## True/False Questions

6. Inheritance promotes code reusability.
   - True / False

7. A child class can access all members of the parent class.
   - True / False

8. All classes in Java implicitly inherit from the Object class.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Vehicle {
    String brand = "Generic";
    void honk() {
        System.out.println(brand + " goes beep!");
    }
}
class Car extends Vehicle {
    String brand = "Toyota";
}
public class Main {
    public static void main(String[] args) {
        Car c = new Car();
        System.out.println(c.brand);
        c.honk();
    }
}
```

10. What is the output of the following code?
```java
class Shape {
    String type = "Shape";
    void describe() {
        System.out.println("I am a " + type);
    }
}
class Circle extends Shape {
    Circle() {
        type = "Circle";
    }
}
public class Main {
    public static void main(String[] args) {
        Circle c = new Circle();
        c.describe();
    }
}
```

---

## Answers

1. B) A mechanism where a child class acquires properties of a parent class
2. B) extends
3. C) Superclass
4. D) One
5. B) Object
6. True
7. False (cannot access private members)
8. True
9. Toyota
Generic goes beep!
10. I am a Circle