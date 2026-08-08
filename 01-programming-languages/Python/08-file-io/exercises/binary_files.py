"""
Module 08 - File I/O: Binary Files Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Reading Binary Files (Difficulty: Beginner)
# =============================================================================
# Read binary files.

# TODO: Implement binary file reading
def read_binary(filename):
    """Read entire binary file."""
    pass

def read_binary_chunks(filename, chunk_size=1024):
    """Read binary file in chunks."""
    pass

def get_file_info(filename):
    """Get binary file information."""
    pass

# Test cases
# data = read_binary("image.png")
# print(f"File size: {len(data)} bytes")
#
# for chunk in read_binary_chunks("large_file.bin"):
#     process_chunk(chunk)


# =============================================================================
# Exercise 2: Writing Binary Files (Difficulty: Beginner)
# =============================================================================
# Write binary data to files.

# TODO: Implement binary file writing
def write_binary(filename, data):
    """Write binary data to file."""
    pass

def write_from_bytes(filename, byte_data):
    """Write bytes to file."""
    pass

# Test cases
# data = bytes([72, 101, 108, 108, 111])  # "Hello"
# write_binary("output.bin", data)


# =============================================================================
# Exercise 3: Working with Images (Difficulty: Intermediate)
# =============================================================================
# Process image files.

# TODO: Get image metadata
def get_image_metadata(filename):
    """Get basic image metadata."""
    pass

# TODO: Resize image (simple)
def resize_image_simple(filename, new_filename, scale=0.5):
    """Simple image resize (conceptual)."""
    pass

# Test cases
# metadata = get_image_metadata("photo.jpg")
# print(metadata)


# =============================================================================
# Exercise 4: Working with Pickle (Difficulty: Intermediate)
# =============================================================================
# Serialize and deserialize objects.

import pickle

# TODO: Implement pickle operations
def save_object(obj, filename):
    """Save Python object to file using pickle."""
    pass

def load_object(filename):
    """Load Python object from pickle file."""
    pass

# TODO: Implement safe pickle
def safe_pickle_save(obj, filename):
    """Save with error handling."""
    pass

def safe_pickle_load(filename):
    """Load with error handling."""
    pass

# Test cases
# data = {"users": [{"name": "Alice", "age": 30}, {"name": "Bob", "age": 25}]}
# save_object(data, "data.pkl")
# loaded = load_object("data.pkl")
# print(loaded)


# =============================================================================
# Exercise 5: Working with JSON Binary (Difficulty: Intermediate)
# =============================================================================
# Process JSON as binary.

# TODO: JSON binary operations
import json

def save_json_binary(obj, filename):
    """Save JSON object as binary file."""
    pass

def load_json_binary(filename):
    """Load JSON from binary file."""
    pass

# Test cases
# data = {"config": {"debug": True, "version": "1.0"}}
# save_json_binary(data, "config.json")
# loaded = load_json_binary("config.json")
# print(loaded)
