# Google Vertex AI

## Overview

Vertex AI is a unified ML platform for building, deploying, and scaling ML models.

## Core Components

```
┌─────────────────────────────────────────────────────────┐
│                   Vertex AI                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │Workbench │  │  Model   │  │  End-    │             │
│  │(Notebook)│  │ Registry │  │  points  │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │   Pipelines   │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Workbench (Notebooks)

```bash
# Create notebook instance
gcloud notebooks instances create my-notebook \
  --vm-image-project=deeplearning-platform-release \
  --vm-image-family=common-cpu-notebooks \
  --machine-type=n1-standard-4 \
  --location=us-central1-a

# Connect to instance
gcloud notebooks instances connect my-notebook \
  --location=us-central1-a
```

## Custom Training

### Custom Training Job
```python
from google.cloud import aiplatform

aiplatform.init(project='my-project', location='us-central1')

job = aiplatform.CustomTrainingJob(
    display_name='my-training-job',
    script_path='train.py',
    container_uri='gcr.io/my-project/my-training-container',
    requirements=['google-cloud-aiplatform'],
)

model = job.run(
    replica_count=1,
    machine_type='n1-standard-4',
    accelerator_type='NVIDIA_TESLA_T4',
    accelerator_count=1,
)
```

## Model Registry

```python
# Upload model
model = aiplatform.Model.upload(
    display_name='my-model',
    serving_container_image_uri='gcr.io/my-project/my-serving-container',
    artifact_uri='gs://my-bucket/model',
)

# Deploy model
endpoint = model.deploy(
    deployed_model_display_name='my-deployment',
    machine_type='n1-standard-4',
    min_replica_count=1,
    max_replica_count=10,
)
```

## Endpoints

```python
# Create endpoint
endpoint = aiplatform.Endpoint.create(
    display_name='my-endpoint',
)

# Deploy model to endpoint
endpoint.deploy(
    model=model,
    deployed_model_display_name='my-deployment',
    machine_type='n1-standard-4',
)

# Predict
prediction = endpoint.predict(
    instances=[['feature1', 'feature2', 'feature3']]
)
```

## AutoML

### AutoML Tables
```python
import google.cloud.aiplatform as aiplatform

# Create dataset
dataset = aiplatform.TabularDataset.create(
    display_name='my-dataset',
    bq_source='bq://my-project.my_dataset.my_table',
)

# Train model
job = aiplatform.AutoMLTabularTrainingJob(
    display_name='my-automl-job',
    optimization_prediction_type='regression',
    optimization_metric_name='r2_score',
)

model = job.run(
    dataset=dataset,
    target_column='target',
    training_fraction_split=0.8,
    validation_fraction_split=0.2,
)
```

### AutoML Vision
```python
dataset = aiplatform.ImageDataset.create(
    display_name='my-image-dataset',
    gcs_source='gs://my-bucket/images.csv',
)

job = aiplatform.AutoMLImageTrainingJob(
    display_name='my-image-job',
    multi_label_classification=False,
)

model = job.run(
    dataset=dataset,
    model_display_name='my-image-model',
)
```

### AutoML NLP
```python
dataset = aiplatform.TextDataset.create(
    display_name='my-text-dataset',
    gcs_source='gs://my-bucket/text.csv',
)

job = aiplatform.AutoMLTextTrainingJob(
    display_name='my-text-job',
    classification_type='singleLabel',
)

model = job.run(
    dataset=dataset,
    model_display_name='my-text-model',
)
```

## Pipelines

```python
from kfp import dsl

@dsl.pipeline(name='my-pipeline')
def pipeline():
    preprocess = preprocess_op()
    train = train_op(preprocess.outputs['output'])
    evaluate = evaluate_op(train.outputs['model'])

# Compile pipeline
from kfp.compiler import Compiler
Compiler().compile(pipeline, 'pipeline.json')

# Run pipeline
job = aiplatform.PipelineJob(
    display_name='my-pipeline-job',
    pipeline_root='gs://my-bucket/pipeline',
    template_path='pipeline.json',
)
job.run()
```

## Feature Store

```python
from google.cloud import aiplatform

# Create featurestore
featurestore = aiplatform.Featurestore.create(
    featurestore_id='my-featurestore',
    online_store_fixed_node_count=10,
)

# Create entity type
entity_type = featurestore.create_entity_type(
    entity_type_id='users',
    description='User features',
)

# Create feature
feature = entity_type.create_feature(
    feature_id='age',
    value_type=aiplatform.ValueType.INT64,
    description='User age',
)
```

## Model Monitoring

```python
# Enable model monitoring
endpoint = aiplatform.Endpoint('my-endpoint')

model_monitor = aiplatform.ModelDeploymentMonitoringJob.create(
    display_name='my-monitoring',
    endpoint=endpoint,
    logging_sampling_strategy={
        'random_sample_config': {'sample_rate': 0.1}
    },
    model_deployment_monitoring_objective_configs=[
        {
            'deployed_model_id': 'my-model',
            'objective_config': {
                'training_dataset': {
                    'bigquery_source': {
                        'input_uri': 'bq://my-project.my_dataset.my_table'
                    }
                },
                'training_prediction_skew_detection_config': {
                    'skew_thresholds': {
                        'feature1': {'value': 0.3},
                    }
                },
            }
        }
    ],
)
```

## Matching Engine

```python
# Create index
index = aiplatform.MatchingEngineIndex.create_tree_ah_index(
    display_name='my-index',
    contents_delta_uri='gs://my-bucket/index',
)

# Create index endpoint
index_endpoint = aiplatform.MatchingEngineIndexEndpoint.create(
    display_name='my-index-endpoint',
    public_endpoint_enabled=True,
)

# Deploy index
index_endpoint.deploy_index(
    index=index,
    deployed_index_id='my-deployed-index',
)

# Find neighbors
response = index_endpoint.find_neighbors(
    deployed_index_id='my-deployed-index',
    queries=[[0.1, 0.2, 0.3]],
    num_neighbors=10,
)
```

## Generative AI

```python
from google.cloud import aiplatform

# Use PaLM
model = aiplatform.TextGenerationModel.from_pretrained('google/text-bison@001')

response = model.predict(
    prompt='Write a poem about AI',
    max_output_tokens=1024,
    temperature=0.8,
)
```

## Monitoring

```python
# Get model metrics
model = aiplatform.Model('my-model')
metrics = model.get_model_artifact()

# Get endpoint metrics
endpoint = aiplatform.Endpoint('my-endpoint')
traffic_split = endpoint.traffic_split
```

## Best Practices

1. **Use Workbench** for experimentation
2. **Implement proper feature engineering**
3. **Use AutoML** for quick prototyping
4. **Implement model monitoring**
5. **Use Feature Store** for feature management
6. **Implement proper versioning**
7. **Use Pipelines** for reproducibility
8. **Monitor model performance**
9. **Implement proper security**
10. **Use batch prediction** for large datasets
