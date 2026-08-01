# Topic Template

> Copy this directory structure for every new topic.
> Replace all `{{placeholder}}` values.

```
topic-name/
├── README.md                    # Main lesson content
├── theory/                      # Theoretical explanations
│   └── index.md
├── diagrams/                    # Visual learning aids
│   ├── architecture.md          # Mermaid diagrams
│   ├── memory-layout.md
│   └── sequence.md
├── examples/
│   ├── easy/                    # Syntax-level examples
│   │   ├── 01-basic-syntax.java
│   │   ├── 02-basic-syntax.java
│   │   └── README.md
│   ├── medium/                  # Concept-combining examples
│   │   ├── 01-concept-combination.java
│   │   ├── 02-concept-combination.java
│   │   └── README.md
│   └── hard/                    # Production-level examples
│       ├── 01-production-problem.java
│       ├── 02-production-problem.java
│       └── README.md
├── exercises/
│   ├── easy/                    # Syntax practice
│   │   ├── 01-exercise.md
│   │   ├── 02-exercise.md
│   │   └── README.md
│   ├── medium/                  # Concept application
│   │   ├── 01-exercise.md
│   │   ├── 02-exercise.md
│   │   └── README.md
│   └── hard/                    # Problem solving
│       ├── 01-exercise.md
│       ├── 02-exercise.md
│       └── README.md
├── assignments/                 # Graded assignments
│   ├── 01-assignment.md
│   └── 02-assignment.md
├── interview/                   # Interview preparation
│   ├── questions.md
│   └── answers.md
├── quiz/                        # Knowledge check
│   └── quiz.md
├── pitfalls/                    # Common mistakes
│   └── index.md
├── best-practices/              # Industry standards
│   └── index.md
├── real-world/                  # Enterprise usage
│   ├── spring-framework.md
│   ├── hibernate.md
│   └── jdk-source.md
├── references/                  # External resources
│   └── index.md
└── solutions/                   # Exercise solutions
    ├── easy/
    ├── medium/
    └── hard/
```

---

## README.md Template

```markdown
# {{Topic Name}}

{{One-paragraph introduction explaining what this topic is about.}}

## Learning Objectives

By the end of this topic, you will be able to:

- Understand {{concept}}
- Apply {{concept}} in Java code
- Identify when to use {{concept}}
- Avoid common {{concept}} mistakes

## Prerequisites

- [Previous Topic](../topic-name/)
- Basic Java syntax
- Understanding of {{prerequisite}}

## Why This Concept Exists

{{Explanation of the problem this concept solves.}}

### Problem Statement

{{Detailed problem description with real-world analogy.}}

## Internal Working

### JVM Perspective

{{How the JVM handles this concept.}}

### Memory Representation

{{How memory is organized for this concept.}}

## Syntax

```java
// Basic syntax example
public class Example {
    // Syntax demonstration
}
```

## Easy Examples

### Example 1: {{Title}}

**Problem Statement:**
{{What does this example solve?}}

**Implementation:**

```java
package academy.javaengineering.{{module}}.{{topic}}.examples.easy;

public class {{ClassName}} {
    public static void main(String[] args) {
        // Implementation
    }
}
```

**Expected Output:**
```
{{Output}}
```

**Execution Flow:**
1. {{Step 1}}
2. {{Step 2}}

**Complexity:**
- Time: O(?)
- Space: O(?)

**Best Practices:**
- {{Practice 1}}
- {{Practice 2}}

## Medium Examples

### Example 1: {{Title}}

**Problem Statement:**
{{What does this example solve?}}

**Requirements:**
- {{Requirement 1}}
- {{Requirement 2}}

**Implementation:**

```java
package academy.javaengineering.{{module}}.{{topic}}.examples.medium;

// Full implementation
```

**Code Walkthrough:**
1. {{Walkthrough step 1}}
2. {{Walkthrough step 2}}

**Expected Output:**
```
{{Output}}
```

**Complexity:**
- Time: O(?)
- Space: O(?)

**Alternative Solution:**
```java
// Different approach
```

## Hard Examples

### Example 1: {{Title}}

**Problem Statement:**
{{Production-level problem}}

**Requirements:**
- {{Requirement 1}}
- {{Requirement 2}}
- {{Requirement 3}}

**Architecture:**
```
{{Package structure}}
```

**Implementation:**

```java
package academy.javaengineering.{{module}}.{{topic}}.examples.hard;

// Production-level implementation
```

**Unit Tests:**

```java
package academy.javaengineering.{{module}}.{{topic}}.examples.hard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class {{ClassName}}Test {
    @Test
    void should{{TestName}}() {
        // Test implementation
    }
}
```

**Execution Flow:**
{{Sequence diagram or step-by-step}}

**Complexity:**
- Time: O(?)
- Space: O(?)

**Best Practices:**
- {{Practice 1}}
- {{Practice 2}}
- {{Practice 3}}

## Exercises

### Easy

{{List of easy exercises with brief descriptions}}

### Medium

{{List of medium exercises with brief descriptions}}

### Hard

{{List of hard exercises with brief descriptions}}

## Assignments

### Assignment 1: {{Title}}

**Objective:** {{What students will build}}

**Requirements:**
- {{Requirement 1}}
- {{Requirement 2}}

**Evaluation Criteria:**
- {{Criteria 1}}
- {{Criteria 2}}

## Quiz

{{5-10 multiple choice questions}}

## Interview Questions

### Easy

1. **Q:** {{Question}}
   **A:** {{Answer}}

### Medium

1. **Q:** {{Question}}
   **A:** {{Answer}}

### Hard

1. **Q:** {{Question}}
   **A:** {{Answer}}

## Common Pitfalls

### Pitfall 1: {{Title}}

**Mistake:**
```java
// Wrong code
```

**Correct:**
```java
// Correct code
```

**Why:** {{Explanation}}

## Best Practices

1. {{Practice 1}}
2. {{Practice 2}}
3. {{Practice 3}}

## Real World Usage

### Spring Framework

{{How Spring uses this concept}}

### Hibernate

{{How Hibernate uses this concept}}

### JDK Source Code

{{Where this appears in JDK}}

### Enterprise Applications

{{Production usage examples}}

## References

- [Official Java Documentation](https://docs.oracle.com/en/java/)
- Effective Java by Joshua Bloch
- [Related Topic](../related-topic/)

## Summary

{{Key takeaways in bullet points}}

---

**Next Topic:** [Next Topic Name](../next-topic/)
**Previous Topic:** [Previous Topic Name](../previous-topic/)
```
```

---

## Exercise Template

```markdown
# Exercise: {{Title}}

