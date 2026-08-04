# Text Preprocessing

## Overview

Text preprocessing transforms raw text into a format suitable for machine learning models. It includes cleaning, normalization, and feature extraction.

## Tokenization

### Word Tokenization

```python
import nltk
from nltk.tokenize import word_tokenize, sent_tokenize

nltk.download('punkt')

text = "Natural language processing is amazing! It's used in many applications."

# Sentence tokenization
sentences = sent_tokenize(text)
print(sentences)

# Word tokenization
words = word_tokenize(text)
print(words)
```

### Subword Tokenization

```python
from transformers import AutoTokenizer

# BERT tokenizer
bert_tokenizer = AutoTokenizer.from_pretrained('bert-base-uncased')
tokens = bert_tokenizer.tokenize("Natural language processing is amazing")
print(tokens)

# GPT-2 tokenizer
gpt_tokenizer = AutoTokenizer.from_pretrained('gpt2')
tokens = gpt_tokenizer.tokenize("Natural language processing is amazing")
print(tokens)
```

### Byte-Pair Encoding (BPE)

```python
import sentencepiece as spm

# Train BPE
spm.SentencePieceTrainer.train(
    input='corpus.txt',
    model_prefix='bpe_model',
    vocab_size=32000,
    model_type='bpe'
)

# Load and use
sp = spm.SentencePieceProcessor(model_file='bpe_model.model')
tokens = sp.encode("Natural language processing", out_type=str)
print(tokens)
```

---

## Stemming

### Porter Stemmer

```python
from nltk.stem import PorterStemmer

ps = PorterStemmer()
words = ['running', 'ran', 'runs', 'easily', 'fairly']

for word in words:
    print(f"{word} -> {ps.stem(word)}")
```

### Snowball Stemmer

```python
from nltk.stem import SnowballStemmer

# English stemmer
snowball = SnowballStemmer('english')
words = ['running', 'ran', 'runs', 'better', 'fairly']

for word in words:
    print(f"{word} -> {snowball.stem(word)}")
```

### Lancaster Stemmer

```python
from nltk.stem import LancasterStemmer

ls = LancasterStemmer()
words = ['running', 'ran', 'runs', 'easily', 'fairly']

for word in words:
    print(f"{word} -> {ls.stem(word)}")
```

---

## Lemmatization

### WordNet Lemmatizer

```python
from nltk.stem import WordNetLemmatizer
import nltk
nltk.download('wordnet')

lemmatizer = WordNetLemmatizer()

# Different POS tags
print(lemmatizer.lemmatize('running', pos='v'))  # verb
print(lemmatizer.lemmatize('better', pos='a'))   # adjective
print(lemmatizer.lemmatize('running', pos='n'))  # noun

# Compare stemming vs lemmatization
from nltk.stem import PorterStemmer
ps = PorterStemmer()

words = ['running', 'better', 'studies', 'flies']
for word in words:
    print(f"{word}: Stem={ps.stem(word)}, Lemma={lemmatizer.lemmatize(word)}")
```

### spaCy Lemmatization

```python
import spacy

nlp = spacy.load('en_core_web_sm')

doc = nlp("The runners were running better than before")
for token in doc:
    print(f"{token.text}: {token.lemma_}")
```

---

## Stop Words

### NLTK Stop Words

```python
from nltk.corpus import stopwords
import nltk
nltk.download('stopwords')

stop_words = set(stopwords.words('english'))
print(f"Number of stop words: {len(stop_words)}")

text = "This is a sample sentence with some stop words"
words = word_tokenize(text.lower())
filtered = [w for w in words if w not in stop_words]
print(filtered)
```

### Custom Stop Words

```python
# Custom stop words list
custom_stop_words = {'the', 'a', 'an', 'is', 'are', 'was', 'were'}

# Add to existing
stop_words.update(custom_stop_words)

# Remove from existing
stop_words.discard('not')
```

---

## Text Cleaning

### Basic Cleaning

```python
import re
import unicodedata

def clean_text(text):
    # Lowercase
    text = text.lower()
    
    # Remove HTML tags
    text = re.sub(r'<[^>]+>', '', text)
    
    # Remove URLs
    text = re.sub(r'http\S+|www\S+', '', text)
    
    # Remove email addresses
    text = re.sub(r'\S+@\S+', '', text)
    
    # Remove special characters
    text = re.sub(r'[^a-zA-Z\s]', '', text)
    
    # Remove extra whitespace
    text = re.sub(r'\s+', ' ', text).strip()
    
    return text

# Example
text = "<p>This is a <b>sample</b> text! Visit https://example.com or email user@email.com</p>"
print(clean_text(text))
```

### Unicode Normalization

```python
def normalize_unicode(text):
    # Normalize to NFKD form
    text = unicodedata.normalize('NFKD', text)
    return text

# Example
text = "café résumé naïve"
print(normalize_unicode(text))
```

---

## Complete Pipeline

```python
import re
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords

class TextPreprocessor:
    def __init__(self, remove_stopwords=True, lemmatize=True, min_word_length=2):
        self.remove_stopwords = remove_stopwords
        self.lemmatize = lemmatize
        self.min_word_length = min_word_length
        self.stop_words = set(stopwords.words('english'))
        self.lemmatizer = WordNetLemmatizer()
    
    def clean(self, text):
        text = text.lower()
        text = re.sub(r'<[^>]+>', '', text)
        text = re.sub(r'http\S+|www\S+', '', text)
        text = re.sub(r'[^a-zA-Z\s]', '', text)
        text = re.sub(r'\s+', ' ', text).strip()
        return text
    
    def tokenize(self, text):
        return word_tokenize(text)
    
    def remove_stop(self, tokens):
        return [t for t in tokens if t not in self.stop_words]
    
    def lemmatize_tokens(self, tokens):
        return [self.lemmatizer.lemmatize(t) for t in tokens]
    
    def filter_short(self, tokens):
        return [t for t in tokens if len(t) >= self.min_word_length]
    
    def preprocess(self, text):
        text = self.clean(text)
        tokens = self.tokenize(text)
        if self.remove_stopwords:
            tokens = self.remove_stop(tokens)
        if self.lemmatize:
            tokens = self.lemmatize_tokens(tokens)
        tokens = self.filter_short(tokens)
        return ' '.join(tokens)

# Usage
preprocessor = TextPreprocessor()
text = "The <b>cats</b> were running quickly to https://example.com!"
print(preprocessor.preprocess(text))
```

---

## Best Practices

1. **Choose based on task**: Stemming for speed, lemmatization for accuracy
2. **Keep domain terms**: Don't remove technical jargon
3. **Handle negations**: "not good" != "good"
4. **Preserve important punctuation**: Sentiment (?!), abbreviations
5. **Test different approaches**: Compare preprocessing methods

## Further Reading

- NLTK documentation
- spaCy documentation
- Hugging Face tokenizers
