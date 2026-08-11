# Quiz: The throws Declaration

## Questions

### Q1: What is the purpose of the `throws` keyword in a method declaration?
**Answer:** B) To declare which exceptions a method might propagate to its caller — `throws` is a contract between the method and its callers about which checked exceptions can occur.

### Q2: Which exception type REQUIRES a `throws` declaration?
**Answer:** A) Checked exceptions (e.g., IOException) — The compiler enforces that checked exceptions are declared or handled.

### Q3: What happens if you remove `throws IOException` from this method signature?
```java
public String read(String path) throws IOException {
    return new String(new FileInputStream(path).readAllBytes());
}
```
**Answer:** B) The code will not compile — FileInputStream constructor and readAllBytes throw checked IOException, which must be declared.

### Q4: Is this code valid?
```java
public void validate(String input) throws IllegalArgumentException {
    if (input == null) throw new IllegalArgumentException("null");
}
```
**Answer:** A) Yes — You CAN declare unchecked exceptions, though it is rarely necessary.

### Q5: What is exception translation?
**Answer:** B) Catching a low-level exception and rethrowing it as a higher-level domain exception — This preserves the root cause while presenting a cleaner API to callers.

### Q6: What is wrong with this declaration?
```java
public void process(String input) throws Exception {
    // ...
}
```
**Answer:** C) It declares the most generic exception type, giving callers no useful information about specific failure modes.

### Q7: What happens when you add a checked exception to an interface method after the interface is published?
**Answer:** B) All existing implementations will fail to compile — This is a breaking change to the interface contract.

### Q8: Which is the correct way to handle InterruptedException?
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // ???
}
```
**Answer:** A) Restore the interrupt status with `Thread.currentThread().interrupt()` — Ignoring the interrupt breaks the thread interruption contract.

### Q9: What is the difference between declaring `throws IOException` vs `throws FileNotFoundException`?
**Answer:** A) `IOException` covers all IO subtypes; `FileNotFoundException` is more specific — Declaring the specific type gives callers more precise information.

### Q10: When should you NOT declare a checked exception in `throws`?
**Answer:** B) When the exception represents a programming error (use RuntimeException instead) — Checked exceptions should represent recoverable conditions, not bugs.
