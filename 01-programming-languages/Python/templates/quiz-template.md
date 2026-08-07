# Quiz Template

Use this template for quiz files (e.g., `quiz.md` in any module).

---

## Template

```markdown
# [Module Name] Quiz

**Questions:** [N]
**Passing Score:** [X/N]
**Time Limit:** [Optional — X minutes]

---

## Questions

### Q1: [Question Text]

**Type:** [MCQ | Code Output | Bug Finding | Scenario | Architecture]

```python
# Code block (if applicable)
```

A) [Option A]
B) [Option B]
C) [Option C]
D) [Option D]

<details>
<summary>Answer</summary>

**Correct:** [Letter]

**Explanation:** [Why this is correct and why others are wrong]

**Code example (if applicable):**

```python
# Supporting code
```

</details>

---

### Q2: [Question Text]

**Type:** [MCQ | Code Output | Bug Finding | Scenario | Architecture]

[Repeat format]

---

## Score

| Metric | Result |
|--------|--------|
| Correct | ? / [N] |
| Passing | [X] / [N] |
| Status | [Pass | Fail] |

## Review Topics

If you scored below passing, revisit:
- [Topic area 1]
- [Topic area 2]
- [Topic area 3]
```

---

## Question Types

Use a mix of these types:

### 1. MCQ (Multiple Choice — Conceptual)

Tests theoretical understanding without code.

```markdown
### Q1: Which statement about Python's GIL is correct?

**Type:** MCQ

A) The GIL prevents all concurrency in Python
B) The GIL protects against data races in CPython's memory manager
C) The GIL can be removed with the `-nogil` flag in all Python versions
D) The GIL only affects single-threaded programs

<details>
<summary>Answer</summary>

**Correct:** B

**Explanation:** The GIL (Global Interpreter Lock) prevents multiple threads from executing Python bytecode simultaneously, protecting CPython's reference counting and garbage collection. It does not prevent all concurrency — `asyncio` and I/O-bound threading still work. The `-nogil` flag is experimental (PEP 703) and not available in all versions.

</details>
```

### 2. Code Output

Tests ability to trace execution and predict results.

```markdown
### Q2: What is the output of this code?

**Type:** Code Output

```python
data = [1, 2, 3, 4, 5]
result = [x * 2 for x in data if x % 2 == 0]
print(result)
```

A) `[2, 4, 6, 8, 10]`
B) `[2, 4]`
C) `[4, 8]`
D) `[1, 2, 3, 4, 5]`

<details>
<summary>Answer</summary>

**Correct:** C

**Explanation:** The list comprehension filters even numbers (`2, 4`) then doubles them. `x * 2 for x in [2, 4]` produces `[4, 8]`. The `if x % 2 == 0` filter runs before the expression `x * 2`.

</details>
```

### 3. Bug Finding

Tests debugging skills and understanding of common mistakes.

```markdown
### Q3: This code is supposed to merge two dictionaries but fails. Find the bug:

**Type:** Bug Finding

```python
def merge_dicts(dict1, dict2):
    result = dict1
    result.update(dict2)
    return result

a = {"x": 1}
b = {"y": 2}
c = merge_dicts(a, b)
print(a)  # Expected: {"x": 1} — Actual: {"x": 1, "y": 2}
```

A) `dict1` is not copied — `result` references the same object
B) `.update()` returns `None` instead of the merged dict
C) `dict2` is modified instead of `dict1`
D) The function should use `**` unpacking instead

<details>
<summary>Answer</summary>

**Correct:** A

**Explanation:** `result = dict1` makes `result` reference the same dict object. `.update()` modifies `result` in-place, which also modifies `a`. Fix: `result = dict1.copy()` or `result = {**dict1, **dict2}`.

</details>
```

### 4. Scenario

Tests decision-making and design skills.

```markdown
### Q4: You are building a REST API that processes 10,000 requests/second. Which approach is best?

**Type:** Scenario

A) Use synchronous Flask with a thread pool
B) Use FastAPI with async/await and uvicorn
C) Use multiprocessing with Flask
D) Use Twisted for the entire application

<details>
<summary>Answer</summary>

**Correct:** B

**Explanation:** FastAPI with async/await is designed for high-concurrency I/O-bound workloads like REST APIs. It uses uvicorn (ASGI) and Starlette's async routing, handling thousands of concurrent connections efficiently. Synchronous Flask with threads doesn't scale well. Multiprocessing has high overhead for I/O-bound tasks. Twisted is viable but has a steeper learning curve and less ecosystem support than FastAPI.

</details>
```

### 5. Architecture

Tests system design and trade-off analysis.

```markdown
### Q5: Which exception handling pattern is correct for a library?

**Type:** Architecture

A) Catch all exceptions, log them, return `None`
B) Let all exceptions propagate to the caller
C) Catch specific exceptions, handle recoverable ones, re-raise unrecoverable ones
D) Convert all exceptions to `ValueError`

<details>
<summary>Answer</summary>

**Correct:** C

**Explanation:** Libraries should catch specific exceptions (e.g., `FileNotFoundError`, `ValueError`), handle what they can recover from (retry, fallback), and re-raise or wrap unrecoverable ones with context. Bare `except:` swallows bugs. Returning `None` hides failures. Converting to `ValueError` loses error context.

</details>
```

---

## Formatting Guidelines

- Questions should test understanding, not memorization
- Explanations should teach — state WHY, not just WHAT
- Mix question types: 40% MCQ, 30% Code Output, 20% Bug Finding, 10% Scenario/Architecture
- Include code-based questions — Python is learned by reading code
- Every question must have exactly one correct answer
- Explanations must address why wrong answers are wrong
- Questions must be unambiguous — one correct interpretation
- Passing score: 70% minimum

---

## Checklist

Before publishing a quiz:

- [ ] Questions are numbered Q1 through Q[N]
- [ ] Each question has a type label
- [ ] Each question has exactly 4 options (A-D)
- [ ] Each question has a `<details>` answer block
- [ ] Explanations teach, not just state the answer
- [ ] Mix of question types (not all MCQ)
- [ ] No trick questions or ambiguous wording
- [ ] Passing score is set (default 70%)
- [ ] Review topics are listed for remediation
