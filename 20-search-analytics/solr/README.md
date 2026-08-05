# Apache Solr

## Overview

Apache Solr is an open-source search platform built on Apache Lucene. It provides distributed indexing, replication, full-text search, faceted search, and rich document handling. Solr has been a production search solution since 2004 and powers sites like eBay, Netflix, and AOL.

## Why It Matters

- Battle-tested with nearly two decades of production use.
- XML-based configuration is familiar to enterprise environments.
- Tight integration with Hadoop ecosystem for large-scale batch indexing.
- SolrCloud provides distributed search with automatic failover.

## Key Concepts

| Concept | Description |
|---------|-------------|
| **Collection** | A distributed index analogous to an Elasticsearch index |
| **Core** | A single-node instance of a Solr index |
| **Shard** | A partition of a collection across nodes |
| **Replica** | A redundant copy of a shard |
| **ZooKeeper** | Coordination service for cluster state and leader election |
| **schema.xml** | Field types, analyzers, and field definitions |

## Core Topics

### Schema Design

Solr uses a declarative `managed-schema` (or `schema.xml`) to define field types, analyzers, and field rules. Dynamic fields allow flexible schema evolution without explicit definitions.

```xml
<field name="title" type="text_general" indexed="true" stored="true"/>
<field name="price" type="pfloat" indexed="true" stored="true"/>
<dynamicField name="*_s" type="string" indexed="true" stored="true"/>
```

### SolrCloud Architecture

- Distributed across multiple nodes with automatic sharding.
- ZooKeeper manages cluster state, configuration, and leader election.
- Near-real-time search with `commit` and `softCommit` operations.
- Automatic failover when nodes go down.

### Query Syntax

Solr offers two query languages: the traditional Lucene query parser and the newer Extended DisMax (eDisMax) parser for more forgiving user-facing search.

```
title:"distributed systems" AND category:tech
{!edismax qf="title^2 author" mm="75%"}solr search
```

### Request Handlers

- **SearchHandler**: Processes search queries with configurable components (query parsing, highlighting, faceting).
- **UpdateHandler**: Handles indexing, updates, and deletes.
- **StreamHandler**: Supports streaming expressions for complex analytics.

### Streaming Expressions

Solr provides a SQL-like streaming API for complex aggregation and computation:

```
search(collection, q="*", fl="*,score", sort="score desc",
      rows="10", fq="category:tech")
```

## Best Practices

- Use `managed-schema` for runtime schema changes without restarting.
- Tune `autoCommit` and `autoSoftCommit` for near-real-time indexing.
- Use `copyField` to index the same content into multiple field types.
- Monitor ZooKeeper quorum health—losing the majority breaks the cluster.
- Use `SolrJ` client library for Java applications over raw HTTP.
- Shard based on cardinality and access patterns, not just data volume.

## Hands-on Labs

1. **Single-Core Setup**: Start a standalone Solr instance and index sample documents.
2. **SolrCloud Deployment**: Spin up a 3-node cluster with Docker Compose.
3. **Faceted Search**: Configure facets and drill-down navigation.
4. **Highlighting**: Implement search result highlighting with configurable snippets.
5. **Streaming Expressions**: Build an analytics pipeline using streaming API.

## Interview Questions

1. What is the difference between a Solr `Core` and a `Collection`?
2. How does SolrCloud use ZooKeeper, and what happens when ZooKeeper loses quorum?
3. Explain `autoCommit` vs `autoSoftCommit`. How do they affect search latency?
4. What are dynamic fields, and when would you use them over explicit fields?
5. Compare the standard query parser with eDisMax. What advantages does eDisMax offer?
6. How does Solr handle document replication across shards?
7. Describe the role of `copyField` in schema design.

## References

- [Apache Solr Reference Guide](https://solr.apache.org/guide/)
- [Solr Tutorial](https://solr.apache.org/tutorial.html)
- [SolrCloud Architecture](https://solr.apache.org/guide/solrcloud.html)
- Apache Solr mailing lists and community wiki
