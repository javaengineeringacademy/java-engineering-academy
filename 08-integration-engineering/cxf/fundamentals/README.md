# Apache CXF - Fundamentals

## Overview

Apache CXF is an open-source framework for building and deploying web services. It supports JAX-RS (REST) and JAX-WS (SOAP) standards.

## Table of Contents

1. [What is CXF](#what-is-cxf)
2. [Architecture](#architecture)
3. [JAX-RS](#jax-rs)
4. [JAX-WS](#jax-ws)
5. [Features](#features)
6. [First Service](#first-service)

## What is CXF

CXF provides:

- JAX-RS for RESTful services
- JAX-WS for SOAP services
- WS-* standards support
- Multiple transports
- Interceptor support

## Architecture

### CXF Architecture

```
┌─────────────────────────────────────────┐
│              Apache CXF                 │
├─────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐            │
│  │ JAX-RS   │  │ JAX-WS   │            │
│  │ (REST)   │  │ (SOAP)   │            │
│  └────┬─────┘  └────┬─────┘            │
│       │              │                  │
│  ┌────▼──────────────▼─────┐            │
│  │     Frontend/API        │            │
│  └────────────┬────────────┘            │
│               │                         │
│  ┌────────────▼────────────┐            │
│  │   Interceptors/Features │            │
│  └────────────┬────────────┘            │
│               │                         │
│  ┌────────────▼────────────┐            │
│  │     Transports          │            │
│  │  HTTP, JMS, WebSocket   │            │
│  └─────────────────────────┘            │
└─────────────────────────────────────────┘
```

## JAX-RS

### REST Service

```java
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    
    @GET
    @Path("/{id}")
    public Order getOrder(@PathParam("id") String id) {
        return orderService.getOrder(id);
    }
    
    @POST
    public Response createOrder(Order order) {
        Order created = orderService.create(order);
        return Response.status(201).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public Order updateOrder(@PathParam("id") String id, Order order) {
        return orderService.update(id, order);
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") String id) {
        orderService.delete(id);
        return Response.noContent().build();
    }
}
```

### JAX-RS Application

```java
@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(OrderResource.class);
        return classes;
    }
}
```

## JAX-WS

### SOAP Service

```java
@WebService(name = "OrderService", 
            targetNamespace = "http://example.com/orders")
public class OrderServiceImpl implements OrderService {
    
    @WebMethod
    public Order getOrder(@WebParam(name = "orderId") String orderId) {
        return orderService.getOrder(orderId);
    }
    
    @WebMethod
    public Order createOrder(@WebParam(name = "order") Order order) {
        return orderService.create(order);
    }
}
```

### WSDL Generation

```xml
<wsdl:definitions>
    <wsdl:types>
        <xs:schema>
            <xs:element name="getOrder">
                <xs:complexType>
                    <xs:sequence>
                        <xs:element name="orderId" type="xs:string"/>
                    </xs:sequence>
                </xs:complexType>
            </xs:element>
        </xs:schema>
    </wsdl:types>
</wsdl:definitions>
```

## Features

### Interceptors

```java
// Logging interceptor
public class LoggingInterceptor extends AbstractPhaseInterceptor<Message> {
    @Override
    public void handleMessage(Message message) {
        System.out.println("Message: " + message.toString());
    }
}

// Add interceptor
JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
factory.getInInterceptors().add(new LoggingInterceptor());
```

### Features

```java
// Apply features
JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
factory.getFeatures().add(new LoggingFeature());
factory.getFeatures().add(new Swagger2Feature());
```

## First Service

### REST Service with CXF

```java
@Path("/hello")
public class HelloResource {
    
    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("name") String name) {
        return "Hello, " + name + "!";
    }
}

// Publish
JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
factory.setResourceClasses(HelloResource.class);
factory.setAddress("http://localhost:8080/api/");
factory.create();
```

## Best Practices

1. **Use annotations**: Leverage JAX-RS/JAX-WS annotations
2. **Content negotiation**: Support multiple formats
3. **Error handling**: Return appropriate HTTP status codes
4. **Validation**: Validate input parameters
5. **Security**: Secure endpoints
6. **Documentation**: Generate API documentation
7. **Testing**: Test services
8. **Performance**: Optimize service performance

## References

- [Apache CXF](https://cxf.apache.org/)
- [CXF JAX-RS](https://cxf.apache.org/docs/jax-rs.html)
- [CXF JAX-WS](https://cxf.apache.org/docs/jax-ws.html)
