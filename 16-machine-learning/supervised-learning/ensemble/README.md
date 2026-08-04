# Ensemble Methods

## Overview

Ensemble methods combine multiple base learners to create a stronger model. They reduce variance (bagging), bias (boosting), or improve predictions (stacking).

## Bagging (Bootstrap Aggregating)

### Theory

1. Create B bootstrap samples (sampling with replacement)
2. Train base learner on each sample
3. Aggregate predictions (majority vote or average)

### Implementation

```python
from sklearn.ensemble import BaggingClassifier, BaggingRegressor
from sklearn.tree import DecisionTreeClassifier
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import numpy as np

# Generate data
X, y = make_classification(n_samples=1000, n_features=20, n_informative=10,
                           n_classes=2, random_state=42)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Bagging Classifier
bagging = BaggingClassifier(
    estimator=DecisionTreeClassifier(),
    n_estimators=100,
    max_samples=0.8,
    max_features=0.8,
    random_state=42,
    n_jobs=-1
)
bagging.fit(X_train, y_train)
print(f"Bagging Accuracy: {bagging.score(X_test, y_test):.4f}")

# Out-of-bag score
print(f"OOB Score: {bagging.oob_score_:.4f}")
```

## Boosting

### AdaBoost

Sequentially trains weak learners, focusing on misclassified samples:

```python
from sklearn.ensemble import AdaBoostClassifier
from sklearn.tree import DecisionTreeClassifier

# AdaBoost with decision stumps
ada = AdaBoostClassifier(
    estimator=DecisionTreeClassifier(max_depth=1),
    n_estimators=100,
    learning_rate=0.1,
    random_state=42
)
ada.fit(X_train, y_train)
print(f"AdaBoost Accuracy: {ada.score(X_test, y_test):.4f}")

# Feature importance
print(f"Feature importances: {ada.feature_importances_}")
```

### Gradient Boosting

```python
from sklearn.ensemble import GradientBoostingClassifier

gb = GradientBoostingClassifier(
    n_estimators=100,
    learning_rate=0.1,
    max_depth=3,
    subsample=0.8,
    random_state=42
)
gb.fit(X_train, y_train)
print(f"Gradient Boosting Accuracy: {gb.score(X_test, y_test):.4f}")

# Staged predictions for early stopping
train_scores = []
for y_pred_staged in gb.staged_predict(X_test):
    train_scores.append(accuracy_score(y_test, y_pred_staged))
```

### XGBoost

```python
import xgboost as xgb

# XGBoost Classifier
xgb_clf = xgb.XGBClassifier(
    n_estimators=100,
    learning_rate=0.1,
    max_depth=6,
    subsample=0.8,
    colsample_bytree=0.8,
    eval_metric='mlogloss',
    use_label_encoder=False,
    random_state=42
)
xgb_clf.fit(X_train, y_train)
print(f"XGBoost Accuracy: {xgb_clf.score(X_test, y_test):.4f}")

# Feature importance
xgb.plot_importance(xgb_clf)
```

### LightGBM

```python
import lightgbm as lgb

lgb_clf = lgb.LGBMClassifier(
    n_estimators=100,
    learning_rate=0.1,
    num_leaves=31,
    subsample=0.8,
    colsample_bytree=0.8,
    random_state=42,
    verbose=-1
)
lgb_clf.fit(X_train, y_train)
print(f"LightGBM Accuracy: {lgb_clf.score(X_test, y_test):.4f}")
```

## Stacking

### Theory

Uses a meta-learner to combine predictions from multiple base learners:

```
Level 0: Base learners (diverse algorithms)
Level 1: Meta-learner (combines base predictions)
```

### Implementation

```python
from sklearn.ensemble import StackingClassifier, VotingClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.svm import SVC
from sklearn.ensemble import RandomForestClassifier
from sklearn.neighbors import KNeighborsClassifier

# Define base learners
base_learners = [
    ('rf', RandomForestClassifier(n_estimators=100, random_state=42)),
    ('svm', SVC(kernel='rbf', probability=True, random_state=42)),
    ('knn', KNeighborsClassifier(n_neighbors=5))
]

# Stacking classifier
stacking = StackingClassifier(
    estimators=base_learners,
    final_estimator=LogisticRegression(),
    cv=5,
    n_jobs=-1
)
stacking.fit(X_train, y_train)
print(f"Stacking Accuracy: {stacking.score(X_test, y_test):.4f}")

# Voting classifier (hard voting)
voting_hard = VotingClassifier(
    estimators=base_learners,
    voting='hard'
)
voting_hard.fit(X_train, y_train)
print(f"Hard Voting Accuracy: {voting_hard.score(X_test, y_test):.4f}")

# Voting classifier (soft voting)
voting_soft = VotingClassifier(
    estimators=base_learners,
    voting='soft'
)
voting_soft.fit(X_train, y_train)
print(f"Soft Voting Accuracy: {voting_soft.score(X_test, y_test):.4f}")
```

## Custom Ensemble

```python
from sklearn.base import BaseEstimator, ClassifierMixin
from sklearn.model_selection import cross_val_score

class CustomEnsemble(BaseEstimator, ClassifierMixin):
    def __init__(self, models):
        self.models = models
    
    def fit(self, X, y):
        self.fitted_models = []
        for model in self.models:
            model.fit(X, y)
            self.fitted_models.append(model)
        return self
    
    def predict(self, X):
        predictions = np.array([model.predict(X) for model in self.fitted_models])
        # Majority vote
        return np.apply_along_axis(
            lambda x: np.bincount(x.astype(int)).argmax(), 0, predictions
        )
    
    def predict_proba(self, X):
        probas = np.array([model.predict_proba(X) for model in self.fitted_models])
        return np.mean(probas, axis=0)

# Usage
ensemble = CustomEnsemble([
    RandomForestClassifier(n_estimators=100, random_state=42),
    GradientBoostingClassifier(n_estimators=100, random_state=42),
    SVC(kernel='rbf', probability=True, random_state=42)
])

ensemble.fit(X_train, y_train)
print(f"Custom Ensemble Accuracy: {ensemble.score(X_test, y_test):.4f}")
```

## Model Comparison

| Method | Variance Reduction | Bias Reduction | Training Speed | Parallelizable |
|--------|-------------------|----------------|----------------|----------------|
| Bagging | High | Low | Fast | Yes |
| Boosting | Medium | High | Slow | Limited |
| Stacking | High | Medium | Slow | Partial |

## Best Practices

1. **Diversity**: Use different algorithms or data subsets
2. **Bagging**: Use for high-variance models (deep trees)
3. **Boosting**: Use for high-bias models (shallow trees)
4. **Stacking**: Use diverse base learners
5. **Cross-validation**: Use for meta-learner training

## Further Reading

- "Ensemble Methods in Machine Learning" by Kuncheva
- "Elements of Statistical Learning" by Hastie et al.
- Scikit-learn ensemble documentation
