# Sprint 2 Interview Questions - Object Oriented Programming

---

## 📋 Core OOP Concepts

### Q1: What are the four pillars of OOP?
**Answer:** Encapsulation, Inheritance, Polymorphism, Abstraction

**Follow-up:** Explain each with a real-world example.
- **Encapsulation**: Pill bottle - you interact with the cap (interface), not the pills directly
- **Inheritance**: Vehicle → Car → ElectricCar (is-a relationship)
- **Polymorphism**: Same "drive()" method behaves differently for Car vs Motorcycle
- **Abstraction**: Steering wheel hides complex engine mechanics

---

### Q2: Difference between abstract class and interface?
| Aspect | Abstract Class | Interface |
|--------|----------------|-----------|
| Multiple inheritance | No (single) | Yes (multiple) |
| Fields | Instance + constants | Constants only |
| Constructors | Yes | No |
| Default methods | N/A | Yes (Java 8+) |
| Use case | Shared code + contract | Pure contract |

**Key insight:** Use abstract class when classes share code; use interface for pure contract.

---

### Q3: What is method overloading vs overriding?
| Aspect | Overloading | Overriding |
|--------|-------------|------------|
| Signature | Different params | Same signature |
| Return type | Can differ | Covariant only |
| Binding | Compile-time (static) | Runtime (dynamic) |
| Scope | Same class | Subclass vs parent |
| Polymorphism | Compile-time | Runtime |

---

### Q4: Explain pass-by-value in Java.
**Answer:** Java is ALWAYS pass-by-value.
- Primitives: Copy of value
- Objects: Copy of reference (both point to same object)

```java
void modify(int x) { x = 20; }           // Primitive: copy of value
void modify(StringBuilder sb) { sb.append("!"); } // Reference: copy of reference
```

---

### Q5: Why is String immutable?
1. **String Pool**: Sharing requires immutability
2. **Security**: Parameters (file paths, URLs) can't be modified
3. **Thread-safety**: Immutable = inherently thread-safe
4. **Performance**: Hashcode caching, substring optimization

---

## 🔢 Data Types & Operators

### Q6: Difference between `==` and `.equals()`?
- `==`: Reference comparison (memory address)
- `.equals()`: Content comparison (can be overridden)

```java
String a = "hello";
String b = new String("hello");
a == b        // false (different objects)
a.equals(b)   // true (same content)
```

---

### Q7: What is autoboxing/unboxing?
- **Autoboxing**: Primitive → Wrapper (`Integer i = 10;`)
- **Unboxing**: Wrapper → Primitive (`int j = i;`)

**Danger:** NPE when unboxing null
```java
Integer n = null;
int x = n; // NullPointerException!
```

---

## 🔄 Control Flow

### Q8: Switch expression vs statement (Java 12+)?
```java
// Statement (traditional)
switch (day) { case 1: ... break; }

// Expression (returns value, no fall-through)
String result = switch (day) {
    case 1,2,3,4,5 -> "Weekday";
    case 6,7 -> "Weekend";
    default -> "Invalid";
};
```

---

## 📦 Collections & Generics (Preview for Sprint 3)

### Q9: ArrayList vs LinkedList?
| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| get(index) | O(1) | O(n) |
| add/remove (end) | O(1)* | O(1) |
| add/remove (middle) | O(n) | O(1)** |

*Amortized, **if you have the iterator position

---

## 🧵 Multithreading (Preview for Sprint 5)

### Q10: Thread vs Runnable?
| Aspect | Thread | Runnable |
|--------|--------|----------|
| Extends | Thread class | Interface |
| Inheritance | Single inheritance | Can extend other class |
| Reusability | Low | High |
| Preferred | No | Yes |

---

## 🧠 Advanced / Design

### Q11: Composition vs Inheritance?
**Guideline:** "Favor composition over inheritance"

| Aspect | Composition | Inheritance |
|--------|-------------|-------------|
| Relationship | Has-a | Is-a |
| Coupling | Loose | Tight |
| Flexibility | High (runtime) | Low (compile-time) |
| Code reuse | Via delegation | Via inheritance |

**Rule:** Use inheritance for "is-a" (Liskov); composition for "has-a"

---

### Q12: What is Dependency Injection?
Providing dependencies from outside rather than creating internally.

```java
// Constructor Injection (Recommended)
class OrderService {
    private final PaymentProcessor processor;
    public OrderService(PaymentProcessor processor) { this.processor = processor; }
}
```

**Benefits:** Testability, Flexibility, Loose Coupling

---

