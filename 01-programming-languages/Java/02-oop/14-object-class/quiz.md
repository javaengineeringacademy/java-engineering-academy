# Quiz: Object Class

## Multiple Choice Questions

1. What is the root class of all Java classes?
   - A) Main
   - B) Class
   - C) Object
   - D) Base

2. Which method returns a string representation of an object?
   - A) toString()
   - B) getString()
   - C) print()
   - D) display()

3. Which method is used to compare two objects for equality?
   - A) equals()
   - B) compare()
   - C) same()
   - D) isEqual()

4. What does the hashCode() method return?
   - A) A string
   - B) An integer hash code
   - C) A boolean
   - D) A double

5. Which method is called when an object is garbage collected?
   - A) close()
   - B) destroy()
   - C) finalize()
   - D) cleanup()

## True/False Questions

6. The equals() method compares memory addresses by default.
   - True / False

7. You should override toString() for meaningful object representation.
   - True / False

8. The hashCode() and equals() methods should be overridden together.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class Person {
    String name;
    Person(String name) { this.name = name; }
    public String toString() {
        return "Person{" + name + "}";
    }
}
public class Main {
    public static void main(String[] args) {
        Person p = new Person("Alice");
        System.out.println(p);
    }
}
```

10. What is the output of the following code?
```java
class Point {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point p = (Point) obj;
        return x == p.x && y == p.y;
    }
}
public class Main {
    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(3, 4);
        System.out.println(p1.equals(p2));
        System.out.println(p1.equals(p3));
    }
}
```

---

## Answers

1. C) Object
2. A) toString()
3. A) equals()
4. B) An integer hash code
5. C) finalize()
6. True (default compares references)
7. True
8. True
9. Person{Alice}
10. true
false