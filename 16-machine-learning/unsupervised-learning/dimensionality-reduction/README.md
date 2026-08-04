# Dimensionality Reduction

## Overview

Dimensionality reduction techniques transform high-dimensional data into lower-dimensional representations while preserving important structure and patterns.

## Principal Component Analysis (PCA)

### Theory

Finds orthogonal directions (principal components) that maximize variance:

```
Maximize: Var(w^T * X) = w^T * Σ * w
Subject to: ||w|| = 1
```

### Implementation

```python
from sklearn.decomposition import PCA
from sklearn.datasets import load_digits
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
import numpy as np

# Load data
digits = load_digits()
X = digits.data
y = digits.target

# Standardize
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# PCA
pca = PCA(n_components=2)
X_pca = pca.fit_transform(X_scaled)

# Plot
plt.figure(figsize=(10, 8))
scatter = plt.scatter(X_pca[:, 0], X_pca[:, 1], c=y, cmap='tab10', s=10)
plt.colorbar(scatter)
plt.xlabel(f'PC1 ({pca.explained_variance_ratio_[0]:.2%} variance)')
plt.ylabel(f'PC2 ({pca.explained_variance_ratio_[1]:.2%} variance)')
plt.title('PCA of Digits Dataset')
plt.show()
```

### Explained Variance

```python
# Full PCA
pca_full = PCA().fit(X_scaled)
cumulative_var = np.cumsum(pca_full.explained_variance_ratio_)

plt.figure(figsize=(10, 6))
plt.plot(range(1, len(cumulative_var) + 1), cumulative_var, marker='o')
plt.xlabel('Number of Components')
plt.ylabel('Cumulative Explained Variance')
plt.title('PCA Explained Variance')
plt.axhline(y=0.95, color='r', linestyle='--', label='95% variance')
plt.legend()
plt.grid(True)
plt.show()

# Find number of components for 95% variance
n_components_95 = np.argmax(cumulative_var >= 0.95) + 1
print(f"Components for 95% variance: {n_components_95}")
```

### PCA Applications

```python
# Noise reduction
from sklearn.datasets import fetch_olivetti_faces

faces = fetch_olivetti_faces()
X_faces = faces.data

# Add noise
np.random.seed(42)
noise = np.random.normal(0, 0.5, X_faces.shape)
X_noisy = X_faces + noise

# PCA denoising
pca = PCA(n_components=100)
X_denoised = pca.inverse_transform(pca.fit_transform(X_noisy))

# Compare
fig, axes = plt.subplots(1, 3, figsize=(15, 5))
axes[0].imshow(X_faces[0].reshape(64, 64), cmap='gray')
axes[0].set_title('Original')
axes[1].imshow(X_noisy[0].reshape(64, 64), cmap='gray')
axes[1].set_title('Noisy')
axes[2].imshow(X_denoised[0].reshape(64, 64), cmap='gray')
axes[2].set_title('Denoised (PCA)')
plt.show()
```

---

## t-SNE

### Theory

t-Distributed Stochastic Neighbor Embedding preserves local structure:

1. Compute pairwise similarities in high dimensions (Gaussian)
2. Compute pairwise similarities in low dimensions (t-distribution)
3. Minimize KL divergence between distributions

### Implementation

```python
from sklearn.manifold import TSNE

# t-SNE
tsne = TSNE(n_components=2, perplexity=30, random_state=42)
X_tsne = tsne.fit_transform(X_scaled)

# Plot
plt.figure(figsize=(10, 8))
scatter = plt.scatter(X_tsne[:, 0], X_tsne[:, 1], c=y, cmap='tab10', s=10)
plt.colorbar(scatter)
plt.title('t-SNE of Digits Dataset')
plt.show()
```

### Perplexity Effect

```python
perplexities = [5, 10, 30, 50, 100]

fig, axes = plt.subplots(1, 5, figsize=(25, 5))
for ax, perp in zip(axes, perplexities):
    tsne = TSNE(n_components=2, perplexity=perp, random_state=42)
    X_tsne = tsne.fit_transform(X_scaled)
    ax.scatter(X_tsne[:, 0], X_tsne[:, 1], c=y, cmap='tab10', s=10)
    ax.set_title(f'Perplexity={perp}')
plt.tight_layout()
plt.show()
```

---

## UMAP

### Theory

Uniform Manifold Approximation and Projection:
- Preserves both local and global structure
- Faster than t-SNE
- Better scaling to large datasets

### Implementation

```python
import umap

# UMAP
reducer = umap.UMAP(n_components=2, n_neighbors=15, min_dist=0.1, random_state=42)
X_umap = reducer.fit_transform(X_scaled)

# Plot
plt.figure(figsize=(10, 8))
scatter = plt.scatter(X_umap[:, 0], X_umap[:, 1], c=y, cmap='tab10', s=10)
plt.colorbar(scatter)
plt.title('UMAP of Digits Dataset')
plt.show()
```

---

## Comparison

| Method | Preserves | Speed | Scalability |
|--------|-----------|-------|-------------|
| PCA | Global variance | Fast | High |
| t-SNE | Local structure | Slow | Low |
| UMAP | Local + Global | Medium | Medium |

## Best Practices

1. **Scale features**: Standardize before PCA/t-SNE/UMAP
2. **PCA first**: Use for initial exploration and noise reduction
3. **t-SNE**: Good for visualization, not for clustering
4. **UMAP**: Good balance of speed and quality
5. **Multiple runs**: t-SNE/UMAP are non-deterministic

## Further Reading

- "Visualizing Data using t-SNE" by van der Maaten
- "UMAP: Uniform Manifold Approximation" by McInnes
- Scikit-learn dimensionality reduction docs
