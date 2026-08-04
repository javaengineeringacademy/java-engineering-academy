# Luigi

## Overview

Luigi is a Python module for building complex pipelines of batch jobs. Developed by Spotify, it handles dependency resolution, workflow management, visualization, and provides built-in support for Hadoop, HDFS, and Google Cloud. Luigi helps you stop managing cross-dependencies between scripts manually and start building complex pipelines of batch jobs.

## Table of Contents

- [Architecture](#architecture)
- [Core Concepts](#core-concepts)
- [Tasks and Targets](#tasks-and-targets)
- [Dependencies](#dependencies)
- [Parameters](#parameters)
- [Central Scheduler](#central-scheduler)
- [Visualization](#visualization)
- [Integration](#integration)
- [Error Handling](#error-handling)
- [Best Practices](#best-practices)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    LUIGI ARCHITECTURE                         │
├─────────────────────────────────────────────────────────────┤
│  Task Definitions                                           │
│  • Define tasks with inputs/outputs                         │
│  • Declare dependencies via requires()                      │
│  • Implement run() method                                   │
├─────────────────────────────────────────────────────────────┤
│  Dependency Graph                                            │
│  • Automatic dependency resolution                          │
│  • Circular dependency detection                            │
│  • DAG construction                                         │
├─────────────────────────────────────────────────────────────┤
│  Execution Engine                                           │
│  • Local execution (default)                                │
│  • Central scheduler (multi-worker)                         │
│  • Hadoop integration                                       │
├─────────────────────────────────────────────────────────────┤
│  Target Management                                          │
│  • File-based targets (Local, HDFS, S3)                     │
│  • Database targets                                         │
│  • Custom targets                                           │
│  • Atomic writes via TemporaryFile                           │
└─────────────────────────────────────────────────────────────┘
```

### Key Components

| Component | Description |
|-----------|-------------|
| Task | Python class defining a unit of work |
| Target | File or resource produced by a task |
| Parameter | Configuration value for a task |
| Worker | Process that executes tasks |
| Scheduler | Coordinates task execution |

## Core Concepts

### Basic Task

```python
import luigi
import pandas as pd

class ExtractData(luigi.Task):
    """Extract data from source system"""
    date = luigi.DateParameter()

    def output(self):
        return luigi.LocalTarget(f'/tmp/extract_{self.date}.csv')

    def run(self):
        df = extract_from_source(self.date)
        df.to_csv(self.output().path, index=False)
```

### Task Lifecycle

```
┌─────────────────────────────────────────────────────────────┐
│  Task Execution Flow                                        │
│                                                             │
│  requires() ──→ complete() ──→ run() ──→ output()          │
│       │              │            │           │              │
│       ▼              ▼            ▼           ▼              │
│  Get deps      Check done    Execute     Verify output     │
│  (recursive)   (skip if yes)             (atomic write)    │
└─────────────────────────────────────────────────────────────┘
```

### Complete Pipeline Example

```python
import luigi
import pandas as pd
from sqlalchemy import create_engine
from datetime import datetime, timedelta

class ExtractOrders(luigi.Task):
    """Extract orders from ERP system"""
    date = luigi.DateParameter()
    
    def output(self):
        return luigi.LocalTarget(f'/data/staging/orders_{self.date}.csv')
    
    def run(self):
        engine = create_engine('postgresql://user:pass@host/erp')
        df = pd.read_sql(
            f"SELECT * FROM orders WHERE order_date = '{self.date}'",
            engine
        )
        df.to_csv(self.output().path, index=False)

class ExtractCustomers(luigi.Task):
    """Extract customer data"""
    date = luigi.DateParameter()
    
    def output(self):
        return luigi.LocalTarget(f'/data/staging/customers_{self.date}.csv')
    
    def run(self):
        engine = create_engine('postgresql://user:pass@host/crm')
        df = pd.read_sql("SELECT * FROM customers", engine)
        df.to_csv(self.output().path, index=False)

class TransformOrders(luigi.Task):
    """Transform and enrich orders"""
    date = luigi.DateParameter()
    
    def requires(self):
        return {
            'orders': ExtractOrders(date=self.date),
            'customers': ExtractCustomers(date=self.date)
        }
    
    def output(self):
        return luigi.LocalTarget(f'/data/transformed/orders_{self.date}.csv')
    
    def run(self):
        orders = pd.read_csv(self.input()['orders'].path)
        customers = pd.read_csv(self.input()['customers'].path)
        
        # Join and transform
        result = orders.merge(
            customers[['customer_id', 'segment', 'region']],
            on='customer_id',
            how='left'
        )
        
        result['total_amount'] = result['quantity'] * result['unit_price']
        result['processed_at'] = datetime.now().isoformat()
        
        result.to_csv(self.output().path, index=False)

class LoadToWarehouse(luigi.Task):
    """Load transformed data to warehouse"""
    date = luigi.DateParameter()
    
    def requires(self):
        return TransformOrders(date=self.date)
    
    def output(self):
        return luigi.LocalTarget(f'/data/loaded/.orders_{self.date}.done')
    
    def run(self):
        df = pd.read_csv(self.input().path)
        engine = create_engine('snowflake://account/db/schema')
        df.to_sql('fact_orders', engine, if_exists='append', index=False)
        
        # Mark as complete
        with self.output().open('w') as f:
            f.write(f'Loaded {len(df)} rows at {datetime.now()}')

# Daily pipeline
class DailyOrderPipeline(luigi.WrapperTask):
    """Wrapper for daily ETL pipeline"""
    date = luigi.DateParameter(default=datetime.now().date())
    
    def requires(self):
        return LoadToWarehouse(date=self.date)
```

## Tasks and Targets

### Target Types

```python
# Local file
target = luigi.LocalTarget('/tmp/output.csv')

# HDFS file
target = luigi.HdfsTarget('/user/data/output/')

# S3 (via luigi-s3)
target = S3Target('s3://bucket/path/to/file.csv')

# MySQL target
target = luigi.mysql_target.MySqlTarget(
    host='localhost',
    database='mydb',
    table='mytable',
    update_id='unique_id'
)

# Temporary target (atomic writes)
target = luigi.LocalTarget(is_tmp=True)
```

### Target Interface

```python
class MyTarget(luigi.Target):
    def exists(self):
        """Check if target exists"""
        return os.path.exists(self.path)
    
    def open(self, mode='r'):
        """Open target for reading/writing"""
        return open(self.path, mode)
    
    def remove(self):
        """Remove target"""
        os.remove(self.path)
```

### Atomic Writes

```python
class SafeWriteTask(luigi.Task):
    def output(self):
        return luigi.LocalTarget('/data/output/result.csv', is_tmp=True)
    
    def run(self):
        # Luigi writes to temp file first, then renames
        # This ensures atomicity
        with self.output().open('w') as f:
            pd.DataFrame(data).to_csv(f, index=False)
```

## Dependencies

### Static Dependencies

```python
class ChildTask(luigi.Task):
    def requires(self):
        return ParentTask(date=self.date)
    
    def requires(self):
        # Multiple dependencies
        return [
            TaskA(param=value),
            TaskB(param=value)
        ]
    
    def requires(self):
        # Named dependencies
        return {
            'orders': ExtractOrders(date=self.date),
            'customers': ExtractCustomers(date=self.date)
        }
```

### Dynamic Dependencies

```python
class DynamicTask(luigi.Task):
    date = luigi.DateParameter()
    
    def requires(self):
        # Generate dependencies based on data
        dates = self.get_processing_dates()
        return [ProcessDate(date=d) for d in dates]
    
    def get_processing_dates(self):
        # Logic to determine which dates need processing
        last_processed = get_last_processed_date()
        return pd.date_range(last_processed, self.date)
```

### Recursive Dependencies

```python
class RecursiveTask(luigi.Task):
    level = luigi.IntParameter()
    
    def requires(self):
        if self.level > 0:
            return RecursiveTask(level=self.level - 1)
        return None
    
    def run(self):
        # Process at this level
        pass
```

## Parameters

### Built-in Parameters

```python
class ConfigurableTask(luigi.Task):
    # Date parameter
    date = luigi.DateParameter()
    
    # Date interval
    date_range = luigi.DateIntervalParameter()
    
    # Integer parameter
    batch_size = luigi.IntParameter(default=1000)
    
    # Float parameter
    threshold = luigi.FloatParameter(default=0.5)
    
    # Boolean parameter
    debug = luigi.BooleanParameter(default=False)
    
    # Choice parameter
    format = luigi.ChoiceParameter(
        choices=['csv', 'json', 'parquet'],
        default='csv'
    )
    
    # Path parameter
    output_path = luigi.PathParameter(is_dir=True)
    
    # Optional parameter
    optional_param = luigi.OptionalParameter(default=None)
```

### Command Line Usage

```bash
# Run with parameters
python pipeline.py ExtractOrders --date 2024-01-15

# Multiple parameters
python pipeline.py LoadData --date 2024-01-15 --batch-size 5000

# Boolean flags
python pipeline.py ProcessData --date 2024-01-15 --debug

# List available tasks
python pipeline.py --help

# Run with local scheduler
python pipeline.py --local-scheduler LoadData --date 2024-01-15
```

### Custom Parameters

```python
class MyParam(luigi.Parameter):
    def parse(self, s):
        # Custom parsing logic
        return custom_parse(s)
    
    def serialize(self, x):
        # Custom serialization
        return custom_serialize(x)

class MyTask(luigi.Task):
    my_param = MyParam(default='value')
```

## Central Scheduler

### Running the Scheduler

```bash
# Start central scheduler
luigid --port 8082 --background --log-path /var/log/luigi/

# With authentication
luigid --port 8082 --username admin --password secret

# View at http://localhost:8082
```

### Worker Configuration

```python
# Run with multiple workers
luigi.build(
    [LoadData(date='2024-01-15')],
    scheduler_host='localhost',
    scheduler_port=8082,
    workers=4,
    log_level='INFO'
)
```

### Scheduler Features

- Task prioritization
- Resource management
- Worker monitoring
- Failure handling
- Retry logic
- Historical task data

## Visualization

### Generate Pipeline Graph

```python
# Generate DOT graph
luigi.build(
    [LoadData(date='2024-01-15')],
    visualization='dot',
    output='/tmp/pipeline.dot'
)

# Convert to image
# dot -Tpng /tmp/pipeline.dot -o /tmp/pipeline.png
```

### Web UI Features

The central scheduler provides a web interface showing:

- Task status (running, pending, failed, done)
- Worker status
- Task history
- Resource usage
- Failures and retries

## Integration

### Hadoop Integration

```python
class HadoopTask(luigi.Task):
    def output(self):
        return luigi.HdfsTarget('/user/data/output/')
    
    def run(self):
        # Luigi can run Hadoop jobs
        job = luigi.hadoop.Job(
            jobconfs={
                'mapred.reduce.tasks': 10
            }
        )
        job.run()
```

### External Programs

```python
class ExternalProgramTask(luigi.Task):
    def program_args(self):
        return ['python', 'scripts/process.py', 
                '--input', self.input().path,
                '--output', self.output().path]
    
    def output(self):
        return luigi.LocalTarget('/tmp/output.csv')
```

### Database Integration

```python
class DatabaseTask(luigi.Task):
    def output(self):
        return luigi.mysql_target.MySqlTarget(
            host='localhost',
            database='warehouse',
            table='fact_orders',
            update_id=f'orders_{self.date}'
        )
    
    def run(self):
        df = self.get_data()
        engine = create_engine('mysql://...')
        df.to_sql('fact_orders', engine, if_exists='append')
```

## Error Handling

### Custom Exceptions

```python
class DataValidationError(luigi.TaskException):
    pass

class ValidatedTask(luigi.Task):
    def run(self):
        data = self.load_data()
        if not self.validate(data):
            raise DataValidationError("Data validation failed")
        self.process(data)
```

### Retry Logic

```python
class RetryableTask(luigi.Task):
    def run(self):
        try:
            self.process_data()
        except TransientError:
            # Luigi will retry based on worker configuration
            raise
    
    @luigi.Task.event_handler(luigi.Event.FAILURE)
    def on_failure(self, exception):
        send_alert(f"Task failed: {exception}")
```

### Event Handlers

```python
class MonitoredTask(luigi.Task):
    @luigi.Task.event_handler(luigi.Event.START)
    def on_start(self):
        print(f"Starting {self.__class__.__name__}")
    
    @luigi.Task.event_handler(luigi.Event.SUCCESS)
    def on_success(self):
        print(f"Completed {self.__class__.__name__}")
    
    @luigi.Task.event_handler(luigi.Event.FAILURE)
    def on_failure(self, exception):
        print(f"Failed {self.__class__.__name__}: {exception}")
    
    @luigi.Task.event_handler(luigi.Event.PROCESSING_TIME)
    def on_processing_time(self, time):
        print(f"Processing time: {time}")
```

## Configuration

### luigi.cfg

```ini
[core]
default_scheduler_host: localhost
default_scheduler_port: 8082
default_worker_count: 4
retry_count: 3
retry_delay: 300

[logging]
level: INFO
formatter: %(asctime)s - %(name)s - %(levelname)s - %(message)s
filename: /var/log/luigi/pipeline.log

[hdfs]
client: webhdfs
namenode: http://namenode:50070

[database]
host: localhost
port: 3306
database: luigi_state
```

### Environment Variables

```bash
export LUIGI_CONFIG_PATH=/etc/luigi/luigi.cfg
export LUIGI_SCHEDULER_HOST=localhost
export LUIGI_SCHEDULER_PORT=8082
```

## Testing

### Unit Testing

```python
import unittest
from luigi import LocalTarget
import luigi.mock
import tempfile

class TestExtractTask(unittest.TestCase):
    def setUp(self):
        luigi.mock.Target.fs = luigi.mock.LocalFileSystem()
    
    def test_extract_orders(self):
        task = ExtractOrders(date='2024-01-15')
        # Run task
        task.run()
        
        # Verify output
        self.assertTrue(task.output().exists())
    
    def test_task_complete(self):
        task = ExtractOrders(date='2024-01-15')
        self.assertFalse(task.complete())

class TestPipeline(unittest.TestCase):
    def test_full_pipeline(self):
        # Build pipeline with mock targets
        with tempfile.TemporaryDirectory() as tmpdir:
            # Configure tasks to use temp directory
            pipeline = DailyOrderPipeline(date='2024-01-15')
            luigi.build([pipeline], local_scheduler=True)
```

### Integration Testing

```python
def test_pipeline_integration():
    # Test with real dependencies
    pipeline = DailyOrderPipeline(date='2024-01-15')
    
    # Run and verify
    result = luigi.build(
        [pipeline],
        local_scheduler=True,
        log_level='WARNING'
    )
    
    assert result
```

## Best Practices

### 1. Idempotent Tasks

```python
class IdempotentTask(luigi.Task):
    def run(self):
        # Check if already completed
        if self.output().exists():
            return
        
        # Process data
        self.process_data()
        
        # Output is written atomically
        # Task is idempotent because output() checks existence
```

### 2. Small, Focused Tasks

```python
# Good: Small, focused tasks
class Extract(luigi.Task): ...
class Transform(luigi.Task): ...
class Validate(luigi.Task): ...
class Load(luigi.Task): ...

# Bad: Large monolithic task
class Everything(luigi.Task):
    def run(self):
        extract()
        transform()
        validate()
        load()
```

### 3. Use Parameters for Configuration

```python
# Good: Configurable
class ProcessTask(luigi.Task):
    date = luigi.DateParameter()
    batch_size = luigi.IntParameter(default=1000)
    output_path = luigi.PathParameter()

# Bad: Hardcoded values
class ProcessTask(luigi.Task):
    def run(self):
        date = '2024-01-15'  # Hardcoded!
        process(date)
```

### 4. Resource Management

```python
class ResourceAwareTask(luigi.Task):
    def resources(self):
        return {'database_connection': 1}
    
    def run(self):
        # Only runs when resource is available
        pass
```

### 5. Documentation

```python
class DocumentedTask(luigi.Task):
    """
    Extract daily order data from ERP system.
    
    This task extracts all orders for a given date
    and saves them to a CSV file for downstream processing.
    
    Parameters:
        date: The date to extract orders for
    
    Output:
        CSV file with order data
    """
    date = luigi.DateParameter()
```

## Further Reading

- [Luigi Documentation](https://luigi.readthedocs.io/)
- [Luigi GitHub](https://github.com/spotify/luigi)
- [Luigi Examples](https://github.com/spotify/luigi/tree/master/examples)
- [Building Data Pipelines with Luigi](https://www.oreilly.com/library/view/building-data-pipelines/9781492048435/)
