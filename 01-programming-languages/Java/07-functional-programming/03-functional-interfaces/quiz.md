# Quiz: Functional Interfaces

## Multiple Choice Questions

1. What is a functional interface?
   - A) Interface with one abstract method
   - B) Interface with no methods
   - C) Interface with many methods
   - D) Abstract class

2. Which annotation marks a functional interface?
   - A) @Override
   - B) @FunctionalInterface
   - C) @Interface
   - D) @Abstract

3. Which is NOT a functional interface?
   - A) Predicate<T>
   - B) Function<T,R>
   - C) Comparator<T>
   - D) Collection<T>

4. What does `Predicate<T>` return?
   - A) void
   - B) T
   - C) boolean
   - D) int

5. What does `Consumer<T>` accept?
   - A) No arguments
   - B) One argument
   - C) Two arguments
   - D) Three arguments

## True/False Questions

6. A functional interface can have default methods.
   - True / False

7. @FunctionalInterface is mandatory.
   - True / False

8. A functional interface can extend another interface.
   - True / False

## Code Output Questions

9. What will this code print?
```java
Predicate<Integer> isEven = n -> n % 2 == 0;
System.out.println(isEven.test(4));
System.out.println(isEven.test(5));
```

10. What will this code print?
```java
Function<String, String> upper = String::toUpperCase;
System.out.println(upper.apply("hello"));
```

## Answers

1. A - Functional interface has one abstract method
2. B - @FunctionalInterface marks functional interfaces
3. D - Collection has many methods
4. C - Predicate returns boolean
5. B - Consumer accepts one argument
6. True - Default methods don't count as abstract
7. False - It's optional but recommended
8. True - Functional interfaces can extend others
9. Output:
```
true
false
```
10. Output:
```
HELLO
```
