# WAR File Deployment

## Overview

WAR (Web Application Archive) files package Java web applications for deployment to servlet containers and Java EE application servers. The WAR format standardizes application structure and dependencies.

## WAR Structure

A WAR file contains compiled classes, libraries, static resources, and configuration files in a standardized directory layout. The web.xml descriptor configures servlets, filters, and other components.

```
myapp.war
  WEB-INF/
    classes/
    lib/
    web.xml
  META-INF/
  index.html
  css/
  js/
```

## Deployment Process

Deploying a WAR involves copying it to the application server's deployment directory or using administrative tools. The server expands the archive and configures the application context.

## Hot Deployment

Most application servers support hot deployment where new or updated WAR files deploy without server restart. This accelerates development iterations but may require careful management of class loading.

## Class Loading

Application servers use hierarchical class loading for WAR deployments. The WAR class loader isolates application classes from server classes and other deployed applications.

## Configuration

External configuration through JNDI resources, environment variables, or external configuration files prevents rebuilding WARs for environment-specific settings.

## Limitations

WAR deployment ties applications to specific servlet container APIs. Class loading issues, library conflicts, and server-specific behaviors can complicate multi-server deployment.

## Modern Alternatives

Executable JARs with embedded servers (Spring Boot) and container deployment have largely replaced WAR deployment for new applications. WAR deployment remains common in enterprise Java EE environments.
