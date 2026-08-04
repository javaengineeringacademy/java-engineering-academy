# Google: Search and Cloud at Global Scale

How Google built infrastructure serving 2B+ users across multiple products.

## Company Overview

Google is the world's largest search engine and a leading cloud provider. Their engineering culture: innovation, scale, and open-source contributions.

## Architecture Evolution

### Phase 1: Early Days (1998-2003)
- Simple web crawler
- PageRank algorithm
- Basic infrastructure

### Phase 2: Scale (2003-2010)
- MapReduce
- BigTable
- Borg cluster management
- Internal tools

### Phase 3: Modern Era (2010-Present)
- Kubernetes
- Spanner
- Monorepo
- Cloud-native services

## Core Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│       (Search, Gmail, Maps, YouTube, Drive, Cloud)          │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    Global Load Balancer                     │
│              (Anycast, SSL Termination)                     │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Service Mesh                              │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Search     │  Gmail       │  Maps        │  YouTube       │
│   Service    │  Service     │  Service     │  Service       │
├──────────────┼──────────────┼──────────────┼────────────────┤
│  Indexing    │  Mail        │  Routing     │  Video         │
│  Ranking     │  Calendar    │  Places      │  Transcoding   │
│  Suggestions│  Contacts    │  Navigation  │  Recommendations│
└──────────────┴──────────────┴──────────────┴────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  Infrastructure Layer                       │
│    (Borg, Spanner, BigTable, Colossus, Ceph)                │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### Borg
- Cluster management system
- Schedules 2B+ containers daily
- Resource optimization
- Failure handling

```
// Borg Architecture
┌─────────────────────────────────────────┐
│                Borg Master              │
│  (Replicated, Paxos consensus)         │
├─────────────────────────────────────────┤
│              Borglet (on each node)     │
│  (Manages containers, reports to master)│
└─────────────────────────────────────────┘

// Borg Job Definition
name: "search-indexer";
owner: "search-team";
priority: 10;
num_instances: 1000;
resources { cpu_cores: 4; ram_gb: 16; disk_gb: 100; }
task {
  command: "/usr/bin/search-indexer";
  args: "--index=/data/index";
}
```

### Spanner
- Globally distributed database
- External consistency (TrueTime)
- SQL interface
- Automatic sharding

```sql
-- Spanner Schema
CREATE TABLE Users (
    user_id INT64 NOT NULL,
    name STRING(MAX),
    email STRING(MAX),
    created_at TIMESTAMP,
) PRIMARY KEY (user_id);

CREATE TABLE Orders (
    order_id INT64 NOT NULL,
    user_id INT64 NOT NULL,
    amount FLOAT64,
    status STRING(MAX),
    created_at TIMESTAMP,
) PRIMARY KEY (order_id),
  INTERLEAVE IN PARENT Users ON DELETE CASCADE;

-- TrueTime Query
SELECT * FROM Orders 
WHERE created_at > TIMESTAMP('2024-01-01')
ORDER BY created_at DESC 
LIMIT 100;
```

### Bigtable
- Wide-column NoSQL database
- Petabyte scale
- Low latency
- Used for: Analytics, IoT, Time Series

### Colossus
- Distributed file system (GFS successor)
- Automatic replication
- Checksumming
- Used for: Storage backbone

### Monorepo
- Single repository for all code
- Atomic commits
- Shared libraries
- Code review at scale

```
// Monorepo Structure
├── src/
│   ├── google/
│   │   ├── search/
│   │   │   ├── BUILD
│   │   │   ├── indexer/
│   │   │   └── ranking/
│   │   ├── gmail/
│   │   │   ├── BUILD
│   │   │   ├── inbox/
│   │   │   └── compose/
│   │   └── maps/
│   │       ├── BUILD
│   │       ├── routing/
│   │       └── places/
├── third_party/
├── tools/
└── WORKSPACE
```

## Search Architecture

