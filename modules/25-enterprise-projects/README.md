# Module 25: Enterprise Projects

## Overview
Enterprise projects demonstrate real-world application of Java concepts. This module covers project ideas, architecture patterns, and implementation strategies.

## Learning Objectives
- Design enterprise applications
- Apply architectural patterns
- Implement business logic
- Handle cross-cutting concerns
- Deploy to production

## Prerequisites
- Java fundamentals
- Spring Boot
- Database knowledge

## Why This Concept Exists
Enterprise projects need:
- Scalable architecture
- Business logic implementation
- Integration with systems
- Production readiness

This module provides:
- Project templates
- Architecture patterns
- Implementation guidance
- Best practices

## Problem Statement
How do you build production-ready enterprise applications?

## Theory

### Project Types

| Type | Description |
|------|-------------|
| Web Application | Browser-based |
| REST API | Service layer |
| Microservices | Distributed system |
| Batch Processing | Scheduled jobs |
| Real-time | Streaming data |

### Architecture Patterns

| Pattern | Use Case |
|---------|----------|
| MVC | Web applications |
| Microservices | Distributed systems |
| Event-Driven | Real-time processing |
| CQRS | Read/write separation |

## Project Ideas

### 1. E-commerce Platform

| Component | Technologies |
|-----------|-------------|
| Backend | Spring Boot, JPA |
| Database | PostgreSQL, Redis |
| Messaging | Kafka |
| Frontend | React/Angular |
| Deployment | Docker, Kubernetes |

### 2. Banking System

| Component | Technologies |
|-----------|-------------|
| Backend | Spring Boot, JPA |
| Database | Oracle, PostgreSQL |
| Security | Spring Security, OAuth2 |
| Messaging | RabbitMQ |
| Monitoring | Prometheus, Grafana |

### 3. Social Media Platform

| Component | Technologies |
|-----------|-------------|
| Backend | Spring Boot, WebFlux |
| Database | MongoDB, Redis |
| Search | Elasticsearch |
| Messaging | Kafka |
| Storage | S3 |

## Enterprise Example

```java
// Complete e-commerce order service
@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final EventPublisher eventPublisher;
    
    public OrderService(OrderRepository orderRepository,
                       ProductRepository productRepository,
                       PaymentService paymentService,
                       InventoryService inventoryService,
                       EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.eventPublisher = eventPublisher;
    }
    
    public OrderDTO createOrder(CreateOrderRequest request) {
        // Validate products
        List<OrderItem> items = validateAndCreateItems(request.getItems());
        
        // Check inventory
        items.forEach(item -> 
            inventoryService.reserve(item.getProductId(), item.getQuantity()));
        
        // Calculate total
        BigDecimal total = items.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = Order.builder()
            .userId(request.getUserId())
            .items(items)
            .total(total)
            .status(OrderStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
        
        Order saved = orderRepository.save(order);
        
        // Process payment
        try {
            paymentService.charge(saved.getId(), total);
            saved.setStatus(OrderStatus.PAID);
            orderRepository.save(saved);
            
            // Publish event
            eventPublisher.publish(new OrderCreatedEvent(saved.getId()));
        } catch (PaymentException e) {
            saved.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(saved);
            
            // Release inventory
            items.forEach(item -> 
                inventoryService.release(item.getProductId(), item.getQuantity()));
            
            throw e;
        }
        
        return OrderMapper.toDTO(saved);
    }
    
    private List<OrderItem> validateAndCreateItems(List<OrderItemRequest> itemRequests) {
        return itemRequests.stream()
            .map(request -> {
                Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));
                
                if (product.getStock() < request.getQuantity()) {
                    throw new InsufficientStockException(product.getId());
                }
                
                return OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantity(request.getQuantity())
                    .build();
            })
            .toList();
    }
}

// REST Controller
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Order operations")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    @Operation(summary = "Create order")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody @Valid CreateOrderRequest request,
            @AuthenticationPrincipal UserDetails user) {
        
        OrderDTO order = orderService.createOrder(request);
        URI location = URI.create("/api/orders/" + order.getId());
        return ResponseEntity.created(location).body(order);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return orderService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "List orders")
    public ResponseEntity<Page<OrderDTO>> listOrders(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        return ResponseEntity.ok(orderService.findAll(pageable));
    }
}

// Event handler
@Component
public class OrderEventHandler {
    
    private final NotificationService notificationService;
    
    public OrderEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        notificationService.sendOrderConfirmation(event.getOrderId());
    }
}
```

## Performance Considerations
- Use caching for frequent queries
- Implement pagination
- Use async processing
- Optimize database queries

## Best Practices
1. Use clean architecture
2. Implement proper error handling
3. Add logging and monitoring
4. Write tests
5. Document API

## Common Mistakes
1. Over-engineering
2. Ignoring cross-cutting concerns
3. Poor error handling
4. Not testing

## Comparison Table

| Aspect | Monolith | Microservices |
|--------|----------|---------------|
| Complexity | Low | High |
| Deployment | Simple | Complex |
| Scaling | Vertical | Horizontal |
| Team Size | Small | Large |

## Interview Questions

### Q1: How do you design an enterprise application?
**Answer:** Use clean architecture, separate concerns, and apply SOLID principles.

### Q2: What is the difference between service and repository?
**Answer:** Service contains business logic, repository handles data access.

### Q3: How do you handle transactions?
**Answer:** Use @Transactional annotation and proper isolation.

### Q4: How do you implement caching?
**Answer:** Use Redis or in-memory cache with appropriate TTL.

### Q5: How do you handle errors?
**Answer:** Use exception handlers and provide meaningful error messages.

## Summary
Enterprise projects require proper architecture, patterns, and best practices.

## References
- Clean Architecture by Robert Martin
- Domain-Driven Design
- Spring Boot Reference
