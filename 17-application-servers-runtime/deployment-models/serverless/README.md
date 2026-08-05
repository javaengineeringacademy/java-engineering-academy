# Serverless Deployment

## Overview

Serverless deployment executes code in response to events without managing server infrastructure. Functions-as-a-Service (FaaS) platforms handle scaling, patching, and operational concerns automatically.

## Function-as-a-Service

FaaS platforms execute functions triggered by events like HTTP requests, queue messages, or file uploads. Each invocation runs in an isolated environment with automatic scaling.

## Major Platforms

- AWS Lambda: event-driven functions with extensive service integrations
- Azure Functions: serverless compute with .NET, Java, JavaScript, Python support
- Google Cloud Functions: lightweight functions with HTTP and event triggers

## Event Sources

Serverless functions respond to diverse event sources: HTTP endpoints, message queues, storage events, database changes, IoT messages, and scheduled invocations.

## Cold Start

Cold starts occur when platforms allocate new instances for function execution. Initialization time depends on runtime, package size, and platform-specific optimizations.

## Pricing

Serverless pricing charges per invocation and execution duration. No charges occur when functions are idle, making serverless cost-effective for variable or sporadic workloads.

## Limitations

- Execution time limits (typically 5-15 minutes)
- Stateless execution requires external storage
- Cold start latency for some runtimes
- Vendor lock-in through platform-specific APIs
- Debugging complexity for distributed functions

## Best Practices

Serverless applications benefit from small function sizes, efficient dependency packaging, connection pooling through provisioned concurrency, and asynchronous processing patterns.

## When to Use Serverless

Serverless suits event-driven architectures, variable workloads, rapid prototyping, and scenarios where operational overhead reduction justifies the trade-offs in control and flexibility.
