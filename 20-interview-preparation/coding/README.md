# Coding Interview Strategy

Master the coding interview with proven strategies and practice.

## Overview

Coding interviews test your problem-solving skills, algorithmic thinking, and coding proficiency.

## Interview Structure

### 1. Understand the Problem (5 min)
- Read the problem carefully
- Ask clarifying questions
- Identify input/output examples
- Discuss edge cases

### 2. Plan Your Approach (5 min)
- Discuss multiple solutions
- Analyze time/space complexity
- Choose the best approach
- Get interviewer approval

### 3. Implement the Solution (20 min)
- Write clean, readable code
- Use meaningful variable names
- Handle edge cases
- Think out loud

### 4. Test Your Solution (5 min)
- Walk through examples
- Test edge cases
- Check for bugs
- Optimize if needed

## Problem-Solving Framework

### Step 1: Clarify Requirements
```
- What are the inputs?
- What are the outputs?
- What are the constraints?
- Are there edge cases?
```

### Step 2: Identify Patterns
- Array/String manipulation
- Two pointers
- Sliding window
- Hash maps
- Trees/Graphs
- Dynamic programming
- Recursion
- Sorting/Searching

### Step 3: Choose Data Structures
- Arrays: Sequential access
- Hash Maps: Fast lookup
- Stacks/Queues: LIFO/FIFO
- Trees: Hierarchical data
- Graphs: Relationships
- Heaps: Priority queues

### Step 4: Implement
- Write pseudocode first
- Implement step by step
- Handle edge cases
- Test as you go

### Step 5: Optimize
- Analyze complexity
- Identify bottlenecks
- Consider alternative approaches
- Trade-offs

## Common Coding Patterns

### 1. Two Pointers
```python
def two_sum(nums, target):
    left, right = 0, len(nums) - 1
    while left < right:
        current = nums[left] + nums[right]
        if current == target:
            return [left, right]
        elif current < target:
            left += 1
        else:
            right -= 1
    return []
```

### 2. Sliding Window
```python
def max_subarray_sum(nums, k):
    window_sum = sum(nums[:k])
    max_sum = window_sum
    
    for i in range(k, len(nums)):
        window_sum = window_sum - nums[i - k] + nums[i]
        max_sum = max(max_sum, window_sum)
    
    return max_sum
```

### 3. Hash Map
```python
def group_anagrams(strs):
    groups = {}
    for s in strs:
        key = ''.join(sorted(s))
        if key not in groups:
            groups[key] = []
        groups[key].append(s)
    return list(groups.values())
```

### 4. Recursion
```python
def fibonacci(n):
    if n <= 1:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)
```

### 5. Dynamic Programming
```python
def coin_change(coins, amount):
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0
    
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] != float('inf') else -1
```

## Time/Space Complexity

### Common Complexities
| Complexity | Name | Example |
|------------|------|---------|
| O(1) | Constant | Array access |
| O(log n) | Logarithmic | Binary search |
| O(n) | Linear | Linear search |
| O(n log n) | Linearithmic | Merge sort |
| O(n²) | Quadratic | Bubble sort |
| O(2ⁿ) | Exponential | Recursive Fibonacci |

### Space Complexity
- **O(1)**: In-place operations
- **O(n)**: New array of size n
- **O(n²)**: 2D array of size n×n
- **O(log n)**: Recursive call stack (balanced tree)

## Testing Strategies

### Edge Cases
- Empty input
- Single element
- Very large input
- Negative numbers
- Duplicates

### Test Cases
```python
# Example: Two Sum
def test_two_sum():
    # Normal case
    assert two_sum([2, 7, 11, 15], 9) == [0, 1]
    
    # No solution
    assert two_sum([1, 2, 3], 7) == []
    
    # Negative numbers
    assert two_sum([-1, -2, -3, -4], -6) == [2, 3]
    
    # Duplicates
    assert two_sum([3, 3], 6) == [0, 1]
```

## Communication Tips

### Think Out Loud
- Explain your thought process
- Discuss trade-offs
- Ask for feedback
- Show your reasoning

### Ask Clarifying Questions
- "Can the input be empty?"
- "Are there negative numbers?"
- "What's the expected output?"
- "Are there performance requirements?"

### Handle Hints
- Listen to interviewer hints
- Adapt your approach
- Show flexibility
- Learn from feedback

## Practice Resources

### LeetCode
- Easy: Start here
- Medium: Build skills
- Hard: Challenge yourself

### Books
- "Cracking the Coding Interview"
- "Elements of Programming Interviews"
- "Algorithm Design Manual"

### Online Platforms
- HackerRank
- CodeSignal
- Pramp (mock interviews)
- Interviewing.io

## Common Mistakes

### Coding
- Not handling edge cases
- Poor variable names
- Not testing code
- Rushing to code

### Communication
- Not explaining thought process
- Ignoring interviewer feedback
- Not asking questions
- Being too quiet

### Problem Solving
- Jumping to code too fast
- Not considering alternatives
- Ignoring complexity
- Not optimizing

## Study Plan

### Week 1-2: Arrays and Strings
- Two pointers
- Sliding window
- Hash maps
- String manipulation

### Week 3-4: Linked Lists and Stacks
- Reversal
- Cycle detection
- Stack operations
- Queue operations

### Week 5-6: Trees and Graphs
- Traversal
- BFS/DFS
- Binary search trees
- Graph algorithms

### Week 7-8: Dynamic Programming
- Memoization
- Tabulation
- Common patterns
- Optimization
