"""
Module 09: Exception Handling - Error Handling Exercises
=======================================================
Practice error handling patterns and best practices.
"""

# =============================================================================
# Exercise 1: Safe Division (★☆☆☆☆)
# =============================================================================
# TODO: Implement safe division that handles all edge cases

def safe_divide(a, b, default=0):
    """Divide a by b, returning default on any error."""
    # TODO: Handle ZeroDivisionError, TypeError, and return default
    pass

# Test Cases
def test_safe_divide():
    assert safe_divide(10, 2) == 5.0
    assert safe_divide(10, 0) == 0
    assert safe_divide(10, 0, default=-1) == -1
    assert safe_divide("10", 2) == 0  # TypeError
    print("✓ Exercise 1 passed: safe division works")

# =============================================================================
# Exercise 2: Context Manager with Cleanup (★★☆☆☆)
# =============================================================================
# TODO: Create context manager that ensures cleanup on any exception

class ManagedResource:
    """Context manager that tracks open/close state."""
    # TODO: Implement __enter__ and __close__ with error handling
    pass

# Test Cases
def test_managed_resource():
    resource = ManagedResource()
    
    with resource as r:
        assert resource.is_open
        # Simulate work
    
    assert not resource.is_open
    print("✓ Exercise 2 passed: resource properly managed")

# =============================================================================
# Exercise 3: Exception Chaining (★★★☆☆)
# =============================================================================
# TODO: Implement proper exception chaining

def process_config(config_path):
    """Load and validate config, chaining exceptions properly."""
    # TODO: Load JSON, validate fields, chain exceptions
    pass

# Test Cases
def test_exception_chaining():
    import tempfile
    import json
    import os
    
    invalid_config = {"host": "localhost"}  # Missing port
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

# =============================================================================
# Exercise 4: Error Collector Pattern (★★★★☆)
# =============================================================================
# TODO: Implement error collector that doesn't stop on first error

class ErrorCollector:
    """Collect errors during batch processing."""
    # TODO: Implement as context manager that collects errors
    pass

# Test Cases
def test_error_collector():
    with ErrorCollector() as collector:
        collector.collect(lambda: 1 / 0)  # Should not raise
        collector.collect(lambda: int("abc"))  # Should not raise
        collector.collect(lambda: 42)  # Should succeed
    
    assert len(collector.errors) == 2
    assert len(collector.results) == 1
    print(f"✓ Exercise 4 passed: collected {len(collector.errors)} errors")

# =============================================================================
# Exercise 5: Resilient Pipeline (★★★★★)
# =============================================================================
# TODO: Build pipeline that handles errors at each stage

class Pipeline:
    """Process data through multiple stages with error handling."""
    # TODO: Implement add_stage and run methods
    # TODO: Track which stages succeeded/failed
    pass

# Test Cases
def test_pipeline():
    pipeline = Pipeline()
    pipeline.add_stage("validate", lambda x: x if x > 0 else None)
    pipeline.add_stage("double", lambda x: x * 2)
    pipeline.add_stage("stringify", lambda x: str(x))
    
    result = pipeline.run(5)
    assert result == "10"
    assert pipeline.stats["validate"]["success"] == 1
    print(f"✓ Exercise 5 passed: pipeline processed through stages")

if __name__ == "__main__":
    print("Running Error Handling Exercises...")
    print("=" * 50)
    test_safe_divide()
    test_managed_resource()
    test_exception_chaining()
    test_error_collector()
    test_pipeline()
    print("=" * 50)
    print("All tests passed!")
