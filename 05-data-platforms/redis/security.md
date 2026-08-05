# Redis Security

## Authentication

### Password Authentication

```conf
# In redis.conf
requirepass your_strong_password_here

# Connect with password
redis-cli -a your_strong_password_here

# Or authenticate after connecting
redis-cli AUTH your_strong_password_here
```

### ACL (Access Control Lists) - Redis 6.0+

```conf
# Enable ACL
aclfile /etc/redis/users.acl

# Create user
ACL SETUSER alice on >password123 ~cache:* +get +set +del

# List users
ACL LIST

# Check permissions
ACL WHOAMI

# Test command
ACL LOG
```

### ACL User Commands

```bash
# Create user with full access
ACL SETUSER admin on >adminpass ~* &* +@all

# Create read-only user
ACL SETUSER reader on >readerpass ~* &* +@read

# Create limited user
ACL SETUSER app on >apppass ~app:* &* +get +set +del +expire

# Delete user
ACL DELUSER alice

# Save ACL to file
ACL SAVE
```

## Network Security

### Bind Configuration

```conf
# Bind to specific interface
bind 127.0.0.1

# Bind to multiple interfaces
bind 127.0.0.1 192.168.1.100

# Disable binding (not recommended)
bind 0.0.0.0
```

### Protected Mode

```conf
# Auto-protects when:
# 1. No bind address specified
# 2. No password set
protected-mode yes
```

### Port Configuration

```conf
# Change default port
port 6380

# Disable TCP (Unix socket only)
port 0
unixsocket /var/run/redis/redis.sock
unixsocketperm 700
```

## TLS/SSL Encryption

### Generate Certificates

```bash
# Generate CA key
openssl genrsa -out ca.key 4096

# Generate CA certificate
openssl req -x509 -new -nodes -sha256 -key ca.key -days 3650 -out ca.crt

# Generate server key
openssl genrsa -out redis.key 2048

# Generate server CSR
openssl req -new -sha256 -key redis.key -out redis.csr

# Sign server certificate
openssl x509 -req -sha256 -in redis.csr -CA ca.crt -CAkey ca.key \
  -CAcreateserial -out redis.crt -days 365
```

### Configure TLS

```conf
# Enable TLS
tls-port 6380
port 0  # Disable non-TLS port

# Certificate files
tls-cert-file /etc/redis/tls/redis.crt
tls-key-file /etc/redis/tls/redis.key
tls-ca-cert-file /etc/redis/tls/ca.crt

# TLS protocols
tls-protocols "TLSv1.2 TLSv1.3"

# TLS ciphers
tls-ciphersuites "TLS_AES_256_GCM_SHA384:TLS_CHACHA20_POLY1305_SHA256"

# Client certificate verification
tls-auth-clients optional
```

## Command Restriction

### Rename Commands

```conf
# Disable FLUSHALL
rename-command FLUSHALL ""

# Rename DEBUG
rename-command DEBUG DEBUG_RENAMED

# Disable dangerous commands
rename-command FLUSHDB ""
rename-command CONFIG CONFIG_RENAMED
rename-command KEYS KEYS_RENAMED
```

### Disable Commands via ACL

```bash
# Block specific commands
ACL SETUSER alice on >password ~* -flushall -flushdb -debug
```

## Data Protection

### Encryption at Rest

- Use full-disk encryption (LUKS, FileVault)
- Encrypt RDB and AOF files
- Secure backup storage

### Secure Temporary Files

```conf
# Disable Lua script caching (if needed)
lua-time-limit 5000

# Secure temp directory
tmpdir /var/lib/redis/tmp
```

## Monitoring and Auditing

### Enable Logging

```conf
# Log level
loglevel notice

# Log file
logfile /var/log/redis/redis.log

# Log client connections
log-queries-slow-at 10000
```

### ACL Logging

```bash
# Enable ACL logging
CONFIG SET acllog-max-len 128

# Check ACL log
ACL LOG

# Clear ACL log
ACL LOG RESET
```

## Firewall Rules

```bash
# Only allow specific IPs
iptables -A INPUT -p tcp --dport 6379 -s 10.0.0.1 -j ACCEPT
iptables -A INPUT -p tcp --dport 6379 -j DROP
```

## Security Checklist

1. Set strong password with `requirepass`
2. Use ACLs for fine-grained access control
3. Enable TLS for encrypted connections
4. Bind to specific interfaces (not 0.0.0.0)
5. Enable `protected-mode yes`
6. Rename or disable dangerous commands
7. Run Redis as non-root user
8. Use firewall to restrict access
9. Regularly update Redis version
10. Monitor ACL logs for unauthorized access
11. Use separate users for different applications
12. Never expose Redis directly to the internet
