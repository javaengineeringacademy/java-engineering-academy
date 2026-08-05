# Airbnb: Travel Platform at Scale

How Airbnb built a platform connecting millions of hosts and guests across 220+ countries.

## Company Overview

Airbnb is an online marketplace for lodging, experiences, and tourism activities. Their engineering challenge: building trust, enabling global operations, and creating magical user experiences.

## Architecture Evolution

### Phase 1: Rails Monolith (2008-2017)
- Single Ruby on Rails application
- MySQL database
- Manual scaling
- Tight coupling

### Phase 2: Service-Oriented Architecture (2017-2020)
- 1,000+ services
- Service mesh
- Event-driven architecture
- Data platform evolution

### Phase 3: Modern Platform (2020-Present)
- Domain-driven design
- GraphQL federation
- Real-time data infrastructure
- AI/ML integration

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│            (Web, iOS, Android, API Partners)                │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    API Gateway                              │
│          (GraphQL, Rate Limiting, Authentication)           │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Domain Services                          │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Search     │  Booking     │  Payments    │  Messaging     │
│   Domain     │  Domain      │  Domain      │  Domain        │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Listing     │  Reservation │  Billing     │  In-App Chat   │
│  Ranking     │  Calendar    │  Fraud       │  Notifications │
│  Filtering   │  Pricing     │  Commissions │  Trust         │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Data Platform                              │
│    (Data Lake, Data Warehouse, ML Pipeline, Analytics)      │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### GraphQL Federation
- Unified API layer
- Schema stitching
- Type-safe contracts
- Performance optimization

```graphql
# Listing Schema
type Listing {
    id: ID!
    title: String!
    description: String!
    price: Price!
    location: Location!
    host: Host!
    amenities: [Amenity!]!
    reviews: [Review!]!
    availability: Availability!
}

type Price {
    amount: Float!
    currency: String!
    nightly: Float!
    cleaningFee: Float!
    serviceFee: Float!
}

# Search Query
type Query {
    searchListings(
        location: LocationInput!
        checkIn: Date!
        checkOut: Date!
        guests: Int!
        filters: SearchFilters
    ): SearchResults!
}
```

### Search Infrastructure
- Elasticsearch cluster
- Real-time indexing
- ML-powered ranking
- Personalized results

```python
# Search Ranking Algorithm
class ListingRanker:
    def rank(self, listings, user_context):
        scores = []
        for listing in listings:
            score = self.calculate_score(listing, user_context)
            scores.append((listing, score))
        return sorted(scores, key=lambda x: x[1], reverse=True)
    
    def calculate_score(self, listing, user_context):
        relevance = self.keyword_match(listing, user_context.query)
        popularity = self.booking_rate(listing)
        quality = self.review_score(listing)
        personalization = self.user_preference(listing, user_context.user)
        
        return (
            relevance * 0.3 +
            popularity * 0.25 +
            quality * 0.25 +
            personalization * 0.2
        )
```

### Calendar System
- Real-time availability
- Conflict resolution
- Timezone handling
- iCal integration

### Pricing Engine
- Dynamic pricing
- Seasonal adjustments
- Event-based pricing
- Competitor analysis

## Data Architecture

### Data Platform
- **Data Lake**: Raw event storage (S3)
- **Data Warehouse**: Analytical queries (Presto/Trino)
- **Real-time**: Stream processing (Kafka, Flink)
- **ML Platform**: Feature store, model serving

### Event Streaming
- 10B+ events/day
- Kafka-based pipelines
- Exactly-once semantics
- Schema evolution

### Data Models
```sql
-- Listing with Pricing History
CREATE TABLE listings (
    id UUID PRIMARY KEY,
    host_id UUID NOT NULL,
    title VARCHAR(200),
    description TEXT,
    price_base DECIMAL(10,2),
    currency VARCHAR(3),
    location_lat DECIMAL(10,8),
    location_lng DECIMAL(11,8),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Booking with Audit Trail
CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    listing_id UUID REFERENCES listings(id),
    guest_id UUID,
    check_in DATE,
    check_out DATE,
    status VARCHAR(20),
    total_price DECIMAL(10,2),
    created_at TIMESTAMP
);
```

## Search and Discovery

### Search Pipeline
1. **Query Understanding**: NLP, intent classification
2. **Candidate Retrieval**: Inverted index, geo-spatial search
3. **Ranking**: ML models, personalization
4. **Filtering**: Availability, price, amenities
5. **Presentation**: Clustering, deduplication

### ML Models
- **Listing Quality**: Predicts guest satisfaction
- **Conversion Rate**: Likelihood of booking
- **Price Optimization**: Suggests optimal pricing
- **Fraud Detection**: Identifies suspicious activity

### A/B Testing Framework
- 1,000+ experiments per year
- Statistical rigor
- Feature flags
- Gradual rollouts

## Trust and Safety

### Verification System
- ID verification
- Phone verification
- Email verification
- Social connections

### Review System
- Double-blind reviews
- Sentiment analysis
- Fraud detection
- Quality scoring

### Fraud Prevention
- Payment fraud detection
- Account takeover prevention
- Listing verification
- Dispute resolution

## Observability

### Metrics Platform
- Custom metrics collection
- Real-time dashboards
- Anomaly detection
- Business metrics

### Tracing
- Distributed tracing
- Latency analysis
- Dependency mapping

### Logging
- Structured logging
- Centralized aggregation
- Real-time analysis

## Developer Productivity

### Development Environment
- Local development setup
- Service templates
- Shared libraries
- Documentation

### CI/CD Pipeline
- Automated testing
- Security scanning
- Performance testing
- Gradual deployment

### Internal Tools
- Service catalog
- Deployment dashboard
- Monitoring tools
- Incident management

## Organizational Structure

### Domain Teams
- Search & Discovery
- Bookings & Payments
- Host Tools
- Guest Experience
- Trust & Safety
- Platform Infrastructure

### Platform Teams
- Developer Experience
- Data Platform
- ML Platform
- Security & Compliance

## Key Lessons

1. **Trust is Everything**: Build verification and review systems early
2. **Search is Complex**: ML-powered ranking and personalization are essential
3. **Global is Hard**: Timezones, currencies, and regulations add complexity
4. **Data Drives Decisions**: A/B testing and analytics guide product development
5. **Community Matters**: Host and guest experience are equally important
6. **Platform Enables Scale**: Internal tools multiply engineering productivity

## Statistics

- **Listings**: 7M+ active listings
- **Countries**: 220+
- **Users**: 1.5B+ guest arrivals
- **Services**: 1,000+ microservices
- **Engineers**: 2,000+
- **Revenue**: $8.4B+ annually

## References

- [Airbnb Engineering Blog](https://medium.com/airbnb-engineering)
- [Airbnb GraphQL Federation](https://medium.com/airbnb-engineering/graphql-at-airbnb-826e5e2c61b1)
- [Airbnb Data Platform](https://medium.com/airbnb-engineering/airbnb-data-platform-2c0e8e0e1f7d)
- [Airbnb Search Ranking](https://medium.com/airbnb-engineering/search-ranking-at-airbnb-33c3d4c3a91)
- [Airbnb Trust and Safety](https://medium.com/airbnb-engineering/keeping-airbnb-safe-6df3e6a23f0)
