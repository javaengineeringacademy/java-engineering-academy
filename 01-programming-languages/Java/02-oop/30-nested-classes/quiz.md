# Quiz: Nested Classes

## Multiple Choice Questions

1. How many types of nested classes are there in Java?
   - A) 2
   - B) 3
   - C) 4
   - D) 5

2. Which nested class does NOT have access to non-static members of the outer class?
   - A) Static nested class
   - B) Non-static inner class
   - C) Both
   - D) Neither

3. Which syntax is correct for creating a static nested class instance?
   - A) new Outer.Inner()
   - B) new Outer.Inner()
   - C) new Outer().new Inner()
   - D) Outer.Inner.new()

4. What is the main difference between a static nested class and an inner class?
   - A) Static nested class has access to outer instance members
   - B) Inner class does not need an outer instance
   - C) Static nested class does not need an outer instance
   - D) They are identical

5. Where can you define a nested class?
   - A) Only inside a method
   - B) Only at class level
   - C) Inside another class or inside a method
   - D) Only in interfaces

## True/False Questions

6. A static nested class can access private static members of the outer class.
   - True / False

7. You can have a nested class inside an interface.
   - True / False

8. Nested classes increase encapsulation by keeping related classes together.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Outer {
    static int count = 0;
    Outer() { count++; }
    static class Nested {
        int getCount() { return count; }
    }
}
class Test {
    public static void main(String[] args) {
        new Outer();
        new Outer();
        Outer.Nested n = new Outer.Nested();
        System.out.println("Count: " + n.getCount());
    }
}
```

10. What will this code print?
```java
class Factory {
    static class Product {
        private String name;
        Product(String name) { this.name = name; }
        String getName() { return name; }
    }
    static Product create(String name) { return new Product(name); }
}
class Test {
    public static void main(String[] args) {
        Factory.Product p1 = Factory.create("Widget");
        Factory.Product p2 = new Factory.Product("Gadget");
        System.out.println(p1.getName());
        System.out.println(p2.getName());
    }
}
```

## Answers

1. C - Inner, static nested, local, anonymous
2. A
3. B
4. C
5. C
6. True
7. True - They are implicitly static
8. True
9. Output:
```
Count: 2
```
10. Output:
```
Widget
Gadget
```
