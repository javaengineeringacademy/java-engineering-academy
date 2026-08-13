# Quiz: Regex

## Multiple Choice Questions

1. What does regex stand for?
   - A) Regular Expression
   - B) Random Expression
   - C) Reactive Expression
   - D) Recursive Expression

2. Which class is used to compile regex patterns?
   - A) Regex
   - B) Pattern
   - C) Matcher
   - D) Expression

3. What does `Pattern.compile()` return?
   - A) A string
   - B) A Pattern object
   - C) A Matcher object
   - D) A boolean

4. Which method finds matches in a string?
   - A) `find()`
   - B) `match()`
   - C) `search()`
   - D) `locate()`

5. What does `\d` match in regex?
   - A) Any character
   - B) A digit
   - C) A letter
   - D) A whitespace

## True/False Questions

6. `.` in regex matches any single character.
   - True / False

7. `*` means zero or more occurrences.
   - True / False

8. Regex patterns are case-sensitive by default.
   - True / False

## Code Output Questions

9. What will this code print?
```java
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher("abc123def456");
while (m.find()) {
    System.out.print(m.group() + " ");
}
```

10. What will this code print?
```java
String s = "Hello World";
System.out.println(s.matches(".*World"));
System.out.println(s.replaceAll("\\s", "_"));
```

## Answers

1. A - Regular Expression
2. B - Pattern.compile() compiles the pattern
3. B - Returns a Pattern object
4. A - find() finds next match
5. B - \d matches a digit
6. True - . matches any character
7. True - * means zero or more
8. True - Regex is case-sensitive by default
9. Output:
```
123 456
```
10. Output:
```
true
Hello_World
```
