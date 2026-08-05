# NGINX

## Overview

NGINX is a high-performance web server and reverse proxy known for its event-driven architecture and efficient resource utilization. It handles concurrent connections using minimal memory while delivering excellent throughput.

## Architecture

NGINX uses an asynchronous, event-driven architecture with a master process and multiple worker processes. Each worker handles thousands of connections simultaneously without thread overhead.

## Reverse Proxy

NGINX excels as a reverse proxy, forwarding requests to upstream application servers. It supports load balancing algorithms including round-robin, least connections, and IP hash.

```nginx
upstream backend {
    server app1:8080;
    server app2:8080;
    server app3:8080;
}

server {
    listen 80;
    
    location / {
        proxy_pass http://backend;
    }
}
```

## Load Balancing

NGINX provides multiple load balancing methods with health checks, session persistence, and weighted distribution. It automatically removes failed upstream servers from rotation.

## SSL/TLS Configuration

NGINX handles SSL termination with support for modern TLS protocols and cipher suites. It can manage multiple SSL certificates for different domains on the same IP address.

## Caching

NGINX can cache responses from upstream servers, reducing load on application servers and improving response times for frequently accessed content.

## WebSocket Support

NGINX supports WebSocket connections through the upgrade mechanism, enabling real-time communication for modern web applications while maintaining reverse proxy functionality.

## Performance Features

Features like sendfile, TCP_NODELAY, tcp_nopush, and worker_connections tuning enable NGINX to handle millions of concurrent connections with minimal resource consumption.
