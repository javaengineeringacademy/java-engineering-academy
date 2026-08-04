# Kubeflow

## Overview
Kubeflow is a platform for deploying ML workflows on Kubernetes, enabling scalable and portable ML systems.

## Components
1. **Kubeflow Pipelines**: Workflow orchestration
2. **Katib**: Hyperparameter tuning
3. **KServe**: Model serving
4. **Training Operators**: Distributed training
5. **Notebooks**: Jupyter environments

## Pipeline Definition
```python
import kfp
from kfp import dsl

@dsl.pipeline(name='training-pipeline')
def training_pipeline():
    # Data preparation
    prepare = dsl.ContainerOp(
        name='prepare-data',
        image='my-registry/data-prep:latest'
    )

    # Training
    train = dsl.ContainerOp(
        name='train-model',
        image='my-registry/trainer:latest'
    ).after(prepare)

    # Evaluation
    evaluate = dsl.ContainerOp(
        name='evaluate-model',
        image='my-registry/evaluator:latest'
    ).after(train)

# Compile and run
client = kfp.Client()
client.create_run_from_pipeline_func(
    training_pipeline,
    arguments={'learning_rate': 0.01}
)
```

## KServe
```yaml
apiVersion: serving.kserve.io/v1beta1
kind: InferenceService
metadata:
  name: my-model
spec:
  predictor:
    model:
      modelFormat:
        name: sklearn
      storageUri: s3://models/my-model
      resources:
        requests:
          cpu: "1"
          memory: "2Gi"
        limits:
          cpu: "2"
          memory: "4Gi"
```

## Best Practices
1. Use pipelines for reproducibility
2. Enable experiment tracking
3. Use GPU nodes for training
4. Monitor resource usage
5. Use KServe for production serving
