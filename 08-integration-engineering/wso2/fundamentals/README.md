# WSO2 Enterprise Integrator - Fundamentals

## Overview

WSO2 Enterprise Integrator (EI) is an open-source integration platform based on WSO2 Synapse, providing mediation, routing, and transformation capabilities.

## Table of Contents

1. [What is WSO2 EI](#what-is-wso2-ei)
2. [Architecture](#architecture)
3. [Synapse](#synapse)
4. [Mediators](#mediators)
5. [Configuration](#configuration)
6. [First Proxy](#first-proxy)

## What is WSO2 EI

WSO2 EI provides:

- Message mediation
- Protocol bridging
- Content-based routing
- Data transformation
- API management

## Architecture

### WSO2 EI Architecture

```
┌─────────────────────────────────────────┐
│         WSO2 Enterprise Integrator      │
├─────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐            │
│  │  Synapse │  │  Synapse │            │
│  │  Engine  │  │  Engine  │            │
│  └────┬─────┘  └────┬─────┘            │
│       │              │                  │
│  ┌────▼─────┐  ┌────▼─────┐            │
│  │  Proxy   │  │  Proxy   │            │
│  │ Service  │  │ Service  │            │
│  └──────────┘  └──────────┘            │
└─────────────────────────────────────────┘
```

### Component Types

| Component | Description |
|-----------|-------------|
| Proxy Service | Message mediation |
| Sequence | Reusable mediation |
| Endpoint | Target service |
| mediator | Message processing |

## Synapse

### Synapse Configuration

```xml
<synapse xmlns="http://ws.apache.org/synapse">
    <proxy name="OrderProxy" startOnLoad="true">
        <target>
            <inSequence>
                <log level="full"/>
                <send>
                    <endpoint>
                        <address uri="http://backend:8080/api/orders"/>
                    </endpoint>
                </send>
            </inSequence>
            <outSequence>
                <log level="full"/>
                <send/>
            </outSequence>
        </target>
    </proxy>
</synapse>
```

### Synapse Flow

```
Client ──> Proxy Service ──> Mediation ──> Backend
   │                           │              │
   │                           ▼              │
   │                      [Mediators]         │
   │                                          │
   └──────────────────────────────────────────┘
```

## Mediators

### Core Mediators

| Mediator | Description |
|----------|-------------|
| Log | Logging messages |
| Send | Send to endpoint |
| Receive | Receive messages |
| Filter | Content-based routing |
| Switch | Choice routing |
| Transform | Message transformation |

### Log Mediator

```xml
<log level="full">
    <property name="MESSAGE" value="Processing message"/>
</log>
```

### Send Mediator

```xml
<send>
    <endpoint>
        <address uri="http://backend:8080"/>
    </endpoint>
</send>
```

### Filter Mediator

```xml
<filter source="get-property('priority')" value="HIGH">
    <then>
        <send>
            <endpoint>
                <address uri="http://high-priority:8080"/>
            </endpoint>
        </send>
    </then>
    <else>
        <send>
            <endpoint>
                <address uri="http://normal:8080"/>
            </endpoint>
        </send>
    </else>
</filter>
```

## Configuration

### synapse.xml

```xml
<synapse xmlns="http://ws.apache.org/synapse">
    <!-- Sequences -->
    <sequence name="fault">
        <fault/>
    </sequence>
    
    <!-- Endpoints -->
    <endpoint name="BackendEndpoint">
        <address uri="http://backend:8080"/>
    </endpoint>
    
    <!-- Proxy Services -->
    <proxy name="MyProxy" startOnLoad="true">
        <!-- Proxy configuration -->
    </proxy>
</synapse>
```

## First Proxy

### Simple Proxy

```xml
<proxy name="HelloProxy" startOnLoad="true">
    <target>
        <inSequence>
            <log level="full"/>
            <send>
                <endpoint>
                    <address uri="http://hello-service:8080"/>
                </endpoint>
            </send>
        </inSequence>
        <outSequence>
            <log level="full"/>
            <send/>
        </outSequence>
    </target>
</proxy>
```

## Best Practices

1. **Use sequences**: Promote reuse
2. **Error handling**: Configure fault sequences
3. **Logging**: Add appropriate logging
4. **Testing**: Test proxy services
5. **Documentation**: Document mediation logic
6. **Monitoring**: Track proxy metrics
7. **Security**: Secure endpoints
8. **Performance**: Optimize mediation

## References

- [WSO2 EI Documentation](https://apim.docs.wso2.com/)
- [Synapse Configuration](https://synapse.apache.org/)
