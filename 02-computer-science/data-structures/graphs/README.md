# Graphs

## Table of Contents

- [Overview](#overview)
- [Graph Representations](#graph-representations)
  - [Adjacency Matrix](#adjacency-matrix)
  - [Adjacency List](#adjacency-list)
- [Graph Types](#graph-types)
- [Graph Algorithms](#graph-algorithms)
  - [BFS](#bfs)
  - [DFS](#dfs)
  - [Topological Sort](#topological-sort)
  - [Shortest Path](#shortest-path)
  - [Minimum Spanning Tree](#minimum-spanning-tree)
- [Time Complexity](#time-complexity)
- [Use Cases](#use-cases)

---

## Overview

A graph is a non-linear data structure consisting of vertices (nodes) and edges (connections).

```
Undirected Graph:          Directed Graph:
    A ─────── B               A ───────► B
    │         │               │           │
    │         │               │           │
    C ─────── D               ▼           ▼
                              C ◄────── D

Weighted Graph:            Cyclic Graph:
    A ───5─── B               A ───► B
    │         │               ▲       │
    3         2               │       ▼
    │         │               D ◄─── C
    C ───1─── D
```

### Key Terms

| Term | Definition |
|------|------------|
| Vertex (Node) | Point in the graph |
| Edge | Connection between vertices |
| Degree | Number of edges to a vertex |
| Path | Sequence of connected vertices |
| Cycle | Path that starts and ends at same vertex |
| Connected | Path exists between all pairs |
| Weighted | Edges have associated costs |

---

## Graph Representations

### Adjacency Matrix

2D array where `matrix[i][j]` indicates edge between vertices i and j.

```
Graph:        Adjacency Matrix:
A ── B        A B C D
│  ╲ │        A 0 1 1 0
│   ╲│        B 1 0 1 1
C ── D        C 1 1 0 1
               D 0 1 1 0
```

```python
from typing import List, Optional

class AdjacencyMatrix:
    def __init__(self, num_vertices: int, directed: bool = False):
        self.num_vertices = num_vertices
        self.directed = directed
        self.matrix: List[List[int]] = [
            [0] * num_vertices for _ in range(num_vertices)
        ]

    def add_edge(self, u: int, v: int, weight: int = 1) -> None:
        self.matrix[u][v] = weight
        if not self.directed:
            self.matrix[v][u] = weight

    def remove_edge(self, u: int, v: int) -> None:
        self.matrix[u][v] = 0
        if not self.directed:
            self.matrix[v][u] = 0

    def has_edge(self, u: int, v: int) -> bool:
        return self.matrix[u][v] != 0

    def get_neighbors(self, vertex: int) -> List[int]:
        neighbors = []
        for v in range(self.num_vertices):
            if self.matrix[vertex][v] != 0:
                neighbors.append(v)
        return neighbors

    def get_edge_weight(self, u: int, v: int) -> int:
        return self.matrix[u][v]

    def display(self) -> None:
        for row in self.matrix:
            print(row)

# Usage
graph = AdjacencyMatrix(4)
graph.add_edge(0, 1)
graph.add_edge(0, 2)
graph.add_edge(1, 3)
graph.display()
# [0, 1, 1, 0]
# [1, 0, 0, 1]
# [1, 0, 0, 0]
# [0, 1, 0, 0]
```

### Adjacency List

Dictionary/list mapping each vertex to its neighbors.

```
Graph:        Adjacency List:
A ── B        A: [B, C]
│  ╲ │        B: [A, D]
C ── D        C: [A, D]
               D: [B, C]
```

```python
from typing import Dict, List, Optional
from collections import deque

class AdjacencyList:
    def __init__(self, directed: bool = False):
        self.graph: Dict[int, List[int]] = {}
        self.directed = directed

    def add_vertex(self, vertex: int) -> None:
        if vertex not in self.graph:
            self.graph[vertex] = []

    def add_edge(self, u: int, v: int) -> None:
        self.add_vertex(u)
        self.add_vertex(v)
        self.graph[u].append(v)
        if not self.directed:
            self.graph[v].append(u)

    def remove_edge(self, u: int, v: int) -> None:
        if u in self.graph:
            self.graph[u] = [x for x in self.graph[u] if x != v]
        if not self.directed and v in self.graph:
            self.graph[v] = [x for x in self.graph[v] if x != u]

    def get_neighbors(self, vertex: int) -> List[int]:
        return self.graph.get(vertex, [])

    def has_edge(self, u: int, v: int) -> bool:
        return v in self.graph.get(u, [])

    def display(self) -> None:
        for vertex in self.graph:
            print(f"{vertex}: {self.graph[vertex]}")

# Usage
graph = AdjacencyList()
graph.add_edge(0, 1)
graph.add_edge(0, 2)
graph.add_edge(1, 3)
graph.display()
# 0: [1, 2]
# 1: [0, 3]
# 2: [0]
# 3: [1]
```

### Comparison

| Feature | Adjacency Matrix | Adjacency List |
|---------|------------------|----------------|
| Space | O(V²) | O(V + E) |
| Add edge | O(1) | O(1) |
| Remove edge | O(1) | O(E) |
| Check edge | O(1) | O(degree) |
| Get neighbors | O(V) | O(degree) |
| Best for | Dense graphs | Sparse graphs |

---

## Graph Types

| Type | Description |
|------|-------------|
| Directed | Edges have direction |
| Undirected | Edges are bidirectional |
| Weighted | Edges have weights |
| Unweighted | All edges equal |
| Cyclic | Contains at least one cycle |
| Acyclic | No cycles (DAG) |
| Connected | Path between all pairs |
| Disconnected | Some vertices unreachable |
| Complete | Edge between every pair |
| Bipartite | Vertices split into two sets |

---

## Graph Algorithms

### BFS

Level-by-level exploration using a queue.

```python
from collections import deque
from typing import Dict, List, Set

def bfs(graph: Dict[int, List[int]], start: int) -> List[int]:
    """Breadth-First Search traversal."""
    visited: Set[int] = {start}
    queue = deque([start])
    result = []

    while queue:
        vertex = queue.popleft()
        result.append(vertex)

        for neighbor in graph[vertex]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

    return result

def bfs_shortest_path(graph: Dict[int, List[int]], start: int, end: int) -> List[int]:
    """Find shortest path in unweighted graph."""
    if start == end:
        return [start]

    visited = {start}
    queue = deque([(start, [start])])

    while queue:
        vertex, path = queue.popleft()
        for neighbor in graph[vertex]:
            if neighbor not in visited:
                new_path = path + [neighbor]
                if neighbor == end:
                    return new_path
                visited.add(neighbor)
                queue.append((neighbor, new_path))

    return []  # No path found

# Example
graph = {
    0: [1, 2],
    1: [0, 3, 4],
    2: [0, 4],
    3: [1, 4],
    4: [1, 2, 3]
}
print(bfs(graph, 0))  # [0, 1, 2, 3, 4]
print(bfs_shortest_path(graph, 0, 4))  # [0, 1, 4]
```

### DFS

Explore as far as possible along each branch before backtracking.

```python
from typing import Dict, List, Set

def dfs_recursive(graph: Dict[int, List[int]], vertex: int, visited: Set[int]) -> List[int]:
    """Recursive DFS."""
    visited.add(vertex)
    result = [vertex]

    for neighbor in graph[vertex]:
        if neighbor not in visited:
            result.extend(dfs_recursive(graph, neighbor, visited))

    return result

def dfs_iterative(graph: Dict[int, List[int]], start: int) -> List[int]:
    """Iterative DFS using stack."""
    visited: Set[int] = set()
    stack = [start]
    result = []

    while stack:
        vertex = stack.pop()
        if vertex not in visited:
            visited.add(vertex)
            result.append(vertex)
            for neighbor in reversed(graph[vertex]):
                if neighbor not in visited:
                    stack.append(neighbor)

    return result

def dfs_all_paths(graph: Dict[int, List[int]], start: int, end: int) -> List[List[int]]:
    """Find all paths from start to end."""
    all_paths = []

    def dfs(current: int, path: List[int]) -> None:
        if current == end:
            all_paths.append(path[:])
            return
        for neighbor in graph[current]:
            if neighbor not in path:
                path.append(neighbor)
                dfs(neighbor, path)
                path.pop()

    dfs(start, [start])
    return all_paths

# Example
print(dfs_iterative(graph, 0))  # [0, 1, 3, 4, 2]
print(dfs_all_paths(graph, 0, 4))  # [[0, 1, 4], [0, 1, 3, 4], [0, 2, 4], [0, 2, 1, 3, 4]]
```

### Topological Sort

Linear ordering of vertices in a DAG such that for every directed edge (u, v), u comes before v.

```python
from collections import deque
from typing import Dict, List

def topological_sort_kahn(graph: Dict[int, List[int]]) -> List[int]:
    """Kahn's algorithm for topological sort."""
    in_degree = {v: 0 for v in graph}
    for v in graph:
        for neighbor in graph[v]:
            in_degree[neighbor] = in_degree.get(neighbor, 0) + 1

    queue = deque([v for v in graph if in_degree[v] == 0])
    result = []

    while queue:
        vertex = queue.popleft()
        result.append(vertex)
        for neighbor in graph[vertex]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)

    if len(result) != len(graph):
        raise ValueError("Graph has a cycle!")

    return result

def topological_sort_dfs(graph: Dict[int, List[int]]) -> List[int]:
    """DFS-based topological sort."""
    visited = set()
    result = []

    def dfs(vertex: int) -> None:
        visited.add(vertex)
        for neighbor in graph[vertex]:
            if neighbor not in visited:
                dfs(neighbor)
        result.append(vertex)

    for vertex in graph:
        if vertex not in visited:
            dfs(vertex)

    return result[::-1]

# Example: Course prerequisites
# 0 → 1 → 3
# 0 → 2 → 3
prerequisites = {0: [1, 2], 1: [3], 2: [3], 3: []}
print(topological_sort_kahn(prerequisites))  # [0, 1, 2, 3] or [0, 2, 1, 3]
```

### Shortest Path

#### Dijkstra's Algorithm

```python
import heapq
from typing import Dict, List, Tuple

def dijkstra(graph: Dict[int, List[Tuple[int, int]]], start: int) -> Dict[int, int]:
    """Dijkstra's shortest path algorithm."""
    distances = {v: float('inf') for v in graph}
    distances[start] = 0
    pq = [(0, start)]

    while pq:
        current_dist, vertex = heapq.heappop(pq)
        if current_dist > distances[vertex]:
            continue
        for neighbor, weight in graph[vertex]:
            distance = current_dist + weight
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                heapq.heappush(pq, (distance, neighbor))

    return distances

# Weighted graph as adjacency list: {vertex: [(neighbor, weight), ...]}
graph = {
    0: [(1, 4), (2, 1)],
    1: [(3, 1)],
    2: [(1, 2), (3, 5)],
    3: []
}
print(dijkstra(graph, 0))  # {0: 0, 1: 3, 2: 1, 3: 4}
```

#### Bellman-Ford Algorithm

```python
def bellman_ford(edges: List[Tuple[int, int, int]], num_vertices: int, start: int) -> List[int]:
    """Bellman-Ford algorithm (handles negative weights)."""
    distances = [float('inf')] * num_vertices
    distances[start] = 0

    for _ in range(num_vertices - 1):
        for u, v, w in edges:
            if distances[u] + w < distances[v]:
                distances[v] = distances[u] + w

    # Check for negative cycles
    for u, v, w in edges:
        if distances[u] + w < distances[v]:
            raise ValueError("Graph contains negative cycle")

    return distances

# edges: [(from, to, weight), ...]
edges = [(0, 1, 4), (0, 2, 1), (2, 1, 2), (1, 3, 1), (2, 3, 5)]
print(bellman_ford(edges, 4, 0))  # [0, 3, 1, 4]
```

#### Floyd-Warshall Algorithm

```python
def floyd_warshall(num_vertices: int, edges: List[Tuple[int, int, int]]) -> List[List[int]]:
    """All-pairs shortest paths."""
    dist = [[float('inf')] * num_vertices for _ in range(num_vertices)]

    for i in range(num_vertices):
        dist[i][i] = 0
    for u, v, w in edges:
        dist[u][v] = w

    for k in range(num_vertices):
        for i in range(num_vertices):
            for j in range(num_vertices):
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

    return dist

edges = [(0, 1, 4), (0, 2, 1), (2, 1, 2), (1, 3, 1)]
result = floyd_warshall(4, edges)
for row in result:
    print(row)
# [0, 3, 1, 4]
# [inf, 0, inf, 1]
# [inf, 2, 0, 3]
# [inf, inf, inf, 0]
```

### Minimum Spanning Tree

#### Prim's Algorithm

```python
import heapq
from typing import Dict, List, Tuple

def prim(graph: Dict[int, List[Tuple[int, int]]]) -> List[Tuple[int, int, int]]:
    """Prim's MST algorithm."""
    if not graph:
        return []

    mst = []
    visited = {next(iter(graph))}
    edges = [(weight, start, end) for start in graph for end, weight in graph[start]]
    heapq.heapify(edges)

    while edges and len(visited) < len(graph):
        weight, u, v = heapq.heappop(edges)
        if v not in visited:
            visited.add(v)
            mst.append((u, v, weight))
            for next_v, next_w in graph[v]:
                if next_v not in visited:
                    heapq.heappush(edges, (next_w, v, next_v))

    return mst

# Example
graph = {
    0: [(1, 4), (2, 1)],
    1: [(0, 4), (2, 2), (3, 1)],
    2: [(0, 1), (1, 2), (3, 5)],
    3: [(1, 1), (2, 5)]
}
print(prim(graph))  # [(0, 2, 1), (1, 3, 1), (2, 1, 2)]
```

#### Kruskal's Algorithm

```python
class UnionFind:
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x: int, y: int) -> bool:
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True

def kruskal(num_vertices: int, edges: List[Tuple[int, int, int]]) -> List[Tuple[int, int, int]]:
    """Kruskal's MST algorithm."""
    edges.sort(key=lambda x: x[2])  # Sort by weight
    uf = UnionFind(num_vertices)
    mst = []

    for u, v, weight in edges:
        if uf.union(u, v):
            mst.append((u, v, weight))
            if len(mst) == num_vertices - 1:
                break

    return mst

edges = [(0, 1, 4), (0, 2, 1), (1, 2, 2), (1, 3, 1), (2, 3, 5)]
print(kruskal(4, edges))  # [(0, 2, 1), (1, 3, 1), (1, 2, 2)]
```

---

## Time Complexity

| Algorithm | Time Complexity | Space |
|-----------|-----------------|-------|
| BFS | O(V + E) | O(V) |
| DFS | O(V + E) | O(V) |
| Topological Sort | O(V + E) | O(V) |
| Dijkstra | O((V + E) log V) | O(V) |
| Bellman-Ford | O(V × E) | O(V) |
| Floyd-Warshall | O(V³) | O(V²) |
| Prim | O((V + E) log V) | O(V) |
| Kruskal | O(E log E) | O(V) |

---

## Use Cases

| Use Case | Algorithm |
|----------|-----------|
| Social networks | BFS/DFS for friend suggestions |
| Web crawling | BFS for breadth-first crawling |
| Dependency resolution | Topological sort |
| GPS navigation | Dijkstra's shortest path |
| Network routing | Bellman-Ford |
| Clustering | Kruskal's MST |
| Recommendation systems | Graph traversal |
| Circuit design | Topological sort |
