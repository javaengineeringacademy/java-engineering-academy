# Case Studies: How Top Tech Companies Build Software

Learn from the engineering practices of the world's most successful technology companies.

## Overview

This module contains detailed case studies of 10 major tech companies, examining their architecture decisions, engineering practices, and lessons learned at scale.

## Companies Covered

| Company | Focus Areas | Scale |
|---------|-------------|-------|
| [Netflix](netflix/) | Microservices, Chaos Engineering | 200M+ subscribers |
| [Uber](uber/) | Domain Microservices, Schema Registry | 19M+ trips/day |
| [Airbnb](airbnb/) | SOA, Migration, Data Platform | 7M+ listings |
| [Spotify](spotify/) | Squads, Tribes, Backends | 400M+ users |
| [LinkedIn](linkedin/) | REST.li, Kafka, Infrastructure | 900M+ members |
| [Twitter](twitter/) | Scaling, Timeline, Cache | 350M+ tweets/day |
| [Amazon](amazon/) | Two-Pizza Teams, ORCA | 1.6M+ employees |
| [Google](google/) | Borg, Spanner, Monorepo | 2B+ users |
| [Facebook](facebook/) | Hack, HHVM, TAO | 3B+ users |
| [Stripe](stripe/) | API Design, Reliability | $640B+ processed |

## Key Themes Across Companies

### 1. Microservices Evolution
- Most companies started monolithic and evolved to microservices
- Domain-driven design guides service boundaries
- API gateways are universal (Zuul, Envoy, etc.)

### 2. Data at Scale
- Custom storage solutions for specific problems
- Event streaming (Kafka) is nearly universal
- Data pipelines are first-class engineering concerns

### 3. Reliability Engineering
- Chaos engineering pioneered by Netflix
- SRE practices adopted broadly
- Redundancy at every layer

### 4. Developer Productivity
- Internal developer platforms
- CI/CD automation
- Monorepo vs. polyrepo debates

### 5. Organizational Structure
- Conway's Law in action
- Team autonomy balanced with platform standardization
- Innovation time (Google's 20%, hackathons)

## How to Use These Studies

1. **For Architects**: Focus on architecture decisions and trade-offs
2. **For Engineers**: Study the technical implementations and tools
3. **For Leaders**: Examine organizational structures and processes
4. **For Interview Prep**: Use as examples in system design interviews

## Common Anti-Patterns Observed

- Over-engineering before reaching sufficient scale
- Premature microservice decomposition
- Under-investing in observability
- Ignoring data consistency in distributed systems
- Copying solutions without adapting to context

## Further Reading

- Engineering blogs of each company
- "Building Microservices" by Sam Newman
- "Designing Data-Intensive Applications" by Martin Kleppmann
- "The Site Reliability Workbook" by Google SRE Team
