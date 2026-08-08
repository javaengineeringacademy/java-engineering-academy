"""
Module 08 - File I/O: Binary Files Solutions
Difficulty: Intermediate
"""

import pickle
import json
import os

# =============================================================================
# Exercise 1: Reading Binary Files - Solution
# =============================================================================
def read_binary(filename):
    """Read entire binary file."""
    with open(filename, 'rb') as f:
        return f.read()

def read_binary_chunks(filename, chunk_size=1024):
    """Read binary file in chunks."""
    with open(filename, 'rb') as f:
        while True:
            chunk = f.read(chunk_size)
            if not chunk:
                break
            yield chunk

def get_file_info(filename):
    """Get binary file information."""
    size = os.path.getsize(filename)
    with open(filename, 'rb') as f:
        header = f.read(16)
    return {
        'size': size,
        'header_hex': header.hex(),
        'header_bytes': header
    }


# =============================================================================
# Exercise 2: Writing Binary Files - Solution
# =============================================================================
def write_binary(filename, data):
    """Write binary data to file."""
    with open(filename, 'wb') as f:
        f.write(data)

def write_from_bytes(filename, byte_data):
    """Write bytes to file."""
    with open(filename, 'wb') as f:
        f.write(bytes(byte_data))

data = bytes([72, 101, 108, 108, 111])  # "Hello"
write_binary("output.bin", data)


# =============================================================================
# Exercise 3: Working with Images - Solution
# =============================================================================
def get_image_metadata(filename):
    """Get basic image metadata."""
    size = os.path.getsize(filename)
    with open(filename, 'rb') as f:
        header = f.read(32)
    return {
        'filename': filename,
        'size_bytes': size,
        'size_kb': size / 1024,
        'header': header[:16].hex()
    }


# =============================================================================
# Exercise 4: Working with Pickle - Solution
# =============================================================================
def save_object(obj, filename):
    """Save Python object to file using pickle."""
    with open(filename, 'wb') as f:
        pickle.dump(obj, f)

def load_object(filename):
    """Load Python object from pickle file."""
    with open(filename, 'rb') as f:
        return pickle.load(f)

def safe_pickle_save(obj, filename):
    """Save with error handling."""
    try:
        with open(filename, 'wb') as f:
            pickle.dump(obj, f)
        return True
    except Exception as e:
        print(f"Error saving: {e}")
        return False

def safe_pickle_load(filename):
    """Load with error handling."""
    try:
        with open(filename, 'rb') as f:
            return pickle.load(f)
    except FileNotFoundError:
        print(f"File not found: {filename}")
        return None
    except Exception as e:
        print(f"Error loading: {e}")
        return None

data = {"users": [{"name": "Alice", "age": 30}, {"name": "Bob", "age": 25}]}
save_object(data, "data.pkl")
loaded = load_object("data.pkl")
print(loaded)


# =============================================================================
# Exercise 5: Working with JSON Binary - Solution
# =============================================================================
def save_json_binary(obj, filename):
    """Save JSON object as binary file."""
    with open(filename, 'wb') as f:
        f.write(json.dumps(obj).encode('utf-8'))

def load_json_binary(filename):
    """Load JSON from binary file."""
    with open(filename, 'rb') as f:
        return json.loads(f.read().decode('utf-8'))

data = {"config": {"debug": True, "version": "1.0"}}
save_json_binary(data, "config.json")
loaded = load_json_binary("config.json")
print(loaded)
