# Facebook/Meta: Social Network at Global Scale

How Meta built a platform connecting 3B+ users across multiple products.

## Company Overview

Meta (formerly Facebook) operates the world's largest social network. Their engineering challenge: connecting billions of users with real-time social interactions.

## Architecture Evolution

### Phase 1: LAMP Stack (2004-2009)
- PHP monolith
- MySQL database
- Manual scaling

### Phase 2: Custom Infrastructure (2009-2015)
- HHVM runtime
- TAO graph store
- HipHop compiler
- Custom hardware

### Phase 3: Modern Platform (2015-Present)
- Hack language
- GraphQL API
- Data warehouse evolution
- React ecosystem

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│    (Web, Mobile, WhatsApp, Instagram, Messenger, VR)        │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    API Gateway                              │
│          (GraphQL, Thrift, Rate Limiting, Auth)             │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Core Services                             │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Feed       │  Profile     │  Messaging   │  Groups        │
│   Service    │  Service     │  Service     │  Service       │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  News Feed   │  Friends     │  Messenger   │  Events        │
│  Stories     │  Photos      │  Presence    │  Pages         │
│  Reels       │  Bio         │  Reactions   │  Marketplace   │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Infrastructure Layer                       │
│    (TAO, MySQL, Memcache, Cassandra, Kafka)                 │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### Hack Language
- PHP dialect with static typing
- Gradual typing
- HHVM runtime
- Performance improvements

```hack
// Hack Language Features
class User {
    private int $id;
    private string $name;
    private string $email;
    private Vector<Friend> $friends;
    
    public function __construct(int $id, string $name, string $email) {
        $this->id = $id;
        $this->name = $name;
        $this->email = $email;
        $this->friends = Vector {};
    }
    
    public function addFriend(Friend $friend): void {
        $this->friends->add($friend);
    }
    
    public function getMutualFriends(User $other): Vector<Friend> {
        return $this->friends->filter(
            $f ==> $other->friends->contains($f)
        );
    }
}

// Async/Await
async function getUserProfile(int $userId): Awaitable<User> {
    $user = await userStore::get($userId);
    $friends = await friendStore::getFriends($userId);
    $user->setFriends($friends);
    return $user;
}
```

### HHVM (HipHop Virtual Machine)
- JIT compiler for Hack/PHP
- Type specialization
- Memory optimization
- Fast startup

### TAO (The Associations and Objects)
- Distributed data store for social graph
- Graph database optimized for social data
- Multi-datacenter replication
- Strong consistency

```hack
// TAO Data Model
// Objects: Users, Posts, Photos
// Associations: Friends, Likes, Comments

// TAO Query
class TAOGraph {
    public function getFriends(int $userId): Vector<User> {
        $associations = $this->dao->associations(
            $userId,
            "user_to_user",
            "friend"
        );
        return $associations->map($a ==> $this->getUser($a->dId));
    }
    
    public function getNewsFeed(int $userId, int $limit): Vector<Post> {
        $friends = $this->getFriends($userId);
        $friendIds = $friends->map($f ==> $f->getId());
        
        return $this->dao->objectsByAssociation(
            $friendIds,
            "user_to_post",
            "published",
            $limit
        );
    }
}
```

### GraphQL
- Flexible API queries
- Type-safe schema
- Client-driven data fetching
- Code generation

```graphql
# Schema Definition
type User {
    id: ID!
    name: String!
    email: String!
    friends: [User!]!
    posts: [Post!]!
    profilePicture: Image!
}

type Post {
    id: ID!
    author: User!
    content: String!
    likes: [User!]!
    comments: [Comment!]!
    createdAt: DateTime!
}

type Query {
    user(id: ID!): User
    feed(userId: ID!, limit: Int): [Post!]!
}

type Mutation {
    createPost(content: String!): Post!
    likePost(postId: ID!): Post!
    addComment(postId: ID!, content: String!): Comment!
}
```

## News Feed System

### Feed Generation
1. **Gather**: Collect posts from friends and followed pages
2. **Rank**: ML-based scoring (engagement prediction)
3. **Filter**: Remove low-quality and redundant content
4. **Diversify**: Ensure content variety
5. **Serve**: Return personalized feed

