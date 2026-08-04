# WSO2 Enterprise Integrator - Mediation

## Overview

Mediation in WSO2 EI processes messages as they flow through proxy services, applying transformations, routing, and filtering.

## Table of Contents

1. [Mediation Sequences](#mediation-sequences)
2. [Filters](#filters)
3. [Routers](#routers)
4. [Transformers](#transformers)
5. [Endpoints](#endpoints)

## Mediation Sequences

### In Sequence

```xml
<inSequence>
    <log level="full"/>
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
</inSequence>
```

### Out Sequence

```xml
<outSequence>
    <log level="full"/>
    <send/>
</outSequence>
```

### Fault Sequence

```xml
<faultSequence>
    <log level="full">
        <property name="ERROR" value="Fault occurred"/>
    </log>
    <makefault version="soap11">
        <faultcode>soap:Server</faultcode>
        <faultstring>Internal Server Error</faultstring>
    </makefault>
    <send/>
</faultSequence>
```

## Filters

### Filter Mediator

```xml
<filter source="get-property('type')" value="ORDER">
    <then>
        <send>
            <endpoint>
                <address uri="http://order-service:8080"/>
            </endpoint>
        </send>
    </then>
    <else>
        <send>
            <endpoint>
                <address uri="http://default-service:8080"/>
            </endpoint>
        </send>
    </else>
</filter>
```

### XPath Filter

```xml
<filter xpath="//order/priority > 5">
    <then>
        <send>
            <endpoint>
                <address uri="http://high-priority:8080"/>
            </endpoint>
        </send>
    </then>
</filter>
```

## Routers

### Switch Mediator

```xml
<switch source="get-property('country')">
    <case regex="US">
        <send>
            <endpoint>
                <address uri="http://us-service:8080"/>
            </endpoint>
        </send>
    </case>
    <case regex="EU">
        <send>
            <endpoint>
                <address uri="http://eu-service:8080"/>
            </endpoint>
        </send>
    </case>
    <default>
        <send>
            <endpoint>
                <address uri="http://global-service:8080"/>
            </endpoint>
        </send>
    </default>
</switch>
```

### Recipient List

```xml
<property name="RECIPIENTS" value="http://service1:8080,http://service2:8080"/>
<iterate expression="get-property('RECIPIENTS')" 
         preservePayload="true">
    <target>
        <sequence>
            <send>
                <endpoint>
                    <address uri="{get-property('RECIPIENT')}"/>
                </endpoint>
            </send>
        </sequence>
    </target>
</iterate>
```

## Transformers

### XSLT Transformation

```xml
<xslt key="transform.xslt"/>
```

### Script Transformation

```xml
<script language="js">
    var payload = mc.getPayloadXML();
    // Transform payload
    mc.setPayloadXML(transformedPayload);
</script>
```

### Property Transformer

```xml
<property name="transformedValue" expression="xpath://order/id"/>
```

## Endpoints

### Address Endpoint

```xml
<endpoint name="BackendEndpoint">
    <address uri="http://backend:8080"/>
</endpoint>
```

### Load Balanced Endpoint

```xml
<endpoint name="LoadBalancedEndpoint">
    <loadBalance>
        <endpoint>
            <address uri="http://server1:8080"/>
        </endpoint>
        <endpoint>
            <address uri="http://server2:8080"/>
        </endpoint>
    </loadBalance>
</endpoint>
```

### Failover Endpoint

```xml
<endpoint name="FailoverEndpoint">
    <failover>
        <endpoint>
            <address uri="http://primary:8080"/>
        </endpoint>
        <endpoint>
            <address uri="http://secondary:8080"/>
        </endpoint>
    </failover>
</endpoint>
```

## Best Practices

1. **Use sequences**: Promote reuse
2. **Error handling**: Configure fault sequences
3. **Logging**: Add appropriate logging
4. **Testing**: Test mediation logic
5. **Documentation**: Document mediation flows
6. **Monitoring**: Track mediation metrics
7. **Security**: Secure endpoints
8. **Performance**: Optimize mediation

## References

- [WSO2 Mediation](https://apim.docs.wso2.com/)
- [Synapse Mediators](https://synapse.apache.org/)
