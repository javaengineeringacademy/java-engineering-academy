# Neo4j Fundamentals

## Comprehensive Guide to Graph Databases

Neo4j is a graph database that stores data as nodes, relationships, and properties. This guide covers Cypher query language and graph modeling.

---

## Table of Contents

1. [Graph Database Concepts](#graph-database-concepts)
2. [Cypher Basics](#cypher-basics)
3. [Nodes and Relationships](#nodes-and-relationships)
4. [Properties](#properties)
5. [Best Practices](#best-practices)

---

## Graph Database Concepts

### Graph Structure

```
(Nodes) --[Relationships]--> (Nodes)
   |                             |
   v                             v
(Properties)                 (Properties)
```

### When to Use Graph Databases

```
- Social networks
- Recommendation engines
- Fraud detection
- Knowledge graphs
- Network/IT infrastructure
- Identity management
```

---

## Cypher Basics

### Create Nodes

```cypher
// Create node with label
CREATE (p:Person {name: 'John', age: 30})

// Create multiple nodes
CREATE (p1:Person {name: 'John', age: 30})
CREATE (p2:Person {name: 'Jane', age: 25})

// Create with MERGE (avoids duplicates)
MERGE (p:Person {name: 'John'})
ON CREATE SET p.createdAt = datetime()
ON MATCH SET p.lastSeen = datetime()
```

### Create Relationships

```cypher
// Create relationship
MATCH (p1:Person {name: 'John'}), (p2:Person {name: 'Jane'})
CREATE (p1)-[:KNOWS {since: 2020}]->(p2)

// Create multiple relationships
MATCH (p:Person {name: 'John'}), (c:Company {name: 'Acme'})
CREATE (p)-[:WORKS_AT {since: 2019}]->(c)
CREATE (p)-[:LIVES_IN]->(city:City {name: 'New York'})
```

### Read Queries

```cypher
// Find all nodes
MATCH (p:Person) RETURN p

// Find with conditions
MATCH (p:Person) WHERE p.age > 25 RETURN p.name, p.age

// Find relationships
MATCH (p:Person)-[:KNOWS]->(friend:Person)
RETURN p.name, friend.name

// Path finding
MATCH path = (start:Person {name: 'John'})-[*]->(end:Person)
RETURN path
```

### Update and Delete

```cypher
// Update properties
MATCH (p:Person {name: 'John'})
SET p.age = 31, p.updatedAt = datetime()

// Delete node (must remove relationships first)
MATCH (p:Person {name: 'John'})
DETACH DELETE p

// Delete relationship
MATCH (p1:Person {name: 'John'})-[r:KNOWS]->(p2:Person {name: 'Jane'})
DELETE r
```

---

## Nodes and Relationships

### Node Labels

```cypher
// Single label
CREATE (p:Person)

// Multiple labels
CREATE (p:Person:Employee:Manager)
```

### Relationship Types

```cypher
// One-way relationship
(p1)-[:KNOWS]->(p2)

// Bidirectional (model as two one-way)
(p1)-[:KNOWS]->(p2)
(p2)-[:KNOWS]->(p1)

// Typed relationships
(p)-[:WORKS_AT]->(c)
(p)-[:LIVES_IN]->(c)
(p)-[:FRIENDS_WITH]->(p)
```

### Relationship Properties

```cypher
// Relationship with properties
CREATE (p1)-[:KNOWS {
    since: 2020,
    strength: 0.8,
    context: 'work'
}]->(p2)
```

---

## Properties

### Property Types

```cypher
// Strings
name: 'John'

// Numbers
age: 30
salary: 75000.50

// Booleans
active: true

// Dates
birthday: date('1990-01-15')
createdAt: datetime()

// Arrays
tags: ['developer', 'java', 'spring']

// Maps
address: {street: '123 Main St', city: 'NYC'}
```

### Property Operations

```cypher
// Set property
SET p.name = 'Jane'

// Remove property
REMOVE p.temporaryField

// Null check
WHERE p.email IS NOT NULL

// Exists check
WHERE exists(p.phone)
```

---

## Best Practices

### 1. Design for Queries

```cypher
// Query: Find friends of friends
MATCH (p:Person {name: 'John'})-[:KNOWS]->(friend)-[:KNOWS]->(fof)
RETURN fof.name

// Design: Model KNOWS relationships for this query
```

### 2. Use Indexes

```cypher
// Create index
CREATE INDEX FOR (p:Person) ON (p.name)

// Create composite index
CREATE INDEX FOR (p:Person) ON (p.lastName, p.firstName)

// Create uniqueness constraint
CREATE CONSTRAINT FOR (p:Person) REQUIRE p.email IS UNIQUE
```

### 3. Use Parameters

```cypher
// Good - Parameterized query
MATCH (p:Person {name: $name}) RETURN p

// Bad - String concatenation (injection risk)
MATCH (p:Person {name: '" + name + "'}) RETURN p
```

### 4. Limit Results

```cypher
// Good - Limited results
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

- [Neo4j Documentation](https://neo4j.com/docs/)
- [Cypher Manual](https://neo4j.com/docs/cypher-manual/)
- [Graph Database Concepts](https://neo4j.com/docs/getting-started/)
