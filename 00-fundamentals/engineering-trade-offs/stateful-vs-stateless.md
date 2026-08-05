# Stateful vs Stateless

## Problem State

Should your service remember information between requests or treat every request independently? Stateless services are simpler to scale. Stateful services are faster and more natural for many use cases.

## The Core Tension

Stateless: Every request contains all information needed to process it. No memory of previous requests. Easy to distribute, scale, and recover.

Stateful: The service remembers something between requests. Faster, more efficient, but harder to scale and recover from failures.

## When to Choose Stateless

**Horizontal scaling**: Stateless services can be scaled by adding more instances. Any instance can handle any request.

**Load balancing**: Requests can go to any instance. No session affinity required.

**Fault tolerance**: If an instance dies, another instance picks up immediately. No state is lost.

**Simplified deployment**: Deploy new versions without worrying about migrating state. Rollbacks are trivial.

**Serverless compatibility**: Functions-as-a-Service requires stateless design.

### How to Make Services Stateless

Store state externally:
- Session data in Redis or Memcached
- User preferences in a database
- Shopping cart in a distributed store
- Authentication tokens validated against a central service

## When to Choose Stateful

**Performance**: Local state is faster than external store lookups. Microseconds vs milliseconds.

**Complex state**: When the state is complex and expensive to reconstruct (cursors, iterators, connection pools).

**Protocol requirements**: Some protocols are inherently stateful (WebSocket connections, TCP sessions).

**Caching**: Local in-memory caches are faster than distributed caches.

**Streaming**: Stateful stream processing (windowed aggregations, session tracking) requires maintaining state.

## Session Management Patterns

**Sticky sessions**: Load balancer routes all requests from a user to the same instance. Simple but limits scaling.

**Session store**: External store (Redis, database) holds session data. Any instance can handle any request. More scalable.

**Token-based**: JWT tokens contain session data. Stateless at the server level. State is in the token itself.

**Database sessions**: Store session in the database. Durable but slower. Good for long-lived sessions.

## Real-World Examples

**E-commerce checkout**: Shopping cart is stateful by nature. Store it externally (Redis, database) so any server instance can access it.

**WebSocket chat server**: The connection itself is stateful. Each connection maintains context. This is unavoidable and acceptable.

**REST API**: Typically stateless. Each request contains authentication token, request parameters. Any instance can handle any request.

**Game server**: Game state is inherently stateful. Player position, health, inventory must be maintained. Stateful design is natural here.

## The State Migration Problem

Moving from stateful to stateful is hard:

1. Identify all state in your application
2. Extract state to an external store
3. Update all code to read/write from external store
4. Handle the transition period where some requests still use local state

This is why starting stateless is easier. You can always add state later, but removing it is painful.

## Decision Matrix

| Factor | Choose Stateless | Choose Stateful |
|--------|-----------------|----------------|
| Scaling needs | Horizontal | Vertical sufficient |
| Performance requirements | Moderate | Extreme |
| Fault tolerance | Critical | Tolerant |
| Protocol | Request-response | Connection-based |
| Complexity budget | Low | High |
| Infrastructure | External stores available | Limited |

## Interview Relevance

**Common questions**:
- "Design a chat system"
- "How would you handle user sessions?"
- "Design a game server"

**What interviewers want**:
- You understand how to externalize state
- You know the scaling implications of stateful design
- You can identify when stateful is unavoidable
- You understand session management patterns

**Red flags**:
- Storing state in application memory without externalizing
- Not considering what happens when an instance dies
- Making everything stateful for simplicity
- Not discussing the trade-offs of different session patterns

## Key Takeaway

Default to stateless. Store state externally when you need it. Accept stateful design only when the protocol or performance requirements demand it. The cost of state is complexity in scaling, deployment, and failure recovery.
