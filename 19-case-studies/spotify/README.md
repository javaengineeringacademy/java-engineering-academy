# Spotify: Music Streaming at Scale

How Spotify built a platform serving 400M+ users with 80M+ tracks.

## Company Overview

Spotify is a digital music, podcast, and video streaming service. Their engineering culture is famous for the Squad/Tribe model and autonomous team structure.

## Architecture Evolution

### Phase 1: Monolith (2008-2012)
- Single Python application
- PostgreSQL database
- Manual deployments

### Phase 2: SOA (2012-2018)
- 800+ microservices
- Squad/Tribe structure
- Event-driven architecture
- Data lake

### Phase 3: Cloud Native (2018-Present)
- Google Cloud Platform
- Kubernetes
- Real-time ML
- Creator tools

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│          (Mobile, Web, Desktop, Smart Speakers, TV)         │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    API Gateway                              │
│            (GraphQL, gRPC, Rate Limiting)                   │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Squad Services                             │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Player     │  Search      │  Playlist    │  Social        │
│   Squad      │  Squad       │  Squad       │  Squad         │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Streaming   │  Discovery   │  Curation    │  Sharing       │
│  Playback    │  Ranking     │  Collaboration│  Activity      │
│  Offline     │  Filtering   │  Recommendations│  Messaging   │
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Platform Layer                             │
│    (Infrastructure, Data, ML, Developer Tools)              │
└─────────────────────────────────────────────────────────────┘
```

## Squad/Tribe Model

### Structure
```
Company
├── Tribe: Music Discovery
│   ├── Squad: Search
│   ├── Squad: Recommendations
│   └── Squad: Browse
├── Tribe: Creator
│   ├── Squad: Upload
│   ├── Squad: Analytics
│   └── Squad: Monetization
├── Tribe: Playback
│   ├── Squad: Player
│   ├── Squad: Offline
│   └── Squad: Quality
└── Tribe: Social
    ├── Squad: Sharing
    ├── Squad: Activity
    └── Squad: Messaging
```

### Squad Characteristics
- **Autonomy**: Own their domain end-to-end
- **Cross-functional**: Design, engineering, data, QA
- **Mission-driven**: Clear goals aligned with company objectives
- **Self-organizing**: Choose their own processes

### Tribe Characteristics
- **2-4 squads** with related missions
- **Tribe Lead**: Facilitates cross-squad coordination
- **Shared resources**: Data, infrastructure, design
- **Regular sync**: Chapter meetings, guild participation

### Chapter Model
- **Chapter**: People with similar skills across squads
- **Chapter Lead**: Mentors and manages chapter members
- **Best practices**: Share knowledge across squads
- **Career development**: Technical growth paths

### Guild Model
- **Guild**: Community of interest across tribes
- **Open participation**: Anyone can join
- **Knowledge sharing**: Regular meetups, talks
- **Standards**: Evolving best practices

## Key Technologies

### Backends for Frontends (BFF)
- Separate backend per client type
- Optimized for specific needs
- Independent deployment
- Technology flexibility

```java
// Mobile BFF Example
@RestController
@RequestMapping("/api/mobile")
public class MobilePlaylistController {
    
    @GetMapping("/playlists/{id}")
    public PlaylistResponse getPlaylist(@PathVariable String id) {
        // Mobile-optimized response
        Playlist playlist = playlistService.getPlaylist(id);
        return MobilePlaylistMapper.toMobileResponse(playlist);
    }
}

// Web BFF Example
@RestController
@RequestMapping("/api/web")
public class WebPlaylistController {
    
    @GetMapping("/playlists/{id}")
    public PlaylistResponse getPlaylist(@PathVariable String id) {
        // Web-optimized response with more data
        Playlist playlist = playlistService.getPlaylist(id);
        return WebPlaylistMapper.toWebResponse(playlist);
    }
}
```

### Event-Driven Architecture
- Kafka-based event streaming
- Domain events
- Event sourcing
- CQRS patterns

```yaml
# Kafka Configuration
spotify:
  kafka:
    clusters:
      - name: events
        brokers: kafka-events:9092
        schema-registry: schema-registry:8081
      - name: metrics
        brokers: kafka-metrics:9092
    topics:
      - name: playback-events
        partitions: 100
        replication: 3
      - name: user-actions
        partitions: 50
        replication: 3
