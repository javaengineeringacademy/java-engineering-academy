# String Methods

## Overview
This module covers the most commonly used String methods in Java including length, access, search, modification, and splitting operations.

## Key Concepts

### 1. Length and Access
```java
String str = "Hello";
str.length()      // 5
str.charAt(0)     // 'H'
str.charAt(4)     // 'o'
```

### 2. Search Methods
```java
String text = "Hello World";
text.contains("World")     // true
text.startsWith("Hello")   // true
text.endsWith("World")     // true
text.indexOf("World")      // 6
text.lastIndexOf('l')      // 9
```

### 3. Substring
```java
String text = "Hello World";
text.substring(6)           // "World"
text.substring(0, 5)       // "Hello"
```

### 4. Modification
```java
String text = "Hello World";
text.replace("World", "Java")    // "Hello Java"
text.replace('l', 'L')           // "HeLLo WorLd"
text.toUpperCase()                // "HELLO WORLD"
text.toLowerCase()                // "hello world"
text.trim()                       // removes whitespace
```

### 5. Split and Join
```java
String csv = "apple,banana,cherry";
String[] fruits = csv.split(",");    // ["apple", "banana", "cherry"]

String joined = String.join(" - ", fruits);  // "apple - banana - cherry"
```

## Code References
- `StringMethods.java` - Detailed examples

## Common Mistakes
1. Not handling `indexOf` returning -1
2. Forgetting that `split` uses regex
3. Assuming `replace` modifies the original string
4. Using `substring` with incorrect indices

## Interview Questions
1. What are the most commonly used String methods?
2. How does `split` handle consecutive delimiters?
3. What is the difference between `replace` and `replaceAll`?
4. How do you check if a string contains only digits?
5. What are the time complexity of common string operations?
