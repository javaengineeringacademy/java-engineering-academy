# MuleSoft / Mule ESB - Fundamentals

## Overview

MuleSoft is a lightweight ESB and integration platform that enables connecting applications, data, and devices through APIs. Mule runtime handles message processing, routing, and transformation.

## Table of Contents

1. [What is Mule](#what-is-mule)
2. [Architecture](#architecture)
3. [Mule Applications](#mule-applications)
4. [Flows](#flows)
5. [Message Processing](#message-processing)
6. [Anypoint Studio](#anypoint-studio)
7. [First Application](#first-application)

## What is Mule

Mule is an integration platform that:

- Connects disparate systems
- Transforms data between formats
- Routes messages based on content
- Handles errors gracefully
- Supports API-led connectivity

## Architecture

### Mule Runtime

```
┌─────────────────────────────────────────────┐
│              Mule Runtime                    │
├─────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │   Flow   │  │   Flow   │  │   Flow   │ │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘ │
│       │              │              │       │
│  ┌────▼─────┐  ┌────▼─────┐  ┌────▼─────┐ │
│  │Connector │  │Connector │  │Connector │ │
│  └──────────┘  └──────────┘  └──────────┘ │
└─────────────────────────────────────────────┘
```

### Component Types

| Component | Description |
|-----------|-------------|
| Source | Entry point (Listener, Scheduler) |
| Processor | Message processing (Transform, Filter) |
| Connector | System connectivity (HTTP, DB, JMS) |
| Error Handler | Error handling (On-Error) |

## Mule Applications

### Application Structure

```
my-app/
├── src/
│   └── main/
│       └── mule/
│           ├── my-flow.xml
│           └── my-config.xml
├── pom.xml
└── mule-deploy.properties
```

### Mule Configuration

```xml
<mule xmlns="http://www.mulesoft.org/schema/mule/core"
      xmlns:http="http://www.mulesoft.org/schema/mule/http"
      xmlns:db="http://www.mulesoft.org/schema/mule/db">
    
    <http:listener-config name="HTTP_Listener_config">
        <http:listener host="0.0.0.0" port="8081" path="/api"/>
    </http:listener-config>
    
    <db:config name="Database_Config">
        <db:derby-config driverClassName="org.apache.derby.jdbc.EmbeddedDriver"/>
    </db:config>
</mule>
```

## Flows

### Main Flow

```xml
<flow name="my-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/orders"/>
    <logger message="Received request: #[payload]"/>
    <set-variable variableName="orderId" value="#[uuid()]"/>
    <transform-message>
        <ee:set-payload resource="transform/order-transform.dwl"/>
    </transform-message>
    <http:request config-ref="HTTP_Request_config" path="/api/orders" method="POST"/>
    <logger message="Response: #[payload]"/>
</flow>
```

### Sub-Flow

```xml
<sub-flow name="process-order">
    <logger message="Processing order: #[payload]"/>
    <choice>
        <when expression="#[payload.type == 'EXPRESS']">
            <flow-ref name="express-processing"/>
        </when>
        <otherwise>
            <flow-ref name="standard-processing"/>
        </otherwise>
    </choice>
</sub-flow>

<sub-flow name="express-processing">
    <logger message="Express processing"/>
</sub-flow>

<sub-flow name="standard-processing">
    <logger message="Standard processing"/>
</sub-flow>
```

### Async Flow

```xml
<flow name="async-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/async"/>
    <async>
        <logger message="Async processing started"/>
        <flow-ref name="process-order"/>
        <logger message="Async processing completed"/>
    </async>
    <set-payload value="Request received"/>
</flow>
```

## Message Processing

### Message Structure

```
┌─────────────────────────────────────┐
│             Message                 │
├─────────────────────────────────────┤
│  Payload: { "orderId": "123" }      │
│  Attributes:                        │
│    - HTTP Method: POST              │
│    - HTTP Path: /api/orders         │
│  Variables:                         │
│    - orderId: uuid                  │
└─────────────────────────────────────┘
```

### Message Operations

```xml
<!-- Set payload -->
<set-payload value="#[payload * 2]"/>

<!-- Set variable -->
<set-variable variableName="count" value="#[payload.size()]"/>

<!-- Set attribute -->
<set-attributes>
    <http:response status="200"/>
</set-attributes>

<!-- Remove variable -->
<remove-variable variableName="temp"/>
```

### Transform Message

```xml
<transform-message>
    <ee:set-payload>
        <![CDATA[
            %dw 2.0
            output application/json
            ---
            {
                orderId: uuid(),
                items: payload.items map (item) -> {
                    id: item.id,
                    quantity: item.quantity,
                    price: item.price
                },
                total: payload.items sum $.price
            }
        ]]>
    </ee:set-payload>
</transform-message>
```

## Anypoint Studio

### Development Environment

- Visual flow designer
- Drag-and-drop components
- DataWeave transformation
- Debugging tools
- Testing framework

### Key Features

| Feature | Description |
|---------|-------------|
| Flow Designer | Visual flow creation |
| DataWeave | Data transformation |
| Debugger | Step-through debugging |
| Testing | Unit and integration tests |
| Deployment | Cloud and on-premises |

## First Application

### HTTP Listener Flow

```xml
<flow name="hello-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/hello"/>
    <set-payload value="Hello World!"/>
</flow>
```

### With Transformation

```xml
<flow name="order-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/api/orders"/>
    <logger message="Received: #[payload]"/>
    <transform-message>
        <ee:set-payload>
            <![CDATA[
                %dw 2.0
                output application/json
                ---
                {
                    orderId: uuid(),
                    status: "RECEIVED",
                    items: payload.items
                }
            ]]>
        </ee:set-payload>
    </transform-message>
    <http:request config-ref="HTTP_Request_config" 
                  path="/api/orders" method="POST"/>
</flow>
```

## Best Practices

1. **Use sub-flows**: Reuse logic with sub-flows
2. **Error handling**: Configure error handlers
3. **Logging**: Add appropriate logging
4. **Testing**: Write unit tests
5. **Documentation**: Document flows
6. **Security**: Secure endpoints
7. **Performance**: Optimize processing
8. **Monitoring**: Track flow metrics

## References

- [MuleSoft Documentation](https://docs.mulesoft.com/)
- [Anypoint Studio](https://docs.mulesoft.com/studio/)
