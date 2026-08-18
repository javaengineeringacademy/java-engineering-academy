# Thread Creation Decision Guide

## Runnable vs Callable

| Aspect | Runnable | Callable |
|--------|----------|----------|
| Return value | void | V (generic) |
| Exception | Unchecked only | Checked exceptions |
| Submission | execute() | submit() returns Future |
| Use case | Fire-and-forget | Tasks needing results |

## Thread Creation Approaches

| Approach | When to Use |
|----------|-------------|
| Lambda Runnable | Simple one-off tasks (preferred) |
| Method reference | Reusable task logic |
| Runnable class | Complex tasks needing multiple methods |
| Callable | When result is needed |
| Thread subclass | Only when extending Thread is necessary |
