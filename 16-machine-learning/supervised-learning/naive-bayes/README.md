# Naive Bayes Classification

## Overview

Naive Bayes is a family of probabilistic classifiers based on Bayes' theorem with the "naive" assumption of feature independence. Despite this simplification, it works surprisingly well in practice, especially for text classification.

## Bayes' Theorem

### Mathematical Foundation

```
P(C|X) = P(X|C) * P(C) / P(X)
```

Where:
- P(C|X): Posterior probability of class C given features X
- P(X|C): Likelihood of features given class C
- P(C): Prior probability of class C
- P(X): Evidence (marginal probability of features)

### The Naive Assumption

Features are conditionally independent given the class:

```
P(X|C) = P(x₁|C) * P(x₂|C) * ... * P(xₙ|C) = Π P(xᵢ|C)
```

### Classification Rule

```
Ĉ = argmax_C P(C) * Π P(xᵢ|C)
```

---

## Gaussian Naive Bayes

### Theory

Assumes features follow a normal distribution:
```
P(xᵢ|C) = (1/√(2πσ²_C)) * exp(-(xᵢ - μ_C)² / (2σ²_C))
```

### Implementation

```python
from sklearn.naive_bayes import GaussianNB
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report, accuracy_score
import numpy as np

# Load data
iris = load_iris()
X_train, X_test, y_train, y_test = train_test_split(
    iris.data, iris.target, test_size=0.2, random_state=42
)

# Train Gaussian NB
gnb = GaussianNB()
gnb.fit(X_train, y_train)

# Predictions
y_pred = gnb.predict(X_test)
print(f"Accuracy: {accuracy_score(y_test, y_pred):.4f}")
print(classification_report(y_test, y_pred))

# Class priors
print(f"Class priors: {gnb.class_prior_}")
print(f"Class counts: {gnb.class_count_}")

# Parameters per class
for i, class_label in enumerate(gnb.classes_):
    print(f"\nClass {class_label}:")
    print(f"  Means: {gnb.theta_[i]}")
    print(f"  Variances: {gnb.var_[i]}")
```

### Probability Estimation

```python
# Predict probabilities
y_prob = gnb.predict_proba(X_test)
print("\nSample probabilities:")
print(y_prob[:5])

# Log probabilities (more numerically stable)
y_log_prob = gnb.predict_log_proba(X_test)
print("\nLog probabilities:")
print(y_log_prob[:5])
```

---

## Multinomial Naive Bayes

### Theory

For discrete features (e.g., word counts):
```
P(xᵢ|C) = (count(xᵢ, C) + α) / (count(C) + α * n_features)
```

### Laplace Smoothing
- α = 1: Laplace smoothing (default)
- α < 1: Lidstone smoothing
- α = 0: No smoothing (can cause zero probabilities)

### Text Classification Example

```python
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer, TfidfVectorizer
from sklearn.pipeline import Pipeline
from sklearn.datasets import fetch_20newsgroups

# Load text data
categories = ['sci.space', 'rec.sport.baseball', 'comp.graphics']
train_data = fetch_20newsgroups(subset='train', categories=categories)
test_data = fetch_20newsgroups(subset='test', categories=categories)

# Pipeline with CountVectorizer
pipeline_count = Pipeline([
    ('vectorizer', CountVectorizer(max_features=5000)),
    ('classifier', MultinomialNB(alpha=1.0))
])

# Pipeline with TF-IDF
pipeline_tfidf = Pipeline([
    ('vectorizer', TfidfVectorizer(max_features=5000, use_idf=True)),
    ('classifier', MultinomialNB(alpha=0.1))
])

# Train and evaluate
pipeline_count.fit(train_data.data, train_data.target)
acc_count = pipeline_count.score(test_data.data, test_data.target)
print(f"CountVectorizer accuracy: {acc_count:.4f}")

pipeline_tfidf.fit(train_data.data, train_data.target)
acc_tfidf = pipeline_tfidf.score(test_data.data, test_data.target)
print(f"TF-IDF accuracy: {acc_tfidf:.4f}")
```

### Feature Extraction from Text

```python
from sklearn.feature_extraction.text import TfidfVectorizer

# Create sample documents
documents = [
    "machine learning is great",
    "natural language processing",
    "deep learning neural networks",
    "support vector machines"
]

# TF-IDF vectorization
tfidf = TfidfVectorizer(max_features=100, ngram_range=(1, 2))
X_tfidf = tfidf.fit_transform(documents)

print(f"Vocabulary size: {len(tfidf.vocabulary_)}")
print(f"Document-term matrix shape: {X_tfidf.shape}")
```

---

## Bernoulli Naive Bayes

### Theory

For binary features:
```
P(xᵢ|C) = P(xᵢ=1|C) * xᵢ + (1 - P(xᵢ=1|C)) * (1 - xᵢ)
```

### Implementation

