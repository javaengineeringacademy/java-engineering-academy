# Data Loading Strategies

Data loading is the final step in ETL/ELT pipelines where transformed data is written to the target system. This covers loading strategies, bulk operations, and upsert patterns.

## Table of Contents

- [Overview](#overview)
- [Loading Strategies](#loading-strategies)
- [Bulk Operations](#bulk-operations)
- [Upsert Patterns](#upsert-patterns)
- [Examples](#examples)
- [Best Practices](#best-practices)

## Overview

Loading strategies determine how data is written to target systems. The choice depends on data volume, update frequency, and target system capabilities.

### Loading Patterns

```
┌─────────────────────────────────────────────────────────────────┐
│                    LOADING PATTERNS                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Full Load          │ Replace all data                         │
│  ─────────────────────────────────────────────────────────────  │
│  + Simple           - Slow for large datasets                  │
│  + Consistent       - High resource usage                      │
│                                                                 │
│  Incremental Load   │ Add new/changed data                     │
│  ─────────────────────────────────────────────────────────────  │
│  + Fast             - Complex logic                             │
│  + Efficient        - Requires change tracking                  │
│                                                                 │
│  Upsert             │ Insert or update                         │
│  ─────────────────────────────────────────────────────────────  │
│  + Handles changes  - Slower than bulk insert                   │
│  + Maintains state  - Requires unique keys                      │
│                                                                 │
│  Streaming          │ Real-time loading                        │
│  ─────────────────────────────────────────────────────────────  │
│  + Low latency      - Complex setup                            │
│  + Continuous       - Higher resource usage                     │
└─────────────────────────────────────────────────────────────────┘
```

## Loading Strategies

### Full Load (Truncate and Load)

```python
from typing import List, Dict
from datetime import datetime

class FullLoader:
    def __init__(self, target_connection):
        self.target = target_connection

    def load(self, table_name: str, data: List[Dict]) -> Dict:
        """Full load - truncate and reload"""
        start_time = datetime.now()

        # Truncate target table
        self.target.execute(f"TRUNCATE TABLE {table_name}")

        # Insert all records
        if data:
            self.bulk_insert(table_name, data)

        end_time = datetime.now()

        return {
            'strategy': 'full_load',
            'records_loaded': len(data),
            'duration_seconds': (end_time - start_time).total_seconds(),
            'start_time': start_time,
            'end_time': end_time
        }

    def bulk_insert(self, table_name: str, data: List[Dict]):
        """Bulk insert data"""
        if not data:
            return

        columns = list(data[0].keys())
        placeholders = ', '.join(['%s'] * len(columns))
        column_names = ', '.join(columns)

        query = f"""
            INSERT INTO {table_name} ({column_names})
            VALUES ({placeholders})
        """

        values = [tuple(record[col] for col in columns) for record in data]
        self.target.executemany(query, values)
```

### Incremental Load

```python
from typing import List, Dict, Optional
from datetime import datetime

class IncrementalLoader:
    def __init__(self, target_connection, watermark_column: str = 'updated_at'):
        self.target = target_connection
        self.watermark_column = watermark_column

    def get_last_watermark(self, table_name: str) -> Optional[datetime]:
        """Get last processed watermark"""
        query = f"""
            SELECT MAX({self.watermark_column}) as last_watermark
            FROM {table_name}
        """
        result = self.target.execute(query).fetchone()
        return result[0] if result else None

    def load_incremental(self, table_name: str, data: List[Dict]) -> Dict:
        """Load incrementally based on watermark"""
        start_time = datetime.now()
        last_watermark = self.get_last_watermark(table_name)

        # Filter data newer than last watermark
        if last_watermark:
            new_data = [
                record for record in data
                if record.get(self.watermark_column) and
                   record[self.watermark_column] > last_watermark
            ]
        else:
            new_data = data

        # Insert new records
        inserted = 0
        updated = 0
        for record in new_data:
            existing = self.find_existing(table_name, record)
            if existing:
                self.update_record(table_name, record)
                updated += 1
            else:
                self.insert_record(table_name, record)
                inserted += 1

        end_time = datetime.now()

        return {
            'strategy': 'incremental',
            'records_inserted': inserted,
            'records_updated': updated,
            'last_watermark': last_watermark,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def find_existing(self, table_name: str, record: Dict) -> bool:
        """Check if record exists"""
        # Implementation depends on primary key structure
        return False

    def update_record(self, table_name: str, record: Dict):
        """Update existing record"""
        pass

    def insert_record(self, table_name: str, record: Dict):
        """Insert new record"""
        pass
```

### Merge (Upsert) Load

```python
from typing import List, Dict
from datetime import datetime

class UpsertLoader:
    def __init__(self, target_connection, primary_key: str):
        self.target = target_connection
        self.primary_key = primary_key

    def upsert(self, table_name: str, data: List[Dict]) -> Dict:
        """Insert or update records"""
        start_time = datetime.now()

        inserted = 0
        updated = 0

        for record in data:
            if self.record_exists(table_name, record):
                self.update(table_name, record)
                updated += 1
            else:
                self.insert(table_name, record)
                inserted += 1

        end_time = datetime.now()

        return {
            'strategy': 'upsert',
            'records_inserted': inserted,
            'records_updated': updated,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def record_exists(self, table_name: str, record: Dict) -> bool:
        """Check if record exists by primary key"""
        pk_value = record.get(self.primary_key)
        query = f"""
            SELECT 1 FROM {table_name}
            WHERE {self.primary_key} = %s
        """
        result = self.target.execute(query, [pk_value]).fetchone()
        return result is not None

    def update(self, table_name: str, record: Dict):
        """Update existing record"""
        pk_value = record.get(self.primary_key)
        set_clause = ', '.join([f"{k} = %s" for k in record.keys() if k != self.primary_key])
        values = [v for k, v in record.items() if k != self.primary_key]
        values.append(pk_value)

        query = f"""
            UPDATE {table_name}
            SET {set_clause}
            WHERE {self.primary_key} = %s
        """
        self.target.execute(query, values)

    def insert(self, table_name: str, record: Dict):
        """Insert new record"""
        columns = ', '.join(record.keys())
        placeholders = ', '.join(['%s'] * len(record))
        values = list(record.values())

        query = f"""
            INSERT INTO {table_name} ({columns})
            VALUES ({placeholders})
        """
        self.target.execute(query, values)
```

## Bulk Operations

### Batch Insert

```python
from typing import List, Dict
from datetime import datetime

class BatchInserter:
    def __init__(self, target_connection, batch_size: int = 1000):
        self.target = target_connection
        self.batch_size = batch_size

    def insert_batch(self, table_name: str, data: List[Dict]) -> Dict:
        """Insert data in batches"""
        start_time = datetime.now()
        total_inserted = 0

        for i in range(0, len(data), self.batch_size):
            batch = data[i:i + self.batch_size]
            self.execute_batch_insert(table_name, batch)
            total_inserted += len(batch)

        end_time = datetime.now()

        return {
            'strategy': 'batch_insert',
            'records_inserted': total_inserted,
            'batch_size': self.batch_size,
            'batches_processed': (len(data) + self.batch_size - 1) // self.batch_size,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def execute_batch_insert(self, table_name: str, batch: List[Dict]):
        """Execute batch insert"""
        if not batch:
            return

        columns = list(batch[0].keys())
        column_names = ', '.join(columns)
        placeholders = ', '.join(['%s'] * len(columns))

        query = f"""
            INSERT INTO {table_name} ({column_names})
            VALUES ({placeholders})
        """

        values = [tuple(record[col] for col in columns) for record in batch]
        self.target.executemany(query, values)
```

### Bulk Update

```python
from typing import List, Dict
from datetime import datetime

class BulkUpdater:
    def __init__(self, target_connection, batch_size: int = 500):
        self.target = target_connection
        self.batch_size = batch_size

    def update_bulk(self, table_name: str, data: List[Dict], key_column: str) -> Dict:
        """Bulk update records"""
        start_time = datetime.now()
        total_updated = 0

        for i in range(0, len(data), self.batch_size):
            batch = data[i:i + self.batch_size]
            updated = self.execute_batch_update(table_name, batch, key_column)
            total_updated += updated

        end_time = datetime.now()

        return {
            'strategy': 'bulk_update',
            'records_updated': total_updated,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def execute_batch_update(self, table_name: str, batch: List[Dict], key_column: str) -> int:
        """Execute batch update"""
        if not batch:
            return 0

        # Get all columns except key
        update_columns = [col for col in batch[0].keys() if col != key_column]

        # Build CASE statement for bulk update
        cases = {col: [] for col in update_columns}
        keys = []

        for record in batch:
            keys.append(record[key_column])
            for col in update_columns:
                cases[col].append(f"WHEN {key_column} = %s THEN %s")

        # Build query
        set_clauses = []
        values = []
        for col in update_columns:
            set_clauses.append(f"{col} = CASE {' '.join(cases[col])} END")
            for record in batch:
                values.append(record[key_column])
                values.append(record[col])

        query = f"""
            UPDATE {table_name}
            SET {', '.join(set_clauses)}
            WHERE {key_column} IN ({', '.join(['%s'] * len(keys))})
        """

        values.extend(keys)
        self.target.execute(query, values)

        return len(batch)
```

### Bulk Delete

```python
from typing import List, Dict
from datetime import datetime

class BulkDeleter:
    def __init__(self, target_connection):
        self.target = target_connection

    def delete_by_ids(self, table_name: str, ids: List, key_column: str = 'id') -> Dict:
        """Delete records by IDs"""
        start_time = datetime.now()

        # Delete in batches to avoid large IN clauses
        batch_size = 1000
        total_deleted = 0

        for i in range(0, len(ids), batch_size):
            batch_ids = ids[i:i + batch_size]
            placeholders = ', '.join(['%s'] * len(batch_ids))
            query = f"""
                DELETE FROM {table_name}
                WHERE {key_column} IN ({placeholders})
            """
            self.target.execute(query, batch_ids)
            total_deleted += len(batch_ids)

        end_time = datetime.now()

        return {
            'strategy': 'bulk_delete',
            'records_deleted': total_deleted,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def delete_by_condition(self, table_name: str, condition: str, params: List = None) -> Dict:
        """Delete records by condition"""
        start_time = datetime.now()

        query = f"DELETE FROM {table_name} WHERE {condition}"
        self.target.execute(query, params or [])

        # Get deleted count if available
        end_time = datetime.now()

        return {
            'strategy': 'conditional_delete',
            'condition': condition,
            'duration_seconds': (end_time - start_time).total_seconds()
        }
```

## Upsert Patterns

### Database-specific Upsert

```python
from typing import List, Dict
from datetime import datetime

class DatabaseUpsert:
    def __init__(self, target_connection, db_type: str):
        self.target = target_connection
        self.db_type = db_type

    def upsert(self, table_name: str, data: List[Dict], key_column: str) -> Dict:
        """Upsert using database-specific syntax"""
        start_time = datetime.now()

        if self.db_type == 'postgresql':
            inserted, updated = self.postgres_upsert(table_name, data, key_column)
        elif self.db_type == 'mysql':
            inserted, updated = self.mysql_upsert(table_name, data, key_column)
        elif self.db_type == 'sqlite':
            inserted, updated = self.sqlite_upsert(table_name, data, key_column)
        else:
            inserted, updated = self.generic_upsert(table_name, data, key_column)

        end_time = datetime.now()

        return {
            'strategy': 'database_upsert',
            'db_type': self.db_type,
            'records_inserted': inserted,
            'records_updated': updated,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def postgres_upsert(self, table_name: str, data: List[Dict], key_column: str):
        """PostgreSQL INSERT ... ON CONFLICT"""
        if not data:
            return 0, 0

        columns = list(data[0].keys())
        column_names = ', '.join(columns)
        placeholders = ', '.join(['%s'] * len(columns))

        # Build update clause for all non-key columns
        update_columns = [col for col in columns if col != key_column]
        update_clause = ', '.join([f"{col} = EXCLUDED.{col}" for col in update_columns])

        query = f"""
            INSERT INTO {table_name} ({column_names})
            VALUES ({placeholders})
            ON CONFLICT ({key_column})
            DO UPDATE SET {update_clause}
        """

        inserted = 0
        updated = 0
        for record in data:
            try:
                self.target.execute(query, [record[col] for col in columns])
                # Check if insert or update happened
                if self.target.rowcount == 1:
                    inserted += 1
                elif self.target.rowcount == 2:  # Update happened
                    updated += 1
            except Exception:
                pass

        return inserted, updated

    def mysql_upsert(self, table_name: str, data: List[Dict], key_column: str):
        """MySQL INSERT ... ON DUPLICATE KEY"""
        if not data:
            return 0, 0

        columns = list(data[0].keys())
        column_names = ', '.join(columns)
        placeholders = ', '.join(['%s'] * len(columns))

        # Build update clause
        update_columns = [col for col in columns if col != key_column]
        update_clause = ', '.join([f"{col} = VALUES({col})" for col in update_columns])

        query = f"""
            INSERT INTO {table_name} ({column_names})
            VALUES ({placeholders})
            ON DUPLICATE KEY UPDATE {update_clause}
        """

        self.target.executemany(query, [[record[col] for col in columns] for record in data])

        return len(data), 0  # MySQL doesn't easily distinguish
```

### Application-level Upsert

```python
from typing import List, Dict
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor, as_completed

class ApplicationUpsert:
    def __init__(self, target_connection, primary_key: str):
        self.target = target_connection
        self.primary_key = primary_key
        self.cache = {}

    def upsert(self, table_name: str, data: List[Dict]) -> Dict:
        """Upsert with application logic"""
        start_time = datetime.now()

        inserted = 0
        updated = 0
        skipped = 0

        for record in data:
            result = self.upsert_record(table_name, record)
            if result == 'inserted':
                inserted += 1
            elif result == 'updated':
                updated += 1
            else:
                skipped += 1

        end_time = datetime.now()

        return {
            'strategy': 'application_upsert',
            'records_inserted': inserted,
            'records_updated': updated,
            'records_skipped': skipped,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def upsert_record(self, table_name: str, record: Dict) -> str:
        """Upsert single record"""
        pk_value = record.get(self.primary_key)

        # Check cache first
        if pk_value in self.cache:
            if self.cache[pk_value] == record.get('checksum'):
                return 'skipped'
            else:
                self.update(table_name, record)
                self.cache[pk_value] = record.get('checksum')
                return 'updated'

        # Check database
        if self.record_exists(table_name, pk_value):
            self.update(table_name, record)
            self.cache[pk_value] = record.get('checksum')
            return 'updated'
        else:
            self.insert(table_name, record)
            self.cache[pk_value] = record.get('checksum')
            return 'inserted'

    def record_exists(self, table_name: str, pk_value) -> bool:
        """Check if record exists"""
        query = f"SELECT 1 FROM {table_name} WHERE {self.primary_key} = %s"
        result = self.target.execute(query, [pk_value]).fetchone()
        return result is not None

    def insert(self, table_name: str, record: Dict):
        """Insert record"""
        columns = ', '.join(record.keys())
        placeholders = ', '.join(['%s'] * len(record))
        query = f"INSERT INTO {table_name} ({columns}) VALUES ({placeholders})"
        self.target.execute(query, list(record.values()))

    def update(self, table_name: str, record: Dict):
        """Update record"""
        pk_value = record.get(self.primary_key)
        set_clause = ', '.join([f"{k} = %s" for k in record.keys() if k != self.primary_key])
        values = [v for k, v in record.items() if k != self.primary_key]
        values.append(pk_value)

        query = f"""
            UPDATE {table_name}
            SET {set_clause}
            WHERE {self.primary_key} = %s
        """
        self.target.execute(query, values)
```

## Examples

### Data Warehouse Loading

```python
from typing import List, Dict
from datetime import datetime

class DataWarehouseLoader:
    def __init__(self, warehouse_connection):
        self.warehouse = warehouse_connection

    def load_dimension(self, table_name: str, data: List[Dict], key_column: str) -> Dict:
        """Load dimension table with SCD Type 2"""
        start_time = datetime.now()

        inserted = 0
        updated = 0

        for record in data:
            # Check if record exists
            existing = self.get_existing_record(table_name, key_column, record[key_column])

            if not existing:
                # New record - insert with effective dates
                record['effective_from'] = datetime.now()
                record['effective_to'] = None
                record['is_current'] = True
                self.insert(table_name, record)
                inserted += 1
            elif self.has_changes(existing, record):
                # Changed record - expire old, insert new
                self.expire_record(table_name, existing)
                record['effective_from'] = datetime.now()
                record['effective_to'] = None
                record['is_current'] = True
                self.insert(table_name, record)
                updated += 1

        end_time = datetime.now()

        return {
            'table': table_name,
            'strategy': 'scd_type2',
            'records_inserted': inserted,
            'records_updated': updated,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def load_fact(self, table_name: str, data: List[Dict], dimension_lookups: Dict) -> Dict:
        """Load fact table with dimension lookups"""
        start_time = datetime.now()
        loaded = 0
        skipped = 0

        for record in data:
            # Resolve foreign keys
            fact_record = self.resolve_foreign_keys(record, dimension_lookups)

            if fact_record:
                self.insert(table_name, fact_record)
                loaded += 1
            else:
                skipped += 1

        end_time = datetime.now()

        return {
            'table': table_name,
            'strategy': 'fact_load',
            'records_loaded': loaded,
            'records_skipped': skipped,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def resolve_foreign_keys(self, record: Dict, dimension_lookups: Dict) -> Dict:
        """Resolve foreign keys for fact table"""
        resolved = {}

        for field, value in record.items():
            if field in dimension_lookups:
                lookup_table = dimension_lookups[field]
                key = self.get_surrogate_key(lookup_table, value)
                if key is not None:
                    resolved[field] = key
                else:
                    return None  # Skip if lookup fails
            else:
                resolved[field] = value

        return resolved
```

### Partition Loading

```python
from typing import List, Dict
from datetime import datetime, timedelta

class PartitionedLoader:
    def __init__(self, target_connection):
        self.target = target_connection

    def load_partition(self, table_name: str, data: List[Dict],
                      partition_column: str, partition_value: str) -> Dict:
        """Load data into specific partition"""
        start_time = datetime.now()

        # Ensure partition exists
        self.create_partition_if_not_exists(table_name, partition_column, partition_value)

        # Load data
        inserted = 0
        for i in range(0, len(data), 1000):
            batch = data[i:i + 1000]
            self.insert_batch(table_name, batch)
            inserted += len(batch)

        end_time = datetime.now()

        return {
            'table': table_name,
            'partition': f"{partition_column}={partition_value}",
            'records_loaded': inserted,
            'duration_seconds': (end_time - start_time).total_seconds()
        }

    def create_partition_if_not_exists(self, table_name: str, partition_column: str, partition_value: str):
        """Create partition if it doesn't exist"""
        query = f"""
            SELECT 1 FROM information_schema.partitions
            WHERE table_name = '{table_name}'
            AND partition_name = '{partition_column}={partition_value}'
        """
        if not self.target.execute(query).fetchone():
            # Create partition
            create_query = f"""
                ALTER TABLE {table_name}
                ADD PARTITION ({partition_column} = '{partition_value}')
            """
            self.target.execute(create_query)

    def insert_batch(self, table_name: str, batch: List[Dict]):
        """Insert batch of records"""
        if not batch:
            return

        columns = ', '.join(batch[0].keys())
        placeholders = ', '.join(['%s'] * len(batch[0]))
        query = f"INSERT INTO {table_name} ({columns}) VALUES ({placeholders})"

        values = [list(record.values()) for record in batch]
        self.target.executemany(query, values)
```

## Best Practices

### 1. Transaction Management

```python
from typing import List, Dict
from contextlib import contextmanager

class TransactionalLoader:
    def __init__(self, target_connection):
        self.target = target_connection

    @contextmanager
    def transaction(self):
        """Transaction context manager"""
        try:
            yield self.target
            self.target.commit()
        except Exception as e:
            self.target.rollback()
            raise e

    def load_with_transaction(self, table_name: str, data: List[Dict]) -> Dict:
        """Load data within a transaction"""
        with self.transaction():
            # Perform loading operations
            for record in data:
                self.insert_or_update(table_name, record)

        return {'status': 'success', 'records_loaded': len(data)}
```

### 2. Error Handling and Recovery

```python
from typing import List, Dict
from datetime import datetime

class ResilientLoader:
    def __init__(self, target_connection, max_retries: int = 3):
        self.target = target_connection
        self.max_retries = max_retries
        self.failed_records = []

    def load_with_retry(self, table_name: str, data: List[Dict]) -> Dict:
        """Load with retry logic"""
        start_time = datetime.now()
        loaded = 0
        failed = 0

        for record in data:
            success = False
            for attempt in range(self.max_retries):
                try:
                    self.insert(table_name, record)
                    success = True
                    break
                except Exception as e:
                    if attempt == self.max_retries - 1:
                        self.failed_records.append({
                            'record': record,
                            'error': str(e),
                            'timestamp': datetime.now()
                        })
                        failed += 1

            if success:
                loaded += 1

        end_time = datetime.now()

        return {
            'records_loaded': loaded,
            'records_failed': failed,
            'duration_seconds': (end_time - start_time).total_seconds(),
            'failed_records': self.failed_records
        }

    def retry_failed(self, table_name: str) -> Dict:
        """Retry loading failed records"""
        original_count = len(self.failed_records)
        still_failed = []

        for failed in self.failed_records:
            try:
                self.insert(table_name, failed['record'])
            except Exception as e:
                still_failed.append({
                    'record': failed['record'],
                    'error': str(e),
                    'timestamp': datetime.now()
                })

        self.failed_records = still_failed

        return {
            'retried': original_count,
            'still_failed': len(still_failed)
        }
```

### 3. Performance Monitoring

```python
from typing import Dict, List
from datetime import datetime
import time

class PerformanceMonitor:
    def __init__(self):
        self.metrics = []

    def monitor_load(self, loader, table_name: str, data: List[Dict]) -> Dict:
        """Monitor loading performance"""
        start_time = time.time()
        start_memory = self.get_memory_usage()

        result = loader.load(table_name, data)

        end_time = time.time()
        end_memory = self.get_memory_usage()

        metrics = {
            'table': table_name,
            'records': len(data),
            'duration_seconds': end_time - start_time,
            'records_per_second': len(data) / (end_time - start_time) if end_time > start_time else 0,
            'memory_used_mb': end_memory - start_memory,
            'result': result
        }

        self.metrics.append(metrics)
        return metrics

    def get_memory_usage(self) -> float:
        """Get current memory usage in MB"""
        import psutil
        process = psutil.Process()
        return process.memory_info().rss / 1024 / 1024

    def get_performance_summary(self) -> Dict:
        """Get performance summary"""
        if not self.metrics:
            return {}

        total_records = sum(m['records'] for m in self.metrics)
        total_duration = sum(m['duration_seconds'] for m in self.metrics)

        return {
            'total_records': total_records,
            'total_duration_seconds': total_duration,
            'average_records_per_second': total_records / total_duration if total_duration > 0 else 0,
            'metrics_count': len(self.metrics)
        }
```

### 4. Data Quality Checks

```python
from typing import List, Dict

class LoadingQualityChecker:
    def __init__(self):
        self.checks = []

    def add_check(self, name: str, check_func):
        """Add quality check"""
        self.checks.append({'name': name, 'func': check_func})

    def validate_before_loading(self, data: List[Dict]) -> Dict:
        """Validate data before loading"""
        results = []

        for check in self.checks:
            try:
                passed = check['func'](data)
                results.append({
                    'check': check['name'],
                    'passed': passed
                })
            except Exception as e:
                results.append({
                    'check': check['name'],
                    'passed': False,
                    'error': str(e)
                })

        return {
            'all_passed': all(r['passed'] for r in results),
            'results': results
        }

# Example quality checks
def create_quality_checks():
    checker = LoadingQualityChecker()

    checker.add_check('not_empty', lambda data: len(data) > 0)
    checker.add_check('no_duplicates', lambda data: len(data) == len(set(str(r) for r in data)))
    checker.add_check('required_fields', lambda data: all('id' in r and 'name' in r for r in data))

    return checker
```

## Further Reading

- [ETL Extract](../extract/) - Data extraction patterns
- [ETL Transform](../transform/) - Data transformation
- [Data Warehouse Loading](../../data-warehouses/etl/) - Warehouse-specific patterns
- [Data Lake Loading](../../data-lakes/fundamentals/) - Lake loading patterns
