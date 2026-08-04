# Spark MLlib

MLlib is Spark's machine learning library, providing scalable implementations of common ML algorithms and utilities. It integrates with Spark's core engine for distributed processing and supports both batch and streaming ML workflows.

## Table of Contents

1. [MLlib Overview](#mllib-overview)
2. [Feature Engineering](#feature-engineering)
3. [Machine Learning Algorithms](#machine-learning-algorithms)
4. [ML Pipelines](#ml-pipelines)
5. [Model Evaluation](#model-evaluation)
6. [Distributed Training](#distributed-training)
7. [Advanced Topics](#advanced-topics)
8. [Best Practices](#best-practices)
9. [Common Patterns](#common-patterns)

---

## MLlib Overview

### MLlib vs ML

| Feature | MLlib (RDD-based) | ML (DataFrame-based) |
|---------|-------------------|---------------------|
| **API** | RDD-based | DataFrame-based |
| **Optimization** | None | Catalyst optimizer |
| **Pipelines** | Manual | Built-in |
| **Features** | Basic | Rich |
| **Recommended** | No | Yes |

### MLlib Architecture

```
MLlib Architecture:
┌─────────────────────────────────────────────────────────────┐
│                      ML Algorithms                           │
│         Classification, Regression, Clustering, etc.       │
├─────────────────────────────────────────────────────────────┤
│                      Feature Engineering                     │
│         Transformers, Feature Extractors, Selectors        │
├─────────────────────────────────────────────────────────────┤
│                      ML Pipelines                            │
│         Estimators, Transformers, Evaluator                │
├─────────────────────────────────────────────────────────────┤
│                      Utilities                               │
│         Linear Algebra, Statistics, Data Types             │
├─────────────────────────────────────────────────────────────┤
│                      Spark Core                              │
│         RDD, DataFrame, Distributed Processing             │
└─────────────────────────────────────────────────────────────┘
```

---

## Feature Engineering

### Feature Transformers

```python
from pyspark.ml.feature import (
    VectorAssembler, StringIndexer, OneHotEncoder,
    StandardScaler, MinMaxScaler, Normalizer,
    Tokenizer, HashingTF, IDF,
    PCA, PolynomialExpansion, SQLTransformer
)

# VectorAssembler: Combine features
assembler = VectorAssembler(
    inputCols=["age", "income", "education"],
    outputCol="features"
)
df = assembler.transform(df)

# StringIndexer: Encode categorical variables
indexer = StringIndexer(
    inputCol="gender",
    outputCol="gender_index"
)
df = indexer.fit(df).transform(df)

# OneHotEncoder: One-hot encoding
encoder = OneHotEncoder(
    inputCol="gender_index",
    outputCol="gender_vec"
)
df = encoder.fit(df).transform(df)

# StandardScaler: Standardize features
scaler = StandardScaler(
    inputCol="features",
    outputCol="scaled_features",
    withStd=True,
    withMean=True
)
df = scaler.fit(df).transform(df)

# MinMaxScaler: Scale features to [0, 1]
scaler = MinMaxScaler(
    inputCol="features",
    outputCol="minmax_features"
)
df = scaler.fit(df).transform(df)

# Normalizer: L2 normalization
normalizer = Normalizer(
    inputCol="features",
    outputCol="normalized_features",
    p=2.0
)
df = normalizer.transform(df)
```

### Text Feature Engineering

```python
# Tokenizer: Split text into words
tokenizer = Tokenizer(
    inputCol="text",
    outputCol="words"
)
df = tokenizer.transform(df)

# HashingTF: Term frequency
hashing_tf = HashingTF(
    inputCol="words",
    outputCol="raw_features",
    numFeatures=1000
)
df = hashing_tf.transform(df)

# IDF: Inverse document frequency
idf = IDF(
    inputCol="raw_features",
    outputCol="features"
)
df = idf.fit(df).transform(df)

# StopWordsRemover
from pyspark.ml.feature import StopWordsRemover

remover = StopWordsRemover(
    inputCol="words",
    outputCol="filtered_words"
)
df = remover.transform(df)
```

### Feature Selection

```python
# ChiSqSelector: Select features using chi-squared test
from pyspark.ml.feature import ChiSqSelector

selector = ChiSqSelector(
    numTopFeatures=10,
    featuresCol="features",
    labelCol="label",
    outputCol="selected_features"
)
df = selector.fit(df).transform(df)

# VarianceFilter: Remove low-variance features
from pyspark.ml.feature import VarianceThresholdSelector

selector = VarianceThresholdSelector(
    varianceThreshold=0.01,
    featuresCol="features",
    outputCol="selected_features"
)
df = selector.fit(df).transform(df)
```

---

## Machine Learning Algorithms

### Classification

```python
from pyspark.ml.classification import (
    LogisticRegression, DecisionTreeClassifier,
    RandomForestClassifier, GBTClassifier,
    SVM, NaiveBayes
)

# Logistic Regression
lr = LogisticRegression(
    featuresCol="features",
    labelCol="label",
    maxIter=100,
    regParam=0.01
)
model = lr.fit(training_data)

# Decision Tree
dt = DecisionTreeClassifier(
    featuresCol="features",
    labelCol="label",
    maxDepth=5
)
model = dt.fit(training_data)

# Random Forest
rf = RandomForestClassifier(
    featuresCol="features",
    labelCol="label",
    numTrees=100,
    maxDepth=5
)
model = rf.fit(training_data)

# Gradient Boosted Trees
gbt = GBTClassifier(
    featuresCol="features",
    labelCol="label",
    maxIter=100,
    maxDepth=5
)
model = gbt.fit(training_data)

# Support Vector Machine
svm = SVM(
    featuresCol="features",
    labelCol="label",
    maxIter=100,
    regParam=0.01
)
model = svm.fit(training_data)

# Naive Bayes
nb = NaiveBayes(
    featuresCol="features",
    labelCol="label",
    modelType="multinomial"
)
model = nb.fit(training_data)
```

### Regression

```python
from pyspark.ml.regression import (
    LinearRegression, DecisionTreeRegressor,
    RandomForestRegressor, GBTRegressor
)

# Linear Regression
lr = LinearRegression(
    featuresCol="features",
    labelCol="label",
    maxIter=100,
    regParam=0.01,
    elasticNetParam=0.8
)
model = lr.fit(training_data)

# Decision Tree Regressor
dt = DecisionTreeRegressor(
    featuresCol="features",
    labelCol="label",
    maxDepth=5
)
model = dt.fit(training_data)

# Random Forest Regressor
rf = RandomForestRegressor(
    featuresCol="features",
    labelCol="label",
    numTrees=100,
    maxDepth=5
)
model = rf.fit(training_data)

# Gradient Boosted Trees Regressor
gbt = GBTRegressor(
    featuresCol="features",
    labelCol="label",
    maxIter=100,
    maxDepth=5
)
model = gbt.fit(training_data)
```

### Clustering

```python
from pyspark.ml.clustering import (
    KMeans, BisectingKMeans,
    GaussianMixture, LDA
)

# K-Means
kmeans = KMeans(
    featuresCol="features",
    k=3,
    maxIter=20
)
model = kmeans.fit(training_data)

# Bisecting K-Means
bkm = BisectingKMeans(
    featuresCol="features",
    k=3,
    maxIter=20
)
model = bkm.fit(training_data)

# Gaussian Mixture
gmm = GaussianMixture(
    featuresCol="features",
    k=3,
    maxIter=100
)
model = gmm.fit(training_data)

# Latent Dirichlet Allocation (LDA)
lda = LDA(
    featuresCol="features",
    k=10,
    maxIter=100
)
model = lda.fit(training_data)
```

### Recommendation

```python
from pyspark.ml.recommendation import ALS

# Alternating Least Squares
als = ALS(
    userCol="user_id",
    itemCol="item_id",
    ratingCol="rating",
    maxIter=10,
    regParam=0.1,
    rank=10
)
model = als.fit(training_data)

# Get recommendations
recommendations = model.recommendForAllUsers(10)
```

---

## ML Pipelines

### Pipeline Components

```python
from pyspark.ml import Pipeline
from pyspark.ml.feature import VectorAssembler, StringIndexer
from pyspark.ml.classification import LogisticRegression

# Create pipeline stages
indexer = StringIndexer(
    inputCol="gender",
    outputCol="gender_index"
)

assembler = VectorAssembler(
    inputCols=["age", "income", "gender_index"],
    outputCol="features"
)

scaler = StandardScaler(
    inputCol="features",
    outputCol="scaled_features"
)

lr = LogisticRegression(
    featuresCol="scaled_features",
    labelCol="label",
    maxIter=100
)

# Create pipeline
pipeline = Pipeline(
    stages=[indexer, assembler, scaler, lr]
)

# Fit pipeline
model = pipeline.fit(training_data)

# Transform data
predictions = model.transform(test_data)
```

### Custom Transformers

```python
from pyspark.ml import Transformer
from pyspark.ml.param.shared import HasInputCol, HasOutputCol
from pyspark import keyword_only

class CustomTransformer(Transformer, HasInputCol, HasOutputCol):
    @keyword_only
    def __init__(self, inputCol=None, outputCol=None):
        super(CustomTransformer, self).__init__()
        kwargs = self._input_kwargs
        self.setParams(**kwargs)
    
    @keyword_only
    def setParams(self, inputCol=None, outputCol=None):
        kwargs = self._input_kwargs
        return self._set(**kwargs)
    
    def _transform(self, dataset):
        input_col = self.getInputCol()
        output_col = self.getOutputCol()
        
        # Custom transformation logic
        return dataset.withColumn(
            output_col,
            custom_function(dataset[input_col])
        )

# Use custom transformer
custom_transformer = CustomTransformer(
    inputCol="input",
    outputCol="output"
)

pipeline = Pipeline(stages=[custom_transformer])
model = pipeline.fit(data)
```

### Cross-Validation

```python
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder

# Define parameter grid
param_grid = ParamGridBuilder() \
    .addGrid(lr.regParam, [0.01, 0.1, 1.0]) \
    .addGrid(lr.elasticNetParam, [0.0, 0.5, 1.0]) \
    .build()

# Create cross-validator
cross_validator = CrossValidator(
    estimator=pipeline,
    estimatorParamMaps=param_grid,
    evaluator=BinaryClassificationEvaluator(),
    numFolds=5,
    seed=42
)

# Fit cross-validator
cv_model = cross_validator.fit(training_data)

# Get best model
best_model = cv_model.bestModel
```

---

## Model Evaluation

### Classification Metrics

```python
from pyspark.ml.evaluation import (
    BinaryClassificationEvaluator,
    MulticlassClassificationEvaluator
)

# Binary classification
binary_evaluator = BinaryClassificationEvaluator(
    rawPredictionCol="rawPrediction",
    labelCol="label",
    metricName="areaUnderROC"
)

auc = binary_evaluator.evaluate(predictions)
print(f"AUC: {auc}")

# Multiclass classification
multi_evaluator = MulticlassClassificationEvaluator(
    predictionCol="prediction",
    labelCol="label",
    metricName="accuracy"
)

accuracy = multi_evaluator.evaluate(predictions)
print(f"Accuracy: {accuracy}")

# Additional metrics
precision = multi_evaluator.evaluate(
    predictions, 
    {multi_evaluator.metricName: "weightedPrecision"}
)

recall = multi_evaluator.evaluate(
    predictions, 
    {multi_evaluator.metricName: "weightedRecall"}
)

f1 = multi_evaluator.evaluate(
    predictions, 
    {multi_evaluator.metricName: "f1"}
)
```

### Regression Metrics

```python
from pyspark.ml.evaluation import RegressionEvaluator

evaluator = RegressionEvaluator(
    predictionCol="prediction",
    labelCol="label"
)

# RMSE
rmse = evaluator.evaluate(
    predictions, 
    {evaluator.metricName: "rmse"}
)

# MAE
mae = evaluator.evaluate(
    predictions, 
    {evaluator.metricName: "mae"}
)

# R-squared
r2 = evaluator.evaluate(
    predictions, 
    {evaluator.metricName: "r2"}
)

# MSE
mse = evaluator.evaluate(
    predictions, 
    {evaluator.metricName: "mse"}
)
```

### Clustering Metrics

```python
from pyspark.ml.evaluation import ClusteringEvaluator

evaluator = ClusteringEvaluator(
    featuresCol="features",
    predictionCol="prediction"
)

# Silhouette score
silhouette = evaluator.evaluate(predictions)
print(f"Silhouette Score: {silhouette}")

# Within Set Sum of Squared Errors (WSSSE)
wssse = model.summary.trainingCost
print(f"WSSSE: {wssse}")
```

---

## Distributed Training

### Parallel Training

```python
# Random Forest is inherently parallel
rf = RandomForestClassifier(
    featuresCol="features",
    labelCol="label",
    numTrees=100,
    maxDepth=5,
    featureSubsetStrategy="auto"
)

# Each tree is trained independently
# Results are aggregated at the end
model = rf.fit(training_data)
```

### Distributed Optimization

```python
# Stochastic Gradient Descent
from pyspark.mllib.classification import SGDWithSGD

# Data is split across partitions
# Each partition computes gradients
# Gradients are aggregated
model = SGDWithSGD.train(
    labeled_point_rdd,
    iterations=100,
    step=0.1
)

# Alternating Least Squares (ALS)
als = ALS(
    userCol="user_id",
    itemCol="item_id",
    ratingCol="rating",
    maxIter=10,
    regParam=0.1
)

# ALS alternates between fixing user and item factors
# Each iteration is distributed across partitions
model = als.fit(training_data)
```

### Model Persistence

```python
# Save model
model.save("hdfs://path/to/model")

# Load model
from pyspark.ml.classification import LogisticRegressionModel

loaded_model = LogisticRegressionModel.load("hdfs://path/to/model")

# Save pipeline
pipeline_model.save("hdfs://path/to/pipeline_model")

# Load pipeline
from pyspark.ml import PipelineModel

loaded_pipeline = PipelineModel.load("hdfs://path/to/pipeline_model")
```

---

## Advanced Topics

### Hyperparameter Tuning

```python
from pyspark.ml.tuning import ParamGridBuilder, CrossValidator

# Define parameter grid
param_grid = ParamGridBuilder() \
    .addGrid(lr.regParam, [0.01, 0.1, 1.0]) \
    .addGrid(lr.maxIter, [10, 50, 100]) \
    .build()

# Train-validation split
from pyspark.ml.tuning import TrainValidationSplit

tvs = TrainValidationSplit(
    estimator=pipeline,
    estimatorParamMaps=param_grid,
    evaluator=BinaryClassificationEvaluator(),
    trainRatio=0.8
)

# Fit model
tvs_model = tvs.fit(training_data)

# Get best model
best_model = tvs_model.bestModel
```

### Feature Importance

```python
# Get feature importance from tree models
rf_model = RandomForestClassifier(
    featuresCol="features",
    labelCol="label",
    numTrees=100
).fit(training_data)

# Get feature importance
importance = rf_model.featureImportances

# Create feature importance DataFrame
feature_importance = spark.createDataFrame(
    zip(feature_names, importance.toArray()),
    ["feature", "importance"]
).orderBy("importance", ascending=False)

feature_importance.show()
```

### Model Interpretation

```python
# Linear model coefficients
lr_model = LinearRegression(
    featuresCol="features",
    labelCol="label"
).fit(training_data)

# Get coefficients
coefficients = lr_model.coefficients
intercept = lr_model.intercept

# Create coefficient DataFrame
coeff_df = spark.createDataFrame(
    zip(feature_names, coefficients.toArray()),
    ["feature", "coefficient"]
)

# Tree model rules
dt_model = DecisionTreeClassifier(
    featuresCol="features",
    labelCol="label"
).fit(training_data)

# Print tree structure
print(dt_model.toDebugString)
```

### Online Learning

```python
# Update model with new data
from pyspark.mllib.classification import StreamingLogisticRegressionWithSGD

# Create streaming model
streaming_model = StreamingLogisticRegressionWithSGD()
streaming_model.setInitialModel(initial_model)

# Train on streaming data
streaming_model.trainOn(training_stream)

# Make predictions on streaming data
prediction_stream = streaming_model.predictOn(testing_stream)
```

---

## Best Practices

### 1. Feature Engineering

```python
# Use appropriate feature transformations
# 1. Handle categorical variables
indexer = StringIndexer(inputCol="category", outputCol="category_index")
encoder = OneHotEncoder(inputCol="category_index", outputCol="category_vec")

# 2. Scale numerical features
scaler = StandardScaler(inputCol="features", outputCol="scaled_features")

# 3. Handle missing values
from pyspark.ml.feature import Imputer
imputer = Imputer(
    inputCols=["age", "income"],
    outputCols=["age_imputed", "income_imputed"]
)

# 4. Create interaction features
assembler = VectorAssembler(
    inputCols=["feature1", "feature2", "feature1_feature2"],
    outputCol="features"
)
```

### 2. Model Selection

```python
# Start with simple models
# 1. Logistic Regression for classification
lr = LogisticRegression(featuresCol="features", labelCol="label")

# 2. Linear Regression for regression
lr = LinearRegression(featuresCol="features", labelCol="label")

# 3. Random Forest for non-linear problems
rf = RandomForestClassifier(featuresCol="features", labelCol="label")

# 4. Gradient Boosted Trees for best performance
gbt = GBTClassifier(featuresCol="features", labelCol="label")
```

### 3. Cross-Validation

```python
# Always use cross-validation
from pyspark.ml.tuning import CrossValidator

cross_validator = CrossValidator(
    estimator=pipeline,
    estimatorParamMaps=param_grid,
    evaluator=BinaryClassificationEvaluator(),
    numFolds=5
)

# Fit cross-validator
cv_model = cross_validator.fit(training_data)

# Get best model
best_model = cv_model.bestModel
```

### 4. Model Persistence

```python
# Save models for production
model.save("hdfs://path/to/model")

# Version models
model.save(f"hdfs://path/to/model/v{version}")

# Load and use
loaded_model = LogisticRegressionModel.load("hdfs://path/to/model")
```

### 5. Performance Optimization

```python
# Use appropriate parallelism
spark.conf.set("spark.sql.shuffle.partitions", "200")

# Cache training data
training_data.cache()

# Use broadcast variables for small datasets
broadcast_var = sc.broadcast(small_dataset)

# Use efficient file formats
training_data.write.parquet("hdfs://path/to/training_data")
```

---

## Common Patterns

### Pattern 1: Classification Pipeline

```python
from pyspark.ml import Pipeline
from pyspark.ml.feature import VectorAssembler, StringIndexer, StandardScaler
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml.evaluation import MulticlassClassificationEvaluator

# Create pipeline
pipeline = Pipeline(stages=[
    StringIndexer(inputCol="category", outputCol="category_index"),
    VectorAssembler(inputCols=["age", "income", "category_index"], outputCol="features"),
    StandardScaler(inputCol="features", outputCol="scaled_features"),
    RandomForestClassifier(featuresCol="scaled_features", labelCol="label")
])

# Fit model
model = pipeline.fit(training_data)

# Make predictions
predictions = model.transform(test_data)

# Evaluate
evaluator = MulticlassClassificationEvaluator(
    labelCol="label", predictionCol="prediction", metricName="accuracy"
)
accuracy = evaluator.evaluate(predictions)
print(f"Accuracy: {accuracy}")
```

### Pattern 2: Recommendation System

```python
from pyspark.ml.recommendation import ALS

# Create ALS model
als = ALS(
    userCol="user_id",
    itemCol="item_id",
    ratingCol="rating",
    maxIter=10,
    regParam=0.1
)

# Fit model
model = als.fit(training_data)

# Get recommendations for all users
user_recs = model.recommendForAllUsers(10)

# Get recommendations for specific user
user_recs = model.recommendForUserSubset(test_users, 10)
```

### Pattern 3: Anomaly Detection

```python
from pyspark.ml.clustering import KMeans

# Train K-Means for anomaly detection
kmeans = KMeans(
    featuresCol="features",
    k=3,
    maxIter=20
)

model = kmeans.fit(training_data)

# Get cluster centers
centers = model.clusterCenters()

# Calculate distances to center
from pyspark.sql.functions import udf
from pyspark.sql.types import DoubleType

@udf(DoubleType())
def calculate_distance(features, center):
    return float(np.linalg.norm(np.array(features) - np.array(center)))

# Add distance column
df_with_distance = model.transform(test_data)
df_with_distance = df_with_distance.withColumn(
    "distance",
    calculate_distance(df_with_distance.features, df_with_distance.prediction)
)

# Flag anomalies (distance > threshold)
anomalies = df_with_distance.filter(df_with_distance.distance > threshold)
```

### Pattern 4: Time Series Forecasting

```python
from pyspark.ml.regression import RandomForestRegressor
from pyspark.ml.feature import VectorAssembler

# Create time-based features
df = df.withColumn("hour", hour("timestamp"))
df = df.withColumn("day_of_week", dayofweek("timestamp"))
df = df.withColumn("month", month("timestamp"))

# Create lag features
from pyspark.sql.functions import lag
df = df.withColumn("lag_1", lag("value", 1).over(window))
df = df.withColumn("lag_7", lag("value", 7).over(window))

# Create rolling average
from pyspark.sql.functions import avg
df = df.withColumn("rolling_avg_7", avg("value").over(window_7))

# Assemble features
assembler = VectorAssembler(
    inputCols=["hour", "day_of_week", "month", "lag_1", "lag_7", "rolling_avg_7"],
    outputCol="features"
)

# Train model
rf = RandomForestRegressor(featuresCol="features", labelCol="value")
model = rf.fit(training_data)
```

---

## Conclusion

Spark MLlib provides:

- **Scalable ML algorithms** for distributed processing
- **Feature engineering** utilities for data preparation
- **ML pipelines** for reproducible workflows
- **Model evaluation** metrics for performance assessment
- **Model persistence** for production deployment

Key takeaways:

1. **Use DataFrame-based API** (ML) over RDD-based API (MLlib)
2. **Build pipelines** for reproducible workflows
3. **Cross-validate** models for robust evaluation
4. **Save models** for production deployment
5. **Monitor performance** and retrain as needed

MLlib is essential for building scalable machine learning applications on Spark.