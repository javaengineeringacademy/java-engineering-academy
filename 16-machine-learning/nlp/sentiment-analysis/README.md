# Sentiment Analysis

## Overview

Sentiment analysis determines the emotional tone of text, classifying it as positive, negative, or neutral.

## VADER Sentiment Analysis

### Implementation

```python
from nltk.sentiment import SentimentIntensityAnalyzer
import nltk
nltk.download('vader_lexicon')

sia = SentimentIntensityAnalyzer()

# Analyze text
text = "This movie was absolutely amazing! I loved every minute."
scores = sia.polarity_scores(text)
print(scores)
# {'neg': 0.0, 'neu': 0.423, 'pos': 0.577, 'compound': 0.8516}

# Compound score interpretation
# > 0.05: Positive
# < -0.05: Negative
# Between: Neutral

def analyze_sentiment(text):
    scores = sia.polarity_scores(text)
    if scores['compound'] >= 0.05:
        return 'positive'
    elif scores['compound'] <= -0.05:
        return 'negative'
    else:
        return 'neutral'

# Examples
texts = [
    "I love this product!",
    "This is terrible.",
    "The weather is okay today.",
    "What an incredible experience!"
]

for text in texts:
    sentiment = analyze_sentiment(text)
    print(f"{text}: {sentiment}")
```

### VADER on Social Media Text

```python
# VADER handles social media well
social_texts = [
    "This is SOOO good!!! 😊",
    "Worst service ever 👎",
    "Can't wait for tomorrow!!!",
    "meh, it's alright I guess :/"
]

for text in social_texts:
    scores = sia.polarity_scores(text)
    print(f"{text}")
    print(f"  Compound: {scores['compound']:.4f}")
    print(f"  Sentiment: {analyze_sentiment(text)}")
    print()
```

---

## TextBlob Sentiment

```python
from textblob import TextBlob

def textblob_sentiment(text):
    blob = TextBlob(text)
    polarity = blob.sentiment.polarity  # -1 to 1
    subjectivity = blob.sentiment.subjectivity  # 0 to 1
    return polarity, subjectivity

# Examples
text = "The movie was great but the ending was disappointing"
polarity, subjectivity = textblob_sentiment(text)
print(f"Polarity: {polarity:.4f}, Subjectivity: {subjectivity:.4f}")
```

---

## Transformer-Based Sentiment Analysis

### BERT for Sentiment

```python
from transformers import pipeline

# Pre-trained sentiment analysis pipeline
sentiment_pipeline = pipeline("sentiment-analysis")

texts = [
    "I love this product!",
    "This is terrible.",
    "It's okay, nothing special."
]

for text in texts:
    result = sentiment_pipeline(text)
    print(f"{text}: {result[0]['label']} ({result[0]['score']:.4f})")
```

### Fine-Tuning BERT

```python
import torch
from torch.utils.data import Dataset, DataLoader
from transformers import BertTokenizer, BertForSequenceClassification
from transformers import AdamW, get_linear_schedule_with_warmup

class SentimentDataset(Dataset):
    def __init__(self, texts, labels, tokenizer, max_length=128):
        self.texts = texts
        self.labels = labels
        self.tokenizer = tokenizer
        self.max_length = max_length
    
    def __len__(self):
        return len(self.texts)
    
    def __getitem__(self, idx):
        text = self.texts[idx]
        label = self.labels[idx]
        
        encoding = self.tokenizer(
            text,
            add_special_tokens=True,
            max_length=self.max_length,
            padding='max_length',
            truncation=True,
            return_tensors='pt'
        )
        
        return {
            'input_ids': encoding['input_ids'].squeeze(),
            'attention_mask': encoding['attention_mask'].squeeze(),
            'labels': torch.tensor(label, dtype=torch.long)
        }

# Setup
tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
model = BertForSequenceClassification.from_pretrained('bert-base-uncased', num_labels=2)

# Training
optimizer = AdamW(model.parameters(), lr=2e-5)
scheduler = get_linear_schedule_with_warmup(
    optimizer, num_warmup_steps=0, num_training_steps=1000
)

def train_epoch(model, dataloader, optimizer, scheduler):
    model.train()
    total_loss = 0
    
    for batch in dataloader:
        outputs = model(
            input_ids=batch['input_ids'],
            attention_mask=batch['attention_mask'],
            labels=batch['labels']
        )
        
        loss = outputs.loss
        total_loss += loss.item()
        
        loss.backward()
        torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)
        optimizer.step()
        scheduler.step()
        optimizer.zero_grad()
    
    return total_loss / len(dataloader)
```

---

## Aspect-Based Sentiment Analysis

```python
# Simple aspect extraction
import re
from collections import defaultdict

def extract_aspects_and_sentiment(text, aspects):
    results = defaultdict(list)
    
    sentences = re.split(r'[.!?]+', text)
    for sentence in sentences:
        for aspect in aspects:
            if aspect.lower() in sentence.lower():
                sentiment = analyze_sentiment(sentence)
                results[aspect].append(sentiment)
    
    return dict(results)

# Usage
text = "The food was great but the service was slow. The ambiance was nice."
aspects = ['food', 'service', 'ambiance']
results = extract_aspects_and_sentiment(text, aspects)
print(results)
```

---

## Evaluation

```python
from sklearn.metrics import classification_report, confusion_matrix
import numpy as np

def evaluate_sentiment(y_true, y_pred):
    print(classification_report(y_true, y_pred))
    print("Confusion Matrix:")
    print(confusion_matrix(y_true, y_pred))

# Example usage
y_true = [1, 0, 1, 1, 0, 1, 0, 0, 1, 1]
y_pred = [1, 0, 1, 0, 0, 1, 1, 0, 1, 1]
evaluate_sentiment(y_true, y_pred)
```

---

## Comparison

| Method | Accuracy | Speed | Domain Specific |
|--------|----------|-------|-----------------|
| VADER | Medium | Fast | Social media |
| TextBlob | Medium | Fast | General |
| BERT | High | Slow | Any |
| Fine-tuned | Very High | Slow | Specific domain |

## Best Practices

1. **Domain matters**: Train on domain-specific data
2. **Handle negations**: "not good" vs "good"
3. **Consider context**: Sarcasm is challenging
4. **Use ensemble**: Combine multiple approaches
5. **Evaluate properly**: Use domain-appropriate metrics

## Further Reading

- "Sentiment Analysis" by Bing Liu
- Hugging Face sentiment analysis tutorial
- VADER paper by Hutto and Gilbert
