# Google BigQuery

## Overview

BigQuery is a serverless, highly scalable, and cost-effective multi-cloud data warehouse for analytics.

## Core Concepts

```
┌─────────────────────────────────────────────────────────┐
│                    BigQuery                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Dataset  │  │  Tables  │  │  Views   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Queries      │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## SQL Syntax

### Basic Query
```sql
SELECT
  name,
  COUNT(*) as count
FROM
  `project.dataset.table`
WHERE
  date >= '2024-01-01'
GROUP BY
  name
ORDER BY
  count DESC
LIMIT 100
```

### Window Functions
```sql
SELECT
  name,
  date,
  revenue,
  SUM(revenue) OVER (PARTITION BY name ORDER BY date) as running_total
FROM
  `project.dataset.sales`
```

### Join Types
```sql
-- Inner Join
SELECT a.*, b.*
FROM `project.dataset.orders` a
INNER JOIN `project.dataset.customers` b
ON a.customer_id = b.id

-- Left Join
SELECT a.*, b.*
FROM `project.dataset.orders` a
LEFT JOIN `project.dataset.customers` b
ON a.customer_id = b.id
```

## Slots & Pricing

### Pricing Models
| Model           | Description                    |
|-----------------|--------------------------------|
| On-Demand       | Pay per query (TB processed)   |
| Capacity        | Pay for reserved slots         |

### Slot Capacities
| Slots | Use Case              |
|-------|------------------------|
| 100   | Development            |
| 500   | Small workloads        |
| 1000  | Medium workloads       |
| 2000+ | Enterprise workloads   |

### Create Reservation
```bash
# Create reservation
bq mk --reservation=500 --edition=ENTERPRISE my-reservation

# Assign to project
bq mk --transfer_config \
  --project_id=my-project \
  --data_source=google_cloud_storage \
  --params='{"destinationTable":"my-project:my_dataset.my_table"}'
```

## Partitions

### Time-Unit Partitioning
```sql
-- Create partitioned table
CREATE TABLE `project.dataset.events`
(
  event_id STRING,
  event_timestamp TIMESTAMP,
  event_data JSON
)
PARTITION BY DATE(event_timestamp)
```

### ingestion-time Partitioning
```sql
-- Partition by ingestion time
CREATE TABLE `project.dataset.events`
(
  event_id STRING,
  event_data JSON
)
PARTITION BY _PARTITIONDATE
```

### Integer-Range Partitioning
```sql
-- Partition by integer range
CREATE TABLE `project.dataset.events`
(
  user_id INT64,
  event_data JSON
)
PARTITION BY RANGE_BUCKET(user_id, GENERATE_ARRAY(0, 1000000, 10000))
```

## Clustering

```sql
-- Create clustered table
CREATE TABLE `project.dataset.events`
(
  event_id STRING,
  event_timestamp TIMESTAMP,
  user_id INT64,
  event_data JSON
)
PARTITION BY DATE(event_timestamp)
CLUSTER BY user_id, event_type
```

## Materialized Views

```sql
-- Create materialized view
CREATE MATERIALIZED VIEW `project.dataset.sales_summary`
AS
SELECT
  product_id,
  DATE(sale_date) as date,
  SUM(amount) as total_sales,
  COUNT(*) as transaction_count
FROM
  `project.dataset.sales`
GROUP BY
  product_id, DATE(sale_date)
```

## Scheduled Queries

```bash
# Create scheduled query
bq mk --transfer_config \
  --project_id=my-project \
  --data_source=google_cloud_storage \
  --display_name="Daily Sales Summary" \
  --schedule="every 24 hours" \
  --params='{
    "query": "SELECT * FROM `project.dataset.sales` WHERE date = CURRENT_DATE()",
    "destinationTable": "project.dataset.daily_summary",
    "writeDisposition": "WRITE_TRUNCATE"
  }'
```

## BigQuery ML

### Create Model
```sql
-- Linear regression
CREATE MODEL `project.dataset.sales_model`
OPTIONS(
  model_type='LINEAR_REG',
  input_label_cols=['revenue']
) AS
SELECT
  product_id,
  category,
  price,
  revenue
FROM
  `project.dataset.sales`

-- Predict
SELECT *
FROM ML.PREDICT(MODEL `project.dataset.sales_model`,
  (SELECT 'product_1' as product_id, 'electronics' as category, 99.99 as price))
```

## Data Transfer

```bash
# Create transfer from GCS
bq mk --transfer_config \
  --project_id=my-project \
  --data_source=google_cloud_storage \
  --display_name="GCS Transfer" \
  --params='{
    "filePaths": ["gs://my-bucket/data/*.csv"],
    "destinationTable": "project.dataset.table",
    "format": "CSV"
  }'
```

## Storage Options

| Type           | Description                    |
|----------------|--------------------------------|
| Native         | BigQuery internal storage      |
| External       | Data outside BigQuery          |
| Iceberg        | Apache Iceberg tables          |
| Delta Lake     | Delta Lake tables              |

## Monitoring

```bash
# Get query statistics
bq show --format=prettyjson project:dataset.table

# Get job statistics
bq ls --all --format=prettyjson project:dataset
```

## Cost Optimization

- **Use clustering** to reduce data scanned
- **Partition tables** by date
- **Use materialized views** for frequent queries
- **Implement partition filters**
- **Use on-demand pricing** for ad-hoc queries
- **Use capacity pricing** for predictable workloads

## Best Practices

1. **Partition tables** by date
2. **Cluster tables** by frequently filtered columns
3. **Use materialized views** for repeated queries
4. **Implement partition filters** to limit data scanned
5. **Use approximate aggregations** when possible
6. **Optimize SQL queries** for performance
7. **Use dry runs** to estimate costs
8. **Implement data retention** policies
9. **Use authorized views** for security
10. **Monitor with Information Schema**
