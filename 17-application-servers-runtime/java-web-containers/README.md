# Java Web Containers Overview

## Overview

Java web containers provide the runtime environment for executing Java web applications based on the Servlet specification. They handle HTTP request processing, session management, and lifecycle management for servlets and JSP pages.

## Servlet Specification

The Servlet specification defines how web components interact with the container. Servlets handle HTTP requests, JSP pages generate dynamic content, and filters intercept request processing.

## Container Responsibilities

Web containers manage servlet lifecycle, thread pooling, session persistence, class loading, and security. They also provide JNDI lookup, JTA transaction support, and logging infrastructure.

## Deployment Units

Java web applications deploy as WAR (Web Application Archive) files containing servlets, JSP pages, static resources, libraries, and configuration descriptors. Exploded deployments are also supported.

## Class Loading

Web containers implement a hierarchical class loading model that separates application classes from container and system classes. This prevents classpath conflicts between applications.

## Common Containers

| Container | Type | Primary Use |
|-----------|------|-------------|
| Tomcat | Servlet container | Standalone web apps |
| Jetty | Embedded server | Embedded deployment |
| Undertow | High-performance | WildFly default |
| Resin | Full container | Enterprise apps |

## Configuration

Web applications configure through web.xml deployment descriptors or annotations. Container-specific configuration handles thread pools, connection limits, and resource allocation.

## Performance Considerations

Performance depends on thread pool configuration, connection handling, session management, and JVM tuning. Each container has specific optimization techniques for different workload patterns.
