# EJB 2.x (Enterprise JavaBeans)

## Overview

EJB 2.x is the second major release of the Enterprise JavaBeans specification, defining component models for server-side business logic. It introduced local interfaces, message-driven beans, and enhanced container-managed persistence.

## Session Beans

Session beans handle business logic and workflow. Stateful session beans maintain client-specific state across method invocations, while stateless session beans provide stateless business services.

## Entity Beans (CMP and BMP)

Container-Managed Persistence (CMP) entity beans delegate database operations to the EJB container. Bean-Managed Persistence (BMP) requires the developer to implement JDBC logic within the entity bean.

```java
public abstract class CustomerBean implements EntityBean {
    public abstract Long getCustomerId();
    public abstract void setCustomerId(Long id);
    public abstract String getName();
    public abstract void setName(String name);
    
    public void ejbCreate(Long id, String name) {
        setCustomerId(id);
        setName(name);
    }
}
```

## Message-Driven Beans

EJB 2.x introduced message-driven beans for asynchronous processing. They consume JMS messages and process them without blocking the calling client.

## Transaction Management

EJB containers provide declarative transaction management using deployment descriptors. Transaction attributes (REQUIRED, REQUIRES_NEW, SUPPORTS) control transaction behavior for each method.

## Deployment Descriptors

EJB 2.x uses complex XML deployment descriptors (ejb-jar.xml) to define bean metadata, relationships, queries, and container configuration. The descriptors are verbose and error-prone.

## Limitations

EJB 2.x was criticized for complexity, heavyweight deployment, and vendor-specific extensions. Entity beans were particularly problematic due to performance overhead and mapping difficulties.

## Migration to EJB 3.x and Beyond

Migrating from EJB 2.x involves simplifying bean classes, replacing entity beans with JPA, and using annotations instead of XML descriptors. The persistence layer typically requires the most significant refactoring effort.
