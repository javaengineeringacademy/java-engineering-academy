# Batch Processing Pipelines

Batch processing processes large volumes of data in discrete groups (batches) at scheduled intervals. This is the traditional approach for data warehousing, reporting, and analytics workloads.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Batch Processing Patterns](#batch-processing-patterns)
- [Scheduling Strategies](#scheduling-strategies)
- [ETL Patterns](#etl-patterns)
- [Examples](#examples)
- [Best Practices](#best-practices)
- [Common Pitfalls](#common-pitfalls)

## Overview

Batch processing is ideal when:
- Real-time processing is not required
- Large datasets need to be processed efficiently
- Complex transformations are needed
- Cost optimization is important
- Data arrives periodically (daily, hourly)

### Batch Processing Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                    BATCH PROCESSING PIPELINE                     │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Sources           Processing          Storage          Serving  │
│  ───────           ──────────          ───────          ───────  │
│  ┌─────┐          ┌─────────┐         ┌──────┐        ┌──────┐ │
│  │ DB  │──┐       │ Extract │──┐      │ Raw  │──┐     │ BI   │ │
│  └─────┘  │       └─────────┘  │      └──────┘  │     │ Tools│ │
│  ┌─────┐  ├──────>│Transform │──┼─────>│Clean  │──┼────>└──────┘ │
│  │Files│──┤       └─────────┘  │      └──────┘  │     ┌──────┐ │
│  └─────┘  │       ┌─────────┐  │      ┌──────┐  │     │ ML   │ │
│  ┌─────┐  │       │  Load   │──┘      │Gold  │──┘────>│Models│ │
│  │ APIs│──┘       └─────────┘         └──────┘        └──────┘ │
│  └─────┘                                                        │
└──────────────────────────────────────────────────────────────────┘
```

## Core Concepts

### Batch Job Lifecycle

```python
from dataclasses import dataclass
from datetime import datetime
from enum import Enum

class JobStatus(Enum):
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"
    RETRYING = "retrying"

@dataclass
class BatchJob:
    job_id: str
    name: str
    status: JobStatus
    start_time: datetime
    end_time: datetime = None
    records_processed: int = 0
    error_message: str = None

    def is_success(self) -> bool:
        return self.status == JobStatus.COMPLETED

    def duration_seconds(self) -> float:
        if self.end_time:
            return (self.end_time - self.start_time).total_seconds()
        return 0
```

### Data Partitioning

```python
# Time-based partitioning
class TimePartitioner:
    def __init__(self, base_path: str, partition_format: str = "yyyy/MM/dd"):
        self.base_path = base_path
        self.partition_format = partition_format

    def get_partition_path(self, timestamp: datetime) -> str:
        return f"{self.base_path}/{timestamp.strftime(self.partition_format)}"

    def get_partitions(self, start_date, end_date):
        partitions = []
        current = start_date
        while current <= end_date:
            partitions.append(self.get_partition_path(current))
            current += timedelta(days=1)
        return partitions

# Hash-based partitioning
class HashPartitioner:
    def __init__(self, num_partitions: int):
        self.num_partitions = num_partitions

    def get_partition(self, key: str) -> int:
        return hash(key) % self.num_partitions

    def get_partition_path(self, key: str, base_path: str) -> str:
        partition = self.get_partition(key)
        return f"{base_path}/partition={partition}"
```

### Checkpointing

```python
import json
from pathlib import Path

class CheckpointManager:
    def __init__(self, checkpoint_dir: str):
        self.checkpoint_dir = Path(checkpoint_dir)
        self.checkpoint_dir.mkdir(parents=True, exist_ok=True)

    def save_checkpoint(self, job_id: str, state: dict):
        checkpoint_path = self.checkpoint_dir / f"{job_id}.json"
        with open(checkpoint_path, 'w') as f:
            json.dump(state, f)

    def load_checkpoint(self, job_id: str) -> dict:
        checkpoint_path = self.checkpoint_dir / f"{job_id}.json"
        if checkpoint_path.exists():
            with open(checkpoint_path, 'r') as f:
                return json.load(f)
        return None

    def is_checkpointed(self, job_id: str) -> bool:
        return (self.checkpoint_dir / f"{job_id}.json").exists()

# Usage
checkpoint_mgr = CheckpointManager("/checkpoints/daily_etl")

def process_batch(batch_id, data):
    if checkpoint_mgr.is_checkpointed(batch_id):
        state = checkpoint_mgr.load_checkpoint(batch_id)
        if state['status'] == 'completed':
            return state['result']

    result = transform(data)
    checkpoint_mgr.save_checkpoint(batch_id, {
        'status': 'completed',
        'result': result,
        'records': len(data)
    })
    return result
```

## Batch Processing Patterns

### MapReduce Pattern

```python
from functools import reduce

class MapReduceBatchProcessor:
    def __init__(self, mapper, reducer, chunk_size=10000):
        self.mapper = mapper
        self.reducer = reducer
        self.chunk_size = chunk_size

    def process(self, data):
        # Map phase - process chunks in parallel
        mapped_chunks = []
        for i in range(0, len(data), self.chunk_size):
            chunk = data[i:i + self.chunk_size]
            mapped = [self.mapper(record) for record in chunk]
            mapped_chunks.append(mapped)

        # Shuffle phase - group by key
        shuffled = {}
        for chunk in mapped_chunks:
            for key, value in chunk:
                if key not in shuffled:
                    shuffled[key] = []
                shuffled[key].append(value)

        # Reduce phase
        reduced = {}
        for key, values in shuffled.items():
            reduced[key] = self.reducer(values)

        return reduced

# Usage
def mapper(record):
    return (record['category'], record['amount'])

def reducer(values):
    return sum(values)

processor = MapReduceBatchProcessor(mapper, reducer)
result = processor.process(sales_data)
```

### Windowed Processing

```python
from datetime import datetime, timedelta
from typing import List, Dict
from collections import defaultdict

class WindowedBatchProcessor:
    def __init__(self, window_size: timedelta, slide_interval: timedelta):
        self.window_size = window_size
        self.slide_interval = slide_interval

    def process_tumbling_window(self, records: List[Dict]) -> List[Dict]:
        """Non-overlapping fixed windows"""
        windows = defaultdict(list)

        for record in records:
            event_time = record['timestamp']
            window_start = event_time.replace(
                minute=(event_time.minute // (self.window_size.seconds // 60)) * (self.window_size.seconds // 60),
                second=0,
                microsecond=0
            )
            windows[window_start].append(record)

        return [
            {'window_start': k, 'records': v, 'count': len(v)}
            for k, v in sorted(windows.items())
        ]

    def process_sliding_window(self, records: List[Dict]) -> List[Dict]:
        """Overlapping windows"""
        windows = []
        sorted_records = sorted(records, key=lambda r: r['timestamp'])

        if not sorted_records:
            return []

        start = sorted_records[0]['timestamp']
        end = sorted_records[-1]['timestamp']

        current = start
        while current <= end:
            window_end = current + self.window_size
            window_records = [
                r for r in sorted_records
                if current <= r['timestamp'] < window_end
            ]
            if window_records:
                windows.append({
                    'window_start': current,
                    'window_end': window_end,
                    'records': window_records,
                    'count': len(window_records)
                })
            current += self.slide_interval

        return windows
```

### Staged Processing

```python
from abc import ABC, abstractmethod
from typing import Any, List

class PipelineStage(ABC):
    @abstractmethod
    def process(self, data: Any) -> Any:
        pass

    @abstractmethod
    def validate(self, data: Any) -> bool:
        pass

class StagedBatchPipeline:
    def __init__(self):
        self.stages: List[PipelineStage] = []
        self.results = []

    def add_stage(self, stage: PipelineStage):
        self.stages.append(stage)
        return self

    def execute(self, initial_data: Any) -> List[Any]:
        data = initial_data

        for i, stage in enumerate(self.stages):
            print(f"Executing stage {i + 1}: {stage.__class__.__name__}")

            if not stage.validate(data):
                raise ValueError(f"Stage {i} validation failed")

            data = stage.process(data)
            self.results.append({
                'stage': stage.__class__.__name__,
                'records': len(data) if hasattr(data, '__len__') else 1
            })

        return data

# Example stages
class ExtractStage(PipelineStage):
    def process(self, data):
        return [extract_record(r) for r in data]

    def validate(self, data):
        return len(data) > 0

class TransformStage(PipelineStage):
    def process(self, data):
        return [transform_record(r) for r in data]

    def validate(self, data):
        return all('required_field' in r for r in data)

class LoadStage(PipelineStage):
    def process(self, data):
        bulk_insert(data)
        return data

    def validate(self, data):
        return True
```

## Scheduling Strategies

### Cron-based Scheduling

```python
from croniter import croniter
from datetime import datetime

class CronScheduler:
    def __init__(self, cron_expression: str):
        self.cron = croniter(cron_expression, datetime.now())

    def get_next_run(self) -> datetime:
        return self.cron.get_next(datetime)

    def get_schedule(self, count: int = 5) -> list:
        return [self.cron.get_next(datetime) for _ in range(count)]

# Common cron expressions
schedules = {
    'hourly': '0 * * * *',
    'daily_midnight': '0 0 * * *',
    'daily_2am': '0 2 * * *',
    'weekly_sunday': '0 0 * * 0',
    'monthly_first': '0 0 1 * *',
    'every_15min': '*/15 * * * *',
    'every_6hours': '0 */6 * * *',
}
```

### Dependency-based Scheduling

```python
from typing import List, Set
from collections import defaultdict

class DependencyScheduler:
    def __init__(self):
        self.jobs = {}
        self.dependencies = defaultdict(set)
        self.reverse_deps = defaultdict(set)

    def add_job(self, job_id: str, func, dependencies: List[str] = None):
        self.jobs[job_id] = func
        for dep in (dependencies or []):
            self.dependencies[job_id].add(dep)
            self.reverse_deps[dep].add(job_id)

    def get_execution_order(self) -> List[str]:
        """Topological sort for execution order"""
        in_degree = defaultdict(int)
        for job in self.jobs:
            in_degree[job] = len(self.dependencies[job])

        queue = [j for j in self.jobs if in_degree[j] == 0]
        order = []

        while queue:
            job = queue.pop(0)
            order.append(job)

            for dependent in self.reverse_deps[job]:
                in_degree[dependent] -= 1
                if in_degree[dependent] == 0:
                    queue.append(dependent)

        if len(order) != len(self.jobs):
            raise ValueError("Circular dependency detected")

        return order

    def execute(self):
        order = self.get_execution_order()
        for job_id in order:
            print(f"Executing {job_id}")
            self.jobs[job_id]()

# Usage
scheduler = DependencyScheduler()
scheduler.add_job('extract_users', extract_users)
scheduler.add_job('extract_orders', extract_orders)
scheduler.add_job('transform_users', transform_users, ['extract_users'])
scheduler.add_job('transform_orders', transform_orders, ['extract_orders'])
scheduler.add_job('load_warehouse', load_warehouse, ['transform_users', 'transform_orders'])

scheduler.execute()
```

### Priority-based Scheduling

```python
import heapq
from dataclasses import dataclass, field

@dataclass(order=True)
class PriorityJob:
    priority: int
    job_id: str = field(compare=False)
    func: callable = field(compare=False)
    params: dict = field(compare=False, default_factory=dict)

class PriorityScheduler:
    def __init__(self):
        self.queue = []
        self.running = {}

    def add_job(self, job_id: str, func, priority: int = 5, **params):
        job = PriorityJob(priority, job_id, func, params)
        heapq.heappush(self.queue, job)

    def execute_next(self):
        if self.queue:
            job = heapq.heappop(self.queue)
            print(f"Executing {job.job_id} with priority {job.priority}")
            return job.func(**job.params)
        return None

    def execute_all(self):
        results = []
        while self.queue:
            result = self.execute_next()
            results.append(result)
        return results
```

## ETL Patterns

### Incremental Loading

```python
from datetime import datetime
from typing import Optional

class IncrementalETL:
    def __init__(self, source, target, watermark_column: str = 'updated_at'):
        self.source = source
        self.target = target
        self.watermark_column = watermark_column

    def get_last_watermark(self) -> Optional[datetime]:
        """Get the last processed watermark from target"""
        query = f"""
            SELECT MAX({self.watermark_column}) as last_watermark
            FROM {self.target.table_name}
        """
        result = self.target.execute(query)
        return result[0]['last_watermark'] if result else None

    def extract_incremental(self, last_watermark: Optional[datetime]) -> list:
        """Extract only new/changed records"""
        if last_watermark:
            query = f"""
                SELECT * FROM {self.source.table_name}
                WHERE {self.watermark_column} > %s
            """
            return self.source.execute(query, [last_watermark])
        else:
            return self.source.execute(f"SELECT * FROM {self.source.table_name}")

    def load_incremental(self, records: list):
        """Load records with upsert logic"""
        for record in records:
            existing = self.target.find_by_id(record['id'])
            if existing:
                self.target.update(record)
            else:
                self.target.insert(record)

    def run(self):
        last_watermark = self.get_last_watermark()
        print(f"Last watermark: {last_watermark}")

        records = self.extract_incremental(last_watermark)
        print(f"Extracted {len(records)} records")

        if records:
            self.load_incremental(records)
            new_watermark = max(r[self.watermark_column] for r in records)
            print(f"New watermark: {new_watermark}")
```

### Full vs Incremental Comparison

```
Full Refresh:
┌─────────────────────────────────────────────────┐
│ Source  │ Extract All │ Transform │ Overwrite   │
│         │ 1M records  │           │ Target      │
└─────────────────────────────────────────────────┘
Pros: Simple, no duplicates
Cons: Slow, expensive for large datasets

Incremental:
┌─────────────────────────────────────────────────┐
│ Source  │ Extract New │ Transform │ Upsert      │
│         │ 10K records │           │ Target      │
└─────────────────────────────────────────────────┘
Pros: Fast, cost-effective
Cons: Complex, requires watermark tracking
```

### Slowly Changing Dimensions (SCD)

```python
class SCDProcessor:
    def __init__(self, target_table: str, scd_type: int = 2):
        self.target_table = target_table
        self.scd_type = scd_type

    def process_scd_type2(self, source_records: list, key_column: str):
        """
        Type 2: Keep full history with effective dates
        """
        for record in source_records:
            # Check if record exists
            existing = self.find_existing(record[key_column])

            if not existing:
                # New record - insert with effective dates
                self.insert({
                    **record,
                    'effective_from': datetime.now(),
                    'effective_to': None,
                    'is_current': True
                })
            elif self.has_changes(existing, record):
                # Changed record - expire old, insert new
                self.update(existing['surrogate_key'], {
                    'effective_to': datetime.now(),
                    'is_current': False
                })
                self.insert({
                    **record,
                    'effective_from': datetime.now(),
                    'effective_to': None,
                    'is_current': True
                })

    def process_scd_type1(self, source_records: list, key_column: str):
        """
        Type 1: Overwrite, no history
        """
        for record in source_records:
            existing = self.find_existing(record[key_column])
            if existing:
                self.update(existing['surrogate_key'], record)
            else:
                self.insert(record)
```

## Examples

### Daily Sales Aggregation

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import sum, count, avg, col, date_format

class DailySalesAggregation:
    def __init__(self):
        self.spark = SparkSession.builder \
            .appName("DailySalesAggregation") \
            .getOrCreate()

    def extract(self, date: str):
        """Extract raw sales data"""
        return self.spark.read \
            .parquet(f"s3://data-lake/raw/sales/date={date}")

    def transform(self, raw_df):
        """Transform and aggregate"""
        return raw_df.groupBy(
            date_format('sale_date', 'yyyy-MM-dd').alias('sale_date'),
            'store_id',
            'product_category'
        ).agg(
            count('*').alias('transaction_count'),
            sum('amount').alias('total_amount'),
            avg('amount').alias('avg_amount')
        )

    def load(self, agg_df, date: str):
        """Load to data warehouse"""
        agg_df.write \
            .mode('overwrite') \
            .partitionBy('sale_date') \
            .parquet(f"s3://data-warehouse/agg/daily_sales/date={date}")

    def run(self, date: str):
        raw = self.extract(date)
        transformed = self.transform(raw)
        self.load(transformed, date)
        print(f"Processed {raw.count()} records for {date}")
```

### Data Quality Check Pipeline

```python
from dataclasses import dataclass
from typing import List, Callable
from enum import Enum

class CheckResult(Enum):
    PASS = "pass"
    FAIL = "fail"
    WARN = "warn"

@dataclass
class QualityCheck:
    name: str
    check_func: Callable
    severity: str = "error"

class DataQualityPipeline:
    def __init__(self):
        self.checks: List[QualityCheck] = []

    def add_check(self, name: str, check_func: Callable, severity: str = "error"):
        self.checks.append(QualityCheck(name, check_func, severity))

    def run_checks(self, data) -> dict:
        results = {}
        all_passed = True

        for check in self.checks:
            try:
                passed = check.check_func(data)
                results[check.name] = {
                    'result': CheckResult.PASS if passed else CheckResult.FAIL,
                    'severity': check.severity
                }
                if not passed and check.severity == "error":
                    all_passed = False
            except Exception as e:
                results[check.name] = {
                    'result': CheckResult.FAIL,
                    'error': str(e),
                    'severity': check.severity
                }
                if check.severity == "error":
                    all_passed = False

        return {
            'checks': results,
            'passed': all_passed,
            'summary': {
                'total': len(self.checks),
                'passed': sum(1 for r in results.values() if r['result'] == CheckResult.PASS),
                'failed': sum(1 for r in results.values() if r['result'] == CheckResult.FAIL)
            }
        }

# Usage
pipeline = DataQualityPipeline()
pipeline.add_check("no_nulls", lambda df: df.filter("name IS NOT NULL").count() == df.count())
pipeline.add_check("valid_dates", lambda df: df.filter("date >= '2020-01-01'").count() == df.count())
pipeline.add_check("positive_amounts", lambda df: df.filter("amount > 0").count() == df.count(), severity="warning")

results = pipeline.run_checks(dataframe)
if not results['passed']:
    raise ValueError(f"Data quality checks failed: {results}")
```

## Best Practices

### 1. Idempotency

```python
def idempotent_load(batch_id: str, data: list, target_table: str):
    """Ensure batch can be safely re-run"""
    # Check if batch already processed
    existing = query(f"SELECT 1 FROM batch_log WHERE batch_id = '{batch_id}'")
    if existing:
        print(f"Batch {batch_id} already processed, skipping")
        return

    try:
        # Process and load
        bulk_insert(target_table, data)

        # Log successful completion
        insert('batch_log', {
            'batch_id': batch_id,
            'status': 'completed',
            'records': len(data),
            'completed_at': datetime.now()
        })
    except Exception as e:
        # Log failure
        insert('batch_log', {
            'batch_id': batch_id,
            'status': 'failed',
            'error': str(e),
            'failed_at': datetime.now()
        })
        raise
```

### 2. Proper Error Handling

```python
import logging
from typing import Optional

logger = logging.getLogger(__name__)

class RobustBatchJob:
    def __init__(self, max_retries: int = 3):
        self.max_retries = max_retries

    def execute_with_retry(self, func, *args, **kwargs):
        last_error = None
        for attempt in range(self.max_retries):
            try:
                return func(*args, **kwargs)
            except Exception as e:
                last_error = e
                logger.warning(f"Attempt {attempt + 1} failed: {e}")
                if attempt < self.max_retries - 1:
                    time.sleep(2 ** attempt)  # Exponential backoff

        raise Exception(f"All {self.max_retries} attempts failed: {last_error}")

    def safe_execute(self, func, fallback=None, *args, **kwargs):
        """Execute with graceful degradation"""
        try:
            return func(*args, **kwargs)
        except Exception as e:
            logger.error(f"Execution failed: {e}")
            if fallback:
                return fallback(*args, **kwargs)
            raise
```

### 3. Resource Management

```python
class ResourceAwareProcessor:
    def __init__(self, memory_limit_gb: float = 8.0, cpu_cores: int = 4):
        self.memory_limit = memory_limit_gb
        self.cpu_cores = cpu_cores

    def get_optimal_batch_size(self, record_size_bytes: int) -> int:
        """Calculate optimal batch size based on available memory"""
        available_memory = self.memory_limit * 1024 * 1024 * 1024 * 0.8  # 80% utilization
        return int(available_memory / record_size_bytes)

    def process_in_chunks(self, data, chunk_size: int = None):
        """Process data in memory-efficient chunks"""
        if chunk_size is None:
            chunk_size = self.get_optimal_batch_size(1024)  # Assume 1KB per record

        results = []
        for i in range(0, len(data), chunk_size):
            chunk = data[i:i + chunk_size]
            processed = self.process_chunk(chunk)
            results.extend(processed)

            # Log memory usage
            import psutil
            memory_percent = psutil.virtual_memory().percent
            if memory_percent > 90:
                logger.warning(f"High memory usage: {memory_percent}%")

        return results
```

## Common Pitfalls

### 1. Data Skew

```python
# Problem: Uneven partition sizes
# Solution: Salting or custom partitioning

def handle_data_skew(df, skew_column, num_partitions=100):
    """Add random salt to handle data skew"""
    from pyspark.sql.functions import rand, concat, lit

    # Add random salt
    salted_df = df.withColumn(
        "salt",
        (rand() * num_partitions).cast("int")
    ).withColumn(
        "salted_key",
        concat(col(skew_column), lit("_"), col("salt"))
    )

    return salted_df.repartition(num_partitions, "salted_key")
```

### 2. Small Files Problem

```python
def merge_small_files(spark, input_path, output_path, target_size_mb=128):
    """Merge small files into optimal-sized files"""
    df = spark.read.parquet(input_path)

    # Calculate optimal partition count
    row_count = df.count()
    estimated_size_mb = (row_count * 200) / (1024 * 1024)  # 200 bytes per row
    optimal_partitions = max(1, int(estimated_size_mb / target_size_mb))

    # Repartition and write
    df.repartition(optimal_partitions) \
      .write \
      .mode("overwrite") \
      .parquet(output_path)
```

### 3. Late Arriving Data

```python
def handle_late_data(current_batch, late_batch, max_late_hours=24):
    """Handle late arriving data"""
    current_watermark = current_batch['watermark']
    late_cutoff = current_watermark - timedelta(hours=max_late_hours)

    # Filter truly late data
    valid_late = [
        r for r in late_batch['records']
        if r['event_time'] >= late_cutoff
    ]

    # Process and merge
    if valid_late:
        merged = merge_with_existing(current_batch['data'], valid_late)
        update_watermark(current_batch['id'], current_batch['watermark'])
        return merged

    return current_batch['data']
```

## Further Reading

- [Streaming Pipelines](../streaming/) - Real-time processing
- [Lambda Architecture](../lambda/) - Batch + speed layers
- [Kappa Architecture](../kappa/) - Stream-only processing
- [Orchestration](../../orchestration/) - Workflow management
- [ETL Patterns](../../etl/) - Extract, Transform, Load
