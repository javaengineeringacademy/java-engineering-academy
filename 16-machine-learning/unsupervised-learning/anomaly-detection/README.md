# Anomaly Detection

## Overview

Anomaly detection identifies rare items, events, or observations that differ significantly from the majority of the data.

## Isolation Forest

### Theory

Isolates anomalies by random recursive partitioning:
- Anomalies are easier to isolate (fewer splits)
- Uses path length as anomaly score

### Implementation

```python
from sklearn.ensemble import IsolationForest
from sklearn.datasets import make_blobs
import numpy as np
import matplotlib.pyplot as plt

# Generate normal data
np.random.seed(42)
X_normal = np.random.randn(300, 2)

# Add anomalies
X_anomaly = np.random.uniform(low=-6, high=6, size=(20, 2))
X = np.vstack([X_normal, X_anomaly])

# Isolation Forest
iso_forest = IsolationForest(contamination=0.06, random_state=42)
y_pred = iso_forest.fit_predict(X)

# Plot
plt.figure(figsize=(10, 8))
plt.scatter(X[y_pred == 1, 0], X[y_pred == 1, 1], c='blue', label='Normal')
plt.scatter(X[y_pred == -1, 0], X[y_pred == -1, 1], c='red', label='Anomaly')
plt.legend()
plt.title('Isolation Forest')
plt.show()
```

### Anomaly Scores

```python
# Get anomaly scores
scores = iso_forest.decision_function(X)
# More negative = more anomalous

# Score histogram
plt.figure(figsize=(10, 6))
plt.hist(scores, bins=50)
plt.xlabel('Anomaly Score')
plt.ylabel('Frequency')
plt.title('Anomaly Score Distribution')
plt.axvline(x=0, color='r', linestyle='--')
plt.show()
```

---

## Local Outlier Factor (LOF)

### Theory

Detects anomalies based on local density deviation:
- Compares local density of point with neighbors
- Points in lower density regions are outliers

### Implementation

```python
from sklearn.neighbors import LocalOutlierFactor

# LOF
lof = LocalOutlierFactor(n_neighbors=20, contamination=0.06)
y_pred_lof = lof.fit_predict(X)

# Negative outlier factor
scores_lof = lof.negative_outlier_factor_

# Plot
plt.figure(figsize=(10, 8))
plt.scatter(X[y_pred_lof == 1, 0], X[y_pred_lof == 1, 1], c='blue', label='Normal')
plt.scatter(X[y_pred_lof == -1, 0], X[y_pred_lof == -1, 1], c='red', label='Anomaly')
plt.legend()
plt.title('Local Outlier Factor')
plt.show()
```

---

## One-Class SVM

### Theory

Learns a boundary around normal data:
- Maps data to higher dimensions
- Finds separating hyperplane

### Implementation

```python
from sklearn.svm import OneClassSVM

# One-Class SVM
oc_svm = OneClassSVM(kernel='rbf', gamma='auto', nu=0.06)
y_pred_svm = oc_svm.fit_predict(X)

# Plot
plt.figure(figsize=(10, 8))
plt.scatter(X[y_pred_svm == 1, 0], X[y_pred_svm == 1, 1], c='blue', label='Normal')
plt.scatter(X[y_pred_svm == -1, 0], X[y_pred_svm == -1, 1], c='red', label='Anomaly')
plt.legend()
plt.title('One-Class SVM')
plt.show()
```

---

## Elliptic Envelope

### Theory

Fits an ellipse to central data points:
- Assumes data is Gaussian
- Robust to outliers in training

### Implementation

```python
from sklearn.covariance import EllipticEnvelope

# Elliptic Envelope
ee = EllipticEnvelope(contamination=0.06, random_state=42)
y_pred_ee = ee.fit_predict(X)

# Plot
plt.figure(figsize=(10, 8))
plt.scatter(X[y_pred_ee == 1, 0], X[y_pred_ee == 1, 1], c='blue', label='Normal')
plt.scatter(X[y_pred_ee == -1, 0], X[y_pred_ee == -1, 1], c='red', label='Anomaly')
plt.legend()
plt.title('Elliptic Envelope')
plt.show()
```

---

## Autoencoders for Anomaly Detection

### Theory

Neural network that learns to reconstruct normal data:
- High reconstruction error = anomaly

### Implementation

```python
import torch
import torch.nn as nn

class Autoencoder(nn.Module):
    def __init__(self, input_dim, encoding_dim):
        super().__init__()
        self.encoder = nn.Sequential(
            nn.Linear(input_dim, 64),
            nn.ReLU(),
            nn.Linear(64, encoding_dim),
            nn.ReLU()
        )
        self.decoder = nn.Sequential(
            nn.Linear(encoding_dim, 64),
            nn.ReLU(),
            nn.Linear(64, input_dim)
        )
    
    def forward(self, x):
        encoded = self.encoder(x)
        decoded = self.decoder(encoded)
        return decoded

# Train autoencoder on normal data only
model = Autoencoder(input_dim=2, encoding_dim=8)
criterion = nn.MSELoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.001)

# Training loop
for epoch in range(100):
    X_tensor = torch.FloatTensor(X_normal)
    reconstructed = model(X_tensor)
    loss = criterion(reconstructed, X_tensor)
    optimizer.zero_grad()
    loss.backward()
    optimizer.step()

# Anomaly detection by threshold
with torch.no_grad():
    X_all_tensor = torch.FloatTensor(X)
    reconstructed = model(X_all_tensor)
    mse = torch.mean((X_all_tensor - reconstructed) ** 2, dim=1).numpy()

threshold = np.percentile(mse, 94)
y_pred_ae = np.where(mse > threshold, -1, 1)
```

---

## Comparison

| Method | Assumptions | Scalability | Interpretability |
|--------|-------------|-------------|------------------|
| Isolation Forest | None | High | Medium |
| LOF | Local density | Medium | Medium |
| One-Class SVM | None | Low | Low |
| Elliptic Envelope | Gaussian | High | High |
| Autoencoder | Data distribution | Medium | Low |

## Best Practices

1. **Choose based on data**: Isolation Forest for most cases
2. **Tune contamination**: Know expected anomaly rate
3. **Combine methods**: Ensemble for robust detection
4. **Feature scaling**: Important for distance-based methods
5. **Evaluate carefully**: Use precision-recall for imbalanced data

## Further Reading

- "Isolation Forest" by Liu et al.
- "Local Outlier Factor" by Breunig et al.
- Scikit-learn outlier detection docs
