# RxJava

## Comprehensive Guide to RxJava

RxJava is a reactive programming library for Java that provides Observable, Single, Maybe, Completable and operators for composing asynchronous and event-based programs. This guide covers reactive types, operators, and best practices.

---

## Table of Contents

1. [Observable](#observable)
2. [Single](#single)
3. [Maybe](#maybe)
4. [Completable](#completable)
5. [Operators](#operators)
6. [Scheduler](#scheduler)
7. [Best Practices](#best-practices)

---

## Observable

### Creating Observable

```java
// Empty Observable
Observable<String> emptyObservable = Observable.empty();

// Observable with values
Observable<String> fromValues = Observable.just("a", "b", "c");

// Observable from array
Observable<String> fromArray = Observable.fromArray(new String[]{"a", "b", "c"});

// Observable from iterable
Observable<String> fromIterable = Observable.fromIterable(List.of("a", "b", "c"));

// Observable from callable
Observable<String> fromCallable = Observable.fromCallable(() -> {
    // Expensive computation
    return "Result";
});

// Observable from publisher
Observable<String> fromPublisher = Observable.from(publisher);

// Observable with create
Observable<String> created = Observable.create(emitter -> {
    emitter.onNext("1");
    emitter.onNext("2");
    emitter.onNext("3");
    emitter.onComplete();
});

// Observable with range
Observable<Integer> range = Observable.range(1, 10);

// Observable with interval
Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
```

### Observable Operations

```java
// Map transformation
Observable<String> mapped = Observable.just("hello")
    .map(String::toUpperCase);

// FlatMap transformation
Observable<String> flatMapped = Observable.just("hello")
    .flatMap(s -> Observable.just(s + " world"));

// ConcatMap transformation
Observable<String> concatMapped = Observable.just("hello")
    .concatMap(s -> Observable.just(s + " world"));

// SwitchMap transformation
Observable<String> switchMapped = Observable.just("hello")
    .switchMap(s -> Observable.just(s + " world"));

// Filter
Observable<String> filtered = Observable.just("hello")
    .filter(s -> s.length() > 3);

// Distinct
Observable<String> distinct = Observable.just("a", "b", "a", "c")
    .distinct();

// Distinct until changed
Observable<String> distinctUntilChanged = Observable.just("a", "a", "b", "b", "c")
    .distinctUntilChanged();

// Take
Observable<String> taken = Observable.just("a", "b", "c", "d")
    .take(3);

// Take while
Observable<String> takenWhile = Observable.just("a", "bb", "ccc", "dddd")
    .takeWhile(s -> s.length() <= 2);

// Skip
Observable<String> skipped = Observable.just("a", "b", "c", "d")
    .skip(2);

// Skip while
Observable<String> skippedWhile = Observable.just("a", "bb", "ccc", "dddd")
    .skipWhile(s -> s.length() <= 1);
```

### Observable Subscribing

```java
// Subscribe with observer
Observable.just("hello")
    .subscribe(new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            // Handle subscription
        }
        
        @Override
        public void onNext(String s) {
            System.out.println("Value: " + s);
        }
        
        @Override
        public void onError(Throwable e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("Completed");
        }
    });

// Subscribe with consumer
Observable.just("hello")
    .subscribe(value -> System.out.println("Value: " + value));

// Subscribe with consumer and error handler
Observable.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage())
    );

// Subscribe with consumer, error handler, and complete handler
Observable.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage()),
        () -> System.out.println("Completed")
    );

// Blocking get (only in non-reactive code)
String value = Observable.just("hello").blockingFirst();

// Blocking to list
List<String> values = Observable.just("a", "b", "c").blockingToList();
```

---

## Single

### Creating Single

```java
// Single with value
Single<String> single = Single.just("hello");

// Single from callable
Single<String> fromCallable = Single.fromCallable(() -> {
    // Expensive computation
    return "Result";
});

// Single from future
Single<String> fromFuture = Single.fromFuture(CompletableFuture.supplyAsync(() -> "Async result"));

// Single from publisher
Single<String> fromPublisher = Single.from(publisher);

// Single with create
Single<String> created = Single.create(emitter -> {
    emitter.onSuccess("value");
});
```

### Single Operations

```java
// Map transformation
Single<String> mapped = Single.just("hello")
    .map(String::toUpperCase);

// FlatMap transformation
Single<String> flatMapped = Single.just("hello")
    .flatMap(s -> Single.just(s + " world"));

// Filter (converts to Maybe)
Maybe<String> filtered = Single.just("hello")
    .filter(s -> s.length() > 3);

// Default if empty (converts to Observable)
Observable<String> withDefault = Single.just("hello")
    .toObservable();

// Switch if empty
Single<String> withSwitch = Single.error(new RuntimeException("Error"))
    .switchIfEmpty(Single.just("fallback"));

// Zip with other Single
Single<String> zipped = Single.just("hello")
    .zipWith(Single.just(" world"), (s1, s2) -> s1 + s2);

// Cache
Single<String> cached = Single.just("expensive result")
    .cache();
```

### Single Subscribing

```java
// Subscribe with observer
Single.just("hello")
    .subscribe(new SingleObserver<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            // Handle subscription
        }
        
        @Override
        public void onSuccess(String s) {
            System.out.println("Value: " + s);
        }
        
        @Override
        public void onError(Throwable e) {
            System.err.println("Error: " + e.getMessage());
        }
    });

// Subscribe with consumer
Single.just("hello")
    .subscribe(value -> System.out.println("Value: " + value));

// Subscribe with consumer and error handler
Single.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage())
    );

// Blocking get
String value = Single.just("hello").blockingGet();

// Blocking get with default
String value = Single.just("hello").blockingGet("default");
```

---

## Maybe

### Creating Maybe

```java
// Maybe with value
Maybe<String> maybe = Maybe.just("hello");

// Maybe empty
Maybe<String> empty = Maybe.empty();

// Maybe from callable
Maybe<String> fromCallable = Maybe.fromCallable(() -> {
    // May return value or empty
    if (someCondition) {
        return "value";
    }
    return null;
});

// Maybe from publisher
Maybe<String> fromPublisher = Maybe.from(publisher);

// Maybe with create
Maybe<String> created = Maybe.create(emitter -> {
    if (someCondition) {
        emitter.onSuccess("value");
    } else {
        emitter.onComplete();
    }
});
```

### Maybe Operations

```java
// Map transformation
Maybe<String> mapped = Maybe.just("hello")
    .map(String::toUpperCase);

// FlatMap transformation
Maybe<String> flatMapped = Maybe.just("hello")
    .flatMap(s -> Maybe.just(s + " world"));

// Filter
Maybe<String> filtered = Maybe.just("hello")
    .filter(s -> s.length() > 3);

// Default if empty
Maybe<String> withDefault = Maybe.empty()
    .defaultIfEmpty("default value");

// Switch if empty
Maybe<String> withSwitch = Maybe.empty()
    .switchIfEmpty(Maybe.just("fallback"));

// To observable
Observable<String> toObservable = Maybe.just("hello")
    .toObservable();

// To single
Single<String> toSingle = Maybe.just("hello")
    .toSingle("default");

// To single or error
Single<String> toSingleOrError = Maybe.empty()
    .toSingleError(() -> new RuntimeException("Empty"));
```

### Maybe Subscribing

```java
// Subscribe with observer
Maybe.just("hello")
    .subscribe(new MaybeObserver<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            // Handle subscription
        }
        
        @Override
        public void onSuccess(String s) {
            System.out.println("Value: " + s);
        }
        
        @Override
        public void onError(Throwable e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("Completed without value");
        }
    });

// Subscribe with consumer
Maybe.just("hello")
    .subscribe(value -> System.out.println("Value: " + value));

// Subscribe with consumer and complete handler
Maybe.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        () -> System.out.println("Completed without value")
    );

// Subscribe with consumer, error handler, and complete handler
Maybe.just("hello")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage()),
        () -> System.out.println("Completed without value")
    );

// Blocking get
String value = Maybe.just("hello").blockingGet();

// Blocking get with default
String value = Maybe.empty().blockingGet("default");
```

---

## Completable

### Creating Completable

```java
// Completable complete
Completable complete = Completable.complete();

// Completable from action
Completable fromAction = Completable.fromAction(() -> {
    // Perform action
    System.out.println("Action performed");
});

// Completable from runnable
Completable fromRunnable = Completable.fromRunnable(() -> {
    // Run code
    System.out.println("Runnable executed");
});

// Completable from callable
Completable fromCallable = Completable.fromCallable(() -> {
    // May throw exception
    if (someCondition) {
        throw new RuntimeException("Error");
    }
});

// Completable from future
Completable fromFuture = Completable.fromFuture(CompletableFuture.runAsync(() -> {
    // Async action
}));

// Completable from publisher
Completable fromPublisher = Completable.from(publisher);

// Completable with create
Completable created = Completable.create(emitter -> {
    // Perform action
    emitter.onComplete();
});
```

### Completable Operations

```java
// And then (chain with Single)
Single<String> andThen = Completable.complete()
    .andThen(Single.just("hello"));

// And then (chain with Observable)
Observable<String> andThenObservable = Completable.complete()
    .andThen(Observable.just("hello", "world"));

// And then (chain with Completable)
Completable andThenCompletable = Completable.complete()
    .andThen(Completable.complete());

// Do on complete
Completable withDoOnComplete = Completable.complete()
    .doOnComplete(() -> System.out.println("Completed"));

// Do on error
Completable withDoOnError = Completable.complete()
    .doOnError(error -> System.err.println("Error: " + error.getMessage()));

// Do on subscribe
Completable withDoOnSubscribe = Completable.complete()
    .doOnSubscribe(disposable -> System.out.println("Subscribed"));

// Do on dispose
Completable withDoOnDispose = Completable.complete()
    .doOnDispose(() -> System.out.println("Disposed"));

// Timeout
Completable withTimeout = Completable.complete()
    .timeout(5, TimeUnit.SECONDS);

// Retry
Completable withRetry = Completable.error(new RuntimeException("Error"))
    .retry(3);

// Retry when
Completable withRetryWhen = Completable.error(new RuntimeException("Error"))
    .retryWhen(errors -> errors.delay(1, TimeUnit.SECONDS));
```

### Completable Subscribing

```java
// Subscribe with observer
Completable.complete()
    .subscribe(new CompletableObserver() {
        @Override
        public void onSubscribe(Disposable d) {
            // Handle subscription
        }
        
        @Override
        public void onComplete() {
            System.out.println("Completed");
        }
        
        @Override
        public void onError(Throwable e) {
            System.err.println("Error: " + e.getMessage());
        }
    });

// Subscribe with action
Completable.complete()
    .subscribe(() -> System.out.println("Completed"));

// Subscribe with action and error handler
Completable.complete()
    .subscribe(
        () -> System.out.println("Completed"),
        error -> System.err.println("Error: " + error.getMessage())
    );

// Blocking await
Completable.complete().blockingAwait();

// Blocking await with timeout
Completable.complete().blockingAwait(5, TimeUnit.SECONDS);
```

---

## Operators

### Map and FlatMap

```java
// Map - 1:1 transformation
Observable<String> mapped = Observable.just(1, 2, 3)
    .map(n -> "Number " + n);

// FlatMap - 1:N transformation
Observable<String> flatMapped = Observable.just(1, 2, 3)
    .flatMap(n -> Observable.just("A" + n, "B" + n));

// ConcatMap - FlatMap with order preservation
Observable<String> concatMapped = Observable.just(1, 2, 3)
    .concatMap(n -> Observable.just("A" + n, "B" + n));

// SwitchMap - Cancel previous and start new
Observable<String> switchMapped = Observable.just(1, 2, 3)
    .switchMap(n -> Observable.just("Result " + n));
```

### Filtering

```java
// Filter
Observable<String> filtered = Observable.just("a", "bb", "ccc")
    .filter(s -> s.length() > 1);

// Distinct
Observable<String> distinct = Observable.just("a", "b", "a", "c")
    .distinct();

// Distinct until changed
Observable<String> distinctUntilChanged = Observable.just("a", "a", "b", "b", "c")
    .distinctUntilChanged();

// Take
Observable<String> taken = Observable.just("a", "b", "c", "d")
    .take(3);

// Take while
Observable<String> takenWhile = Observable.just("a", "bb", "ccc", "dddd")
    .takeWhile(s -> s.length() <= 2);

// Take until
Observable<String> takenUntil = Observable.just("a", "bb", "ccc", "dddd")
    .takeUntil(s -> s.length() > 2);

// Skip
Observable<String> skipped = Observable.just("a", "b", "c", "d")
    .skip(2);

// Skip while
Observable<String> skippedWhile = Observable.just("a", "bb", "ccc", "dddd")
    .skipWhile(s -> s.length() <= 1);
```

### Combining

```java
// Zip
Observable<String> zipped = Observable.just("a", "b", "c")
    .zipWith(Observable.just("1", "2", "3"),
        (s1, s2) -> s1 + s2);

// Zip with function
Observable<String> zippedWith = Observable.zip(
    Observable.just("a", "b", "c"),
    Observable.just("1", "2", "3"),
    (s1, s2) -> s1 + s2);

// Merge
Observable<String> merged = Observable.merge(
    Observable.just("a", "b"),
    Observable.just("c", "d"));

// Concat
Observable<String> concatenated = Observable.concat(
    Observable.just("a", "b"),
    Observable.just("c", "d"));

// Start with
Observable<String> startedWith = Observable.just("b", "c")
    .startWith("a");

// Start with array
Observable<String> startedWithArray = Observable.just("c", "d")
    .startWithArray("a", "b");
```

### Buffering and Windowing

```java
// Buffer by size
Observable<List<String>> buffered = Observable.just("a", "b", "c", "d", "e")
    .buffer(3);

// Buffer by time
Observable<List<String>> bufferedByTime = Observable.interval(1, TimeUnit.SECONDS)
    .take(10)
    .buffer(3, TimeUnit.SECONDS);

// Window by size
Observable<Observable<String>> windowed = Observable.just("a", "b", "c", "d", "e")
    .window(3);

// Window by time
Observable<Observable<String>> windowedByTime = Observable.interval(1, TimeUnit.SECONDS)
    .take(10)
    .window(3, TimeUnit.SECONDS);
```

### Mathematical Operations

```java
// Sum
Single<Integer> sum = Observable.just(1, 2, 3)
    .reduce(0, Integer::sum)
    .toSingle();

// Average
Single<Double> average = Observable.just(1, 2, 3)
    .reduce(0.0, (acc, n) -> acc + n)
    .map(sum -> sum / 3.0)
    .toSingle();

// Min
Single<Integer> min = Observable.just(1, 2, 3)
    .reduce((a, b) -> Math.min(a, b))
    .toSingle();

// Max
Single<Integer> max = Observable.just(1, 2, 3)
    .reduce((a, b) -> Math.max(a, b))
    .toSingle();

// Count
Single<Long> count = Observable.just(1, 2, 3).count();
```

---

## Scheduler

### Schedulers

```java
// Computation scheduler
Observable<String> computationObservable = Observable.just("hello")
    .subscribeOn(Schedulers.computation());

// IO scheduler
Observable<String> ioObservable = Observable.just("hello")
    .subscribeOn(Schedulers.io());

// Single scheduler
Observable<String> singleObservable = Observable.just("hello")
    .subscribeOn(Schedulers.single());

// New thread scheduler
Observable<String> newThreadObservable = Observable.just("hello")
    .subscribeOn(Schedulers.newThread());

// Trampoline scheduler
Observable<String> trampolineObservable = Observable.just("hello")
    .subscribeOn(Schedulers.trampoline());

// Custom scheduler
Scheduler customScheduler = Schedulers.fromExecutor(Executors.newFixedThreadPool(4));
Observable<String> customObservable = Observable.just("hello")
    .subscribeOn(customScheduler);
```

### subscribeOn vs observeOn

```java
// subscribeOn affects entire chain
Observable.range(1, 10)
    .subscribeOn(Schedulers.io())
    .map(n -> {
        System.out.println("Map: " + Thread.currentThread().getName());
        return n * 2;
    })
    .subscribe();

// observeOn affects downstream operators
Observable.range(1, 10)
    .observeOn(Schedulers.io())
    .map(n -> {
        System.out.println("Map: " + Thread.currentThread().getName());
        return n * 2;
    })
    .subscribe();
```

### Time-Based Operations

```java
// Delay
Observable<String> delayed = Observable.just("hello")
    .delay(1, TimeUnit.SECONDS);

// Delay with scheduler
Observable<String> delayedWithScheduler = Observable.just("hello")
    .delay(1, TimeUnit.SECONDS, Schedulers.io());

// Timeout
Observable<String> withTimeout = Observable.just("hello")
    .timeout(5, TimeUnit.SECONDS);

// Interval
Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);

// Timer
Observable<String> timer = Observable.just("hello")
    .delay(1, TimeUnit.SECONDS)
    .timestamp()
    .map(timestamped -> "Timestamp: " + timestamped.timestamp());
```

---

## Best Practices

### 1. Use Appropriate Reactive Types

```java
// Good - Single for single values
Single<User> getUser(String id) {
    return userRepository.findById(id);
}

// Good - Observable for multiple values
Observable<User> getAllUsers() {
    return userRepository.findAll();
}

// Good - Maybe for optional values
Maybe<User> findUser(String id) {
    return userRepository.findById(id);
}

// Good - Completable for actions without return value
Completable saveUser(User user) {
    return Completable.fromAction(() -> userRepository.save(user));
}
```

### 2. Handle Errors

```java
// Good - With error handling
Observable<String> observable = Observable.just("a", "b", "c")
    .map(s -> {
        if (s.equals("b")) throw new RuntimeException("Error");
        return s;
    })
    .onErrorReturn("fallback")
    .subscribe(
        value -> System.out.println("Value: " + value),
        error -> System.err.println("Error: " + error.getMessage()),
        () -> System.out.println("Completed")
    );
```

### 3. Use Proper Schedulers

```java
// Good - Using appropriate schedulers
Observable.just("hello")
    .subscribeOn(Schedulers.io()) // For I/O operations
    .observeOn(Schedulers.computation()) // For CPU-intensive operations
    .subscribe();
```

### 4. Handle Backpressure

```java
// Good - With backpressure handling
Observable.range(1, 1000000)
    .onBackpressureBuffer(1000)
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.computation())
    .subscribe();
```

### 5. Test Reactive Code

```java
TestObserver<String> testObserver = Observable.just("a", "b", "c")
    .test();

testObserver.assertValues("a", "b", "c");
testObserver.assertComplete();
testObserver.assertNoErrors();
```

---

## Common Pitfalls

### 1. Blocking in Reactive Code

```java
// Bad - Blocking call
Observable<String> result = Observable.fromCallable(() -> {
    return blockingService.getData(); // Don't do this!
});

// Good - Non-blocking
Observable<String> result = nonBlockingService.getData();
```

### 2. Not Handling Errors

```java
// Bad - No error handling
Observable<String> observable = Observable.just("a", "b", "c")
    .map(s -> {
        if (s.equals("b")) throw new RuntimeException("Error");
        return s;
    });

// Good - With error handling
Observable<String> observable = Observable.just("a", "b", "c")
    .map(s -> {
        if (s.equals("b")) throw new RuntimeException("Error");
        return s;
    })
    .onErrorReturn("fallback");
```

### 3. Ignoring Backpressure

```java
// Bad - No backpressure handling
Observable.range(1, 1000000)
    .flatMap(this::processItem)
    .subscribe();

// Good - With backpressure
Observable.range(1, 1000000)
    .onBackpressureBuffer(1000)
    .flatMap(this::processItem, 10)
    .subscribe();
```

---

## Further Reading

- [RxJava Official Documentation](https://github.com/ReactiveX/RxJava)
- [RxJava Wiki](https://github.com/ReactiveX/RxJava/wiki)
- [Baeldung RxJava](https://www.baeldung.com/rx-java)
- [ReactiveX](http://reactivex.io/)
