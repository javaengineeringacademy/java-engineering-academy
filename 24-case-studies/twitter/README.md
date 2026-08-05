# Twitter: Real-Time Communication at Scale

How Twitter built a platform handling 350M+ tweets daily with real-time delivery.

## Company Overview

Twitter (now X) is a social media platform for real-time public conversations. Their engineering challenge: delivering 350M+ tweets daily with sub-second latency.

## Architecture Evolution

### Phase 1: Rails Monolith (2006-2010)
- Single Ruby on Rails application
- MySQL database
- "Fail Whale" era

### Phase 2: Scala Migration (2010-2015)
- Scala-based services
- Finagle RPC framework
- Manhattan storage system
- Real-time infrastructure

### Phase 3: Modern Platform (2015-Present)
- Microservices architecture
- GraphQL API
- Machine learning pipeline
- Global distribution

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│         (Web, Mobile, API, Third-party Apps)                │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    API Gateway                              │
│          (RPC, GraphQL, Rate Limiting, Auth)                │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Core Services                             │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Tweet      │  User        │  Timeline    │  Search        │
│   Service    │  Service     │  Service     │  Service       │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Creation    │  Profiles    │  Fan-out     │  Indexing      │
│  Storage     │  Follow      │  Ranking     │  Query         │
│  Delivery    │  Verification│  Caching     │  Results       │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Infrastructure Layer                       │
│    (Manhattan, Memphis, Gizzard, Cache, Kafka)              │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### Finagle RPC Framework
- Thrift-based RPC
- Service discovery
- Load balancing
- Circuit breaking

```scala
// Finagle Service Example
class TweetService extends TweetService.MethodPerEndpoint {
  
  def getTweet(id: TweetId): Future[Tweet] = {
    tweetRepository.get(id).map {
      case Some(tweet) => tweet
      case None => throw new TweetNotFoundException(id)
    }
  }
  
  def createTweet(request: CreateTweetRequest): Future[Tweet] = {
    for {
      tweet <- tweetRepository.create(request)
      _ <- fanoutService.fanout(tweet)
      _ <- searchIndexService.index(tweet)
    } yield tweet
  }
}

// Finagle Client
val client = ThriftMux.client
  .withTransport.maxLifeTime(Duration.fromDays(1))
  .withSession.maxLifeTime(Duration.fromHours(1))
  .newClient[TweetService.MethodPerEndpoint]("tweet-service:8080")
```

### Manhattan Storage System
- Distributed key-value store
- Multi-datacenter replication
- Strong consistency
- Low latency

### Gizzard Sharding Framework
- Horizontal sharding
- Consistent hashing
- Read/write splitting
- Data migration

### Memcached/Mcrouter
- Distributed caching
- Multi-layer caching
- Consistent hashing
- Cluster management

## Timeline System

### Fan-out on Write
```
Tweet Creation
    │
    ├── Timeline Cache (for followers)
    │   └── Update all follower timelines
    │
    ├── User Timeline Cache
    │   └── Store in user's timeline
    │
    └── Home Timeline Cache
        └── Store in home timeline
```

### Fan-out on Read
```
Timeline Request
    │
    ├── Check Timeline Cache
    │   └── Hit: Return cached timeline
    │
    └── Miss: Merge on Read
        ├── User's tweets
        ├── Followed users' tweets
        ├── Ranked by relevance
        └── Cache result
```

### Hybrid Approach
- **Celebrity accounts**: Fan-out on read (too many followers)
- **Regular users**: Fan-out on write (manageable follower count)
- **Dynamic threshold**: Based on follower count

```scala
// Fan-out Service
class FanoutService {
  
  def fanout(tweet: Tweet): Future[Unit] = {
    val followerCount = getFollowerCount(tweet.authorId)
    
    if (followerCount < CELEBRITY_THRESHOLD) {
      // Fan-out on write
      fanoutOnWrite(tweet)
    } else {
      // Fan-out on read (lazy evaluation)
      Future.value(())
    }
  }
  
  private def fanoutOnWrite(tweet: Tweet): Future[Unit] = {
    val followers = getFollowers(tweet.authorId)
    Future.collect(
      followers.map(followerId => 
        timelineCache.append(followerId, tweet)
      )
    ).map(_ => ())
  }
}
```

### Timeline Ranking
- Real-time ML models
- Engagement prediction
- Personalization
- Content filtering

## Tweet Storage

### Manhattan
- Distributed KV store
- Multi-region replication
- Strong consistency
- Low latency reads/writes

```scala
// Manhattan Client
class ManhattanTweetStore {
  
  def getTweet(id: TweetId): Future[Option[Tweet]] = {
    val key = ManhattanKey("tweets", id.toString)
    manhattanClient.get(key).map {
      case Some(value) => Some(value.toTweet)
      case None => None
    }
  }
  
  def storeTweet(tweet: Tweet): Future[Unit] = {
    val key = ManhattanKey("tweets", tweet.id.toString)
    manhattanClient.put(key, tweet.toBytes)
  }
}
```

### Gizzard
- Horizontal sharding
- Consistent hashing
- Read/write splitting
- Data migration

### Cache Strategy
- **L1**: In-process cache (100ms)
- **L2**: Memcached (1ms)
- **L3**: Manhattan (10ms)
- **L4**: MySQL (50ms)

## Search Infrastructure

### Real-time Indexing
- Kafka-based pipeline
- Near-real-time updates
- Incremental indexing
- Schema evolution

### Search Features
- Full-text search
- Autocomplete
- Trending topics
- Geographic search
- Advanced filters

### Index Architecture
- Inverted index
- Forward index
- Geographic index
- Temporal index

## Real-Time Systems

### Streaming
- Real-time tweet delivery
- WebSocket connections
- Push notifications
- Live updates

### Event Processing
- Kafka-based pipelines
- Real-time analytics
- Anomaly detection
- Trending topics

## Observability

### Metrics
- 100M+ metrics
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
- Tweet & Content
- User & Identity
- Timeline & Feed
- Search & Discovery
- Platform Infrastructure

### Platform Teams
- Developer Experience
- Data Platform
- ML Platform
- Security & Compliance

## Key Lessons

1. **Fan-out Strategy Matters**: Hybrid approach balances write and read costs
2. **Caching is Critical**: Multi-layer caching enables low latency
3. **Real-time is Complex**: Streaming and push require specialized systems
4. **Search is Essential**: Real-time indexing powers discovery
5. **ML Drives Engagement**: Personalization increases time on platform
6. **Simplicity Wins**: Simple solutions often outperform complex ones

## Statistics

- **Tweets**: 350M+ daily
- **Users**: 350M+ monthly active
- **Services**: 1,000+ microservices
- **Kafka Messages**: 1T+/day
- **Engineers**: 2,000+
- **Data**: 500PB+ stored

## References

- [Twitter Engineering Blog](https://blog.twitter.com/engineering)
- [Finagle Framework](https://twitter.github.io/finagle/)
- [Manhattan Storage](https://blog.twitter.com/engineering/en_us/topics/infrastructure/2019/manhattan-real-time-distributed-datastore.html)
- [Gizzard Sharding](https://github.com/twitter/gizzard)
- [Twitter Infrastructure](https://blog.twitter.com/engineering/en_us/topics/infrastructure.html)
