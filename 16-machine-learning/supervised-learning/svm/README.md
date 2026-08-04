# Support Vector Machines (SVM)

## Overview

SVM is a powerful supervised learning algorithm that finds the optimal hyperplane separating classes by maximizing the margin between them. It can handle linear and non-linear classification through kernel tricks.

## Linear SVM

### Theory

The goal is to find the hyperplane w^T * x + b = 0 that maximizes the margin between classes:

```
Margin = 2 / ||w||
```

### Optimization Problem
```
Minimize: 0.5 * ||w||²
Subject to: yᵢ(w^T * xᵢ + b) ≥ 1 for all i
```

### Support Vectors
- Points closest to the decision boundary
- Only support vectors determine the hyperplane
- Robust to outliers beyond the margin

### Implementation

```python
from sklearn.svm import SVC, LinearSVC
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import Pipeline
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
import numpy as np

# Generate data
X, y = make_classification(
    n_samples=500, n_features=2, n_classes=2,
    n_clusters_per_class=1, random_state=42
)

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# Linear SVM
linear_svm = Pipeline([
    ('scaler', StandardScaler()),
    ('svm', SVC(kernel='linear', C=1.0))
])
linear_svm.fit(X_train, y_train)

# Support vectors
print(f"Number of support vectors: {linear_svm.named_steps['svm'].n_support_}")
print(f"Support vector indices: {linear_svm.named_steps['svm'].support_}")

# Decision function
decision = linear_svm.decision_function(X_test)
```

---

## Soft Margin SVM

### Theory

Allows some misclassifications for better generalization:

```
Minimize: 0.5 * ||w||² + C * Σξᵢ
Subject to: yᵢ(w^T * xᵢ + b) ≥ 1 - ξᵢ
           ξᵢ ≥ 0
```

- **C**: Regularization parameter
  - Small C → wider margin, more misclassifications
  - Large C → narrower margin, fewer misclassifications

### Implementation

```python
# Different C values
for C in [0.01, 0.1, 1.0, 10.0, 100.0]:
    svm = Pipeline([
        ('scaler', StandardScaler()),
        ('svm', SVC(kernel='linear', C=C))
    ])
    svm.fit(X_train, y_train)
    score = svm.score(X_test, y_test)
    n_sv = svm.named_steps['svm'].n_support_.sum()
    print(f"C={C:6.2f}: Accuracy={score:.4f}, Support Vectors={n_sv}")
```

---

## Kernel SVM

### Theory

Maps data to higher dimensions where it becomes linearly separable:

```
φ: Rⁿ → Rᵐ (m > n)
K(xᵢ, xⱼ) = φ(xᵢ)^T * φ(xⱼ)
```

### Kernel Types

#### Linear Kernel
```
K(x, y) = x^T * y
```

#### Polynomial Kernel
```
K(x, y) = (γ * x^T * y + r)^d
- γ: Kernel coefficient
- r: Independent term
- d: Degree
```

#### RBF (Gaussian) Kernel
```
K(x, y) = exp(-γ * ||x - y||²)
- γ: Controls reach of single training example
- Large γ → complex boundary (overfitting)
- Small γ → smooth boundary (underfitting)
```

#### Sigmoid Kernel
```
K(x, y) = tanh(γ * x^T * y + r)
```

### Implementation

```python
from sklearn.svm import SVC
from sklearn.model_selection import GridSearchCV

# Different kernels
kernels = ['linear', 'poly', 'rbf', 'sigmoid']
for kernel in kernels:
    svm = Pipeline([
        ('scaler', StandardScaler()),
        ('svm', SVC(kernel=kernel, random_state=42))
    ])
    svm.fit(X_train, y_train)
    score = svm.score(X_test, y_test)
    print(f"{kernel:10s}: Accuracy = {score:.4f}")

# RBF kernel with tuning
param_grid = {
    'svm__C': [0.1, 1, 10, 100],
    'svm__gamma': ['scale', 'auto', 0.01, 0.1, 1]
}

rbf_svm = Pipeline([
    ('scaler', StandardScaler()),
    ('svm', SVC(kernel='rbf'))
])

grid_search = GridSearchCV(rbf_svm, param_grid, cv=5, scoring='accuracy', n_jobs=-1)
grid_search.fit(X_train, y_train)
print(f"Best params: {grid_search.best_params_}")
print(f"Best score: {grid_search.best_score_:.4f}")
```

---

## Multi-Class SVM

### Strategies

#### One-vs-One (OvO)
```
For k classes: k(k-1)/2 classifiers
Each classifier trained on two classes
Majority vote for prediction
```

#### One-vs-Rest (OvR)
```
For k classes: k classifiers
Each classifier trained on one class vs all others
Highest decision function wins
```

### Implementation

```python
from sklearn.svm import SVC
from sklearn.datasets import load_iris
from sklearn.multiclass import OneVsOneClassifier, OneVsRestClassifier

# Load multi-class data
iris = load_iris()
X_train, X_test, y_train, y_test = train_test_split(
    iris.data, iris.target, test_size=0.2, random_state=42
)

# One-vs-One (default)
ovo_svm = Pipeline([
    ('scaler', StandardScaler()),
    ('svm', OneVsOneClassifier(SVC(kernel='rbf', C=1.0)))
])
ovo_svm.fit(X_train, y_train)
print(f"OvO Accuracy: {ovo_svm.score(X_test, y_test):.4f}")

# One-vs-Rest
ovr_svm = Pipeline([
    ('scaler', StandardScaler()),
    ('svm', OneVsRestClassifier(SVC(kernel='rbf', C=1.0)))
])
ovr_svm.fit(X_train, y_train)
print(f"OvR Accuracy: {ovr_svm.score(X_test, y_test):.4f}")
```

