# C# Monitoring

## App Metrics

```csharp
// Register metrics
var metrics = AppMetrics.CreateDefaultBuilder()
    .Configuration.Configure(new MetricsOptions
    {
        GlobalTags = new GlobalTags { { "env", "production" } }
    })
    .Build();

builder.Services.AddMetrics(metrics);
builder.Services.AddMetricsTrackingMiddleware();
builder.Services.AddMetricsEndpoints();

// Custom metrics
public class OrderMetrics
{
    private readonly ICounter _ordersCreated;
    private readonly IHistogram _orderValues;

    public OrderMetrics(IMetrics metrics)
    {
        _ordersCreated = metrics.Registry.Counter("orders.created");
        _orderValues = metrics.Registry.Histogram("orders.values",
            new UniformReservoir());
    }

    public void OrderCreated(decimal value)
    {
        _ordersCreated.Increment();
        _orderValues.Update((long)value);
    }
}
```

## Serilog Structured Logging

```csharp
// Configuration
builder.Host.UseSerilog((context, config) =>
{
    config
        .MinimumLevel.Information()
        .Enrich.FromLogContext()
        .Enrich.WithMachineName()
        .Enrich.WithThreadId()
        .WriteTo.Console()
        .WriteTo.Seq("http://localhost:5341")
        .WriteTo.File("logs/log-.txt", rollingInterval: RollingInterval.Day);
});

// Usage
public class OrderService
{
    private readonly ILogger<OrderService> _logger;

    public async Task<Order> CreateOrderAsync(CreateOrderRequest request)
    {
        _logger.LogInformation("Creating order for {CustomerId}", request.CustomerId);

        try
        {
            var order = await ProcessOrder(request);
            _logger.LogInformation("Order {OrderId} created successfully", order.Id);
            return order;
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to create order for {CustomerId}",
                request.CustomerId);
            throw;
        }
    }
}
```

## Application Insights

```csharp
builder.Services.AddApplicationInsightsTelemetry(options =>
{
    options.ConnectionString = builder.Configuration["ApplicationInsights:ConnectionString"];
});

// Custom telemetry
public class CustomTelemetry
{
    private readonly TelemetryClient _telemetry;

    public void TrackBusinessEvent(string name, IDictionary<string, string> props)
    {
        _-telemetry.TrackEvent(name, props);
    }

    public void TrackDependency(string name, TimeSpan duration, bool success)
    {
        _telemetry.TrackDependency("HTTP", name, duration, success);
    }
}
```

## Health Checks

```csharp
builder.Services.AddHealthChecks()
    .AddSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")!)
    .AddRedis("localhost:6379")
    .AddCheck<CustomHealthCheck>("custom");

app.MapHealthChecks("/health");
app.MapHealthChecks("/health/ready", new HealthCheckOptions
{
    Predicate = check => check.Tags.Contains("ready")
});
```

## OpenTelemetry

```csharp
builder.Services.AddOpenTelemetry()
    .WithTracing(b => b
        .AddAspNetCoreInstrumentation()
        .AddHttpClientInstrumentation()
        .AddJaegerExporter())
    .WithMetrics(b => b
        .AddRuntimeInstrumentation()
        .AddPrometheusExporter());
```
