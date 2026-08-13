# Quiz: Value Objects

## Multiple Choice Questions

1. What is a value object?
   - A) An object that represents a value and is compared by its attributes, not identity
   - B) An object that holds a single primitive value
   - C) An object that can be null
   - D) An object with only static methods

2. Which characteristics define a value object?
   - A) Mutable, identity-based
   - B) Immutable, equality by value
   - C) Mutable, equality by reference
   - D) Static, no fields

3. What is the main benefit of value objects?
   - A) They save memory
   - B) They are predictable and easy to reason about
   - C) They are faster than primitives
   - D) They can be null

4. Which Java record keyword helps create value objects?
   - A) class
   - B) struct
   - C) record
   - D) value

5. What must value objects override for proper comparison?
   - A) toString()
   - B) equals() and hashCode()
   - C) clone()
   - D) compareTo()

## True/False Questions

6. Value objects should be mutable to allow updates.
   - True / False

7. Two value objects with the same attribute values should be considered equal.
   - True / False

8. Java records automatically create value objects with equals(), hashCode(), and toString().
   - True / False

## Code Output Questions

9. What will this code print?
```java
record Point(int x, int y) {}
class Test {
    public static void main(String[] args) {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(5, 6);
        System.out.println(p1.equals(p2));
        System.out.println(p1.equals(p3));
        System.out.println(p1 == p2);
        System.out.println(p1);
    }
}
```

10. What will this code print?
```java
record Money(double amount, String currency) {
    Money add(Money other) {
        if (!this.currency.equals(other.currency))
            throw new RuntimeException("Currency mismatch");
        return new Money(this.amount + other.amount, this.currency);
    }
}
class Test {
    public static void main(String[] args) {
        Money a = new Money(10.0, "USD");
        Money b = new Money(5.0, "USD");
        Money c = a.add(b);
        System.out.println(c);
        System.out.println(c.equals(new Money(15.0, "USD")));
        System.out.println(a.equals(c));
    }
}
```

## Answers

1. A
2. B
3. B
4. C
5. B
6. False - Value objects should be immutable
7. True
8. True
9. Output:
```
true
false
false
Point[x=3, y=4]
```
10. Output:
```
Money[amount=15.0, currency=USD]
true
false
```
