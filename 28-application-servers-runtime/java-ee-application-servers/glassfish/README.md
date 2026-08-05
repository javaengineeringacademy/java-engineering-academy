# GlassFish

## Overview

GlassFish is the open-source reference implementation of Java EE, originally developed by Sun Microsystems and now maintained by the Eclipse Foundation. It provides the baseline implementation for Java EE specification compliance.

## History

GlassFish originated as Sun's reference implementation and served as the proving ground for Java EE features. Oracle continued development before donating the project to Eclipse Foundation in 2013.

## Architecture

GlassFish uses a modular architecture based on OSGi (HK2 kernel). Components are registered as services that can be dynamically loaded and managed at runtime.

## Administration

GlassFish provides a web-based Administration Console and the asadmin command-line tool for configuration and management. Both tools support all administrative operations.

## Deployment

GlassFish deploys applications through the admin console, asadmin commands, or hot-deployment by dropping files in the autodeploy directory. Deployment descriptors and annotations configure applications.

## Clustering

GlassFish supports clustering with session replication, load balancing, and high availability. The HA session store provides in-memory and persistent session storage options.

## Apache Derby

GlassFish includes Apache Derby (JavaDB) as its default database. Derby provides embedded database capabilities for development and testing without external database dependencies.

## Community vs Commercial

Oracle stopped commercial support for GlassFish in 2013. The Eclipse GlassFish project continues community development, with Payara providing commercially supported derivatives.
