# IIS for .NET

## Overview

Internet Information Services (IIS) serves as the primary hosting platform for ASP.NET applications on Windows. It provides process management, security integration, and advanced web server features for enterprise deployments.

## ASP.NET Hosting Models

IIS hosts ASP.NET through in-process (direct hosting) or out-of-process (reverse proxy to Kestrel) models. In-process hosting provides better performance by eliminating inter-process communication.

## Application Pools

Application pools isolate .NET applications into separate worker processes. Each pool runs under its own identity with configurable recycling, resource limits, and health monitoring.

## Configuration

IIS uses web.config files for ASP.NET configuration. The configuration system supports inheritance, environment-specific overrides, and lock/unlock at different configuration levels.

## URL Rewrite

IIS URL Rewrite module enables request routing, redirects, and content rewriting. Rules support pattern matching, conditions, and server variables for complex routing logic.

## Security Features

IIS provides Windows Authentication, forms authentication, IP restrictions, request filtering, and SSL/TLS management. Security features integrate with Active Directory for enterprise authentication.

## Performance Features

Output caching, dynamic compression (gzip/deflate), static compression, and kernel-mode caching optimize response times. Connection throttling prevents resource exhaustion.

## Management

IIS Manager provides GUI administration, while PowerShell cmdlets and appcmd.exe enable automation. Web Deploy handles application packaging and deployment across environments.

## Monitoring

IIS provides performance counters, logging, and Failed Request Tracing for monitoring application health and diagnosing issues in production environments.
