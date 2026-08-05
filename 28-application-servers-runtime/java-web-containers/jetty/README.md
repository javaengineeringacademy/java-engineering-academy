# Eclipse Jetty

## Overview

Eclipse Jetty is a lightweight, embeddable Java servlet container known for its flexibility and small footprint. It provides HTTP server and servlet container functionality that can be embedded in Java applications.

## Embedded Deployment

Jetty excels as an embedded server within Java applications. Its modular architecture allows selective inclusion of only required components, minimizing application size and startup time.

```java
Server server = new Server(8080);
ServletContextHandler context = new ServletContextHandler(server, "/");
context.addServlet(new ServletHolder(new MyServlet()), "/api/*");
server.start();
```

## Architecture

Jetty uses a handler-based architecture where requests flow through a chain of handlers. Handlers process requests, generate responses, or delegate to other handlers in the chain.

## Asynchronous I/O

Jetty supports asynchronous servlet processing and non-blocking I/O. This enables efficient handling of long-polling, streaming, and WebSocket connections without blocking threads.

## WebSocket Support

Jetty provides comprehensive WebSocket support for both client and server applications. It supports the JSR-356 WebSocket API and its own native WebSocket implementation.

## Configuration

Jetty configuration can be done programmatically, through XML files, or using properties files. This flexibility supports both embedded deployment and standalone server operation.

## Deployment

Jetty Deployer supports hot deployment of WAR files and web applications. The deployment scanner monitors directories for changes and automatically deploys or undeploys applications.

## Performance

Jetty delivers excellent performance for both HTTP and WebSocket workloads. Its small memory footprint and fast startup make it ideal for cloud deployments and microservices.
