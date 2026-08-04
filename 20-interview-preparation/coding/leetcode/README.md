# LeetCode Strategy Guide

Master LeetCode with pattern-based learning and efficient problem-solving strategies.

## Overview

LeetCode is the primary platform for coding interview preparation. This guide provides a structured approach to solving problems efficiently.

## Getting Started

### Account Setup
- **Premium**: Consider for company-specific questions
- **Track Progress**: Use lists to organize problems
- **Daily Streak**: Build consistency

### Problem Organization
```
LeetCode/
├── Easy/          (150-200 problems)
├── Medium/        (300-400 problems)
└── Hard/          (100-150 problems)
```

## Problem-Solving Framework

### Step 1: Read and Understand (2-3 minutes)
- Read problem statement carefully
- Identify input/output types
- Note constraints
- Look at examples

### Step 2: Plan Approach (5 minutes)
- Identify problem pattern
- Discuss multiple solutions
- Analyze time/space complexity
- Choose optimal approach

### Step 3: Implement (15-20 minutes)
- Write clean, readable code
- Use meaningful variable names
- Handle edge cases
- Think out loud

### Step 4: Test (5 minutes)
- Walk through examples
- Test edge cases
- Check for bugs
- Optimize if needed

## Common Patterns

### 1. Two Pointers
**When to Use:** Sorted arrays, pair problems

```python
# Two Sum II (Sorted Array)
def two_sum_sorted(numbers, target):
    left, right = 0, len(numbers) - 1
    while left < right:
        current = numbers[left] + numbers[right]
        if current == target:
            return [left + 1, right + 1]
        elif current < target:
            left += 1
        else:
            right -= 1
    return []
```

**Problems:**
- Two Sum II (167)
- Valid Palindrome (125)
- Container With Most Water (11)
- 3Sum (15)

### 2. Sliding Window
**When to Use:** Subarray/substring problems

```python
# Longest Substring Without Repeating Characters
def length_of_longest_substring(s):
    char_map = {}
    left = 0
    max_length = 0

    for right in range(len(s)):
        if s[right] in char_map and char_map[s[right]] >= left:
            left = char_map[s[right]] + 1
        char_map[s[right]] = right
        max_length = max(max_length, right - left + 1)

    return max_length
```

**Problems:**
- Minimum Size Subarray Sum (209)
- Longest Substring Without Repeating Characters (3)
- Sliding Window Maximum (239)
- Permutation in String (567)

### 3. Hash Map
**When to Use:** Frequency counting, fast lookup

```python
# Group Anagrams
def group_anagrams(strs):
    anagram_map = {}
    for s in strs:
        sorted_s = ''.join(sorted(s))
        if sorted_s not in anagram_map:
            anagram_map[sorted_s] = []
        anagram_map[sorted_s].append(s)
    return list(anagram_map.values())
```

**Problems:**
- Two Sum (1)
- Group Anagrams (49)
- Top K Frequent Elements (347)
- LRU Cache (146)

### 4. Binary Search
**When to Use:** Sorted data, search space reduction

```python
# Search in Rotated Sorted Array
def search(nums, target):
    left, right = 0, len(nums) - 1

    while left <= right:
        mid = (left + right) // 2

        if nums[mid] == target:
            return mid

        if nums[left] <= nums[mid]:
            if nums[left] <= target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        else:
            if nums[mid] < target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1

    return -1
```

**Problems:**
- Binary Search (704)
- Search in Rotated Sorted Array (33)
- Find Minimum in Rotated Sorted Array (153)
- Search a 2D Matrix (74)

### 5. Tree Traversal
**When to Use:** Hierarchical data, recursive problems

```python
# Maximum Depth of Binary Tree
def max_depth(root):
    if not root:
        return 0
    return 1 + max(max_depth(root.left), max_depth(root.right))

# Validate BST
def is_valid_bst(root):
    def validate(node, min_val, max_val):
        if not node:
            return True
        if node.val <= min_val or node.val >= max_val:
            return False
        return (validate(node.left, min_val, node.val) and
                validate(node.right, node.val, max_val))
    return validate(root, float('-inf'), float('inf'))
```

