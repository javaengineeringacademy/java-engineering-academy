# Quiz: Optional

## Multiple Choice Questions

1. What is Optional in Java?
   - A) Container for null
   - B) Optional parameter
   - C) Optional method
   - D) Optional class

2. Which method returns Optional?
   - A) `Optional.of()`
   - B) `Optional.get()`
   - C) `Optional.null()`
   - D) `Optional.create()`

3. What does `orElse()` do?
   - A) Returns value or default
   - B) Throws exception
   - C) Returns null
   - D) Returns Optional

4. What does `isPresent()` check?
   - A) If value is null
   - B) If value is present
   - C) If value is empty
   - D) If value is valid

5. Which method throws exception if empty?
   - A) `orElse()`
   - B) `orElseThrow()`
   - C) `orElseGet()`
   - D) `orElseNull()`

## True/False Questions

6. Optional can be null.
   - True / False

7. Optional.get() throws exception if empty.
   - True / False

8. Optional is for return types, not parameters.
   - True / False

## Code Output Questions

9. What will this code print?
```java
Optional<String> opt = Optional.of("Hello");
System.out.println(opt.isPresent());
System.out.println(opt.get());
```

10. What will this code print?
```java
Optional<String> opt = Optional.empty();
System.out.println(opt.orElse("Default"));
```

## Answers

1. A - Optional is a container for null
2. A - Optional.of() creates Optional with value
3. A - orElse() returns value or default
4. B - isPresent() checks if value is present
5. B - orElseThrow() throws exception if empty
6. False - Optional itself cannot be null
7. True - get() throws NoSuchElementException if empty
8. True - Optional is recommended for return types
9. Output:
```
true
Hello
```
10. Output:
```
Default
```
