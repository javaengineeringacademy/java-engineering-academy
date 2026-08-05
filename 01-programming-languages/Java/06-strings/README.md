# Strings Module

## Overview
This module covers Java String class and related classes for text manipulation, including string basics, methods, StringBuilder, formatting, and common patterns.

## Key Concepts

### 1. String Class
- Immutable sequence of characters
- Stored in string pool for memory optimization
- Provides rich set of methods for manipulation

### 2. StringBuilder
- Mutable sequence of characters
- Efficient for string concatenation in loops
- Not thread-safe (use StringBuffer for thread-safety)

### 3. String Formatting
- `String.format()` for formatted output
- Text blocks for multi-line strings
- Format specifiers for different data types

### 4. Common Patterns
- Palindrome, anagram, reverse operations
- Character frequency analysis
- Duplicate character detection

## Module Structure
- `01-string-basics/` - String creation, immutability, comparison
- `02-string-methods/` - Common String methods
- `03-stringbuilder/` - StringBuilder class operations
- `04-string-formatting/` - String.format() and text blocks
- `05-patterns/` - Common string pattern problems

## Code References
- `StringBasicsExample.java` - Basic string concepts

## Common Mistakes
1. Using `==` instead of `.equals()` for string comparison
2. Using String concatenation in loops instead of StringBuilder
3. Not handling null strings properly
4. Forgetting string immutability behavior

## Interview Questions
1. Why are strings immutable in Java?
2. What is the string pool?
3. Difference between StringBuilder and StringBuffer?
4. How to check if a string is a palindrome?
5. What are the time complexity of common string operations?
