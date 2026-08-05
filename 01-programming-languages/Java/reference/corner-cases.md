# Java Corner Cases

## Integer Overflow

Java fixed-width integers silently overflow on arithmetic. `Integer.MAX_VALUE + 1` wraps to `Integer.MIN_VALUE`. No exception is thrown. This is a common source of bugs in loop counters, array indexing, and financial calculations.

Division by zero on `int` throws `ArithmeticException`. Division of a floating-point value by zero produces `Infinity` or `NaN`, not an exception.

```java
int a = Integer.MAX_VALUE; // 2147483647
int b = a + 1;            // -2147483648 (wraps, no error)
```

## Float and Double Precision

`float` and `double` use IEEE 754 representation. Comparing them with `==` is unreliable due to rounding errors. `0.1 + 0.2 != 0.3` is true in Java. Use `Math.abs(a - b) < epsilon` for approximate equality.

BigDecimal should be used for currency. Construct it from a `String`, never from a `double`, to avoid inheriting the double's imprecision.

## String Pooling and Immutability

String literals are interned in the string pool. Two literals with the same content share the same object, but `new String("hello")` always creates a new object on the heap. The `==` operator compares references, not content.

String concatenation in loops creates many intermediate objects. Use `StringBuilder` for non-trivial concatenation. The compiler may optimize simple concatenations, but loop-based concatenation is not optimized.

## Null Pointer Behavior

NullPointer Exception is thrown on dereferencing a null reference. The exception message may indicate which operation caused it (method call, field access, array access), but not always.

Autoboxing a null `Integer` to `int` throws NullPointerException. This happens silently when assigning a null boxed type to a primitive.

## Concurrency Race Conditions

Shared mutable state without synchronization causes data races. The Java Memory Model does not guarantee visibility of writes across threads unless proper happens-before relationships are established via `volatile`, `synchronized`, or concurrent utilities.

Double-checked locking for lazy initialization is broken without `volatile`. The object reference may be published before the constructor finishes executing on another thread.

## Equals and Hash Code Contract

If two objects are equal according to `equals()`, they must have the same `hashCode()`. Violating this breaks hash-based collections like `HashMap` and `HashSet`. The reverse is not required: equal hash codes do not imply equality.

Overriding `equals()` without overriding `hashCode()` (or vice versa) leads to subtle bugs where objects are not found in collections.

## Classloader Issues

`ClassNotFoundException` vs `NoClassDefFoundError`: the first means the class was never found, the second means it was found at compile time but not at runtime. Often caused by mismatched dependency versions or transitive dependencies.

`ClassCastException` can occur even without explicit casts due to generics erasure. The generic type parameter is erased at runtime, so type safety depends on the compile-time check.

## Try-with-resources and Suppressed Exceptions

When an exception occurs in both the try block and the `close()` method, the close exception is added as a suppressed exception. The original exception remains the primary one. Use `getSuppressed()` to retrieve them.

Implementing `AutoCloseable` incorrectly (e.g., swallowing exceptions in `close()`) can hide failures in the try block.

## Switch Statement Fall-through

The `switch` statement on `String` uses `.hashCode()` and `.equals()`. If two different strings have the same hash code (possible with crafted input), the switch can match the wrong case. This is rare but real.

`switch` on `enum` requires all enum constants to be handled, or a `default` case is needed. Missing cases cause a compile-time error.

## Serialization Gotchas

`serialVersionUID` should be explicitly defined. If omitted, the compiler generates one based on the class structure. Changing the class structure without updating the UID causes `InvalidClassException` on deserialization.

`transient` fields are not serialized. If a field is `transient` and not re-initialized in a custom `readObject()`, it will be null or default-valued after deserialization.

## Lambda and Method Reference Pitfalls

Capturing variables in lambdas must be effectively final. You cannot reassign a captured variable. This is a compile-time restriction.

Method references like `MyClass::staticMethod` do not capture `this`. Instance method references like `instance::method` do capture the instance. Confusing the two leads to unexpected behavior.
