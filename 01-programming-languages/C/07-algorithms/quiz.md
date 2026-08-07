# Algorithms Quiz

## Questions

1. What is the difference between O(n) and O(log n)?
2. What is the time complexity of binary search?
3. What is dynamic programming?
4. What is the difference between BFS and DFS?
5. What is a greedy algorithm?
6. What is the difference between stable and unstable sort?
7. What is tail recursion?
8. What is memoization?
9. What is the difference between best, average, and worst case?
10. What is Big O notation?
11. What is the time complexity of quicksort in the average and worst case?
12. What is the difference between recursion and iteration?
13. What is a divide-and-conquer algorithm?
14. What is in-place sorting and why does it matter?
15. What is the difference between Θ (Theta) and O (Big O) notation?

## Answers

1. O(n) is linear, O(log n) is logarithmic (faster for large n)
2. O(log n)
3. Solving problems by breaking into overlapping subproblems
4. BFS: level-order, uses queue; DFS: depth-first, uses stack/recursion
5. Making locally optimal choices for global optimum
6. Stable preserves equal elements' order; unstable doesn't
7. Recursive call as last operation (can be optimized)
8. Storing results of expensive function calls
9. Best: minimum time; Average: expected; Worst: maximum time
10. Upper bound of algorithm growth rate
11. Average: O(n log n); Worst: O(n²) — occurs with bad pivot selection
12. Recursion uses function calls (stack); iteration uses loops; recursion can cause stack overflow for deep call chains
13. Algorithm that breaks problem into subproblems, solves them independently, and combines results (e.g., merge sort, quicksort)
14. Sorts data without requiring extra memory proportional to input; important for memory-constrained environments
15. Θ (tight bound) = exact growth rate; O (upper bound) = worst-case or equal growth rate
