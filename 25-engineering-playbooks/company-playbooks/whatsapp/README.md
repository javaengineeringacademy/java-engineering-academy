# WhatsApp Engineering Playbook

## Company Context

WhatsApp serves over 2 billion users with a remarkably small engineering team. The company built its messaging platform on Erlang, leveraging the language's strengths in concurrency, fault tolerance, and distributed systems to achieve massive scale with minimal operational overhead.

## Technology Stack

### Erlang/OTP

WhatsApp chose Erlang for its lightweight process model, which enables millions of concurrent connections on a single server. Each WhatsApp connection runs as an independent Erlang process, isolated from other connections.

Erlang's supervision trees provide automatic failure recovery. When a process crashes, its supervisor restarts it automatically, maintaining system availability without operator intervention.

### Message Storage

WhatsApp stores messages in Mnesia, Erlang's built-in distributed database. Messages are replicated across nodes for durability, with eventual consistency for read operations.

For media storage, WhatsApp uses custom storage servers that manage file deduplication, encryption, and distribution across the network.

### Protocol Design

WhatsApp uses a custom binary protocol optimized for mobile networks. The protocol minimizes bandwidth usage through compression, efficient encoding, and delta synchronization.

## Architecture Decisions

### Minimal Server Footprint

WhatsApp famously operated with a very small engineering team relative to its user base. This was possible because Erlang's fault tolerance and self-healing properties reduced operational overhead.

### Push Notification Architecture

WhatsApp built a distributed push notification system that routes messages to devices through multiple channels. The system handles device offline scenarios, message queuing, and delivery confirmation.

### End-to-End Encryption

WhatsApp implemented the Signal Protocol for end-to-end encryption, ensuring that messages are encrypted on the sender's device and only decrypted on the recipient's device. This required careful key management and protocol design.

## Lessons Learned

### Choose Languages That Match the Problem

Erlang's strengths in concurrency and fault tolerance made it ideal for a messaging platform. WhatsApp did not choose Erlang because it was trendy, but because it was the right tool for the problem.

### Embrace Immutability

Erlang's immutable data structures simplify concurrent programming by eliminating shared state. This reduces bugs and makes the system easier to reason about.

### Optimize for Failure

Erlang's "let it crash" philosophy means failures are handled gracefully rather than avoided. Supervision trees restart failed processes automatically, maintaining system availability.

## Takeaways

WhatsApp demonstrates that the right technology choices, combined with disciplined engineering, can achieve extraordinary scale with minimal resources. Erlang's concurrency model and fault tolerance provide a blueprint for building reliable distributed messaging systems.
