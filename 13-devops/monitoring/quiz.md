# Observability Quiz

## Question 1
What are the three pillars of observability?
- A) Monitoring, alerting, and dashboards
- B) Logs, metrics, and traces
- C) CPU, memory, and disk usage
- D) Testing, debugging, and profiling

**Answer: B**
**Explanation:** The three pillars are Logs (discrete event records), Metrics (numeric measurements over time), and Traces (request flow across services). Together they provide a complete picture of system behavior.

## Question 2
What is structured logging and why is it important?
- A) Logging with timestamps only
- B) Logging events in a machine-readable format like JSON with key-value pairs
- C) Logging in alphabetical order
- D) Using only the ERROR level

**Answer: B**
**Explanation:** Structured logging outputs logs in a standardized, machine-readable format (typically JSON) with consistent fields. This enables efficient parsing, searching, filtering, and integration with log aggregation systems.

## Question 3
What is distributed tracing used for?
- A) Tracking employee work hours
- B) Tracking the flow of a request across multiple services in a distributed system
- C) Monitoring CPU temperature
- D) Managing database connections

**Answer: B**
**Explanation:** Distributed tracing follows a request as it flows through multiple services, showing the path, timing, and any errors at each step. It helps identify bottlenecks and failures in microservice architectures.

## Question 4
What is the difference between a counter and a gauge metric?
- A) They are the same
- B) A counter only increments monotonically, a gauge can go up and down
- C) A gauge only works with integers
- D) A counter measures time, a gauge measures count

**Answer: B**
**Explanation:** A counter (e.g., total requests) only increases and resets on process restart. A gauge (e.g., current queue size, temperature) represents a value that can increase or decrease over time.

## Question 5
What is a health check endpoint?
- A) An endpoint that checks disk space
- B) An HTTP endpoint that returns the service's readiness and liveness status
- C) An authentication endpoint
- D) A logging endpoint

**Answer: B**
**Explanation:** A health check endpoint (e.g., `/health` or `/ready`) allows orchestrators like Kubernetes to determine if a service is ready to receive traffic (readiness) or is alive and functioning (liveness).