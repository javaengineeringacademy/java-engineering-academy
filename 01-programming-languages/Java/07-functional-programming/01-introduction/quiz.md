# Quiz: Functional Programming Introduction

## Multiple Choice Questions

1. What is functional programming?
   - A) Programming with functions
   - B) Object-oriented programming
   - C) Procedural programming
   - D) Event-driven programming

2. What is a pure function?
   - A) Function with no arguments
   - B) Function with no side effects
   - C) Function with no return value
   - D) Function with no variables

3. What is immutability?
   - A) Variables can change
   - B) Variables cannot change after creation
   - C) Objects can change
   - D) Methods can change state

4. What is a lambda expression?
   - A) Anonymous function
   - B) Named function
   - C) Recursive function
   - D) Static function

5. What is method reference?
   - A) Pointer to a method
   - B) Method name
   - C) Method signature
   - D) Method body

## True/False Questions

6. Functional programming avoids mutable state.
   - True / False

7. Lambda expressions can have side effects.
   - True / False

8. Method references are syntactic sugar for lambdas.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<Integer> list = List.of(1, 2, 3, 4, 5);
int sum = list.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(n -> n)
    .sum();
System.out.println(sum);
```

10. What will this code print?
```java
Function<Integer, Integer> doubleIt = n -> n * 2;
System.out.println(doubleIt.apply(5));
```

## Answers

1. A - Functional programming uses functions
2. B - Pure functions have no side effects
3. B - Immutability means variables cannot change
4. A - Lambda is an anonymous function
5. A - Method reference is a pointer to a method
6. True - Functional programming avoids mutable state
7. False - Lambdas should be pure (no side effects)
8. True - Method references are syntactic sugar
9. Output:
```
6
```
10. Output:
```
10
```
