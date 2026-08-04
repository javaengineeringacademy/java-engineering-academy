# MapReduce Programming Model

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Architecture](#architecture)
- [MapReduce Workflow](#mapreduce-workflow)
- [Input and Output Formats](#input-and-output-formats)
- [Combiner and Partitioner](#combiner-and-partitioner)
- [Hadoop Streaming](#hadoop-streaming)
- [Performance Optimization](#performance-optimization)
- [Limitations](#limitations)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

MapReduce is a programming model and processing framework for distributed computing
developed by Google and implemented in Apache Hadoop. It enables parallel processing
of massive datasets across clusters of commodity machines by abstracting the complexity
of distributed computation into two fundamental operations: Map and Reduce.

### Key Characteristics

- **Fault tolerant**: Automatic recovery from node failures
- **Scalable**: Horizontal scaling across thousands of nodes
- **Simple model**: Two core operations abstract complexity
- **Data locality**: Move computation to data, not data to computation
- **Batch oriented**: Optimized for large-scale batch processing

### When to Use MapReduce

- Large-scale data processing requiring fault tolerance
- ETL operations on datasets exceeding single-machine capacity
- Log processing and analytics on distributed storage
- Batch jobs with high throughput requirements
- Processing structured and unstructured data at scale

### Historical Context

MapReduce was introduced in a 2004 paper by Jeffrey Dean and Sanjay Ghemawat
at Google. Apache Hadoop's implementation became the foundation of the big data
ecosystem and inspired many subsequent distributed processing frameworks.

---

## Core Concepts

### The Map Function

The Map function processes input key-value pairs and produces intermediate
key-value pairs. Each input record is processed independently, enabling
parallel execution across multiple nodes.

```java
map(K1, V1) -> list(K2, V2)
```

**Responsibilities:**
- Parse and transform input records
- Filter records based on criteria
- Extract and emit intermediate key-value pairs
- Handle data normalization and cleansing

### The Shuffle and Sort

Between Map and Reduce, Hadoop performs:

1. **Partitioning**: Assigns intermediate keys to reducers
2. **Sorting**: Groups all values for the same key together
3. **Transfer**: Moves data across the network to appropriate reducers
4. **Merging**: Combines sorted runs from multiple mappers

### The Reduce Function

The Reduce function processes intermediate key-value pairs grouped by key
and produces the final output.

```java
reduce(K2, list(V2)) -> list(V3)
```

**Responsibilities:**
- Aggregate values associated with each key
- Perform summarization, counting, or joining operations
- Emit final output key-value pairs

### Data Types

MapReduce uses a framework of serializable types:

- `IntWritable`: 32-bit integer
- `LongWritable`: 64-bit integer
- `Text`: UTF-8 string
- `BytesWritable`: Raw byte array
- `DoubleWritable`: 64-bit double
- `BooleanWritable`: Boolean value

---

## Architecture

### Hadoop Cluster Components

```
┌─────────────────────────────────────────────────┐
│                  Client                          │
│            (Submits Job)                         │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│              JobTracker                          │
│         (Master Node)                           │
│    - Schedules Map/Reduce tasks                 │
│    - Monitors task progress                     │
│    - Handles fault recovery                     │
└──────────────────┬──────────────────────────────┘
                   │
    ┌──────────────┼──────────────┐
    │              │              │
┌───▼────┐  ┌────▼─────┐  ┌────▼─────┐
│TaskTrack│  │TaskTrack  │  │TaskTrack  │
│  er 1   │  │  er 2    │  │  er 3    │
│(Map +   │  │(Map +    │  │(Map +    │
│ Reduce) │  │ Reduce)  │  │ Reduce)  │
└─────────┘  └──────────┘  └──────────┘
```

### Job Execution Flow

1. **Job Submission**: Client submits job configuration and input data path
2. **Input Splitting**: Input data is divided into InputSplits
3. **Task Assignment**: JobTracker assigns Map tasks to TaskTrackers
4. **Map Phase**: Mappers process InputSplits and emit intermediate data
5. **Shuffle Phase**: Intermediate data is transferred to reducers
6. **Reduce Phase**: Reducers aggregate and write final output
7. **Job Completion**: Results stored in HDFS

### YARN Resource Management

Modern Hadoop uses YARN (Yet Another Resource Negotiator):

- **ResourceManager**: Central authority for resource allocation
- **NodeManager**: Per-node agent managing containers
- **ApplicationMaster**: Per-application coordinator
- **Container**: Resource allocation unit (CPU, memory)

---

## MapReduce Workflow

### Step-by-Step Process

```
Input Data (HDFS)
      │
      ▼
┌─────────────┐
│ InputFormat  │ ── Reads data, creates InputSplits
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Record    │ ── Parses raw bytes into key-value pairs
│   Reader    │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│     Map     │ ── Processes each record, emits intermediate KVPs
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Partitioner│ ── Assigns keys to reducers
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Combiner  │ ── Local pre-aggregation (optional)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Shuffle   │ ── Transfers and sorts data by key
│   & Sort    │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Reduce    │ ── Aggregates values per key
└──────┬──────┘
       │
       ▼
┌─────────────┐
│OutputFormat │ ── Writes results to HDFS
└─────────────┘
```

### Job Configuration

```java
Configuration conf = new Configuration();
Job job = Job.getInstance(conf, "word-count");

job.setJarByClass(WordCount.class);
job.setMapperClass(WordCountMapper.class);
job.setReducerClass(WordCountReducer.class);

job.setOutputKeyClass(Text.class);
job.setOutputValueClass(IntWritable.class);

job.setInputFormatClass(TextInputFormat.class);
job.setOutputFormatClass(TextOutputFormat.class);

FileInputFormat.addInputPath(job, new Path(args[0]));
FileOutputFormat.setOutputPath(job, new Path(args[1]));

System.exit(job.waitForCompletion(true) ? 0 : 1);
```

---

## Input and Output Formats

### Input Formats

| Format | Description | Use Case |
|--------|-------------|----------|
| `TextInputFormat` | Line-based text files | Log files, CSV data |
| `KeyValueTextInputFormat` | Tab-separated key-value | Configuration files |
| `SequenceFileInputFormat` | Binary key-value pairs | Intermediate data |
| `NLineInputFormat` | Fixed lines per split | Uniform processing |
| `DBInputFormat` | JDBC database input | Database ETL |
| `CombineFileInputFormat` | Merges small files | Many small files |

### Output Formats

| Format | Description | Use Case |
|--------|-------------|----------|
| `TextOutputFormat` | Tab-separated text | Default output |
| `SequenceFileOutputFormat` | Binary output | Intermediate results |
| `MapFileOutputFormat` | Indexed key-value | Lookup tables |
| `DBOutputFormat` | JDBC database output | Database loading |
| `NullOutputFormat` | Discards output | Side-effect jobs |

### Custom InputFormat

```java
public class CustomInputFormat extends FileInputFormat<Key, Value> {

    @Override
    public RecordReader<Key, Value> createRecordReader(
            InputSplit split, TaskAttemptContext context) {
        return new CustomRecordReader();
    }

    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return true;
    }
}
```

### InputSplit Configuration

```java
// Control split size
long minSize = FileInputFormat.getMinSplitSize(job);    // default 1
long maxSize = FileInputFormat.getMaxSplitSize(job);    // default Long.MAX
long blockSize = fs.getFileStatus(path).getBlockSize();

long splitSize = Math.max(minSize, Math.min(maxSize, blockSize));
```

---

## Combiner and Partitioner

### Combiner

A Combiner performs local aggregation on the mapper side, reducing network
traffic by pre-aggregating values before they are sent to reducers.

```java
// Combiner is often the same as Reducer
job.setCombinerClass(WordCountReducer.class);
```

**Important constraints:**
- Must be idempotent (output is used as input to reducer)
- Must have same input/output key-value types as mapper output
- Cannot change the logical meaning of the computation
- Works best with associative and commutative operations (sum, count, max)

### Partitioner

A Partitioner controls which reducer receives each intermediate key.

```java
public class CustomPartitioner extends Partitioner<Text, IntWritable> {

    @Override
    public int getPartition(Text key, IntWritable value, int numReduceTasks) {
        // Custom partitioning logic
        return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}
```

**Use cases:**
- Load balancing across reducers
- Ensuring related keys go to the same reducer
- Custom data distribution strategies

### Secondary Sort

To sort values within each key group:

```java
// Define composite key
public class CompositeKey extends WritableComparable<CompositeKey> {
    private Text naturalKey;
    private IntWritable secondaryKey;
}

// Custom partitioner uses only natural key
// Sort comparator uses composite key
// Group comparator uses only natural key
```

---

## Hadoop Streaming

Hadoop Streaming allows MapReduce programs to be written in any language
that can read from stdin and write to stdout.

### Python Example

```python
#!/usr/bin/env python3
import sys

# Mapper
for line in sys.stdin:
    line = line.strip()
    words = line.split()
    for word in words:
        print(f"{word}\t1")

# Reducer
from itertools import groupby
from operator import itemgetter

input_data = map(lambda x: x.strip(), sys.stdin)
sorted_data = sorted(input_data, key=lambda x: x.split('\t')[0])

for key, group in groupby(sorted_data, key=lambda x: x.split('\t')[0]):
    count = sum(1 for _ in group)
    print(f"{key}\t{count}")
```

### Running Streaming Jobs

```bash
hadoop jar hadoop-streaming.jar \
  -input /input/path \
  -output /output/path \
  -mapper mapper.py \
  -reducer reducer.py \
  -file mapper.py \
  -file reducer.py
```

### Streaming Options

```bash
# Add helper files
-file mapper.py              # Local file to distribute
-file reducer.py

# Configure environment
-D mapreduce.reduce.memory.mb=4096
-D mapreduce.task.timeout=600000

# Custom streaming format
-inputformat org.apache.hadoop.mapred.TextInputFormat
-outputformat org.apache.hadoop.mapred.TextOutputFormat
```

---

## Performance Optimization

### Mapper Optimization

1. **Minimize output**: Emit only necessary data
2. **Use Combiners**: Pre-aggregate when possible
3. **Batch writes**: Reduce I/O operations
4. **Optimize serialization**: Use Writable efficiently

```java
// Efficient output
context.write(key, new IntWritable(1));  // Use writable objects

// Inefficient output
context.write(key, new Text(String.valueOf(1)));  // Avoid string creation
```

### Reducer Optimization

1. **Tune number of reducers**: Based on output size
2. **Use DistributedCache**: For large lookup data
3. **Compress intermediate data**: Reduce network transfer

```bash
# Set number of reducers
hadoop jar job.jar -D mapreduce.job.reduces=20

# Enable compression
-D mapreduce.output.fileoutputformat.compress=true
-D mapreduce.map.output.compress=true
-D mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec
```

### Network Optimization

1. **Data locality**: Schedule tasks on data-local nodes
2. **Speculative execution**: Launch backup tasks for slow nodes
3. **Compress shuffle data**: Reduce network transfer

```bash
# Speculative execution settings
-D mapreduce.map.speculative=true
-D mapreduce.reduce.speculative=true
```

### Memory Management

```bash
# Container memory settings
-D mapreduce.map.memory.mb=4096
-D mapreduce.reduce.memory.mb=8192
-D mapreduce.map.java.opts=-Xmx3276m
-D mapreduce.reduce.java.opts=-Xmx6553m

# Shuffle buffer settings
-D mapreduce.reduce.shuffle.input.buffer.percent=0.70
-D mapreduce.reduce.shuffle.merge.percent=0.66
```

---

## Limitations

### Functional Limitations

- **Latency**: Not suitable for real-time or low-latency processing
- **Iterative processing**: Inefficient for algorithms requiring multiple passes
- **Interactive queries**: No support for ad-hoc interactive analysis
- **Complex graphs**: Limited support for graph algorithms
- **Stateful processing**: Stateless map and reduce tasks

### Operational Challenges

- **Small files problem**: Too many small files degrade performance
- **Skewed data**: Uneven key distribution causes load imbalance
- **Debugging difficulty**: Distributed debugging is complex
- **Resource overhead**: JVM startup costs per task
- **Network bottleneck**: Shuffle phase can saturate network

### Comparison with Alternatives

| Feature | MapReduce | Spark | Flink |
|---------|-----------|-------|-------|
| Processing | Batch | Batch + Micro-batch | Stream + Batch |
| Speed | Slow | 10-100x faster | 10x faster |
| Memory | Disk-based | In-memory | In-memory |
| Iterative | Poor | Excellent | Good |
| Fault Tolerance | Checkpointing | RDD lineage | Checkpointing |
| Ease of Use | Verbose | Concise | Moderate |

---

## Best Practices

### Job Design

1. **Partition wisely**: Choose partitioner to balance load
2. **Minimize shuffle**: Use Combiners and avoid unnecessary keys
3. **Handle skew**: Use secondary sort or salting for skewed keys
4. **Chain jobs**: Break complex logic into sequential jobs

### Code Quality

1. **Use counters**: Track custom metrics for monitoring
2. **Handle malformed records**: Skip or log bad input
3. **Write unit tests**: Test mapper and reducer independently
4. **Profile and optimize**: Identify bottlenecks

```java
// Use counters for monitoring
context.getCounter("MyApp", "RecordsProcessed").increment(1);
context.getCounter("MyApp", "InvalidRecords").increment(1);
```

### Data Management

1. **Optimize file size**: Aim for 128MB-256MB per block
2. **Use compression**: Especially for intermediate data
3. **Handle small files**: Use CombineFileInputFormat or SequenceFiles
4. **Partition output**: Use Hive-style partitioning for downstream queries

### Monitoring and Debugging

```bash
# Check job history
mapred job -history all <job-id>

# View job counters
mapred job -counter <job-id>

# Analyze slow tasks
mapred job -list-events <job-id> <start-event>
```

---

## Examples

### Word Count

```java
public class WordCount {

    public static class TokenizerMapper
            extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
}
```

### Inverted Index

```java
public class InvertedIndex {

    public static class IndexMapper
            extends Mapper<LongWritable, Text, Text, Text> {

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens = value.toString().split("\\s+");
            String documentId = tokens[0];

            for (int i = 1; i < tokens.length; i++) {
                context.write(new Text(tokens[i]),
                    new Text(documentId));
            }
        }
    }

    public static class IndexReducer
            extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            Set<String> documents = new TreeSet<>();
            for (Text val : values) {
                documents.add(val.toString());
            }
            context.write(key, new Text(String.join(",", documents)));
        }
    }
}
```

### Distributed Grep

```java
public class DistributedGrep {

    public static class GrepMapper
            extends Mapper<LongWritable, Text, Text, NullWritable> {

        private Pattern pattern;

        @Override
        protected void setup(Context context) {
            String regex = context.getConfiguration().get("grep.pattern");
            pattern = Pattern.compile(regex);
        }

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            if (pattern.matcher(value.toString()).find()) {
                context.write(value, NullWritable.get());
            }
        }
    }
}
```

### Secondary Sort (Top N)

```java
public class TopN {

    public static class SortMapper
            extends Mapper<LongWritable, Text, IntWritable, Text> {

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] parts = value.toString().split("\\t");
            context.write(
                new IntWritable(Integer.parseInt(parts[1])),
                new Text(parts[0])
            );
        }
    }

    public static class TopReducer
            extends Reducer<IntWritable, Text, Text, IntWritable> {

        private int count = 0;
        private static final int N = 10;

        public void reduce(IntWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            for (Text val : values) {
                if (count < N) {
                    context.write(val, key);
                    count++;
                }
            }
        }
    }
}
```

---

## Debugging and Troubleshooting

### Common Issues

1. **Out of Memory**: Increase heap size or reduce split size
2. **Task failures**: Check logs for exceptions
3. **Slow jobs**: Profile map and reduce phases
4. **Data skew**: Use salting or custom partitioners

### Debugging Tools

```bash
# Enable debug mode
-D mapreduce.map.debug=true
-D mapreduce.reduce.debug=true

# Local mode testing
-D mapreduce.framework.name=local

# View task logs
yarn logs -applicationId <app-id>
```

---

## References

- [Hadoop MapReduce Tutorial](https://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html)
- [MapReduce: Simplified Data Processing on Large Clusters](https://research.google/pubs/pub62/)
- [Hadoop: The Definitive Guide](http://shop.oreilly.com/product/0636920028512.do)
- [MapReduce Design Patterns](https://www.oreilly.com/library/view/mapreduce-design-patterns/9781491905847/)
- [Apache Hadoop Documentation](https://hadoop.apache.org/docs/)
