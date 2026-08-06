# OOP Concepts Quiz

## Question 1 (MCQ)
Which OOP principle wraps data and methods together, hiding internal state?
- A) Inheritance
- B) Polymorphism
- C) Encapsulation
- D) Abstraction

**Answer: C**
**Explanation:** Encapsulation bundles data (fields) and methods into a single unit (class) and restricts direct access to fields via access modifiers, providing public getters/setters for controlled access.

---

## Question 2 (MCQ)
What is the key difference between an abstract class and an interface in Java?
- A) Abstract classes can have constructors; interfaces cannot
- B) Interfaces can have instance variables; abstract classes cannot
- C) Both can have constructors
- D) There is no difference

**Answer: A**
**Explanation:** Abstract classes can have constructors (called via `super()` from subclasses), instance variables, and non-abstract methods. Interfaces cannot have constructors (prior to Java 8 default methods).

---

## Question 3 (MCQ)
Which access modifier provides the most restriction?
- A) public
- B) protected
- C) default (package-private)
- D) private

**Answer: D**
**Explanation:** `private` restricts access to within the same class only. `default` allows package access, `protected` allows subclass access, and `public` allows global access.

---

## Question 4 (MCQ)
What is the benefit of programming to an interface rather than a concrete class?
- A) Faster execution
- B) Reduced memory usage
- C) Flexibility to swap implementations without changing client code
- D) Automatic garbage collection

**Answer: C**
**Explanation:** Programming to an interface decouples the client from concrete implementations, making it easy to swap implementations (e.g., switching from ArrayList to LinkedList) without modifying client code.

---

## Question 5 (Code Output)
What does this code print?

```java
class Animal {
    public void speak() {
        System.out.println("Animal speaks");
    }
}

class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Dog barks");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal animal = new Dog();
        animal.speak();
    }
}
```

**Answer:** Dog barks
**Explanation:** Even though the reference type is `Animal`, the actual object is `Dog`. Due to dynamic method dispatch (runtime polymorphism), the overridden `speak()` method in `Dog` is called.

---

## Question 6 (Code Output)
What does this code print?

```java
public class Main {
    static void process(Animal a) {
        a.speak();
    }

    public static void main(String[] args) {
        Animal a1 = new Animal();
        Animal a2 = new Dog();
        process(a1);
        process(a2);
    }
}

class Animal {
    void speak() { System.out.print("Animal "); }
}

class Dog extends Animal {
    void speak() { System.out.print("Dog "); }
}
```

**Answer:** Animal Dog
**Explanation:** Method overriding with dynamic dispatch: `a1` is an Animal object, so `Animal.speak()` is called. `a2` is a Dog object, so `Dog.speak()` is called even though the parameter type is `Animal`.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

**Bug:** The `withdraw()` method does not check if the account has sufficient funds, allowing negative balances.
**Fix:** Add a validation check:
```java
public boolean withdraw(double amount) {
    if (amount > 0 && amount <= balance) {
        balance -= amount;
        return true;
    }
    return false;
}
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public boolean equals(Person other) {
        return this.name.equals(other.name) && this.age == other.age;
    }
}
```

**Bug:** The `equals()` method overrides `Object.equals()` incorrectly — it has parameter type `Person` instead of `Object`, so it overloads rather than overrides. Also, `hashCode()` is not overridden, violating the contract.
**Fix:** Use `@Override` annotation and correct signature:
```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Person)) return false;
    Person other = (Person) obj;
    return this.name.equals(other.name) && this.age == other.age;
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

---

## Question 9 (Scenario-based)
You are designing a system for different payment processors (CreditCard, PayPal, BankTransfer). Each processor has its own validation and processing logic. Which approach is best?

- A) Create a single PaymentProcessor class with if-else for each type
- B) Define a PaymentProcessor interface with process() method, implement separately for each type
- C) Use an abstract class with all methods concrete
- D) Store payment logic in a properties file

**Answer: B**
**Explanation:** Using an interface allows each payment processor to implement its own logic independently. This follows the Open/Closed Principle — new payment types can be added without modifying existing code. It also enables dependency injection and testability.

---

## Question 10 (Architecture Decision)
You need to design a notification system that sends alerts via Email, SMS, and Push. New notification channels may be added in the future. How should you architect this?

- A) Create a single `NotificationService` with methods `sendEmail()`, `sendSms()`, `sendPush()`
- B) Define a `NotificationChannel` interface with `send()`, implement `EmailChannel`, `SmsChannel`, `PushChannel`
- C) Create a `Notification` abstract class with concrete send methods
- D) Use static methods for each channel type

**Answer: B**
**Explanation:** The Strategy pattern with an interface makes the system extensible. Adding a new channel (e.g., Slack) requires only implementing the interface. The main service depends on the abstraction, not concrete classes, following the Dependency Inversion Principle.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
class Parent {
    Parent() { System.out.print("Parent "); }
    Parent(String s) { System.out.print("Parent-" + s + " "); }
}

class Child extends Parent {
    Child() { super("Hello"); System.out.print("Child "); }
    Child(String s) { this(); System.out.print("Child-" + s + " "); }
}

public class Main {
    public static void main(String[] args) {
        new Child("World");
    }
}
```

A) Parent-Hello Child Child-World
B) Parent Child Parent-Hello Child-World
C) Parent-Hello Child-World
D) Child Parent-Hello Child-World

**Answer: A**
**Explanation:** `new Child("World")` calls `Child(String s)`, which calls `this()` (Child()). Child() calls `super("Hello")` which prints "Parent-Hello ". Then Child() prints "Child ". Back in Child(String s), after this() completes, it prints "Child-World ". Output: `Parent-Hello Child Child-World`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
class Base {
    void display() { System.out.print("Base "); }
}

class Derived extends Base {
    void display() { System.out.print("Derived "); }
    void display(int x) { System.out.print("Derived-" + x + " "); }
}

public class Main {
    public static void main(String[] args) {
        Base obj = new Derived();
        obj.display();
        obj.display(5);
    }
}
```

A) Derived Derived-5
B) Base Derived-5
C) Derived Base-5
D) Compilation error at obj.display(5)

**Answer: D**
**Explanation:** `obj` is declared as type `Base`. Method overriding resolves at runtime (polymorphic), so `obj.display()` calls Derived's display() — prints "Derived ". However, `display(int)` is not defined in Base, so it's a compile-time error. Overloading is resolved at compile time based on reference type, and Base has no `display(int)` method.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
abstract class Animal {
    Animal() { System.out.print("Animal "); }
    abstract void speak();
    void breathe() { System.out.print("Breathing "); }
}

class Cat extends Animal {
    Cat() { System.out.print("Cat "); }
    void speak() { System.out.print("Meow "); }
}

public class Main {
    public static void main(String[] args) {
        Animal a = new Cat();
        a.speak();
        a.breathe();
    }
}
```

A) Animal Cat Meow Breathing
B) Cat Meow Breathing
C) Animal Cat Meow
D) Compilation error - cannot instantiate abstract class

**Answer: A**
**Explanation:** `new Cat()` calls Cat() constructor, which implicitly calls super() (Animal()), printing "Animal ". Then Cat() prints "Cat ". `a.speak()` calls Cat's speak(), printing "Meow ". `a.breathe()` calls Animal's breathe() (not overridden), printing "Breathing ". Output: `Animal Cat Meow Breathing`.
