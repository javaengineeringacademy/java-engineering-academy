# Architecture Interview Examples

Practice architecture interviews with real-world examples.

## Overview

This guide provides detailed solutions to common architecture interview questions, showing the thought process and trade-offs involved.

## Example 1: Design a URL Shortener

### Requirements
**Functional:**
- Shorten URLs
- Redirect to original URL
- Custom aliases
- Expiration

**Non-Functional:**
- 100M URLs/day
- 10:1 read:write ratio
- Low latency (<100ms)
- High availability

### High-Level Design
```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│   API   │────▶│Database │
└─────────┘     │ Gateway │     └─────────┘
                └────┬────┘
                     │
                ┌────▼────┐
                │  Cache  │
                │ (Redis) │
                └─────────┘
```

### API Design
```
POST /api/shorten
{
  "url": "https://example.com/very/long/url",
  "custom_alias": "myalias",
  "expiration": "2024-12-31"
}

Response:
{
  "short_url": "https://short.ly/myalias",
  "original_url": "https://example.com/very/long/url"
}

GET /{short_url}
Redirects to original URL
```

### Database Schema
```sql
CREATE TABLE urls (
  id BIGINT PRIMARY KEY,
  short_code VARCHAR(10) UNIQUE,
  original_url TEXT,
  user_id BIGINT,
  created_at TIMESTAMP,
  expires_at TIMESTAMP,
  click_count BIGINT DEFAULT 0
);

CREATE INDEX idx_short_code ON urls(short_code);
CREATE INDEX idx_user_id ON urls(user_id);
```

### Key Components
1. **Hash Generation**: Base62 encoding of unique ID
2. **Database**: NoSQL for high write throughput
3. **Caching**: Redis for frequently accessed URLs
4. **Analytics**: Click tracking and statistics

### Scaling Strategy
- **Database Sharding**: Hash-based sharding by short code
- **Caching**: Redis cluster for hot URLs
- **CDN**: Static assets and redirects
- **Rate Limiting**: Prevent abuse

### Trade-offs
- **Consistency vs. Availability**: Eventual consistency acceptable
- **Storage vs. Speed**: Cache frequently accessed URLs
- **Security vs. Convenience**: Rate limiting vs. open access

## Example 2: Design a Chat System

### Requirements
**Functional:**
- One-on-one messaging
- Group messaging
- Online status
- Message history
- Read receipts

**Non-Functional:**
- 50M daily active users
- 10B messages/day
- Real-time delivery
- Message persistence

### High-Level Design
```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │◀───▶│ WebSocket│◀───▶│  Chat   │
│ (Mobile)│     │  Server  │     │ Service │
└─────────┘     └─────────┘     └────┬────┘
                                     │
┌─────────┐     ┌─────────┐     ┌────▼────┐
│ Client  │◀───▶│   API   │◀───▶│Database │
│  (Web)  │     │ Gateway │     └─────────┘
└─────────┘     └─────────┘
```

### API Design
```
WebSocket: ws://chat.example.com/ws

Messages:
- Send: { type: "message", to: "user123", content: "Hello" }
- Receive: { type: "message", from: "user456", content: "Hi" }
- Typing: { type: "typing", to: "user123" }
- Online: { type: "online", user: "user123" }

REST API:
POST /api/conversations
GET /api/conversations/{id}/messages
PUT /api/messages/{id}/read
```

### Database Schema
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(50),
  status ENUM('online', 'offline', 'away'),
  last_seen TIMESTAMP
);

CREATE TABLE conversations (
  id BIGINT PRIMARY KEY,
  type ENUM('direct', 'group'),
  name VARCHAR(100),
  created_at TIMESTAMP
);

CREATE TABLE messages (
  id BIGINT PRIMARY KEY,
  conversation_id BIGINT,
  sender_id BIGINT,
  content TEXT,
  created_at TIMESTAMP,
  read_at TIMESTAMP
);

