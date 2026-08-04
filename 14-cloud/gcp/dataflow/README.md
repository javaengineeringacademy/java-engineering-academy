# Google Cloud Dataflow

## Overview

Dataflow is a fully managed service for stream and batch data processing using Apache Beam.

## Core Concepts

```
┌─────────────────────────────────────────────────────────┐
│                    Dataflow                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Source  │  │Transform │  │   Sink   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │   Pipeline    │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Apache Beam SDK

### Python Example
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

def run():
    pipeline_options = PipelineOptions()
    
    with beam.Pipeline(options=pipeline_options) as p:
        (
            p
            | 'Read' >> beam.io.ReadFromText('input.txt')
            | 'Transform' >> beam.Map(lambda x: x.upper())
            | 'Write' >> beam.io.WriteToText('output.txt')
        )

if __name__ == '__main__':
    run()
```

### Java Example
```java
PipelineOptions options = PipelineOptionsFactory.create();
Pipeline p = Pipeline.create(options);

p.apply("Read", TextIO.read().from("input.txt"))
 .apply("Transform", MapElements.via(new SimpleFunction<String, String>() {
     @Override
     public String apply(String input) {
         return input.toUpperCase();
     }
 }))
 .apply("Write", TextIO.write().to("output.txt"));
```

## PTransforms

### Map
```python
# Transform each element
output = input | beam.Map(lambda x: x * 2)

# With multiple arguments
output = input | beam.Map(transform_fn, arg1, arg2)
```

### FlatMap
```python
# Transform and expand
output = input | beam.FlatMap(lambda x: x.split(' '))
```

### Filter
```python
# Filter elements
output = input | beam.Filter(lambda x: x > 10)
```

### GroupByKey
```python
# Group by key
output = input | beam.GroupByKey()
```

### Combine
```python
# Combine values
output = input | beam.CombineGlobally(sum)
```

## Windowing

### Fixed Windows
```python
# Fixed windows of 1 minute
output = input | beam.WindowInto(beam.window.FixedWindows(60))
```

### Sliding Windows
```python
# Sliding windows of 1 minute, slide every 30 seconds
output = input | beam.WindowInto(
    beam.window.SlidingWindows(60, 30)
)
```

### Session Windows
```python
# Session windows with gap duration of 5 minutes
output = input | beam.WindowInto(
    beam.window.Sessions(300)
)
```

## Triggers

### Early Trigger
```python
# Fire early and periodically
output = input | beam.WindowInto(
    beam.window.FixedWindows(60),
    trigger=beam.trigger.AfterWatermark(
        early=beam.trigger.AfterProcessingTime(10)
    ),
    accumulation_mode=beam.trigger.AccumulationMode.DISCARDING
)
```

## Watermarks

```python
# Handle late data
output = input | beam.WindowInto(
    beam.window.FixedWindows(60),
    trigger=beam.trigger.AfterWatermark(
        early=beam.trigger.AfterProcessingTime(10),
        late=beam.trigger.AfterCount(1)
    ),
    allowed_lateness=300,
    accumulation_mode=beam.trigger.AccumulationMode.ACCUMULATING
)
```

## State & Timers

```python
# Use stateful processing
class StatefulDoFn(beam.DoFn):
    def process(self, element, state=beam.DoFn.StateParam(
        beam.transforms.userstate.BagStateSpec('state', beam.VarIntCoder())
    )):
        state.add(element[1])
        yield sum(state.read())
```

## Side Inputs

```python
# Use side inputs
def my_fn(element, side_input):
    return element + side_input

output = input | beam.Map(my_fn, beam.pvalue.AsSingleton(side_input))
```

## Streaming Pipeline

```python
# Read from Pub/Sub
messages = (
    p
    | 'ReadPubSub' >> beam.io.ReadFromPubSub(topic='projects/my-project/topics/my-topic')
    | 'ParseJson' >> beam.Map(json.loads)
    | 'Window' >> beam.WindowInto(beam.window.FixedWindows(60))
    | 'GroupByKey' >> beam.GroupByKey()
    | 'Aggregate' >> beam.MapTuple(aggregate_fn)
)

# Write to BigQuery
messages | 'WriteBQ' >> beam.io.WriteToBigQuery(
    'my-project:my_dataset.my_table',
    schema='event_id:STRING, count:INTEGER',
    write_disposition=beam.io.BigQueryDisposition.WRITE_APPEND
)
```

## Batch Pipeline

```python
# Read from GCS
records = (
    p
    | 'ReadGCS' >> beam.io.ReadFromText('gs://my-bucket/input/*.csv')
    | 'ParseCSV' >> beam.Map(parse_csv)
    | 'Transform' >> beam.Map(transform_record)
)

# Write to multiple sinks
records | 'WriteToBQ' >> beam.io.WriteToBigQuery(...)
records | 'WriteToGCS' >> beam.io.WriteToText(...)
```

## Dataflow Templates

```bash
# Run template
gcloud dataflow jobs run my-job \
  --gcs-location=gs://my-bucket/templates/template \
  --parameters=inputFile=gs://my-bucket/input/outputFile=gs://my-bucket/output

# Create template
gcloud dataflow flex-template run my-flex-job \
  --template-file-gcs-location=gs://my-bucket/templates/flex-template.json
```

## Monitoring

```bash
# Get job status
gcloud dataflow jobs list --status=running

# Get job details
gcloud dataflow jobs describe JOB_ID

# Get job metrics
gcloud monitoring metrics list \
  --filter='metric.type="dataflow.googleapis.com/job/element_count"'
```

## Autoscaling

```python
# Configure autoscaling
from apache_beam.options.pipeline_options import GoogleCloudOptions, StandardOptions

options = PipelineOptions()
gcloud_options = options.view_as(GoogleCloudOptions)
gcloud_options.autoscaling_algorithm = 'THROUGHPUT_BASED'
gcloud_options.max_num_workers = 100
```

## Cost Optimization

- **Use autoscaling** to match demand
- **Implement proper windowing**
- **Use streaming engine** for large datasets
- **Monitor with Dataflow monitoring**
- **Right-size worker types**

## Best Practices

1. **Use streaming engine** for streaming
2. **Implement proper windowing**
3. **Use autoscaling** for cost efficiency
4. **Monitor pipeline metrics**
5. **Implement dead letter queues**
6. **Use side inputs** for lookup data
7. **Implement proper error handling**
8. **Use state and timers** for stateful processing
9. **Test with Dataflow Prime**
10. **Implement proper monitoring**
