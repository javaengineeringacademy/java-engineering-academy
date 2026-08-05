# MongoDB Security

## Authentication

### SCRAM-SHA-256

```javascript
// Create user
use admin
db.createUser({
  user: "admin",
  pwd: "password",
  roles: ["root"]
})

// Create application user
use mydb
db.createUser({
  user: "appuser",
  pwd: "password",
  roles: ["readWrite"]
})
```

### Authentication Methods

- SCRAM-SHA-256: Default, recommended
- X.509: Certificate-based
- LDAP: External authentication
- Kerberos: Enterprise authentication

### Enable Authentication

```yaml
# In mongod.conf
security:
  authorization: enabled
```

## TLS/SSL

### Generate Certificates

```bash
# Generate CA key
openssl genrsa -out ca.key 4096

# Generate CA certificate
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 -out ca.pem

# Generate server key
openssl genrsa -out server.key 4096

# Generate server certificate
openssl req -new -key server.key -out server.csr
openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.pem -days 3650
```

### Configure TLS

```yaml
# In mongod.conf
net:
  tls:
    mode: requireTLS
    certificateKeyFile: /etc/ssl/server.pem
    CAFile: /etc/ssl/ca.pem
```

### Client TLS

```bash
# Connect with TLS
mongosh --tls --tlsCAFile /etc/ssl/ca.pem
```

## Role-Based Access Control

### Built-in Roles

- read: Read-only access
- readWrite: Read and write access
- dbAdmin: Database administration
- userAdmin: User management
- root: Full access

### Custom Roles

```javascript
// Create custom role
db.createRole({
  role: "customRole",
  privileges: [
    {
      resource: { db: "mydb", collection: "users" },
      actions: ["find", "insert", "update"]
    }
  ],
  roles: []
})
```

### Grant Roles

```javascript
// Grant role to user
db.grantRolesToUser("appuser", ["readWrite"])
```

## Network Security

### Bind IP

```yaml
# In mongod.conf
net:
  bindIp: 127.0.0.1  # localhost only
```

### Port Configuration

```yaml
net:
  port: 27017
```

### Firewall Rules

```bash
# Allow only specific IPs
sudo ufw allow from 10.0.0.0/8 to any port 27017
```

## Encryption

### Encryption at Rest

```yaml
# In mongod.conf
storage:
  WiredTiger:
    encryption:
      keyId: <encryption-key-id>
```

### Client-Side Field Level Encryption

```javascript
const encryption = {
  keyVaultNamespace: "encryption.__keyVault",
  kmsProviders: {
    local: {
      key: BinData(0, "<base64-key>")
    }
  }
}

const client = new MongoClient(uri, {
  autoEncryption: {
    keyVaultNamespace: "encryption.__keyVault",
    kmsProviders: {
      local: {
        key: BinData(0, "<base64-key>")
      }
    }
  }
})
```

## Audit Logging

### Enable Audit

```yaml
# In mongod.conf
auditLog:
  destination: file
  format: JSON
  path: /var/log/mongodb/audit.json
```

### Audit Filter

```javascript
// Audit specific operations
db.adminCommand({
  setAuditParameter: 1,
  parameter: {
    auditFilter: {
      atype: "authenticate"
    }
  }
})
```

## Authorization

### Read-Only User

```javascript
db.createUser({
  user: "readonly",
  pwd: "password",
  roles: ["read"]
})
```

### Read-Write User

```javascript
db.createUser({
  user: "readwrite",
  pwd: "password",
  roles: ["readWrite"]
})
```

### Database Admin

```javascript
db.createUser({
  user: "dbadmin",
  pwd: "password",
  roles: ["dbAdmin"]
})
```

## Best Practices

1. Enable authentication
2. Use TLS for connections
3. Implement role-based access
4. Audit sensitive operations
5. Use strong passwords
6. Regular security reviews
