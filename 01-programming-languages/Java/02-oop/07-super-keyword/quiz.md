# Quiz: Super Keyword

## Multiple Choice Questions

1. What does the `super` keyword refer to?
   - A) The current object
   - B) The parent class object
   - C) The static context
   - D) The main method

2. When is `super` used to access parent class members?
   - A) When child class shadows parent class members
   - B) When using static methods
   - C) When importing packages
   - D) When defining constants

3. Can `super` be used in a static context?
   - A) Yes, always
   - B) Yes, but only with an instance
   - C) No, never
   - D) Only in main method

4. What does `super()` do when called in a constructor?
   - A) Creates a new object
   - B) Calls the parent class constructor
   - C) Calls another constructor of the same class
   - D) Returns the parent object

5. Can `super` be used to call a method of the parent class?
   - A) No, never
   - B) Yes, using super.methodName()
   - C) Only in abstract classes
   - D) Only in interfaces

## True/False Questions

6. `super` can be used to access private members of the parent class.
   - True / False

7. `super()` must be the first statement in a child constructor.
   - True / False

8. `super` can be used to call a static method of the parent class.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Animal {
    String type = "Animal";
    void eat() {
        System.out.println(type + " is eating");
    }
}
class Dog extends Animal {
    String type = "Dog";
    void printType() {
        System.out.println("Child: " + type);
        System.out.println("Parent: " + super.type);
    }
}
public class Main {
    public static void main(String[] args) {
        Dog d = new Dog();
        d.printType();
    }
}
```

10. What is the output of the following code?
```java
class Parent {
    Parent() {
        System.out.println("Parent constructor");
    }
}
class Child extends Parent {
    Child() {
        super();
        System.out.println("Child constructor");
    }
}
public class Main {
    public static void main(String[] args) {
        Child c = new Child();
    }
}
```

---

## Answers

1. B) The parent class object
2. A) When child class shadows parent class members
3. C) No, never
4. B) Calls the parent class constructor
5. B) Yes, using super.methodName()
6. False (super cannot access private members)
7. True
8. False (super is for instance contexts)
9. Child: Dog
Parent: Animal
10. Parent constructor
Child constructor