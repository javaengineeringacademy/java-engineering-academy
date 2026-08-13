# Quiz: Stream API

## Multiple Choice Questions

1. What is a Stream in Java?
   - A) Sequence of elements
   - B) I/O stream
   - C) File stream
   - D) Network stream

2. What does `stream()` return?
   - A) List
   - B) Stream<T>
   - C) Collection
   - D) Array

3. Which is an intermediate operation?
   - A) forEach
   - B) collect
   - C) filter
   - D) reduce

4. Which is a terminal operation?
   - A) map
   - B) filter
   - C) sorted
   - D) count

5. What is stream pipeline?
   - A) Chain of operations
   - B) Stream source
   - C) Stream destination
   - D) Stream buffer

## True/False Questions

6. Streams can be processed in parallel.
   - True / False

7. Streams are reusable.
   - True / False

8. Stream operations are lazy.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<Integer> list = List.of(1, 2, 3, 4, 5);
long count = list.stream()
    .filter(n -> n > 3)
    .count();
System.out.println(count);
```

10. What will this code print?
```java
List<String> list = List.of("a", "bb", "ccc");
String result = list.stream()
    .map(s -> s.toUpperCase())
    .collect(Collectors.joining(", "));
System.out.println(result);
```

## Answers

1. A - Stream is a sequence of elements
2. B - stream() returns Stream<T>
3. C - filter is an intermediate operation
4. D - count is a terminal operation
5. A - Pipeline is a chain of operations
6. True - Streams support parallel processing
7. False - Streams are single-use
8. True - Intermediate operations are lazy
9. Output:
```
2
```
10. Output:
```
A, BB, CCC
```
