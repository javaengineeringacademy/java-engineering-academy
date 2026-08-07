# Exercise Template

Use this template for exercise READMEs (e.g., `exercises/README.md` in any module).

---

## Template

```markdown
# [Module Name] Exercises

## Learning Objectives

- [Objective 1 — what the learner will be able to do]
- [Objective 2 — what the learner will be able to do]
- [Objective 3 — what the learner will be able to do]

## Exercises

### [Category 1].py

1. **[Exercise Name]** ([Difficulty]) — [One-line description]
2. **[Exercise Name]** ([Difficulty]) — [One-line description]
3. **[Exercise Name]** ([Difficulty]) — [One-line description]

### [Category 2].py

1. **[Exercise Name]** ([Difficulty]) — [One-line description]
2. **[Exercise Name]** ([Difficulty]) — [One-line description]
3. **[Exercise Name]** ([Difficulty]) — [One-line description]

## Difficulty Levels

| Level | Time | Description |
|-------|------|-------------|
| Easy | 10-15 min | Straightforward application of concepts |
| Medium | 20-30 min | Requires combining multiple concepts |
| Hard | 30-45 min | Complex problem, multiple approaches possible |

## Tips

- Start with the easier exercises and work your way up
- Use Python's built-in functions when appropriate
- Write clean, readable code with proper variable names
- Test your solutions with the provided test cases
- [Tip specific to this module]

## Solutions

Solutions are in the `solutions/` directory. Try to solve each exercise before checking the answer.

```

---

## Formatting Guidelines

- Learning Objectives must use action verbs: "Implement", "Explain", "Debug" — not "Understand"
- Exercises must be grouped by file, matching the actual `.py` file structure
- Every exercise must have a difficulty level (Easy/Medium/Hard)
- Difficulty must correlate with time: Easy=10-15m, Medium=20-30m, Hard=30-45m
- One-line descriptions must be unambiguous — the learner knows what to build
- Tips must include at least one module-specific hint

---

## Example: OOP Exercises

```markdown
# OOP Exercises

## Learning Objectives

- Implement classes with proper encapsulation
- Design inheritance hierarchies using composition over inheritance
- Apply polymorphism through duck typing and abstract base classes
- Use magic methods to make objects behave like built-in types

## Exercises

### basics.py

1. **Bank Account** (Easy) — Create a BankAccount class with deposit, withdraw, and balance methods
2. **Rectangle** (Easy) — Implement a Rectangle class with area, perimeter, and `__repr__`
3. **Student Record** (Easy) — Build a Student class with name, grades, and GPA calculation

### intermediate.py

1. **Shape Hierarchy** (Medium) — Create Circle, Rectangle, Triangle inheriting from a Shape ABC
2. **Stack Implementation** (Medium) — Build a Stack class using a list with push, pop, peek
3. **Custom Iterator** (Medium) — Implement a Fibonacci iterator class

### advanced.py

1. **ORM Lite** (Hard) — Build a minimal ORM with Model base class, field descriptors, and save/load
2. **Singleton Metaclass** (Hard) — Implement a Singleton using metaclasses, not module-level instances
3. **Protocol Mixin** (Hard) — Create composable behaviors using Protocol and multiple inheritance

## Difficulty Levels

| Level | Time | Description |
|-------|------|-------------|
| Easy | 10-15 min | Straightforward application of concepts |
| Medium | 20-30 min | Requires combining multiple concepts |
| Hard | 30-45 min | Complex problem, multiple approaches possible |

## Tips

- Start with the easier exercises and work your way up
- Use `__repr__` and `__str__` for debugging — print your objects
- Test edge cases: empty inputs, negative numbers, None values
- Prefer composition over inheritance when designing relationships
- Solutions are in `solutions/` — try each exercise first
```

---

## Individual Exercise File Template

Use this format within each exercise `.py` file:

```python
"""
Exercise: [Name]
Difficulty: [Easy | Medium | Hard]
Time: [X minutes]
Topic: [Concept being tested]
"""

# Problem:
# [Clear description of what to implement]
#
# Requirements:
# - [ ] [Requirement 1]
# - [ ] [Requirement 2]
# - [ ] [Requirement 3]
#
# Constraints:
# - [Constraint 1, e.g., "Do not use eval()"]
# - [Constraint 2, e.g., "Must handle empty input"]
#
# Example:
# >>> solution([1, 2, 3])
# [expected output]
#
# >>> solution([])
# [expected output]


def solution():
    pass
    # Your code here


# Test cases
if __name__ == "__main__":
    assert solution([1, 2, 3]) == [expected1]
    assert solution([]) == [expected2]
    print("All tests passed!")
```

---

## Checklist

Before publishing exercises:

- [ ] Learning objectives use action verbs
- [ ] Each exercise has a difficulty level
- [ ] Difficulty matches time estimate
- [ ] Descriptions are unambiguous
- [ ] All `.py` files listed in README exist
- [ ] Test cases cover happy path and edge cases
- [ ] Solutions directory exists with working solutions
