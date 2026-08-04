# Data Lake Governance

## Overview

Data lake governance encompasses the policies, processes, and technologies that ensure data is properly managed, secured, and utilized across the organization. Effective governance enables data-driven decision-making while maintaining compliance and security.

## Table of Contents

- [Governance Framework](#governance-framework)
- [Data Catalog](#data-catalog)
- [Data Lineage](#data-lineage)
- [Data Quality](#data-quality)
- [Security & Access Control](#security--access-control)
- [Compliance](#compliance)
- [Implementation](#implementation)
- [Best Practices](#best-practices)

## Governance Framework

### Governance Pillars

```
┌─────────────────────────────────────────────────────────────┐
│                   DATA GOVERNANCE                            │
├─────────────────────────────────────────────────────────────┤
│  Discovery     │  Catalog    │  Lineage   │  Quality       │
│  • Search      │  • Metadata │  • Track   │  • Validate    │
│  • Browse      │  • Glossary │  • Impact  │  • Monitor     │
│  • Subscribe   │  • Tags     │  • Audit   │  • Alert       │
├─────────────────────────────────────────────────────────────┤
│  Security      │  Compliance │  Stewardship│  Operations   │
│  • Access      │  • GDPR     │  • Owners  │  • SLA         │
│  • Encryption  │  • HIPAA    │  • Review  │  • Lifecycle   │
│  • Masking     │  • SOC2     │  • Policy  │  • Cost        │
└─────────────────────────────────────────────────────────────┘
```

### Governance Operating Model

```yaml
governance_model:
  roles:
    data_steward:
      responsibilities:
        - Data quality definition
        - Business glossary maintenance
        - Data issue resolution
      reporting: "Business Units"
    
    data_custodian:
      responsibilities:
        - Technical implementation
        - Access management
        - Security controls
      reporting: "IT/Data Engineering"
    
    data_owner:
      responsibilities:
        - Data classification
        - Usage policies
        - Compliance oversight
      reporting: "Executive Leadership"

  processes:
    - data_classification
    - access_review
    - quality_monitoring
    - incident_response
    - audit_trail
```

## Data Catalog

### Metadata Schema

```python
# Data catalog metadata structure
catalog_metadata = {
    "asset": {
        "name": "customer_orders",
        "type": "table",
        "description": "Daily customer order transactions",
        "owner": "data-commerce-team",
        "domain": "commerce",
        "classification": "confidential",
        "tags": ["orders", "revenue", "customer"]
    },
    "schema": {
        "columns": [
            {
                "name": "order_id",
                "type": "bigint",
                "description": "Unique order identifier",
                "is_pii": False,
                "is_nullable": False
            },
            {
                "name": "customer_email",
                "type": "string",
                "description": "Customer contact email",
                "is_pii": True,
                "is_nullable": True
            }
        ]
    },
    "quality": {
        "completeness": 0.99,
        "accuracy": 0.98,
        "freshness_hours": 2
    },
    "lineage": {
        "upstream": ["raw_orders", "customer_master"],
        "downstream": ["order_analytics", "revenue_reports"]
    }
}
```

### Catalog Implementation

```python
# OpenMetadata catalog configuration
from openmetadata import OpenMetadata

# Initialize catalog
metadata = OpenMetadata(config={
    "api_endpoint": "http://metadata:8585/api",
    "auth_provider": "basic",
    "username": "admin",
    "password": "secret"
})

# Register table
metadata.create_or_update_table({
    "name": "customer_orders",
    "database": "commerce",
    "schema": "public",
    "columns": [
        {"name": "order_id", "type": "BIGINT"},
        {"name": "customer_id", "type": "VARCHAR"}
    ],
    "owner": "data-commerce-team",
    "tags": ["production", "critical"]
})
```

## Data Lineage

### Lineage Tracking

```python
# Track data lineage
lineage_tracker = {
    "pipeline": "daily_order_etl",
    "run_date": "2024-01-15",
    "lineage": {
        "sources": [
            {"name": "raw_orders", "type": "parquet", "location": "s3://raw/orders/"},
            {"name": "customer_dim", "type": "iceberg", "location": "iceberg://catalog/customers"}
        ],
        "transformations": [
            {"step": 1, "operation": "filter", "description": "Remove cancelled orders"},
            {"step": 2, "operation": "join", "description": "Enrich with customer data"},
            {"step": 3, "operation": "aggregate", "description": "Daily totals"}
        ],
        "targets": [
            {"name": "order_facts", "type": "delta", "location": "s3://lake/gold/order_facts/"}
        ]
    }
}
```

### Impact Analysis

```python
# Analyze impact of schema changes
def analyze_impact(column_name, table_name):
    """Find all downstream dependencies"""
    downstream = metadata.get_downstream_assets(table_name)
    
    impact_report = {
        "column": column_name,
        "table": table_name,
        "affected_assets": [],
        "affected_pipelines": [],
        "risk_level": "low"
    }
    
    for asset in downstream:
        if column_name in [col["name"] for col in asset["columns"]]:
            impact_report["affected_assets"].append(asset["name"])
            impact_report["risk_level"] = "high"
    
    return impact_report
```

## Data Quality

### Quality Framework

```python
# Data quality rules
quality_rules = {
    "completeness": [
        {"column": "order_id", "threshold": 1.0, "action": "alert"},
        {"column": "customer_id", "threshold": 0.99, "action": "block"}
    ],
    "uniqueness": [
        {"column": "order_id", "threshold": 1.0, "action": "alert"}
    ],
    "validity": [
        {"column": "order_date", "rule": "not_null", "action": "alert"},
        {"column": "amount", "rule": "positive", "action": "block"}
    ],
    "freshness": [
        {"table": "order_facts", "max_delay_hours": 2, "action": "alert"}
    ]
}
```

### Quality Monitoring

```python
# Great Expectations quality checks
import great_expectations as gx

# Create expectation suite
suite = gx.core.ExpectationSuite(expectation_suite_name="order_quality")

# Add expectations
suite.add_expectation(
    gx.expectations.ExpectColumnValuesToNotBeNull(column="order_id")
)
suite.add_expectation(
    gx.expectations.ExpectColumnValuesToBeUnique(column="order_id")
)
suite.add_expectation(
    gx.expectations.ExpectColumnValuesToBeBetween(
        column="amount", min_value=0, max_value=1000000
    )
)

# Run validation
result = context.run_checkpoint("order_quality_checkpoint")
```

## Security & Access Control

### Access Control Matrix

```yaml
access_control:
  roles:
    data_viewer:
      permissions:
        - read
      tables:
        - gold.*
        - silver.public_*
    
    data_analyst:
      permissions:
        - read
        - create_view
      tables:
        - gold.*
        - silver.*
    
    data_engineer:
      permissions:
        - read
        - write
        - create_table
        - drop_table
      tables:
        - bronze.*
        - silver.*
        - gold.*
    
    data_admin:
      permissions:
        - all
      tables:
        - "*"
```

### Row-Level Security

```python
# Implement row-level security
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("RLS").getOrCreate()

# Create security policy
spark.sql("""
    CREATE ROW FILTER policy order_region_filter
    FOR orders USING (region = current_user_region())
""")

# Apply to table
spark.sql("""
    ALTER TABLE orders SET ROW FILTER order_region_filter
""")
```

### Column-Level Security

```python
# Mask PII columns
def mask_email(email):
    """Mask email for non-privileged users"""
    if email:
        local, domain = email.split("@")
        masked_local = local[0] + "***"
        return f"{masked_local}@{domain}"
    return None

# Apply masking
from pyspark.sql.functions import udf

mask_udf = udf(mask_email)
masked_df = df.withColumn("email_masked", mask_udf(col("customer_email")))
```

## Compliance

### GDPR Compliance

```yaml
gdpr_compliance:
  data_inventory:
    - table: customer_pii
      classification: "pii"
      retention_days: 730
      lawful_basis: "consent"
  
  right_to_erasure:
    process: "automated_deletion"
    sla_days: 30
    verification: "audit_log"
  
  data_portability:
    format: "json"
    endpoint: "api.company.com/data-export"
    response_sla_hours: 72
```

### Audit Trail

```python
# Track all data access
audit_log = {
    "timestamp": "2024-01-15T10:30:00Z",
    "user": "analyst@company.com",
    "action": "read",
    "resource": "gold.customer_orders",
    "columns_accessed": ["order_id", "customer_id", "amount"],
    "query": "SELECT * FROM gold.customer_orders WHERE region = 'US'",
    "rows_returned": 15000
}
```

## Implementation

### Governance Pipeline

```python
# Governance automation pipeline
from airflow import DAG
from airflow.operators.python import PythonOperator

def run_quality_checks():
    """Run data quality checks"""
    pass

def update_catalog():
    """Update data catalog metadata"""
    pass

def audit_access():
    """Audit data access patterns"""
    pass

with DAG("data_governance", schedule_interval="@daily") as dag:
    quality = PythonOperator(
        task_id="quality_checks",
        python_callable=run_quality_checks
    )
    
    catalog = PythonOperator(
        task_id="update_catalog",
        python_callable=update_catalog
    )
    
    audit = PythonOperator(
        task_id="audit_access",
        python_callable=audit_access
    )
    
    quality >> catalog >> audit
```

## Best Practices

### 1. Automate Governance

```python
# Auto-classify new data
def auto_classify_columns(table_name):
    """Automatically classify columns based on patterns"""
    pii_patterns = {
        "email": r".*@.*\..*",
        "phone": r"\d{3}-\d{3}-\d{4}",
        "ssn": r"\d{3}-\d{2}-\d{4}"
    }
    
    # Scan columns and apply classifications
    for column in get_columns(table_name):
        for pii_type, pattern in pii_patterns.items():
            if matches_pattern(column.sample_data, pattern):
                add_classification(table_name, column.name, pii_type)
```

### 2. Monitor Governance Metrics

```python
governance_metrics = {
    "catalog_coverage": 0.85,  # % of assets cataloged
    "quality_score": 0.92,     # Overall quality score
    "access_compliance": 0.98, # % compliant access
    "lineage_coverage": 0.78,  # % of assets with lineage
    "avg_resolution_time": 4.5 # Hours to resolve issues
}
```

### 3. Implement Data Contracts

```python
# Define data contracts
data_contract = {
    "producer": "order-service",
    "consumer": "analytics-team",
    "schema_version": "2.1.0",
    "sla": {
        "freshness_hours": 1,
        "completeness": 0.99
    },
    "schema": {
        "order_id": {"type": "bigint", "required": True},
        "amount": {"type": "decimal", "required": True, "min": 0}
    }
}
```

## Further Reading

- [Data Governance Framework](https://www.dataversity.net/what-is-data-governance/)
- [OpenMetadata Documentation](https://docs.open-metadata.org/)
- [Great Expectations](https://greatexpectations.io/)
- [Apache Atlas](https://atlas.apache.org/)
