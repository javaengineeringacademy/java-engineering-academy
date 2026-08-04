# Trees

## Table of Contents

- [Overview](#overview)
- [Binary Tree](#binary-tree)
- [Binary Search Tree (BST)](#binary-search-tree-bst)
- [Self-Balancing Trees](#self-balancing-trees)
  - [AVL Tree](#avl-tree)
  - [Red-Black Tree](#red-black-tree)
- [B-Tree](#b-tree)
- [Trie](#trie)
- [Tree Traversals](#tree-traversals)
- [Time Complexity](#time-complexity)
- [Use Cases](#use-cases)

---

## Overview

A tree is a hierarchical data structure consisting of nodes connected by edges, with a single root node and no cycles.

```
Tree Structure:
           ┌─────────┐
           │    A    │  ← Root
           └────┬────┘
         ┌──────┴──────┐
    ┌────┴────┐   ┌────┴────┐
    │    B    │   │    C    │
    └────┬────┘   └────┬────┘
    ┌────┴────┐        │
┌───┴───┐ ┌───┴───┐ ┌───┴───┐
│   D   │ │   E   │ │   F   │
└───────┘ └───────┘ └───────┘
```

### Key Terminology

| Term | Definition |
|------|------------|
| Root | Top node with no parent |
| Leaf | Node with no children |
| Internal node | Node with at least one child |
| Parent | Node directly above |
| Child | Node directly below |
| Sibling | Nodes sharing same parent |
| Height | Longest path from root to leaf |
| Depth | Distance from root to node |
| Degree | Number of children |

---

## Binary Tree

Each node has at most two children (left and right).

```python
from typing import Any, Optional, List

class TreeNode:
    def __init__(self, data: Any):
        self.data = data
        self.left: Optional['TreeNode'] = None
        self.right: Optional['TreeNode'] = None

class BinaryTree:
    def __init__(self):
        self.root: Optional[TreeNode] = None

    def insert(self, data: Any) -> None:
        if not self.root:
            self.root = TreeNode(data)
        else:
            self._insert_recursive(self.root, data)

    def _insert_recursive(self, node: TreeNode, data: Any) -> None:
        if data < node.data:
            if node.left is None:
                node.left = TreeNode(data)
            else:
                self._insert_recursive(node.left, data)
        else:
            if node.right is None:
                node.right = TreeNode(data)
            else:
                self._insert_recursive(node.right, data)

    def search(self, data: Any) -> Optional[TreeNode]:
        return self._search_recursive(self.root, data)

    def _search_recursive(self, node: Optional[TreeNode], data: Any) -> Optional[TreeNode]:
        if node is None or node.data == data:
            return node
        if data < node.data:
            return self._search_recursive(node.left, data)
        return self._search_recursive(node.right, data)

    def inorder(self) -> List[Any]:
        result = []
        self._inorder_recursive(self.root, result)
        return result

    def _inorder_recursive(self, node: Optional[TreeNode], result: List) -> None:
        if node:
            self._inorder_recursive(node.left, result)
            result.append(node.data)
            self._inorder_recursive(node.right, result)
```

---

## Binary Search Tree (BST)

A binary tree where left child < parent < right child.

```
BST Property:
           ┌─────────┐
           │    8    │
           └────┬────┘
         ┌──────┴──────┐
    ┌────┴────┐   ┌────┴────┐
    │    3    │   │    10   │
    └────┬────┘   └────┬────┘
    ┌────┴────┐        │
┌───┴───┐ ┌───┴───┐ ┌───┴───┐
│   1   │ │   6   │ │   14  │
└───────┘ └───┬───┘ └───────┘
           ┌───┴───┐
           │   7   │
           └───────┘
```

```python
class BST:
    def __init__(self):
        self.root: Optional[TreeNode] = None

    def insert(self, data: Any) -> None:
        self.root = self._insert(self.root, data)

    def _insert(self, node: Optional[TreeNode], data: Any) -> TreeNode:
        if node is None:
            return TreeNode(data)
        if data < node.data:
            node.left = self._insert(node.left, data)
        elif data > node.data:
            node.right = self._insert(node.right, data)
        return node

    def delete(self, data: Any) -> None:
        self.root = self._delete(self.root, data)

    def _delete(self, node: Optional[TreeNode], data: Any) -> Optional[TreeNode]:
        if node is None:
            return None

        if data < node.data:
            node.left = self._delete(node.left, data)
        elif data > node.data:
            node.right = self._delete(node.right, data)
        else:
            # Node found - three cases:
            # Case 1: Leaf node
            if node.left is None and node.right is None:
                return None
            # Case 2: One child
            elif node.left is None:
                return node.right
            elif node.right is None:
                return node.left
            # Case 3: Two children - find inorder successor
            else:
                successor = self._find_min(node.right)
                node.data = successor.data
                node.right = self._delete(node.right, successor.data)

        return node

    def _find_min(self, node: TreeNode) -> TreeNode:
        while node.left:
            node = node.left
        return node

    def find_min(self) -> Optional[Any]:
        if not self.root:
            return None
        return self._find_min(self.root).data

    def find_max(self) -> Optional[Any]:
        if not self.root:
            return None
        node = self.root
        while node.right:
            node = node.right
        return node.data
```

---

## Self-Balancing Trees

### AVL Tree

A BST that maintains height balance (|height(left) - height(right)| <= 1).

```
Rotations:

Left Rotation (LL):      Right Rotation (RR):
     z                        z
    / \                      / \
   y   T4      →           T1   y
  / \                          / \
 T1  T2                      T2   T4

Left-Right (LR):        Right-Left (RL):
     z                        z
    / \                      / \
   y   T4      →          T1   y
  / \                          / \
 T1   x                      x   T4
     / \                    / \
    T2  T3                T2  T3
```

```python
class AVLNode:
    def __init__(self, data: Any):
        self.data = data
        self.left: Optional['AVLNode'] = None
        self.right: Optional['AVLNode'] = None
        self.height: int = 1

class AVLTree:
    def _height(self, node: Optional[AVLNode]) -> int:
        return node.height if node else 0

    def _update_height(self, node: AVLNode) -> None:
        node.height = 1 + max(self._height(node.left), self._height(node.right))

    def _balance_factor(self, node: AVLNode) -> int:
        return self._height(node.left) - self._height(node.right)

    def _rotate_right(self, y: AVLNode) -> AVLNode:
        x = y.left
        T2 = x.right
        x.right = y
        y.left = T2
        self._update_height(y)
        self._update_height(x)
        return x

    def _rotate_left(self, x: AVLNode) -> AVLNode:
        y = x.right
        T2 = y.left
        y.left = x
        x.right = T2
        self._update_height(x)
        self._update_height(y)
        return y

    def _rebalance(self, node: AVLNode) -> AVLNode:
        self._update_height(node)
        balance = self._balance_factor(node)

        # Left heavy
        if balance > 1:
            if self._balance_factor(node.left) < 0:
                node.left = self._rotate_left(node.left)
            return self._rotate_right(node)

        # Right heavy
        if balance < -1:
            if self._balance_factor(node.right) > 0:
                node.right = self._rotate_right(node.right)
            return self._rotate_left(node)

        return node

    def insert(self, root: Optional[AVLNode], data: Any) -> AVLNode:
        if not root:
            return AVLNode(data)
        if data < root.data:
            root.left = self.insert(root.left, data)
        elif data > root.data:
            root.right = self.insert(root.right, data)
        else:
            return root
        return self._rebalance(root)
```

### Red-Black Tree

A BST with node coloring (red/black) ensuring no long paths.

```
Red-Black Properties:
1. Every node is either red or black
2. Root is black
3. All leaves (NIL) are black
4. Red node cannot have red children
5. All paths from node to leaves have same black count

        ┌───┐
        │13B│  ← B = Black, R = Red
        └───┘
       /     \
    ┌───┐   ┌───┐
    │ 8R│   │17R│
    └───┘   └───┘
   /     \       \
┌───┐   ┌───┐   ┌───┐
│ 1B│   │11R│   │25B│
└───┘   └───┘   └───┘
       /     \
    ┌───┐   ┌───┐
    │ 6R│   │15B│
    └───┘   └───┘
```

---

## B-Tree

A self-balancing tree optimized for disk I/O. Each node can have multiple keys and children.

```
B-Tree of order 3 (2-3 Tree):
                ┌─────────────────┐
                │   10  |  20    │
                └───────┬─────────┘
          ┌─────────────┼─────────────┐
    ┌─────┴─────┐ ┌─────┴─────┐ ┌─────┴─────┐
    │ 3  |  7   │ │ 12 | 15   │ │ 22 | 25   │
    └───────────┘ └───────────┘ └───────────┘
```

```python
class BTreeNode:
    def __init__(self, leaf: bool = False):
        self.keys: List[Any] = []
        self.children: List['BTreeNode'] = []
        self.leaf = leaf

class BTree:
    def __init__(self, t: int):
        """t = minimum degree"""
        self.root = BTreeNode(True)
        self.t = t

    def search(self, node: BTreeNode, key: Any) -> Optional[tuple]:
        i = 0
        while i < len(node.keys) and key > node.keys[i]:
            i += 1
        if i < len(node.keys) and key == node.keys[i]:
            return (node, i)
        if node.leaf:
            return None
        return self.search(node.children[i], key)

    def insert(self, key: Any) -> None:
        root = self.root
        if len(root.keys) == 2 * self.t - 1:
            new_root = BTreeNode()
            new_root.children.append(root)
            self._split_child(new_root, 0)
            self.root = new_root
        self._insert_non_full(self.root, key)

    def _insert_non_full(self, node: BTreeNode, key: Any) -> None:
        i = len(node.keys) - 1
        if node.leaf:
            node.keys.append(None)
            while i >= 0 and key < node.keys[i]:
                node.keys[i + 1] = node.keys[i]
                i -= 1
            node.keys[i + 1] = key
        else:
            while i >= 0 and key < node.keys[i]:
                i -= 1
            i += 1
            if len(node.children[i].keys) == 2 * self.t - 1:
                self._split_child(node, i)
                if key > node.keys[i]:
                    i += 1
            self._insert_non_full(node.children[i], key)

    def _split_child(self, parent: BTreeNode, index: int) -> None:
        t = self.t
        node = parent.children[index]
        new_node = BTreeNode(node.leaf)

        parent.children.insert(index + 1, new_node)
        parent.keys.insert(index, node.keys[t - 1])

        new_node.keys = node.keys[t:]
        node.keys = node.keys[:t - 1]

        if not node.leaf:
            new_node.children = node.children[t:]
            node.children = node.children[:t]
```

---

## Trie

A tree-like data structure for storing strings, where each node represents a character.

```
Trie for words: "cat", "car", "dog", "dot"

         ┌─────┐
         │     │
         └──┬──┘
        /       \
    ┌───┐       ┌───┐
    │ c │       │ d │
    └─┬─┘       └─┬─┘
    /   \         /   \
┌───┐ ┌───┐   ┌───┐ ┌───┐
│ a │ │   │   │ o │ │   │
└─┬─┘ └───┘   └─┬─┘ └───┘
  │             /   \
┌───┐        ┌───┐ ┌───┐
│ t │        │ g │ │ t │
│ ★ │        │ ★ │ │ ★ │
└───┘        └───┘ └───┘
```

```python
class TrieNode:
    def __init__(self):
        self.children: Dict[str, 'TrieNode'] = {}
        self.is_end: bool = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True

    def search(self, word: str) -> bool:
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end

    def starts_with(self, prefix: str) -> bool:
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True

    def autocomplete(self, prefix: str) -> List[str]:
        node = self.root
        for char in prefix:
            if char not in node.children:
                return []
            node = node.children[char]

        results = []
        self._collect_words(node, prefix, results)
        return results

    def _collect_words(self, node: TrieNode, current: str, results: List[str]) -> None:
        if node.is_end:
            results.append(current)
        for char, child in node.children.items():
            self._collect_words(child, current + char, results)

# Usage
trie = Trie()
for word in ["cat", "car", "card", "care", "dog", "dot"]:
    trie.insert(word)

print(trie.search("car"))       # True
print(trie.search("ca"))        # False
print(trie.starts_with("ca"))   # True
print(trie.autocomplete("ca"))  # ['cat', 'car', 'card', 'care']
```

---

## Tree Traversals

### Depth-First Traversals

```
Binary Tree:
           ┌─────────┐
           │    1    │
           └────┬────┘
         ┌──────┴──────┐
    ┌────┴────┐   ┌────┴────┐
    │    2    │   │    3    │
    └────┬────┘   └────┬────┘
    ┌────┴────┐        │
┌───┴───┐ ┌───┴───┐ ┌───┴───┐
│   4   │ │   5   │ │   6   │
└───────┘ └───────┘ └───────┘
```

```python
def inorder(node: Optional[TreeNode]) -> List[Any]:
    """Left → Root → Right"""
    if node is None:
        return []
    return inorder(node.left) + [node.data] + inorder(node.right)
# Result: [4, 2, 5, 1, 6, 3]

def preorder(node: Optional[TreeNode]) -> List[Any]:
    """Root → Left → Right"""
    if node is None:
        return []
    return [node.data] + preorder(node.left) + preorder(node.right)
# Result: [1, 2, 4, 5, 3, 6]

def postorder(node: Optional[TreeNode]) -> List[Any]:
    """Left → Right → Root"""
    if node is None:
        return []
    return postorder(node.left) + postorder(node.right) + [node.data]
# Result: [4, 5, 2, 6, 3, 1]
```

### Breadth-First (Level-Order) Traversal

```python
from collections import deque

def level_order(root: Optional[TreeNode]) -> List[List[Any]]:
    if not root:
        return []
    result = []
    queue = deque([root])
    while queue:
        level = []
        for _ in range(len(queue)):
            node = queue.popleft()
            level.append(node.data)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.append(level)
    return result
# Result: [[1], [2, 3], [4, 5, 6]]
```

### Morris Traversal (O(1) Space)

```python
def morris_inorder(root: TreeNode) -> List[Any]:
    """Inorder traversal with O(1) space."""
    result = []
    current = root

    while current:
        if current.left is None:
            result.append(current.data)
            current = current.right
        else:
            predecessor = current.left
            while predecessor.right and predecessor.right != current:
                predecessor = predecessor.right

            if predecessor.right is None:
                predecessor.right = current
                current = current.left
            else:
                predecessor.right = None
                result.append(current.data)
                current = current.right

    return result
```

---

## Time Complexity

| Operation | BST (Avg) | BST (Worst) | AVL | Red-Black | B-Tree |
|-----------|-----------|-------------|-----|-----------|--------|
| Search | O(log n) | O(n) | O(log n) | O(log n) | O(log n) |
| Insert | O(log n) | O(n) | O(log n) | O(log n) | O(log n) |
| Delete | O(log n) | O(n) | O(log n) | O(log n) | O(log n) |
| Space | O(n) | O(n) | O(n) | O(n) | O(n) |

---

## Use Cases

| Data Structure | Use Case |
|----------------|----------|
| BST | In-memory sorted data |
| AVL | When strict balancing needed |
| Red-Black | General purpose (C++ map, Java TreeMap) |
| B-Tree | Database indexing, file systems |
| Trie | Autocomplete, spell checkers |
| Heap | Priority queues |
| Segment Tree | Range queries |
| Fenwick Tree | Prefix sums |
