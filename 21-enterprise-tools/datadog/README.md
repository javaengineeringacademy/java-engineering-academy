# Datadog

## Overview

Datadog is a cloud-scale monitoring and security platform providing unified observability across infrastructure, applications, logs, and security. It offers APM, infrastructure monitoring, log management, real-user monitoring (RUM), and security monitoring with 750+ integrations.

## Why It Matters

Datadog eliminates tool sprawl by consolidating metrics, traces, logs, and security data into a single platform. It accelerates troubleshooting, enables proactive alerting, and provides end-to-end visibility for modern cloud-native and hybrid environments.

## Key Concepts

- **APM (Application Performance Monitoring):** Distributed tracing and service mapping for request flow analysis
- **Infrastructure Monitoring:** Host, container, and cloud resource metrics with automatic discovery
- **Log Management:** Centralized log collection, parsing, and search across all sources
- **RUM (Real User Monitoring):** Front-end performance and user experience tracking
- **Security Monitoring:** Threat detection using logs and cloud audit trails
- **Dashboards:** Customizable visualizations for metrics, traces, and logs
- **SLOs (Service Level Objectives):** Reliability tracking with error budgets
- **Integrations:** 750+ pre-built connectors for cloud providers, databases, and tools

## Core Topics

- Distributed tracing with trace search and service maps
- Custom metrics and tags for environment-specific monitoring
- APM instrumentation for Java, Python, Go, Node.js, and .NET
- Log pipelines and processors for structured parsing
- Alert monitors with anomaly detection and composite alerts
- Synthetics for API and browser-based uptime testing
- Cloud Security Monitoring and Compliance
- Integration with AWS, Azure, GCP, Kubernetes, and Docker

## Best Practices

- Use consistent tagging conventions across all data sources
- Create dedicated monitors for critical service SLOs
- Enable log indexing only for actionable logs to manage costs
- Use APM trace context propagation for end-to-end visibility
- Leverage anomaly detection monitors for proactive alerting
- Implement Synthetics for external-facing service checks
- Review and tune alert thresholds regularly to reduce noise

## Hands-on Labs

1. Install the Datadog Agent and configure a basic integration
2. Create a custom dashboard with multiple widget types
3. Set up APM instrumentation for a sample application
4. Configure log pipelines to parse and enrich application logs
5. Create an SLO monitor and track error budgets over time
6. Build a Synthetics API test for endpoint availability

## Interview Questions

1. How does Datadog's distributed tracing differ from traditional log-based monitoring?
2. Explain the role of tags in Datadog's data model and how they enable cross-source correlation.
3. What strategies would you use to manage log management costs in a high-volume environment?
4. Describe how Datadog's anomaly detection monitors work compared to threshold-based alerts.
5. How would you design a monitoring strategy for a microservices architecture using Datadog?
6. What is the difference between Datadog APM and RUM, and when would you use each?
7. How do SLOs and error budgets help teams prioritize reliability work?
8. Explain the purpose of Synthetics testing and how it complements real-user monitoring.

## References

- [Datadog Official Documentation](https://docs.datadoghq.com/)
- [Datadog Learning Center](https://learn.datadoghq.com/)
- [APM and Distributed Tracing Guide](https://docs.datadoghq.com/tracing/)
- [Log Management Documentation](https://docs.datadoghq.com/logs/)
- [Datadog GitHub](https://github.com/DataDog)
