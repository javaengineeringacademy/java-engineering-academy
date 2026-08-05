# Spring Framework Version History

## Spring 1.0
- **Release Date:** October 1, 2002
- **Features:** Inversion of Control (IoC) container, dependency injection, bean factory, application context, AOP support, JDBC abstraction, transaction management, MVC framework, remote access (RMI, HTTP invoker)
- **Deprecated:** N/A (initial release)
- **Removed:** N/A
- **Performance:** Lightweight container compared to EJB
- **Security:** Declarative transaction management
- **Why Introduced:** Rod Johnson created Spring as a simpler alternative to J2EE, addressing complexity of enterprise Java development

## Spring 1.2
- **Release Date:** March 2003
- **Features:** Improved AOP, better XML configuration, enhanced transaction management, JMX support
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** AOP proxy improvements
- **Security:** Transaction management improvements
- **Why Introduced:** AOP and transaction management improvements

## Spring 2.0
- **Release Date:** October 2006
- **Features:** XML Schema support, new bean scopes, AOP improvements, JMX enhancements, Java 5 support (generics, annotations), new expression language (SpEL), portlet support
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Java 5 generics for type-safe collections
- **Security:** SpEL for dynamic configuration
- **Why Introduced:** Java 5 support and SpEL for more powerful configuration

## Spring 2.5
- **Release Date:** November 2007
- **Features:** Annotation-driven configuration (@Autowired, @Component, @Service, @Repository, @Controller), component scanning, JSR-250 support (@Resource, @PostConstruct, @PreDestroy), OXM support, JPA support
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Annotation-based configuration reduces XML
- **Security:** JSR-250 annotations for standard dependency injection
- **Why Introduced:** Annotation-driven configuration to reduce XML verbosity

## Spring 3.0
- **Release Date:** December 2009
- **Features:** Java 5+ baseline, expression language (SpEL), REST support (@ResponseBody, @RequestMapping), MVC improvements, validation (JSR-303), Java EE 6 support, environment profiles, property placeholders
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** REST support for web services
- **Security:** Bean validation for input validation
- **Why Introduced:** REST support for web services, Java EE 6 integration

## Spring 3.1
- **Release Date:** December 2011
- **Features:** Environment abstraction, @Profile, caching support (@Cacheable), @Configuration class enhancement, web application context hierarchy
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Caching abstraction for performance improvement
- **Security:** Environment profiles for safer configuration
- **Why Introduced:** Caching support and environment abstraction

## Spring 3.2
- **Release Date:** November 2012
- **Features:** Async MVC processing, Servlet 3.0 support, deferred results, content negotiation, @ControllerAdvice, @DateTimeFormat
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Async processing for non-blocking requests
- **Security:** @ControllerAdvice for cross-cutting security concerns
- **Why Introduced:** Async MVC for better web application performance

## Spring 4.0
- **Release Date:** December 2013
- **Features:** Java 8 support (lambdas, streams), WebSocket support, @Conditional annotations, generic injection, @Order, @Priority, Spring Expression Language improvements, JSR-310 support
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Java 8 lambdas for concise configuration
- **Security:** WebSocket security support
- **Why Introduced:** Java 8 support, WebSocket for real-time communication

## Spring 4.1
- **Release Date:** September 2014
- **Features:** JMS improvements, @JmsListener annotation, WebSocket STOMP improvements, messaging abstraction, caching improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** JMS improvements for messaging
- **Security:** Messaging security improvements
- **Why Introduced:** Messaging and caching improvements

## Spring 4.2
- **Release Date:** October 2015
- **Features:** HTTP cache support, @Caching, WebSocket improvements, Java 8 Optional support in MVC, Spring Test improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** HTTP caching for web performance
- **Security:** WebSocket security enhancements
- **Why Introduced:** HTTP caching and WebSocket improvements

## Spring 4.3
- **Release Date:** July 2016
- **Features:** @GetMapping/@PostMapping etc., constructor injection implicit, @RequestParam improvements, @SessionAttributes improvements, @CrossOrigin improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Annotation simplification reduces boilerplate
- **Security:** CORS improvements
- **Why Introduced:** MVC annotation simplification

## Spring 5.0
- **Release Date:** September 2017
- **Features:** Reactive programming (Spring WebFlux), JDK 9+ support, HTTP/2 support, Kotlin support, Spring MVC 5, Java EE 8 support, JUnit 5 support, Null safety annotations
- **Deprecated:** Spring MVC 4 (still supported)
- **Removed:** N/A
- **Performance:** Reactive programming for non-blocking I/O
- **Security:** Reactive security model
- **Why Introduced:** Reactive programming for modern web applications

## Spring 5.1
- **Release Date:** September 2018
- **Features:** Java 11 support, JDK 11 support, reactive security improvements, web flux improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** JDK 11 optimizations
- **Security:** Reactive security improvements
- **Why Introduced:** JDK 11 support and reactive improvements

## Spring 5.2
- **Release Date:** October 2019
- **Features:** Reactive Streams support improvements, Spring WebFlux improvements, Jakarta EE support preparation
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Reactive Streams optimizations
- **Security:** Security improvements
- **Why Introduced:** Reactive and Jakarta EE preparation

## Spring 5.3
- **Release Date:** November 2020
- **Features:** GraalVM native image support (experimental), Spring Boot 2.4+ improvements, Kotlin coroutines support, reactive security enhancements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** GraalVM native image for faster startup
- **Security:** Security enhancements
- **Why Introduced:** Native image support, Kotlin coroutines integration

## Spring 6.0
- **Release Date:** November 2022
- **Features:** Jakarta EE 9+ support, Java 17 baseline, GraalVM native image (stable), virtual threads support (preview), Spring Boot 3.0, Micrometer improvements
- **Deprecated:** javax.* namespace (replaced by jakarta.*)
- **Removed:** javax.* namespace (migrated to jakarta.*)
- **Performance:** Virtual threads for improved concurrency
- **Security:** Jakarta EE 9+ security improvements
- **Why Introduced:** Jakarta EE migration, Java 17 baseline, virtual threads support

## Spring 6.1
- **Release Date:** November 2023
- **Features:** Virtual threads (stable), improved GraalVM support, observability improvements, Kotlin improvements, Spring Framework 6.1 with Spring Boot 3.2
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Virtual threads for high-concurrency workloads
- **Security:** Observability improvements for security monitoring
- **Why Introduced:** Virtual threads stability, observability enhancements
