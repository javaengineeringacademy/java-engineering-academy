"""
Module 09: Exception Handling - Error Handling Solutions
Practice error handling patterns and best practices.
"""

import json
import tempfile
import os


def safe_divide(a, b, default=0):
    """Divide a by b, returning default on any error."""
    try:
        result = a / b
        return result
    except (ZeroDivisionError, TypeError):
        return default


class ManagedResource:
    """Context manager that tracks open/close state."""

    def __init__(self):
        self.is_open = False

    def __enter__(self):
        self.is_open = True
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.is_open = False
        return False


def process_config(config_path):
    """Load and validate config, chaining exceptions properly."""
    try:
        with open(config_path, 'r') as f:
            config = json.load(f)
    except json.JSONDecodeError as e:
        raise ValueError("Invalid JSON format") from e

    if "host" not in config:
        raise ValueError("Missing required field: host")
    if "port" not in config:
        raise ValueError("Missing required field: port") from KeyError("port")

    return config


class ErrorCollector:
    """Collect errors during batch processing."""

    def __init__(self):
        self.errors = []
        self.results = []

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        return False

    def collect(self, func):
        """Execute function and collect errors or results."""
        try:
            result = func()
            self.results.append(result)
        except Exception as e:
            self.errors.append(e)


class Pipeline:
    """Process data through multiple stages with error handling."""

    def __init__(self):
        self.stages = []
        self.stats = {}

    def add_stage(self, name, func):
        """Add a processing stage."""
        self.stages.append((name, func))
        self.stats[name] = {"success": 0, "failure": 0}

    def run(self, data):
        """Run data through all stages."""
        result = data

        for name, func in self.stages:
            try:
                result = func(result)
                if result is not None:
                    self.stats[name]["success"] += 1
                else:
                    self.stats[name]["failure"] += 1
                    return result
            except Exception:
                self.stats[name]["failure"] += 1
                return None

        return result


if __name__ == "__main__":
    print("Testing Error Handling Solutions...")

    # Test safe_divide
    assert safe_divide(10, 2) == 5.0
    assert safe_divide(10, 0) == 0
    assert safe_divide(10, 0, default=-1) == -1
    assert safe_divide("10", 2) == 0
    print("✓ Exercise 1 passed: safe division works")

    # Test ManagedResource
    resource = ManagedResource()
    with resource as r:
        assert resource.is_open
    assert not resource.is_open
    print("✓ Exercise 2 passed: resource properly managed")

    # Test exception chaining
    invalid_config = {"host": "localhost"}
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        json.dump(invalid_config, f)
        temp_path = f.name

    try:
        process_config(temp_path)
    except ValueError as e:
        assert e.__cause__ is not None
        print(f"✓ Exercise 3 passed: exception chained properly")
    finally:
        os.unlink(temp_path)

    # Test ErrorCollector
    with ErrorCollector() as collector:
        collector.collect(lambda: 1 / 0)
        collector.collect(lambda: int("abc"))
        collector.collect(lambda: 42)

    assert len(collector.errors) == 2
    assert len(collector.results) == 1
    print(f"✓ Exercise 4 passed: collected {len(collector.errors)} errors")

    # Test Pipeline
    pipeline = Pipeline()
    pipeline.add_stage("validate", lambda x: x if x > 0 else None)
    pipeline.add_stage("double", lambda x: x * 2)
    pipeline.add_stage("stringify", lambda x: str(x))

    result = pipeline.run(5)
    assert result == "10"
    assert pipeline.stats["validate"]["success"] == 1
    print(f"✓ Exercise 5 passed: pipeline processed through stages")

    print("All Error Handling solutions passed!")
