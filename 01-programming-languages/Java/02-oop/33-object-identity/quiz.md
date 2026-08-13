# Quiz: Object Identity

## Multiple Choice Questions

1. What does the == operator check for objects?
   - A) If two references point to the same object in memory
   - B) If two objects have equal values
   - C) If two objects have the same type
   - D) If two objects are null

2. What method should be overridden to compare object content?
   - A) == operator
   - B) compareTo()
   - C) equals()
   - D) hashCode()

3. What is the default behavior of Object.equals()?
   - A) Compares field values
   - B) Compares memory addresses (same as ==)
   - C) Always returns true
   - D) Throws an exception

4. If you override equals(), what else should you override?
   - A) toString()
   - B) hashCode()
   - C) clone()
   - D) finalize()

5. What does System.identityHashCode() return?
   - A) The value of hashCode()
   - B) A unique ID based on memory address
   - C) The object's toString()
   - D) null for all objects

## True/False Questions

6. Two objects that are equal via equals() must have the same hashCode().
   - True / False

7. == compares the content of two String objects.
   - True / False

8. Every object in Java has a unique identity, even if its content is equal to another object.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Person {
    String name;
    Person(String name) { this.name = name; }
}
class Test {
    public static void main(String[] args) {
        Person p1 = new Person("Alice");
        Person p2 = new Person("Alice");
        Person p3 = p1;
        System.out.println(p1 == p2);
        System.out.println(p1 == p3);
        System.out.println(p1.equals(p2));
    }
}
```

10. What will this code print?
```java
class Item {
    String name;
    Item(String name) { this.name = name; }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return name.equals(((Item) o).name);
    }
    public int hashCode() { return name.hashCode(); }
}
class Test {
    public static void main(String[] args) {
        Item a = new Item("Book");
        Item b = new Item("Book");
        Item c = new Item("Pen");
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a.equals(c));
        System.out.println(a.hashCode() == b.hashCode());
    }
}
```

## Answers

1. A
2. C
3. B - Default equals() is equivalent to ==
4. B - Contract requires equal objects to have equal hash codes
5. B
6. True
7. False - == checks reference equality; use .equals() for content
8. True
9. Output:
```
false
true
false
```
10. Output:
```
false
true
false
true
```
