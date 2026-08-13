# Quiz: Access Modifiers

## Multiple Choice Questions

1. Which access modifier provides the most restrictive access?
   - A) public
   - B) protected
   - C) private
   - D) default (package-private)

2. What is the default access modifier in Java?
   - A) public
   - B) private
   - C) protected
   - D) No keyword (package-private)

3. Can a private member be accessed from a subclass?
   - A) Yes
   - B) No, never
   - C) Only if in the same package
   - D) Only with the protected keyword

4. Which access modifier allows access within the same package AND subclasses in other packages?
   - A) private
   - B) default
   - C) protected
   - D) public

5. What is the access level of a public class member?
   - A) Only within the class
   - B) Within the package
   - C) Within subclasses
   - D) Everywhere

## True/False Questions

6. A private constructor prevents instantiation outside the class.
   - True / False

7. Protected members can be accessed from any class in the same package.
   - True / False

8. The default access modifier is more restrictive than protected.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Account {
    private double balance = 1000;
    protected double getBalance() { return balance; }
    public void display() { System.out.println("Balance: " + getBalance()); }
}
class Savings extends Account {
    void check() {
        System.out.println("Direct: " + getBalance());
    }
}
class Test {
    public static void main(String[] args) {
        Account a = new Account();
        a.display();
        Savings s = new Savings();
        s.check();
    }
}
```

10. What will this code print?
```java
class Secret {
    private int code = 42;
    int defaultVal = 10;
    protected String hint = "hidden";
    public String reveal = "public";
}
class Test {
    public static void main(String[] args) {
        Secret s = new Secret();
        // System.out.println(s.code);      // Line A
        System.out.println(s.defaultVal);
        System.out.println(s.hint);
        System.out.println(s.reveal);
    }
}
```

## Answers

1. C
2. D
3. B - Private members are not inherited
4. C
5. D
6. True
7. True
8. True - Default restricts to package only; protected allows subclasses too
9. Output:
```
Balance: 1000.0
Direct: 1000.0
```
(Line A would cause compilation error - balance is private)

10. Output:
```
10
hidden
public
```
(Line A is commented out because `code` is private and inaccessible)
