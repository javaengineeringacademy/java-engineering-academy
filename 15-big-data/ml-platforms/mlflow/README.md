# MLflow

## Overview
MLflow is an open-source platform for managing the end-to-end machine learning lifecycle: experimentation, reproducibility, and deployment.

## Components
1. **MLflow Tracking**: Log experiments, parameters, metrics, artifacts
2. **MLflow Models**: Package models for deployment
3. **MLflow Model Registry**: Version and stage models
4. **MLflow Projects**: Reproducible ML projects

## Tracking
```python
import mlflow

mlflow.set_experiment("my-experiment")

with mlflow.start_run():
    # Log parameters
    mlflow.log_param("learning_rate", 0.01)
    mlflow.log_param("n_estimators", 100)

    # Train model
    model = train_model(X_train, y_train)

    # Log metrics
    mlflow.log_metric("accuracy", 0.95)
    mlflow.log_metric("f1", 0.93)

    # Log model
    mlflow.sklearn.log_model(model, "model")

    # Log artifacts
    mlflow.log_artifact("confusion_matrix.png")
```

## Model Registry
```python
# Register model
mlflow.register_model(
    "runs:/run_id/model",
    "my-model"
)

# Transition stage
client = mlflow.tracking.MlflowClient()
client.transition_model_version_stage(
    name="my-model",
    version=1,
    stage="production"
)
```

## Serving
```bash
# Serve model
mlflow models serve -m "models:/my-model/production" -p 5001

# Call API
curl -X POST http://localhost:5001/invocations   -H "Content-Type: application/json"   -d '{"inputs": [[1.0, 2.0, 3.0]]}'
```

## Best Practices
1. Log all experiments for reproducibility
2. Use model registry for version control
3. Tag runs with meaningful metadata
4. Use MLflow Projects for reproducibility
5. Monitor model performance in production
