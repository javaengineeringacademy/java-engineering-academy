# Common OOP Mistakes

## Design Mistakes

| Mistake | Problem | Solution |
|---------|---------|----------|
| `Square extends Rectangle` | LSP violation | Composition / separate interfaces |
| God Class | Does everything | Split by SRP |
| Anemic Domain Model | Data only, no behavior | Move logic to domain objects |
| Primitive Obsession | Primitives everywhere | Value Objects (Money, Email) |
| Feature Envy | Method uses other object's data | Move method to data owner |
| Shotgun Surgery | Change requires many edits | Consolidate related changes |
| Circular Dependencies | A→B→A | Invert dependencies |

## Code Mistakes

| Mistake | Example | Fix |
|---------|---------|-----|
| `==` for String | `str == "hello"` | `.equals()` |
| `==` for Integer | `Integer a = 1000; a == 1000` | `.equals()` or `Objects.equals()` |
| `equals()` without `hashCode()` | Breaks `HashMap`/`HashSet` | Override both together |
| Mutable fields in `hashCode()` | Breaks `HashMap` | Use only immutable fields |
| `equals` without null/class check | NPE or wrong type | Check null + class |
| Using `finalize()` | Deprecated, unreliable | Try-with-resources / Cleaner |
| `instanceof` + cast everywhere | Not polymorphic | Use polymorphism |
| `null` returns | NPE risk | `Optional<T>` or empty collection |
| Public fields | No encapsulation | Private + getters/setters |
| Mutable static state | Thread safety issues | Avoid or synchronize |

## Inheritance Mistakes

| Mistake | Example | Fix |
|---------|---------|-----|
| `Square extends Rectangle` | `setWidth` changes height | Composition / separate interfaces |
| Deep inheritance hierarchies | 5+ levels | Flatten, prefer composition |
| Calling overridable in constructor | Calls overridden method | Avoid or use factory |
| Overriding `equals` but not `hashCode` | HashMap breaks | Override both together |
| `Square.setWidth()` changes height | LSP violation | Separate classes or composition |

## Concurrency Mistakes

| Mistake | Fix |
|---------|-----|
| Mutable shared state | Immutable objects, synchronization |
| `volatile` for compound actions | Use `Atomic*` or locks |
| `synchronized` on `this` (leaky) | Private lock object |
| `Thread.stop()` / `suspend()` | Deprecated, use interruption |

## Collection Mistakes

| Mistake | Fix |
|---------|-----|
| Modifying during iteration | `Iterator.remove()` or copy |
| `ArrayList` for frequent insert/delete | `LinkedList` or different structure |
| `HashMap` with mutable keys | Immutable keys only |
| `equals()` without `hashCode()` | Override both |
| `==` for enum comparison | `==` is fine for enums |

## Exception Handling Mistakes

| Mistake | Fix |
|---------|-----|
| Catch `Exception` / `Throwable` | Catch specific exceptions |
| Swallow exceptions | Log or rethrow |
| Exception for control flow | Use conditionals |
| Empty catch block | Log or handle |
| `throw e` (loses stack) | `throw` or `throw new X(e)` |

## Design Anti-Patterns

| Pattern | Description | Better Approach |
|---------|-------------|-----------------|
| Singleton | Global state | Dependency Injection |
| Service Locator | Hidden dependencies | Constructor Injection |
| Anemic Domain Model | Logic in services | Rich Domain Model |
| DTOs in Domain | Leaking infrastructure | Separate DTOs |
| God Service | Does everything | Split by bounded context |
| Fat Controller | Logic in controller | Move to service |

## Quick Reference: Do's and Don'ts

| Do | Don't |
|----|-------|
| Use `final` by default | Leave fields mutable |
| Prefer `Optional<T>` over `null` | Return `null` |
| Use `Objects.requireNonNull()` | Null checks everywhere |
| Use `Objects.equals()` | `a.equals(b)` (NPE risk) |
| Use `StringBuilder` in loops | `String s += "a"` |
| Use `try-with-resources` | Manual `close()` |
| Use `Optional` for nullable returns | Return `null` |
| Use `enum` for fixed sets | `String` constants |
| Use `enum` for singleton | `class Singleton { INSTANCE }` |
| Use `record` for data | `class` with getters/setters |

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

---

## 🎯 Score Interpretation
- **28-30:** Excellent (Mastery)
- **24-27:** Good (Proficient)
- **20-23:** Fair (Needs review)
- **<20:** Retake recommended

---

*Self-grade honestly. Review wrong answers with theory.md.*