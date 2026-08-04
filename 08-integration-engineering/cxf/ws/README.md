# Apache CXF - Web Services (WS)

## Overview

CXF provides full support for building SOAP-based web services using JAX-WS standard with WSDL-first or code-first approaches.

## Table of Contents

1. [SOAP Web Services](#soap-web-services)
2. [WSDL-First Approach](#wsdl-first-approach)
3. [Code-First Approach](#code-first-approach)
4. [WS-* Standards](#ws-standards)
5. [Client Development](#client-development)

## SOAP Web Services

### Service Endpoint

```java
@WebService(name = "OrderService",
            targetNamespace = "http://example.com/orders")
public class OrderServiceImpl implements OrderService {
    
    @WebMethod(operationName = "getOrder")
    public Order getOrder(
            @WebParam(name = "orderId") String orderId) {
        return orderService.getOrder(orderId);
    }
    
    @WebMethod(operationName = "createOrder")
    public Order createOrder(
            @WebParam(name = "order") Order order) {
        return orderService.create(order);
    }
}
```

### Service Interface

```java
@WebService(name = "OrderService",
            targetNamespace = "http://example.com/orders")
public interface OrderService {
    
    @WebMethod
    Order getOrder(@WebParam(name = "orderId") String orderId);
    
    @WebMethod
    Order createOrder(@WebParam(name = "order") Order order);
}
```

## WSDL-First Approach

### Generate from WSDL

```bash
wsdl2java -d src/main/java -s orders.wsdl
```

### WSDL File

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions name="OrderService"
             targetNamespace="http://example.com/orders"
             xmlns="http://schemas.xmlsoap.org/wsdl/"
             xmlns:tns="http://example.com/orders"
             xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    
    <types>
        <schema targetNamespace="http://example.com/orders">
            <element name="getOrder">
                <complexType>
                    <sequence>
                        <element name="orderId" type="xsd:string"/>
                    </sequence>
                </complexType>
            </element>
            <element name="Order" type="tns:Order"/>
        </schema>
    </types>
    
    <message name="GetOrderInput">
        <part name="parameters" element="tns:getOrder"/>
    </message>
    
    <portType name="OrderServicePortType">
        <operation name="getOrder">
            <input message="tns:GetOrderInput"/>
        </operation>
    </portType>
    
    <binding name="OrderServiceBinding" type="tns:OrderServicePortType">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
    </binding>
    
    <service name="OrderService">
        <port name="OrderServicePort" binding="tns:OrderServiceBinding">
            <soap:address location="http://localhost:8080/services/orders"/>
        </port>
    </service>
</definitions>
```

## Code-First Approach

### Service Implementation

```java
@WebService
public class OrderServiceImpl implements OrderService {
    
    @Override
    public Order getOrder(String orderId) {
        return orderService.getOrder(orderId);
    }
    
    @Override
    public Order createOrder(Order order) {
        return orderService.create(order);
    }
}
```

### Publish Service

```java
JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
factory.setServiceClass(OrderServiceImpl.class);
factory.setAddress("http://localhost:8080/services/orders");
factory.create();
```

## WS-* Standards

### WS-Security

```java
// Add security interceptor
WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor();
Map<String, Object> props = new HashMap<>();
props.put("action", "UsernameToken");
props.put("passwordType", "PasswordText");
inInterceptor.setProperties(props);

factory.getInInterceptors().add(inInterceptor);
```

### WS-Addressing

```java
// Enable addressing
factory.getFeatures().add(new WSAddressingFeature());
```

### WS-Policy

```java
// Apply policy
factory.getFeatures().add(new WSManagementFeature());
```

## Client Development

### Generated Client

```java
OrderService_Service service = new OrderService_Service();
OrderService port = service.getOrderServicePort();
Order order = port.getOrder("123");
```

### CXF Client

```java
JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
factory.setServiceClass(OrderService.class);
factory.setAddress("http://localhost:8080/services/orders");

OrderService client = (OrderService) factory.create();
Order order = client.getOrder("123");
```

## Best Practices

1. **Choose approach**: WSDL-first for contracts, code-first for speed
2. **Use interfaces**: Define service interfaces
3. **Handle exceptions**: Use fault beans
4. **Security**: Implement WS-Security
5. **Testing**: Test services thoroughly
6. **Documentation**: Generate WSDL documentation
7. **Versioning**: Plan service versioning
8. **Performance**: Optimize service performance

## References

- [CXF JAX-WS](https://cxf.apache.org/docs/jax-ws.html)
- [WS-* Standards](https://cxf.apache.org/docs/ws-related-standards.html)
