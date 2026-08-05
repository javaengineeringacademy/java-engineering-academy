# Apache Tomcat

## Overview

Apache Tomcat is the most widely used Java servlet container, implementing the Servlet, JSP, and WebSocket specifications. It serves as both a standalone web server and an embedded component in larger applications.

## Architecture

Tomcat uses a hierarchical component model with Server, Service, Connector, Engine, Host, and Context elements. Each component manages specific aspects of request processing and application hosting.

## Connectors

Tomcat supports multiple connectors for different protocols. The NIO connector provides non-blocking I/O for high concurrency, while the APR connector uses native libraries for maximum performance.

## Deployment

Tomcat deploys WAR files to the webapps directory or uses the Manager app for remote deployment. Context XML files provide application-specific configuration for database resources and environment variables.

## Configuration

Key configuration files include server.xml for global settings, context.xml for application defaults, and web.xml for servlet configuration. JNDI resources are configured in context files.

## Performance Tuning

Performance optimization includes adjusting maxThreads, minSpareThreads, connection timeouts, and JVM memory settings. The NIO connector with appropriate thread pool sizing handles most workloads effectively.

## Security

Tomcat implements Java EE security with authentication realms, authorization constraints, and SSL/TLS support. The Security Manager restricts application access to system resources.

## Embedded Tomcat

Tomcat can be embedded in applications using the TomcatEmbed library. Spring Boot uses embedded Tomcat by default for standalone application deployment.
