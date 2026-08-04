# Feature Stores

## Overview
A feature store is a centralized repository for storing, managing, and serving machine learning features at scale.

## Key Features
- Feature versioning
- Point-in-time correctness
- Online/offline serving
- Feature sharing across teams
- Feature monitoring

## Architecture
```
Feature Store:
  Offline Store (Data Lake/Warehouse):
    - Training data
    - Historical features
    - Batch serving
  
  Online Store (Redis/DynamoDB):
    - Low-latency serving
    - Real-time features
  
  Feature Registry:
    - Feature metadata
    - Schema definitions
    - Version management
```

## Feature Definition
```python
from feast import Feature, ValueType, Entity
from feast.data_source import KafkaSource
from feast.data_format import AvroFormat

# Define entity
driver = Entity(
    name="driver_id",
    value_type=ValueType.INT64,
    description="Driver identifier"
)

# Define feature view
driver_stats = FeatureView(
    name="driver_stats",
    entities=["driver_id"],
    ttl=timedelta(days=1),
    features=[
        Feature(name="lifetime_trips", value_type=ValueType.INT64),
        Feature(name="average_rating", value_type=ValueType.FLOAT),
    ],
    online=True,
    batch_source=BigQuerySource(table="driver_stats"),
    stream_source=KafkaSource(
        kafka_bootstrap_servers="kafka:9092",
        topic="driver_stats",
        message_format=AvroFormat(schema_json=schema)
    )
)

# Materialize features
store.materialize(start_date, end_date)

# Get features for training
training_df = store.get_historical_features(
    entity_df=entity_df,
    features=["driver_stats:lifetime_trips", "driver_stats:average_rating"]
).to_df()

# Get features for serving
feature_vector = store.get_online_features(
    features=["driver_stats:lifetime_trips", "driver_stats:average_rating"],
    entity_rows=[{"driver_id": 123}]
).to_dict()
```

## Popular Feature Stores
- **Feast**: Open-source, works with any ML framework
- **Tecton**: Managed, real-time features
- **Hopsworks**: Full MLOps platform
- **Databricks Feature Store**: Integrated with Databricks

## Best Practices
1. Define features as code for version control
2. Use point-in-time correctness for training
3. Monitor feature distributions
4. Share features across teams
5. Use online store for low-latency serving
