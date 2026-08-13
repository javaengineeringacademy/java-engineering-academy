# Quiz: Interfaces

## Multiple Choice Questions

1. What is an interface in Java?
   - A) A class with implementation
   - B) A contract that defines methods a class must implement
   - C) A variable type
   - D) A constructor

2. Which keyword is used to implement an interface?
   - A) extends
   - B) implements
   - C) inherits
   - D) uses

3. Can interfaces have instance variables?
   - A) Yes, any type
   - B) Yes, but only public static final
   - C) No, never
   - D) Only private

4. Can a class implement multiple interfaces?
   - A) No
   - B) Yes
   - C) Only two
   - D) Only in Java 8+

5. What is a functional interface?
   - A) An interface with no methods
   - B) An interface with exactly one abstract method
   - C) An interface with only static methods
   - D) An interface with only default methods

## True/False Questions

6. An interface can extend another interface.
   - True / False

7. All methods in an interface are public by default.
   - True / False

8. An interface can have a constructor.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
interface Drawable {
    void draw();
}
class Circle implements Drawable {
    public void draw() {
        System.out.println("Drawing circle");
    }
}
class Square implements Drawable {
    public void draw() {
        System.out.println("Drawing square");
    }
}
public class Main {
    public static void main(String[] args) {
        Drawable d1 = new Circle();
        Drawable d2 = new Square();
        d1.draw();
        d2.draw();
    }
}
```

10. What is the output of the following code?
```java
interface Playable {
    default void play() {
        System.out.println("Playing...");
    }
}
interface Recordable {
    default void record() {
        System.out.println("Recording...");
    }
}
class Device implements Playable, Recordable {
}
public class Main {
    public static void main(String[] args) {
        Device d = new Device();
        d.play();
        d.record();
    }
}
```

---

## Answers

1. B) A contract that defines methods a class must implement
2. B) implements
3. B) Yes, but only public static final
4. B) Yes
5. B) An interface with exactly one abstract method
6. True
7. True
8. False (interfaces cannot have constructors)
9. Drawing circle
Drawing square
10. Playing...
Recording...