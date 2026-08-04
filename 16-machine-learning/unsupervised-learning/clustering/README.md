# Clustering Algorithms

## Overview

Clustering groups similar data points together without labeled data. It discovers natural patterns and structures in the data.

## K-Means Clustering

### Theory

Minimizes within-cluster sum of squares:

```
Minimize: Σ Σ ||x - μₖ||²
where μₖ is the centroid of cluster k
```

### Algorithm
```
1. Initialize K centroids randomly
2. Repeat:
   a. Assign each point to nearest centroid
   b. Update centroids as mean of assigned points
3. Until convergence
```

### Implementation

```python
from sklearn.cluster import KMeans
from sklearn.datasets import make_blobs
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
import numpy as np

# Generate data
X, y_true = make_blobs(n_samples=300, centers=4, cluster_std=0.60, random_state=0)

# K-Means
kmeans = KMeans(n_clusters=4, init='k-means++', n_init=10, random_state=42)
y_kmeans = kmeans.fit_predict(X)

# Plot
plt.figure(figsize=(10, 8))
plt.scatter(X[:, 0], X[:, 1], c=y_kmeans, s=50, cmap='viridis')
centers = kmeans.cluster_centers_
plt.scatter(centers[:, 0], centers[:, 1], c='red', s=200, alpha=0.7, marker='X')
plt.title('K-Means Clustering')
plt.show()
```

### Elbow Method

```python
inertias = []
K_range = range(1, 11)

for k in K_range:
    kmeans = KMeans(n_clusters=k, random_state=42)
    kmeans.fit(X)
    inertias.append(kmeans.inertia_)

plt.figure(figsize=(10, 6))
plt.plot(K_range, inertias, marker='o')
plt.xlabel('Number of Clusters (K)')
plt.ylabel('Inertia')
plt.title('Elbow Method for Optimal K')
plt.grid(True)
plt.show()
```

### Silhouette Analysis

```python
from sklearn.metrics import silhouette_score

silhouette_scores = []
K_range = range(2, 11)

for k in K_range:
    kmeans = KMeans(n_clusters=k, random_state=42)
    labels = kmeans.fit_predict(X)
    score = silhouette_score(X, labels)
    silhouette_scores.append(score)

plt.figure(figsize=(10, 6))
plt.plot(K_range, silhouette_scores, marker='o')
plt.xlabel('Number of Clusters')
plt.ylabel('Silhouette Score')
plt.title('Silhouette Analysis')
plt.grid(True)
plt.show()
```

---

## DBSCAN

### Theory

Density-Based Spatial Clustering of Applications with Noise:
- Groups points that are closely packed
- Marks outliers in low-density regions

### Parameters
- **ε (eps)**: Maximum distance between two points
- **min_samples**: Minimum points to form a dense region

### Implementation

```python
from sklearn.cluster import DBSCAN
from sklearn.datasets import make_moons

# Generate non-convex data
X_moons, y_moons = make_moons(n_samples=300, noise=0.05, random_state=42)

# DBSCAN
dbscan = DBSCAN(eps=0.3, min_samples=5)
y_dbscan = dbscan.fit_predict(X_moons)

# Plot
plt.figure(figsize=(10, 8))
plt.scatter(X_moons[:, 0], X_moons[:, 1], c=y_dbscan, s=50, cmap='viridis')
plt.title('DBSCAN Clustering')
plt.show()

# Number of clusters (excluding noise)
n_clusters = len(set(y_dbscan)) - (1 if -1 in y_dbscan else 0)
n_noise = list(y_dbscan).count(-1)
print(f"Clusters: {n_clusters}, Noise points: {n_noise}")
```

### Parameter Selection

```python
from sklearn.neighbors import NearestNeighbors

# Find optimal eps using k-distance graph
nn = NearestNeighbors(n_neighbors=5)
nn.fit(X_moons)
distances, indices = nn.kneighbors(X_moons)
distances = np.sort(distances[:, -1])

plt.figure(figsize=(10, 6))
plt.plot(distances)
plt.xlabel('Points')
plt.ylabel('5-NN Distance')
plt.title('K-Distance Graph for DBSCAN')
plt.show()
```

---

## Hierarchical Clustering

### Agglomerative (Bottom-up)

```python
from sklearn.cluster import AgglomerativeClustering
from scipy.cluster.hierarchy import dendrogram, linkage

# Generate data
X_hier, y_hier = make_blobs(n_samples=100, centers=3, random_state=42)

# Linkage matrix
Z = linkage(X_hier, method='ward')

# Dendrogram
plt.figure(figsize=(12, 8))
dendrogram(Z, truncate_mode='lastp', p=30)
plt.title('Hierarchical Clustering Dendrogram')
plt.xlabel('Cluster Size')
plt.ylabel('Distance')
plt.show()

# Agglomerative Clustering
agg = AgglomerativeClustering(n_clusters=3, linkage='ward')
y_agg = agg.fit_predict(X_hier)

plt.figure(figsize=(10, 8))
plt.scatter(X_hier[:, 0], X_hier[:, 1], c=y_agg, s=50, cmap='viridis')
plt.title('Agglomerative Clustering')
plt.show()
```

### Linkage Methods

| Method | Description |
|--------|-------------|
| Ward | Minimizes variance within clusters |
| Complete | Maximum distance between clusters |
| Average | Average distance between clusters |
| Single | Minimum distance between clusters |

---

## Gaussian Mixture Models (GMM)

### Theory

Probabilistic model assuming data is generated from a mixture of Gaussians:

```
p(x) = Σ πₖ * N(x|μₖ, Σₖ)
```

### Implementation

```python
from sklearn.mixture import GaussianMixture

# GMM
gmm = GaussianMixture(n_components=3, covariance_type='full', random_state=42)
y_gmm = gmm.fit_predict(X_hier)

# Probabilities
probs = gmm.predict_proba(X_hier)

# BIC for model selection
bic_scores = []
n_range = range(1, 10)

for n in n_range:
    gmm = GaussianMixture(n_components=n, random_state=42)
    gmm.fit(X_hier)
    bic_scores.append(gmm.bic(X_hier))

plt.figure(figsize=(10, 6))
plt.plot(n_range, bic_scores, marker='o')
plt.xlabel('Number of Components')
plt.ylabel('BIC Score')
plt.title('BIC for GMM')
plt.grid(True)
plt.show()
```

---

## Comparison

| Algorithm | Complexity | Handles Non-Convex | Scalability |
|-----------|------------|-------------------|-------------|
| K-Means | O(nKt) | No | High |
| DBSCAN | O(n log n) | Yes | Medium |
| Hierarchical | O(n²) | Yes | Low |
| GMM | O(nK³) | No | Medium |

## Best Practices

1. **Scale features**: Use StandardScaler
2. **Choose K**: Elbow method, silhouette analysis
3. **DBSCAN**: Use k-distance graph for eps
4. **Evaluation**: Silhouette score, Calinski-Harabasz index
5. **Visualization**: Use t-SNE or PCA for high dimensions

## Further Reading

- "Pattern Recognition and Machine Learning" by Bishop
- Scikit-learn clustering documentation
