# Windows Services Hosting

## Overview

Windows Services enable .NET applications to run as background processes without user interaction. They start automatically with the operating system and continue running independently of user sessions.

## Creating Windows Services

.NET provides Worker Service template and BackgroundService class for creating long-running services. The Worker Service template generates projects configured for Windows Service hosting.

```csharp
public class Worker : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            // Background work
            await Task.Delay(1000, stoppingToken);
        }
    }
}
```

## Service Installation

.NET services install using the sc.exe command-line tool or the Windows Service Control Manager. The PublishSingleFile option creates self-contained executables for deployment.

## Service Configuration

Services configure startup type (automatic, manual, delayed), failure recovery actions, and service accounts. Log On As settings determine the security context for service execution.

## Integration with IIS

IIS hosting is preferred for web applications due to process management and recycling features. Windows Services suit background processing, scheduled tasks, and non-HTTP workloads.

## Monitoring

Windows Services integrate with Windows Event Log for operational monitoring. Performance counters and custom logging provide additional visibility into service health and performance.

## Deployment

Services deploy using installers, PowerShell scripts, or deployment tools. The sc create command registers services, while sc delete removes them from the system.

## Modern Alternatives

Windows Container support and Azure services provide alternatives to traditional Windows Services. Worker Services can be containerized for consistent deployment across environments.
