# Spring Framework Internals

## IoC Container

The Inversion of Control container manages object lifecycle and dependencies. ApplicationContext is the central interface; it reads bean definitions, instantiates beans, and wires dependencies. Bean definitions specify class, scope, constructor arguments, and properties. The container uses reflection to instantiate objects and dependency injection to resolve references.

Two container types exist: BeanFactory (lazy initialization) and ApplicationContext (eager initialization with additional features). ApplicationContext adds event publishing, internationalization, and AOP integration. The container supports constructor injection (preferred), setter injection, and field injection. Circular dependencies are resolved using three-level caching (early reference in singletonFactories).

Bean definitions are loaded from XML files, annotations, or Java configuration. The `@ComponentScan` annotation triggers classpath scanning. The `@Import` annotation imports configuration classes. Bean factories provide post-processing hooks for custom modification. The `BeanFactoryPostProcessor` can modify bean definitions before instantiation.

The container uses a bean definition registry to store bean metadata. The `BeanDefinition` interface defines bean properties: scope, lazy-init, depends-on, and autowire mode. The `GenericBeanDefinition` class provides a common implementation. The `@Scope` annotation defines bean lifecycle scope. The `@Lazy` annotation delays initialization until first use.

## Bean Lifecycle

Bean lifecycle follows a defined sequence. The container instantiates the bean using the constructor. If BeanNameAware, BeanFactoryAware, or ApplicationContextAware are implemented, the container calls the respective setter methods. BeanPostProcessor.postProcessBeforeInitialization is invoked. The @PostConstruct method or init-method is called. BeanPostProcessor.postProcessAfterInitialization runs, potentially wrapping the bean with a proxy.

For destruction, @PreDestroy or destroy-method is called when the container shuts down. The DisposableBean interface provides programmatic destruction. The complete lifecycle: instantiation -> populate properties -> BeanNameAware -> BeanFactoryAware -> ApplicationContextAware -> BeanPostProcessor.postProcessBeforeInitialization -> @PostConstruct -> InitializingBean.afterPropertiesSet -> custom init -> BeanPostProcessor.postProcessAfterInitialization.

The `@Scope` annotation defines bean lifecycle scope: singleton (one instance per container), prototype (new instance per request), request (one per HTTP request), session (one per HTTP session), and application (one per ServletContext). The `@Lazy` annotation delays initialization until first use. The `@DependsOn` annotation specifies bean initialization order.

The bean lifecycle includes: instantiation, populate properties, BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor.postProcessBeforeInitialization, @PostConstruct, InitializingBean.afterPropertiesSet, custom init-method, BeanPostProcessor.postProcessAfterInitialization, and destruction.

## AOP (Aspect-Oriented Programming)

Spring AOP uses proxy-based weaving. For interfaces, JDK dynamic proxies are used. For classes (or when configured), CGLIB generates subclasses. Aspects define cross-cutting concerns using @Aspect. Advice types include @Before, @After, @Around, @AfterReturning, and @AfterThrowing.

Pointcuts define join point matching using expressions. The execution pointcut matches method execution: `execution(* com.example.service.*.*(..))`. The @Around advice is the most powerful, controlling proceed() invocation. Spring AOP is runtime-woven, unlike compile-time weaving (AspectJ). The ProxyFactory creates proxies; advisors combine pointcuts with advice.

The `@Pointcut` annotation defines reusable pointcut expressions. The `@Aspect` annotation marks a class as an aspect. Advice ordering is controlled by the `@Order` annotation. The `@AfterReturning` advice can capture return values. The `@AfterThrowing` advice can capture exceptions. The `@Around` advice can modify arguments and return values.

Spring AOP supports aspect ordering. The `@Order` annotation controls aspect priority. Lower order values have higher priority. The `@Priority` annotation (JSR-250) provides standard ordering. AspectJ has its own ordering mechanism. The `Ordered` interface provides programmatic ordering.

## Auto-Configuration

Spring Boot auto-configuration uses conditional annotations. @ConditionalOnClass checks if a class is on the classpath. @ConditionalOnMissingBean ensures no conflicting bean exists. @ConditionalOnProperty checks configuration properties. Auto-configuration classes are registered in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports (Spring Boot 3.0+).

Auto-configuration orders are controlled via @AutoConfigureBefore and @AutoConfigureAfter. The @EnableAutoConfiguration annotation triggers scanning. The spring.factories mechanism (legacy) registers classes under the EnableAutoConfiguration key. Auto-configuration classes are typically named *AutoConfiguration and annotated with @Configuration.

Auto-configuration classes are loaded after application configuration, allowing overrides. The `@ConditionalOnMissingBean` annotation ensures auto-configured beans are replaceable. The `@ConditionalOnWebApplication` annotation checks for web context. The `@AutoConfigureOrder` annotation controls the order of auto-configuration classes. The `@ConditionalOnProperty` annotation checks configuration properties.

The `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` file lists auto-configuration classes. The `spring.autoconfigure.exclude` property excludes specific auto-configuration classes. The `@ConditionalOnBean` annotation checks for bean existence. The `@ConditionalOnExpression` annotation evaluates SpEL expressions.

