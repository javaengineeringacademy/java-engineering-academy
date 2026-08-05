# Kubernetes Deployment

## Overview

Kubernetes is an open-source container orchestration platform that automates deployment, scaling, and management of containerized applications. It provides self-healing, load balancing, and automated rollouts.

## Core Concepts

- Pods: smallest deployable units containing one or more containers
- Services: stable network endpoints for accessing pods
- Deployments: declarative updates for pods and replicas
- ConfigMaps and Secrets: configuration and credential management

## Deployment Configuration

Kubernetes deployments use YAML manifests defining desired state. The deployment controller ensures the actual state matches the declared specification.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: myapp
spec:
  replicas: 3
  selector:
    matchLabels:
      app: myapp
  template:
    spec:
      containers:
      - name: myapp
        image: myapp:1.0
        ports:
        - containerPort: 8080
```

## Scaling

Horizontal Pod Autoscaler (HPA) adjusts replica count based on CPU, memory, or custom metrics. Cluster Autoscaler adds or removes nodes based on pod scheduling requirements.

## Service Discovery

Kubernetes provides DNS-based service discovery. Services receive DNS names, and kube-proxy handles load balancing across healthy pods.

## Rolling Updates

Deployments support rolling updates with configurable surge and unavailable parameters. Health checks ensure new pods are ready before old pods are terminated.

## Configuration Management

ConfigMaps and Secrets inject configuration into pods as environment variables or mounted files. This separates configuration from container images for environment flexibility.

## Monitoring and Logging

Prometheus, Grafana, and centralized logging (EFK stack) provide monitoring for Kubernetes clusters. Kubernetes dashboards and kubectl commands offer operational visibility.
