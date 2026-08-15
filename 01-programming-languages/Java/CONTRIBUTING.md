# Contributing to Java Engineering Academy

## Folder Structure Convention

Every topic folder must follow this exact structure:

```
XX-topic-name/
├── 00-internals/
│   ├── README.md          # Overview of internals
│   └── TopicInternals.java
├── 01-memory/
│   ├── README.md          # Memory-related concepts
│   └── TopicMemory.java
├── examples/
│   └── Examples.java      # Working code examples
├── practices/
│   └── Practices.java     # Exercise stubs with TODO markers
├── solutions/
│   └── Solutions.java     # Complete solutions
├── README.md              # Topic overview
├── quiz.md                # 10+ questions (MCQ, True/False, Code Output)
├── references.md          # Topic-specific resources
└── decision.md            # When to use, trade-offs, alternatives
```

## Root Module Structure

Each module folder must contain:

```
XX-module-name/
├── 01-topic-one/
├── 02-topic-two/
├── ...
├── XX-examples/           # Aggregated examples (can be empty)
├── XX-practices/          # Aggregated practices (can be empty)
├── XX-solutions/          # Aggregated solutions (can be empty)
├── practices/             # Root-level exercises
├── README.md              # Module overview
└── quiz.md                # Module-level quiz
```

## Naming Standards

- **Topic folders:** `XX-topic-name/` (e.g., `01-string/`, `02-classes/`)
- **Subfolders:** Always `00-internals/`, `01-memory/`, `examples/`, `practices/`, `solutions/`
- **Root utility folders:** Always `practices/` (NOT `exercises/`)
- **Java files:** PascalCase matching topic (e.g., `StringInternals.java`)

## File Content Requirements

### examples/Examples.java
- Real, working Java code
- Topic-specific examples with comments explaining WHY
- Should compile and run independently

### practices/Practices.java
- TODO markers for students to complete
- Class stubs and method signatures
- Clear instructions in comments

### solutions/Solutions.java
- Complete working implementations
- Matches practices exercises
- Comments explaining key decisions

### quiz.md
- Minimum 10 questions per topic
- Mix of: Multiple Choice, True/False, Code Output
- Production scenarios and debugging questions
- Answers with explanations at the end

### references.md
- Topic-specific books, articles, JEPs
- Official Oracle documentation links
- Practice platforms (LeetCode, HackerRank)

### decision.md
- When to use the concept
- When NOT to use
- Trade-offs table
- Expert recommendation

## Quality Checklist

- [ ] All 6 subfolders present (00-internals, 01-memory, examples, practices, solutions)
- [ ] All 4 files present (README.md, quiz.md, references.md, decision.md)
- [ ] README.md in 00-internals/ and 01-memory/
- [ ] Quiz has 10+ questions
- [ ] Examples compile and run
- [ ] No duplicate folder names
- [ ] Proper numbering sequence
