# WSO2 Enterprise Integrator - Connectors

## Overview

WSO2 EI connectors provide reusable integration components for connecting to external systems like Salesforce, Google, and databases.

## Table of Contents

1. [Connector Architecture](#connector-architecture)
2. [Built-in Connectors](#built-in-connectors)
3. [Connector Configuration](#connector-configuration)
4. [Custom Connectors](#custom-connectors)

## Connector Architecture

### Connector Structure

```
┌─────────────────────────────────────────┐
│           WSO2 Connector                │
├─────────────────────────────────────────┤
│  Config: Connection settings            │
│  Operations: Available actions          │
│  Parameters: Input parameters           │
└─────────────────────────────────────────┘
```

## Built-in Connectors

### Available Connectors

| Connector | Description |
|-----------|-------------|
| Salesforce | CRM integration |
| Google | Google APIs |
| Amazon | AWS services |
| Database | Database operations |
| File | File operations |
| FTP/SFTP | File transfer |
| Email | Email operations |

## Connector Configuration

### Salesforce Connector

```xml
<sequence name="salesforce-sequence">
    <salesforce.init username="user" password="pass" 
                      loginUrl="https://login.salesforce.com"/>
    <salesforce.create objectType="Account" 
                       sessionId="{sessionId}">
        <salesforce.sObject>
            <salesforce:name>Acme Corp</salesforce:name>
        </salesforce.sObject>
    </salesforce.create>
</sequence>
```

### Database Connector

```xml
<sequence name="database-sequence">
    <db.config name="MySQL">
        <db.url>jdbc:mysql://localhost:3306/orders</db.url>
        <db.username>root</db.username>
        <db.password>password</db.password>
    </db.config>
    <db.select config="MySQL">
        <sql>SELECT * FROM orders WHERE status = 'PENDING'</sql>
    </db.select>
</sequence>
```

## Custom Connectors

### Creating a Connector

```java
public class CustomConnector {
    public String fetchData(String id) {
        // Connector logic
        return "data";
    }
}
```

### Registering Connector

```xml
<synapse>
    <sequence name="custom-sequence">
        <custom connector="CustomConnector" method="fetchData">
            <param name="id" value="123"/>
        </custom>
    </sequence>
</synapse>
```

## Best Practices

1. **Use official connectors**: Prefer official connectors
2. **Configure security**: Secure connector credentials
3. **Handle errors**: Configure error handling
4. **Test connectors**: Test connector operations
5. **Monitor usage**: Track connector metrics
6. **Version control**: Manage connector versions
7. **Documentation**: Document connector usage
8. **Performance**: Consider connector performance

## References

- [WSO2 Connectors](https://store.wso2.com/)
- [Connector Development](https://apim.docs.wso2.com/)