**Difficulty:** {{Easy|Medium|Hard}}
**Estimated Time:** {{minutes}} minutes
**Topic:** [{{Topic Name}}](../)

## Objective

{{What students will practice}}

## Requirements

1. {{Requirement 1}}
2. {{Requirement 2}}
3. {{Requirement 3}}

## Starter Code

```java
package academy.javaengineering.{{module}}.{{topic}}.exercises.{{difficulty}};

public class {{ClassName}} {
    // TODO: Implement
}
```

## Expected Behavior

```
Input: {{input}}
Output: {{output}}
```

## Evaluation Criteria

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] Follows Google Java Style
- [ ] Handles edge cases
- [ ] Includes Javadoc

## Hints

1. {{Hint 1}}
2. {{Hint 2}}

## Solution

See [solutions/{{difficulty}}/{{exercise-file}}](../../solutions/{{difficulty}}/{{exercise-file}})
```

---

## Interview Template

```markdown
# Interview Questions: {{Topic}}

## Easy (0-2 years experience)

### Q1: {{Question}}

**Answer:**
{{Comprehensive answer}}

**Follow-up:** {{Follow-up question}}

---

## Medium (2-5 years experience)

### Q1: {{Question}}

**Answer:**
{{Comprehensive answer with examples}}

**Code Example:**
```java
// Example code
```

**Follow-up:** {{Follow-up question}}

---

## Hard (5+ years experience)

### Q1: {{Question}}

**Answer:**
{{Comprehensive answer with architecture considerations}}

**Real-world Context:**
{{How this applies in enterprise}}

**Follow-up:** {{Follow-up question}}
```

---

## Quiz Template

```markdown
# Quiz: {{Topic}}

**Time Limit:** {{minutes}} minutes
**Passing Score:** {{percent}}%
**Questions:** {{count}}

---

## Question 1

{{Question text}}

- A) {{Option A}}
- B) {{Option B}}
- C) {{Option C}}
- D) {{Option D}}

**Correct Answer:** {{Letter}}

**Explanation:** {{Why this answer is correct}}

---

## Question 2

{{Question text}}

- A) {{Option A}}
- B) {{Option B}}
- C) {{Option C}}
- D) {{Option D}}

**Correct Answer:** {{Letter}}

**Explanation:** {{Why this answer is correct}}

---

## Score Interpretation

| Score | Level | Recommendation |
|-------|-------|----------------|
| 90-100% | Excellent | Ready for advanced topics |
| 70-89% | Good | Review weak areas |
| 50-69% | Needs Work | Re-study the topic |
| Below 50% | Review | Complete topic again |
```

---

## Assignment Template

```markdown
# Assignment: {{Title}}

**Module:** {{Module Name}}
**Topic:** {{Topic Name}}
**Difficulty:** {{Easy|Medium|Hard}}
**Estimated Time:** {{hours}} hours
**Due Date:** {{date}}

## Objective

{{What students will build and learn}}

## Background

{{Context and motivation for the assignment}}

## Requirements

### Functional Requirements

1. {{Requirement 1}}
2. {{Requirement 2}}
3. {{Requirement 3}}

### Technical Requirements

- Use Java 21
- Follow Google Java Style
- Include JUnit 5 tests
- Achieve {{percent}}% code coverage

### Design Requirements

- Follow SOLID principles
- Use appropriate design patterns
- Include proper documentation

## Deliverables

- [ ] Source code in correct package structure
- [ ] Unit tests with {{percent}}% coverage
- [ ] README.md with setup instructions
- [ ] Javadoc for public APIs

## Evaluation Criteria

| Criteria | Weight | Description |
|----------|--------|-------------|
| Functionality | 40% | All requirements implemented |
| Code Quality | 25% | Clean, readable, maintainable |
| Testing | 20% | Comprehensive test coverage |
| Documentation | 15% | Clear README and Javadoc |

## Starter Template

```
{{Package structure}}
```

## Resources

- [Related Documentation](../)
- [Example Code](../examples/)

## Submission

1. Commit all changes
2. Push to your fork
3. Create a Pull Request
4. Fill out the submission form

## Academic Integrity

This assignment must be your own work. You may reference documentation and examples, but do not copy code from other students or online sources.
```

---

## Usage Instructions

1. **Copy the directory structure** from the Topic Template
2. **Replace all `{{placeholders}}`** with actual content
3. **Follow the README.md template** for consistent lesson format
4. **Use exercise template** for practice materials
5. **Use interview template** for interview prep
6. **Use quiz template** for knowledge checks
7. **Use assignment template** for graded work

---

*Templates maintained by Java Engineering Academy. Update as needed.*
