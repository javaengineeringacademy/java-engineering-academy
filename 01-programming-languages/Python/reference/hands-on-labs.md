# Python Hands-On Labs

## Lab 1: Basic Data Structures

### Exercise
```python
# Create a function that takes a list and returns a dictionary
# with elements as keys and their counts as values

def count_elements(lst):
    """Count occurrences of each element."""
    counts = {}
    for item in lst:
        counts[item] = counts.get(item, 0) + 1
    return counts

# Test
words = ['apple', 'banana', 'apple', 'cherry', 'banana', 'apple']
print(count_elements(words))
# Expected: {'apple': 3, 'banana': 2, 'cherry': 1}
```

## Lab 2: File Processing

### Exercise
```python
# Create a function that reads a CSV file and returns
# the average of a numeric column

import csv

def average_column(filename, column_name):
    """Calculate average of a CSV column."""
    total = 0
    count = 0
    
    with open(filename, 'r') as f:
        reader = csv.DictReader(f)
        for row in reader:
            total += float(row[column_name])
            count += 1
    
    return total / count if count > 0 else 0

# Test with sample CSV
# Create sample.csv first:
# name,score
# Alice,95
# Bob,87
# Charlie,92
```

## Lab 3: Decorators

### Exercise
```python
# Create a retry decorator that retries a function
# up to N times on failure

import time
from functools import wraps

def retry(max_attempts=3, delay=1):
    """Retry decorator."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts - 1:
                        raise
                    print(f"Attempt {attempt + 1} failed: {e}")
                    time.sleep(delay)
        return wrapper
    return decorator

# Test
@retry(max_attempts=3, delay=0.5)
def unstable_function():
    import random
    if random.random() < 0.7:
        raise ValueError("Random failure")
    return "Success!"
```

## Lab 4: Context Managers

### Exercise
```python
# Create a context manager for database connections

from contextlib import contextmanager

@contextmanager
def database_connection(connection_string):
    """Context manager for database connections."""
    print(f"Connecting to {connection_string}")
    connection = {"connected": True, "string": connection_string}
    
    try:
        yield connection
    except Exception as e:
        print(f"Error: {e}")
        connection["connected"] = False
    finally:
        print("Closing connection")
        connection["connected"] = False

# Test
with database_connection("postgresql://localhost/mydb") as conn:
    print(f"Connected: {conn['connected']}")
    # Simulate database operation
    print("Performing query...")
```

## Lab 5: Generators

### Exercise
```python
# Create a generator that yields Fibonacci numbers
# up to a maximum value

def fibonacci_up_to(max_value):
    """Generate Fibonacci numbers up to max_value."""
    a, b = 0, 1
    while a <= max_value:
        yield a
        a, b = b, a + b

# Test
for num in fibonacci_up_to(100):
    print(num, end=" ")
# Output: 0 1 1 2 3 5 8 13 21 34 55 89
```

## Lab 6: Class Design

### Exercise
```python
# Create a stack data structure class

class Stack:
    def __init__(self):
        self.items = []
    
    def push(self, item):
        self.items.append(item)
    
    def pop(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self.items.pop()
    
    def peek(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self.items[-1]
    
    def is_empty(self):
        return len(self.items) == 0
    
    def size(self):
        return len(self.items)

# Test
stack = Stack()
stack.push(1)
stack.push(2)
stack.push(3)
print(stack.pop())  # 3
print(stack.peek())  # 2
print(stack.size())  # 2
```

## Lab 7: Exception Handling

### Exercise
```python
# Create a safe calculator with proper error handling

def safe_calculate(expression):
    """Safely evaluate a mathematical expression."""
    try:
        # Validate input
        allowed_chars = set('0123456789+-*/.() ')
        if not all(c in allowed_chars for c in expression):
            raise ValueError("Invalid characters in expression")
        
        # Evaluate
        result = eval(expression)
        return {"success": True, "result": result}
    
    except ZeroDivisionError:
        return {"success": False, "error": "Division by zero"}
    except SyntaxError:
        return {"success": False, "error": "Invalid syntax"}
    except Exception as e:
        return {"success": False, "error": str(e)}

# Test
print(safe_calculate("2 + 3 * 4"))  # {'success': True, 'result': 14}
print(safe_calculate("10 / 0"))  # {'success': False, 'error': 'Division by zero'}
print(safe_calculate("2 +"))  # {'success': False, 'error': 'Invalid syntax'}
```

## Lab 8: Async Programming

### Exercise
```python
import asyncio

async def fetch_data(url, delay):
    """Simulate async data fetching."""
    print(f"Fetching {url}...")
    await asyncio.sleep(delay)
    return f"Data from {url}"

async def main():
    """Fetch data from multiple URLs concurrently."""
    urls = [
        ("http://example.com/1", 1),
        ("http://example.com/2", 2),
        ("http://example.com/3", 1.5),
    ]
    
    tasks = [fetch_data(url, delay) for url, delay in urls]
    results = await asyncio.gather(*tasks)
    
    for result in results:
        print(result)

# Run
asyncio.run(main())
```

## Lab 9: Testing

### Exercise
```python
# Create tests for a Calculator class

import pytest

class Calculator:
    def add(self, a, b):
        return a + b
    
    def subtract(self, a, b):
        return a - b
    
    def multiply(self, a, b):
        return a * b
    
    def divide(self, a, b):
        if b == 0:
            raise ValueError("Cannot divide by zero")
        return a / b

# Write tests
def test_add():
    calc = Calculator()
    assert calc.add(2, 3) == 5
    assert calc.add(-1, 1) == 0

def test_divide_by_zero():
    calc = Calculator()
    with pytest.raises(ValueError):
        calc.divide(10, 0)

# Run: pytest test_calculator.py -v
```

## Lab 10: Data Processing

### Exercise
```python
# Process a list of dictionaries and generate a report

from collections import defaultdict

def generate_report(orders):
    """Generate sales report from orders."""
    report = {
        "total_orders": len(orders),
        "total_revenue": 0,
        "by_category": defaultdict(float),
        "by_date": defaultdict(float),
    }
    
    for order in orders:
        report["total_revenue"] += order["amount"]
        report["by_category"][order["category"]] += order["amount"]
        report["by_date"][order["date"]] += order["amount"]
    
    return dict(report)

# Test data
orders = [
    {"id": 1, "category": "electronics", "amount": 999.99, "date": "2024-01-15"},
    {"id": 2, "category": "clothing", "amount": 49.99, "date": "2024-01-15"},
    {"id": 3, "category": "electronics", "amount": 199.99, "date": "2024-01-16"},
]

report = generate_report(orders)
print(f"Total orders: {report['total_orders']}")
print(f"Total revenue: ${report['total_revenue']:.2f}")
print("By category:", dict(report["by_category"]))
```

## Solutions

Each lab includes:
1. Problem description
2. Starter code
3. Test cases
4. Expected output

### Running Labs
```bash
# Create virtual environment
python -m venv venv
source venv/bin/activate

# Install dependencies
pip install pytest

# Run tests
pytest labs/ -v
```
