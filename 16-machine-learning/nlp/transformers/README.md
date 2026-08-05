# Transformers and Attention Mechanisms

## Overview

Transformers use self-attention to process sequences in parallel, revolutionizing NLP with models like BERT, GPT, and T5.

## Self-Attention Mechanism

### Theory

Scaled Dot-Product Attention:
```
Attention(Q, K, V) = softmax(QK^T / √d_k) * V
```

### Implementation

```python
import torch
import torch.nn as nn
import math

class SelfAttention(nn.Module):
    def __init__(self, embed_dim, num_heads):
        super().__init__()
        self.embed_dim = embed_dim
        self.num_heads = num_heads
        self.head_dim = embed_dim // num_heads
        
        self.qkv = nn.Linear(embed_dim, 3 * embed_dim)
        self.output = nn.Linear(embed_dim, embed_dim)
    
    def forward(self, x):
        batch_size, seq_len, _ = x.shape
        
        # Compute Q, K, V
        qkv = self.qkv(x).reshape(batch_size, seq_len, 3, self.num_heads, self.head_dim)
        qkv = qkv.permute(2, 0, 3, 1, 4)
        q, k, v = qkv.unbind(0)
        
        # Attention scores
        scores = torch.matmul(q, k.transpose(-2, -1)) / math.sqrt(self.head_dim)
        attention = torch.softmax(scores, dim=-1)
        
        # Apply attention to values
        out = torch.matmul(attention, v)
        out = out.transpose(1, 2).reshape(batch_size, seq_len, self.embed_dim)
        
        return self.output(out)

# Usage
attention = SelfAttention(embed_dim=512, num_heads=8)
x = torch.randn(2, 10, 512)  # batch=2, seq_len=10, embed_dim=512
out = attention(x)
print(f"Output shape: {out.shape}")
```

---

## Multi-Head Attention

```python
class MultiHeadAttention(nn.Module):
    def __init__(self, embed_dim, num_heads, dropout=0.1):
        super().__init__()
        self.num_heads = num_heads
        self.head_dim = embed_dim // num_heads
        
        self.q_proj = nn.Linear(embed_dim, embed_dim)
        self.k_proj = nn.Linear(embed_dim, embed_dim)
        self.v_proj = nn.Linear(embed_dim, embed_dim)
        self.out_proj = nn.Linear(embed_dim, embed_dim)
        
        self.dropout = nn.Dropout(dropout)
        self.scale = math.sqrt(self.head_dim)
    
    def forward(self, x, mask=None):
        batch_size, seq_len, _ = x.shape
        
        q = self.q_proj(x).view(batch_size, seq_len, self.num_heads, self.head_dim).transpose(1, 2)
        k = self.k_proj(x).view(batch_size, seq_len, self.num_heads, self.head_dim).transpose(1, 2)
        v = self.v_proj(x).view(batch_size, seq_len, self.num_heads, self.head_dim).transpose(1, 2)
        
        scores = torch.matmul(q, k.transpose(-2, -1)) / self.scale
        
        if mask is not None:
            scores = scores.masked_fill(mask == 0, float('-inf'))
        
        attention = torch.softmax(scores, dim=-1)
        attention = self.dropout(attention)
        
        out = torch.matmul(attention, v)
        out = out.transpose(1, 2).contiguous().view(batch_size, seq_len, -1)
        
        return self.out_proj(out)
```

---

## Transformer Block

