# MuleSoft / Mule - Connectors

## Overview

Connectors enable Mule applications to connect to external systems. They provide sources (inbound) and operations (outbound) for data exchange.

## Table of Contents

1. [Connector Architecture](#connector-architecture)
2. [Built-in Connectors](#built-in-connectors)
3. [HTTP Connector](#http-connector)
4. [Database Connector](#database-connector)
5. [File Connector](#file-connector)
6. [JMS Connector](#jms-connector)
7. [Custom Connectors](#custom-connectors)

## Connector Architecture

### Connector Types

| Type | Description |
|------|-------------|
| Source | Entry point (Listener) |
| Operation | Outbound action |
| Request-Reply | Two-way communication |

### Connector Structure

```
┌─────────────────────────────────────┐
│           Connector                 │
├─────────────────────────────────────┤
│  Config: Connection settings        │
│  Source: Listener configuration     │
│  Operations: Available actions      │
└─────────────────────────────────────┘
```

## Built-in Connectors

### Available Connectors

| Connector | Description |
|-----------|-------------|
| HTTP | REST/SOAP APIs |
| Database | SQL databases |
| File | File system |
| JMS | Messaging |
| FTP/SFTP | File transfer |
| Email | SMTP/IMAP |
| SMTP | Send emails |
| IMAP | Receive emails |
| Web Service Consumer | SOAP services |

## HTTP Connector

### Listener Configuration

```xml
<http:listener-config name="HTTP_Listener_config">
    <http:listener host="0.0.0.0" port="8081" path="/api"/>
</http:listener-config>

<flow name="http-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/orders"/>
    <logger message="Received: #[payload]"/>
</flow>
```

### Request Configuration

```xml
<http:request-config name="HTTP_Request_config">
    <http:request-connection host="api.example.com" port="443" protocol="HTTPS"/>
</http:request-config>

<flow name="http-request-flow">
    <http:listener config-ref="HTTP_Listener_config" path="/proxy"/>
    <http:request config-ref="HTTP_Request_config" 
                  path="/api/orders" 
                  method="GET"/>
</flow>
```

### HTTP Operations

```xml
<!-- GET -->
<http:request config-ref="HTTP_Request_config" 
              path="/api/orders" 
              method="GET"/>

<!-- POST -->
<http:request config-ref="HTTP_Request_config" 
              path="/api/orders" 
              method="POST"
              contentType="application/json"/>

<!-- PUT -->
<http:request config-ref="HTTP_Request_config" 
              path="/api/orders/123" 
              method="PUT"/>

<!-- DELETE -->
<http:request config-ref="HTTP_Request_config" 
              path="/api/orders/123" 
              method="DELETE"/>
```

## Database Connector

### Configuration

```xml
<db:config name="Database_Config">
    <db:derby-config driverClassName="org.apache.derby.jdbc.EmbeddedDriver"/>
</db:config>

<!-- Or with connection pool -->
<db:config name="Database_Pooled">
    <db:my-sql-config host="localhost" port="3306" database="orders"/>
    <db:pooling-profile maxPoolSize="10" minPoolSize="2"/>
</db:config>
```

### Database Operations

```xml
<!-- Select -->
<db:select config-ref="Database_Config">
    <sql>SELECT * FROM orders WHERE status = 'PENDING'</sql>
</db:select>

<!-- Insert -->
<db:insert config-ref="Database_Config">
    <sql>INSERT INTO orders (id, status) VALUES (#[payload.id], 'NEW')</sql>
</db:insert>

<!-- Update -->
<db:update config-ref="Database_Config">
    <sql>UPDATE orders SET status = 'PROCESSED' WHERE id = #[payload.id]</sql>
</db:update>

<!-- Delete -->
<db:delete config-ref="Database_Config">
    <sql>DELETE FROM orders WHERE id = #[payload.id]</sql>
</db:delete>

<!-- Stored Procedure -->
<db:stored-procedure config-ref="Database_Config">
    <sql>CALL processOrder(#[payload.id])</sql>
</db:stored-procedure>
```

## File Connector

### Listener Configuration

```xml
<file:listener-config name="File_Listener_config">
    <file:listener-directory directory="/input" 
                             autoDelete="true"
                             watermark="0"/>
</file:listener-config>

<flow name="file-flow">
    <file:listener config-ref="File_Listener_config"/>
    <file:read path="/input" outputMimeType="text/plain"/>
</flow>
```

### File Operations

```xml
<!-- Read -->
<file:read path="/input" outputMimeType="text/plain"/>

<!-- Write -->
<file:write path="/output" outputMimeType="application/json">
    <file:content value="#[payload]"/>
</file:write>

<!-- Copy -->
<file:copy sourcePath="/input" destinationPath="/backup"/>

<!-- Move -->
<file:move sourcePath="/input" destinationPath="/archive"/>

<!-- Delete -->
<file:delete path="/temp/file.txt"/>
```

## JMS Connector

### Configuration

```xml
<jms:config name="JMS_Config">
    <jms:active-mq-connection username="admin" password="admin"/>
</jms:config>

<!-- Or with custom connection -->
<jms:config name="JMS_Custom">
    <jms:connection-factory ref="connectionFactory"/>
</jms:config>
```

### JMS Operations

```xml
<!-- Publish -->
<jms:publish config-ref="JMS_Config" destination="orders-queue"/>

<!-- Consume -->
<jms:consume config-ref="JMS_Config" destination="orders-queue"/>

<!-- Request-Reply -->
<jms:publish-consume config-ref="JMS_Config" 
                     destination="request-queue"
                     replyToDestination="reply-queue"/>
```

## Custom Connectors

### DevKit Connector

```java
@Connector(name="custom", friendlyName="Custom Connector")
public class CustomConnector {
    
    @ConnectionStrategy
    public void connect(@ConnectionKey String host, @Optional String port) {
        // Connection logic
    }
    
    @Processor
    public String fetchData(@ConnectionKey String id) {
        // Processor logic
        return "data";
    }
    
    @Source
    public boolean onData(@SourceStrategy SourceCallback callback) {
        // Source logic
        return true;
    }
}
```

### Connector Configuration

```xml
<custom:config name="Custom_Config">
    <custom:connection host="localhost" port="8080"/>
</custom:config>

<flow name="custom-flow">
    <custom:source config-ref="Custom_Config"/>
    <custom:fetch-data id="#[payload.id]"/>
</flow>
```

## Best Practices

1. **Use connection pooling**: Configure connection pools
2. **Handle errors**: Configure error handling
3. **Set timeouts**: Configure connection and read timeouts
4. **Security**: Use secure connections
5. **Monitoring**: Track connector metrics
6. **Testing**: Test with mocks
7. **Documentation**: Document connector usage
8. **Versioning**: Use compatible connector versions

## References

- [Mule Connectors](https://docs.mulesoft.com/connectors/)
- [DevKit](https://docs.mulesoft.com/devkit/)
