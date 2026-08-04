# Decision Trees and Ensemble Methods

## Overview

Decision trees partition the feature space into regions and make predictions based on the majority class or mean value within each region. Ensemble methods combine multiple trees for better performance.

## Decision Trees

### Theory

A decision tree recursively splits the data based on feature values to minimize impurity:

```
Root Node → Split → Child Nodes → Split → ... → Leaf Nodes (predictions)
```

### Splitting Criteria

#### Classification: Gini Impurity
```
Gini(t) = 1 - Σpᵢ²
where pᵢ = proportion of class i in node t
```

#### Classification: Entropy
```
Entropy(t) = -Σpᵢ * log₂(pᵢ)
Information Gain = Entropy(parent) - Σweighted Entropy(children)
```

#### Regression: Variance Reduction
```
Var(t) = Σ(yᵢ - ȳ)² / n
```

### Implementation

```python
from sklearn.tree import DecisionTreeClassifier, DecisionTreeRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report
from sklearn.datasets import load_iris

# Load data
data = load_iris()
X_train, X_test, y_train, y_test = train_test_split(
    data.data, data.target, test_size=0.2, random_state=42
)

# Classification tree
clf = DecisionTreeClassifier(
    max_depth=5,
    min_samples_split=10,
    min_samples_leaf=5,
    random_state=42
)
clf.fit(X_train, y_train)

# Evaluate
y_pred = clf.predict(X_test)
print(f"Accuracy: {accuracy_score(y_test, y_pred):.4f}")
print(classification_report(y_test, y_pred))

# Feature importance
print(f"Feature importances: {clf.feature_importances_}")
```

### Hyperparameters

| Parameter | Description | Effect |
|-----------|-------------|--------|
| max_depth | Maximum tree depth | Controls overfitting |
| min_samples_split | Min samples to split | Controls overfitting |
| min_samples_leaf | Min samples in leaf | Controls overfitting |
| max_features | Features per split | Controls randomness |
| criterion | Split criterion | Gini vs Entropy |

### Visualization

```python
from sklearn.tree import export_text, plot_tree
import matplotlib.pyplot as plt

# Text representation
tree_rules = export_text(clf, feature_names=data.feature_names)
print(tree_rules)

# Graphical representation
plt.figure(figsize=(20, 10))
plot_tree(clf, filled=True, feature_names=data.feature_names,
          class_names=data.target_names, rounded=True)
plt.title("Decision Tree Visualization")
plt.tight_layout()
plt.show()
```

### Regression Tree

```python
from sklearn.tree import DecisionTreeRegressor
from sklearn.metrics import mean_squared_error
import numpy as np

# Generate regression data
np.random.seed(42)
X_reg = np.sort(5 * np.random.rand(200, 1), axis=0)
y_reg = np.sin(X_reg).ravel() + np.random.randn(200) * 0.1

X_train_r, X_test_r, y_train_r, y_test_r = train_test_split(
    X_reg, y_reg, test_size=0.2, random_state=42
)

# Regression tree
reg_tree = DecisionTreeRegressor(max_depth=5, random_state=42)
reg_tree.fit(X_train_r, y_train_r)
y_pred_r = reg_tree.predict(X_test_r)

print(f"RMSE: {np.sqrt(mean_squared_error(y_test_r, y_pred_r)):.4f}")
```

---

## Random Forest

### Theory

Ensemble of decision trees using bagging and feature randomness:

1. Create B bootstrap samples
2. Train tree on each sample with random feature subsets
3. Aggregate predictions (majority vote or average)

### Algorithm
```
For b = 1 to B:
    1. Sample D_b with replacement from D
    2. Train decision tree T_b on D_b
    3. At each split, select √p or p/3 random features

Prediction = majority_vote(T_1(x), ..., T_B(x))
```

### Implementation

