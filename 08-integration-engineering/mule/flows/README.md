# MuleSoft / Mule - Flows

## Overview

Flows are the core building blocks in Mule applications. They define the sequence of message processing steps.

## Table of Contents

1. [Flow Types](#flow-types)
2. [Main Flows](#main-flows)
3. [Sub-Flows](#sub-flows)
4. [Choice Routing](#choice-routing)
5. [Error Handlers](#error-handlers)
6. [Async Processing](#async-processing)
7. [Flow Configuration](#flow-configuration)

## Flow Types

### Main Flow

- Has a source (entry point)
- Processes messages
- Can be invoked externally

### Sub-Flow

- No source
- Invoked by other flows
- Reusable components

### Async Flow

- Processes messages asynchronously
- Fire-and-forget pattern
- Parallel processing

## Main Flows

### HTTP Listener Flow

```xml
<flow name="order-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/api/orders"/>
    <logger message="Received order"/>
    <transform-message>
        <ee:set-payload>
            <![CDATA[
                %dw 2.0
                output application/json
                ---
                {
                    orderId: uuid(),
                    status: "RECEIVED"
                }
            ]]>
        </ee:set-payload>
    </transform-message>
    <http:request config-ref="HTTP_Request_config" path="/process"/>
</flow>
```

### Scheduler Flow

```xml
<flow name="scheduled-flow">
    <scheduler>
        <scheduling-strategy>
            <fixed-frequency frequency="5" timeUnit="SECONDS"/>
        </scheduling-strategy>
    </scheduler>
    <logger message="Scheduled execution"/>
    <db:select config-ref="Database_Config">
        <sql>SELECT * FROM orders WHERE status = 'PENDING'</sql>
    </db:select>
</flow>
```

### File Source Flow

```xml
<flow name="file-flow">
    <file:listener config-ref="File_Listener_config" 
                   directory="/input" 
                   autoDelete="true"
                   watermark="0"/>
    <logger message="File received: #[attributes.fileName]"/>
    <file:read path="/input" outputMimeType="text/plain"/>
</flow>
```

## Sub-Flows

### Basic Sub-Flow

```xml
<sub-flow name="validate-order">
    <logger message="Validating order: #[payload]"/>
    <choice>
        <when expression="#[payload.items.isEmpty()]">
            <raise-error type="VALIDATION" description="No items"/>
        </when>
        <otherwise>
            <logger message="Order is valid"/>
        </otherwise>
    </choice>
</sub-flow>

<!-- Usage -->
<flow name="main-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/orders"/>
    <flow-ref name="validate-order"/>
    <logger message="Order processed"/>
</flow>
```

### Sub-Flow with Parameters

```xml
<sub-flow name="process-item">
    <logger message="Processing item: #[payload]"/>
    <transform-message>
        <ee:set-payload>
            <![CDATA[
                %dw 2.0
                output application/json
                ---
                {
                    id: payload.id,
                    processed: true,
                    timestamp: now()
                }
            ]]>
        </ee:set-payload>
    </transform-message>
</sub-flow>
```

## Choice Routing

### Basic Choice

```xml
<flow name="routing-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/route"/>
    <choice>
        <when expression="#[payload.type == 'ORDER']">
            <flow-ref name="process-order"/>
        </when>
        <when expression="#[payload.type == 'INVOICE']">
            <flow-ref name="process-invoice"/>
        </when>
        <otherwise>
            <flow-ref name="process-default"/>
        </otherwise>
    </choice>
</flow>
```

### Choice with DataWeave

```xml
<choice>
    <when expression="#[payload.total > 1000]">
        <flow-ref name="high-value-processing"/>
    </when>
    <when expression="#[payload.total > 100]">
        <flow-ref name="medium-value-processing"/>
    </when>
    <otherwise>
        <flow-ref name="low-value-processing"/>
    </otherwise>
</choice>
```

### Scatter-Gather

```xml
<flow name="scatter-gather-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/aggregate"/>
    <scatter-gather>
        <route>
            <http:request config-ref="HTTP_Request_config" path="/service1"/>
        </route>
        <route>
            <http:request config-ref="HTTP_Request_config" path="/service2"/>
        </route>
        <route>
            <http:request config-ref="HTTP_Request_config" path="/service3"/>
        </route>
    </scatter-gather>
    <transform-message>
        <ee:set-payload>
            <![CDATA[
                %dw 2.0
                output application/json
                ---
                {
                    results: payload.*$
                }
            ]]>
        </ee:set-payload>
    </transform-message>
</flow>
```

## Error Handlers

### On-Error Continue

```xml
<flow name="error-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/api"/>
    <try>
        <flow-ref name="process-order"/>
        <error-handler>
            <on-error continue="true">
                <logger message="Error: #[error.description]"/>
                <set-payload value="Error occurred"/>
            </on-error>
        </error-handler>
    </try>
</flow>
```

### On-Error Propagate

```xml
<try>
    <flow-ref name="process-order"/>
    <error-handler>
        <on-error propagate="true" type="VALIDATION">
            <logger message="Validation error"/>
            <set-payload value="Validation failed"/>
        </on-error>
        <on-error propagate="false">
            <logger message="Other error"/>
        </on-error>
    </error-handler>
</try>
```

### Global Error Handler

```xml
<configuration-properties file="application.properties"/>

<error-handler name="globalErrorHandler">
    <on-error propagate="true" type="ANY">
        <logger message="Global error: #[error.description]"/>
        <set-payload value="Internal server error"/>
    </on-error>
</error-handler>
```

## Async Processing

### Async Block

```xml
<flow name="async-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/async"/>
    <set-payload value="Request received"/>
    <async>
        <logger message="Starting async processing"/>
        <flow-ref name="long-running-task"/>
        <logger message="Async processing completed"/>
    </async>
</flow>
```

### Async with Queue

```xml
<flow name="producer-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/produce"/>
    <logger message="Producing message"/>
    <flow-ref name="process-message"/>
</flow>

<flow name="consumer-flow">
    <scheduler>
        <scheduling-strategy>
            <fixed-frequency frequency="1" timeUnit="SECONDS"/>
        </scheduling-strategy>
    </scheduler>
    <logger message="Consuming messages"/>
    <flow-ref name="process-message"/>
</flow>
```

## Flow Configuration

### Flow Attributes

```xml
<flow name="my-flow" 
      processingStrategy="synchronous"
      initialState="started">
    <!-- Flow components -->
</flow>
```

### Processing Strategies

```xml
<!-- Synchronous (default) -->
<flow processingStrategy="synchronous">

<!-- Asynchronous -->
<flow processingStrategy="asynchronous">
```

### Flow References

```xml
<!-- Simple reference -->
<flow-ref name="sub-flow"/>

<!-- Conditional reference -->
<flow-ref name="#[payload.type == 'A' ? 'flowA' : 'flowB']"/>
```

## Best Practices

1. **Use sub-flows**: Promote reuse
2. **Error handling**: Configure error handlers
3. **Logging**: Add appropriate logging
4. **Async**: Use async for long operations
5. **Testing**: Test flows independently
6. **Documentation**: Document flow purpose
7. **Monitoring**: Track flow metrics
8. **Security**: Secure entry points

## References

- [Mule Flows](https://docs.mulesoft.com/mule-runtime/)
- [Flow Reference](https://docs.mulesoft.com/mule-sdk/)
