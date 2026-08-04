# Experiment Tracking

## Overview

Experiment tracking records and manages ML experiments, including parameters, metrics, artifacts, and code versions.

## MLflow

### Implementation

```python
import mlflow
import mlflow.sklearn
from sklearn.ensemble import RandomForestClassifier
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# Load data
X, y = load_iris(return_X_y=True)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# Set experiment
mlflow.set_experiment("iris_classification")

# Start run
with mlflow.start_run():
    # Parameters
    params = {"n_estimators": 100, "max_depth": 5, "random_state": 42}
    
    # Train model
    model = RandomForestClassifier(**params)
    model.fit(X_train, y_train)
    
    # Predictions
    y_pred = model.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)
    
    # Log parameters, metrics, model
    mlflow.log_params(params)
    mlflow.log_metric("accuracy", accuracy)
    mlflow.sklearn.log_model(model, "model")
    
    print(f"Accuracy: {accuracy:.4f}")
```

### MLflow Tracking UI

```bash
# Start MLflow UI
mlflow ui --port 5000
# Open http://localhost:5000
```

### Comparing Runs

```python
import mlflow.tracking

client = mlflow.tracking.MlflowClient()
experiment = client.get_experiment_by_name("iris_classification")

runs = client.search_runs(
    experiment_ids=[experiment.experiment_id],
    order_by=["metrics.accuracy DESC"],
    max_results=10
)

for run in runs:
    print(f"Run {run.info.run_id}: accuracy={run.data.metrics['accuracy']:.4f}")
```

---

## Weights & Biases (W&B)

### Implementation

```python
import wandb
from sklearn.ensemble import RandomForestClassifier
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# Initialize wandb
wandb.init(project="iris-classification", config={"n_estimators": 100})

# Load data
X, y = load_iris(return_X_y=True)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# Train model
model = RandomForestClassifier(n_estimators=wandb.config.n_estimators)
model.fit(X_train, y_train)

# Evaluate
y_pred = model.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)

# Log metrics
wandb.log({"accuracy": accuracy})

# Log model
wandb.sklog.sklearn.log_model(model, "model")

# Finish run
wandb.finish()
```

### W&B sweeps

```python
sweep_config = {
    "method": "grid",
    "metric": {"name": "accuracy", "goal": "maximize"},
    "parameters": {
        "n_estimators": {"values": [50, 100, 200]},
        "max_depth": {"values": [5, 10, 20]}
    }
}

sweep_id = wandb.sweep(sweep_config, project="iris-classification")

def train():
    wandb.init()
    model = RandomForestClassifier(
        n_estimators=wandb.config.n_estimators,
        max_depth=wandb.config.max_depth
    )
    model.fit(X_train, y_train)
    accuracy = accuracy_score(y_test, model.predict(X_test))
    wandb.log({"accuracy": accuracy})

wandb.agent(sweep_id, function=train)
```

---

## TensorBoard

### Implementation

```python
import torch
from torch.utils.tensorboard import SummaryWriter
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
import numpy as np

# Create writer
writer = SummaryWriter('runs/iris_experiment')

# Load data
X, y = load_iris(return_X_y=True)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# Convert to tensors
X_train_t = torch.FloatTensor(X_train)
y_train_t = torch.LongTensor(y_train)

# Simple model
model = torch.nn.Linear(4, 3)
criterion = torch.nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

# Training loop
for epoch in range(100):
    optimizer.zero_grad()
    outputs = model(X_train_t)
    loss = criterion(outputs, y_train_t)
    loss.backward()
    optimizer.step()
    
    # Log to TensorBoard
    writer.add_scalar('training/loss', loss.item(), epoch)

writer.close()

# Launch TensorBoard
# tensorboard --logdir=runs
```

---

## Comparison

| Tool | Features | Integration | Free Tier |
|------|----------|-------------|-----------|
| MLflow | Open-source, local/cloud | scikit-learn, PyTorch, TF | Yes |
| W&B | Collaborative, rich viz | All frameworks | Yes (limited) |
| TensorBoard | Visualization, profiling | PyTorch, TF | Yes |

## Best Practices

1. **Log everything**: Parameters, metrics, artifacts
2. **Use consistent naming**:便于comparison
3. **Version control**: Link to git commits
4. **Artifact storage**: Models, data, configs
5. **Reproducibility**: Set random seeds

## Further Reading

- MLflow documentation
- Weights & Biases documentation
- TensorBoard documentation
