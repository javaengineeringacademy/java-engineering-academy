"""
Module 12 - Collections: Counter and Deque Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Counter and deque from collections module
"""


# =============================================================================
# Exercise 1: Counter Basics (⭐⭐⭐)
# =============================================================================

def exercise_1_counter_basics():
    """
    Use Counter for counting operations.
    
    TODO:
    1. Count character frequency in a string
    2. Find most common elements
    3. Count words in a sentence
    """
    from collections import Counter
    
    text = "hello world hello python world hello"
    
    char_count = Counter()
    most_common = []
    word_count = Counter()
    
    # TODO: Implement Counter operations
    pass


# =============================================================================
# Exercise 2: Counter Arithmetic (⭐⭐⭐)
# =============================================================================

def exercise_2_counter_arithmetic():
    """
    Perform arithmetic operations on Counters.
    
    TODO:
    1. Add two counters
    2. Subtract counters
    3. Find common elements
    """
    from collections import Counter
    
    counter1 = Counter({'a': 3, 'b': 1, 'c': 2})
    counter2 = Counter({'a': 1, 'b': 2, 'd': 4})
    
    added = Counter()
    subtracted = Counter()
    common = Counter()
    
    # TODO: Implement Counter arithmetic
    pass


# =============================================================================
# Exercise 3: Deque Basics (⭐⭐⭐)
# =============================================================================

def exercise_3_deque_basics():
    """
    Use deque for efficient queue/stack operations.
    
    TODO:
    1. Create deque from list
    2. Append and appendleft
    3. Pop and popleft
    """
    from collections import deque
    
    dq = deque()
    results = []
    
    # TODO: Implement deque operations
    pass


# =============================================================================
# Exercise 4: Deque with maxlen (⭐⭐⭐)
# =============================================================================

def exercise_4_deque_maxlen():
    """
    Use deque with maxlen for sliding window.
    
    TODO:
    1. Create deque with maxlen
    2. Implement sliding window average
    3. Track last N items
    """
    from collections import deque
    
    def sliding_average(data, window_size):
        # TODO: Calculate sliding window average
        pass
    
    def last_n_items(items, n):
        # TODO: Track last n items
        pass
    
    return sliding_average, last_n_items


# =============================================================================
# Exercise 5: Advanced Counter and Deque (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_advanced_counter_deque():
    """
    Advanced operations with Counter and deque.
    
    TODO:
    1. Find anagrams using Counter
    2. Implement LRU cache using deque
    3. Merge sorted deques
    """
    def find_anagrams(words):
        # TODO: Group anagrams using Counter
        pass
    
    class LRUCache:
        def __init__(self, capacity):
            # TODO: Initialize LRU cache
            pass
        
        def get(self, key):
            # TODO: Get item from cache
            pass
        
        def put(self, key, value):
            # TODO: Put item in cache
            pass
    
    def merge_sorted_deques(dq1, dq2):
        # TODO: Merge two sorted deques
        pass
    
    return find_anagrams, LRUCache, merge_sorted_deques


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 12 - Counter and Deque Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Counter Basics")
    try:
        result = exercise_1_counter_basics()
        print(f"  Char count: {dict(result['char_count'])}")
        print(f"  Most common: {result['most_common']}")
        print(f"  Word count: {dict(result['word_count'])}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Counter Arithmetic")
    try:
        result = exercise_2_counter_arithmetic()
        print(f"  Added: {dict(result['added'])}")
        print(f"  Subtracted: {dict(result['subtracted'])}")
        print(f"  Common: {dict(result['common'])}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Deque Basics")
    try:
        result = exercise_3_deque_basics()
        print(f"  Deque: {result['deque']}")
        print(f"  Results: {result['results']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Deque with maxlen")
    try:
        sliding_average, last_n_items = exercise_4_deque_maxlen()
        avgs = sliding_average([1, 2, 3, 4, 5, 6], 3)
        print(f"  Sliding averages: {avgs}")
        items = last_n_items([1, 2, 3, 4, 5], 3)
        print(f"  Last 3 items: {items}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced Counter and Deque")
    try:
        find_anagrams, LRUCache, merge_sorted_deques = exercise_5_advanced_counter_deque()
        
        anagrams = find_anagrams(['eat', 'tea', 'tan', 'ate', 'nat', 'bat'])
        print(f"  Anagrams: {anagrams}")
        
        cache = LRUCache(2)
        cache.put('a', 1)
        cache.put('b', 2)
        print(f"  LRU cache: {cache.get('a')}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
