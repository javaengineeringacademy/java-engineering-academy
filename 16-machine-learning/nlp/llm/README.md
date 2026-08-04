# Large Language Models (LLM)

## Overview

Large Language Models are transformer-based models trained on massive text corpora, capable of understanding and generating human-like text.

## Model Architectures

### GPT (Generative Pre-trained Transformer)

```python
from transformers import GPT2LMHeadModel, GPT2Tokenizer

tokenizer = GPT2Tokenizer.from_pretrained('gpt2')
model = GPT2LMHeadModel.from_pretrained('gpt2')

# Text generation
input_text = "The future of artificial intelligence"
inputs = tokenizer(input_text, return_tensors='pt')
outputs = model.generate(**inputs, max_length=100, temperature=0.7)
print(tokenizer.decode(outputs[0], skip_special_tokens=True))
```

### BERT (Bidirectional Encoder Representations)

```python
from transformers import BertTokenizer, BertModel

tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
model = BertModel.from_pretrained('bert-base-uncased')

text = "BERT understands context from both directions"
inputs = tokenizer(text, return_tensors='pt')
outputs = model(**inputs)

# Get embeddings
embeddings = outputs.last_hidden_state
print(f"Embeddings shape: {embeddings.shape}")
```

### LLaMA

```python
from transformers import LlamaForCausalLM, LlamaTokenizer

tokenizer = LlamaTokenizer.from_pretrained('meta-llama/Llama-2-7b-chat-hf')
model = LlamaForCausalLM.from_pretrained('meta-llama/Llama-2-7b-chat-hf')

# Chat format
prompt = "[INST] <<SYS>>\nYou are a helpful assistant.\n<</SYS>>\n\nWhat is machine learning? [/INST]"
inputs = tokenizer(prompt, return_tensors='pt')
outputs = model.generate(**inputs, max_length=500)
print(tokenizer.decode(outputs[0], skip_special_tokens=True))
```

---

## Fine-Tuning

### LoRA (Low-Rank Adaptation)

```python
from peft import LoraConfig, get_peft_model
from transformers import AutoModelForCausalLM, AutoTokenizer

# Load base model
model = AutoModelForCausalLM.from_pretrained("meta-llama/Llama-2-7b-hf")
tokenizer = AutoTokenizer.from_pretrained("meta-llama/Llama-2-7b-hf")

# LoRA configuration
lora_config = LoraConfig(
    r=16,
    lora_alpha=32,
    target_modules=["q_proj", "v_proj"],
    lora_dropout=0.05,
    bias="none",
    task_type="CAUSAL_LM"
)

# Apply LoRA
model = get_peft_model(model, lora_config)
model.print_trainable_parameters()
```

### Fine-Tuning with Hugging Face

```python
from transformers import (
    AutoModelForCausalLM, AutoTokenizer,
    TrainingArguments, Trainer
)
from datasets import Dataset

# Load model and tokenizer
model_name = "gpt2"
model = AutoModelForCausalLM.from_pretrained(model_name)
tokenizer = AutoTokenizer.from_pretrained(model_name)

# Prepare dataset
train_texts = ["Text 1", "Text 2", "Text 3"]
train_dataset = Dataset.from_dict({"text": train_texts})

def tokenize_function(examples):
    return tokenizer(examples["text"], truncation=True, padding=True)

train_dataset = train_dataset.map(tokenize_function, batched=True)

# Training arguments
training_args = TrainingArguments(
    output_dir="./fine_tuned_model",
    num_train_epochs=3,
    per_device_train_batch_size=4,
    learning_rate=2e-5,
    save_strategy="epoch",
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

## RAG (Retrieval-Augmented Generation)

### Implementation

```python
from langchain.vectorstores import FAISS
from langchain.embeddings import HuggingFaceEmbeddings
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.document_loaders import TextLoader

# Load documents
loader = TextLoader("documents.txt")
documents = loader.load()

# Split documents
text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=500,
    chunk_overlap=50
)
chunks = text_splitter.split_documents(documents)

# Create embeddings and vector store
embeddings = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")
vectorstore = FAISS.from_documents(chunks, embeddings)

# RAG query
query = "What is machine learning?"
relevant_docs = vectorstore.similarity_search(query, k=3)

# Combine context and generate
context = "\n".join([doc.page_content for doc in relevant_docs])
prompt = f"Context: {context}\n\nQuestion: {query}\nAnswer:"
```

---

## Prompt Engineering

### Few-Shot Prompting

```python
def few_shot_prompt(examples, query):
    prompt = "Examples:\n"
    for example in examples:
        prompt += f"Input: {example['input']}\nOutput: {example['output']}\n\n"
    prompt += f"Input: {query}\nOutput:"
    return prompt
```

### Chain-of-Thought

```python
def chain_of_thought_prompt(question):
    return f"""Question: {question}

Let's think step by step:
1. 
2. 
3. 

Answer:"""
```

---

## Evaluation

```python
import numpy as np
from rouge_score import rouge_scorer
from nltk.translate.bleu_score import corpus_bleu

def evaluate_llm(generated, reference):
    # ROUGE scores
    scorer = rouge_scorer.RougeScorer(['rouge1', 'rouge2', 'rougeL'], use_stemmer=True)
    scores = scorer.score(reference, generated)
    
    print("ROUGE scores:")
    for key, score in scores.items():
        print(f"  {key}: {score.fmeasure:.4f}")
    
    return scores
```

---

## Comparison

| Model | Parameters | Context Length | Use Case |
|-------|------------|----------------|----------|
| GPT-2 | 1.5B | 1K | Text generation |
| BERT | 110M | 512 | Understanding |
| LLaMA 2 | 7B-70B | 4K | General |
| GPT-4 | ~1.8T | 128K | General |

## Best Practices

1. **Choose model size**: Balance performance and resources
2. **Fine-tune carefully**: Avoid catastrophic forgetting
3. **Use LoRA**: Memory-efficient fine-tuning
4. **RAG for knowledge**: Combine with retrieval
5. **Evaluate thoroughly**: Use multiple metrics

## Further Reading

- "Language Models are Few-Shot Learners" (GPT-3)
- "BERT: Pre-training of Deep Bidirectional Transformers"
- Hugging Face LLM documentation
