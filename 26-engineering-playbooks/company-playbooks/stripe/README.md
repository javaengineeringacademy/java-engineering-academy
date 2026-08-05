# Stripe Engineering Playbook

## Company Context

Stripe processes billions of dollars in payments annually, requiring extreme reliability and correctness. The company's engineering practices around idempotency, distributed systems, and API design have become reference implementations for financial technology.

## Technology Stack

### Payment Processing

Stripe's core system handles payment authorization, capture, and settlement across multiple payment methods and currencies. Every operation must be idempotent to handle retries safely.

### Distributed Systems

Stripe built a custom distributed systems layer on top of existing infrastructure, implementing consensus protocols, distributed transactions, and exactly-once processing guarantees.

### API-First Design

Stripe's API design philosophy prioritizes developer experience. APIs are consistent, well-documented, and versioned to maintain backward compatibility while enabling rapid iteration.

## Architecture Decisions

### Idempotency Keys

Stripe requires idempotency keys for all write operations. Each API request includes a unique key that the server uses to deduplicate requests. If a request fails and the client retries, the server returns the original response rather than processing the operation twice.

This pattern is critical for payment processing where duplicate charges would be catastrophic. The idempotency key is stored with the operation result, and subsequent requests with the same key return the cached response.

### Double-Entry Accounting

Every financial transaction in Stripe follows double-entry bookkeeping principles. Every debit has a corresponding credit, ensuring the system remains balanced at all times. This enables accurate reconciliation and audit trails.

### Distributed Ledgers

Stripe maintains distributed ledgers for tracking balances across accounts. Ledgers are partitioned by account and replicated for durability, with consensus protocols ensuring consistency.

### Safe State Machines

Payment operations follow strict state machines with defined transitions. State changes are atomic and auditable, preventing invalid state transitions that could corrupt financial data.

## Lessons Learned

### Correctness Over Speed

In financial systems, correctness is non-negotiable. Stripe invests heavily in testing, formal verification, and audit trails to ensure every transaction is processed correctly.

### API Versioning is Essential

Breaking API changes can disrupt thousands of businesses. Stripe maintains multiple API versions simultaneously, allowing clients to migrate at their own pace while the platform evolves.

### Reconciliation Must Be Automatic

Manual reconciliation does not scale. Stripe built automated reconciliation systems that continuously verify consistency across internal ledgers and external payment networks.

## Takeaways

Stripe demonstrates that financial systems require rigorous attention to idempotency, consistency, and auditability. The combination of idempotency keys, double-entry accounting, and state machines provides a robust foundation for payment processing at scale.
