# Go Installation

## Official Download

Download from https://go.dev/dl/

```bash
# macOS
brew install go

# Linux
sudo apt install golang-go

# Windows
# Download .msi installer from go.dev
```

## SDKMAN

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash

# Install Go
sdk install java   # Java is required
sdk install go

# List available versions
sdk list go

# Switch version
sdk use go 1.21.0
```

## Docker

```bash
# Use official Go image
docker run --rm -v $(pwd):/app -w /app golang:1.21 go build -o main .

# Multi-stage build
FROM golang:1.21 AS builder
WORKDIR /app
COPY . .
RUN go build -o main .

FROM alpine:latest
COPY --from=builder /app/main /main
CMD ["/main"]
```

## GOPATH

Default workspace at `~/go`:

```
~/go/
  bin/         # Compiled binaries
  pkg/         # Compiled packages
  src/         # Source files (pre-modules)
```

- Set custom: `export GOPATH=$HOME/custom`
- `go install` places binaries in `$GOPATH/bin`
- Add `$GOPATH/bin` to PATH

## Version Management

```bash
# Check current version
go version

# Update Go (macOS)
brew upgrade go

# Use specific version in Docker
FROM golang:1.20
```

## Verification

```bash
go version
go env
go env GOPATH GOROOT
```

## Uninstall

```bash
# macOS
brew uninstall go

# Linux
sudo apt remove golang-go

# Remove GOPATH
rm -rf ~/go
```