```python
from sklearn.naive_bayes import BernoulliNB
from sklearn.datasets import make_classification

# Generate binary data
X_binary, y_binary = make_classification(
    n_samples=500, n_features=20, n_informative=10,
    n_classes=2, random_state=42
)
X_binary = (X_binary > 0).astype(int)  # Convert to binary

X_train_b, X_test_b, y_train_b, y_test_b = train_test_split(
    X_binary, y_binary, test_size=0.2, random_state=42
)

# Bernoulli NB
bnb = BernoulliNB(alpha=1.0)
bnb.fit(X_train_b, y_train_b)

print(f"Accuracy: {bnb.score(X_test_b, y_test_b):.4f}")
print(f"Class log-priors: {bnb.class_log_prior_}")
print(f"Feature log-probabilities shape: {bnb.feature_log_prob_.shape}")
```

---

## Complement Naive Bayes

### Theory

Designed for imbalanced datasets by using complement statistics:

```
P(xᵢ|C) = (count(xᵢ, ¬C) + α) / (count(¬C) + α * n_features)
```

### Implementation

```python
from sklearn.naive_bayes import ComplementNB
from imblearn.datasets import make_imbalance

# Create imbalanced dataset
X_imb, y_imb = make_imbalance(X_binary, y_binary, 
                               sampling_strategy={0: 200, 1: 20},
                               random_state=42)

X_train_i, X_test_i, y_train_i, y_test_i = train_test_split(
    X_imb, y_imb, test_size=0.2, random_state=42, stratify=y_imb
)

# Complement NB
cnb = ComplementNB(alpha=1.0)
cnb.fit(X_train_i, y_train_i)

# Compare with Multinomial NB
mnb = MultinomialNB(alpha=1.0)
mnb.fit(X_train_i, y_train_i)

print(f"Complement NB accuracy: {cnb.score(X_test_i, y_test_i):.4f}")
print(f"Multinomial NB accuracy: {mnb.score(X_test_i, y_test_i):.4f}")
```

---

## Categorical Naive Bayes

### Implementation

```python
from sklearn.naive_bayes import CategoricalNB

# Generate categorical data
np.random.seed(42)
X_cat = np.random.randint(0, 5, size=(500, 4))
y_cat = np.random.randint(0, 3, size=500)

X_train_c, X_test_c, y_train_c, y_test_c = train_test_split(
    X_cat, y_cat, test_size=0.2, random_state=42
)

# Categorical NB
cat_nb = CategoricalNB(alpha=1.0)
cat_nb.fit(X_train_c, y_train_c)

print(f"Accuracy: {cat_nb.score(X_test_c, y_test_c):.4f}")
```

---

## Model Comparison

| Type | Feature Type | Use Case |
|------|--------------|----------|
| GaussianNB | Continuous | General classification |
| MultinomialNB | Counts/Frequencies | Text classification |
| BernoulliNB | Binary | Binary features |
| ComplementNB | Counts | Imbalanced text |
| CategoricalNB | Categorical | Categorical features |

## Laplace Smoothing Effect

```python
import matplotlib.pyplot as plt

alphas = [0.001, 0.01, 0.1, 0.5, 1.0, 5.0, 10.0]
scores = []

for alpha in alphas:
    mnb = MultinomialNB(alpha=alpha)
    mnb.fit(X_tfidf, train_data.target)
    score = mnb.score(tfidf.transform(test_data.data), test_data.target)
    scores.append(score)

plt.figure(figsize=(10, 6))
plt.plot(alphas, scores, marker='o')
plt.xlabel('Alpha (Smoothing)')
plt.ylabel('Accuracy')
plt.title('Effect of Laplace Smoothing')
plt.xscale('log')
plt.grid(True)
plt.show()
```

---

## When to Use Naive Bayes

### Good For
- Text classification (spam, sentiment, topic)
- High-dimensional data
- Small training datasets
- Real-time prediction (fast)
- Multi-class problems

### Not Good For
- Features are correlated
- Need probability calibration
- Complex relationships
- Continuous features without distribution assumption

## Handling Zero Probabilities

### Smoothing Techniques
```python
# Laplace smoothing
mnb_laplace = MultinomialNB(alpha=1.0)

# Lidstone smoothing
mnb_lidstone = MultinomialNB(alpha=0.1)

# No smoothing (risky)
mnb_none = MultinomialNB(alpha=0.0)
```

## Feature Selection with Naive Bayes

```python
from sklearn.feature_selection import SelectKBest, chi2
from sklearn.pipeline import Pipeline

# Feature selection pipeline
pipeline_fs = Pipeline([
    ('selector', SelectKBest(chi2, k=1000)),
    ('classifier', MultinomialNB(alpha=0.1))
])

pipeline_fs.fit(X_tfidf, train_data.target)
score = pipeline_fs.score(tfidf.transform(test_data.data), test_data.target)
print(f"Accuracy with feature selection: {score:.4f}")
```

## Best Practices

1. **Choose the right variant**: GaussianNB for continuous, MultinomialNB for counts
2. **Use smoothing**: Always use α > 0 to avoid zero probabilities
3. **Feature engineering**: TF-IDF often better than raw counts
4. **Feature selection**: Remove irrelevant features
5. **Combine with other models**: Ensemble for better performance

## Further Reading

- "Naive Bayes models" in scikit-learn documentation
- "Text Classification with Naive Bayes" tutorial
- "Probabilistic Machine Learning" by Kevin Murphy
