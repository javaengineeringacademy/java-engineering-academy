# Spark SQL

## Table of Contents

- [Overview](#overview)
- [DataFrame API](#dataframe-api)
- [SQL Queries](#sql-queries)
- [Catalyst Optimizer](#catalyst-optimizer)
- [Data Sources](#data-sources)
- [Window Functions](#window-functions)
- [UDFs and UDAFs](#udfs-and-udafs)
- [Performance Optimization](#performance-optimization)
- [Advanced Features](#advanced-features)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Spark SQL is a Spark module for structured data processing providing a
programming abstraction called DataFrames and SQL interface for querying
data. It supports reading from various sources including Hive tables, JSON,
Parquet, and JDBC.

### Key Characteristics

- **DataFrame abstraction**: Structured data with schema
- **SQL support**: Standard SQL queries on Spark data
- **Catalyst optimizer**: Automatic query optimization
- **Vectorized execution**: Columnar processing
- **Multiple data sources**: JSON, Parquet, ORC, JDBC, Hive

### When to Use Spark SQL

- Structured data processing and analysis
- SQL-based analytics on big data
- ETL pipelines with structured schemas
- Interoperability between SQL and programmatic access
- Performance-critical data processing

---

## DataFrame API

### Creating DataFrames

```python
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

spark = SparkSession.builder.appName("SparkSQL").getOrCreate()

# From list of tuples
data = [("Alice", 34), ("Bob", 45), ("Charlie", 29)]
df = spark.createDataFrame(data, ["name", "age"])

# From list of dictionaries
data = [{"name": "Alice", "age": 34}, {"name": "Bob", "age": 45}]
df = spark.createDataFrame(data)

# From RDD
rdd = sc.parallelize([("Alice", 34), ("Bob", 45)])
df = spark.createDataFrame(rdd, ["name", "age"])

# From JSON file
df = spark.read.json("people.json")

# From CSV file
df = spark.read.csv("people.csv", header=True, inferSchema=True)

# From Parquet file
df = spark.read.parquet("people.parquet")

# From JDBC source
df = spark.read.format("jdbc") \
    .option("url", "jdbc:postgresql://localhost/db") \
    .option("dbtable", "people") \
    .option("user", "user") \
    .option("password", "password") \
    .load()

# With explicit schema
schema = StructType([
    StructField("name", StringType(), True),
    StructField("age", IntegerType(), True)
])
df = spark.createDataFrame(data, schema)
```

### DataFrame Operations

```python
# Show data
df.show()
df.show(10, truncate=False)

# Print schema
df.printSchema()

# Display schema as tree
df.describe().show()

# Select columns
df.select("name", "age").show()
df.select(df.name, df.age).show()
from pyspark.sql.functions import col
df.select(col("name"), col("age")).show()

# Filter rows
df.filter(df.age > 30).show()
df.where(df.age > 30).show()
df.filter("age > 30").show()

# Add columns
from pyspark.sql.functions import lit
df.withColumn("country", lit("USA")).show()
df.withColumn("age_doubled", df.age * 2).show()

# Rename columns
df.withColumnRenamed("name", "full_name").show()

# Drop columns
df.drop("age").show()

# Distinct values
df.distinct().show()

# Drop duplicates
df.dropDuplicates(["name"]).show()

# Sort data
df.orderBy("age").show()
df.orderBy(df.age.desc()).show()

# Limit results
df.limit(10).show()

# Cache DataFrame
df.cache()

# Unpersist
df.unpersist()
```

### DataFrame Aggregations

```python
from pyspark.sql.functions import (
    count, countDistinct, sum, avg, min, max,
    first, last, lit, round, collect_list, collect_set
)

# Group by
df.groupBy("department").count().show()

# Multiple aggregations
df.groupBy("department").agg(
    count("*").alias("employee_count"),
    avg("salary").alias("avg_salary"),
    max("salary").alias("max_salary"),
    sum("salary").alias("total_salary")
).show()

# Count distinct
df.select(countDistinct("department")).show()

# Collect list
df.groupBy("department").agg(
    collect_list("name").alias("employees")
).show()

# Pivot
df.groupBy("department").pivot("year").sum("salary").show()
```

---

## SQL Queries

### Registering Temp Views

```python
# Create temporary view
df.createOrReplaceTempView("people")

# Create global temporary view
df.createOrReplaceGlobalTempView("people")

# Register as table
spark.catalog.registerTable("people", df)

# List tables
spark.catalog.listTables()

# Check if table exists
spark.catalog.tableExists("people")
```

### Running SQL Queries

```python
# Simple SELECT
result = spark.sql("SELECT name, age FROM people WHERE age > 30")
result.show()

# Aggregation
result = spark.sql("""
    SELECT department, COUNT(*) as count, AVG(salary) as avg_salary
    FROM employees
    GROUP BY department
    HAVING COUNT(*) > 5
""")
result.show()

# Join
result = spark.sql("""
    SELECT e.name, e.salary, d.department_name
    FROM employees e
    JOIN departments d ON e.dept_id = d.id
""")
result.show()

# Subquery
result = spark.sql("""
    SELECT name, salary
    FROM employees
    WHERE salary > (SELECT AVG(salary) FROM employees)
""")
result.show()

# Window function
result = spark.sql("""
    SELECT name, department, salary,
        ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as rank
    FROM employees
""")
result.show()

# CTE (Common Table Expression)
result = spark.sql("""
    WITH dept_stats AS (
        SELECT department, AVG(salary) as avg_salary
        FROM employees
        GROUP BY department
    )
    SELECT e.name, e.salary, ds.avg_salary
    FROM employees e
    JOIN dept_stats ds ON e.department = ds.department
""")
result.show()
```

### Using Variables in SQL

```python
# Using string formatting (NOT recommended - SQL injection risk)
table_name = "employees"
df = spark.sql(f"SELECT * FROM {table_name}")

# Using parameters (recommended)
params = {"min_salary": 50000}
df = spark.sql("SELECT * FROM employees WHERE salary > :min_salary", params=params)
```

---

## Catalyst Optimizer

### Optimization Phases

```
┌──────────────────────────────────────────────────────────────┐
│                    Catalyst Optimizer                         │
├──────────────────────────────────────────────────────────────┤
│  1. Analysis                                                  │
│     └─ Resolve references, check schema validity             │
│                                                               │
│  2. Logical Optimization                                      │
│     ├─ Predicate pushdown                                     │
│     ├─ Column pruning                                         │
│     ├─ Constant folding                                       │
│     ├─ Query rewrite                                          │
│     └─ Join reordering                                        │
│                                                               │
│  3. Physical Planning                                         │
│     ├─ Multiple physical plans                                │
│     ├─ Cost-based optimization                                │
│     └─ Select best plan                                       │
│                                                               │
│  4. Code Generation                                           │
│     ├─ Whole-stage code generation                            │
│     ├─ Tungsten execution engine                              │
│     └─ Optimized bytecode                                     │
└──────────────────────────────────────────────────────────────┘
```

### Viewing Query Plans

```python
# Explain the query plan
df.explain()

# Show extended plan
df.explain(True)

# Show formatted plan
df.explain("formatted")

# Show mode-specific plan
df.explain("simple")
df.explain("extended")
df.explain("codegen")
df.explain("parsed")
df.explain("optimized")
df.explain("execution")
```

### Optimization Rules

```python
# Predicate pushdown
# Before optimization:
# Filter -> Join -> Scan
# After optimization:
# Join -> Filter -> Scan

# Column pruning
# Before: SELECT * FROM table
# After: Only read required columns

# Constant folding
# Before: WHERE 1 = 1 AND age > 30
# After: WHERE age > 30

# Join reordering
# Reorder joins based on table sizes and selectivity
```

---

## Data Sources

### Built-in Formats

```python
# JSON
df = spark.read.json("data.json")
df.write.json("output.json")

# CSV
df = spark.read.csv("data.csv", header=True, inferSchema=True)
df.write.csv("output.csv", header=True)

# Parquet (columnar, compressed)
df = spark.read.parquet("data.parquet")
df.write.parquet("output.parquet", compression="snappy")

# ORC (optimized columnar)
df = spark.read.orc("data.orc")
df.write.orc("output.orc", compression="zlib")

# Text
df = spark.read.text("data.txt")
df.write.text("output.txt")

# Avro
df = spark.read.format("avro").load("data.avro")
df.write.format("avro").save("output.avro")
```

### JDBC Sources

```python
# Read from database
df = spark.read.format("jdbc") \
    .option("url", "jdbc:postgresql://localhost/mydb") \
    .option("dbtable", "public.users") \
    .option("user", "postgres") \
    .option("password", "password") \
    .option("driver", "org.postgresql.Driver") \
    .option("fetchsize", 10000) \
    .option("partitionColumn", "id") \
    .option("lowerBound", 1) \
    .option("upperBound", 1000000) \
    .option("numPartitions", 10) \
    .load()

# Write to database
df.write.format("jdbc") \
    .option("url", "jdbc:postgresql://localhost/mydb") \
    .option("dbtable", "public.users") \
    .option("user", "postgres") \
    .option("password", "password") \
    .mode("overwrite") \
    .option("batchsize", 10000) \
    .save()

# Using properties
properties = {
    "user": "postgres",
    "password": "password",
    "driver": "org.postgresql.Driver"
}
df = spark.read.jdbc("jdbc:postgresql://localhost/mydb", "users", properties=properties)
```

### Hive Integration

```python
# Enable Hive support
spark = SparkSession.builder \
    .enableHiveSupport() \
    .getOrCreate()

# Read Hive table
df = spark.sql("SELECT * FROM mydb.mytable")

# Write Hive table
df.write.saveAsTable("mydb.mytable")

# Write as managed table
df.write.mode("overwrite").saveAsTable("mytable")

# Write as external table
df.write.mode("overwrite") \
    .option("path", "/external/path") \
    .saveAsTable("external_table")
```

---

## Window Functions

### Window Specifications

```python
from pyspark.sql.window import Window
from pyspark.sql.functions import (
    row_number, rank, dense_rank, ntile,
    lag, lead, first_value, last_value
)

# Define window specification
windowSpec = Window.partitionBy("department").orderBy("salary")

# Range-based window
rangeWindow = Window.partitionBy("department") \
    .orderBy("hire_date") \
    .rangeBetween(-365, 0)  # Last year

# Rows-based window
rowsWindow = Window.partitionBy("department") \
    .orderBy("hire_date") \
    .rowsBetween(-10, 0)  # Last 10 rows
```

### Ranking Functions

```python
# Row number
df.withColumn("row_num", row_number().over(windowSpec)).show()

# Rank
df.withColumn("rank", rank().over(windowSpec)).show()

# Dense rank
df.withColumn("dense_rank", dense_rank().over(windowSpec)).show()

# Percent rank
df.withColumn("percent_rank", percent_rank().over(windowSpec)).show()

# N-tile
df.withColumn("ntile", ntile(4).over(windowSpec)).show()
```

### Analytic Functions

```python
# Lag
df.withColumn("prev_salary", lag("salary", 1).over(windowSpec)).show()

# Lead
df.withColumn("next_salary", lead("salary", 1).over(windowSpec)).show()

# First value
df.withColumn("first_salary", first_value("salary").over(windowSpec)).show()

# Last value
df.withColumn("last_salary", last_value("salary").over(windowSpec)).show()
```

### Aggregate Window Functions

```python
# Running total
windowRunning = Window.partitionBy("department") \
    .orderBy("hire_date") \
    .rowsBetween(Window.unboundedPreceding, Window.currentRow)

df.withColumn("running_total", sum("salary").over(windowRunning)).show()

# Moving average
windowMoving = Window.partitionBy("department") \
    .orderBy("hire_date") \
    .rowsBetween(-2, 0)  # Last 3 rows

df.withColumn("moving_avg", avg("salary").over(windowMoving)).show()

# Cumulative distribution
df.withColumn("cum_dist", cume_dist().over(windowSpec)).show()
```

---

## UDFs and UDAFs

### User Defined Functions (UDFs)

```python
from pyspark.sql.functions import udf
from pyspark.sql.types import StringType, IntegerType

# Define UDF
@udf(returnType=StringType())
def upper_case(name):
    if name is None:
        return None
    return name.upper()

# Register UDF
spark.udf.register("upper_case", upper_case)

# Use UDF in DataFrame
df.withColumn("name_upper", upper_case(df.name)).show()

# Use UDF in SQL
df.createOrReplaceTempView("people")
spark.sql("SELECT upper_case(name) FROM people").show()

# UDF with multiple parameters
@udf(returnType=IntegerType())
def calculate_bonus(salary, years):
    if salary is None or years is None:
        return None
    return int(salary * 0.1 * years)
```

### User Defined Aggregate Functions (UDAFs)

```python
from pyspark.sql import Row
from pyspark.sql.types import DoubleType
from pyspark.sql.functions import UserDefinedAggregateFunction

# Define UDAF
class MeanUDAF(UserDefinedAggregateFunction):
    def _init_(self):
        self._sum = 0.0
        self._count = 0

    @property
    def inputSchema(self):
        return StructType([StructField("value", DoubleType(), True)])

    @property
    def bufferSchema(self):
        return StructType([
            StructField("sum", DoubleType(), True),
            StructField("count", DoubleType(), True)
        ])

    @property
    def dataType(self):
        return DoubleType()

    @property
    def deterministic(self):
        return True

    def initialize(self, buffer):
        buffer['sum'] = 0.0
        buffer['count'] = 0.0

    def update(self, buffer, input):
        if input[0] is not None:
            buffer['sum'] += input[0]
            buffer['count'] += 1.0

    def merge(self, buffer1, buffer2):
        buffer1['sum'] += buffer2['sum']
        buffer1['count'] += buffer2['count']

    def evaluate(self, buffer):
        if buffer['count'] == 0:
            return 0.0
        return buffer['sum'] / buffer['count']

# Register UDAF
mean_udaf = MeanUDAF()
spark.udf.register("mean_udaf", mean_udaf)

# Use UDAF
df.groupBy("department").agg(mean_udaf(df.salary).alias("avg_salary")).show()
```

---

## Performance Optimization

### Partitioning

```python
# Optimal partition count
# Rule: 200MB per partition for large datasets
partition_count = total_data_size / (200 * 1024 * 1024)

# Repartition before writing
df.repartition(100).write.parquet("output")

# Repartition by column
df.repartition("department").write.parquet("output")

# Coalesce for reducing partitions
df.coalesce(10).write.parquet("output")
```

### Caching

```python
# Cache DataFrame
df.cache()

# Cache with storage level
from pyspark import StorageLevel
df.persist(StorageLevel.MEMORY_AND_DISK)

# Check if cached
print(df.is_cached)

# Unpersist
df.unpersist()
```

### File Format Optimization

```python
# Parquet with compression
df.write \
    .mode("overwrite") \
    .option("compression", "snappy") \
    .parquet("output")

# Partitioned Parquet
df.write \
    .mode("overwrite") \
    .partitionBy("year", "month") \
    .parquet("output")

# Bucketed output
df.write \
    .mode("overwrite") \
    .bucketBy(100, "user_id") \
    .sortBy("user_id") \
    .saveAsTable("bucketed_table")
```

### Configuration Tuning

```python
# Shuffle partitions
spark.conf.set("spark.sql.shuffle.partitions", 200)

# Adaptive query execution
spark.conf.set("spark.sql.adaptive.enabled", True)
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", True)
spark.conf.set("spark.sql.adaptive.skewJoin.enabled", True)

# Broadcast join threshold
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", 10 * 1024 * 1024)  # 10MB

# Broadcast timeout
spark.conf.set("spark.sql.broadcastTimeout", 600)

# Code generation
spark.conf.set("spark.sql.codegen.wholeStage", True)

# Columnar batch size
spark.conf.set("spark.sql.inMemoryColumnarStorage.batchSize", 10000)

# Vectorized reader
spark.conf.set("spark.sql.parquet.enableVectorizedReader", True)
spark.conf.set("spark.sql.orc.enableVectorizedReader", True)
```

---

## Advanced Features

### Data Time Operations

```python
from pyspark.sql.functions import (
    current_date, current_timestamp, date_format,
    to_date, to_timestamp, datediff, date_add, date_sub,
    year, month, dayofmonth, dayofweek, hour, minute, second,
    extract
)

# Current date/time
df.withColumn("current_date", current_date()).show()
df.withColumn("current_timestamp", current_timestamp()).show()

# Date formatting
df.withColumn("formatted", date_format("date", "yyyy-MM-dd")).show()

# Date parsing
df.withColumn("parsed", to_date("date_string", "yyyy-MM-dd")).show()

# Date arithmetic
df.withColumn("next_week", date_add("date", 7)).show()
df.withColumn("last_week", date_sub("date", 7)).show()
df.withColumn("days_diff", datediff("end_date", "start_date")).show()

# Extract components
df.withColumn("year", year("date")).show()
df.withColumn("month", month("date")).show()
df.withColumn("day", dayofmonth("date")).show()
```

### String Operations

```python
from pyspark.sql.functions import (
    concat, concat_ws, length, lower, upper, initcap,
    trim, ltrim, rtrim, lpad, rpad, regexp_replace,
    regexp_extract, substring, split, array_contains
)

# Concatenation
df.withColumn("full_name", concat("first_name", lit(" "), "last_name")).show()
df.withColumn("full_name", concat_ws(" ", "first_name", "last_name")).show()

# String manipulation
df.withColumn("name_upper", upper("name")).show()
df.withColumn("name_lower", lower("name")).show()
df.withColumn("name_initcap", initcap("name")).show()
df.withColumn("name_trim", trim("name")).show()
df.withColumn("name_padded", lpad("name", 10, "*")).show()

# String operations
df.withColumn("name_length", length("name")).show()
df.withColumn("cleaned", regexp_replace("name", "[^a-zA-Z]", "")).show()
df.withColumn("extracted", regexp_extract("email", "@(.+)$", 1)).show()
df.withColumn("substr", substring("name", 1, 3)).show()

# Split and array operations
df.withColumn("words", split("text", " ")).show()
df.withColumn("first_word", split("text", " ")[0]).show()
```

---

## Best Practices

### Schema Design

1. **Use appropriate data types**: Don't use STRING for dates or numbers
2. **Partition wisely**: Low-cardinality columns (year, month, region)
3. **Bucket high-cardinality columns**: For efficient joins
4. **Use columnar formats**: Parquet or ORC for analytical queries

### Query Optimization

1. **Select specific columns**: Avoid `SELECT *`
2. **Filter early**: Push predicates down
3. **Broadcast small tables**: Use `broadcast()` hint
4. **Cache frequently used DataFrames**: Avoid recomputation

### Code Quality

1. **Use DataFrame API over SQL**: Better type safety and optimization
2. **Avoid UDFs when possible**: Built-in functions are optimized
3. **Test with sample data**: Use `limit()` for quick validation
4. **Monitor query plans**: Use `explain()` to understand execution

### Performance Monitoring

```python
# Monitor query performance
spark.sparkContext._jsc.sc().getExecutorMemoryStatus()

# Check shuffle write
df.write.mode("overwrite").parquet("output")

# View Spark UI
# http://<driver>:4040
```

---

## Examples

### Complete Analytics Pipeline

```python
# Read data
sales = spark.read.parquet("sales.parquet")
customers = spark.read.parquet("customers.parquet")
products = spark.read.parquet("products.parquet")

# Join data
enriched = sales \
    .join(customers, "customer_id", "left") \
    .join(products, "product_id", "left")

# Add calculated columns
from pyspark.sql.functions import col, when, lit, current_date, datediff

analysis = enriched \
    .withColumn("revenue", col("quantity") * col("price")) \
    .withColumn("profit", col("revenue") - col("cost")) \
    .withColumn("customer_tenure", datediff(current_date(), col("signup_date"))) \
    .withColumn("customer_segment",
        when(col("customer_tenure") > 365, "Long-term")
        .when(col("customer_tenure") > 90, "Medium-term")
        .otherwise("New"))

# Aggregations
product_performance = analysis \
    .groupBy("product_name", "category") \
    .agg(
        sum("revenue").alias("total_revenue"),
        sum("profit").alias("total_profit"),
        count("order_id").alias("order_count"),
        avg("quantity").alias("avg_quantity")
    ) \
    .orderBy(col("total_revenue").desc())

# Write results
product_performance.write.mode("overwrite") \
    .partitionBy("category") \
    .parquet("output/product_performance")

# SQL analysis
analysis.createOrReplaceTempView("sales_analysis")
result = spark.sql("""
    SELECT
        customer_segment,
        category,
        SUM(revenue) as total_revenue,
        AVG(profit) as avg_profit,
        COUNT(DISTINCT customer_id) as unique_customers
    FROM sales_analysis
    GROUP BY customer_segment, category
    ORDER BY total_revenue DESC
""")
result.show()
```

---

## References

- [Spark SQL Programming Guide](https://spark.apache.org/docs/latest/sql-programming-guide.html)
- [DataFrame API Reference](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql.html)
- [Catalyst Optimizer](https://spark.apache.org/docs/latest/sql-optimizer.html)
- [Spark SQL Functions](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql/functions.html)
- [Learning Spark](http://shop.oreilly.com/product/0636920028512.do)
