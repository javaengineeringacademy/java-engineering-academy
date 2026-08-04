# Neural Networks

## Overview

Neural networks are computational models inspired by biological neural networks. They learn complex patterns through layers of interconnected neurons with adjustable weights.

## Perceptron

### Theory

The simplest neural network unit:

```
output = activation(Σ(wᵢ * xᵢ) + b)
```

### Implementation

```python
import numpy as np

class Perceptron:
    def __init__(self, learning_rate=0.01, n_iterations=100):
        self.lr = learning_rate
        self.n_iter = n_iterations
        self.weights = None
        self.bias = None
    
    def fit(self, X, y):
        n_samples, n_features = X.shape
        self.weights = np.zeros(n_features)
        self.bias = 0
        
        for _ in range(self.n_iter):
            for idx, x_i in enumerate(X):
                prediction = self.predict(x_i.reshape(1, -1))
                update = self.lr * (y[idx] - prediction)
                self.weights += update * x_i
                self.bias += update
    
    def predict(self, X):
        linear_output = np.dot(X, self.weights) + self.bias
        return np.where(linear_output >= 0, 1, 0)

# Usage
perceptron = Perceptron(learning_rate=0.1, n_iterations=100)
```

## Multi-Layer Perceptron (MLP)

### Theory

Multiple layers of neurons with non-linear activation functions:

```
Input Layer → Hidden Layer(s) → Output Layer
```

### Implementation with scikit-learn

```python
from sklearn.neural_network import MLPClassifier, MLPRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import Pipeline
from sklearn.datasets import load_digits
from sklearn.model_selection import train_test_split

# Load data
digits = load_digits()
X_train, X_test, y_train, y_test = train_test_split(
    digits.data, digits.target, test_size=0.2, random_state=42
)

# MLP Classifier
mlp = Pipeline([
    ('scaler', StandardScaler()),
    ('mlp', MLPClassifier(
        hidden_layer_sizes=(100, 50),
        activation='relu',
        solver='adam',
        max_iter=500,
        random_state=42
    ))
])

mlp.fit(X_train, y_train)
print(f"Accuracy: {mlp.score(X_test, y_test):.4f}")
```

### PyTorch Implementation

```python
import torch
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader, TensorDataset

# Define model
class MLP(nn.Module):
    def __init__(self, input_size, hidden_sizes, output_size):
        super().__init__()
        layers = []
        prev_size = input_size
        
        for hidden_size in hidden_sizes:
            layers.append(nn.Linear(prev_size, hidden_size))
            layers.append(nn.ReLU())
            layers.append(nn.Dropout(0.2))
            prev_size = hidden_size
        
        layers.append(nn.Linear(prev_size, output_size))
        self.network = nn.Sequential(*layers)
    
    def forward(self, x):
        return self.network(x)

# Create model
model = MLP(input_size=64, hidden_sizes=[128, 64], output_size=10)
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)
```

## Activation Functions

### Common Functions

```python
import numpy as np
import matplotlib.pyplot as plt

x = np.linspace(-5, 5, 100)

# Sigmoid
def sigmoid(x):
    return 1 / (1 + np.exp(-x))

# Tanh
def tanh(x):
    return np.tanh(x)

# ReLU
def relu(x):
    return np.maximum(0, x)

# Leaky ReLU
def leaky_relu(x, alpha=0.01):
    return np.where(x > 0, x, alpha * x)

# ELU
def elu(x, alpha=1.0):
    return np.where(x > 0, x, alpha * (np.exp(x) - 1))

# GELU
def gelu(x):
    return 0.5 * x * (1 + np.tanh(np.sqrt(2/np.pi) * (x + 0.044715 * x**3)))

# Plot
fig, axes = plt.subplots(2, 3, figsize=(15, 10))
activations = [
    ('Sigmoid', sigmoid), ('Tanh', tanh), ('ReLU', relu),
    ('Leaky ReLU', leaky_relu), ('ELU', elu), ('GELU', gelu)
]

for ax, (name, func) in zip(axes.flatten(), activations):
    ax.plot(x, func(x))
    ax.set_title(name)
    ax.grid(True)
    ax.axhline(y=0, color='k', linestyle='--')
    ax.axvline(x=0, color='k', linestyle='--')

plt.tight_layout()
plt.show()
```

