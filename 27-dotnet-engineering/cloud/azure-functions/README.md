## Azure Functions

Serverless compute platform for running event-driven code without managing infrastructure.

## Overview

Azure Functions provides a serverless execution model where code runs in response to triggers (HTTP, timer, queue, etc.) with automatic scaling and pay-per-execution pricing.

## Why It Matters

- No infrastructure management
- Automatic scaling based on demand
- Pay only for execution time
- Rich trigger and binding system
- Ideal for event-driven and scheduled workloads

## Key Concepts

- **Triggers**: Events that invoke functions (HTTP, Timer, Queue, etc.)
- **Bindings**: Declarative input/output connections
- **Function App**: Container for related functions
- **Consumption Plan**: Pay-per-execution hosting
- **Premium Plan**: Pre-warmed instances for low latency
- **Dedicated Plan**: Reserved VM instances

## Core Topics

- Function triggers and bindings
- Durable Functions for orchestrations
- HTTP triggers for APIs
- Queue and blob triggers
- Timer triggers for scheduling
- Output bindings for writing data
- Local development and testing

## Best Practices

- Keep functions small and focused
- Use Durable Functions for long-running workflows
- Implement proper error handling with retry policies
- Use managed identities for Azure service access
- Monitor with Application Insights

## Hands-on Labs

- Create an HTTP-triggered function
- Build a queue-triggered processing function
- Implement Durable Functions orchestration
- Schedule tasks with timer triggers

## Interview Questions

1. What is the difference between Consumption and Premium plans?
2. How do Durable Functions work?
3. What triggers are available in Azure Functions?

## References

- https://learn.microsoft.com/azure/azure-functions/
- https://learn.microsoft.com/azure/azure-functions/functions-triggers-bindings
- https://learn.microsoft.com/azure/azure-functions/durable/
