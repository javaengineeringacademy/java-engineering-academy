# Azure Functions

## Overview

Azure Functions is a serverless compute service for event-driven applications.

## Runtime Versions

| Runtime    | Use Case              |
|------------|------------------------|
| .NET 8     | .NET applications      |
| Node.js 20 | JavaScript apps        |
| Python 3.11| General purpose        |
| Java 17    | Enterprise apps        |
| PowerShell | Automation             |

## Creating Functions

### Azure CLI
```bash
# Create function app
az functionapp create \
  --resource-group myResourceGroup \
  --name myFunctionApp \
  --storage-account mystorageaccount \
  --consumption-plan-location eastus \
  --runtime dotnet \
  --runtime-version 8 \
  --functions-version 4

# Deploy function
az functionapp deployment source config-zip \
  --resource-group myResourceGroup \
  --name myFunctionApp \
  --src ./function-app.zip
```

### ARM Template
```json
{
  "type": "Microsoft.Web/sites",
  "apiVersion": "2022-09-01",
  "name": "myFunctionApp",
  "location": "eastus",
  "kind": "functionapp",
  "properties": {
    "siteConfig": {
      "appSettings": [
        {
          "name": "FUNCTIONS_WORKER_RUNTIME",
          "value": "dotnet"
        }
      ]
    }
  }
}
```

## Trigger Types

| Trigger              | Use Case                    |
|----------------------|-----------------------------|
| HTTP                 | REST API, webhooks          |
| Blob Storage         | File processing             |
| Queue Storage        | Message processing          |
| Timer                | Scheduled tasks             |
| Event Hub            | Event streaming             |
| Service Bus          | Enterprise messaging        |
| Cosmos DB            | Database changes            |
| Event Grid           | Event-driven apps           |

### HTTP Trigger
```csharp
[Function("HttpTrigger")]
public static HttpResponseData Run(
    [HttpTrigger(AuthorizationLevel.Anonymous, "get", "post")] HttpRequestData req)
{
    var response = req.CreateResponse(HttpStatusCode.OK);
    return response;
}
```

### Blob Trigger
```csharp
[Function("BlobTrigger")]
public static void Run(
    [BlobTrigger("my-container/{name}")] Stream myBlob, string name)
{
    Console.WriteLine($"Processing blob\n Name:{name}");
}
```

### Queue Trigger
```csharp
[Function("QueueTrigger")]
public static void Run(
    [QueueTrigger("myqueue")] string message)
{
    Console.WriteLine($"Processing message: {message}");
}
```

### Timer Trigger
```csharp
[Function("TimerTrigger")]
public static void Run(
    [TimerTrigger("0 */5 * * * *")] TimerInfo timer)
{
    Console.WriteLine($"Timer triggered at {DateTime.Now}");
}
```

## Function Hosting Plans

### Consumption Plan
```bash
# Create consumption plan
az functionapp create \
  --resource-group myResourceGroup \
  --name myFunctionApp \
  --storage-account mystorageaccount \
  --consumption-plan-location eastus
```

### Premium Plan
```bash
# Create premium plan
az functionapp create \
  --resource-group myResourceGroup \
  --name myFunctionApp \
  --storage-account mystorageaccount \
  --plan myPremiumPlan \
  --runtime dotnet
```

### Dedicated Plan
```bash
# Create dedicated plan
az appservice plan create \
  --resource-group myResourceGroup \
  --name myDedicatedPlan \
  --sku B1

az functionapp create \
  --resource-group myResourceGroup \
  --name myFunctionApp \
  --storage-account mystorageaccount \
  --plan myDedicatedPlan
```

## Durable Functions

### Function Chaining
```csharp
[Function("Chaining")]
public static async Task<string> RunOrchestrator(
    [OrchestrationTrigger] TaskOrchestrationContext context)
{
    var result = await context.CallActivityAsync<string>("SayHello", "Tokyo");
    result = await context.CallActivityAsync<string>("SayHello", "Seattle");
    result = await context.CallActivityAsync<string>("SayHello", "London");
    return result;
}
```

### Fan-out/Fan-in
```csharp
[Function("FanOutFanIn")]
public static async Task<int> RunOrchestrator(
    [OrchestrationTrigger] TaskOrchestrationContext context)
{
    var tasks = new List<Task<int>>();
    for (int i = 0; i < 100; i++)
    {
        tasks.Add(context.CallActivityAsync<int>("ProcessItem", i));
    }
    await Task.WhenAll(tasks);
    return tasks.Sum(t => t.Result);
}
```

### Monitor Pattern
```csharp
[Function("Monitor")]
public static async Task RunOrchestrator(
    [OrchestrationTrigger] TaskOrchestrationContext context)
{
    while (true)
    {
        var jobStatus = await context.CallActivityAsync<string>("CheckJobStatus");
        if (jobStatus == "Completed")
            break;
        await context.CreateTimer(context.CurrentUtcDateTime.AddSeconds(5), CancellationToken.None);
    }
}
```

## Bindings

### Input Bindings
```csharp
[Function("InputBinding")]
public static void Run(
    [QueueTrigger("myqueue")] string message,
    [Blob("mycontainer/{queueTrigger}")] string blobContent)
{
    Console.WriteLine($"Message: {message}, Blob: {blobContent}");
}
```

### Output Bindings
```csharp
[Function("OutputBinding")]
public static void Run(
    [HttpTrigger] HttpRequestData req,
    [Queue("outputqueue")] ICollector<string> outputQueue)
{
    outputQueue.Add("Message from function");
}
```

## Configuration

```bash
# Set app settings
az functionapp config appsettings set \
  --resource-group myResourceGroup \
  --name myFunctionApp \
  --settings "MySetting=value"

# Get connection strings
az functionapp config connection-string list \
  --resource-group myResourceGroup \
  --name myFunctionApp
```

## Monitoring

```bash
# Get function logs
az functionapp log tail \
  --resource-group myResourceGroup \
  --name myFunctionApp

# Get function metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Web/sites/myFunctionApp \
  --metric "FunctionExecutionCount"
```

## Cost Optimization

- **Use consumption plan** for variable workloads
- **Implement proper retry policies**
- **Use durable functions** for orchestration
- **Monitor execution counts**

## Best Practices

1. **Use appropriate trigger** for use case
2. **Implement proper error handling**
3. **Use durable functions** for workflows
4. **Implement proper security**
5. **Use managed identities**
6. **Monitor with Application Insights**
7. **Implement proper logging**
8. **Use bindings** appropriately
9. **Optimize memory usage**
10. **Regular function reviews**
