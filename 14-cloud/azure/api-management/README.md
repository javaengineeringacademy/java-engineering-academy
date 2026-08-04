# Azure API Management (APIM)

## Overview

Azure API Management is a hybrid, multi-cloud API gateway for managing APIs.

## Components

| Component       | Description                    |
|-----------------|--------------------------------|
| Gateway         | API gateway                    |
| Developer Portal| API documentation              |
| Management API  | Control plane API              |

## Creating APIM

### Azure CLI
```bash
# Create APIM instance
az apim create \
  --resource-group myResourceGroup \
  --name myapim \
  --location eastus \
  --publisher-email admin@example.com \
  --publisher-name "My Organization" \
  --sku-name Developer
```

### ARM Template
```json
{
  "type": "Microsoft.ApiManagement/service",
  "apiVersion": "2022-08-01",
  "name": "myapim",
  "location": "eastus",
  "sku": {
    "name": "Developer",
    "capacity": 1
  }
}
```

## API Import

```bash
# Import from OpenAPI
az apim api import \
  --resource-group myResourceGroup \
  --service-name myapim \
  --api-id myapi \
  --path api \
  --specification-format OpenApiJson \
  --specification-url https://petstore3.swagger.io/api/v3/openapi.json

# Import from WSDL
az apim api import \
  --resource-group myResourceGroup \
  --service-name myapim \
  --api-id myapi \
  --path api \
  --specification-format Wadl \
  --specification-url https://example.com/api?wsdl
```

## Policies

### Inbound Policies
```xml
<policies>
    <inbound>
        <base />
        <set-header name="X-Custom-Header" exists-action="override">
            <value>custom-value</value>
        </set-header>
        <rate-limit calls="10" renewal-period="60" />
    </inbound>
</policies>
```

### Outbound Policies
```xml
<policies>
    <outbound>
        <base />
        <set-header name="X-Powered-By" exists-action="delete" />
    </outbound>
</policies>
```

### Error Policies
```xml
<policies>
    <on-error>
        <base />
        <set-status code="500" reason="Internal Server Error" />
    </on-error>
</policies>
```

## Products

```bash
# Create product
az apim product create \
  --resource-group myResourceGroup \
  --service-name myapim \
  --product-id myproduct \
  --product-name "My Product" \
  --state published \
  --subscription-required true

# Add API to product
az apim product-api add \
  --resource-group myResourceGroup \
  --service-name myapim \
  --product-id myproduct \
  --api-id myapi
```

## Rate Limiting

```xml
<policies>
    <inbound>
        <base />
        <rate-limit calls="100" renewal-period="60" />
        <rate-limit-by-key calls="10" renewal-period="60" key="@(context.Request.Headers.GetValueOrDefault("Authorization", "anonymous"))" />
    </inbound>
</policies>
```

## Authentication

### OAuth 2.0
```xml
<policies>
    <inbound>
        <base />
        <oauth2-authorization-server
            authorize-endpoint="https://login.microsoftonline.com/{tenant}/oauth2/authorize"
            token-endpoint="https://login.microsoftonline.com/{tenant}/oauth2/token"
            client-id="{client-id}"
            client-secret="{client-secret}"
            authorization-grant-type="client_credentials" />
    </inbound>
</policies>
```

### JWT Validation
```xml
<policies>
    <inbound>
        <base />
        <validate-jwt
            header-name="Authorization"
            failed-validation-httpcode="401"
            failed-validation-error-message="Unauthorized">
            <openid-config url="https://login.microsoftonline.com/{tenant}/.well-known/openid-configuration" />
            <required-claims>
                <claim name="aud" match="all">
                    <value>api://my-api</value>
                </claim>
            </required-claims>
        </validate-jwt>
    </inbound>
</policies>
```

## Transformations

```xml
<policies>
    <inbound>
        <base />
        <set-body template="liquid">
{
    "data": "@(JObject.Parse(context.Request.Body))",
    "timestamp": "@(DateTime.Now)"
}
        </set-body>
    </inbound>
</policies>
```

## Developer Portal

```bash
# Enable developer portal
az apim show \
  --resource-group myResourceGroup \
  --name myapim \
  --query "developerPortalUrl"
```

## Monitoring

```bash
# Get APIM metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.ApiManagement/service/myapim \
  --metric "TotalRequests"

# Get logs
az monitor diagnostic-settings create \
  --name mylogs \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.ApiManagement/service/myapim \
  --logs '[{"category":"GatewayLogs","enabled":true}]'
```

## Cost Optimization

- **Use appropriate SKUs**
- **Implement caching**
- **Use consumption tier** for dev/test
- **Monitor API usage**

## Best Practices

1. **Implement proper policies**
2. **Use OAuth 2.0** for authentication
3. **Implement rate limiting**
4. **Use caching** for performance
5. **Monitor with Azure Monitor**
6. **Implement proper logging**
7. **Use developer portal** for documentation
8. **Implement proper versioning**
9. **Use products** for organization
10. **Regular security reviews**
