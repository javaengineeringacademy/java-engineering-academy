# C# Scaling Strategies

## Horizontal Scaling

Run multiple instances behind a load balancer.

### Stateless Services

```csharp
// Store session state externally
builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

// Use Redis for distributed session
builder.Services.AddStackExchangeRedisCache(options =>
{
    options.Configuration = "localhost:6379";
    options.InstanceName = "session_";
});
```

### Load Balancing

| Algorithm | Description | Use Case |
|-----------|-------------|----------|
| Round Robin | Sequential distribution | Equal-weight servers |
| Least Connections | Fewest active connections | Varying request times |
| IP Hash | Consistent server per IP | Session affinity |
| Weighted | Capacity-based distribution | Heterogeneous servers |

## Microservices Architecture

```csharp
// Service registration
builder.Services.AddGrpc();
builder.Services.AddHttpClient("inventory", client =>
{
    client.BaseAddress = new Uri("http://inventory-service:5000");
});

// Circuit breaker
builder.Services.AddHttpClient("inventory")
    .AddPolicyHandler(Policy<HttpResponseMessage>
        .Handle<HttpRequestException>()
        .CircuitBreakerAsync(3, TimeSpan.FromSeconds(30)));
```

## Caching Strategies

```csharp
// In-memory caching
builder.Services.AddMemoryCache();

// Distributed caching
builder.Services.AddStackExchangeRedisCache(options =>
{
    options.Configuration = builder.Configuration["Redis:ConnectionString"];
});

// Response caching
builder.Services.AddResponseCaching();
app.UseResponseCaching();

[ResponseCache(Duration = 60)]
public IActionResult GetData() => Ok(data);
```

## Database Scaling

```csharp
// Connection pooling
builder.Services.AddDbContext<AppDbContext>(options =>
{
    options.UseSqlServer(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        sqlOptions =>
        {
            sqlOptions.CommandTimeout(30);
            sqlOptions.EnableRetryOnFailure(3);
        });
});

// Read replicas
builder.Services.AddDbContext<ReadOnlyDbContext>(options =>
{
    options.UseSqlServer(
        builder.Configuration.GetConnectionString("ReadOnlyConnection"));
});
```

## Message Queues

```csharp
// Background processing with hosted services
public class OrderProcessingService : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            var message = await _queue.DequeueAsync(stoppingToken);
            if (message != null)
            {
                await ProcessMessage(message);
            }
        }
    }
}
```

## Auto-Scaling Rules

```yaml
# Azure example
autoscale:
  minimum: 2
  maximum: 10
  rules:
    - metric: CPU
      threshold: 70
      action: scale-out
    - metric: CPU
      threshold: 30
      action: scale-in
```
