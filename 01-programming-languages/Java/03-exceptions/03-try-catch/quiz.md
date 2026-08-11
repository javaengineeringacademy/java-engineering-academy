# Quiz — Try-Catch Exception Handling

---

### Q1: What happens if an exception is thrown inside a try block and no matching catch block exists?
A) The program crashes immediately
B) The exception is silently ignored
C) The exception propagates up the call stack
D) The finally block is skipped

**Answer:** C

---

### Q2: Which of the following is the correct syntax for multi-catch in Java 7+?
A) `catch (IOException, SQLException e)`
B) `catch (IOException | SQLException e)`
C) `catch (IOException and SQLException e)`
D) `catch (IOException || SQLException e)`

**Answer:** B

---

### Q3: In multi-catch, what happens if one exception type is a subclass of another?
A) It works fine — subclasses are caught first
B) It causes a compile error
C) The catch block catches only the parent type
D) The JVM ignores the subclass

**Answer:** B

---

### Q4: What is the output of this code?

```java
try {
    System.out.println("A");
    int x = 10 / 0;
    System.out.println("B");
} catch (ArithmeticException e) {
    System.out.println("C");
}
System.out.println("D");
```

A) A B D
B) A C D
C) A B C D
D) A C

**Answer:** B

---

### Q5: Why should catch blocks be ordered from most specific to most general?
A) Java requires it by language specification
B) A general catch block makes specific catch blocks unreachable
C) It improves runtime performance
D) It's only a coding convention, not required

**Answer:** B

---

### Q6: What is the catch parameter in a multi-catch block?
A) Mutable — you can reassign it
B) Effectively final — you cannot reassign it
C) Always null
D) An array of exceptions

**Answer:** B

---

### Q7: What happens to remaining try statements after an exception is thrown?
A) They execute normally
B) They are skipped and control goes to the matching catch block
C) They execute in reverse order
D) They execute only if there is a finally block

**Answer:** B

---

### Q8: Which JDK version introduced multi-catch syntax?
A) JDK 1.0
B) JDK 5
C) JDK 7
D) JDK 8

**Answer:** C

---

### Q9: What is the output of this code?

```java
try {
    String s = null;
    s.length();
} catch (NullPointerException e) {
    System.out.println("caught");
} finally {
    System.out.println("finally");
}
```

A) caught
B) finally
C) caught finally
D) NullPointerException

**Answer:** C

---

### Q10: Which of the following is a reason NOT to catch `Exception` directly?
A) It's not a valid exception type
B) It catches too broadly, including RuntimeExceptions you should fix
C) It causes a compile error
D) Java doesn't allow catching checked exceptions

**Answer:** B

---

### Q11: What does rethrowing an exception mean?
A) Creating a new exception and throwing it
B) Catching an exception and throwing the same one up the call stack
C) Ignoring the exception
D) Converting a checked exception to a runtime exception

**Answer:** B

---

### Q12: In Java 7+, what happens to the type of a rethrown exception from a multi-catch block?
A) It becomes Exception
B) It becomes the most specific common supertype
C) It becomes RuntimeException
D) The compiler loses type information

**Answer:** B

---

### Q13: What is the output of this nested try-catch?

```java
try {
    try {
        throw new IOException("inner");
    } catch (IOException e) {
        System.out.println("inner catch");
        throw e;
    }
} catch (IOException e) {
    System.out.println("outer catch");
}
```

A) inner catch
B) outer catch
C) inner catch outer catch
D) Compilation error

**Answer:** C

---

### Q14: Why is using exceptions for control flow considered bad practice?
A) It's illegal in Java
B) Exceptions are expensive — creating stack traces takes time
C) It causes memory leaks
D) It only works in multi-threaded code

**Answer:** B

---

### Q15: Which of the following is true about multi-catch compared to multiple catch blocks?
A) Multi-catch is faster at runtime
B) Multi-catch allows different exception types with different handlers
C) Multi-catch reduces code duplication when handling is the same
D) Multi-catch can catch related exception types (parent-child)

**Answer:** C
