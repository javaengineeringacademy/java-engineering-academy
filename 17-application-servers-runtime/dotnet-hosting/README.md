# .NET Hosting Overview

## Overview

.NET hosting platforms provide the runtime environment for executing .NET applications. From traditional IIS hosting to modern containerized deployments, multiple options exist for deploying .NET applications.

## Hosting Models

.NET supports in-process hosting, out-of-process hosting, and self-hosting models. Each model offers different trade-offs for performance, flexibility, and operational complexity.

## IIS Hosting

Internet Information Services (IIS) is the traditional hosting platform for ASP.NET applications. It provides process management, security, and integration with Windows authentication systems.

## Kestrel

Kestrel is the cross-platform web server included with ASP.NET Core. It handles HTTP requests directly and is typically deployed behind a reverse proxy like NGINX or IIS.

## Self-Hosting

.NET applications can self-host HTTP servers using Kestrel or custom listeners. This enables console applications, Windows Services, and Docker containers to serve HTTP without external web servers.

## Cloud Hosting

Azure App Service provides managed hosting with auto-scaling, deployment slots, and integrated DevOps. AWS Elastic Beanstalk and Google Cloud Run offer similar managed hosting capabilities.

## Windows Services

.NET applications can run as Windows Services for background processing and long-running operations. They integrate with Windows Service Control Manager for lifecycle management.

## Container Deployment

.NET applications package as Docker containers for consistent deployment. The official Microsoft Docker images provide runtime environments for various .NET versions.

## Performance Considerations

Performance depends on hosting model, web server configuration, and application architecture. Kestrel provides excellent raw performance, while IIS adds features like request filtering and compression.