```python
from sklearn.ensemble import RandomForestClassifier, RandomForestRegressor
from sklearn.model_selection import GridSearchCV

# Random Forest Classifier
rf_clf = RandomForestClassifier(
    n_estimators=100,
    max_depth=10,
    max_features='sqrt',
    min_samples_split=5,
    min_samples_leaf=2,
    n_jobs=-1,
    random_state=42
)
rf_clf.fit(X_train, y_train)

# Hyperparameter tuning
param_grid = {
    'n_estimators': [50, 100, 200],
    'max_depth': [5, 10, 20, None],
    'min_samples_split': [2, 5, 10],
    'min_samples_leaf': [1, 2, 4]
}

grid_search = GridSearchCV(
    RandomForestClassifier(random_state=42),
    param_grid, cv=5, scoring='accuracy', n_jobs=-1
)
grid_search.fit(X_train, y_train)
print(f"Best params: {grid_search.best_params_}")
```

### Out-of-Bag Error
```python
# OOB score without separate validation set
rf_oob = RandomForestClassifier(
    n_estimators=100, oob_score=True, random_state=42
)
rf_oob.fit(X_train, y_train)
print(f"OOB Score: {rf_oob.oob_score_:.4f}")
```

### Feature Importance
```python
import numpy as np
import matplotlib.pyplot as plt

importances = rf_clf.feature_importances_
std = np.std([tree.feature_importances_ for tree in rf_clf.estimators_], axis=0)

indices = np.argsort(importances)[::-1]

plt.figure(figsize=(12, 6))
plt.title("Feature Importances with Std Dev")
plt.bar(range(X_train.shape[1]), importances[indices], yerr=std[indices])
plt.xticks(range(X_train.shape[1]), [data.feature_names[i] for i in indices])
plt.ylabel("Importance")
plt.tight_layout()
plt.show()
```

---

## Gradient Boosting

### Theory

Sequentially builds trees where each new tree corrects errors of previous ensemble:

```
F₀(x) = initial prediction
Fₘ(x) = Fₘ₋₁(x) + η * hₘ(x)
where hₘ(x) fits negative gradients (pseudo-residuals)
```

### Algorithm
```
1. Initialize with mean/median
2. For m = 1 to M:
   a. Compute pseudo-residuals: rᵢₘ = -∂L(yᵢ, F(xᵢ))/∂F(xᵢ)
   b. Fit tree hₘ to residuals
   c. Find optimal step: γₘ = argmin_γ Σ L(yᵢ, Fₘ₋₁(xᵢ) + γ * hₘ(xᵢ))
   d. Update: Fₘ = Fₘ₋₁ + η * γₘ * hₘ
3. Final: F(x) = F₀(x) + Σ η * γₘ * hₘ(x)
```

### Implementation

```python
from sklearn.ensemble import GradientBoostingClassifier, GradientBoostingRegressor
from sklearn.model_selection import GridSearchCV

# Gradient Boosting Classifier
gb_clf = GradientBoostingClassifier(
    n_estimators=100,
    learning_rate=0.1,
    max_depth=3,
    min_samples_split=5,
    subsample=0.8,
    random_state=42
)
gb_clf.fit(X_train, y_train)

# Staged predictions for early stopping
train_scores = []
test_scores = []
for y_pred_staged in gb_clf.staged_predict(X_test):
    test_scores.append(accuracy_score(y_test, y_pred_staged))

plt.figure(figsize=(10, 6))
plt.plot(test_scores)
plt.xlabel('Boosting Iterations')
plt.ylabel('Test Accuracy')
plt.title('Test Accuracy vs Boosting Iterations')
plt.show()
```

### Hyperparameters

| Parameter | Effect | Typical Range |
|-----------|--------|---------------|
| n_estimators | Number of trees | 100-1000 |
| learning_rate | Step size | 0.01-0.3 |
| max_depth | Tree complexity | 3-8 |
| subsample | Row sampling | 0.6-0.9 |
| min_samples_split | Split threshold | 2-20 |

---

## XGBoost

### Theory

Optimized gradient boosting with:
- Regularized objective
- Weighted quantile sketch
- Sparsity-aware split finding
- Cache-aware access patterns

### Implementation

