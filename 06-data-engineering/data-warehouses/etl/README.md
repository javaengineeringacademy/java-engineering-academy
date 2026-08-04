# Data Warehouse ETL

## Overview

ETL (Extract, Transform, Load) processes move data from operational systems into the data warehouse. This document covers ETL patterns, best practices, and implementation strategies for data warehouses.

## Table of Contents

- [ETL Architecture](#etl-architecture)
- [Extract Patterns](#extract-patterns)
- [Transform Patterns](#transform-patterns)
- [Load Patterns](#load-patterns)
- [Incremental Loading](#incremental-loading)
- [Error Handling](#error-handling)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)

## ETL Architecture

### Traditional ETL

```
┌─────────────────────────────────────────────────────────────┐
│                    ETL PIPELINE                              │
├─────────────────────────────────────────────────────────────┤
│  Extract          Transform           Load                   │
│  ┌─────────┐     ┌─────────┐       ┌─────────┐             │
│  │ Source  │────→│ Cleanse │──────→│ Target  │             │
│  │ Systems │     │ Enrich  │       │ Warehouse│             │
│  └─────────┘     │ Conform │       └─────────┘             │
│                  └─────────┘                                 │
├─────────────────────────────────────────────────────────────┤
│  Orchestration & Scheduling                                 │
│  Monitoring & Alerting                                      │
│  Error Handling & Recovery                                  │
└─────────────────────────────────────────────────────────────┘
```

### Modern ELT (Cloud)

```
┌─────────────────────────────────────────────────────────────┐
│                    ELT PIPELINE                              │
├─────────────────────────────────────────────────────────────┤
│  Extract          Load               Transform               │
│  ┌─────────┐     ┌─────────┐       ┌─────────┐             │
│  │ Source  │────→│ Staging │──────→│ dbt     │             │
│  │ Systems │     │ Area    │       │ Models  │             │
│  └─────────┘     └─────────┘       └─────────┘             │
│                                      (in-warehouse)         │
└─────────────────────────────────────────────────────────────┘
```

## Extract Patterns

### Full Extract

```python
def full_extract(source_table, source_engine):
    """Extract all data from source"""
    query = f"SELECT * FROM {source_table}"
    return pd.read_sql(query, source_engine)
```

### Incremental Extract

```python
def incremental_extract(source_table, source_engine, watermark_column):
    """Extract only changed data"""
    last_watermark = get_last_watermark(source_table)
    
    query = f"""
        SELECT * FROM {source_table}
        WHERE {watermark_column} > '{last_watermark}'
    """
    
    df = pd.read_sql(query, source_engine)
    
    if not df.empty:
        new_watermark = df[watermark_column].max()
        update_watermark(source_table, new_watermark)
    
    return df
```

### Change Data Capture (CDC)

```python
# Debezium CDC example
import json
from kafka import KafkaConsumer

def consume_cdc_events():
    consumer = KafkaConsumer(
        'dbserver1.public.orders',
        bootstrap_servers=['kafka:9092'],
        value_deserializer=lambda m: json.loads(m.decode('utf-8'))
    )
    
    for message in consumer:
        operation = message.value.get('op')  # c=create, u=update, d=delete
        data = message.value.get('after')
        
        if operation == 'c':
            insert_to_warehouse(data)
        elif operation == 'u':
            update_in_warehouse(data)
        elif operation == 'd':
            delete_from_warehouse(data)
```

### File-Based Extraction

```python
def extract_from_files(source_path, file_format):
    """Extract from files (CSV, JSON, Parquet)"""
    if file_format == 'csv':
        return pd.read_csv(source_path)
    elif file_format == 'json':
        return pd.read_json(source_path)
    elif file_format == 'parquet':
        return pd.read_parquet(source_path)
```

## Transform Patterns

### Data Cleansing

```python
def cleanse_data(df):
    """Clean and validate data"""
    # Remove duplicates
    df = df.drop_duplicates()
    
    # Handle nulls
    df['customer_id'] = df['customer_id'].fillna('UNKNOWN')
    
    # Standardize formats
    df['email'] = df['email'].str.lower().str.strip()
    
    # Validate data types
    df['amount'] = pd.to_numeric(df['amount'], errors='coerce')
    
    # Remove invalid records
    df = df[df['amount'] > 0]
    
    return df
```

### Data Enrichment

```python
def enrich_data(df):
    """Add additional context to data"""
    # Lookup customer segment
    customer_segments = pd.read_sql(
        "SELECT customer_id, segment FROM dim_customer",
        warehouse_engine
    )
    
    df = df.merge(customer_segments, on='customer_id', how='left')
    
    # Add geographic information
    df['region'] = df['city'].map(city_to_region_mapping)
    
    # Calculate derived fields
    df['order_value_tier'] = pd.cut(
        df['amount'],
        bins=[0, 50, 200, 1000, float('inf')],
        labels=['low', 'medium', 'high', 'premium']
    )
    
    return df
```

### Data Conformity

```python
def conform_data(df):
    """Standardize data to warehouse schema"""
    # Rename columns to match target
    column_mapping = {
        'cust_id': 'customer_id',
        'ord_dt': 'order_date',
        'total_amt': 'total_amount'
    }
    df = df.rename(columns=column_mapping)
    
    # Apply business rules
    df['status'] = df['status'].map({
        'P': 'pending',
        'S': 'shipped',
        'D': 'delivered',
        'C': 'cancelled'
    })
    
    # Surrogate keys
    df['customer_key'] = df['customer_id'].map(customer_key_lookup)
    df['product_key'] = df['product_id'].map(product_key_lookup)
    
    return df
```

## Load Patterns

### Full Load

```python
def full_load(df, target_table, warehouse_engine):
    """Truncate and reload entire table"""
    with warehouse_engine.connect() as conn:
        conn.execute(f"TRUNCATE TABLE {target_table}")
    
    df.to_sql(
        target_table,
        warehouse_engine,
        if_exists='append',
        index=False
    )
```

### Incremental Load

```python
def incremental_load(df, target_table, warehouse_engine, key_column):
    """Insert new records, update existing"""
    # Get existing keys
    existing_keys = pd.read_sql(
        f"SELECT {key_column} FROM {target_table}",
        warehouse_engine
    )[key_column].tolist()
    
    # Split into new and existing
    new_records = df[~df[key_column].isin(existing_keys)]
    existing_records = df[df[key_column].isin(existing_keys)]
    
    # Insert new records
    if not new_records.empty:
        new_records.to_sql(target_table, warehouse_engine, 
                          if_exists='append', index=False)
    
    # Update existing records
    if not existing_records.empty:
        for _, row in existing_records.iterrows():
            update_record(target_table, row, warehouse_engine)
```

### Merge (Upsert)

```sql
-- Snowflake merge
MERGE INTO target_table t
USING staging_table s
ON t.key = s.key
WHEN MATCHED THEN
    UPDATE SET t.col1 = s.col1, t.col2 = s.col2
WHEN NOT MATCHED THEN
    INSERT (key, col1, col2) VALUES (s.key, s.col1, s.col2);

-- Delta Lake merge
MERGE INTO target_table t
USING staging_table s
ON t.key = s.key
WHEN MATCHED THEN UPDATE *
WHEN NOT MATCHED THEN INSERT *;
```

## Incremental Loading

### Watermark Pattern

```python
def get_watermark(table_name):
    """Get last processed watermark"""
    query = f"""
        SELECT MAX(last_updated) as watermark
        FROM etl_watermarks
        WHERE table_name = '{table_name}'
    """
    result = pd.read_sql(query, metadata_engine)
    return result['watermark'].iloc[0] or '1900-01-01'

def update_watermark(table_name, watermark):
    """Update watermark after successful load"""
    query = f"""
        INSERT INTO etl_watermarks (table_name, watermark, updated_at)
        VALUES ('{table_name}', '{watermark}', CURRENT_TIMESTAMP)
    """
    execute_query(query, metadata_engine)
```

### Timestamp-Based

```python
def incremental_by_timestamp(source_table, target_table, timestamp_col):
    """Incremental load based on timestamp"""
    last_sync = get_watermark(target_table)
    
    source_data = pd.read_sql(f"""
        SELECT * FROM {source_table}
        WHERE {timestamp_col} > '{last_sync}'
    """, source_engine)
    
    if not source_data.empty:
        # Load to target
        load_to_target(source_data, target_table)
        
        # Update watermark
        new_watermark = source_data[timestamp_col].max()
        update_watermark(target_table, new_watermark)
```

### Log-Based (CDC)

```python
def cdc_incremental(source_table, target_table):
    """CDC-based incremental loading"""
    # Read change log
    changes = read_cdc_log(source_table)
    
    for change in changes:
        if change['operation'] == 'INSERT':
            insert_record(target_table, change['data'])
        elif change['operation'] == 'UPDATE':
            update_record(target_table, change['data'])
        elif change['operation'] == 'DELETE':
            soft_delete_record(target_table, change['data'])
```

## Error Handling

### Dead Letter Queue

```python
def process_with_dlq(source_data, target_table):
    """Process data with dead letter queue for failures"""
    success_records = []
    failed_records = []
    
    for _, record in source_data.iterrows():
        try:
            validate_record(record)
            insert_record(target_table, record)
            success_records.append(record)
        except Exception as e:
            record['_error'] = str(e)
            record['_failed_at'] = datetime.now()
            failed_records.append(record)
    
    # Load failures to DLQ
    if failed_records:
        pd.DataFrame(failed_records).to_sql(
            f"{target_table}_dlq",
            warehouse_engine,
            if_exists='append',
            index=False
        )
    
    return success_records, failed_records
```

### Retry Logic

```python
import time
from functools import wraps

def retry(max_attempts=3, delay=1):
    """Retry decorator for transient failures"""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts - 1:
                        raise
                    time.sleep(delay * (attempt + 1))
            return None
        return wrapper
    return decorator

@retry(max_attempts=3, delay=5)
def load_data_with_retry(df, target_table):
    """Load data with automatic retry"""
    df.to_sql(target_table, warehouse_engine, if_exists='append')
```

## Performance Optimization

### Bulk Loading

```python
def bulk_load(df, target_table, chunk_size=10000):
    """Bulk load data for better performance"""
    for i in range(0, len(df), chunk_size):
        chunk = df.iloc[i:i+chunk_size]
        
        # Use COPY command for PostgreSQL
        chunk.to_csv('/tmp/temp.csv', index=False, header=False)
        
        with warehouse_engine.connect() as conn:
            conn.execute(f"""
                COPY {target_table} FROM STDIN WITH CSV
            """, open('/tmp/temp.csv', 'r'))
```

### Parallel Processing

```python
from concurrent.futures import ThreadPoolExecutor
import pandas as pd

def parallel_extract(source_tables, source_engine):
    """Extract from multiple sources in parallel"""
    def extract_single(table):
        return pd.read_sql(f"SELECT * FROM {table}", source_engine)
    
    with ThreadPoolExecutor(max_workers=4) as executor:
        futures = {executor.submit(extract_single, t): t 
                  for t in source_tables}
        
        results = {}
        for future in futures:
            table = futures[future]
            results[table] = future.result()
    
    return results
```

### Staging Tables

```python
def load_via_staging(source_data, target_table):
    """Load via staging table for better performance"""
    staging_table = f"{target_table}_staging"
    
    # Load to staging
    source_data.to_sql(staging_table, warehouse_engine, 
                      if_exists='replace', index=False)
    
    # Merge from staging to target
    with warehouse_engine.connect() as conn:
        conn.execute(f"""
            INSERT INTO {target_table}
            SELECT * FROM {staging_table}
            ON CONFLICT (key_column) 
            DO UPDATE SET
                col1 = EXCLUDED.col1,
                col2 = EXCLUDED.col2
        """)
        
        conn.execute(f"DROP TABLE {staging_table}")
```

## Best Practices

### 1. Idempotent ETL

```python
def idempotent_load(df, target_table, batch_id):
    """Ensure ETL can be safely re-run"""
    # Check if batch already processed
    if is_batch_processed(batch_id, target_table):
        return
    
    # Process and mark as complete
    load_data(df, target_table)
    mark_batch_complete(batch_id, target_table)
```

### 2. Data Quality Gates

```python
def validate_before_load(df, rules):
    """Validate data quality before loading"""
    errors = []
    
    for rule in rules:
        if rule['type'] == 'not_null':
            if df[rule['column']].isnull().any():
                errors.append(f"{rule['column']} has nulls")
        
        elif rule['type'] == 'range':
            if df[rule['column']].min() < rule['min']:
                errors.append(f"{rule['column']} below minimum")
    
    if errors:
        raise DataQualityError(errors)
    
    return True
```

### 3. Monitoring

```python
def log_etl_metrics(pipeline_name, metrics):
    """Log ETL metrics for monitoring"""
    metrics_data = {
        'pipeline': pipeline_name,
        'start_time': metrics['start_time'],
        'end_time': datetime.now(),
        'records_extracted': metrics['extracted'],
        'records_transformed': metrics['transformed'],
        'records_loaded': metrics['loaded'],
        'errors': metrics['errors'],
        'duration_seconds': (datetime.now() - metrics['start_time']).seconds
    }
    
    # Send to monitoring system
    send_to_monitoring(metrics_data)
```

### 4. Documentation

```yaml
etl_documentation:
  pipeline: daily_order_etl
  schedule: "0 2 * * *"  # Daily at 2 AM
  sources:
    - table: orders
      system: erp
      extract_type: incremental
  targets:
    - table: fact_orders
      warehouse: snowflake
  transforms:
    - name: cleanse
      description: "Remove duplicates, validate fields"
    - name: enrich
      description: "Add customer segment, region"
  owners:
    - data-engineering-team
```

## Further Reading

- [The Data Warehouse ETL Toolkit - Ralph Kimball](https://www.kimballgroup.com/data-warehouse-business-intelligence-resources/kimball-techniques/dimensional-modeling-techniques/)
- [dbt Documentation](https://docs.getdbt.com/)
- [Apache Spark ETL](https://spark.apache.org/docs/latest/etl.html)
