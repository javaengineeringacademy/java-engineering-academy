# Module 21: Docker

## Overview

This module covers Docker containerization for Java applications. Students will learn to create Dockerfiles, manage containers, orchestrate multi-container applications with Docker Compose, and implement container networking and storage for consistent development and deployment environments.

## Learning Objectives

By the end of this module, you will be able to:

- Create optimized Docker images for Java applications
- Write multi-stage Dockerfiles for smaller images
- Manage containers and volumes effectively
- Orchestrate services with Docker Compose
- Configure container networking and security
- Implement health checks and logging
- Integrate Docker with CI/CD pipelines

## Prerequisites

- [Module 20: Redis](../20-redis/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Docker Fundamentals](01-docker-fundamentals/) | 2 hours | Container concepts, Docker architecture |
| 02 | [Dockerfile](02-dockerfile/) | 3 hours | Instructions, multi-stage builds, optimization |
| 03 | [Docker Compose](03-docker-compose/) | 2 hours | Multi-container apps, service definitions |
| 04 | [Networking](04-docker-networking/) | 2 hours | Bridge, overlay networks, DNS resolution |
| 05 | [Volumes](05-docker-volumes/) | 2 hours | Persistent storage, bind mounts |
| 06 | [Java Docker](06-docker-java/) | 2 hours | JVM tuning in containers, base images |

## Key Concepts

- Container vs. virtual machine
- Image layering and caching
- Container orchestration basics
- Security best practices
- Resource limits and constraints

## Enterprise Applications

Docker enables consistent development environments, simplified deployments, and scalable microservices infrastructure, reducing "works on my machine" issues and accelerating release cycles.

## Estimated Total Time

**13 hours**

## Module Project

Build a **Dockerized Microservices Application** that:
- Creates optimized multi-stage Dockerfiles
- Orchestrates multiple services with Compose
- Implements health checks and logging
- Configures persistent volumes for data
- Demonstrates container networking

## Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker for Java Developers](https://docs.docker.com/engine/reference/builder/)

**Previous Module**: [Module 20: Redis](../20-redis/)
**Next Module**: [Module 22: Kubernetes](../22-kubernetes/)