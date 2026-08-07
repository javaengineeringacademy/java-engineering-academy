# Python Templates — Standard Formats for Learning Content

Consistency makes content maintainable. Use these templates when creating new exercises, quizzes, or documentation.

---

## Standard README Template

Use this for any Python module, topic folder, or project.

```markdown
# [Title]

**Level:** [Beginner | Intermediate | Advanced]
**Estimated Time:** [X hours/minutes]
**Prerequisites:** [What you need to know first]

---

## What You'll Learn

- [ ] Concept 1
- [ ] Concept 2
- [ ] Concept 3

## Why It Matters

[1-2 sentences explaining real-world relevance]

## Quick Example

```python
# The most minimal working example
# Should run and produce output
```

## Deep Dive

### [Subtopic 1]

[Explanation with code examples]

### [Subtopic 2]

[Explanation with code examples]

## Common Mistakes

| Mistake | Why It's Wrong | Correct Approach |
|---------|---------------|-----------------|
| [Bad pattern] | [Reason] | [Better way] |

## Exercises

1. [Exercise 1 — description]
2. [Exercise 2 — description]
3. [Exercise 3 — description]

## Resources

- [Link 1 — description]
- [Link 2 — description]

---

*Next: [Link to next topic]*
```

**Filling in the template:**
- Title should be specific: "Context Managers" not "Advanced Python"
- Time estimates help learners plan their sessions
- Prerequisites prevent frustration from missing foundations
- The "Why It Matters" section answers the student's inevitable "so what?"

---

## Standard Exercise Template

Use this for individual coding exercises.

```markdown
# Exercise: [Name]

**Topic:** [Concept being tested]
**Difficulty:** [⭐ | ⭐⭐ | ⭐⭐⭐]
**Time:** [X minutes]

---

## Problem Statement

[Clear, specific description of what the learner needs to build or fix]

### Requirements

- [ ] Requirement 1
- [ ] Requirement 2
- [ ] Requirement 3

### Constraints

- [Constraint 1 — e.g., "Do not use `eval()`"]
- [Constraint 2 — e.g., "Must handle empty input"]

## Starter Code

```python
def solution():
    pass
    # Your code here
```

## Test Cases

```python
# Example 1
assert solution(input1) == expected1

# Example 2
assert solution(input2) == expected2

# Edge case
assert solution(edge_input) == expected_edge
```

## Hints

<details>
<summary>Hint 1</summary>

[Conceptual nudge without giving away the answer]

</details>

<details>
<summary>Hint 2</summary>

[More specific guidance]

</details>

## Solution

<details>
<summary>Click to reveal solution</summary>

```python
def solution():
    # Implementation here
    pass
```

**Key insight:** [What the learner should take away]

**Time complexity:** O(?)
**Space complexity:** O(?)

</details>

---

## Discussion

- What alternative approaches could work?
- When would this pattern be useful in production?
- What edge cases did you consider?
```

**Filling in the template:**
- Problem statements should be unambiguous — a new reader should understand without asking
- Test cases should cover happy path, error cases, and edge cases
- Hints should progressively reveal the approach without spoiling the learning
- Solutions should include complexity analysis

---

## Standard Quiz Template

Use this for knowledge-check quizzes (conceptual, not coding).

```markdown
# Quiz: [Topic]

**Questions:** [N]
**Passing Score:** [X/N]
**Time Limit:** [Optional — X minutes]

---

## Questions

### Q1: [Question text]

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

### Q2: [Question text]

A) [Option A]
B) [Option B]
C) [Option C]
D) [Option D]

<details>
<summary>Answer</summary>

**Correct:** [Letter]

**Explanation:** [Why this is correct]

</details>

---

[Repeat for each question]

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

**Filling in the template:**
- Questions should test understanding, not memorization
- Explanations should teach, not just state "A is correct"
- Mix question types: conceptual, code reading, "what does this output?", bug identification
- Include code-based questions — Python is learned by reading code

---

## Quiz Question Types

Use a mix of these:

| Type | Example | Tests |
|------|---------|-------|
| **Conceptual** | "What does the GIL protect?" | Theoretical understanding |
| **Code Reading** | "What does this print?" | Ability to trace execution |
| **Bug Hunt** | "Find the bug in this code" | Debugging skills |
| **True/False** | "EAFP is always preferred over LBYL" | Nuanced understanding |
| **Fill-in** | "The method `__` makes objects callable" | Recall of key terms |
| **Multiple Answer** | "Which are valid ways to create a virtual environment?" | Broad knowledge |

---

## Usage Notes

- **Exercises** should be completable in 10-45 minutes
- **Quizzes** should have 5-15 questions per topic
- **READMEs** should be skimmable — use headers and bullet points
- All templates support Markdown rendering in most platforms (GitHub, VS Code, etc.)
- Customize freely — these are starting points, not rigid rules

---

*Use these templates to keep content consistent and learner-friendly.*
