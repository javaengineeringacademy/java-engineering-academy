# Typer

## Why Typer Exists

Every Python developer building CLIs faces a choice: Click's decorator-based API is powerful but verbose, while argparse is built-in but cumbersome. Typer was created to combine the best of both — using Python type hints to define CLI arguments and options. It's built on Click but eliminates boilerplate, making CLIs faster to write and easier to maintain.

## What You'll Learn

By the end of this section, you'll be able to:

- Build CLIs using Python type hints instead of decorators
- Create command groups with subcommands and automatic help
- Test CLIs using Typer's testing utilities

## When to Use Typer

| Use Case | Why Typer | Alternative |
|----------|---------|-------------|
| Simple script | Type hints as CLI definition | argparse |
| Multi-command tool | Automatic subcommand groups | Click |
| File processing | Type-safe file arguments | Click |
| DevOps scripts | Clean, readable CLI code | Click |
| Package CLIs | Modern Python packaging | Click |
| Data pipelines | Type-validated arguments | argparse |

## How Typer Works Internally

Typer uses Python's `inspect` module to analyze function signatures. When you define `def main(name: str, age: int = 30)`, Typer inspects the parameters and their types to create CLI arguments and options. String parameters become arguments, and parameters with defaults become options. Type hints drive validation and help text generation.

Under the hood, Typer converts your function into a Click command. It wraps Click's `command()` and `option()` decorators with a simpler API. The result is the same powerful CLI framework with less boilerplate. Typer also adds features like automatic shell completion and progress bars.

```python
import typer

app = typer.Typer()

@app.command()
def greet(name: str, age: int = 30, formal: bool = False):
    """Greet someone."""
    if formal:
        typer.echo(f"Good day, {name}. You are {age} years old.")
    else:
        typer.echo(f"Hey {name}! You're {age}")

if __name__ == "__main__":
    app()
```

## Production Checklist

### ✅ Before using Typer in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Typer is just Click with less code
**Reality:** Typer adds significant features on top of Click: automatic shell completion, progress bars, color output, and testing utilities. It's a full CLI framework.

### ❌ Myth 2: Type hints make CLIs slower
**Reality:** Type hints are inspected at import time, not runtime. The overhead is negligible.

### ❌ Myth 3: Typer doesn't support complex CLIs
**Reality:** Typer supports command groups, subcommands, callbacks, and all Click features. It scales to complex CLI tools.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Modern CLI framework with type hints |
| Complexity | O(1) per command invocation |
| Thread Safe | Yes |
| Best Alternative | Click for decorator-based APIs |
| When to Use | New CLI tools, Python packages |
| When to Avoid | Legacy projects using Click |

## Related Topics

- [08-click](../08-click/) - Click foundation
- [15-pydantic](../15-pydantic/) - Data validation
- [04-flask](../04-flask/) - Flask CLI integration
