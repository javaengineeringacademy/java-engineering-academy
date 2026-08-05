# SQL Projects

Database design exercises, query challenges, and performance tuning scenarios.

## Project Ideas

### 1. E-Commerce Database Design

Design a complete e-commerce database schema.

**Requirements:**
- Users with roles (admin, customer, vendor)
- Products with categories, variants, and inventory
- Orders with order items and payment tracking
- Shopping cart functionality
- Reviews and ratings

**Tables to Create:**
```sql
-- Users and authentication
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    parent_id INTEGER REFERENCES categories(id)
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    price NUMERIC(10,2),
    category_id INTEGER REFERENCES categories(id),
    stock INTEGER DEFAULT 0
);

-- Orders
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    status VARCHAR(20) DEFAULT 'pending',
    total NUMERIC(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(id),
    product_id INTEGER REFERENCES products(id),
    quantity INTEGER,
    price NUMERIC(10,2)
);
```

**Queries to Implement:**
- Top 10 best-selling products
- Revenue by category per month
- Customers who haven't ordered in 90 days
- Inventory alerts (stock < 10)
- Average order value by customer segment

---

### 2. Employee Management System

Design a database for HR management.

**Requirements:**
- Employee hierarchy (manager-reporting)
- Department management
- Salary and benefits tracking
- Performance reviews
- Time-off requests

**Challenges:**
- Recursive query to display org chart
- Find all reports of a manager (direct and indirect)
- Calculate total compensation including bonuses
- Track vacation days used vs. remaining
- Performance review rankings

---

### 3. Social Media Analytics

Build analytics queries for a social media platform.

**Schema:**
```sql
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    likes INTEGER DEFAULT 0
);

CREATE TABLE follows (
    follower_id INTEGER REFERENCES users(id),
    following_id INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, following_id)
);

CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(id),
    user_id INTEGER REFERENCES users(id),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Analytics Queries:**
- Most active users (posts per week)
- Engagement rate (likes + comments / followers)
- Viral posts (above average engagement)
- Follower growth rate
- Content performance trends

---

### 4. Time-Series Analysis

Work with time-series data for IoT or business metrics.

**Schema:**
```sql
CREATE TABLE sensor_readings (
    id SERIAL PRIMARY KEY,
    sensor_id INTEGER,
    reading NUMERIC(10,4),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Exercises:**
- Calculate moving averages (5-minute, 1-hour)
- Detect anomalies (readings outside 2 standard deviations)
- Fill gaps in time series with interpolated values
- Aggregate data by time buckets
- Calculate rate of change

---

### 5. Financial Ledger System

Implement double-entry bookkeeping.

**Schema:**
```sql
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(20) CHECK (type IN ('asset', 'liability', 'equity', 'revenue', 'expense'))
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id INTEGER REFERENCES accounts(id),
    debit NUMERIC(12,2) DEFAULT 0,
    credit NUMERIC(12,2) DEFAULT 0,
    description TEXT,
    transaction_date DATE
);
```

**Chercises:**
- Verify debits equal credits for each transaction
- Calculate account balances
- Generate balance sheet
- Generate income statement
- Audit trail for specific accounts

---

### 6. Query Optimization Challenge

Given a slow database, improve performance.

**Setup:**
```sql
-- Slow schema (no indexes)
CREATE TABLE large_orders (
    id SERIAL,
    customer_id INTEGER,
    order_date DATE,
    amount NUMERIC(10,2),
    status VARCHAR(20)
);

-- Insert 1 million rows
INSERT INTO large_orders (customer_id, order_date, amount, status)
SELECT
    (random() * 10000)::INTEGER,
    CURRENT_DATE - (random() * 365)::INTEGER,
    (random() * 1000)::NUMERIC(10,2),
    CASE (random() * 3)::INTEGER
        WHEN 0 THEN 'pending'
        WHEN 1 THEN 'completed'
        ELSE 'cancelled'
    END
FROM generate_series(1, 1000000);
```

**Tasks:**
1. Find the slowest queries
2. Add appropriate indexes
3. Rewrite inefficient queries
4. Implement partitioning
5. Measure improvement

---

### 7. Data Warehouse Schema

Design a star schema for analytics.

**Fact Table:**
```sql
CREATE TABLE fact_sales (
    sale_id SERIAL PRIMARY KEY,
    date_key INTEGER,
    product_key INTEGER,
    customer_key INTEGER,
    store_key INTEGER,
    quantity INTEGER,
    amount NUMERIC(12,2),
    cost NUMERIC(12,2)
);
```

**Dimension Tables:**
```sql
CREATE TABLE dim_date (
    date_key INTEGER PRIMARY KEY,
    full_date DATE,
    year INTEGER,
    quarter INTEGER,
    month INTEGER,
    day INTEGER,
    day_of_week VARCHAR(10)
);

CREATE TABLE dim_product (
    product_key INTEGER PRIMARY KEY,
    product_name VARCHAR(200),
    category VARCHAR(100),
    brand VARCHAR(100)
);

CREATE TABLE dim_customer (
    customer_key INTEGER PRIMARY KEY,
    customer_name VARCHAR(200),
    segment VARCHAR(50),
    region VARCHAR(50)
);
```

**Analytics:**
- Year-over-year comparison
- Product performance by region
- Customer segmentation analysis
- Seasonal trends
- Profit margin analysis

---

### 8. Real-Time Analytics Dashboard

Build queries for a live dashboard.

**Metrics:**
- Current active users (last 5 minutes)
- Orders per minute
- Revenue today vs. yesterday
- Top products being viewed
- Geographic distribution of users

**Implementation:**
```sql
-- Active users (last 5 minutes)
SELECT COUNT(DISTINCT user_id)
FROM user_sessions
WHERE last_active >= NOW() - INTERVAL '5 minutes';

-- Orders per minute (last hour)
SELECT
    DATE_TRUNC('minute', created_at) AS minute,
    COUNT(*) AS orders
FROM orders
WHERE created_at >= NOW() - INTERVAL '1 hour'
GROUP BY 1
ORDER BY 1;
```

---

### 9. Data Migration Challenge

Migrate data between different schemas.

**Scenario:**
- Legacy system uses a single flat table
- New system uses normalized schema
- Must maintain data integrity

**Tasks:**
1. Analyze source data structure
2. Design target schema
3. Write migration queries
4. Validate data post-migration
5. Handle data quality issues

---

### 10. API-Backed Database

Design a database optimized for API access patterns.

**Requirements:**
- CRUD operations
- Pagination support
- Filtering and sorting
- Rate limiting tracking
- Audit logging

**Design Considerations:**
- Optimize for read patterns
- Implement soft deletes
- Add created_at/updated_at timestamps
- Design for horizontal scaling
- Consider caching layers

---

## Difficulty Levels

### Beginner
- Basic CRUD operations
- Simple JOINs
- GROUP BY aggregations
- WHERE clause filtering

### Intermediate
- Window functions
- Subqueries and CTEs
- Complex JOINs
- Data modification

### Advanced
- Recursive queries
- Query optimization
- Partitioning strategies
- Performance tuning
- Complex analytical queries

---

## Resources

- [SQLBolt](https://sqlbolt.com/) - Interactive SQL tutorial
- [HackerRank SQL](https://www.hackerrank.com/domains/sql) - SQL challenges
- [LeetCode Database](https://leetcode.com/problemset/database/) - Database problems
- [SQLZoo](https://sqlzoo.net/) - SQL tutorials
- [Mode Analytics SQL Tutorial](https://mode.com/sql-tutorial/) - Advanced SQL
