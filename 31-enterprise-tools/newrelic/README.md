# New Relic

## Overview

New Relic is a full-stack observability platform offering APM, infrastructure monitoring, browser monitoring, mobile monitoring, log management, and distributed tracing. It uses NRQL (New Relic Query Language) for querying telemetry data and provides a unified view of application and infrastructure health.

## Why It Matters

New Relic consolidates observability data into a single platform, reducing context switching and accelerating incident response. Its NRQL query language enables custom analysis, and its error analytics and service maps help teams quickly identify root causes in complex distributed systems.

## Key Concepts

- **APM:** Transaction-level performance monitoring with distributed tracing
- **Browser Monitoring:** Front-end performance and JavaScript error tracking
- **Mobile Monitoring:** Native mobile app performance and crash analytics
- **Infrastructure Monitoring:** Host, container, and cloud service metrics
- **Logs:** Centralized log management with context linking to APM
- **NRQL:** SQL-like query language for telemetry data exploration
- **Service Maps:** Automated dependency visualization for services
- **Errors Inbox:** Centralized error tracking and grouping across all telemetry

## Core Topics

- Application performance monitoring with transaction traces
- Distributed tracing across microservices and message queues
- Infrastructure agent deployment and configuration
- NRQL query construction for custom dashboards and alerts
- Browser agent installation and front-end performance tracking
- Mobile agent SDK integration for iOS and Android
- Log forwarding and contextual log correlation
- Alert condition configuration with NRQL-based thresholds

## Best Practices

- Use consistent entity naming conventions across agents
- Create NRQL alert conditions for flexible threshold monitoring
- Enable distributed tracing context propagation in all services
- Use service maps to identify critical dependency paths
- Leverage Errors Inbox for prioritized error resolution
- Implement golden signals (latency, traffic, errors, saturation) in dashboards
- Regularly review and archive historical data to control costs

## Hands-on Labs

1. Install the New Relic APM agent and instrument a sample application
2. Build a NRQL query to analyze transaction throughput and error rates
3. Create a custom dashboard using NRQL-powered widgets
4. Set up infrastructure monitoring for a Docker or Kubernetes environment
5. Configure an alert condition using NRQL for response time thresholds
6. Integrate log forwarding and correlate logs with APM transaction data

## Interview Questions

1. How does NRQL differ from SQL, and what telemetry-specific features does it provide?
2. Explain the concept of golden signals in New Relic and why they matter for SRE practices.
3. How would you use New Relic's distributed tracing to troubleshoot latency in a microservices architecture?
4. Describe the difference between APM, browser monitoring, and mobile monitoring use cases.
5. How do you correlate logs with APM data in New Relic, and why is this correlation valuable?
6. What strategies would you use to manage alert noise in New Relic?
7. Explain how New Relic's service maps are generated and how they aid incident response.
8. How would you design a monitoring strategy for a hybrid cloud environment using New Relic?

## References

- [New Relic Official Documentation](https://docs.newrelic.com/)
- [NRQL Reference](https://docs.newrelic.com/docs/nrql/nrql-query-language/nrql-query-components/)
- [New Relic University](https://learn.newrelic.com/)
- [Error Analytics Guide](https://docs.newrelic.com/docs/apm/apm-ui-pages/errors-analytics/errors-analytics-ui-page/)
- [New Relic GitHub](https://github.com/newrelic)
