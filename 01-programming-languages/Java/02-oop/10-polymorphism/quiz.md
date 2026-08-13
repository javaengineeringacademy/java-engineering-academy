# Quiz: Polymorphism

## Multiple Choice Questions

1. What is polymorphism in OOP?
   - A) Having multiple constructors
   - B) The ability of an object to take many forms
   - C) Creating multiple objects
   - D) Defining multiple variables

2. Which type of polymorphism is resolved at compile time?
   - A) Runtime polymorphism
   - B) Dynamic polymorphism
   - C) Compile-time polymorphism
   - D) Ad-hoc polymorphism

3. What is method overriding?
   - A) Defining multiple methods with the same name
   - B) Redefining a parent class method in a child class
   - C) Calling a method multiple times
   - D) Creating a new method

4. Which keyword is used for runtime polymorphism?
   - A) static
   - B) final
   - C) virtual (implicit in Java)
   - D) abstract

5. Can a parent class reference point to a child class object?
   - A) No, never
   - B) Yes, this is called upcasting
   - C) Only in abstract classes
   - D) Only with interfaces

## True/False Questions

6. Method overloading is an example of polymorphism.
   - True / False

7. Polymorphism makes code more flexible and maintainable.
   - True / False

8. You can call child-specific methods using a parent class reference.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Animal {
    void sound() {
        System.out.println("Some sound");
    }
}
class Dog extends Animal {
    void sound() {
        System.out.println("Bark");
    }
}
class Cat extends Animal {
    void sound() {
        System.out.println("Meow");
    }
}
public class Main {
    public static void main(String[] args) {
        Animal a1 = new Dog();
        Animal a2 = new Cat();
        a1.sound();
        a2.sound();
    }
}
```

10. What is the output of the following code?
```java
class Calculator {
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
    int add(int a, int b, int c) { return a + b + c; }
}
public class Main {
    public static void main(String[] args) {
        Calculator c = new Calculator();
        System.out.println(c.add(2, 3));
        System.out.println(c.add(2.5, 3.5));
        System.out.println(c.add(1, 2, 3));
    }
}
```

---

## Answers

1. B) The ability of an object to take many forms
2. C) Compile-time polymorphism
3. B) Redefining a parent class method in a child class
4. C) virtual (implicit in Java)
5. B) Yes, this is called upcasting
6. True
7. True
8. False (need casting to access child-specific methods)
9. Bark
Meow
10. 5
6.0
6