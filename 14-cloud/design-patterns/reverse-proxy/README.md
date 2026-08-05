# Reverse Proxy Pattern

## Overview

The Reverse Proxy pattern places a proxy server between clients and backend services. The proxy receives client requests, forwards them to appropriate backend servers, and returns responses. It provides load balancing, SSL termination, caching, security, and request routing while hiding the backend topology from clients.

## When to Use

- Load balancing across multiple backend instances
- SSL/TLS termination for backend services
- Caching static content and API responses
- Providing security layers and DDoS protection
- Routing requests based on URL paths or headers
- Hiding backend server details from clients

## Implementation

### AWS
- Application Load Balancer (ALB)
- Network Load Balancer (NLB)
- CloudFront as edge reverse proxy
- API Gateway as managed reverse proxy

### Azure
- Azure Application Gateway
- Azure Front Door
- Azure CDN for edge caching
- Azure Load Balancer

### Google Cloud
- Cloud Load Balancing
- Cloud CDN for content caching
- Cloud Armor for security
- Cloud Run with built-in proxy

### Open Source
- Nginx - High-performance reverse proxy
- HAProxy - TCP/HTTP load balancer
- Traefik - Cloud-native reverse proxy
- Caddy - Automatic HTTPS reverse proxy
- Envoy - Modern L7 proxy

## Best Practices

1. Configure appropriate health checks for backend servers
2. Implement connection pooling for backend efficiency
3. Use SSL/TLS termination to offload encryption
4. Configure request timeouts appropriate to backend capabilities
5. Implement rate limiting at the proxy level
6. Monitor proxy performance as critical infrastructure
7. Use sticky sessions only when necessary

## Interview Questions

1. What is the difference between a reverse proxy and a load balancer?
2. How do you handle WebSocket connections through a reverse proxy?
3. Describe strategies for reverse proxy high availability.
4. How would you implement request caching at the proxy level?
5. What security hardening should be applied to a reverse proxy?

## References

- Nginx Documentation - https://nginx.org
- HAProxy Documentation - https://www.haproxy.org
- Traefik Documentation - https://traefik.io
- Envoy Proxy Documentation
- Cloud Design Patterns - Microsoft Azure Architecture Center
- High Performance Browser Networking - Ilya Grigorik
