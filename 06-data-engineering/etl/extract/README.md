# Data Extraction

Data extraction is the process of retrieving data from various sources and making it available for further processing. This covers extraction patterns, connectors, and Change Data Capture (CDC).

## Table of Contents

- [Overview](#overview)
- [Extraction Patterns](#extraction-patterns)
- [Connectors](#connectors)
- [Change Data Capture (CDC)](#change-data-capture-cdc)
- [Examples](#examples)
- [Best Practices](#best-practices)

## Overview

Data extraction is the first step in the ETL/ELT pipeline. It involves connecting to source systems, reading data, and preparing it for transformation and loading.

### Extraction Types

```
┌─────────────────────────────────────────────────────────────────┐
│                    EXTRACTION TYPES                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Full Extraction     │ Extract all data every time             │
│  ─────────────────────────────────────────────────────────────  │
│  + Simple to implement                                          │
│  + No state tracking needed                                     │
│  - Slow for large datasets                                      │
│  - Resource intensive                                           │
│                                                                 │
│  Incremental Extraction │ Extract only changed data            │
│  ─────────────────────────────────────────────────────────────  │
│  + Faster                                                       │
│  + Lower resource usage                                         │
│  - Complex implementation                                       │
│  - Requires change tracking                                     │
│                                                                 │
│  CDC (Change Data Capture) │ Capture real-time changes         │
│  ─────────────────────────────────────────────────────────────  │
│  + Near real-time                                                │
│  + Low impact on source                                          │
│  - Complex setup                                                │
│  - Requires database support                                     │
└─────────────────────────────────────────────────────────────────┘
```

## Extraction Patterns

### Full Extraction

```python
from datetime import datetime
from typing import List, Dict

class FullExtractor:
    def __init__(self, source_connection):
        self.source = source_connection

    def extract(self, query: str) -> List[Dict]:
        """Extract all data matching query"""
        cursor = self.source.execute(query)
        columns = [desc[0] for desc in cursor.description]
        return [dict(zip(columns, row)) for row in cursor.fetchall()]

    def extract_table(self, table_name: str) -> List[Dict]:
        """Extract entire table"""
        return self.extract(f"SELECT * FROM {table_name}")

    def extract_with_timestamp(self, table_name: str, timestamp_column: str = 'updated_at'):
        """Extract with metadata"""
        data = self.extract_table(table_name)
        return {
            'data': data,
            'extracted_at': datetime.now(),
            'record_count': len(data),
            'source': table_name
        }
```

### Incremental Extraction

```python
from datetime import datetime, timedelta
from typing import Optional, List, Dict

class IncrementalExtractor:
    def __init__(self, source_connection, watermark_column: str = 'updated_at'):
        self.source = source_connection
        self.watermark_column = watermark_column

    def get_last_watermark(self, target_table: str) -> Optional[datetime]:
        """Get last processed watermark"""
        query = f"""
            SELECT MAX({self.watermark_column}) as last_watermark
            FROM {target_table}
        """
        result = self.source.execute(query).fetchone()
        return result[0] if result else None

    def extract_incremental(self, source_table: str, last_watermark: Optional[datetime]) -> List[Dict]:
        """Extract only new/changed records"""
        if last_watermark:
            query = f"""
                SELECT * FROM {source_table}
                WHERE {self.watermark_column} > %s
                ORDER BY {self.watermark_column}
            """
            cursor = self.source.execute(query, [last_watermark])
        else:
            query = f"SELECT * FROM {source_table} ORDER BY {self.watermark_column}"
            cursor = self.source.execute(query)

        columns = [desc[0] for desc in cursor.description]
        return [dict(zip(columns, row)) for row in cursor.fetchall()]

    def extract(self, source_table: str, target_table: str) -> Dict:
        """Full incremental extraction"""
        last_watermark = self.get_last_watermark(target_table)
        data = self.extract_incremental(source_table, last_watermark)

        new_watermark = None
        if data:
            new_watermark = max(record[self.watermark_column] for record in data)

        return {
            'data': data,
            'last_watermark': last_watermark,
            'new_watermark': new_watermark,
            'record_count': len(data)
        }
```

### Cursor-based Extraction

```python
class CursorExtractor:
    def __init__(self, source_connection, batch_size: int = 1000):
        self.source = source_connection
        self.batch_size = batch_size

    def extract_in_batches(self, query: str):
        """Extract data using cursor for memory efficiency"""
        cursor = self.source.execute(query)
        columns = [desc[0] for desc in cursor.description]

        while True:
            rows = cursor.fetchmany(self.batch_size)
            if not rows:
                break

            yield [dict(zip(columns, row)) for row in rows]

    def extract_large_table(self, table_name: str, order_column: str = 'id'):
        """Extract large table using keyset pagination"""
        last_key = 0
        query = f"""
            SELECT * FROM {table_name}
            WHERE {order_column} > %s
            ORDER BY {order_column}
            LIMIT {self.batch_size}
        """

        while True:
            cursor = self.source.execute(query, [last_key])
            rows = cursor.fetchall()

            if not rows:
                break

            columns = [desc[0] for desc in cursor.description]
            batch = [dict(zip(columns, row)) for row in rows]

            yield batch

            last_key = rows[-1][0]  # Assuming first column is the key
```

## Connectors

### Database Connectors

```python
from abc import ABC, abstractmethod
from typing import List, Dict, Optional
import psycopg2
import sqlalchemy

class DatabaseConnector(ABC):
    @abstractmethod
    def connect(self):
        pass

    @abstractmethod
    def execute(self, query: str, params=None):
        pass

    @abstractmethod
    def close(self):
        pass

class PostgreSQLConnector(DatabaseConnector):
    def __init__(self, host: str, port: int, database: str, user: str, password: str):
        self.connection_params = {
            'host': host,
            'port': port,
            'database': database,
            'user': user,
            'password': password
        }
        self.connection = None

    def connect(self):
        self.connection = psycopg2.connect(**self.connection_params)
        return self

    def execute(self, query: str, params=None):
        cursor = self.connection.cursor()
        cursor.execute(query, params)
        return cursor

    def close(self):
        if self.connection:
            self.connection.close()

class SQLAlchemyConnector(DatabaseConnector):
    def __init__(self, connection_string: str):
        self.engine = sqlalchemy.create_engine(connection_string)
        self.connection = None

    def connect(self):
        self.connection = self.engine.connect()
        return self

    def execute(self, query: str, params=None):
        return self.connection.execute(query, params)

    def close(self):
        if self.connection:
            self.connection.close()
```

### File Connectors

```python
import csv
import json
from pathlib import Path
from typing import List, Dict

class CSVConnector:
    def __init__(self, file_path: str, delimiter: str = ','):
        self.file_path = file_path
        self.delimiter = delimiter

    def read(self) -> List[Dict]:
        """Read CSV file"""
        with open(self.file_path, 'r') as f:
            reader = csv.DictReader(f, delimiter=self.delimiter)
            return list(reader)

    def read_in_batches(self, batch_size: int = 1000):
        """Read CSV in batches"""
        with open(self.file_path, 'r') as f:
            reader = csv.DictReader(f, delimiter=self.delimiter)
            batch = []
            for row in reader:
                batch.append(row)
                if len(batch) >= batch_size:
                    yield batch
                    batch = []
            if batch:
                yield batch

class JSONConnector:
    def __init__(self, file_path: str):
        self.file_path = file_path

    def read(self) -> List[Dict]:
        """Read JSON file"""
        with open(self.file_path, 'r') as f:
            return json.load(f)

    def read_jsonl(self) -> List[Dict]:
        """Read JSON Lines file"""
        data = []
        with open(self.file_path, 'r') as f:
            for line in f:
                if line.strip():
                    data.append(json.loads(line))
        return data
```

### API Connectors

```python
import requests
from typing import Dict, List, Optional
from datetime import datetime

class RESTAPIConnector:
    def __init__(self, base_url: str, auth_header: Optional[str] = None):
        self.base_url = base_url
        self.session = requests.Session()
        if auth_header:
            self.session.headers['Authorization'] = auth_header

    def get(self, endpoint: str, params: Optional[Dict] = None) -> Dict:
        """Make GET request"""
        response = self.session.get(f"{self.base_url}/{endpoint}", params=params)
        response.raise_for_status()
        return response.json()

    def get_paginated(self, endpoint: str, page_size: int = 100) -> List[Dict]:
        """Handle paginated API"""
        all_data = []
        page = 1

        while True:
            data = self.get(endpoint, {'page': page, 'page_size': page_size})
            if not data:
                break
            all_data.extend(data)
            if len(data) < page_size:
                break
            page += 1

        return all_data

    def get_with_retry(self, endpoint: str, max_retries: int = 3) -> Dict:
        """GET with retry logic"""
        for attempt in range(max_retries):
            try:
                return self.get(endpoint)
            except requests.RequestException as e:
                if attempt == max_retries - 1:
                    raise
                time.sleep(2 ** attempt)
```

## Change Data Capture (CDC)

### CDC Patterns

```python
from datetime import datetime
from typing import List, Dict
from enum import Enum

class CDCOperation(Enum):
    INSERT = "INSERT"
    UPDATE = "UPDATE"
    DELETE = "DELETE"

class CDCEvent:
    def __init__(self, operation: CDCOperation, table: str, data: Dict, timestamp: datetime):
        self.operation = operation
        self.table = table
        self.data = data
        self.timestamp = timestamp

class CDCExtractor:
    def __init__(self, source_connection):
        self.source = source_connection

    def extract_with_log_sequence(self, table_name: str, last_sequence: int) -> List[CDCEvent]:
        """Extract changes using log sequence number"""
        query = f"""
            SELECT * FROM {table_name}_cdc_log
            WHERE log_sequence > %s
            ORDER BY log_sequence
        """
        cursor = self.source.execute(query, [last_sequence])
        columns = [desc[0] for desc in cursor.description]

        events = []
        for row in cursor.fetchall():
            record = dict(zip(columns, row))
            events.append(CDCEvent(
                operation=CDCOperation(record['operation']),
                table=table_name,
                data=record['data'],
                timestamp=record['timestamp']
            ))

        return events

    def extract_with_timestamp(self, table_name: str, last_timestamp: datetime) -> List[CDCEvent]:
        """Extract changes using timestamp"""
        query = f"""
            SELECT * FROM {table_name}_cdc_log
            WHERE timestamp > %s
            ORDER BY timestamp
        """
        cursor = self.source.execute(query, [last_timestamp])
        columns = [desc[0] for desc in cursor.description]

        events = []
        for row in cursor.fetchall():
            record = dict(zip(columns, row))
            events.append(CDCEvent(
                operation=CDCOperation(record['operation']),
                table=table_name,
                data=record['data'],
                timestamp=record['timestamp']
            ))

        return events
```

### Debezium-style CDC

```python
class DebeziumCDC:
    def __init__(self, kafka_config: Dict):
        self.kafka_config = kafka_config

    def setup_connector(self, database_config: Dict):
        """Setup Debezium connector"""
        connector_config = {
            "name": "inventory-connector",
            "config": {
                "connector.class": "io.debezium.connector.mysql.MySqlConnector",
                "database.hostname": database_config['host'],
                "database.port": database_config['port'],
                "database.user": database_config['user'],
                "database.password": database_config['password'],
                "database.server.id": "1",
                "database.include.list": database_config['database'],
                "database.history.kafka.bootstrap.servers": self.kafka_config['bootstrap_servers'],
                "database.history.kafka.topic": "schema-changes.inventory"
            }
        }
        return connector_config

    def process_cdc_event(self, event: Dict) -> Dict:
        """Process Debezium CDC event"""
        # Debezium wraps the actual data in before/after
        return {
            'operation': event['op'],
            'timestamp': event['ts_ms'],
            'database': event['source']['db'],
            'table': event['source']['table'],
            'before': event.get('before'),
            'after': event.get('after'),
            'primary_key': event['key']
        }
```

## Examples

### Multi-source Extraction

```python
from typing import Dict, List
from datetime import datetime

class MultiSourceExtractor:
    def __init__(self):
        self.extractors = {}

    def add_extractor(self, name: str, extractor):
        self.extractors[name] = extractor

    def extract_all(self) -> Dict[str, List]:
        """Extract from all sources"""
        results = {}
        for name, extractor in self.extractors.items():
            print(f"Extracting from {name}...")
            results[name] = extractor.extract()
        return results

    def extract_parallel(self, max_workers: int = 4):
        """Extract from all sources in parallel"""
        from concurrent.futures import ThreadPoolExecutor, as_completed

        results = {}
        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            future_to_source = {
                executor.submit(ext.extract): name
                for name, ext in self.extractors.items()
            }

            for future in as_completed(future_to_source):
                name = future_to_source[future]
                try:
                    results[name] = future.result()
                except Exception as e:
                    print(f"Error extracting from {name}: {e}")
                    results[name] = None

        return results
```

### Extract with Validation

```python
from typing import Dict, List

class ValidatingExtractor:
    def __init__(self, extractor, validation_rules: List[Dict]):
        self.extractor = extractor
        self.validation_rules = validation_rules

    def extract_and_validate(self) -> Dict:
        """Extract and validate data"""
        data = self.extractor.extract()

        validation_results = []
        for rule in self.validation_rules:
            result = self.validate_rule(data, rule)
            validation_results.append(result)

        return {
            'data': data,
            'validation': validation_results,
            'is_valid': all(r['passed'] for r in validation_results)
        }

    def validate_rule(self, data: List[Dict], rule: Dict) -> Dict:
        """Validate a single rule"""
        rule_type = rule['type']

        if rule_type == 'not_null':
            column = rule['column']
            invalid_rows = [r for r in data if r.get(column) is None]
            return {
                'rule': rule_type,
                'column': column,
                'passed': len(invalid_rows) == 0,
                'invalid_count': len(invalid_rows)
            }
        elif rule_type == 'unique':
            column = rule['column']
            values = [r.get(column) for r in data]
            return {
                'rule': rule_type,
                'column': column,
                'passed': len(values) == len(set(values)),
                'duplicate_count': len(values) - len(set(values))
            }

        return {'rule': rule_type, 'passed': True}
```

### Extract with Monitoring

```python
import time
from dataclasses import dataclass
from typing import Optional

@dataclass
class ExtractionMetrics:
    source: str
    start_time: float
    end_time: float
    record_count: int
    success: bool
    error_message: Optional[str] = None

    @property
    def duration_seconds(self) -> float:
        return self.end_time - self.start_time

    @property
    def records_per_second(self) -> float:
        return self.record_count / self.duration_seconds if self.duration_seconds > 0 else 0

class MonitoredExtractor:
    def __init__(self, extractor, source_name: str):
        self.extractor = extractor
        self.source_name = source_name
        self.metrics = []

    def extract(self) -> Dict:
        """Extract with monitoring"""
        start_time = time.time()
        success = True
        error_message = None
        record_count = 0

        try:
            data = self.extractor.extract()
            record_count = len(data) if isinstance(data, list) else 1
        except Exception as e:
            success = False
            error_message = str(e)
            raise
        finally:
            end_time = time.time()
            metrics = ExtractionMetrics(
                source=self.source_name,
                start_time=start_time,
                end_time=end_time,
                record_count=record_count,
                success=success,
                error_message=error_message
            )
            self.metrics.append(metrics)
            self.log_metrics(metrics)

        return data

    def log_metrics(self, metrics: ExtractionMetrics):
        """Log extraction metrics"""
        print(f"Extraction from {metrics.source}:")
        print(f"  Duration: {metrics.duration_seconds:.2f}s")
        print(f"  Records: {metrics.record_count}")
        print(f"  Rate: {metrics.records_per_second:.2f} records/sec")
        print(f"  Success: {metrics.success}")
```

## Best Practices

### 1. Connection Management

```python
from contextlib import contextmanager

class ConnectionPool:
    def __init__(self, max_connections: int = 10):
        self.max_connections = max_connections
        self.connections = []
        self.available = []

    @contextmanager
    def get_connection(self):
        """Get connection from pool"""
        if self.available:
            conn = self.available.pop()
        elif len(self.connections) < self.max_connections:
            conn = self.create_connection()
            self.connections.append(conn)
        else:
            raise Exception("Connection pool exhausted")

        try:
            yield conn
        finally:
            self.available.append(conn)

    def create_connection(self):
        """Create new connection"""
        # Implementation depends on database type
        pass
```

### 2. Error Handling

```python
class RobustExtractor:
    def __init__(self, max_retries: int = 3, retry_delay: float = 1.0):
        self.max_retries = max_retries
        self.retry_delay = retry_delay

    def extract_with_retry(self, query: str):
        """Extract with retry logic"""
        last_error = None

        for attempt in range(self.max_retries):
            try:
                return self.extract(query)
            except Exception as e:
                last_error = e
                if attempt < self.max_retries - 1:
                    time.sleep(self.retry_delay * (2 ** attempt))

        raise Exception(f"Extraction failed after {self.max_retries} attempts: {last_error}")
```

### 3. Idempotent Extraction

```python
class IdempotentExtractor:
    def __init__(self, checkpoint_store):
        self.checkpoint_store = checkpoint_store

    def extract_idempotent(self, extraction_id: str, query: str):
        """Extract idempotently using checkpoints"""
        # Check if already extracted
        checkpoint = self.checkpoint_store.get(extraction_id)
        if checkpoint and checkpoint['status'] == 'completed':
            print(f"Extraction {extraction_id} already completed")
            return checkpoint['result']

        # Perform extraction
        data = self.extract(query)

        # Save checkpoint
        self.checkpoint_store.save(extraction_id, {
            'status': 'completed',
            'result': data,
            'record_count': len(data)
        })

        return data
```

### 4. Monitoring and Alerting

```python
class ExtractionMonitor:
    def __init__(self):
        self.metrics = []
        self.alert_thresholds = {
            'max_duration_seconds': 3600,
            'min_records': 0,
            'max_error_rate': 0.1
        }

    def check_alerts(self) -> List[Dict]:
        """Check for alert conditions"""
        alerts = []

        # Check duration
        recent_metrics = self.metrics[-10:]  # Last 10 extractions
        avg_duration = sum(m.duration_seconds for m in recent_metrics) / len(recent_metrics)
        if avg_duration > self.alert_thresholds['max_duration_seconds']:
            alerts.append({
                'type': 'HIGH_DURATION',
                'message': f'Average extraction duration: {avg_duration:.2f}s',
                'severity': 'WARNING'
            })

        # Check error rate
        error_count = sum(1 for m in recent_metrics if not m.success)
        error_rate = error_count / len(recent_metrics)
        if error_rate > self.alert_thresholds['max_error_rate']:
            alerts.append({
                'type': 'HIGH_ERROR_RATE',
                'message': f'Error rate: {error_rate:.2%}',
                'severity': 'CRITICAL'
            })

        return alerts
```

## Further Reading

- [ETL Transform](../transform/) - Data transformation patterns
- [ETL Load](../load/) - Loading strategies
- Kafka Connect - Stream-based extraction
- [Apache NiFi](../../../08-integration-engineering/protocols/file/) - Data flow automation
