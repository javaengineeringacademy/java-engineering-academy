# Validation Scripts

Automated quality checks for the Java Engineering Academy repository.

## Quick Start

```bash
# Run all checks
bash scripts/run-all-checks.sh

# Run individual checks
bash scripts/validate-java.sh
bash scripts/validate-python.sh
bash scripts/validate-markdown.sh
bash scripts/validate-structure.sh
```

## Scripts

### validate-java.sh

Checks Java code quality.

| Check | Description |
|-------|-------------|
| `catch(Exception)` | Flags broad exception catching |
| `printStackTrace()` | Flags stack trace printing (use logging) |
| Raw types | Flags `List` without generics (use `List<String>`) |
| `System.out` | Flags print statements in non-demo files |

### validate-python.sh

Checks Python code quality.

| Check | Description |
|-------|-------------|
| Bare `except:` | Flags `except:` without specific exception |
| `except Exception` | Flags broad exception catching |
| Mutable defaults | Flags `def f(x=[])` (use `None`) |
| `global` keyword | Flags global variable usage |
| `print()` | Flags print in non-educational files |

### validate-markdown.sh

Checks Markdown documentation quality.

| Check | Description |
|-------|-------------|
| Code fences | Flags odd number of ` ``` ` (unclosed blocks) |
| Placeholders | Flags TODO, FIXME, Coming soon, TBD |
| File length | Flags files over 400 lines |
| Broken links | Flags local links pointing to non-existent files |
| README sections | Flags missing required sections |
| Quiz completeness | Flags quizzes with fewer than 10 questions |
| Naming conventions | Flags uppercase directory names |

### validate-structure.sh

Checks repository structure.

| Check | Description |
|-------|-------------|
| Missing README | Flags module directories without README.md |
| Missing quiz | Flags module directories without quiz.md |
| Empty files | Flags empty files |
| Naming inconsistency | Flags uppercase directory names |
| Duplicate files | Flags files with identical content |
| Learning paths | Flags broken references in learning paths |
| Missing exercises | Flags modules without exercises/ directory |

### run-all-checks.sh

Runs all four validators and reports a summary.

```bash
$ bash scripts/run-all-checks.sh

========================================
   Repository Quality Check Summary
========================================

--- Markdown Validation ---
All markdown files passed!

--- Java Validation ---
All Java files passed!

--- Python Validation ---
All Python files passed!

--- Structure Validation ---
Structure is clean!

========================================
All checks passed! Repository is clean.
```

## Exit Codes

| Code | Meaning |
|------|---------|
| 0 | All checks passed |
| 1 | One or more checks failed |

## Adding New Checks

Each validator follows the same pattern:

1. Find files to check
2. Run grep/sed to detect issues
3. Print issues with file:line format
4. Exit 0 (clean) or 1 (issues found)

To add a new check, add a grep pattern to the appropriate validator.

## Color Codes

| Color | Meaning |
|-------|---------|
| Red | Issue found |
| Green | Check passed |
| Yellow | Header/progress |
