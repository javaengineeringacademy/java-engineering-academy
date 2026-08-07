# Click — Command Line Interface Creation Kit

> **Building CLIs shouldn't feel like parsing arguments. Click makes it elegant.**

## What

Click is a Python library for creating beautiful command-line interfaces. It uses decorators to define commands, options, and arguments. It handles parsing, type conversion, help generation, and error messaging automatically. It's the foundation behind Flask's CLI and many popular Python tools.

## Why

- **Decorator-based:** Define CLI with `@click.command()` and `@click.option()` — no argparse boilerplate.
- **Automatic help:** `--help` is built-in for every command and option.
- **Type safety:** Built-in type checking and conversion for arguments and options.
- **Composable:** Commands nest naturally with groups and subcommands.
- **Testing:** Click's `CliRunner` makes testing CLIs straightforward.

## When

| Scenario | Click Approach | Why |
|----------|---------------|-----|
| Simple script | `@click.command()` | One decorator, done |
| Multi-command tool | `@click.group()` + subcommands | Organized, discoverable |
| File processing | `@click.argument('file', type=click.File())` | Auto file handling |
| Database migrations | Command groups with options | Organized, chainable |
| DevOps scripts | Click + rich for output | Beautiful, informative output |
| Package CLI tools | Entry points with Click | Standard Python packaging |

## How

### Basic Command

```python
import click

@click.command()
@click.option('--name', '-n', default='World', help='Who to greet')
def hello(name):
    """Simple program that greets NAME."""
    click.echo(f'Hello, {name}!')

if __name__ == '__main__':
    hello()

# Usage:
# python hello.py
# python hello.py --name Alice
# python hello.py -n Alice
# python hello.py --help
```

### Arguments and Options

```python
import click

@click.command()
@click.argument('filename', type=click.Path(exists=True))
@click.option('--output', '-o', type=click.Path(), help='Output file path')
@click.option('--verbose', '-v', is_flag=True, help='Enable verbose output')
@click.option('--count', '-c', type=int, default=1, help='Number of iterations')
@click.option('--format', type=click.Choice(['json', 'csv', 'xml']), default='json')
def process(filename, output, verbose, count, format):
    """Process FILENAME and write results."""
    if verbose:
        click.echo(f'Processing {filename} ({count} times, format={format})')

    for i in range(count):
        click.echo(f'Iteration {i+1}/{count}')

    if output:
        click.echo(f'Output written to {output}')

# Usage:
# python process.py data.txt
# python process.py data.txt -o result.json -v -c 3
# python process.py data.txt --format csv
```

### Command Groups

```python
import click

@click.group()
@click.version_option(version='1.0.0')
def cli():
    """My CLI tool with multiple commands."""
    pass

@cli.command()
@click.argument('name')
def greet(name):
    """Greet someone."""
    click.echo(f'Hello, {name}!')

@cli.command()
@click.option('--all', is_flag=True, help='Show all items')
def list(all):
    """List items."""
    items = ['item1', 'item2', 'item3']
    if all:
        items.extend(['hidden1', 'hidden2'])
    for item in items:
        click.echo(item)

@cli.command()
@click.argument('item')
@click.confirmation_option(prompt='Are you sure?')
def delete(item):
    """Delete an item (with confirmation)."""
    click.echo(f'Deleted {item}')

if __name__ == '__main__':
    cli()

# Usage:
# python tool.py greet Alice
# python tool.py list --all
# python tool.py delete item1  # asks for confirmation
# python tool.py --version
# python tool.py --help
```

### Progress Bars and Colors

```python
import click
import time

@click.command()
@click.option('--count', default=100, help='Number of items')
def process(count):
    """Process items with progress bar."""
    click.echo('Processing items...')
    with click.progressbar(range(count), label='Progress') as bar:
        for i in bar:
            time.sleep(0.01)  # Simulate work

    click.secho('Done!', fg='green', bold=True)

@click.command()
def colors():
    """Demonstrate colored output."""
    click.secho('This is red', fg='red')
    click.secho('This is green', fg='green')
    click.secho('This is bold blue', fg='blue', bold=True)
    click.secho('This is underline', underline=True)

# Usage:
# python process.py --count 50
# python colors.py
```

### Testing with CliRunner

