# OOP Best Practices

## Core Principles

| Principle | Guideline |
|-----------|-----------|
| **Favor composition** | Over inheritance |
| **Program to interfaces** | Not implementations |
| **Prefer immutability** | Use `final`, records |
| **Single Responsibility** | One reason to change |
| **Open/Closed** | Extend, don't modify |
| **Liskov Substitution** | Subtypes substitutable |
| **Interface Segregation** | Many small interfaces |
| **Dependency Inversion** | Depend on abstractions |

## Design Guidelines

### Class Design
```java
// ✓ Good: Immutable, final, validated
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }
}

// ✗ Bad: Mutable, no validation, public fields
class Money {
    public BigDecimal amount;
    public String currency;
}
```

### Method Design
```java
// ✓ Good: Clear, single purpose, documented
/**
 * Calculates compound interest
 * @param principal initial amount
 * @param rate annual rate (e.g., 0.05 for 5%)
 * @param years number of years
 * @return future value
 */
public BigDecimal calculateCompoundInterest(BigDecimal principal, BigDecimal rate, int years) {
    // ...
}
```

### Overloading
```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public int add(int... numbers) { return Arrays.stream(numbers).sum(); }
}
```

### Varargs
```java
public void printAll(String... items) {
    for (String item : items) System.out.println(item);
}

// Called as: printAll("a", "b", "c") or printAll(new String[]{"a", "b"})
```

## Code Organization

### Package Structure
```
src/
└── main/
    └── java/
        └── com/
            └── company/
                ├── domain/          # Core business logic
                │   ├── model/       # Entities, value objects
                │   ├── service/     # Domain services
                │   └── repository/  # Repository interfaces
                ├── application/     # Use cases, commands
                │   ├── command/
                │   └── query/
                └── infrastructure/  # External concerns
                    ├── persistence/
                    ├── messaging/
                    └── config/
```

## Code Quality

### Checklist
- [ ] All classes compile without warnings
- [ ] Checkstyle passes (Google style)
- [ ] SpotBugs clean
- [ ] PMD clean
- [ ] Tests pass with 80%+ coverage
- [ ] Javadoc for all public APIs
- [ ] No TODOs in production code
- [ ] Immutable where possible
- [ ] No raw types, use generics
- [ ] No raw exceptions, use custom exceptions

## Naming Conventions

| Element | Convention |
|---------|------------|
| Classes | PascalCase (`UserService`) |
| Interfaces | PascalCase (`UserRepository`) |
| Methods | camelCase (`getUserById`) |
| Constants | UPPER_SNAKE (`MAX_RETRIES`) |
| Packages | lowercase (`com.company.module`) |
| Type Parameters | Single uppercase (`T`, `E`, `K`, `V`) |

## Testing Standards

### Test Coverage Targets
| Layer | Minimum |
|-------|---------|
| Domain | 90% |
| Service | 85% |
| Controller | 80% |
| Repository | 80% (integration) |

### Test Structure
```java
class UserServiceTest {
    @Test
    void shouldCalculateDiscount_whenUserIsPremium() {
        // Given
        User user = new User("john", UserType.PREMIUM);
        Order order = new Order(BigDecimal.valueOf(100));
        
        // When
        BigDecimal discount = service.calculateDiscount(user, order);
        
        // Then
        assertThat(discount).isEqualByComparingTo("10.00");
    }
}
```

## Pro Tips for Interviews

1. **Always clarify:** "May I assume input is valid?" "What's the expected input size?"
2. **Think aloud:** Explain your approach before coding
3. **Edge cases:** Empty, null, single element, duplicates, overflow
4. **Complexity:** State time/space complexity
5. **Trade-offs:** "ArrayList is faster for random access but LinkedList for frequent insertions"

---

## Quick Reference Card

| Concept | Key Point |
|---------|-----------|
| `main` signature | `public static void main(String[] args)` |
| Integer division | `10/3 = 3` (not 3.33) |
| String comparison | `.equals()` not `==` |
| String mutability | Immutable (use StringBuilder) |
| Pass-by-value | Always (references passed by value) |
| Switch expression | Returns value, no fall-through |
| Varargs | `type...` last parameter only |
| Default char | `'\u0000'` |
| Default boolean | `false` |
| Default char | `'\u0000'` |

---

## Further Reading

### Official Documentation
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se21/html/jls-21.html)
- [Java Tutorials - Oracle](https://docs.oracle.com/javase/tutorial/)
- [OpenJDK 21 Documentation](https://openjdk.org/projects/jdk/21/)

### Books
- *Effective Java* (3rd Ed.) — Joshua Bloch — Items 1-9
- *Java: The Complete Reference* — Herbert Schildt — Ch. 1-6
- *Head First Java* — Sierra & Bates — Ch. 1-5

### Articles & Blogs
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)
- [JVM Internals](https://blog.codefx.org/java/jvm/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

### Practice Platforms
- [LeetCode](https://leetcode.com/tag/java/) - Easy/Medium/Hard
- [HackerRank](https://www.hackerrank.com/domains/tutorials/10-days-of-java)
- [Codewars](https://www.codewars.com/kata/search/java?q=&r%5B%5D=-7&beta=false)
- [Exercism](https://exercism.org/tracks/java)

---

## Quick Reference Card

| Concept | Key Point |
|---------|-----------|
| `main` signature | `public static void main(String[] args)` |
| Integer division | `10/3 = 3` (not 3.33) |
| String comparison | `.equals()` not `==` |
| String mutability | Immutable (use StringBuilder) |
| Pass-by-value | Always (references passed by value) |
| Switch expression | Returns value, no fall-through |
| Varargs | `type...` last parameter only |
| Default char | `'\u0000'` |
| Default boolean | `false` |
| Default char | `'\u0000'` |

---

*Keep this handy during development and interviews!*