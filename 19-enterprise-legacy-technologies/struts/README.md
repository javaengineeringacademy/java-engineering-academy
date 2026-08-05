# Struts (Apache Struts)

## Overview

Apache Struts is a server-side Java web application framework that implements the Model-View-Controller (MVC) pattern. It provides a structure for developing maintainable Java web applications using action classes, form beans, and XML-configured navigation, separating presentation from business logic.

## History

Struts 1.0 was released in June 2000 as an open-source framework by Craig McClanahan. It quickly became the dominant Java web framework through the mid-2000s. Struts 1.3 (2006) was the final 1.x release. Struts 2.0 (2007) merged with WebWork, introducing a new architecture based on XWork. Struts 2.5 (2016) addressed critical security vulnerabilities. Apache announced end-of-life for Struts in 2022.

## Why It Is Considered Legacy

Struts 1 has a rigid action-based architecture that limits flexibility. Configuration through struts-config.xml is verbose and error-prone. Form beans create tight coupling between action classes and presentation. Struts 2 suffered severe security vulnerabilities (CVE-2017-5638, CVE-2018-11776) that damaged enterprise confidence. The community has shifted to Spring MVC and other modern frameworks.

## Key Concepts

- **Action Classes**: Server-side components handling HTTP requests and returning ActionForward objects specifying the next view
- **ActionForm Beans**: JavaBeans that capture and validate user input from HTML forms, automatically populated by the framework
- **ActionMapping**: Configuration objects defining URL patterns, action classes, form beans, and navigation outcomes
- **ActionForward**: Objects representing navigation targets including pages, redirects, and action URIs
- **Tiles Integration**: Template composition framework providing consistent layouts across pages (separate technology)
- **Validator Framework**: Declarative validation rules configured in XML, applied to ActionForm properties

## When It Was Used

Struts was the standard Java web framework from 2000 to 2010. Enterprise applications in banking, insurance, telecommunications, and government adopted it extensively. Banking portals, insurance claim systems, HR applications, and government services all relied on Struts for request handling and view navigation. Struts 2 continued in enterprise environments through the mid-2010s.

## Why It Was Replaced

Spring MVC offers annotation-driven controllers, dependency injection, and flexible view resolution. Struts 2 security vulnerabilities caused significant data breaches in major enterprises. The action-based model does not support RESTful architectures cleanly. Modern frameworks provide better testing support, less configuration, and stronger community maintenance.

## Migration Path

Replace Struts Action classes with Spring MVC @Controller or @RestController classes. Convert ActionForm beans to Spring model objects with Bean Validation annotations. Replace struts-config.xml navigation with Spring MVC view resolvers and @RequestMapping. Migrate Tiles layouts to Thymeleaf template fragments. Update validation logic to use JSR 380 Bean Validation.

## Modern Alternative

Spring MVC with Spring Boot provides the most common migration target for Struts applications. JAX-RS (Jersey, RESTEasy) offers RESTful web services for API-first architectures. Microservices frameworks like Spring Boot, Quarkus, and Micronaut handle modern distributed application requirements.

## Interview Questions

1. Explain the Struts MVC architecture and the role of Action, ActionForm, and ActionForward classes.
2. What security vulnerabilities affected Struts 2, and how did they impact enterprise adoption?
3. How does Spring MVC's annotation-driven approach differ from Struts' XML configuration model?
4. What are the challenges of migrating a large Struts 1 application to Spring MVC?
5. Why did Struts 2 fail to recover enterprise confidence after the security incidents?

## References

- Apache Struts Official Documentation
- Oracle: Struts Migration Guide to Spring MVC
- NIST CVE Database: Struts Security Vulnerabilities
- Baeldung: Spring MVC vs Struts Comparison
