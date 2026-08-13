# Quiz: Function Composition

## Multiple Choice Questions

1. What is function composition?
   - A) Combining functions
   - B) Creating functions
   - C) Deleting functions
   - D) Sorting functions

2. Which method chains functions?
   - A) `andThen()`
   - B) `compose()`
   - C) `chain()`
   - D) Both A and B

3. What does `andThen()` do?
   - A) Runs first function, then second
   - B) Runs second function, then first
   - C) Runs both in parallel
   - D) Runs random function

4. What does `compose()` do?
   - A) Runs first function, then second
   - B) Runs second function, then first
   - C) Runs both in parallel
   - D) Runs random function

5. Which is a common use case?
   - A) Data transformation
   - B) Event handling
   - C) Thread management
   - D) File I/O

## True/False Questions

6. Composed functions are immutable.
   - True / False

7. You can chain multiple functions.
   - True / False

8. Function composition is only for Function type.
   - True / False

## Code Output Questions

9. What will this code print?
```java
Function<Integer, Integer> doubleIt = n -> n * 2;
Function<Integer, Integer> addTen = n -> n + 10;
Function<Integer, Integer> combined = doubleIt.andThen(addTen);
System.out.println(combined.apply(5));
```

10. What will this code print?
```java
Function<Integer, Integer> doubleIt = n -> n * 2;
Function<Integer, Integer> addTen = n -> n + 10;
Function<Integer, Integer> combined = doubleIt.compose(addTen);
System.out.println(combined.apply(5));
```

## Answers

1. A - Function composition combines functions
2. D - Both andThen() and compose() chain functions
3. A - andThen() runs first, then second
4. B - compose() runs second, then first
5. A - Data transformation is common use case
6. True - Composed functions are immutable
7. True - You can chain multiple functions
8. False - Predicate, Consumer also support composition
9. Output:
```
20
```
10. Output:
```
30
```
