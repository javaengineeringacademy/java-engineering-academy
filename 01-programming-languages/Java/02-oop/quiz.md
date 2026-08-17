# OOP Concepts Quiz

## Question 1 (Production Scenario)
Your team is designing a payment processing system. You need to support CreditCard, PayPal, and BankTransfer. Each has its own validation and processing logic. A new payment method (Crypto) must be added next quarter without modifying existing code. Which design approach should you choose?

- A) Create a single `PaymentProcessor` class with if-else for each type
- B) Define a `PaymentProcessor` interface with `process()`, implement separately for each type
- C) Use an abstract class with all methods concrete
- D) Store payment logic in a properties file

**Answer: B**
**Explanation:** Using an interface allows each payment processor to implement its own logic independently. This follows the Open/Closed Principle — new payment types can be added without modifying existing code. It also enables dependency injection, easier testing, and runtime swapping of implementations.

---

## Question 2 (Production Scenario)
You are building a notification system that sends alerts via Email, SMS, and Push. New notification channels may be added in the future. The system must send 1 million notifications per hour. How should you architect this?

- A) Create a single `NotificationService` with methods `sendEmail()`, `sendSms()`, `sendPush()`
- B) Define a `NotificationChannel` interface with `send()`, implement `EmailChannel`, `SmsChannel`, `PushChannel`
- C) Create a `Notification` abstract class with concrete send methods
- D) Use static methods for each channel type

**Answer: B**
**Explanation:** The Strategy pattern with an interface makes the system extensible. Adding a new channel (e.g., Slack) requires only implementing the interface. The main service depends on the abstraction, not concrete classes, following the Dependency Inversion Principle. This also allows independent testing and scaling of each channel.

---

## Question 3 (Debugging)
A junior developer creates a `Person` class with an `equals()` method. Users report that `HashSet<Person>` contains duplicate entries. The code is:

```java
public class Person {
    private String name;
    private int age;
    
    public boolean equals(Person other) {
        return this.name.equals(other.name) && this.age == other.age;
    }
}
```

What is the bug?

- A) The `equals()` method should use `==` for String comparison
- B) The `equals()` method has the wrong signature — it overloads instead of overrides `Object.equals(Object)`
- C) The class needs to implement `Comparable`
- D) HashSet doesn't support custom objects

**Answer: B**
**Explanation:** The method `equals(Person)` doesn't override `equals(Object)` from Object. It creates a new overloaded method. HashSet uses `equals(Object)` to check for duplicates, so it falls back to reference equality. The fix: override `equals(Object obj)` and also override `hashCode()` to maintain the contract.

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

## Question 5 (Production Scenario)
Your application has a `Shape` class hierarchy with `Circle`, `Rectangle`, and `Triangle`. A new requirement needs to add a `draw()` method that behaves differently for each shape. The system processes 10,000 shapes per second. Which approach is most appropriate?

- A) Use `instanceof` checks in a single `draw(Shape s)` method
- B) Add abstract `draw()` to `Shape` and override in each subclass
- C) Create separate `drawCircle()`, `drawRectangle()` methods in a manager class
- D) Use a `switch` on shape type string

**Answer: B**
**Explanation:** An abstract `draw()` method uses polymorphism — the JVM resolves the correct implementation at runtime via dynamic dispatch. This is O(1) per call, maintains the Open/Closed Principle (adding a new shape doesn't modify existing code), and keeps drawing logic co-located with the shape data.

---

## Question 6 (Debugging)
A banking application has two threads transferring money between accounts. After running overnight, account balances are incorrect. The code uses:

```java
public void transfer(Account from, Account to, double amount) {
    from.debit(amount);
    to.credit(amount);
}
```

What is the bug and how do you fix it?

- A) Use `synchronized` on the method to ensure atomicity
- B) The method should return a boolean instead of void
- C) Use `volatile` on the amount parameter
- D) Add a `Thread.sleep()` between operations

**Answer: A**
**Explanation:** Without synchronization, Thread A can debit from account1 while Thread B is also debiting from account1, causing lost updates. The `synchronized` keyword ensures only one thread can execute the transfer at a time per account instance. For better performance, consider using `ReentrantLock` with fine-grained locking or optimistic locking with version numbers.

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

## Question 9 (Constructor Chaining)
A junior developer writes the following code and it fails to compile:

```java
class Animal {
    private String name;
    Animal(String name) { this.name = name; }
    String getName() { return name; }
}

class Dog extends Animal {
    private String breed;
    Dog(String name, String breed) { this.breed = breed; }
}
```

What is the bug and how do you fix it?

- A) The `Dog` class needs a no-arg constructor
- B) The `Dog` constructor must call `super(name)` as its first statement
- C) The `Animal` class needs a default constructor
- D) The `Dog` class cannot extend `Animal` because `Animal` has a parameterized constructor

**Answer: B**
**Explanation:** When a subclass constructor does not explicitly call `super()` or `this()`, Java inserts an implicit `super()` (no-arg). Since `Animal` has no no-arg constructor, this fails. The fix: `Dog(String name, String breed) { super(name); this.breed = breed; }`. A parent constructor must always be called before the child can access `this`.

---

## Question 10 (Interface Segregation)
You are designing a file processing system. You create one interface:

```java
interface FileProcessor {
    void read();
    void write();
    void compress();
    void encrypt();
    void delete();
}
```

A `ReadOnlyReader` class must implement this interface but only needs `read()`. What problem does this violate and how do you fix it?

- A) Single Responsibility Principle — split into separate classes
- B) Interface Segregation Principle — split the fat interface into smaller, focused interfaces
- C) Liskov Substitution Principle — use an abstract class instead
- D) Dependency Inversion Principle — depend on a concrete class

**Answer: B**
**Explanation:** The Interface Segregation Principle (ISP) states that clients should not be forced to depend on methods they don't use. The `ReadOnlyReader` is forced to implement `write()`, `compress()`, `encrypt()`, and `delete()` it never uses. The fix: split into `Readable`, `Writable`, `Compressible`, `Encryptable`, `Deletable` interfaces. Classes implement only what they need.

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
