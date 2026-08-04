# K-Nearest Neighbors (KNN)

## Overview

KNN is a simple, instance-based learning algorithm that classifies new points based on the majority class among K nearest neighbors. It is a non-parametric, lazy learning method.

## Theory

### Algorithm
```
1. Store all training samples
2. For a new point x:
   a. Calculate distances to all training points
   b. Find K closest points
   c. Assign majority class (classification) or average (regression)
```

### Distance Metrics

#### Euclidean Distance
```
d(x, y) = √(Σ(xᵢ - yᵢ)²)
```

#### Manhattan Distance
```
d(x, y) = Σ|xᵢ - yᵢ|
```

#### Minkowski Distance
```
d(x, y) = (Σ|xᵢ - yᵢ|^p)^(1/p)
```

#### Cosine Similarity
```
cos(x, y) = (x · y) / (||x|| * ||y||)
```

## Implementation

```python
from sklearn.neighbors import KNeighborsClassifier, KNeighborsRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import Pipeline
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import accuracy_score
import numpy as np

# Load data
iris = load_iris()
X_train, X_test, y_train, y_test = train_test_split(
    iris.data, iris.target, test_size=0.2, random_state=42
)

# KNN with scaling
knn = Pipeline([
    ('scaler', StandardScaler()),
    ('knn', KNeighborsClassifier(n_neighbors=5, weights='uniform'))
])

knn.fit(X_train, y_train)
y_pred = knn.predict(X_test)
print(f"Accuracy: {accuracy_score(y_test, y_pred):.4f}")
```

## Choosing K

### Finding Optimal K
```python
import matplotlib.pyplot as plt

k_range = range(1, 31)
k_scores = []

for k in k_range:
    knn = Pipeline([
        ('scaler', StandardScaler()),
        ('knn', KNeighborsClassifier(n_neighbors=k))
    ])
    scores = cross_val_score(knn, X_train, y_train, cv=5, scoring='accuracy')
    k_scores.append(scores.mean())

plt.figure(figsize=(10, 6))
plt.plot(k_range, k_scores, marker='o')
plt.xlabel('K')
plt.ylabel('Accuracy')
plt.title('K vs Accuracy')
plt.grid(True)
plt.show()

optimal_k = k_range[np.argmax(k_scores)]
print(f"Optimal K: {optimal_k}")
```

## Weighted KNN

```python
# Uniform weights
knn_uniform = KNeighborsClassifier(n_neighbors=5, weights='uniform')

# Distance weights (closer neighbors have more influence)
knn_distance = KNeighborsClassifier(n_neighbors=5, weights='distance')

# Compare
for weights in ['uniform', 'distance']:
    knn = Pipeline([
        ('scaler', StandardScaler()),
        ('knn', KNeighborsClassifier(n_neighbors=5, weights=weights))
    ])
    knn.fit(X_train, y_train)
    score = knn.score(X_test, y_test)
    print(f"{weights:10s}: Accuracy = {score:.4f}")
```

## KNN for Regression

```python
from sklearn.neighbors import KNeighborsRegressor
from sklearn.metrics import mean_squared_error

# Generate regression data
np.random.seed(42)
X_reg = np.sort(5 * np.random.rand(200, 1), axis=0)
y_reg = np.sin(X_reg).ravel() + np.random.randn(200) * 0.1

X_train_r, X_test_r, y_train_r, y_test_r = train_test_split(
    X_reg, y_reg, test_size=0.2, random_state=42
)

knn_reg = Pipeline([
    ('scaler', StandardScaler()),
    ('knn', KNeighborsRegressor(n_neighbors=5, weights='distance'))
])

knn_reg.fit(X_train_r, y_train_r)
y_pred_r = knn_reg.predict(X_test_r)
rmse = np.sqrt(mean_squared_error(y_test_r, y_pred_r))
print(f"RMSE: {rmse:.4f}")
```

## Ball Tree and KD Tree

```python
# Ball Tree (better for high dimensions)
knn_ball = KNeighborsClassifier(n_neighbors=5, algorithm='ball_tree')

# KD Tree (better for low dimensions)
knn_kd = KNeighborsClassifier(n_neighbors=5, algorithm='kd_tree')

# Brute force (simple, for small datasets)
knn_brute = KNeighborsClassifier(n_neighbors=5, algorithm='brute')

# Auto (best algorithm selection)
knn_auto = KNeighborsClassifier(n_neighbors=5, algorithm='auto')
```

## Advantages and Disadvantages

### Advantages
- Simple and intuitive
- No training phase
- Naturally handles multi-class
- Works with any distance metric

### Disadvantages
- Slow prediction (O(n) per query)
- Sensitive to irrelevant features
- Sensitive to feature scaling
- Struggles with high dimensions (curse of dimensionality)

## Best Practices

1. **Always scale features**: KNN is distance-based
2. **Choose odd K**: Avoids ties in binary classification
3. **Use cross-validation**: Find optimal K
4. **Consider dimensionality reduction**: For high-dimensional data
5. **Use weighted KNN**: Closer neighbors are more relevant

## Further Reading

- "Introduction to K-Nearest Neighbors" in scikit-learn docs
- "Pattern Recognition and Machine Learning" by Bishop
