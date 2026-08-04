# Word Embeddings

## Overview

Word embeddings map words to dense vector representations, capturing semantic relationships and contextual meaning.

## Word2Vec

### Theory

Two architectures:
- **CBOW**: Predicts target word from context
- **Skip-gram**: Predicts context words from target

### Implementation

```python
from gensim.models import Word2Vec
from gensim.models.word2vec import LineSentence
import numpy as np

# Training data
sentences = [
    ["machine", "learning", "is", "a", "branch", "of", "artificial", "intelligence"],
    ["deep", "learning", "uses", "neural", "networks"],
    ["natural", "language", "processing", "deals", "with", "text"],
    ["computer", "vision", "processes", "images"]
]

# Train Word2Vec
model = Word2Vec(
    sentences,
    vector_size=100,
    window=5,
    min_count=1,
    workers=4,
    sg=1,  # 1 for Skip-gram, 0 for CBOW
    epochs=100
)

# Get word vectors
vector = model.wv['machine']
print(f"Vector shape: {vector.shape}")

# Similar words
similar = model.wv.most_similar('learning', topn=5)
print("Similar words to 'learning':")
for word, score in similar:
    print(f"  {word}: {score:.4f}")

# Word analogies
# king - man + woman = queen
try:
    analogy = model.wv.most_similar(positive=['king', 'woman'], negative=['man'])
    print(f"\nking - man + woman = {analogy[0][0]}")
except KeyError as e:
    print(f"Word not in vocabulary: {e}")

# Save and load model
model.save("word2vec.model")
loaded_model = Word2Vec.load("word2vec.model")
```

---

## GloVe

### Implementation

```python
import numpy as np

def load_glove_embeddings(glove_file):
    embeddings_index = {}
    with open(glove_file, encoding='utf-8') as f:
        for line in f:
            values = line.split()
            word = values[0]
            vector = np.asarray(values[1:], dtype='float32')
            embeddings_index[word] = vector
    return embeddings_index

# Load GloVe embeddings
# Download from: https://nlp.stanford.edu/projects/glove/
embeddings_index = load_glove_embeddings('glove.6B.100d.txt')

# Get embedding
vector = embeddings_index.get('machine')
if vector is not None:
    print(f"Vector shape: {vector.shape}")

# Create embedding matrix
def create_embedding_matrix(word_index, embeddings_index, embedding_dim=100):
    num_words = len(word_index) + 1
    embedding_matrix = np.zeros((num_words, embedding_dim))
    
    for word, i in word_index.items():
        embedding_vector = embeddings_index.get(word)
        if embedding_vector is not None:
            embedding_matrix[i] = embedding_vector
    
    return embedding_matrix
```

---

## FastText

### Implementation

```python
from gensim.models import FastText

# Train FastText
model = FastText(
    sentences,
    vector_size=100,
    window=5,
    min_count=1,
    workers=4,
    epochs=100,
    min_n=3,
    max_n=6
)

# FastText can handle out-of-vocabulary words
vector = model.wv['nonexistentword']
print(f"OOV vector shape: {vector.shape}")

# Similar words
similar = model.wv.most_similar('learning', topn=5)
print("Similar words to 'learning':")
for word, score in similar:
    print(f"  {word}: {score:.4f}")
```

---

## Pre-trained Embeddings

### Using Hugging Face

```python
from transformers import AutoTokenizer, AutoModel
import torch

# Load pre-trained model
tokenizer = AutoTokenizer.from_pretrained('bert-base-uncased')
model = AutoModel.from_pretrained('bert-base-uncased')

# Get embeddings
text = "Machine learning is amazing"
inputs = tokenizer(text, return_tensors='pt')
outputs = model(**inputs)

# Last hidden state
embeddings = outputs.last_hidden_state
print(f"Embeddings shape: {embeddings.shape}")

# CLS token embedding
cls_embedding = embeddings[:, 0, :]
print(f"CLS embedding shape: {cls_embedding.shape}")
```

---

## Embedding Visualization

```python
from sklearn.manifold import TSNE
import matplotlib.pyplot as plt
import numpy as np

def visualize_embeddings(words, embeddings, title="Word Embeddings"):
    vectors = np.array([embeddings[w] for w in words if w in embeddings])
    valid_words = [w for w in words if w in embeddings]
    
    tsne = TSNE(n_components=2, random_state=42)
    reduced = tsne.fit_transform(vectors)
    
    plt.figure(figsize=(12, 8))
    plt.scatter(reduced[:, 0], reduced[:, 1])
    
    for i, word in enumerate(valid_words):
        plt.annotate(word, (reduced[i, 0], reduced[i, 1]))
    
    plt.title(title)
    plt.show()

# Usage
words = ['king', 'queen', 'man', 'woman', 'prince', 'princess', 
         'car', 'bus', 'train', 'airplane']
visualize_embeddings(words, model.wv)
```

---

## Comparison

| Method | Training | Context | OOV Handling | Speed |
|--------|----------|---------|--------------|-------|
| Word2Vec | Fast | Window | No | Fast |
| GloVe | Medium | Global | No | Fast |
| FastText | Medium | Window | Yes (subword) | Medium |
| BERT | Slow | Contextual | Yes | Slow |

## Best Practices

1. **Choose based on task**: Static vs contextual embeddings
2. **Domain adaptation**: Train on domain-specific corpus
3. **Fine-tuning**: Update embeddings during training
4. **Combine with other features**: Don't rely solely on embeddings
5. **Evaluate carefully**: Use intrinsic and extrinsic tasks

## Further Reading

- "Efficient Estimation of Word Representations" by Mikolov et al.
- "GloVe: Global Vectors" by Pennington et al.
- Hugging Face embeddings documentation
