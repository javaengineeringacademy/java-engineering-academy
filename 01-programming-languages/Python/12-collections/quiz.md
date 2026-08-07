# Collections Quiz

## Question 1 (Multiple Choice)
What is the time complexity of checking if an element exists in a `set` versus a `list` in Python?

- A) Both are O(1) — Python optimizes both
- B) Set membership is O(1) on average; list membership is O(n)
- C) List membership is O(1) due to indexing; set is O(n)
- D) Both are O(n) — must scan all elements

**Answer: B**
**Explanation:** Sets use hash tables internally, so membership checks average O(1) by computing the hash and looking up the bucket. Lists require a linear scan through all elements — O(n). This is why sets are preferred for "is this element in the collection?" questions, especially with large datasets. The trade-off is that sets are unordered and don't support indexing.

---

## Question 2 (Multiple Choice)
When should you use `defaultdict` over a regular `dict` with `.get()` or `.setdefault()`?

- A) Always — `defaultdict` is always faster
- B) When you need to aggregate values into groups (e.g., word lists by first letter) and want cleaner code without boilerplate key creation
- C) When you need to handle missing keys with custom logic beyond a default value
- D) `defaultdict` is deprecated — use `dict` with `setdefault()` instead

**Answer: B**
**Explanation:** `defaultdict(list)` eliminates the `if key not in d: d[key] = []` boilerplate. It's cleaner for grouping operations like `d[word[0]].append(word)`. However, `dict.get()` is better when you want a default without modifying the dict, and `.setdefault()` is useful when you want atomic get-and-set. `defaultdict` is not deprecated — it's a standard library tool with clear use cases.

---

## Question 3 (Multiple Choice)
What is the key difference between `frozenset` and `set` in Python?

- A) `frozenset` is faster but smaller
- B) `frozenset` is immutable and hashable; `set` is mutable and unhashable
- C) `frozenset` can store duplicates; `set` cannot
- D) There is no difference — `frozenset` is just an alias

**Answer: B**
**Explanation:** `frozenset` is the immutable version of `set`. Because it's immutable, it can be used as a dictionary key or as an element in another set — something `set` cannot do. `set` is mutable (add/remove operations) but unhashable. Use `frozenset` when you need a set-like collection that must not change, such as config keys or cache identifiers.

---

## Question 4 (Multiple Choice)
Which `collections` module class is optimized for O(1) append and pop from both ends, making it ideal for implementing queues?

- A) `list` — fast append/pop from the end
- B) `deque` (double-ended queue) — O(1) operations on both ends
- C) `defaultdict` — fast key lookup
- D) `Counter` — counting elements

**Answer: B**
**Explanation:** `deque` (from `collections`) uses a doubly-linked list of blocks internally, giving O(1) append and pop from both ends. Python's `list` has O(1) append/pop from the end but O(n) from the front because it shifts all elements. For queue implementations (`collections.deque` is the recommended approach), always prefer `deque` over `list`. The name comes from "double-ended queue."

---

## Question 5 (Code Output)
What is the output of this code?

```python
from collections import Counter

words = ["apple", "banana", "apple", "cherry", "banana", "apple"]
word_counts = Counter(words)
print(word_counts.most_common(2))
```

**Answer:** `[('apple', 3), ('banana', 2)]`
**Explanation:** `Counter` creates a dictionary-like object mapping elements to their counts. `most_common(n)` returns the `n` most frequent elements as (element, count) tuples sorted by count descending. "apple" appears 3 times, "banana" appears 2 times, "cherry" appears once. `most_common(2)` returns the top two. This is far more efficient than manually counting with loops or dictionaries.

---

## Question 6 (Code Output)
What is the output of this code?

```python
from collections import defaultdict

d = defaultdict(list)
for word in ["hello", "world", "hey", "wish", "wonderful"]:
    d[word[0]].append(word)

print(dict(d))
```

**Answer:** `{'h': ['hello', 'hey'], 'w': ['world', 'wish', 'wonderful']}`
**Explanation:** `defaultdict(list)` automatically creates an empty list for any new key. When `word[0]` is `'h'` and `'h'` doesn't exist yet, it creates `d['h'] = []` and then appends. Same for `'w'`. Converting to `dict` with `dict(d)` removes the `defaultdict` type wrapper but keeps the data. The result groups words by their first letter — a classic use case for `defaultdict`.

---

## Question 7 (Bug Finding)
Find the bug in this code:

```python
from collections import Counter

def top_words(text):
    words = text.lower().split()
    counts = Counter(words)
    return counts.most_common(3)

result = top_words("the cat sat on the mat the cat")
print(result)
```

**Bug:** The function counts words case-insensitively (good), but doesn't strip punctuation. If the input is `"the cat, sat on the mat."`, the counts would be `{'the': 2, 'cat,': 1, 'sat': 1, ...}` — punctuation becomes part of the word. "cat" and "cat," are different tokens. Additionally, this doesn't handle stop words ("the", "on", "sat") which often dominate word frequency lists.
**Fix:** Add punctuation stripping and optionally filter stop words:
```python
import re
from collections import Counter

def top_words(text):
    words = re.findall(r'\b\w+\b', text.lower())
    return Counter(words).most_common(3)
```

---

## Question 8 (Bug Finding)
Find the bug in this code:

```python
my_list = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5]
unique_ordered = list(set(my_list))
print(unique_ordered)
```

**Bug:** Converting to `set` removes duplicates (intended) but destroys ordering. Sets are unordered, so `list(set(my_list))` produces a list in an unpredictable order. The comment suggests the developer wanted unique elements in their original order. This is a common Python gotcha.
**Fix:** Use `dict.fromkeys()` to preserve order (Python 3.7+ guarantees dict insertion order):
```python
unique_ordered = list(dict.fromkeys(my_list))
# [3, 1, 4, 5, 9, 2, 6]
```
Or use `OrderedDict.fromkeys()` for older Python versions.

---

## Question 9 (Scenario)
You're processing a 10GB CSV file that doesn't fit in memory. You need to find the top 10 most frequent values in a specific column. How should you approach this?

- A) Load the entire file into a list and use `Counter`
- B) Stream the file line by line, maintain a running `Counter` in chunks, and merge results
- C) Use `set` to track unique values and count manually
- D) Convert the file to JSON first for easier processing

**Answer: B**
**Explanation:** Streaming avoids loading the entire file into memory. Process chunks (e.g., 100K lines at a time), update a `Counter` for each chunk, and periodically merge/purge small counts. After processing, the Counter holds the top results. Option A would cause OOM. Option C loses count information. Option D is wasteful — JSON is larger than CSV. This is how production ETL pipelines handle large datasets.

---

## Question 10 (Architecture Decision)
You're designing a cache system that stores 1M key-value pairs. Keys must be checked in O(1), entries expire after a TTL, and you need to track access order for LRU eviction. Which combination of collections should you use?

- A) `dict` for storage + `set` for TTL tracking
- B) `OrderedDict` (or `dict` in 3.7+) for LRU order + `dict` with timestamps for TTL + `set` for fast membership checks
- C) `list` for all operations — simple and fast enough
- D) `Counter` for everything — it's optimized for counts

**Answer: B**
**Explanation:** `OrderedDict` (or `dict` in Python 3.7+ which preserves insertion order) provides O(1) move-to-end for access order tracking. A separate `dict` mapping keys to expiry timestamps enables O(1) TTL checks. A `set` of expired keys allows O(1) membership testing during cleanup. This layered approach handles the three distinct requirements: LRU eviction, TTL expiration, and fast lookup. Libraries like `cachetools` implement exactly this architecture.

---
