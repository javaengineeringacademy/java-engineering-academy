# Regression Algorithms

## Overview

Regression is a supervised learning task that predicts continuous numerical values. The model learns a mapping function from input features to a continuous output variable.

## Types of Regression

### Simple Linear Regression
- One input feature, one output
- y = mx + b

### Multiple Linear Regression
- Multiple input features, one output
- y = w₁x₁ + w₂x₂ + ... + wₙxₙ + b

### Polynomial Regression
- Non-linear relationship
- y = w₀ + w₁x + w₂x² + ... + wₙxⁿ

---

## Linear Regression

### Theory

Finds the best-fitting linear relationship by minimizing the sum of squared residuals:

```
Minimize: Σ(yᵢ - ŷᵢ)²
where ŷᵢ = w^T * xᵢ + b
```

### Closed-Form Solution (Normal Equation)
```
w = (X^T * X)^(-1) * X^T * y
```

### Assumptions
1. **Linearity**: Linear relationship between features and target
2. **Independence**: Observations are independent
3. **Homoscedasticity**: Constant variance of residuals
4. **Normality**: Residuals are normally distributed
5. **No multicollinearity**: Features are not highly correlated

### Implementation

```python
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import numpy as np

# Generate sample data
np.random.seed(42)
X = 2 * np.random.rand(100, 1)
y = 4 + 3 * X + np.random.randn(100, 1)

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# Train model
lr = LinearRegression()
lr.fit(X_train, y_train)

# Coefficients
print(f"Intercept: {lr.intercept_[0]:.4f}")
print(f"Coefficient: {lr.coef_[0][0]:.4f}")

# Predictions
y_pred = lr.predict(X_test)
print(f"R² Score: {r2_score(y_test, y_pred):.4f}")
print(f"RMSE: {np.sqrt(mean_squared_error(y_test, y_pred)):.4f}")
```

### Diagnostics

```python
import matplotlib.pyplot as plt
from scipy import stats

# Residual analysis
residuals = y_test - y_pred

fig, axes = plt.subplots(1, 3, figsize=(15, 5))

# Residuals vs Fitted
axes[0].scatter(y_pred, residuals)
axes[0].axhline(y=0, color='r', linestyle='--')
axes[0].set_xlabel('Fitted values')
axes[0].set_ylabel('Residuals')
axes[0].set_title('Residuals vs Fitted')

# Q-Q plot
stats.probplot(residuals.flatten(), dist="norm", plot=axes[1])
axes[1].set_title('Q-Q Plot')

# Residual distribution
axes[2].hist(residuals, bins=20, edgecolor='black')
axes[2].set_xlabel('Residuals')
axes[2].set_ylabel('Frequency')
axes[2].set_title('Residual Distribution')

plt.tight_layout()
plt.show()
```

---

## Polynomial Regression

### Theory

Extends linear regression by adding polynomial terms to capture non-linear relationships:

```
y = w₀ + w₁x + w₂x² + w₃x³ + ... + wₙxⁿ
```

### Implementation

```python
from sklearn.preprocessing import PolynomialFeatures
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LinearRegression

# Polynomial regression pipeline
degree = 3
poly_reg = Pipeline([
    ('poly_features', PolynomialFeatures(degree=degree, include_bias=False)),
    ('linear_regression', LinearRegression())
])

poly_reg.fit(X_train, y_train)
y_pred_poly = poly_reg.predict(X_test)

print(f"R² Score (degree={degree}): {r2_score(y_test, y_pred_poly):.4f}")

# Compare different degrees
for d in range(1, 6):
    model = Pipeline([
        ('poly', PolynomialFeatures(degree=d, include_bias=False)),
        ('lr', LinearRegression())
    ])
    model.fit(X_train, y_train)
    r2 = r2_score(y_test, model.predict(X_test))
    print(f"Degree {d}: R² = {r2:.4f}")
```

### Underfitting vs Overfitting
- **Degree 1**: Underfitting (high bias)
- **Degree 3**: Good fit
- **Degree 10+**: Overfitting (high variance)

---

## Ridge Regression (L2 Regularization)

### Theory

Adds L2 penalty to prevent overfitting:
```
Minimize: Σ(yᵢ - ŷᵢ)² + α * Σwⱼ²
```

### Effect
- Shrinks coefficients toward zero
- Never makes coefficients exactly zero
- Handles multicollinearity

### Implementation

```python
from sklearn.linear_model import Ridge, RidgeCV

# Basic Ridge
ridge = Ridge(alpha=1.0)
ridge.fit(X_train, y_train)

# Ridge with cross-validation
alphas = np.logspace(-3, 3, 100)
ridge_cv = RidgeCV(alphas=alphas, scoring='r2', cv=5)
ridge_cv.fit(X_train, y_train)

print(f"Best alpha: {ridge_cv.alpha_:.4f}")
print(f"R² Score: {ridge_cv.score(X_test, y_test):.4f}")

# Compare coefficients
print("\nCoefficient comparison:")
print(f"Linear Regression: {lr.coef_.flatten()}")
print(f"Ridge Regression: {ridge_cv.coef_.flatten()}")
```

