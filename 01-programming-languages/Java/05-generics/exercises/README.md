# Generics Exercises

Practice Java generics through hands-on exercises.

## Exercise 1: Generic Class

**Problem Statement:**
Create a generic `Pair<T, U>` class that holds two values of potentially different types. Implement `getFirst()`, `getSecond()`, `equals()`, `hashCode()`, and `toString()` methods.

**Expected Behavior:**
- A `Pair<String, Integer>` can hold a name and age.
- A `Pair<Integer, Double>` can hold an ID and score.
- Two pairs are equal if both their first and second values are equal.
- `toString()` returns a readable representation like `(hello, 42)`.

**Hints:**
- Declare the class as `class Pair<T, U>`.
- Use `Objects.equals()` for null-safe equality checks.
- Use `Objects.hash()` for `hashCode()`.

---

## Exercise 2: Generic Method

**Problem Statement:**
Write a generic method `swap(T[] array, int i, int j)` that swaps two elements in an array. Also write a generic method `first(List<T> list)` that returns the first element or throws `NoSuchElementException` if empty.

**Expected Behavior:**
- `swap` works with `String[]`, `Integer[]`, or any object array.
- `swap` throws `IndexOutOfBoundsException` for invalid indices.
- `first` returns the first element of any `List<T>`.
- `first` throws `NoSuchElementException` for an empty list.

**Hints:**
- Declare generic methods with `<T>` before the return type.
- Use `Collections.swap()` or implement manual swap with a temporary variable.
- Check `list.isEmpty()` before calling `list.get(0)`.

---

## Exercise 3: Bounded Type Parameters

**Problem Statement:**
Write a generic method `max(List<T extends Comparable<T>> list)` that finds the maximum element in a list. Create a `Stats<T extends Number>` class that computes the average of a list of numbers.

**Expected Behavior:**
- `max` only accepts lists of `Comparable` types (String, Integer, etc.).
- `max` throws `NoSuchElementException` for empty lists.
- `Stats<Integer>` computes the average of integers.
- `Stats<Double>` computes the average of doubles.
- Passing a `List<StringBuilder>` to `max` causes a compile error.

**Hints:**
- Use `<T extends Comparable<T>>` to restrict type parameters.
- Initialize the maximum with the first element, then iterate.
- For `Stats`, use `doubleValue()` from the `Number` class.

---

## Exercise 4: Wildcards

**Problem Statement:**
Write three methods that demonstrate upper-bounded, lower-bounded, and unbounded wildcards: `sumUpper(List<? extends Number>)`, `addElements(List<? super Integer>)`, and `printAll(List<?> list)`.

**Expected Behavior:**
- `sumUpper` accepts `List<Integer>`, `List<Double>`, etc., and returns their sum.
- `addElements` accepts `List<Number>`, `List<Object>`, etc., and adds integers.
- `printAll` accepts any `List<?>` and prints all elements.
- `sumUpper` cannot add elements to the list (read-only).
- `addElements` cannot read specific types from the list.

**Hints:**
- `? extends Number` means "any subtype of Number" (PECS: Producer Extends).
- `? super Integer` means "any supertype of Integer" (PECS: Consumer Super).
- `?` unbounded means you can only read as `Object`.

---

## Exercise 5: Type Erasure

**Problem Statement:**
Write a program that demonstrates type erasure. Create a `Box<T>` generic class, then show that `Box<String>` and `Box<Integer>` have the same runtime class. Write a method that uses reflection to inspect generic type information.

**Expected Behavior:**
- `Box<String>.class == Box<Integer>.class` returns `true`.
- `box.getClass()` returns the raw `Box` class, not the parameterized type.
- Use `getGenericSuperclass()` to recover type parameters from anonymous subclasses.
- Demonstrate that you cannot create `new T()` due to erasure.

**Hints:**
- Use `getClass()` and `==` to compare runtime class objects.
- Create an anonymous subclass `new Box<String>(){}` to preserve type info.
- Use `ParameterizedType` to extract `getActualTypeArguments()`.

---

## Exercise 6: Generic Inheritance

**Problem Statement:**
Create a generic `Repository<T>` abstract class with `save(T entity)`, `findById(int id)`, and `findAll()` methods. Extend it with `UserRepository extends Repository<User>` and `OrderRepository extends Repository<Order>`.

**Expected Behavior:**
- `UserRepository.save(User)` compiles and works correctly.
- `UserRepository.save(Order)` causes a compile error.
- Each repository can have type-specific methods (e.g., `findByEmail` for users).
- The abstract class provides common CRUD logic.

**Hints:**
- Declare the abstract class as `abstract class Repository<T>`.
- Use concrete types in subclasses: `class UserRepository extends Repository<User>`.
- Add type-specific methods in the concrete subclasses.

---

## Exercise 7: Generic Builder

**Problem Statement:**
Implement a type-safe Builder pattern using generics. Create a `QueryBuilder<T>` that supports chained methods and builds different types of queries based on the entity type.

**Expected Behavior:**
- `QueryBuilder<User>` supports `whereName(String)` and `whereEmail(String)`.
- `QueryBuilder<Order>` supports `whereAmount(double)` and `whereDate(LocalDate)`.
- Common methods like `limit(int)` and `build()` work for all types.
- Method chaining is fluent and type-safe.

**Hints:**
- Use a generic base class with common builder methods.
- Create specific builder subclasses that add type-specific where clauses.
- Return `this` from each method for fluent chaining.

---

## Exercise 8: Type-Safe Heterogeneous Container

**Problem Statement:**
Implement a type-safe heterogeneous container (as described by Joshua Bloch) using `Class<T>` as the key. The container stores values of different types, but retrieval is type-safe through the class token.

**Expected Behavior:**
- `container.put(String.class, "hello")` stores a String.
- `container.put(Integer.class, 42)` stores an Integer.
- `container.get(String.class)` returns a `String` (no casting needed).
- `container.get(Integer.class)` returns an `Integer`.
- `container.get(Double.class)` returns `null` if not stored.
- Type safety is enforced at compile time.

**Hints:**
- Use a `Map<Class<?>, Object>` internally.
- The `put` method signature should be `<T> void put(Class<T> key, T value)`.
- The `get` method signature should be `<T> T get(Class<T> key)`.
- Use `Class.cast()` for safe retrieval.