```python
import xgboost as xgb
from sklearn.metrics import accuracy_score

# XGBoost Classifier
xgb_clf = xgb.XGBClassifier(
    n_estimators=100,
    learning_rate=0.1,
    max_depth=6,
    subsample=0.8,
    colsample_bytree=0.8,
    reg_alpha=0.1,
    reg_lambda=1.0,
    random_state=42,
    use_label_encoder=False,
    eval_metric='mlogloss'
)
xgb_clf.fit(X_train, y_train, eval_set=[(X_test, y_test)], early_stopping_rounds=10)

# Predictions
y_pred = xgb_clf.predict(X_test)
print(f"Accuracy: {accuracy_score(y_test, y_pred):.4f}")

# Feature importance
xgb.plot_importance(xgb_clf)
plt.tight_layout()
plt.show()

# Tree visualization
xgb.plot_tree(xgb_clf, num_trees=0)
plt.tight_layout()
plt.show()
```

### Advanced Features
```python
# Custom objective function
def custom_objective(y_pred, dtrain):
    y_true = dtrain.get_label()
    grad = y_pred - y_true  # gradient
    hess = np.ones_like(y_pred)  # hessian
    return grad, hess

# Learning with early stopping
evals_result = {}
xgb_clf = xgb.XGBClassifier(n_estimators=1000, learning_rate=0.1)
xgb_clf.fit(
    X_train, y_train,
    eval_set=[(X_train, y_train), (X_test, y_test)],
    eval_metric='mlogloss',
    early_stopping_rounds=50,
    verbose=100
)

# Plot learning curves
xgb.plot_metric(evals_result)
plt.show()
```

---

## LightGBM

### Theory

Gradient boosting with:
- Leaf-wise (best-first) tree growth
- Gradient-based One-Side Sampling (GOSS)
- Exclusive Feature Bundling (EFB)

### Implementation

```python
import lightgbm as lgb

# LightGBM Classifier
lgb_clf = lgb.LGBMClassifier(
    n_estimators=100,
    learning_rate=0.1,
    num_leaves=31,
    max_depth=-1,
    min_child_samples=20,
    subsample=0.8,
    colsample_bytree=0.8,
    random_state=42,
    verbose=-1
)
lgb_clf.fit(X_train, y_train, eval_set=[(X_test, y_test)])

# Feature importance
lgb.plot_importance(lgb_clf)
plt.tight_layout()
plt.show()

# Learning curves
lgb.plot_metric(lgb_clf)
plt.show()
```

---

## CatBoost

### Implementation

```python
import catboost as cb

# CatBoost Classifier
cb_clf = cb.CatBoostClassifier(
    iterations=100,
    learning_rate=0.1,
    depth=6,
    l2_leaf_reg=3,
    random_seed=42,
    verbose=100
)
cb_clf.fit(X_train, y_train, eval_set=(X_test, y_test))

# Feature importance
cb_clf.get_feature_importance(prettified=True)

# SHAP values
shap_values = cb_clf.get_shap_values(X_test)
```

---

## Model Comparison

| Model | Speed | Accuracy | Overfitting Risk | Interpretability |
|-------|-------|----------|------------------|------------------|
| Decision Tree | Fast | Medium | High | High |
| Random Forest | Medium | High | Low | Medium |
| Gradient Boosting | Slow | High | Medium | Low |
| XGBoost | Medium | Very High | Medium | Low |
| LightGBM | Fast | Very High | Medium | Low |
| CatBoost | Medium | Very High | Low | Low |

## Best Practices

1. **Start with Random Forest**: Good baseline, robust
2. **Use early stopping**: Prevent overfitting with boosting
3. **Tune learning rate**: Lower = more robust, needs more trees
4. **Feature importance**: Use for feature selection
5. **Cross-validate**: Use stratified k-fold for classification
6. **Handle imbalanced data**: Use class weights or sampling

## Further Reading

- "Gradient Boosting Machine Learning" by Friedman
- XGBoost documentation
- LightGBM documentation
- CatBoost documentation
