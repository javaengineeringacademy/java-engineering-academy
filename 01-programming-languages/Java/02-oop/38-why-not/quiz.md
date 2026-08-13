# Quiz: Why Not (Common OOP Pitfalls)

## Multiple Choice Questions

1. Why should you avoid god classes?
   - A) They are too small
   - B) They violate single responsibility principle and become hard to maintain
   - C) They use too many interfaces
   - D) They have too many constructors

2. What is the problem with using `==` to compare Strings?
   - A) It's slower
   - B) It compares references, not content
   - C) It throws an exception
   - D) It only works with literals

3. Why is it bad to catch generic Exception?
   - A) It's syntactically incorrect
   - B) It masks specific errors and makes debugging harder
   - C) It causes memory leaks
   - D) It's not allowed in Java

4. Why should you avoid deep inheritance hierarchies?
   - A) They are faster
   - B) They create tight coupling and fragile base class problems
   - C) They use less memory
   - D) They improve readability

5. What is the problem with mutable shared state?
   - A) It improves performance
   - B) It causes unpredictable behavior in concurrent programs
   - C) It simplifies code
   - D) It reduces memory usage

## True/False Questions

6. Premature optimization is considered a good practice in OOP.
   - True / False

7. Returning null from methods can lead to NullPointerException and should be avoided when possible.
   - True / False

8. God objects that do everything make code easier to test.
   - True / False

## Code Output Questions

9. What is problematic about this code?
```java
class UserManager {
    void createUser(String name) { /* ... */ }
    void deleteUser(String name) { /* ... */ }
    void sendEmail(String to, String msg) { /* ... */ }
    void generateReport() { /* ... */ }
    void connectDatabase() { /* ... */ }
    void encryptData(byte[] data) { /* ... */ }
}
```
What OOP principle does this violate?
- A) Open/Closed
- B) Single Responsibility
- Liskov Substitution
- Interface Segregation

10. What will this code print?
```java
class Parent {
    void show() { System.out.println("Parent"); }
}
class Child extends Parent {
    @Override
    void show() { System.out.println("Child"); }
}
class Test {
    public static void main(String[] args) {
        Parent p = new Parent();
        Child c = new Child();
        p = c;     // p now points to Child
        c = (Child) p;  // Safe cast
        System.out.println("Safe");
        // Parent p2 = new Parent();
        // Child c2 = (Child) p2;  // This would throw ClassCastException
    }
}
```

## Answers

1. B
2. B
3. B
4. B
5. B
6. False - Premature optimization is a well-known anti-pattern
7. True - Consider Optional or sentinel values
8. False - Large classes are harder to test and maintain
9. B - Single Responsibility Principle - this class handles users, emails, reports, database, and encryption
10. Output:
```
Safe
```
The cast on line 6 is safe; the commented-out cast would throw ClassCastException.
