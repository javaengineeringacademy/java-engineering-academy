# Quiz: Introduction to OOP

## Multiple Choice Questions

1. What does OOP stand for?
   - A) Object Process Programming
   - B) Object-Oriented Programming
   - C) Oriented Object Programming
   - D) Object Organization Protocol

2. Which of the following is NOT a pillar of OOP?
   - A) Encapsulation
   - B) Inheritance
   - C) Compilation
   - D) Polymorphism

3. What is an object in OOP?
   - A) A function that returns values
   - B) An instance of a class
   - C) A data type
   - D) A variable name

4. What is a class in OOP?
   - A) A blueprint for creating objects
   - B) An actual instance in memory
   - C) A method inside a function
   - D) A type of variable

5. Which language is the foundation for Java's OOP model?
   - A) C
   - B) Smalltalk
   - C) Python
   - D) Ruby

## True/False Questions

6. OOP promotes code reusability through inheritance.
   - True / False

7. In OOP, data and the methods that operate on that data are bundled together.
   - True / False

8. Java supports multiple inheritance using classes.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Dog {
    String name;
    void bark() {
        System.out.println(name + " says Woof!");
    }
}
public class Main {
    public static void main(String[] args) {
        Dog d = new Dog();
        d.name = "Buddy";
        d.bark();
    }
}
```

10. What is the output of the following code?
```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }
}
public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println(calc.add(5, 3));
    }
}
```

---

## Answers

1. B) Object-Oriented Programming
2. C) Compilation
3. B) An instance of a class
4. A) A blueprint for creating objects
5. B) Smalltalk
6. True
7. True
8. False (Java does not support multiple inheritance with classes)
9. Buddy says Woof!
10. 8