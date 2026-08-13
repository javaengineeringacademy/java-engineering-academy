# Quiz: Cloning

## Multiple Choice Questions

1. What does the Cloneable interface indicate?
   - A) The class supports cloning
   - B) The class is immutable
   - C) The class is thread-safe
   - D) The class implements Serializable

2. What does Object.clone() return by default?
   - A) A reference to the same object
   - B) A shallow copy of the object
   - C) A deep copy of the object
   - D) A null value

3. What exception does clone() throw if the class doesn't implement Cloneable?
   - A) IOException
   - B) CloneNotSupportedException
   - C) NullPointerException
   - D) ClassNotFoundException

4. What is the difference between shallow copy and deep copy?
   - A) Shallow copy clones nested objects; deep copy doesn't
   - B) Deep copy clones nested objects; shallow copy shares references
   - C) They are the same
   - D) Shallow copy is faster

5. To create a deep copy, what must you do?
   - A) Just call super.clone()
   - B) Manually clone each mutable field
   - C) Implement Serializable
   - D) Use the == operator

## True/False Questions

6. The clone() method is protected in the Object class.
   - True / False

7. A shallow copy creates independent copies of nested objects.
   - True / False

8. You can override clone() to change its access modifier to public.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Address implements Cloneable {
    String city;
    Address(String city) { this.city = city; }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
class Person implements Cloneable {
    String name;
    Address addr;
    Person(String name, Address addr) { this.name = name; this.addr = addr; }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
class Test {
    public static void main(String[] args) throws Exception {
        Address a = new Address("Mumbai");
        Person p1 = new Person("Alice", a);
        Person p2 = (Person) p1.clone();
        p2.addr.city = "Delhi";
        System.out.println(p1.addr.city);
        System.out.println(p2.addr.city);
        System.out.println(p1.addr == p2.addr);
    }
}
```

10. What will this code print?
```java
class Point implements Cloneable {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public String toString() { return "(" + x + "," + y + ")"; }
}
class Test {
    public static void main(String[] args) throws Exception {
        Point p1 = new Point(3, 4);
        Point p2 = (Point) p1.clone();
        p2.x = 10;
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p1 == p2);
    }
}
```

## Answers

1. A
2. B
3. B
4. B
5. B - Must manually clone each mutable reference type field
6. True
7. False - Shallow copy shares references to nested objects
8. True
9. Output:
```
Delhi
Delhi
true
```
The shallow copy shares the same Address reference, so changing p2's address also changes p1's.

10. Output:
```
(3,4)
(10,4)
false
```
Primitive fields are independently copied; the objects are different but share primitive values are copied.
