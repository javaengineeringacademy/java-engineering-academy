# System Design Examples

Complete system design solutions for common interview problems.

## Overview

This document provides detailed solutions for frequently asked system design questions, following the standard interview framework.

## 1. URL Shortener

### Requirements
- Shorten URLs (e.g., bit.ly/abc123)
- Redirect to original URL
- Custom aliases (optional)
- Analytics (clicks, geography)

### High-Level Design

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│   API   │────▶│Database │
└─────────┘     │ Gateway │     └─────────┘
                └─────────┘
                     │
                ┌────▼────┐
                │  Cache  │
                │ (Redis) │
                └─────────┘
```

### API Design
```
POST /api/shorten
  Request: { "url": "https://example.com/long/path", "custom_alias": "myalias" }
  Response: { "short_url": "https://short.ly/abc123" }

GET /{short_code}
  Response: 301 Redirect to original URL
```

### Database Schema
```sql
CREATE TABLE urls (
    id BIGINT PRIMARY KEY,
    short_code VARCHAR(10) UNIQUE,
    original_url TEXT,
    user_id BIGINT,
    created_at TIMESTAMP,
    expires_at TIMESTAMP
);

CREATE INDEX idx_short_code ON urls(short_code);
```

### Key Algorithms

**Base62 Encoding:**
```python
def encode_base62(num):
    chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    result = []
    while num > 0:
        result.append(chars[num % 62])
        num //= 62
    return ''.join(reversed(result))

def generate_short_code(url):
    hash_val = hashlib.md5(url.encode()).hexdigest()
    num = int(hash_val[:8], 16)
    return encode_base62(num)[:7]
```

**Collision Handling:**
```python
def create_short_url(url):
    short_code = generate_short_code(url)
    while db.exists(short_code):
        short_code = generate_short_code(url + str(time.time()))
    db.save(short_code, url)
    return short_code
```

### Caching Strategy
- Cache hot URLs in Redis
- TTL: 24 hours
- Cache invalidation on update

### Scaling
- **Read-heavy**: Replicate database
- **Write-heavy**: Shard by short code
- **Global**: CDN for redirects

---

## 2. Chat Application

### Requirements
- 1-on-1 and group messaging
- Real-time delivery
- Message history
- Online/offline status
- Read receipts

### High-Level Design

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │◀───▶│WebSocket│◀───▶│  Chat   │
│         │     │ Server  │     │ Service │
└─────────┘     └─────────┘     └────┬────┘
                                     │
                              ┌──────▼──────┐
                              │   Message   │
                              │   Queue     │
                              └──────┬──────┘
                                     │
                              ┌──────▼──────┐
                              │  Database   │
                              └─────────────┘
```

### API Design
```
WebSocket: ws://chat.example.com/ws

Messages:
  - Send: { "type": "message", "chat_id": "123", "content": "Hello" }
  - Receive: { "type": "message", "from": "user1", "content": "Hello" }

REST API:
  GET /api/chats - List user's chats
  POST /api/chats - Create new chat
  GET /api/chats/{id}/messages - Get message history
```

### Database Schema
```sql
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(50),
    status VARCHAR(20),
    last_seen TIMESTAMP
);

CREATE TABLE chats (
    chat_id BIGINT PRIMARY KEY,
    type VARCHAR(20), -- 'direct' or 'group'
    name VARCHAR(100),
    created_at TIMESTAMP
);

CREATE TABLE chat_members (
    chat_id BIGINT,
    user_id BIGINT,
    joined_at TIMESTAMP,
    PRIMARY KEY (chat_id, user_id)
);

CREATE TABLE messages (
    message_id BIGINT PRIMARY KEY,
    chat_id BIGINT,
    sender_id BIGINT,
    content TEXT,
    type VARCHAR(20), -- 'text', 'image', 'file'
    created_at TIMESTAMP,
    delivered_at TIMESTAMP,
    read_at TIMESTAMP
);
```

### Message Delivery
1. **Send**: Client → WebSocket Server → Message Queue → Recipient
2. **Offline**: Store in database, deliver when online
3. **Group**: Fan-out to all members

### Presence System
- Heartbeat mechanism
- Redis for online status
- Pub/sub for status updates

### Scaling
- **WebSocket Servers**: Horizontal scaling with sticky sessions
- **Messages**: Shard by chat_id
- **Media**: CDN for file storage

---

## 3. News Feed (Twitter/Facebook)

### Requirements
- Post tweets/posts
- Follow users
- View timeline
- Like/Retweet/Comment
- Real-time updates

### High-Level Design

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│   API   │────▶│  Feed   │
└─────────┘     │ Gateway │     │ Service │
                └─────────┘     └────┬────┘
                                     │
                    ┌────────────────┼────────────────┐
                    │                │                │
               ┌────▼────┐     ┌────▼────┐     ┌────▼────┐
               │  Post   │     │ Follow  │     │ Timeline│
               │ Service │     │ Service │     │ Service │
               └─────────┘     └─────────┘     └─────────┘
```

### API Design
```
POST /api/posts
  Request: { "content": "Hello world", "media": [...] }
  Response: { "post_id": "123" }

GET /api/timeline
  Response: { "posts": [...], "cursor": "abc" }

POST /api/follow/{user_id}
  Response: { "status": "following" }

POST /api/posts/{post_id}/like
  Response: { "likes_count": 42 }
```

### Database Schema
```sql
CREATE TABLE posts (
    post_id BIGINT PRIMARY KEY,
    user_id BIGINT,
    content TEXT,
    media_urls JSONB,
    created_at TIMESTAMP
);

CREATE TABLE follows (
    follower_id BIGINT,
    following_id BIGINT,
    created_at TIMESTAMP,
    PRIMARY KEY (follower_id, following_id)
);

