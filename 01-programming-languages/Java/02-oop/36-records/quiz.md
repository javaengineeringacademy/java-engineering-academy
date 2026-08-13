# Quiz: Records

## Multiple Choice Questions

1. What is a record in Java?
   - A) A special class for immutable data carriers
   - B) A type of database table
   - C) A collection type
   - D) A loop structure

2. Which components does a record automatically generate?
   - A) Constructor, getters, equals(), hashCode(), toString()
   - B) Only constructor
   - C) Setters and getters
   - D) Main method

3. What is the canonical constructor of a record?
   - A) A no-arg constructor
   - B) A constructor that takes all components as parameters
   - C) A private constructor
   - D) A static factory method

4. Can records have additional methods beyond the automatic ones?
   - A) No
   - B) Yes
   - C) Only static methods
   - D) Only if they override equals()

5. Records implicitly extend which class?
   - A) Object
   - B) Record
   - C) AbstractRecord
   - D) They don't extend anything

## True/False Questions

6. Records are implicitly final.
   - True / False

7. Record components are final fields.
   - True / False

8. Records can implement interfaces.
   - True / False

## Code Output Questions

9. What will this code print?
```java
record Point(int x, int y) {
    Point {  // compact constructor
        if (x < 0 || y < 0) throw new IllegalArgumentException("Negative coords");
    }
}
class Test {
    public static void main(String[] args) {
        Point p = new Point(3, 4);
        System.out.println(p);
        System.out.println(p.x());
        System.out.println(p.y());
        System.out.println(p.equals(new Point(3, 4)));
    }
}
```

10. What will this code print?
```java
record Person(String name, int age) {
    String describe() { return name + " (" + age + ")"; }
}
class Test {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Bob", 25);
        System.out.println(p1.describe());
        System.out.println(p2.describe());
        System.out.println(p1.equals(new Person("Alice", 30)));
        System.out.println(p1.equals(p2));
    }
}
```

## Answers

1. A
2. A
3. B
4. B
5. A - All records extend java.lang.Record
6. True
7. True
8. True
9. Output:
```
Point[x=3, y=4]
3
4
true
```
10. Output:
```
Alice (30)
Bob (25)
true
false
```