---

## SVM for Regression (SVR)

### Theory

Finds a function that deviates from target by at most ε:

```
Minimize: 0.5 * ||w||² + C * Σ(ξᵢ + ξᵢ*)
Subject to: yᵢ - w^T * xᵢ - b ≤ ε + ξᵢ
           w^T * xᵢ + b - yᵢ ≤ ε + ξᵢ*
           ξᵢ, ξᵢ* ≥ 0
```

### Implementation

```python
from sklearn.svm import SVR
from sklearn.metrics import mean_squared_error
import numpy as np

# Generate regression data
np.random.seed(42)
X_reg = np.sort(5 * np.random.rand(200, 1), axis=0)
y_reg = np.sin(X_reg).ravel() + np.random.randn(200) * 0.1

X_train_r, X_test_r, y_train_r, y_test_r = train_test_split(
    X_reg, y_reg, test_size=0.2, random_state=42
)

# SVR with different kernels
for kernel in ['linear', 'rbf', 'poly']:
    svr = Pipeline([
        ('scaler', StandardScaler()),
        ('svr', SVR(kernel=kernel, C=1.0, epsilon=0.1))
    ])
    svr.fit(X_train_r, y_train_r)
    y_pred_r = svr.predict(X_test_r)
    rmse = np.sqrt(mean_squared_error(y_test_r, y_pred_r))
    print(f"{kernel:8s}: RMSE = {rmse:.4f}")
```

---

## Visualization

```python
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap

def plot_svm_decision_boundary(X, y, model, title):
    h = 0.02
    x_min, x_max = X[:, 0].min() - 1, X[:, 0].max() + 1
    y_min, y_max = X[:, 1].min() - 1, X[:, 1].max() + 1
    xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                          np.arange(y_min, y_max, h))

    Z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)

    plt.figure(figsize=(10, 8))
    plt.contourf(xx, yy, Z, alpha=0.8, cmap=ListedColormap(['#FFAAAA', '#AAAAFF']))
    plt.scatter(X[:, 0], X[:, 1], c=y, cmap=ListedColormap(['#FF0000', '#0000FF']),
                edgecolors='k', s=50)

    # Mark support vectors
    sv = model.named_steps['svm'].support_vectors_
    scaler = model.named_steps['scaler']
    sv_original = scaler.inverse_transform(sv)
    plt.scatter(sv_original[:, 0], sv_original[:, 1], s=200, facecolors='none',
                edgecolors='black', linewidths=2, label='Support Vectors')

    plt.title(title)
    plt.legend()
    plt.tight_layout()
    plt.show()

# Plot for different kernels
for kernel in ['linear', 'rbf']:
    model = Pipeline([
        ('scaler', StandardScaler()),
        ('svm', SVC(kernel=kernel, C=1.0))
    ])
    model.fit(X_train, y_train)
    plot_svm_decision_boundary(X_train, y_train, model, f'SVM with {kernel} kernel')
```

---

## Hyperparameter Tuning

### Important Parameters

| Parameter | Description | Range |
|-----------|-------------|-------|
| C | Regularization | 0.001 - 1000 |
| kernel | Kernel type | linear, rbf, poly, sigmoid |
| gamma | Kernel coefficient | scale, auto, 0.001 - 100 |
| degree | Polynomial degree | 2 - 10 |
| epsilon | SVR margin width | 0.01 - 1.0 |

### Tuning Strategy

```python
from sklearn.model_selection import RandomizedSearchCV
from scipy.stats import uniform, loguniform

param_distributions = {
    'svm__C': loguniform(1e-3, 1e3),
    'svm__gamma': loguniform(1e-4, 1e2),
    'svm__kernel': ['rbf', 'poly']
}

random_search = RandomizedSearchCV(
    Pipeline([
        ('scaler', StandardScaler()),
        ('svm', SVC())
    ]),
    param_distributions,
    n_iter=50,
    cv=5,
    scoring='accuracy',
    random_state=42,
    n_jobs=-1
)
random_search.fit(X_train, y_train)
print(f"Best params: {random_search.best_params_}")
```

---

## Advantages and Disadvantages

### Advantages
- Effective in high dimensions
- Memory efficient (uses support vectors)
- Versatile through different kernels
- Strong theoretical foundation (VC dimension)

### Disadvantages
- Slow on large datasets (O(n²) to O(n³))
- Sensitive to feature scaling
- No probability estimates by default
- Difficult to interpret
- Kernel selection requires expertise

## Best Practices

1. **Always scale features**: SVM is sensitive to feature magnitudes
2. **Start with RBF kernel**: Good default choice
3. **Tune C and gamma**: Use grid search or random search
4. **Use LinearSVC for large datasets**: More efficient than SVC with linear kernel
5. **Consider Nystroem approximation**: For very large datasets

## Further Reading

- "Support Vector Machines" by Steinwart and Christmann
- "Learning with Kernels" by Schölkopf and Smola
- Scikit-learn SVM documentation
