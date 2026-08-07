# Python Regular Expressions Reference

## What are Regular Expressions?

Regular expressions (regex) are patterns used to match character combinations in strings. The `re` module in Python provides tools for working with regular expressions.

## Why does Regular Expressions matter?

Understanding regex helps you:
- Search and extract text patterns
- Validate input data
- Parse complex strings
- Perform text transformations

---

## 1. Basic Patterns

```python
import re

# Literal characters
print(re.findall(r'hello', 'hello world'))  # ['hello']

# Special characters
print(re.findall(r'\d', 'abc123'))  # ['1', '2', '3']
print(re.findall(r'\w', 'hello world'))  # ['h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd']
print(re.findall(r'\s', 'hello world'))  # [' ']

# Quantifiers
print(re.findall(r'go*d', 'gd god good'))  # ['gd', 'god', 'good']
print(re.findall(r'go+d', 'gd god good'))  # ['god', 'good']
print(re.findall(r'go?d', 'gd god good'))  # ['gd', 'god']
```

---

## 2. Character Classes

```python
import re

# Dot (any character except newline)
print(re.findall(r'h.t', 'hat hit hot'))  # ['hat', 'hit', 'hot']

# Character set
print(re.findall(r'[aeiou]', 'hello'))  # ['e', 'o']

# Negated set
print(re.findall(r'[^aeiou]', 'hello'))  # ['h', 'l', 'l']

# Range
print(re.findall(r'[a-z]', 'Hello World'))  # ['e', 'l', 'l', 'o', 'o', 'r', 'l', 'd']
print(re.findall(r'[A-Za-z]', 'Hello World'))  # ['H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd']

# Special sets
print(re.findall(r'\d', 'abc123'))  # ['1', '2', '3']
print(re.findall(r'\D', 'abc123'))  # ['a', 'b', 'c']
print(re.findall(r'\w', 'hello 123'))  # ['h', 'e', 'l', 'l', 'o', '1', '2', '3']
print(re.findall(r'\W', 'hello 123'))  # [' ']
print(re.findall(r'\s', 'hello world'))  # [' ']
print(re.findall(r'\S', 'hello world'))  # ['h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd']
```

---

## 3. Anchors

```python
import re

# Start of string
print(re.findall(r'^Hello', 'Hello World'))  # ['Hello']
print(re.findall(r'^Hello', 'World Hello'))  # []

# End of string
print(re.findall(r'World$', 'Hello World'))  # ['World']
print(re.findall(r'World$', 'World Hello'))  # []

# Word boundary
print(re.findall(r'\bword\b', 'word words'))  # ['word']
print(re.findall(r'\bword\b', 'sword words'))  # []
```

---

## 4. Groups

```python
import re

# Basic group
match = re.search(r'(\w+) (\w+)', 'Hello World')
if match:
    print(match.group(0))  # Hello World (full match)
    print(match.group(1))  # Hello
    print(match.group(2))  # World

# Named groups
match = re.search(r'(?P<first>\w+) (?P<second>\w+)', 'Hello World')
if match:
    print(match.group('first'))  # Hello
    print(match.group('second'))  # World

# Groups with findall
print(re.findall(r'(\w+) (\w+)', 'Hello World Goodbye'))
# [('Hello', 'World'), ('Goodbye', '')]
```

---

## 5. Alternation

```python
import re

# Basic alternation
print(re.findall(r'cat|dog', 'I have a cat and a dog'))  # ['cat', 'dog']

# Grouped alternation
print(re.findall(r'(cat|dog)', 'I have a cat and a dog'))  # ['cat', 'dog']
```

---

## 6. Repetition

```python
import re

# {n} exactly n times
print(re.findall(r'a{3}', 'aa aaa aaaa'))  # ['aaa', 'aaa']

# {n,} at least n times
print(re.findall(r'a{2,}', 'aa aaa aaaa'))  # ['aa', 'aaa', 'aaaa']

# {n,m} between n and m times
print(re.findall(r'a{2,3}', 'aa aaa aaaa'))  # ['aa', 'aaa', 'aaa']

# * zero or more
print(re.findall(r'a*', 'aab'))  # ['aa', '', '', '']

# + one or more
print(re.findall(r'a+', 'aab'))  # ['aa']

# ? zero or one
print(re.findall(r'a?', 'aab'))  # ['a', 'a', '', '']
```

---

## 7. Non-Greedy Matching

```python
import re

# Greedy (default)
print(re.findall(r'<.*>', '<b>hello</b>'))  # ['<b>hello</b>']

# Non-greedy
print(re.findall(r'<.*?>', '<b>hello</b>'))  # ['<b>', '</b>']
```

