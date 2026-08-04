# PySpark

## Table of Contents

- [Overview](#overview)
- [SparkSession and SparkContext](#sparksession-and-sparkcontext)
- [DataFrame API](#dataframe-api)
- [SQL Queries](#sql-queries)
- [RDD Operations](#rdd-operations)
- [Machine Learning (MLlib)](#machine-learning-mllib)
- [Streaming](#streaming)
- [Data Sources](#data-sources)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

PySpark is the Python API for Apache Spark, providing an interface for
interacting with Spark using Python. It allows Python developers to write
Spark applications and leverage the distributed computing capabilities
of Spark.

### Key Characteristics

- **Python integration**: Write Spark apps in Python
- **Full API coverage**: Most Spark features available in PySpark
- **Pandas integration**: `spark.createDataFrame(pandas_df)`
- **SQL support**: Run SQL queries on Spark
- **ML support**: PySpark MLlib for machine learning

### When to Use PySpark

- Data engineering pipelines in Python
- ETL processes on large datasets
- Machine learning at scale
- Interactive data analysis
- Integration with Python ecosystem (pandas, numpy, scikit-learn)

### PySpark vs Scala Spark

| Feature | PySpark | Scala Spark |
|---------|---------|-------------|
| Performance | Slower (serialization) | Faster (native) |
| Ease of Use | Easier | Steeper learning curve |
| API Coverage | Most features | Complete |
| Community | Larger | Smaller but dedicated |
| Python Integration | Native | Via py4j |

---

## SparkSession and SparkContext

### Creating SparkSession

```python
from pyspark.sql import SparkSession

# Basic session
spark = SparkSession.builder \
    .appName("MyApplication") \
    .getOrCreate()

# With configuration
spark = SparkSession.builder \
    .appName("MyApplication") \
    .master("local[*]") \
    .config("spark.sql.shuffle.partitions", "200") \
    .config("spark.driver.memory", "4g") \
    .config("spark.executor.memory", "8g") \
    .getOrCreate()

# With Hive support
spark = SparkSession.builder \
    .appName("MyApplication") \
    .enableHiveSupport() \
    .getOrCreate()

# Get existing session
spark = SparkSession.builder.getOrCreate()

# Stop session
spark.stop()
```

### SparkContext

```python
# Get SparkContext from SparkSession
sc = spark.sparkContext

# Configure SparkContext
conf = sc._conf
print(conf.get("spark.app.name"))

# Stop SparkContext
sc.stop()
```

### Configuration

```python
# Set configuration
spark.conf.set("spark.sql.shuffle.partitions", "200")

# Get configuration
value = spark.conf.get("spark.sql.shuffle.partitions")

# Get with default
value = spark.conf.get("spark.some.config", "default_value")

# Check if config exists
exists = spark.conf.contains("spark.sql.shuffle.partitions")
```

---

## DataFrame API

### Creating DataFrames

```python
# From list of tuples
data = [("Alice", 34), ("Bob", 45), ("Charlie", 29)]
df = spark.createDataFrame(data, ["name", "age"])

# From list of dictionaries
data = [{"name": "Alice", "age": 34}, {"name": "Bob", "age": 45}]
df = spark.createDataFrame(data)

# From RDD
rdd = sc.parallelize([("Alice", 34), ("Bob", 45)])
df = spark.createDataFrame(rdd, ["name", "age"])

# From pandas DataFrame
import pandas as pd
pandas_df = pd.DataFrame({"name": ["Alice", "Bob"], "age": [34, 45]})
df = spark.createDataFrame(pandas_df)

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

### Window Functions

```python
from pyspark.sql.window import Window
from pyspark.sql.functions import row_number, rank, dense_rank, lag, lead

# Define window specification
windowSpec = Window.partitionBy("department").orderBy("salary")

# Row number
df.withColumn("row_num", row_number().over(windowSpec)).show()

# Rank
df.withColumn("rank", rank().over(windowSpec)).show()

# Dense rank
df.withColumn("dense_rank", dense_rank().over(windowSpec)).show()

# Lag
df.withColumn("prev_salary", lag("salary", 1).over(windowSpec)).show()

# Lead
df.withColumn("next_salary", lead("salary", 1).over(windowSpec)).show()

# Running total
windowRunning = Window.partitionBy("department") \
    .orderBy("hire_date") \
    .rowsBetween(Window.unboundedPreceding, Window.currentRow)

df.withColumn("running_total", sum("salary").over(windowRunning)).show()
```

---

## SQL Queries

### Registering Temp Views

```python
# Create temporary view
df.createOrReplaceTempView("people")

# Create global temporary view
df.createOrReplaceGlobalTempView("people")

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
```

---

## RDD Operations

### Creating RDDs

```python
# From Python collection
rdd = sc.parallelize([1, 2, 3, 4, 5])

# From text file
rdd = sc.textFile("hdfs://path/to/file.txt")

# From multiple files
rdd = sc.textFile("hdfs://path/to/file1.txt,hdfs://path/to/file2.txt")

# From directory
rdd = sc.textFile("hdfs://path/to/directory/")
```

### RDD Transformations

```python
# map
rdd.map(lambda x: x * 2)

# filter
rdd.filter(lambda x: x > 3)

# flatMap
rdd.flatMap(lambda x: [x, x * 2])

# distinct
rdd.distinct()

# union
rdd1.union(rdd2)

# intersection
rdd1.intersection(rdd2)

# subtract
rdd1.subtract(rdd2)

# cartesian
rdd1.cartesian(rdd2)

# groupByKey
pair_rdd.groupByKey()

# reduceByKey
pair_rdd.reduceByKey(lambda a, b: a + b)

# sortByKey
pair_rdd.sortByKey()

# join
rdd1.join(rdd2)
```

### RDD Actions

```python
# collect
data = rdd.collect()

# take
first_10 = rdd.take(10)

# first
first = rdd.first()

# count
count = rdd.count()

# reduce
total = rdd.reduce(lambda a, b: a + b)

# foreach
rdd.foreach(lambda x: print(x))

# saveAsTextFile
rdd.saveAsTextFile("hdfs://path/to/output")

# countByKey
pair_rdd.countByKey()

# lookup
values = pair_rdd.lookup(5)
```

---

## Machine Learning (MLlib)

### Feature Engineering

```python
from pyspark.ml.feature import (
    VectorAssembler, StringIndexer, StandardScaler,
    MinMaxScaler, PCA, HashingTF, IDF
)

# VectorAssembler
assembler = VectorAssembler(
    inputCols=["feature1", "feature2", "feature3"],
    outputCol="features"
)
df = assembler.transform(data)

# StringIndexer
indexer = StringIndexer(inputCol="category", outputCol="categoryIndex")
df = indexer.fit(data).transform(data)

# StandardScaler
scaler = StandardScaler(inputCol="features", outputCol="scaledFeatures",
                       withStd=True, withMean=False)
scalerModel = scaler.fit(df)
df = scalerModel.transform(df)

# PCA
pca = PCA(k=3, inputCol="features", outputCol="pcaFeatures")
pcaModel = pca.fit(df)
df = pcaModel.transform(df)
```

### Classification

```python
from pyspark.ml.classification import (
    LogisticRegression, DecisionTreeClassifier,
    RandomForestClassifier, GBTClassifier
)

# Logistic Regression
lr = LogisticRegression(
    maxIter=100,
    regParam=0.01,
    featuresCol="features",
    labelCol="label"
)
lrModel = lr.fit(trainingData)
predictions = lrModel.transform(testData)

# Random Forest
rf = RandomForestClassifier(
    numTrees=100,
    maxDepth=10,
    featuresCol="features",
    labelCol="label"
)
rfModel = rf.fit(trainingData)
predictions = rfModel.transform(testData)
```

### Regression

```python
from pyspark.ml.regression import (
    LinearRegression, DecisionTreeRegressor,
    RandomForestRegressor, GBTRegressor
)

# Linear Regression
lr = LinearRegression(
    maxIter=100,
    regParam=0.0,
    featuresCol="features",
    labelCol="label"
)
lrModel = lr.fit(trainingData)
predictions = lrModel.transform(testData)
```

### Clustering

```python
from pyspark.ml.clustering import KMeans, GaussianMixture

# K-Means
kmeans = KMeans(
    k=3,
    seed=42,
    featuresCol="features",
    predictionCol="prediction"
)
kmeansModel = kmeans.fit(trainingData)
predictions = kmeansModel.transform(testData)

# Get cluster centers
centers = kmeansModel.clusterCenters()
```

### Model Evaluation

```python
from pyspark.ml.evaluation import (
    BinaryClassificationEvaluator,
    MulticlassClassificationEvaluator,
    RegressionEvaluator
)

# Binary classification
evaluator = BinaryClassificationEvaluator(
    labelCol="label",
    rawPredictionCol="rawPrediction",
    metricName="areaUnderROC"
)
auc = evaluator.evaluate(predictions)

# Multiclass classification
evaluator = MulticlassClassificationEvaluator(
    labelCol="label",
    predictionCol="prediction",
    metricName="accuracy"
)
accuracy = evaluator.evaluate(predictions)

# Regression
evaluator = RegressionEvaluator(
    labelCol="label",
    predictionCol="prediction",
    metricName="rmse"
)
rmse = evaluator.evaluate(predictions)
```

### Pipelines

```python
from pyspark.ml import Pipeline
from pyspark.ml.feature import VectorAssembler, StringIndexer
from pyspark.ml.classification import RandomForestClassifier

# Define pipeline stages
indexer = StringIndexer(inputCol="category", outputCol="categoryIndex")
assembler = VectorAssembler(inputCols=["categoryIndex", "feature1", "feature2"],
                           outputCol="features")
classifier = RandomForestClassifier(featuresCol="features", labelCol="label")

# Create pipeline
pipeline = Pipeline(stages=[indexer, assembler, classifier])

# Fit pipeline
model = pipeline.fit(trainingData)

# Transform data
predictions = model.transform(testData)

# Save pipeline
model.write().overwrite().save("path/to/pipeline")

# Load pipeline
from pyspark.ml import PipelineModel
loaded_model = PipelineModel.load("path/to/pipeline")
```

---

## Streaming

### DStreams (Legacy)

```python
from pyspark.streaming import StreamingContext

# Create StreamingContext
ssc = StreamingContext(sc, batchInterval=5)

# Create DStream
socket_stream = ssc.socketTextStream("localhost", 9999)

# Process stream
words = socket_stream.flatMap(lambda line: line.split(" "))
word_counts = words.map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)
word_counts.pprint()

# Start streaming
ssc.start()
ssc.awaitTermination()
```

### Structured Streaming

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("StructuredStreaming").getOrCreate()

# Read from Kafka
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Parse JSON
schema = StructType() \
    .add("user_id", StringType()) \
    .add("event_type", StringType()) \
    .add("timestamp", TimestampType())

events = df \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Windowed aggregation
windowed_counts = events \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes", "5 minutes"),
        "event_type"
    ) \
    .count()

# Write to console
query = windowed_counts \
    .writeStream \
    .outputMode("update") \
    .format("console") \
    .start()

query.awaitTermination()
```

---

## Data Sources

### Reading Data

```python
# JSON
df = spark.read.json("data.json")

# CSV
df = spark.read.csv("data.csv", header=True, inferSchema=True)

# Parquet
df = spark.read.parquet("data.parquet")

# ORC
df = spark.read.orc("data.orc")

# Text
df = spark.read.text("data.txt")

# Avro
df = spark.read.format("avro").load("data.avro")

# JDBC
df = spark.read.format("jdbc") \
    .option("url", "jdbc:postgresql://localhost/db") \
    .option("dbtable", "users") \
    .option("user", "user") \
    .option("password", "password") \
    .load()

# Multiple files
df = spark.read.json("file1.json,file2.json")

# Directory
df = spark.read.json("hdfs://path/to/directory/")
```

### Writing Data

```python
# JSON
df.write.json("output.json")

# CSV
df.write.csv("output.csv", header=True)

# Parquet
df.write.parquet("output.parquet", compression="snappy")

# ORC
df.write.orc("output.orc", compression="zlib")

# Text
df.write.text("output.txt")

# JDBC
df.write.format("jdbc") \
    .option("url", "jdbc:postgresql://localhost/db") \
    .option("dbtable", "users") \
    .option("user", "user") \
    .option("password", "password") \
    .mode("overwrite") \
    .save()

# Partitioned
df.write.partitionBy("year", "month").parquet("output")

# Bucketed
df.write.bucketBy(100, "user_id").sortBy("user_id").saveAsTable("bucketed_table")
```

---

## Performance Optimization

### Partitioning

```python
# Check partitions
print(df.rdd.getNumPartitions())

# Repartition
df = df.repartition(100)

# Repartition by column
df = df.repartition("department")

# Coalesce (reduce partitions)
df = df.coalesce(10)

# Optimal partition size
# Rule of thumb: 128MB-256MB per partition
partition_count = total_data_size / (200 * 1024 * 1024)
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

### Configuration

```python
# Shuffle partitions
spark.conf.set("spark.sql.shuffle.partitions", 200)

# Adaptive query execution
spark.conf.set("spark.sql.adaptive.enabled", True)
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", True)
spark.conf.set("spark.sql.adaptive.skewJoin.enabled", True)

# Broadcast join threshold
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", 10 * 1024 * 1024)

# Code generation
spark.conf.set("spark.sql.codegen.wholeStage", True)

# Columnar batch size
spark.conf.set("spark.sql.inMemoryColumnarStorage.batchSize", 10000)

# Vectorized reader
spark.conf.set("spark.sql.parquet.enableVectorizedReader", True)
spark.conf.set("spark.sql.orc.enableVectorizedReader", True)
```

### Serialization

```python
# Kryo serialization
spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
spark.conf.set("spark.kryo.registrationRequired", False)

# Register classes
spark.conf.set("spark.kryo.classesToRegister", "com.example.MyClass")
```

---

## Best Practices

### Code Quality

1. **Use DataFrame API**: Better optimization than RDD API
2. **Avoid UDFs when possible**: Built-in functions are optimized
3. **Test with sample data**: Use `limit()` for quick validation
4. **Monitor query plans**: Use `explain()` to understand execution

### Performance

1. **Minimize shuffles**: Use `reduceByKey` instead of `groupByKey`
2. **Broadcast small tables**: Use `broadcast()` for joins
3. **Cache frequently used DataFrames**: Avoid recomputation
4. **Use appropriate file formats**: Parquet or ORC for analytical queries

### Resource Management

1. **Tune executor memory**: Based on workload
2. **Use dynamic allocation**: Scale up/down based on load
3. **Monitor resource usage**: Check Spark UI for bottlenecks
4. **Set appropriate parallelism**: Match partition count to cluster size

### Error Handling

```python
# Handle missing values
df = df.na.drop()  # Drop rows with nulls
df = df.na.fill(0)  # Fill nulls with 0
df = df.na.fill({"name": "Unknown", "age": 0})  # Fill with specific values

# Handle bad records
df = spark.read.csv("data.csv", mode="PERMISSIVE")  # Allow bad records
df = spark.read.csv("data.csv", mode="DROPMALFORMED")  # Drop bad records
df = spark.read.csv("data.csv", mode="FAILFAST")  # Fail on bad records
```

---

## Examples

### Complete ETL Pipeline

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.types import *

spark = SparkSession.builder \
    .appName("ETL Pipeline") \
    .getOrCreate()

# Extract
raw_data = spark.read \
    .option("header", "true") \
    .csv("hdfs://input/data.csv")

# Transform
cleaned_data = raw_data \
    .dropDuplicates() \
    .na.drop(subset=["user_id", "timestamp"]) \
    .withColumn("timestamp", to_timestamp(col("timestamp"))) \
    .withColumn("date", to_date(col("timestamp"))) \
    .withColumn("hour", hour(col("timestamp"))) \
    .withColumn("amount", col("amount").cast(DoubleType())) \
    .filter(col("amount") > 0)

# Enrich
enriched_data = cleaned_data \
    .join(user_profiles, "user_id", "left") \
    .withColumn("user_segment",
        when(col("total_purchases") > 100, "VIP")
        .when(col("total_purchases") > 10, "Regular")
        .otherwise("New"))

# Load
enriched_data \
    .repartition(100) \
    .write \
    .mode("overwrite") \
    .partitionBy("date") \
    .parquet("hdfs://output/enriched_data")

spark.stop()
```

### Data Analysis

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("DataAnalysis").getOrCreate()

# Load data
df = spark.read.parquet("hdfs://data/events.parquet")

# Basic statistics
df.describe().show()

# Aggregations
daily_stats = df \
    .groupBy("date") \
    .agg(
        count("*").alias("event_count"),
        countDistinct("user_id").alias("unique_users"),
        sum("amount").alias("total_amount"),
        avg("amount").alias("avg_amount")
    ) \
    .orderBy("date")

daily_stats.show()

# Window functions
windowSpec = Window.partitionBy("user_id").orderBy("timestamp")

user_activity = df \
    .withColumn("event_rank", row_number().over(windowSpec)) \
    .withColumn("prev_event", lag("event_type", 1).over(windowSpec)) \
    .withColumn("time_since_last",
        unix_seconds(col("timestamp")) - unix_seconds(lag("timestamp", 1).over(windowSpec)))

user_activity.show()

spark.stop()
```

---

## References

- [PySpark Documentation](https://spark.apache.org/docs/latest/api/python/)
- [PySpark SQL Guide](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql.html)
- [PySpark MLlib Guide](https://spark.apache.org/docs/latest/api/python/reference/pyspark.ml.html)
- [PySpark Streaming Guide](https://spark.apache.org/docs/latest/api/python/reference/pyspark.streaming.html)
- [Learning Spark](http://shop.oreilly.com/product/0636920028512.do)
- [Spark: The Definitive Guide](http://shop.oreilly.com/product/0636920028512.do)
