# EAR File Deployment

## Overview

EAR (Enterprise Application Archive) files package multi-module Java EE applications combining web modules (WARs) and EJB modules (JARs). EAR deployment provides enterprise-level services like transaction management and security.

## EAR Structure

An EAR file contains one or more WAR and JAR modules along with a deployment descriptor (application.xml). Each module deploys to its appropriate container within the application server.

```
myapp.ear
  application.xml
  web-module.war
  ejb-module.jar
  lib/
    shared-library.jar
```

## Module Types

- Web modules (WAR) handle HTTP requests and user interface
- EJB modules (JAR) contain business logic and data access
- Client modules (JAR) provide application clients
- Resource adapter modules (RAR) integrate with external systems

## Deployment Descriptors

The application.xml file defines module structure, security roles, and resource mappings. Individual modules have their own descriptors (web.xml, ejb-jar.xml) for component configuration.

## Class Loading

EAR deployments use a parent-first class loading model. The EAR class loader loads shared libraries before delegating to module class loaders for module-specific classes.

## Transaction Management

EAR deployments leverage container-managed transactions for EJB modules. Transaction boundaries are configured declaratively or programmatically for enterprise operations.

## Security

EAR deployments implement Java EE security with role-based access control. Security constraints protect web resources, and EJB methods enforce authorization requirements.

## Modern Usage

EAR deployment is less common in cloud-native architectures. Microservices and Spring Boot have replaced monolithic EAR deployments in many organizations, though EAR persists in conservative enterprise environments.
