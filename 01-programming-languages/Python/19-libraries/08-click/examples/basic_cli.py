"""
Click CLI Basics
Demonstrates commands, options, arguments, groups, and testing
"""

import click
import os
import json

# ============================================
# Basic Command
# ============================================

@click.command()
@click.option('--name', '-n', default='World', help='Who to greet')
@click.option('--shout', is_flag=True, help='Uppercase the greeting')
def hello(name, shout):
    """Simple program that greets NAME."""
    greeting = f'Hello, {name}!'
    if shout:
        greeting = greeting.upper()
    click.echo(greeting)

# ============================================
# Command with Multiple Options
# ============================================

@click.command()
@click.argument('filename', type=click.Path(exists=True))
@click.option('--output', '-o', type=click.Path(), help='Output file path')
@click.option('--verbose', '-v', is_flag=True, help='Enable verbose output')
@click.option('--format', type=click.Choice(['json', 'csv', 'text']), default='text')
def process(filename, output, verbose, format):
    """Process FILENAME and write results."""
    if verbose:
        click.echo(f'Reading: {filename}')
        click.echo(f'Format: {format}')

    # Simulate processing
    content = f'Processed content from {filename}'

    if output:
        with open(output, 'w') as f:
            f.write(content)
        click.echo(f'Output written to {output}')
    else:
        click.echo(content)

# ============================================
# Command Group (Multi-command CLI)
# ============================================

@click.group()
@click.version_option(version='1.0.0')
def cli():
    """File management CLI tool."""
    pass

@cli.command()
@click.argument('directory', default='.')
def list_files(directory):
    """List files in DIRECTORY."""
    files = os.listdir(directory)
    for f in sorted(files):
        if os.path.isfile(os.path.join(directory, f)):
            click.echo(f'  {f}')

@cli.command()
@click.argument('filename')
def info(filename):
    """Show information about FILENAME."""
    if not os.path.exists(filename):
        click.echo(f'Error: {filename} not found', err=True)
        raise SystemExit(1)

    size = os.path.getsize(filename)
    click.echo(f'File: {filename}')
    click.echo(f'Size: {size:,} bytes')

@cli.command()
@click.argument('filename')
@click.confirmation_option(prompt='Are you sure you want to delete?')
def delete(filename):
    """Delete FILENAME (with confirmation)."""
    os.remove(filename)
    click.secho(f'Deleted {filename}', fg='green')

# ============================================
# Colored Output and Progress
# ============================================

@click.command()
def demo_colors():
    """Demonstrate colored output."""
    click.secho('Error message', fg='red')
    click.secho('Warning message', fg='yellow')
    click.secho('Success message', fg='green')
    click.secho('Bold text', bold=True)
    click.secho('Underlined', underline=True)

@click.command()
@click.option('--count', default=10, help='Number of items')
def demo_progress(count):
    """Show progress bar."""
    import time
    click.echo('Processing...')
    with click.progressbar(range(count), label='Progress') as bar:
        for i in bar:
            time.sleep(0.1)
    click.secho('Done!', fg='green', bold=True)

# ============================================
# Testing with CliRunner
# ============================================

def run_tests():
    """Test CLI commands using CliRunner."""
    from click.testing import CliRunner

    runner = CliRunner()

    # Test hello command
    result = runner.invoke(hello)
    assert result.exit_code == 0
    assert 'Hello, World!' in result.output
    print('PASS: hello (default)')

    result = runner.invoke(hello, ['--name', 'Alice'])
    assert result.exit_code == 0
    assert 'Hello, Alice!' in result.output
    print('PASS: hello --name Alice')

    result = runner.invoke(hello, ['--shout'])
    assert result.exit_code == 0
    assert 'HELLO, WORLD!' in result.output
    print('PASS: hello --shout')

    # Test group commands
    result = runner.invoke(cli, ['--version'])
    assert result.exit_code == 0
    assert '1.0.0' in result.output
    print('PASS: cli --version')

    result = runner.invoke(cli, ['--help'])
    assert result.exit_code == 0
    assert 'File management CLI tool' in result.output
    print('PASS: cli --help')

    result = runner.invoke(list_files, ['.'])
    assert result.exit_code == 0
    print('PASS: list_files')

    print()
    print('All CLI tests passed!')

# ============================================
# Main Execution
# ============================================

if __name__ == '__main__':
    import sys

    if len(sys.argv) > 1 and sys.argv[1] == 'test':
        # Run tests: python basic_cli.py test
        run_tests()
    elif len(sys.argv) > 1 and sys.argv[1] in ['process', 'list-files', 'info', 'delete', 'demo-colors', 'demo-progress']:
        # Run as grouped CLI
        cli()
    else:
        # Run basic hello command
        hello(standalone_mode=False)
