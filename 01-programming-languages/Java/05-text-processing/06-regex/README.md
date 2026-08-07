# Java Regular Expressions

## Why Regex Exists

You need to validate an email address, extract dates from a log file, or find all URLs in a block of text. You could write dozens of `startsWith`, `contains`, and `substring` calls — or you could express what you're looking for in a pattern. Regular expressions let you describe text patterns concisely, but they come with trade-offs: they're powerful, but easy to get wrong and expensive when poorly written. This section covers when regex is the right tool, when it's not, and how to write patterns that don't explode your CPU.

## Table of Contents
1. [Pattern and Matcher Overview](#pattern-and-matcher-overview)
2. [Common Patterns Reference](#common-patterns-reference)
3. [Quantifiers Reference](#quantifiers-reference)
4. [Character Classes Reference](#character-classes-reference)
5. [Groups and Capturing](#groups-and-capturing)
6. [Lookahead and Lookbehind](#lookahead-and-lookbehind)
7. [Performance Tips](#performance-tips)
8. [Common Interview Questions](#common-interview-questions)

---

## Pattern and Matcher Overview

### Pattern Class
The `Pattern` class is a compiled representation of a regular expression. It is immutable and thread-safe.

```java
// Compile a pattern
Pattern pattern = Pattern.compile("regex");

// Compile with flags
Pattern pattern = Pattern.compile("regex", Pattern.CASE_INSENSITIVE);
```

**Key Methods:**
| Method | Description |
|--------|-------------|
| `compile(String regex)` | Compiles the given regex into a Pattern |
| `matcher(String input)` | Creates a Matcher for the given input |
| `matches(String regex, CharSequence input)` | Static convenience method for matching |
| `split(CharSequence input)` | Splits input around matches |
| `pattern()` | Returns the regex string |

### Matcher Class
The `Matcher` class performs matching operations on a character sequence by interpreting a `Pattern`.

```java
Pattern pattern = Pattern.compile("\\d+");
Matcher matcher = pattern.matcher("abc123def456");
```

**Key Methods:**
| Method | Description |
|--------|-------------|
| `matches()` | Attempts to match the entire input sequence |
| `lookingAt()` | Attempts to match the input from the beginning |
| `find()` | Finds the next match |
| `group()` | Returns the matched subsequence |
| `group(int group)` | Returns the captured group by index |
| `group(String name)` | Returns the named capturing group |
| `start()` | Returns the start index of the previous match |
| `end()` | Returns the offset after the last match |
| `reset()` | Resets the matcher |

---

## Common Patterns Reference

| Pattern | Description | Example Match |
|---------|-------------|---------------|
| `[a-zA-Z]` | Any letter | "a", "Z" |
| `[0-9]` or `\\d` | Any digit | "5", "9" |
| `[a-zA-Z0-9]` or `\\w` | Word character | "a", "5", "_" |
| `\\s` | Whitespace | " ", "\t" |
| `.` | Any character (except newline) | "a", "5", "@" |
| `^` | Start of line | ^Hello |
| `$` | End of line | world$ |
| `\\b` | Word boundary | \bcat\b |

### Email Validation
```
^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$
```

### Phone Number (US)
```
\\d{3}-\\d{3}-\\d{4}
```

### URL
```
https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}
```

---

## Quantifiers Reference

| Quantifier | Description | Example |
|------------|-------------|---------|
| `*` | Zero or more | `a*` matches "", "a", "aa" |
| `+` | One or more | `a+` matches "a", "aa" |
| `?` | Zero or one | `a?` matches "", "a" |
| `{n}` | Exactly n times | `a{3}` matches "aaa" |
| `{n,}` | n or more times | `a{2,}` matches "aa", "aaa" |
| `{n,m}` | Between n and m times | `a{2,4}` matches "aa", "aaa", "aaaa" |

### Greedy vs Lazy
- **Greedy**: Matches as much as possible (default)
  - `.*` matches entire string
- **Lazy**: Matches as little as possible (add `?`)
  - `.*?` matches shortest possible

```java
String text = "<b>bold</b> and <i>italic</i>";
// Greedy: matches "<b>bold</b> and <i>italic</i>"
Pattern.compile("<.*>").matcher(text).find();

// Lazy: matches "<b>" then "<i>"
Pattern.compile("<.*?>").matcher(text).find();
```

---

## Character Classes Reference

| Syntax | Description |
|--------|-------------|
| `[abc]` | a, b, or c |
| `[^abc]` | Not a, b, or c |
| `[a-zA-Z]` | a to z or A to Z |
| `[a-z&&[def]]` | d, e, or f (intersection) |
| `[a-z&&[^bc]]` | a to z except b and c (subtraction) |
| `[a-z&&[^m-p]]` | a to z except m to p |

### Predefined Character Classes
| Syntax | Description |
|--------|-------------|
| `.` | Any character |
| `\d` | Digit: `[0-9]` |
| `\D` | Non-digit: `[^0-9]` |
| `\w` | Word character: `[a-zA-Z0-9_]` |
| `\W` | Non-word character |
| `\s` | Whitespace: `[ \t\n\x0B\f\r]` |
| `\S` | Non-whitespace |

---

## Groups and Capturing

Groups are created using parentheses `()`.

### Capturing Groups
```java
String regex = "(\\w+)\\s(\\w+)";
// Group 1: First word
// Group 2: Second word
```

### Accessing Groups
```java
Matcher matcher = pattern.matcher(input);
if (matcher.matches()) {
    matcher.group(0);  // Entire match
    matcher.group(1);  // First capturing group
    matcher.group(2);  // Second capturing group
}
```

### Non-capturing Groups
Use `(?:...)` to group without capturing:
```java
String regex = "(?:https?|ftp)://[a-zA-Z0-9.-]+";
```

### Named Groups
```java
String regex = "(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})";
Matcher matcher = pattern.matcher("2024-01-15");
if (matcher.matches()) {
    String year = matcher.group("year");
    String month = matcher.group("month");
}
```

---

## Lookahead and Lookbehind

### Positive Lookahead `(?=...)`
Matches if followed by the pattern:
```java
"cat(?=\\d)"  // "cat" followed by a digit
```

### Negative Lookahead `(?!...)`
Matches if NOT followed by the pattern:
```java
"cat(?!\\d)"  // "cat" not followed by a digit
```

### Positive Lookbehind `(?<=...)`
Matches if preceded by the pattern:
```java
"(?<=\\d)cat"  // "cat" preceded by a digit
```

### Negative Lookbehind `(?<!...)`
Matches if NOT preceded by the pattern:
```java
"(?<!\\d)cat"  // "cat" not preceded by a digit
```

---

## Performance Tips

1. **Compile patterns once**: Reuse compiled `Pattern` objects
   ```java
   // Bad
   for (String s : list) {
       s.matches("\\d+");  // Compiles pattern each time
   }
   
   // Good
   Pattern pattern = Pattern.compile("\\d+");
   for (String s : list) {
       pattern.matcher(s).matches();
   }
   ```

2. **Use non-capturing groups** when you don't need to capture:
   ```java
   "(?:http|https)://"  // Better than "(http|https)://"
   ```

3. **Be specific**: Avoid `.*` when possible
   ```java
   // Bad
   ".*email.*"
   
   // Good
   "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
   ```

4. **Use `Pattern.quote()`** for user input:
   ```java
   String userInput = "special.pattern";
   Pattern pattern = Pattern.compile(Pattern.quote(userInput));
   ```

5. **Avoid backtracking** in complex patterns

---

## When NOT to Use This

- Simple string operations suffice — `startsWith`, `contains`, `equals` are faster and clearer
- The pattern is trivial — if a `String.split()` or `indexOf()` works, use it
- User input is untrusted — regex is vulnerable to ReDoS (catastrophic backtracking attacks)
- You need to parse structured data like JSON or XML — use a proper parser, not regex

## Trade-offs

| Aspect | Regex | String methods |
|--------|-------|---------------|
| Expressiveness | Powerful pattern matching | Limited to exact matches |
| Readability | Cryptic to non-experts | Self-documenting |
| Performance | Compilation overhead, backtracking risk | Fast for simple operations |
| Maintenance | Hard to debug complex patterns | Easy to understand |
| Flexibility | Handles variable formats | Requires knowing exact format |

## Engineering Decision Framework

### ✅ Use Regex when:
- Complex pattern matching is required (email, URL, phone validation)
- Text extraction with capture groups is needed
- Search-and-replace with patterns is required
- Input validation against structured formats
- Log file parsing with variable formats

### ❌ Avoid Regex when:
- Simple string operations suffice (startsWith, contains, equals)
- Performance is critical in hot paths (regex compilation overhead)
- Patterns are simple and static (use String methods instead)
- User input is untrusted (risk of ReDoS attacks)

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| String methods | Simple prefix/suffix/contains checks |
| StringTokenizer | Basic delimiter-based splitting |
| Scanner | Token-based input parsing |
| Parser combinators | Complex grammar-based parsing |

### Production Examples
- Email and phone number validation
- URL parsing and normalization
- Log file pattern extraction
- CSV/TSV data processing
- Security input sanitization

### Common Production Mistakes
- Compiling patterns inside loops (reuse compiled Pattern objects)
- Using greedy quantifiers where lazy is needed (catastrophic backtracking)
- Not using Pattern.quote() for user-provided patterns
- Overly complex regex that nobody can maintain
- Not testing edge cases (empty strings, special characters)

## Production Checklist

### ✅ Before using Regex in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Common Interview Questions

### Q1: What is the difference between `matches()` and `find()`?
**A:** `matches()` checks if the entire string matches the pattern. `find()` searches for the pattern anywhere in the string.

### Q2: How do you escape special characters in regex?
**A:** Use `Pattern.quote()` or double backslashes (`\\`).

### Q3: What are the differences between greedy and lazy quantifiers?
**A:** Greedy (`*`, `+`) matches as much as possible. Lazy (`*?`, `+?`) matches as little as possible.

### Q4: How do you extract data using groups?
**A:** Use `matcher.group(n)` for numbered groups or `matcher.group("name")` for named groups.

### Q5: What are lookahead and lookbehind assertions?
**A:** They match patterns without consuming characters. Lookahead checks what follows; lookbehind checks what precedes.

### Q6: How do you make a regex pattern case-insensitive?
**A:** Use `Pattern.compile("regex", Pattern.CASE_INSENSITIVE)` or inline `(?i)`.

### Q7: What is the difference between `^` in `[^abc]` and `^Hello`?
**A:** In `[^abc]`, `^` inside brackets means negation. At the start, `^` anchors to the beginning of the line.

---

## Alternatives

| Approach | Pattern Matching | Performance | Maintenance | Use When |
|----------|-----------------|-------------|-------------|----------|
| Regex | Full | Moderate | Hard | Complex pattern matching |
| String methods | Basic | High | Easy | Simple prefix/suffix/contains |
| StringTokenizer | Basic | High | Easy | Basic delimiter-based splitting |
| Scanner | Token-based | Moderate | Easy | Token-based input parsing |
| Parser combinators | Full | Moderate | Moderate | Complex grammar-based parsing |

## Trade-offs

Regex provides powerful pattern matching because it:
- Compiling patterns is expensive (reuse compiled Pattern objects)
- Can be vulnerable to ReDoS attacks (test with adversarial input)
- Greedy quantifiers cause catastrophic backtracking (use possessive or atomic groups)
- Hard to read and maintain (document complex patterns thoroughly)
- Not portable across languages (Java-specific syntax nuances exist)

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations

## Additional Resources
- [Java Regex Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/regex/package-summary.html)
- [Regex101](https://regex101.com/) - Online regex tester
- [RegExr](https://regexr.com/) - Another regex testing tool

## Common Myths

### ❌ Myth 1: Regex is always the best solution
**Reality:** Often overkill. Simple string methods (startsWith, contains) are faster for basic operations.

### ❌ Myth 2: Compiled patterns are always faster
**Reality:** Depends on reuse. Compiling once and reusing is faster; compiling in loops is wasteful.

### ❌ Myth 3: Regex is portable
**Reality:** Flavors differ. Java regex syntax may not work in other languages or tools.

## Learning Objectives

By the end of this topic you will be able to:

- Compile and reuse Pattern objects for efficient repeated matching
- Use capturing and named groups to extract structured data from text
- Distinguish greedy from lazy quantifiers and predict their behavior on real input
- Write regex patterns that avoid catastrophic backtracking
- Know when regex is the right tool and when a simple String method is better
