# Quiz: Abstract Classes

## Multiple Choice Questions

1. What is an abstract class?
   - A) A class that cannot be instantiated
   - B) A class that can only be instantiated
   - C) A class with only static methods
   - D) A class with only final methods

2. Which keyword is used to declare an abstract class?
   - A) final
   - B) static
   - C) abstract
   - D) interface

3. Can an abstract class have a constructor?
   - A) No, never
   - B) Yes
   - C) Only if it's public
   - D) Only if it has no abstract methods

4. What happens if a concrete class extends an abstract class?
   - A) Nothing
   - B) It must implement all abstract methods
   - C) It can leave some methods unimplemented
   - D) It becomes abstract

5. Can an abstract class have both abstract and concrete methods?
   - A) No
   - B) Yes
   - C) Only abstract methods
   - D) Only concrete methods

## True/False Questions

6. An abstract class can be declared as final.
   - True / False

7. An abstract class can have instance variables.
   - True / False

8. An abstract class can extend another abstract class.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
abstract class Animal {
    String name;
    Animal(String name) { this.name = name; }
    abstract void makeSound();
    void sleep() {
        System.out.println(name + " is sleeping");
    }
}
class Cat extends Animal {
    Cat(String name) { super(name); }
    void makeSound() {
        System.out.println(name + " says Meow");
    }
}
public class Main {
    public static void main(String[] args) {
        Cat c = new Cat("Whiskers");
        c.makeSound();
        c.sleep();
    }
}
```

10. What is the output of the following code?
```java
abstract class Logger {
    abstract void log(String message);
    void logWithTimestamp(String message) {
        System.out.println("[2024] " + message);
    }
}
class ConsoleLogger extends Logger {
    void log(String message) {
        System.out.println("LOG: " + message);
    }
}
public class Main {
    public static void main(String[] args) {
        Logger l = new ConsoleLogger();
        l.log("Error occurred");
        l.logWithTimestamp("Warning");
    }
}
```

---

## Answers

1. A) A class that cannot be instantiated
2. C) abstract
3. B) Yes
4. B) It must implement all abstract methods
5. B) Yes
6. False (abstract and final are mutually exclusive)
7. True
8. True
9. Whiskers says Meow
Whiskers is sleeping
10. LOG: Error occurred
[2024] Warning