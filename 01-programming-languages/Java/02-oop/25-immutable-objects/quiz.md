# Quiz: Immutable Objects

## Multiple Choice Questions

1. What makes an object immutable?
   - A) Its state cannot change after construction
   - B) It has no methods
   - C) It is declared as static
   - D) It implements Cloneable

2. Which keyword is commonly used for immutable class fields?
   - A) volatile
   - B) final
   - C) transient
   - D) synchronized

3. What should you do with mutable fields in an immutable class?
   - A) Make them public
   - B) Return direct references to them
   - C) Return defensive copies
   - D) Make them static

4. Which Java class is an example of an immutable object?
   - A) StringBuilder
   - B) String
   - C) ArrayList
   - D) HashMap

5. What is a benefit of immutable objects?
   - A) They use less memory
   - B) They are inherently thread-safe
   - C) They are faster to create
   - D) They can be modified

## True/False Questions

6. All fields in an immutable class must be final.
   - True / False

7. An immutable class can have a non-final field if it's private and never changed.
   - True / False

8. Immutable objects are safe to share across threads without synchronization.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Money {
    private final double amount;
    private final String currency;
    Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    double getAmount() { return amount; }
    String getCurrency() { return currency; }
    Money add(Money other) {
        if (!this.currency.equals(other.currency))
            throw new RuntimeException("Different currencies");
        return new Money(this.amount + other.amount, this.currency);
    }
    public String toString() { return amount + " " + currency; }
}
class Test {
    public static void main(String[] args) {
        Money m1 = new Money(100, "USD");
        Money m2 = new Money(50, "USD");
        Money m3 = m1.add(m2);
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);
    }
}
```

10. What will this code print?
```java
import java.util.*;
class Config {
    private final String host;
    private final int port;
    Config(String host, int port) { this.host = host; this.port = port; }
    String getHost() { return host; }
    int getPort() { return port; }
    public String toString() { return host + ":" + port; }
}
class Test {
    public static void main(String[] args) {
        Config c = new Config("localhost", 8080);
        List<Config> configs = new ArrayList<>();
        configs.add(c);
        System.out.println(configs.get(0));
        System.out.println(c.getHost());
    }
}
```

## Answers

1. A
2. B
3. C - Return defensive copies to prevent external modification
4. B - String is immutable in Java
5. B - No mutable state means no thread-safety issues
6. True - Essential for immutability
7. True - If the field is never reassigned after construction
8. True
9. Output:
```
100.0 USD
50.0 USD
150.0 USD
```
m1 and m2 remain unchanged; m3 is a new object.

10. Output:
```
localhost:8080
localhost
```
