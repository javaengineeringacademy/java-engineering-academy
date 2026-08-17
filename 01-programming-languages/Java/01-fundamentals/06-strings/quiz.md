# Strings - Quiz

## Questions

### Q1: Are Strings mutable in Java?
- A) Yes
- B) No, they are immutable
- C) Only with StringBuilder
- D) Only with final

### Q2: What is the String Pool?
- A) A connection pool
- B) A cache of interned string literals
- C) A thread pool
- D) A garbage collector

### Q3: What is the difference between `==` and `.equals()` for Strings?
- A) `==` compares content, `.equals()` compares references
- B) `==` compares references, `.equals()` compares content
- C) They are identical
- D) `.equals()` is slower

### Q4: What does `StringBuilder` provide?
- A) Immutable strings
- B) Mutable sequence of characters
- C) Thread safety
- D) Automatic boxing

### Q5: What is string interning?
- A) Converting to int
- B) Storing a string in the pool to share instances
- C) Compressing strings
- D) Encrypting strings

### Q6: What does `String.format()` do?
- A) Parses a string
- B) Returns a formatted string using a format specifier
- C) Converts to uppercase
- D) Splits a string

### Q7: What is the output of `"hello".length()`?
- A) 4
- B) 5
- C) 6
- D) Compilation error

### Q8: Which method splits a string into an array?
- A) `split()`
- B) `divide()`
- C) `separate()`
- D) `partition()`

### Q9: What is a text block (Java 15+)?
- A) A block of code
- B) A multi-line string literal
- C) A character array
- D) A file reader

### Q10: What does `StringBuilder.reverse()` return?
- A) void
- B) A new StringBuilder with reversed content
- C) A String
- D) A char array

## Answers

1. **B** - String objects are immutable in Java
2. **B** - String pool caches string literals for memory efficiency
3. **B** - `==` checks reference equality; `.equals()` checks content
4. **B** - StringBuilder is a mutable alternative to String
5. **B** - Interning places strings in the pool to share instances
6. **B** - `String.format()` creates formatted strings like printf
7. **B** - "hello" has 5 characters (indices 0-4)
8. **A** - `split(regex)` divides a string by a delimiter
9. **B** - Text blocks (`"""`) allow multi-line string literals
10. **B** - `reverse()` returns the same StringBuilder instance, reversed
