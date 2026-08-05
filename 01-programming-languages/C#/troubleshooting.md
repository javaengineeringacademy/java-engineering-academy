# C# Troubleshooting

## Build Errors

### NuGet Restore Failures

```bash
# Clear NuGet cache
dotnet nuget locals all --clear

# Force restore
dotnet restore --force

# Check package sources
dotnet nuget list source
```

### Version Conflicts

```bash
# Check dependency tree
dotnet list package --include-transitive

# Find outdated packages
dotnet list package --outdated
```

## Runtime Errors

### Port Already in Use

```bash
# Find process using port
lsof -i :5000
netstat -tlnp | grep 5000

# Kill process
kill -9 <PID>
```

### Configuration Issues

```csharp
// Debug configuration
foreach (var provider in config.Providers)
{
    Console.WriteLine($"{provider}: loaded={provider.TryGet("key", out var value)}");
}
```

### Dependency Injection Failures

```csharp
// Register all services properly
// Missing registration causes InvalidOperationException
builder.Services.AddScoped<IMyService, MyService>();
builder.Services.AddScoped<IMyRepository, MyRepository>();
```

## Performance Issues

### High Memory Usage

```bash
# Capture memory snapshot
dotnet-dump collect -p <pid>

# Analyze
dotnet-dump analyze <dumpfile>
> dumpheap -stat
> gcroot <address>
```

### Slow Response Times

```bash
# Collect trace
dotnet-trace collect -p <pid> --duration 00:00:30

# Analyze with SpeedScope
dotnet-trace convert trace.nettrace --format SpeedScope
```

## Connection Issues

### Database Connection

```csharp
// Test connection string
using var connection = new SqlConnection(connectionString);
await connection.OpenAsync();
Console.WriteLine($"Server version: {connection.ServerVersion}");
```

### HttpClient Timeouts

```csharp
// Configure timeouts
builder.Services.AddHttpClient("api", client =>
{
    client.Timeout = TimeSpan.FromSeconds(30);
});
```

## Logging Debug

### Enable Verbose Logging

```json
// appsettings.json
{
  "Logging": {
    "LogLevel": {
      "Default": "Debug",
      "Microsoft": "Information",
      "Microsoft.Hosting.Lifetime": "Trace"
    }
  }
}
```

### View Logs in Real Time

```bash
# Windows
Get-Content -Path logs/log-*.txt -Wait

# macOS/Linux
tail -f logs/log-*.txt
```

## Common Exceptions

| Exception | Cause | Solution |
|-----------|-------|----------|
| NullReferenceException | Null dereference | Null checks |
| InvalidOperationException | Invalid state | Check preconditions |
| TaskCanceledException | Timeout | Increase timeout |
| SqlException | Database error | Check connection/query |
| HttpRequestException | Network error | Check endpoint/retry |
