# Apache Airflow

Apache Airflow is a workflow orchestration platform for programmatically authoring, scheduling, and monitoring data pipelines. It uses Directed Acyclic Graphs (DAGs) to manage workflow dependencies.

## Table of Contents

1. [DAGs](#dags)
2. [Operators](#operators)
3. [Sensors](#sensors)
4. [Hooks](#hooks)
5. [Connections and Pools](#connections-and-pools)
6. [XCom](#xcom)
7. [Task Lifecycle](#task-lifecycle)
8. [Scheduling and Backfill](#scheduling-and-backfill)
9. [Kubernetes Executor](#kubernetes-executor)
10. [Best Practices](#best-practices)

---

## DAGs

A DAG (Directed Acyclic Graph) defines the workflow structure:

### DAG Definition

```python
from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime, timedelta

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime(2024, 1, 1),
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

dag = DAG(
    'my_pipeline',
    default_args=default_args,
    description='Example DAG',
    schedule_interval='@daily',
    catchup=False,
)

# Define tasks
task1 = PythonOperator(
    task_id='extract',
    python_callable=extract_data,
    dag=dag,
)

task2 = PythonOperator(
    task_id='transform',
    python_callable=transform_data,
    dag=dag,
)

task3 = PythonOperator(
    task_id='load',
    python_callable=load_data,
    dag=dag,
)

# Set task dependencies
task1 >> task2 >> task3
```

### DAG Properties

- **dag_id**: Unique identifier
- **schedule_interval**: Cron expression or preset
- **start_date**: When DAG starts
- **end_date**: When DAG stops (optional)
- **catchup**: Whether to backfill missed runs
- **max_active_runs**: Maximum concurrent runs
- **concurrency**: Maximum tasks across all runs

### Schedule Intervals

```python
# Presets
schedule_interval='@hourly'    # Every hour
schedule_interval='@daily'     # Every day at midnight
schedule_interval='@weekly'    # Every Sunday
schedule_interval='@monthly'   # First of month

# Cron expressions
schedule_interval='0 * * * *'          # Every hour
schedule_interval='0 0 * * *'          # Daily at midnight
schedule_interval='0 0 * * 0'          # Weekly on Sunday
schedule_interval='0 0 1 * *'          # Monthly on 1st
```

---

## Operators

Operators define the work to be done:

### BashOperator

```python
from airflow.operators.bash import BashOperator

task = BashOperator(
    task_id='run_script',
    bash_command='python /path/to/script.py',
    dag=dag,
)
```

### PythonOperator

```python
from airflow.operators.python import PythonOperator

def my_function(**context):
    # Access context
    execution_date = context['execution_date']
    # Do work
    return result

task = PythonOperator(
    task_id='python_task',
    python_callable=my_function,
    provide_context=True,
    dag=dag,
)
```

### EmailOperator

```python
from airflow.operators.email import EmailOperator

task = EmailOperator(
    task_id='send_email',
    to='team@example.com',
    subject='Pipeline Complete',
    html_content='<h1>Success!</h1>',
    dag=dag,
)
```

### DummyOperator

```python
from airflow.operators.dummy import DummyOperator

task = DummyOperator(
    task_id='noop',
    dag=dag,
)
```

### ExternalTaskOperator

```python
from airflow.operators.external_task import ExternalTaskOperator

task = ExternalTaskOperator(
    task_id='wait_for_other_dag',
    external_dag_id='other_pipeline',
    external_task_id='final_task',
    dag=dag,
)
```

### Custom Operators

```python
from airflow.models import BaseOperator
from airflow.utils.decorators import apply_defaults

class MyCustomOperator(BaseOperator):
    
    @apply_defaults
    def __init__(self, my_param, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.my_param = my_param
    
    def execute(self, context):
        self.log.info(f"Running with param: {self.my_param}")
        # Do work
        return result
```

---

## Sensors

Sensors wait for a condition to be met:

### FileSensor

```python
from airflow.sensors.filesystem import FileSensor

task = FileSensor(
    task_id='wait_for_file',
    filepath='/data/input/file.csv',
    poke_interval=30,
    timeout=3600,
    dag=dag,
)
```

### HttpSensor

```python
from airflow.sensors.http_sensor import HttpSensor

task = HttpSensor(
    task_id='wait_for_api',
    http_conn_id='my_api',
    endpoint='/status',
    response_check=lambda response: response.json()['status'] == 'ready',
    poke_interval=10,
    dag=dag,
)
```

### ExternalTaskSensor

```python
from airflow.sensors.external_task import ExternalTaskSensor

task = ExternalTaskSensor(
    task_id='wait_for_task',
    external_dag_id='other_pipeline',
    external_task_id='extract_task',
    dag=dag,
)
```

### SqlSensor

```python
from airflow.sensors.sql import SqlSensor

task = SqlSensor(
    task_id='wait_for_data',
    conn_id='my_db',
    sql='SELECT COUNT(*) FROM new_data WHERE date = %s',
    params={'date': '{{ ds }}'},
    dag=dag,
)
```

### Custom Sensors

```python
from airflow.sensors.base import BaseSensorOperator
from airflow.utils.decorators import apply_defaults

class MyCustomSensor(BaseSensorOperator):
    
    @apply_defaults
    def __init__(self, my_param, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.my_param = my_param
    
    def poke(self, context):
        # Return True if condition met, False to keep waiting
        return check_condition()
```

---

## Hooks

Hooks manage connections to external systems:

### Database Hooks

```python
from airflow.providers.postgres.hooks.postgres import PostgresHook
from airflow.providers.mysql.hooks.mysql import MySqlHook

# Postgres
hook = PostgresHook(postgres_conn_id='my_postgres')
conn = hook.get_conn()
cursor = conn.cursor()
cursor.execute("SELECT * FROM users")

# MySQL
hook = MySqlHook(mysql_conn_id='my_mysql')
df = hook.get_pandas_df("SELECT * FROM users")
```

### HTTP Hooks

```python
from airflow.providers.http.hooks.http import HttpHook

hook = HttpHook(http_conn_id='my_api', method='GET')
response = hook.run(endpoint='/users')
```

### AWS Hooks

```python
from airflow.providers.amazon.aws.hooks.s3 import S3Hook
from airflow.providers.amazon.aws.hooks.redshift_sql import RedshiftSQLHook

# S3
hook = S3Hook(aws_conn_id='my_aws')
hook.load_file(filename='local.csv', key='s3://bucket/file.csv')

# Redshift
hook = RedshiftSQLHook(redshift_conn_id='my_redshift')
hook.run("COPY ... FROM S3 ...")
```

### Custom Hooks

```python
from airflow.hooks.base import BaseHook

class MyCustomHook(BaseHook):
    
    def __init__(self, my_conn_id):
        self.my_conn_id = my_conn_id
        self.connection = None
    
    def get_conn(self):
        if self.connection is None:
            conn_config = self.get_connection(self.my_conn_id)
            self.connection = create_connection(conn_config)
        return self.connection
    
    def run(self, query):
        conn = self.get_conn()
        return execute_query(conn, query)
```

---

## Connections and Pools

### Connections

Store credentials and configuration:

```python
from airflow.models import Connection

# Via UI or CLI
# airflow connections add my_db --conn-type postgres \
#   --conn-host localhost --conn-schema mydb \
#   --conn-login user --conn-password pass

# In code
from airflow.hooks.base import BaseHook

conn = BaseHook.get_connection('my_db')
host = conn.host
port = conn.port
```

### Connection Types

- **postgres**: PostgreSQL
- **mysql**: MySQL
- **amazon_web_services**: AWS
- **http**: HTTP/HTTPS
- **ssh**: SSH
- **google_cloud_platform**: GCP

### Pools

Control concurrency of tasks:

```python
from airflow.models import Pool

# Via UI or CLI
# airflow pools set my_pool 10 'My pool description'

# In code
from airflow import settings

session = settings.Session()
pool = Pool(pool='my_pool', slots=10, description='My pool')
session.add(pool)
session.commit()
```

### Pool Usage

```python
task = PythonOperator(
    task_id='task_with_pool',
    python_callable=my_function,
    pool='my_pool',
    pool_slots=2,  # Use 2 slots from pool
    dag=dag,
)
```

---

## XCom

XCom (cross-communication) passes data between tasks:

### Pushing XCom

```python
def push_function(**context):
    result = {'key': 'value', 'count': 42}
    context['ti'].xcom_push(key='my_result', value=result)
    return result  # Also pushes automatically
```

### Pulling XCom

```python
def pull_function(**context):
    # From same DAG run
    result = context['ti'].xcom_pull(
        task_ids='push_task',
        key='my_result'
    )
    
    # From specific DAG run
    result = context['ti'].xcom_pull(
        task_ids='push_task',
        dag_id='other_dag',
        execution_date=context['execution_date']
    )
```

### XCom Patterns

```python
# Producer task
def produce(**context):
    data = fetch_large_data()
    context['ti'].xcom_push(key='data', value=data)

# Consumer task
def consume(**context):
    data = context['ti'].xcom_pull(
        task_ids='producer',
        key='data'
    )
    process(data)

# Chain tasks
producer >> consumer
```

### XCom Limitations

- Data stored in metadata database
- Not suitable for large data
- Default backend limits size
- Consider S3/GCS for large payloads

---

## Task Lifecycle

### Task States

```
┌─────────────┐
│  queued     │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ running     │
└──────┬──────┘
       │
       ├──────────────────┐
       ▼                  ▼
┌─────────────┐    ┌─────────────┐
│ success     │    │ failed      │
└─────────────┘    └──────┬──────┘
                          │
                          ▼
                   ┌─────────────┐
                   │ up_for_retry│
                   └──────┬──────┘
                          │
                          ▼
                   ┌─────────────┐
                   │ running     │
                   └─────────────┘
```

### Task Callbacks

```python
task = PythonOperator(
    task_id='task_with_callbacks',
    python_callable=my_function,
    on_success_callback=handle_success,
    on_failure_callback=handle_failure,
    on_retry_callback=handle_retry,
    on_execute_callback=handle_execute,
    dag=dag,
)
```

### Retry Mechanism

```python
task = PythonOperator(
    task_id='retry_task',
    python_callable=my_function,
    retries=3,
    retry_delay=timedelta(minutes=5),
    retry_exponential_backoff=True,
    max_retry_delay=timedelta(hours=1),
    dag=dag,
)
```

### Task Timeout

```python
task = PythonOperator(
    task_id='timeout_task',
    python_callable=my_function,
    execution_timeout=timedelta(hours=2),
    dag=dag,
)
```

---

## Scheduling and Backfill

### Schedule Interval

```python
# Cron expression
schedule_interval='0 0 * * *'  # Daily

# Presets
schedule_interval='@hourly'
schedule_interval='@daily'
schedule_interval='@weekly'
```

### Backfill

Run historical DAG runs:

```bash
# Backfill specific date range
airflow dags backfill my_dag \
    --start-date 2024-01-01 \
    --end-date 2024-01-31

# Backfill with specific parameters
airflow dags backfill my_dag \
    --start-date 2024-01-01 \
    --end-date 2024-01-31 \
    --conf '{"key": "value"}'
```

### Catchup

```python
dag = DAG(
    'my_dag',
    catchup=True,  # Run historical
    # OR
    catchup=False,  # Skip historical
)
```

### Manual Runs

```bash
# Trigger DAG manually
airflow dags trigger my_dag

# Trigger with config
airflow dags trigger my_dag --conf '{"key": "value"}'
```

---

## Kubernetes Executor

### Configuration

```python
# airflow.cfg
[core]
executor = KubernetesExecutor

[kubernetes]
namespace = airflow
worker_container_repository = apache/airflow
worker_container_tag = latest
delete_worker_pods = True
delete_worker_pods_on_failure = False
```

### Task Configuration

```python
from airflow.providers.cncf.kubernetes.operators.kubernetes_pod import (
    KubernetesPodOperator
)

task = KubernetesPodOperator(
    task_id='k8s_task',
    name='my-task',
    namespace='airflow',
    image='python:3.9',
    cmds=['python'],
    arguments=['-c', 'print("Hello from K8s!")'],
    labels={'app': 'my-pipeline'},
    env_vars={'ENV': 'production'},
    resources={
        'request_memory': '128Mi',
        'request_cpu': '100m',
        'limit_memory': '256Mi',
        'limit_cpu': '250m',
    },
    dag=dag,
)
```

### Executor Config

```python
from airflow.kubernetes.volume import Volume
from airflow.kubernetes.volume_mount import VolumeMount

volume_mount = VolumeMount(
    name='data-volume',
    mount_path='/data',
    sub_path=None,
    read_only=False
)

volume = Volume(
    name='data-volume',
    configs={
        'persistentVolumeClaim': {
            'claimName': 'data-pvc'
        }
    }
)

task = PythonOperator(
    task_id='task_with_volume',
    python_callable=my_function,
    executor_config={
        'KubernetesExecutor': {
            'volumes': [volume],
            'volume_mounts': [volume_mount],
            'node_selector': {'disktype': 'ssd'},
            'tolerations': [
                {
                    'key': 'dedicated',
                    'operator': 'Equal',
                    'value': 'data',
                    'effect': 'NoSchedule'
                }
            ],
        }
    },
    dag=dag,
)
```

---

## Best Practices

### DAG Design

1. Keep DAGs idempotent
2. Use meaningful task IDs
3. Set appropriate retry policies
4. Use templates for dynamic values
5. Avoid heavy computation in DAG file

### Task Design

1. Make tasks atomic
2. Handle failures gracefully
3. Use hooks for external systems
4. Log meaningful messages
5. Return data via XCom when needed

### Performance

1. Use sensors with appropriate timeouts
2. Limit parallel tasks with pools
3. Use appropriate executor
4. Monitor resource usage
5. Clean up old data

### Security

1. Use connections for credentials
2. Never hardcode passwords
3. Use variables for sensitive data
4. Implement access controls
5. Audit task execution

### Testing

1. Test tasks independently
2. Use test connections
3. Mock external systems
4. Test with production data samples
5. Monitor test coverage

---

## Further Reading

- [Airflow Documentation](https://airflow.apache.org/)
- [Airflow Best Practices](https://airflow.apache.org/docs/apache-airflow/stable/best-practices.html)
- [Airflow Providers](https://airflow.apache.org/docs/apache-airflow-providers/)
