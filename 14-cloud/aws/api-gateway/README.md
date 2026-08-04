# Amazon API Gateway

## Overview

Amazon API Gateway is a fully managed service for creating, publishing, and securing APIs at scale.

## API Types

| Type        | Protocol   | Use Case                    | Cost      |
|-------------|------------|-----------------------------|-----------|
| REST API    | REST       | Complex APIs                | Higher    |
| HTTP API    | HTTP/2     | Serverless, simple APIs     | Lower     |
| WebSocket  | WebSocket  | Real-time apps              | Moderate  |

## REST API

### Create REST API
```bash
# Create API
aws apigateway create-rest-api \
  --name my-api \
  --description "My REST API" \
  --endpoint-configuration types=REGIONAL

# Create resource
aws apigateway create-resource \
  --rest-api-id abc123def456 \
  --parent-id abc123def456 \
  --path-part "users"

# Create method
aws apigateway put-method \
  --rest-api-id abc123def456 \
  --resource-id xyz789 \
  --http-method GET \
  --authorization-type NONE

# Set up Lambda integration
aws apigateway put-integration \
  --rest-api-id abc123def456 \
  --resource-id xyz789 \
  --http-method GET \
  --type AWS_PROXY \
  --integration-http-method POST \
  --uri arn:aws:apigateway:us-east-1:lambda:path/2015-03-31/functions/arn:aws:lambda:us-east-1:123456789012:function:getUsers/invocations
```

### Deployment
```bash
# Deploy API
aws apigateway create-deployment \
  --rest-api-id abc123def456 \
  --stage-name prod \
  --stage-description "Production"
```

## HTTP API

### Create HTTP API
```bash
# Create HTTP API
aws apigatewayv2 create-api \
  --name my-http-api \
  --protocol-type HTTP

# Create route
aws apigatewayv2 create-route \
  --api-id abc123def456 \
  --route-key "GET /users"

# Create integration
aws apigatewayv2 create-integration \
  --api-id abc123def456 \
  --integration-type AWS_PROXY \
  --integration-uri arn:aws:lambda:us-east-1:123456789012:function:getUsers
```

## WebSocket API

### Create WebSocket API
```bash
# Create WebSocket API
aws apigatewayv2 create-api \
  --name my-websocket-api \
  --protocol-type WEBSOCKET \
  --route-selection-expression "$request.body.action"

# Create route
aws apigatewayv2 create-route \
  --api-id abc123def456 \
  --route-key "connect" \
  --route-response-selection-expression "$default"

# Create integration
aws apigatewayv2 create-integration \
  --api-id abc123def456 \
  --integration-type AWS_PROXY \
  --integration-uri arn:aws:lambda:us-east-1:123456789012:function:handleConnect
```

## Authentication

### Cognito Authorizer
```bash
# Create Cognito authorizer
aws apigateway create-authorizer \
  --rest-api-id abc123def456 \
  --name my-cognito-authorizer \
  --type COGNITO_USER_POOLS \
  --provider-arns "arn:aws:cognito-idp:us-east-1:123456789012:userpool/us-east-1_xxxxx"
```

### Lambda Authorizer
```bash
# Create Lambda authorizer
aws apigateway create-authorizer \
  --rest-api-id abc123def456 \
  --name my-lambda-authorizer \
  --type TOKEN \
  --authorizer-uri arn:aws:apigateway:us-east-1:lambda:path/2015-03-31/functions/arn:aws:lambda:us-east-1:123456789012:function:authorize/invocations \
  --authorizer-result-ttl-in-seconds 300
```

### IAM Authorization
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "execute-api:Invoke",
      "Resource": "arn:aws:execute-api:us-east-1:123456789012:abc123def456/prod/GET/users"
    }
  ]
}
```

## Request/Response Transformations

### Request Mapping
```json
{
  "application/json": {
    "userId": "$input.path('$.userId')",
    "name": "$input.path('$.name')"
  }
}
```

### Response Mapping
```json
{
  "application/json": {
    "id": "$input.path('$.body.id')",
    "message": "Success"
  }
}
```

## Usage Plans & API Keys

```bash
# Create usage plan
aws apigateway create-usage-plan \
  --name my-usage-plan \
  --throttle burstLimit=100,rateLimit=50 \
  --quota limit=1000,period=MONTH

# Create API key
aws apigateway create-api-key \
  --name my-api-key \
  --enabled

# Associate API key with usage plan
aws apigateway create-usage-plan-key \
  --usage-plan-id abc123def456 \
  --key-id xyz789 \
  --key-type API_KEY
```

## Throttling

```bash
# Set method throttling
aws apigateway update-method \
  --rest-api-id abc123def456 \
  --resource-id xyz789 \
  --http-method GET \
  --patch-operations \
    op=replace,path=/throttling/rateLimit,value=100 \
    op=replace,path=/throttling/burstLimit,value=50
```

## CORS Configuration

```bash
# Enable CORS
aws apigateway update-method \
  --rest-api-id abc123def456 \
  --resource-id xyz789 \
  --http-method OPTIONS \
  --patch-operations \
    op=replace,path=/methodResponses/status/200/responseParameters/access-control-allow-methods,value=GET,OPTIONS \
    op=replace,path=/methodResponses/status/200/responseParameters/access-control-allow-headers,value=Content-Type \
    op=replace,path=/methodResponses/status/200/responseParameters/access-control-allow-origin,value=*
```

## Custom Domain Names

```bash
# Create custom domain
aws apigateway create-domain-name \
  --domain-name api.example.com \
  --regional-certificate-arn arn:aws:acm:us-east-1:123456789012:certificate/abc123

# Map base path
aws apigateway create-base-path-mapping \
  --domain-name api.example.com \
  --rest-api-id abc123def456 \
  --stage prod
```

## Request Validation

```json
{
  "RequestValidator": {
    "ValidateRequestBody": true,
    "ValidateRequestParameters": true
  }
}
```

## WAF Integration

```bash
# Associate WAF with API
aws wafv2 associate-web-acl \
  --web-acl-arn arn:aws:wafv2:us-east-1:123456789012:regional/webacl/my-webacl/abc123 \
  --resource-arn arn:aws:apigateway:us-east-1::/restapis/abc123def456/stages/prod
```

## Monitoring

```bash
# Get API metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/ApiGateway \
  --metric-name Count \
  --dimensions Name=ApiName,Value=my-api \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Sum
```

## Cost Optimization

- **HTTP APIs** are 70% cheaper than REST APIs
- Use **cached responses** to reduce calls
- Implement **usage plans** to control costs
- Use **regional endpoints** for lower latency

## Best Practices

1. **Use HTTP APIs** for serverless workloads
2. **Implement caching** for frequent requests
3. **Use WAF** for security
4. **Set up usage plans** for rate limiting
5. **Enable logging** to CloudWatch
6. **Use custom domains** for branding
7. **Implement request validation**
8. **Use throttling** to protect backends
9. **Monitor with CloudWatch** metrics
10. **Use stages** for deployments
