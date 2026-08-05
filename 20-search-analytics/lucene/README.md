# Apache Lucene

## Overview

Apache Lucene is a high-performance, full-featured text search engine library written in Java. It is the foundational technology behind Elasticsearch, Solr, and many other search systems. Lucene provides indexing, querying, and analysis capabilities but requires embedding within an application—it is not a standalone server.

## Why It Matters

- Every major search engine in production today is built on Lucene.
- Understanding Lucene internals explains how Elasticsearch and Solr work under the hood.
- Optimized inverted index implementation delivers sub-millisecond query performance.
- Rich text analysis pipeline supports complex NLP and search requirements.

## Key Concepts

| Concept | Description |
|---------|-------------|
| **IndexWriter** | Creates and updates the index |
| **IndexReader** | Reads and searches the index |
| **Document** | A collection of fields representing a searchable unit |
| **Field** | An individual piece of data within a document |
| **Term** | The basic unit of search, representing a word in the index |
| **Analyzer** | Tokenizes and normalizes text before indexing |

## Core Topics

### Inverted Index

Lucene's inverted index maps each unique term to the list of documents containing it. This structure enables fast full-text lookups—the core of search engine performance.

### Segment Architecture

Lucene indexes are organized into immutable **segments**. New documents are written to a new segment, and segments are periodically merged. This design enables fast writes and consistent reads.

```
Index
  ├── Segment_0 (merged, immutable)
  ├── Segment_1 (merged, immutable)
  └── Segment_2 (new, being written)
```

### Text Analysis Pipeline

1. **CharFilter**: Pre-processes raw character stream (e.g., strip HTML).
2. **Tokenizer**: Splits text into tokens (terms, positions, offsets).
3. **TokenFilter**: Modifies tokens (lowercasing, stemming, synonyms, stop words).

### Scoring Model

Lucene uses the **TF-IDF** (and newer **BM25**) scoring model to rank documents by relevance. Factors include term frequency, inverse document frequency, field length, and query boost.

### Point Fields (Numeric Indexing)

Lucene uses BKD trees for efficient numeric, date, and geospatial queries. These support range queries and sorting without the overhead of inverted index term matching.

## Best Practices

- Choose analyzers carefully—the wrong analyzer leads to poor search results.
- Use `StringField` for exact matches and `TextField` for full-text search.
- Avoid updating documents in place—delete and re-index instead.
- Pre-analyze field content to understand token output before indexing.
- Use `StoredField` only when you need to retrieve the original value.
- Monitor segment count—too many small segments degrade performance.

## Hands-on Labs

1. **Basic Indexing**: Create an index, add documents, and search with `IndexSearcher`.
2. **Custom Analyzer**: Build a custom analyzer with synonyms and stemming.
3. **Phrase Queries**: Implement proximity and phrase matching with `slop` parameter.
4. **Faceting**: Use `FacetCollector` to count documents by category.
5. **Segment Merging**: Observe how merging affects index size and query speed.

## Interview Questions

1. How does the inverted index enable fast full-text search?
2. Explain the segment architecture. Why are segments immutable?
3. What is the difference between `TextField` and `StringField` in Lucene?
4. Describe the BM25 scoring model. How does it differ from TF-IDF?
5. What is a TokenFilter, and how does it differ from a Tokenizer?
6. How does Lucene handle concurrent reads and writes to an index?
7. Explain how BKD trees enable efficient numeric range queries.

## References

- [Lucene Documentation](https://lucene.apache.org/core/9_0/core/index.html)
- [Lucene in Action](https://www.manning.com/books/lucene-in-action-second-edition)
- [Apache Lucene GitHub](https://github.com/apache/lucene)
- [How Elasticsearch Uses Lucene](https://www.elastic.co/blog/found-elasticsearch-from-the-ground-up)
