# Elasticsearch Analysis

## Comprehensive Guide to Analysis and Text Processing

Analysis is the process of converting text into tokens for indexing. This guide covers analyzers, tokenizers, and filters.

---

## Table of Contents

1. [Analysis Overview](#analysis-overview)
2. [Built-in Analyzers](#built-in-analyzers)
3. [Custom Analyzers](#custom-analyzers)
4. [Tokenizers](#tokenizers)
5. [Token Filters](#token-filters)
6. [Best Practices](#best-practices)

---

## Analysis Overview

### Analysis Process

```
Input Text --> Character Filters --> Tokenizer --> Token Filters --> Output Tokens
```

### Analysis Chain

```
Character Filters (optional)
    ↓
Tokenizer (required)
    ↓
Token Filters (optional)
    ↓
Output Tokens
```

---

## Built-in Analyzers

### Standard Analyzer

```json
{
  "analyzer": "standard"
}
```

```
Input: "The quick brown fox jumps over the lazy dog"
Output: ["quick", "brown", "fox", "jumps", "lazy", "dog"]
```

### Simple Analyzer

```json
{
  "analyzer": "simple"
}
```

```
Input: "The quick brown fox jumps over the lazy dog"
Output: ["the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"]
```

### Whitespace Analyzer

```json
{
  "analyzer": "whitespace"
}
```

```
Input: "The quick brown fox jumps over the lazy dog"
Output: ["The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"]
```

### Keyword Analyzer

```json
{
  "analyzer": "keyword"
}
```

```
Input: "The quick brown fox jumps over the lazy dog"
Output: ["The quick brown fox jumps over the lazy dog"]
```

### Pattern Analyzer

```json
{
  "analyzer": "pattern",
  "pattern": "\\W+"
}
```

```
Input: "The quick brown fox jumps over the lazy dog"
Output: ["The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"]
```

---

## Custom Analyzers

### Create Custom Analyzer

```json
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_custom_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "stop",
            "snowball"
          ]
        }
      }
    }
  }
}
```

### Test Custom Analyzer

```bash
curl -X POST "localhost:9200/_analyze" -H 'Content-Type: application/json' -d'
{
  "analyzer": "my_custom_analyzer",
  "text": "The quick brown fox jumps over the lazy dog"
}'
```

---

## Tokenizers

### Standard Tokenizer

```json
{
  "tokenizer": "standard"
}
```

### Keyword Tokenizer

```json
{
  "tokenizer": "keyword"
}
```

### Letter Tokenizer

```json
{
  "tokenizer": "letter"
}
```

### Whitespace Tokenizer

```json
{
  "tokenizer": "whitespace"
}
```

### UAX URL Email Tokenizer

```json
{
  "tokenizer": "uax_url_email"
}
```

```
Input: "Email me at john.doe@example.com"
Output: ["Email", "me", "at", "john.doe@example.com"]
```

---

## Token Filters

### Lowercase Filter

```json
{
  "filter": ["lowercase"]
}
```

### Stop Filter

```json
{
  "filter": ["stop"]
}
```

### Snowball Filter

```json
{
  "filter": ["snowball"]
}
```

### Synonym Filter

```json
{
  "filter": {
    "my_synonyms": {
      "type": "synonym",
      "synonyms": [
        "quick, fast, speedy",
        "lazy, idle, inactive"
      ]
    }
  }
}
```

### Edge Ngram Filter

```json
{
  "filter": {
    "my_edge_ngram": {
      "type": "edge_ngram",
      "min_gram": 2,
      "max_gram": 10
    }
  }
}
```

---

## Best Practices

### 1. Use Appropriate Analyzer

```json
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "analyzer": "standard"
      },
      "content": {
        "type": "text",
        "analyzer": "english"
      },
      "code": {
        "type": "text",
        "analyzer": "whitespace"
      }
    }
  }
}
```

### 2. Use Multi-field Mapping

```json
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword"
          },
          "autocomplete": {
            "type": "text",
            "analyzer": "my_custom_analyzer"
          }
        }
      }
    }
  }
}
```

### 3. Test Analyzers

```bash
# Test before indexing
curl -X POST "localhost:9200/_analyze" -H 'Content-Type: application/json' -d'
{
  "analyzer": "my_custom_analyzer",
  "text": "Test your analyzer here"
}'
```

### 4. Use Character Filters

```json
{
  "analyzer": {
    "my_analyzer": {
      "tokenizer": "standard",
      "char_filter": ["html_strip"]
    }
  }
}
```

### 5. Monitor Performance

```bash
# Check index stats
curl -X GET "localhost:9200/myindex/_stats"
```

---

## Further Reading

- [Analysis Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html)
- [Built-in Analyzers](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-analyzers.html)
- [Token Filters](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-tokenfilters.html)
