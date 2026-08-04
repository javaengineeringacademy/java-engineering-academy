# LinkedIn: Professional Network at Scale

How LinkedIn built a platform connecting 900M+ professionals worldwide.

## Company Overview

LinkedIn is the world's largest professional network. Their engineering challenge: building a social graph, feed, and talent marketplace at massive scale.

## Architecture Evolution

### Phase 1: Monolith (2003-2010)
- Single Java application
- Oracle database
- Manual scaling

### Phase 2: SOA (2010-2015)
- 500+ services
- REST.li framework
- Kafka adoption
- Graph database

### Phase 3: Cloud Native (2015-Present)
- Microsoft Azure
- Event-driven architecture
- Real-time ML
- Data platform evolution

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│         (Web, Mobile, API Partners, LinkedIn Lite)          │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    API Gateway                              │
│          (REST.li, GraphQL, Rate Limiting)                  │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Domain Services                          │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Profile    │  Feed        │  Messaging   │  Jobs          │
│   Domain     │  Domain      │  Domain      │  Domain        │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Connections │  Timeline    │  InMail      │  Applications  │
│  Endorsements│  Activity    │  Notifications│  Recommendations│
│  Groups      │  Content     │  Presence    │  Recruiter     │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Infrastructure Layer                       │
│    (Databases, Message Queues, Caches, ML Platform)         │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### REST.li Framework
- RESTful service framework
- Code generation
- Type safety
- Batch operations

```java
// REST.li Resource Example
@RestletActions(Metadata.class)
public class MetadataResource extends CollectionResourceTemplate<String, Metadata> {
    
    @Override
    public Metadata get(String id) {
        return metadataService.getMetadata(id);
    }
    
    @Override
    public void update(String id, Metadata entity) {
        metadataService.updateMetadata(id, entity);
    }
    
    @Action(name = "batchGet")
    public BatchFindResult<String, Metadata> batchGet(BatchGetRequest<String> request) {
        return metadataService.batchGet(request.getKeys());
    }
}

// REST.li Client Example
public class MetadataClient {
    private final RestClient client;
    
    public Metadata get(String id) throws RemoteInvocationException {
        GetRequest<String, Metadata> request = new GetRequestBuilder<String, Metadata>(
            "metadata", Metadata.class).setId(id).build();
        return client.sendRequest(request).getResponse();
    }
}
```

### LinkedIn Graph (LiDG)
- Professional social graph
- 900M+ members
- 12B+ connections
- Real-time updates

### Kafka at LinkedIn
- Invented Kafka
- 7 trillion messages/day
- Event sourcing backbone
- Real-time data pipelines

### Azure Migration
- Hybrid cloud approach
- Lift-and-shift strategy
- Cloud-native services
- Cost optimization

## Data Architecture

### Venice
- Derived data platform
- Online/offline serving
- Versioned datasets
- ML feature store

### Data Hub
- Metadata management
- Data discovery
- Data lineage
- Data quality

### Event Streaming
```java
// Kafka Producer
public class EventProducer {
    private final KafkaProducer<String, Event> producer;
    
    public void sendEvent(Event event) {
        ProducerRecord<String, Event> record = new ProducerRecord<>(
            "member-events",
            event.getMemberId(),
            event
        );
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send event", exception);
            }
        });
    }
}

// Kafka Consumer
@Component
public class EventConsumer {
    
    @KafkaListener(topics = "member-events", groupId = "feed-service")
    public void handleEvent(ConsumerRecord<String, Event> record) {
        Event event = record.value();
        feedService.processEvent(event);
    }
}
```

### Graph Database
- Member connections
- Company relationships
- Skill endorsements
- Content interactions

### Data Models
```sql
-- Member Profile
CREATE TABLE members (
    member_id BIGINT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    headline VARCHAR(300),
    location VARCHAR(200),
    industry VARCHAR(100),
    summary TEXT,
    created_at TIMESTAMP
);

-- Connection Relationships
CREATE TABLE connections (
    member_id BIGINT,
    connection_id BIGINT,
    connected_at TIMESTAMP,
    relationship_strength FLOAT,
    PRIMARY KEY (member_id, connection_id)
);

-- Feed Activity
CREATE TABLE feed_activities (
    activity_id VARCHAR(50) PRIMARY KEY,
    actor_id BIGINT,
    activity_type VARCHAR(50),
    content TEXT,
    created_at TIMESTAMP,
    engagement_count INT
);
```