```

### Apollo GraphQL
- Schema federation
- Type safety
- Performance optimization
- Client-specific schemas

```graphql
# Schema Definition
type Track {
    id: ID!
    name: String!
    artists: [Artist!]!
    album: Album!
    duration: Int!
    popularity: Int!
    previewUrl: String
}

type Playlist {
    id: ID!
    name: String!
    description: String
    tracks: [Track!]!
    owner: User!
    collaborative: Boolean!
    public: Boolean!
}

type Query {
    playlist(id: ID!): Playlist
    search(query: String!): SearchResult!
    recommendations(seed: [ID!]!): [Track!]!
}
```

## Data Architecture

### Data Lake
- Raw event storage
- Schema evolution
- Batch processing
- Historical analysis

### Data Warehouse
- Analytical queries
- Business intelligence
- Reporting
- Ad-hoc analysis

### Real-time Data
- Stream processing (Kafka Streams)
- Real-time dashboards
- Feature engineering
- ML pipelines

### Data Models
```sql
-- Track Metadata
CREATE TABLE tracks (
    id VARCHAR(22) PRIMARY KEY,
    name VARCHAR(500),
    artist_ids VARCHAR(22)[],
    album_id VARCHAR(22),
    duration_ms INT,
    popularity INT,
    explicit BOOLEAN,
    created_at TIMESTAMP
);

-- User Listening History
CREATE TABLE listening_history (
    user_id VARCHAR(22),
    track_id VARCHAR(22),
    played_at TIMESTAMP,
    ms_played INT,
    context_type VARCHAR(50),
    PRIMARY KEY (user_id, played_at)
);
```

## ML and Recommendations

### Discover Weekly
- Collaborative filtering
- Content-based filtering
- Natural language processing
- Audio analysis

### Algorithm Pipeline
1. **Data Collection**: Listening history, skips, saves
2. **Feature Engineering**: Audio features, user preferences
3. **Model Training**: Deep learning models
4. **Serving**: Real-time predictions
5. **Evaluation**: A/B testing, metrics

### Audio Analysis
- Loudness analysis
- Tempo detection
- Key detection
- Mood classification

## Creator Tools

### Spotify for Artists
- Streaming analytics
- Audience insights
- Playlist placement
- Release management

### API Platform
- Web API
- Webhook API
- Advertising API
- Partner ecosystem

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

### Backstage
- Service catalog
- Documentation
- Scaffolding
- Plugin ecosystem

### CI/CD
- Automated testing
- Security scanning
- Performance testing
- Gradual rollout

### Local Development
- Docker Compose
- Service virtualization
- Mock services
- Development environment

## Organizational Culture

### Squad Autonomy
- Choose your own tools
- Flexible processes
- Ownership and accountability
- Innovation time

### Failure Culture
- Blameless postmortems
- Learning from incidents
- Chaos engineering
- Resilience testing

## Key Lessons

1. **Autonomy Enables Innovation**: Squads can move fast independently
2. **Alignment is Critical**: Clear mission and objectives keep everyone moving together
3. **Platform Multiplies**: Backstage and internal tools boost productivity
4. **Data is King**: Recommendations drive engagement
5. **Creator Tools Matter**: Empowering creators strengthens the ecosystem
6. **Culture Scales**: The squad model has been adopted by many companies

## Statistics

- **Users**: 400M+ (180M+ premium)
- **Tracks**: 80M+
- **Podcasts**: 4M+
- **Markets**: 180+
- **Services**: 800+ microservices
- **Engineers**: 4,000+

## References

- [Spotify Engineering Blog](https://engineering.atspotify.com/)
- [Squad Model](https://blog.crisp.se/wp-content/uploads/2012/11/SpotifyScaling.pdf)
- [Backstage](https://backstage.io/)
- [Spotify GraphQL](https://engineering.atspotify.com/2018/11/how-we-use-graphql-at-spotify/)
- [Spotify ML](https://engineering.atspotify.com/2022/03/introducing-annoy/)
