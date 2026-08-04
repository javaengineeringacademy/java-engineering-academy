# Module 16: Machine Learning

## Overview

Machine learning is a subset of artificial intelligence that enables systems to learn from data and improve over time. This module covers supervised, unsupervised, and reinforcement learning, NLP, computer vision, MLOps, and more.

## Table of Contents

### Supervised Learning
| Topic | Description |
|-------|-------------|
| [Classification](supervised-learning/classification/README.md) | Logistic regression, SVM, trees |
| [Regression](supervised-learning/regression/README.md) | Linear, polynomial, ridge |
| [Decision Trees](supervised-learning/decision-trees/README.md) | Trees, random forests |
| [SVM](supervised-learning/svm/README.md) | Support Vector Machines |
| [Naive Bayes](supervised-learning/naive-bayes/README.md) | Probabilistic classifier |
| [KNN](supervised-learning/knn/README.md) | K-Nearest Neighbors |
| [Neural Networks](supervised-learning/neural-networks/README.md) | Deep learning basics |
| [Ensemble](supervised-learning/ensemble/README.md) | Boosting, bagging |

### Unsupervised Learning
| Topic | Description |
|-------|-------------|
| [Clustering](unsupervised-learning/clustering/README.md) | K-means, DBSCAN, hierarchical |
| [Dimensionality Reduction](unsupervised-learning/dimensionality-reduction/README.md) | PCA, t-SNE, UMAP |
| [Anomaly Detection](unsupervised-learning/anomaly-detection/README.md) | Isolation forest, autoencoders |
| [Pattern Mining](unsupervised-learning/pattern-mining/README.md) | Association rules, FP-Growth |

### Reinforcement Learning
| Topic | Description |
|-------|-------------|
| [RL Basics](reinforcement-learning/basics/README.md) | Q-learning, MDP |
| [Policy Gradient](reinforcement-learning/policy-gradient/README.md) | REINFORCE, A2C |
| [Deep RL](reinforcement-learning/deep-rl/README.md) | DQN, PPO |

### Natural Language Processing
| Topic | Description |
|-------|-------------|
| [Text Preprocessing](nlp/text-preprocessing/README.md) | Tokenization, stemming |
| [Sentiment Analysis](nlp/sentiment-analysis/README.md) | Positive/negative classification |
| [NER](nlp/named-entity-recognition/README.md) | Named entity recognition |
| [Machine Translation](nlp/machine-translation/README.md) | Seq2seq, transformers |
| [Text Classification](nlp/text-classification/README.md) | Topic classification |
| [LLM](nlp/llm/README.md) | Large language models |
| [Embeddings](nlp/embeddings/README.md) | Word2Vec, GloVe, BERT |
| [Transformers](nlp/transformers/README.md) | Attention mechanism |
| [Prompt Engineering](nlp/prompt-engineering/README.md) | LLM patterns |

### Computer Vision
| Topic | Description |
|-------|-------------|
| [Image Classification](cv/image-classification/README.md) | ResNet, EfficientNet |
| [Object Detection](cv/object-detection/README.md) | YOLO, Faster R-CNN |
| [Segmentation](cv/segmentation/README.md) | Semantic, instance |
| [GAN](cv/gan/README.md) | Generative Adversarial Networks |
| [CNN](cv/cnn/README.md) | Convolutional Neural Networks |
| [Vision Transformers](cv/vision-transformers/README.md) | ViT, DeiT |

### MLOps
| Topic | Description |
|-------|-------------|
| [Experiment Tracking](mlops/experiment-tracking/README.md) | MLflow, W&B, TensorBoard |
| [Model Deployment](mlops/model-deployment/README.md) | Serving, TensorFlow Serving |
| [Feature Engineering](mlops/feature-engineering/README.md) | Feature stores, pipelines |
| [Model Monitoring](mlops/model-monitoring/README.md) | Drift detection |
| [AutoML](mlops/automl/README.md) | Hyperparameter tuning |
| [ML Pipeline](mlops/pipeline/README.md) | Orchestration |

