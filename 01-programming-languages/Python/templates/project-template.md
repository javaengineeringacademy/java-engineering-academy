# Project Template

Use this template for project READMEs (e.g., `projects/[project-name]/README.md`).

---

## Template

```markdown
# [Project Name]

[One-sentence summary of what the project does and what concepts it demonstrates.]

## Overview

[2-3 paragraphs explaining the project's purpose, the problem it solves, and what the learner will build. Include the real-world context if applicable.]

## Features

- [Feature 1 — what it does]
- [Feature 2 — what it does]
- [Feature 3 — what it does]
- [Feature 4 — what it does]

## Architecture

```
project-name/
├── main.py              # Entry point
├── models.py            # Data models
├── services.py          # Business logic
├── storage.py           # Persistence layer
├── utils.py             # Helper functions
├── config.py            # Configuration
└── test_project.py      # Unit tests
```

| File | Purpose |
|------|---------|
| `main.py` | [What it does] |
| `models.py` | [What it does] |
| `services.py` | [What it does] |
| `storage.py` | [What it does] |
| `test_project.py` | [What it tests] |

## Learning Objectives

- [Objective 1 — skill or concept the learner will practice]
- [Objective 2 — skill or concept the learner will practice]
- [Objective 3 — skill or concept the learner will practice]
- [Objective 4 — skill or concept the learner will practice]

## Prerequisites

- [Prerequisite 1 — what they must know first]
- [Prerequisite 2 — what they must know first]

## How to Run

```bash
# Navigate to project directory
cd projects/[project-name]

# Install dependencies (if any)
pip install -r requirements.txt

# Run the application
python main.py

# Run tests
python -m pytest test_[project].py -v
```

## Expected Output

```
[Paste sample output from running the application]
```

## Extension Ideas

- [Idea 1 — how to extend the project]
- [Idea 2 — how to extend the project]
- [Idea 3 — how to extend the project]
```

---

## Formatting Guidelines

- Project name must be descriptive: "Mini Banking System" not "OOP Project"
- Overview must explain what they'll BUILD, not just what concepts they'll learn
- Features must be specific user-facing capabilities, not implementation details
- Architecture must show actual file structure with purpose of each file
- Learning Objectives must use action verbs: "Implement", "Design", "Apply"
- How to Run must be copy-pasteable and produce visible output
- Expected Output must match what they'll actually see
- Extension Ideas give ambitious learners next steps

---

## Example: CLI Task Manager

```markdown
# CLI Task Manager

A command-line task management application demonstrating OOP, file I/O, and exception handling in Python.

## Overview

This project builds a fully functional task manager that runs in the terminal. Users can add, list, complete, and delete tasks. Tasks persist to a JSON file between sessions. The project demonstrates how to structure a CLI application using classes, handle user input gracefully, and manage file I/O safely.

The task manager includes input validation, error handling for missing files, and clean separation of concerns between the data model, storage layer, and CLI interface.

## Features

- Add tasks with title, priority, and due date
- List tasks filtered by status (pending, completed)
- Mark tasks as complete
- Delete tasks with confirmation
- Persistent storage using JSON files
- Input validation with helpful error messages

## Architecture

```
task-manager/
├── main.py              # CLI interface and user interaction
├── models.py            # Task and TaskList dataclasses
├── storage.py           # JSON file read/write operations
├── utils.py             # Date parsing and input helpers
└── test_tasks.py        # Unit tests for models and storage
```

| File | Purpose |
|------|---------|
| `main.py` | Entry point, menu display, user input handling |
| `models.py` | `Task` dataclass with fields and methods |
| `storage.py` | `save_tasks()` and `load_tasks()` functions |
| `utils.py` | `parse_date()`, `validate_input()` helpers |
| `test_tasks.py` | Unit tests for Task creation, storage round-trip |

## Learning Objectives

- Design a CLI application with clean separation of concerns
- Implement dataclasses for structured data models
- Perform JSON serialization/deserialization with error handling
- Write unit tests for business logic and I/O operations
- Handle user input validation and edge cases gracefully

## Prerequisites

- Python 3.10+ (for dataclass features)
- Understanding of dictionaries, lists, and file I/O
- Basic knowledge of functions and modules

## How to Run

```bash
cd projects/task-manager

# Run the application
python main.py

# Run tests
python -m pytest test_tasks.py -v
```

## Expected Output

```
=== Task Manager ===
1. Add task
2. List tasks
3. Complete task
4. Delete task
5. Quit

Choose an option: 1
Title: Buy groceries
Priority (low/medium/high): high
Due date (YYYY-MM-DD): 2026-08-15
Task added successfully!

Choose an option: 2
Pending tasks:
  [1] Buy groceries (high) — Due: 2026-08-15
```

## Extension Ideas

- Add categories/tags for tasks
- Implement recurring tasks
- Add a priority queue for task ordering
- Export tasks to CSV format
- Add a search function across all tasks
```

---

## Checklist

Before publishing a project:

- [ ] Title is descriptive and specific
- [ ] Overview explains what they'll build
- [ ] Features are user-facing capabilities
- [ ] Architecture shows actual file structure
- [ ] Learning Objectives use action verbs
- [ ] Prerequisites are listed
- [ ] How to Run is copy-pasteable
- [ ] Expected Output matches actual output
- [ ] Extension Ideas give next steps
