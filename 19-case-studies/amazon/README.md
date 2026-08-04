# Amazon: E-Commerce and Cloud at Scale

How Amazon built a platform handling millions of transactions daily and created AWS.

## Company Overview

Amazon is the world's largest e-commerce platform and cloud computing provider. Their engineering philosophy: "Two-Pizza Teams" and "Day 1" mentality.

## Architecture Evolution

### Phase 1: Monolith (1994-2001)
- Single application
- Oracle database
- Manual scaling

### Phase 2: SOA (2001-2010)
- Service-oriented architecture
- Two-Pizza Teams
- Internal APIs
- AWS begins

### Phase 3: Microservices (2010-Present)
- 100,000+ microservices
- AWS-native services
- Serverless architecture
- Machine learning everywhere

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│      (Web, Mobile, Alexa, Kindle, Fire TV, Partners)        │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    API Gateway                              │
│          (REST, GraphQL, Rate Limiting, Caching)            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Domain Services                            │
├──────────────┬──────────────┬──────────────┬────────────────┤
│  Catalog     │  Ordering    │  Payments    │  Logistics     │
│  Service     │  Service     │  Service     │  Service       │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Products    │  Cart        │  Billing     │  Fulfillment   │
│  Search      │  Checkout    │  Fraud       │  Shipping      │
│  Recommendations│  Orders  │  Commissions │  Tracking      │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  AWS Infrastructure                         │
│    (EC2, S3, DynamoDB, Lambda, SQS, SNS, etc.)             │
└─────────────────────────────────────────────────────────────┘
```

## Two-Pizza Teams

### Structure
- **Team Size**: 6-10 people (can be fed by two pizzas)
- **Ownership**: End-to-end ownership of service
- **Autonomy**: Choose their own tools and processes
- **Accountability**: Responsible for their service's success

### Benefits
- Faster decision-making
- Clear ownership
- Reduced coordination overhead
- Increased innovation

### Implementation
```
Team: Cart Service
├── 2 Backend Engineers
├── 2 Frontend Engineers
├── 1 Data Engineer
├── 1 QA Engineer
├── 1 Product Manager
└── 1 Engineering Manager

Responsibilities:
- Cart API
- Cart UI
- Cart Data
- Cart Analytics
- On-call support
```

### Service Ownership
- Build, test, deploy, operate
- On-call rotation
- Capacity planning
- Cost optimization

## Key Technologies

### ORCA (Operational Resource Control Architecture)
- Service discovery
- Load balancing
- Health checking
- Circuit breaking

```java
// ORCA Client Example
@ORCAService(name = "order-service")
public class OrderServiceClient {
    
    @ORCAMethod(retry = 3, timeout = 1000)
    public Order getOrder(String orderId) {
        return orcaClient.call("order-service", "getOrder", orderId);
    }
    
    @ORCAMethod(retry = 2, timeout = 5000)
    public Order createOrder(CreateOrderRequest request) {
        return orcaClient.call("order-service", "createOrder", request);
    }
}
```

### DynamoDB
- NoSQL database
- Single-digit millisecond latency
- Auto-scaling
- Global tables

### SQS/SNS
- Message queuing
- Pub/sub messaging
- Decoupled services
- Reliable delivery

### Lambda
- Serverless computing
- Event-driven
- Auto-scaling
- Pay-per-use

## Data Architecture

### Data Stores
- **DynamoDB**: Primary data store
- **Aurora**: Relational data
- **S3**: Object storage
- **ElastiCache**: Caching
- **Redshift**: Analytics

### Data Pipeline
- Kinesis for real-time streaming
- Glue for ETL
- Athena for ad-hoc queries
- QuickSight for visualization

### Data Models
```sql
-- Product Catalog
CREATE TABLE products (
    product_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(500),
    description TEXT,
    price DECIMAL(10,2),
    category_id VARCHAR(50),
    inventory_count INT,
    created_at TIMESTAMP
);

-- Order Management
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50),
    status VARCHAR(20),
    total_amount DECIMAL(10,2),
    shipping_address JSONB,
    created_at TIMESTAMP
);

-- Order Items
CREATE TABLE order_items (
    order_id VARCHAR(50),
    product_id VARCHAR(50),
    quantity INT,
    price DECIMAL(10,2),
    PRIMARY KEY (order_id, product_id)
);
```

## Recommendation Engine

### Collaborative Filtering
- User-item interactions
- Similar user patterns
- Item-item similarity
- Matrix factorization

### Content-Based Filtering
- Product attributes
- User preferences
- Category matching
- Feature extraction

### Deep Learning
- Neural collaborative filtering
- Sequence models
- Attention mechanisms
- Multi-task learning

### Real-time Personalization
- Session-based recommendations
- Context-aware suggestions
- A/B testing framework
- Online learning

## Search and Discovery

### Product Search
- Inverted index
- Fuzzy matching
- Faceted search
- Geographic search

### Ranking Algorithm
- Relevance scoring
- Personalization
- Business rules
- ML models

### A/B Testing
- Statistical significance
- Gradual rollouts
- Feature flags
- Metrics tracking

## Observability

### CloudWatch
- Metrics collection
- Log aggregation
- Alarming
- Dashboards

### X-Ray
- Distributed tracing
- Service maps
- Latency analysis

### GuardDuty
- Threat detection
- Anomaly detection
- Security monitoring

## Developer Productivity

### Internal Developer Platform
- Self-service deployments
- Standardized tooling
- Shared libraries
- Documentation

### CI/CD Pipeline
- CodePipeline
- CodeBuild
- CodeDeploy
- Automated testing

### Development Environment
- Cloud9 IDE
- Local development
- Service templates
- Documentation

## Organizational Structure

### Two-Pizza Teams
- Autonomous service teams
- Clear ownership
- Independent deployment
- On-call responsibility

### Bar Raiser Program
- Interview excellence
- Hire better than average
- Culture add, not culture fit
- Long-term thinking

### Leadership Principles
- Customer Obsession
- Ownership
- Invent and Simplify
- Are Right, A Lot
- Learn and Be Curious
- Hire and Develop the Best
- Insist on the Highest Standards
- Think Big
- Bias for Action
- Frugality
- Earn Trust
- Dive Deep
- Have Backbone; Disagree and Commit
- Deliver Results

## Key Lessons

1. **Two-Pizza Teams Work**: Small, autonomous teams move fast
2. **APIs First**: Internal APIs enable reusability
3. **Everything as a Service**: AWS itself is the product
4. **Customer Obsession**: Start with the customer and work backwards
5. **Day 1 Mentality**: Avoid complacency, stay innovative
6. **Scale Through Automation**: Automate everything possible

## Statistics

- **Products**: 350M+
- **Services**: 100,000+ microservices
- **Employees**: 1.6M+
- **AWS Revenue**: $80B+ annually
- **Transactions**: 1M+/hour
- **Uptime**: 99.99%

## References

- [Amazon Engineering Blog](https://www.amazon.science/)
- [AWS Architecture Center](https://aws.amazon.com/architecture/)
- [Two-Pizza Teams](https://www.technologyreview.com/2015/01/08/167263/how-amazon-uses-data-to-make-you-buy-more/)
- [DynamoDB Paper](https://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf)
- [ORCA Framework](https://www.amazon.science/publications/orca-a-customizable-availability-and-latency-management-system)
