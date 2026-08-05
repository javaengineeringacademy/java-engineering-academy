# Kubernetes Hands-On Labs

## Lab 1: Deploy Your First Application

### Objective
Deploy a simple Nginx application and expose it via a Service.

### Steps

```bash
# Create a deployment
kubectl create deployment nginx --image=nginx:latest

# Check deployment status
kubectl get deployments
kubectl get pods

# Expose the deployment
kubectl expose deployment nginx --port=80 --type=NodePort

# Access the service
kubectl get svc nginx
curl http://localhost:<node-port>

# Scale the deployment
kubectl scale deployment nginx --replicas=3

# Clean up
kubectl delete deployment nginx
kubectl delete svc nginx
```

## Lab 2: ConfigMaps and Secrets

### Objective
Create ConfigMaps and Secrets and use them in Pods.

### Steps

```bash
# Create ConfigMap
kubectl create configmap app-config --from-literal=DATABASE_HOST=mysql --from-literal=DATABASE_PORT=3306

# Create Secret
kubectl create secret generic app-secret --from-literal=username=admin --from-literal=password=secret123

# Use in a Pod
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Pod
metadata:
  name: config-demo
spec:
  containers:
  - name: app
    image: busybox
    command: ["sleep", "3600"]
    envFrom:
    - configMapRef:
        name: app-config
    - secretRef:
        name: app-secret
EOF

# Verify
kubectl exec config-demo -- env
```

## Lab 3: Rolling Updates and Rollbacks

### Objective
Perform a rolling update and rollback a deployment.

### Steps

```bash
# Create deployment
kubectl create deployment web --image=nginx:1.20 --replicas=3

# Update image to trigger rolling update
kubectl set image deployment/web nginx=nginx:1.21

# Watch rollout status
kubectl rollout status deployment/web

# View rollout history
kubectl rollout history deployment/web

# Rollback to previous version
kubectl rollout undo deployment/web

# Rollback to specific revision
kubectl rollout undo deployment/web --to-revision=1
```

## Lab 4: Persistent Storage

### Objective
Use PersistentVolumeClaims for stateful applications.

### Steps

```bash
# Create a PVC
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: my-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
EOF

# Use PVC in a Pod
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Pod
metadata:
  name: storage-demo
spec:
  containers:
  - name: app
    image: busybox
    command: ["sleep", "3600"]
    volumeMounts:
    - mountPath: /data
      name: my-volume
  volumes:
  - name: my-volume
    persistentVolumeClaim:
      claimName: my-pvc
EOF

# Verify
kubectl exec storage-demo -- df -h /data
```

## Lab 5: Network Policies

### Objective
Implement network policies to control traffic flow.

### Steps

```bash
# Create namespace
kubectl create namespace netpol-demo

# Deploy two apps
kubectl create deployment frontend --image=nginx --namespace=netpol-demo
kubectl create deployment backend --image=nginx --namespace=netpol-demo

# Create default deny all policy
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny
  namespace: netpol-demo
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
EOF

# Allow frontend to access backend
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend
  namespace: netpol-demo
spec:
  podSelector:
    matchLabels:
      app: backend
  policyTypes:
  - Ingress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: frontend
EOF

# Test connectivity
kubectl exec -n netpol-demo deployment/frontend -- curl -s backend
```

## Lab 6: Ingress Configuration

### Objective
Set up an Ingress to route external traffic to Services.

### Steps

```bash
# Install NGINX Ingress Controller
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm install ingress-nginx ingress-nginx/ingress-nginx

# Create deployment and service
kubectl create deployment web --image=nginx
kubectl expose deployment web --port=80

# Create Ingress
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: web-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: web.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: web
            port:
              number: 80
EOF

# Test
curl -H "Host: web.example.com" http://<ingress-ip>
```

## Lab 7: RBAC Setup

### Objective
Create roles and rolebindings for access control.

### Steps

```bash
# Create namespace
kubectl create namespace rbac-demo

# Create role
cat <<EOF | kubectl apply -f -
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: rbac-demo
  name: pod-reader
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log"]
  verbs: ["get", "watch", "list"]
EOF

# Create service account
kubectl create serviceaccount dev-user --namespace=rbac-demo

# Bind role to service account
cat <<EOF | kubectl apply -f -
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: read-pods
  namespace: rbac-demo
subjects:
- kind: ServiceAccount
  name: dev-user
  namespace: rbac-demo
roleRef:
  kind: Role
  name: pod-reader
  apiGroup: rbac.authorization.k8s.io
EOF

# Test access
kubectl auth can-i list pods --namespace=rbac-demo --as=system:serviceaccount:rbac-demo:dev-user
```

## Lab 8: Horizontal Pod Autoscaler

### Objective
Configure HPA to automatically scale Pods based on CPU usage.

### Steps

```bash
# Install metrics-server
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml

# Create deployment
kubectl create deployment php-apache --image=php:8.0-apache --replicas=2
kubectl expose deployment php-apache --port=80

# Create HPA
kubectl autoscale deployment php-apache --cpu-percent=50 --min=2 --max=10

# Generate load
kubectl run load-generator --rm -it --image=busybox -- /bin/sh
# Inside the pod:
while true; do wget -qO- http://php-apache; done

# Watch scaling
kubectl get hpa php-apache --watch
```

## Lab 9: Helm Chart Development

### Objective
Create and deploy a custom Helm chart.

### Steps

```bash
# Create chart
helm create my-app

# Modify templates/my-app-deployment.yaml
# Modify values.yaml

# Template and validate
helm template my-app .

# Dry run
helm install my-app . --dry-run --debug

# Install chart
helm install my-app . --namespace=production --create-namespace

# Upgrade chart
helm upgrade my-app . --set replicaCount=5

# Rollback
helm rollback my-app 1
```

## Lab 10: Debugging Pods

### Objective
Debug common Pod issues using kubectl tools.

### Steps

```bash
# Create a failing deployment
kubectl create deployment debug-app --image=busybox -- sleep 1

# Check status
kubectl get pods -w

# View logs
kubectl logs debug-app-xxxxx

# Describe for events
kubectl describe pod debug-app-xxxxx

# Exec into pod
kubectl exec -it debug-app-xxxxx -- /bin/sh

# Port forward
kubectl port-forward debug-app-xxxxx 8080:80

# Debug node
kubectl debug node/<node-name> -it --image=busybox
```
