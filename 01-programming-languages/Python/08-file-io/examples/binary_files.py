"""
Binary File Operations in Python
Demonstrates reading and writing binary data
"""

import struct
import pickle
import json
import os

# ============================================
# Writing Binary Files
# ============================================

def write_binary_data() -> None:
    """Write raw binary data to a file."""
    data = bytes([0x48, 0x65, 0x6C, 0x6C, 0x6F])  # "Hello"
    with open("binary.bin", "wb") as f:
        f.write(data)

def write_numbers_as_binary() -> None:
    """Write numbers in binary format."""
    numbers = [1, 2, 3, 4, 5]
    with open("numbers.bin", "wb") as f:
        for num in numbers:
            f.write(struct.pack('i', num))  # Pack as 4-byte integer

# ============================================
# Reading Binary Files
# ============================================

def read_binary_data() -> bytes:
    """Read raw binary data."""
    with open("binary.bin", "rb") as f:
        return f.read()

def read_numbers_from_binary() -> list:
    """Read numbers from binary file."""
    numbers = []
    with open("numbers.bin", "rb") as f:
        while True:
            data = f.read(4)  # Read 4 bytes (one integer)
            if not data:
                break
            num = struct.unpack('i', data)[0]
            numbers.append(num)
    return numbers

# ============================================
# Pickle Serialization
# ============================================

def pickle_example() -> None:
    """Demonstrate pickle for complex objects."""
    data = {
        "users": [
            {"name": "Alice", "age": 25, "scores": [90, 85, 92]},
            {"name": "Bob", "age": 30, "scores": [88, 95, 90]}
        ],
        "settings": {"theme": "dark", "notifications": True}
    }
    
    with open("data.pkl", "wb") as f:
        pickle.dump(data, f)

def unpickle_example() -> dict:
    """Load pickled data."""
    with open("data.pkl", "rb") as f:
        return pickle.load(f)

# ============================================
# Binary Packing with struct
# ============================================

def pack_multiple_values() -> None:
    """Pack multiple values into binary."""
    # Pack: int, float, char
    data = struct.pack('ifc', 42, 3.14, b'A')
    with open("packed.bin", "wb") as f:
        f.write(data)

def unpack_multiple_values() -> tuple:
    """Unpack multiple values from binary."""
    with open("packed.bin", "rb") as f:
        data = f.read(9)  # int(4) + float(4) + char(1) = 9 bytes
        return struct.unpack('ifc', data)

# ============================================
# Working with Image-like Data
# ============================================

def create_pixel_data() -> None:
    """Create and save pixel data (simulated image)."""
    width, height = 3, 3
    # Create RGB pixel data
    pixels = []
    for y in range(height):
        for x in range(width):
            r = (x * 85) % 256
            g = (y * 85) % 256
            b = 128
            pixels.extend([r, g, b])
    
    with open("pixels.bin", "wb") as f:
        f.write(struct.pack('HH', width, height))  # Header
        f.write(bytes(pixels))  # Pixel data

def read_pixel_data() -> tuple:
    """Read and parse pixel data."""
    with open("pixels.bin", "rb") as f:
        width, height = struct.unpack('HH', f.read(4))
        pixel_data = f.read()
        return width, height, pixel_data

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Binary data
    write_binary_data()
    binary = read_binary_data()
    print(f"Binary data: {binary}")
    print(f"Decoded: {binary.decode('utf-8')}")
    
    # Numbers
    write_numbers_as_binary()
    numbers = read_numbers_from_binary()
    print(f"\nNumbers from binary: {numbers}")
    
    # Pickle
    pickle_example()
    loaded = unpickle_example()
    print(f"\nPickle loaded: {loaded}")
    
    # Pack multiple
    pack_multiple_values()
    unpacked = unpack_multiple_values()
    print(f"\nUnpacked values: {unpacked}")
    print(f"  int: {unpacked[0]}")
    print(f"  float: {unpacked[1]}")
    print(f"  char: {unpacked[2]}")
    
    # Pixels
    create_pixel_data()
    width, height, pixels = read_pixel_data()
    print(f"\nPixel data:")
    print(f"  Dimensions: {width}x{height}")
    print(f"  Total bytes: {len(pixels)}")
    
    # Cleanup
    for file in ["binary.bin", "numbers.bin", "data.pkl", "packed.bin", "pixels.bin"]:
        if os.path.exists(file):
            os.remove(file)
            print(f"\nCleaned up: {file}")
