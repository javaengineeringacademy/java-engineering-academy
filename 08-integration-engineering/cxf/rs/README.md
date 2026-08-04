# Apache CXF - RESTful Services (RS)

## Overview

CXF provides full JAX-RS support for building RESTful web services with annotations, content negotiation, and multiple data formats.

## Table of Contents

1. [JAX-RS Basics](#jax-rs-basics)
2. [Resource Classes](#resource-classes)
3. [Content Negotiation](#content-negotiation)
4. [Filters and Interceptors](#filters-and-interceptors)
5. [Client API](#client-api)

## JAX-RS Basics

### Resource Class

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
    
    @GET
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }
    
    @POST
    public Response createOrder(Order order) {
        Order created = orderService.create(order);
        return Response.status(201)
            .entity(created)
            .build();
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

### Application Path

```java
@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(OrderResource.class);
        classes.add(CustomerResource.class);
        return classes;
    }
}
```

## Resource Classes

### Sub-Resources

```java
@Path("/orders")
public class OrderResource {
    
    @Path("/{id}/items")
    public OrderItemsResource getItems(@PathParam("id") String orderId) {
        return new OrderItemsResource(orderId);
    }
}

public class OrderItemsResource {
    private final String orderId;
    
    public OrderItemsResource(String orderId) {
        this.orderId = orderId;
    }
    
    @GET
    public List<OrderItem> getItems() {
        return orderService.getItems(orderId);
    }
}
```

### Bean Parameters

```java
@GET
@Path("/search")
public List<Order> searchOrders(@BeanParam OrderQuery query) {
    return orderService.search(query);
}

public class OrderQuery {
    @QueryParam("status")
    private String status;
    
    @QueryParam("minTotal")
    private Double minTotal;
    
    @QueryParam("maxTotal")
    private Double maxTotal;
    
    // Getters and setters
}
```

## Content Negotiation

### Produces

```java
@GET
@Path("/{id}")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public Order getOrder(@PathParam("id") String id) {
    return orderService.getOrder(id);
}
```

### Consumes

```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response createOrder(Order order) {
    return Response.status(201).entity(orderService.create(order)).build();
}

@POST
@Consumes(MediaType.APPLICATION_XML)
public Response createOrderXml(Order order) {
    return Response.status(201).entity(orderService.create(order)).build();
}
```

### Content Negotiation via Header

```java
@GET
@Path("/{id}")
public Response getOrder(@PathParam("id") String id,
                         @HeaderParam("Accept") String accept) {
    Order order = orderService.getOrder(id);
    
    if (accept.contains("application/xml")) {
        return Response.ok(order, MediaType.APPLICATION_XML).build();
    }
    return Response.ok(order, MediaType.APPLICATION_JSON).build();
}
```

## Filters and Interceptors

### Request Filter

```java
@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        System.out.println("Request: " + context.getMethod() + " " + 
            context.getUriInfo().getRequestUri());
    }
}
```

### Response Filter

```java
@Provider
public class ResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("X-Powered-By", "CXF");
    }
}
```

### Entity Filter

```java
@Provider
public class GZipFilter implements ReaderInterceptor, WriterInterceptor {
    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException {
        InputStream is = context.getInputStream();
        context.setInputStream(new GZIPInputStream(is));
        return context.proceed();
    }
    
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException {
        OutputStream os = context.getOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(os);
        context.setOutputStream(gos);
        context.proceed();
        gos.finish();
    }
}
```

## Client API

### WebClient

```java
// GET
Order order = WebClient.create("http://localhost:8080/api")
    .path("/orders/123")
    .accept(MediaType.APPLICATION_JSON)
    .get(Order.class);

// POST
Order created = WebClient.create("http://localhost:8080/api")
    .path("/orders")
    .accept(MediaType.APPLICATION_JSON)
    .type(MediaType.APPLICATION_JSON)
    .post(order, Order.class);

// PUT
Order updated = WebClient.create("http://localhost:8080/api")
    .path("/orders/123")
    .accept(MediaType.APPLICATION_JSON)
    .type(MediaType.APPLICATION_JSON)
    .put(order, Order.class);
```

### Client with Configuration

```java
WebClient client = WebClient.create("http://localhost:8080/api");
client.path("/orders");
client.accept(MediaType.APPLICATION_JSON);
client.header("Authorization", "Bearer token");

List<Order> orders = client.get(new GenericType<List<Order>>() {});
```

## Best Practices

1. **Use proper HTTP methods**: GET, POST, PUT, DELETE
2. **Return appropriate status codes**: 200, 201, 404, 500
3. **Use content negotiation**: Support multiple formats
4. **Validate input**: Validate request parameters
5. **Handle errors**: Return meaningful error responses
6. **Document APIs**: Use Swagger/OpenAPI
7. **Secure endpoints**: Implement authentication
8. **Test thoroughly**: Test all endpoints

## References

- [CXF JAX-RS](https://cxf.apache.org/docs/jax-rs.html)
- [JAX-RS Specification](https://jakarta.ee/specifications/platform/9.1/apidocs/jakarta/ws/rs/package-summary.html)
