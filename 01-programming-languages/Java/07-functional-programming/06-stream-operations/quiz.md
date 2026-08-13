# Quiz: Stream Operations

## Multiple Choice Questions

1. Which is an intermediate operation?
   - A) forEach
   - B) collect
   - C) map
   - D) reduce

2. Which method transforms elements?
   - A) filter
   - B) map
   - C) count
   - D) sorted

3. Which method filters elements?
   - A) map
   - B) filter
   - C) reduce
   - D) collect

4. What does `reduce()` do?
   - A) Filters elements
   - B) Combines elements
   - C) Sorts elements
   - D) Counts elements

5. Which method sorts elements?
   - A) sort
   - B) order
   - C) sorted
   - D) arrange

## True/False Questions

6. `map()` can change element type.
   - True / False

7. `filter()` returns a new stream.
   - True / False

8. Intermediate operations are eager.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<Integer> list = List.of(1, 2, 3, 4, 5);
List<Integer> result = list.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * 10)
    .collect(Collectors.toList());
System.out.println(result);
```

10. What will this code print?
```java
List<String> list = List.of("a", "bb", "ccc");
int totalLength = list.stream()
    .map(String::length)
    .reduce(0, Integer::sum);
System.out.println(totalLength);
```

## Answers

1. C - map is an intermediate operation
2. B - map transforms elements
3. B - filter filters elements
4. B - reduce combines elements
5. C - sorted sorts elements
6. True - map can change element type
7. True - filter returns a new stream
8. False - Intermediate operations are lazy
9. Output:
```
[20, 40]
```
10. Output:
```
6
```
