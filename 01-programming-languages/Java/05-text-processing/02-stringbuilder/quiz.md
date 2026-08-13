# Quiz: StringBuilder

## Multiple Choice Questions

1. What is StringBuilder in Java?
   - A) Immutable string
   - B) Mutable sequence of characters
   - C) Thread-safe string
   - D) String array

2. What is the default capacity of StringBuilder?
   - A) 8
   - B) 16
   - C) 32
   - D) 64

3. Which method adds a string to the end?
   - A) `add()`
   - B) `append()`
   - C) `insert()`
   - D) `concat()`

4. What does `reverse()` do?
   - A) Sorts characters
   - B) Reverses the string
   - C) Removes characters
   - D) Converts to uppercase

5. When should you use StringBuilder over String?
   - A) Always
   - B) When string doesn't change
   - C) When string changes frequently
   - D) Never

## True/False Questions

6. StringBuilder is thread-safe.
   - True / False

7. StringBuilder is faster than String for concatenation in loops.
   - True / False

8. StringBuilder implements CharSequence interface.
   - True / False

## Code Output Questions

9. What will this code print?
```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
sb.reverse();
System.out.println(sb);
```

10. What will this code print?
```java
StringBuilder sb = new StringBuilder();
sb.append("Java");
sb.insert(4, " is");
sb.append(" fun");
System.out.println(sb);
```

## Answers

1. B - StringBuilder is a mutable sequence of characters
2. B - Default capacity is 16 characters
3. B - append() adds to the end
4. B - reverse() reverses the string
5. C - Use StringBuilder when string changes frequently
6. False - StringBuilder is not thread-safe
7. True - StringBuilder avoids creating new objects
8. True - StringBuilder implements CharSequence
9. Output:
```
dlroW olleH
```
10. Output:
```
Java is fun
```
