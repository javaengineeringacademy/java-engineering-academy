"""
Module 12 - Collections: Counter and Deque Solutions
Complete solutions with explanations
"""

from collections import Counter, deque
from functools import reduce


# =============================================================================
# Exercise 1: Counter Basics - SOLUTION
# =============================================================================

def exercise_1_counter_basics():
    """
    Use Counter for counting operations.
    """
    text = "hello world hello python world hello"
    
    # Count character frequency in a string
    char_count = Counter(text.replace(" ", ""))
    
    # Find most common elements
    most_common = char_count.most_common(3)
    
    # Count words in a sentence
    word_count = Counter(text.split())
    
    return {
        'char_count': char_count,
        'most_common': most_common,
        'word_count': word_count
    }


# =============================================================================
# Exercise 2: Counter Arithmetic - SOLUTION
# =============================================================================

def exercise_2_counter_arithmetic():
    """
    Perform arithmetic operations on Counters.
    """
    counter1 = Counter({'a': 3, 'b': 1, 'c': 2})
    counter2 = Counter({'a': 1, 'b': 2, 'd': 4})
    
    # Add two counters
    added = counter1 + counter2
    
    # Subtract counters
    subtracted = counter1 - counter2
    
    # Find common elements (intersection)
    common = counter1 & counter2
    
    return {
        'added': added,
        'subtracted': subtracted,
        'common': common
    }


# =============================================================================
# Exercise 3: Deque Basics - SOLUTION
# =============================================================================

def exercise_3_deque_basics():
    """
    Use deque for efficient queue/stack operations.
    """
    # Create deque from list
    dq = deque([1, 2, 3])
    results = []
    
    # Append and appendleft
    dq.append(4)      # Add to right
    dq.appendleft(0)  # Add to left
    results.append(('after_append', list(dq)))
    
    # Pop and popleft
    right = dq.pop()      # Remove from right
    left = dq.popleft()   # Remove from left
    results.append(('popped', right, left))
    
    return {
        'deque': list(dq),
        'results': results
    }


# =============================================================================
# Exercise 4: Deque with maxlen - SOLUTION
# =============================================================================

def exercise_4_deque_maxlen():
    """
    Use deque with maxlen for sliding window.
    """
    def sliding_average(data, window_size):
        """Calculate sliding window average."""
        window = deque(maxlen=window_size)
        averages = []
        
        for num in data:
            window.append(num)
            if len(window) == window_size:
                averages.append(sum(window) / window_size)
        
        return averages
    
    def last_n_items(items, n):
        """Track last n items."""
        tracker = deque(maxlen=n)
        for item in items:
            tracker.append(item)
        return list(tracker)
    
    return sliding_average, last_n_items


# =============================================================================
# Exercise 5: Advanced Counter and Deque - SOLUTION
# =============================================================================

def exercise_5_advanced_counter_deque():
    """
    Advanced operations with Counter and deque.
    """
    def find_anagrams(words):
        """Group anagrams using Counter."""
        anagrams = {}
        for word in words:
            # Sort the word to create a key
            key = ''.join(sorted(word.lower()))
            if key not in anagrams:
                anagrams[key] = []
            anagrams[key].append(word)
        return anagrams
    
    class LRUCache:
        """LRU Cache using deque and dict."""
        
        def __init__(self, capacity):
            self.capacity = capacity
            self.cache = {}
            self.order = deque()
        
        def get(self, key):
            """Get item from cache."""
            if key in self.cache:
                # Move to end (most recently used)
                self.order.remove(key)
                self.order.append(key)
                return self.cache[key]
            return None
        
        def put(self, key, value):
            """Put item in cache."""
            if key in self.cache:
                # Update existing
                self.cache[key] = value
                self.order.remove(key)
                self.order.append(key)
            else:
                # Add new
                if len(self.cache) >= self.capacity:
                    # Remove least recently used
                    oldest = self.order.popleft()
                    del self.cache[oldest]
                self.cache[key] = value
                self.order.append(key)
    
    def merge_sorted_deques(dq1, dq2):
        """Merge two sorted deques."""
        result = deque()
        i, j = 0, 0
        
        while i < len(dq1) and j < len(dq2):
            if dq1[i] <= dq2[j]:
                result.append(dq1[i])
                i += 1
            else:
                result.append(dq2[j])
                j += 1
        
        while i < len(dq1):
            result.append(dq1[i])
            i += 1
        
        while j < len(dq2):
            result.append(dq2[j])
            j += 1
        
        return result
    
    return find_anagrams, LRUCache, merge_sorted_deques


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 12 - Counter and Deque Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Counter Basics")
    result = exercise_1_counter_basics()
    assert result['char_count']['l'] == 6  # 'hello' has 2 l's, repeated 3 times
    assert result['word_count']['hello'] == 3
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Counter Arithmetic")
    result = exercise_2_counter_arithmetic()
    assert result['added']['a'] == 4
    assert result['added']['d'] == 4
    assert result['subtracted']['b'] == -1
    assert result['common']['a'] == 1
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Deque Basics")
    result = exercise_3_deque_basics()
    assert result['deque'] == [0, 1, 2, 3]
    assert result['results'][1] == ('popped', 4, 0)
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Deque with maxlen")
    sliding_average, last_n_items = exercise_4_deque_maxlen()
    
    avgs = sliding_average([1, 2, 3, 4, 5, 6], 3)
    assert avgs == [2.0, 3.0, 4.0, 5.0]
    
    items = last_n_items([1, 2, 3, 4, 5], 3)
    assert items == [3, 4, 5]
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced Counter and Deque")
    find_anagrams, LRUCache, merge_sorted_deques = exercise_5_advanced_counter_deque()
    
    anagrams = find_anagrams(['eat', 'tea', 'tan', 'ate', 'nat', 'bat'])
    assert 'aet' in anagrams
    assert set(anagrams['aet']) == {'eat', 'tea', 'ate'}
    
    cache = LRUCache(2)
    cache.put('a', 1)
    cache.put('b', 2)
    assert cache.get('a') == 1
    cache.put('c', 3)  # Should evict 'b'
    assert cache.get('b') is None
    assert cache.get('c') == 3
    
    dq1 = deque([1, 3, 5])
    dq2 = deque([2, 4, 6])
    merged = merge_sorted_deques(dq1, dq2)
    assert list(merged) == [1, 2, 3, 4, 5, 6]
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
