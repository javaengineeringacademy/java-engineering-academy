# C# Debugging

## Visual Studio Debugger

### Breakpoints

| Type | Shortcut | Purpose |
|------|----------|---------|
| Standard | F9 | Pause execution |
| Conditional | Right-click | Break when condition is true |
| Hit Count | Right-click | Break after N hits |
| Tracepoint | Right-click | Log without pausing |

### Debug Window

- **Watch**: Monitor variables and expressions
- **Locals**: Current scope variables
- **Autos**: Current and previous lines
- **Call Stack**: Method invocation chain
- **Threads**: Thread inspection
- **Exceptions**: Configure exception handling

## VS Code Debugging

```json
// .vscode/launch.json
{
  "version": "0.2.0",
  "configurations": [
    {
      "name": "Launch .NET",
      "type": "coreclr",
      "request": "launch",
      "program": "${workspaceFolder}/bin/Debug/net8.0/MyApp.dll",
      "args": [],
      "cwd": "${workspaceFolder}",
      "stopAtEntry": false,
      "env": {
        "ASPNETCORE_ENVIRONMENT": "Development"
      }
    }
  ]
}
```

## Logging for Debugging

```csharp
// Debug output
System.Diagnostics.Debug.WriteLine("Entering method");

// Trace logging
Trace.TraceInformation("Processing order {OrderId}", orderId);

// Structured logging
_logger.LogDebug("Processing {@Order}", order);
```

## Diagnostic Tools

```bash
# dotnet-dump - collect and analyze dumps
dotnet-dump collect -p <pid>
dotnet-dump analyze <dumpfile>

# dotnet-trace - collect traces
dotnet-trace collect -p <pid>

# dotnet-counters - real-time metrics
dotnet-counters monitor -p <pid>
```

## Exception Settings

### First Chance Exceptions

```csharp
// Log all first chance exceptions
AppDomain.CurrentDomain.FirstChanceException += (sender, args) =>
{
    Console.WriteLine($"First chance: {args.Exception.Message}");
};
```

### Global Exception Handling

```csharp
// For unhandled exceptions
AppDomain.CurrentDomain.UnhandledException += (sender, args) =>
{
    var exception = args.ExceptionObject as Exception;
    _logger.LogCritical(exception, "Unhandled exception");
};
```

## Memory Debugging

```bash
# Analyze memory usage
dotnet-counters monitor -p <pid> \
    --counters System.Runtime,Microsoft.AspNetCore.Hosting

# Memory snapshot comparison
dotnet-dump collect -p <pid>
```

## Remote Debugging

```csharp
// Enable in Program.cs for development
if (builder.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
}
```

## Hot Reload

```bash
# During development
dotnet watch

# Visual Studio: Ctrl+Shift+F5 to restart
# VS Code: Restart debugging session
```
