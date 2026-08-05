# NGINX Patterns

## 1. Reverse Proxy

**Problem:** Application servers are directly exposed to the internet, leaking internal details and lacking TLS termination.

**Solution:** NGINX sits in front of application servers, forwarding requests and managing TLS.

**Implementation:**
```nginx
upstream app_backend {
    server 127.0.0.1:8080;
    server 127.0.0.1:8081;
}

server {
    listen 80;
    server_name app.example.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl http2;
    server_name app.example.com;

    ssl_certificate /etc/ssl/certs/app.pem;
    ssl_certificate_key /etc/ssl/private/app.key;

    location / {
        proxy_pass http://app_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

**When to Use:** Every web application in production. Direct exposure of application servers is an anti-pattern.

**When NOT to Use:** Static file serving where NGINX serves files directly without a backend.

---

## 2. SSL/TLS Termination

**Problem:** Every application server must manage certificates, increasing operational complexity and attack surface.

**Solution:** Terminate TLS at NGINX, forwarding decrypted traffic to backends over a trusted network.

**Implementation:**
```nginx
server {
    listen 443 ssl http2;
    server_name secure.example.com;

    ssl_certificate /etc/letsencrypt/live/secure.example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/secure.example.com/privkey.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256;
    ssl_prefer_server_ciphers off;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;

    ssl_stapling on;
    ssl_stapling_verify on;

    location / {
        proxy_pass http://backend;
    }
}
```

**When to Use:** Always in production. Centralizes certificate management and enables consistent TLS configuration.

**When NOT to Use:** When end-to-end encryption is required between client and application (mutual TLS).

---

## 3. Load Balancing Algorithms

**Problem:** A single backend cannot handle all traffic, and uneven distribution causes hotspots.

**Solution:** NGINX distributes requests across multiple backends using configurable algorithms.

**Implementation:**
```nginx
# Round-robin (default)
upstream roundrobin {
    server 10.0.0.1:8080;
    server 10.0.0.2:8080;
}

# Least connections
upstream leastconn {
    least_conn;
    server 10.0.0.1:8080;
    server 10.0.0.2:8080;
}

# IP hash for session affinity
upstream iphash {
    ip_hash;
    server 10.0.0.1:8080;
    server 10.0.0.2:8080;
}

# Weighted
upstream weighted {
    server 10.0.0.1:8080 weight=3;
    server 10.0.0.2:8080 weight=1;
}
```

**When to Use:** Any multi-backend deployment. Choose based on workload characteristics.

**When NOT to Use:** Single-backend deployments or when the orchestrator handles load balancing.

---

## 4. Rate Limiting

**Problem:** APIs are vulnerable to abuse without request throttling.

**Solution:** NGINX limits request rates per client IP or API key.

**Implementation:**
```nginx
http {
    limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;
    limit_req_zone $http_api_key zone=key_limit:10m rate=100r/s;

    server {
        listen 443 ssl;

        location /api/ {
            limit_req zone=api_limit burst=20 nodelay;
            limit_req_status 429;
            proxy_pass http://backend;
        }

        location /api/v2/ {
            limit_req zone=key_limit burst=50 nodelay;
            proxy_pass http://backend_v2;
        }
    }
}
```

**When to Use:** Public-facing APIs, login endpoints, and any endpoint susceptible to abuse.

**When NOT to Use:** Internal services or when rate limiting is handled at a higher layer (API gateway).

---

## 5. A/B Routing with Split Clients

**Problem:** Testing new features requires routing a subset of users to different backends.

**Solution:** NGINX split_clients divide traffic based on a variable (IP, cookie, header).

**Implementation:**
```nginx
http {
    split_clients "${remote_addr}${http_user_agent}" $variant {
        50%    stable;
        50%    canary;
    }

    upstream stable_backend {
        server 10.0.0.1:8080;
    }

    upstream canary_backend {
        server 10.0.1.1:8080;
    }

    server {
        listen 443 ssl;

        location / {
            set $upstream "${variant}_backend";
            proxy_pass http://$upstream;
        }

        # Or route to different backends explicitly
        location / {
            if ($variant = "canary") {
                proxy_pass http://canary_backend;
            }
            proxy_pass http://stable_backend;
        }
    }
}
```

**When to Use:** Gradual rollouts, canary deployments, and feature flag routing.

**When NOT to Use:** When routing logic is complex (use application-level routing or an API gateway).

---

## 6. Blue-Green Switching

**Problem:** Deploying new versions requires downtime or complex rolling updates.

**Solution:** Maintain two identical environments and switch NGINX upstream at once.

**Implementation:**
```nginx
upstream app {
    server 10.0.0.1:8080;  # blue
    # server 10.0.1.1:8080;  # green (swap on deploy)
}

