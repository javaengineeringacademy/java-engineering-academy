# Uber: Ride-Sharing at Global Scale

How Uber built a platform handling 19M+ trips daily across 10,000+ cities.

## Company Overview

Uber is a mobility platform connecting riders with drivers, delivering food, and enabling freight logistics. Their engineering challenge: matching supply and demand in real-time across the globe.

## Architecture Evolution

### Phase 1: Monolith (2010-2014)
- Single Ruby on Rails application
- PostgreSQL database
- Manual scaling

### Phase 2: SOA (2014-2018)
- Domain-oriented microservices
- Schema registry for contracts
- Domain-oriented microservice architecture

### Phase 3: Platform (2018-Present)
- Self-serve platform
- Domain-oriented architecture
- Advanced data infrastructure

## Core Architecture

### Domain-Oriented Microservice Architecture (DOMA)

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│         (Rider App, Driver App, Uber Eats, Uber Freight)   │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    Gateway Layer                            │
│              (API Gateway, Rate Limiting)                   │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Domain Layer                              │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Trip Domain│  Payment     │  Location    │  Matching      │
│              │  Domain      │  Domain      │  Domain        │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Trip State  │  Billing     │  GPS         │  Driver Match  │
│  Trip Events │  Fraud       │  Geofence    │  ETA           │
│  Trip History│  Commissions │  Routing     │  Dispatch      │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Infrastructure Layer                       │
│    (Databases, Message Queues, Caches, Storage)             │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### Schema Registry
- Enforces API contracts
- Backward/forward compatibility
- Code generation
- Schema evolution

```protobuf
// Proto definition for Trip
syntax = "proto3";

message Trip {
    string trip_id = 1;
    string rider_id = 2;
    string driver_id = 3;
    TripStatus status = 4;
    Location pickup = 5;
    Location dropoff = 6;
    google.protobuf.Timestamp created_at = 7;
    google.protobuf.Timestamp updated_at = 8;
}

enum TripStatus {
    TRIP_STATUS_UNSPECIFIED = 0;
    TRIP_STATUS_REQUESTING = 1;
    TRIP_STATUS_MATCHED = 2;
    TRIP_STATUS_IN_PROGRESS = 3;
    TRIP_STATUS_COMPLETED = 4;
    TRIP_STATUS_CANCELLED = 5;
}
```

### Domain-Oriented Architecture

**Principles:**
1. Enforce clear domain boundaries
2. Provide well-defined public APIs
3. Make internal implementation details private
4. Enable domain-specific optimizations

**Benefits:**
- Reduced coupling between teams
- Clear ownership boundaries
- Easier reasoning about systems
- Better scalability

### Cadence Workflow Engine
- Durable execution framework
- Handles complex workflows
- Fault-tolerant orchestration
- Used for: trip lifecycle, payments, promotions

```go
// Trip Workflow Example
func TripWorkflow(ctx workflow.Context, tripRequest TripRequest) (*Trip, error) {
    // Request matching
    var matchedDriver Driver
    err := workflow.GetSignalChannel(ctx, "driver-match").Receive(ctx, &matchedDriver)
    if err != nil {
        return nil, err
    }
    
    // Wait for trip completion
    var tripComplete TripComplete
    err = workflow.GetSignalChannel(ctx, "trip-complete").Receive(ctx, &tripComplete)
    if err != nil {
        return nil, err
    }
    
    // Process payment
    err = workflow.ExecuteActivity(ctx, ProcessPayment, tripComplete.Fare).Get(ctx, nil)
    if err != nil {
        return nil, err
    }
    
    return &tripComplete.Trip, nil
}
```

### Schema Evolution Strategy

**Compatibility Levels:**
- **BACKWARD**: New schema can read old data
- **FORWARD**: Old schema can read new data
- **FULL**: Both backward and forward compatible

**Migration Process:**
1. Add new fields as optional
2. Update consumers to handle new schema
3. Remove deprecated fields after migration
4. Version schemas with semantic versioning

## Data Architecture

### MySQL Sharding
- Horizontal sharding by domain
- Consistent hashing for distribution
- Cross-shard queries avoided
- Read replicas for scaling reads

### Kafka
- 5M+ events/second
- Real-time data pipelines
- Event sourcing patterns
- Exactly-once semantics

### Apache Cassandra
- Time-series data (trip history)
- Multi-datacenter replication
- Tunable consistency

### Redis
- Session management
- Rate limiting
- Geospatial indexing

## Real-Time Systems

### Trip Execution
```
Rider Request → Matching → Driver Acceptance → GPS Tracking → Trip Completion → Payment
     │              │              │                │               │             │
     ▼              ▼              ▼                ▼               ▼             ▼
  Validate      Find Driver   Update State     Stream GPS     Calculate Fare  Process Payment
  Location      (ML Model)    (Trip Service)   (WebSockets)   (Pricing Svc)   (Payment Svc)
```

### ETA Prediction
- ML-based models
- Real-time traffic data
- Historical patterns
- Route optimization

### Surge Pricing
- Dynamic pricing algorithm
- Supply-demand balancing
- Real-time adjustments
- Regulatory compliance

## Observability

### Metrics
- 1M+ metrics in production
- Real-time dashboards
- Anomaly detection
- Custom alerting

### Tracing
- Distributed tracing (OpenTracing)
- End-to-end latency tracking
- Dependency mapping

### Logging
- Structured logging
- Centralized log aggregation
- Real-time log analysis

## Reliability Engineering

### Chaos Engineering
- Game days
- Failure injection
- Region failover testing
- Network partition simulation

### Circuit Breaking
- Automatic fallbacks
- Graceful degradation
- Load shedding

### Multi-Region
- Active-active deployment
- Data replication
- Traffic routing
- Failover automation

## Developer Productivity

### Internal Developer Platform
- Self-service deployments
- Standardized tooling
- Shared libraries
- Documentation generation

### Testing Infrastructure
- Integration testing
- Chaos testing
- Performance testing
- Security testing

## Organizational Structure

### Domain Teams
- Each domain has dedicated teams
- Clear ownership boundaries
- Independent release cycles
- Shared platform services

### Platform Teams
- Infrastructure provisioning
- Developer tools
- Observability stack
- Security and compliance

## Key Lessons

1. **Domain Boundaries Matter**: Align architecture with business domains
2. **Contracts are Critical**: Schema registries prevent breaking changes
3. **Workflows Need Durability**: Handle failures gracefully in distributed systems
4. **Real-Time is Complex**: GPS, matching, and pricing require specialized systems
5. **Platform Enables Velocity**: Self-serve platforms multiply engineering productivity
6. **Data Consistency is Hard**: Event sourcing and saga patterns help

## Statistics

- **Trips**: 19M+ trips daily
- **Cities**: 10,000+ globally
- **Services**: 4,000+ microservices
- **Engineers**: 5,000+
- **Deployments**: 1,000+ per day
- **Uptime**: 99.99%

## References

- [Uber Engineering Blog](https://www.uber.com/blog/engineering/)
- [Domain-Oriented Microservice Architecture](https://www.uber.com/blog/microservice-architecture/)
- [Schema Registry](https://www.uber.com/blog/schema-registry/)
- [Cadence Workflow Engine](https://www.uber.com/blog/cadence/)
- [Uber's Data Infrastructure](https://www.uber.com/blog/data-infrastructure-at-uber/)
