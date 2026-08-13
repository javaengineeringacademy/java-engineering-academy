# Quiz: Encapsulation

## Multiple Choice Questions

1. What is encapsulation in OOP?
   - A) Hiding implementation details
   - B) Bundling data and methods together and restricting access
   - C) Creating multiple classes
   - D) Using inheritance

2. Which access modifier provides the highest level of encapsulation?
   - A) public
   - B) protected
   - C) private
   - D) default

3. What are getter methods used for?
   - A) To modify private fields
   - B) To access private fields
   - C) To create new objects
   - D) To call other methods

4. What are setter methods used for?
   - A) To read private fields
   - B) To modify private fields
   - C) To delete objects
   - D) To call constructors

5. Which of the following promotes encapsulation?
   - A) Making all fields public
   - B) Making fields private and providing public getters/setters
   - C) Not using any access modifiers
   - D) Using only static methods

## True/False Questions

6. Encapsulation improves code maintainability.
   - True / False

7. With encapsulation, you can change internal implementation without affecting external code.
   - True / False

8. Encapsulation eliminates the need for data validation.
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class BankAccount {
    private double balance;
    BankAccount(double initial) {
        balance = initial;
    }
    public double getBalance() {
        return balance;
    }
    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }
}
public class Main {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount(1000);
        acc.deposit(500);
        acc.deposit(-100);
        System.out.println("Balance: " + acc.getBalance());
    }
}
```

10. What is the output of the following code?
```java
class Person {
    private String name;
    private int age;
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setAge(int age) {
        if (age > 0 && age < 150) this.age = age;
    }
}
public class Main {
    public static void main(String[] args) {
        Person p = new Person("Alice", 25);
        p.setAge(30);
        p.setAge(-5);
        System.out.println(p.getName() + " is " + p.getAge());
    }
}
```

---

## Answers

1. B) Bundling data and methods together and restricting access
2. C) private
3. B) To access private fields
4. B) To modify private fields
5. B) Making fields private and providing public getters/setters
6. True
7. True
8. False (encapsulation allows adding validation in setters)
9. Balance: 1500.0
10. Alice is 30