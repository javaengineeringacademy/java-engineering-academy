# Database Design

## Comprehensive Guide to Database Design

Database design is the process of creating a well-structured database schema. This guide covers normalization, denormalization, relationships, and best practices.

---

## Table of Contents

1. [Design Principles](#design-principles)
2. [Normalization](#normalization)
3. [Denormalization](#denormalization)
4. [Relationships](#relationships)
5. [Best Practices](#best-practices)

---

## Design Principles

### Design Process

```
1. Requirements Analysis
   ↓
2. Conceptual Design
   ↓
3. Logical Design
   ↓
4. Physical Design
   ↓
5. Implementation
```

### Design Goals

```
- Data integrity
- Minimal redundancy
- Efficient queries
- Scalability
- Maintainability
- Performance
```

---

## Normalization

### First Normal Form (1NF)

```
- Each column contains atomic values
- Each row is unique
- No repeating groups
```

```sql
-- Bad - Repeating groups
CREATE TABLE orders (
  id INT PRIMARY KEY,
  product1 VARCHAR(100),
  product2 VARCHAR(100),
  product3 VARCHAR(100)
);

-- Good - 1NF
CREATE TABLE orders (
  id INT PRIMARY KEY,
  product_name VARCHAR(100),
  quantity INT
);
```

### Second Normal Form (2NF)

```
- Must be in 1NF
- All non-key columns depend on the entire primary key
```

```sql
-- Bad - Partial dependency
CREATE TABLE orders (
  order_id INT,
  product_id INT,
  product_name VARCHAR(100),
  quantity INT,
  PRIMARY KEY (order_id, product_id)
);

-- Good - 2NF
CREATE TABLE orders (
  order_id INT PRIMARY KEY,
  product_id INT,
  quantity INT
);

CREATE TABLE products (
  product_id INT PRIMARY KEY,
  product_name VARCHAR(100)
);
```

### Third Normal Form (3NF)

```
- Must be in 2NF
- No transitive dependencies
```

```sql
-- Bad - Transitive dependency
CREATE TABLE employees (
  id INT PRIMARY KEY,
  name VARCHAR(100),
  department_id INT,
  department_name VARCHAR(100)
);

-- Good - 3NF
CREATE TABLE employees (
  id INT PRIMARY KEY,
  name VARCHAR(100),
  department_id INT
);

CREATE TABLE departments (
  id INT PRIMARY KEY,
  name VARCHAR(100)
);
```

---

## Denormalization

### When to Denormalize

```
- Read-heavy applications
- Complex joins are expensive
- Data warehousing
- Reporting systems
- Caching layers
```

### Denormalization Techniques

```sql
-- Add redundant column
CREATE TABLE orders (
  id INT PRIMARY KEY,
  customer_name VARCHAR(100),  -- Redundant
  customer_email VARCHAR(100)  -- Redundant
);

-- Create summary table
CREATE TABLE order_summary (
  customer_id INT PRIMARY KEY,
  total_orders INT,
  total_amount DECIMAL(10,2)
);

-- Create materialized view
CREATE MATERIALIZED VIEW order_summary AS
SELECT customer_id, COUNT(*) as total_orders, SUM(amount) as total_amount
FROM orders
GROUP BY customer_id;
```

---

## Relationships

### One-to-One

```sql
CREATE TABLE users (
  id INT PRIMARY KEY,
  username VARCHAR(50),
  email VARCHAR(100)
);

CREATE TABLE user_profiles (
  user_id INT PRIMARY KEY,
  bio TEXT,
  avatar_url VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### One-to-Many

```sql
CREATE TABLE customers (
  id INT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100)
);

CREATE TABLE orders (
  id INT PRIMARY KEY,
  customer_id INT,
  total DECIMAL(10,2),
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);
```

### Many-to-Many

```sql
CREATE TABLE students (
  id INT PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE courses (
  id INT PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE enrollments (
  student_id INT,
  course_id INT,
  enrollment_date DATE,
  PRIMARY KEY (student_id, course_id),
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (course_id) REFERENCES courses(id)
);
```

---

## Best Practices

### 1. Use Appropriate Data Types

```sql
-- Good - Appropriate types
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  email VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE
);

-- Bad - Inappropriate types
CREATE TABLE users (
  id VARCHAR(100),
  name TEXT,
  email TEXT,
  created_at VARCHAR(50),
  is_active INT
);
```

### 2. Use Primary Keys

```sql
-- Good - Primary key
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100)
);

-- Bad - No primary key
CREATE TABLE users (
  name VARCHAR(100),
  email VARCHAR(100)
);
```

### 3. Use Foreign Keys

```sql
-- Good - Foreign key
CREATE TABLE orders (
  id INT PRIMARY KEY,
  customer_id INT,
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Bad - No foreign key
CREATE TABLE orders (
  id INT PRIMARY KEY,
  customer_id INT
);
```

### 4. Use Indexes

```sql
-- Good - Index on frequently queried columns
CREATE INDEX idx_customers_email ON customers(email);

-- Bad - No index
CREATE TABLE customers (
  id INT PRIMARY KEY,
  email VARCHAR(100)
);
```

### 5. Use Constraints

```sql
-- Good - Constraints
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(100) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  age INT CHECK (age >= 0 AND age <= 150)
);

-- Bad - No constraints
CREATE TABLE users (
  id INT,
  email VARCHAR(100),
  name VARCHAR(100),
  age INT
);
```

---

## Further Reading

- [Database Design](https://www.postgresql.org/docs/current/ddl.html)
- [Normalization](https://en.wikipedia.org/wiki/Database_normalization)
- [ER Diagrams](https://en.wikipedia.org Entity-relationship_model)
