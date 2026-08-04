# Dagster

Dagster is a modern, Python-native data orchestration platform designed for building, running, and monitoring data pipelines. It treats pipelines as software, applying software engineering best practices like versioning, testing, and modularity to data workflows.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
  - [Software-Defined Assets](#software-defined-assets)
  - [Ops](#ops)
  - [Jobs](#jobs)
  - [Resources](#resources)
  - [IO Managers](#io-managers)
  - [Schedules](#schedules)
  - [Sensors](#sensors)
  - [Graphs](#graphs)
- [Dagster UI (Dagit)](#dagster-ui-dagit)
- [Partitioning and Backfill](#partitioning-and-backfill)
- [Testing in Dagster](#testing-in-dagster)
- [Dagster vs Apache Airflow](#dagster-vs-apache-airflow)
- [Installation and Setup](#installation-and-setup)
- [Example: End-to-End Pipeline](#example-end-to-end-pipeline)
- [Best Practices](#best-practices)

---

## Overview

Dagster was created to address common pain points in data engineering: fragile pipelines, lack of testability, poor visibility into pipeline health, and tight coupling between orchestration logic and infrastructure. It provides a unified abstraction layer that separates the logical definition of data transformations from their physical execution.

**Key Design Principles:**

- **Software engineering mindset**: Pipelines are code, not configuration files
- **Lazy evaluation**: Assets are materialized on demand, not on a schedule
- **Testability first**: Every component can be unit-tested in isolation
- **Separation of concerns**: Business logic, infrastructure, and orchestration are decoupled
- **Type-safe I/O**: Strong typing between assets and operations

---

## Core Concepts

### Software-Defined Assets

Assets are the central abstraction in Dagster. An asset represents a logical unit of data—a table, file, model, or any dataset that has a name and can be materialized.

```python
from dagster import asset, AssetExecutionContext
import pandas as pd

@asset
def raw_sales_data(context: AssetExecutionContext) -> pd.DataFrame:
    """Raw sales data ingested from the source system."""
    df = pd.read_csv("s3://data-lake/raw/sales.csv")
    context.log.info(f"Loaded {len(df)} rows")
    return df

@asset
deps=[raw_sales_data]
def cleaned_sales_data(raw_sales_data: pd.DataFrame) -> pd.DataFrame:
    """Cleaned sales data with nulls removed and types cast."""
    df = raw_sales_data.dropna()
    df["amount"] = df["amount"].astype(float)
    return df

@asset
deps=[cleaned_sales_data]
def daily_sales_summary(cleaned_sales_data: pd.DataFrame) -> pd.DataFrame:
    """Aggregated daily sales summary."""
    return (
        cleaned_sales_data
        .groupby("sale_date")
        .agg(total_amount=("amount", "sum"), total_orders=("order_id", "count"))
        .reset_index()
    )
```

**Key features of assets:**

- **Dependency graph**: Dagster automatically infers the DAG from asset dependencies
- **Incremental materialization**: Only recompute assets that have changed or are outdated
- **Asset observations**: Track metadata about assets without materializing them
- **Asset checks**: Define data quality checks that run after materialization

```python
from dagster import asset, AssetCheckResult, asset_check

@asset_check(asset=daily_sales_summary)
def check_no_negative_amounts(daily_sales_summary):
    has_negatives = (daily_sales_summary["total_amount"] < 0).any()
    return AssetCheckResult(
        passed=not has_negatives,
        metadata={"negative_count": int(daily_sales_summary["total_amount"].lt(0).sum())}
    )
```

### Ops

Ops are the low-level building blocks for executing computations. They are functionally similar to tasks in other orchestration tools but are designed to be composable and testable.

```python
from dagster import op, OpExecutionContext, In, Out
import pandas as pd

@op(
    ins={"file_path": In(str)},
    out=Out(pd.DataFrame),
    config_schema={"encoding": str}
)
def load_csv(context: OpExecutionContext, file_path: str) -> pd.DataFrame:
    encoding = context.op_config.get("encoding", "utf-8")
    return pd.read_csv(file_path, encoding=encoding)

@op(
    ins={"df": In(pd.DataFrame)},
    out=Out(pd.DataFrame)
)
def filter_valid_rows(context: OpExecutionContext, df: pd.DataFrame) -> pd.DataFrame:
    valid = df[df["status"].isin(["active", "pending"])]
    context.log.info(f"Filtered to {len(valid)} valid rows")
    return valid
```

### Jobs

Jobs are collections of ops wired together. They represent executable pipelines.

```python
from dagster import job, op, In, Out

@op
def extract():
    return {"users": 1000, "orders": 5000}

@op
def transform(data: dict) -> dict:
    data["conversion_rate"] = data["orders"] / data["users"]
    return data

@op
def load(data: dict):
    print(f"Loaded: {data}")

@job
def etl_pipeline():
    data = extract()
    transformed = transform(data)
    load(transformed)
```

### Resources

Resources provide external dependencies to ops and assets, making pipelines configurable and testable.

```python
from dagster import resource, op, OpExecutionContext
import boto3

@resource(config_schema={"region": str})
def s3_client(context) -> boto3.client:
    return boto3.client("s3", region_name=context.resource_config["region"])

@resource
def database_connection():
    return create_engine("postgresql://user:pass@localhost/mydb")

@op(required_resource_keys={"s3_client"})
def upload_to_s3(context: OpExecutionContext, data: bytes):
    context.resources.s3_client.put_object(
        Bucket="my-bucket",
        Key="output/data.parquet",
        Body=data
    )
```

### IO Managers

IO managers handle the serialization and deserialization of assets between operations, abstracting away storage details.

```python
from dagster import IOManager, io_manager, InputContext, OutputContext
import pandas as pd
import pyarrow.parquet as pq

class ParquetIOManager(IOManager):
    def __init__(self, base_path: str):
        self.base_path = base_path

    def handle_output(self, context: OutputContext, obj: pd.DataFrame):
        path = f"{self.base_path}/{context.asset_key.path[-1]}.parquet"
        obj.to_parquet(path)
        context.log.info(f"Wrote asset to {path}")

    def load_input(self, context: InputContext) -> pd.DataFrame:
        path = f"{self.base_path}/{context.asset_key.path[-1]}.parquet"
        return pd.read_parquet(path)

@io_manager(config_schema={"base_path": str})
def parquet_io_manager(context) -> ParquetIOManager:
    return ParquetIOManager(context.resource_config["base_path"])
```

### Schedules

Schedules trigger jobs at fixed time intervals using standard cron expressions.

```python
from dagster import schedule, ScheduleEvaluationContext

@schedule(cron_schedule="0 6 * * *", job=etl_pipeline)
def daily_etl_schedule(context: ScheduleEvaluationContext):
    return {}
```

### Sensors

Sensors are event-driven triggers that poll external systems and launch runs when conditions are met.

```python
from dagster import sensor, SensorEvaluationContext, RunRequest

@sensor(job=etl_pipeline, minimum_interval_seconds=300)
def new_data_sensor(context: SensorEvaluationContext):
    last_count = int(context.cursor or 0)
    current_count = get_new_file_count()

    if current_count > last_count:
        context.update_cursor(str(current_count))
        return RunRequest(run_key=f"data_batch_{current_count}")
    return None
```

---

## Dagster UI (Dagit)

Dagster provides a rich web-based UI called Dagit that offers:

- **Asset graph visualization**: Interactive DAG showing all assets and their dependencies
- **Run history**: Detailed execution logs with timing, errors, and retry information
- **Asset materialization timeline**: Track when assets were last updated
- **Launchpad**: Manually trigger runs with custom configurations
- **Global search**: Search across runs, assets, jobs, and schedules
- **Resource management**: View and configure resources in one place
- **Real-time monitoring**: Live view of running executions

Launch the UI with:

```bash
dagit -f my_pipeline.py
# or for a project
dagster dev -m my_project.definitions
```

---

## Partitioning and Backfill

Partitioning allows you to divide your assets into manageable chunks, typically by time or category.

```python
from dagster import (
    asset, DailyPartitionsDefinition, PartitionKeyRange,
    materialize, define_asset_job
)
from datetime import date

daily_partitions = DailyPartitionsDefinition(start_date="2024-01-01")

@asset(partitions_def=daily_partitions)
def daily_revenue(context):
    partition_date = context.partition_key
    context.log.info(f"Processing partition: {partition_date}")
    # Query data for this specific date
    return query_revenue_for_date(partition_date)

daily_job = define_asset_job(
    name="daily_revenue_job",
    selection=[daily_revenue],
    partitions_def=daily_partitions
)
```

**Backfilling** lets you reprocess historical partitions:

```python
# Backfill a range of dates
result = materialize(
    [daily_revenue],
    partition_key_range=PartitionKeyRange("2024-01-01", "2024-01-31"),
    resources={"io_manager": s3_io_manager}
)
```

**Partition types available:**

| Type | Description | Use Case |
|------|-------------|----------|
| `DailyPartitionsDefinition` | Day-level partitions | Daily ETL jobs |
| `HourlyPartitionsDefinition` | Hour-level partitions | Streaming aggregations |
| `WeeklyPartitionsDefinition` | Week-level partitions | Weekly reports |
| `MonthlyPartitionsDefinition` | Month-level partitions | Monthly rollups |
| `StaticPartitionsDefinition` | Custom partition keys | Region-based data |
| `MultiPartitionsDefinition` | Combinatorial partitions | Date + region |
| `TimeWindowPartitionsDefinition` | Custom time windows | Flexible scheduling |

---

## Testing in Dagster

Dagster treats testing as a first-class concern. Every component can be tested in isolation without launching a full pipeline.

**Asset testing:**

```python
import pandas as pd
from dagster import materialize, AssetKey

def test_daily_sales_summary():
    sample_data = pd.DataFrame({
        "sale_date": ["2024-01-01", "2024-01-01", "2024-01-02"],
        "amount": [100.0, 200.0, 150.0],
        "order_id": [1, 2, 3]
    })

    result = materialize(
        [raw_sales_data, cleaned_sales_data, daily_sales_summary],
        resources={"io_manager": in_memory_io_manager},
        assets=[daily_sales_summary],
        run_config={
            "ops": {"raw_sales_data": {"config": {"input_df": sample_data.to_dict()}}}
        }
    )
    assert result.success
    summary = result.output_for_node("daily_sales_summary")
    assert len(summary) == 2
    assert summary.iloc[0]["total_amount"] == 300.0
```

**Op testing:**

```python
from dagster import build_op_context

def test_filter_valid_rows():
    context = build_op_context()
    input_df = pd.DataFrame({
        "status": ["active", "inactive", "pending", "active"],
        "value": [1, 2, 3, 4]
    })
    result = filter_valid_rows(context, input_df)
    assert len(result) == 2
    assert list(result["status"]) == ["active", "pending"]
```

**Resource testing:**

```python
from dagster import build_resource_context

def test_s3_client():
    context = build_resource_context(config={"region": "us-east-1"})
    client = s3_client(context)
    assert client.meta.region_name == "us-east-1"
```

---

## Dagster vs Apache Airflow

| Feature | Dagster | Apache Airflow |
|---------|---------|----------------|
| **Primary abstraction** | Assets (data-centric) | DAGs (task-centric) |
| **Language** | Python-native | Python with Airflow DSL |
| **Configuration** | Strongly typed (Pydantic) | Dictionary-based |
| **Testing** | First-class unit testing | Limited; often needs test infra |
| **UI** | Dagit (asset-focused) | Airflow UI (DAG-focused) |
| **Backfill** | Built-in partition backfill | Manual backfill commands |
| **Asset awareness** | Native asset lineage | Requires plugins |
| **Execution model** | Lazy/on-demand | Eager/scheduled |
| **Error handling** | Fine-grained retries | Task-level retries |
| **Community** | Growing, enterprise adoption | Large, mature ecosystem |
| **Learning curve** | Moderate | Moderate-High |
| **Deployment** | Docker/K8s native | Multiple executors |

**When to choose Dagster:**

- Data pipelines are the primary focus (not just task orchestration)
- You want strong typing and testability
- Asset lineage and data quality monitoring are important
- You prefer Python-native abstractions over YAML/XML

**When to choose Airflow:**

- You need a large ecosystem of pre-built operators
- Your workflows are primarily task-based, not data-based
- Your team is already familiar with Airflow
- You need extensive third-party integrations out of the box

---

## Installation and Setup

```bash
# Install Dagster and Dagit
pip install dagster dagit

# Create a new project
dagster new-project my_dagster_project
cd my_dagster_project

# Install dependencies
pip install -e ".[dev]"

# Start the Dagster dev server
dagster dev

# Run a specific job
dagster job execute -f my_pipeline.py -j etl_pipeline

# Run with configuration
dagster job execute -f my_pipeline.py -j etl_pipeline --config run_config.yaml
```

---

## Example: End-to-End Pipeline

```python
from dagster import (
    Definitions, asset, ResourceDefinition,
    DailyPartitionsDefinition, define_asset_job,
    ScheduleDefinition, in_io_manager
)

# Define partitions
daily_partitions = DailyPartitionsDefinition(start_date="2024-01-01")

# Define resources
@ResourceDefinition
def warehouse_connection():
    return create_connection("warehouse.db")

# Define assets
@asset(partitions_def=daily_partitions)
def raw_events(context):
    date = context.partition_key
    return fetch_events(date)

@asset(deps=[raw_events])
def processed_events(raw_events):
    return raw_events.drop_duplicates(subset=["event_id"])

@asset(deps=[processed_events], partitions_def=daily_partitions)
def event_metrics(processed_events):
    return processed_events.groupby("event_type").size().to_dict()

# Define jobs
daily_metrics_job = define_asset_job(
    name="daily_metrics",
    selection="*",
    partitions_def=daily_partitions
)

# Define schedules
daily_schedule = ScheduleDefinition(
    job=daily_metrics_job,
    cron_schedule="0 7 * * *"
)

# Assemble definitions
defs = Definitions(
    assets=[raw_events, processed_events, event_metrics],
    jobs=[daily_metrics_job],
    schedules=[daily_schedule],
    resources={
        "warehouse": warehouse_connection,
        "io_manager": in_io_manager
    }
)
```

---

## Best Practices

1. **Model your data as assets**: Use asset-centric design for data pipelines; save ops for complex non-data computations
2. **Keep assets pure**: Assets should be deterministic given their inputs; avoid side effects
3. **Use IO managers for storage abstraction**: Swap between local and cloud storage without changing asset logic
4. **Partition large assets**: Avoid materializing entire datasets when only a subset changed
5. **Write tests for every asset**: Dagster makes this easy—take advantage of it
6. **Use resources for external dependencies**: Never hardcode connections, credentials, or infrastructure details
7. **Leverage asset checks**: Define data quality assertions that run automatically
8. **Use sensors for event-driven workflows**: Don't rely solely on schedules when data arrives unpredictably
9. **Monitor with Dagit**: Use the asset health view to catch issues early
10. **Version your pipelines**: Treat pipeline code with the same rigor as application code

---

## Additional Resources

- [Dagster Documentation](https://docs.dagster.io/)
- [Dagster GitHub](https://github.com/dagster-io/dagster)
- [Dagster Examples](https://github.com/dagster-io/dagster/tree/master/examples)
- [Dagster Cloud](https://dagster.io/product/cloud)
- [Dagster University](https://courses.dagster.io/)
