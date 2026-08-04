# Cypher Query Language

## Comprehensive Guide to Cypher Syntax

Cypher is a declarative graph query language for Neo4j. This guide covers syntax, patterns, and advanced features.

---

## Table of Contents

1. [Cypher Syntax](#cypher-syntax)
2. [Clauses](#clauses)
3. [Expressions](#expressions)
4. [Functions](#functions)
5. [Best Practices](#best-practices)

---

## Cypher Syntax

### Basic Syntax

```cypher
// Comments
// Single line comment
/* Multi-line comment */

// Keywords (case insensitive)
MATCH (n) RETURN n
MATCH (n) RETURN n

// Semicolons (optional)
MATCH (n) RETURN n;
```

### Clauses

```cypher
MATCH     // Find patterns
WHERE     // Filter results
RETURN    // Return results
CREATE    // Create nodes/relationships
MERGE     // Create or match
SET       // Set properties
REMOVE    // Remove properties/labels
DELETE    // Delete nodes/relationships
WITH      // Chain queries
UNWIND    // Expand list to rows
ORDER BY  // Sort results
LIMIT     // Limit results
SKIP      // Skip results
```

---

## Clauses

### MATCH

```cypher
// Match nodes
MATCH (p:Person) RETURN p

// Match with properties
MATCH (p:Person {name: 'John'}) RETURN p

// Match relationships
MATCH (p:Person)-[:KNOWS]->(friend:Person)
RETURN p, friend

// Optional match
MATCH (p:Person)-[r?:KNOWS]->(friend:Person)
RETURN p.name, friend.name

// Multiple patterns
MATCH (p:Person)-[:WORKS_AT]->(c:Company), (p)-[:LIVES_IN]->(city:City)
RETURN p.name, c.name, city.name
```

### WHERE

```cypher
// Comparison operators
WHERE p.age > 25
WHERE p.age >= 18 AND p.age <= 65
WHERE p.name STARTS WITH 'J'
WHERE p.name CONTAINS 'oh'
WHERE p.name ENDS WITH 'n'

// Logical operators
WHERE p.age > 25 AND p.active = true
WHERE p.age > 25 OR p.age < 18
WHERE NOT p.active = false

// Property existence
WHERE exists(p.email)
WHERE p.email IS NOT NULL

// List operations
WHERE p.age IN [25, 30, 35]
WHERE ALL(x IN p.tags WHERE x CONTAINS 'dev')
WHERE ANY(x IN p.tags WHERE x = 'admin')
WHERE NONE(x IN p.tags WHERE x = 'blocked')
```

### RETURN

```cypher
// Return all
MATCH (p:Person) RETURN p

// Return specific properties
MATCH (p:Person) RETURN p.name, p.age

// Aliases
MATCH (p:Person) RETURN p.name AS name, p.age AS age

// Expressions
MATCH (p:Person) RETURN p.name, p.age * 2 AS doubledAge

// Aggregation
MATCH (p:Person) RETURN count(p) AS totalPeople

// Distinct
MATCH (p:Person) RETURN DISTINCT p.department

// Order by
MATCH (p:Person) RETURN p.name, p.age ORDER BY p.age DESC

// Limit and skip
MATCH (p:Person) RETURN p.name LIMIT 10 SKIP 20
```

### CREATE

```cypher
// Create node
CREATE (p:Person {name: 'John', age: 30})

// Create relationship
MATCH (p1:Person {name: 'John'}), (p2:Person {name: 'Jane'})
CREATE (p1)-[:KNOWS {since: 2020}]->(p2)

// Create multiple
CREATE (p1:Person {name: 'John'}), (p2:Person {name: 'Jane'}), (p1)-[:KNOWS]->(p2)

// Create with WITH
MATCH (c:Company {name: 'Acme'})
CREATE (p:Person {name: 'New Employee'})-[:WORKS_AT]->(c)
RETURN p
```

### MERGE

```cypher
// Match or create
MERGE (p:Person {name: 'John'})
ON CREATE SET p.createdAt = datetime()
ON MATCH SET p.lastSeen = datetime()

// Merge relationship
MATCH (p1:Person {name: 'John'}), (p2:Person {name: 'Jane'})
MERGE (p1)-[:KNOWS]->(p2)

// Merge with ON CREATE/MATCH
MERGE (p:Person {name: 'John'})
ON CREATE SET p.createdAt = datetime(), p.version = 1
ON MATCH SET p.lastSeen = datetime(), p.version = p.version + 1
```

---

## Expressions

### List Expressions

```cypher
// List comprehension
[x IN range(1, 10) | x * 2]

// Filter
[x IN list WHERE x > 5]

// Map
[x IN list | x.name]
```

### Map Expressions

```cypher
// Map projection
WITH {name: p.name, age: p.age} AS personMap

// Property access
p.name
p['name']
```

### Pattern Expressions

```cypher
// Pattern comprehension
[(p)-[:KNOWS]->(f) | f.name]

// Pattern existence
exists((p)-[:KNOWS]->(:Person))
```

---

## Functions

### Aggregate Functions

```cypher
count(x)          // Count
count(DISTINCT x) // Count distinct
sum(x)            // Sum
avg(x)            // Average
min(x)            // Minimum
max(x)            // Maximum
collect(x)        // Collect to list
```

### String Functions

```cypher
toUpper(string)      // Uppercase
toLower(string)      // Lowercase
trim(string)         // Trim whitespace
substring(string, start, length)  // Substring
replace(string, search, replace)  // Replace
split(string, delimiter)          // Split
size(string)          // Length
```

### List Functions

```cypher
size(list)           // Length
head(list)           // First element
last(list)           // Last element
tail(list)           // All but first
reverse(list)        // Reverse
range(start, end)    // Generate range
```

### Mathematical Functions

```cypher
abs(x)              // Absolute value
ceil(x)             // Ceiling
floor(x)            // Floor
round(x)            // Round
sqrt(x)             // Square root
log(x)              // Logarithm
```

### Date/Time Functions

```cypher
datetime()          // Current datetime
date()              // Current date
time()              // Current time
duration('P1Y2M')  // Duration
year(date)          // Extract year
month(date)         // Extract month
day(date)           // Extract day
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
// Good
MATCH (p:Person) RETURN p

// Bad
MATCH (p) RETURN p
```

### 3. Use Indexes

```cypher
// Create index
CREATE INDEX FOR (p:Person) ON (p.name)

// Use indexed property
MATCH (p:Person {name: 'John'}) RETURN p
```

### 4. Limit Results

```cypher
// Good
MATCH (p:Person) RETURN p LIMIT 100

// Bad
MATCH (p:Person) RETURN p
```

### 5. Use EXPLAIN/PROFILE

```cypher
// Check query plan
EXPLAIN MATCH (p:Person {name: 'John'}) RETURN p

// Profile query execution
PROFILE MATCH (p:Person {name: 'John'}) RETURN p
```

---

## Further Reading

- [Cypher Manual](https://neo4j.com/docs/cypher-manual/)
- [Cypher Reference](https://neo4j.com/docs/cypher-manual/current/cypher-reference/)
- [Query Tuning](https://neo4j.com/docs/cypher-manual/current/query-tuning/)
