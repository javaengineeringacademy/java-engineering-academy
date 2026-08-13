# Quiz: Functional Programming Best Practices

## Multiple Choice Questions

1. What is the key principle of functional programming?
   - A) Mutability
   - B) Immutability
   - C) Side effects
   - D) Global state

2. What should pure functions avoid?
   - A) Arguments
   - B) Return values
   - C) Side effects
   - D) Local variables

3. When should you use lambdas?
   - A) Always
   - B) For simple operations
   - C) For complex logic
   - D) Never

4. What is the benefit of immutability?
   - A) Better performance
   - B) Thread safety
   - C) Less code
   - D) More flexibility

5. Which is a functional programming anti-pattern?
   - A) Pure functions
   - B) Immutable data
   - C) Mutable shared state
   - D) Function composition

## True/False Questions

6. Functional programming is always better than OOP.
   - True / False

7. Side effects make code harder to test.
   - True / False

8. Immutability improves concurrent programming.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<Integer> list = List.of(1, 2, 3, 4, 5);
int sum = list.stream()
    .filter(n -> n > 2)
    .mapToInt(n -> n)
    .sum();
System.out.println(sum);
```

10. What will this code print?
```java
List<String> list = List.of("Java", "Python", "Go");
String result = list.stream()
    .filter(s -> s.length() > 2)
    .findFirst()
    .orElse("None");
System.out.println(result);
```

## Answers

1. B - Immutability is key principle
2. C - Pure functions avoid side effects
3. B - Use lambdas for simple operations
4. B - Immutability provides thread safety
5. C - Mutable shared state is an anti-pattern
6. False - Each paradigm has its place
7. True - Side effects make testing harder
8. True - Immutability helps with concurrency
9. Output:
```
12
```
10. Output:
```
Java
```
