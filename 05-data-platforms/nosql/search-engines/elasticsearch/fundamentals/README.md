# Elasticsearch Fundamentals

## Comprehensive Guide to Elasticsearch

Elasticsearch is a distributed search and analytics engine. This guide covers indices, mapping, analysis, and basic operations.

---

## Table of Contents

1. [Elasticsearch Overview](#elasticsearch-overview)
2. [Indices and Documents](#indices-and-documents)
3. [Mapping](#mapping)
4. [Analysis](#analysis)
5. [Best Practices](#best-practices)

---

## Elasticsearch Overview

### Architecture

```
+------------------+     +------------------+
|   Node 1         |     |   Node 2         |
|   (Master)       |<--->|                  |
+------------------+     +------------------+
        ^                         ^
        |                         |
        v                         v
+------------------+     +------------------+
|   Node 3         |     |   Node 4         |
|   (Data)         |<--->|   (Data)         |
+------------------+     +------------------+
```

### Features

```
- Full-text search
- Distributed and scalable
- Real-time indexing
- RESTful API
- Schema-free (dynamic mapping)
- Analytics engine
```

---

## Indices and Documents

### Create Index

```bash
# Create index
curl -X PUT "localhost:9200/myindex"

# Create index with settings
curl -X PUT "localhost:9200/myindex" -H 'Content-Type: application/json' -d'
{
  "settings": {
    "number_of_shards": 5,
    "number_of_replicas": 1
  }
}'

# Delete index
curl -X DELETE "localhost:9200/myindex"
```

### Index Document

```bash
# Index document
curl -X PUT "localhost:9200/myindex/_doc/1" -H 'Content-Type: application/json' -d'
{
  "title": "Elasticsearch Guide",
  "content": "Elasticsearch is a distributed search engine",
  "tags": ["search", "database"],
  "timestamp": "2024-01-15T10:30:00Z"
}'

# Auto-generate ID
curl -X POST "localhost:9200/myindex/_doc" -H 'Content-Type: application/json' -d'
{
  "title": "Document Title",
  "content": "Document content"
}'
```

### Get Document

```bash
# Get by ID
curl -X GET "localhost:9200/myindex/_doc/1"

# Get with source
curl -X GET "localhost:9200/myindex/_doc/1?_source=title,content"
```

### Update Document

```bash
# Partial update
curl -X POST "localhost:9200/myindex/_update/1" -H 'Content-Type: application/json' -d'
{
  "doc": {
    "title": "Updated Title"
  }
}'

# Script update
curl -X POST "localhost:9200/myindex/_update/1" -H 'Content-Type: application/json' -d'
{
  "script": {
    "source": "ctx._source.tags.add(params.tag)",
    "params": {
      "tag": "new-tag"
    }
  }
}'
```

### Delete Document

```bash
# Delete by ID
curl -X DELETE "localhost:9200/myindex/_doc/1"
```

---

## Mapping

### Create Mapping

```bash
# Create mapping
curl -X PUT "localhost:9200/myindex/_mapping" -H 'Content-Type: application/json' -d'
{
  "properties": {
    "title": {
      "type": "text",
      "analyzer": "standard"
    },
    "content": {
      "type": "text",
      "analyzer": "english"
    },
    "tags": {
      "type": "keyword"
    },
    "timestamp": {
      "type": "date"
    },
    "views": {
      "type": "integer"
    }
  }
}'
```

### Field Types

```json
{
  "properties": {
    "text_field": {"type": "text"},
    "keyword_field": {"type": "keyword"},
    "integer_field": {"type": "integer"},
    "long_field": {"type": "long"},
    "float_field": {"type": "float"},
    "double_field": {"type": "double"},
    "boolean_field": {"type": "boolean"},
    "date_field": {"type": "date"},
    "object_field": {
      "properties": {
        "nested_field": {"type": "text"}
      }
    }
  }
}
```

---

## Analysis

### Analyzers

```bash
# Standard analyzer
curl -X POST "localhost:9200/_analyze" -H 'Content-Type: application/json' -d'
{
  "analyzer": "standard",
  "text": "The quick brown fox jumps over the lazy dog"
}'

# Simple analyzer
curl -X POST "localhost:9200/_analyze" -H 'Content-Type: application/json' -d'
{
  "analyzer": "simple",
  "text": "The quick brown fox jumps over the lazy dog"
}'

# Whitespace analyzer
curl -X POST "localhost:9200/_analyze" -H 'Content-Type: application/json' -d'
{
  "analyzer": "whitespace",
  "text": "The quick brown fox jumps over the lazy dog"
}'
```

### Custom Analyzer

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

---

## Best Practices

### 1. Use Proper Mapping

```bash
# Good - Explicit mapping
curl -X PUT "localhost:9200/myindex" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "title": {"type": "text"},
      "tags": {"type": "keyword"}
    }
  }
}'

# Bad - Dynamic mapping
curl -X PUT "localhost:9200/myindex"
```

### 2. Use Bulk API

```bash
# Good - Bulk operations
curl -X POST "localhost:9200/_bulk" -H 'Content-Type: application/json' -d'
{"index": {"_index": "myindex", "_id": "1"}}
{"title": "Doc 1"}
{"index": {"_index": "myindex", "_id": "2"}}
{"title": "Doc 2"}
'

# Bad - Individual operations
curl -X PUT "localhost:9200/myindex/_doc/1" -d '{"title": "Doc 1"}'
curl -X PUT "localhost:9200/myindex/_doc/2" -d '{"title": "Doc 2"}'
```

### 3. Use Scroll for Large Results

```bash
# Initial query with scroll
curl -X GET "localhost:9200/myindex/_search?scroll=1m" -H 'Content-Type: application/json' -d'
{
  "size": 100,
  "query": {"match_all": {}}
}'

# Continue scrolling
curl -X POST "localhost:9200/_search/scroll" -H 'Content-Type: application/json' -d'
{
  "scroll": "1m",
  "scroll_id": "DnF1ZXJ5VGhlbkZldGNoBQAAAAAAA..."
}'
```

### 4. Use Point in Time

```bash
# Open point in time
curl -X PUT "localhost:9200/myindex/_pit?keep_alive=1m"

# Search with point in time
curl -X GET "localhost:9200/_search" -H 'Content-Type: application/json' -d'
{
  "pit": {"id": "pit_id", "keep_alive": "1m"},
  "query": {"match_all": {}},
  "sort": [{"_shard_doc": "asc"}]
}'
```

### 5. Monitor Cluster Health

```bash
# Check cluster health
curl -X GET "localhost:9200/_cluster/health"

# Check node stats
curl -X GET "localhost:9200/_nodes/stats"
```

---

## Further Reading

- [Elasticsearch Documentation](https://www.elastic.co/guide/)
- [Mapping Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html)
- [Analysis Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html)
