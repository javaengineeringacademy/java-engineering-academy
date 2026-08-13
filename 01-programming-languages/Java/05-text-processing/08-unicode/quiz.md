# Quiz: Unicode

## Multiple Choice Questions

1. What is Unicode?
   - A) A character encoding
   - B) A character set standard
   - C) A programming language
   - D) A file format

2. How many characters can Unicode represent?
   - A) 256
   - B) 65,536
   - C) 1,114,112
   - D) Unlimited

3. What is a code point in Unicode?
   - A) A character
   - B) A number representing a character
   - C) A byte
   - D) A string

4. What is a surrogate pair?
   - A) Two bytes
   - B) Two characters
   - C) Two code units
   - D) Two strings

5. Which plane contains most common characters?
   - A) Supplementary
   - B) BMP (Basic Multilingual Plane)
   - C) Auxiliary
   - D) Private Use

## True/False Questions

6. Unicode is backward compatible with ASCII.
   - True / False

7. BMP characters use 2 bytes.
   - True / False

8. Emoji requires surrogate pairs.
   - True / False

## Code Output Questions

9. What will this code print?
```java
String s = "Hello";
System.out.println(s.codePointCount(0, s.length()));
System.out.println(s.charAt(0));
```

10. What will this code print?
```java
String emoji = "\uD83D\uDE00";
System.out.println(emoji.length());
System.out.println(emoji.codePointCount(0, emoji.length()));
```

## Answers

1. B - Unicode is a character set standard
2. C - Unicode can represent 1,114,112 characters
3. B - Code point is a number representing a character
4. C - Surrogate pair uses two code units
5. B - BMP contains most common characters
6. True - Unicode first 128 characters match ASCII
7. True - BMP characters use 2 bytes (16 bits)
8. True - Emoji are outside BMP and need surrogate pairs
9. Output:
```
5
H
```
10. Output:
```
2
1
```
(Emoji is 2 chars but 1 code point)
