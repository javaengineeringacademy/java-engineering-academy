# Module 22: Kubernetes

## Overview

This module covers Kubernetes, the container orchestration platform for managing containerized applications at scale. Students will learn about pods, services, deployments, and configuration management for building resilient, scalable cloud-native applications.

## Learning Objectives

By the end of this module, you will be able to:

- Understand Kubernetes architecture and components
- Deploy and manage applications using pods
- Create services for network exposure
- Implement rolling updates and rollbacks
- Manage application configuration and secrets
- Use Helm for package management
- Monitor and troubleshoot cluster health

## Prerequisites

- [Module 21: Docker](../21-docker/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [K8s Fundamentals](01-k8s-fundamentals/) | 2 hours | Architecture, components, CLI |
| 02 | [Pods](02-k8s-pods/) | 2 hours | Pod lifecycle, multi-container pods |
| 03 | [Services](03-k8s-services/) | 2 hours | ClusterIP, NodePort, LoadBalancer |
| 04 | [Deployments](04-k8s-deployments/) | 2 hours | ReplicaSets, rolling updates, scaling |
| 05 | [ConfigMaps](05-k8s-configmaps/) | 2 hours | Configuration, secrets, environment variables |
| 06 | [Helm](06-helm/) | 2 hours | Charts, releases, package management |

## Key Concepts

- Declarative vs. imperative management
- Desired state and reconciliation
- Service discovery and load balancing
- Horizontal pod autoscaling
- Resource requests and limits

## Enterprise Applications

Kubernetes is the industry standard for container orchestration, enabling organizations to deploy, scale, and manage containerized applications across hybrid and multi-cloud environments with high availability.

## Estimated Total Time

**12 hours**

## Module Project

Build a **Kubernetes Deployment** that:
- Defines pods and deployments for Java services
- Creates services for internal and external access
- Implements ConfigMaps and Secrets management
- Demonstrates rolling updates and scaling
- Uses Helm for application packaging

## Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/home/)
- [Helm Documentation](https://helm.sh/docs/)

**Previous Module**: [Module 21: Docker](../21-docker/)
**Next Module**: [Module 23: AWS](../23-aws/)