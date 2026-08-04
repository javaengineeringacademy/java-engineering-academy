# Spark MLlib

## Table of Contents

- [Overview](#overview)
- [ML Pipeline API](#ml-pipeline-api)
- [Feature Engineering](#feature-engineering)
- [Classification Algorithms](#classification-algorithms)
- [Regression Algorithms](#regression-algorithms)
- [Clustering Algorithms](#clustering-algorithms)
- [Collaborative Filtering](#collaborative-filtering)
- [Dimensionality Reduction](#dimensionality-reduction)
- [Model Evaluation](#model-evaluation)
- [Hyperparameter Tuning](#hyperparameter-tuning)
- [Model Persistence](#model-persistence)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

MLlib is Spark's machine learning library providing scalable implementations
of common ML algorithms and utilities. It supports three main APIs: RDD-based
(legacy), DataFrame-based (ML), and Structured Streaming ML.

### Key Characteristics

- **Scalable**: Distributed computing for large datasets
- **Integrated**: Works with Spark SQL, Streaming, and GraphX
- **Pipeline API**: Standardized ML workflow components
- **Algorithms**: Classification, regression, clustering, recommendation
- **Utilities**: Feature engineering, model evaluation, tuning

### When to Use MLlib

- Large-scale machine learning on distributed data
- Feature engineering on big datasets
- Model training on terabytes of data
- Real-time prediction with streaming
- Recommendation systems at scale

### MLlib vs scikit-learn

| Feature | MLlib | scikit-learn |
|---------|-------|--------------|
| Scale | Distributed (TB+) | Single machine (GB) |
| Speed | Fast on large data | Fast on small data |
| Algorithms | Comprehensive | More comprehensive |
| Pipelines | Built-in | Manual composition |
| Ease of Use | Moderate | Easy |
| Ecosystem | Spark integration | Standalone |

---

## ML Pipeline API

### Pipeline Components

```python
from pyspark.ml import Pipeline
from pyspark.ml.feature import VectorAssembler, StringIndexer
from pyspark.ml.classification import RandomForestClassifier

# Define pipeline stages
indexer = StringIndexer(inputCol="category", outputCol="categoryIndex")
assembler = VectorAssembler(inputCols=["categoryIndex", "feature1", "feature2"],
                           outputCol="features")
classifier = RandomForestClassifier(featuresCol="features", labelCol="label")

# Create pipeline
pipeline = Pipeline(stages=[indexer, assembler, classifier])

# Fit pipeline
model = pipeline.fit(training_data)

# Transform data
predictions = model.transform(test_data)
```

### Transformers

```python
from pyspark.ml import Transformer
from pyspark.ml.param.shared import HasInputCol, HasOutputCol

class CustomTransformer(Transformer):
    def __init__(self, inputCol="input", outputCol="output"):
        super().__init__()
        self.inputCol = inputCol
        self.outputCol = outputCol

    def _transform(self, dataset):
        # Implement transformation logic
        return dataset.withColumn(
            self.outputCol,
            custom_function(dataset[self.inputCol])
        )
```

### Estimators

```python
from pyspark.ml import Estimator
from pyspark.ml.param.shared import HasFeaturesCol, HasLabelCol

class CustomEstimator(Estimator):
    def __init__(self, featuresCol="features", labelCol="label"):
        super().__init__()
        self.featuresCol = featuresCol
        self.labelCol = labelCol

    def _fit(self, dataset):
        # Implement training logic
        return CustomModel(featuresCol=self.featuresCol)
```

---

## Feature Engineering

### Feature Transformers

```python
from pyspark.ml.feature import (
    VectorAssembler, StringIndexer, OneHotEncoder,
    StandardScaler, MinMaxScaler, Normalizer,
    HashingTF, IDF, Tokenizer, StopWordsRemover,
    PCA, FeatureHasher, Bucketizer
)

# VectorAssembler
assembler = VectorAssembler(
    inputCols=["feature1", "feature2", "feature3"],
    outputCol="features"
)
df = assembler.transform(data)

# StringIndexer
indexer = StringIndexer(inputCol="category", outputCol="categoryIndex")
df = indexer.fit(data).transform(data)

# OneHotEncoder
encoder = OneHotEncoder(inputCol="categoryIndex", outputCol="categoryVec")
df = encoder.fit(df).transform(df)

# StandardScaler
scaler = StandardScaler(inputCol="features", outputCol="scaledFeatures",
                       withStd=True, withMean=False)
scalerModel = scaler.fit(df)
df = scalerModel.transform(df)

# MinMaxScaler
scaler = MinMaxScaler(inputCol="features", outputCol="scaledFeatures")
scalerModel = scaler.fit(df)
df = scalerModel.transform(df)

# Normalizer
normalizer = Normalizer(inputCol="features", outputCol="normFeatures", p=2.0)
df = normalizer.transform(df)

# PCA
pca = PCA(k=3, inputCol="features", outputCol="pcaFeatures")
pcaModel = pca.fit(df)
df = pcaModel.transform(df)

# Bucketizer
splits = [-float("inf"), -10, 0, 10, float("inf")]
bucketizer = Bucketizer(splits=splits, inputCol="value", outputCol="buckets")
df = bucketizer.transform(df)
```

### Text Feature Engineering

```python
from pyspark.ml.feature import (
    Tokenizer, RegexTokenizer, StopWordsRemover,
    HashingTF, IDF, Word2Vec, CountVectorizer
)

# Tokenizer
tokenizer = Tokenizer(inputCol="text", outputCol="words")
df = tokenizer.transform(data)

# RegexTokenizer
regexTokenizer = RegexTokenizer(
    inputCol="text", outputCol="words", pattern="\\W+"
)
df = regexTokenizer.transform(data)

# Stop Words Remover
remover = StopWordsRemover(inputCol="words", outputCol="filteredWords")
df = remover.transform(df)

# HashingTF
hashingTF = HashingTF(inputCol="filteredWords", outputCol="rawFeatures", numFeatures=1000)
df = hashingTF.transform(df)

# IDF
idf = IDF(inputCol="rawFeatures", outputCol="features")
idfModel = idf.fit(df)
df = idfModel.transform(df)

# Word2Vec
word2Vec = Word2Vec(vectorSize=100, minCount=0, inputCol="words", outputCol="features")
word2VecModel = word2Vec.fit(df)
df = word2VecModel.transform(df)

# CountVectorizer
cv = CountVectorizer(inputCol="words", outputCol="features")
cvModel = cv.fit(df)
df = cvModel.transform(df)
```

### Feature Selection

```python
from pyspark.ml.feature import (
    ChiSqSelector, UnivariateFeatureSelector,
    VectorSlicer
)

# Chi-Squared selector
selector = ChiSqSelector(
    numTopFeatures=10,
    featuresCol="features",
    labelCol="label",
    selectorType="numTopFeatures",
    outputCol="selectedFeatures"
)
selectorModel = selector.fit(df)
df = selectorModel.transform(df)

# Univariate feature selector
selector = UnivariateFeatureSelector(
    featuresCol="features",
    labelCol="label",
    selectionMode="numTopFeatures",
    numTopFeatures=10
)
selectorModel = selector.fit(df)
df = selectorModel.transform(df)

# VectorSlicer
slicer = VectorSlicer(inputCol="features", outputCol="selectedFeatures",
                     indices=[0, 2, 5])
df = slicer.transform(df)
```

---

## Classification Algorithms

### Logistic Regression

```python
from pyspark.ml.classification import LogisticRegression

lr = LogisticRegression(
    maxIter=100,
    regParam=0.01,
    elasticNetParam=0.8,
    featuresCol="features",
    labelCol="label",
    probabilityCol="probability",
    rawPredictionCol="rawPrediction"
)

lrModel = lr.fit(trainingData)
predictions = lrModel.transform(testData)

# Access model coefficients
print(lrModel.coefficients)
print(lrModel.intercept)
```

### Decision Tree

```python
from pyspark.ml.classification import DecisionTreeClassifier

dt = DecisionTreeClassifier(
    maxDepth=5,
    maxBins=32,
    minInstancesPerNode=1,
    minInfoGain=0.0,
    impurity="gini",
    featuresCol="features",
    labelCol="label"
)

dtModel = dt.fit(trainingData)
predictions = dtModel.transform(testData)

# Access tree structure
print(dtModel.toDebugString)
```

### Random Forest

```python
from pyspark.ml.classification import RandomForestClassifier

rf = RandomForestClassifier(
    numTrees=100,
    maxDepth=10,
    maxBins=32,
    featureSubsetStrategy="auto",
    impurity="gini",
    seed=42,
    featuresCol="features",
    labelCol="label"
)

rfModel = rf.fit(trainingData)
predictions = rfModel.transform(testData)

# Feature importance
print(rfModel.featureImportances)
```

### Gradient-Boosted Trees

```python
from pyspark.ml.classification import GBTClassifier

gbt = GBTClassifier(
    maxIter=100,
    maxDepth=5,
    maxBins=32,
    stepSize=0.1,
    subsamplingRate=0.8,
    seed=42,
    featuresCol="features",
    labelCol="label"
)

gbtModel = gbt.fit(trainingData)
predictions = gbtModel.transform(testData)
```

### Support Vector Machine

```python
from pyspark.ml.classification import LinearSVC

lsvc = LinearSVC(
    maxIter=100,
    regParam=0.1,
    featuresCol="features",
    labelCol="label"
)

lsvcModel = lsvc.fit(trainingData)
predictions = lsvcModel.transform(testData)
```

### Naive Bayes

```python
from pyspark.ml.classification import NaiveBayes

nb = NaiveBayes(
    modelType="multinomial",
    smoothing=1.0,
    featuresCol="features",
    labelCol="label"
)

nbModel = nb.fit(trainingData)
predictions = nbModel.transform(testData)
```

### Multi-Layer Perceptron

```python
from pyspark.ml.classification import MultilayerPerceptronClassifier

mlp = MultilayerPerceptronClassifier(
    layers=[4, 128, 64, 2],
    maxIter=200,
    featuresCol="features",
    labelCol="label"
)

mlpModel = mlp.fit(trainingData)
predictions = mlpModel.transform(testData)
```

---

## Regression Algorithms

### Linear Regression

```python
from pyspark.ml.regression import LinearRegression

lr = LinearRegression(
    maxIter=100,
    regParam=0.0,
    elasticNetParam=0.0,
    featuresCol="features",
    labelCol="label"
)

lrModel = lr.fit(trainingData)
predictions = lrModel.transform(testData)

# Access model
print(lrModel.coefficients)
print(lrModel.intercept)
print(lrModel.summary.r2)
```

### Decision Tree Regression

```python
from pyspark.ml.regression import DecisionTreeRegressor

dt = DecisionTreeRegressor(
    maxDepth=5,
    maxBins=32,
    minInstancesPerNode=1,
    minInfoGain=0.0,
    impurity="variance",
    featuresCol="features",
    labelCol="label"
)

dtModel = dt.fit(trainingData)
predictions = dtModel.transform(testData)
```

### Random Forest Regression

```python
from pyspark.ml.regression import RandomForestRegressor

rf = RandomForestRegressor(
    numTrees=100,
    maxDepth=10,
    maxBins=32,
    featureSubsetStrategy="auto",
    impurity="variance",
    seed=42,
    featuresCol="features",
    labelCol="label"
)

rfModel = rf.fit(trainingData)
predictions = rfModel.transform(testData)
```

### GBT Regression

```python
from pyspark.ml.regression import GBTRegressor

gbt = GBTRegressor(
    maxIter=100,
    maxDepth=5,
    maxBins=32,
    stepSize=0.1,
    subsamplingRate=0.8,
    seed=42,
    featuresCol="features",
    labelCol="label"
)

gbtModel = gbt.fit(trainingData)
predictions = gbtModel.transform(testData)
```

### Survival Regression

```python
from pyspark.ml.regression import AFTSurvivalRegression

aft = AFTSurvivalRegression(
    maxIter=100,
    featuresCol="features",
    labelCol="label"
)

aftModel = aft.fit(trainingData)
predictions = aftModel.transform(testData)
```

---

## Clustering Algorithms

### K-Means

```python
from pyspark.ml.clustering import KMeans

kmeans = KMeans(
    k=3,
    seed=42,
    maxIter=20,
    initMode="k-means||",
    initSteps=5,
    tol=0.0001,
    distanceMeasure="euclidean",
    featuresCol="features",
    predictionCol="prediction"
)

kmeansModel = kmeans.fit(trainingData)
predictions = kmeansModel.transform(testData)

# Access cluster centers
centers = kmeansModel.clusterCenters()
print(centers)

# Cost (within-cluster sum of squares)
print(kmeansModel.summary.trainingCost)
```

### Bisecting K-Means

```python
from pyspark.ml.clustering import BisectingKMeans

bkm = BisectingKMeans(
    k=3,
    maxIter=20,
    minDivisibleClusterSize=1.0,
    seed=42,
    featuresCol="features",
    predictionCol="prediction"
)

bkmModel = bkm.fit(trainingData)
predictions = bkmModel.transform(testData)
```

### Gaussian Mixture Model

```python
from pyspark.ml.clustering import GaussianMixture

gmm = GaussianMixture(
    k=3,
    maxIter=100,
    tol=0.01,
    seed=42,
    featuresCol="features",
    predictionCol="prediction",
    probabilityCol="probability"
)

gmmModel = gmm.fit(trainingData)
predictions = gmmModel.transform(testData)

# Access model parameters
print(gmmModel.gaussiansDF.show())
print(gmmModel.weights)
```

### LDA (Latent Dirichlet Allocation)

```python
from pyspark.ml.clustering import LDA

lda = LDA(
    k=10,
    maxIter=20,
    optimizer="online",
    learningDecay=0.7,
    subsamplingRate=0.05,
    topicDistributionCol="topicDistribution",
    featuresCol="features"
)

ldaModel = lda.fit(trainingData)
transformed = ldaModel.transform(testData)

# Access topics
topics = ldaModel.topicsMatrix
print(topics)
```

---

## Collaborative Filtering

### ALS (Alternating Least Squares)

```python
from pyspark.ml.recommendation import ALS

als = ALS(
    maxIter=10,
    regParam=0.1,
    rank=10,
    userCol="userId",
    itemCol="itemId",
    ratingCol="rating",
    coldStartStrategy="drop",
    nonnegative=True,
    implicitPrefs=False
)

alsModel = als.fit(trainingData)
predictions = alsModel.transform(testData)

# Generate recommendations
# For all users
userRecs = alsModel.recommendForAllUsers(10)

# For all items
itemRecs = alsModel.recommendForAllItems(10)

# For specific users
userRecs = alsModel.recommendForUserSubset(users, 10)

# For specific items
itemRecs = alsModel.recommendForItemSubset(items, 10)

# Access factors
userFactors = alsModel.userFactors
itemFactors = alsModel.itemFactors
```

### Recommendation Patterns

```python
# Implicit feedback
als = ALS(
    maxIter=10,
    regParam=0.1,
    implicitPrefs=True,
    userCol="userId",
    itemCol="itemId",
    ratingCol="implicitRating"
)

# Cold start handling
als = ALS(
    coldStartStrategy="drop"  # or "nan"
)

# Cross-validation for hyperparameter tuning
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder

paramGrid = ParamGridBuilder() \
    .addGrid(als.rank, [10, 20, 30]) \
    .addGrid(als.regParam, [0.01, 0.1, 0.5]) \
    .build()

crossval = CrossValidator(
    estimator=als,
    estimatorParamMaps=paramGrid,
    evaluator=evaluator,
    numFolds=3
)
```

---

## Dimensionality Reduction

### PCA (Principal Component Analysis)

```python
from pyspark.ml.feature import PCA

pca = PCA(
    k=10,
    inputCol="features",
    outputCol="pcaFeatures"
)

pcaModel = pca.fit(df)
df = pcaModel.transform(df)

# Access principal components
print(pcaModel.pc)
print(pcaModel.explainedVariance)
```

### SVD (Singular Value Decomposition)

```python
from pyspark.mllib.linalg.distributed import (
    RowMatrix, IndexedRowMatrix, CoordinateMatrix
)

# Create RowMatrix
rowMatrix = RowMatrix(rdd)

# Compute SVD
svd = rowMatrix.computeSVD(k=10, computeU=True)

# Access factors
U = svd.U
S = svd.s
V = svd.V
```

### Truncated SVD

```python
from pyspark.ml.feature import TruncatedSVD

svd = TruncatedSVD(
    k=10,
    inputCol="features",
    outputCol="svdFeatures"
)

svdModel = svd.fit(df)
df = svdModel.transform(df)
```

---

## Model Evaluation

### Classification Evaluation

```python
from pyspark.ml.evaluation import (
    BinaryClassificationEvaluator,
    MulticlassClassificationEvaluator
)

# Binary classification evaluator
evaluator = BinaryClassificationEvaluator(
    labelCol="label",
    rawPredictionCol="rawPrediction",
    metricName="areaUnderROC"
)

auc = evaluator.evaluate(predictions)
print(f"AUC: {auc}")

# Multiclass classification evaluator
evaluator = MulticlassClassificationEvaluator(
    labelCol="label",
    predictionCol="prediction",
    metricName="accuracy"
)

accuracy = evaluator.evaluate(predictions)
print(f"Accuracy: {accuracy}")

# Available metrics
metrics = ["accuracy", "f1", "weightedPrecision", "weightedRecall",
           "weightedFMeasure", "weightedPrecision", "weightedRecall"]
```

### Regression Evaluation

```python
from pyspark.ml.evaluation import RegressionEvaluator

evaluator = RegressionEvaluator(
    labelCol="label",
    predictionCol="prediction",
    metricName="rmse"
)

rmse = evaluator.evaluate(predictions)
print(f"RMSE: {rmse}")

# Available metrics
metrics = ["rmse", "mse", "mae", "r2", "msle", "rmsle"]
```

### Ranking Evaluation

```python
from pyspark.ml.evaluation import RankingEvaluator

evaluator = RankingEvaluator(
    labelCol="label",
    predictionCol="prediction",
    metricName="ndcgAtK"
)

ndcg = evaluator.evaluate(predictions)
print(f"NDCG: {ndcg}")

# Available metrics
metrics = ["meanAveragePrecision", "ndcgAtK", "precisionAtK", "recallAtK"]
```

---

## Hyperparameter Tuning

### Cross-Validation

```python
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml.evaluation import BinaryClassificationEvaluator

# Define parameter grid
paramGrid = ParamGridBuilder() \
    .addGrid(rf.numTrees, [10, 50, 100]) \
    .addGrid(rf.maxDepth, [5, 10, 20]) \
    .addGrid(rf.featureSubsetStrategy, ["auto", "sqrt", "log2"]) \
    .build()

# Create cross-validator
crossval = CrossValidator(
    estimator=rf,
    estimatorParamMaps=paramGrid,
    evaluator=BinaryClassificationEvaluator(),
    numFolds=3,
    seed=42
)

# Fit cross-validator
cvModel = crossval.fit(trainingData)

# Get best model
bestModel = cvModel.bestModel
print(bestModel.getNumTrees)
print(bestModel.getMaxDepth)

# Make predictions
predictions = cvModel.transform(testData)
```

### Train-Validation Split

```python
from pyspark.ml.tuning import TrainValidationSplit

# Create train-validation split
tvs = TrainValidationSplit(
    estimator=rf,
    estimatorParamMaps=paramGrid,
    evaluator=BinaryClassificationEvaluator(),
    trainRatio=0.8,
    seed=42
)

# Fit
tvsModel = tvs.fit(trainingData)

# Get best model
bestModel = tvsModel.bestModel
```

### Hyperparameter Search Strategies

```python
# Grid search (exhaustive)
paramGrid = ParamGridBuilder() \
    .addGrid(lr.regParam, [0.01, 0.1, 1.0]) \
    .addGrid(lr.maxIter, [10, 50, 100]) \
    .build()

# Random search
import random

paramGrid = []
for _ in range(20):
    paramGrid.append({
        lr.regParam: random.uniform(0.01, 1.0),
        lr.maxIter: random.randint(10, 100)
    })

# Bayesian optimization (not built-in, use external libraries)
```

---

## Model Persistence

### Saving and Loading Models

```python
# Save model
model.write().overwrite().save("path/to/model")

# Load model
from pyspark.ml.classification import RandomForestClassificationModel
model = RandomForestClassificationModel.load("path/to/model")

# Save pipeline
pipeline.write().overwrite().save("path/to/pipeline")

# Load pipeline
from pyspark.ml import PipelineModel
pipeline = PipelineModel.load("path/to/pipeline")

# Save as JSON
model.write().overwrite().save("path/to/model.json")

# Load from JSON
model = RandomForestClassificationModel.load("path/to/model.json")
```

### Model Export

```python
# Export to PMML
model.toPMML("path/to/model.pmml")

# Export to JSON
model.save("path/to/model.json")

# Export to MOJO (for scoring)
from pyspark.ml.classification import RandomForestClassificationModel
model.save("path/to/model.mojo")
```

---

## Best Practices

### Data Preparation

1. **Handle missing values**: Use `Imputer` or `fillna()`
2. **Normalize features**: Use `StandardScaler` or `MinMaxScaler`
3. **Encode categorical variables**: Use `StringIndexer` and `OneHotEncoder`
4. **Balance classes**: Use `ClassBalancer` or resampling

### Feature Engineering

1. **Feature selection**: Use `ChiSqSelector` or feature importance
2. **Dimensionality reduction**: Use PCA or SVD for high-dimensional data
3. **Feature interactions**: Use `VectorAssembler` for feature combinations
4. **Text features**: Use TF-IDF or Word2Vec for text data

### Model Selection

1. **Start simple**: Begin with linear models
2. **Cross-validate**: Use proper validation splits
3. **Tune hyperparameters**: Use grid or random search
4. **Ensemble methods**: Combine multiple models

### Production Deployment

```python
# Save model for production
model.write().overwrite().save("hdfs://models/classifier")

# Load in production
model = RandomForestClassificationModel.load("hdfs://models/classifier")

# Score new data
predictions = model.transform(newData)

# Monitor model performance
accuracy = evaluator.evaluate(predictions)
```

---

## Examples

### Complete ML Pipeline

```python
from pyspark.sql import SparkSession
from pyspark.ml import Pipeline
from pyspark.ml.feature import (
    VectorAssembler, StringIndexer, StandardScaler
)
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml.evaluation import BinaryClassificationEvaluator
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder

spark = SparkSession.builder.appName("MLPipeline").getOrCreate()

# Load data
data = spark.read.parquet("data.parquet")

# Split data
train, test = data.randomSplit([0.8, 0.2], seed=42)

# Feature engineering
indexer = StringIndexer(inputCol="category", outputCol="categoryIndex")
assembler = VectorAssembler(
    inputCols=["categoryIndex", "feature1", "feature2", "feature3"],
    outputCol="rawFeatures"
)
scaler = StandardScaler(inputCol="rawFeatures", outputCol="features")

# Classifier
classifier = RandomForestClassifier(
    featuresCol="features",
    labelCol="label",
    seed=42
)

# Create pipeline
pipeline = Pipeline(stages=[indexer, assembler, scaler, classifier])

# Hyperparameter tuning
paramGrid = ParamGridBuilder() \
    .addGrid(classifier.numTrees, [50, 100, 200]) \
    .addGrid(classifier.maxDepth, [5, 10, 15]) \
    .build()

# Cross-validation
crossval = CrossValidator(
    estimator=pipeline,
    estimatorParamMaps=paramGrid,
    evaluator=BinaryClassificationEvaluator(),
    numFolds=3,
    seed=42
)

# Train model
cvModel = crossval.fit(train)

# Evaluate
predictions = cvModel.transform(test)
evaluator = BinaryClassificationEvaluator()
auc = evaluator.evaluate(predictions)
print(f"AUC: {auc}")

# Save model
cvModel.bestModel.write().overwrite().save("hdfs://models/rf_classifier")
```

---

## References

- [Spark MLlib Programming Guide](https://spark.apache.org/docs/latest/ml-guide.html)
- [MLlib API Reference](https://spark.apache.org/docs/latest/api/python/reference/pyspark.ml.html)
- [ML Pipelines](https://spark.apache.org/docs/latest/ml-pipeline.html)
- [Feature Transformers](https://spark.apache.org/docs/latest/ml-features.html)
- [Machine Learning with Spark](http://shop.oreilly.com/product/0636920028512.do)