## Feed System

### Feed Generation
1. **Gather**: Collect relevant activities
2. **Rank**: ML-based scoring
3. **Filter**: Remove noise and duplicates
4. **Assemble**: Create final feed
5. **Serve**: Return to client

### Ranking Algorithm
```python
class FeedRanker:
    def rank(self, activities, member):
        scores = []
        for activity in activities:
            score = self.calculate_score(activity, member)
            scores.append((activity, score))
        return sorted(scores, key=lambda x: x[1], reverse=True)
    
    def calculate_score(self, activity, member):
        affinity = self.affinity_score(activity.actor, member)
        relevance = self.relevance_score(activity, member)
        timeliness = self.timeliness_score(activity)
        engagement = self.engagement_score(activity)
        
        return (
            affinity * 0.35 +
            relevance * 0.30 +
            timeliness * 0.20 +
            engagement * 0.15
        )
```

### Real-time Updates
- WebSocket connections
- Push notifications
- Live streaming
- Activity feeds

## Search Infrastructure

### Search Pipeline
1. **Query Understanding**: NLP, intent detection
2. **Candidate Retrieval**: Inverted index, fuzzy matching
3. **Ranking**: ML models, personalization
4. **Filtering**: Location, industry, connections
5. **Presentation**: Clustering, deduplication

### Search Features
- People search
- Job search
- Company search
- Content search
- Skill search

## Talent Solutions

### Job Recommendations
- ML-based matching
- Skill extraction
- Career path analysis
- Market insights

### Recruiter Tools
- Advanced search
- InMail messaging
- Candidate tracking
- Analytics dashboard

### Learning Platform
- Skill assessments
- Course recommendations
- Learning paths
- Certifications

## Observability

### Metrics
- Real-time dashboards
- Anomaly detection
- Business metrics
- Infrastructure metrics

### Tracing
- Distributed tracing
- Latency analysis
- Dependency mapping

### Logging
- Structured logging
- Centralized aggregation
- Real-time analysis

## Developer Productivity

### Internal Developer Platform
- Self-service deployments
- Standardized tooling
- Shared libraries
- Documentation

### CI/CD Pipeline
- Automated testing
- Security scanning
- Performance testing
- Gradual deployment

### Development Environment
- Local development setup
- Service templates
- Shared libraries
- Documentation

## Organizational Structure

### Domain Teams
- Identity & Profile
- Feed & Content
- Messaging & Notifications
- Jobs & Recruiter
- Learning & Development
- Platform Infrastructure

### Platform Teams
- Developer Experience
- Data Platform
- ML Platform
- Security & Compliance

## Key Lessons

1. **Graph is Core**: The social graph underpins everything
2. **REST.li Works**: Consistent API frameworks improve productivity
3. **Kafka is Essential**: Event streaming enables real-time systems
4. **Data Platform Matters**: Venice and Data Hub enable analytics
5. **ML Drives Engagement**: Personalization increases retention
6. **Professional Context**: Domain-specific solutions beat generic ones

## Statistics

- **Members**: 900M+
- **Countries**: 200+
- **Services**: 500+ microservices
- **Kafka Messages**: 7T+/day
- **Engineers**: 3,000+
- **Revenue**: $15B+ annually

## References

- [LinkedIn Engineering Blog](https://engineering.linkedin.com/)
- [REST.li Framework](https://linkedin.github.io/rest.li/)
- [Kafka at LinkedIn](https://engineering.linkedin.com/kafka)
- [LinkedIn Graph](https://engineering.linkedin.com/blog/2019/04/the-linkedin-graph)
- [Venice Platform](https://engineering.linkedin.com/blog/2022/08/venice--linkedin-s-derived-data-platform)
