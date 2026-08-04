# Elasticsearch Queries

## Comprehensive Guide to Query DSL

Elasticsearch Query DSL provides a rich query language. This guide covers match, term, bool queries, and aggregations.

---

## Table of Contents

1. [Query DSL Basics](#query-dsl-basics)
2. [Full-Text Queries](#full-text-queries)
3. [Term Queries](#term-queries)
4. [Boolean Queries](#boolean-queries)
5. [Aggregations](#aggregations)
6. [Best Practices](#best-practices)

---

## Query DSL Basics

### Basic Query

```json
{
  "query": {
    "match_all": {}
  }
}
```

### Query Context

```json
{
  "query": {
    "match": {
      "title": "elasticsearch"
    }
  }
}
```

### Filter Context

```json
{
  "query": {
    "bool": {
      "filter": [
        { "term": { "status": "published" } }
      ]
    }
  }
}
```

---

## Full-Text Queries

### Match Query

```json
{
  "query": {
    "match": {
      "title": {
        "query": "elasticsearch guide",
        "operator": "and"
      }
    }
  }
}
```

### Match Phrase Query

```json
{
  "query": {
    "match_phrase": {
      "title": "elasticsearch guide"
    }
  }
}
```

### Multi Match Query

```json
{
  "query": {
    "multi_match": {
      "query": "elasticsearch",
      "fields": ["title", "content"]
    }
  }
}
```

### Query String Query

```json
{
  "query": {
    "query_string": {
      "query": "title:elasticsearch AND content:guide",
      "fields": ["title", "content"]
    }
  }
}
```

---

## Term Queries

### Term Query

```json
{
  "query": {
    "term": {
      "status": "published"
    }
  }
}
```

### Terms Query

```json
{
  "query": {
    "terms": {
      "status": ["published", "draft"]
    }
  }
}
```

### Range Query

```json
{
  "query": {
    "range": {
      "date": {
        "gte": "2024-01-01",
        "lte": "2024-12-31"
      }
    }
  }
}
```

### Exists Query

```json
{
  "query": {
    "exists": {
      "field": "title"
    }
  }
}
```

### Prefix Query

```json
{
  "query": {
    "prefix": {
      "title": "elastic"
    }
  }
}
```

### Wildcard Query

```json
{
  "query": {
    "wildcard": {
      "title": "elastic*"
    }
  }
}
```

### Regex Query

```json
{
  "query": {
    "regexp": {
      "title": ".*search.*"
    }
  }
}
```

---

## Boolean Queries

### Bool Query

```json
{
  "query": {
    "bool": {
      "must": [
        { "match": { "title": "elasticsearch" } }
      ],
      "filter": [
        { "term": { "status": "published" } }
      ],
      "should": [
        { "match": { "content": "guide" } }
      ],
      "must_not": [
        { "term": { "status": "draft" } }
      ]
    }
  }
}
```

### Bool Query Operators

```json
{
  "must": {},      // Must match (AND)
  "filter": {},    // Must match (cached)
  "should": {},    // Should match (OR)
  "must_not": {}   // Must not match (NOT)
}
```

---

## Aggregations

### Bucket Aggregation

```json
{
  "aggs": {
    "by_status": {
      "terms": {
        "field": "status",
        "size": 10
      }
    }
  }
}
```

### Metric Aggregation

```json
{
  "aggs": {
    "avg_views": {
      "avg": {
        "field": "views"
      }
    },
    "sum_views": {
      "sum": {
        "field": "views"
      }
    },
    "min_views": {
      "min": {
        "field": "views"
      }
    },
    "max_views": {
      "max": {
        "field": "views"
      }
    }
  }
}
```

### Date Histogram Aggregation

```json
{
  "aggs": {
    "over_time": {
      "date_histogram": {
        "field": "timestamp",
        "calendar_interval": "month"
      }
    }
  }
}
```

### Nested Aggregation

```json
{
  "aggs": {
    "by_status": {
      "terms": {
        "field": "status"
      },
      "aggs": {
        "avg_views": {
          "avg": {
            "field": "views"
          }
        }
      }
    }
  }
}
```

---

## Best Practices

### 1. Use Filter Context

```json
{
  "query": {
    "bool": {
      "filter": [
        { "term": { "status": "published" } }
      ]
    }
  }
}
```

### 2. Use Query String for Complex Queries

```json
{
  "query": {
    "query_string": {
      "query": "title:elasticsearch AND (content:guide OR content:tutorial)"
    }
  }
}
```

### 3. Use Source Filtering

```json
{
  "_source": ["title", "content"],
  "query": {
    "match_all": {}
  }
}
```

### 4. Use Pagination

```json
{
  "from": 0,
  "size": 10,
  "query": {
    "match_all": {}
  }
}
```

### 5. Use Sort

```json
{
  "sort": [
    { "date": "desc" },
    { "_score": "desc" }
  ],
  "query": {
    "match_all": {}
  }
}
```

---

## Further Reading

- [Query DSL Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
- [Full-Text Queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/full-text-queries.html)
- [Aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/aggregations.html)
