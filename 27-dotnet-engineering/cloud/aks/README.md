## Azure Kubernetes Service (AKS)

Managed Kubernetes container orchestration service for deploying and managing containerized .NET applications.

## Overview

AKS simplifies deploying and managing Kubernetes clusters. It handles critical tasks like health monitoring, maintenance, and upgrades, letting you focus on application development.

## Why It Matters

- Managed Kubernetes control plane
- Auto-scaling for pods and nodes
- Integration with Azure services
- Enterprise security features
- CI/CD pipeline integration

## Key Concepts

- **Pod**: Smallest deployable unit
- **Deployment**: Manages pod replicas and updates
- **Service**: Network endpoint for pods
- **Ingress**: HTTP routing to services
- **ConfigMap/Secret**: Configuration management
- **Namespace**: Resource isolation

## Core Topics

- AKS cluster creation and configuration
- Deploying .NET containers
- Service and Ingress configuration
- Horizontal pod autoscaling
- ConfigMaps and Secrets management
- Health probes (liveness, readiness)
- CI/CD with Azure DevOps or GitHub Actions

## Best Practices

- Use managed identities for Azure access
- Implement resource limits and requests
- Use liveness and readiness probes
- Deploy with rolling update strategy
- Monitor with Container Insights

## Hands-on Labs

- Deploy a .NET app to AKS
- Configure horizontal pod autoscaling
- Set up ConfigMaps and Secrets
- Implement health probes

## Interview Questions

1. What is the difference between a Pod and a Deployment?
2. How does horizontal pod autoscaling work?
3. What are liveness and readiness probes?

## References

- https://learn.microsoft.com/azure/aks/
- https://learn.microsoft.com/azure/aks/concepts-clusters-workloads
- https://learn.microsoft.com/azure/aks/tutorial-kubernetes-deploy-app
