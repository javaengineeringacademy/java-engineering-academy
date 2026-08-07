"""
Module 13: Logging - Structured Logging Exercises
=================================================
Practice implementing structured logging in Python.
"""

import json
import logging
import time
from datetime import datetime

# =============================================================================
# Exercise 1: JSON Logger (★☆☆☆☆)
# =============================================================================
# TODO: Create logger that outputs JSON

class JSONFormatter(logging.Formatter):
    """Format log records as JSON."""
    # TODO: Override format to produce JSON output
    pass

# Test Cases
def test_json_logger():
    logger = logging.getLogger("json_test")
    logger.handlers.clear()
    handler = logging.StreamHandler()
    handler.setFormatter(JSONFormatter())
    logger.addHandler(handler)
    logger.setLevel(logging.INFO)
    
    # Capture output
    import io
    stream = io.StringIO()
    handler.stream = stream
    
    logger.info("Test event", extra={"user_id": 123})
    
    output = stream.getvalue().strip()
    data = json.loads(output)
    assert data["message"] == "Test event"
    assert data["user_id"] == 123
    print("✓ Exercise 1 passed: JSON logger works")

# =============================================================================
# Exercise 2: Structured Log Record (★★☆☆☆)
# =============================================================================
# TODO: Create structured log record class

class StructuredRecord:
    """Structured log record with typed fields."""
    # TODO: Support various field types
    # TODO: Support nested fields
    pass

# Test Cases
def test_structured_record():
    record = StructuredRecord(
        level="INFO",
        message="User logged in",
        fields={
            "user_id": 123,
            "ip": "192.168.1.1",
            "metadata": {"browser": "Chrome", "os": "Windows"}
        }
    )
    
    assert record.level == "INFO"
    assert record.fields["user_id"] == 123
    assert record.fields["metadata"]["browser"] == "Chrome"
    print("✓ Exercise 2 passed: structured record created")

# =============================================================================
# Exercise 3: Context Propagation (★★★☆☆)
# =============================================================================
# TODO: Propagate context through log records

class ContextLogger:
    """Logger that propagates context automatically."""
    # TODO: Store context in thread-local storage
    # TODO: Add context to all log records
    pass

# Test Tests
def test_context_propagation():
    logger = ContextLogger()
    logger.set_context(request_id="req-123", user_id=456)
    
    # All logs should have context
    record = logger.create_record("INFO", "Processing order")
    assert record["request_id"] == "req-123"
    assert record["user_id"] == 456
    
    # Clear context
    logger.clear_context()
    record2 = logger.create_record("INFO", "Background task")
    assert "request_id" not in record2
    print("✓ Exercise 3 passed: context propagation works")

# =============================================================================
# Exercise 4: Log Aggregator (★★★★☆)
# =============================================================================
# TODO: Aggregate log records for batch processing

class LogAggregator:
    """Aggregate log records for batch sending."""
    # TODO: Buffer records
    # TODO: Flush when buffer full or timeout reached
    pass

# Test Cases
def test_log_aggregator():
    aggregator = LogAggregator(buffer_size=3)
    flushed = []
    
    def on_flush(records):
        flushed.extend(records)
    
    aggregator.on_flush = on_flush
    
    aggregator.add({"level": "INFO", "msg": "1"})
    aggregator.add({"level": "INFO", "msg": "2"})
    assert len(flushed) == 0  # Not yet
    
    aggregator.add({"level": "INFO", "msg": "3"})  # Triggers flush
    assert len(flushed) == 3
    
    print("✓ Exercise 4 passed: log aggregation works")

# =============================================================================
# Exercise 5: Distributed Tracing Logger (★★★★★)
# =============================================================================
# TODO: Implement distributed tracing with log correlation

class TracingLogger:
    """Logger with distributed tracing support."""
    # TODO: Generate and propagate trace IDs
    # TODO: Create spans for operations
    pass

# Test Cases
def test_tracing_logger():
    tracer = TracingLogger()
    
    with tracer.start_span("operation1") as span1:
        span1.log("Starting operation")
        time.sleep(0.01)
        
        with tracer.start_span("operation2", parent=span1) as span2:
            span2.log("Nested work")
    
    trace = tracer.get_trace()
    assert len(trace) == 2
    assert trace[0]["parent_id"] is None
    assert trace[1]["parent_id"] == trace[0]["span_id"]
    
    print("✓ Exercise 5 passed: distributed tracing works")

if __name__ == "__main__":
    print("Running Structured Logging Exercises...")
    print("=" * 50)
    test_json_logger()
    test_structured_record()
    test_context_propagation()
    test_log_aggregator()
    test_tracing_logger()
    print("=" * 50)
    print("All tests passed!")
