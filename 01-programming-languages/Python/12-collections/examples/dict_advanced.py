"""
Advanced Dictionary Operations in Python
Demonstrates advanced dictionary techniques
"""

from collections import defaultdict, OrderedDict, Counter
from typing import Dict, List, Any

# ============================================
# Dictionary Comprehensions
# ============================================

def dictionary_comprehensions() -> None:
    """Demonstrate dictionary comprehensions."""
    print("=== Dictionary Comprehensions ===")
    
    # Basic comprehension
    squares = {x: x**2 for x in range(10)}
    print(f"  Squares: {squares}")
    
    # Conditional comprehension
    even_squares = {x: x**2 for x in range(10) if x % 2 == 0}
    print(f"  Even squares: {even_squares}")
    
    # From two lists
    keys = ["a", "b", "c", "d"]
    values = [1, 2, 3, 4]
    paired = {k: v for k, v in zip(keys, values)}
    print(f"  Paired: {paired}")
    
    # Transform existing dict
    original = {"a": 1, "b": 2, "c": 3}
    doubled = {k: v * 2 for k, v in original.items()}
    print(f"  Doubled: {doubled}")

# ============================================
# Nested Dictionaries
# ============================================

def nested_dictionaries() -> None:
    """Work with nested dictionaries."""
    print("\n=== Nested Dictionaries ===")
    
    # Create nested structure
    users = {
        "alice": {
            "name": "Alice Smith",
            "age": 30,
            "scores": {"math": 95, "science": 88}
        },
        "bob": {
            "name": "Bob Jones",
            "age": 25,
            "scores": {"math": 82, "science": 91}
        }
    }
    
    # Access nested values
    print(f"  Alice's math score: {users['alice']['scores']['math']}")
    
    # Safe access with get()
    charlie_math = users.get("charlie", {}).get("scores", {}).get("math", "N/A")
    print(f"  Charlie's math score: {charlie_math}")
    
    # Flatten nested dict
    flattened = {}
    for user_id, user_data in users.items():
        for key, value in user_data.items():
            if isinstance(value, dict):
                for sub_key, sub_value in value.items():
                    flattened[f"{user_id}_{key}_{sub_key}"] = sub_value
            else:
                flattened[f"{user_id}_{key}"] = value
    
    print(f"  Flattened: {flattened}")

# ============================================
# DefaultDict
# ============================================

def defaultdict_examples() -> None:
    """Demonstrate defaultdict usage."""
    print("\n=== DefaultDict ===")
    
    # Group by first letter
    words = ["apple", "banana", "cherry", "avocado", "blueberry"]
    grouped = defaultdict(list)
    for word in words:
        grouped[word[0]].append(word)
    print(f"  Grouped by first letter: {dict(grouped)}")
    
    # Count occurrences
    text = "hello world hello python world hello"
    word_count = defaultdict(int)
    for word in text.split():
        word_count[word] += 1
    print(f"  Word count: {dict(word_count)}")
    
    # Build tree structure
    tree = defaultdict(dict)
    tree["animals"]["mammals"]["dogs"] = "Canis"
    tree["animals"]["mammals"]["cats"] = "Felis"
    tree["animals"]["birds"]["eagles"] = "Aquila"
    print(f"  Tree structure: {dict(tree)}")

# ============================================
# OrderedDict
# ============================================

def ordered_dict_examples() -> None:
    """Demonstrate OrderedDict usage."""
    print("\n=== OrderedDict ===")
    
    # Maintain insertion order (Python 3.7+ dicts do this too)
    od = OrderedDict()
    od["first"] = 1
    od["second"] = 2
    od["third"] = 3
    
    print(f"  OrderedDict: {od}")
    
    # Move to end
    od.move_to_end("first")
    print(f"  After move_to_end(first): {od}")
    
    # Move to beginning
    od.move_to_end("third", last=False)
    print(f"  After move_to_end(third, last=False): {od}")
    
    # Pop last item
    last = od.popitem()
    print(f"  Popped last: {last}")
    print(f"  Remaining: {od}")
    
    # LRU Cache simulation
    class LRUCache:
        def __init__(self, capacity: int) -> None:
            self.cache = OrderedDict()
            self.capacity = capacity
        
        def get(self, key: str) -> Any:
            if key in self.cache:
                self.cache.move_to_end(key)
                return self.cache[key]
            return None
        
        def put(self, key: str, value: Any) -> None:
            if key in self.cache:
                self.cache.move_to_end(key)
            self.cache[key] = value
            if len(self.cache) > self.capacity:
                self.cache.popitem(last=False)
    
    cache = LRUCache(3)
    cache.put("a", 1)
    cache.put("b", 2)
    cache.put("c", 3)
    print(f"\n  LRU Cache after inserts: {list(cache.cache.keys())}")
    cache.get("a")  # Move 'a' to end
    print(f"  After accessing 'a': {list(cache.cache.keys())}")

# ============================================
# Counter
# ============================================

def counter_examples() -> None:
    """Demonstrate Counter usage."""
    print("\n=== Counter ===")
    
    # Count characters
    text = "mississippi"
    char_count = Counter(text)
    print(f"  Character count: {char_count}")
    print(f"  Most common 3: {char_count.most_common(3)}")
    
    # Count words
    words = ["apple", "banana", "apple", "cherry", "banana", "apple"]
    word_count = Counter(words)
    print(f"  Word count: {word_count}")
    
    # Math operations
    c1 = Counter(a=3, b=1)
    c2 = Counter(a=1, b=2)
    print(f"  c1 + c2: {c1 + c2}")
    print(f"  c1 - c2: {c1 - c2}")
    
    # Elements
    print(f"  Elements: {list(c1.elements())}")

# ============================================
# Dictionary Merging
# ============================================

def dictionary_merging() -> None:
    """Demonstrate dictionary merging techniques."""
    print("\n=== Dictionary Merging ===")
    
    dict1 = {"a": 1, "b": 2}
    dict2 = {"b": 3, "c": 4}
    
    # Using | operator (Python 3.9+)
    merged = dict1 | dict2
    print(f"  dict1 | dict2: {merged}")
    
    # Using update()
    merged_copy = dict1.copy()
    merged_copy.update(dict2)
    print(f"  Using update(): {merged_copy}")
    
    # Merge with |=
    dict3 = {"d": 5}
    dict3 |= {"e": 6}
    print(f"  Using |=: {dict3}")
    
    # Merge multiple dicts
    def merge_dicts(*dicts):
        result = {}
        for d in dicts:
            result.update(d)
        return result
    
    merged_multiple = merge_dicts({"a": 1}, {"b": 2}, {"c": 3})
    print(f"  Merged multiple: {merged_multiple}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    dictionary_comprehensions()
    nested_dictionaries()
    defaultdict_examples()
    ordered_dict_examples()
    counter_examples()
    dictionary_merging()
