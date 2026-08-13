# Quiz: Collectors

## Multiple Choice Questions

1. What is a Collector?
   - A) Stream terminal operation
   - B) Stream intermediate operation
   - C) Stream source
   - D) Stream buffer

2. Which collector joins strings?
   - A) `toList()`
   - B) `joining()`
   - C) `groupingBy()`
   - D) `partitioningBy()`

3. What does `groupingBy()` return?
   - A) List
   - B) Map
   - C) Set
   - D) Stream

4. Which collector counts elements?
   - A) `summarizingInt()`
   - B) `counting()`
   - C) `summingInt()`
   - D) `averagingInt()`

5. What does `partitioningBy()` return?
   - A) List
   - B) Map<Boolean, List>
   - C) Map<String, List>
   - D) Set

## True/False Questions

6. Collectors are terminal operations.
   - True / False

7. `toList()` returns an unmodifiable list.
   - True / False

8. You can create custom collectors.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<String> list = List.of("a", "bb", "ccc", "dd");
Map<Integer, List<String>> grouped = list.stream()
    .collect(Collectors.groupingBy(String::length));
System.out.println(grouped);
```

10. What will this code print?
```java
List<String> list = List.of("a", "b", "c");
String result = list.stream()
    .collect(Collectors.joining(", "));
System.out.println(result);
```

## Answers

1. A - Collector is a terminal operation
2. B - joining() joins strings
3. B - groupingBy() returns a Map
4. B - counting() counts elements
5. B - partitioningBy() returns Map<Boolean, List>
6. True - Collectors are terminal operations
7. False - toList() returns a modifiable list (pre-Java 10)
8. True - Custom collectors can be created
9. Output:
```
{1=[a], 2=[bb, dd], 3=[ccc]}
```
10. Output:
```
a, b, c
```
