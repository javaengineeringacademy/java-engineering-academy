# Decision Guide: Pass by Value

## When to Apply
- Understanding method parameter behavior in all Java methods
- Designing methods that modify caller state (return new objects, use mutable containers)
- Debugging why method parameters are not being modified as expected

## When NOT to Worry
- Single-threaded code where mutation through references is intentional
- Methods that return new objects instead of modifying parameters
- When using immutable objects (pass-by-value semantics are irrelevant)

## Trade-offs
| Approach | Pros | Cons |
|----------|------|------|
| Modify through reference | No object creation | Caller's object changes (may be unexpected) |
| Return new object | No side effects | Object creation overhead |
| Use mutable container (AtomicReference) | Can swap references | Adds complexity |

## Expert Recommendation
Design methods to either: (1) return a new object, or (2) clearly document that the method modifies the passed object's state. Avoid designing methods that reassign parameters expecting the caller to see the change.
