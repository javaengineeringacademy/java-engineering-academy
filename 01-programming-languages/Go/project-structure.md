# Go Project Structure

## Standard Layout

```
project/
  cmd/                    # Main applications
    app/
      main.go
    server/
      main.go
  pkg/                    # Public library code
    handler/
      handler.go
    middleware/
      auth.go
  internal/               # Private application code
    database/
      db.go
    service/
      user.go
  api/                    # API definitions
    openapi.yaml
    proto/
      user.proto
  configs/                # Configuration files
    config.yaml
    config.go
  scripts/                # Build and deployment scripts
    build.sh
  deployments/            # Deployment configurations
    docker/
      Dockerfile
    kubernetes/
      deployment.yaml
  test/                   # Integration tests
    integration_test.go
  docs/                   # Documentation
    architecture.md
  vendor/                 # Vendored dependencies
  go.mod
  go.sum
  Makefile
  README.md
```

## cmd/ Directory

Contains main packages for executables:

```
cmd/
  api-server/
    main.go
  worker/
    main.go
  cli/
    main.go
```

Each subdirectory has its own `main.go` entry point.

## pkg/ Directory

Public library code reusable across projects:

- Exported packages available to external consumers
- Should have minimal dependencies
- Ideal for libraries and frameworks
- Keep internal implementation details in internal/

## internal/ Directory

Private code restricted to the module:

- Cannot be imported by external packages
- Contains business logic and private utilities
- Enforced by the Go compiler
- Organize by domain or layer

## Root Directory

Minimal root with:

- `main.go` if single binary
- `go.mod` and `go.sum`
- `Makefile` for common commands
- `README.md` for documentation
- `.gitignore` for version control

## Makefile Pattern

```makefile
.PHONY: build test lint run

build:
	go build -o bin/app ./cmd/app

test:
	go test ./... -v -cover

lint:
	golangci-lint run

run:
	go run ./cmd/app
```

## Best Practices

- Keep main.go minimal, delegate to packages
- Use dependency injection at package boundaries
- Separate concerns: handlers, services, repositories
- Place tests next to source files
- Use build tags for platform-specific code
