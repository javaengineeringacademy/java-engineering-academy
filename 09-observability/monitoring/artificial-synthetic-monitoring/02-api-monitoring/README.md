# API Monitoring

## Overview

API monitoring validates that REST, GraphQL, and gRPC endpoints respond correctly, within expected latency bounds, and with proper data structures. It is a critical component of synthetic monitoring for modern applications.

---

## REST API Monitoring

### Health Check Pattern

```yaml
# Synthetic monitor configuration
api_monitors:
  - name: "User API Health"
    endpoint: https://api.example.com/v1/users
    method: GET
    headers:
      Authorization: "Bearer ${SYNTHETIC_TOKEN}"
      Accept: "application/json"
    expected:
      status_code: 200
      response_time_ms: 500
      json_schema:
        type: object
        required: ["data", "meta"]
    assertions:
      - field: "data.length"
        operator: "gte"
        value: 1
    schedule: "*/5 * * * *"
```

### Validation Patterns

```python
import requests
import json
from jsonschema import validate, ValidationError

class APIMonitor:
    def __init__(self, base_url, auth_token=None):
        self.base_url = base_url
        self.headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
        if auth_token:
            self.headers['Authorization'] = f'Bearer {auth_token}'
    
    def check_endpoint(self, endpoint, method='GET', payload=None, 
                       expected_status=200, schema=None):
        """Perform API endpoint check."""
        url = f"{self.base_url}{endpoint}"
        
        try:
            response = requests.request(
                method, url,
                headers=self.headers,
                json=payload,
                timeout=30
            )
            
            result = {
                'status': response.status_code,
                'duration_ms': response.elapsed.total_seconds() * 1000,
                'size_bytes': len(response.content),
                'success': response.status_code == expected_status
            }
            
            # Validate JSON schema if provided
            if schema and response.status_code == 200:
                try:
                    data = response.json()
                    validate(instance=data, schema=schema)
                    result['schema_valid'] = True
                except (json.JSONDecodeError, ValidationError) as e:
                    result['schema_valid'] = False
                    result['schema_error'] = str(e)
                    result['success'] = False
            
            return result
            
        except requests.exceptions.RequestException as e:
            return {
                'status': 0,
                'duration_ms': 0,
                'success': False,
                'error': str(e)
            }
    
    def check_crud(self, resource_path, create_payload, test_id=None):
        """Perform complete CRUD operation check."""
        results = {}
        
        # CREATE
        results['create'] = self.check_endpoint(
            resource_path, 'POST', create_payload, 201
        )
        
        # READ
        results['read'] = self.check_endpoint(
            f"{resource_path}/{test_id}", 'GET', expected_status=200
        )
        
        # UPDATE
        update_payload = {**create_payload, 'updated': True}
        results['update'] = self.check_endpoint(
            f"{resource_path}/{test_id}", 'PUT', update_payload, 200
        )
        
        # DELETE
        results['delete'] = self.check_endpoint(
            f"{resource_path}/{test_id}", 'DELETE', expected_status=204
        )
        
        return results
```

---

## GraphQL API Monitoring

### Query-Based Health Check

```javascript
// GraphQL synthetic monitor
const healthQuery = {
  query: `
    query HealthCheck {
      systemHealth {
        status
        uptime
        version
        dependencies {
          name
          status
          latencyMs
        }
      }
    }
  `
};

const checkGraphQLHealth = async (endpoint, token) => {
  const start = Date.now();
  
  try {
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(healthQuery)
    });
    
    const data = await response.json();
    const duration = Date.now() - start;
    
    // Check for GraphQL errors
    if (data.errors && data.errors.length > 0) {
      throw new Error(`GraphQL errors: ${JSON.stringify(data.errors)}`);
    }
    
    // Validate health status
    const health = data.data.systemHealth;
    if (health.status !== 'healthy') {
      throw new Error(`System unhealthy: ${health.status}`);
    }
    
    // Check dependencies
    const unhealthyDeps = health.dependencies.filter(d => d.status !== 'healthy');
    if (unhealthyDeps.length > 0) {
      throw new Error(`Unhealthy dependencies: ${unhealthyDeps.map(d => d.name).join(', ')}`);
    }
    
    return {
      success: true,
      duration,
      health,
      dependencies: health.dependencies
    };
    
  } catch (error) {
    return {
      success: false,
      duration: Date.now() - start,
      error: error.message
    };
  }
};
```

### Schema Validation

