# N-Tier Architecture

N-Tier architecture physically separates application into distinct tiers (layers) that can be deployed independently. Each tier runs on a separate server or process.

## Table of Contents

1. [Concepts](#concepts)
2. [Three-Tier](#three-tier)
3. [Physical Separation](#physical-separation)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is N-Tier?

N-Tier separates application into physically independent tiers that communicate over network.

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│  Client  │────▶│  Server  │────▶│ Database │
│  (Tier1) │     │  (Tier2) │     │  (Tier3) │
└──────────┘     └──────────┘     └──────────┘
```

### Benefits

- **Scalability** - scale tiers independently
- **Security** - isolate sensitive tiers
- **Flexibility** - different technologies per tier
- **Deployment** - deploy tiers independently

---

## Three-Tier

### Presentation Tier

```java
// Web application - presentation tier
@Controller
public class ProductController {
    private final ProductServiceClient client;

    @GetMapping("/products")
    public List<ProductDto> listProducts() {
        return client.getAllProducts();  // Calls middle tier
    }
}
```

### Application Tier

```java
// Middle tier - business logic
@Service
public class ProductService {
    private final ProductRepository repository;

    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {
        Product product = new Product(request.name(), request.price());
        repository.save(product);
        return toDto(product);
    }
}
```

### Data Tier

```java
// Database tier - data access
@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public Product findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM products WHERE id = ?",
            new ProductRowMapper(), id);
    }
}
```

---

## Physical Separation

### API Gateway

```java
// Presentation tier calls API gateway
@RestController
public class GatewayController {
    private final WebClient webClient;

    @GetMapping("/api/products")
    public Mono<List<Product>> getProducts() {
        return webClient.get()
            .uri("http://product-service/products")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Product>>() {});
    }
}
```

### Inter-Tier Communication

```java
// REST between tiers
public class OrderClient {
    private final RestTemplate restTemplate;

    public Order createOrder(OrderRequest request) {
        return restTemplate.postForObject(
            "http://order-service/orders",
            request, Order.class);
    }
}

// gRPC between tiers
public class ProductGrpcClient {
    private final ProductGrpc.ProductBlockingStub stub;

    public Product getProduct(Long id) {
        return stub.getProduct(GetProductRequest.newBuilder()
            .setId(id)
            .build());
    }
}
```

---

## Best Practices

### Do

```java
// 1. Use API contracts between tiers
public interface ProductService {
    ProductDto getProduct(Long id);
}

// 2. Handle network failures gracefully
public Product getProductWithFallback(Long id) {
    try {
        return client.getProduct(id);
    } catch (Exception e) {
        return Product.defaultValue();
    }
}
```

### Don't

```java
// 1. Don't create tight coupling between tiers
// Use contracts and DTOs

// 2. Don't ignore latency
// Network calls are expensive
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **N-Tier** | Physical separation of layers |
| **Presentation** | User interface tier |
| **Application** | Business logic tier |
| **Data** | Database tier |
| **Independent Deployment** | Deploy tiers separately |
| **Scalability** | Scale tiers independently |
| **Network Communication** | REST, gRPC between tiers |
| **vs Layered** | Physical vs logical separation |
