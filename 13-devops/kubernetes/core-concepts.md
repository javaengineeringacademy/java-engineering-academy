# Kubernetes Core Concepts

## Pods

The smallest deployable unit in Kubernetes. A Pod represents one or more containers that share storage, network, and a specification for how to run. Pods are ephemeral and can be terminated and replaced at any time. Each Pod gets a unique IP address within the cluster.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
  labels:
    app: my-app
spec:
  containers:
  - name: my-app
    image: nginx:latest
    ports:
    - containerPort: 80
```

## Deployments

A Deployment provides declarative updates for Pods and ReplicaSets. You describe a desired state, and the Deployment controller changes the actual state to the desired state at a controlled rate. Supports rolling updates, rollbacks, and scaling.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.21
        resources:
          requests:
            memory: "64Mi"
            cpu: "250m"
          limits:
            memory: "128Mi"
            cpu: "500m"
```

## Services

An abstract way to expose an application running on a set of Pods. Services provide a stable IP address and DNS name. Types include ClusterIP (internal), NodePort (external via node), LoadBalancer (cloud provider), and ExternalName (DNS alias).

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  selector:
    app: my-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: ClusterIP
```

## ConfigMaps

Stores non-confidential configuration data as key-value pairs. Pods can consume ConfigMaps as environment variables, command-line arguments, or as configuration files. ConfigMaps must be created before pods that reference them.

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  DATABASE_HOST: "mysql-service"
  DATABASE_PORT: "3306"
  config.json: |
    {"key": "value"}
```

## Secrets

Stores sensitive information like passwords, tokens, or keys. Secrets are similar to ConfigMaps but are base64 encoded. For production use, enable encryption at rest and consider external secret stores (Vault, AWS Secrets Manager).

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
type: Opaque
data:
  username: YWRtaW4=  # base64 encoded
  password: cGFzc3dvcmQ=
```

## Namespaces

A mechanism for isolating groups of resources within a single cluster. Namespaces are useful for multi-tenancy, environment separation, and applying resource quotas. Default namespaces include default, kube-system, kube-public, and kube-node-lease.

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
  labels:
    env: production
```

## RBAC (Role-Based Access Control)

Regulates access to resources based on user roles. Consists of Roles (namespace-scoped permissions), ClusterRoles (cluster-wide permissions), RoleBindings (grant Role to subjects), and ClusterRoleBindings (grant ClusterRole to subjects).

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: production
  name: pod-reader
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log"]
  verbs: ["get", "watch", "list"]
```

## StatefulSets

Manages stateful applications with guarantees about ordering and uniqueness of Pods. Provides stable network identities, stable persistent storage, and ordered deployment/scaling. Use for databases, message queues, and distributed systems.

## DaemonSets

Ensures a copy of a Pod runs on all (or specific) nodes. Used for logging agents, monitoring agents, and storage daemons. Pods are automatically added to new nodes and removed when nodes are deleted.

## Jobs and CronJobs

Jobs create one or more Pods and ensure a specified number successfully terminate. CronJobs create Jobs on a repeating schedule. Use for batch processing, data migrations, and scheduled tasks.

## Ingress

Manages external access to Services in a cluster, typically HTTP/HTTPS. Provides load balancing, SSL termination, and name-based virtual hosting. Requires an Ingress Controller (NGINX, Traefik, HAProxy).

## Labels and Selecters

Labels are key-value pairs attached to objects for organization and selection. Selectors filter objects based on labels. Used by Services, Deployments, and other resources to identify target Pods.

## Resource Management

Each container can specify resource requests and limits. Requests guarantee minimum resources; limits enforce maximum. Resource Quotas limit total resources per namespace. LimitRanges set default and maximum limits per Pod/Container.
