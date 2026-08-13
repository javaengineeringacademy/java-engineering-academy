# Quiz: Sealed Classes

## Multiple Choice Questions

1. What does the `sealed` keyword do in Java?
   - A) Makes a class immutable
   - B) Restricts which classes can extend or implement it
   - C) Makes a class thread-safe
   - D) Prevents instantiation

2. What must subclasses of a sealed class use?
   - A) open
   - B) final, sealed, or non-sealed
   - C) abstract
   - D) public

3. Which Java version introduced sealed classes?
   - A) Java 8
   - B) Java 11
   - C) Java 17
   - D) Java 21

4. What is the `permits` clause used for?
   - A) To list allowed subclasses
   - B) To define methods
   - C) To create instances
   - D) To import packages

5. Can a sealed interface have permitted subtypes?
   - A) No
   - B) Yes, using the permits keyword
   - C) Only in Java 21
   - D) Only with abstract classes

## True/False Questions

6. A sealed class must be in the same module as its permitted subclasses.
   - True / False

7. A sealed class can have a `non-sealed` subclass that anyone can extend.
   - True / False

8. Sealed classes enforce exhaustive pattern matching in switch expressions.
   - True / False

## Code Output Questions

9. What will this code print?
```java
sealed interface Shape permits Circle, Rectangle {}
record Circle(double radius) implements Shape {}
record Rectangle(double w, double h) implements Shape {}
class Test {
    static String describe(Shape s) {
        return switch (s) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.w() + "x" + r.h();
        };
    }
    public static void main(String[] args) {
        System.out.println(describe(new Circle(5.0)));
        System.out.println(describe(new Rectangle(3.0, 4.0)));
    }
}
```

10. What will this code print?
```java
sealed interface Animal permits Dog, Cat {}
final class Dog implements Animal { String sound() { return "Woof"; } }
final class Cat implements Animal { String sound() { return "Meow"; } }
class Test {
    public static void main(String[] args) {
        Animal a = new Dog();
        String s = switch (a) {
            case Dog d -> d.sound();
            case Cat c -> c.sound();
        };
        System.out.println(s);
    }
}
```

## Answers

1. B
2. B
3. C - Java 17 (preview in 16)
4. A
5. B
6. True - Unless in the same package in the unnamed module
7. True
8. True
9. Output:
```
Circle with radius 5.0
Rectangle 3.0x4.0
```
10. Output:
```
Woof
```