### Indexing Pipeline
1. **Crawl**: Fetch web pages
2. **Parse**: Extract text and links
3. **Index**: Build inverted index
4. **Rank**: Apply PageRank and ML

### Serving Pipeline
1. **Query Understanding**: Parse and expand query
2. **Candidate Retrieval**: Find relevant documents
3. **Ranking**: Score and rank results
4. **Filtering**: Remove spam and low-quality
5. **Presentation**: Format and return results

### Ranking Algorithm
- PageRank (link analysis)
- Relevance scoring (TF-IDF, BM25)
- ML models (neural ranking)
- Freshness signals
- User engagement

## Gmail Architecture

### Storage
- Petabytes of email data
- Multi-replica storage
- Automatic deduplication
- Compression

### Features
- Real-time sync
- Search (full-text)
- Spam filtering
- Smart compose

### Infrastructure
- Spanner for metadata
- Colossus for email content
- Real-time indexing
- ML-based features

## YouTube Architecture

### Video Pipeline
1. **Upload**: Client uploads video
2. **Processing**: Transcode to multiple formats
3. **Storage**: Store in Colossus
4. **CDN**: Distribute globally
5. **Serving**: Stream to viewers

### Recommendation System
- Watch history
- Similar videos
- Trending content
- Personalization

### Live Streaming
- Real-time encoding
- Adaptive bitrate
- Low latency delivery
- Global distribution

## Data Architecture

### Spanner
- Global consistency
- SQL interface
- Automatic sharding
- Cross-region replication

### Bigtable
- Wide-column storage
- Petabyte scale
- Low latency
- Time-series data

### Dataflow
- Stream processing
- Batch processing
- Unified programming model
- Auto-scaling

### BigQuery
- Serverless analytics
- SQL interface
- Petabyte scale
- Real-time streaming

## Observability

### Cloud Monitoring
- Metrics collection
- Alerting
- Dashboards
- SLO tracking

### Cloud Logging
- Centralized logging
- Real-time analysis
- Log-based metrics
- Audit logging

### Cloud Trace
- Distributed tracing
- Latency analysis
- Performance monitoring

## Developer Productivity

### Blaze (Bazel)
- Build system for monorepo
- Incremental builds
- Remote caching
- Distributed execution

### Gerrit
- Code review system
- Atomic changes
- Approval workflow
- Integration with CI/CD

### Mondrian
- Internal code review
- Used before Gerrit
- Custom workflows
- Integration with tools

## Organizational Structure

### Product Teams
- Search, Gmail, Maps, YouTube, etc.
- Autonomous teams
- Clear ownership
- Independent deployment

### Infrastructure Teams
- Core infrastructure
- Cloud platform
- Developer tools
- Security and compliance

### Research Teams
- AI/ML research
- Quantum computing
- New technologies
- Academic partnerships

## Key Lessons

1. **Monorepo Scales**: With proper tooling, monorepos work at massive scale
2. **Spanner is Revolutionary**: TrueTime enables globally consistent databases
3. **Borg Precedes Kubernetes**: Lessons learned led to open-source K8s
4. **ML Everywhere**: Machine learning powers most products
5. **Infrastructure is the Product**: Google's infrastructure enables AWS and GCP
6. **Innovation Culture**: 20% time, hackathons, and research partnerships

## Statistics

- **Users**: 2B+ across products
- **Search Queries**: 8.5B+/day
- **Email Users**: 1.8B+
- **YouTube Users**: 2B+
- **Data Centers**: 30+ globally
- **Employees**: 180,000+

## References

- [Google Research](https://research.google/)
- [Google Cloud Blog](https://cloud.google.com/blog)
- [Spanner Paper](https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/41345.pdf)
- [Borg Paper](https://research.google/pubs/pub43438/)
- [Monorepo at Google](https://testing.googleblog.com/2023/06/taming-monorepo-with-bazel-tales-from.html)
- [Google Infrastructure](https://cloud.google.com/docs/overview/google-cloud-platform)
