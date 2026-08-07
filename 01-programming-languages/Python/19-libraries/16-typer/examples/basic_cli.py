import typer
from typing import Optional

app = typer.Typer()

@app.command()
def hello(name: str, formal: bool = False):
    """Say hello to someone."""
    if formal:
        typer.echo(f"Good day, {name}!")
    else:
        typer.echo(f"Hello, {name}!")

@app.command()
def add(a: int, b: int):
    """Add two numbers."""
    result = a + b
    typer.echo(f"{a} + {b} = {result}")

@app.command()
def greet(name: str, times: int = typer.Option(1, help="Number of times to greet")):
    """Greet someone multiple times."""
    for _ in range(times):
        typer.echo(f"Hello, {name}!")

if __name__ == "__main__":
    app()
