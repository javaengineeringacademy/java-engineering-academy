"""
Counter and Deque in Python
Demonstrates advanced collections module usage
"""

from collections import Counter, deque, namedtuple, defaultdict
from typing import List, Dict, Any

# ============================================
# Counter Advanced Usage
# ============================================

def counter_advanced() -> None:
    """Demonstrate advanced Counter features."""
    print("=== Counter Advanced ===")
    
    # From dictionary
    counts = Counter({"a": 3, "b": 1, "c": 2})
    print(f"  From dict: {counts}")
    
    # Math operations
    c1 = Counter(a=4, b=2, c=0)
    c2 = Counter(a=1, b=3, d=5)
    
    print(f"  c1: {c1}")
    print(f"  c2: {c2}")
    print(f"  c1 + c2: {c1 + c2}")
    print(f"  c1 - c2: {c1 - c2}")
    print(f"  c1 & c2 (min): {c1 & c2}")
    print(f"  c1 | c2 (max): {c1 | c2}")
    
    # Update and subtract
    c3 = Counter(a=1, b=2)
    c3.update({"a": 3, "c": 4})
    print(f"  After update: {c3}")
    
    c3.subtract({"a": 1, "b": 1})
    print(f"  After subtract: {c3}")

# ============================================
# Counter for Text Analysis
# ============================================

def text_analysis() -> None:
    """Use Counter for text analysis."""
    print("\n=== Text Analysis ===")
    
    text = """
    Python is a programming language that lets you work quickly
    and integrate systems more effectively.
    """
    
    # Character frequency
    char_freq = Counter(text.lower())
    print(f"  Top 5 characters: {char_freq.most_common(5)}")
    
    # Word frequency
    words = text.lower().split()
    word_freq = Counter(words)
    print(f"  Top 5 words: {word_freq.most_common(5)}")
    
    # Bigrams
    bigrams = list(zip(words, words[1:]))
    bigram_freq = Counter(bigrams)
    print(f"  Top 3 bigrams: {bigram_freq.most_common(3)}")

# ============================================
# Deque Basics
# ============================================

def deque_basics() -> None:
    """Demonstrate deque basics."""
    print("\n=== Deque Basics ===")
    
    # Create deque
    d = deque([1, 2, 3, 4, 5])
    print(f"  Initial: {d}")
    
    # Add to ends
    d.append(6)
    d.appendleft(0)
    print(f"  After append/appendleft: {d}")
    
    # Remove from ends
    right = d.pop()
    left = d.popleft()
    print(f"  After pop/popleft: {d}")
    print(f"  Removed: right={right}, left={left}")
    
    # Extend
    d.extend([7, 8])
    d.extendleft([-2, -1])
    print(f"  After extend/extendleft: {d}")

# ============================================
# Deque Rotation
# ============================================

def deque_rotation() -> None:
    """Demonstrate deque rotation."""
    print("\n=== Deque Rotation ===")
    
    d = deque([1, 2, 3, 4, 5])
    print(f"  Original: {d}")
    
    # Rotate right
    d.rotate(2)
    print(f"  Rotate right 2: {d}")
    
    # Rotate left
    d.rotate(-2)
    print(f"  Rotate left 2: {d}")
    
    # Negative rotation
    d.rotate(-1)
    print(f"  Rotate left 1: {d}")

# ============================================
# Deque with maxlen
# ============================================

def deque_maxlen() -> None:
    """Demonstrate deque with maxlen."""
    print("\n=== Deque with maxlen ===")
    
    # Fixed-size buffer
    buffer = deque(maxlen=5)
    
    for i in range(10):
        buffer.append(i)
        print(f"  Append {i}: {list(buffer)}")
    
    print(f"  Final buffer: {list(buffer)}")

# ============================================
# Deque as Queue
# ============================================

def deque_as_queue() -> None:
    """Use deque as a queue."""
    print("\n=== Deque as Queue ===")
    
    queue = deque()
    
    # Enqueue
    queue.append("task1")
    queue.append("task2")
    queue.append("task3")
    print(f"  Queue: {queue}")
    
    # Dequeue
    while queue:
        task = queue.popleft()
        print(f"  Processing: {task}")

# ============================================
# Deque as Stack
# ============================================

def deque_as_stack() -> None:
    """Use deque as a stack."""
    print("\n=== Deque as Stack ===")
    
    stack = deque()
    
    # Push
    stack.append("frame1")
    stack.append("frame2")
    stack.append("frame3")
    print(f"  Stack: {stack}")
    
    # Pop
    while stack:
        frame = stack.pop()
        print(f"  Popping: {frame}")

# ============================================
# Practical: Sliding Window
# ============================================

def sliding_window() -> None:
    """Implement sliding window using deque."""
    print("\n=== Sliding Window ===")
    
    def max_sliding_window(nums: List[int], k: int) -> List[int]:
        """Find maximum in each sliding window."""
        result = []
        dq = deque()
        
        for i, num in enumerate(nums):
            # Remove indices outside window
            while dq and dq[0] < i - k + 1:
                dq.popleft()
            
            # Remove smaller elements
            while dq and nums[dq[-1]] < num:
                dq.pop()
            
            dq.append(i)
            
            # Add to result when window is complete
            if i >= k - 1:
                result.append(nums[dq[0]])
        
        return result
    
    nums = [1, 3, -1, -3, 5, 3, 6, 7]
    k = 3
    print(f"  Array: {nums}")
    print(f"  Window size: {k}")
    print(f"  Max in each window: {max_sliding_window(nums, k)}")

# ============================================
# Practical: LRU Cache
# ============================================

def lru_cache_demo() -> None:
    """Implement LRU cache using deque."""
    print("\n=== LRU Cache ===")
    
    class LRUCache:
        def __init__(self, capacity: int) -> None:
            self.capacity = capacity
            self.cache = {}
            self.order = deque()
        
        def get(self, key: str) -> Any:
            if key in self.cache:
                self.order.remove(key)
                self.order.append(key)
                return self.cache[key]
            return None
        
        def put(self, key: str, value: Any) -> None:
            if key in self.cache:
                self.order.remove(key)
            elif len(self.cache) >= self.capacity:
                oldest = self.order.popleft()
                del self.cache[oldest]
            
            self.cache[key] = value
            self.order.append(key)
        
        def display(self) -> None:
            print(f"    Cache: {self.cache}")
            print(f"    Order: {list(self.order)}")
    
    cache = LRUCache(3)
    cache.put("a", 1)
    cache.put("b", 2)
    cache.put("c", 3)
    print("  After inserting a, b, c:")
    cache.display()
    
    cache.get("a")  # Move 'a' to end
    print("\n  After accessing 'a':")
    cache.display()
    
    cache.put("d", 4)  # Evict 'b'
    print("\n  After inserting 'd':")
    cache.display()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    counter_advanced()
    text_analysis()
    deque_basics()
    deque_rotation()
    deque_maxlen()
    deque_as_queue()
    deque_as_stack()
    sliding_window()
    lru_cache_demo()
