# Data Structures Quiz

## Questions

1. What are the advantages of linked lists over arrays?
2. What is a stack and its LIFO principle?
3. What is a circular queue?
4. What is a hash table?
5. What is a binary search tree?
6. What is the time complexity of searching in a BST?
7. What is separate chaining?
8. What is a graph?
9. When would you use a linked list over an array?
10. What is cache locality?
11. What is an AVL tree and why is it useful?
12. What is the difference between a priority queue and a regular queue?
13. What is a trie and what is it used for?
14. What is open addressing in hash tables?
15. What is the difference between a directed and undirected graph?

## Answers

1. Dynamic size, efficient insertion/deletion at head
2. Last In, First Out - like a stack of plates
3. A queue where the last position connects back to the first
4. A data structure mapping keys to values using a hash function
5. A binary tree where left < root < right
6. O(log n) average, O(n) worst case
7. Using linked lists to handle hash collisions
8. A collection of vertices connected by edges
9. When you need frequent insertions/deletions
10. Data stored close in memory improves cache performance
11. A self-balancing BST where the height difference between left and right subtrees is at most 1; guarantees O(log n) operations
12. Priority queue serves elements by priority (not insertion order); regular queue is FIFO
13. A tree-like data structure for storing strings with shared prefixes; used in autocomplete, spell checkers, and routing tables
14. Collisions are resolved by finding another slot in the hash table (linear probing, quadratic probing, double hashing)
15. Directed graph has ordered edges (A→B ≠ B→A); undirected graph has unordered edges (A-B implies both directions)
