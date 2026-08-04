# Feature Engineering

## Overview

Feature engineering creates, transforms, and selects features to improve model performance.

## Feature Stores

### Implementation

```python
from feast import FeatureStore, Entity, Feature, ValueType
from feast.data_source import FileSource
from feast import RepoConfig
from feast.infra.offline_stores.file_source import FileOfflineStore

# Define entity
customer = Entity(
    name="customer_id",
    value_type=ValueType.INT64,
    description="Customer ID"
)

# Define feature view
customer_features = FeatureView(
    name="customer_features",
    entities=["customer_id"],
    features=[
        Feature(name="age", value_type=ValueType.INT32),
        Feature(name="income", value_type=ValueType.FLOAT),
        Feature(name="credit_score", value_type=ValueType.FLOAT)
    ],
    ttl=timedelta(days=1),
    source=FileSource(
        path="data/customer_features.parquet",
        timestamp_field="event_timestamp"
    )
)

# Store features
store = FeatureStore(config=RepoConfig(
    project="my_project",
    provider="local",
    offline_store=FileOfflineStore(),
    entity_key_serialization_version=2
))

store.apply([customer, customer_features])

# Retrieve features
feature_vector = store.get_online_features(
    features=["customer_features:age", "customer_features:income"],
    entity_rows=[{"customer_id": 123}]
).to_dict()
```

---

## Feature Pipelines

### Scikit-learn Pipeline

```python
from sklearn.pipeline import Pipeline
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.impute import SimpleImputer
from sklearn.ensemble import RandomForestClassifier
import pandas as pd

# Sample data
df = pd.DataFrame({
    'age': [25, 30, None, 45],
    'income': [50000, 60000, 70000, None],
    'city': ['NYC', 'LA', 'NYC', 'SF'],
    'purchased': [0, 1, 0, 1]
})

# Define preprocessing
numeric_features = ['age', 'income']
categorical_features = ['city']

numeric_transformer = Pipeline(steps=[
    ('imputer', SimpleImputer(strategy='median')),
    ('scaler', StandardScaler())
])

categorical_transformer = Pipeline(steps=[
    ('imputer', SimpleImputer(strategy='constant', fill_value='missing')),
    ('onehot', OneHotEncoder(handle_unknown='ignore'))
])

preprocessor = ColumnTransformer(
    transformers=[
        ('num', numeric_transformer, numeric_features),
        ('cat', categorical_transformer, categorical_features)
    ])

# Full pipeline
pipeline = Pipeline(steps=[
    ('preprocessor', preprocessor),
    ('classifier', RandomForestClassifier(n_estimators=100, random_state=42))
])

# Fit and predict
X = df.drop('purchased', axis=1)
y = df['purchased']
pipeline.fit(X, y)
```

---

## Feature Transformation

### Automated Feature Engineering

```python
from sklearn.preprocessing import PolynomialFeatures

# Polynomial features
poly = PolynomialFeatures(degree=2, interaction_only=True)
X_poly = poly.fit_transform(X_numeric)
print(f"Original features: {X_numeric.shape[1]}")
print(f"Polynomial features: {X_poly.shape[1]}")

# Log transform
import numpy as np
df['log_income'] = np.log1p(df['income'])

# Binning
df['age_group'] = pd.cut(df['age'], bins=[0, 18, 35, 50, 100], 
                          labels=['youth', 'young_adult', 'middle_age', 'senior'])

# Date features
df['date'] = pd.to_datetime(df['date'])
df['year'] = df['date'].dt.year
df['month'] = df['date'].dt.month
df['day_of_week'] = df['date'].dt.dayofweek
df['is_weekend'] = df['date'].dt.dayofweek >= 5
```

---

## Feature Selection

### Filter Methods

```python
from sklearn.feature_selection import SelectKBest, f_classif, mutual_info_classif

# Select top K features
selector = SelectKBest(score_func=f_classif, k=5)
X_selected = selector.fit_transform(X, y)

# Get selected features
mask = selector.get_support()
selected_features = X.columns[mask]
print(f"Selected features: {selected_features.tolist()}")

# Mutual information
selector_mi = SelectKBest(score_func=mutual_info_classif, k=5)
X_selected_mi = selector_mi.fit_transform(X, y)
```

### Wrapper Methods

```python
from sklearn.feature_selection import RFE
from sklearn.ensemble import RandomForestClassifier

# Recursive Feature Elimination
estimator = RandomForestClassifier(n_estimators=100, random_state=42)
selector = RFE(estimator, n_features_to_select=5, step=1)
X_selected = selector.fit_transform(X, y)

# Get selected features
selected_features = X.columns[selector.support_]
print(f"Selected features: {selected_features.tolist()}")
```

### Embedded Methods

```python
from sklearn.linear_model import LassoCV

# Lasso for feature selection
lasso = LassoCV(cv=5, random_state=42)
lasso.fit(X, y)

# Get important features
importance = np.abs(lasso.coef_)
selected = importance > 0
print(f"Selected features: {X.columns[selected].tolist()}")
```

---

## Best Practices

1. **Understand domain**: Create meaningful features
2. **Handle missing values**: Imputation strategies
3. **Scale features**: Standardization/normalization
4. **Encode categoricals**: One-hot, target encoding
5. **Feature selection**: Remove irrelevant features
6. **Automate**: Use pipelines for reproducibility

## Further Reading

- "Feature Engineering for Machine Learning" by Zheng
- Feature store documentation (Feast, Tecton)
- Scikit-learn feature engineering tutorial
