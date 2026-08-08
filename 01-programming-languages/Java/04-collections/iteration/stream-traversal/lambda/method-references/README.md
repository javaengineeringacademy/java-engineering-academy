# Method References

Method references are a shorthand notation for lambda expressions that simply call an existing method. They make code more readable when the lambda body is a single method call.

## Types of Method References

### 1. Static Method Reference

References a static method. The first parameter becomes the method argument.

```java
// Lambda version
Function<String, Integer> parseInt = s -> Integer.parseInt(s);

// Method reference version
Function<String, Integer> parseInt = Integer::parseInt;
```

### 2. Bound Instance Method Reference

References an instance method on a particular object. The lambda takes no parameters.

```java
// Lambda version
String str = "hello";
Supplier<Integer> lengthSupplier = () -> str.length();

// Method reference version
Supplier<Integer> lengthSupplier = str::length;
```

### 3. Unbound Instance Method Reference

References an instance method on an arbitrary object of a particular type. The first parameter becomes the method receiver.

```java
// Lambda version
Function<String, Integer> toLength = s -> s.length();

// Method reference version
Function<String, Integer> toLength = String::length;
```

### 4. Constructor Reference

References a constructor. The factory function creates new instances.

```java
// Lambda version
Supplier<ArrayList<String>> listFactory = () -> new ArrayList<>();

// Method reference version
Supplier<ArrayList<String>> listFactory = ArrayList::new;
```

## Examples

```java
// Static method reference
Function<String, Integer> parseInt = Integer::parseInt;
Integer num = parseInt.apply("42");

// Bound instance method reference
String str = "hello world";
Function<String, String> toUpper = str::toUpperCase;
// Note: This is actually an unbound reference
Function<String, Integer> len = String::length;
Integer length = len.apply("hello");

// Constructor reference
Function<Integer, ArrayList> listFactory = ArrayList::new;
ArrayList list = listFactory.get(10);

// Constructor reference with Supplier
Supplier<HashSet<String>> setFactory = HashSet::new;
HashSet<String> set = setFactory.get();
```

## Method References in Stream Operations

```java
List<String> names = Arrays.asList("alice", "bob", "charlie");

// Method reference with map
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Method reference with filter
List<String> nonEmpty = names.stream()
    .filter(s -> !s.isEmpty())
    .collect(Collectors.toList());

// Method reference with forEach
names.forEach(System.out::println);

// Method reference with reduce
int sum = IntStream.rangeClosed(1, 10)
    .reduce(0, Integer::sum);

// Method reference with collect
List<String> sorted = names.stream()
    .sorted(String::compareToIgnoreCase)
    .collect(Collectors.toList());
```

## Comparing Lambda vs Method Reference

```java
// These are equivalent:
list.stream().map(x -> x.toString());
list.stream().map(Object::toString);

list.stream().filter(x -> x.isEmpty());
list.stream().filter(String::isEmpty); // if x is String

list.stream().forEach(x -> System.out.println(x));
list.stream().forEach(System.out::println);

list.stream().reduce(0, (a, b) -> a + b);
list.stream().reduce(0, Integer::sum);
```

## When to Use Method References

Use method references when:
- The lambda body is a single method call
- The method reference is clearer than the lambda
- The parameters match the method signature

Use lambdas when:
- You need to perform additional operations
- The method reference would be less readable
- You need to capture variables from the enclosing scope

## Quick Reference

| Type | Syntax | Lambda Equivalent |
|------|--------|-------------------|
| Static | `ClassName::staticMethod` | `(args) -> ClassName.staticMethod(args)` |
| Bound | `object::instanceMethod` | `() -> object.instanceMethod()` |
| Unbound | `ClassName::instanceMethod` | `(obj, args) -> obj.instanceMethod(args)` |
| Constructor | `ClassName::new` | `(args) -> new ClassName(args)` |