CREATE TABLE conversation_participants (
  conversation_id BIGINT,
  user_id BIGINT,
  joined_at TIMESTAMP,
  PRIMARY KEY (conversation_id, user_id)
);
```

### Key Components
1. **WebSocket Server**: Real-time communication
2. **Message Queue**: Kafka for message persistence
3. **Presence Service**: Online status tracking
4. **Push Notifications**: Mobile and web notifications

### Scaling Strategy
- **WebSocket Clusters**: Horizontal scaling of connections
- **Message Sharding**: By conversation ID
- **Read Replicas**: For message history queries
- **CDN**: Static assets and media

### Trade-offs
- **Real-time vs. Battery**: WebSocket vs. polling
- **Storage vs. Cost**: Message retention policies
- **Privacy vs. Features**: End-to-end encryption complexity

## Example 3: Design a News Feed

### Requirements
**Functional:**
- Post updates (text, images, videos)
- Follow users
- View personalized feed
- Like and comment
- Real-time updates

**Non-Functional:**
- 300M daily active users
- 500M posts/day
- 99.99% availability
- <200ms latency

### High-Level Design
```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│   API   │────▶│ Feed    │
└─────────┘     │ Gateway │     │ Service │
                └─────────┘     └────┬────┘
                                     │
┌─────────┐     ┌─────────┐     ┌────▼────┐
│  CDN    │◀────│  Cache  │◀────│Database │
└─────────┘     │ (Redis) │     └─────────┘
                └─────────┘
```

### API Design
```
POST /api/posts
{
  "content": "Hello world!",
  "media": ["image1.jpg"],
  "visibility": "public"
}

GET /api/feed?cursor={cursor}&limit=20

POST /api/follow/{userId}
DELETE /api/follow/{userId}

POST /api/posts/{postId}/like
POST /api/posts/{postId}/comment
```

### Database Schema
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(50),
  follower_count BIGINT DEFAULT 0
);

CREATE TABLE posts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  content TEXT,
  media_urls JSON,
  created_at TIMESTAMP,
  like_count BIGINT DEFAULT 0,
  comment_count BIGINT DEFAULT 0
);

CREATE TABLE follows (
  follower_id BIGINT,
  following_id BIGINT,
  created_at TIMESTAMP,
  PRIMARY KEY (follower_id, following_id)
);

CREATE TABLE feed_cache (
  user_id BIGINT,
  post_id BIGINT,
  created_at TIMESTAMP,
  PRIMARY KEY (user_id, post_id)
);
```

### Key Components
1. **Feed Generation**: Fan-out on write vs. read
2. **Ranking Algorithm**: ML-based content ranking
3. **Cache Layer**: Redis for feed caching
4. **Media Service**: Image and video processing

### Fan-out Strategy
**Fan-out on Write:**
- Write feed to followers' caches
- Fast read performance
- Higher write amplification

**Fan-out on Read:**
- Generate feed on request
- Lower write amplification
- Higher read latency

**Hybrid Approach:**
- Fan-out on write for users with <10K followers
- Fan-out on read for users with >10K followers

### Scaling Strategy
- **Feed Sharding**: By user ID
- **Cache Layer**: Redis cluster for hot feeds
- **CDN**: Media and static content
- **Async Processing**: Kafka for feed generation

### Trade-offs
- **Consistency vs. Speed**: Eventual consistency for feed
- **Freshness vs. Cost**: Real-time vs. batch processing
- **Personalization vs. Privacy**: Data usage for ranking

## Example 4: Design a Payment System

### Requirements
**Functional:**
- Process payments
- Refunds
- Payment history
- Multiple payment methods
- Currency conversion

**Non-Functional:**
- 10M transactions/day
- 99.99% availability
- Exactly-once processing
- PCI compliance

### High-Level Design
```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│   API   │────▶│Payment  │
└─────────┘     │ Gateway │     │ Service │
                └─────────┘     └────┬────┘
                                     │
┌─────────┐     ┌─────────┐     ┌────▼────┐
│  Bank   │◀────│ Message │◀────│Ledger   │
│ Gateway │     │  Queue  │     │ Service │
└─────────┘     └─────────┘     └─────────┘
```

### API Design
```
POST /api/payments
{
  "amount": 100.00,
  "currency": "USD",
  "payment_method": "card_123",
  "idempotency_key": "unique_key"
}

Response:
{
  "payment_id": "pay_123",
  "status": "completed",
  "amount": 100.00,
  "currency": "USD"
}

POST /api/payments/{id}/refund
GET /api/payments/{id}
```

