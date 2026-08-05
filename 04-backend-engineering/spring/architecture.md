# Spring Architecture

## IoC Container

Spring IoC (Inversion of Control) container manages object creation, configuration, and lifecycle. The container reads metadata (annotations, XML) and uses it to assemble fully configured application.

### Container Types

- **BeanFactory**: Lightweight container, lazy initialization
- **ApplicationContext**: Full-featured container, eager initialization, event publishing, i18n

### ApplicationContext Implementations

| Implementation | Use Case |
|---------------|----------|
| `AnnotationConfigApplicationContext` | Annotation-based configuration |
| `ClassPathXmlApplicationContext` | XML configuration from classpath |
| `FileSystemXmlApplicationContext` | XML configuration from filesystem |
| `GenericWebApplicationContext` | Web applications |
| `AnnotationConfigServletWebServerApplicationContext` | Spring Boot web apps |

## Bean Lifecycle

### Bean Creation Process

1. **Instantiation**: Container creates bean instance
2. **Populate Properties**: Dependencies injected
3. **BeanNameAware**: If bean implements `BeanNameAware`
4. **BeanFactoryAware**: If bean implements `BeanFactoryAware`
5. **ApplicationContextAware**: If bean implements `ApplicationContextAware`
6. **BeanPostProcessor.postProcessBeforeInitialization**
7. **@PostConstruct**
8. **InitializingBean.afterPropertiesSet**
9. **Custom init-method**
10. **BeanPostProcessor.postProcessAfterInitialization**
11. **Bean ready for use**
12. **@PreDestroy**
13. **DisposableBean.destroy**
14. **Custom destroy-method**

### Bean Scopes

| Scope | Description |
|-------|-------------|
| `singleton` | One instance per container (default) |
| `prototype` | New instance per request |
| `request` | One instance per HTTP request |
| `session` | One instance per HTTP session |
| `application` | One instance per ServletContext |
| `websocket` | One instance per WebSocket session |

## AOP (Aspect-Oriented Programming)

### Core Concepts

- **Aspect**: Modular concern (logging, security, transactions)
- **Join Point**: Point in execution (method call)
- **Advice**: Action taken at join point
- **Pointcut**: Expression matching join points
- **Weaving**: Linking aspects with target objects

### Advice Types

```java
@Before("execution(* com.example.service.*.*(..))")
public void beforeAdvice(JoinPoint jp) {
    // Before method execution
}

@After("execution(* com.example.service.*.*(..))")
public void afterAdvice(JoinPoint jp) {
    // After method execution (finally)
}

@AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", returning = "result")
public void afterReturningAdvice(JoinPoint jp, Object result) {
    // After successful return
}

@AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", throwing = "ex")
public void afterThrowingAdvice(JoinPoint jp, Exception ex) {
    // After exception
}

@Around("execution(* com.example.service.*.*(..))")
public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
    // Around method execution
    Object result = pjp.proceed();
    return result;
}
```

### Proxy Types

- **JDK Dynamic Proxy**: For interfaces (default)
- **CGLIB Proxy**: For classes (requires `@EnableAspectJAutoProxy(proxyTargetClass = true)`)

### Pointcut Expressions

```java
// Execution patterns
execution(* com.example.service.*.*(..))     // Any method in service package
execution(public * *(..))                     // Any public method
execution(* save*(..))                        // Methods starting with save
execution(* com.example.*.*(..))              // Any method in com.example

// Annotation-based
@annotation(org.springframework.transaction.annotation.Transactional)
bean(orderService)
within(com.example.service.*)
```

## Event System

### Application Events

```java
// Custom event
public class OrderCreatedEvent extends ApplicationEvent {
    private final Order order;
    
    public OrderCreatedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}

// Publisher
@Component
public class OrderEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;
    
    public void publishOrderCreated(Order order) {
        publisher.publishEvent(new OrderCreatedEvent(this, order));
    }
}

// Listener
@Component
public class OrderEventListener {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Handle event
    }
}
```

### Async Events

```java
@Async
@EventListener
public void handleOrderCreatedAsync(OrderCreatedEvent event) {
    // Handle asynchronously
}
```

## Conditional Bean Registration

```java
@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnProperty(name = "feature.enabled", havingValue = "true")
    public FeatureService featureService() {
        return new FeatureService();
    }
    
    @Bean
    @ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
    public DataSource mysqlDataSource() {
        return new MysqlDataSource();
    }
    
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager defaultCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
```

## Profile System

```java
@Configuration
@Profile("production")
public class ProductionConfig {
    @Bean
    public DataSource dataSource() {
        // Production datasource
    }
}

// Activate via property
spring.profiles.active=production

// Activate via annotation
@ActiveProfiles("production")

// Activate via JVM argument
-Dspring.profiles.active=production
```

## Configuration Classes

### @Configuration

```java
@Configuration
public class AppConfig {
    @Bean
    public ServiceA serviceA() {
        return new ServiceA();
    }
    
    @Bean
    public ServiceB serviceB() {
        return new ServiceB(serviceA());
    }
}
```

### @Import

```java
@Configuration
@Import({DataSourceConfig.class, SecurityConfig.class})
public class AppConfig {
    // Imports other configuration classes
}
```

### @PropertySource

```java
@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig {
    @Value("${app.name}")
    private String appName;
}
```

## Auto-Configuration

### How It Works

1. Spring Boot reads `META-INF/spring.factories`
2. Conditions evaluate (`@ConditionalOn*`)
3. Matching configurations imported
4. Beans created in order

### Custom Auto-Configuration

```java
@Configuration
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
public class MyAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MyService myService() {
        return new MyService();
    }
}
```

## Module System

### Spring Modules

- **spring-core**: Core utilities
- **spring-beans**: Bean factory
- **spring-context**: Application context
- **spring-aop**: AOP support
- **spring-web**: Web utilities
- **spring-webmvc**: MVC framework
- **spring-data**: Data access
- **spring-security**: Security framework
- **spring-boot**: Boot auto-configuration

### Dependency Injection in Modules

```
spring-boot-starter-web
├── spring-boot-starter
│   ├── spring-core
│   ├── spring-beans
│   ├── spring-context
│   └── spring-aop
├── spring-web
└── spring-webmvc
```