### Feed Ranking Algorithm
```python
class FeedRanker:
    def rank(self, posts, user):
        scores = []
        for post in posts:
            score = self.calculate_score(post, user)
            scores.append((post, score))
        return sorted(scores, key=lambda x: x[1], reverse=True)
    
    def calculate_score(self, post, user):
        # Signals
        affinity = self.affinity_score(post.author, user)
        relevance = self.relevance_score(post, user)
        timeliness = self.timeliness_score(post)
        engagement = self.engagement_score(post)
        quality = self.quality_score(post)
        
        # ML model prediction
        features = {
            'affinity': affinity,
            'relevance': relevance,
            'timeliness': timeliness,
            'engagement': engagement,
            'quality': quality
        }
        
        return self.model.predict(features)
```

### Real-time Updates
- WebSocket connections
- Push notifications
- Live comments
- Reactions

## Data Architecture

### MySQL
- Primary data store
- Sharded by user
- Read replicas
- Strong consistency

### Memcache
- Distributed caching
- Multi-layer caching
- Consistent hashing
- Cache invalidation

### Cassandra
- Time-series data
- Messaging storage
- High write throughput

### TAO
- Social graph
- Associations and objects
- Multi-datacenter replication

### Data Models
```sql
-- User Profile
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(200),
    bio TEXT,
    profile_picture_url VARCHAR(500),
    created_at TIMESTAMP
);

-- Posts
CREATE TABLE posts (
    post_id BIGINT PRIMARY KEY,
    author_id BIGINT,
    content TEXT,
    privacy VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Friend Relationships
CREATE TABLE friendships (
    user_id_1 BIGINT,
    user_id_2 BIGINT,
    created_at TIMESTAMP,
    PRIMARY KEY (user_id_1, user_id_2)
);

-- Reactions
CREATE TABLE reactions (
    post_id BIGINT,
    user_id BIGINT,
    reaction_type VARCHAR(20),
    created_at TIMESTAMP,
    PRIMARY KEY (post_id, user_id)
);
```

## Messaging System

### Messenger Architecture
- Real-time messaging
- Presence tracking
- End-to-end encryption
- Media sharing

### Message Storage
- Cassandra for message history
- Memcache for recent messages
- Real-time sync across devices

### Presence System
- Online/offline status
- Last seen
- Typing indicators
- Read receipts

## Instagram Integration

### Media Pipeline
1. **Upload**: Client uploads photo/video
2. **Processing**: Resize, filter, transcode
3. **Storage**: Store in TAO/Cassandra
4. **Distribution**: Push to followers' feeds
5. **CDN**: Serve globally

### Explore Page
- ML-based recommendations
- Content discovery
- Trending topics
- Personalized content

## WhatsApp Integration

### End-to-End Encryption
- Signal Protocol
- Key management
- Forward secrecy
- Metadata protection

### Message Delivery
- Store-and-forward
- Offline support
- Multi-device sync
- Media compression

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

### Sapling (Source Control)
- Scalable VCS
- Monorepo support
- Fast operations
- Custom workflows

### Buck2 (Build System)
- Fast incremental builds
- Distributed execution
- Remote caching
- Dependency analysis

### Internal Developer Platform
- Self-service deployments
- Standardized tooling
- Shared libraries
- Documentation

## Organizational Structure

### Product Teams
- Facebook App
- Instagram
- WhatsApp
- Messenger
- Reality Labs
- Infrastructure

### Platform Teams
- Developer Experience
- Data Platform
- ML Platform
- Security & Compliance

## Key Lessons

1. **Hack Language Works**: Static typing improves performance and reliability
2. **TAO is Powerful**: Graph databases are ideal for social networks
3. **GraphQL is Essential**: Flexible APIs enable fast iteration
4. **Feed Ranking is Complex**: ML-driven personalization drives engagement
5. **Encryption Matters**: End-to-end encryption builds trust
6. **Scale Through Specialization**: Custom solutions for specific problems

## Statistics

- **Users**: 3B+ monthly active
- **Messages**: 100B+ daily
- **Photos**: 350M+ uploaded daily
- **Video Views**: 4B+ daily
- **Services**: 10,000+ microservices
- **Engineers**: 30,000+

## References

- [Meta Engineering Blog](https://engineering.fb.com/)
- [TAO: Facebook's Distributed Data Store](https://www.usenix.org/system/files/conference/atc13/atc13-bronson.pdf)
- [GraphQL Specification](https://graphql.org/)
- [HHVM](https://hhvm.com/)
- [Hack Language](https://hacklang.org/)
- [Sapling Source Control](https://sapling-scm.com/)
