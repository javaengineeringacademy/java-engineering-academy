# Project Reactor

## Comprehensive Guide to Project Reactor

Project Reactor is the reactive library at the core of Spring's reactive support. This guide covers Mono, Flux, operators, scheduling, and backpressure management.

---

## Table of Contents

1. [Mono](#mono)
2. [Flux](#flux)
3. [Operators](#operators)
4. [Scheduling](#scheduling)
5. [Backpressure](#backpressure)
6. [Error Handling](#error-handling)
7. [Best Practices](#best-practices)

---

## Mono

### Creating Mono

```java
// Empty Mono
Mono<Void> emptyMono = Mono.empty();

// Mono with value
Mono<String> singleValue = Mono.just("Hello");

// Mono from supplier
Mono<String> fromSupplier = Mono.fromSupplier(() -> "Computed value");

// Mono from callable
Mono<String> fromCallable = Mono.fromCallable(() -> {
    // Expensive computation
    return "Result";
});

// Mono from Future
Mono<String> fromFuture = Mono.fromFuture(CompletableFuture.supplyAsync(() -> "Async result"));

// Mono from publisher
Mono<String> fromPublisher = Mono.from(publisher);
```

### Mono Operations

```java
// Map transformation
Mono<String> mapped = Mono.just("hello")
    .map(String::toUpperCase);

// FlatMap transformation
Mono<String> flatMapped = Mono.just("hello")
    .flatMap(s -> Mono.just(s + " world"));

// Filter
Mono<String> filtered = Mono.just("hello")
    .filter(s -> s.length() > 3);

// Default if empty
Mono<String> withDefault = Mono.empty()
    .defaultIfEmpty("default value");

// Switch if empty
Mono<String> withSwitch = Mono.empty()
    .switchIfEmpty(Mono.just("fallback"));

// Zip with other Mono
Mono<String> zipped = Mono.just("hello")
    .zipWith(Mono.just(" world"))
    .map(tuple -> tuple.getT1() + tuple.getT2());

// Cache
Mono<String> cached = Mono.just("expensive result")
    .cache(Duration.ofMinutes(5));
```

### Mono Subscribing

```java
// Subscribe with consumer
Mono.just("hello")
    .subscribe(value -> System.out.println("Value: " + value));

// Subscribe with consumer and error handler
Mono.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage())
    );

// Subscribe with consumer, error handler, and complete handler
Mono.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage()),
        () -> System.out.println("Completed")
    );

// Block (only in non-reactive code)
String value = Mono.just("hello").block();

// Block with timeout
String value = Mono.just("hello")
    .block(Duration.ofSeconds(5));
```

### Mono from Reactive Types

```java
// From Optional
Mono<String> fromOptional = Mono.justOrEmpty(Optional.of("value"));

// From nullable
Mono<String> fromNullable = Mono.justOrEmpty(null);

// From Supplier
Mono<String> fromSupplier = Mono.defer(() -> Mono.just("computed"));

// From Publisher
Mono<String> fromFlux = Mono.from(Flux.just("first", "second"));
```

---

## Flux

### Creating Flux

```java
// Empty Flux
Flux<String> emptyFlux = Flux.empty();

// Flux from values
Flux<String> fromValues = Flux.just("a", "b", "c");

// Flux from array
Flux<String> fromArray = Flux.fromArray(new String[]{"a", "b", "c"});

// Flux from iterable
Flux<String> fromIterable = Flux.fromIterable(List.of("a", "b", "c"));

// Flux from range
Flux<Integer> fromRange = Flux.range(1, 10);

// Flux from stream
Flux<String> fromStream = Flux.fromStream(Stream.of("a", "b", "c"));

// Flux with interval
Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

// Flux from publisher
Flux<String> fromPublisher = Flux.from(publisher);
```

### Flux Operations

```java
// Map transformation
Flux<String> mapped = Flux.just("a", "b", "c")
    .map(String::toUpperCase);

// FlatMap transformation
Flux<String> flatMapped = Flux.just("a", "b", "c")
    .flatMap(s -> Flux.just(s, s.toUpperCase()));

// ConcatMap (preserves order)
Flux<String> concatMapped = Flux.just("a", "b", "c")
    .concatMap(s -> Flux.just(s, s.toUpperCase()));

// Filter
Flux<String> filtered = Flux.just("a", "bb", "ccc")
    .filter(s -> s.length() > 1);

// Distinct
Flux<String> distinct = Flux.just("a", "b", "a", "c")
    .distinct();

// Take
Flux<String> taken = Flux.just("a", "b", "c", "d")
    .take(3);

// Skip
Flux<String> skipped = Flux.just("a", "b", "c", "d")
    .skip(2);

// Sort
Flux<String> sorted = Flux.just("c", "a", "b")
    .sort();

// Zip
Flux<String> zipped = Flux.just("a", "b", "c")
    .zipWith(Flux.just("1", "2", "3"))
    .map(tuple -> tuple.getT1() + tuple.getT2());

// Merge
Flux<String> merged = Flux.merge(
    Flux.just("a", "b"),
    Flux.just("c", "d"));

// Concat
Flux<String> concatenated = Flux.concat(
    Flux.just("a", "b"),
    Flux.just("c", "d"));
```

### Flux Buffering

```java
// Buffer by size
Flux<List<String>> buffered = Flux.just("a", "b", "c", "d", "e")
    .buffer(3);

// Buffer by time
Flux<List<String>> bufferedByTime = Flux.interval(Duration.ofSeconds(1))
    .take(10)
    .buffer(Duration.ofSeconds(3));

// Window by size
Flux<Flux<String>> windowed = Flux.just("a", "b", "c", "d", "e")
    .window(3);

// Window by time
Flux<Flux<String>> windowedByTime = Flux.interval(Duration.ofSeconds(1))
    .take(10)
    .window(Duration.ofseconds(3));
```

### Flux Aggregation

```java
// Collect list
Mono<List<String>> collected = Flux.just("a", "b", "c")
    .collectList();

// Collect to map
Mono<Map<String, Integer>> collectedToMap = Flux.just("a", "bb", "ccc")
    .collectMap(String::length);

// Reduce
Mono<String> reduced = Flux.just("a", "b", "c")
    .reduce("", (acc, s) -> acc + s);

// Count
Mono<Long> count = Flux.just("a", "b", "c").count();

// Any
Mono<Boolean> any = Flux.just(1, 2, 3).any(n -> n > 2);

// All
Mono<Boolean> all = Flux.just(1, 2, 3).all(n -> n > 0);
```

---

## Operators

### Map and FlatMap

```java
// Map - 1:1 transformation
Flux<String> mapped = Flux.just(1, 2, 3)
    .map(n -> "Number " + n);

// FlatMap - 1:N transformation
Flux<String> flatMapped = Flux.just(1, 2, 3)
    .flatMap(n -> Flux.just("A" + n, "B" + n));

// ConcatMap - FlatMap with order preservation
Flux<String> concatMapped = Flux.just(1, 2, 3)
    .concatMap(n -> Flux.just("A" + n, "B" + n));

// SwitchMap - Cancel previous and start new
Flux<String> switchMapped = Flux.just(1, 2, 3)
    .switchMap(n -> Mono.just("Result " + n));
```

### Filtering

```java
// Filter
Flux<String> filtered = Flux.just("a", "bb", "ccc")
    .filter(s -> s.length() > 1);

// Distinct
Flux<String> distinct = Flux.just("a", "b", "a", "c")
    .distinct();

// Distinct until changed
Flux<String> distinctUntilChanged = Flux.just("a", "a", "b", "b", "c")
    .distinctUntilChanged();

// Take
Flux<String> taken = Flux.just("a", "b", "c", "d")
    .take(3);

// Take while
Flux<String> takenWhile = Flux.just("a", "bb", "ccc", "dddd")
    .takeWhile(s -> s.length() <= 2);

// Take until
Flux<String> takenUntil = Flux.just("a", "bb", "ccc", "dddd")
    .takeUntil(s -> s.length() > 2);

// Skip
Flux<String> skipped = Flux.just("a", "b", "c", "d")
    .skip(2);

// Skip while
Flux<String> skippedWhile = Flux.just("a", "bb", "ccc", "dddd")
    .skipWhile(s -> s.length() <= 1);
```

### Transforming

```java
// Map
Flux<String> mapped = Flux.just(1, 2, 3)
    .map(n -> "Number " + n);

// FlatMap
Flux<String> flatMapped = Flux.just(1, 2, 3)
    .flatMap(n -> Flux.just("A" + n, "B" + n));

// Transform
Flux<String> transformed = Flux.just(1, 2, 3)
    .transform(flux -> flux.map(n -> "Transformed " + n));

// As
Flux<String> asFlux = Flux.just(1, 2, 3)
    .as(flux -> flux.map(n -> "As " + n));

// Handle
Flux<String> handled = Flux.just(1, 2, 3)
    .handle((n, sink) -> {
        if (n > 1) {
            sink.next("Value " + n);
        }
    });

// Index
Flux<String> indexed = Flux.just("a", "b", "c")
    .index((index, value) -> index + ": " + value);
```

### Combining

```java
// Zip
Flux<String> zipped = Flux.just("a", "b", "c")
    .zipWith(Flux.just("1", "2", "3"))
    .map(tuple -> tuple.getT1() + tuple.getT2());

// Zip with function
Flux<String> zippedWith = Flux.zip(
    Flux.just("a", "b", "c"),
    Flux.just("1", "2", "3"),
    (s1, s2) -> s1 + s2);

// Merge
Flux<String> merged = Flux.merge(
    Flux.just("a", "b"),
    Flux.just("c", "d"));

// Concat
Flux<String> concatenated = Flux.concat(
    Flux.just("a", "b"),
    Flux.just("c", "d"));

// Start with
Flux<String> startedWith = Flux.just("b", "c")
    .startWith("a");

// Merge with
Flux<String> mergedWith = Flux.just("b", "c")
    .mergeWith(Flux.just("a"));
```

### Buffering and Windowing

```java
// Buffer by size
Flux<List<String>> buffered = Flux.just("a", "b", "c", "d", "e")
    .buffer(3);

// Buffer by time
Flux<List<String>> bufferedByTime = Flux.interval(Duration.ofSeconds(1))
    .take(10)
    .buffer(Duration.ofSeconds(3));

// Window by size
Flux<Flux<String>> windowed = Flux.just("a", "b", "c", "d", "e")
    .window(3);

// Window by time
Flux<Flux<String>> windowedByTime = Flux.interval(Duration.ofSeconds(1))
    .take(10)
    .window(Duration.ofSeconds(3));

// Buffer with overlap
Flux<List<String>> bufferedWithOverlap = Flux.just("a", "b", "c", "d", "e")
    .buffer(3, 1);

// Buffer with padding
Flux<List<String>> bufferedWithPadding = Flux.just("a", "b", "c", "d", "e")
    .buffer(3, 0);
```

### Mathematical Operations

```java
// Sum
Mono<Integer> sum = Flux.just(1, 2, 3)
    .reduce(0, Integer::sum);

// Average
Mono<Double> average = Flux.just(1, 2, 3)
    .reduce(0.0, (acc, n) -> acc + n)
    .map(sum -> sum / 3.0);

// Min
Mono<Integer> min = Flux.just(1, 2, 3)
    .reduce((a, b) -> Math.min(a, b));

// Max
Mono<Integer> max = Flux.just(1, 2, 3)
    .reduce((a, b) -> Math.max(a, b));

// Count
Mono<Long> count = Flux.just(1, 2, 3).count();
```

---

## Scheduling

### Schedulers

```java
// Parallel scheduler
Mono<String> parallelMono = Mono.just("hello")
    .publishOn(Schedulers.parallel());

// Single scheduler
Mono<String> singleMono = Mono.just("hello")
    .publishOn(Schedulers.single());

// Bounded elastic scheduler
Mono<String> elasticMono = Mono.just("hello")
    .publishOn(Schedulers.boundedElastic());

// Cached scheduler
Mono<String> cachedMono = Mono.just("hello")
    .publishOn(Schedulers.fromExecutor(Executors.newCachedThreadPool()));

// Custom scheduler
Scheduler customScheduler = Schedulers.newParallel("custom", 4);
Mono<String> customMono = Mono.just("hello")
    .publishOn(customScheduler);
```

### publishOn vs subscribeOn

```java
// subscribeOn affects entire chain
Flux.range(1, 10)
    .subscribeOn(Schedulers.boundedElastic())
    .map(n -> {
        System.out.println("Map: " + Thread.currentThread().getName());
        return n * 2;
    })
    .subscribe();

// publishOn affects downstream operators
Flux.range(1, 10)
    .publishOn(Schedulers.boundedElastic())
    .map(n -> {
        System.out.println("Map: " + Thread.currentThread().getName());
        return n * 2;
    })
    .subscribe();
```

### Time-Based Operations

```java
// Delay
Mono<String> delayed = Mono.just("hello")
    .delayElement(Duration.ofSeconds(1));

// Delay subscription
Mono<String> delayedSubscription = Mono.just("hello")
    .delaySubscription(Duration.ofSeconds(1));

// Timeout
Mono<String> withTimeout = Mono.just("hello")
    .timeout(Duration.ofSeconds(5));

// Interval
Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

// Timer
Mono<String> timer = Mono.just("hello")
    .delayElement(Duration.ofSeconds(1))
    .elapsed()
    .map(tuple -> "Elapsed: " + tuple.getT1() + "ms");
```

---

## Backpressure

### Backpressure Strategies

```java
// Buffer (default)
Flux.range(1, 1000000)
    .onBackpressureBuffer()
    .subscribe();

// Buffer with size
Flux.range(1, 1000000)
    .onBackpressureBuffer(1000)
    .subscribe();

// Buffer with overflow strategy
Flux.range(1, 1000000)
    .onBackpressureBuffer(1000, OverflowStrategy.DROP_OLDEST)
    .subscribe();

// Drop
Flux.range(1, 1000000)
    .onBackpressureDrop()
    .subscribe();

// Drop with consumer
Flux.range(1, 1000000)
    .onBackpressureDrop(dropped -> 
        System.out.println("Dropped: " + dropped))
    .subscribe();

// Latest
Flux.range(1, 1000000)
    .onBackpressureLatest()
    .subscribe();

// Error
Flux.range(1, 1000000)
    .onBackpressureError()
    .subscribe();
```

### Controlling Demand

```java
// Request specific amount
Flux.range(1, 100)
    .subscribe(value -> {
        System.out.println("Value: " + value);
        // Manually request more
    }, 
    error -> {},
    () -> {});

// Using Subscriber
Flux.range(1, 100)
    .subscribe(new Subscriber<>() {
        private Subscription subscription;
        
        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            subscription.request(10); // Request first 10
        }
        
        @Override
        public void onNext(Integer value) {
            System.out.println("Value: " + value);
            if (value % 10 == 0) {
                subscription.request(10); // Request next 10
            }
        }
        
        @Override
        public void onError(Throwable throwable) {
            System.err.println("Error: " + throwable.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("Completed");
        }
    });
```

### Limit Rate

```java
// Limit request rate
Flux.range(1, 100)
    .limitRate(10) // Request 10, then 10 more when 75% consumed
    .subscribe();

// Limit request rate with prefetch
Flux.range(1, 100)
    .limitRate(10, 5) // Request 10, prefetch 5
    .subscribe();
```

---

## Error Handling

### Error Operators

```java
// onErrorReturn
Flux<String> withDefault = Flux.error(new RuntimeException("Error"))
    .onErrorReturn("default value");

// onErrorReturn with predicate
Flux<String> withConditionalDefault = Flux.error(new RuntimeException("Error"))
    .onErrorReturn(ex -> ex instanceof RuntimeException, "runtime error");

// onErrorResume
Flux<String> withFallback = Flux.error(new RuntimeException("Error"))
    .onErrorResume(ex -> Flux.just("fallback", "values"));

// onErrorMap
Flux<String> withMappedError = Flux.error(new RuntimeException("Error"))
    .onErrorMap(ex -> new ApiException("Mapped error", ex));

// retry
Flux<String> withRetry = Flux.error(new RuntimeException("Error"))
    .retry(3);

// retryWhen
Flux<String> withRetryWhen = Flux.error(new RuntimeException("Error"))
    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));

// switchIfEmpty
Flux<String> withFallbackOnEmpty = Flux.empty()
    .switchIfEmpty(Flux.just("fallback"));
```

### Error Handling Example

```java
Flux.range(1, 10)
    .map(n -> {
        if (n == 5) {
            throw new RuntimeException("Error at 5");
        }
        return n;
    })
    .onErrorResume(ex -> {
        System.err.println("Error: " + ex.getMessage());
        return Flux.range(100, 5);
    })
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Final error: " + error.getMessage()),
        () -> System.out.println("Completed")
    );
```

### Exception-Specific Handling

```java
Flux<String> flux = Flux.range(1, 10)
    .map(n -> {
        if (n == 3) throw new IllegalArgumentException("Illegal argument");
        if (n == 7) throw new RuntimeException("Runtime error");
        return "Value " + n;
    })
    .onErrorReturn(IllegalArgumentException.class, "Illegal argument fallback")
    .onErrorResume(RuntimeException.class, ex -> 
        Flux.just("Runtime error fallback"))
    .onErrorReturn("General fallback");
```

---

## Best Practices

### 1. Use Appropriate Reactive Types

```java
// Good - Mono for single values
Mono<User> getUser(String id) {
    return userRepository.findById(id);
}

// Good - Flux for multiple values
Flux<User> getAllUsers() {
    return userRepository.findAll();
}

// Avoid - Converting Flux to List unnecessarily
Mono<List<User>> getAllUsersAsList() {
    return userRepository.findAll().collectList();
}
```

### 2. Handle Backpressure

```java
// Good - With backpressure handling
Flux.range(1, 1000000)
    .onBackpressureBuffer(1000)
    .flatMap(this::processItem, 10) // Concurrency limit
    .subscribe();
```

### 3. Use Proper Timeouts

```java
// Good - With timeout
Mono<User> getUser(String id) {
    return userRepository.findById(id)
        .timeout(Duration.ofSeconds(5))
        .onErrorResume(TimeoutException.class, e -> 
            Mono.error(new ServiceUnavailableException("Service timeout")));
}
```

### 4. Prefer Functional Operators

```java
// Good - Using operators
Flux<String> result = Flux.just("a", "b", "c")
    .map(String::toUpperCase)
    .filter(s -> s.length() > 1)
    .collectList()
    .flatMapMany(Flux::fromIterable);
```

### 5. Test Reactive Code

```java
StepVerifier.create(Flux.just("a", "b", "c"))
    .expectNext("A", "B", "C")
    .verifyComplete();

StepVerifier.create(Mono.error(new RuntimeException("Error")))
    .expectError(RuntimeException.class)
    .verify();
```

---

## Common Pitfalls

### 1. Blocking in Reactive Code

```java
// Bad - Blocking call
Mono<String> result = Mono.fromCallable(() -> {
    return blockingService.getData(); // Don't do this!
});

// Good - Non-blocking
Mono<String> result = nonBlockingService.getData();
```

### 2. Not Handling Errors

```java
// Bad - No error handling
Flux<String> flux = Flux.range(1, 10)
    .map(n -> {
        if (n == 5) throw new RuntimeException("Error");
        return "Value " + n;
    });

// Good - With error handling
Flux<String> flux = Flux.range(1, 10)
    .map(n -> {
        if (n == 5) throw new RuntimeException("Error");
        return "Value " + n;
    })
    .onErrorResume(ex -> Flux.just("fallback"));
```

### 3. Ignoring Backpressure

```java
// Bad - No backpressure handling
Flux.range(1, 1000000)
    .flatMap(this::processItem)
    .subscribe();

// Good - With backpressure
Flux.range(1, 1000000)
    .onBackpressureBuffer(1000)
    .flatMap(this::processItem, 10)
    .subscribe();
```

---

## Further Reading

- [Project Reactor Official Documentation](https://projectreactor.io/)
- [Reactor Core Reference](https://projectreactor.io/docs)
- [Reactor Wiki](https://github.com/reactor/reactor-core/wiki)
- [Baeldung Reactor](https://www.baeldung.com/reactor-core)
