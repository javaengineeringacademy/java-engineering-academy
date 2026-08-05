# Docker Scaling

## Docker Swarm

### Initialize Swarm
```bash
docker swarm init
```

### Join Swarm
```bash
# Get join token
docker swarm join-token worker

# Join as worker
docker swarm join --token <token> <manager-ip>:2377
```

### Deploy Services
```yaml
# docker-compose.yml
version: '3.8'

services:
  web:
    image: nginx
    ports:
      - "80:80"
    deploy:
      replicas: 3
      update_config:
        parallelism: 1
        delay: 10s
      restart_policy:
        condition: on-failure
```

```bash
# Deploy stack
docker stack deploy -c docker-compose.yml myapp

# List services
docker service ls

# Scale service
docker service scale myapp_web=5
```

### Service Management
```bash
# List services
docker service ls

# View service details
docker service ps myapp_web

# Update service
docker service update --image nginx:1.21 myapp_web

# Remove service
docker service rm myapp_web
```

## Replica Scaling

### Scale Command
```bash
# Scale to 5 replicas
docker service scale myapp_web=5

# Scale multiple services
docker service scale myapp_web=3 myapp_api=3
```

### Auto Scaling
```yaml
# docker-compose.yml
version: '3.8'

services:
  web:
    image: nginx
    deploy:
      replicas: 3
      resources:
        limits:
          cpus: '0.5'
          memory: 256M
```

## Load Balancing

### Swarm Mode Load Balancing
```yaml
services:
  web:
    image: nginx
    ports:
      - "80:80"
    deploy:
      replicas: 3
```

### External Load Balancer
```nginx
# nginx.conf
upstream docker {
    server 127.0.0.1:8001;
    server 127.0.0.1:8002;
    server 127.0.0.1:8003;
}

server {
    listen 80;
    location / {
        proxy_pass http://docker;
    }
}
```

## Kubernetes Integration

### Deploy to Kubernetes
```yaml
# k8s-deployment.yaml
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
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: myapp
        image: myapp:latest
        ports:
        - containerPort: 8000
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

### Service
```yaml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
spec:
  selector:
    app: myapp
  ports:
  - port: 80
    targetPort: 8000
  type: LoadBalancer
```

### Horizontal Pod Autoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: myapp-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: myapp
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## Service Discovery

### Swarm DNS
```bash
# Services discover each other by name
docker service create --name web nginx
docker service create --name app myapp

# app can reach web at hostname "web"
```

### External DNS
```yaml
services:
  app:
    image: myapp
    dns:
      - 8.8.8.8
      - 8.8.4.4
    dns_search:
      - example.com
```

## Rolling Updates

### Update Configuration
```yaml
services:
  web:
    image: nginx:1.20
    deploy:
      update_config:
        parallelism: 1
        delay: 10s
        failure_action: rollback
        order: start-first
```

### Manual Update
```bash
docker service update --image nginx:1.21 myapp_web
```

## Rolling Back

### Rollback Configuration
```yaml
services:
  web:
    image: nginx:1.21
    deploy:
      update_config:
        failure_action: rollback
      rollback_config:
        parallelism: 1
        delay: 5s
```

### Manual Rollback
```bash
docker service rollback myapp_web
```

## Blue-Green Deployment

### Strategy
```yaml
# Blue (current)
services:
  blue:
    image: myapp:1.0
    ports:
      - "8000:8000"

# Green (new)
services:
  green:
    image: myapp:2.0
    ports:
      - "8001:8000"
```

### Switch Traffic
```bash
# Update load balancer to point to green
# Stop blue after verification
docker service rm myapp_blue
```

## Canary Deployment

### Strategy
```yaml
services:
  stable:
    image: myapp:1.0
    deploy:
      replicas: 9

  canary:
    image: myapp:2.0
    deploy:
      replicas: 1
```

## Best Practices

1. Use Swarm for simple orchestration
2. Consider Kubernetes for complex deployments
3. Implement health checks for all services
4. Use rolling updates for zero downtime
5. Set resource limits for all services
6. Use service discovery for communication
7. Implement blue-green or canary deployments
8. Monitor service health and performance
