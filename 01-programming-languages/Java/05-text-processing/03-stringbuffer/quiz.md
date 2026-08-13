# Quiz: StringBuffer

## Multiple Choice Questions

1. What is the main difference between StringBuilder and StringBuffer?
   - A) StringBuilder is mutable, StringBuffer is immutable
   - B) StringBuilder is faster, StringBuffer is thread-safe
   - C) StringBuilder is thread-safe, StringBuffer is not
   - D) No difference

2. When should you use StringBuffer?
   - A) Single-threaded applications
   - B) Multi-threaded applications
   - C) When performance is critical
   - D) Never

3. Which method is synchronized in StringBuffer?
   - A) append()
   - B) insert()
   - C) delete()
   - D) All of the above

4. What is the default capacity of StringBuffer?
   - A) 8
   - B) 16
   - C) 32
   - D) 64

5. What happens when capacity is exceeded?
   - A) Exception is thrown
   - B) Capacity doubles
   - C) Capacity increases by 2
   - D) String is truncated

## True/False Questions

6. StringBuffer is faster than StringBuilder.
   - True / False

7. StringBuffer is thread-safe.
   - True / False

8. StringBuffer implements CharSequence interface.
   - True / False

## Code Output Questions

9. What will this code print?
```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World");
System.out.println(sb);
System.out.println(sb.capacity());
```

10. What will this code print?
```java
StringBuffer sb = new StringBuffer("Java");
sb.insert(4, " is fun");
System.out.println(sb);
```

## Answers

1. B - StringBuilder is faster, StringBuffer is thread-safe
2. B - Use StringBuffer in multi-threaded applications
3. D - All methods in StringBuffer are synchronized
4. B - Default capacity is 16 characters
5. B - Capacity doubles when exceeded
6. False - StringBuilder is faster due to no synchronization
7. True - StringBuffer is thread-safe
8. True - StringBuffer implements CharSequence
9. Output:
```
Hello World
32
```
10. Output:
```
Java is fun
```
