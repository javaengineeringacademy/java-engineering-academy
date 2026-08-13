# Quiz: Classes

## Multiple Choice Questions

1. What keyword is used to define a class in Java?
   - A) struct
   - B) class
   - C) object
   - D) type

2. Which of the following is a valid class definition?
   - A) `class MyClass {}`
   - B) `define MyClass {}`
   - C) `create MyClass {}`
   - D) `new MyClass {}`

3. What is a member variable of a class also called?
   - A) Local variable
   - B) Instance variable
   - C) Global variable
   - D) Static variable

4. Which access modifier makes a class accessible only within the same package?
   - A) public
   - B) private
   - C) protected
   - D) default (no modifier)

5. Can a Java file contain multiple public classes?
   - A) Yes, unlimited
   - B) Yes, but only two
   - C) No, only one
   - D) Only if they are inner classes

## True/False Questions

6. A class can exist without any methods defined.
   - True / False

7. The name of a public class must match the filename.
   - True / False

8. A class can extend multiple classes.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Student {
    String name = "Alice";
    int age = 20;
}
public class Main {
    public static void main(String[] args) {
        Student s = new Student();
        System.out.println(s.name + " is " + s.age + " years old.");
    }
}
```

10. What is the output of the following code?
```java
class Box {
    int width = 10;
    int height = 20;
    int getArea() {
        return width * height;
    }
}
public class Main {
    public static void main(String[] args) {
        Box b = new Box();
        System.out.println("Area: " + b.getArea());
    }
}
```

---

## Answers

1. B) class
2. A) `class MyClass {}`
3. B) Instance variable
4. D) default (no modifier)
5. C) No, only one
6. True
7. True
8. False (Java supports single inheritance only)
9. Alice is 20 years old.
10. Area: 200