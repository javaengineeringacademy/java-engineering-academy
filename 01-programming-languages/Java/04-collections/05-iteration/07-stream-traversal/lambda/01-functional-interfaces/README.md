# Functional Interfaces

Functional interfaces have exactly one abstract method and serve as the target type for lambda expressions. The `java.util.function` package provides the most commonly used functional interfaces.

## Predicate\<T\>

Tests a condition and returns a boolean result. Used extensively with `filter()`.

```java
Predicate<String> isLong = s -> s.length() > 5;
isLong.test("hello");      // false
isLong.test("hello world"); // true
```

### Composition

```java
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> hasLengthFour = s -> s.length() == 4;

Predicate<String> startsWithAAndLengthFour = startsWithA.and(hasLengthFour);
Predicate<String> startsWithAOrLengthFour = startsWithA.or(hasLengthFour);
Predicate<String> notStartsWithA = startsWithA.negate();
```

## Function\<T, R\>

Transforms an input of type T into an output of type R. Used with `map()` and `flatMap()`.

```java
Function<String, Integer> toLength = String::length;
toLength.apply("hello"); // 5
```

### Composition

```java
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
doubleThenAdd.apply(5); // 20

Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
addThenDouble.apply(5); // 30
```

## Consumer\<T\>

Performs an operation on an argument without returning a result. Used with `forEach()` and `peek()`.

```java
Consumer<String> print = System.out::println;
print.accept("hello"); // prints: hello
```

### Chaining

```java
Consumer<String> shout = s -> System.out.println(s.toUpperCase());
Consumer<String> log = s -> System.out.println("[LOG] " + s);
Consumer<String> combined = shout.andThen(log);
combined.accept("hello");
// Prints: HELLO
// Prints: [LOG] hello
```

## Supplier\<T\>

Provides a value without taking any input. Used with `Stream.generate()` and lazy initialization.

```java
Supplier<List<String>> listFactory = ArrayList::new;
listFactory.get(); // new empty ArrayList
```

## UnaryOperator\<T\>

Specialized `Function<T, T>` where input and output types are the same. Used with `map()` for same-type transformations.

```java
UnaryOperator<String> exclaim = s -> s + "!";
exclaim.apply("hello"); // "hello!"
```

## BinaryOperator\<T\>

Takes two arguments of the same type and returns a result of the same type. Used with `reduce()`.

```java
BinaryOperator<Integer> sum = Integer::sum;
sum.apply(3, 4); // 7

BinaryOperator<Integer> max = BinaryOperator.maxBy(Integer::compareTo);
max.apply(3, 7); // 7
```

## BiFunction\<T, U, R\>

Takes two arguments of different types and returns a result. Used with `map()` in paired streams.

```java
BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
repeat.apply("ha", 3); // "hahaha"
```

## BiPredicate\<T, U\>

Takes two arguments and returns a boolean. Useful for two-argument filtering.

```java
BiPredicate<String, Integer> lengthCheck = (s, n) -> s.length() >= n;
lengthCheck.test("hello", 3); // true
```

## BiConsumer\<T, U\>

Takes two arguments with no return value. Used for two-argument side effects.

```java
BiConsumer<String, Integer> printer = (s, n) -> 
    System.out.println(s + ": " + n);
printer.accept("age", 25); // prints: age: 25
```

## Using @FunctionalInterface

Custom functional interfaces should be annotated with `@FunctionalInterface` for compile-time verification.

```java
@FunctionalInterface
public interface Transformer<T> {
    T transform(T input);
    
    // Adding a default method is allowed
    default Transformer<T> andThen(Transformer<T> after) {
        return input -> after.transform(this.transform(input));
    }
}
```

## Quick Reference

| Interface | Method | Common Use |
|-----------|--------|------------|
| `Predicate<T>` | `test(T) → boolean` | `filter()` |
| `Function<T, R>` | `apply(T) → R` | `map()` |
| `Consumer<T>` | `accept(T) → void` | `forEach()` |
| `Supplier<T>` | `get() → T` | `generate()` |
| `UnaryOperator<T>` | `apply(T) → T` | `map()` same type |
| `BinaryOperator<T>` | `apply(T, T) → T` | `reduce()` |
