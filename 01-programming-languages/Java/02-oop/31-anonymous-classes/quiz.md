# Quiz: Anonymous Classes

## Multiple Choice Questions

1. What is an anonymous class?
   - A) A class without a name defined and instantiated in a single expression
   - B) A class declared as private
   - C) A class without any methods
   - D) A static class

2. What can an anonymous class extend or implement?
   - A) Only interfaces
   - B) Only abstract classes
   - C) An interface or a class (abstract or concrete)
   - D) Nothing

3. Where can anonymous classes be defined?
   - A) Only at class level
   - B) Only inside methods
   - C) At class level or inside methods
   - D) Only in main method

4. What is a common use of anonymous classes?
   - A) Creating reusable, named classes
   - B) Implementing short, one-time-use event handlers
   - C) Extending String class
   - D) Defining package structure

5. Can anonymous classes have constructors?
   - A) Yes, named constructors
   - B) No, they cannot define constructors
   - C) Yes, using the class name
   - D) Only if they extend a class

## True/False Questions

6. Anonymous classes can access local variables of the enclosing method if they are final or effectively final.
   - True / False

7. An anonymous class can extend a class and implement an interface simultaneously.
   - True / False

8. Anonymous classes are compiled into separate .class files.
   - True / False

## Code Output Questions

9. What will this code print?
```java
interface Greeter {
    void greet();
}
class Test {
    public static void main(String[] args) {
        Greeter g = new Greeter() {
            public void greet() { System.out.println("Hello!"); }
        };
        g.greet();
    }
}
```

10. What will this code print?
```java
abstract class Animal {
    abstract String sound();
}
class Test {
    public static void main(String[] args) {
        Animal dog = new Animal() { String sound() { return "Woof"; } };
        Animal cat = new Animal() { String sound() { return "Meow"; } };
        System.out.println(dog.sound());
        System.out.println(cat.sound());
    }
}
```

## Answers

1. A
2. C
3. C
4. B
5. B - They cannot define constructors (use instance initializer instead)
6. True
7. False - It can extend a class OR implement an interface, not both
8. True - Named like OuterClass$1.class
9. Output:
```
Hello!
```
10. Output:
```
Woof
Meow
```
