# Model Deployment

## Overview

Model deployment serves trained models in production, enabling real-time or batch predictions.

## TensorFlow Serving

### Implementation

```python
import tensorflow as tf

# Save model
model = tf.keras.Sequential([
    tf.keras.layers.Dense(64, activation='relu'),
    tf.keras.layers.Dense(10, activation='softmax')
])
model.compile(optimizer='adam', loss='sparse_categorical_crossentropy')

# Save in SavedModel format
model.save('models/my_model/1')

# Docker deployment
# docker run -p 8501:8501 \
#   --mount type=bind,source=/path/to/models,target=/models \
#   -e MODEL_NAME=my_model \
#   tensorflow/serving

# Client
import requests
import json

data = {"instances": [[1.0, 2.0, 3.0, 4.0]]}
response = requests.post('http://localhost:8501/v1/models/my_model:predict', json=data)
print(response.json())
```

---

## TorchServe

### Implementation

```python
import torch
import torch.nn as nn
from ts.torch_handler.image_classifier import ImageClassifier

# Save model
class MyModel(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc = nn.Linear(10, 2)
    
    def forward(self, x):
        return self.fc(x)

model = MyModel()
torch.save(model.state_dict(), 'model.pth')

# Create handler
# Create MAR file
# torch-model-archiver --model-name my_model \
#   --version 1.0 \
#   --model-file model.py \
#   --serialized-file model.pth \
#   --handler image_classifier

# Start TorchServe
# torchserve --start --model-store model_store --models my_model=my_model.mar
```

---

## Triton Inference Server

### Implementation

```python
# Model directory structure
# models/
#   my_model/
#     config.pbtxt
#     1/
#       model.onnx

# config.pbtxt
# name: "my_model"
# platform: "onnxruntime_onnx"
# input [
#   {
#     name: "input"
#     data_type: TYPE_FP32
#     dims: [10]
#   }
# ]
# output [
#   {
#     name: "output"
#     data_type: TYPE_FP32
#     dims: [2]
#   }
# ]

# Client
import tritonclient.grpc as grpcclient

client = grpcclient.InferenceServerClient(url='localhost:8001')
inputs = [grpcclient.InferInput('input', [1, 10], 'FP32')]
outputs = [grpcclient.InferRequestedOutput('output')]

result = client.infer('my_model', inputs, outputs=outputs)
print(result.as_numpy('output'))
```

---

## FastAPI Deployment

### Implementation

```python
from fastapi import FastAPI
from pydantic import BaseModel
import torch
import numpy as np

app = FastAPI()

class PredictionRequest(BaseModel):
    features: list

class PredictionResponse(BaseModel):
    prediction: int
    probability: float

# Load model
model = torch.load('model.pth')
model.eval()

@app.post("/predict", response_model=PredictionResponse)
def predict(request: PredictionRequest):
    with torch.no_grad():
        input_tensor = torch.FloatTensor(request.features).unsqueeze(0)
        output = model(input_tensor)
        probability = torch.softmax(output, dim=1)
        prediction = torch.argmax(probability, dim=1).item()
    
    return PredictionResponse(
        prediction=prediction,
        probability=probability[0][prediction].item()
    )

# Run with: uvicorn main:app --host 0.0.0.0 --port 8000
```

---

## Docker Deployment

```dockerfile
# Dockerfile
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

```yaml
# docker-compose.yml
version: '3.8'
services:
  model-api:
    build: .
    ports:
      - "8000:8000"
    environment:
      - MODEL_PATH=/models/my_model
    volumes:
      - ./models:/models
```

---

## Kubernetes Deployment

```yaml
# deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: model-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: model-api
  template:
    metadata:
      labels:
        app: model-api
    spec:
      containers:
      - name: model-api
        image: my-model-api:latest
        ports:
        - containerPort: 8000
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
```

---

## Best Practices

1. **Containerization**: Use Docker for consistency
2. **Health checks**: Implement /health endpoint
3. **Logging**: Structured logs for debugging
4. **Monitoring**: Track latency, throughput, errors
5. **A/B testing**: Gradual rollout of new models

## Further Reading

- TensorFlow Serving documentation
- TorchServe documentation
- Triton Inference Server documentation
- FastAPI documentation
