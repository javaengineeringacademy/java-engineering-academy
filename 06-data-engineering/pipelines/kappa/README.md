# Kappa Architecture

Kappa Architecture is a stream processing pattern that uses only stream processing for all workloads, eliminating the need for separate batch processing systems. It was popularized by Jay Kreps, co-creator of Apache Kafka.

## Table of Contents

- [Overview](#overview)
- [Core Principles](#core-principles)
- [Architecture](#architecture)
- [Stream Processing](#stream-processing)
- [Reprocessing](#reprocessing)
- [Examples](#examples)
- [Best Practices](#best-practices)
- [Kappa vs Lambda](#kappa-vs-lambda)

## Overview

Kappa Architecture simplifies the data processing stack by treating all data as streams. Instead of maintaining separate batch and speed layers, it uses a single stream processing layer that can handle both real-time and historical data.

```
┌─────────────────────────────────────────────────────────────────┐
│                    KAPPA ARCHITECTURE                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Sources ──> Message Queue ──> Stream Processing ──> Serving  │
│                                                                 │
│   ┌─────────┐      ┌─────────┐      ┌─────────┐    ┌────────┐ │
│   │         │      │         │      │         │    │ Views  │ │
│   │ Sources │─────>│  Kafka  │─────>│ Flink/  │───>│        │ │
│   │         │      │         │      │Streams  │    │        │ │
│   └─────────┘      └─────────┘      └─────────┘    └────────┘ │
│                                                                 │
│   Reprocessing: Replay from Kafka with new consumer group      │
└─────────────────────────────────────────────────────────────────┘
```

## Core Principles

### 1. Single Stream Processing

```python
class KappaProcessor:
    def __init__(self):
        self.stream_processor = StreamProcessor()
        self.output_views = {}

    def process_event(self, event):
        """Process event through single stream processor"""
        # Apply transformation
        transformed = self.transform(event)

        # Update output views
        self.update_views(transformed)

        return transformed

    def transform(self, event):
        """Single transformation logic for all use cases"""
        return {
            'key': event['key'],
            'value': self.compute_value(event),
            'timestamp': event['timestamp']
        }
```

### 2. Event Sourcing

```python
class EventStore:
    def __init__(self):
        self.events = []
        self.version = 0

    def append(self, event: dict):
        """Append event to store"""
        self.events.append({
            **event,
            'version': self.version
        })
        self.version += 1

    def get_events(self, after_version: int = 0) -> list:
        """Get events after a specific version"""
        return [e for e in self.events if e['version'] > after_version]

    def rebuild_state(self, up_to_version: int = None) -> dict:
        """Rebuild state from events"""
        state = {}
        for event in self.get_events():
            if up_to_version and event['version'] > up_to_version:
                break
            state = self.apply_event(state, event)
        return state

    def apply_event(self, state: dict, event: dict) -> dict:
        """Apply event to state"""
        if event['type'] == 'ORDER_CREATED':
            state[event['order_id']] = {
                'amount': event['amount'],
                'status': 'created'
            }
        elif event['type'] == 'ORDER_COMPLETED':
            if event['order_id'] in state:
                state[event['order_id']]['status'] = 'completed'
        return state
```

## Architecture

### Complete Kappa Architecture

```python
from abc import ABC, abstractmethod
from datetime import datetime
from typing import Any

class KappaArchitecture:
    def __init__(self):
        self.message_queue = MessageQueue()
        self.stream_processors = []
        self.serving_layer = ServingLayer()

    def ingest(self, event: Any):
        """Ingest event into message queue"""
        self.message_queue.append(event)

    def add_processor(self, processor: 'StreamProcessor'):
        """Add a stream processor"""
        self.stream_processors.append(processor)

    def process_stream(self, processor_name: str):
        """Process stream with specific processor"""
        processor = next(
            p for p in self.stream_processors
            if p.name == processor_name
        )

        for event in self.message_queue.consume(processor_name):
            result = processor.process(event)
            self.serving_layer.update(result)

    def reprocess(self, processor_name: str, from_offset: int = 0):
        """Reprocess stream from specific offset"""
        # Create new consumer group
        consumer_group = f"reprocess_{processor_name}_{datetime.now().timestamp()}"

        # Process from beginning
        for event in self.message_queue.consume(consumer_group, from_offset=from_offset):
            processor = next(
                p for p in self.stream_processors
                if p.name == processor_name
            )
            result = processor.process(event)
            self.serving_layer.update(result, overwrite=True)

class MessageQueue:
    def __init__(self):
        self.topics = {}

    def append(self, topic: str, event: dict):
        if topic not in self.topics:
            self.topics[topic] = []
        self.topics[topic].append(event)

    def consume(self, topic: str, from_offset: int = 0):
        if topic not in self.topics:
            return []
        return self.topics[topic][from_offset:]
```

### Stream Processing Pipeline

```python
from collections import defaultdict
from datetime import timedelta

class StreamProcessor:
    def __init__(self, name: str):
        self.name = name
        self.state = {}

    def process(self, event: dict) -> dict:
        """Process single event"""
        raise NotImplementedError

class AggregationProcessor(StreamProcessor):
    def __init__(self, name: str, window_size: timedelta):
        super().__init__(name)
        self.window_size = window_size
        self.windows = defaultdict(lambda: defaultdict(float))

    def process(self, event: dict) -> dict:
        """Aggregate events in windows"""
        window_key = self.get_window_key(event['timestamp'])
        aggregation_key = event.get('category', 'default')

        # Update aggregation
        self.windows[window_key][aggregation_key] += event.get('value', 0)

        return {
            'window': window_key,
            'category': aggregation_key,
            'total': self.windows[window_key][aggregation_key]
        }

    def get_window_key(self, timestamp: datetime) -> datetime:
        """Get window key for timestamp"""
        return timestamp.replace(
            minute=(timestamp.minute // (self.window_size.seconds // 60)) * (self.window_size.seconds // 60),
            second=0,
            microsecond=0
        )
```

## Stream Processing

### Stateless Processing

```python
class StatelessProcessor(StreamProcessor):
    def __init__(self, name: str, transform_func):
        super().__init__(name)
        self.transform_func = transform_func

    def process(self, event: dict) -> dict:
        """Process event without state"""
        return self.transform_func(event)

# Example: Filter and transform
def filter_transform(event):
    if event.get('amount', 0) > 100:
        return {
            'key': event['id'],
            'value': event['amount'] * 1.1,  # Apply tax
            'timestamp': event['timestamp']
        }
    return None

processor = StatelessProcessor('filter_high_value', filter_transform)
```

### Stateful Processing

```python
class StatefulProcessor(StreamProcessor):
    def __init__(self, name: str):
        super().__init__(name)
        self.state = {}

    def process(self, event: dict) -> dict:
        """Process event with state"""
        key = event['key']

        # Get current state
        current_state = self.state.get(key, self.initial_state())

        # Update state
        new_state = self.update_state(current_state, event)
        self.state[key] = new_state

        # Emit result
        return self.emit_result(key, new_state, event)

    def initial_state(self):
        return {'count': 0, 'sum': 0}

    def update_state(self, state: dict, event: dict) -> dict:
        return {
            'count': state['count'] + 1,
            'sum': state['sum'] + event.get('value', 0)
        }

    def emit_result(self, key: str, state: dict, event: dict) -> dict:
        return {
            'key': key,
            'count': state['count'],
            'average': state['sum'] / state['count'] if state['count'] > 0 else 0
        }
```

### Windowed Processing

```python
from collections import defaultdict
from datetime import datetime, timedelta

class WindowedProcessor(StreamProcessor):
    def __init__(self, name: str, window_size: timedelta, slide_interval: timedelta = None):
        super().__init__(name)
        self.window_size = window_size
        self.slide_interval = slide_interval or window_size
        self.windows = defaultdict(list)

    def process(self, event: dict) -> dict:
        """Process event in windows"""
        event_time = event['timestamp']

        # Assign to windows
        windows = self.assign_to_windows(event_time)

        # Update each window
        results = []
        for window_start in windows:
            window_key = (event.get('key', 'default'), window_start)
            self.windows[window_key].append(event)

            # Compute window result
            result = self.compute_window_result(window_key)
            results.append(result)

        return results

    def assign_to_windows(self, event_time: datetime) -> list:
        """Assign event to all relevant windows"""
        windows = []
        window_start = event_time - (event_time.timestamp() % self.window_size.total_seconds())

        while window_start <= event_time:
            windows.append(window_start)
            window_start += self.slide_interval

        return windows

    def compute_window_result(self, window_key: tuple) -> dict:
        """Compute result for a window"""
        key, window_start = window_key
        events = self.windows[window_key]

        return {
            'key': key,
            'window_start': window_start,
            'count': len(events),
            'sum': sum(e.get('value', 0) for e in events),
            'avg': sum(e.get('value', 0) for e in events) / len(events) if events else 0
        }
```

## Reprocessing

### Reprocessing Strategy

```python
class ReprocessingManager:
    def __init__(self, message_queue):
        self.message_queue = message_queue

    def reprocess_from_beginning(self, processor_name: str):
        """Reprocess all data from beginning"""
        consumer_group = f"reprocess_{processor_name}_full"

        # Create new processor instance
        processor = self.create_processor(processor_name)

        # Process all events
        count = 0
        for event in self.message_queue.consume(consumer_group, from_beginning=True):
            processor.process(event)
            count += 1

            if count % 10000 == 0:
                print(f"Reprocessed {count} events...")

        print(f"Reprocessing complete: {count} events")

    def reprocess_from_time(self, processor_name: str, start_time: datetime):
        """Reprocess from specific time"""
        consumer_group = f"reprocess_{processor_name}_{start_time.timestamp()}"

        processor = self.create_processor(processor_name)

        count = 0
        for event in self.message_queue.consume(consumer_group, start_time=start_time):
            processor.process(event)
            count += 1

        print(f"Reprocessing complete: {count} events")

    def reprocess_with_new_logic(self, old_processor_name: str, new_logic):
        """Reprocess with updated processing logic"""
        # Save current state
        current_state = self.get_current_state(old_processor_name)

        # Create processor with new logic
        processor = StreamProcessor(old_processor_name)
        processor.process = new_logic

        # Reprocess all data
        self.reprocess_from_beginning(old_processor_name)

        # Validate results
        new_state = self.get_current_state(old_processor_name)
        self.validate_reprocessing(current_state, new_state)
```

### Versioned Reprocessing

```python
class VersionedProcessor:
    def __init__(self):
        self.versions = {}
        self.current_version = 0

    def add_version(self, version: int, process_func):
        """Add processing logic version"""
        self.versions[version] = process_func

    def process(self, event: dict, version: int = None) -> dict:
        """Process event with specific version"""
        if version is None:
            version = self.current_version

        process_func = self.versions[version]
        return process_func(event)

    def upgrade_version(self, new_version: int):
        """Upgrade to new processing version"""
        self.current_version = new_version

    def reprocess_with_version(self, events: list, version: int):
        """Reprocess events with specific version"""
        results = []
        for event in events:
            result = self.process(event, version)
            results.append(result)
        return results
```

## Examples

### Real-time Order Processing

```python
from datetime import datetime
from collections import defaultdict

class KappaOrderProcessor:
    def __init__(self):
        self.orders = {}
        self.analytics = defaultdict(float)

    def process_event(self, event: dict):
        """Process order events"""
        event_type = event['type']
        order_id = event['order_id']

        if event_type == 'ORDER_CREATED':
            self.orders[order_id] = {
                'id': order_id,
                'customer_id': event['customer_id'],
                'amount': event['amount'],
                'status': 'created',
                'created_at': event['timestamp']
            }
            self.analytics['total_orders'] += 1
            self.analytics['total_revenue'] += event['amount']

        elif event_type == 'ORDER_COMPLETED':
            if order_id in self.orders:
                self.orders[order_id]['status'] = 'completed'
                self.orders[order_id]['completed_at'] = event['timestamp']

        elif event_type == 'ORDER_CANCELLED':
            if order_id in self.orders:
                self.orders[order_id]['status'] = 'cancelled'
                self.analytics['total_orders'] -= 1
                self.analytics['total_revenue'] -= self.orders[order_id]['amount']

        return {
            'order_id': order_id,
            'analytics': dict(self.analytics)
        }

    def get_order(self, order_id: str):
        return self.orders.get(order_id)

    def get_analytics(self):
        return dict(self.analytics)
```

### User Activity Tracking

```python
from datetime import datetime, timedelta
from collections import defaultdict

class KappaActivityTracker:
    def __init__(self):
        self.user_sessions = defaultdict(list)
        self.user_metrics = defaultdict(lambda: {
            'page_views': 0,
            'clicks': 0,
            'time_on_site': 0
        })

    def process_activity(self, event: dict):
        """Process user activity events"""
        user_id = event['user_id']
        activity_type = event['activity_type']
        timestamp = event['timestamp']

        # Update session
        self.user_sessions[user_id].append({
            'type': activity_type,
            'timestamp': timestamp,
            'page': event.get('page'),
            'duration': event.get('duration', 0)
        })

        # Update metrics
        if activity_type == 'PAGE_VIEW':
            self.user_metrics[user_id]['page_views'] += 1
        elif activity_type == 'CLICK':
            self.user_metrics[user_id]['clicks'] += 1

        # Calculate session duration
        session = self.user_sessions[user_id]
        if len(session) > 1:
            duration = (session[-1]['timestamp'] - session[0]['timestamp']).total_seconds()
            self.user_metrics[user_id]['time_on_site'] = duration

        return {
            'user_id': user_id,
            'metrics': self.user_metrics[user_id],
            'session_length': len(self.user_sessions[user_id])
        }

    def get_user_metrics(self, user_id: str):
        return self.user_metrics.get(user_id, {})

    def get_active_users(self, minutes: int = 30) -> list:
        """Get users active in last N minutes"""
        cutoff = datetime.now() - timedelta(minutes=minutes)
        active_users = []

        for user_id, session in self.user_sessions.items():
            if session and session[-1]['timestamp'] > cutoff:
                active_users.append(user_id)

        return active_users
```

### Real-time Recommendation Engine

```python
from collections import defaultdict
from datetime import datetime, timedelta

class KappaRecommender:
    def __init__(self):
        self.user_interactions = defaultdict(list)
        self.item_popularity = defaultdict(int)
        self.user_preferences = defaultdict(lambda: defaultdict(int))

    def process_event(self, event: dict):
        """Process user interaction events"""
        user_id = event['user_id']
        item_id = event['item_id']
        interaction_type = event['interaction_type']
        timestamp = event['timestamp']

        # Store interaction
        self.user_interactions[user_id].append({
            'item_id': item_id,
            'type': interaction_type,
            'timestamp': timestamp
        })

        # Update item popularity
        self.item_popularity[item_id] += 1

        # Update user preferences
        weight = self.get_interaction_weight(interaction_type)
        self.user_preferences[user_id][item_id] += weight

        # Generate recommendations
        recommendations = self.get_recommendations(user_id)

        return {
            'user_id': user_id,
            'recommendations': recommendations[:10]  # Top 10
        }

    def get_interaction_weight(self, interaction_type: str) -> int:
        weights = {
            'VIEW': 1,
            'LIKE': 3,
            'ADD_TO_CART': 5,
            'PURCHASE': 10
        }
        return weights.get(interaction_type, 1)

    def get_recommendations(self, user_id: str) -> list:
        """Get personalized recommendations"""
        user_prefs = self.user_preferences[user_id]

        # Get similar items based on user history
        recommendations = defaultdict(float)

        for item_id, weight in user_prefs.items():
            # Find similar items
            similar_items = self.get_similar_items(item_id)
            for similar_item, similarity in similar_items.items():
                if similar_item not in user_prefs:  # Don't recommend already interacted items
                    recommendations[similar_item] += weight * similarity

        # Sort by score
        sorted_recs = sorted(
            recommendations.items(),
            key=lambda x: x[1],
            reverse=True
        )

        return [item_id for item_id, score in sorted_recs]

    def get_similar_items(self, item_id: str) -> dict:
        """Get similar items (simplified)"""
        # In production, use collaborative filtering or content-based similarity
        return {}
```

## Best Practices

### 1. Immutable Event Log

```python
class ImmutableEventLog:
    def __init__(self):
        self.events = []
        self.version = 0

    def append(self, event: dict):
        """Append event - never modify"""
        self.events.append({
            **event,
            'version': self.version,
            'appended_at': datetime.now()
        })
        self.version += 1

    def get_event(self, version: int) -> dict:
        """Get event by version"""
        if 0 <= version < len(self.events):
            return self.events[version]
        return None

    def get_events_since(self, version: int) -> list:
        """Get all events since version"""
        return self.events[version:]
```

### 2. State Management

```python
class StateManager:
    def __init__(self, checkpoint_interval: int = 1000):
        self.state = {}
        self.checkpoint_interval = checkpoint_interval
        self.events_processed = 0

    def update_state(self, key: str, value: any):
        """Update state"""
        self.state[key] = value
        self.events_processed += 1

        # Checkpoint periodically
        if self.events_processed % self.checkpoint_interval == 0:
            self.checkpoint()

    def checkpoint(self):
        """Save state to durable storage"""
        # Implementation depends on state backend
        pass

    def restore(self, checkpoint_id: str):
        """Restore state from checkpoint"""
        # Implementation depends on state backend
        pass
```

### 3. Schema Evolution

```python
class SchemaEvolution:
    def __init__(self):
        self.schemas = {}
        self.current_version = 1

    def register_schema(self, version: int, schema: dict):
        """Register schema version"""
        self.schemas[version] = schema

    def migrate_event(self, event: dict, from_version: int, to_version: int) -> dict:
        """Migrate event between schema versions"""
        # Apply migrations sequentially
        for version in range(from_version, to_version):
            migration = self.get_migration(version, version + 1)
            event = migration(event)

        return event

    def get_migration(self, from_version: int, to_version: int):
        """Get migration function between versions"""
        # Define migrations between versions
        migrations = {
            (1, 2): lambda e: {**e, 'new_field': 'default_value'},
            (2, 3): lambda e: {**e, 'renamed_field': e.get('old_field')}
        }
        return migrations.get((from_version, to_version), lambda e: e)
```

### 4. Error Handling

```python
class ErrorHandler:
    def __init__(self):
        self.failed_events = []
        self.retry_counts = defaultdict(int)
        self.max_retries = 3

    def handle_error(self, event: dict, error: Exception):
        """Handle processing error"""
        event_id = event.get('id', str(hash(str(event))))

        self.retry_counts[event_id] += 1

        if self.retry_counts[event_id] <= self.max_retries:
            # Retry
            return {'action': 'RETRY', 'attempt': self.retry_counts[event_id]}
        else:
            # Send to dead letter queue
            self.failed_events.append({
                'event': event,
                'error': str(error),
                'failed_at': datetime.now()
            })
            return {'action': 'DLQ'}

    def get_failed_events(self):
        return self.failed_events.copy()

    def replay_failed_events(self, processor):
        """Replay failed events"""
        for failed in self.failed_events:
            try:
                processor.process(failed['event'])
            except Exception as e:
                print(f"Failed to replay event: {e}")
```

## Kappa vs Lambda

### Comparison

| Aspect | Kappa | Lambda |
|--------|-------|--------|
| Complexity | Simpler | More complex |
| Code duplication | None | Yes |
| Maintenance | Easier | Harder |
| Reprocessing | Replay from queue | Recompute batch |
| Latency | Consistent | Batch + real-time |
| State management | Single state | Two states |
| Resource usage | Higher | Lower (batch optimized) |

### Decision Matrix

```
Use Kappa When:
├─ Real-time is primary requirement
├─ Simplified operations desired
├─ Strong stream processing ecosystem
├─ Can replay from message queue
└─ Consistent latency needed

Use Lambda When:
├─ Historical accuracy critical
├─ Batch processing more efficient
├─ Complex transformations needed
├─ Limited stream processing resources
└─ Need both batch and real-time views
```

## Further Reading

- [Lambda Architecture](../lambda/) - Batch + speed layers alternative
- [Batch Processing](../batch/) - Batch processing patterns
- [Streaming Processing](../streaming/) - Stream processing fundamentals
- [Kafka Streams](../../streaming/kafka-streams/) - Kafka-native stream processing
- [Apache Flink](../../streaming/flink/) - Unified stream processor