CREATE TABLE likes (
    user_id BIGINT,
    post_id BIGINT,
    created_at TIMESTAMP,
    PRIMARY KEY (user_id, post_id)
);
```

### Feed Generation

**Fan-out on Write:**
```python
def create_post(user_id, content):
    post = db.create_post(user_id, content)
    
    # Get all followers
    followers = db.get_followers(user_id)
    
    # Add to each follower's timeline
    for follower_id in followers:
        redis.lpush(f"timeline:{follower_id}", post.post_id)
        redis.ltrim(f"timeline:{follower_id}", 0, 999)
    
    return post
```

**Fan-out on Read:**
```python
def get_timeline(user_id, cursor):
    # Get user's posts
    user_posts = db.get_posts(user_id)
    
    # Get posts from followed users
    following = db.get_following(user_id)
    following_posts = db.get_posts_from_users(following)
    
    # Merge and sort by time
    all_posts = user_posts + following_posts
    all_posts.sort(key=lambda p: p.created_at, reverse=True)
    
    return all_posts[:20]
```

### Ranking Algorithm
```python
def rank_post(post, user):
    affinity = calculate_affinity(post.author, user)
    recency = calculate_recency(post.created_at)
    engagement = calculate_engagement(post)
    
    return affinity * 0.4 + recency * 0.3 + engagement * 0.3
```

### Scaling
- **Feed Cache**: Redis sorted sets
- **Post Storage**: Shard by post_id
- **Media**: CDN
- **Real-time**: WebSocket for live updates

---

## 4. Rate Limiter

### Requirements
- Limit requests per client
- Multiple rate limiting algorithms
- Distributed system support
- Low latency

### Algorithms

**Token Bucket:**
```python
class TokenBucket:
    def __init__(self, capacity, refill_rate):
        self.capacity = capacity
        self.tokens = capacity
        self.refill_rate = refill_rate
        self.last_refill = time.time()
    
    def allow(self):
        self.refill()
        if self.tokens >= 1:
            self.tokens -= 1
            return True
        return False
    
    def refill(self):
        now = time.time()
        elapsed = now - self.last_refill
        self.tokens = min(
            self.capacity,
            self.tokens + elapsed * self.refill_rate
        )
        self.last_refill = now
```

**Sliding Window:**
```python
class SlidingWindow:
    def __init__(self, limit, window_size):
        self.limit = limit
        self.window_size = window_size
        self.requests = []
    
    def allow(self):
        now = time.time()
        # Remove old requests
        self.requests = [r for r in self.requests if now - r < self.window_size]
        
        if len(self.requests) < self.limit:
            self.requests.append(now)
            return True
        return False
```

### Distributed Rate Limiting
```python
class DistributedRateLimiter:
    def __init__(self, redis_client, limit, window):
        self.redis = redis_client
        self.limit = limit
        self.window = window
    
    def allow(self, key):
        pipe = self.redis.pipeline()
        now = time.time()
        window_start = now - self.window
        
        # Remove old entries
        pipe.zremrangebyscore(key, 0, window_start)
        # Count current entries
        pipe.zcard(key)
        # Add new entry
        pipe.zadd(key, {str(now): now})
        # Set expiry
        pipe.expire(key, self.window)
        
        results = pipe.execute()
        request_count = results[1]
        
        return request_count < self.limit
```

### Response Headers
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1623456789
```

### Scaling
- **Redis Cluster**: For distributed rate limiting
- **Local Cache**: For fast lookups
- **Consistent Hashing**: For key distribution

---

## 5. Key-Value Store

### Requirements
- PUT/GET/DELETE operations
- High availability
- Partition tolerance
- Consistent hashing

### High-Level Design

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Client  │────▶│Coordinator│───▶│  Node 1 │
└─────────┘     └─────────┘     └─────────┘
                                   │
                              ┌────▼────┐
                              │  Node 2 │
                              └─────────┘
                                   │
                              ┌────▼────┐
                              │  Node 3 │
                              └─────────┘
```

### Consistent Hashing
```python
class ConsistentHash:
    def __init__(self, nodes, virtual_nodes=150):
        self.ring = {}
        self.sorted_keys = []
        
        for node in nodes:
            for i in range(virtual_nodes):
                key = self.hash(f"{node}:{i}")
                self.ring[key] = node
                self.sorted_keys.append(key)
        
        self.sorted_keys.sort()
    
    def get_node(self, key):
        hash_val = self.hash(key)
        for sorted_key in self.sorted_keys:
            if hash_val <= sorted_key:
                return self.ring[sorted_key]
        return self.ring[self.sorted_keys[0]]
    
    def hash(self, key):
        return int(hashlib.md5(key.encode()).hexdigest(), 16)
```

### Data Replication
```python
class ReplicationManager:
    def __init__(self, replication_factor=3):
        self.replication_factor = replication_factor
    
    def get_replica_nodes(self, key):
        primary = consistent_hash.get_node(key)
        replicas = [primary]
        
        for node in nodes:
            if node != primary and len(replicas) < self.replication_factor:
                replicas.append(node)
        
        return replicas
```

### Conflict Resolution
```python
class VectorClock:
    def __init__(self):
        self.clock = {}
    
    def increment(self, node_id):
        self.clock[node_id] = self.clock.get(node_id, 0) + 1
    
    def merge(self, other):
        for node_id, counter in other.clock.items():
            self.clock[node_id] = max(
                self.clock.get(node_id, 0),
                counter
            )
    
    def is_newer(self, other):
        for node_id, counter in self.clock.items():
            if counter <= other.clock.get(node_id, 0):
                return False
        return True
```

### Scaling
- **Partitioning**: Consistent hashing
- **Replication**: Multiple replicas per partition
- **Read/write**: Quorum-based consistency
