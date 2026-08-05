# Docker Monitoring

## Docker Stats

### Real-Time Monitoring
```bash
# All containers
docker stats

# Specific container
docker stats mycontainer

# No streaming (one-time)
docker stats --no-stream

# Format output
docker stats --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

### Available Metrics
```bash
# CPU
- CPU percentage
- CPU throttling

# Memory
- Memory usage
- Memory limit
- Memory percentage

# Network
- Network I/O
- Network interfaces

# Disk
- Block I/O
```

## cAdvisor

### Container Advisor
```bash
# Run cAdvisor
docker run -d \
  --name=cadvisor \
  -p 8080:8080 \
  -v /:/rootfs:ro \
  -v /var/run:/var/run:ro \
  -v /sys:/sys:ro \
  -v /var/lib/docker/:/var/lib/docker:ro \
  gcr.io/cadvisor/cadvisor
```

### Access Web UI
```bash
# Open browser
open http://localhost:8080
```

### Docker Compose
```yaml
version: '3.8'

services:
  cadvisor:
    image: gcr.io/cadvisor/cadvisor
    ports:
      - "8080:8080"
    volumes:
      - /:/rootfs:ro
      - /var/run:/var/run:ro
      - /sys:/sys:ro
      - /var/lib/docker/:/var/lib/docker:ro
    privileged: true
    devices:
      - /dev/kmsg:/dev/kmsg
```

## Prometheus

### Docker Metrics Exporter
```bash
# Install
brew install prom

# Run
prom --config.file=prometheus.yml
```

### Configuration
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'docker'
    static_configs:
      - targets: ['localhost:8080']
```

### Docker Metrics Endpoint
```bash
# Enable metrics in daemon.json
{
  "metrics-addr": "0.0.0.0:9323",
  "experimental": true
}
```

## Logging Drivers

### JSON File (Default)
```bash
docker run --log-driver=json-file \
  --log-opt max-size=10m \
  --log-opt max-file=3 \
  myimage
```

### Syslog
```bash
docker run --log-driver=syslog \
  --log-opt syslog-address=tcp://syslog-server:514 \
  myimage
```

### Fluentd
```bash
docker run --log-driver=fluentd \
  --log-opt fluentd-address=localhost:24224 \
  myimage
```

### AWS CloudWatch
```bash
docker run --log-driver=awslogs \
  --log-opt awslogs-region=us-east-1 \
  --log-opt awslogs-group=myapp \
  myimage
```

### Docker Compose Logging
```yaml
services:
  app:
    image: myimage
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"
        tag: "{{.Name}}"
```

## Health Checks

### Dockerfile Health Check
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8000/health || exit 1
```

### Container Health Status
```bash
# Check health
docker inspect --format='{{.State.Health.Status}}' mycontainer

# View health logs
docker inspect --format='{{json .State.Health}}' mycontainer | jq
```

### Compose Health Check
```yaml
services:
  app:
    image: myimage
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8000/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

## Log Aggregation

### ELK Stack
```yaml
version: '3.8'

services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.10.0
    environment:
      - discovery.type=single-node
    ports:
      - "9200:9200"

  logstash:
    image: docker.elastic.co/logstash/logstash:8.10.0
    volumes:
      - ./logstash.conf:/usr/share/logstash/pipeline/logstash.conf
    ports:
      - "5044:5044"

  kibana:
    image: docker.elastic.co/kibana/kibana:8.10.0
    ports:
      - "5601:5601"
```

### Loki
```yaml
version: '3.8'

services:
  loki:
    image: grafana/loki:latest
    ports:
      - "3100:3100"

  promtail:
    image: grafana/promtail:latest
    volumes:
      - /var/log:/var/log
      - ./promtail.yml:/etc/promtail/config.yml
```

## Dashboards

### Grafana Dashboard
```yaml
version: '3.8'

services:
  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    volumes:
      - grafana-data:/var/lib/grafana
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin

volumes:
  grafana-data:
```

## Alerting

### Prometheus Alerting Rules
```yaml
groups:
  - name: docker
    rules:
      - alert: ContainerDown
        expr: up{job="docker"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: Container is down

      - alert: HighMemory
        expr: container_memory_usage_bytes > 1073741824
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: High memory usage
```

## Best Practices

1. Use Docker stats for quick monitoring
2. Deploy cAdvisor for container metrics
3. Use Prometheus for time-series data
4. Set up Grafana for visualization
5. Configure health checks for all services
6. Use appropriate logging drivers
7. Aggregate logs with ELK or Loki
8. Set up alerting for critical conditions
