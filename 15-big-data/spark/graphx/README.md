# Spark GraphX

GraphX is Spark's API for graph computation and graph-parallel processing. It extends Spark RDDs by introducing the Resilient Distributed Property Graph, a directed multigraph with properties attached to each vertex and edge. GraphX provides a unified framework for both graph-parallel and data-parallel computations.

## Table of Contents

1. [GraphX Overview](#graphx-overview)
2. [Property Graphs](#property-graphs)
3. [Graph Operations](#graph-operations)
4. [Pregel API](#pregel-api)
5. [Graph Algorithms](#graph-algorithms)
6. [Graph Constructors](#graph-constructors)
7. [Advanced Features](#advanced-features)
8. [Performance Optimization](#performance-optimization)
9. [Best Practices](#best-practices)
10. [Common Patterns](#common-patterns)

---

## GraphX Overview

### What is GraphX?

GraphX is a graph processing framework built on Spark that extends the RDD abstraction to introduce the Resilient Distributed Property Graph: a directed multigraph with user-defined objects attached to each vertex and edge.

### GraphX Architecture

```
GraphX Architecture:
┌─────────────────────────────────────────────────────────────┐
│                      GraphX API                              │
│         (Graph, VertexRDD, EdgeRDD)                        │
├─────────────────────────────────────────────────────────────┤
│                      Graph Algorithms                        │
│         (PageRank, Connected Components, etc.)             │
├─────────────────────────────────────────────────────────────┤
│                      Pregel API                              │
│         (Iterative Graph-Parallel Computation)             │
├─────────────────────────────────────────────────────────────┤
│                      Spark Core                              │
│         (RDD, Distributed Processing)                       │
└─────────────────────────────────────────────────────────────┘
```

### GraphX vs Other Graph Systems

| Feature | GraphX | Neo4j | Giraph | GraphFrames |
|---------|--------|-------|--------|-------------|
| **Integration** | Native Spark | Standalone | Hadoop | Spark SQL |
| **API** | RDD-based | Cypher | BSP | DataFrame |
| **Fault Tolerance** | Yes | No | Yes | Yes |
| **Language** | Scala, Python | Multiple | Java | Python, Scala |
| **Use Case** | Analytics | OLTP | Large-scale | Graph queries |

---

## Property Graphs

### Creating Property Graphs

```python
from pyspark.sql import SparkSession
from pyspark.graphframes import GraphFrame

# Create Spark session
spark = SparkSession.builder \
    .appName("GraphX Example") \
    .getOrCreate()

# Create vertices DataFrame
vertices = spark.createDataFrame([
    (1, "Alice", 28),
    (2, "Bob", 32),
    (3, "Charlie", 25),
    (4, "David", 30),
    (5, "Eve", 35)
], ["id", "name", "age"])

# Create edges DataFrame
edges = spark.createDataFrame([
    (1, 2, "friend"),
    (2, 3, "colleague"),
    (3, 4, "friend"),
    (4, 5, "family"),
    (5, 1, "friend")
], ["src", "dst", "relationship"])

# Create GraphFrame
graph = GraphFrame(vertices, edges)
```

### Graph Properties

```python
# Get vertices
graph.vertices.show()

# Get edges
graph.edges.show()

# Get in-degree
graph.inDegrees.show()

# Get out-degree
graph.outDegrees.show()

# Get degree
graph.degrees.show()

# Get number of vertices
num_vertices = graph.vertices.count()

# Get number of edges
num_edges = graph.edges.count()
```

### Graph Transformations

```python
# Filter vertices
filtered_vertices = graph.vertices.filter("age > 25")

# Filter edges
filtered_edges = graph.edges.filter("relationship = 'friend'")

# Create subgraph
subgraph = graph.subgraph(
    edgeFilter=lambda e: e["relationship"] == "friend"
)

# Reverse edges
reversed_graph = graph.reverse()

# Union graphs
graph1 = GraphFrame(vertices1, edges1)
graph2 = GraphFrame(vertices2, edges2)
union_graph = graph1.unionVertices(graph2)
```

---

## Graph Operations

### Vertex Operations

```python
# Map vertices
mapped_vertices = graph.vertices.withColumn(
    "age_group",
    when(col("age") < 30, "Young")
    .when(col("age") < 40, "Middle")
    .otherwise("Senior")
)

# Join vertices
joined_vertices = graph.vertices.join(
    other_vertices,
    graph.vertices.id == other_vertices.id
)

# Aggregate vertices
aggregated = graph.vertices.groupBy("age_group").count()

# Get vertex attributes
vertex_attrs = graph.vertices.select("id", "name", "age")
```

### Edge Operations

```python
# Map edges
mapped_edges = graph.edges.withColumn(
    "weight",
    when(col("relationship") == "friend", 1.0)
    .when(col("relationship") == "colleague", 0.8)
    .otherwise(0.5)
)

# Filter edges
friend_edges = graph.edges.filter("relationship = 'friend'")

# Aggregate edges
edge_counts = graph.edges.groupBy("src").count()

# Get edge attributes
edge_attrs = graph.edges.select("src", "dst", "relationship")
```

### Aggregate Operations

```python
# Aggregate messages
from pyspark.sql.functions import collect_list, collect_set, count

# Aggregate to vertices
agg_messages = graph.aggregateMessages(
    sendToDst=lambda msg: msg.srcVertex.name,
    sendToSrc=lambda msg: msg.dstVertex.name
)

# Collect messages
collected = graph.aggregateMessages(
    sendToDst=lambda msg: collect_list(msg.srcVertex.name),
    sendToSrc=lambda msg: collect_list(msg.dstVertex.name)
)
```

---

## Pregel API

### What is Pregel?

Pregel is a graph-parallel processing model that performs iterative computations on graphs. GraphX implements Pregel through the `pregel` method on graphs.

### Pregel Algorithm

```python
from pyspark.sql.functions import col, when, lit

# PageRank example using Pregel
def pagerank(graph, num_iterations=10, damping_factor=0.85):
    # Initialize ranks
    ranks = graph.vertices.withColumn("rank", lit(1.0))
    
    for i in range(num_iterations):
        # Send messages
        messages = graph.aggregateMessages(
            sendToDst=lambda msg: msg.srcVertex["rank"] / msg.srcVertex["outDegree"],
            sendToSrc=lambda msg: msg.dstVertex["rank"] / msg.dstVertex["outDegree"]
        )
        
        # Update ranks
        ranks = messages.withColumn(
            "rank",
            (1 - damping_factor) + damping_factor * col("message")
        )
    
    return ranks

# Run PageRank
pagerank_result = pagerank(graph)
```

### Pregel Implementation

```python
def pregel_example(graph, max_iterations):
    # Initialize vertex attributes
    vertices = graph.vertices.withColumn("value", lit(0))
    
    for i in range(max_iterations):
        # Send messages
        messages = graph.aggregateMessages(
            sendToDst=lambda msg: msg.srcVertex["value"] + 1,
            sendToSrc=lambda msg: msg.dstVertex["value"] + 1
        )
        
        # Update vertices
        vertices = vertices.join(
            messages,
            vertices.id == messages.dstId,
            "left_outer"
        ).withColumn(
            "value",
            when(col("message").isNotNull(), col("message"))
            .otherwise(col("value"))
        )
        
        # Check convergence
        if messages.count() == 0:
            break
    
    return vertices
```

### Pregel Operations

```python
# Send messages
def send_message(msg):
    return msg.srcVertex["value"] + 1

# Receive messages
def receive_message(vertex, message):
    return vertex["value"] + message

# Run Pregel
result = graph.pregel(
    maxIterations=10,
    activeDirection="either",
    sendMessage=send_message,
    mergeMessage=receive_message
)
```

---

## Graph Algorithms

### PageRank

```python
from graphframes import GraphFrame

# Run PageRank
pagerank = graph.pageRank(resetProbability=0.15, maxIter=10)

# Get vertices with PageRank
pagerank_vertices = pagerank.vertices.select("id", "pagerank")

# Get edges with weights
pagerank_edges = pagerank.edges
```

### Connected Components

```python
# Find connected components
connected_components = graph.connectedComponents()

# Get component IDs
components = connected_components.vertices.select("id", "component")

# Count components
num_components = components.select("component").distinct().count()
```

### Strongly Connected Components

```python
# Find strongly connected components
scc = graph.stronglyConnectedComponents(maxIter=10)

# Get component IDs
scc_components = scc.vertices.select("id", "component")
```

### Triangle Count

```python
# Count triangles
triangle_count = graph.triangleCount()

# Get triangle counts
triangle_counts = triangle_count.vertices.select("id", "count")
```

### Label Propagation

```python
# Run label propagation
lpa = graph.labelPropagation(maxIter=5)

# Get labels
labels = lpa.vertices.select("id", "label")
```

### Breadth-First Search

```python
from pyspark.sql.functions import lit, when

def bfs(graph, source_id, max_depth):
    # Initialize distances
    distances = graph.vertices.withColumn(
        "distance",
        when(col("id") == source_id, lit(0)).otherwise(lit(float("inf")))
    )
    
    for depth in range(max_depth):
        # Send messages to neighbors
        messages = graph.aggregateMessages(
            sendToDst=lambda msg: msg.srcVertex["distance"] + 1,
            sendToSrc=lambda msg: msg.dstVertex["distance"] + 1
        )
        
        # Update distances
        distances = distances.join(
            messages,
            distances.id == messages.dstId,
            "left_outer"
        ).withColumn(
            "distance",
            when(
                col("message").isNotNull() & 
                (col("message") < col("distance")),
                col("message")
            ).otherwise(col("distance"))
        )
    
    return distances

# Run BFS
bfs_result = bfs(graph, source_id=1, max_depth=5)
```

---

## Graph Constructors

### From RDDs

```python
from pyspark.sql import Row

# Create vertices from RDD
vertices_rdd = sc.parallelize([
    Row(id=1, name="Alice", age=28),
    Row(id=2, name="Bob", age=32),
    Row(id=3, name="Charlie", age=25)
])
vertices = spark.createDataFrame(vertices_rdd)

# Create edges from RDD
edges_rdd = sc.parallelize([
    Row(src=1, dst=2, relationship="friend"),
    Row(src=2, dst=3, relationship="colleague"),
    Row(src=3, dst=1, relationship="friend")
])
edges = spark.createDataFrame(edges_rdd)

# Create graph
graph = GraphFrame(vertices, edges)
```

### From Files

```python
# From CSV files
vertices = spark.read.csv("vertices.csv", header=True, inferSchema=True)
edges = spark.read.csv("edges.csv", header=True, inferSchema=True)

graph = GraphFrame(vertices, edges)

# From Parquet files
vertices = spark.read.parquet("vertices.parquet")
edges = spark.read.parquet("edges.parquet")

graph = GraphFrame(vertices, edges)
```

### From adjacency list

```python
# From adjacency list
adjacency_list = [
    (1, [2, 3]),
    (2, [1, 3]),
    (3, [1, 2])
]

vertices = spark.createDataFrame([(1,), (2,), (3,)], ["id"])

edges = []
for src, dsts in adjacency_list:
    for dst in dsts:
        edges.append((src, dst))

edges = spark.createDataFrame(edges, ["src", "dst"])

graph = GraphFrame(vertices, edges)
```

### From Matrix

```python
# From adjacency matrix
import numpy as np

adjacency_matrix = np.array([
    [0, 1, 1],
    [1, 0, 1],
    [1, 1, 0]
])

# Create edges from matrix
edges = []
for i in range(len(adjacency_matrix)):
    for j in range(len(adjacency_matrix[i])):
        if adjacency_matrix[i][j] == 1:
            edges.append((i+1, j+1))

vertices = spark.createDataFrame([(1,), (2,), (3,)], ["id"])
edges = spark.createDataFrame(edges, ["src", "dst"])

graph = GraphFrame(vertices, edges)
```

---

## Advanced Features

### GraphX with RDD API

```python
from pyspark.graphframes import GraphFrame

# Convert to RDD
vertex_rdd = graph.vertices.rdd
edge_rdd = graph.edges.rdd

# Process with RDD operations
processed_vertices = vertex_rdd.map(lambda row: (row.id, row.name))
processed_edges = edge_rdd.map(lambda row: (row.src, row.dst))

# Convert back to GraphFrame
vertices = spark.createDataFrame(processed_vertices, ["id", "name"])
edges = spark.createDataFrame(processed_edges, ["src", "dst"])
graph = GraphFrame(vertices, edges)
```

### Graph Serialization

```python
# Save graph to disk
graph.vertices.write.parquet("vertices.parquet")
graph.edges.write.parquet("edges.parquet")

# Load graph from disk
vertices = spark.read.parquet("vertices.parquet")
edges = spark.read.parquet("edges.parquet")
graph = GraphFrame(vertices, edges)

# Save as JSON
graph.vertices.write.json("vertices.json")
graph.edges.write.json("edges.json")
```

### Graph Visualization

```python
# Export graph for visualization
def export_graph_for_visualization(graph, output_dir):
    # Export vertices
    graph.vertices.write.mode("overwrite").json(f"{output_dir}/vertices")
    
    # Export edges
    graph.edges.write.mode("overwrite").json(f"{output_dir}/edges")
    
    # Create visualization-ready format
    nodes = graph.vertices.select("id", "name").toPandas()
    links = graph.edges.select("src", "dst", "relationship").toPandas()
    
    return {
        "nodes": nodes.to_dict("records"),
        "links": links.to_dict("records")
    }
```

### Graph Partitioning

```python
# Partition graph by vertices
partitioned_graph = graph.partitionBy(numPartitions=4)

# Partition by edges
partitioned_graph = graph.partitionBy(
    numPartitions=4,
    partitionStrategy="edgecut"
)

# Check partitioning
print(f"Number of partitions: {partitioned_graph.vertices.rdd.getNumPartitions()}")
```

---

## Performance Optimization

### Graph Partitioning

```python
# Use appropriate partitioning
graph = graph.partitionBy(numPartitions=10)

# Edge cut partitioning
graph = graph.partitionBy(
    numPartitions=10,
    partitionStrategy="edgecut"
)

# Vertex cut partitioning
graph = graph.partitionBy(
    numPartitions=10,
    partitionStrategy="vertexcut"
)
```

### Caching

```python
# Cache graph
graph.vertices.cache()
graph.edges.cache()

# Check if cached
print(f"Vertices cached: {graph.vertices.is_cached}")
print(f"Edges cached: {graph.edges.is_cached}")

# Unpersist
graph.vertices.unpersist()
graph.edges.unpersist()
```

### Parallelism

```python
# Set parallelism
spark.conf.set("spark.sql.shuffle.partitions", "200")

# Repartition for parallelism
graph = graph.partitionBy(numPartitions=200)

# Use broadcast for small graphs
from pyspark.sql.functions import broadcast

small_graph = graph.subgraph(
    edgeFilter=lambda e: e["relationship"] == "friend"
)
```

### Memory Management

```python
# Use off-heap memory
spark.conf.set("spark.memory.offHeap.enabled", "true")
spark.conf.set("spark.memory.offHeap.size", "1g")

# Configure memory fractions
spark.conf.set("spark.memory.fraction", "0.6")
spark.conf.set("spark.memory.storageFraction", "0.5")
```

---

## Best Practices

### 1. Graph Construction

```python
# Create efficient graph structure
vertices = spark.createDataFrame([
    (1, "Alice", 28),
    (2, "Bob", 32)
], ["id", "name", "age"])

edges = spark.createDataFrame([
    (1, 2, "friend")
], ["src", "dst", "relationship"])

# Use appropriate data types
vertices = vertices.withColumn("age", col("age").cast("integer"))

# Create graph
graph = GraphFrame(vertices, edges)
```

### 2. Algorithm Selection

```python
# Choose appropriate algorithm
# PageRank: For importance ranking
pagerank = graph.pageRank(resetProbability=0.15, maxIter=10)

# Connected Components: For connectivity analysis
components = graph.connectedComponents()

# Triangle Count: For clustering coefficient
triangles = graph.triangleCount()

# Label Propagation: For community detection
labels = graph.labelPropagation(maxIter=5)
```

### 3. Iterative Algorithms

```python
# Use appropriate convergence criteria
def iterative_algorithm(graph, max_iterations, tolerance):
    previous_result = None
    
    for i in range(max_iterations):
        current_result = compute_iteration(graph)
        
        if previous_result is not None:
            # Check convergence
            diff = compute_difference(current_result, previous_result)
            if diff < tolerance:
                break
        
        previous_result = current_result
    
    return current_result
```

### 4. Memory Management

```python
# Cache frequently used graphs
graph.vertices.cache()
graph.edges.cache()

# Use appropriate storage levels
from pyspark import StorageLevel

graph.vertices.persist(StorageLevel.MEMORY_AND_DISK)
graph.edges.persist(StorageLevel.MEMORY_AND_DISK)

# Unpersist when done
graph.vertices.unpersist()
graph.edges.unpersist()
```

### 5. Performance Monitoring

```python
# Monitor graph metrics
num_vertices = graph.vertices.count()
num_edges = graph.edges.count()
avg_degree = (2 * num_edges) / num_vertices

print(f"Vertices: {num_vertices}")
print(f"Edges: {num_edges}")
print(f"Average degree: {avg_degree}")

# Monitor algorithm performance
import time

start_time = time.time()
pagerank = graph.pageRank(resetProbability=0.15, maxIter=10)
end_time = time.time()

print(f"PageRank took {end_time - start_time} seconds")
```

---

## Common Patterns

### Pattern 1: Social Network Analysis

```python
# Analyze social network
# 1. Find influential users
pagerank = graph.pageRank(resetProbability=0.15, maxIter=10)
influential_users = pagerank.vertices.orderBy("pagerank", ascending=False)

# 2. Find communities
communities = graph.connectedComponents()

# 3. Find shortest paths
shortest_paths = graph.bfs(fromExpr="id = 1", toExpr="id = 10")
```

### Pattern 2: Recommendation System

```python
# Build recommendation graph
# 1. Create user-item bipartite graph
user_vertices = spark.createDataFrame(user_ids, ["id"])
item_vertices = spark.createDataFrame(item_ids, ["id"])

user_item_edges = spark.createDataFrame(
    user_item_interactions, ["src", "dst", "rating"]
)

# 2. Run personalized PageRank
personalized_pagerank = graph.pageRank(
    resetProbability=0.15,
    maxIter=10,
    sourceId=1  # User ID
)

# 3. Get recommendations
recommendations = personalized_pagerank.vertices \
    .filter(col("id").isin(item_ids)) \
    .orderBy("pagerank", ascending=False)
```

### Pattern 3: Fraud Detection

```python
# Detect fraud rings
# 1. Find connected components
components = graph.connectedComponents()

# 2. Analyze component sizes
component_sizes = components.vertices \
    .groupBy("component") \
    .count() \
    .orderBy("count", ascending=False)

# 3. Flag suspicious components
suspicious_components = component_sizes \
    .filter(col("count") > 10)

# 4. Get suspicious users
suspicious_users = components.vertices \
    .join(suspicious_components, "component")
```

### Pattern 4: Knowledge Graph

```python
# Build knowledge graph
# 1. Create entity vertices
entities = spark.createDataFrame([
    (1, "Person", "Alice"),
    (2, "Company", "TechCorp"),
    (3, "Product", "Widget")
], ["id", "type", "name"])

# 2. Create relationship edges
relationships = spark.createDataFrame([
    (1, 2, "works_at"),
    (2, 3, "produces")
], ["src", "dst", "relationship"])

# 3. Create graph
knowledge_graph = GraphFrame(entities, relationships)

# 4. Query graph
# Find all products made by companies where Alice works
products = knowledge_graph.bfs(
    fromExpr="name = 'Alice'",
    toExpr="type = 'Product'"
)
```

### Pattern 5: Graph ML

```python
# Graph-based machine learning
# 1. Extract graph features
# Node degree
degrees = graph.degrees

# PageRank
pagerank = graph.pageRank(resetProbability=0.15, maxIter=10)

# Triangle count
triangles = graph.triangleCount()

# 2. Create feature vector
features = degrees.join(pagerank.vertices, "id") \
    .join(triangles.vertices, "id") \
    .select("id", "degree", "pagerank", "count")

# 3. Train ML model
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.clustering import KMeans

assembler = VectorAssembler(
    inputCols=["degree", "pagerank", "count"],
    outputCol="features"
)

features_df = assembler.transform(features)

kmeans = KMeans(featuresCol="features", k=3)
model = kmeans.fit(features_df)

# 4. Get clusters
clusters = model.transform(features_df)
```

---

## Conclusion

GraphX provides:

- **Unified graph-parallel processing** on Spark
- **Property graphs** with rich vertex and edge attributes
- **Pregel API** for iterative graph algorithms
- **Built-in algorithms** for common graph operations
- **Integration** with Spark ecosystem

Key takeaways:

1. **Use GraphFrames** for graph processing on Spark
2. **Choose appropriate algorithms** for your use case
3. **Optimize partitioning** for performance
4. **Cache graphs** for iterative algorithms
5. **Monitor performance** and convergence

GraphX is essential for graph analytics, social network analysis, recommendation systems, and other graph-parallel workloads.