# Apache CXF - Servlet

## Overview

CXF Servlet enables deploying CXF services in servlet containers like Tomcat, Jetty, and application servers.

## Table of Contents

1. [CXF Servlet](#cxf-servlet)
2. [Bus Configuration](#bus-configuration)
3. [Publishing Services](#publishing-services)
4. [Configuration](#configuration)

## CXF Servlet

### web.xml Configuration

```xml
<servlet>
    <servlet-name>cxf</servlet-name>
    <servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>cxf</servlet-name>
    <url-pattern>/services/*</url-pattern>
</servlet-mapping>
```

### Spring Configuration

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:jaxrs="http://cxf.apache.org/jaxrs"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://cxf.apache.org/jaxrs
           http://cxf.apache.org/schemas/jaxrs.xsd">
    
    <import resource="classpath:META-INF/cxf/cxf.xml"/>
    
    <jaxrs:server id="orderService" address="/orders">
        <jaxrs:serviceBeans>
            <ref bean="orderResource"/>
        </jaxrs:serviceBeans>
        <jaxrs:features>
            <ref bean="loggingFeature"/>
        </jaxrs:features>
    </jaxrs:server>
    
    <bean id="orderResource" class="com.example.OrderResource"/>
    <bean id="loggingFeature" class="org.apache.cxf.feature.LoggingFeature"/>
</beans>
```

## Bus Configuration

### Default Bus

```java
// Get default bus
Bus bus = BusFactory.getDefaultBus();

// Create bus
Bus bus = new DefaultBus();
```

### Custom Bus

```xml
<beans>
    <bean id="cxfBus" class="org.apache.cxf.bus.spring.SpringBus">
        <property name="features">
            <list>
                <ref bean="loggingFeature"/>
            </list>
        </property>
    </bean>
</beans>
```

## Publishing Services

### Annotation-Based

```java
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    @GET
    @Path("/{id}")
    public Order getOrder(@PathParam("id") String id) {
        return orderService.getOrder(id);
    }
}
```

### Programmatic

```java
JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
factory.setBus(bus);
factory.setAddress("/orders");
factory.setResourceClasses(OrderResource.class);
factory.create();
```

## Configuration

### cxf.xml

```xml
<beans xmlns="http://www.springframework.org/schema/beans">
    <import resource="classpath:META-INF/cxf/cxf.xml"/>
    
    <bean id="loggingFeature" class="org.apache.cxf.feature.LoggingFeature"/>
</beans>
```

### cxf-servlet.xml

```xml
<beans>
    <jaxrs:server address="/api">
        <jaxrs:serviceBeans>
            <ref bean="orderResource"/>
        </jaxrs:serviceBeans>
    </jaxrs:server>
</beans>
```

## Best Practices

1. **Use Spring**: Leverage Spring for configuration
2. **Configure interceptors**: Add logging and security
3. **Use features**: Apply cross-cutting concerns
4. **Test endpoints**: Test published services
5. **Monitor services**: Track service metrics
6. **Secure services**: Add authentication/authorization
7. **Document APIs**: Generate API documentation
8. **Handle errors**: Configure error handling

## References

- [CXF Servlet](https://cxf.apache.org/docs/servlet-transport.html)
- [CXF Spring](https://cxf.apache.org/docs/spring-boot.html)
