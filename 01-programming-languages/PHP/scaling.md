# PHP Scaling

## Load Balancing

Distribute traffic across multiple PHP-FPM instances.

Nginx upstream configuration:
```nginx
upstream php_backend {
    least_conn;
    server 10.0.0.1:9000;
    server 10.0.0.2:9000;
    server 10.0.0.3:9000 backup;
    keepalive 32;
}
```

Session handling with shared storage:
```ini
session.save_handler = redis
session.save_path = "tcp://redis-host:6379"
```

## Stateless Architecture

Make PHP applications stateless for horizontal scaling.

- Store sessions in Redis or Memcached instead of files
- Use database for persistent data, not local filesystem
- Store uploads in object storage (S3, GCS)
- Use environment variables for configuration
- Externalize all state to shared services

## Caching Layers

Multi-level caching for high-traffic applications.

```php
// L1: OPcache (in-memory bytecode)
// L2: APCu (in-memory user data)
// L3: Redis/Memcached (distributed)
// L4: CDN (static assets)

function getWithCache(string $key, callable $builder, int $ttl = 300) {
    $value = apcu_fetch($key, $success);
    if ($success) return $value;

    $value = $builder();
    apcu_store($key, $value, $ttl);
    return $value;
}
```

## Database Scaling

```php
// Read replicas for read-heavy workloads
$writeDb = new PDO($masterDsn, $user, $pass);
$readDb = new PDO($replicaDsn, $user, $pass);

// Connection pooling with external tools
// Use ProxySQL or PgBouncer for connection management
```

## Queue Systems

Offload long-running tasks to background workers.

```php
// Using Laravel Queue or custom implementation
dispatch(new SendEmail($user));
dispatch(new ProcessReport($reportId))->delay(now()->addMinutes(5));

// Worker command
// php artisan queue:work --queue=emails,reports
```

## Auto-Scaling

```yaml
# Docker Compose for horizontal scaling
services:
  php-fpm:
    image: php:8.2-fpm
    deploy:
      replicas: 3
      resources:
        limits:
          cpus: '0.5'
          memory: 256M
```

## Monitoring at Scale

- Use centralized logging (ELK, Graylog)
- Monitor PHP-FPM process count and response times
- Track queue depth and worker health
- Set up alerts for error rate spikes and latency increases
