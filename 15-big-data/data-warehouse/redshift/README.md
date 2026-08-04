# Amazon Redshift

## Overview
Redshift is a fully managed cloud data warehouse optimized for analytics on large datasets.

## Architecture
- **Leader Node**: Query parsing and planning
- **Compute Nodes**: Parallel query execution
- **S3**: External data storage
- **VPC**: Network isolation

## Creating Tables
```sql
-- Cluster table
CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT GETDATE()
) DISTSTYLE KEY DISTKEY (id) SORTKEY (created_at);

-- External table (S3)
CREATE EXTERNAL TABLE events (
    id BIGINT,
    event_type VARCHAR(50),
    timestamp TIMESTAMP
) ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
LOCATION 's3://bucket/events/';
```

## Query Optimization
```sql
-- Use distribution keys
DISTSTYLE KEY DISTKEY (user_id)

-- Use sort keys
SORTKEY (created_at)

-- Use sort key on filters
SORTKEY(event_type, created_at)

-- Use materialized views
CREATE MATERIALIZED VIEW monthly_sales AS
SELECT date_trunc('month', sale_date) as month, sum(amount) as total
FROM sales GROUP BY 1;

-- Vacuum and analyze
VACUUM SORT ONLY users;
ANALYZE users;
```

## Best Practices
1. Choose DISTSTYLE based on join patterns
2. Use SORTKEY on frequently filtered columns
3. Enable automatic compression
4. Use workload management (WLM) queues
5. Monitor query performance with SVV_QUERY_INFO