### Database Schema
```sql
CREATE TABLE payments (
  id BIGINT PRIMARY KEY,
  idempotency_key VARCHAR(255) UNIQUE,
  amount DECIMAL(10,2),
  currency VARCHAR(3),
  status ENUM('pending', 'completed', 'failed', 'refunded'),
  payment_method_id BIGINT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE transactions (
  id BIGINT PRIMARY KEY,
  payment_id BIGINT,
  type ENUM('charge', 'refund', 'transfer'),
  amount DECIMAL(10,2),
  currency VARCHAR(3),
  status ENUM('pending', 'completed', 'failed'),
  created_at TIMESTAMP
);

CREATE TABLE ledger (
  id BIGINT PRIMARY KEY,
  account_id BIGINT,
  transaction_id BIGINT,
  amount DECIMAL(10,2),
  balance DECIMAL(10,2),
  created_at TIMESTAMP
);
```

### Key Components
1. **Payment Service**: Core payment processing
2. **Ledger Service**: Double-entry accounting
3. **Risk Service**: Fraud detection
4. **Notification Service**: Payment confirmations

### Exactly-Once Processing
- **Idempotency Keys**: Prevent duplicate transactions
- **Deduplication**: Database constraints
- **Saga Pattern**: Distributed transaction management

### Scaling Strategy
- **Database Sharding**: By payment ID
- **Read Replicas**: For payment history
- **Message Queue**: Kafka for async processing
- **Cache**: Redis for frequent lookups

### Trade-offs
- **Consistency vs. Availability**: Strong consistency required
- **Security vs. Performance**: Encryption overhead
- **Compliance vs. Flexibility**: PCI requirements

## Example 5: Design a Search Engine

### Requirements
**Functional:**
- Full-text search
- Auto-complete
- Spell correction
- Search suggestions
- Results ranking

**Non-Functional:**
- 5B searches/day
- <200ms latency
- 99.99% availability
- Personalized results

### High-Level Design
```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│   API   │────▶│ Search  │
└─────────┘     │ Gateway │     │ Service │
                └─────────┘     └────┬────┘
                                     │
┌─────────┐     ┌─────────┐     ┌────▼────┐
│   CDN   │◀────│  Cache  │◀────│Index    │
└─────────┘     │ (Redis) │     │ Service │
                └─────────┘     └─────────┘
```

### API Design
```
GET /api/search?q={query}&page={page}&limit={20}

Response:
{
  "results": [...],
  "total": 1000,
  "page": 1,
  "suggestions": ["related query 1", "related query 2"]
}

GET /api/autocomplete?q={prefix}

Response:
{
  "suggestions": ["complete 1", "complete 2", "complete 3"]
}
```

### Database Schema
```sql
CREATE TABLE documents (
  id BIGINT PRIMARY KEY,
  title TEXT,
  content TEXT,
  url TEXT,
  rank FLOAT,
  updated_at TIMESTAMP
);

CREATE TABLE search_index (
  term VARCHAR(100),
  document_id BIGINT,
  frequency FLOAT,
  position JSON,
  PRIMARY KEY (term, document_id)
);

CREATE TABLE search_history (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  query TEXT,
  results_count INT,
  clicked_result_id BIGINT,
  created_at TIMESTAMP
);
```

### Key Components
1. **Indexer**: Build and update search index
2. **Ranker**: ML-based result ranking
3. **Auto-complete**: Trie-based suggestions
4. **Spell Checker**: Edit distance algorithms

### Ranking Algorithm
- **TF-IDF**: Term frequency-inverse document frequency
- **PageRank**: Link analysis
- **ML Ranking**: Neural network-based
- **Personalization**: User history and preferences

### Scaling Strategy
- **Index Sharding**: By document ID
- **Read Replicas**: For search queries
- **Cache Layer**: Redis for popular searches
- **CDN**: Static assets and suggestions

### Trade-offs
- **Freshness vs. Cost**: Real-time indexing expensive
- **Relevance vs. Speed**: Complex ranking slower
- **Privacy vs. Personalization**: Data usage for ranking

## Interview Tips

1. **Start with Requirements**: Clarify functional and non-functional
2. **High-Level First**: Draw major components before details
3. **Discuss Trade-offs**: Show awareness of alternatives
4. **Consider Scale**: Address how system grows
5. **Address Failures**: Discuss reliability and recovery
6. **Think About Monitoring**: Observability is critical

## Practice Problems

### Easy
- Design a rate limiter
- Design a key-value store
- Design a unique ID generator

### Medium
- Design a notification system
- Design a parking lot
- Design an elevator system

### Hard
- Design a distributed cache
- Design a real-time analytics system
- Design a global file storage system