# Or use map for atomic switching
map $uri $backend {
    default blue_backend;
    /v2/*   green_backend;
}

upstream blue_backend {
    server 10.0.0.1:8080;
}

upstream green_backend {
    server 10.0.1.1:8080;
}

server {
    listen 80;
    location / {
        proxy_pass http://$backend;
    }
}
```

**When to Use:** When zero-downtime deployment is critical and infrastructure cost is acceptable.

**When NOT to Use:** When maintaining duplicate infrastructure is cost-prohibitive.

---

## 7. Proxy Caching

**Problem:** Repeated requests to the same backend waste resources and increase latency.

**Solution:** NGINX caches responses and serves them directly without hitting the backend.

**Implementation:**
```nginx
http {
    proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=app_cache:10m max_size=1g;

    server {
        listen 443 ssl;

        location /api/public/ {
            proxy_cache app_cache;
            proxy_cache_valid 200 5m;
            proxy_cache_valid 404 1m;
            proxy_cache_use_stale error timeout updating;
            proxy_cache_lock on;

            add_header X-Cache-Status $upstream_cache_status;

            proxy_pass http://backend;
        }

        location /api/private/ {
            proxy_pass http://backend;
        }
    }
}
```

**When to Use:** Public APIs, static content, and any response that is the same for multiple clients.

**When NOT to Use:** Personalized content, real-time data, or responses with sensitive information.

---

## 8. Compression (gzip and Brotli)

**Problem:** Large text responses consume excessive bandwidth and increase page load times.

**Solution:** NGINX compresses responses using gzip or Brotli.

**Implementation:**
```nginx
http {
    gzip on;
    gzip_vary on;
    gzip_proxied any;
    gzip_comp_level 6;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
    gzip_min_length 256;

    brotli on;
    brotli_comp_level 6;
    brotli_types text/plain text/css application/json application/javascript text/xml;

    server {
        listen 443 ssl;

        location / {
            proxy_pass http://backend;
        }
    }
}
```

**When to Use:** Always for text-based responses. Brotli offers better compression than gzip for modern browsers.

**When NOT to Use:** Already-compressed content (images, videos) or when CPU usage is a bottleneck.

---

## 9. Security Headers

**Problem:** Applications are vulnerable to XSS, clickjacking, and other attacks without proper headers.

**Solution:** NGINX adds security headers to all responses.

**Implementation:**
```nginx
server {
    listen 443 ssl;

    add_header Strict-Transport-Security "max-age=63072000; includeSubDomains; preload" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-Frame-Options "DENY" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;
    add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline'" always;
    add_header Permissions-Policy "camera=(), microphone=(), geolocation=()" always;

    location / {
        proxy_pass http://backend;
    }
}
```

**When to Use:** Every public-facing web application. These headers are low-effort, high-impact security measures.

**When NOT to Use:** Never. There is no reason to omit security headers.

---

## 10. Limit Request Body

**Problem:** Large request bodies can exhaust memory or bandwidth.

**Solution:** NGINX enforces maximum request body size.

**Implementation:**
```nginx
server {
    listen 443 ssl;
    client_max_body_size 10m;

    location /upload {
        client_max_body_size 50m;
        proxy_pass http://backend;
    }

    location /api/ {
        client_max_body_size 1k;
        proxy_pass http://backend;
    }
}
```

**When to Use:** Every server block. Prevents accidental or malicious large uploads.

**When NOT to Use:** Never. Always set a reasonable limit.

---

## Best Practices

- Use include files for reusable configuration snippets.
- Never expose server version: `server_tokens off;`.
- Use `proxy_pass` with a trailing slash only when stripping the location prefix.
- Set `proxy_read_timeout` and `proxy_connect_timeout` appropriately.
- Use `try_files` for static content before falling back to a backend.
- Monitor with stub_status and Prometheus exporter.
- Use `if` sparingly in location blocks; prefer map or separate server blocks.
