# Monads

Monads provide a way to chain operations while managing context like optional values, error handling, or asynchronous computation. They enable clean composition of operations that share a common pattern.

## Table of Contents

1. [Concepts](#concepts)
2. [Optional](#optional)
3. [Either](#either)
4. [Try](#try)
5. [Composition and Chaining](#composition-and-chaining)
6. [Custom Monads](#custom-monads)
7. [Best Practices](#best-practices)
8. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is a Monad?

A monad wraps a value with context and provides two operations:
- **wrap** (unit/return) - put a value into the monadic context
- **bind** (flatmap) - chain operations that return monads

```
Optional<Integer> a = Optional.of(5);       // wrap
Optional<Integer> b = a.map(x -> x * 2);    // bind
Optional<Integer> c = a.flatMap(x -> ...);  // bind
```

### Why Monads?

- **Null Safety** - Optional eliminates null checks
- **Error Handling** - Either/Try handle errors functionally
- **Composition** - Chain operations cleanly
- **Context Management** - Handle cross-cutting concerns uniformly

---

## Optional

### Basic Usage

```java
// Creating Optional
Optional<String> present = Optional.of("Hello");
Optional<String> empty = Optional.empty();
Optional<String> nullable = Optional.ofNullable(getStringOrNull());

// Using Optional
String result = nullable.orElse("default");
String result2 = nullable.orElseGet(() -> computeDefault());
String result3 = nullable.orElseThrow(() -> new IllegalStateException());
```

### Chaining with Optional

```java
public record User(String name, Address address) {}
public record Address(String city, String zipCode) {}

// Deep access without null checks
public Optional<String> getUserCity(User user) {
    return Optional.ofNullable(user)
        .map(User::address)
        .map(Address::city);
}

// Chain operations
public Optional<String> formatUserAddress(User user) {
    return Optional.ofNullable(user)
        .map(User::address)
        .filter(addr -> addr.city() != null)
        .map(addr -> addr.city() + ", " + addr.zipCode());
}
```

### Optional in Collections

```java
// Find with Optional
Optional<User> found = users.stream()
    .filter(u -> u.id().equals(targetId))
    .findFirst();

// Transform with Optional
List<String> cities = users.stream()
    .map(User::address)
    .map(Address::city)
    .filter(Optional::isPresent)
    .map(Optional::get)
    .toList();

// Better: use flatMap
List<String> cities = users.stream()
    .flatMap(u -> Optional.ofNullable(u.address()).stream())
    .map(Address::city)
    .toList();
```

### Optional Pitfalls

```java
// BAD: Using Optional.get() without checking
String value = optional.get();  // NoSuchElementException!

// BAD: Using isPresent() then get()
if (optional.isPresent()) {
    String value = optional.get();  // Works but verbose
}

// GOOD: Use orElse/orElseGet/orElseThrow
String value = optional.orElse("default");

// GOOD: Use map/flatMap/filter
Optional<String> result = optional
    .map(String::toUpperCase)
    .filter(s -> s.length() > 3);

// BAD: Using Optional as method parameter
public void process(Optional<String> value) { ... }  // Don't do this

// GOOD: Use nullable parameter
public void process(String value) { ... }
```

---

## Either

### Basic Either Implementation

```java
public sealed interface Either<L, R>
    permits Left, Right {

    record Left<L, R>(L value) implements Either<L, R> {}
    record Right<L, R>(R value) implements Either<L, R> {}

    default <T> Either<L, T> map(Function<R, T> mapper) {
        return switch (this) {
            case Left<L, R> left -> left;
            case Right<L, R> right -> new Right<>(mapper.apply(right.value()));
        };
    }

    default <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
        return switch (this) {
            case Left<L, R> left -> left;
            case Right<L, R> right -> mapper.apply(right.value());
        };
    }

    default R orElse(R defaultValue) {
        return switch (this) {
            case Left<L, R> left -> defaultValue;
            case Right<L, R> right -> right.value();
        };
    }

    default R orElseThrow() {
        return switch (this) {
            case Left<L, R> left -> throw new RuntimeException(left.value().toString());
            case Right<L, R> right -> right.value();
        };
    }
}
```

### Using Either for Error Handling

```java
public record ValidationError(String field, String message) {}

public Either<ValidationError, User> validateUser(String name, String email) {
    if (name == null || name.isBlank()) {
        return new Either.Left<>(new ValidationError("name", "Name required"));
    }
    if (email == null || !email.contains("@")) {
        return new Either.Left<>(new ValidationError("email", "Invalid email"));
    }
    return new Either.Right<>(new User(name, email));
}

// Chaining validations
public Either<ValidationError, User> createUser(String name, String email, int age) {
    return validateName(name)
        .flatMap(validName -> validateEmail(email)
            .flatMap(validEmail -> validateAge(age)
                .map(validAge -> new User(validName, validEmail, validAge))));
}
```

### Either in Practice

```java
// Service returning Either
public Either<Error, Order> processOrder(OrderRequest request) {
    return validateOrder(request)
        .flatMap(order -> checkInventory(order))
        .flatMap(order -> calculateTotal(order))
        .map(order -> order.withStatus(OrderStatus.CONFIRMED));
}

// Client handling Either
public void handleOrder(OrderRequest request) {
    Either<Error, Order> result = processOrder(request);
    
    switch (result) {
        case Either.Left<Error, Order> error -> 
            showError(error.value());
        case Either.Right<Error, Order> order -> 
            showConfirmation(order.value());
    }
}
```

---

## Try

### Basic Try Implementation

```java
public sealed interface Try<T>
    permits Success, Failure {

    record Success<T>(T value) implements Try<T> {}
    record Failure<T>(Throwable exception) implements Try<T> {}

    static <T> Try<T> of(Supplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    default <U> Try<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T> success -> Try.of(() -> mapper.apply(success.value()));
            case Failure<T> failure -> new Failure<>(failure.exception());
        };
    }

    default <U> Try<U> flatMap(Function<T, Try<U>> mapper) {
        return switch (this) {
            case Success<T> success -> {
                try {
                    yield mapper.apply(success.value());
                } catch (Throwable t) {
                    yield new Failure<>(t);
                }
            }
            case Failure<T> failure -> new Failure<>(failure.exception());
        };
    }

    default T orElse(T defaultValue) {
        return switch (this) {
            case Success<T> success -> success.value();
            case Failure<T> failure -> defaultValue;
        };
    }

    default T orElseThrow() {
        return switch (this) {
            case Success<T> success -> success.value();
            case Failure<T> failure -> throw new RuntimeException(failure.exception());
        };
    }
}
```

### Using Try for Exception Handling

```java
// Parse integer safely
Try<Integer> parsed = Try.of(() -> Integer.parseInt("123"));
// Success(123)

Try<Integer> failed = Try.of(() -> Integer.parseInt("abc"));
// Failure(NumberFormatException)

// Chain operations that might throw
Try<Path> path = Try.of(() -> Path.of("file.txt"))
    .flatMap(p -> Try.of(() -> Files.readString(p)))
    .map(String::trim)
    .map(Integer::parseInt);

// Handle result
int value = path.orElse(0);
```

### Try in Practice

```java
// File processing with Try
public Try<Report> generateReport(String filename) {
    return Try.of(() -> Files.readString(Path.of(filename)))
        .map(this::parseData)
        .map(this::validateData)
        .map(this::createReport);
}

// API call with retry
public <T> Try<T> retryRequest(int maxRetries, Supplier<T> request) {
    Try<T> result = Try.of(request::get);
    
    for (int i = 0; i < maxRetries && result instanceof Try.Failure<T> failure; i++) {
        result = Try.of(request::get);
    }
    
    return result;
}
```

---

## Composition and Chaining

### Chaining Operations

```java
// Optional chaining
Optional<String> result = Optional.of("  Hello World  ")
    .map(String::trim)
    .map(String::toLowerCase)
    .filter(s -> s.length() > 5)
    .map(s -> s.replaceAll("\\s+", "_"));

// Either chaining
Either<Error, String> result = validateInput(input)
    .flatMap(this::processInput)
    .map(this::formatOutput);

// Try chaining
Try<Config> config = Try.of(() -> Files.readString(Path.of("config.json")))
    .map(this::parseJson)
    .map(this::validateConfig);
```

### Combining Multiple Monads

```java
// Combining two Optionals
public <A, B, C> Optional<C> combine(
        Optional<A> a, 
        Optional<B> b, 
        BiFunction<A, B, C> combiner) {
    return a.flatMap(va -> b.map(vb -> combiner.apply(va, vb)));
}

// Combining three Optionals
public <A, B, C, D> Optional<D> combine3(
        Optional<A> a, Optional<B> b, Optional<C> c,
        Function3<A, B, C, D> combiner) {
    return a.flatMap(va -> 
        b.flatMap(vb -> 
            c.map(vc -> combiner.apply(va, vb, vc))));
}

// Usage
Optional<String> name = Optional.of("Alice");
Optional<Integer> age = Optional.of(30);
Optional<String> city = Optional.of("NYC");

Optional<String> summary = combine3(name, age, city,
    (n, a, c) -> n + " (" + a + ") from " + c);
```

---

## Custom Monads

### Validation Monad

```java
public sealed interface Validation<T>
    permits Valid, Invalid {

    record Valid<T>(T value) implements Validation<T> {}
    record Invalid<T>(List<String> errors) implements Validation<T> {}

    static <T> Validation<T> valid(T value) {
        return new Valid<>(value);
    }

    static <T> Validation<T> invalid(String error) {
        return new Invalid<>(List.of(error));
    }

    static <T> Validation<T> invalid(List<String> errors) {
        return new Invalid<>(errors);
    }

    default <U> Validation<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Valid<T> valid -> new Valid<>(mapper.apply(valid.value()));
            case Invalid<T> invalid -> new Invalid<>(invalid.errors());
        };
    }

    default <U> Validation<U> flatMap(Function<T, Validation<U>> mapper) {
        return switch (this) {
            case Valid<T> valid -> mapper.apply(valid.value());
            case Invalid<T> invalid -> new Invalid<>(invalid.errors());
        };
    }

    // Accumulate errors
    default Validation<T> combine(Validation<T> other) {
        return switch (this) {
            case Valid<T> valid -> other;
            case Invalid<T> invalid1 -> switch (other) {
                case Valid<T> valid -> this;
                case Invalid<T> invalid2 -> new Invalid<>(
                    Stream.concat(
                        invalid1.errors().stream(),
                        invalid2.errors().stream()
                    ).toList()
                );
            };
        };
    }
}

// Usage
Validation<String> name = validateName("Alice");
Validation<Integer> age = validateAge(30);
Validation<String> email = validateEmail("alice@example.com");

Validation<User> user = name.combine(age).combine(email)
    .map((n, a, e) -> new User(n, a, e));
```

### Either as a Monad

```java
// Either with error accumulation
public <T> Either<List<String>, T> validate(T value, List<Validator<T>> validators) {
    List<String> errors = validators.stream()
        .map(v -> v.validate(value))
        .filter(Option::isPresent)
        .map(Option::get)
        .toList();
    
    return errors.isEmpty() 
        ? Either.right(value)
        : Either.left(errors);
}
```

---

## Best Practices

### Do

```java
// 1. Use Optional for nullable returns
public Optional<User> findUser(Long id) {
    return Optional.ofNullable(repository.findById(id));
}

// 2. Use Either for recoverable errors
public Either<Error, Order> processOrder(OrderRequest request) {
    return validate(request).flatMap(this::createOrder);
}

// 3. Use Try for exception-prone operations
Try<Data> data = Try.of(() -> parseJson(input));

// 4. Chain operations instead of nesting
Optional<String> result = Optional.of(input)
    .map(this::trim)
    .map(this::normalize)
    .filter(this::isValid);
```

### Don't

```java
// 1. Don't use get() without checking
optional.get();  // BAD

// 2. Don't ignore failures
try { riskyOperation(); } catch (Exception e) { }  // BAD

// 3. Don't create deeply nested chains
Optional<String> result = a.flatMap(va -> 
    b.flatMap(vb -> 
        c.map(vc -> combine(va, vb, vc))));  // Hard to read

// 4. Don't use monads for simple cases
if (value != null) { ... }  // Sometimes simpler than Optional
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Monad** | Wrapper with context and chaining operations |
| **Optional** | Handles nullable values safely |
| **Either** | Handles recoverable errors functionally |
| **Try** | Handles exceptions functionally |
| **map** | Transform wrapped value |
| **flatMap** | Chain operations returning monad |
| **Composition** | Combine multiple monadic values |
| **Null Safety** | Optional eliminates null checks |
| **Error Handling** | Either/Try replace try-catch |
| **Validation** | Custom monads for domain validation |
