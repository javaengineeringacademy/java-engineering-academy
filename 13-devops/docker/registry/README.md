# Docker Registry

## Overview

A Docker registry is a storage and distribution system for Docker images. Docker Hub is the default registry, but you can host private registries.

## Docker Hub

```bash
# Login
docker login

# Tag image
docker tag my-app:latest username/my-app:1.0

# Push image
docker push username/my-app:1.0

# Pull image
docker pull username/my-app:1.0
```

## Harbor Registry

### Installation
```bash
# Download Harbor
wget https://github.com/goharbor/harbor/releases/download/v2.9.0/harbor-offline-installer-v2.9.0.tgz

# Extract and configure
tar xzf harbor-offline-installer-v2.9.0.tgz
cd harbor
cp harbor.yml.tmpl harbor.yml

# Edit harbor.yml
# hostname: harbor.example.com
# harbor_admin_password: Harbor12345

# Install
./install.sh --with-trivy
```

### Push to Harbor
```bash
# Login
docker login harbor.example.com

# Tag image
docker tag my-app:latest harbor.example.com/myproject/my-app:1.0

# Push image
docker push harbor.example.com/myproject/my-app:1.0
```

## AWS ECR

```bash
# Create repository
aws ecr create-repository --repository-name my-app

# Login to ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin \
  123456789012.dkr.ecr.us-east-1.amazonaws.com

# Tag image
docker tag my-app:latest \
  123456789012.dkr.ecr.us-east-1.amazonaws.com/my-app:1.0

# Push image
docker push \
  123456789012.dkr.ecr.us-east-1.amazonaws.com/my-app:1.0
```

## Google Container Registry

```bash
# Configure Docker
gcloud auth configure-docker

# Tag image
docker tag my-app:latest gcr.io/my-project/my-app:1.0

# Push image
docker push gcr.io/my-project/my-app:1.0
```

## Azure Container Registry

```bash
# Create ACR
az acr create --resource-group myResourceGroup \
  --name myregistry --sku Standard

# Login
az acr login --name myregistry

# Tag image
docker tag my-app:latest myregistry.azurecr.io/my-app:1.0

# Push image
docker push myregistry.azurecr.io/my-app:1.0
```

## Best Practices

1. **Use private registries** - Don't expose sensitive images
2. **Enable image scanning** - Scan for vulnerabilities
3. **Implement access control** - Use RBAC
4. **Use image signing** - Verify image integrity
5. **Implement image retention** - Clean up old images
6. **Use replication** - Mirror images across registries
7. **Monitor registry activity** - Track pulls and pushes
8. **Use webhooks** - Notify on image events
9. **Implement caching** - Speed up pulls
10. **Document image tags** - Use semantic versioning
