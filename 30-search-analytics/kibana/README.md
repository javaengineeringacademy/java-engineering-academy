# Kibana

## Overview

Kibana is an open-source data visualization and exploration tool for Elasticsearch and OpenSearch. It provides dashboards, charts, maps, and query interfaces that make it possible to analyze large volumes of log data, metrics, and traces without writing complex queries.

## Why It Matters

- Provides the user-facing layer for the Elastic/ELK stack.
- Enables non-technical stakeholders to explore data through drag-and-drop dashboards.
- Supports security analytics (SIEM), observability (APM), and operational monitoring.
- Reduces time-to-insight from hours to minutes for incident response.

## Key Concepts

| Concept | Description |
|---------|-------------|
| **Dashboard** | A collection of visualizations and saved searches |
| **Visualization** | A chart, metric, or table built from Elasticsearch data |
| **Lens** | Drag-and-drop visualization builder |
| **Discover** | Ad-hoc query and exploration interface |
| **Index Pattern** | Defines which Elasticsearch indices Kibana queries |
| **Alerting Rule** | Automated notifications based on query results |

## Core Topics

### Dashboard Architecture

Dashboards combine multiple panels, each backed by a saved search, visualization, or Lens configuration. Panels can be resized, rearranged, and filtered interactively.

### Query Language Support

- **KQL (Kibana Query Language)**: Lucene-based syntax for filtering documents.
- **Lucene Query Syntax**: Full access to Lucene query capabilities.
- **ES|QL**: New piped query language for composable data transformations.

```
status: 500 and host.name: web-server-* 
```

### Lens and Visualization Types

- **Line/Bar/Area charts**: Time-series and categorical data.
- **Maps**: Geospatial data with tile layers.
- **TSVB**: Advanced time-series visualizations.
- **Vega**: Custom D3-based visualizations.
- **Gauge/Metric**: Single-value KPI displays.

### Observability and Security

- **APM**: Application performance monitoring with distributed tracing.
- **Logs**: Centralized log viewing with live streaming.
- **Metrics**: System and application metric collection.
- **SIEM**: Security event correlation and threat detection.
- **Case Management**: Collaborative investigation and incident tracking.

### Alerting and Actions

Kibana can trigger notifications via email, Slack, PagerDuty, or custom webhooks when data meets defined conditions. Rules support threshold, Elasticsearch query, and metric threshold types.

## Best Practices

- Use index patterns with date-based naming (`logs-YYYY.MM.DD`) for ILM integration.
- Pin frequently-used filters to dashboards for consistency.
- Use Kibana Spaces to organize dashboards by team or environment.
- Export and version-control dashboard JSON for reproducibility.
- Use Lens for quick exploration and Vega for complex custom visualizations.
- Set up alerting early to avoid blind spots during production incidents.

## Hands-on Labs

1. **Data Exploration**: Use Discover to search and filter Elasticsearch data.
2. **Dashboard Creation**: Build a multi-panel dashboard with charts and tables.
3. **Custom Visualization**: Create a Vega visualization for a specific use case.
4. **Alerting Rule**: Set up a threshold alert for error rate spikes.
5. **APM Integration**: Instrument a sample application and view traces in Kibana.

## Interview Questions

1. What is the difference between Kibana Lens and traditional visualizations?
2. Explain KQL vs Lucene query syntax. When would you choose one over the other?
3. How do Kibana Spaces help organize a multi-team environment?
4. Describe the components of a Kibana alerting rule.
5. How does Kibana handle index pattern rotation with ILM-managed indices?
6. What visualization type would you use for geospatial log data?
7. How do you version-control Kibana dashboards for deployment across environments?

## References

- [Kibana Guide](https://www.elastic.co/guide/en/kibana/current/)
- [Kibana Lens Documentation](https://www.elastic.co/guide/en/kibana/lens.html)
- [Elastic Observability](https://www.elastic.co/observability)
- [Elastic Security](https://www.elastic.co/security)