## Event System

Spring provides application events for decoupled communication. ApplicationContext publishes events; beans implement ApplicationListener or use @EventListener. Events extend ApplicationEvent. The container supports transactional event listeners (@TransactionalEventListener) that trigger after commit, before commit, after rollback, or during completion.

Spring 4.2+ supports payload-based events (not extending ApplicationEvent). The SimpleApplicationEventMulticaster dispatches events synchronously by default; configure an executor for async processing. Events enable loose coupling between components. Spring Cloud uses events for service instance registration and configuration changes.

The `ApplicationEventPublisher` interface publishes events. The `@EventListener` annotation marks a method as an event listener. The `@TransactionalEventListener` annotation binds to transaction phases. Event ordering can be controlled using the `@Order` annotation. The `ResolvableTypeProvider` interface provides generic type information for events.

Events can be filtered using `@EventListener(condition = "...")`. The `@EventListener` annotation supports SpEL expressions for conditional handling. The `@TransactionalEventListener` annotation supports phases: BEFORE_COMMIT, AFTER_COMMIT, AFTER_ROLLBACK, and AFTER_COMPLETION. The `ApplicationEventPublisherAware` interface provides access to the event publisher.

## Transaction Management

Spring provides declarative transaction management via @Transactional. The TransactionInterceptor intercepts method calls and manages transactions. The PlatformTransactionManager abstraction supports JDBC (DataSourceTransactionManager), JPA (JpaTransactionManager), and JTA (JtaTransactionManager). The @Transactional annotation specifies propagation (REQUIRED, REQUIRES_NEW, NESTED), isolation level, timeout, and rollback rules.

Transaction synchronization registers callbacks (afterCommit, afterCompletion). Programmatic transaction management uses TransactionTemplate or TransactionManager directly. The @EnableTransactionManagement annotation enables proxy-based transaction handling. Spring's transaction infrastructure integrates with JTA for distributed transactions.

The `@Transactional` annotation supports: propagation behavior, isolation level, timeout, read-only flag, and rollback rules. The `rollbackFor` attribute specifies which exceptions trigger rollback. The `noRollbackFor` attribute specifies exceptions that do not trigger rollback. The `@ReadOnly` optimization hints at the database for read-only transactions.

Transaction propagation defines how transactions relate to each other. REQUIRED (default) joins existing or creates new. REQUIRES_NEW always creates new. NESTED creates savepoints. NOT_SUPPORTED suspends existing. NEVER throws exception if existing. SUPPORTS joins existing or runs non-transactional.

## Spring MVC

DispatcherServlet is the front controller. It maps requests to handler methods via HandlerMapping. HandlerAdapter executes handlers. ViewResolver resolves view names to View objects. The request lifecycle: DispatcherServlet -> HandlerMapping -> HandlerAdapter -> Controller -> Model -> ViewResolver -> View -> Response.

Spring MVC supports annotation-based controllers (@Controller, @RestController). @RequestMapping maps HTTP methods and paths. @RequestBody and @ResponseBody use HttpMessageConverter for JSON/XML serialization. ExceptionHandler provides centralized error handling. Interceptors (HandlerInterceptor) provide pre/post request processing.

The `@PathVariable` annotation extracts URL path variables. The `@RequestParam` annotation extracts query parameters. The `@RequestHeader` annotation extracts HTTP headers. The `@ModelAttribute` annotation binds form data. The `@ResponseBody` annotation uses HttpMessageConverter for response serialization. The `ResponseEntity` class provides full control over HTTP responses.

Spring MVC supports content negotiation. The `ContentNegotiationConfigurer` configures media type resolution. The `@RequestMapping(produces = "...")` specifies response content type. The `@RequestMapping(consumes = "...")` specifies request content type. The `HttpMessageConverter` interface converts between Java objects and HTTP messages.

## Spring Security Architecture

SecurityFilterChain processes HTTP requests through a chain of filters. The FilterChainProxy manages multiple security filter chains. Key filters: SecurityContextPersistenceFilter, UsernamePasswordAuthenticationFilter, BasicAuthenticationFilter, ExceptionTranslationFilter, FilterSecurityInterceptor.

AuthenticationManager delegates to AuthenticationProvider implementations. UserDetailsService loads user-specific data. PasswordEncoder handles password hashing. The SecurityContext holds the Authentication object. Method security (@PreAuthorize, @PostAuthorize) uses AOP to enforce access rules.

The `@EnableWebSecurity` annotation enables web security configuration. The `SecurityFilterChain` bean defines the filter chain. The `@PreAuthorize` annotation uses SpEL expressions for access control. The `@Secured` annotation provides role-based access control. The `@RolesAllowed` annotation (JSR-250) provides standard role-based access control.

Spring Security supports OAuth2 and OIDC. The `@EnableOAuth2Client` annotation enables OAuth2 client. The `@EnableOAuth2Sso` annotation enables OAuth2 SSO. The `@EnableResourceServer` annotation enables resource server. The `JwtDecoder` decodes JWT tokens. The `OAuth2AuthorizationService` manages authorization.