```python
import click
from click.testing import CliRunner

@click.command()
@click.option('--name', default='World')
def greet(name):
    click.echo(f'Hello, {name}!')

def test_greet():
    runner = CliRunner()

    # Test default
    result = runner.invoke(greet)
    assert result.exit_code == 0
    assert 'Hello, World!' in result.output

    # Test with option
    result = runner.invoke(greet, ['--name', 'Alice'])
    assert result.exit_code == 0
    assert 'Hello, Alice!' in result.output

if __name__ == '__main__':
    test_greet()
    print('All tests passed!')
```

### File Handling

```python
import click

@click.command()
@click.argument('input_file', type=click.File('r'))
@click.argument('output_file', type=click.File('w'))
def convert(input_file, output_file):
    """Convert INPUT_FILE to OUTPUT_FILE."""
    content = input_file.read()
    output_file.write(content.upper())
    click.echo(f'Converted {input_file.name} to {output_file.name}')

@click.command()
@click.argument('filename', type=click.Path(exists=True, dir_okay=False))
def info(filename):
    """Show file information."""
    import os
    size = os.path.getsize(filename)
    click.echo(f'File: {filename}')
    click.echo(f'Size: {size:,} bytes')
```

### Environment Variables

```python
import click
import os

@click.command()
@click.option('--api-key', envvar='API_KEY', help='API key for authentication')
@click.option('--verbose', envvar='VERBOSE', is_flag=True)
def connect(api_key, verbose):
    """Connect to external service."""
    if verbose:
        click.echo(f'Connecting with key: {api_key[:8]}...')

    click.echo('Connected!')

# Usage:
# API_KEY=abc123 python connect.py
# python connect.py --api-key abc123
```

## Production Checklist

- [ ] **Add `--help` text** — Click does this automatically, but write good descriptions
- [ ] **Use `click.Path(exists=True)`** — validate file arguments before processing
- [ ] **Set exit codes** — `sys.exit(1)` on errors, Click handles this with exceptions
- [ ] **Use environment variables** — `envvar=` parameter for secrets and config
- [ ] **Add `--version`** — `click.version_option()` for every tool
- [ ] **Test with CliRunner** — verify output and exit codes
- [ ] **Use `click.confirmation_option`** — for destructive operations
- [ ] **Add progress bars** — for long-running operations

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **argparse** | Raw `argparse` usage. Verbose, manual help text. |
| 2 | **Basic Click** | `@click.command()`, options, arguments. Auto-help. |
| 3 | **Groups** | Command groups, subcommands, nested CLIs. |
| 4 | **Production** | CliRunner tests, env vars, progress bars, colored output. |
| 5 | **Advanced** | Custom types, plugins, lazy loading, async commands. |

## Common Myths

### Myth 1: "Click is overkill for simple scripts"
**Reality:** A simple `@click.command()` with `@click.option()` is fewer lines than argparse and gives you free `--help`. For anything beyond a trivial script, Click pays for itself immediately.

### Myth 2: "argparse is built-in, so it's better"
**Reality:** argparse is built-in but verbose. Click provides a cleaner API, automatic help, type safety, and testing tools. The dependency is worth the trade-off for any non-trivial CLI.

### Myth 3: "Click CLIs are hard to test"
**Reality:** Click's `CliRunner` makes testing straightforward. It captures output, exit codes, and even stdin. Testing Click CLIs is easier than testing argparse-based ones.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Command | `@click.command()` | Define a CLI command |
| Option | `@click.option('--name', '-n')` | Named parameter with default |
| Argument | `@click.argument('file')` | Positional parameter |
| Group | `@click.group()` | Parent for subcommands |
| Type | `type=click.Path()` | Type checking/conversion |
| Flag | `is_flag=True` | Boolean option |
| Choice | `type=click.Choice([...])` | Limited set of values |
| Confirm | `@click.confirmation_option()` | Yes/no prompt |
| Version | `@click.version_option()` | `--version` flag |
| Test | `CliRunner().invoke(cmd)` | Test CLI output |

## Related Topics

- [01-fundamentals](../01-fundamentals/) - Python script basics
- [09-exception-handling](../09-exception-handling/) - Error handling in CLIs
- [13-logging](../13-logging/) - Logging in CLI tools
- [16-best-practices](../16-best-practices/) - CLI design patterns

---

> **Remember:** A good CLI is self-documenting. With Click, `--help` is free. Write clear help text, and your users will never need to read the docs.
