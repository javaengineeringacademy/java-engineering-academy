# OpenSearch

## Overview

OpenSearch is a community-driven, open-source fork of Elasticsearch 7.10, maintained by AWS. It provides the same distributed search and analytics capabilities with a commitment to fully open-source licensing (Apache 2.0).

## Why It Matters

- Addresses licensing concerns after Elastic moved Elasticsearch to SSPL.
- Fully open-source with no commercial restrictions on use or distribution.
- Maintains API compatibility with Elasticsearch 7.x for easy migration.
- Includes integrated security, alerting, and anomaly detection out of the box.

## Key Concepts

| Concept | Description |
|---------|-------------|
| **Index** | Collection of documents with mappings and settings |
| **Domain** | AWS Managed service for running OpenSearch clusters |
| **ISM** | Index State Management for lifecycle policies |
| **ISM Templates** | Reusable policies applied across multiple indices |
| **Observability** | Integrated logs, traces, and metrics in one platform |
| **Security Plugin** | Built-in authentication, authorization, and audit logging |

## Core Topics

### Architecture Compatibility

OpenSearch preserves the Elasticsearch 7.10 architecture: same inverted index, same shard model, same cluster topology. Plugins are recompiled for OpenSearch but follow the same extension model.

### Query DSL

OpenSearch uses the same Query DSL as Elasticsearch 7.x:

```json
{
  "query": {
    "bool": {
      "must": [
        { "match": { "message": "connection timeout" } }
      ],
      "filter": [
        { "term": { "level": "error" } }
      ]
    }
  }
}
```

### Security Plugin

- **Fine-grained access control**: Index, field, and document-level permissions.
- **Authentication**: Internal user database, LDAP, SAML, OIDC.
- **Audit logging**: Tracks all cluster access and configuration changes.

### Alerting and Anomaly Detection

- **Alerting**: Define monitors that trigger actions when thresholds are met.
- **Anomaly Detection**: ML-based identification of unusual patterns in time-series data.
- **Notebooks**: Collaborative investigative documents combining queries and visualizations.

### Migration from Elasticsearch

Migration path: Snapshot-based restore from ES 7.10 to OpenSearch, or reindex from remote cluster using the `_reindex` API with a remote source.

## Best Practices

- Use index templates with ILM policies for log data retention.
- Enable the security plugin even in development to avoid production surprises.
- Use fine-grained access control to restrict sensitive fields.
- Monitor cluster with the built-in Performance Analyzer plugin.
- Use cross-cluster replication for disaster recovery and read-heavy workloads.

## Hands-on Labs

1. **Cluster Deployment**: Deploy OpenSearch with Docker Compose including security.
2. **Security Configuration**: Set up SAML authentication and index-level permissions.
3. **Alerting**: Create a monitor that triggers when error rates exceed a threshold.
4. **Migration Lab**: Migrate a snapshot from Elasticsearch 7.10 to OpenSearch.
5. **Observability**: Ingest logs, traces, and metrics into OpenSearch Dashboards.

## Interview Questions

1. What are the key differences between OpenSearch and Elasticsearch 8.x?
2. How does OpenSearch handle security differently from Elasticsearch's basic license tier?
3. Explain the purpose of Index State Management (ISM). How does it differ from ILM?
4. Describe the alerting monitor types. When would you use each?
5. How would you migrate an existing Elasticsearch 7.10 cluster to OpenSearch?
6. What is the Performance Analyzer plugin, and how does it help in production?
7. How does cross-cluster replication work in OpenSearch for disaster recovery?

## References

- [OpenSearch Documentation](https://opensearch.org/docs/latest/)
- [OpenSearch GitHub](https://github.com/opensearch-project/OpenSearch)
- [AWS OpenSearch Service](https://docs.aws.amazon.com/opensearch-service/)
- OpenSearch community forums and Slack channel
