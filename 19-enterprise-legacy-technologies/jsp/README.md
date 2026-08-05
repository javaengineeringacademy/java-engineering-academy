# JSP (JavaServer Pages)

## Overview

JavaServer Pages (JSP) is a server-side technology for creating dynamic web content using HTML, XML, and Java code. JSP pages are translated into servlets at runtime, allowing developers to embed Java expressions and scriptlets directly within markup for rapid web application development.

## History

JSP was first released in 1999 as part of the J2EE platform. Version 1.2 arrived in 2001, followed by JSP 2.0 in 2003, which introduced Expression Language (EL) and the JSP Standard Tag Library (JSTL). JSP 2.1 shipped in 2006 with JSP 2.2 following in 2009, aligning with Java EE 6 specifications.

## Why It Is Considered Legacy

JSP mixes presentation with business logic through scriptlets, making code difficult to maintain and test. The tag-based approach feels verbose compared to modern template engines. Poor separation of concerns leads to sprawling JSP files that are hard to refactor and debug. Debugging scriptlet errors provides cryptic stack traces that point to generated servlet code rather than the original JSP source.

## Key Concepts

- **Scriptlets**: Inline Java code blocks enclosed in `<% %>` tags within HTML markup
- **Expression Language (EL)**: A concise syntax `${expression}` for accessing beans, request attributes, and implicit objects
- **JSTL (JSP Standard Tag Library)**: A set of standard tags for common operations like iteration, conditionals, and formatting
- **Tag Libraries (TLD)**: Custom tags defined by developers to encapsulate reusable UI or processing logic
- **Directives**: Page-level instructions (`<%@ page %>`, `<%@ include %>`) that control translation and compilation behavior
- **Implicit Objects**: Predefined objects like `request`, `response`, `session`, `out`, and `application` available in every JSP page

## When It Was Used

JSP dominated enterprise web development from 1999 through the mid-2010s. It was the standard presentation layer for J2EE applications, powering banking portals, e-commerce sites, government systems, and internal enterprise dashboards. Virtually every Java web framework initially relied on JSP for rendering views.

## Why It Was Replaced

Template engines like Thymeleaf, FreeMarker, and Mustache offer cleaner separation of concerns without embedding executable code in markup. Server-side rendering frameworks (JSF, Vaadin) abstract HTTP handling entirely. Single-page applications using Angular, React, and Vue moved rendering to the client, eliminating server-rendered pages. Spring MVC decoupled controllers from views, making JSP unnecessary for view resolution.

## Migration Path

Migrate incrementally by replacing JSP files with Thymeleaf templates, which integrate seamlessly with Spring MVC. Replace scriptlets with controller logic in Spring `@Controller` methods. Convert JSTL tags to Thymeleaf attributes. Use Spring Boot's embedded Tomcat to simplify deployment. Test each migrated view independently before removing the original JSP file.

## Modern Alternative

Thymeleaf is the primary server-side replacement, offering natural template syntax that works in browsers without server processing. For client-side rendering, React, Angular, and Vue.js provide component-based architectures with rich ecosystem support. Server-side rendering frameworks like Next.js handle SEO and initial load performance.

## Interview Questions

1. How do JSP scriptlets differ from Expression Language, and why is EL preferred for modern applications?
2. Explain the lifecycle of a JSP page from first request through compilation and execution.
3. What problems arise from mixing business logic in JSP files, and how does the MVC pattern address them?
4. Describe how JSTL custom tags improve maintainability compared to raw scriptlets.

## References

- Oracle Java EE Documentation: JavaServer Pages Technology
- Baeldung: JSP Tutorial
- Spring Framework: View Technologies Documentation
- Apache Tomcat JSP Container Specification
