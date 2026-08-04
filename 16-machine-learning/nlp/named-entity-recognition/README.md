# Named Entity Recognition (NER)

## Overview

NER identifies and classifies named entities in text into predefined categories like person, organization, location, date, etc.

## spaCy NER

### Implementation

```python
import spacy

# Load pre-trained model
nlp = spacy.load('en_core_web_sm')

text = "Apple Inc. was founded by Steve Jobs in Cupertino, California in 1976."
doc = nlp(text)

# Extract entities
for ent in doc.ents:
    print(f"{ent.text}: {ent.label_} ({spacy.explain(ent.label_)})")

# Custom entity extraction
def extract_entities(text):
    doc = nlp(text)
    entities = []
    for ent in doc.ents:
        entities.append({
            'text': ent.text,
            'label': ent.label_,
            'start': ent.start_char,
            'end': ent.end_char
        })
    return entities
```

### Training Custom NER

```python
import spacy
from spacy.tokens import DocBin
from spacy.training import Example

# Training data
TRAIN_DATA = [
    ("Apple is looking at buying U.K. startup for $1 billion", {
        "entities": [(0, 5, "ORG"), (28, 31, "GPE"), (44, 54, "MONEY")]
    }),
    ("San Francisco considers banning sidewalk delivery robots", {
        "entities": [(0, 13, "GPE")]
    })
]

# Create blank model
nlp = spacy.blank("en")
ner = nlp.add_pipe("ner")

# Add labels
for _, annotations in TRAIN_DATA:
    for ent in annotations.get("entities"):
        ner.add_label(ent[2])

# Training loop
optimizer = nlp.begin_training()
for epoch in range(20):
    losses = {}
    for text, annotations in TRAIN_DATA:
        doc = nlp.make_doc(text)
        example = Example.from_dict(doc, annotations)
        nlp.update([example], sgd=optimizer, losses=losses)
    print(f"Epoch {epoch}, Losses: {losses}")
```

---

## Hugging Face NER

### Pre-trained Models

```python
from transformers import pipeline

# NER pipeline
ner_pipeline = pipeline("ner", grouped_entities=True)

text = "Apple Inc. was founded by Steve Jobs in Cupertino, California."
entities = ner_pipeline(text)

for entity in entities:
    print(f"{entity['word']}: {entity['entity_group']} ({entity['score']:.4f})")
```

### Fine-Tuning BERT for NER

```python
from transformers import AutoTokenizer, AutoModelForTokenClassification
from transformers import TrainingArguments, Trainer
from datasets import load_dataset

# Load dataset
dataset = load_dataset("conll2003")

# Load tokenizer and model
tokenizer = AutoTokenizer.from_pretrained("bert-base-cased")
model = AutoModelForTokenClassification.from_pretrained(
    "bert-base-cased", num_labels=9
)

# Tokenize and align labels
def tokenize_and_align_labels(examples):
    tokenized = tokenizer(
        examples["tokens"],
        truncation=True,
        is_split_into_words=True
    )
    
    labels = []
    for i, label in enumerate(examples["ner_tags"]):
        word_ids = tokenized.word_ids(batch_index=i)
        previous_word_idx = None
        label_ids = []
        
        for word_idx in word_ids:
            if word_idx is None:
                label_ids.append(-100)
            elif word_idx != previous_word_idx:
                label_ids.append(label[word_idx])
            else:
                label_ids.append(-100)
            previous_word_idx = word_idx
        
        labels.append(label_ids)
    
    tokenized["labels"] = labels
    return tokenized

tokenized_dataset = dataset.map(tokenize_and_align_labels, batched=True)

# Training arguments
training_args = TrainingArguments(
    output_dir="./ner_model",
    num_train_epochs=3,
    per_device_train_batch_size=16,
    evaluation_strategy="epoch",
    save_strategy="epoch",
    load_best_model_at_end=True
)

# Train
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=tokenized_dataset["train"],
    eval_dataset=tokenized_dataset["validation"]
)
trainer.train()
```

---

## CRF for NER

### Implementation

```python
import sklearn_crfsuite
from sklearn_crfsuite import metrics

def word2features(sent, i):
    word = sent[i][0]
    postag = sent[i][1]
    
    features = {
        'bias': 1.0,
        'word.lower()': word.lower(),
        'word[-3:]': word[-3:],
        'word[-2:]': word[-2:],
        'word.isupper()': word.isupper(),
        'word.istitle()': word.istitle(),
        'word.isdigit()': word.isdigit(),
        'postag': postag,
        'postag[:2]': postag[:2],
    }
    
    if i > 0:
        word1 = sent[i-1][0]
        postag1 = sent[i-1][1]
        features.update({
            '-1:word.lower()': word1.lower(),
            '-1:word.istitle()': word1.istitle(),
            '-1:word.isupper()': word1.isupper(),
            '-1:postag': postag1,
            '-1:postag[:2]': postag1[:2],
        })
    else:
        features['BOS'] = True
    
    if i < len(sent)-1:
        word1 = sent[i+1][0]
        postag1 = sent[i+1][1]
        features.update({
            '+1:word.lower()': word1.lower(),
            '+1:word.istitle()': word1.istitle(),
            '+1:word.isupper()': word1.isupper(),
            '+1:postag': postag1,
            '+1:postag[:2]': postag1[:2],
        })
    else:
        features['EOS'] = True
    
    return features

def sent2features(sent):
    return [word2features(sent, i) for i in range(len(sent))]

# Train CRF
crf = sklearn_crfsuite.CRF(
    algorithm='lbfgs',
    c1=0.1,
    c2=0.1,
    max_iterations=100
)
```

---

## Evaluation

```python
from seqeval.metrics import classification_report, f1_score

def evaluate_ner(true_labels, pred_labels, label_list):
    print(classification_report(true_labels, pred_labels))
    
    # Per-entity F1
    f1_per_entity = f1_score(true_labels, pred_labels, mode='per_entity')
    print("\nF1 per entity:")
    for entity, score in f1_per_entity.items():
        print(f"  {entity}: {score:.4f}")
```

---

## Common NER Tag Sets

### IOB Format
```
B-PER: Beginning of person
I-PER: Inside of person
B-ORG: Beginning of organization
I-ORG: Inside of organization
B-LOC: Beginning of location
I-LOC: Inside of location
O: Outside any entity
```

## Best Practices

1. **Data quality**: Clean, consistent annotations
2. **Domain adaptation**: Fine-tune on domain data
3. **Context window**: Use surrounding words as features
4. **Ensemble**: Combine multiple models
5. **Post-processing**: Validate entity consistency

## Further Reading

- spaCy NER documentation
- Hugging Face NER tutorial
- "Named Entity Recognition" in NLP textbooks
