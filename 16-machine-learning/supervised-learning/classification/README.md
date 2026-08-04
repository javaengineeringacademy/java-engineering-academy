# Classification Algorithms

## Overview

Classification is a supervised learning task where the goal is to predict a discrete class label for each input instance. The model learns a mapping function from input features to output classes based on labeled training data.

## Types of Classification

### Binary Classification
- Two possible output classes (e.g., spam/not spam, malignant/benign)
- Metrics: Accuracy, Precision, Recall, F1-score, AUC-ROC
- Examples: Email spam detection, disease diagnosis, fraud detection

### Multi-Class Classification
- More than two possible output classes
- One-vs-Rest (OvR) or One-vs-One (OvO) strategies
- Examples: Handwritten digit recognition, species classification, sentiment analysis

### Multi-Label Classification
- Each instance can belong to multiple classes simultaneously
- Binary Relevance, Classifier Chains
- Examples: Image tagging, topic labeling, protein function prediction

---

## Logistic Regression

### Theory

Logastic regression models the probability that an instance belongs to a particular class using the sigmoid function:

```
P(y=1|X) = σ(w^T * x + b) = 1 / (1 + e^(-(w^T * x + b)))
```

### Decision Boundary
- Linear decision boundary in feature space
- Probabilistic output (0 to 1)
- Threshold typically set at 0.5

### Loss Function (Binary Cross-Entropy)
```
L = -[y * log(p) + (1-y) * log(1-p)]
```

### Regularization
- **L1 (Lasso)**: Promotes sparsity, feature selection
- **L2 (Ridge)**: Prevents overfitting, shrinks coefficients
- **Elastic Net**: Combination of L1 and L2

### Implementation

```python
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
from sklearn.datasets import load_breast_cancer

# Load data
data = load_breast_cancer()
X_train, X_test, y_train, y_test = train_test_split(
    data.data, data.target, test_size=0.2, random_state=42
)

# Train logistic regression
model = LogisticRegression(max_iter=10000, C=1.0, penalty='l2')
model.fit(X_train, y_train)

# Evaluate
y_pred = model.predict(X_test)
print(classification_report(y_test, y_pred))

# Probabilities
y_prob = model.predict_proba(X_test)[:, 1]
```

### Advantages
- Simple, interpretable, fast training
- Outputs probabilities
- Low variance, high bias (regularization controlled)

### Disadvantages
- Assumes linear decision boundary
- Sensitive to multicollinearity
- May underfit complex relationships

---

## Random Forest Classification

### Theory

Random Forest is an ensemble method that builds multiple decision trees and aggregates their predictions through voting or averaging.

### Key Concepts
1. **Bagging**: Bootstrap Aggregating - sampling with replacement
2. **Feature Randomness**: Random subset of features at each split
3. **Majority Voting**: Final prediction from all trees

### Algorithm
```
1. For each tree t in forest:
   a. Sample data with replacement (bootstrap)
   b. For each node:
      - Select random subset of features
      - Find best split among subset
      - Split node
   c. Grow tree to maximum depth
2. Predict by majority vote of all trees
```

### Hyperparameters
- `n_estimators`: Number of trees
- `max_depth`: Maximum tree depth
- `max_features`: Number of features per split
- `min_samples_split`: Minimum samples to split a node
- `min_samples_leaf`: Minimum samples in leaf

### Implementation

```python
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV

# Basic Random Forest
rf = RandomForestClassifier(
    n_estimators=100,
    max_depth=10,
    max_features='sqrt',
    random_state=42
)
rf.fit(X_train, y_train)

# Hyperparameter tuning
param_grid = {
    'n_estimators': [50, 100, 200],
    'max_depth': [5, 10, 20, None],
    'min_samples_split': [2, 5, 10]
}

grid_search = GridSearchCV(
    RandomForestClassifier(random_state=42),
    param_grid, cv=5, scoring='accuracy', n_jobs=-1
)
grid_search.fit(X_train, y_train)
print(f"Best params: {grid_search.best_params_}")
```

### Feature Importance
```python
import numpy as np
import matplotlib.pyplot as plt

importances = rf.feature_importances_
indices = np.argsort(importances)[::-1]

plt.figure(figsize=(10, 6))
plt.bar(range(X_train.shape[1]), importances[indices])
plt.xticks(range(X_train.shape[1]), indices, rotation=90)
plt.title('Feature Importances')
plt.tight_layout()
plt.show()
```

### Advantages
- Handles high-dimensional data
- Robust to outliers
- Provides feature importance
- Low overfitting risk

### Disadvantages
- Less interpretable than single trees
- Slower inference than single trees
- Memory intensive

---

## Support Vector Machines (SVM)

### Theory

SVM finds the optimal hyperplane that maximizes the margin between classes. Support vectors are the closest points to the decision boundary.

### Mathematical Formulation
```
Maximize: 2 / ||w||  (margin width)
Subject to: y_i(w^T * x_i + b) >= 1 for all i
```

### Kernel Trick
Maps data to higher dimensions where it becomes linearly separable:

| Kernel | Formula | Use Case |
|--------|---------|----------|
| Linear | K(x,y) = x·y | Linearly separable data |
| Polynomial | K(x,y) = (x·y + c)^d | Non-linear, low dimensions |
| RBF (Gaussian) | K(x,y) = exp(-γ\|\|x-y\|\|²) | General non-linear |
| Sigmoid | K(x,y) = tanh(αx·y + c) | Neural network-like |

