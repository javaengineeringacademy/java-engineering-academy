# Apache Airflow

## Overview

Apache Airflow is an open-source workflow orchestration platform for programmatically authoring, scheduling, and monitoring data pipelines. It uses Python to define workflows as Directed Acyclic Graphs (DAGs).

## Table of Contents

- [Architecture](#architecture)
- [Core Concepts](#core-concepts)
- [DAGs and Tasks](#dags-and-tasks)
- [Operators](#operators)
- [Hooks and Connections](#hooks-and-connections)
- [Sensors](#sensors)
- [XCom](#xcom)
- [Best Practices](#best-practices)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    AIRFLOW ARCHITECTURE                       │
├─────────────────────────────────────────────────────────────┤
│  Web Server          │  Scheduler                            │
│  • DAG editor        │  • Parses DAGs                        │
│  • Monitoring UI     │  • Schedules tasks                    │
│  • History           │  • Manages dependencies               │
├─────────────────────────────────────────────────────────────┤
│  Executor                                                │
│  • SequentialExecutor (testing)                            │
│  • LocalExecutor (small scale)                             │
│  • CeleryExecutor (distributed)                            │
│  • KubernetesExecutor (cloud-native)                       │
├─────────────────────────────────────────────────────────────┤
│  Metadata Database (PostgreSQL, MySQL)                     │
│  • DAG definitions                                         │
│  • Task states                                             │
│  • History and logs                                        │
├─────────────────────────────────────────────────────────────┤
│  Workers (Celery/Kubernetes)                               │
│  • Execute tasks                                           │
│  • Report status                                           │
└─────────────────────────────────────────────────────────────┘
```

## Core Concepts

### DAG (Directed Acyclic Graph)

```python
from airflow import DAG
from datetime import datetime

default_args = {
    'owner': 'data-team',
    'depends_on_past': False,
    'start_date': datetime(2024, 1, 1),
    'retries': 3,
    'retry_delay': timedelta(minutes=5),
    'email_on_failure': True,
    'email': ['alerts@company.com']
}

with DAG(
    'daily_etl',
    default_args=default_args,
    description='Daily ETL pipeline',
    schedule_interval='@daily',
    catchup=False,
    tags=['etl', 'production']
) as dag:
    # Tasks defined here
    pass
```

### Task Dependencies

```python
# Define task dependencies
extract >> transform >> load

# Multiple dependencies
extract >> [transform_a, transform_b] >> merge >> load

# Complex dependencies
extract >> validate
validate >> [process_a, process_b]
[process_a, process_b] >> aggregate
aggregate >> load
```

## DAGs and Tasks

### Complete DAG Example

```python
from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.operators.bash import BashOperator
from airflow.operators.empty import EmptyOperator
from airflow.utils.task_group import TaskGroup
from datetime import datetime, timedelta

default_args = {
    'owner': 'data-team',
    'depends_on_past': False,
    'start_date': datetime(2024, 1, 1),
    'retries': 2,
    'retry_delay': timedelta(minutes=5),
}

def extract_orders(**context):
    """Extract orders from source system"""
    import pandas as pd
    from sqlalchemy import create_engine
    
    engine = create_engine('postgresql://host/erp')
    df = pd.read_sql("SELECT * FROM orders WHERE date = CURRENT_DATE", engine)
    
    # Save to staging
    df.to_parquet('/tmp/orders.parquet')
    
    # Push to XCom
    context['ti'].xcom_push(key='row_count', value=len(df))
    return len(df)

def transform_orders(**context):
    """Transform orders"""
    import pandas as pd
    
    df = pd.read_parquet('/tmp/orders.parquet')
    
    # Business logic
    df['total_amount'] = df['quantity'] * df['unit_price']
    df['category'] = df['product_id'].map(get_category_mapping())
    
    df.to_parquet('/tmp/orders_transformed.parquet')

def load_orders(**context):
    """Load to data warehouse"""
    import pandas as pd
    from sqlalchemy import create_engine
    
    df = pd.read_parquet('/tmp/orders_transformed.parquet')
    engine = create_engine('snowflake://account/db/schema')
    
    df.to_sql('fact_orders', engine, if_exists='append', index=False)

# Define DAG
with DAG(
    'order_etl',
    default_args=default_args,
    description='Order ETL pipeline',
    schedule_interval='0 2 * * *',  # Daily at 2 AM
    catchup=False
) as dag:

    start = EmptyOperator(task_id='start')
    
    with TaskGroup('extract') as extract_group:
        extract_orders = PythonOperator(
            task_id='extract_orders',
            python_callable=extract_orders
        )
        
        validate_extract = PythonOperator(
            task_id='validate_extract',
            python_callable=lambda: print("Validation passed")
        )
        
        extract_orders >> validate_extract
    
    transform = PythonOperator(
        task_id='transform_orders',
        python_callable=transform_orders
    )
    
    load = PythonOperator(
        task_id='load_orders',
        python_callable=load_orders
    )
    
    end = EmptyOperator(task_id='end')
    
    start >> extract_group >> transform >> load >> end
```

## Operators

### PythonOperator

```python
from airflow.operators.python import PythonOperator

def my_python_function(param1, param2):
    print(f"Processing {param1} and {param2}")
    return "result"

task = PythonOperator(
    task_id='python_task',
    python_callable=my_python_function,
    op_kwargs={'param1': 'value1', 'param2': 'value2'},
    provide_context=True
)
```

### BashOperator

```python
from airflow.operators.bash import BashOperator

# Simple bash command
task = BashOperator(
    task_id='bash_task',
    bash_command='echo "Hello World"'
)

# With variables
task = BashOperator(
    task_id='process_file',
    bash_command='python /scripts/process.py {{ params.filename }}',
    params={'filename': 'data.csv'}
)
```

### SQL Operators

```python
from airflow.providers.common.sql.operators.sql import SQLExecuteQueryOperator

# Execute SQL
task = SQLExecuteQueryOperator(
    task_id='run_query',
    conn_id='warehouse_connection',
    sql="""
        INSERT INTO target_table
        SELECT * FROM staging_table
        WHERE date = '{{ ds }}'
    """
)
```

### EmailOperator

```python
from airflow.operators.email import EmailOperator

task = EmailOperator(
    task_id='send_email',
    to=['team@company.com'],
    subject='Pipeline Complete',
    html_content='{{ task_instance }} completed successfully'
)
```

### DockerOperator

```python
from airflow.providers.docker.operators.docker import DockerOperator

task = DockerOperator(
    task_id='docker_task',
    image='my-app:latest',
    command='python process.py',
    docker_url='unix://var/run/docker.sock',
    network_mode='bridge'
)
```

## Hooks and Connections

### Hooks

```python
from airflow.providers.postgres.hooks.postgres import PostgresHook
from airflow.providers.amazon.aws.hooks.s3 import S3Hook

# PostgreSQL hook
hook = PostgresHook(postgres_conn_id='postgres_default')
df = hook.get_pandas_df("SELECT * FROM orders")

# S3 hook
s3_hook = S3Hook(aws_conn_id='aws_default')
s3_hook.load_file(
    filename='/tmp/data.csv',
    key='data/orders.csv',
    bucket_name='my-bucket'
)
```

### Connections

```python
# Define connections in Airflow UI or code
from airflow.models import Connection

# PostgreSQL connection
conn = Connection(
    conn_id='postgres_warehouse',
    conn_type='postgres',
    host='warehouse.example.com',
    schema='analytics',
    login='user',
    password='password',
    port=5432
)

# S3 connection
conn = Connection(
    conn_id='s3_data_lake',
    conn_type='aws',
    extra={
        'aws_access_key_id': 'AKIA...',
        'aws_secret_access_key': 'secret'
    }
)
```

## Sensors

### FileSensor

```python
from airflow.sensors.filesystem import FileSensor

task = FileSensor(
    task_id='wait_for_file',
    filepath='/data/incoming/{{ ds }}/data.csv',
    poke_interval=60,
    timeout=3600
)
```

### ExternalTaskSensor

```python
from airflow.sensors.external_task import ExternalTaskSensor

task = ExternalTaskSensor(
    task_id='wait_for_upstream',
    external_dag_id='upstream_dag',
    external_task_id='final_task',
    mode='reschedule'
)
```

### HttpSensor

```python
from airflow.sensors.http_sensor import HttpSensor

task = HttpSensor(
    task_id='wait_for_api',
    http_conn_id='my_api',
    endpoint='/api/status',
    response_check=lambda response: response.json()['status'] == 'ready',
    poke_interval=30
)
```

## XCom

### Push Values

```python
def push_data(**context):
    """Push data to XCom"""
    context['ti'].xcom_push(key='result', value={'count': 100})
    context['ti'].xcom_push(key='status', value='success')
```

### Pull Values

```python
def pull_data(**context):
    """Pull data from XCom"""
    result = context['ti'].xcom_pull(
        task_ids='push_task',
        key='result'
    )
    print(f"Result: {result}")
```

### Template Usage

```python
# Use XCom in templates
task = PythonOperator(
    task_id='use_xcom',
    python_callable=lambda **ctx: print(
        ctx['ti'].xcom_pull(task_ids='other_task', key='result')
    )
)
```

## Best Practices

### 1. DAG Design

```python
# Good: Small, focused DAGs
with DAG('daily_orders_etl') as dag:
    extract >> transform >> load

# Bad: Large, monolithic DAG
with DAG('everything_etl') as dag:
    # 100+ tasks in one DAG
    pass
```

### 2. Task Design

```python
# Good: Idempotent tasks
def process_data(**context):
    ds = context['ds']  # Execution date
    # Process only data for this date
    process_date(ds)

# Good: Handle failures gracefully
def process_data(**context):
    try:
        process()
    except Exception as e:
        # Log error and alert
        log.error(f"Processing failed: {e}")
        raise
```

### 3. Performance

```python
# Use TaskGroup for related tasks
with TaskGroup('extraction') as extract_group:
    extract_orders = PythonOperator(...)
    extract_customers = PythonOperator(...)
    extract_products = PythonOperator(...)

# Parallel execution
[extract_orders, extract_customers, extract_products] >> transform
```

### 4. Testing

```python
# Test tasks independently
def test_extract_orders():
    result = extract_orders()
    assert result > 0

# Test DAG structure
def test_dag_structure():
    from airflow.models import DagBag
    dag_bag = DagBag(dag_folder='/dags', include_examples=False)
    dag = dag_bag.get_dag('daily_etl')
    assert len(dag.tasks) > 0
```

### 5. Monitoring

```python
# Alert on failures
default_args = {
    'email_on_failure': True,
    'email': ['alerts@company.com'],
    'on_failure_callback': alert_on_failure
}

def alert_on_failure(context):
    """Custom failure handler"""
    send_alert(
        f"Task {context['task_instance'].task_id} failed",
        severity='critical'
    )
```

## Further Reading

- [Apache Airflow Documentation](https://airflow.apache.org/docs/)
- [Airflow Best Practices](https://airflow.apache.org/docs/apache-airflow/stable/best-practices.html)
- [Airflow Provider Packages](https://airflow.apache.org/docs/apache-airflow-providers/)
