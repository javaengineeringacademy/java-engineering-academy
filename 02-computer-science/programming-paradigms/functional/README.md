# Functional Programming (FP)

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
  - [Pure Functions](#pure-functions)
  - [Immutability](#immutability)
  - [First-Class and Higher-Order Functions](#first-class-and-higher-order-functions)
  - [Closures](#closures)
  - [Currying and Partial Application](#currying-and-partial-application)
  - [Function Composition](#function-composition)
- [Advanced Concepts](#advanced-concepts)
  - [Monads](#monads)
  - [Functors](#functors)
  - [Lazy Evaluation](#lazy-evaluation)
- [FP in Different Languages](#fp-in-different-languages)
  - [Haskell](#haskell)
  - [Python](#python)
  - [JavaScript](#javascript)
  - [Java](#java)
- [FP vs OOP](#fp-vs-oop)
- [Common Patterns](#common-patterns)

---

## Overview

Functional Programming is a programming paradigm that treats computation as the evaluation of mathematical functions and avoids changing state and mutable data.

### Core Principles

1. **Pure functions** - No side effects, same input always gives same output
2. **Immutability** - Data cannot be changed after creation
3. **First-class functions** - Functions are values
4. **Declarative style** - Describe what to do, not how

### Why Functional Programming?

- **Easier to test** - Pure functions are deterministic
- **Easier to reason about** - No hidden state changes
- **Better concurrency** - No shared mutable state
- **Composable** - Functions combine like building blocks
- **More maintainable** - Less coupling between components

---

## Core Concepts

### Pure Functions

A pure function:
1. Always returns the same output for the same input
2. Has no side effects

```
Pure Function:
┌─────────────┐      ┌─────────────┐
│   Input     │ ───► │   Output    │
│   (x, y)    │      │   (result)  │
└─────────────┘      └─────────────┘
  No external state changes

Impure Function:
┌─────────────┐      ┌─────────────┐
│   Input     │ ───► │   Output    │
│   (x, y)    │      │   (result)  │
└─────────────┘      └─────────────┘
        │
        ▼
  ┌─────────────┐
  │ Side Effect │
  │ (DB, File,  │
  │  Global)    │
  └─────────────┘
```

#### Examples

```python
# PURE FUNCTION - no side effects
def add(a: int, b: int) -> int:
    return a + b

def calculate_discount(price: float, discount_percent: float) -> float:
    return price * (1 - discount_percent / 100)

# IMPURE FUNCTION - has side effects
total = 0
def add_to_total(value: int) -> int:
    global total  # Modifies external state
    total += value
    return total

# IMPURE FUNCTION - depends on external state
def get_user_greeting(name: str) -> str:
    from datetime import datetime
    hour = datetime.now().hour  # Depends on current time
    if hour < 12:
        return f"Good morning, {name}!"
    return f"Good afternoon, {name}!"
```

```javascript
// PURE
const add = (a, b) => a + b;
const multiply = (x, y) => x * y;

// IMPURE
let counter = 0;
const increment = () => ++counter;  // Modifies external state

// PURE
const pureIncrement = (count) => count + 1;
```

---

### Immutability

Data cannot be modified after creation. Instead of changing data, you create new copies with the desired changes.

```python
# MUTABLE (imperative style)
user = {"name": "Alice", "age": 30}
user["age"] = 31  # Mutates original object

# IMMUTABLE (functional style)
from typing import Dict, Any

def update_age(user: Dict[str, Any], new_age: int) -> Dict[str, Any]:
    return {**user, "age": new_age}  # Creates new dictionary

user = {"name": "Alice", "age": 30}
new_user = update_age(user, 31)
print(user)      # {"name": "Alice", "age": 30} - unchanged
print(new_user)  # {"name": "Alice", "age": 31} - new copy
```

```javascript
// MUTABLE
const array = [1, 2, 3];
array.push(4);  // Mutates original array

// IMMUTABLE
const addToArray = (arr, item) => [...arr, item];  // Spread operator
const newArray = addToArray([1, 2, 3], 4);
console.log(newArray);  // [1, 2, 3, 4]
// Original array unchanged
```

```javascript
// Immutable updates with nested objects
const state = {
    user: {
        name: "Alice",
        address: {
            city: "Seattle",
            zip: "98101"
        }
    }
};

// Deep update - creates new objects at each level
const newState = {
    ...state,
    user: {
        ...state.user,
        address: {
            ...state.user.address,
            zip: "98102"
        }
    }
};
```

---

### First-Class and Higher-Order Functions

**First-class functions**: Functions are treated as values - can be assigned, passed, returned.

**Higher-order functions**: Functions that take functions as arguments or return functions.

```python
from typing import Callable, List

# First-class functions - assign to variables
def greet(name: str) -> str:
    return f"Hello, {name}!"

say_hello = greet  # Function as variable
print(say_hello("World"))

# Higher-order function - takes function as argument
def apply_operation(func: Callable[[int, int], int], a: int, b: int) -> int:
    return func(a, b)

def multiply(x: int, y: int) -> int:
    return x * y

result = apply_operation(multiply, 3, 4)  # 12

# Higher-order function - returns function
def create_multiplier(factor: int) -> Callable[[int], int]:
    def multiplier(x: int) -> int:
        return x * factor
    return multiplier

double = create_multiplier(2)
triple = create_multiplier(3)
print(double(5))  # 10
print(triple(5))  # 15
```

#### Common Higher-Order Functions

```python
from functools import reduce
from typing import List, Callable, Any

numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

# MAP - transform each element
squares = list(map(lambda x: x ** 2, numbers))
# [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]

# FILTER - keep elements that satisfy condition
evens = list(filter(lambda x: x % 2 == 0, numbers))
# [2, 4, 6, 8, 10]

# REDUCE - combine elements into single value
total = reduce(lambda acc, x: acc + x, numbers, 0)
# 55

# LIST COMPREHENSION (Pythonic way)
squares = [x ** 2 for x in numbers]
evens = [x for x in numbers if x % 2 == 0]
```

```javascript
const numbers = [1, 2, 3, 4, 5];

// MAP
const squares = numbers.map(x => x ** 2);
// [1, 4, 9, 16, 25]

// FILTER
const evens = numbers.filter(x => x % 2 === 0);
// [2, 4]

// REDUCE
const sum = numbers.reduce((acc, x) => acc + x, 0);
// 15

// CHAINING
const result = numbers
    .filter(x => x % 2 === 0)
    .map(x => x ** 2)
    .reduce((acc, x) => acc + x, 0);
// 20 (4 + 16)
```

---

### Closures

A closure is a function that remembers the variables from its enclosing scope, even when the function is executed outside that scope.

```python
def create_counter(initial: int = 0):
    """Creates a counter with private state using closure."""
    count = [initial]  # Using list to allow mutation in closure

    def increment():
        count[0] += 1
        return count[0]

    def decrement():
        count[0] -= 1
        return count[0]

    def get_count():
        return count[0]

    return {
        "increment": increment,
        "decrement": decrement,
        "get_count": get_count
    }

counter = create_counter(10)
print(counter["increment"]())  # 11
print(counter["increment"]())  # 12
print(counter["decrement"]())  # 11
print(counter["get_count"]())  # 11
```

```javascript
// Closure for private variables
function createBankAccount(initialBalance) {
    let balance = initialBalance;  // Private variable

    return {
        deposit(amount) {
            if (amount > 0) {
                balance += amount;
                return `Deposited $${amount}. Balance: $${balance}`;
            }
        },
        withdraw(amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                return `Withdrew $${amount}. Balance: $${balance}`;
            }
            return "Insufficient funds";
        },
        getBalance() {
            return balance;
        }
    };
}

const account = createBankAccount(1000);
console.log(account.deposit(500));    // Deposited $500. Balance: $1500
console.log(account.withdraw(200));   // Withdrew $200. Balance: $1300
console.log(account.getBalance());    // 1300
// console.log(balance);              // ReferenceError - balance is private
```

---

### Currying and Partial Application

**Currying**: Transform a function that takes multiple arguments into a series of functions that each take one argument.

**Partial Application**: Fix some arguments of a function and return a new function with fewer arguments.

```python
from typing import Callable

# CURRYING
def curry(f: Callable) -> Callable:
    def curried(*args):
        if len(args) >= f.__code__.co_argcount:
            return f(*args)
        return lambda *more: curried(*(args + more))
    return curried

@curry
def add(a: int, b: int) -> int:
    return a + b

add5 = add(5)
print(add5(3))   # 8
print(add(2)(3))  # 5

# PARTIAL APPLICATION
from functools import partial

def power(base: float, exponent: float) -> float:
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)

print(square(5))  # 25
print(cube(5))    # 125
```

```javascript
// CURRYING
const curry = (fn) => {
    const arity = fn.length;
    return function curried(...args) {
        if (args.length >= arity) {
            return fn(...args);
        }
        return (...moreArgs) => curried(...args, ...moreArgs);
    };
};

const add = curry((a, b) => a + b);
const add5 = add(5);
console.log(add5(3));  // 8
console.log(add(2)(3));  // 5

// PARTIAL APPLICATION
const partial = (fn, ...args) => (...moreArgs) => fn(...args, ...moreArgs);
const multiply = (a, b) => a * b;
const double = partial(multiply, 2);
console.log(double(5));  // 10
```

---

### Function Composition

Combining simple functions to build more complex ones.

```
f: A → B
g: B → C
g ∘ f: A → C  (composition)

┌─────┐    ┌─────┐    ┌─────┐
│  A  │──f──│  B  │──g──│  C  │
└─────┘    └─────┘    └─────┘

Composition: A → C directly
```

```python
from typing import Callable

def compose(*functions: Callable) -> Callable:
    """Right-to-left function composition."""
    def composed(x):
        result = x
        for f in reversed(functions):
            result = f(result)
        return result
    return composed

def pipe(*functions: Callable) -> Callable:
    """Left-to-right function composition."""
    def piped(x):
        result = x
        for f in functions:
            result = f(result)
        return result
    return piped

# Example transformations
def double(x: int) -> int:
    return x * 2

def add_ten(x: int) -> int:
    return x + 10

def square(x: int) -> int:
    return x ** 2

# Compose: square(add_ten(double(x)))
transform = compose(square, add_ten, double)
print(transform(3))  # square(add_ten(double(3))) = square(add_ten(6)) = square(16) = 256

# Pipe: double -> add_ten -> square
transform_pipe = pipe(double, add_ten, square)
print(transform_pipe(3))  # double(3) = 6 -> add_ten(6) = 16 -> square(16) = 256
```

```javascript
// Function composition
const compose = (...fns) => (x) => fns.reduceRight((acc, fn) => fn(acc), x);
const pipe = (...fns) => (x) => fns.reduce((acc, fn) => fn(acc), x);

const double = x => x * 2;
const addTen = x => x + 10;
const square = x => x ** 2;

const transform = compose(square, addTen, double);
console.log(transform(3));  // 256

const transformPipe = pipe(double, addTen, square);
console.log(transformPipe(3));  // 256
```

---

## Advanced Concepts

### Functors

A functor is a type that can be mapped over (like a container with a map function).

```python
from typing import Callable, Any

class Maybe:
    """Maybe functor - handles optional values."""

    def __init__(self, value: Any = None):
        self._value = value

    @classmethod
    def just(cls, value: Any) -> 'Maybe':
        return cls(value)

    @classmethod
    def nothing(cls) -> 'Maybe':
        return cls(None)

    def map(self, func: Callable) -> 'Maybe':
        if self._value is None:
            return self.nothing()
        return Maybe.just(func(self._value))

    def flat_map(self, func: Callable) -> 'Maybe':
        if self._value is None:
            return self.nothing()
        return func(self._value)

    def get_or_else(self, default: Any) -> Any:
        return self._value if self._value is not None else default

    def __repr__(self) -> str:
        return f"Just({self._value})" if self._value is not None else "Nothing"

# Usage
result = Maybe.just(10)
    .map(lambda x: x * 2)
    .map(lambda x: x + 5)
    .get_or_else(0)
print(result)  # 25

# With None value
result = Maybe.nothing()
    .map(lambda x: x * 2)
    .map(lambda x: x + 5)
    .get_or_else(0)
print(result)  # 0
```

---

### Monads

A monad is a design pattern that allows chaining operations while managing side effects (like I/O, exceptions, or async operations).

```
Monad: A type that:
1. Wraps a value (unit/return)
2. Chains operations (bind/flatMap)
3. Follows three laws:
   - Left identity: return a >>= f ≡ f a
   - Right identity: m >>= return ≡ m
   - Associativity: (m >>= f) >>= g ≡ m >>= (\x -> f x >>= g)
```

```python
from typing import Callable, Any, List, Optional

class Maybe:
    """Maybe monad for handling optional values."""

    def __init__(self, value: Any = None):
        self._value = value

    @classmethod
    def unit(cls, value: Any) -> 'Maybe':
        return cls(value)

    def bind(self, func: Callable) -> 'Maybe':
        if self._value is None:
            return Maybe(None)
        return func(self._value)

    def __repr__(self) -> str:
        return f"Just({self._value})" if self._value is not None else "Nothing"

# Safe division using Maybe monad
def safe_divide(x: float, y: float) -> Maybe:
    if y == 0:
        return Maybe(None)
    return Maybe.unit(x / y)

def square(x: float) -> Maybe:
    return Maybe.unit(x ** 2)

# Chaining operations
result = Maybe.unit(100) \
    .bind(lambda x: safe_divide(x, 4)) \
    .bind(lambda x: square(x)) \
    .bind(lambda x: safe_divide(x, 2))
print(result)  # Just(312.5)

# With division by zero
result = Maybe.unit(100) \
    .bind(lambda x: safe_divide(x, 0)) \
    .bind(lambda x: square(x))  # Short-circuits
print(result)  # Nothing
```

```python
class Either:
    """Either monad for handling success/failure."""

    def __init__(self, value: Any = None, error: str = None):
        self._value = value
        self._error = error

    @classmethod
    def right(cls, value: Any) -> 'Either':
        return cls(value=value)

    @classmethod
    def left(cls, error: str) -> 'Either':
        return cls(error=error)

    def bind(self, func: Callable) -> 'Either':
        if self._error:
            return self
        return func(self._value)

    def is_right(self) -> bool:
        return self._error is None

    def __repr__(self) -> str:
        if self._error:
            return f"Left({self._error})"
        return f"Right({self._value})"

# Usage
def parse_int(s: str) -> Either:
    try:
        return Either.right(int(s))
    except ValueError:
        return Either.left(f"Cannot parse '{s}' as integer")

def double_if_positive(n: int) -> Either:
    if n > 0:
        return Either.right(n * 2)
    return Either.left("Number must be positive")

result = parse_int("42") \
    .bind(double_if_positive)
print(result)  # Right(84)

result = parse_int("abc") \
    .bind(double_if_positive)
print(result)  # Left(Cannot parse 'abc' as integer)
```

---

### Lazy Evaluation

Expressions are not evaluated until their values are needed.

```python
from typing import Iterator, Callable, Any

class LazyList:
    """A list that generates elements on demand."""

    def __init__(self, generator: Callable[[], Iterator]):
        self._generator = generator
        self._cache = []
        self._exhausted = False
        self._iterator = None

    def _ensure_iterator(self):
        if self._iterator is None:
            self._iterator = self._generator()

    def __getitem__(self, index: int) -> Any:
        self._ensure_iterator()
        while len(self._cache) <= index and not self._exhausted:
            try:
                self._cache.append(next(self._iterator))
            except StopIteration:
                self._exhausted = True
                raise IndexError("Index out of range")
        return self._cache[index]

    def __iter__(self):
        self._ensure_iterator()
        for item in self._cache:
            yield item
        for item in self._iterator:
            self._cache.append(item)
            yield item
        self._exhausted = True

# Infinite sequence - only generates what's needed
def fibonacci() -> Iterator[int]:
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

fib = LazyList(fibonacci)
print(fib[0])   # 0
print(fib[5])   # 5
print(fib[10])  # 55
# Only computed values 0-10, not beyond

# Python generators are naturally lazy
def naturals():
    n = 0
    while True:
        yield n
        n += 1

def take(n: int, iterable) -> list:
    return [next(iterable) for _ in range(n)]

squares = (x**2 for x in naturals())  # Generator expression - lazy
print(take(5, squares))  # [0, 1, 4, 9, 16]
```

---

## FP in Different Languages

### Haskell

```haskell
-- Haskell is a purely functional language

-- Define a function
add :: Int -> Int -> Int
add x y = x + y

-- Pattern matching
factorial :: Integer -> Integer
factorial 0 = 1
factorial n = n * factorial (n - 1)

-- List operations
doubleList :: [Int] -> [Int]
doubleList = map (* 2)

sumOfSquares :: [Int] -> Int
sumOfSquares = sum . map (^ 2)

-- Higher-order functions
applyTwice :: (a -> a) -> a -> a
applyTwice f x = f (f x)

-- Monadic operations
safeDivide :: Double -> Double -> Maybe Double
safeDivide _ 0 = Nothing
safeDivide x y = Just (x / y)

-- Composition
process :: [Int] -> [Int]
process = filter even . map (* 2) . filter (> 0)
```

---

### Python

```python
from functools import reduce, partial
from typing import Callable, List, Any

# Pure functions
def add(a: int, b: int) -> int:
    return a + b

def multiply(a: int, b: int) -> int:
    return a * b

# Higher-order functions with reduce
product = partial(reduce, multiply)
print(product([1, 2, 3, 4, 5]))  # 120

# List comprehension (Pythonic FP)
numbers = range(1, 11)
result = [x ** 2 for x in numbers if x % 2 == 0]
print(result)  # [4, 16, 36, 64, 100]

# Chaining with itertools
from itertools import chain, filterfalse, starmap

data = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
flattened = list(chain.from_iterable(data))
print(flattened)  # [1, 2, 3, 4, 5, 6, 7, 8, 9]

# Using reduce for deep operations
nested = [[1, 2], [3, 4], [5, 6]]
flattened = reduce(lambda a, b: a + b, nested)
print(flattened)  # [1, 2, 3, 4, 5, 6]

# Immutable data with named tuples
from typing import NamedTuple

class Point(NamedTuple):
    x: float
    y: float

def translate(point: Point, dx: float, dy: float) -> Point:
    return Point(point.x + dx, point.y + dy)

p1 = Point(0, 0)
p2 = translate(p1, 1, 2)
print(p1)  # Point(x=0, y=0) - unchanged
print(p2)  # Point(x=1, y=2)
```

---

### JavaScript

```javascript
// Functional programming in JavaScript

// Pure functions
const add = (a, b) => a + b;
const multiply = (a, b) => a * b;
const square = x => x ** 2;

// Currying
const curry = (fn) => {
    const arity = fn.length;
    return function curried(...args) {
        if (args.length >= arity) {
            return fn(...args);
        }
        return (...moreArgs) => curried(...args, ...moreArgs);
    };
};

const curriedAdd = curry(add);
console.log(curriedAdd(1)(2));  // 3

// Function composition
const compose = (...fns) => (x) => fns.reduceRight((acc, fn) => fn(acc), x);
const pipe = (...fns) => (x) => fns.reduce((acc, fn) => fn(acc), x);

// Example: Data transformation pipeline
const users = [
    { name: "Alice", age: 30, active: true },
    { name: "Bob", age: 25, active: false },
    { name: "Charlie", age: 35, active: true },
];

const getActiveNames = pipe(
    users => users.filter(u => u.active),
    users => users.map(u => u.name),
    names => names.join(", ")
);

console.log(getActiveNames(users));  // "Alice, Charlie"

// Immutable operations
const immutablePush = (arr, item) => [...arr, item];
const immutableUpdate = (arr, index, item) => [
    ...arr.slice(0, index),
    item,
    ...arr.slice(index + 1)
];

const arr = [1, 2, 3];
const newArr = immutablePush(arr, 4);
console.log(arr);    // [1, 2, 3]
console.log(newArr); // [1, 2, 3, 4]

// Maybe monad
class Maybe {
    constructor(value) {
        this._value = value;
    }

    static just(value) {
        return new Maybe(value);
    }

    static nothing() {
        return new Maybe(null);
    }

    map(fn) {
        return this._value === null ? this : Maybe.just(fn(this._value));
    }

    flatMap(fn) {
        return this._value === null ? this : fn(this._value);
    }

    getOrElse(defaultValue) {
        return this._value !== null ? this._value : defaultValue;
    }
}

const result = Maybe.just(10)
    .map(x => x * 2)
    .map(x => x + 5)
    .getOrElse(0);

console.log(result);  // 25
```

---

### Java

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class FunctionalJava {

    // Pure function
    public static int add(int a, int b) {
        return a + b;
    }

    // Higher-order function
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    // Function composition
    public static <T> UnaryOperator<T> compose(
            UnaryOperator<T> f, UnaryOperator<T> g) {
        return x -> f.apply(g.apply(x));
    }

    // Curry-like behavior with BiFunction
    public static <T, U, R> Function<T, Function<U, R>> curry(
            BiFunction<T, U, R> biFunction) {
        return a -> b -> biFunction.apply(a, b);
    }

    public static void main(String[] args) {
        // Lambda expressions
        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> doubleValue = x -> x * 2;

        // Function composition
        Function<Integer, Integer> squareThenDouble = compose(doubleValue, square);
        System.out.println(squareThenDouble.apply(3));  // 18

        // Stream operations (FP style)
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sumOfSquaresOfEvens = numbers.stream()
            .filter(n -> n % 2 == 0)          // Filter even numbers
            .map(n -> n * n)                  // Square them
            .reduce(0, Integer::sum);         // Sum them

        System.out.println(sumOfSquaresOfEvens);  // 220

        // Currying
        Function<Integer, Function<Integer, Integer>> curriedAdd =
            curry(Integer::sum);

        Function<Integer, Integer> add5 = curriedAdd.apply(5);
        System.out.println(add5.apply(3));  // 8

        // Optional monad
        Optional.ofNullable(findUser(1))
            .map(User::getName)
            .ifPresent(name -> System.out.println("User: " + name));

        Optional.ofNullable(findUser(999))
            .ifPresentOrElse(
                name -> System.out.println("Found"),
                () -> System.out.println("Not found")
            );
    }

    static User findUser(int id) {
        return id == 1 ? new User("Alice") : null;
    }
}

class User {
    private String name;
    public User(String name) { this.name = name; }
    public String getName() { return name; }
}
```

---

## FP vs OOP

| Aspect | Functional | Object-Oriented |
|--------|------------|-----------------|
| Primary unit | Function | Object |
| State | Immutable | Mutable (encapsulated) |
| Data & behavior | Separate | Bundled |
| Primary mechanism | Composition | Inheritance/Composition |
| Side effects | Minimized/controlled | Encapsulated |
| Concurrency | Easier (no shared state) | Requires synchronization |
| Testing | Easier (pure functions) | May need mocking |
| Learning curve | Steeper initially | More intuitive for beginners |

### When to Use FP

- Data transformations and pipelines
- Concurrent/parallel programming
- Mathematical computations
- When testability is critical
- Event-driven systems

### When to Use OOP

- GUI applications
- Game development
- Systems with clear entity boundaries
- When state management is complex
- Team projects with OOP-experienced developers

### Hybrid Approach

Many modern languages support both paradigms. Use the right tool for each problem:

```python
# OOP for structure
class DataProcessor:
    def __init__(self, config: dict):
        self.config = config

    def process(self, data: list) -> list:
        return self._transform(data)

    def _transform(self, data: list) -> list:
        # FP style for data transformation
        return list(map(self._apply_rules, filter(self._is_valid, data)))

    def _is_valid(self, item: dict) -> bool:
        return item.get("active", False)

    def _apply_rules(self, item: dict) -> dict:
        return {**item, "processed": True}
```

---

## Common Patterns

### Map-Reduce-Filter Pipeline

```python
from typing import List, Dict, Callable

def process_data(data: List[Dict]) -> List[Dict]:
    """Process data using FP pipeline."""
    return (
        data
        |> filter(lambda x: x["active"])  # Filter
        |> map(lambda x: {**x, "score": x["value"] * 10})  # Transform
        |> sorted(key=lambda x: x["score"], reverse=True)  # Sort
        |> take(10)  # Limit
    )
```

### Immutable State Management

```python
from typing import NamedTuple, Tuple
from enum import Enum

class Action(Enum):
    INCREMENT = "INCREMENT"
    DECREMENT = "DECREMENT"

class State(NamedTuple):
    count: int
    history: Tuple[int, ...]

def reducer(state: State, action: Action) -> State:
    if action == Action.INCREMENT:
        new_count = state.count + 1
    elif action == Action.DECREMENT:
        new_count = state.count - 1
    else:
        return state

    return State(
        count=new_count,
        history=state.history + (new_count,)
    )

# Usage
initial_state = State(count=0, history=())
state1 = reducer(initial_state, Action.INCREMENT)
state2 = reducer(state1, Action.INCREMENT)
state3 = reducer(state2, Action.DECREMENT)

print(state3)  # State(count=1, history=(1, 2, 1))
print(initial_state)  # State(count=0, history=()) - unchanged
```

---

## Summary

| Concept | Description | Key Benefit |
|---------|-------------|-------------|
| Pure Functions | No side effects | Predictability, testability |
| Immutability | No data modification | Thread safety, no bugs |
| Higher-Order Functions | Functions as values | Reusability, abstraction |
| Closures | Remember scope | Encapsulation without classes |
| Monads | Chain operations with context | Handle side effects cleanly |
| Composition | Combine small functions | Modular, composable code |
