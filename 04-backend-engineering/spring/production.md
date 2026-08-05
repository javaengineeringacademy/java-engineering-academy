# Spring Production Configuration

## Externalized Configuration

### Properties Priority

1. Command-line arguments
2. JNDI attributes
3. System properties
4. Environment variables
5. Application properties (outside JAR)
6. Application properties (inside JAR)
7. @PropertySource
8. Default properties

### Environment Variables

```bash
# Set environment variables
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/mydb
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=password
export SPRING_PROFILES_ACTIVE=prod
```

### Command Line Arguments

```bash
java -jar app.jar \
  --server.port=9090 \
  --spring.datasource.url=jdbc:mysql://localhost:3306/mydb \
  --spring.profiles.active=prod
```

## Production Properties

```properties
# application-prod.properties

# Server
server.port=80
server.tomcat.max-threads=200
server.tomcat.min-spare-threads=20
server.tomcat.accept-count=100

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/proddb
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging
logging.level.root=WARN
logging.level.com.example=INFO
logging.file.name=/var/log/myapp/app.log

# Actuator
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
```

## Graceful Shutdown

```properties
# Enable graceful shutdown
server.shutdown=graceful

# Timeout for pending requests
spring.lifecycle.timeout-per-shutdown-phase=30s
```

```java
@Configuration
public class GracefulShutdownConfig {
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandler() {
        return handler -> handler.setExecutor(
            Executors.newFixedThreadPool(50)
        );
    }
}
```

## Database Configuration

```properties
# HikariCP
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.leak-detection-threshold=60000

# Connection validation
spring.datasource.hikari.validation-timeout=5000
spring.datasource.hikari.connection-test-query=SELECT 1
```

## Security Configuration

```properties
# HTTPS
server.ssl.key-store=file:/path/to/keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=myalias

# Session
server.servlet.session.timeout=30m
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=lax
```

## Logging Configuration

```properties
# File logging
logging.file.name=/var/log/myapp/app.log
logging.file.max-size=10MB
logging.file.max-history=30

# Log levels
logging.level.root=WARN
logging.level.com.example=INFO
logging.level.org.springframework.web=WARN
logging.level.org.hibernate=WARN

# Log pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

## JVM Tuning

```bash
# Production JVM settings
java -Xms2g -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/log/myapp/heapdump.hprof \
     -Djava.security.egd=file:/dev/./urandom \
     -jar app.jar
```

## Docker Production

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Xms512m", \
  "-Xmx512m", \
  "-XX:+UseG1GC", \
  "-jar", \
  "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/mydb
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=password
    depends_on:
      - mysql
      - redis
    deploy:
      resources:
        limits:
          memory: 1G
          cpus: '1'
    restart: unless-stopped

  mysql:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=password
      - MYSQL_DATABASE=mydb
    volumes:
      - mysql-data:/var/lib/mysql
    ports:
      - "3306:3306"

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

volumes:
  mysql-data:
```

## Health Checks

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Check application health
            return Health.up()
                .withDetail("status", "running")
                .withDetail("version", getAppVersion())
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

## Monitoring Setup

```yaml
# docker-compose-monitoring.yml
version: '3.8'
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin

  redis-exporter:
    image: oliver006/redis_exporter
    ports:
      - "9121:9121"
```

## Backup Strategy

```bash
#!/bin/bash
# backup.sh

# Database backup
mysqldump -u root -p${DB_PASSWORD} mydb > /backup/db/mydb_$(date +%Y%m%d).sql

# Redis backup
redis-cli BGSAVE
cp /var/lib/redis/dump.rdb /backup/redis/dump_$(date +%Y%m%d).rdb

# Application config backup
cp -r /etc/myapp /backup/config/myapp_$(date +%Y%m%d)

# Cleanup old backups (keep 30 days)
find /backup -name "*.sql" -mtime +30 -delete
find /backup -name "*.rdb" -mtime +30 -delete
```

## Production Checklist

1. Externalize configuration
2. Enable graceful shutdown
3. Configure connection pooling
4. Set up logging properly
5. Enable HTTPS
6. Configure security headers
7. Set up monitoring and alerting
8. Implement health checks
9. Configure backup strategy
10. Test failover scenarios
