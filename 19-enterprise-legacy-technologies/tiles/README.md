# Apache Tiles

## Overview

Apache Tiles is a templating framework for Java web applications that enables composition of pages from reusable tile definitions. It provides a mechanism for defining page layouts as reusable templates, allowing consistent structure across multiple pages while separating layout concerns from content.

## History

Tiles originated as part of the Struts framework in 2000 as "Struts Tiles." It was extracted into a standalone project in 2002. Tiles 1.0 provided basic template composition. Tiles 2.0 (2006) introduced theDefinitionBean API and improved configuration. Tiles 2.2 (2009) added expression language support. Tiles 3.0 (2012) aligned with Servlet 3.0 specifications. Apache declared Tiles end-of-life in 2020.

## Why It Is Considered Legacy

Tiles relies heavily on XML configuration for definition files, making maintenance cumbersome. The template model assumes server-side rendering, conflicting with modern client-side architectures. Integration with newer frameworks requires adapter code. Expression language support is limited compared to template engines like Thymeleaf. The project received minimal community activity before end-of-life.

## Key Concepts

- **Definitions**: Named configurations specifying base templates and attribute values for page composition
- **Attributes**: Named parameters passed to templates, supporting string, template, and definition types
- **Base Templates**: JSP or Velocity pages defining the page structure with insertable attribute placeholders
- **Preparer**: Classes that execute before rendering to modify definitions or attributes dynamically
- **Wildcard Definitions**: Pattern-based definitions that match URL patterns to template configurations
- **TilesContainer**: Core container managing definition loading, attribute resolution, and rendering

## When It Was Used

Tiles was heavily integrated with Struts and Spring MVC applications from 2002 through 2015. Enterprise applications used Tiles for consistent page layouts across large portal systems. Banking, insurance, and government web applications relied on Tiles for header, footer, navigation, and content area composition. Any application using Struts typically adopted Tiles for layout management.

## Why It Was Replaced

Thymeleaf's natural template approach provides layout composition without XML configuration. Thymeleaf Layout Dialect and Spring Boot's view resolvers handle template inheritance natively. Client-side rendering with React, Angular, or Vue.js uses component composition rather than server-side templates. Single-page applications eliminate the need for server-side page assembly.

## Migration Path

Replace Tiles definitions with Thymeleaf layout fragments using th:fragment and th:replace attributes. Convert Tiles attribute passing to Thymeleaf template variables. Remove Tiles XML configuration files and use Spring Boot auto-configuration for Thymeleaf. Test each page migration independently by comparing rendered output. For Spring MVC applications, configure ThymeleafViewResolver and remove Tiles dependency.

## Modern Alternative

Thymeleaf with Layout Dialect provides server-side template composition with natural template syntax. Client-side frameworks (React, Angular, Vue) use component-based composition for page assembly. Server-side rendering frameworks like Next.js and Nuxt.js handle layout composition in JavaScript ecosystems. Spring Boot auto-configures Thymeleaf without explicit Tiles setup.

## Interview Questions

1. How does Tiles template composition differ from Thymeleaf layout fragments?
2. What is the role of a Tiles Preparer, and when would you implement one?
3. Explain how Tiles wildcard definitions simplify configuration for applications with many pages.
4. What challenges arise when migrating a Tiles-based application to client-side rendering?
5. Why did Tiles decline in popularity despite its integration with popular frameworks?

## References

- Apache Tiles Official Documentation
- Spring MVC: View Technologies with Tiles
- Thymeleaf Layout Dialect Documentation
- Baeldung: Tiles to Thymeleaf Migration
