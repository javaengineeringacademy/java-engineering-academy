# WildFly (JBoss)

## Overview

WildFly, formerly JBoss Application Server, is a lightweight Java EE application server developed by Red Hat. It provides full Java EE compliance with fast startup and low memory consumption.

## Architecture

WildFly uses a modular architecture with a custom module system for class loading. Subsystems provide specific functionality like web serving, messaging, and transaction management.

## Management

WildFly provides CLI (Command Line Interface), web-based Management Console, and Management API for administration. Configuration is centralized in standalone.xml or domain.xml files.

## Domain Mode

Domain mode enables centralized management of multiple WildFly instances. A Domain Controller manages host configurations and distributes application deployments across the domain.

## Undertow Subsystem

WildFly uses Undertow as its web server subsystem. Undertow provides high-performance HTTP handling with servlet container capabilities and WebSocket support.

## Messaging

WildFly includes Apache ActiveMQ Artemis as its messaging subsystem. It provides full JMS 2.0 support with clustering, persistence, and transactional messaging capabilities.

## Clustering

WildFly clustering supports session replication, distributed caching with Infinispan, and singleton services. JGroups provides the communication layer for cluster coordination.

## Standalone vs Domain

Standalone mode suits single-server deployments with independent configuration. Domain mode coordinates multiple servers for centralized management and consistent configuration across a cluster.
