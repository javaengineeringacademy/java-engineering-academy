# Splunk - Log Analytics and Operational Intelligence

## Overview

Splunk is a platform for searching, monitoring, and analyzing machine-generated data. It provides real-time visibility into infrastructure and application logs, enabling teams to troubleshoot issues, detect security threats, and gain operational insights from their data.

## Why It Matters

- Indexes and searches massive volumes of machine data in real time
- Provides dashboards and visualizations for operational monitoring
- Enables proactive alerting on errors and anomalies
- Supports compliance requirements through centralized log retention
- Offers machine learning capabilities for predictive analytics

## Key Concepts

- **Index**: Storage bucket where Splunk stores ingested data
- **Source**: Origin of the data (file, network, application)
- **SPL**: Search Processing Language for querying indexed data
- **Dashboard**: Visual representation of search results and metrics
- **Alert**: Automated notification triggered by search results
- **Forwarder**: Agent that collects and sends data to Splunk

## Core Topics

### Data Ingestion
- Universal forwarder and heavy forwarder configuration
- Data inputs: files, network streams, scripts
- Data parsing and transformation at ingestion

### Search and Analysis
- SPL fundamentals for querying data
- Field extraction and normalization
- Statistical functions and reporting commands

### Dashboards and Visualization
- Building real-time dashboards with XML or Studio
- Drill-down links and interactive elements
- Dashboard performance optimization

### Alerting and Monitoring
- Creating alerts with threshold and scheduled conditions
- Alert actions: email, webhook, integration with ITSM
- Notable events and correlation rules for security

## Best Practices

1. Use indexers and search heads for scalable architecture
2. Implement data models for consistent field normalization
3. Create summary indexes for frequently run searches
4. Use role-based access control to restrict data access
5. Monitor Splunk health and indexing performance
6. Retain logs according to compliance requirements with tiered storage

## Hands-on Labs

1. **Splunk Installation**: Deploy Splunk Enterprise using Docker
2. **Log Ingestion**: Configure forwarders to send application logs to Splunk
3. **SPL Queries**: Write searches to analyze log patterns and errors
4. **Dashboard Creation**: Build a real-time operational dashboard
5. **Alert Configuration**: Set up alerts for critical error thresholds

## Interview Questions

1. What is SPL and how does it differ from SQL?
2. Explain the difference between Splunk forwarders (universal vs. heavy)
3. How would you optimize dashboard performance in Splunk?
4. What are data models and why are they important for Splunk analysis?
5. Describe a use case for Splunk alerts in incident management
6. How does Splunk handle high-volume log ingestion and storage?

## References

- Splunk Documentation: https://docs.splunk.com/
- SPL Search Commands: https://docs.splunk.com/Splexicon:Search
- Splunk Enterprise: https://www.splunk.com/en_us/products/splunk-enterprise.html
- Splunk Cloud: https://www.splunk.com/en_us/products/splunk-cloud-platform.html
