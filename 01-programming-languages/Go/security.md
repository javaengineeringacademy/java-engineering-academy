# Go Security

## gosec - Security Scanner

```bash
# Install
go install github.com/securego/gosec/v2/cmd/gosec@latest

# Scan project
gosec ./...

# Scan with specific rules
gosec -include=G101,G201 ./...

# Exclude rules
gosec -exclude=G104 ./...

# Output format
gosec -fmt=json ./...
```

Common rules:

- G101: Hardcoded credentials
- G104: Unhandled errors
- G204: Command injection
- G301-G306: File path issues
- G401-G407: Weak crypto usage

## Vulnerability Scanning

```bash
# Check dependencies
go install golang.org/x/vuln/cmd/govulncheck@latest
govulncheck ./...

# Check for known CVEs
go list -m -json all | jq '.[] | select(.GoVersion != "")'
```

## Crypto Best Practices

- Use `crypto/rand` for cryptographic randomness
- Avoid `math/rand` for security purposes
- Use `crypto/sha256` over `crypto/sha1`
- Prefer `crypto/aes` over DES/3DES
- Use `golang.org/x/crypto` for modern algorithms
- Hash passwords with `bcrypt` or `argon2`

## Input Validation

```go
func validateInput(input string) error {
    if len(input) > maxLen {
        return errors.New("input too long")
    }
    if !regexp.MustCompile(`^[a-zA-Z0-9]+$`).MatchString(input) {
        return errors.New("invalid characters")
    }
    return nil
}
```

## SQL Injection Prevention

```go
// Never concatenate user input into queries
query := "SELECT * FROM users WHERE id = $1"
row := db.QueryRow(query, userID)

// Use parameterized queries
stmt, err := db.Prepare("INSERT INTO users (name) VALUES ($1)")
```

## HTTP Security Headers

```go
func securityHeaders(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        w.Header().Set("X-Content-Type-Options", "nosniff")
        w.Header().Set("X-Frame-Options", "DENY")
        w.Header().Set("X-XSS-Protection", "1; mode=block")
        w.Header().Set("Strict-Transport-Security", "max-age=31536000")
        next.ServeHTTP(w, r)
    })
}
```

## Secrets Management

- Never commit secrets to version control
- Use environment variables for configuration
- Use `os.Getenv` to read secrets
- Use Vault or AWS Secrets Manager for production
- Rotate secrets regularly
- Audit secret access