```python
class TransformerBlock(nn.Module):
    def __init__(self, embed_dim, num_heads, ff_dim, dropout=0.1):
        super().__init__()
        self.attention = MultiHeadAttention(embed_dim, num_heads, dropout)
        self.norm1 = nn.LayerNorm(embed_dim)
        self.norm2 = nn.LayerNorm(embed_dim)
        
        self.feed_forward = nn.Sequential(
            nn.Linear(embed_dim, ff_dim),
            nn.GELU(),
            nn.Dropout(dropout),
            nn.Linear(ff_dim, embed_dim),
            nn.Dropout(dropout)
        )
    
    def forward(self, x, mask=None):
        # Self-attention with residual connection
        attention_out = self.attention(x, mask)
        x = self.norm1(x + attention_out)
        
        # Feed-forward with residual connection
        ff_out = self.feed_forward(x)
        x = self.norm2(x + ff_out)
        
        return x

# Usage
block = TransformerBlock(embed_dim=512, num_heads=8, ff_dim=2048)
x = torch.randn(2, 10, 512)
out = block(x)
print(f"Output shape: {out.shape}")
```

---

## Positional Encoding

```python
class PositionalEncoding(nn.Module):
    def __init__(self, embed_dim, max_len=5000):
        super().__init__()
        pe = torch.zeros(max_len, embed_dim)
        position = torch.arange(0, max_len).unsqueeze(1).float()
        div_term = torch.exp(torch.arange(0, embed_dim, 2).float() * 
                            -(math.log(10000.0) / embed_dim))
        
        pe[:, 0::2] = torch.sin(position * div_term)
        pe[:, 1::2] = torch.cos(position * div_term)
        pe = pe.unsqueeze(0)
        
        self.register_buffer('pe', pe)
    
    def forward(self, x):
        return x + self.pe[:, :x.size(1)]
```

---

## Using Hugging Face Transformers

```python
from transformers import (
    AutoTokenizer, AutoModel,
    BertTokenizer, BertModel,
    GPT2Tokenizer, GPT2LMHeadModel
)

# BERT
tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
model = BertModel.from_pretrained('bert-base-uncased')

text = "The transformer architecture changed NLP"
inputs = tokenizer(text, return_tensors='pt')
outputs = model(**inputs)

print(f"Last hidden state: {outputs.last_hidden_state.shape}")
print(f"Pooler output: {outputs.pooler_output.shape}")

# GPT-2
gpt2_tokenizer = GPT2Tokenizer.from_pretrained('gpt2')
gpt2_model = GPT2LMHeadModel.from_pretrained('gpt2')

text = "The future of AI is"
inputs = gpt2_tokenizer(text, return_tensors='pt')
outputs = gpt2_model.generate(**inputs, max_length=50)
print(gpt2_tokenizer.decode(outputs[0]))
```

---

## BERT for Classification

```python
from transformers import BertForSequenceClassification

model = BertForSequenceClassification.from_pretrained(
    'bert-base-uncased', num_labels=2
)

# Forward pass
inputs = tokenizer("This is great!", return_tensors='pt')
labels = torch.tensor([1]).unsqueeze(0)

outputs = model(**inputs, labels=labels)
print(f"Loss: {outputs.loss.item()}")
print(f"Logits: {outputs.logits.shape}")
```

---

## GPT for Text Generation

```python
from transformers import pipeline

# Text generation
generator = pipeline('text-generation', model='gpt2')

prompt = "In the future, artificial intelligence will"
generated = generator(prompt, max_length=100, num_return_sequences=3)

for i, text in enumerate(generated):
    print(f"\nGeneration {i+1}:")
    print(text['generated_text'])
```

---

## Model Comparison

| Model | Architecture | Pre-training | Use Case |
|-------|--------------|--------------|----------|
| BERT | Encoder | MLM, NSP | Understanding |
| GPT | Decoder | LM | Generation |
| T5 | Encoder-Decoder | Span corruption | Both |
| RoBERTa | Encoder | MLM (improved) | Understanding |

## Best Practices

1. **Choose the right model**: BERT for understanding, GPT for generation
2. **Fine-tune on your data**: Pre-trained models are starting points
3. **Learning rate scheduling**: Use warmup and decay
4. **Gradient accumulation**: For large batch sizes
5. **Mixed precision**: Use fp16 for efficiency

## Further Reading

- "Attention Is All You Need" by Vaswani et al.
- Hugging Face documentation
- "BERT: Pre-training of Deep Bidirectional Transformers"
