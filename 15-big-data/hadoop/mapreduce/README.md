# Apache MapReduce

MapReduce is a programming model for processing large datasets in parallel across a Hadoop cluster.

## Table of Contents

1. [Overview](#overview)
2. [Programming Model](#programming-model)
3. [Execution Flow](#execution-flow)
4. [Map Phase](#map-phase)
5. [Shuffle and Sort](#shuffle-and-sort)
6. [Reduce Phase](#reduce-phase)
7. [Combiner](#combiner)
8. [Input Formats](#input-formats)
9. [Output Formats](#output-formats)
10. [Counters](#counters)
11. [Examples](#examples)
12. [Performance Tuning](#performance-tuning)
13. [MapReduce vs Spark](#mapreduce-vs-spark)

---

## Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    MapReduce Framework                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Input ──► Split ──► Map ──► Shuffle ──► Reduce ──► Output │
│                                                             │
│  ┌─────────┐    ┌─────┐    ┌─────┐    ┌─────────┐         │
│  │ Split 1 │───►│Map 1│───►│     │───►│Reduce 1 │───►Out 1 │
│  └─────────┘    └─────┘    │     │    └─────────┘         │
│  ┌─────────┐    ┌─────┐    │Sort │    ┌─────────┐         │
│  │ Split 2 │───►│Map 2│───►│     │───►│Reduce 2 │───►Out 2 │
│  └─────────┘    └─────┘    │     │    └─────────┘         │
│  ┌─────────┐    ┌─────┐    │     │    ┌─────────┐         │
│  │ Split 3 │───►│Map 3│───►│     │───►│Reduce 3 │───►Out 3 │
│  └─────────┘    └─────┘    └─────┘    └─────────┘         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Key Concepts

- **Input Split**: A chunk of input data processed by a single Map task
- **Record Reader**: Converts input split into key-value pairs
- **Map**: Processes input records and emits key-value pairs
- **Combiner**: Local reducer that reduces data transfer
- **Partitioner**: Determines which reducer receives each key
- **Shuffle**: Transfers map output to reducers
- **Sort**: Groups values by key before reduce
- **Reduce**: Processes grouped values and produces output

---

## Programming Model

### Map Function

```java
// Input: <key, value> pairs
// Output: List of <key, value> pairs

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    
    @Override
    protected void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
        // Split line into words
        String[] tokens = value.toString().split("\\s+");
        
        // Emit each word with count 1
        for (String token : tokens) {
            word.set(token.toLowerCase());
            context.write(word, one);
        }
    }
}
```

### Reduce Function

```java
// Input: <key, List<values>> pairs
// Output: List of <key, value> pairs

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {
        int sum = 0;
        
        // Sum all values for this key
        for (IntWritable val : values) {
            sum += val.get();
        }
        
        // Emit word with total count
        context.write(key, new IntWritable(sum));
    }
}
```

### Driver Program

```java
public class WordCount {
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

---

## Execution Flow

### Detailed Pipeline

```
┌────────────────────────────────────────────────────────────┐
│                    Job Execution Flow                       │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  1. Input Format                                           │
│     └─► InputSplit[] ──► RecordReader                      │
│                                                            │
│  2. Map Phase                                              │
│     └─► Mapper.map() ──► Context.write()                   │
│                                                            │
│  3. Combine (Optional)                                     │
│     └─► Combiner.run() ──► Local aggregation               │
│                                                            │
│  4. Partition                                               │
│     └─► Partitioner.getPartition() ──► Reducer assignment  │
│                                                            │
│  5. Shuffle                                                │
│     └─► HTTP transfer ──► Sort by key                      │
│                                                            │
│  6. Reduce                                                 │
│     └─► Reducer.reduce() ──► Context.write()               │
│                                                            │
│  7. Output Format                                          │
│     └─► RecordWriter ──► Output files                      │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### Task Execution

```
JobTracker (YARN ResourceManager)
    │
    ├── ApplicationMaster
    │   ├── Task 1 (Map) ──► Container on NodeManager
    │   ├── Task 2 (Map) ──► Container on NodeManager
    │   ├── Task 3 (Map) ──► Container on NodeManager
    │   ├── Task 4 (Reduce) ──► Container on NodeManager
    │   └── Task 5 (Reduce) ──► Container on NodeManager
    │
    └── Task Logs
        ├── stdout
        ├── stderr
        └── syslog
```

---

## Map Phase

### Input Splitting

```
File: /data/access.log (1 GB)
Block Size: 128 MB

Split 1: bytes 0 - 134217727
Split 2: bytes 134217728 - 268435455
Split 3: bytes 268435456 - 402653183
Split 4: bytes 402653184 - 536870911
Split 5: bytes 536870912 - 671088639
Split 6: bytes 671088640 - 805306367
Split 7: bytes 805306368 - 939524095
Split 8: bytes 939524096 - 1073741823
```

### Mapper Processing

```java
// Example: Processing a single split
InputSplit: <InputSplit: length=134217728>

Record 1: <0, "hello world">
Record 2: <12, "foo bar baz">
Record 3: <24, "hello hadoop">
...

Mapper Output:
<("hello", 1), ("world", 1), ("foo", 1), ("bar", 1), ...>
```

### Map Task Configuration

```java
// Set number of map tasks
job.setNumMapTasks(10);  // Hint, not guaranteed

// Custom RecordReader
job.setInputFormatClass(CustomInputFormat.class);

// Map output compression
job.setMapOutputCompressorClass(GzipCodec.class);
```

---

## Shuffle and Sort

### Shuffle Process

```
Map Task Output                    Reducer Input
┌─────────────┐                   ┌─────────────┐
│ (a, 1)      │    ──►  Sort  ──►│ (a, [1,1])  │
│ (b, 1)      │                   │ (b, [1])    │
│ (a, 1)      │                   │ (c, [1,1])  │
│ (c, 1)      │                   └─────────────┘
│ (c, 1)      │
│ (b, 1)      │
└─────────────┘

Shuffle Steps:
1. Map output written to memory buffer (sort buffer)
2. Sort buffer by key
3. Spill to disk when buffer full
4. Merge spilled files
5. Transfer to reducer
6. Reducer merges all map outputs
7. Sort by key
8. Group values by key
```

### Sort Buffer

```
Sort Buffer (default 100 MB):
┌───────────────────────────────────┐
│  (a, 1) (b, 1) (a, 1) (c, 1)   │
│  (c, 1) (b, 1)                   │
│                                   │
│  Threshold: 80% (mapreduce.task.io.sort.mb) │
│  ─────────────────────────────────│
│  Spill to disk when 80% full      │
└───────────────────────────────────┘
         │
         ▼
┌─────────────────┐
│  Spill File 1   │  (sorted)
└─────────────────┘
         │
         ▼ (merge)
┌─────────────────┐
│  Final Output   │
└─────────────────┘
```

### Partitioning

```java
// Default: HashPartitioner
public class HashPartitioner<K, V> extends Partitioner<K, V> {
    public int getPartition(K key, V value, int numReduceTasks) {
        return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}

// Custom Partitioner
public class CustomPartitioner extends Partitioner<Text, IntWritable> {
    @Override
    public int getPartition(Text key, IntWritable value, int numReduceTasks) {
        // Route specific keys to specific reducers
        if (key.toString().startsWith("ERROR")) {
            return 0;  // All errors to reducer 0
        }
        return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}
```

---

## Reduce Phase

### Reducer Processing

```java
// Input to reducer:
// Key: "hello"
// Values: [1, 1, 1, 1, 1]

@Override
protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
        throws IOException, InterruptedException {
    
    int sum = 0;
    for (IntWritable val : values) {
        sum += val.get();
    }
    
    // Output: ("hello", 5)
    context.write(key, new IntWritable(sum));
}
```

### Reduce Task Configuration

```java
// Set number of reducers
job.setNumReduceTasks(3);

// Custom reducer class
job.setReducerClass(CustomReducer.class);

// Output compression
FileOutputFormat.setCompressOutput(job, true);
FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
```

### Secondary Sort

```java
// Sort values within a key
job.setSortComparatorClass(ValueComparator.class);
job.setGroupingComparatorClass(KeyComparator.class);

public class ValueComparator extends WritableComparator {
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        IntWritable val1 = (IntWritable) a;
        IntWritable val2 = (IntWritable) b;
        return -val1.compareTo(val2);  // Descending order
    }
}
```

---

## Combiner

### Purpose

Combiner reduces the amount of data transferred from mappers to reducers by performing local aggregation.

```
Without Combiner:
Map 1: (a,1) (b,1) (a,1) (c,1) ──────────────► Reducer
Map 2: (a,1) (b,1) (c,1) (c,1) ──────────────► Reducer

With Combiner:
Map 1: (a,1) (b,1) (a,1) (c,1) ──► (a,2)(b,1)(c,1) ──► Reducer
Map 2: (a,1) (b,1) (c,1) (c,1) ──► (a,1)(b,1)(c,2) ──► Reducer
```

### Implementation

```java
// Same logic as reducer
public class WordCountCombiner extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        context.write(key, new IntWritable(sum));
    }
}

// Set combiner
job.setCombinerClass(WordCountCombiner.class);
```

### Combiner Requirements

| Requirement | Description |
|-------------|-------------|
| Same input/output types | Input and output key-value types must match |
| Associative | (a op b) op c = a op (b op c) |
| Commutative | a op b = b op a |
| No side effects | Must not affect final result |

### When to Use Combiner

```java
// Good: Word count, sum, max, min
// Bad: Average (cannot combine averages directly)

// Solution for average:
// Emit (sum, count) instead of (value)
// Combiner: (sum1, count1) + (sum2, count2) = (sum1+sum2, count1+count2)
// Reducer: sum / count = average
```

---

## Input Formats

### Built-in InputFormats

| InputFormat | Description |
|-------------|-------------|
| TextInputFormat | Lines of text (default) |
| KeyValueTextInputFormat | Tab-separated key-value |
| SequenceFileInputFormat | Hadoop binary format |
| NLineInputFormat | Fixed lines per split |
| CombineTextInputFormat | Combines small files |
| DBInputFormat | Database input |
| MultipleInputs | Multiple input sources |

### TextInputFormat Details

```
Input File: access.log
┌──────────────────────────────────────────┐
│ 192.168.1.1 - - [15/Jan/2024:10:00:00]  │
│ 192.168.1.2 - - [15/Jan/2024:10:00:01]  │
│ 192.168.1.1 - - [15/Jan/2024:10:00:02]  │
└──────────────────────────────────────────┘

Output to Mapper:
<0, "192.168.1.1 - - [15/Jan/2024:10:00:00] ...">
<150, "192.168.1.2 - - [15/Jan/2024:10:00:01] ...">
<300, "192.168.1.1 - - [15/Jan/2024:10:00:02] ...">
```

### Custom InputFormat

```java
public class CustomInputFormat extends FileInputFormat<LongWritable, Text> {
    
    @Override
    public RecordReader<LongWritable, Text> createRecordReader(
            InputSplit split, TaskAttemptContext context) {
        return new CustomRecordReader();
    }
    
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return true;  // Allow splitting
    }
}

public class CustomRecordReader extends RecordReader<LongWritable, Text> {
    private LineReader reader;
    private LongWritable key = new LongWritable();
    private Text value = new Text();
    
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) 
            throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        FileSplit fileSplit = (FileSplit) split;
        Path file = fileSplit.getPath();
        FileSystem fs = file.getFileSystem(conf);
        FSDataInputStream in = fs.open(file);
        reader = new LineReader(in);
    }
    
    @Override
    public boolean nextKeyValue() throws IOException {
        key.set(reader.getPos());
        int bytesRead = reader.readLine(value);
        return bytesRead > 0;
    }
    
    @Override
    public LongWritable getCurrentKey() {
        return key;
    }
    
    @Override
    public Text getCurrentValue() {
        return value;
    }
    
    @Override
    public float getProgress() {
        return reader.getProgress();
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
```

---

## Output Formats

### Built-in OutputFormats

| OutputFormat | Description |
|--------------|-------------|
| TextOutputFormat | Tab-separated text (default) |
| SequenceFileOutputFormat | Hadoop binary format |
| MapFileOutputFormat | Sorted key-value pairs |
| NullOutputFormat | Discards output |
| DBOutputFormat | Database output |
| LazyOutputFormat | Creates output only when needed |

### TextOutputFormat

```
Output:
hello	5
world	3
hadoop	2
mapreduce	1

Key and value separated by tab
```

### SequenceFileOutputFormat

```
Binary format with:
- Key-value structure
- Compression support
- Block compression
- Splittable for MapReduce

Advantages:
- Compact storage
- Faster to read/write
- Supports compression
- Native Hadoop format
```

### Custom OutputFormat

```java
public class CustomOutputFormat extends FileOutputFormat<Key, Value> {
    
    @Override
    public RecordWriter<Key, Value> getRecordWriter(TaskAttemptContext context) 
            throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        Path outputPath = getOutputPath(context);
        FileSystem fs = outputPath.getFileSystem(conf);
        
        Path filePath = new Path(outputPath, "output.txt");
        FSDataOutputStream out = fs.create(filePath);
        
        return new CustomRecordWriter(out);
    }
}

public class CustomRecordWriter extends RecordWriter<Key, Value> {
    private DataOutputStream out;
    
    public CustomRecordWriter(DataOutputStream out) {
        this.out = out;
    }
    
    @Override
    public void write(Key key, Value value) throws IOException {
        out.writeBytes(key.toString() + "," + value.toString() + "\n");
    }
    
    @Override
    public void close(TaskAttemptContext context) throws IOException {
        out.close();
    }
}
```

---

## Counters

### Built-in Counters

```java
// Access counters in mapper/reducer
context.getCounter("Custom Group", "Custom Counter").increment(1);

// Get counter values after job completes
CounterGroup group = job.getCounters().getGroup("Custom Group");
for (Counter counter : group) {
    System.out.println(counter.getName() + ": " + counter.getValue());
}
```

### Common Counters

| Counter | Description |
|---------|-------------|
| `Map-Reduce Framework` | Framework counters |
| `FileSystemCounters` | File system operations |
| `Job Counters` | Job-level metrics |
| `File Input Format Counters` | Input metrics |
| `File Output Format Counters` | Output metrics |

### Custom Counters

```java
// Define counter enum
public enum ProcessCounter {
    RECORDS_READ,
    RECORDS_WRITTEN,
    ERRORS,
    SKIPPED_RECORDS
}

// Use in mapper
context.getCounter(ProcessCounter.RECORDS_READ).increment(1);

// Access after job
long errors = job.getCounters()
    .findCounter(ProcessCounter.ERRORS)
    .getValue();
```

### Counter Usage Patterns

```java
// Track data quality
if (record.isValid()) {
    context.getCounter("DataQuality", "ValidRecords").increment(1);
} else {
    context.getCounter("DataQuality", "InvalidRecords").increment(1);
}

// Track processing metrics
context.getCounter("Processing", "TotalBytes").increment(record.length());
context.getCounter("Processing", "TotalRecords").increment(1);
```

---

## Examples

### Word Count (Complete)

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
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

### Data Aggregation

```java
// Calculate average temperature per city
public class TemperatureAggregator {
    
    public static class TempMapper 
            extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            String city = parts[0];
            double temp = Double.parseDouble(parts[1]);
            context.write(new Text(city), new DoubleWritable(temp));
        }
    }
    
    public static class TempReducer 
            extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        
        public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
            double sum = 0;
            int count = 0;
            for (DoubleWritable val : values) {
                sum += val.get();
                count++;
            }
            context.write(key, new DoubleWritable(sum / count));
        }
    }
}
```

### Secondary Sort Example

```java
// Sort temperatures within each city
public class TemperatureSort {
    
    public static class CompositeKey 
            extends TextComparableWritable {
        private Text city = new Text();
        private DoubleWritable temp = new DoubleWritable();
        
        // Natural key: city
        // Secondary key: temperature
    }
    
    public static class KeyComparator extends WritableComparator {
        protected KeyComparator() {
            super(CompositeKey.class, true);
        }
        
        public int compare(WritableComparable a, WritableComparable b) {
            CompositeKey k1 = (CompositeKey) a;
            CompositeKey k2 = (CompositeKey) b;
            
            int cmp = k1.getCity().compareTo(k2.getCity());
            if (cmp != 0) return cmp;
            
            return -k1.getTemp().compareTo(k2.getTemp());  // Descending
        }
    }
}
```

---

## Performance Tuning

### Configuration Parameters

```xml
<!-- Map task configuration -->
<property>
    <name>mapreduce.task.io.sort.mb</name>
    <value>100</value>  <!-- Sort buffer size (MB) -->
</property>

<property>
    <name>mapreduce.map.sort.spill.percent</name>
    <value>0.80</value>  <!-- Spill threshold -->
</property>

<property>
    <name>mapreduce.task.io.sort.factor</name>
    <value>10</value>  <!-- Merge factor -->
</property>

<!-- Reduce task configuration -->
<property>
    <name>mapreduce.reduce.shuffle.parallelcopies</name>
    <value>50</value>  <!-- Parallel shuffle copies -->
</property>

<property>
    <name>mapreduce.reduce.shuffle.input.buffer.percent</name>
    <value>0.70</value>  <!-- Shuffle buffer -->
</property>
```

### Performance Tips

| Tip | Description |
|-----|-------------|
| Use Combiner | Reduces shuffle data |
| Optimize number of mappers | One per HDFS block |
| Tune reducer count | Start with 0.95 × nodes × reducers per node |
| Use compression | Reduces I/O and network |
| Avoid skew | Ensure balanced key distribution |
| Use SequenceFile | For intermediate data |

### Monitoring

```bash
# Check job progress
http://jobtracker-host:8088

# View job counters
hadoop job -counter <job-id>

# Analyze task logs
yarn logs -applicationId <application-id>

# Check data skew
hadoop job -history <job-output-path>
```

---

## MapReduce vs Spark

| Feature | MapReduce | Spark |
|---------|-----------|-------|
| Speed | Slower (disk I/O) | 10-100x faster |
| Model | Map + Reduce | RDD/DataFrame |
| Languages | Java only | Scala, Python, Java, R |
| Memory | Disk-based | In-memory |
| Iterations | Multiple jobs | Single DAG |
| Ease of Use | Complex | Simple API |
| Real-time | No | Yes (Streaming) |

### When to Use MapReduce

- Very large datasets that don't fit in memory
- Simple batch processing
- Legacy systems already using MapReduce
- When disk I/O is not a bottleneck

### When to Use Spark

- Interactive analytics
- Machine learning
- Real-time streaming
- Complex transformations
- Iterative algorithms