### Ridge Path
```python
alphas = np.logspace(-2, 5, 200)
coefs = []
for a in alphas:
    ridge = Ridge(alpha=a)
    ridge.fit(X_train, y_train)
    coefs.append(ridge.coef_.flatten())

plt.figure(figsize=(10, 6))
plt.plot(np.log10(alphas), coefs)
plt.xlabel('log(alpha)')
plt.ylabel('Coefficients')
plt.title('Ridge Coefficients Path')
plt.show()
```

---

## Lasso Regression (L1 Regularization)

### Theory

Adds L1 penalty for feature selection:
```
Minimize: Σ(yᵢ - ŷᵢ)² + α * Σ|wⱼ|
```

### Effect
- Can shrink coefficients to exactly zero
- Performs automatic feature selection
- Produces sparse models

### Implementation

```python
from sklearn.linear_model import Lasso, LassoCV

# Basic Lasso
lasso = Lasso(alpha=0.1)
lasso.fit(X_train, y_train)

# Lasso with cross-validation
lasso_cv = LassoCV(alphas=None, cv=5, random_state=42)
lasso_cv.fit(X_train, y_train)

print(f"Best alpha: {lasso_cv.alpha_:.4f}")
print(f"R² Score: {lasso_cv.score(X_test, y_test):.4f}")

# Feature selection
selected = np.sum(lasso_cv.coef_ != 0)
print(f"Features selected: {selected}/{X_train.shape[1]}")
```

### Lasso Path
```python
from sklearn.linear_model import lasso_path

alphas, coefs, _ = lasso_path(X_train, y_train, alphas=np.logspace(-3, 3, 100))

plt.figure(figsize=(10, 6))
plt.plot(np.log10(alphas), coefs.T)
plt.xlabel('log(alpha)')
plt.ylabel('Coefficients')
plt.title('Lasso Coefficients Path')
plt.axhline(y=0, color='k', linestyle='--')
plt.show()
```

---

## Elastic Net

### Theory

Combines L1 and L2 penalties:
```
Minimize: Σ(yᵢ - ŷᵢ)² + α * (l1_ratio * Σ|wⱼ| + (1-l1_ratio) * Σwⱼ²)
```

### Implementation

```python
from sklearn.linear_model import ElasticNet, ElasticNetCV

# Elastic Net
enet = ElasticNet(alpha=1.0, l1_ratio=0.5)
enet.fit(X_train, y_train)

# With cross-validation
enet_cv = ElasticNetCV(
    l1_ratio=[0.1, 0.5, 0.7, 0.9, 0.99],
    alphas=None,
    cv=5,
    random_state=42
)
enet_cv.fit(X_train, y_train)

print(f"Best alpha: {enet_cv.alpha_:.4f}")
print(f"Best l1_ratio: {enet_cv.l1_ratio_:.4f}")
print(f"R² Score: {enet_cv.score(X_test, y_test):.4f}")
```

---

## Regularization Comparison

| Method | Penalty | Feature Selection | Use Case |
|--------|---------|-------------------|----------|
| Linear | None | No | Baseline |
| Ridge | L2 | No | Multicollinearity |
| Lasso | L1 | Yes | Sparse features |
| Elastic Net | L1+L2 | Partial | Correlated features |

## Algorithm Selection Guide

```
Data Size          Feature Count     Best Choice
< 100              Any               Linear/Ridge
100 - 10,000       < 20              Ridge/Lasso
100 - 10,000       > 20              Lasso/ElasticNet
> 10,000           Any               SGD variants
Non-linear         Any               Polynomial/SVR
```

## Evaluation Metrics

```python
from sklearn.metrics import (
    mean_squared_error, mean_absolute_error,
    r2_score, mean_absolute_percentage_error
)

def evaluate_regression(y_true, y_pred):
    print(f"RMSE: {np.sqrt(mean_squared_error(y_true, y_pred)):.4f}")
    print(f"MAE: {mean_absolute_error(y_true, y_pred):.4f}")
    print(f"R²: {r2_score(y_true, y_pred):.4f}")
    print(f"MAPE: {mean_absolute_percentage_error(y_true, y_pred):.4f}")

evaluate_regression(y_test, y_pred)
```

## Best Practices

1. **Feature scaling**: Standardize features for regularized models
2. **Cross-validation**: Use k-fold CV for model selection
3. **Regularization tuning**: Use RidgeCV/LassoCV for automatic tuning
4. **Feature engineering**: Create interaction and polynomial features
5. **Outlier handling**: Robust regression or outlier removal
6. **Assumption checking**: Validate linearity, normality, homoscedasticity

## Further Reading

- "An Introduction to Statistical Learning" by James et al.
- "Elements of Statistical Learning" by Hastie et al.
- Scikit-learn documentation
