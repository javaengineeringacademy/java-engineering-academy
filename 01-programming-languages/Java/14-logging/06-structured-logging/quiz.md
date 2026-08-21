# Quiz: Structured Logging

## Multiple Choice

### Q1: What is the primary benefit of structured logging?
- A) Smaller log files
- B) Faster log writing
- C) Machine-parseable, searchable log entries
- D) More colorful output

### Q2: Which format is most commonly used for structured logging?
- A) CSV
- B) XML
- C) JSON
- D) YAML

### Q3: What field should always include a unique identifier for request tracing?
- A) `id`
- B) `requestId`
- C) `trace`
- D) `logId`

### Q4: How do you include MDC values in Logback JSON output?
- A) They are included automatically
- B) Use includeMdcKeyName configuration
- C) Manually add each field in code
- D) MDC does not work with JSON

### Q5: What is the standard timestamp format for structured logs?
- A) 2024-01-15 10:30:45
- B) 01/15/2024 10:30:45
- C) 2024-01-15T10:30:45.123Z
- D) 10:30:45 2024-01-15

### Q6: What is the tradeoff of structured logging?
- A) Slower query performance
- B) Larger log files and more CPU for serialization
- C) Less readable output
- D) Both B and C

### Q7: Which tool benefits most from structured logging?
- A) grep
- B) tail
- C) ELK Stack
- D) cat

## Answers

1. **C** - Structured logs are machine-parseable and searchable
2. **C** - JSON is the standard format for structured logging
3. **B** - `requestId` is the standard field for request tracing
4. **A** - MDC values are automatically included when configured properly
5. **C** - ISO 8601 format with timezone is the standard
6. **D** - Structured logging increases file size and CPU usage, and reduces human readability
7. **C** - ELK Stack (Elasticsearch, Logstash, Kibana) is designed for structured log analysis
