# Text Classification

## Overview

Text classification assigns predefined categories to text documents. Common applications include spam detection, sentiment analysis, and topic categorization.

## TF-IDF Based Classification

### Implementation

```python
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import LogisticRegression
from sklearn.svm import LinearSVC
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import classification_report
from sklearn.datasets import fetch_20newsgroups

# Load dataset
categories = ['sci.space', 'rec.sport.baseball', 'comp.graphics', 'talk.politics.misc']
data = fetch_20newsgroups(subset='all', categories=categories, remove=('headers', 'footers'))

X_train, X_test, y_train, y_test = train_test_split(
    data.data, data.target, test_size=0.2, random_state=42
)

# TF-IDF Pipeline
tfidf_pipeline = Pipeline([
    ('tfidf', TfidfVectorizer(max_features=10000, ngram_range=(1, 2))),
    ('classifier', LogisticRegression(max_iter=1000))
])

tfidf_pipeline.fit(X_train, y_train)
y_pred = tfidf_pipeline.predict(X_test)
print(classification_report(y_test, y_pred, target_names=data.target_names))
```

### Hyperparameter Tuning

```python
from sklearn.model_selection import GridSearchCV

param_grid = {
    'tfidf__max_features': [5000, 10000, 20000],
    'tfidf__ngram_range': [(1, 1), (1, 2)],
    'classifier__C': [0.1, 1, 10]
}

grid_search = GridSearchCV(
    tfidf_pipeline,
    param_grid,
    cv=5,
    scoring='accuracy',
    n_jobs=-1
)
grid_search.fit(X_train, y_train)
print(f"Best params: {grid_search.best_params_}")
print(f"Best score: {grid_search.best_score_:.4f}")
```

---

## Deep Learning Classification

### CNN for Text

```python
import torch
import torch.nn as nn

class TextCNN(nn.Module):
    def __init__(self, vocab_size, embed_dim, num_classes, kernel_sizes=[3, 4, 5]):
        super().__init__()
        self.embedding = nn.Embedding(vocab_size, embed_dim)
        self.convs = nn.ModuleList([
            nn.Conv1d(embed_dim, 128, k) for k in kernel_sizes
        ])
        self.dropout = nn.Dropout(0.5)
        self.fc = nn.Linear(128 * len(kernel_sizes), num_classes)
    
    def forward(self, x):
        x = self.embedding(x).transpose(1, 2)
        
        conv_outputs = []
        for conv in self.convs:
            c = torch.relu(conv(x))
            c = torch.max_pool1d(c, c.size(2)).squeeze(2)
            conv_outputs.append(c)
        
        out = torch.cat(conv_outputs, dim=1)
        out = self.dropout(out)
        return self.fc(out)
```

### LSTM for Text

```python
class TextLSTM(nn.Module):
    def __init__(self, vocab_size, embed_dim, hidden_dim, num_classes, num_layers=2):
        super().__init__()
        self.embedding = nn.Embedding(vocab_size, embed_dim)
        self.lstm = nn.LSTM(
            embed_dim, hidden_dim, num_layers,
            batch_first=True, dropout=0.5, bidirectional=True
        )
        self.fc = nn.Linear(hidden_dim * 2, num_classes)
    
    def forward(self, x):
        x = self.embedding(x)
        lstm_out, _ = self.lstm(x)
        out = lstm_out[:, -1, :]
        return self.fc(out)
```

---

## BERT for Text Classification

### Implementation

```python
from transformers import BertTokenizer, BertForSequenceClassification
from transformers import Trainer, TrainingArguments
from datasets import Dataset

# Load data
train_texts = ["This is great!", "This is terrible!", "This is okay!"]
train_labels = [1, 0, 1]

# Create dataset
train_dataset = Dataset.from_dict({
    'text': train_texts,
    'label': train_labels
})

# Tokenize
tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')

def tokenize_function(examples):
    return tokenizer(examples['text'], padding='max_length', truncation=True)

train_dataset = train_dataset.map(tokenize_function, batched=True)

# Load model
model = BertForSequenceClassification.from_pretrained(
    'bert-base-uncased', num_labels=2
)

# Training arguments
training_args = TrainingArguments(
    output_dir='./results',
    num_train_epochs=3,
    per_device_train_batch_size=8,
    learning_rate=2e-5,
    weight_decay=0.01,
)

# Train
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=train_dataset,
)
trainer.train()
```

---

## Zero-Shot Classification

```python
from transformers import pipeline

classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")

text = "The stock market crashed today due to economic concerns"
candidate_labels = ["business", "sports", "technology", "politics"]

result = classifier(text, candidate_labels)
print(f"Text: {text}")
print(f"Label: {result['labels'][0]} (confidence: {result['scores'][0]:.4f})")
```

---

## Evaluation Metrics

```python
from sklearn.metrics import (
    accuracy_score, precision_score, recall_score,
    f1_score, confusion_matrix, classification_report
)

def evaluate_classification(y_true, y_pred):
    print(f"Accuracy: {accuracy_score(y_true, y_pred):.4f}")
    print(f"Precision: {precision_score(y_true, y_pred, average='weighted'):.4f}")
    print(f"Recall: {recall_score(y_true, y_pred, average='weighted'):.4f}")
    print(f"F1 Score: {f1_score(y_true, y_pred, average='weighted'):.4f}")
    print("\nClassification Report:")
    print(classification_report(y_true, y_pred))
```

---

## Comparison

| Method | Speed | Accuracy | Interpretability |
|--------|-------|----------|------------------|
| TF-IDF + NB | Fast | Medium | High |
| TF-IDF + LR | Fast | Medium | High |
| CNN | Medium | High | Low |
| LSTM | Slow | High | Low |
| BERT | Very Slow | Very High | Low |

## Best Practices

1. **Start simple**: TF-IDF + Logistic Regression
2. **Data augmentation**: Back-translation, synonym replacement
3. **Class imbalance**: Use class weights or oversampling
4. **Hyperparameter tuning**: Grid search or Bayesian optimization
5. **Ensemble**: Combine multiple models

## Further Reading

- "Speech and Language Processing" by Jurafsky and Martin
- Hugging Face text classification tutorial
- scikit-learn text classification documentation
