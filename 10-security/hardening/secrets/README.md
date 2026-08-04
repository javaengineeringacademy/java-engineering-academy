# Secret Management

## Overview

Secret management involves securely storing, accessing, and rotating sensitive data like API keys, passwords, and certificates.

## HashiCorp Vault

### Configuration
```yaml
spring:
  cloud:
    vault:
      uri: https://vault.example.com
      token: ${VAULT_TOKEN}
      kv:
        enabled: true
        backend: secret
        default-context: application
```

### Usage
```java
@Service
public class SecretService {
    @Value("${db.password}")
    private String dbPassword;
    
    @Value("${api.key}")
    private String apiKey;
}
```

### Programmatic Access
```java
@Bean
public VaultTemplate vaultTemplate() {
    VaultTemplate template = new VaultTemplate(
        VaultEndpoint.create("vault.example.com", 8200),
        new TokenAuthentication(vaultToken)
    );
    return template;
}

// Read secret
VaultResponse response = vaultTemplate.read("secret/myapp");
String password = response.getData().get("password");
```

## Spring Cloud Config

```yaml
# bootstrap.yml
spring:
  cloud:
    config:
      uri: https://config-server.example.com
      token: ${CONFIG_TOKEN}
```

## Environment Variables

```bash
# Set in .env file (never commit)
DB_PASSWORD=secure_password
API_KEY=your_api_key

# Reference in application
spring.datasource.password=${DB_PASSWORD}
```

## Kubernetes Secrets

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: db-credentials
type: Opaque
stringData:
  username: admin
  password: secure_password
---
apiVersion: apps/v1
kind: Deployment
spec:
  containers:
  - name: app
    env:
    - name: DB_PASSWORD
      valueFrom:
        secretKeyRef:
          name: db-credentials
          key: password
```

## AWS Secrets Manager

```java
@Bean
public AWSSecretsManager secretsManager() {
    return AWSSecretsManagerClientBuilder.standard()
        .withRegion(Regions.US_EAST_1)
        .build();
}

public String getSecret(String secretName) {
    GetSecretValueRequest request = GetSecretValueRequest.builder()
        .secretId(secretName)
        .build();
    
    GetSecretValueResponse response = secretsManager.getSecretValue(request);
    return response.secretString();
}
```

## Best Practices

1. Never store secrets in code
2. Use secret management tools
3. Rotate secrets regularly
4. Audit secret access
5. Use least-privilege access
6. Encrypt secrets at rest
7. Use environment variables
8. Monitor secret usage
