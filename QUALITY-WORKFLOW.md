# Content Quality Workflow

Every content task MUST follow this process. No exceptions.

---

## Step 1: RAG — Understand Context

Before writing anything, gather:

```
□ What module? (e.g., 04-collections)
□ What topic? (e.g., 02-list/01-arraylist)
□ What exists already? (read existing files)
□ What's the target audience? (Student → CTO)
□ What language? (Java, C, C++, Python)
```

---

## Step 2: Load Checklist

Apply ALL 24 quality factors to EVERY file:

### Required Sections (Every README.md must have ALL of these):

```
□ WHY-first narrative (Why It Matters / Why This Concept Exists)
□ What It Is (clear definition)
□ Internal Working (how it works under the hood)
□ Syntax / Code Examples (multiple, real-world)
□ Production Incidents (2-3 real failure scenarios)
□ Engineering Decision Framework (When to use / When NOT to use / Alternatives)
□ Performance (complexity, benchmarks, optimization)
□ Debugging Tips (problem/tool/how table)
□ Code Review Checklist (5-7 items)
□ Architecture Considerations (how it fits in larger systems)
□ Security Considerations (risk/impact/mitigation table)
□ Evolution & Modernization (version history, migration paths)
□ Version Validation (feature/version/status table)
□ Best Practices (do's and don'ts)
□ Common Mistakes (with code examples)
□ Common Myths (myth vs truth)
□ One-Minute Revision (quick reference table)
□ Related Topics (cross-links to other modules)
□ Interview Questions (5+ with answers)
□ References (further reading)
```

### Required Supporting Files:

```
□ Demo.java — runnable example
□ Test.java — unit test
□ exercises/ — 5+ TODO problems per topic
□ solutions/ — complete implementations
□ quiz.md — 10 questions with answers
□ roadmap.md — visual learning path (for mini-courses)
□ interview/ — interview Q&A
□ projects/ — real-world mini projects (for major modules)
□ references/ — version history, cheat sheets
```

---

## Step 3: Write Content

```
□ Write README.md with ALL 20 sections above
□ Write Demo.java with runnable code
□ Write Test.java with assertions
□ Write exercises/ with 5+ TODO problems
□ Write solutions/ with complete implementations
□ Write quiz.md with 10 questions
□ Ensure code compiles (Java: javac, C: gcc, Python: python3 -m py_compile)
□ Ensure no AI-sounding words (comprehensive, robust, leverage, facilitate, holistic)
□ Ensure WHY-first narrative (NOT "Java provides..." but "When you're building X, you need Y")
□ Ensure human writing style (friendly senior engineer, NOT textbook)
```

---

## Step 4: Validate Content

For EVERY file created, verify:

```
□ Section count: Does README.md have ALL 20 required sections?
□ Line count: Is README.md 200-500 lines minimum?
□ Code examples: Are there 3+ runnable examples?
□ Production incidents: Are there 2-3 real scenarios?
□ Exercises: Are there 5+ per topic?
□ Solutions: Do all exercises have solutions?
□ Quiz: Are there 10 questions with answers?
□ Cross-links: Does it link to related modules?
□ No placeholders: Are there any "TODO", "TBD", "[Coming soon]"?
□ No broken links: Do all relative paths work?
```

---

## Step 5: Validate Examples

For EVERY Java/C/C++/Python file:

```
□ File exists in correct location (inside topic subfolder, not parent)
□ Filename matches convention (TopicDemo.java, TopicTest.java)
□ Code compiles without errors
□ Code runs without exceptions
□ Tests pass (if test file exists)
□ No printStackTrace() — use proper error handling
□ No System.out.println() in production code (use logging)
□ No catch(Exception e) — use specific exceptions
□ Comments explain WHY, not WHAT
```

---

## Step 6: Final Check

Before marking task as complete:

```
□ Count all files created/modified
□ Verify folder structure matches target
□ Run: find . -name "*.java" | wc -l (should match expected)
□ Run: find . -name "*.md" | wc -l (should match expected)
□ Spot-check 3 random files for quality
□ Confirm no AI-sounding words remain
□ Confirm all cross-links work
□ Git add + commit with descriptive message
```

---

## Quick Reference: File Structure Per Topic

```
TopicFolder/
├── README.md          (200-500 lines, ALL 20 sections)
├── TopicDemo.java     (runnable example)
├── TopicTest.java     (unit test)
├── exercises/
│   └── exercises.java (5 TODO problems)
├── solutions/
│   └── solutions.java (complete implementations)
└── quiz.md            (10 questions with answers)
```

---

## Quick Reference: File Structure Per Module (Mini-Course)

```
Module/
├── README.md          (overview + learning path)
├── roadmap.md         (visual progression)
├── 01-topic/
│   ├── README.md
│   ├── TopicDemo.java
│   └── TopicTest.java
├── 02-topic/
│   └── ...
├── examples/
├── exercises/
├── solutions/
├── quizzes/
├── interview/
├── projects/
└── references/
```

---

## Common Mistakes to Avoid

| Mistake | Fix |
|---------|-----|
| Demo.java in parent folder | Move INTO topic subfolder |
| Exercises at module level | Move INTO topic subfolder |
| Missing Production Incidents | Add 2-3 real scenarios |
| Missing Engineering Decision Framework | Add When/When NOT/Alternatives table |
| AI-sounding words | Replace: comprehensive→detailed, robust→reliable, leverage→use |
| "Java provides..." opening | Rewrite: "When you're building X, you need Y because Z" |
| Placeholder content | Remove all TODO/TBD/Coming soon |
| Broken cross-links | Verify all relative paths |
| No solutions for exercises | Add complete implementations |
| Quiz without answers | Add detailed answers to every question |

---

## Verification Commands

```bash
# Count files
find . -name "*.md" | wc -l
find . -name "*.java" | wc -l
find . -path "*/exercises/*.java" | wc -l
find . -path "*/solutions/*.java" | wc -l
find . -name "quiz.md" | wc -l

# Check for missing sections
grep -rL "Production Incidents" */README.md
grep -rL "Engineering Decision Framework" */README.md
grep -rL "Debugging Tips" */README.md
grep -rL "Interview Questions" */README.md

# Check for AI-sounding words
grep -ril "comprehensive\|robust\|holistic\|leverage\|facilitate" */README.md

# Check for placeholders
grep -ril "TODO\|TBD\|Coming soon\|FIXME" */README.md */*.java
```
