# GraphX

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Graph Operations](#graph-operations)
- [Vertex and Edge RDDs](#vertex-and-edge-rdds)
- [Graph Algorithms](#graph-algorithms)
- [Graph Builders](#graph-builders)
- [Aggregate Messages](#aggregate-messages)
- [PageRank](#pagerank)
- [Connected Components](#connected-components)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

GraphX is a Spark component for graph processing and graph-parallel computation.
It extends the Spark RDD abstraction by introducing the Resilient Distributed
Property Graph: a directed multigraph with properties attached to each vertex
and edge.

### Key Characteristics

- **Graph-parallel computation**: Distributed graph processing
- **Fault tolerant**: Built on RDD lineage
- **Unified API**: Works with Spark SQL and MLlib
- **Scalable**: Processes billions of vertices and edges
- **Optimized**: Pregel API for iterative algorithms

### When to Use GraphX

- Social network analysis
- Graph analytics (PageRank, connected components)
- Network traffic analysis
- Recommendation systems (graph-based)
- Fraud detection (anomaly detection in graphs)

### GraphX vs Other Graph Systems

| Feature | GraphX | Neo4j | Titan |
|---------|--------|-------|-------|
| Processing | Distributed | Single machine | Distributed |
| Scale | TB+ | GB | TB+ |
| API | RDD-based | Cypher/Gremlin | Gremlin |
| Integration | Spark | Standalone | Multiple |
| Latency | Batch | Interactive | Batch |

---

## Core Concepts

### Property Graph

```python
from pyspark.sql import SparkSession
from graphframes import GraphFrame

spark = SparkSession.builder.appName("GraphX").getOrCreate()

# Create vertices DataFrame
vertices = spark.createDataFrame([
    (1, "Alice", 34),
    (2, "Bob", 45),
    (3, "Charlie", 29),
    (4, "Diana", 35),
    (5, "Eve", 28)
], ["id", "name", "age"])

# Create edges DataFrame
edges = spark.createDataFrame([
    (1, 2, "friend"),
    (2, 3, "colleague"),
    (3, 4, "friend"),
    (4, 5, "colleague"),
    (5, 1, "friend")
], ["src", "dst", "relationship"])

# Create GraphFrame
graph = GraphFrame(vertices, edges)
```

### Graph Properties

```python
# Number of vertices
print(graph.vertices.count())

# Number of edges
print(graph.edges.count())

# Degree of vertices
inDegrees = graph.inDegrees
outDegrees = graph.outDegrees
degrees = graph.degrees
```

---

## Graph Operations

### Basic Operations

```python
# Filter vertices
filtered_vertices = graph.vertices.filter("age > 30")

# Filter edges
filtered_edges = graph.edges.filter("relationship = 'friend'")

# Select edges
selected_edges = graph.edges.select("src", "dst")

# Union graphs
graph1 = GraphFrame(vertices1, edges1)
graph2 = GraphFrame(vertices2, edges2)

# Note: GraphFrames don't have direct union, use DataFrames
```

### Join Operations

```python
# Join vertices with external data
external_data = spark.createDataFrame([
    (1, "Engineering"),
    (2, "Marketing"),
    (3, "Sales")
], ["id", "department"])

# Join with vertices
new_vertices = graph.vertices.join(external_data, "id")

# Create new graph
new_graph = GraphFrame(new_vertices, graph.edges)

# Join edges with vertex data
edge_with_src = graph.edges.join(
    graph.vertices,
    graph.edges.src == graph.vertices.id
).select("src", "dst", "relationship", "name")

# Join with both source and target vertices
edge_with_both = graph.edges.join(
    graph.vertices,
    graph.edges.src == graph.vertices.id
).join(
    graph.vertices,
    graph.edges.dst == graph.vertices.id,
    "inner"
).select(
    "src", "dst", "relationship",
    graph.vertices["name"].alias("src_name"),
    graph.vertices["name"].alias("dst_name")
)
```

### Subgraph Operations

```python
# Subgraph by edge filter
friend_graph = graph.subgraph(
    edges=graph.edges.filter("relationship = 'friend'")
)

# Subgraph by vertex filter
young_graph = graph.subgraph(
    vertices=graph.vertices.filter("age < 35")
)

# Connected components subgraph
connected = graph.connectedComponents()
```

---

## Vertex and Edge RDDs

### Vertex RDD Operations

```python
# Get vertex attributes
vertices = graph.vertices

# Filter vertices
young_vertices = vertices.filter("age < 35")

# Join vertices
joined_vertices = vertices.join(external_data, "id")

# Aggregate vertex data
vertex_stats = vertices.agg(
    count("*").alias("total_vertices"),
    avg("age").alias("avg_age")
)

# Map vertex attributes
mapped_vertices = vertices.rdd.map(lambda v: (v.id, v.name.upper()))
```

### Edge RDD Operations

```python
# Get edges
edges = graph.edges

# Filter edges
friend_edges = edges.filter("relationship = 'friend'")

# Join edges
joined_edges = edges.join(vertices, edges.src == vertices.id)

# Aggregate edge data
edge_stats = edges.agg(
    count("*").alias("total_edges"),
    countDistinct("src").alias("unique_sources")
)

# Reverse edges
reversed_edges = edges.select("dst", "src", "relationship")

# Map edge attributes
mapped_edges = edges.rdd.map(lambda e: (e.src, e.dst, e.relationship.upper()))
```

---

## Graph Algorithms

### Shortest Paths

```python
from graphframes import GraphFrame

# Compute shortest paths from landmark vertices
landmarks = [1, 4]
paths = graph.shortestPaths(landmarks=landmarks)

# Show results
paths.select("id", "distances").show()
```

### Triangle Count

```python
# Count triangles in the graph
triangles = graph.triangleCount()

# Show results
triangles.select("id", "count").show()
```

### Connected Components

```python
# Find connected components
connected = graph.connectedComponents()

# Show components
connected.select("id", "component").show()
```

### Strongly Connected Components

```python
# Find strongly connected components
strongly_connected = graph.stronglyConnectedComponents(maxIter=10)

# Show components
strongly_connected.select("id", "component").show()
```

---

## Graph Builders

### Creating Graphs from DataFrames

```python
# From vertices and edges
graph = GraphFrame(vertices, edges)

# From CSV files
vertices_df = spark.read.csv("vertices.csv", header=True, inferSchema=True)
edges_df = spark.read.csv("edges.csv", header=True, inferSchema=True)
graph = GraphFrame(vertices_df, edges_df)

# From JSON files
vertices_df = spark.read.json("vertices.json")
edges_df = spark.read.json("edges.json")
graph = GraphFrame(vertices_df, edges_df)

# From Parquet files
vertices_df = spark.read.parquet("vertices.parquet")
edges_df = spark.read.parquet("edges.parquet")
graph = GraphFrame(vertices_df, edges_df)
```

### Creating Graphs from RDDs

```python
from graphframes import GraphFrame
from pyspark.sql.types import StructType, StructField, IntegerType, StringType

# Create from RDD
vertices_rdd = sc.parallelize([
    (1, "Alice", 34),
    (2, "Bob", 45),
    (3, "Charlie", 29)
])

edges_rdd = sc.parallelize([
    (1, 2, "friend"),
    (2, 3, "colleague"),
    (3, 1, "friend")
])

# Convert to DataFrames
schema_vertices = StructType([
    StructField("id", IntegerType(), True),
    StructField("name", StringType(), True),
    StructField("age", IntegerType(), True)
])

schema_edges = StructType([
    StructField("src", IntegerType(), True),
    StructField("dst", IntegerType(), True),
    StructField("relationship", StringType(), True)
])

vertices_df = spark.createDataFrame(vertices_rdd, schema_vertices)
edges_df = spark.createDataFrame(edges_rdd, schema_edges)

graph = GraphFrame(vertices_df, edges_df)
```

### Creating Graphs from Edge Lists

```python
# From edge list
edge_list = [
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (5, 1)
]

edges_df = spark.createDataFrame(edge_list, ["src", "dst"])

# Create vertices from edges
vertices_df = edges_df.select(
    col("src").alias("id")
).union(
    edges_df.select(col("dst").alias("id"))
).distinct()

graph = GraphFrame(vertices_df, edges_df)
```

---

## Aggregate Messages

### Basic Message Passing

```python
# Aggregate messages from neighbors
result = graph.aggregateMessages(
    sendToDst=lambda edge: edge.srcAttr,
    sendToSrc=lambda edge: edge.dstAttr
)

# Show results
result.show()
```

### Complex Aggregation

```python
# Send messages based on edge attributes
result = graph.aggregateMessages(
    sendToDst=lambda edge: edge.srcAttr * edge.weight,
    sendToSrc=lambda edge: edge.dstAttr * edge.weight,
    aggregateFunc=lambda a, b: a + b
)

# Show results
result.show()
```

### Custom Aggregation

```python
# Custom message aggregation
def send_message(edge):
    # Send message from source to destination
    return (edge.srcId, edge.srcAttr)

def aggregate_messages(messages):
    # Aggregate messages for each vertex
    return sum(messages)

# Apply aggregation
result = graph.aggregateMessages(
    sendToDst=send_message,
    aggregateFunc=aggregate_messages
)
```

---

## PageRank

### Basic PageRank

```python
# Compute PageRank
ranks = graph.pageRank(resetProbability=0.15, maxIter=10)

# Show vertex ranks
ranks.vertices.select("id", "pagerank").show()

# Show edge ranks
ranks.edges.show()
```

### PageRank with Tolerance

```python
# PageRank with tolerance
ranks = graph.pageRank(resetProbability=0.15, tol=0.01)

# Show results
ranks.vertices.select("id", "pagerank").show()
```

### Personalized PageRank

```python
# Personalized PageRank (not directly supported in GraphFrames)
# Use custom implementation or GraphX RDD API
```

---

## Connected Components

### Connected Components

```python
# Find connected components
components = graph.connectedComponents()

# Show components
components.select("id", "component").show()

# Count components
component_counts = components.groupBy("component").count()
component_counts.show()
```

### Strongly Connected Components

```python
# Find strongly connected components
strongly_connected = graph.stronglyConnectedComponents(maxIter=10)

# Show components
strongly_connected.select("id", "component").show()
```

### Label Propagation

```python
# Label propagation for community detection
labels = graph.labelPropagationAlg(maxIter=5)

# Show labels
labels.select("id", "label").show()
```

---

## Best Practices

### Graph Construction

1. **Use appropriate ID types**: Integers or longs for better performance
2. **Partition wisely**: Use edge partitioning for better locality
3. **Cache graph**: Cache frequently accessed graphs
4. **Handle missing data**: Filter null vertices and edges

### Algorithm Selection

1. **Use built-in algorithms**: Prefer GraphX built-in algorithms over custom implementations
2. **Iterate efficiently**: Use Pregel API for iterative algorithms
3. **Monitor convergence**: Check algorithm convergence criteria
4. **Parallelize appropriately**: Set appropriate parallelism levels

### Performance Optimization

```python
# Cache graph for iterative algorithms
graph.cache()

# Use appropriate partitioning
graph.partitionBy("random", numPartitions=100)

# Optimize message passing
graph.aggregateMessages(
    sendToDst=lambda e: e.srcAttr,
    aggregateFunc=lambda a, b: a + b
)

# Monitor memory usage
graph.vertices.unpersist()
graph.edges.unpersist()
```

### Common Pitfalls

1. **Memory issues**: Large graphs may cause OOM errors
2. **Long iterations**: Some algorithms may not converge
3. **Data skew**: Uneven graph structure can cause performance issues
4. **Missing edges**: Ensure graph connectivity for certain algorithms

---

## Examples

### Social Network Analysis

```python
from graphframes import GraphFrame

# Create social network graph
vertices = spark.createDataFrame([
    (1, "Alice", 34, "Engineering"),
    (2, "Bob", 45, "Marketing"),
    (3, "Charlie", 29, "Sales"),
    (4, "Diana", 35, "Engineering"),
    (5, "Eve", 28, "Marketing"),
    (6, "Frank", 42, "Sales")
], ["id", "name", "age", "department"])

edges = spark.createDataFrame([
    (1, 2, "friend"),
    (2, 3, "colleague"),
    (3, 4, "friend"),
    (4, 5, "colleague"),
    (5, 6, "friend"),
    (6, 1, "friend"),
    (1, 3, "colleague"),
    (2, 5, "friend")
], ["src", "dst", "relationship"])

graph = GraphFrame(vertices, edges)

# PageRank
ranks = graph.pageRank(resetProbability=0.15, maxIter=10)
ranks.vertices.select("name", "pagerank").orderBy("pagerank", ascending=False).show()

# Connected components
components = graph.connectedComponents()
components.select("name", "component").show()

# Degree analysis
degrees = graph.degrees
degrees.join(vertices, "id").select("name", "degree").orderBy("degree", ascending=False).show()

# Triangle count
triangles = graph.triangleCount()
triangles.select("name", "count").show()
```

### Recommendation System

```python
# User-item interaction graph
vertices = spark.createDataFrame([
    (1, "user", "Alice"),
    (2, "user", "Bob"),
    (3, "user", "Charlie"),
    (4, "item", "Movie A"),
    (5, "item", "Movie B"),
    (6, "item", "Movie C")
], ["id", "type", "name"])

edges = spark.createDataFrame([
    (1, 4, "rated", 5),
    (1, 5, "rated", 4),
    (2, 4, "rated", 3),
    (2, 6, "rated", 5),
    (3, 5, "rated", 4),
    (3, 6, "rated", 3)
], ["src", "dst", "relationship", "rating"])

graph = GraphFrame(vertices, edges)

# Find similar users
user_similarity = graph.aggregateMessages(
    sendToDst=lambda edge: (edge.srcId, edge.srcAttr),
    aggregateFunc=lambda a, b: a + [b] if isinstance(a, list) else [a, b]
)

# User-item recommendations
recommendations = graph.pageRank(resetProbability=0.15, maxIter=10)
```

---

## References

- [GraphX Programming Guide](https://spark.apache.org/docs/latest/graphx-programming-guide.html)
- [GraphFrames Documentation](https://graphframes.github.io/graphframes/)
- [Graph Algorithms on Spark](https://spark.apache.org/docs/latest/mllib-guide.html)
- [GraphX API Reference](https://spark.apache.org/docs/latest/api/scala/#org.apache.spark.graphx.package)
- [Graph Analytics with Spark](http://shop.oreilly.com/product/0636920028512.do)
