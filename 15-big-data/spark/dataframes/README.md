# Spark DataFrames and Spark SQL

DataFrames are the primary abstraction in Spark for working with structured data, providing a distributed collection of data organized into named columns. Spark SQL enables querying structured data using SQL syntax and the DataFrame API, with the Catalyst optimizer for query optimization.

## Table of Contents

1. [DataFrame Overview](#dataframe-overview)
2. [Creating DataFrames](#creating-dataframes)
3. [DataFrame Operations](#dataframe-operations)
4. [Spark SQL](#spark-sql)
5. [Catalyst Optimizer](#catalyst-optimizer)
6. [Advanced Features](#advanced-features)
7. [Performance Optimization](#performance-optimization)
8. [Best Practices](#best-practices)
9. [Common Patterns](#common-patterns)

---

## DataFrame Overview

### What is a DataFrame?

A DataFrame is a distributed collection of data organized into named columns, similar to a table in a relational database or a data frame in R/Python. Key characteristics:

- **Distributed**: Data is partitioned across multiple nodes
- **Typed**: Each column has a specific data type
- **Optimized**: Catalyst optimizer for query optimization
- **Lazy**: Operations are deferred until an action is triggered

### DataFrame vs RDD vs Dataset

| Feature | DataFrame | Dataset | RDD |
|---------|-----------|---------|-----|
| **Type Safety** | Runtime | Compile-time | Compile-time |
| **Schema** | Yes | Yes | No |
| **Optimization** | Catalyst | Catalyst | None |
| **API** | High-level | High-level | Low-level |
| **Language Support** | Python, Scala, Java, R | Scala, Java | Python, Scala, Java |
| **Use Case** | Structured data | Typed structured data | Unstructured data |

### DataFrame Architecture

```
DataFrame Architecture:
┌─────────────────────────────────────────────────────────────┐
│                      DataFrame API                           │
├─────────────────────────────────────────────────────────────┤
│                      Catalyst Optimizer                       │
│         (Logical Plan → Physical Plan → Code Generation)   │
├─────────────────────────────────────────────────────────────┤
│                      Tungsten Execution Engine               │
│         (Memory Management, Code Generation)               │
├─────────────────────────────────────────────────────────────┤
│                      RDD Layer                               │
│         (Distributed Execution)                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Creating DataFrames

### From Files

```python
from pyspark.sql import SparkSession

# Create Spark session
spark = SparkSession.builder \
    .appName("DataFrame Example") \
    .config("spark.some.config.option", "some-value") \
    .getOrCreate()

# From CSV file
df = spark.read.csv("hdfs://path/to/file.csv", header=True, inferSchema=True)

# From JSON file
df = spark.read.json("hdfs://path/to/file.json")

# From Parquet file
df = spark.read.parquet("hdfs://path/to/file.parquet")

# From ORC file
df = spark.read.orc("hdfs://path/to/file.orc")

# From text file
df = spark.read.text("hdfs://path/to/file.txt")

# From JDBC source
df = spark.read.jdbc(
    url="jdbc:mysql://host:3306/database",
    table="table_name",
    properties={"user": "username", "password": "password"}
)

# From Hive table
df = spark.sql("SELECT * FROM database.table_name")
```

### From RDDs

```python
# From RDD of tuples
rdd = sc.parallelize([(1, "Alice", 25), (2, "Bob", 30)])
df = rdd.toDF(["id", "name", "age"])

# From RDD with schema
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

schema = StructType([
    StructField("id", IntegerType(), True),
    StructField("name", StringType(), True),
    StructField("age", IntegerType(), True)
])

df = spark.createDataFrame(rdd, schema)

# From RDD of dictionaries
rdd = sc.parallelize([
    {"id": 1, "name": "Alice", "age": 25},
    {"id": 2, "name": "Bob", "age": 30}
])
df = spark.createDataFrame(rdd)
```

### From Other Sources

```python
# From Pandas DataFrame
import pandas as pd
pandas_df = pd.DataFrame({"id": [1, 2], "name": ["Alice", "Bob"]})
spark_df = spark.createDataFrame(pandas_df)

# From list of dictionaries
data = [
    {"id": 1, "name": "Alice", "age": 25},
    {"id": 2, "name": "Bob", "age": 30}
]
df = spark.createDataFrame(data)

# From list of tuples
data = [(1, "Alice", 25), (2, "Bob", 30)]
df = spark.createDataFrame(data, ["id", "name", "age"])
```

---

## DataFrame Operations

### Selection and Filtering

```python
# Select columns
df.select("name", "age")
df.select(df.name, df.age)
df.select(col("name"), col("age"))

# Filter rows
df.filter(df.age > 25)
df.where(df.age > 25)

# Complex filters
df.filter((df.age > 25) & (df.name.startswith("A")))
df.where("age > 25 AND name LIKE 'A%'")

# Select distinct values
df.select("name").distinct()

# Limit rows
df.limit(10)

# Sample data
df.sample(fraction=0.1, seed=42)
```

### Aggregations

```python
# Basic aggregations
df.agg({"age": "mean", "salary": "sum"})
df.select(avg("age"), sum("salary"), count("*"))

# Group by
df.groupBy("department").agg(
    avg("salary").alias("avg_salary"),
    max("salary").alias("max_salary"),
    count("*").alias("count")
)

# Multiple aggregations
df.groupBy("department", "level").agg(
    avg("salary").alias("avg_salary"),
    sum("salary").alias("total_salary"),
    count("*").alias("count")
)

# Window functions
from pyspark.sql import Window
from pyspark.sql.functions import rank, dense_rank, row_number

windowSpec = Window.partitionBy("department").orderBy(df.salary.desc())
df.withColumn("rank", rank().over(windowSpec))
df.withColumn("dense_rank", dense_rank().over(windowSpec))
df.withColumn("row_number", row_number().over(windowSpec))
```

### Joins

```python
# Inner join
joined_df = df1.join(df2, df1.id == df2.user_id)

# Left join
joined_df = df1.join(df2, df1.id == df2.user_id, "left")

# Right join
joined_df = df1.join(df2, df1.id == df2.user_id, "right")

# Full outer join
joined_df = df1.join(df2, df1.id == df2.user_id, "outer")

# Cross join
cross_df = df1.crossJoin(df2)

# Join on multiple conditions
joined_df = df1.join(df2, 
    (df1.id == df2.user_id) & (df1.date == df2.date))

# Using column names
joined_df = df1.join(df2, ["id", "name"])

# Self join
self_joined = df.alias("a").join(df.alias("b"), 
    col("a.manager_id") == col("b.id"))
```

### Sorting and Ordering

```python
# Sort ascending
df.sort("name")
df.orderBy("name")

# Sort descending
df.sort(df.name.desc())
df.orderBy(df.name.desc())

# Multiple sort columns
df.sort("department", df.salary.desc())

# Nulls handling
df.sort(df.name.asc_nulls_last())
df.sort(df.name.desc_nulls_first())
```

### Mutations

```python
# Add new column
from pyspark.sql.functions import lit, col, when

df.withColumn("new_column", lit("default_value"))
df.withColumn("bonus", df.salary * 0.1)

# Conditional column
df.withColumn("category", 
    when(df.age > 30, "Senior")
    .when(df.age > 20, "Mid")
    .otherwise("Junior"))

# Rename column
df.withColumnRenamed("old_name", "new_name")

# Drop column
df.drop("column_name")

# Cast column type
df.withColumn("age", df.age.cast("string"))

# Replace values
df.withColumn("name", 
    when(df.name == "Alice", "A")
    .otherwise(df.name))
```

### String Operations

```python
from pyspark.sql.functions import upper, lower, trim, concat, substring

# String transformations
df.withColumn("name_upper", upper(df.name))
df.withColumn("name_lower", lower(df.name))
df.withColumn("name_trim", trim(df.name))

# String concatenation
df.withColumn("full_name", concat(df.first_name, lit(" "), df.last_name))

# Substring
df.withColumn("name_short", substring(df.name, 1, 3))

# String length
df.withColumn("name_length", length(df.name))

# Regex operations
df.withColumn("email_domain", 
    regexp_extract(df.email, "@(.*)$", 1))

# Split string
df.withColumn("name_parts", split(df.name, " "))
```

### Date and Time Operations

```python
from pyspark.sql.functions import current_date, current_timestamp, datediff, date_format

# Current date and timestamp
df.withColumn("current_date", current_date())
df.withColumn("current_timestamp", current_timestamp())

# Date difference
df.withColumn("days_since_join", 
    datediff(current_date(), df.join_date))

# Date formatting
df.withColumn("formatted_date", 
    date_format(df.date, "yyyy-MM-dd"))

# Extract date components
from pyspark.sql.functions import year, month, dayofmonth

df.withColumn("year", year(df.date))
df.withColumn("month", month(df.date))
df.withColumn("day", dayofmonth(df.date))

# Date arithmetic
from pyspark.sql.functions import date_add, date_sub

df.withColumn("next_week", date_add(df.date, 7))
df.withColumn("last_week", date_sub(df.date, 7))
```

---

## Spark SQL

### Creating Temporary Views

```python
# Create temporary view
df.createOrReplaceTempView("employees")

# Create global temporary view
df.createOrReplaceGlobalTempView("employees")

# Register as table
spark.catalog.registerTempTable("employees", df)
```

### Running SQL Queries

```python
# Simple query
result = spark.sql("SELECT * FROM employees WHERE age > 25")

# Aggregation query
result = spark.sql("""
    SELECT department, AVG(salary) as avg_salary
    FROM employees
    GROUP BY department
    HAVING AVG(salary) > 50000
""")

# Join query
result = spark.sql("""
    SELECT e.name, d.department_name
    FROM employees e
    JOIN departments d ON e.department_id = d.id
""")

# Subquery
result = spark.sql("""
    SELECT * FROM employees
    WHERE salary > (SELECT AVG(salary) FROM employees)
""")

# Window function
result = spark.sql("""
    SELECT name, salary,
        RANK() OVER (PARTITION BY department ORDER BY salary DESC) as rank
    FROM employees
""")
```

### SQL Functions

```python
# Built-in functions
from pyspark.sql.functions import (
    col, lit, when, coalesce, nvl, 
    sum, avg, count, min, max,
    year, month, dayofmonth,
    upper, lower, trim, concat,
    date_format, datediff, current_date
)

# Aggregate functions
spark.sql("""
    SELECT 
        department,
        COUNT(*) as count,
        AVG(salary) as avg_salary,
        MAX(salary) as max_salary,
        MIN(salary) as min_salary
    FROM employees
    GROUP BY department
""")

# String functions
spark.sql("""
    SELECT 
        UPPER(name) as name_upper,
        LOWER(email) as email_lower,
        CONCAT(first_name, ' ', last_name) as full_name
    FROM employees
""")

# Date functions
spark.sql("""
    SELECT 
        name,
        YEAR(join_date) as join_year,
        DATEDIFF(CURRENT_DATE(), join_date) as days_employed
    FROM employees
""")
```

### User-Defined Functions (UDFs)

```python
from pyspark.sql.functions import udf
from pyspark.sql.types import StringType, IntegerType

# Define UDF
@udf(returnType=StringType())
def format_name(name):
    return name.title()

# Register UDF
spark.udf.register("format_name", format_name, StringType())

# Use in DataFrame
df.withColumn("formatted_name", format_name(df.name))

# Use in SQL
spark.sql("SELECT format_name(name) FROM employees")

# Pandas UDF for better performance
from pyspark.sql.functions import pandas_udf
import pandas as pd

@pandas_udf(IntegerType())
def add_one(s: pd.Series) -> pd.Series:
    return s + 1

df.withColumn("age_plus_one", add_one(df.age))
```

---

## Catalyst Optimizer

### How Catalyst Works

Catalyst is Spark SQL's query optimizer that transforms logical plans into optimized physical plans:

```
Catalyst Optimization Pipeline:
┌─────────────────────────────────────────────────────────────┐
│                      Unresolved Logical Plan                  │
│         (Parsed from SQL or DataFrame API)                  │
├─────────────────────────────────────────────────────────────┤
│                      Analysis Phase                          │
│         (Resolve references, check types)                   │
├─────────────────────────────────────────────────────────────┤
│                      Logical Optimization                    │
│         (Predicate pushdown, constant folding)              │
├─────────────────────────────────────────────────────────────┤
│                      Physical Planning                       │
│         (Select join strategies, scan methods)              │
├─────────────────────────────────────────────────────────────┤
│                      Code Generation                          │
│         (Generate optimized Java bytecode)                  │
├─────────────────────────────────────────────────────────────┤
│                      Execution                               │
│         (Execute optimized plan)                            │
└─────────────────────────────────────────────────────────────┘
```

### Optimization Rules

```python
# Catalyst applies various optimization rules:

# 1. Predicate Pushdown
# Pushes filters down to data source
df.filter(df.age > 25).select("name", "age")
# Optimized: Read only relevant data

# 2. Projection Pruning
# Reads only required columns
df.select("name", "age")
# Optimized: Read only "name" and "age" columns

# 3. Constant Folding
# Evaluates constant expressions at compile time
df.withColumn("result", lit(2) + lit(3))
# Optimized: df.withColumn("result", lit(5))

# 4. Column Pruning
# Removes unused columns from intermediate results
df.select("name").filter(df.age > 25)
# Optimized: Filter before select

# 5. Join Reordering
# Reorders joins for better performance
df1.join(df2, "id").join(df3, "id")
# Optimized: Choose optimal join order

# 6. Broadcast Join
# Broadcasts small tables
df1.join(df2, "id")  # if df2 is small
# Optimized: Broadcast df2 to all nodes
```

### Explain Plans

```python
# View logical plan
df.explain(True)

# View optimized plan
df.filter(df.age > 25).select("name").explain()

# Output example:
# == Optimized Logical Plan ==
# Project [name#1]
# +- Filter (age#2 > 25)
#    +- Relation[id#0, name#1, age#2] parquet

# View physical plan
df.filter(df.age > 25).select("name").explain("extended")

# View code generation
df.filter(df.age > 25).select("name").explain("codegen")
```

---

## Advanced Features

### Window Functions

```python
from pyspark.sql import Window
from pyspark.sql.functions import (
    rank, dense_rank, row_number, 
    lead, lag, first, last,
    sum, avg, count
)

# Define window specification
windowSpec = Window.partitionBy("department").orderBy("salary")

# Ranking functions
df.withColumn("rank", rank().over(windowSpec))
df.withColumn("dense_rank", dense_rank().over(windowSpec))
df.withColumn("row_number", row_number().over(windowSpec))

# Analytic functions
df.withColumn("next_salary", lead("salary", 1).over(windowSpec))
df.withColumn("prev_salary", lag("salary", 1).over(windowSpec))

# Aggregate window functions
windowSpec = Window.partitionBy("department")
df.withColumn("dept_avg", avg("salary").over(windowSpec))
df.withColumn("dept_total", sum("salary").over(windowSpec))

# Running total
windowSpec = Window.partitionBy("department").orderBy("date")
df.withColumn("running_total", sum("salary").over(windowSpec))

# Moving average
windowSpec = Window.partitionBy("department").orderBy("date").rowsBetween(-2, 0)
df.withColumn("moving_avg", avg("salary").over(windowSpec))
```

### Pivot and Unpivot

```python
# Pivot: Convert rows to columns
df.groupBy("name").pivot("subject").avg("score")

# Pivot with specific values
df.groupBy("name").pivot("subject", ["Math", "Science", "English"]).avg("score")

# Unpivot: Convert columns to rows
from pyspark.sql.functions import stack

df.select("name", 
    stack(3, "Math", "Math_score", 
          "Science", "Science_score", 
          "English", "English_score")
).withColumnRenamed("col0", "subject")
 .withColumnRenamed("col1", "score")
```

### Advanced Aggregations

```python
# Cube: All combinations of grouping columns
df.cube("department", "level").agg(avg("salary"))

# Rollup: Hierarchical grouping
df.rollup("department", "level").agg(avg("salary"))

# Grouping sets
from pyspark.sql.functions import grouping, grouping_id

df.groupBy("department", "level").agg(
    avg("salary"),
    grouping("department").alias("dept_grouping"),
    grouping("level").alias("level_grouping")
)

# Collect list and set
df.groupBy("department").agg(
    collect_list("name").alias("employees"),
    collect_set("level").alias("levels")
)

# Flatten arrays
from pyspark.sql.functions import flatten

df.withColumn("flat_employees", flatten(df.employees))
```

### Complex Data Types

```python
from pyspark.sql.types import (
    StructType, StructField, StringType, 
    IntegerType, ArrayType, MapType
)

# Array columns
df.withColumn("skills_array", split(df.skills, ","))
df.withColumn("skills_count", size(df.skills_array))
df.withColumn("has_skill", array_contains(df.skills_array, "Python"))

# Map columns
df.withColumn("metadata", 
    create_map(
        lit("key1"), col("value1"),
        lit("key2"), col("value2")
    ))

# Nested structures
schema = StructType([
    StructField("id", IntegerType(), True),
    StructField("name", StringType(), True),
    StructField("address", StructType([
        StructField("street", StringType(), True),
        StructField("city", StringType(), True),
        StructField("state", StringType(), True)
    ]), True),
    StructField("skills", ArrayType(StringType()), True)
])

# Access nested fields
df.select(df.address.city)
df.select(df.skills[0])
```

---

## Performance Optimization

### Partitioning

```python
# Check current partitions
print(f"Number of partitions: {df.rdd.getNumPartitions()}")

# Repartition by column
df = df.repartition("department")

# Coalesce to reduce partitions
df = df.coalesce(10)

# Custom partitioning
df = df.repartition(10, "department")

# Write with partitioning
df.write.partitionBy("department", "year").parquet("output_path")

# Bucketing
df.write.bucketBy(10, "id").sortBy("id").saveAsTable("bucketed_table")
```

### Caching

```python
# Cache DataFrame
df.cache()

# Persist with specific storage level
from pyspark import StorageLevel
df.persist(StorageLevel.MEMORY_AND_DISK)

# Check if cached
print(df.is_cached)

# Unpersist
df.unpersist()

# Cache strategy
# Cache before multiple operations
df.cache()
df.filter(df.age > 25).count()
df.groupBy("department").count()
df.unpersist()
```

### Broadcast Variables

```python
from pyspark.sql.functions import broadcast

# Broadcast small DataFrame for join
result = df1.join(broadcast(df2), "id")

# Configure broadcast threshold
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "10m")

# Manual broadcast
broadcast_df = broadcast(df2)
result = df1.join(broadcast_df, "id")
```

### AQE (Adaptive Query Execution)

```python
# Enable AQE
spark.conf.set("spark.sql.adaptive.enabled", "true")
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", "true")
spark.conf.set("spark.sql.adaptive.skewJoin.enabled", "true")

# AQE features:
# 1. Coalesce partitions after shuffle
# 2. Handle skewed joins
# 3. Optimize join strategies
# 4. Dynamic partition pruning
```

### Vectorized Execution

```python
# Enable vectorized execution
spark.conf.set("spark.sql.orc.enableVectorizedReader", "true")
spark.conf.set("spark.sql.parquet.enableVectorizedReader", "true")

# Vectorized execution processes data in batches
# for better CPU utilization
```

---

## Best Practices

### 1. Use Appropriate Data Sources

```python
# Use Parquet for analytics
df.write.parquet("output_path")

# Use ORC for Hive compatibility
df.write.orc("output_path")

# Use JSON for semi-structured data
df.write.json("output_path")

# Use JDBC for relational databases
df.write.jdbc(url="jdbc:...", table="table", properties={})
```

### 2. Optimize Schema

```python
# Use appropriate data types
df = df.withColumn("id", col("id").cast("integer"))
df = df.withColumn("amount", col("amount").cast("decimal(10,2)"))

# Avoid unnecessary columns
df = df.select("col1", "col2", "col3")

# Use nullable=false when appropriate
schema = StructType([
    StructField("id", IntegerType(), False),
    StructField("name", StringType(), True)
])
```

### 3. Minimize Shuffles

```python
# Use broadcast joins for small tables
result = df1.join(broadcast(df2), "id")

# Use reduceByKey instead of groupByKey
df.groupBy("key").agg(sum("value"))  # Good

# Partition data appropriately
df = df.repartition("join_key")
```

### 4. Cache Strategically

```python
# Cache frequently used DataFrames
frequently_used_df = spark.read.parquet("large_dataset")
frequently_used_df.cache()

# Use appropriate storage level
large_df.persist(StorageLevel.MEMORY_AND_DISK)

# Unpersist when done
frequently_used_df.unpersist()
```

### 5. Use SQL When Appropriate

```python
# Use SQL for complex queries
result = spark.sql("""
    SELECT department, AVG(salary)
    FROM employees
    GROUP BY department
    HAVING AVG(salary) > 50000
""")

# Use DataFrame API for programmatic transformations
result = df.groupBy("department").agg(avg("salary"))
```

---

## Common Patterns

### Pattern 1: ETL Pipeline

```python
# Extract
raw_df = spark.read.json("hdfs://path/to/raw_data")

# Transform
cleaned_df = (raw_df
    .filter(col("id").isNotNull())
    .withColumn("date", to_date(col("timestamp")))
    .withColumn("amount", col("amount").cast("decimal(10,2)"))
    .groupBy("user_id", "date")
    .agg(sum("amount").alias("total_amount")))

# Load
cleaned_df.write.partitionBy("date").parquet("hdfs://path/to/clean_data")
```

### Pattern 2: Data Quality Checks

```python
from pyspark.sql.functions import col, count, when, isnan

# Check for nulls
null_counts = df.select([
    count(when(col(c).isNull(), c)).alias(c) for c in df.columns
])

# Check for duplicates
duplicate_count = df.count() - df.dropDuplicates().count()

# Check data types
schema_info = df.schema

# Check statistics
df.describe().show()
```

### Pattern 3: Slowly Changing Dimensions

```python
# SCD Type 1: Overwrite
new_df.write.mode("overwrite").parquet("output_path")

# SCD Type 2: Historical tracking
from pyspark.sql.functions import current_timestamp, lit

# Add effective dates
new_df = new_df.withColumn("effective_date", current_timestamp())
new_df = new_df.withColumn("expiry_date", lit(None))

# Union with historical data
full_df = historical_df.union(new_df)
```

### Pattern 4: Data Validation

```python
from pyspark.sql.functions import col, when, count

# Define validation rules
validated_df = df.withColumn(
    "is_valid",
    when(
        (col("age") > 0) & 
        (col("age") < 150) & 
        (col("email").contains("@")),
        True
    ).otherwise(False)
)

# Get validation statistics
validation_stats = validated_df.groupBy("is_valid").count()
```

### Pattern 5: Feature Engineering

```python
from pyspark.sql.functions import when, col, lit

# Create categorical features
df = df.withColumn(
    "age_group",
    when(col("age") < 25, "Young")
    .when(col("age") < 45, "Middle")
    .otherwise("Senior")
)

# Create numerical features
df = df.withColumn("income_per_age", col("income") / col("age"))

# Create interaction features
df = df.withColumn("feature_interaction", col("feature1") * col("feature2"))
```

---

## Conclusion

Spark DataFrames and Spark SQL provide:

- **High-level API** for working with structured data
- **Catalyst optimizer** for automatic query optimization
- **Tungsten execution engine** for efficient execution
- **Multiple data source support** for flexibility
- **Rich function library** for data transformations

Key takeaways:

1. **Use DataFrames** over RDDs for structured data
2. **Leverage Catalyst** optimizations automatically
3. **Choose appropriate data formats** (Parquet, ORC)
4. **Optimize joins** with broadcast variables
5. **Cache strategically** for iterative workloads

DataFrames are the recommended abstraction for most Spark applications, providing the best balance of performance, ease of use, and flexibility.