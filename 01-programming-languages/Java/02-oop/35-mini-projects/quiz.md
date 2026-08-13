# Quiz: Mini Projects (OOP Concepts Applied)

## Multiple Choice Questions

1. In a mini project applying OOP, what is the first step?
   - A) Write the main method
   - B) Identify classes, attributes, and behaviors from the problem
   - C) Create all constructors
   - D) Define all getters and setters

2. Which OOP concept is used when a Library contains Books?
   - A) Inheritance
   - B) Composition
   - C) Polymorphism
   - D) Encapsulation

3. When designing a shape hierarchy (Circle, Rectangle), which concept is best?
   - A) Composition
   - B) Association
   - C) Inheritance with polymorphism
   - D) Dependency

4. In a banking mini project, what should Account class use to ensure data integrity?
   - A) Public fields
   - B) Private fields with encapsulation
   - C) Static fields only
   - D) No fields

5. What design principle suggests "program to an interface, not an implementation"?
   - A) Single Responsibility
   - B) Open/Closed
   - C) Liskov Substitution
   - D) Interface Segregation

## True/False Questions

6. A well-designed mini project should follow SOLID principles.
   - True / False

7. In a mini project, you should always use inheritance over composition.
   - True / False

8. Testing individual classes in isolation is called unit testing.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class BankAccount {
    private String id;
    private double balance;
    BankAccount(String id, double balance) { this.id = id; this.balance = balance; }
    void deposit(double amt) { balance += amt; }
    void withdraw(double amt) { if (amt <= balance) balance -= amt; }
    double getBalance() { return balance; }
    String getId() { return id; }
}
class Test {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount("A001", 1000);
        acc.deposit(500);
        acc.withdraw(200);
        System.out.println("Balance: " + acc.getBalance());
        acc.withdraw(2000);
        System.out.println("Balance after overdraft: " + acc.getBalance());
    }
}
```

10. What will this code print?
```java
abstract class Shape {
    abstract double area();
}
class Circle extends Shape {
    double r;
    Circle(double r) { this.r = r; }
    double area() { return Math.PI * r * r; }
}
class Rectangle extends Shape {
    double w, h;
    Rectangle(double w, double h) { this.w = w; this.h = h; }
    double area() { return w * h; }
}
class Test {
    public static void main(String[] args) {
        Shape[] shapes = { new Circle(5), new Rectangle(4, 6) };
        double total = 0;
        for (Shape s : shapes) total += s.area();
        System.out.printf("Total area: %.2f%n", total);
    }
}
```

## Answers

1. B
2. B
3. C
4. B
5. B - Though all SOLID principles apply to mini projects
6. True
7. False - Composition is generally preferred ("has-a" over "is-a")
8. True
9. Output:
```
Balance: 1300.0
Balance after overdraft: 1300.0
```
10. Output:
```
Total area: 103.04
```
(Circle: PI*25 ≈ 78.54, Rectangle: 24, Total ≈ 103.04)