```python
import graphql
from graphql.utilities import build_ast_schema
from graphql.language import parse

class GraphQLMonitor:
    def __init__(self, endpoint, schema_sdl=None):
        self.endpoint = endpoint
        self.schema = None
        if schema_sdl:
            self.schema = build_ast_schema(parse(schema_sdl))
    
    def validate_query(self, query_string):
        """Validate a query against the schema."""
        if not self.schema:
            return {'valid': False, 'error': 'No schema loaded'}
        
        try:
            document = parse(query_string)
            errors = graphql.validate(self.schema, document)
            
            if errors:
                return {
                    'valid': False,
                    'errors': [str(e) for e in errors]
                }
            
            return {'valid': True}
            
        except graphql.GraphQLError as e:
            return {
                'valid': False,
                'errors': [str(e)]
            }
    
    def execute_query(self, query, variables=None):
        """Execute a GraphQL query."""
        import requests
        
        payload = {'query': query}
        if variables:
            payload['variables'] = variables
        
        response = requests.post(
            self.endpoint,
            json=payload,
            headers={'Content-Type': 'application/json'},
            timeout=30
        )
        
        return response.json()
```

---

## gRPC Monitoring

### Health Check Service

```protobuf
syntax = "proto3";
package grpc.health.v1;

service Health {
  rpc Check(HealthCheckRequest) returns (HealthCheckResponse);
  rpc Watch(HealthCheckRequest) returns (stream HealthCheckResponse);
}

message HealthCheckRequest {
  string service = 1;
}

message HealthCheckResponse {
  enum ServingStatus {
    UNKNOWN = 0;
    SERVING = 1;
    NOT_SERVING = 2;
    SERVICE_UNKNOWN = 3;
  }
  ServingStatus status = 1;
}
```

### gRPC Client Monitor

```python
import grpc
import time
from concurrent.futures import TimeoutError

class GRPCMonitor:
    def __init__(self, target, credentials=None):
        if credentials:
            self.channel = grpc.secure_channel(target, credentials)
        else:
            self.channel = grpc.insecure_channel(target)
    
    def check_health(self, service='', timeout=5):
        """Check gRPC service health."""
        start = time.time()
        
        try:
            # Wait for channel to be ready
            grpc.channel_ready_future(self.channel).result(timeout=timeout)
            
            # Import generated stubs
            from grpc_health.v1 import health_pb2, health_pb2_grpc
            
            stub = health_pb2_grpc.HealthStub(self.channel)
            request = health_pb2.HealthCheckRequest(service=service)
            
            response = stub.Check(request, timeout=timeout)
            duration = (time.time() - start) * 1000
            
            return {
                'success': response.status == 1,  # SERVING
                'duration_ms': duration,
                'status': response.status
            }
            
        except TimeoutError:
            return {
                'success': False,
                'duration_ms': (time.time() - start) * 1000,
                'error': 'Health check timeout'
            }
        except grpc.RpcError as e:
            return {
                'success': False,
                'duration_ms': (time.time() - start) * 1000,
                'error': f'gRPC error: {e.code().name}'
            }
    
    def check_reflection(self, timeout=5):
        """Check if gRPC reflection is available."""
        try:
            from grpc_reflection.v1alpha import reflection_pb2, reflection_pb2_grpc
            
            stub = reflection_pb2_grpc.ServerReflectionStub(self.channel)
            request = reflection_pb2.ServerReflectionRequest(
                list_services=""
            )
            
            responses = list(stub.ServerReflectionInfo(
                iter([request]), timeout=timeout
            ))
            
            return {
                'success': True,
                'services': [r.list_services_response.service for r in responses 
                           if r.HasField('list_services_response')]
            }
        except Exception as e:
            return {
                'success': False,
                'error': str(e)
            }
```

---

## Alert Configuration

### API-Specific Alerts

```yaml
groups:
  - name: api_monitoring
    rules:
      # High error rate
      - alert: APIHighErrorRate
        expr: |
          rate(http_requests_total{status=~"5.."}[5m]) / 
          rate(http_requests_total[5m]) > 0.05
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High API error rate"
          description: "Error rate is {{ $value | humanizePercentage }}"
      
      # Slow responses
      - alert: APISlowResponse
        expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "Slow API responses"
          description: "P95 latency is {{ $value }}s"
      
      # Endpoint down
      - alert: APIEndpointDown
        expr: up{job="api"} == 0
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "API endpoint is down"
```

---

## Best Practices

1. **Validate Response Structure:** Don't just check status codes; validate response schemas
2. **Monitor All HTTP Methods:** Check GET, POST, PUT, DELETE as appropriate
3. **Test Authentication:** Verify token refresh and expiration handling
4. **Check Rate Limiting:** Ensure rate limits are properly enforced
5. **Monitor Dependencies:** Track upstream service health
6. **Set Realistic Thresholds:** Base thresholds on actual performance data
7. **Log Request/Response:** Capture details for debugging failures
8. **Use Connection Pooling:** Reuse connections in monitors to reduce overhead

---

## Next Steps

- [Browser Monitoring](../03-browser-monitoring/README.md) - Browser-based synthetic checks
- [Load Testing](../04-load-testing/README.md) - Load testing as monitoring
- [Alerting Thresholds](../06-alerting-thresholds/README.md) - Setting up alerts
