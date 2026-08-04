# Apache Pig

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Pig Latin Basics](#pig-latin-basics)
- [Data Types](#data-types)
- [Relations and Operations](#relations-and-operations)
- [Expressions](#expressions)
- [UDFs (User Defined Functions)](#udfs-user-defined-functions)
- [Execution Modes](#execution-modes)
- [Optimization](#optimization)
- [Pig vs SQL](#pig-vs-sql)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Apache Pig is a high-level platform for creating MapReduce programs using a
language called Pig Latin. It provides a simpler abstraction over MapReduce,
making it easier to write complex data transformation pipelines without
writing Java code.

### Key Characteristics

- **High-level language**: Pig Latin for data flow programming
- **Extensible**: Custom UDFs in Java, Python, JavaScript, Ruby
- **Schema on read**: Flexible data schema handling
- **Optimization**: Automatic optimization of MapReduce jobs
- **Debugging**: Interactive mode for development and testing

### When to Use Pig

- Data transformation and ETL pipelines
- Processing unstructured and semi-structured data
- Rapid prototyping of data processing logic
- Complex multi-stage data transformations
- Working with nested data structures

### Pig vs Hive

| Feature | Pig | Hive |
|---------|-----|------|
| Language | Pig Latin (procedural) | HiveQL (declarative SQL) |
| Paradigm | Data flow | SQL queries |
| Learning Curve | Moderate | Easy for SQL users |
| Schema | Optional | Required |
| Best For | ETL, unstructured data | Analytics, structured data |
| Performance | Similar | Similar |
| UDF Support | Java, Python, JS | Java |

---

## Architecture

### Execution Flow

```
┌───────────────────────────────────────────────────────┐
│                    Pig Script                          │
│               (Pig Latin statements)                   │
└───────────────────┬───────────────────────────────────┘
                    │
┌───────────────────▼───────────────────────────────────┐
│                  Parser                                │
│         (Converts to logical plan)                     │
└───────────────────┬───────────────────────────────────┘
                    │
┌───────────────────▼───────────────────────────────────┐
│              Logical Optimizer                         │
│      (Predicate pushdown, column pruning)              │
└───────────────────┬───────────────────────────────────┘
                    │
┌───────────────────▼───────────────────────────────────┐
│            Physical Plan Generator                     │
│      (Converts to MapReduce jobs)                      │
└───────────────────┬───────────────────────────────────┘
                    │
┌───────────────────▼───────────────────────────────────┐
│              Physical Optimizer                        │
│      (Optimizes MapReduce execution)                   │
└───────────────────┬───────────────────────────────────┘
                    │
┌───────────────────▼───────────────────────────────────┐
│           MapReduce Plan Generator                     │
│      (Creates MapReduce job chain)                     │
└───────────────────┬───────────────────────────────────┘
                    │
┌───────────────────▼───────────────────────────────────┐
│              MapReduce Jobs                            │
│         (Executed on Hadoop cluster)                   │
└───────────────────────────────────────────────────────┘
```

### Components

1. **Parser**: Validates syntax and generates logical plan
2. **Logical Optimizer**: Applies optimization rules
3. **Physical Plan Generator**: Converts logical to physical operations
4. **Physical Optimizer**: Optimizes MapReduce execution
5. **MR Plan Generator**: Creates MapReduce job chain

---

## Pig Latin Basics

### Basic Syntax

```pig
-- Load data
raw = LOAD '/data/users.json' USING JsonLoader(
    'user_id:int, name:chararray, email:chararray, age:int'
);

-- Filter records
adults = FILTER raw BY age >= 18;

-- Project columns
projection = FOREACH adults GENERATE user_id, name, email;

-- Store results
STORE projection INTO '/output/adults' USING PigStorage('\t');
```

### Comments

```pig
-- Single line comment

/*
Multi-line
comment
*/

-- Explain execution plan
EXPLAIN adults;

-- Describe schema
DESCRIBE raw;

-- Illustrate sample execution
ILLUSTRATE adults;
```

---

## Data Types

### Primitive Types

```pig
int         -- 32-bit signed integer
long        -- 64-bit signed integer
float       -- 32-bit floating point
double      -- 64-bit floating point
chararray   -- Character array (string)
bytearray   -- Byte array (binary)
boolean     -- Boolean (true/false)
datetime    -- Date/time
```

### Complex Types

```pig
-- Tuple (ordered collection of fields)
t = FOREACH raw GENERATE (user_id, name, age) AS user_tuple;

-- Bag (collection of tuples)
bags = FOREACH raw GENERATE { (name, email), (phone, address) } AS user_bags;

-- Map (key-value pairs)
maps = FOREACH raw GENERATE ['name'..'John', 'email'..'john@example.com'] AS user_map;
```

### Schema Definition

```pig
-- Load with schema
raw = LOAD '/data/users' AS (
    user_id:int,
    name:chararray,
    email:chararray,
    age:int,
    address:chararray
);

-- Define schema using LOAD with column definitions
raw = LOAD '/data/users' AS (
    user_id:int,
    name:chararray,
    email:chararray,
    age:int,
    street:chararray,
    city:chararray,
    state:chararray,
    zip:chararray
);
```

---

## Relations and Operations

### LOAD and STORE

```pig
-- Load from HDFS
raw = LOAD '/data/input' USING PigStorage('\t') AS (col1:chararray, col2:int);

-- Load from S3
raw = LOAD 's3://bucket/data' USING PigStorage(',');

-- Load JSON
raw = LOAD '/data/input.json' USING JsonLoader('schema');

-- Load with custom loader
raw = LOAD '/data/input' USING MyCustomLoader();

-- Store results
STORE results INTO '/output/results' USING PigStorage('\t');

-- Store as JSON
STORE results INTO '/output/results' USING JsonStorage();

-- Store to HDFS
STORE results INTO '/output/results' USING PigStorage(',');
```

### FOREACH (Transform)

```pig
-- Simple projection
projection = FOREACH raw GENERATE user_id, name;

-- Add computed columns
enriched = FOREACH raw GENERATE
    user_id,
    name,
    age,
    age * 1.0 AS age_double,
    CONCAT(name, ' (', (chararray)age, ')') AS display_name;

-- Flatten nested structures
flattened = FOREACH raw GENERATE
    user_id,
    FLATTEN(address);  -- Flattens tuple into separate columns

-- Generate bags
bagged = FOREACH raw {
    recent = FILTER orders BY date >= '2024-01-01';
    GENERATE user_id, name, COUNT(recent) AS recent_orders;
};
```

### FILTER

```pig
-- Simple filter
adults = FILTER raw BY age >= 18;

-- Multiple conditions
active_adults = FILTER raw BY age >= 18 AND status == 'active';

-- Regex filter
gmail_users = FILTER raw BY email MATCHES '.*@gmail.com';

-- Null check
non_null = FILTER raw BY name IS NOT NULL;

-- Range filter
age_range = FILTER raw BY age >= 18 AND age <= 65;

-- Not condition
inactive = FILTER raw BY status != 'active';
```

### GROUP

```pig
-- Group by single column
grouped = GROUP raw BY department;

-- Group with aggregation
dept_stats = FOREACH grouped {
    employees = raw;
    GENERATE
        group AS department,
        COUNT(employees) AS employee_count,
        AVG(employees.age) AS avg_age,
        MAX(employees.salary) AS max_salary;
};

-- Group by multiple columns
grouped = GROUP raw BY (department, role);

-- Group all
all_data = GROUP raw ALL;
total_count = FOREACH all_data GENERATE COUNT(raw) AS total;
```

### JOIN

```pig
-- Inner join
joined = JOIN users BY user_id, orders BY user_id;

-- Left outer join
left_joined = JOIN users BY user_id LEFT OUTER, orders BY user_id;

-- Right outer join
right_joined = JOIN users BY user_id RIGHT OUTER, orders BY user_id;

-- Full outer join
full_joined = JOIN users BY user_id FULL OUTER, orders BY user_id;

-- Multiple joins
joined = JOIN
    users BY user_id,
    orders BY user_id,
    products BY product_id;

-- Self join
self_joined = JOIN raw BY user_id, raw BY manager_id;
```

### ORDER BY

```pig
-- Ascending order (default)
sorted = ORDER raw BY name;

-- Descending order
reverse_sorted = ORDER raw BY age DESC;

-- Multiple sort keys
multi_sorted = ORDER raw BY (department, name);

-- Limit results
top_10 = ORDER raw BY salary DESC;
top_10_limited = LIMIT top_10 10;
```

### DISTINCT

```pig
-- Remove duplicates
unique_users = DISTINCT raw;

-- Distinct on specific columns
unique_names = FOREACH raw GENERATE name;
unique_names = DISTINCT unique_names;
```

### UNION

```pig
-- Union two relations
combined = UNION relation1, relation2;

-- Union with duplicate removal
combined_unique = UNION DISTINCT relation1, relation2;

-- Union preserving duplicates
combined_all = UNION ONSCHEMA relation1, relation2;
```

---

## Expressions

### Arithmetic Expressions

```pig
-- Basic arithmetic
a = FOREACH raw GENERATE
    price,
    quantity,
    price * quantity AS total,
    price * 0.1 AS tax,
    price + (price * 0.1) AS total_with_tax;

-- Modulo
remainder = FOREACH raw GENERATE id, id % 10 AS bucket;
```

### Comparison Expressions

```pig
-- Equality
active = FILTER raw BY status == 'active';

-- Not equal
inactive = FILTER raw BY status != 'active';

-- Greater than, less than
adults = FILTER raw BY age > 18;
young = FILTER raw BY age < 30;

-- Range check
range_check = FOREACH raw GENERATE
    age,
    (age < 18 ? 'minor' : (age >= 18 AND age < 65 ? 'adult' : 'senior')) AS age_group;
```

### Boolean Expressions

```pig
-- AND, OR, NOT
filtered = FILTER raw BY (age >= 18 AND age <= 65) OR status == 'exempt';

-- IS NULL, IS NOT NULL
non_null = FILTER raw BY name IS NOT NULL;
has_email = FILTER raw BY email IS NOT NULL;
```

### String Expressions

```pig
-- Concatenation
full_name = FOREACH raw GENERATE CONCAT(first_name, ' ', last_name) AS full_name;

-- Substring
initial = FOREACH raw GENERATE SUBSTRING(name, 0, 1) AS first_char;

-- Replace
cleaned = FOREACH raw GENERATE REPLACE(email, '@.*', '') AS username;

-- Upper/Lower case
upper_name = FOREACH raw GENERATE UPPER(name) AS upper_name;
lower_email = FOREACH raw GENERATE LOWER(email) AS lower_email;

-- Trim
trimmed = FOREACH raw GENERATE TRIM(name) AS trimmed_name;

-- Matches (regex)
is_gmail = FOREACH raw GENERATE email MATCHES '.*@gmail.com' AS is_gmail;
```

### Date/Time Expressions

```pig
-- Current date
today = FOREACH raw GENERATE GetDay(current_date()) AS day_of_week;

-- Date difference
days_between = FOREACH raw GENERATE
    DaysBetween(end_date, start_date) AS duration_days;

-- Date formatting
formatted = FOREACH raw GENERATE
    ToString(timestamp, 'yyyy-MM-dd') AS formatted_date;
```

### Tuple/Bag Expressions

```pig
-- Create tuple
with_tuple = FOREACH raw GENERATE
    (name, age, email) AS user_tuple;

-- Access tuple field
name_only = FOREACH raw GENERATE user_tuple.$0 AS name;

-- Bag operations
bag_count = FOREACH raw GENERATE
    user_id,
    COUNT(orders) AS order_count;

-- Bag size
bag_size = FOREACH raw GENERATE
    user_id,
    SIZE(orders) AS order_count;

-- Flatten bag
flattened = FOREACH raw GENERATE
    user_id,
    FLATTEN(orders);
```

### Cast Expressions

```pig
-- Type casting
with_cast = FOREACH raw GENERATE
    (int) age_string AS age_int,
    (float) price AS price_float,
    (chararray) user_id AS user_id_string,
    (long) timestamp AS timestamp_long;
```

---

## UDFs (User Defined Functions)

### Built-in UDFs

```pig
-- String functions
UPPER('hello')            -- 'HELLO'
LOWER('HELLO')            -- 'hello'
TRIM('  hello  ')         -- 'hello'
CONCAT('hello', ' world') -- 'hello world'
SUBSTRING('hello', 0, 3)  -- 'hel'
REPLACE('hello', 'l', 'r') -- 'herro'
SIZE('hello')             -- 5

-- Math functions
ABS(-5)                   -- 5
CEIL(4.3)                 -- 5
FLOOR(4.7)                -- 4
ROUND(4.5)                -- 5
SQRT(16)                  -- 4.0
LOG(100)                  -- 2.0

-- Bag functions
SIZE(bag)                 -- Number of tuples
ISEMPTY(bag)              -- Check if empty
ORDER bag BY field        -- Sort bag
DISTINCT bag              -- Remove duplicates
FILTER bag BY condition   -- Filter bag
TOP(n, 'field', bag)      -- Top N tuples

-- Date functions
CURRENT_DATE()            -- Current date
CURRENT_TIMESTAMP()       -- Current timestamp
DaysBetween(date1, date2) -- Days between dates
MonthsBetween(date1, date2) -- Months between dates
AddDays(date, n)          -- Add n days
ToString(date, format)    -- Format date
```

### Custom UDFs (Java)

```java
public class WordCount extends EvalFunc<Long> {

    @Override
    public Long exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }

        try {
            String text = (String) input.get(0);
            if (text == null) {
                return null;
            }

            String[] words = text.split("\\s+");
            return (long) words.length;
        } catch (Exception e) {
            throw new IOException("Error processing input", e);
        }
    }

    @Override
    public Schema outputSchema(Schema input) {
        return new Schema(new Schema.FieldSchema("word_count", DataType.LONG));
    }
}
```

### Registering UDFs

```pig
-- Register JAR
REGISTER '/path/to/my-udf.jar';

-- Register multiple JARs
REGISTER '/path/to/udf1.jar';
REGISTER '/path/to/udf2.jar';

-- Define alias
DEFINE MyUDF com.example.MyUDF();

-- Use UDF
results = FOREACH raw GENERATE MyUDF(field1, field2) AS result;
```

### Custom UDFs (Python)

```python
# my_udf.py
@outputSchema("result:chararray")
def clean_text(text):
    if text is None:
        return None
    return text.strip().lower()

@outputSchema("count:int")
def count_words(text):
    if text is None:
        return 0
    return len(text.split())
```

```pig
-- Register Python UDF
REGISTER 'my_udf.py' USING jython AS my_udf;

-- Use Python UDF
cleaned = FOREACH raw GENERATE my_udf.clean_text(name) AS clean_name;
```

---

## Execution Modes

### Local Mode

```bash
# Run in local mode (for testing)
pig -x local script.pig

# Interactive local mode
pig -x local
grunt> LOAD '/data/users' AS (id:int, name:chararray);
grunt> DUMP;
```

### MapReduce Mode

```bash
# Run on Hadoop cluster
pig script.pig

# With specific settings
pig -param INPUT=/data/input -param OUTPUT=/output/results script.pig

# With debug
pig -debug script.pig
```

### Parameters

```pig
-- Define parameters in script
%default INPUT '/data/input'
%default OUTPUT '/output/results'

-- Use parameters
raw = LOAD '$INPUT' AS (id:int, name:chararray);
STORE results INTO '$OUTPUT';
```

```bash
# Pass parameters from command line
pig -param INPUT=/data/input -param OUTPUT=/output/results script.pig

# Use parameter file
pig -param_file params.txt script.pig
```

---

## Optimization

### Auto-Optimization

```pig
-- Enable optimizations
SET opt.multiquery true;          -- Combine multiple STORE statements
SET opt.filter false;             -- Disable filter optimization
SET opt.limit 100;                -- Optimize LIMIT operations
SET opt.union true;               -- Optimize UNION operations
SET opt.skewedjoin true;          -- Handle skewed joins
SET opt.distinct 2;               -- Parallelism for DISTINCT

-- Memory settings
SET mapreduce.map.memory.mb 4096;
SET mapreduce.reduce.memory.mb 8192;
```

### Common Optimizations

```pig
-- Predicate pushdown (automatic)
raw = LOAD '/data/input' AS (id:int, name:chararray, age:int);
filtered = FILTER raw BY age >= 18;  -- Pushed to LOAD

-- Column pruning (automatic)
projection = FOREACH raw GENERATE name, email;  -- Only reads needed columns

-- Limit pushdown
sorted = ORDER raw BY name;
limited = LIMIT sorted 100;  -- Limited at LOAD stage

-- Join optimization
-- Replicate small tables to all mappers
SET pig.join.mapparallel 10;
SET pig.join.replicated true;
```

### Performance Tuning

```pig
-- Use combiner for aggregation
SET pig.exec.reducers.bytes.per.reducer 1000000000;

-- Increase parallelism
SET default_parallel 20;

-- Use skewed join for uneven data
SET pig.skewedjoin.reduce.memusage 0.3;

-- Optimize memory usage
SET pig.cachedbatch.memusage 0.2;

-- Debug mode
SET pig.debugplan true;
EXPLAIN script.pig;
```

---

## Pig vs SQL

### Key Differences

| Feature | Pig Latin | SQL |
|---------|-----------|-----|
| Paradigm | Data flow (procedural) | Declarative |
| Schema | Optional | Required |
| Complex types | Native support | Limited |
| Nested data | Natural handling | Requires flattening |
| Order | Data flow order | Set-based |
| Optimization | Manual hints | Query optimizer |
| Debugging | ILLUSTRATE, DUMP | EXPLAIN |

### Equivalent Operations

```pig
-- Pig: Filter
filtered = FILTER raw BY age >= 18;

-- SQL: WHERE
SELECT * FROM raw WHERE age >= 18;

-- Pig: Project
projected = FOREACH raw GENERATE name, email;

-- SQL: SELECT
SELECT name, email FROM raw;

-- Pig: Group
grouped = GROUP raw BY department;
stats = FOREACH grouped GENERATE group, COUNT(raw);

-- SQL: GROUP BY
SELECT department, COUNT(*) FROM raw GROUP BY department;

-- Pig: Join
joined = JOIN users BY id, orders BY user_id;

-- SQL: JOIN
SELECT * FROM users JOIN orders ON users.id = orders.user_id;

-- Pig: Order
sorted = ORDER raw BY name;

-- SQL: ORDER BY
SELECT * FROM raw ORDER BY name;

-- Pig: Limit
limited = LIMIT raw 10;

-- SQL: LIMIT
SELECT * FROM raw LIMIT 10;
```

---

## Best Practices

### Script Design

1. **Use meaningful aliases**: `users`, `orders`, `filtered_users`
2. **Comment complex logic**: Explain business rules and transformations
3. **Use parameters**: Make scripts configurable
4. **Test in local mode**: Validate logic before cluster execution

### Performance

1. **Filter early**: Reduce data volume as soon as possible
2. **Use appropriate parallelism**: Match cluster resources
3. **Avoid unnecessary DISTINCT**: Use only when needed
4. **Use replicated joins**: For small tables (< 1GB)

### Debugging

```pig
-- Check execution plan
EXPLAIN script.pig;

-- Sample data
ILLUSTRATE script.pig;

-- Debug output
DUMP relation;

-- Describe schema
DESCRIBE relation;
```

---

## Examples

### Log Processing

```pig
-- Load log data
logs = LOAD '/data/access.log' AS (
    ip:chararray,
    timestamp:chararray,
    method:chararray,
    url:chararray,
    status:int,
    size:long
);

-- Parse and filter
valid_logs = FILTER logs BY status >= 200 AND status < 400;

-- Extract page views
page_views = FOREACH valid_logs GENERATE
    url,
    timestamp,
    size;

-- Count page views by URL
grouped = GROUP page_views BY url;
page_view_counts = FOREACH grouped GENERATE
    group AS url,
    COUNT(page_views) AS view_count;

-- Sort by popularity
sorted_views = ORDER page_view_counts BY view_count DESC;

-- Top 10 pages
top_10 = LIMIT sorted_views 10;

-- Store results
STORE top_10 INTO '/output/top_pages' USING PigStorage('\t');
```

### ETL Pipeline

```pig
-- Load raw data
raw = LOAD '/data/raw/users.json' USING JsonLoader(
    'user_id:int, name:chararray, email:chararray, age:int, created_at:chararray'
);

-- Clean and transform
cleaned = FOREACH raw GENERATE
    user_id,
    TRIM(LOWER(name)) AS name,
    LOWER(TRIM(email)) AS email,
    age,
    ToDate(created_at, 'yyyy-MM-dd') AS created_date;

-- Remove duplicates and invalid records
valid = FILTER cleaned BY
    email IS NOT NULL AND
    email MATCHES '.*@.*\\..*' AND
    age >= 0 AND age <= 150;

-- Deduplicate
unique_users = DISTINCT valid;

-- Add derived columns
enriched = FOREACH unique_users GENERATE
    *,
    (age < 18 ? 'minor' : (age >= 18 AND age < 65 ? 'adult' : 'senior')) AS age_group,
    GetYear(created_date) AS registration_year;

-- Partition by year
grouped = GROUP enriched BY registration_year;

-- Store partitioned data
STORE grouped INTO '/output/users' USING PigStorage('\t');
```

---

## References

- [Apache Pig Documentation](https://pig.apache.org/docs/)
- [Pig Latin Reference Manual](https://pig.apache.org/docs/r0.17.0/basic.html)
- [Pig UDF Guide](https://pig.apache.org/docs/r0.17.0/udf.html)
- [Pig Performance Tuning](https://cwiki.apache.org/confluence/display/PIG/Pig+Performance)
- [Programming Pig](http://chimera.labs.oreilly.com/books/1234000001811/)
