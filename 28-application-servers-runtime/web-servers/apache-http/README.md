# Apache HTTP Server

## Overview

Apache HTTP Server is the world's most widely deployed web server, powering approximately 30% of all websites. Developed by the Apache Software Foundation, it has been continuously maintained since 1995.

## Architecture

Apache uses a multi-processing module (MPM) architecture with options for prefork, worker, or event-based processing. Each model handles concurrent connections differently to optimize for various workloads.

## Module System

Apache's modular architecture enables extending functionality through compiled modules. Common modules include mod_rewrite for URL manipulation, mod_ssl for HTTPS, and mod_proxy for reverse proxying.

## mod_rewrite

mod_rewrite provides powerful URL rewriting capabilities using regular expressions. It enables clean URLs, redirect logic, and content negotiation for web applications.

```apache
RewriteEngine On
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule ^(.*)$ index.php/$1 [L]
```

## Virtual Hosts

Apache supports name-based and IP-based virtual hosting. Virtual host configurations define document roots, server names, and directory-specific settings for each hosted domain.

## Directory Configuration

.htaccess files provide per-directory configuration without server restart. They control access, authentication, caching headers, and custom error pages for specific URL paths.

## Performance Tuning

Performance optimization includes adjusting MPM settings, enabling caching modules, configuring compression, and optimizing KeepAlive parameters for connection reuse.

## Security Hardening

Security configuration includes disabling directory listing, restricting access to sensitive files, implementing security headers, and configuring SSL/TLS with strong cipher suites.
