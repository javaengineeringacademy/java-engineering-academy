# Quiz: Text Blocks

## Multiple Choice Questions

1. When were text blocks introduced in Java?
   - A) Java 11
   - B) Java 12
   - C) Java 13
   - D) Java 15

2. What is the syntax for text blocks?
   - A) `"text"`
   - B) `"""text"""`
   - C) `'''text'''`
   - D) `<<<text>>>`

3. What does text block preserve?
   - A) Line breaks
   - B) Indentation
   - C) Formatting
   - D) All of the above

4. What is the purpose of `\s` in text blocks?
   - A) Space
   - B) Escape
   - C) Strip
   - D) Split

5. Can text blocks contain double quotes?
   - A) Yes
   - B) No
   - C) Only with escaping
   - D) Only single quotes

## True/False Questions

6. Text blocks are immutable.
   - True / False

7. Text blocks use `"""` as delimiter.
   - True / False

8. Text blocks automatically strip trailing whitespace.
   - True / False

## Code Output Questions

9. What will this code print?
```java
String s = """
        Hello
        World
        """;
System.out.println(s.strip());
```

10. What will this code print?
```java
String s = """
        "Java"
        """;
System.out.println(s.strip());
```

## Answers

1. D - Text blocks were finalized in Java 15
2. B - Text blocks use triple quotes """
3. D - Text blocks preserve all formatting
4. C - \s strips trailing whitespace
5. A - Yes, text blocks can contain double quotes
6. True - Text blocks are immutable
7. True - Text blocks use triple quotes
8. True - Text blocks strip trailing whitespace
9. Output:
```
Hello
World
```
10. Output:
```
"Java"
```
