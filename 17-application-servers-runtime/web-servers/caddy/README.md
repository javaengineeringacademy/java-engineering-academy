# Caddy Web Server

## Overview

Caddy is a modern web server written in Go that emphasizes automatic HTTPS, ease of use, and security. It automatically obtains and renews SSL/TLS certificates from Let's Encrypt with zero configuration.

## Automatic HTTPS

Caddy automatically provisions SSL/TLS certificates for all configured domains. It handles certificate renewal, OCSP stapling, and redirects HTTP to HTTPS without manual certificate management.

## Configuration Format

Caddy uses a simple Caddyfile format for configuration. The configuration syntax is human-readable and focuses on clarity over verbosity.

```
example.com {
    root * /var/www/html
    file_server
    
    reverse_proxy /api/* localhost:3000
    
    log {
        output file /var/log/caddy/access.log
    }
}
```

## Reverse Proxy

Caddy provides reverse proxying with load balancing, health checks, and circuit breaking. It supports HTTP/2, gRPC, and WebSocket proxying to backend services.

## Dynamic Configuration

Caddy supports dynamic configuration updates through its API without requiring server restarts. This enables runtime changes to routing, TLS configuration, and backend definitions.

## Modular Architecture

Caddy is built from modular components that can be combined and extended. The xcaddy build tool creates custom Caddy binaries with specific module combinations.

## Security Defaults

Caddy enforces security best practices by default including HTTPS, security headers, and modern TLS configuration. This reduces the attack surface without requiring explicit security configuration.

## Use Cases

Caddy is ideal for small to medium deployments, development environments, and situations where automatic certificate management reduces operational overhead significantly.
