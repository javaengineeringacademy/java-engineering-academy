# Internet Information Services (IIS)

## Overview

Internet Information Services is Microsoft's web server for Windows platforms. It provides integrated hosting for ASP.NET applications, static content, and web services with deep Windows ecosystem integration.

## Architecture

IIS uses a modular architecture with a kernel-mode HTTP listener (http.sys) and user-mode worker processes. Application pools isolate web applications for reliability and security.

## ASP.NET Integration

IIS hosts ASP.NET applications through the ISAPI filter or the integrated pipeline. The integrated pipeline allows managed code to participate in all stages of request processing.

## Application Pools

Application pools separate web applications into independent worker processes. Each pool has its own identity, recycling schedule, and resource limits, preventing one application from affecting others.

## Module System

IIS uses native and managed modules for request processing. Common modules include URL Rewrite, Windows Authentication, Output Caching, and Compression modules.

## Configuration

IIS uses XML-based configuration files (web.config) for application settings. Configuration can be inherited from parent directories and overridden at any level.

## Management Tools

IIS Manager provides graphical administration, while PowerShell cmdlets and appcmd.exe enable scripting and automation. IIS also integrates with Visual Studio for development and debugging.

## Performance Features

Features like output caching, dynamic compression, kernel-mode caching, and connection throttling optimize performance for both static and dynamic content delivery.
