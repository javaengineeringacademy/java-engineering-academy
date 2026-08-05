# 30 - Search and Analytics

## Overview

Modern applications rely on search and analytics to unlock value from large datasets. This module covers the foundational technologies for full-text search, log analytics, and observability—starting from the core indexing library (Lucene) up through distributed search engines (Elasticsearch, OpenSearch, Solr) and visualization platforms (Kibana).

## Why It Matters

- **User Experience**: Fast, relevant search results drive product adoption and revenue.
- **Operational Intelligence**: Real-time log analysis and metrics enable rapid incident response.
- **Cost Efficiency**: Choosing the right tool avoids over-provisioning infrastructure.
- **Scalability**: Distributed search architectures handle billions of documents across clusters.

## Module Structure

| Path | Topic |
|------|-------|
| `lucene/` | Apache Lucene core library |
| `elasticsearch/` | Elasticsearch distributed search |
| `opensearch/` | OpenSearch (Elasticsearch fork) |
| `solr/` | Apache Solr search platform |
| `kibana/` | Kibana visualization and dashboards |

## Technology Landscape

```
┌─────────────────────────────────────────────┐
│              Application Layer              │
│           Kibana / Dashboards / UI           │
├─────────────────────────────────────────────┤
│            Search Engine Layer               │
│   Elasticsearch / OpenSearch / Solr          │
├─────────────────────────────────────────────┤
│            Core Indexing Layer               │
│              Apache Lucene                   │
└─────────────────────────────────────────────┘
```

## Key Distinctions

- **Lucene** is a library—used within Elasticsearch and Solr, not a standalone server.
- **Elasticsearch** is the dominant commercial/open-source distributed search engine.
- **OpenSource** is the community-driven fork of Elasticsearch after license changes.
- **Solr** is an older, battle-tested alternative built on the same Lucene core.
- **Kibana** provides the UI layer for Elasticsearch/OpenSearch analytics.

## Prerequisites

- Familiarity with JSON and REST APIs
- Basic understanding of inverted indexes and text analysis
- Docker or a local dev environment for hands-on labs

## Learning Path

1. Start with `lucene/` to understand indexing fundamentals
2. Move to `elasticsearch/` for distributed search architecture
3. Compare with `opensearch/` and `solr/` for alternative approaches
4. Finish with `kibana/` for visualization and dashboards
