# Streaming Processing Pipelines

Stream processing handles data in real-time or near real-time as it arrives. Unlike batch processing, streaming processes individual events or small micro-batches continuously.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Stream Processing Models](#stream-processing-models)
- [Windowing Strategies](#windowing-strategies)
- [State Management](#state-management)
- [Examples](#examples)
- [Best Practices](#best-practices)

## Overview

Stream processing is ideal when:
- Low latency is required (milliseconds to seconds)
- Real-time monitoring and alerting needed
- Event-driven architectures
- Continuous data transformation
- Fraud detection, IoT, real-time analytics

### Streaming vs Batch Comparison

| Aspect | Batch | Streaming |
|--------|-------|-----------|
| Latency | Minutes-hours | Milliseconds-seconds |
| Throughput | Very high | High |
| Completeness | Complete data | Approximate data |
| State | Stateless | Stateful |
| Complexity | Lower | Higher |
| Cost | Lower per GB | Higher per GB |
| Use cases | Analytics, reporting | Monitoring, alerts |

## Core Concepts

### Event Time vs Processing Time

```python
from datetime import datetime
from dataclasses import dataclass

@dataclass
class StreamingEvent:
    event_id: str
    event_type: str
    payload: dict
    event_time: datetime      # When event occurred
    processing_time: datetime  # When event was received

    @property
    def latency_ms(self) -> float:
        return (self.processing_time - self.event_time).total_seconds() * 1000

# Event time = when the event happened in the real world
# Processing time = when the system received the event
# Latency = processing_time - event_time
```

### Watermarks

```python
from datetime import timedelta

class Watermark:
    """Tracks progress of event time in a stream"""
    def __init__(self, max_out_of_orderness: timedelta = timedelta(seconds=30)):
        self.max_out_of_orderness = max_out_of_orderness
        self.current_watermark = None

    def update(self, event_time: datetime):
        """Update watermark based on observed event times"""
        new_watermark = event_time - self.max_out_of_orderness
        if self.current_watermark is None or new_watermark > self.current_watermark:
            self.current_watermark = new_watermark

    def is_late(self, event_time: datetime) -> bool:
        """Check if event is late based on current watermark"""
        if self.current_watermark is None:
            return False
        return event_time < self.current_watermark

    def get_watermark(self) -> datetime:
        return self.current_watermark

# Usage
watermark = Watermark(max_out_of_orderness=timedelta(seconds=30))
watermark.update(datetime(2024, 1, 1, 10, 0, 0))
# Watermark is now 09:59:30

# Event at 09:59:45 is not late
# Event at 09:59:20 is late
```

### Exactly-Once Semantics

```python
class ExactlyOnceProcessor:
    """Ensures each event is processed exactly once"""
    def __init__(self, checkpoint_store):
        self.checkpoint_store = checkpoint_store
        self.processing_buffer = {}

    def process_event(self, event):
        # Check if already processed
        if self.checkpoint_store.is_processed(event.event_id):
            return self.checkpoint_store.get_result(event.event_id)

        # Process event
        result = self.process(event)

        # Store result and mark as processed
        self.checkpoint_store.mark_processed(event.event_id, result)

        return result

    def process(self, event):
        # Business logic here
        return {"event_id": event.event_id, "result": "processed"}
```

## Stream Processing Models

### Micro-batch Processing

```python
from datetime import timedelta
import time

class MicroBatchProcessor:
    def __init__(self, batch_interval: timedelta = timedelta(seconds=5)):
        self.batch_interval = batch_interval
        self.buffer = []

    def process_stream(self, event_stream):
        last_batch_time = time.time()

        for event in event_stream:
            self.buffer.append(event)

            current_time = time.time()
            if current_time - last_batch_time >= self.batch_interval.total_seconds():
                if self.buffer:
                    self.process_batch(self.buffer)
                    self.buffer = []
                    last_batch_time = current_time

    def process_batch(self, batch):
        """Process accumulated events as a batch"""
        # Similar to batch processing
        transformed = [self.transform(e) for e in batch]
        self.sink(transformed)
```

### Continuous Processing

```python
class ContinuousProcessor:
    def __init__(self):
        self.state = {}

    def process_event(self, event):
        """Process each event immediately"""
        # Update state
        key = self.get_key(event)
        if key not in self.state:
            self.state[key] = self.init_state(event)
        else:
            self.state[key] = self.update_state(self.state[key], event)

        # Emit results immediately
        return self.emit(event, self.state[key])
```

## Windowing Strategies

### Tumbling Windows

```python
from datetime import datetime, timedelta
from collections import defaultdict

class TumblingWindow:
    def __init__(self, window_size: timedelta):
        self.window_size = window_size

    def get_window_key(self, event_time: datetime) -> datetime:
        """Get the window start time for an event"""
        timestamp = event_time.timestamp()
        window_seconds = self.window_size.total_seconds()
        window_start = timestamp - (timestamp % window_seconds)
        return datetime.fromtimestamp(window_start)

    def assign_windows(self, events):
        """Assign events to windows"""
        windows = defaultdict(list)
        for event in events:
            window_key = self.get_window_key(event['event_time'])
            windows[window_key].append(event)
        return dict(windows)

# Example: 5-minute tumbling windows
tumbling = TumblingWindow(timedelta(minutes=5))
# Event at 10:07:30 goes to window starting at 10:05:00
# Event at 10:08:15 goes to window starting at 10:05:00
# Event at 10:11:00 goes to window starting at 10:10:00
```

### Sliding Windows

```python
class SlidingWindow:
    def __init__(self, window_size: timedelta, slide_interval: timedelta):
        self.window_size = window_size
        self.slide_interval = slide_interval

    def assign_windows(self, events):
        """Assign events to all overlapping windows"""
        windows = defaultdict(list)

        if not events:
            return {}

        min_time = min(e['event_time'] for e in events)
        max_time = max(e['event_time'] for e in events)

        current_start = min_time
        while current_start <= max_time:
            window_end = current_start + self.window_size
            window_events = [
                e for e in events
                if current_start <= e['event_time'] < window_end
            ]
            if window_events:
                windows[current_start] = window_events
            current_start += self.slide_interval

        return dict(windows)

# Example: 10-minute windows sliding every 1 minute
sliding = SlidingWindow(timedelta(minutes=10), timedelta(minutes=1))
# Each event appears in up to 10 different windows
```

### Session Windows

```python
class SessionWindow:
    def __init__(self, gap_duration: timedelta):
        self.gap_duration = gap_duration

    def assign_windows(self, events):
        """Group events by user activity sessions"""
        sessions = defaultdict(list)
        sorted_events = sorted(events, key=lambda e: (e['user_id'], e['event_time']))

        current_session = None
        current_user = None

        for event in sorted_events:
            user_id = event['user_id']

            if user_id != current_user:
                # New user
                current_user = user_id
                current_session = event['event_time']
                sessions[(user_id, current_session)].append(event)
            else:
                # Same user - check if within session gap
                last_event_time = max(
                    e['event_time'] for e in sessions[(user_id, current_session)]
                )
                if event['event_time'] - last_event_time <= self.gap_duration:
                    sessions[(user_id, current_session)].append(event)
                else:
                    # New session
                    current_session = event['event_time']
                    sessions[(user_id, current_session)].append(event)

        return dict(sessions)

# Example: Session gap of 30 minutes
# User active at 10:00, 10:05, 10:10 - same session
# User inactive until 10:45 - new session starts
```

## State Management

### Stateful Processing

```python
from typing import Dict, Any
import json

class StatefulProcessor:
    def __init__(self):
        self.state: Dict[str, Any] = {}

    def update_state(self, key: str, event: dict):
        """Update state based on incoming event"""
        if key not in self.state:
            self.state[key] = {
                'count': 0,
                'sum': 0,
                'min': float('inf'),
                'max': float('-inf'),
                'last_updated': None
            }

        state = self.state[key]
        state['count'] += 1
        state['sum'] += event.get('value', 0)
        state['min'] = min(state['min'], event.get('value', 0))
        state['max'] = max(state['max'], event.get('value', 0))
        state['last_updated'] = event['timestamp']

        return state

    def get_state(self, key: str):
        return self.state.get(key)

    def clear_state(self, key: str):
        if key in self.state:
            del self.state[key]
```

### State Backend

```python
from abc import ABC, abstractmethod

class StateBackend(ABC):
    @abstractmethod
    def get(self, key: str) -> dict:
        pass

    @abstractmethod
    def put(self, key: str, value: dict):
        pass

    @abstractmethod
    def delete(self, key: str):
        pass

class RocksDBStateBackend(StateBackend):
    """Local state storage for low-latency access"""
    def __init__(self, db_path: str):
        import rocksdb
        self.db = rocksdb.DB(db_path, rocksdb.Options(create_if_missing=True))

    def get(self, key: str) -> dict:
        value = self.db.get(key.encode())
        if value:
            return json.loads(value.decode())
        return None

    def put(self, key: str, value: dict):
        self.db.put(key.encode(), json.dumps(value).encode())

    def delete(self, key: str):
        self.db.delete(key.encode())

class RedisStateBackend(StateBackend):
    """Distributed state for scalable processing"""
    def __init__(self, host: str = 'localhost', port: int = 6379):
        import redis
        self.client = redis.Redis(host=host, port=port, decode_responses=True)

    def get(self, key: str) -> dict:
        value = self.client.get(key)
        if value:
            return json.loads(value)
        return None

    def put(self, key: str, value: dict):
        self.client.set(key, json.dumps(value))

    def delete(self, key: str):
        self.client.delete(key)
```

### State Cleanup

```python
from datetime import datetime, timedelta

class StateManager:
    def __init__(self, state_backend: StateBackend, ttl: timedelta = timedelta(hours=24)):
        self.state_backend = state_backend
        self.ttl = ttl
        self.access_times = {}

    def update_access_time(self, key: str):
        self.access_times[key] = datetime.now()

    def cleanup_expired(self):
        """Remove states older than TTL"""
        now = datetime.now()
        expired_keys = [
            key for key, access_time in self.access_times.items()
            if now - access_time > self.ttl
        ]

        for key in expired_keys:
            self.state_backend.delete(key)
            del self.access_times[key]

        return len(expired_keys)
```

## Examples

### Real-time Fraud Detection

```python
from datetime import datetime, timedelta
from collections import defaultdict

class FraudDetector:
    def __init__(self):
        self.transaction_history = defaultdict(list)
        self.velocity_threshold = 5  # Max transactions per minute
        self.amount_threshold = 10000  # Max single transaction
        self.daily_limit = 50000  # Daily spending limit

    def process_transaction(self, transaction: dict) -> dict:
        user_id = transaction['user_id']
        amount = transaction['amount']
        timestamp = transaction['timestamp']

        # Check single transaction limit
        if amount > self.amount_threshold:
            return self.flag_transaction(transaction, 'HIGH_AMOUNT')

        # Check velocity (transactions per minute)
        recent_transactions = [
            t for t in self.transaction_history[user_id]
            if timestamp - t['timestamp'] < timedelta(minutes=1)
        ]
        if len(recent_transactions) >= self.velocity_threshold:
            return self.flag_transaction(transaction, 'HIGH_VELOCITY')

        # Check daily limit
        daily_total = sum(
            t['amount'] for t in self.transaction_history[user_id]
            if timestamp.date() == t['timestamp'].date()
        )
        if daily_total + amount > self.daily_limit:
            return self.flag_transaction(transaction, 'DAILY_LIMIT_EXCEEDED')

        # Store transaction and approve
        self.transaction_history[user_id].append(transaction)
        return {'status': 'APPROVED', 'transaction': transaction}

    def flag_transaction(self, transaction: dict, reason: str) -> dict:
        return {
            'status': 'FLAGGED',
            'reason': reason,
            'transaction': transaction,
            'flagged_at': datetime.now()
        }
```

### Real-time Analytics Dashboard

```python
from collections import defaultdict
from datetime import datetime, timedelta

class RealTimeAnalytics:
    def __init__(self):
        self.metrics = defaultdict(lambda: {
            'count': 0,
            'sum': 0,
            'min': float('inf'),
            'max': float('-inf')
        })
        self.time_windows = defaultdict(lambda: defaultdict(float))

    def record_event(self, metric_name: str, value: float, timestamp: datetime):
        """Record a metric value"""
        self.metrics[metric_name]['count'] += 1
        self.metrics[metric_name]['sum'] += value
        self.metrics[metric_name]['min'] = min(self.metrics[metric_name]['min'], value)
        self.metrics[metric_name]['max'] = max(self.metrics[metric_name]['max'], value)

        # Update time-based windows
        minute_bucket = timestamp.replace(second=0, microsecond=0)
        self.time_windows[metric_name][minute_bucket] += value

    def get_current_rate(self, metric_name: str, window_minutes: int = 5) -> float:
        """Get current rate (events per second)"""
        now = datetime.now()
        cutoff = now - timedelta(minutes=window_minutes)

        relevant_events = sum(
            count for timestamp, count in self.time_windows[metric_name].items()
            if timestamp >= cutoff
        )

        return relevant_events / (window_minutes * 60)

    def get_percentile(self, metric_name: str, percentile: float) -> float:
        """Calculate percentile (simplified)"""
        if not self.metrics[metric_name]['count']:
            return 0

        avg = self.metrics[metric_name]['sum'] / self.metrics[metric_name]['count']
        # Simplified - in production, use proper percentile calculation
        return avg * (percentile / 100)
```

### Click Stream Processing

```python
from datetime import datetime, timedelta
from collections import defaultdict

class ClickStreamProcessor:
    def __init__(self):
        self.user_sessions = defaultdict(list)
        self.page_views = defaultdict(int)
        self.user_paths = defaultdict(list)

    def process_click(self, click_event: dict):
        user_id = click_event['user_id']
        page = click_event['page']
        timestamp = click_event['timestamp']

        # Update page view counts
        self.page_views[page] += 1

        # Track user path
        self.user_paths[user_id].append({
            'page': page,
            'timestamp': timestamp
        })

        # Session management
        if self.user_sessions[user_id]:
            last_click = self.user_sessions[user_id][-1]
            if timestamp - last_click['timestamp'] > timedelta(minutes=30):
                # New session
                self.user_sessions[user_id] = []

        self.user_sessions[user_id].append(click_event)

        # Calculate session metrics
        session = self.user_sessions[user_id]
        if len(session) > 1:
            duration = (session[-1]['timestamp'] - session[0]['timestamp']).total_seconds()
            return {
                'user_id': user_id,
                'session_duration': duration,
                'pages_in_session': len(session),
                'current_page': page
            }

        return None
```

## Best Practices

### 1. Handle Late Data

```python
class LateDataHandler:
    def __init__(self, max_late_duration: timedelta = timedelta(hours=1)):
        self.max_late_duration = max_late_duration
        self.allowed_lateness = timedelta(minutes=5)

    def handle_late_event(self, event, current_watermark):
        event_time = event['event_time']

        # Check if event is too late
        if current_watermark - event_time > self.max_late_duration:
            return {'status': 'DROPPED', 'reason': 'TOO_LATE'}

        # Check if within allowed lateness
        if current_watermark - event_time <= self.allowed_lateness:
            return {'status': 'ACCEPTED', 'window': 'CURRENT'}

        # Late but within tolerance
        return {'status': 'ACCEPTED', 'window': 'HISTORICAL'}
```

### 2. Backpressure Handling

```python
import asyncio
from collections import deque

class BackpressureHandler:
    def __init__(self, max_buffer_size: int = 10000, high_watermark: float = 0.8):
        self.buffer = deque(maxlen=max_buffer_size)
        self.max_buffer_size = max_buffer_size
        self.high_watermark = high_watermark
        self.dropped_count = 0

    async def handle_event(self, event):
        buffer_usage = len(self.buffer) / self.max_buffer_size

        if buffer_usage >= self.high_watermark:
            # Apply backpressure
            self.dropped_count += 1
            if self.dropped_count % 1000 == 0:
                print(f"Backpressure: dropped {self.dropped_count} events")
            return {'status': 'DROPPED', 'reason': 'BACKPRESSURE'}

        self.buffer.append(event)
        return {'status': 'BUFFERED'}

    async def process_buffer(self):
        while True:
            if self.buffer:
                event = self.buffer.popleft()
                await self.process(event)
            await asyncio.sleep(0.001)  # Small delay to prevent CPU spinning
```

### 3. Monitoring and Alerting

```python
from datetime import datetime, timedelta

class StreamMonitor:
    def __init__(self):
        self.metrics = {
            'events_processed': 0,
            'events_failed': 0,
            'processing_latency': [],
            'throughput': []
        }
        self.alert_thresholds = {
            'latency_ms': 1000,
            'error_rate': 0.01,
            'throughput_min': 1000
        }

    def record_metric(self, metric_name: str, value):
        self.metrics[metric_name].append({
            'value': value,
            'timestamp': datetime.now()
        })

    def check_alerts(self) -> list:
        alerts = []
        now = datetime.now()

        # Check latency
        recent_latencies = [
            m['value'] for m in self.metrics['processing_latency']
            if now - m['timestamp'] < timedelta(minutes=5)
        ]
        if recent_latencies:
            avg_latency = sum(recent_latencies) / len(recent_latencies)
            if avg_latency > self.alert_thresholds['latency_ms']:
                alerts.append(f"High latency: {avg_latency:.2f}ms")

        # Check error rate
        if self.metrics['events_processed'] > 0:
            error_rate = self.metrics['events_failed'] / self.metrics['events_processed']
            if error_rate > self.alert_thresholds['error_rate']:
                alerts.append(f"High error rate: {error_rate:.2%}")

        return alerts
```

### 4. Exactly-Once Processing

```python
class ExactlyOnceStream:
    def __init__(self, transaction_manager):
        self.transaction_manager = transaction_manager

    def process_events(self, events):
        for event in events:
            # Start transaction
            transaction_id = self.transaction_manager.begin()

            try:
                # Process event
                result = self.process(event)

                # Write output with transaction
                self.transaction_manager.write(transaction_id, result)

                # Commit
                self.transaction_manager.commit(transaction_id)

            except Exception as e:
                # Rollback on failure
                self.transaction_manager.rollback(transaction_id)
                raise
```

## Further Reading

- [Batch Processing](../batch/) - Batch processing patterns
- [Lambda Architecture](../lambda/) - Combining batch and streaming
- [Kappa Architecture](../kappa/) - Stream-only processing
- [Kafka Streams](../../streaming/kafka-streams/) - Kafka-native streaming
- [Apache Flink](../../streaming/flink/) - Stateful stream processing
