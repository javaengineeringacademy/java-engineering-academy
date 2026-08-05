# Caucho Resin

## Overview

Caucho Resin is a high-performance Java application server and servlet container developed by Caucho Technology. It provides fast servlet/JSP execution with additional enterprise features including clustering and Hessian web services.

## Architecture

Resin uses a custom HTTP server with native I/O optimizations. It implements the Servlet specification while providing performance enhancements beyond standard container capabilities.

## Hessian Web Services

Resin includes Hessian, a binary web service protocol that provides efficient RPC communication between Java applications. Hessian offers faster serialization than SOAP for internal service communication.

## Clustering

Resin supports session replication and distributed caching across cluster nodes. It uses multicast discovery or static configuration for cluster membership and session synchronization.

## Configuration

Resin uses XML configuration files (resin.xml) defining clusters, servers, web applications, and resource factories. Configuration supports environment-specific overrides and variable substitution.

## Performance

Resin emphasizes fast startup, low memory usage, and high throughput. Its custom HTTP implementation and optimized JSP compilation contribute to excellent performance metrics.

## Open Source and Commercial

Resin is available in both open-source (GPL) and commercial editions. The commercial edition adds management features, commercial support, and additional clustering capabilities.

## Modern Usage

While Resin maintains an active user base, it has less market share than Tomcat or Jetty. Organizations using Resin benefit from its performance characteristics and Hessian web services for internal communication.
