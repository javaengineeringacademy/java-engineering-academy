# Kestrel Web Server

## Overview

Kestrel is the cross-platform web server included with ASP.NET Core. It handles HTTP/1.1, HTTP/2, and HTTP/3 requests directly, providing high-performance request processing without external web server dependencies.

## Architecture

Kestrel uses a libuv-based or socket-based transport layer for handling connections. The connection handler pipeline processes HTTP requests through middleware before reaching application code.

## Deployment Patterns

Kestrel typically deploys behind a reverse proxy (NGINX, IIS, Apache) in production. The reverse proxy handles SSL termination, request buffering, and static file serving.

```csharp
var builder = WebApplication.CreateBuilder(args);
builder.WebHost.UseKestrel(options =>
{
    options.ListenLocalhost(5000);
    options.ListenAnyIP(5001, listenOptions =>
    {
        listenOptions.UseHttps();
    });
});
```

## Configuration

Kestrel configuration includes endpoint definitions, limits, and transport settings. Configuration can be done in code, appsettings.json, or command-line arguments.

## Performance Tuning

Performance optimization includes adjusting maximum concurrent connections, request body size limits, keep-alive timeouts, and thread pool settings for high-throughput scenarios.

## HTTP/2 and HTTP/3

Kestrel supports HTTP/2 for multiplexed connections and HTTP/3 for improved performance on unreliable networks. Both protocols provide better resource utilization than HTTP/1.1.

## HTTPS Configuration

Kestrel supports HTTPS with certificate configuration through code, configuration files, or developer certificates. It handles TLS termination directly when not deployed behind a reverse proxy.

## Limitations

Kestrel is designed as a leaf server, not a public-facing web server. It lacks features like request filtering, URL rewriting, and advanced security that reverse proxies provide.
