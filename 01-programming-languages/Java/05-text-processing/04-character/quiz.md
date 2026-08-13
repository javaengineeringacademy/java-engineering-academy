# Quiz: Character

## Multiple Choice Questions

1. What is the size of a char in Java?
   - A) 1 byte
   - B) 2 bytes
   - C) 4 bytes
   - D) 8 bytes

2. Which class wraps a char value?
   - A) String
   - B) Character
   - C) Char
   - D) CharacterWrapper

3. What does `Character.isDigit('5')` return?
   - A) '5'
   - B) true
   - C) false
   - D) 5

4. Which method converts char to uppercase?
   - A) `toUpper()`
   - B) `toUpperCase()`
   - C) `upperCase()`
   - D) `convertUpper()`

5. What does `Character.isLetter('a')` return?
   - A) 'a'
   - B) true
   - C) false
   - D) 1

## True/False Questions

6. char in Java is signed.
   - True / False

7. Character class provides static utility methods.
   - True / False

8. Unicode characters can be stored in char.
   - True / False

## Code Output Questions

9. What will this code print?
```java
char c = 'A';
System.out.println(Character.toLowerCase(c));
System.out.println(Character.isLetter(c));
```

10. What will this code print?
```java
String s = "Hello123";
for (char c : s.toCharArray()) {
    if (Character.isDigit(c)) {
        System.out.print(c + " ");
    }
}
```

## Answers

1. B - char is 2 bytes (16 bits) in Java
2. B - Character class wraps a char value
3. B - isDigit() returns true for digit characters
4. B - toUpperCase() converts to uppercase
5. B - isLetter() returns true for letter characters
6. False - char is unsigned in Java
7. True - Character provides static utility methods
8. True - Basic multilingual plane characters fit in char
9. Output:
```
a
true
```
10. Output:
```
1 2 3
```
