# Decision Guide: Erasure of Generic Methods

## Decision Tree

```
Do you need to understand generic method erasure?
├── Is a generic method being overridden?
│   ├── Yes → Bridge methods are generated
│   └── No → See method erasure rules below
├── Are there overloading conflicts?
│   ├── Yes → Erasure causes same erased signature — rename
│   └── No → Proceed
└── Do you need to understand bridge methods?
    ├── Yes → See bridge method explanation
    └── No → Normal development
```

## Generic Method Erasure Rules

| Original Method | Erased Method |
|---|---|
| `<T> T process(T input)` | `Object process(Object input)` |
| `<T extends Number> T process(T input)` | `Number process(Number input)` |
| `<T> List<T> filter(List<T> list)` | `List filter(List list)` |
| `<T extends Comparable> int compare(T a, T b)` | `int compare(Comparable a, Comparable b)` |

## Bridge Methods

| Scenario | Bridge Method Purpose |
|---|---|
| Overriding with narrower type | Compiler generates bridge to call the narrower method |
| Covariant return types | Bridge ensures correct dispatch |
| Type erasure in subclass | Bridge preserves polymorphic behavior |

## When This Matters

- Debugging unexpected `ClassCastException`
- Understanding stack traces with synthetic methods
- Reflection-based frameworks calling generic methods
- Implementing generic interfaces in non-generic classes

## Decision Rules

1. **Generic methods erase to their bounded types** — `<T extends X>` becomes `X`
2. **Bridge methods are generated automatically** — don't write them manually
3. **Overloading with different generic params causes conflicts** — rename methods
4. **Covariant returns in generics use bridge methods** — understand this for debugging
5. **Reflection on generic methods requires `getGenericReturnType()`** — not just `getReturnType()`

## Engineering Trade-offs

| Concern | Erasure Impact | Mitigation |
|---|---|---|
| Method overloading | Conflict on erased signature | Rename methods |
| Stack traces | Synthetic/bridge methods confuse | Learn to recognize them |
| Reflection | Need `getGeneric*` methods | Use `Method.getGenericReturnType()` |
| Performance | No boxing overhead | Benefit of erasure |

## Common Code Review Comments

- "Bridge methods are generated here — check stack traces carefully"
- "These two methods have the same erasure — rename one"
- "Use `getGenericReturnType()` to preserve generic type info via reflection"
- "This override generates a bridge method — verify behavior"

## Production Patterns

```java
// Pattern: Generic method in interface
interface Converter<F, T> {
    T convert(F from);
}

// Erasure: Object convert(Object from);
// Bridge method: generated in implementing class

// Pattern: Avoiding overloading conflicts
public void process(List<String> strings) { ... }
public void processNumbers(List<Integer> numbers) { ... }
// NOT: process(List<Integer>) — conflicts with erasure

// Pattern: Reflecting on generic methods
Method method = MyClass.class.getMethod("process", List.class);
Type genericReturn = method.getGenericReturnType();
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Overloading `process(List<String>)` / `process(List<Integer>)` | Rename methods |
| Expecting runtime type checks in generic methods | Understand erasure removes them |
| Ignoring bridge methods in debugging | Check for synthetic methods |
| Using `getReturnType()` instead of `getGenericReturnType()` | Use the generic variant |
