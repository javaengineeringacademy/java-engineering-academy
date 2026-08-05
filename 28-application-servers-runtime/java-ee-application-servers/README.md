# Java EE Application Servers Overview

## Overview

Java EE application servers provide the complete runtime environment for enterprise Java applications. They implement the full Java EE specification including EJB containers, JMS messaging, transaction management, and connection pooling.

## Core Services

Java EE servers provide EJB container services, JNDI naming, JTA transaction management, JDBC connection pooling, JMS messaging, and JavaMail support. These services enable enterprise application development.

## EJB Container

The EJB container manages enterprise bean lifecycle, transaction boundaries, security, and remote communication. It provides declarative services that simplify enterprise application development.

## Messaging

Java EE servers include JMS providers for asynchronous messaging. Message-driven beans consume JMS messages while session beans produce messages for decoupled communication.

## Management

Application servers provide administrative consoles, command-line tools, and APIs for deployment, monitoring, and configuration management. JMX support enables integration with monitoring systems.

## Major Implementations

| Server | Vendor | Open Source |
|--------|--------|-------------|
| WebLogic | Oracle | No |
| WebSphere | IBM | No |
| WildFly | Red Hat | Yes |
| GlassFish | Eclipse | Yes |
| Payara | Payara | Yes |

## Deployment

Enterprise applications deploy as EAR files containing WAR modules and EJB JARs. Application servers handle class loading, dependency injection, and resource binding during deployment.

## Modern Evolution

Jakarta EE continues the Java EE legacy with cloud-native features, lightweight profiles, and integration with modern frameworks like MicroProfile for microservices development.
