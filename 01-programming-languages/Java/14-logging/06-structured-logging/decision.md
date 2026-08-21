# Decision Framework: Structured Logging

## When to Use Structured Logging

### Use Structured Logging When:

- **Centralized logging** - Sending to ELK, Splunk, Datadog, etc.
- **Microservices** - Correlating events across services
- **High volume** - Need efficient querying/filtering
- **Compliance** - Audit trails require structured data
- **Machine analysis** - Automated alerting, anomaly detection
- **Multi-tenant systems** - Filter by tenant easily

### Consider Alternatives When:

- **Development only** - Human-readable text is fine
- **Low volume** - grep works for small logs
- **No log aggregation** - Console output only
- **Performance critical** - JSON serialization has overhead

## JSON vs Text Logging

| Aspect | JSON (Structured) | Text (Traditional) |
|--------|------------------|-------------------|
| Machine parsing | Excellent | Poor (regex required) |
| Human readability | Moderate | Excellent |
| Searchability | Field-based | Line-based |
| Storage efficiency | Larger | Smaller |
| Query performance | Fast (indexed) | Slow (scan) |
| Log analysis tools | Native support | Limited |
| Network transfer | Larger payload | Smaller payload |

## Field Naming Strategy

| Category | Convention | Example |
|----------|-----------|---------|
| Identifiers | camelCase | `userId`, `requestId` |
| Timestamps | ISO 8601 | `2024-01-15T10:30:45.123Z` |
| Levels | Uppercase | `INFO`, `ERROR` |
| Booleans | camelCase | `isSuccess`, `hasError` |
| Counts | camelCase | `itemCount`, `retryCount` |
| Durations | Suffix `Ms` or `Duration` | `durationMs` |

## Log Analysis Tool Integration

| Tool | Format | Configuration |
|------|--------|--------------|
| ELK Stack | JSON | Logstash encoder |
| Splunk | JSON/HEC | HTTP Event Collector |
| Datadog | JSON | Datadog appender |
| CloudWatch | JSON | CloudWatch appender |
| Azure Monitor | JSON | Application Insights |

## Performance Considerations

| Aspect | Impact | Mitigation |
|--------|--------|------------|
| JSON serialization | +10-30% CPU | Use compact mode, async |
| Larger payload | +20-50% network | Compress, batch |
| Field overhead | +10-30% memory | Limit custom fields |
| Indexing | -80% query time | Index common fields |

## Migration Strategy

1. **Start with MDC** - Add requestId, userId to existing logs
2. **Add JSON appender** - Keep text for console, JSON for file
3. **Standardize fields** - Document naming conventions
4. **Update patterns** - Ensure all services use same field names
5. **Test queries** - Verify search works in analysis tool
6. **Roll out gradually** - Service by service
