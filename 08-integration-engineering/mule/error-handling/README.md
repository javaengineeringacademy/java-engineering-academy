# MuleSoft / Mule - Error Handling

## Overview

Mule provides comprehensive error handling mechanisms to manage exceptions, retry failed operations, and ensure system reliability.

## Table of Contents

1. [Error Handling Basics](#error-handling-basics)
2. [Error Types](#error-types)
3. [On-Error Handlers](#on-error-handlers)
4. [Error Propagation](#error-propagation)
5. [Retry Policies](#retry-policies)
6. [Global Error Handling](#global-error-handling)
7. [Best Practices](#best-practices)

## Error Handling Basics

### Error Structure

```
┌─────────────────────────────────────┐
│              Error                  │
├─────────────────────────────────────┤
│  Type: VALIDATION                   │
│  Description: Invalid order         │
│  Cause: Exception                   │
│  Stack Trace: ...                   │
└─────────────────────────────────────┘
```

### Try-Scope

```xml
<flow name="error-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/api"/>
    <try>
        <flow-ref name="process-order"/>
        <error-handler>
            <on-error continue="true">
                <logger message="Error handled"/>
            </on-error>
        </error-handler>
    </try>
</flow>
```

## Error Types

### Mule Error Types

| Error Type | Description |
|------------|-------------|
| TRANSFORMATION | Data transformation errors |
| VALIDATION | Validation errors |
| EXPRESSION | Expression errors |
| CONNECTIVITY | Connection errors |
| TIMEOUT | Timeout errors |
| SECURITY | Security errors |
| UNKNOWN | Unknown errors |

### Custom Error Types

```xml
<!-- Raise custom error -->
<raise-error type="MY_CUSTOM_ERROR" description="Custom error occurred"/>

<!-- Catch custom error -->
<error-handler>
    <on-error type="MY_CUSTOM_ERROR">
        <logger message="Custom error: #[error.description]"/>
    </on-error>
</error-handler>
```

## On-Error Handlers

### On-Error Continue

```xml
<try>
    <flow-ref name="process-order"/>
    <error-handler>
        <on-error continue="true" type="VALIDATION">
            <logger message="Validation error"/>
            <set-payload value="Validation failed"/>
        </on-error>
    </error-handler>
</try>
```

### On-Error Propagate

```xml
<try>
    <flow-ref name="process-order"/>
    <error-handler>
        <on-error propagate="true" type="CONNECTIVITY">
            <logger message="Connection error"/>
            <set-payload value="Service unavailable"/>
        </on-error>
    </error-handler>
</try>
```

### On-Error Custom Handler

```xml
<try>
    <flow-ref name="process-order"/>
    <error-handler>
        <on-error type="MY_ERROR">
            <flow-ref name="handle-custom-error"/>
        </on-error>
    </error-handler>
</try>

<sub-flow name="handle-custom-error">
    <logger message="Custom error handler"/>
    <set-payload value="Custom error occurred"/>
</sub-flow>
```

## Error Propagation

### Error Propagation Rules

```
Error occurs
    │
    ├── On-Error Continue → Error handled, flow continues
    │
    ├── On-Error Propagate → Error propagated to caller
    │
    └── No handler → Error propagated to global handler
```

### Propagation Example

```xml
<flow name="outer-flow">
    <flow-ref name="inner-flow"/>
</flow>

<flow name="inner-flow">
    <try>
        <flow-ref name="process"/>
        <error-handler>
            <on-error propagate="true">
                <logger message="Error propagated"/>
            </on-error>
        </error-handler>
    </try>
</flow>
```

## Retry Policies

### Simple Retry

```xml
<try>
    <flow-ref name="external-call"/>
    <error-handler>
        <on-error type="CONNECTIVITY" continue="true">
            <retry policy="fixed" count="3" delay="1000"/>
        </on-error>
    </error-handler>
</try>
```

### Advanced Retry

```xml
<try>
    <flow-ref name="external-call"/>
    <error-handler>
        <on-error type="CONNECTIVITY" continue="true">
            <retry policy="exponential" 
                    count="5" 
                    delay="1000" 
                    maxDelay="10000"/>
        </on-error>
    </error-handler>
</try>
```

### Retry with Backoff

```xml
<retry policy="exponential"
       count="3"
       delay="1000"
       maxDelay="30000"
       backOffMultiplier="2"/>
```

## Global Error Handling

### Global Error Handler

```xml
<error-handler name="globalErrorHandler">
    <on-error type="VALIDATION">
        <logger message="Validation error"/>
        <set-payload value="Invalid input"/>
    </on-error>
    <on-error type="CONNECTIVITY">
        <logger message="Connection error"/>
        <set-payload value="Service unavailable"/>
    </on-error>
    <on-error type="ANY">
        <logger message="Unknown error: #[error.description]"/>
        <set-payload value="Internal server error"/>
    </on-error>
</error-handler>

<!-- Apply to flow -->
<flow name="my-flow" errorHandler="globalErrorHandler">
    <http:listener config-ref="HTTP_Listener_config" path="/api"/>
    <flow-ref name="process-order"/>
</flow>
```

### Error Handler Scope

```xml
<flow name="my-flow">
    <try errorHandler-ref="globalErrorHandler">
        <flow-ref name="process-order"/>
    </try>
</flow>
```

## Best Practices

1. **Use specific error types**: Catch specific errors
2. **Log errors**: Always log error details
3. **Return meaningful messages**: Provide user-friendly errors
4. **Handle timeouts**: Configure appropriate timeouts
5. **Use retry wisely**: Retry transient failures only
6. **Monitor errors**: Track error metrics
7. **Test error scenarios**: Test error handling paths
8. **Document errors**: Document error types and handling

## References

- [Mule Error Handling](https://docs.mulesoft.com/mule-runtime/)
- [Error Handling Best Practices](https://docs.mulesoft.com/)
