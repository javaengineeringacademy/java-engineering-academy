# Servlets (Java Servlet API)

## Overview

Java Servlets are server-side Java classes that handle HTTP requests and generate dynamic responses. They form the foundation of Java web applications, providing a programmatic interface for request processing, session management, and response generation within a servlet container.

## History

The Servlet API was introduced in 1997 with Servlet 1.0 as part of the Java Development Kit. Servlet 2.0 (1997) added filters and listeners. Servlet 2.3 (2000) introduced the Filter API. Servlet 2.4 (2003) aligned with JSP 2.0. Servlet 3.0 (2009) added annotation-based configuration and async support. Servlet 3.1 (2013) and 4.0 (2017) followed with HTTP/2 support.

## Why It Is Considered Legacy

Raw servlets require extensive boilerplate for request parsing, response construction, and routing. URL mapping in web.xml is verbose and error-prone. Threading model complexity leads to concurrency bugs. No built-in support for dependency injection, validation, or content negotiation. Unit testing servlets requires heavy container mocking frameworks.

## Key Concepts

- **HttpServlet**: Abstract class providing doGet, doPost, doPut, doDelete methods for HTTP method handling
- **Servlet Lifecycle**: init() for initialization, service() for request handling, destroy() for cleanup
- **Request and Response Objects**: HttpServletRequest and HttpServletResponse provide headers, parameters, cookies, and output streams
- **Filters**: ServletRequestFilter and ServletResponseFilter intercept requests and responses for preprocessing and postprocessing
- **Listeners**: ServletContextListener, HttpSessionListener, and ServletRequestListener respond to container and session events
- **Async Processing**: Servlet 3.0 introduced async context for long-running requests without blocking container threads

## When It Was Used

Servlets were the backbone of all Java web applications from 1997 through the 2010s. Every JSP page compiled to a servlet. Frameworks like Struts, Spring MVC, and JSF built on top of the Servlet API. Banking systems, e-commerce platforms, and enterprise portals all relied on servlet containers for request processing.

## Why It Was Replaced

Spring MVC provides annotation-driven controllers, automatic parameter binding, content negotiation, and exception handling that eliminate servlet boilerplate. Frameworks like Spring Boot embed Tomcat and auto-configure applications. Microservices architectures prefer lightweight HTTP clients over servlet containers. Reactive frameworks (WebFlux, Vert.x) handle high concurrency without servlet thread-per-request limitations.

## Migration Path

Replace HttpServlet subclasses with Spring MVC @Controller or @RestController classes. Move request handling logic into service layer beans. Replace web.xml servlet mappings with @RequestMapping annotations. Adopt Spring Boot for embedded container management. Migrate filters to Spring HandlerInterceptor or @Component Filter beans.

## Modern Alternative

Spring Boot with Spring MVC provides the most natural migration path for servlet-based applications. Reactive alternatives include Spring WebFlux, Vert.x, and Quarkus. For cloud-native applications, serverless functions (AWS Lambda, Azure Functions) and service meshes handle HTTP processing without traditional containers.

## Interview Questions

1. Describe the servlet lifecycle and the role of init(), service(), and destroy() methods.
2. How do servlet filters differ from interceptors, and when would you use each?
3. Explain the threading model of servlets and the concurrency issues it creates.
4. What improvements did Servlet 3.0 introduce for modern web application development?
5. How does Spring MVC abstract away servlet-level concerns while still running on top of the Servlet API?

## References

- Oracle: Java Servlet Technology Documentation
- Jakarta Servlet Specification (JSR 369)
- Baeldung: Spring MVC Tutorial
- Apache Tomcat Servlet Container Documentation
