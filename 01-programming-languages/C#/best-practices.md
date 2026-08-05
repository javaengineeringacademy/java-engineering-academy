# C# Best Practices

## 1. Use Async/Await Correctly

```csharp
// Good - avoid async void
public async Task<int> GetDataAsync()
{
    return await _httpClient.GetAsync<int>("/api/data");
}

// Bad - fire and forget without control
public async void Button_Click() { }
```

## 2. Prefer Composition Over Inheritance

```csharp
// Good - compose behavior
public class OrderService
{
    private readonly IInventoryService _inventory;
    private readonly IPaymentService _payment;

    public OrderService(IInventoryService inventory, IPaymentService payment)
    {
        _inventory = inventory;
        _payment = payment;
    }
}
```

## 3. Use Dependency Injection

```csharp
// Register services in Program.cs
builder.Services.AddScoped<IOrderRepository, OrderRepository>();
builder.Services.AddScoped<IOrderService, OrderService>();
```

## 4. Validate Input Early

```csharp
public class OrderDto
{
    [Required]
    [StringLength(100)]
    public string CustomerName { get; set; } = string.Empty;

    [Range(1, 1000)]
    public int Quantity { get; set; }
}
```

## 5. Use Records for Immutable Data

```csharp
public record OrderDto(int Id, string Customer, decimal Total);
public record CreateOrderRequest(string Customer, List<OrderItemDto> Items);
```

## 6. Handle Exceptions Appropriately

```csharp
// Don't catch and swallow
try { await ProcessAsync(); }
catch (ValidationException ex) { return BadRequest(ex.Message); }
catch (Exception) { throw; } // Let middleware handle
```

## 7. Use Pattern Matching

```csharp
var result = input switch
{
    > 100 => "large",
    > 0 => "positive",
    0 => "zero",
    _ => "negative"
};
```

## 8. Prefer String Interpolation

```csharp
// Good
_logger.LogInformation("Order {OrderId} for {Customer}", order.Id, customer);

// Bad
_logger.LogInformation("Order " + order.Id + " for " + customer);
```

## 9. Use Nullable Reference Types

```csharp
#nullable enable
public string? GetName(int? id)
{
    return id?.ToString() ?? "unknown";
}
```

## 10. Dispose Resources Properly

```csharp
// Use using statements
using var connection = new SqlConnection(connectionString);
await connection.OpenAsync();
```

## 11. Use Collection Expressions

```csharp
// Modern C# syntax
int[] numbers = [1, 2, 3, 4, 5];
List<string> names = ["Alice", "Bob"];
```

## 12. Avoid Blocking Calls

```csharp
// Bad - blocks thread pool
var result = GetDataAsync().Result;

// Good - properly async
var result = await GetDataAsync();
```

## 13. Use Configuration for Environment Differences

```csharp
var connectionString = builder.Configuration
    .GetConnectionString("DefaultConnection");
```

## 14. Log Structured Data

```csharp
// Good - structured logging
_logger.LogInformation("Processed {Count} items in {Elapsed}ms", count, elapsed);

// Bad - string concatenation
_logger.LogInformation("Processed " + count + " items in " + elapsed + "ms");
```

## 15. Write Unit Tests

```csharp
public class OrderServiceTests
{
    [Fact]
    public async Task CreateOrder_ShouldReturnValidOrder()
    {
        var mockRepo = new Mock<IOrderRepository>();
        var service = new OrderService(mockRepo.Object);

        var result = await service.CreateOrderAsync(new CreateOrderRequest());

        Assert.NotNull(result);
        Assert.True(result.Id > 0);
    }
}
```