## Loss Functions

### Classification
```python
# Binary Cross-Entropy
bce_loss = nn.BCELoss()
bce_loss_logits = nn.BCEWithLogitsLoss()

# Cross-Entropy (for multi-class)
ce_loss = nn.CrossEntropyLoss()

# Example
logits = torch.randn(3, 5)  # batch_size=3, num_classes=5
targets = torch.tensor([1, 0, 4])
loss = ce_loss(logits, targets)
```

### Regression
```python
# Mean Squared Error
mse_loss = nn.MSELoss()

# Mean Absolute Error
mae_loss = nn.L1Loss()

# Huber Loss (robust to outliers)
huber_loss = nn.SmoothL1Loss()
```

## Optimizers

### Common Optimizers

```python
# SGD
optimizer_sgd = optim.SGD(model.parameters(), lr=0.01, momentum=0.9)

# Adam
optimizer_adam = optim.Adam(model.parameters(), lr=0.001, betas=(0.9, 0.999))

# AdamW (with weight decay)
optimizer_adamw = optim.AdamW(model.parameters(), lr=0.001, weight_decay=0.01)

# Learning rate scheduling
scheduler = optim.lr_scheduler.StepLR(optimizer_adam, step_size=10, gamma=0.1)
scheduler_cosine = optim.lr_scheduler.CosineAnnealingLR(optimizer_adam, T_max=50)
```

## Training Loop

```python
def train_model(model, train_loader, val_loader, epochs=50):
    history = {'train_loss': [], 'val_loss': [], 'train_acc': [], 'val_acc': []}
    
    for epoch in range(epochs):
        # Training
        model.train()
        train_loss = 0
        correct = 0
        total = 0
        
        for X_batch, y_batch in train_loader:
            optimizer.zero_grad()
            outputs = model(X_batch)
            loss = criterion(outputs, y_batch)
            loss.backward()
            optimizer.step()
            
            train_loss += loss.item()
            _, predicted = outputs.max(1)
            total += y_batch.size(0)
            correct += predicted.eq(y_batch).sum().item()
        
        # Validation
        model.eval()
        val_loss = 0
        val_correct = 0
        val_total = 0
        
        with torch.no_grad():
            for X_batch, y_batch in val_loader:
                outputs = model(X_batch)
                loss = criterion(outputs, y_batch)
                val_loss += loss.item()
                _, predicted = outputs.max(1)
                val_total += y_batch.size(0)
                val_correct += predicted.eq(y_batch).sum().item()
        
        # Record history
        history['train_loss'].append(train_loss / len(train_loader))
        history['val_loss'].append(val_loss / len(val_loader))
        history['train_acc'].append(correct / total)
        history['val_acc'].append(val_correct / val_total)
        
        if (epoch + 1) % 10 == 0:
            print(f"Epoch {epoch+1}/{epochs}")
            print(f"  Train Loss: {history['train_loss'][-1]:.4f}, Acc: {history['train_acc'][-1]:.4f}")
            print(f"  Val Loss: {history['val_loss'][-1]:.4f}, Acc: {history['val_acc'][-1]:.4f}")
    
    return history
```

## Regularization Techniques

### Dropout
```python
# In model definition
self.dropout = nn.Dropout(p=0.5)  # 50% dropout

# In forward pass
x = self.dropout(x)  # Only during training
```

### Batch Normalization
```python
self.bn = nn.BatchNorm1d(num_features=64)
x = self.bn(x)
```

### Weight Decay
```python
optimizer = optim.Adam(model.parameters(), lr=0.001, weight_decay=1e-4)
```

## Best Practices

1. **Start simple**: Increase complexity gradually
2. **Normalize inputs**: Standardize features
3. **Use dropout**: Prevent overfitting
4. **Monitor loss curves**: Detect overfitting
5. **Use learning rate scheduling**: Fine-tune training
6. **Experiment with architectures**: Different depths and widths

## Further Reading

- "Deep Learning" by Goodfellow et al.
- PyTorch documentation
- Neural Networks and Deep Learning (online book)
