# Decision Guide: Generics and Inheritance / Subtyping

## Decision Tree

```
Is List<X> a subtype of List<Y>?
├── Does X = Y?
│   ├── Yes → Same type — fully compatible
│   └── No → Is X a subtype of Y?
│       ├── Yes → NOT a subtype of List (invariant)
│       └── No → Not compatible at all
├── Do you want covariance?
│   ├── Yes → Use wildcards: List<? extends Y>
│   └── No → Keep invariant: List<X> stays List<X>
└── Do you want contravariance?
    ├── Yes → Use wildcards: List<? super X>
    └── No → Keep invariant
```

## Key Subtyping Rules

| Relationship | Example | Subtype? |
|---|---|---|
| Invariance | `List<Integer>` vs `List<Number>` | No |
| Covariance (wildcard) | `List<Integer>` vs `List<? extends Number>` | Yes |
| Contravariance (wildcard) | `List<Number>` vs `List<? super Integer>` | Yes |
| Same type | `List<String>` vs `List<String>` | Yes |
| Raw vs parameterized | `List` vs `List<String>` | Yes (unsafe) |

## When Generics and Inheritance Interact

- Designing class hierarchies with type parameters
- Ensuring Liskov Substitution Principle (LSP) with generics
- Factory method patterns returning different subtypes
- Implementing interfaces with type parameters

## Invariance by Default (Why?)

1. **Type safety**: Prevents runtime `ArrayStoreException`-like errors
2. **Mutability**: Allows both reading and writing safely
3. **Simplicity**: Invariant types are easier to reason about

## When to Use Variance (Wildcards)

- Need to pass `List<Integer>` where `List<Number>` is expected → covariant wildcard
- Need to accept `List<Number>` as parameter storing `Integer` → contravariant wildcard
- API design for maximum flexibility
- Implementing generic algorithms

## Decision Rules

1. **Java generics are invariant** — `List<Integer>` is NOT `List<Number>`
2. **Use `? extends T` for read-only covariance** — producer pattern
3. **Use `? super T` for write-only contravariance** — consumer pattern
4. **Arrays are covariant** — `Integer[]` IS `Number[]` (unsafely)
5. **Generic type parameters don't inherit** — `Container<Cat>` is NOT `Container<Animal>`
6. **Self-types in inheritance** — use F-bounded polymorphism: `<T extends Base<T>>`

## Engineering Trade-offs

| Variance | Safe? | Readable | Flexible | Use Case |
|---|---|---|---|---|
| Invariant | Very safe | High | Low | Default choice |
| Covariant | Safe (read-only) | Moderate | High | Producer |
| Contravariant | Safe (write-only) | Moderate | High | Consumer |
| Array covariance | Unsafe | High | High | Legacy / avoid |

## Common Code Review Comments

- "Generics are invariant — this won't compile without wildcards"
- "Use `? extends Number` to accept subtypes for read-only access"
- "This violates LSP with generics — reconsider the type hierarchy"
- "Raw types bypass subtyping checks — add type parameters"
- "Consider F-bounded polymorphism for this builder pattern"

## Production Patterns

```java
// Pattern: Covariant return in generic hierarchy
abstract class Box<T> { T get(); }
class StringBox extends Box<String> { String get() { ... } }

// Pattern: F-bounded polymorphism
interface Repository<T extends Entity<T>> { T findById(Long id); }
class UserRepo implements Repository<User> { ... }

// Pattern: Wildcard for API flexibility
void merge(Collection<? extends Number> src, Collection<? super Number> dest)

// Pattern: Subtyping with interfaces
class ImmutableArrayList<E> implements List<E> { ... }
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Assuming `List<Child>` is `List<Parent>` | Use wildcards: `List<? extends Parent>` |
| Returning generic arrays | Use `List<T>` instead |
| Raw type assignment | Add proper type parameters |
| Not using F-bounded types for self-referential patterns | Add `<T extends Base<T>>` |