**Problems:**
- Maximum Depth of Binary Tree (104)
- Validate Binary Search Tree (98)
- Lowest Common Ancestor (236)
- Binary Tree Level Order Traversal (102)

### 6. Graph Algorithms
**When to Use:** Relationships, networks, paths

```python
# Number of Islands
def num_islands(grid):
    if not grid:
        return 0

    count = 0

    def dfs(i, j):
        if (i < 0 or i >= len(grid) or
            j < 0 or j >= len(grid[0]) or
            grid[i][j] == '0'):
            return
        grid[i][j] = '0'
        dfs(i + 1, j)
        dfs(i - 1, j)
        dfs(i, j + 1)
        dfs(i, j - 1)

    for i in range(len(grid)):
        for j in range(len(grid[0])):
            if grid[i][j] == '1':
                dfs(i, j)
                count += 1

    return count
```

**Problems:**
- Number of Islands (200)
- Clone Graph (133)
- Course Schedule (207)
- Word Ladder (127)

### 7. Dynamic Programming
**When to Use:** Optimization problems, overlapping subproblems

```python
# Climbing Stairs
def climb_stairs(n):
    if n <= 2:
        return n
    dp = [0] * (n + 1)
    dp[1] = 1
    dp[2] = 2
    for i in range(3, n + 1):
        dp[i] = dp[i - 1] + dp[i - 2]
    return dp[n]

# Coin Change
def coin_change(coins, amount):
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)
    return dp[amount] if dp[amount] != float('inf') else -1
```

**Problems:**
- Climbing Stairs (70)
- House Robber (198)
- Coin Change (322)
- Longest Increasing Subsequence (300)

### 8. Backtracking
**When to Use:** Permutations, combinations, constraints

```python
# Permutations
def permute(nums):
    result = []

    def backtrack(start):
        if start == len(nums):
            result.append(nums[:])
            return
        for i in range(start, len(nums)):
            nums[start], nums[i] = nums[i], nums[start]
            backtrack(start + 1)
            nums[start], nums[i] = nums[i], nums[start]

    backtrack(0)
    return result
```

**Problems:**
- Permutations (46)
- Subsets (78)
- Combination Sum (39)
- N-Queens (51)

## Study Plan

### Week 1-2: Arrays and Strings
- [ ] Two Sum (1)
- [ ] Best Time to Buy and Sell Stock (121)
- [ ] Contains Duplicate (217)
- [ ] Maximum Subarray (53)
- [ ] Product of Array Except Self (238)

### Week 3-4: Linked Lists
- [ ] Reverse Linked List (206)
- [ ] Merge Two Sorted Lists (21)
- [ ] Linked List Cycle (141)
- [ ] Remove Nth Node From End (19)
- [ ] Reorder List (143)

### Week 5-6: Trees
- [ ] Maximum Depth of Binary Tree (104)
- [ ] Validate BST (98)
- [ ] Level Order Traversal (102)
- [ ] Lowest Common Ancestor (236)
- [ ] Serialize and Deserialize Binary Tree (297)

### Week 7-8: Dynamic Programming
- [ ] Climbing Stairs (70)
- [ ] Coin Change (322)
- [ ] Longest Increasing Subsequence (300)
- [ ] Word Break (139)
- [ ] House Robber (198)

### Week 9-10: Graphs
- [ ] Number of Islands (200)
- [ ] Course Schedule (207)
- [ ] Pacific Atlantic Water Flow (417)
- [ ] Word Ladder (127)
- [ ] Alien Dictionary (269)

## Tips for Success

1. **Quality Over Quantity**: Understand patterns, not just solutions
2. **Time Yourself**: 20-25 minutes per problem
3. **Review Solutions**: Learn from optimal approaches
4. **Track Progress**: Use LeetCode lists
5. **Mock Interviews**: Practice under pressure

## Common Mistakes

- Not testing edge cases
- Rushing to code without planning
- Ignoring time/space complexity
- Not asking clarifying questions
- Giving up too easily