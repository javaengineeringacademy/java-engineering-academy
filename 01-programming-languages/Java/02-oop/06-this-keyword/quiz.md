# Quiz: This Keyword

## Multiple Choice Questions

1. What does the `this` keyword refer to?
   - A) The parent class
   - B) The current object
   - C) The static context
   - D) The main method

2. When is `this` used to resolve name ambiguity?
   - A) When a local variable shadows an instance variable
   - B) When using static methods
   - C) When importing packages
   - D) When defining constants

3. Can `this` be used in a static context?
   - A) Yes, always
   - B) Yes, but only with an instance
   - C) No, never
   - D) Only in main method

4. What does `this()` do when called in a constructor?
   - A) Creates a new object
   - B) Calls another constructor of the same class
   - C) Calls the superclass constructor
   - D) Returns the current object

5. Can `this` be passed as a method argument?
   - A) No, never
   - B) Yes, it passes the current object reference
   - C) Only in abstract classes
   - D) Only in interfaces

## True/False Questions

6. `this` can be used to access private members of the same class.
   - True / False

7. `this` can be used to call methods of the current object.
   - True / False

8. `this` can be used to call a static method.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void print() {
        System.out.println("(" + x + ", " + y + ")");
    }
}
public class Main {
    public static void main(String[] args) {
        Point p = new Point(5, 10);
        p.print();
    }
}
```

10. What is the output of the following code?
```java
class Student {
    String name;
    Student(String name) {
        this.name = name;
    }
    Student getThis() {
        return this;
    }
    void print() {
        System.out.println("Student: " + name);
    }
}
public class Main {
    public static void main(String[] args) {
        Student s = new Student("Bob");
        Student ref = s.getThis();
        ref.print();
    }
}
```

---

## Answers

1. B) The current object
2. A) When a local variable shadows an instance variable
3. C) No, never
4. B) Calls another constructor of the same class
5. B) Yes, it passes the current object reference
6. True
7. True
8. False (this cannot be used in static contexts)
9. (5, 10)
10. Student: Bob