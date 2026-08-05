# C# Configuration

## appsettings.json

The primary configuration file for .NET applications.

```json
{
  "Logging": {
    "LogLevel": {
      "Default": "Information",
      "Microsoft": "Warning"
    }
  },
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=MyApp;Trusted_Connection=True;"
  },
  "AppSettings": {
    "MaxRetries": 3,
    "EnableFeature": true
  }
}
```

Environment-specific files override defaults:
- `appsettings.Development.json`
- `appsettings.Staging.json`
- `appsettings.Production.json`

## IConfiguration

```csharp
// Register in Program.cs
var builder = WebApplication.CreateBuilder(args);
var config = builder.Configuration;

// Read values
var connectionString = config.GetConnectionString("DefaultConnection");
var maxRetries = config.GetValue<int>("AppSettings:MaxRetries");
```

## Options Pattern

```csharp
// Strongly typed configuration
public class AppSettings
{
    public int MaxRetries { get; set; }
    public bool EnableFeature { get; set; }
}

// Register
builder.Services.Configure<AppSettings>(
    builder.Configuration.GetSection("AppSettings"));

// Inject
public class MyService
{
    private readonly AppSettings _settings;
    public MyService(IOptions<AppSettings> options)
    {
        _settings = options.Value;
    }
}

// Validate
builder.Services.AddOptions<AppSettings>()
    .Bind(builder.Configuration.GetSection("AppSettings"))
    .ValidateDataAnnotations()
    .ValidateOnStart();
```

## Configuration Sources

```csharp
// Multiple providers
builder.Configuration
    .AddJsonFile("appsettings.json")
    .AddEnvironmentVariables()
    .AddCommandLine(args)
    .AddUserSecrets<Program>()
    .AddAzureAppConfiguration();
```

## Environment Variables

```csharp
// Override any config with environment variables
// ConnectionStrings__DefaultConnection overrides appsettings
Environment.SetEnvironmentVariable("AppSettings__MaxRetries", "5");
```

## User Secrets (Development)

```bash
dotnet user-secrets init
dotnet user-secrets set "ApiKey" "secret-value"
```

## Validation

```csharp
public class AppSettings
{
    [Required]
    [Range(1, 100)]
    public int MaxRetries { get; set; }

    [Required]
    public string ApiKey { get; set; } = string.Empty;
}
```

## Hot Reload

Configuration changes can be detected at runtime using `IOptionsSnapshot<T>` for automatic reloading.
