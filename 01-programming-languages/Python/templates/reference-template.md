# Reference Template

Use this template for reference documentation (e.g., `reference/[topic].md`).

---

## Template

```markdown
# [Topic] Reference

[One-sentence summary of what this reference covers.]

## What

[1-2 paragraphs defining the topic. What is it? How does it fit into Python?]

## Why

- **[Reason 1]:** [Explanation]
- **[Reason 2]:** [Explanation]
- **[Reason 3]:** [Explanation]

## API Reference

### [Class/Function/Module 1]

```python
# Signature
ClassName(param1: type, param2: type) -> ReturnType

# Description of what it does
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `param1` | `type` | — | [What it does] |
| `param2` | `type` | `value` | [What it does] |

**Returns:** [What the return value means]

**Raises:** [Exceptions that can occur]

---

### [Class/Function/Module 2]

```python
# Signature and description
```

## Examples

### Basic Usage

```python
# Simplest working example
```

### Common Pattern 1

```python
# Real-world usage pattern
```

### Common Pattern 2

```python
# Another real-world usage pattern
```

## Common Mistakes

| Mistake | Why It's Wrong | Correct Approach |
|---------|---------------|-----------------|
| [Bad pattern] | [Reason it fails] | [Better way] |
| [Bad pattern] | [Reason it fails] | [Better way] |

## Production Notes

- **Thread Safety:** [Is it thread-safe? How?]
- **Performance:** [Time/space complexity]
- **Memory:** [Memory behavior and gotchas]
- **Alternatives:** [When to use something else]

## See Also

- [Related Topic 1](link) — [Why it's related]
- [Related Topic 2](link) — [Why it's related]
```

---

## Formatting Guidelines

- Title must match the API/module being documented: "collections.defaultdict Reference" not "Dictionary Reference"
- "What" section must define the concept in 1-2 paragraphs — not a tutorial
- "Why" must have 3+ reasons for using this over alternatives
- API Reference must include actual function signatures with type hints
- Parameter tables must include types, defaults, and descriptions
- Examples must range from basic to common real-world patterns
- Common Mistakes must address actual errors students make
- Production Notes must answer: thread safety, performance, memory, alternatives

---

## Example: collections.defaultdict Reference

```markdown
# collections.defaultdict Reference

`defaultdict` is a `dict` subclass that calls a factory function to provide default values for missing keys, eliminating `KeyError` and reducing boilerplate for grouping and counting patterns.

## What

`defaultdict` extends Python's built-in `dict` with a `default_factory` that is called when a missing key is accessed via `__getitem__`. Instead of raising `KeyError`, it calls `default_factory()` and inserts the result. The factory function takes no arguments and returns the default value for the dict's value type.

This is particularly useful for grouping patterns (e.g., "word → list of sentences"), counting patterns (e.g., "item → count"), and accumulating results across iterations.

## Why

- **Eliminates KeyError:** Missing keys return a default instead of raising an exception
- **Reduces boilerplate:** No need for `if key not in d: d[key] = []` patterns
- **Grouping patterns:** Naturally collects items into groups during iteration
- **Performance:** Slightly faster than `dict.setdefault()` for repeated key access

## API Reference

### defaultdict

```python
from collections import defaultdict

d = defaultdict(default_factory, *args, **kwargs)
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `default_factory` | `callable` or `None` | `None` | Function called to create default values. `None` behaves like normal `dict`. |

**Returns:** A new `defaultdict` object

**Raises:** `TypeError` if `default_factory` is not callable and not `None`

### Inherited from dict

| Method | Description |
|--------|-------------|
| `d[key]` | Returns `default_factory()` if key is missing, stores and returns it |
| `d.get(key, default)` | Does NOT trigger `default_factory` — returns `default` argument |
| `d.setdefault(key, default)` | Triggers `default_factory` if key is missing and `default` not provided |
| `d.update(other)` | Updates with another dict; does not trigger factory for existing keys |

## Examples

### Basic Usage

```python
from collections import defaultdict

d = defaultdict(int)  # default value: 0
d["apples"] += 1
d["apples"] += 1
print(d["apples"])  # 2
print(d["oranges"])  # 0 (auto-created)
```

### Grouping Pattern

```python
from collections import defaultdict

sentences = [
    "the cat sat",
    "the dog ran",
    "the cat played"
]

word_sentences = defaultdict(list)
for sentence in sentences:
    for word in sentence.split():
        word_sentences[word].append(sentence)

print(word_sentences["the"])
# ['the cat sat', 'the dog ran', 'the cat played']
```

### Counting Pattern

```python
from collections import defaultdict

words = ["apple", "banana", "apple", "cherry", "banana", "apple"]
counts = defaultdict(int)
for word in words:
    counts[word] += 1

print(dict(counts))
# {'apple': 3, 'banana': 2, 'cherry': 1}
```

### Nested Structure

```python
from collections import defaultdict

def nested_dict():
    return defaultdict(int)

graph = defaultdict(nested_dict)
graph["A"]["B"] = 5
graph["A"]["C"] = 3
graph["B"]["A"] = 2

print(graph["A"]["B"])  # 5
print(graph["D"]["X"])  # 0 (auto-created)
```

## Common Mistakes

| Mistake | Why It's Wrong | Correct Approach |
|---------|---------------|-----------------|
| `d.get(key)` triggers factory | `get()` does NOT call `default_factory` — it returns `None` | Use `d[key]` to trigger the factory |
| `defaultdict(list)` vs `defaultdict(dict)` | Wrong factory for the pattern | Match factory to accumulation: `list` for grouping, `int` for counting |
| Checking `if key in d` before access | Defeats the purpose — factory handles missing keys | Just access `d[key]` directly |
| Using `defaultdict` with `json.dump` | `defaultdict` is not JSON-serializable by default | Convert to `dict` first: `json.dump(dict(d), f)` |

## Production Notes

- **Thread Safety:** Not thread-safe. Use `threading.Lock` for concurrent access.
- **Performance:** `d[key]` on missing key is O(1) factory call + O(1) insert. Faster than `setdefault()` for hot paths.
- **Memory:** Each entry stores the value plus dict overhead. `default_factory` is stored once per dict, not per key.
- **Alternatives:** Use `dict.setdefault()` if you need different defaults per access. Use `Counter` for counting patterns. Use `dict` with `try/except KeyError` if factory has side effects.

## See Also

- [`collections.Counter`](https://docs.python.org/3/library/collections.html#collections-specialized-containers) — Optimized counting dictionary
- [`dict.setdefault()`](https://docs.python.org/3/library/stdtypes.html#dict.setdefault) — Similar pattern without factory function
- [`itertools.groupby()`](https://docs.python.org/3/library/itertools.html#itertools.groupby) — Grouping with sorting requirements
```

---

## Checklist

Before publishing reference documentation:

- [ ] Title is specific to the API/module
- [ ] "What" defines in 1-2 paragraphs
- [ ] "Why" has 3+ reasons
- [ ] API Reference includes actual function signatures
- [ ] Parameter table has types, defaults, descriptions
- [ ] Examples range from basic to advanced
- [ ] Common Mistakes address real student errors
- [ ] Production Notes cover thread safety, performance, memory
- [ ] See Also links to related topics
