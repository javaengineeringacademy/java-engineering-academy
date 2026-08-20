# BDD Testing - Internals

## Cucumber Execution Flow

```
Feature Files → Parser → Scenarios → Step Runner → Reports
                                  ↓
                    Step Definitions → Execution
                                  ↓
                    Hooks (Before/After)
```

## Step Matching

Cucumber matches steps using:
1. Regular expressions
2. String patterns with {int}, {string}
3. Doc strings for multi-line text
4. Data tables for tabular data

## World Object

- Shared state between steps
- Created per scenario
- Cleaned up after scenario
- Can be dependency-injected

## Report Generation

- HTML reports with scenario details
- JSON reports for CI integration
- Pretty output for console
- Custom formatters available
