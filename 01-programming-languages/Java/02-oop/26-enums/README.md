# Enums in Java

## Overview
Enums are special classes that represent a fixed set of constants. They can have fields, methods, and implement interfaces.

## When to Use
- For fixed sets of constants (days, seasons, states)
- When you need type-safe constants
- For enum-specific behavior and fields

## Code Example
See `src/main/java/academy/javaengineering/oop/enums/` (Day.java, Season.java)

```java
Day today = Day.MONDAY;
today.isWeekend();     // false
today.getAbbreviation(); // "Mon"
```

## Common Mistakes
1. Using int constants instead of enums
2. Not using enums for type safety
3. Forgetting enums can have fields and methods
4. Using ordinal() for comparison (fragile)

## Interview Questions
1. What are the benefits of enums over int constants?
2. Can enums implement interfaces?
3. What methods do all enums have?
4. How do you iterate over all enum values?
5. What is the EnumSet and EnumMap?
