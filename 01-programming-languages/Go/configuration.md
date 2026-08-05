# Go Configuration

## go.mod

The module definition file:

```go
module github.com/user/project

go 1.21

require (
    github.com/gin-gonic/gin v1.9.1
    github.com/stretchr/testify v1.8.4
)

require (
    golang.org/x/net v0.17.0 // indirect
)
```

- Declares module path and Go version
- Lists direct and indirect dependencies
- Use `go mod init` to create
- Use `go mod tidy` to update

## go.sum

Lock file containing checksums for all dependencies. Ensures reproducible builds and verifies module integrity. Never manually edit this file.

## go.env

Global Go environment settings:

```bash
go env GOPATH          # Default: ~/go
go env GOROOT          # Go installation directory
go env GOPROXY         # Module proxy URL
go env GO111MODULE     # Module mode (on/off/auto)
go env GONOSUMCHECK    # Skip checksum verification
```

Set with `go env -w KEY=VALUE`.

## Build Tags

Conditional compilation:

```go
//go:build linux
// +build linux

package main

func init() {
    // Linux-specific code
}
```

Common tags: `linux`, `darwin`, `windows`, `cgo`, `race`, `integration`

Usage: `go build -tags=integration`

## ldflags

Linker flags for build-time variables:

```bash
go build -ldflags "-X main.Version=1.0.0 -X main.BuildTime=$(date -u +%Y-%m-%dT%H:%M:%SZ)"
```

- `-X`: Set string variables
- `-s`: Omit symbol table
- `-w`: Omit DWARF debug info
- `-compressdwarf`: Compress DWARF data

## Environment Variables

Key variables for development:

- `CGO_ENABLED`: Enable/disable CGo (default 1)
- `GOOS`: Target operating system
- `GOARCH`: Target architecture
- `GOPRIVATE`: Private module paths
- `GONOSUMDB`: Skip checksum database
- `GOFLAGS`: Default flags for go commands

## Testing Configuration

```bash
go test -v ./...                    # Verbose tests
go test -cover ./...               # Coverage report
go test -race ./...                # Race detection
go test -count=1 ./...             # Disable caching
go test -timeout 30s ./...         # Set timeout
go test -run TestSpecific ./...    # Run specific test
```

## Code Generation

```bash
go generate ./...              # Run generate directives
go generate -run "stringer"    # Run specific generators
```

Common generators: `stringer`, `enumer`, `mockgen`, `protobuf`

## Cross-Compilation

```bash
GOOS=linux GOARCH=amd64 go build -o binary-linux-amd64
GOOS=darwin GOARCH=arm64 go build -o binary-darwin-arm64
GOOS=windows GOARCH=amd64 go build -o binary-windows-amd64.exe
```