### Q13: Explain SOLID with examples.
| Principle | Violation | Fix |
|-----------|-----------|-----|
| **S**RP | God class | Split responsibilities |
| **O**CP | `if/else` for types | Polymorphism |
| **L**SP | Square extends Rectangle | Composition |
| **I**SP | Fat interface | Segregate |
| **D**IP | `new StripeProcessor()` | Depend on interface |

---

### Q14: Explain Liskov Substitution Principle.
**Principle:** Subtype must be substitutable for base type without breaking behavior.

**Classic violation:** Square extends Rectangle
```java
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(10); // Square sets both to 10!
assert r.getWidth() == 5; // FAILS!
```

**Fix:** Use composition or separate interfaces.

---

### Q15: What is the diamond problem?
**Problem:** Multiple inheritance ambiguity when class inherits from two classes that inherit from same base.

**Java's solution:** Single inheritance + interfaces (default methods resolved by explicit override)

---

### Q16: What are Design Patterns?
Reusable solutions to common problems.

| Category | Patterns |
|----------|----------|
| Creational | Singleton, Factory, Builder, Prototype |
| Structural | Adapter, Decorator, Facade, Composite |
| Behavioral | Strategy, Observer, Command, Template Method |

---

### Q17: Explain Strategy Pattern.
**Definition:** Define family of algorithms, encapsulate each, make interchangeable.

```java
interface PaymentStrategy { void pay(BigDecimal amount); }
class CreditCard implements PaymentStrategy { ... }
class PayPal implements PaymentStrategy { ... }

class ShoppingCart {
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy s) { strategy = s; }
    public void checkout() { strategy.pay(total); }
}
```

---

### Q18: What is the Builder Pattern?
**Use when:** 4+ parameters, some optional

```java
Computer c = Computer.builder()
    .cpu("i7").ram(32).storage("1TB")
    .gpu("RTX 4090").build();
```

**Benefits:** Readable, immutable, validates required fields

---

### Q19: What is Observer Pattern?
**Definition:** One-to-many dependency; when one changes, all notified.

```java
interface Listener { void onEvent(Event e); }
class EventSource {
    List<Listener> listeners = new ArrayList<>();
    void addListener(Listener l) { listeners.add(l); }
    void fireEvent(Event e) { listeners.forEach(l -> l.onEvent(e)); }
}
```

---

### Q20: What is Singleton? When to use/avoid?
```java
public enum Singleton { INSTANCE; } // Best: thread-safe, serialization-safe
```

**Use:** Configuration, logging, connection pools
**Avoid:** Global state, testing difficulties, hidden dependencies

---

## 💡 Pro Tips for Interviews

1. **Always clarify:** "May I assume input is valid?" "What's the expected input size?"
2. **Think aloud:** Explain your approach before coding
3. **Edge cases:** Empty, null, single element, duplicates, overflow
4. **Complexity:** State time/space complexity
5. **Trade-offs:** "ArrayList is faster for random access but LinkedList for frequent insertions"

---

## 📝 Quick Reference Card

| Concept | Key Point |
|---------|-----------|
| `main` signature | `public static void main(String[] args)` |
| Integer division | `10/3 = 3` (not 3.33) |
| String comparison | `.equals()` not `==` |
| String mutability | Immutable (use StringBuilder) |
| Pass-by-value | Always (references passed by value) |
| Switch expression | Returns value, no fall-through |
| Varargs | `type...` last parameter only |
| Pass-by-value | Always (references passed by value) |
| Default char | `'\u0000'` |
| Default boolean | `false` |

---

## 🎯 Behavioral / Design

### Q21: How would you design a parking lot system?
**Approach:**
1. Clarify requirements (floors, spots, vehicle types, pricing)
2. Core classes: `ParkingLot`, `Floor`, `Spot`, `Vehicle`, `Ticket`, `Payment`
3. Patterns: Factory (Vehicle), Strategy (Pricing), State (Spot status)
4. Concurrency: Synchronized spots, atomic counters

### Q22: How would you implement a rate limiter?
**Approaches:**
- Token Bucket (smooth)
- Leaky Bucket (constant rate)
- Fixed Window (simple, burst at boundaries)
- Sliding Window (accurate)

### Q23: How do you handle cache invalidation?
**Strategies:**
- TTL (Time To Live)
- Write-through / Write-behind
- Cache-aside (lazy loading)
- Event-driven invalidation

---

## 🎯 Score Interpretation
- **20-22:** Excellent (Mastery)
- **15-19:** Good (Proficient)
- **10-14:** Fair (Needs review)
- **<10:** Retake recommended

---

*Self-grade honestly. Review weak areas with theory.md.*