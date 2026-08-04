# Lambda Architecture

Lambda Architecture is a data processing pattern that combines batch and stream processing to handle massive quantities of data. It was designed by Nathan Marz to handle the limitations of both batch and stream processing.

## Table of Contents

- [Overview](#overview)
- [Architecture Components](#architecture-components)
- [Batch Layer](#batch-layer)
- [Speed Layer](#speed-layer)
- [Serving Layer](#serving-layer)
- [Examples](#examples)
- [Best Practices](#best-practices)
- [When to Use](#when-to-use)

## Overview

Lambda Architecture addresses the trade-off between accuracy and latency by maintaining two parallel processing paths:

```
                         ┌─────────────────┐
                         │   All incoming  │
                         │     data        │
                         └────────┬────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
                    ▼                           ▼
            ┌───────────────┐         ┌───────────────┐
            │  Batch Layer  │         │  Speed Layer  │
            │  (accuracy)   │         │  (latency)    │
            └───────┬───────┘         └───────┬───────┘
                    │                         │
                    │    Batch Views          │ Real-time
                    │    (complete)           │ Views
                    │                         │
                    └─────────────┬───────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │ Serving Layer   │
                         │ (merged views)  │
                         └─────────────────┘
```

### Key Principles

- **Immutable, append-only data**: Raw data is never modified
- **Recompute everything**: Batch layer can rebuild from raw data
- **Real-time approximation**: Speed layer provides low-latency views
- **Merging views**: Serving layer combines batch and real-time views

## Architecture Components

### Complete Lambda Architecture

```python
from abc import ABC, abstractmethod
from datetime import datetime
from typing import Any, List

class LambdaArchitecture:
    def __init__(self):
        self.batch_layer = BatchLayer()
        self.speed_layer = SpeedLayer()
        self.serving_layer = ServingLayer()

    def ingest(self, data: Any):
        """Ingest data to both layers"""
        # Write to raw data store (immutable)
        self.raw_store.append(data)

        # Process in batch layer
        self.batch_layer.process(data)

        # Process in speed layer
        self.speed_layer.process(data)

    def query(self, query: Any) -> Any:
        """Merge results from both layers"""
        batch_result = self.batch_layer.query(query)
        real_time_result = self.speed_layer.query(query)
        return self.serving_layer.merge(batch_result, real_time_result)

class RawStore:
    def __init__(self):
        self.data = []

    def append(self, record: dict):
        self.data.append({
            **record,
            'ingestion_time': datetime.now()
        })

    def get_all(self):
        return self.data.copy()
```

## Batch Layer

### Master Dataset

```python
class BatchLayer:
    def __init__(self):
        self.master_dataset = []
        self.batch_views = {}

    def process(self, record: dict):
        """Add record to master dataset"""
        self.master_dataset.append(record)

    def recompute_views(self):
        """Periodically recompute all batch views"""
        for view_name, view_computer in self.batch_views.items():
            print(f"Recomputing {view_name}...")
            result = view_computer.compute(self.master_dataset)
            self.store_view(view_name, result)

    def register_view(self, name: str, computer):
        self.batch_views[name] = computer

    def query(self, view_name: str, query: Any):
        """Query batch view"""
        return self.batch_views[view_name].query(query)

class DailyAggregation:
    def __init__(self):
        self.results = {}

    def compute(self, dataset: list):
        """Compute daily aggregation"""
        from collections import defaultdict
        daily = defaultdict(float)

        for record in dataset:
            date = record['timestamp'].date()
            daily[date] += record['amount']

        self.results = dict(daily)
        return self.results

    def query(self, query_date):
        return self.results.get(query_date, 0)
```

### Batch Processing with MapReduce

```python
from collections import defaultdict
from typing import Tuple

class MapReduceBatchProcessor:
    def __init__(self):
        self.master_data = []

    def ingest(self, record):
        self.master_data.append(record)

    def map_phase(self, record) -> List[Tuple[str, Any]]:
        """Map phase - emit key-value pairs"""
        # Example: emit category and amount
        return [(record['category'], record['amount'])]

    def reduce_phase(self, key: str, values: List[Any]) -> Any:
        """Reduce phase - aggregate values"""
        return sum(values)

    def process_batch(self) -> dict:
        """Execute full MapReduce job"""
        # Map phase
        mapped = []
        for record in self.master_data:
            mapped.extend(self.map_phase(record))

        # Shuffle phase - group by key
        shuffled = defaultdict(list)
        for key, value in mapped:
            shuffled[key].append(value)

        # Reduce phase
        results = {}
        for key, values in shuffled.items():
            results[key] = self.reduce_phase(key, values)

        return results
```

## Speed Layer

### Real-time Processing

```python
from datetime import datetime, timedelta
from collections import defaultdict

class SpeedLayer:
    def __init__(self, window_size: timedelta = timedelta(minutes=5)):
        self.window_size = window_size
        self.real_time_views = {}
        self.buffer = []

    def process(self, record: dict):
        """Process record in real-time"""
        self.buffer.append({
            **record,
            'processing_time': datetime.now()
        })

        # Clean old records
        cutoff = datetime.now() - self.window_size
        self.buffer = [
            r for r in self.buffer
            if r['processing_time'] > cutoff
        ]

        # Update real-time views
        self.update_views()

    def update_views(self):
        """Update real-time aggregation views"""
        for view_name in self.real_time_views:
            self.real_time_views[view_name].update(self.buffer)

    def query(self, view_name: str):
        """Query real-time view"""
        return self.real_time_views[view_name].get()

class RealTimeAggregation:
    def __init__(self):
        self.aggregations = defaultdict(float)

    def update(self, records: list):
        """Update aggregation with new records"""
        for record in records:
            key = self.get_key(record)
            self.aggregations[key] += self.get_value(record)

    def get(self):
        return dict(self.aggregations)

    def get_key(self, record):
        return record.get('category', 'unknown')

    def get_value(self, record):
        return record.get('amount', 0)
```

### Handling Late Data in Speed Layer

```python
class LateDataHandler:
    def __init__(self):
        self.late_buffer = []
        self.processed_windows = set()

    def handle_event(self, event, current_watermark):
        event_time = event['event_time']

        # Check if event is late
        if event_time < current_watermark:
            # Store in late buffer
            self.late_buffer.append(event)

            # Check if window already processed
            window_key = self.get_window_key(event_time)
            if window_key in self.processed_windows:
                # Update existing window
                return self.update_existing_window(event)
            else:
                # Store for later processing
                return {'status': 'STORED', 'reason': 'LATE_DATA'}
        else:
            # Process normally
            return self.process_event(event)

    def get_window_key(self, event_time):
        """Get the window key for an event"""
        return event_time.replace(second=0, microsecond=0)
```

## Serving Layer

### Merging Batch and Real-time Views

```python
class ServingLayer:
    def __init__(self):
        self.batch_views = {}
        self.real_time_views = {}

    def register_batch_view(self, name: str, view):
        self.batch_views[name] = view

    def register_real_time_view(self, name: str, view):
        self.real_time_views[name] = view

    def query(self, view_name: str, query: Any) -> Any:
        """Merge results from batch and real-time views"""
        batch_result = self.batch_views.get(view_name, {}).get(query)
        real_time_result = self.real_time_views.get(view_name, {}).get(query)

        if batch_result is None and real_time_result is None:
            return None

        if batch_result is None:
            return real_time_result

        if real_time_result is None:
            return batch_result

        # Merge results
        return self.merge_results(batch_result, real_time_result)

    def merge_results(self, batch_result: Any, real_time_result: Any) -> Any:
        """Merge batch and real-time results"""
        if isinstance(batch_result, dict) and isinstance(real_time_result, dict):
            merged = batch_result.copy()
            for key, value in real_time_result.items():
                if key in merged:
                    merged[key] = merged[key] + value
                else:
                    merged[key] = value
            return merged
        elif isinstance(batch_result, (int, float)) and isinstance(real_time_result, (int, float)):
            return batch_result + real_time_result
        else:
            return real_time_result
```

## Examples

### Log Analytics Pipeline

```python
from datetime import datetime, timedelta
from collections import defaultdict
import json

class LogAnalyticsLambda:
    def __init__(self):
        self.raw_logs = []
        self.batch_views = {
            'hourly_errors': defaultdict(int),
            'daily_requests': defaultdict(int)
        }
        self.real_time_views = {
            'recent_errors': [],
            'current_rps': []
        }

    def ingest_log(self, log_entry: dict):
        """Ingest a log entry"""
        self.raw_logs.append({
            **log_entry,
            'ingestion_time': datetime.now()
        })

        # Update batch layer
        self.update_batch_views(log_entry)

        # Update speed layer
        self.update_real_time_views(log_entry)

    def update_batch_views(self, log: dict):
        """Update batch views (periodic recomputation)"""
        if log.get('level') == 'ERROR':
            hour = log['timestamp'].replace(minute=0, second=0, microsecond=0)
            self.batch_views['hourly_errors'][hour] += 1

        day = log['timestamp'].date()
        self.batch_views['daily_requests'][day] += 1

    def update_real_time_views(self, log: dict):
        """Update real-time views"""
        # Track recent errors
        if log.get('level') == 'ERROR':
            self.real_time_views['recent_errors'].append({
                'message': log['message'],
                'timestamp': log['timestamp']
            })
            # Keep only last 100 errors
            self.real_time_views['recent_errors'] = \
                self.real_time_views['recent_errors'][-100:]

        # Track requests per second
        now = datetime.now()
        self.real_time_views['current_rps'].append(now)
        # Keep only last minute
        cutoff = now - timedelta(minutes=1)
        self.real_time_views['current_rps'] = [
            t for t in self.real_time_views['current_rps'] if t > cutoff
        ]

    def query_errors(self, start_time: datetime, end_time: datetime) -> dict:
        """Query error counts"""
        # Get batch results
        batch_errors = 0
        current = start_time
        while current <= end_time:
            hour = current.replace(minute=0, second=0, microsecond=0)
            batch_errors += self.batch_views['hourly_errors'].get(hour, 0)
            current += timedelta(hours=1)

        # Get real-time errors
        real_time_errors = sum(
            1 for e in self.real_time_views['recent_errors']
            if start_time <= e['timestamp'] <= end_time
        )

        return {
            'total_errors': batch_errors + real_time_errors,
            'batch_errors': batch_errors,
            'real_time_errors': real_time_errors
        }

    def get_current_rps(self) -> float:
        """Get current requests per second"""
        return len(self.real_time_views['current_rps']) / 60.0
```

### E-commerce Order Processing

```python
from datetime import datetime, timedelta
from collections import defaultdict

class OrderProcessingLambda:
    def __init__(self):
        self.orders = []
        self.batch_views = {
            'daily_revenue': defaultdict(float),
            'product_sales': defaultdict(int)
        }
        self.real_time_views = {
            'recent_orders': [],
            'current_hour_revenue': 0
        }

    def process_order(self, order: dict):
        """Process a new order"""
        self.orders.append(order)

        # Batch layer
        day = order['timestamp'].date()
        self.batch_views['daily_revenue'][day] += order['amount']
        for item in order['items']:
            self.batch_views['product_sales'][item['product_id']] += item['quantity']

        # Speed layer
        self.real_time_views['recent_orders'].append(order)
        self.real_time_views['current_hour_revenue'] += order['amount']

    def get_daily_revenue(self, date: datetime.date) -> float:
        """Get revenue for a specific date"""
        return self.batch_views['daily_revenue'].get(date, 0)

    def get_top_products(self, n: int = 10) -> list:
        """Get top selling products"""
        sorted_products = sorted(
            self.batch_views['product_sales'].items(),
            key=lambda x: x[1],
            reverse=True
        )
        return sorted_products[:n]

    def get_real_time_metrics(self) -> dict:
        """Get real-time metrics"""
        now = datetime.now()
        recent_orders = [
            o for o in self.real_time_views['recent_orders']
            if now - o['timestamp'] < timedelta(minutes=5)
        ]

        return {
            'orders_last_5min': len(recent_orders),
            'revenue_last_5min': sum(o['amount'] for o in recent_orders),
            'current_hour_revenue': self.real_time_views['current_hour_revenue']
        }
```

## Best Practices

### 1. Keep Code DRY

```python
# Shared transformation logic
class SharedTransformations:
    @staticmethod
    def normalize_amount(amount: float) -> float:
        return round(amount, 2)

    @staticmethod
    def parse_timestamp(ts: str) -> datetime:
        return datetime.fromisoformat(ts)

    @staticmethod
    def validate_order(order: dict) -> bool:
        required_fields = ['order_id', 'customer_id', 'amount', 'timestamp']
        return all(field in order for field in required_fields)

# Use in both layers
class BatchProcessor:
    def process(self, record):
        if SharedTransformations.validate_order(record):
            amount = SharedTransformations.normalize_amount(record['amount'])
            # Batch processing logic

class SpeedProcessor:
    def process(self, record):
        if SharedTransformations.validate_order(record):
            amount = SharedTransformations.normalize_amount(record['amount'])
            # Real-time processing logic
```

### 2. Idempotent Processing

```python
class IdempotentProcessor:
    def __init__(self):
        self.processed_ids = set()

    def process(self, event_id: str, data: dict):
        """Process event idempotently"""
        if event_id in self.processed_ids:
            return {'status': 'ALREADY_PROCESSED'}

        # Process
        result = self.process_data(data)

        # Mark as processed
        self.processed_ids.add(event_id)

        return {'status': 'PROCESSED', 'result': result}
```

### 3. Monitor Both Layers

```python
class LambdaMonitor:
    def __init__(self):
        self.batch_metrics = {
            'last_run_time': None,
            'records_processed': 0,
            'errors': 0
        }
        self.speed_metrics = {
            'events_per_second': 0,
            'latency_ms': 0,
            'buffer_size': 0
        }

    def check_health(self) -> dict:
        """Check health of both layers"""
        health = {
            'batch_layer': 'HEALTHY',
            'speed_layer': 'HEALTHY'
        }

        # Check batch layer
        if self.batch_metrics['errors'] > 100:
            health['batch_layer'] = 'UNHEALTHY'

        # Check speed layer
        if self.speed_metrics['latency_ms'] > 1000:
            health['speed_layer'] = 'DEGRADED'

        return health
```

### 4. Handle Reconciliation

```python
class ReconciliationHandler:
    def __init__(self):
        self.discrepancies = []

    def reconcile(self, batch_result: dict, real_time_result: dict):
        """Find discrepancies between batch and real-time results"""
        all_keys = set(batch_result.keys()) | set(real_time_result.keys())

        for key in all_keys:
            batch_val = batch_result.get(key, 0)
            real_time_val = real_time_result.get(key, 0)

            if batch_val != real_time_val:
                self.discrepancies.append({
                    'key': key,
                    'batch_value': batch_val,
                    'real_time_value': real_time_val,
                    'difference': batch_val - real_time_val
                })

        return self.discrepancies

    def get_reconciliation_report(self) -> dict:
        return {
            'total_discrepancies': len(self.discrepancies),
            'total_difference': sum(d['difference'] for d in self.discrepancies),
            'details': self.discrepancies
        }
```

## When to Use

### Use Lambda When:

- You need both historical accuracy and real-time views
- You have massive datasets that can't fit in real-time processing
- You can tolerate some data inconsistency between layers
- You have resources to maintain two processing systems

### Consider Alternatives When:

- You only need real-time processing (use Kappa)
- You only need batch processing (use pure batch)
- Resources are limited (maintenance overhead is high)
- Data consistency is critical (complex reconciliation)

## Further Reading

- [Batch Processing](../batch/) - Batch processing patterns
- [Streaming Processing](../streaming/) - Stream processing
- [Kappa Architecture](../kappa/) - Stream-only alternative
- [Apache Flink](../../streaming/flink/) - Unified batch and streaming
- [Apache Spark](../../streaming/spark-streaming/) - Batch and micro-batch processing
