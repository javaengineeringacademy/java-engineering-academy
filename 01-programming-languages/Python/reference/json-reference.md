# Python JSON Reference

## What is JSON?

JSON (JavaScript Object Notation) is a lightweight data interchange format. The json module in Python provides tools for encoding and decoding JSON data.

## Why does JSON matter?

Understanding JSON helps you:
- Work with REST APIs
- Store and load configuration files
- Exchange data between systems
- Serialize Python objects

---

## 1. Encoding (Python to JSON)

```python
import json

# Basic types
print(json.dumps(42))          # "42"
print(json.dumps(3.14))        # "3.14"
print(json.dumps("hello"))     # "\"hello\""
print(json.dumps(True))        # "true"
print(json.dumps(None))        # "null"

# Lists and dicts
print(json.dumps([1, 2, 3]))   # "[1, 2, 3]"
print(json.dumps({"a": 1}))    # "{\"a\": 1}"

# Pretty printing
data = {"name": "Alice", "age": 30, "city": "New York"}
print(json.dumps(data, indent=2))
# {
#   "name": "Alice",
#   "age": 30,
#   "city": "New York"
# }

# Sorting keys
print(json.dumps(data, sort_keys=True))

# Custom separators
print(json.dumps(data, separators=(',', ':')))
```

---

## 2. Decoding (JSON to Python)

```python
import json

# Basic types
print(json.loads("42"))        # 42
print(json.loads("3.14"))      # 3.14
print(json.loads("\"hello\"")) # hello
print(json.loads("true"))      # True
print(json.loads("null"))      # None

# Lists and dicts
print(json.loads("[1, 2, 3]"))  # [1, 2, 3]
print(json.loads("{\"a\": 1}")) # {'a': 1}

# From file
with open('data.json') as f:
    data = json.load(f)

# Error handling
try:
    data = json.loads("invalid json")
except json.JSONDecodeError as e:
    print(f"Error: {e}")
```

---

## 3. Custom Serializer

```python
import json
from datetime import datetime

class DateTimeEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime):
            return obj.isoformat()
        return super().default(obj)

data = {
    "name": "Alice",
    "created_at": datetime.now()
}

print(json.dumps(data, cls=DateTimeEncoder))
```

---

## 4. Custom Decoder

```python
import json
from datetime import datetime

def datetime_decoder(dct):
    for key, value in dct.items():
        if isinstance(value, str):
            try:
                dct[key] = datetime.fromisoformat(value)
            except ValueError:
                pass
    return dct

json_str = '{"name": "Alice", "created_at": "2024-01-01T12:00:00"}'
data = json.loads(json_str, object_hook=datetime_decoder)
```

---

## 5. JSON Lines

```python
import json

# Write JSON lines
with open('data.jsonl', 'w') as f:
    for item in data:
        f.write(json.dumps(item) + '\n')

# Read JSON lines
with open('data.jsonl') as f:
    data = [json.loads(line) for line in f]
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **dumps** | Encode to JSON string | `json.dumps(data)` |
| **dump** | Encode to file | `json.dump(data, f)` |
| **loads** | Decode from JSON string | `json.loads(json_str)` |
| **load** | Decode from file | `json.load(f)` |
| **JSONEncoder** | Custom encoder | `cls=MyEncoder` |
| **JSONDecodeError** | Decode error | `except json.JSONDecodeError` |

---

## Common Mistakes

### 1. Forgetting About Non-Serializable Types

```python
# WRONG
import json
from datetime import datetime
json.dumps(datetime.now())  # TypeError

# RIGHT
class DateTimeEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime):
            return obj.isoformat()
        return super().default(obj)

json.dumps(datetime.now(), cls=DateTimeEncoder)
```

### 2. Not Using `indent` for Debugging

```python
# WRONG
print(json.dumps(data))  # Hard to read

# RIGHT
print(json.dumps(data, indent=2))  # Easy to read
```

### 3. Not Handling Errors

```python
# WRONG
data = json.loads("invalid json")

# RIGHT
try:
    data = json.loads("invalid json")
except json.JSONDecodeError as e:
    print(f"Error: {e}")
```

---

## Production Notes

1. **Use `indent` for debugging** - Makes output readable
2. **Use `sort_keys` for consistent output** - Easier to diff
3. **Use custom encoder for non-standard types** - datetime, Decimal, etc.
4. **Use `json.loads` for strings** - `json.load` for files
5. **Use `json.dumps` for strings** - `json.dump` for files
6. **Handle JSONDecodeError** - Always wrap in try/except
7. **Use JSON Lines for streaming** - Line-delimited JSON
8. **Use `ensure_ascii=False` for non-ASCII** - Preserve Unicode
9. **Use `default` parameter for custom types** - Alternative to subclassing
10. **Be careful with `object_hook`** - Can be slow for large objects

---

## Further Reading

- Python documentation on json module
- RFC 8259 - The JavaScript Object Notation
- json.org
