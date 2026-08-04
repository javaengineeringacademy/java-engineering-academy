# Apache Beam

Apache Beam provides a unified programming model for both batch and streaming data processing. It runs on multiple execution engines including Apache Spark, Apache Flink, and Google Cloud Dataflow.

## Table of Contents

1. [Unified Model](#unified-model)
2. [PCollections](#pcollections)
3. [Transforms](#transforms)
4. [Windowing](#windowing)
5. [Triggers](#triggers)
6. [Side Inputs and Outputs](#side-inputs-and-outputs)
7. [Runners](#runners)
8. [DoFn and ParDo](#dofn-and-pardo)
9. [Best Practices](#best-practices)

---

## Unified Model

Beam's unified model handles both batch and stream processing:

### Core Concepts

- **Pipeline**: Complete data processing workflow
- **PCollection**: Immutable, distributed dataset
- **Transform**: Operation on PCollections
- **Runner**: Execution engine (Spark, Flink, Dataflow)
- **Source**: Data input
- **Sink**: Data output

### Batch vs Stream

```
Batch Processing:
┌─────────┐      ┌─────────┐      ┌─────────┐
│  Input  │ ──── │ Transform│ ──── │  Output │
│ (finite)│      │         │      │ (finite)│
└─────────┘      └─────────┘      └─────────┘

Stream Processing:
┌─────────┐      ┌─────────┐      ┌─────────┐
│  Input  │ ──── │ Transform│ ──── │  Output │
│(unbound)│      │         │      │(unbound)│
└─────────┘      └─────────┘      └─────────┘
```

### Language Support

- **Java**: Primary SDK
- **Python**: SDK (Apache Beam Python)
- **Go**: SDK (Apache Beam Go)
- **SQL**: Beam SQL for SQL-based processing

---

## PCollections

PCollections are the fundamental data structure in Beam:

### Creating PCollections

```java
// From bounded source
PCollection<String> lines = pipeline.apply(
    "ReadLines",
    TextIO.read().from("gs://bucket/input.txt")
);

// From unbounded source
PCollection<String> kafkaMessages = pipeline.apply(
    "ReadKafka",
    KafkaIO.readStrings()
        .withBootstrapServers("localhost:9092")
        .withTopic("my-topic")
);

// From in-memory data
List<String> data = Arrays.asList("one", "two", "three");
PCollection<String> input = pipeline.apply(Create.of(data));
```

### PCollection Properties

- **Immutable**: Cannot be modified after creation
- **Distributed**: Data is spread across workers
- **Typed**: Each PCollection has element type
- **Bounded/Unbounded**: Finite or infinite data

### Windowed PCollections

```java
PCollection<String> windowedLines = lines
    .apply(Window.<String>into(FixedWindows.of(Duration.standardSeconds(10))));
```

---

## Transforms

Transforms are operations on PCollections:

### Map Transform

```java
PCollection<String> uppercased = input.apply(
    "Uppercase",
    MapElements.via(new SimpleFunction<String, String>() {
        @Override
        public String apply(String element) {
            return element.toUpperCase();
        }
    })
);
```

### FlatMap Transform

```java
PCollection<String> words = lines.apply(
    "SplitIntoWords",
    FlatMapElements.via(new SimpleFunction<String, String[]>() {
        @Override
        public String[] apply(String line) {
            return line.split("\\s+");
        }
    })
);
```

### Filter Transform

```java
PCollection<String> filtered = input.apply(
    "FilterNonEmpty",
    Filter.by(new SerializablePredicate<String>() {
        @Override
        public boolean apply(String element) {
            return !element.isEmpty();
        }
    })
);
```

### GroupByKey Transform

```java
PCollection<KV<String, Long>> wordCounts = words
    .apply(MapElements.via(new SimpleFunction<String, KV<String, Long>>() {
        @Override
        public KV<String, Long> apply(String word) {
            return KV.of(word, 1L);
        }
    }))
    .apply(GroupByKey.<String, Long>create())
    .apply(Combine.<String, Long, Long>groupedValues(
        new Sum.SumLongFn()));
```

### Composite Transforms

```java
public class WordCount extends PTransform<PCollection<String>, PCollection<String>> {
    @Override
    public PCollection<String> expand(PCollection<String> lines) {
        return lines
            .apply(FlatMapElements.via(
                new SimpleFunction<String, String[]>() {
                    @Override
                    public String[] apply(String line) {
                        return line.split("\\s+");
                    }
                }))
            .apply(MapElements.via(
                new SimpleFunction<String, KV<String, Long>>() {
                    @Override
                    public KV<String, Long> apply(String word) {
                        return KV.of(word, 1L);
                    }
                }))
            .apply(GroupByKey.<String, Long>create())
            .apply(Combine.<String, Long, Long>groupedValues(
                new Sum.SumLongFn()))
            .apply(MapElements.via(
                new SimpleFunction<KV<String, Long>, String>() {
                    @Override
                    public String apply(KV<String, Long> element) {
                        return element.getKey() + ": " + element.getValue();
                    }
                }));
    }
}
```

---

## Windowing

Windowing divides unbounded data into finite windows:

### Window Types

**Fixed Windows**
```java
PCollection<String> windowed = input.apply(
    Window.<String>into(FixedWindows.of(Duration.standardSeconds(30)))
);
```

**Sliding Windows**
```java
PCollection<String> windowed = input.apply(
    Window.<String>into(SlidingWindows.of(Duration.standardMinutes(5))
        .every(Duration.standardSeconds(30)))
);
```

**Session Windows**
```java
PCollection<String> windowed = input.apply(
    Window.<String>into(SessionWindows.withGapDuration(Duration.standardSeconds(10)))
);
```

**Calendar Windows**
```java
PCollection<String> windowed = input.apply(
    Window.<String>into(CalendarWindows.weeksOn(1)
        .startingAt(0))
);
```

### Window Assignment

```java
// Assign windows
PCollection<String> windowed = input.apply(
    Window.<String>into(FixedWindows.of(Duration.standardSeconds(10)))
        .withAllowedLateness(Duration.standardMinutes(1))
        .withTimestampCombiner(TimestampCombiner.EARLIEST)
);
```

### Window Triggers

```java
// Default trigger
PCollection<String> windowed = input.apply(
    Window.<String>into(FixedWindows.of(Duration.standardSeconds(10)))
        .triggering(AfterWatermark.pastEndOfWindow())
);

// Early results
PCollection<String> windowed = input.apply(
    Window.<String>into(FixedWindows.of(Duration.standardSeconds(10)))
        .triggering(AfterWatermark.pastEndOfWindow()
            .withEarlyFirings(AfterProcessingTime.pastFirstElementInPane()
                .plusDelayOf(Duration.standardSeconds(5))))
        .withAllowedLateness(Duration.standardMinutes(1))
        .accumulatingFiredPanes()
);
```

---

## Triggers

Triggers determine when to emit results:

### Built-in Triggers

**AfterWatermark**
```java
// Fire when watermark passes window end
AfterWatermark.pastEndOfWindow()

// With early firings
AfterWatermark.pastEndOfWindow()
    .withEarlyFirings(AfterProcessingTime.pastFirstElementInPane()
        .plusDelayOf(Duration.standardSeconds(5)));

// With late firings
AfterWatermark.pastEndOfWindow()
    .withLateFirings(AfterProcessingTime.pastFirstElementInPane()
        .plusDelayOf(Duration.standardSeconds(1)));
```

**AfterProcessingTime**
```java
// Fire after processing time delay
AfterProcessingTime.pastFirstElementInPane()
    .plusDelayOf(Duration.standardSeconds(10));
```

**AfterCount**
```java
// Fire after specific element count
AfterFirst.of(
    AfterPane.elementCountAtLeast(10),
    AfterWatermark.pastEndOfWindow()
);
```

**AfterAny**
```java
// Fire when any trigger fires
AfterAny.of(
    AfterWatermark.pastEndOfWindow(),
    AfterProcessingTime.pastFirstElementInPane()
        .plusDelayOf(Duration.standardMinutes(5))
);
```

### Composite Triggers

```java
// Combine triggers
AfterFirst.of(
    AfterCount.atLeast(100),
    AfterWatermark.pastEndOfWindow()
);

AfterAll.of(
    AfterCount.atLeast(100),
    AfterWatermark.pastEndOfWindow()
);
```

---

## Side Inputs and Outputs

### Side Inputs

Side inputs allow additional data in transforms:

```java
PCollection<String> mainInput = ...;
PCollection<KV<String, String>> sideInput = ...;

PCollection<String> result = mainInput.apply(
    ParDo.of(new DoFn<String, String>() {
        @ProcessElement
        public void processElement(
            @Element String element,
            @SideInput("lookupTable") KV<String, String> lookupTable,
            OutputReceiver<String> out) {
            
            String value = lookupTable.getValue();
            out.process(element + ": " + value);
        }
    }).withSideInputs("lookupTable", sideInput)
);
```

### Side Outputs

Side outputs emit to multiple PCollections:

```java
PCollection<String> mainOutput = input.apply(
    ParDo.of(new DoFn<String, String>() {
        final OutputTag<String> validTag = new OutputTag<String>("valid") {};
        final OutputTag<String> invalidTag = new OutputTag<String>("invalid") {};
        
        @ProcessElement
        public void processElement(
            @Element String element,
            OutputReceiver<String> mainReceiver,
            OutputReceiver<String> validReceiver,
            OutputReceiver<String> invalidReceiver) {
            
            if (isValid(element)) {
                validReceiver.output(element);
            } else {
                invalidReceiver.output(element);
            }
            mainReceiver.output(element);
        }
    })
);
```

### Multi-Output

```java
PCollectionTuple outputs = input.apply(
    ParDo.of(new DoFn<String, String[]>() {
        final OutputTag<String> tag1 = new OutputTag<String>("output1") {};
        final OutputTag<String> tag2 = new OutputTag<String>("output2") {};
        
        @ProcessElement
        public void processElement(
            @Element String element,
            OutputReceiver<String> mainReceiver,
            OutputReceiver<String> out1,
            OutputReceiver<String> out2) {
            
            mainReceiver.output(element);
            out1.output("processed: " + element);
            out2.output("logged: " + element);
        }
    })
);

PCollection<String> main = outputs.getMain();
PCollection<String> output1 = outputs.get(tag1);
PCollection<String> output2 = outputs.get(tag2);
```

---

## Runners

Beam supports multiple execution engines:

### Apache Spark Runner

```java
PipelineOptions options = SparkPipelineOptions.defaults();
options.setSparkMaster("local[*]");
options.setRunner(SparkRunner.class);

Pipeline pipeline = Pipeline.create(options);
```

### Apache Flink Runner

```java
PipelineOptions options = FlinkPipelineOptions.defaults();
options.setFlinkMaster("localhost:8081");
options.setRunner(FlinkRunner.class);

Pipeline pipeline = Pipeline.create(options);
```

### Google Cloud Dataflow Runner

```java
PipelineOptions options = DataflowPipelineOptions.defaults();
options.setProject("my-project");
options.setRegion("us-central1");
options.setRunner(DataflowRunner.class);
options.setTempLocation("gs://bucket/temp");

Pipeline pipeline = Pipeline.create(options);
```

### Direct Runner

```java
PipelineOptions options = PipelineOptionsFactory.create();
options.setRunner(DirectRunner.class);

Pipeline pipeline = Pipeline.create(options);
```

### Runner Comparison

| Runner | Batch | Streaming | Latency |
|--------|-------|-----------|---------|
| Spark | ✓ | ✓ | Seconds |
| Flink | ✓ | ✓ | Milliseconds |
| Dataflow | ✓ | ✓ | Milliseconds |
| Direct | ✓ | Limited | Low |

---

## DoFn and ParDo

### DoFn Basics

```java
public class MyDoFn extends DoFn<String, String> {
    @ProcessElement
    public void processElement(@Element String element, 
                               OutputReceiver<String> out) {
        out.process(element.toUpperCase());
    }
}
```

### Using ParDo

```java
PCollection<String> output = input.apply(
    ParDo.of(new MyDoFn())
);
```

### Stateful DoFn

```java
public class StatefulDoFn extends DoFn<String, String> {
    @StateId("count")
    private final StateSpec<ValueState<Long>> countSpec = 
        StateSpecs.value(VarLongCoder.of());
    
    @ProcessElement
    public void processElement(
        @Element String element,
        @StateId("count") ValueState<Long> countState,
        OutputReceiver<String> out) {
        
        Long count = countState.read();
        if (count == null) count = 0L;
        countState.write(count + 1);
        out.process(element + ": " + count);
    }
}
```

### Timers

```java
public class TimerDoFn extends DoFn<String, String> {
    @TimerId("expiryTimer")
    private final TimerSpec timerSpec = TimerSpecs.timer(TimeDomain.PROCESSING_TIME);
    
    @ProcessElement
    public void processElement(
        @Element String element,
        @TimerId("expiryTimer") Timer timer) {
        
        timer.offset(Duration.standardSeconds(30)).setRelative();
    }
    
    @OnTimer("expiryTimer")
    public void onExpiry(OutputReceiver<String> out) {
        out.process("Timer fired!");
    }
}
```

### Bundle Finalization

```java
public class FinalizingDoFn extends DoFn<String, String> {
    @FinishBundle
    public void finishBundle() {
        // Cleanup after each bundle
        // Flush any buffered data
    }
    
    @ProcessElement
    public void processElement(
        @Element String element,
        OutputReceiver<String> out) {
        out.process(element);
    }
}
```

---

## Best Practices

### Code Organization

1. Use composite transforms for reusability
2. Keep DoFns stateless when possible
3. Use side inputs for small datasets
4. Prefer Combine over GroupByKey when possible

### Performance

1. Use MapElements over ParDo when possible
2. Batch small operations
3. Use Combine for aggregation
4. Avoid unnecessary windowing

### Testing

1. Use PAssert for assertions
2. Test with TestPipeline
3. Use TestStream for streaming tests
4. Mock external systems

### Error Handling

1. Use dead letter patterns
2. Handle malformed data gracefully
3. Log errors with context
4. Use Try/Result patterns

---

## Further Reading

- [Beam Documentation](https://beam.apache.org/)
- [Beam Programming Guide](https://beam.apache.org/documentation/programming-guide/)
- [Beam Examples](https://github.com/apache/beam/tree/master/examples)
