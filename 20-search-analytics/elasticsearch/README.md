# Elasticsearch

## Overview

Elasticsearch is a distributed, RESTful search and analytics engine built on Apache Lucene. It provides near-real-time indexing, full-text search, structured queries, and aggregations at scale. Elasticsearch is the core of the Elastic Stack (formerly ELK Stack).

## Why It Matters

- Powers search for companies like Wikipedia, GitHub, Uber, and Netflix.
- De facto standard for log analytics, security monitoring (SIEM), and APM.
- Rich query DSL enables complex search patterns without writing custom code.
- Horizontal scaling allows petabyte-scale deployments.

## Key Concepts

| Concept | Description |
|---------|-------------|
| **Document** | A JSON object stored in an index |
| **Index** | A collection of documents with a schema mapping |
| **Shard** | A partition of an index for horizontal scaling |
| **Replica** | A copy of a shard for fault tolerance and read throughput |
| **Cluster** | A group of nodes sharing the same cluster name |
| **Mapping** | Schema definition controlling field types and analyzers |

## Core Topics

### Inverted Index

Elasticsearch uses an inverted index to map terms to documents. Each field is tokenized, normalized, and indexed so that full-text queries execute in milliseconds regardless of dataset size.

### Query DSL

```json
{
  "query": {
    "bool": {
      "must": [
        { "match": { "title": "distributed systems" } }
      ],
      "filter": [
        { "range": { "published_at": { "gte": "2024-01-01" } } }
      ]
    }
  }
}
```

### Cluster Architecture

- **Master-eligible nodes** manage cluster state.
- **Data nodes** store shards and handle CRUD operations.
- **Coordinating nodes** route requests and aggregate results.
- **Ingest nodes** preprocess documents before indexing.

### Sharding and Replicas

- Default: 1 primary shard per index, 1 replica (2 total copies).
- Shard count is fixed at index creation—plan carefully.
- Replicas promote during node failures, maintaining availability.

### Analyzers

Three-stage pipeline: **Character Filter** -> **Tokenizer** -> **Token Filter**. Built-in analyzers include `standard`, `simple`, `whitespace`, `keyword`, and custom analyzers via plugins.

## Best Practices

- Use index templates and ILM (Index Lifecycle Management) for time-series data.
- Avoid deep mappings—flatten nested objects when possible.
- Set explicit mappings instead of relying on dynamic mapping.
- Size shards between 10-50 GB for optimal performance.
- Monitor cluster health with `_cluster/health` and `_nodes/stats`.
- Use `_bulk` API for batch indexing operations.

## Hands-on Labs

1. **Cluster Setup**: Start a single-node cluster with Docker and index sample documents.
2. **Mapping and Indexing**: Define a custom mapping with analyzers and bulk-load data.
3. **Query Workshop**: Practice `match`, `term`, `range`, `bool`, and `aggs` queries.
4. **Shard Rebalancing**: Simulate node failure and observe shard migration.
5. **ILM Policy**: Create a hot-warm-cold lifecycle policy for log indices.

## Interview Questions

1. Explain the difference between `match` and `term` queries. When would you use each?
2. What happens when a node holding a primary shard goes down?
3. How does an inverted index work, and why is it faster than a relational index for text search?
4. Describe the stages of the analysis pipeline. How do you handle synonyms?
5. What is the difference between a coordinating node and a master node?
6. How do you prevent a mapping explosion in a field with high cardinality?
7. Explain the `_bulk` API. What happens if one document in a bulk request fails?

## References

- [Elasticsearch Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/)
- [Elasticsearch: The Definitive Guide](https://www.elastic.co/guide/en/elasticsearch/guide/current/)
- [Elasticsearch Internals](https://www.elastic.co/blog/found-elasticsearch-from-the-ground-up)
- Elastic blog: Architecture and design patterns
