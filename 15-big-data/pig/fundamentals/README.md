# Apache Pig Fundamentals

Apache Pig is a high-level platform for creating programs that run on Apache Hadoop. Pig uses a language called Pig Latin, which provides a simpler abstraction over MapReduce programming. Pig is designed for analyzing large datasets and supports ETL operations, data processing, and ad-hoc analysis.

## Table of Contents

1. [Pig Overview](#pig-overview)
2. [Architecture](#architecture)
3. [Pig Latin Basics](#pig-latin-basics)
4. [Relations and Bags](#relations-and-bags)
5. [Data Types](#data-types)
6. [Operators](#operators)
7. [Built-in Functions](#built-in-functions)
8. [Best Practices](#best-practices)
9. [Common Patterns](#common-patterns)

---

## Pig Overview

### What is Pig?

Apache Pig is a platform for analyzing large datasets that consists of a high-level language for expressing data analysis programs, coupled with infrastructure for evaluating these programs. The key property of Pig programs is that they are amenable to parallelization.

### Pig Features

- **High-level language**: Pig Latin for data processing
- **Parallel processing**: Automatic parallelization
- **Extensible**: UDFs for custom functions
- **Flexible**: Handles structured and semi-structured data
- **Integration**: Works with Hadoop ecosystem

### Pig vs SQL vs MapReduce

| Feature | Pig | SQL | MapReduce |
|---------|-----|-----|-----------|
| **Language** | Pig Latin | SQL | Java |
| **Abstraction** | High | High | Low |
| **Parallelism** | Automatic | Automatic | Manual |
| **Schema** | Schema on read | Schema on write | None |
| **Use Case** | ETL, Ad-hoc | Queries | Custom processing |

---

## Architecture

### Pig Architecture

```
Pig Architecture:
┌─────────────────────────────────────────────────────────────┐
│                      Pig Latin Script                        │
│         (Data transformation logic)                        │
├─────────────────────────────────────────────────────────────┤
│                      Parser                                  │
│         (Parse Pig Latin to logical plan)                   │
├─────────────────────────────────────────────────────────────┤
│                      Optimizer                               │
│         (Optimize logical plan)                            │
├─────────────────────────────────────────────────────────────┤
│                      Compiler                                │
│         (Generate MapReduce jobs)                          │
├─────────────────────────────────────────────────────────────┤
│                      Execution Engine                        │
│         (Execute MapReduce jobs on Hadoop)                 │
└─────────────────────────────────────────────────────────────┘
```

### Execution Modes

```bash
# Local mode (for development)
pig -x local

# MapReduce mode (for production)
pig -x mapreduce

# Tez mode (for better performance)
pig -x tez
```

---

## Pig Latin Basics

### Loading Data

```pig
-- Load from local file
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int, department:chararray);

-- Load from HDFS
employees = LOAD 'hdfs://path/to/employees' USING PigStorage(',')
    AS (id:int, name:chararray, age:int, department:chararray);

-- Load with different format
employees = LOAD 'employees.json' USING JsonLoader()
    AS (id:int, name:chararray, age:int, department:chararray);
```

### Transforming Data

```pig
-- Filter
young_employees = FILTER employees BY age < 30;

-- Map
names = MAP employees BY name;

-- FlatMap
words = FLATTEN(BAGOFCHARARRAY(employees.name));

-- Join
joined_data = JOIN employees BY department, departments BY id;

-- Group
grouped = GROUP employees BY department;

-- Aggregate
avg_salary = FOREACH grouped GENERATE department, AVG(employees.salary);
```

### Storing Data

```pig
-- Store to local file
STORE employees INTO 'output' USING PigStorage(',');

-- Store to HDFS
STORE employees INTO 'hdfs://path/to/output' USING PigStorage(',');

-- Store with different format
STORE employees INTO 'output.json' USING JsonOutput();
```

---

## Relations and Bags

### Relations

```pig
-- A relation is a bag of tuples
-- Each tuple is a set of fields

-- Create relation
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int, salary:double);

-- Relation operations
filtered = FILTER employees BY age > 30;
projected = FOREACH employees GENERATE name, salary;
grouped = GROUP employees BY department;
```

### Tuples

```pig
-- A tuple is an ordered set of fields
-- Tuples can have different types

-- Create tuple
employee = (1, 'John', 30, 50000.0);

-- Access tuple fields
name = employee.name;
salary = employee.salary;
```

### Bags

```pig
-- A bag is a collection of tuples
-- Bags can have duplicate tuples

-- Create bag
employees_bag = {
    (1, 'John', 30),
    (2, 'Jane', 25),
    (3, 'Bob', 35)
};

-- Bag operations
grouped = GROUP employees BY department;
-- grouped contains bags of employees for each department

-- Access bag
department_groups = FOREACH grouped GENERATE department, employees;
```

### Maps

```pig
-- A map is a set of key-value pairs
-- Keys are chararray, values can be any type

-- Create map
employee_map = (name#'John', age#'30', department#'Engineering');

-- Access map
name = employee_map#'name';
```

---

## Data Types

### Primitive Types

```pig
-- Integer types
int        -- 32-bit signed integer
long       -- 64-bit signed integer

-- Float types
float      -- 32-bit floating point
double     -- 64-bit floating point

-- String types
chararray  -- Character array (string)
bytearray  -- Byte array (binary)

-- Boolean type
boolean    -- TRUE/FALSE

-- DateTime type
datetime   -- Date and time
```

### Complex Types

```pig
-- Tuple
-- Ordered collection of fields
tuple = (1, 'John', 30);

-- Bag
-- Collection of tuples
bag = {
    (1, 'John', 30),
    (2, 'Jane', 25)
};

-- Map
-- Key-value pairs
map = (name#'John', age#'30');
```

### Type Conversion

```pig
-- Cast types
age_int = (int) age_string;
salary_double = (double) salary_string;

-- Convert to string
name_string = (chararray) name;

-- Convert to bytearray
data_bytearray = (bytearray) data;
```

---

## Operators

### Relational Operators

```pig
-- LOAD
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int);

-- STORE
STORE employees INTO 'output' USING PigStorage(',');

-- FILTER
young_employees = FILTER employees BY age < 30;

-- DISTINCT
unique_departments = DISTINCT employees.department;

-- FOREACH
names = FOREACH employees GENERATE name;

-- GROUP
grouped = GROUP employees BY department;

-- JOIN
joined = JOIN employees BY department, departments BY id;

-- CROSS
crossed = CROSS employees, departments;

-- ORDER BY
sorted = ORDER employees BY salary DESC;

-- LIMIT
limited = LIMIT employees 100;
```

### Bag Operators

```pig
-- Flatten
flattened = FLATTEN(grouped);

-- BagToString
bag_string = BagToString(grouped.employees, ',');

-- SIZE
bag_size = SIZE(grouped.employees);

-- IsEmpty
is_empty = IsEmpty(grouped.employees);
```

### Map Operators

```pig
-- Map lookup
name = employee_map#'name';

-- Map keys
keys = KEYS(employee_map);

-- Map values
values = VALUES(employee_map);

-- Map size
map_size = SIZE(employee_map);
```

---

## Built-in Functions

### String Functions

```pig
-- CONCAT
full_name = CONCAT(first_name, ' ', last_name);

-- SUBSTRING
short_name = SUBSTRING(name, 0, 5);

-- UPPER/LOWER
upper_name = UPPER(name);
lower_name = LOWER(name);

-- TRIM
trimmed_name = TRIM(name);

-- REPLACE
replaced = REPLACE(name, 'John', 'Jane');

-- SPLIT
parts = SPLIT(name, ' ');

-- INDEXOF
position = INDEXOF(name, 'o');

-- LAST_INDEX_OF
last_position = LAST_INDEX_OF(name, 'o');

-- REGEX_EXTRACT
extracted = REGEX_EXTRACT(email, '(.*)@(.*)', 1);
```

### Math Functions

```pig
-- ABS
abs_value = ABS(value);

-- CEIL/FLOOR
ceil_value = CEIL(value);
floor_value = FLOOR(value);

-- ROUND
rounded = ROUND(value);

-- SQRT/SIN/COS/TAN
sqrt_value = SQRT(value);
sin_value = SIN(value);

-- RANDOM
random_value = RANDOM();
```

### Date Functions

```pig
-- CURRENT_DATETIME
now = CURRENT_DATETIME();

-- GetYear/GetMonth/GetDay
year = GetYear(date);
month = GetMonth(date);
day = GetDay(date);

-- DaysBetween
days = DaysBetween(date1, date2);

-- MonthsBetween
months = MonthsBetween(date1, date2);

-- ToDate
date = ToDate('2024-01-01', 'yyyy-MM-dd');

-- ToString
date_string = ToString(date, 'yyyy-MM-dd');
```

### Collection Functions

```pig
-- SIZE
bag_size = SIZE(bag);

-- ISEMPTY
is_empty = IsEmpty(bag);

-- BAGTOSTRING
bag_string = BagToString(bag, ',');

-- MAPKEYS/VALUES
keys = MAPKEYS(map);
values = MAPVALUES(map);
```

---

## Best Practices

### 1. Use Appropriate Data Types

```pig
-- Use appropriate types
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int, salary:double);

-- Avoid using chararray for numbers
-- Use int/long for integers
-- Use float/double for decimals
```

### 2. Use FILTER Early

```pig
-- Filter early to reduce data
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int, salary:double);

-- Filter early
young_employees = FILTER employees BY age < 30;

-- Then process
processed = FOREACH young_employees GENERATE name, salary;
```

### 3. Use FOREACH Instead of MAP

```pig
-- Use FOREACH for transformation
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int, salary:double);

-- Use FOREACH
processed = FOREACH employees GENERATE 
    name, 
    salary * 0.1 as bonus;
```

### 4. Use COGROUP for Multiple Relations

```pig
-- Use COGROUP for multiple relations
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, department:chararray);

departments = LOAD 'departments.csv' USING PigStorage(',')
    AS (id:int, name:chararray);

-- COGROUP
cogrouped = COGROUP employees BY department, departments BY id;
```

### 5. Use PARALLEL for Parallelism

```pig
-- Use PARALLEL for parallelism
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, age:int);

-- Set parallelism
grouped = GROUP employees BY department PARALLEL 10;
```

---

## Common Patterns

### Pattern 1: ETL Pipeline

```pig
-- ETL pipeline
-- Extract
raw_data = LOAD 'hdfs://path/to/raw_data' USING PigStorage(',')
    AS (id:int, data:chararray);

-- Transform
cleaned_data = FILTER raw_data BY data IS NOT NULL;
processed_data = FOREACH cleaned_data GENERATE 
    id, 
    UPPER(data) as data;

-- Load
STORE processed_data INTO 'hdfs://path/to/output' USING PigStorage(',');
```

### Pattern 2: Word Count

```pig
-- Word count
-- Load data
lines = LOAD 'input.txt' AS (line:chararray);

-- Split into words
words = FOREACH lines GENERATE FLATTEN(TOKENIZE(line)) AS word;

-- Group by word
grouped = GROUP words BY word;

-- Count words
word_counts = FOREACH grouped GENERATE 
    group AS word, 
    COUNT(words) AS count;

-- Order by count
sorted_counts = ORDER word_counts BY count DESC;

-- Store results
STORE sorted_counts INTO 'output' USING PigStorage(',');
```

### Pattern 3: Join Tables

```pig
-- Join tables
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, department:chararray);

departments = LOAD 'departments.csv' USING PigStorage(',')
    AS (id:int, name:chararray);

-- Join
joined = JOIN employees BY department, departments BY id;

-- Project
result = FOREACH joined GENERATE 
    employees.name AS employee_name, 
    departments.name AS department_name;

-- Store
STORE result INTO 'output' USING PigStorage(',');
```

### Pattern 4: Aggregation

```pig
-- Aggregation
employees = LOAD 'employees.csv' USING PigStorage(',')
    AS (id:int, name:chararray, department:chararray, salary:double);

-- Group by department
grouped = GROUP employees BY department;

-- Aggregate
dept_stats = FOREACH grouped GENERATE 
    group AS department,
    COUNT(employees) AS employee_count,
    AVG(employees.salary) AS avg_salary,
    MAX(employees.salary) AS max_salary,
    MIN(employees.salary) AS min_salary;

-- Store
STORE dept_stats INTO 'output' USING PigStorage(',');
```

---

## Conclusion

Apache Pig provides:

- **High-level language** (Pig Latin) for data processing
- **Automatic parallelization** for Hadoop
- **Extensibility** through UDFs
- **Integration** with Hadoop ecosystem

Key takeaways:

1. **Use Pig Latin** for data processing
2. **Filter early** to reduce data
3. **Use appropriate data types**
4. **Use PARALLEL** for parallelism
5. **Use built-in functions** for common operations

Pig is essential for ETL operations and ad-hoc analysis on Hadoop.