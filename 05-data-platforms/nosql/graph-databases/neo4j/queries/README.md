# Neo4j Queries

## Comprehensive Guide to Cypher Queries

Cypher is Neo4j's query language. This guide covers patterns, path finding, and advanced queries.

---

## Table of Contents

1. [Pattern Matching](#pattern-matching)
2. [Path Finding](#path-finding)
3. [Aggregation](#aggregation)
4. [Advanced Queries](#advanced-queries)
5. [Best Practices](#best-practices)

---

## Pattern Matching

### Basic Patterns

```cypher
// Match all Person nodes
MATCH (p:Person) RETURN p

// Match with properties
MATCH (p:Person {name: 'John'}) RETURN p

// Match relationships
MATCH (p:Person)-[:KNOWS]->(friend:Person)
RETURN p.name, friend.name

// Variable length paths
MATCH (p:Person {name: 'John'})-[:KNOWS*1..3]->(friend)
RETURN friend.name
```

### Pattern Syntax

```cypher
// Node pattern
(p:Person {name: 'John'})

// Relationship pattern
-[r:KNOWS]->

// Path pattern
path = (start)-[:KNOWS*]->(end)

// Optional match
MATCH (p:Person)-[r?:KNOWS]->(friend)
RETURN p.name, friend.name
```

### Multiple Patterns

```cypher
// Multiple patterns
MATCH (p:Person)-[:WORKS_AT]->(c:Company), (p)-[:LIVES_IN]->(city:City)
RETURN p.name, c.name, city.name

// Different relationship types
MATCH (p:Person)-[:KNOWS|WORKS_WITH]->(other:Person)
RETURN p.name, other.name
```

---

## Path Finding

### Shortest Path

```cypher
// Shortest path
MATCH path = shortestPath(
    (start:Person {name: 'John'})-[*]-(end:Person {name: 'Jane'})
)
RETURN path

// All shortest paths
MATCH path = allShortestPaths(
    (start:Person {name: 'John'})-[*]-(end:Person {name: 'Jane'})
)
RETURN path
```

### Variable Length Paths

```cypher
// Find friends up to 3 hops
MATCH (p:Person {name: 'John'})-[:KNOWS*1..3]->(friend)
RETURN friend.name

// Find all connected nodes
MATCH (p:Person {name: 'John'})-[*]->(connected)
RETURN connected.name

// With relationship properties
MATCH (p:Person {name: 'John'})-[r:KNOWS*]->(friend)
WHERE ALL(rel in r WHERE rel.since > 2020)
RETURN friend.name
```

### Path Functions

```cypher
// Path length
MATCH path = (start)-[:KNOWS*]->(end)
RETURN length(path)

// Nodes in path
MATCH path = (start)-[:KNOWS*]->(end)
RETURN nodes(path)

// Relationships in path
MATCH path = (start)-[:KNOWS*]->(end)
RETURN relationships(path)
```

---

## Aggregation

### Aggregate Functions

```cypher
// Count
MATCH (p:Person) RETURN count(p)

// Count distinct
MATCH (p:Person)-[:KNOWS]->(friend)
RETURN p.name, count(DISTINCT friend) as friendCount

// Sum, avg, min, max
MATCH (p:Person) RETURN sum(p.age), avg(p.age), min(p.age), max(p.age)

// Collect
MATCH (p:Person)-[:KNOWS]->(friend)
RETURN p.name, collect(friend.name) as friends
```

### Group By

```cypher
// Group by property
MATCH (p:Person)
RETURN p.department, count(p) as count
ORDER BY count DESC

// Group by relationship
MATCH (p:Person)-[:WORKS_AT]->(c:Company)
RETURN c.name, count(p) as employeeCount
```

---

## Advanced Queries

### List Comprehension

```cypher
// Filter and transform
MATCH (p:Person)
RETURN p.name, [friend IN p.friends WHERE friend.age > 25 | friend.name] as youngFriends
```

### Pattern Comprehension

```cypher
// Pattern comprehension
MATCH (p:Person)
RETURN p.name, [(p)-[:KNOWS]->(f) | f.name] as friends
```

### Exists

```cypher
// Check if relationship exists
MATCH (p:Person)
WHERE exists((p)-[:WORKS_AT]->(:Company))
RETURN p.name

// Check if property exists
MATCH (p:Person)
WHERE exists(p.email)
RETURN p.name
```

### Conditional

```cypher
// Case expression
MATCH (p:Person)
RETURN p.name,
    CASE
        WHEN p.age < 18 THEN 'minor'
        WHEN p.age < 65 THEN 'adult'
        ELSE 'senior'
    END as ageGroup
```

---

## Best Practices

### 1. Use Parameters

```cypher
// Good
MATCH (p:Person {name: $name}) RETURN p

// Bad
MATCH (p:Person {name: 'John'}) RETURN p
```

### 2. Use Labels

```cypher
// Good - With label
MATCH (p:Person) RETURN p

// Bad - Without label
MATCH (p) RETURN p
```

### 3. Use Indexes

```cypher
// Create index
CREATE INDEX FOR (p:Person) ON (p.name)

// Use indexed property in WHERE
MATCH (p:Person {name: 'John'}) RETURN p
```

### 4. Limit Results

```cypher
// Good - Limited
MATCH (p:Person) RETURN p LIMIT 100

// Bad - All results
MATCH (p:Person) RETURN p
```

### 5. Use EXPLAIN

```cypher
// Check query plan
EXPLAIN MATCH (p:Person {name: 'John'})-[:KNOWS]->(friend)
RETURN friend.name
```

---

## Further Reading

- [Cypher Manual](https://neo4j.com/docs/cypher-manual/)
- [Query Tuning](https://neo4j.com/docs/cypher-manual/current/query-tuning/)
- [Neo4j Best Practices](https://neo4j.com/docs/cypher-manual/current/cypher-best-practices/)
