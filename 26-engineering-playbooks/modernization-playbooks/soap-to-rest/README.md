# SOAP to REST Migration

## Overview

SOAP (Simple Object Access Protocol) web services were the standard for enterprise integration, but their complexity, verbose XML payloads, and tight coupling have driven organizations to migrate to RESTful APIs. This playbook covers the migration from SOAP to REST.

## Migration Strategy

### WSDL Analysis

Analyze existing WSDL files to understand service contracts, operations, data types, and message patterns. The WSDL provides the complete picture of what the SOAP service exposes.

### API Design

Design RESTful APIs that expose the same business capabilities as SOAP services. Map SOAP operations to REST resources and HTTP methods. Consider API versioning and backward compatibility.

### Incremental Migration

Migrate one service at a time, starting with the most commonly used or simplest services. Deploy REST APIs alongside SOAP endpoints, routing traffic based on client capability.

## Implementation Patterns

### Operation to Resource Mapping

SOAP operations map to REST resources with HTTP method semantics:

- Create operations become POST requests
- Read operations become GET requests
- Update operations become PUT or PATCH requests
- Delete operations become DELETE requests

Complex SOAP operations that combine multiple actions may require decomposition into multiple REST resources.

### XML to JSON

SOAP uses XML for message payloads. REST APIs typically use JSON. Map SOAP XML schemas to JSON structures, handling naming conventions, data types, and nesting differences.

SOAP headers map to REST HTTP headers or query parameters. Custom SOAP headers for authentication or context may become request headers or claim checks.

### WS-* Standards

SOAP relies on WS-* standards for security (WS-Security), transactions (WS-AtomicTransaction), and reliable messaging (WS-ReliableMessaging). These capabilities must be reimplemented using REST patterns:

- WS-Security becomes OAuth 2.0 or JWT
- WS-Transactions become saga patterns or compensating transactions
- WS-ReliableMessaging becomes idempotency keys and retry logic

## Key Differences

### Contract First vs Resource First

SOAP uses WSDL as a contract-first approach. REST uses resource-oriented design without a strict contract. OpenAPI/Swagger provides similar contract capabilities for REST APIs.

### Statefulness

SOAP services may maintain state across operations through WS-Addressing or session headers. REST APIs should be stateless, with state managed through tokens or client-side storage.

### Error Handling

SOAP uses fault elements for error responses. REST uses HTTP status codes and error response bodies. Design consistent error response formats for REST APIs.

## Lessons Learned

### Preserve Business Semantics

The REST API should expose the same business capabilities as the SOAP service. Changing semantics during migration creates risk and confusion for consumers.

### Version API Changes

REST API versioning through URL path or header allows clients to migrate gradually. Breaking changes should be introduced in new versions rather than modifying existing endpoints.

### Update Client Libraries

Client code that consumes SOAP services through WSDL-generated proxies must be updated to use REST clients. Consider generating client code from OpenAPI specifications.

### Handle Asynchronous Operations

SOAP may use asynchronous patterns through WS-Addressing or polling. REST APIs can use webhooks, polling, or server-sent events for asynchronous notifications.
