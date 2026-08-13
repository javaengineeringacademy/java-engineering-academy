# Quiz: Inner Classes

## Multiple Choice Questions

1. What is a non-static inner class?
   - A) A class defined inside another class without the static keyword
   - B) A class defined outside another class
   - C) A static class
   - D) An abstract class

2. How does a non-static inner class access outer class members?
   - A) It cannot
   - B) Through an implicit reference to the outer class instance
   - C) Using the static keyword
   - D) Via the super keyword only

3. Which syntax creates an instance of a non-static inner class?
   - A) new Outer().new Inner()
   - B) new Inner()
   - C) Outer.Inner.new()
   - D) new Outer.Inner()

4. What is the main use case for inner classes?
   - A) To increase memory usage
   - B) To group related classes and access outer class members
   - C) To replace interfaces
   - D) To create static utility methods

5. Can an inner class be private?
   - A) No
   - B) Yes
   - C) Only in Java 17+
   - D) Only if outer is abstract

## True/False Questions

6. A non-static inner class can access private members of the outer class.
   - True / False

7. Inner classes cannot have static members.
   - True / False

8. You must have an instance of the outer class to create an instance of a non-static inner class.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class School {
    private String name = "Springfield";
    class Classroom {
        void display() { System.out.println("Classroom in " + name); }
    }
}
class Test {
    public static void main(String[] args) {
        School s = new School();
        School.Classroom c = s.new Classroom();
        c.display();
    }
}
```

10. What will this code print?
```java
class Outer {
    int x = 10;
    class Inner {
        int x = 20;
        void show() {
            System.out.println("Inner x = " + x);
            System.out.println("Outer x = " + Outer.this.x);
        }
    }
}
class Test {
    public static void main(String[] args) {
        Outer o = new Outer();
        Outer.Inner i = o.new Inner();
        i.show();
    }
}
```

## Answers

1. A
2. B
3. A
4. B
5. B
6. True - Inner classes have access to all members of the outer class
7. True - Unless they are compile-time constants (static final)
8. True
9. Output:
```
Classroom in Springfield
```
10. Output:
```
Inner x = 20
Outer x = 10
```
