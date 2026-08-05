# Undertow

## Overview

Undertow is a flexible, high-performance Java web server developed by Red Hat. It serves as the default web server in WildFly application server and can run standalone or embedded in applications.

## Architecture

Undertow uses a non-blocking architecture with a core based on Java NIO. It handles concurrent connections efficiently using minimal threads, making it suitable for high-concurrency workloads.

## Embedded Usage

Undertow embeds easily in Java applications with a fluent builder API. Spring Boot can use Undertow as an alternative to Tomcat for improved performance characteristics.

```java
Undertow server = Undertow.builder()
    .addHttpListener(8080, "localhost")
    .setHandler(Handlers.path()
        .addPrefixPath("/api", new HttpHandler() {
            public void handleRequest(HttpServerExchange exchange) {
                exchange.getResponseSender().send("Hello");
            }
        }))
    .build();
server.start();
```

## Servlet Support

Undertow implements the Servlet specification and supports JSP, WebSocket, and JSR-356. It provides high servlet performance while maintaining specification compliance.

## Reverse Proxy

Undertow includes a reverse proxy handler for forwarding requests to backend servers. It supports load balancing, health checks, and session affinity for upstream clusters.

## Configuration

Undertow configuration uses XML deployment descriptors or programmatic configuration. Buffer sizes, thread pools, and connector settings can be tuned for specific workload requirements.

## Performance Features

Features like direct ByteBuffer usage, zero-copy file serving, and HTTP/2 support contribute to Undertow's excellent performance. It handles static content and dynamic servlets with minimal overhead.

## Integration

Undertow integrates with WildFly, Spring Boot, and other frameworks. Its modular design allows embedding only required components for minimal footprint in microservice deployments.