### Time Series
| Topic | Description |
|-------|-------------|
| [Forecasting](time-series/forecasting/README.md) | ARIMA, Prophet, LSTM |
| [Anomaly Detection](time-series/anomaly/README.md) | Time series anomalies |
| [Classification](time-series/classification/README.md) | Time series classification |

### Recommendation Systems
| Topic | Description |
|-------|-------------|
| [Collaborative Filtering](recommendation/collaborative/README.md) | User/item-based |
| [Content-Based](recommendation/content-based/README.md) | Feature-based |
| [Hybrid](recommendation/hybrid/README.md) | Combined approaches |

### Explainable AI (XAI)
| Topic | Description |
|-------|-------------|
| [SHAP](xai/shap/README.md) | SHAP values |
| [LIME](xai/lime/README.md) | Local explanations |
| [Attention Visualization](xai/attention-viz/README.md) | Attention maps |

### Ethics in ML
| Topic | Description |
|-------|-------------|
| [Bias](ethics/bias/README.md) | Bias detection |
| [Fairness](ethics/fairness/README.md) | Fairness metrics |
| [Interpretability](ethics/interpretability/README.md) | Model interpretability |

### Data Preprocessing
| Topic | Description |
|-------|-------------|
| [Data Cleaning](data-preprocessing/cleaning/README.md) | Missing values, outliers |
| [Feature Selection](data-preprocessing/feature-selection/README.md) | Feature importance |
| [Feature Scaling](data-preprocessing/scaling/README.md) | Normalization, standardization |
| [Feature Encoding](data-preprocessing/encoding/README.md) | One-hot, label encoding |

### Model Selection
| Topic | Description |
|-------|-------------|
| [Cross-Validation](model-selection/cross-validation/README.md) | K-fold, stratified |
| [Hyperparameter Tuning](model-selection/hyperparameter-tuning/README.md) | Grid search, Bayesian |
| [Evaluation Metrics](model-selection/metrics/README.md) | Accuracy, F1, AUC |

### Frameworks
| Topic | Description |
|-------|-------------|
| [PyTorch](frameworks/pytorch/README.md) | Dynamic computation graphs |
| [TensorFlow](frameworks/tensorflow/README.md) | Static graphs, Keras |
| [Scikit-learn](frameworks/scikit-learn/README.md) | Classical ML |
| [XGBoost](frameworks/xgboost/README.md) | Gradient boosting |
| [Hugging Face](frameworks/hugging-face/README.md) | Transformers, models |

## Key Concepts

### ML Workflow
```
Data Collection → Preprocessing → Feature Engineering → Model Training
       ↑                                                    ↓
  Data Source ←←←←←←←←←← Model Evaluation ←←←←←←←←←←←←←←←←
                                                      ↓
                                              Model Deployment
                                                      ↓
                                             Monitoring & Retraining
```

### Learning Paradigms
1. **Supervised Learning** - Labeled data, learn mapping
2. **Unsupervised Learning** - No labels, find patterns
3. **Reinforcement Learning** - Agent learns from environment
4. **Self-Supervised Learning** - Learn from unlabeled data
5. **Transfer Learning** - Leverage pre-trained models

### Model Evaluation
| Task | Metrics |
|------|---------|
| Classification | Accuracy, Precision, Recall, F1, AUC |
| Regression | MSE, RMSE, MAE, R² |
| Clustering | Silhouette, Davies-Bouldin |
| Ranking | NDCG, MAP, MRR |

### Deep Learning Architectures
```
CNN → Image tasks (classification, detection)
RNN/LSTM → Sequential data (time series, NLP)
Transformers → All tasks (NLP, CV, multimodal)
GAN → Generative tasks
VAE → Generative tasks
Autoencoder → Anomaly detection, compression
```

## Learning Path

1. Start with data preprocessing and EDA
2. Learn classical ML (scikit-learn)
3. Master supervised learning algorithms
4. Explore unsupervised learning
5. Study deep learning fundamentals (PyTorch/TensorFlow)
6. Specialize in NLP or Computer Vision
7. Implement MLOps practices
8. Explore advanced topics (RL, GANs, Transformers)
9. Focus on ethics and responsible AI
