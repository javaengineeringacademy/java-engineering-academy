# Quiz: Objects

## Multiple Choice Questions

1. What keyword is used to create an object in Java?
   - A) create
   - B) new
   - C) make
   - D) object

2. What is the reference variable in the following code? `Car myCar = new Car();`
   - A) Car
   - B) myCar
   - C) new
   - D) Car()

3. What happens if you try to use an uninitialized reference variable?
   - A) It works normally
   - B) Compilation error
   - C) NullPointerException at runtime
   - D) It defaults to 0

4. How many objects can be created from a single class?
   - A) Only one
   - B) Only two
   - C) Unlimited
   - D) Depends on the JVM

5. What does the `new` keyword do?
   - A) Declares a variable
   - B) Allocates memory for an object
   - C) Calls a method
   - D) Imports a package

## True/False Questions

6. An object is an instance of a class.
   - True / False

7. Two object references can point to the same object in memory.
   - True / False

8. Objects in Java are passed by value to methods.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Person {
    String name;
    int age;
    void introduce() {
        System.out.println("Hi, I'm " + name);
    }
}
public class Main {
    public static void main(String[] args) {
        Person p1 = new Person();
        Person p2 = p1;
        p1.name = "John";
        p2.name = "Jane";
        p1.introduce();
    }
}
```

10. What is the output of the following code?
```java
class Counter {
    int count = 0;
    void increment() {
        count++;
    }
}
public class Main {
    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        c1.increment();
        c1.increment();
        c2.increment();
        System.out.println("c1: " + c1.count + ", c2: " + c2.count);
    }
}
```

---

## Answers

1. B) new
2. B) myCar
3. C) NullPointerException at runtime
4. C) Unlimited
5. B) Allocates memory for an object
6. True
7. True
8. True (the reference is passed by value)
9. Hi, I'm Jane
10. c1: 2, c2: 1