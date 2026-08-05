# String Patterns

## Overview
This module covers common string pattern problems including palindrome, anagram, reverse, and duplicate character detection.

## Key Concepts

### 1. Palindrome
A string that reads the same forwards and backwards.
```java
boolean isPalindrome(String s) {
    String cleaned = s.toLowerCase().replaceAll("[^a-z0-9]", "");
    return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
}
```

### 2. Anagram
Two strings containing the same characters in different orders.
```java
boolean isAnagram(String s1, String s2) {
    char[] chars1 = s1.toLowerCase().toCharArray();
    char[] chars2 = s2.toLowerCase().toCharArray();
    Arrays.sort(chars1);
    Arrays.sort(chars2);
    return Arrays.equals(chars1, chars2);
}
```

### 3. Reverse String
```java
String reverse(String s) {
    return new StringBuilder(s).reverse().toString();
}
```

### 4. Duplicate Characters
```java
Set<Character> findDuplicates(String s) {
    Set<Character> seen = new HashSet<>();
    Set<Character> duplicates = new HashSet<>();
    for (char c : s.toCharArray()) {
        if (!seen.add(c)) {
            duplicates.add(c);
        }
    }
    return duplicates;
}
```

## Code References
- `StringPatterns.java` - Comprehensive examples

## Common Mistakes
1. Not handling null or empty strings
2. Ignoring case sensitivity when not needed
3. Not considering special characters in palindrome check
4. Inefficient algorithms for pattern matching

## Interview Questions
1. How do you check if a string is a palindrome?
2. How do you check if two strings are anagrams?
3. What are different ways to reverse a string?
4. How do you find duplicate characters in a string?
5. How do you count character frequency in a string?