---

## 8. Flags

```python
import re

# re.IGNORECASE
print(re.findall(r'hello', 'Hello WORLD', re.IGNORECASE))  # ['Hello']

# re.MULTILINE
print(re.findall(r'^Hello', 'Hello World\nHello Python', re.MULTILINE))  # ['Hello', 'Hello']

# re.DOTALL
print(re.findall(r'h.t', 'hat\nhit', re.DOTALL))  # ['hat', 'hit']
```

---

## 9. Substitution

```python
import re

# Basic substitution
print(re.sub(r'\d', 'NUM', 'abc123'))  # abcNUMNUMNUM

# With function
def replace_num(match):
    return f"[{match.group()}]"

print(re.sub(r'\d', replace_num, 'abc123'))  # abc[1][2][3]

# Limit
print(re.sub(r'\d', 'NUM', 'abc123', count=1))  # abcNUM23
```

---

## 10. Split

```python
import re

# Basic split
print(re.split(r'\s+', 'hello world foo'))  # ['hello', 'world', 'foo']

# With groups
print(re.split(r'(\s+)', 'hello world foo'))  # ['hello', ' ', 'world', ' ', 'foo']

# Limit
print(re.split(r'\s+', 'hello world foo', maxsplit=1))  # ['hello', 'world foo']
```

---

## One-Minute Revision Table

| Pattern | Description | Example |
|---------|-------------|---------|
| **.** | Any character | `r'h.t'` matches 'hat', 'hit' |
| **\d** | Digit | `r'\d'` matches '1' |
| **\w** | Word character | `r'\w'` matches 'a', '1' |
| **\s** | Whitespace | `r'\s'` matches ' ' |
| **[abc]** | Character set | `r'[aeiou]'` matches vowels |
| **[^abc]** | Negated set | `r'[^aeiou]'` matches non-vowels |
| **^** | Start of string | `r'^Hello'` matches 'Hello' at start |
| **$** | End of string | `r'World$'` matches 'World' at end |
| **\b** | Word boundary | `r'\bword\b'` matches 'word' |
| **(abc)** | Group | `r'(\w+) (\w+)'` captures two words |
| **(?P<name>abc)** | Named group | `r'(?P<first>\w+)'` captures named group |
| **\*** | Zero or more | `r'a*'` matches '', 'a', 'aa' |
| **+** | One or more | `r'a+'` matches 'a', 'aa' |
| **?** | Zero or one | `r'a?'` matches '', 'a' |
| **{n}** | Exactly n | `r'a{3}'` matches 'aaa' |
| **{n,}** | At least n | `r'a{2,}'` matches 'aa', 'aaa' |
| **{n,m}** | Between n and m | `r'a{2,3}'` matches 'aa', 'aaa' |
| **\|** | Alternation | `r'cat\|dog'` matches 'cat' or 'dog' |

---

## Common Mistakes

### 1. Forgetting Raw Strings

```python
# WRONG
re.findall('\d', 'abc123')  # Works but bad practice

# RIGHT
re.findall(r'\d', 'abc123')
```

### 2. Greedy vs Non-Greedy

```python
# WRONG (greedy)
re.findall(r'<.*>', '<b>hello</b>')  # ['<b>hello</b>']

# RIGHT (non-greedy)
re.findall(r'<.*?>', '<b>hello</b>')  # ['<b>', '</b>']
```

### 3. Not Compiling Patterns

```python
# WRONG (for repeated use)
for item in data:
    re.findall(r'\d+', item)

# RIGHT (compile once)
pattern = re.compile(r'\d+')
for item in data:
    pattern.findall(item)
```

### 4. Forgetting Groups in findall

```python
# WRONG
re.findall(r'(\w+) (\w+)', 'Hello World')  # [('Hello', 'World')]

# RIGHT (if you want full matches)
re.findall(r'\w+ \w+', 'Hello World')  # ['Hello World']
```

---

## Production Notes

1. **Always use raw strings** - Avoid escape character issues
2. **Compile patterns for repeated use** - More efficient
3. **Use non-greedy matching when needed** - Avoid unexpected matches
4. **Use named groups for clarity** - More readable
5. **Use `re.VERBOSE` for complex patterns** - More readable
6. **Be careful with user input** - Prevent ReDoS attacks
7. **Use `re.escape` for literal strings** - Escape special characters
8. **Test patterns thoroughly** - Edge cases matter
9. **Use `re.sub` for replacements** - More powerful than `str.replace`
10. **Use `re.split` for complex splitting** - More flexible than `str.split`

---

## Further Reading

- Python documentation on re module
- Regular Expressions Cookbook
- regex101.com - Online regex tester
