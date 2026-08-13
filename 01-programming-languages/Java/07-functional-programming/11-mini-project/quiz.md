# Quiz: Functional Programming Mini Project

## Multiple Choice Questions

1. What is the goal of this mini project?
   - A) Practice functional programming
   - B) Learn OOP
   - C) Build GUI
   - D) Database programming

2. Which concepts are used together?
   - A) Lambda + Stream
   - B) Class + Object
   - C) Loop + Array
   - D) Try + Catch

3. What is the benefit of combining concepts?
   - A) More code
   - B) Better readability
   - C) Slower execution
   - D) More complexity

4. Which is a common project pattern?
   - A) Data processing pipeline
   - B) Event handling
   - C) GUI creation
   - D) Database queries

5. What should you focus on?
   - A) Writing more code
   - B) Writing cleaner code
   - C) Using more classes
   - D) Using more loops

## True/False Questions

6. Mini projects help reinforce learning.
   - True / False

7. Functional programming is only for simple tasks.
   - True / False

8. You should use all concepts in every project.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
int result = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .reduce(0, Integer::sum);
System.out.println(result);
```

10. What will this code print?
```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");
String result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.joining(", "));
System.out.println(result);
```

## Answers

1. A - Practice functional programming
2. A - Lambda + Stream are combined
3. B - Combining concepts improves readability
4. A - Data processing pipeline is common
5. B - Focus on cleaner code
6. True - Mini projects reinforce learning
7. False - FP works for complex tasks too
8. False - Use only what's needed
9. Output:
```
220
```
10. Output:
```
ALICE, CHARLIE, DAVID
```
