# Quiz: Constructors

## Multiple Choice Questions

1. What is a constructor in Java?
   - A) A method that returns a value
   - B) A special method called when an object is created
   - C) A static method
   - D) A method that destroys objects

2. What is the name of a constructor?
   - A) The class name
   - B) init
   - C) constructor
   - D) create

3. What is a parameterized constructor?
   - A) A constructor with no parameters
   - B) A constructor that accepts parameters
   - C) A constructor that is private
   - D) A static constructor

4. What happens if you don't define any constructor?
   - A) The class cannot be instantiated
   - B) Java provides a default no-arg constructor
   - C) You must use a factory method
   - D) Compilation error

5. Can a constructor be private?
   - A) No, never
   - B) Yes, for singleton patterns
   - C) Only in abstract classes
   - D) Only in interfaces

## True/False Questions

6. A constructor has a return type.
   - True / False

7. Constructor overloading is possible in Java.
   - True / False

8. The `this()` call must be the first statement in a constructor.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Book {
    String title;
    Book(String title) {
        this.title = title;
    }
    void display() {
        System.out.println("Title: " + title);
    }
}
public class Main {
    public static void main(String[] args) {
        Book b = new Book("Java Programming");
        b.display();
    }
}
```

10. What is the output of the following code?
```java
class Employee {
    String name;
    double salary;
    Employee() {
        name = "Unknown";
        salary = 0;
    }
    Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    void print() {
        System.out.println(name + ": $" + salary);
    }
}
public class Main {
    public static void main(String[] args) {
        Employee e1 = new Employee();
        Employee e2 = new Employee("Alice", 75000);
        e1.print();
        e2.print();
    }
}
```

---

## Answers

1. B) A special method called when an object is created
2. A) The class name
3. B) A constructor that accepts parameters
4. B) Java provides a default no-arg constructor
5. B) Yes, for singleton patterns
6. False (constructors have no return type)
7. True
8. True
9. Title: Java Programming
10. Unknown: $0.0
Alice: $75000.0