### Soft Margin
Allows some misclassifications for better generalization:
```
Minimize: 0.5 * ||w||² + C * Σξ_i
Subject to: y_i(w^T * x_i + b) >= 1 - ξ_i
```

### Implementation

```python
from sklearn.svm import SVC
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import Pipeline

# SVM with different kernels
svm_linear = SVC(kernel='linear', C=1.0)
svm_rbf = SVC(kernel='rbf', C=1.0, gamma='scale')
svm_poly = SVC(kernel='poly', degree=3, C=1.0)

# Pipeline with scaling
pipeline = Pipeline([
    ('scaler', StandardScaler()),
    ('svm', SVC(kernel='rbf', C=1.0, gamma='scale'))
])
pipeline.fit(X_train, y_train)

# Probability calibration
svm_prob = SVC(kernel='rbf', probability=True)
svm_prob.fit(X_train, y_train)
y_prob = svm_prob.predict_proba(X_test)
```

### Hyperparameters
- **C**: Regularization parameter (small = smooth boundary)
- **gamma**: Kernel coefficient (large = complex boundary)
- **kernel**: Kernel type
- **degree**: Polynomial kernel degree

### Advantages
- Effective in high dimensions
- Memory efficient (uses support vectors only)
- Versatile kernels

### Disadvantages
- Slow on large datasets
- Sensitive to feature scaling
- No probability estimates by default
- Difficult to interpret

---

## Naive Bayes Classification

### Theory

Based on Bayes' theorem with the "naive" assumption of feature independence:

```
P(C|X) = P(X|C) * P(C) / P(X)
```

Where P(X|C) = Π P(x_i|C) (independence assumption)

### Types

#### Gaussian Naive Bayes
```python
from sklearn.naive_bayes import GaussianNB

gnb = GaussianNB()
gnb.fit(X_train, y_train)
```

#### Multinomial Naive Bayes
```python
from sklearn.naive_bayes import MultinomialNB

# For text classification (word counts)
mnb = MultinomialNB(alpha=1.0)  # Laplace smoothing
mnb.fit(X_train_counts, y_train)
```

#### Bernoulli Naive Bayes
```python
from sklearn.naive_bayes import BernoulliNB

# For binary features
bnb = BernoulliNB()
bnb.fit(X_binary, y_train)
```

### Laplace Smoothing
Adds α to each count to avoid zero probabilities:
```
P(x_i|C) = (count(x_i, C) + α) / (count(C) + α * n_features)
```

### Text Classification Example

```python
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.pipeline import Pipeline

text_clf = Pipeline([
    ('tfidf', TfidfVectorizer(max_features=5000, ngram_range=(1, 2))),
    ('clf', MultinomialNB(alpha=0.1))
])

text_clf.fit(X_train_text, y_train)
```

### Advantages
- Fast training and prediction
- Works well with small datasets
- Handles high-dimensional data
- Good baseline for text classification

### Disadvantages
- Independence assumption rarely holds
- Poor probability estimates
- Can't learn feature interactions

---

## Model Comparison

| Algorithm | Training Speed | Prediction Speed | Interpretability | Accuracy |
|-----------|---------------|-----------------|-----------------|----------|
| Logistic Regression | Fast | Fast | High | Medium |
| Random Forest | Medium | Fast | Medium | High |
| SVM | Slow | Medium | Low | High |
| Naive Bayes | Fast | Fast | High | Medium |

## Choosing the Right Algorithm

1. **Start simple**: Logistic Regression as baseline
2. **Small dataset**: Naive Bayes, SVM
3. **Large dataset**: Logistic Regression, Random Forest
4. **Interpretability needed**: Logistic Regression, Decision Tree
5. **Maximum accuracy**: Random Forest, SVM with tuning
6. **Text data**: Naive Bayes, SVM with linear kernel

## Evaluation Metrics

```python
from sklearn.metrics import (
    accuracy_score, precision_score, recall_score,
    f1_score, roc_auc_score, confusion_matrix,
    classification_report
)

# Complete evaluation
print(f"Accuracy: {accuracy_score(y_test, y_pred):.4f}")
print(f"Precision: {precision_score(y_test, y_pred, average='weighted'):.4f}")
print(f"Recall: {recall_score(y_test, y_pred, average='weighted'):.4f}")
print(f"F1 Score: {f1_score(y_test, y_pred, average='weighted'):.4f}")
print(f"AUC-ROC: {roc_auc_score(y_test, y_prob):.4f}")
print(confusion_matrix(y_test, y_pred))
```

## Best Practices

1. **Data preprocessing**: Scale features for SVM, handle missing values
2. **Cross-validation**: Use stratified k-fold for imbalanced classes
3. **Hyperparameter tuning**: GridSearchCV or RandomizedSearchCV
4. **Class imbalance**: SMOTE, class weights, undersampling
5. **Ensemble**: Combine multiple classifiers for better performance

## Further Reading

- "Pattern Recognition and Machine Learning" by Bishop
- "The Elements of Statistical Learning" by Hastie et al.
- Scikit-learn documentation: https://scikit-learn.org
