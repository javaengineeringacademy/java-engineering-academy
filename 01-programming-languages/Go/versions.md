# Go Version History

## Go 1.0
- **Release Date:** March 28, 2012
- **Features:** Go 1 specification, standard library, go tool, goroutines, channels, garbage collection, interfaces, packages, testing package
- **Deprecated:** N/A (initial release)
- **Removed:** N/A
- **Performance:** Garbage collector with stop-the-world pauses, goroutines for concurrency
- **Security:** Basic security model, no external dependencies by default
- **Why Introduced:** Google created Go to address issues with C++ compilation speed, dependency management, and concurrent programming

## Go 1.1
- **Release Date:** May 13, 2013
- **Features:** 64-bit on ARM, race detector, performance improvements, parallel compilation, goroutine stack growth improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Goroutine stack growth reduced memory usage, parallel compilation speeds
- **Security:** Race detector identifies data races at runtime
- **Why Introduced:** Performance improvements and race detection tool for concurrent code

## Go 1.2
- **Release Date:** December 1, 2013
- **Features:** Three-index slices, guaranteed goroutine stack minimum size, increased GOGC range, test coverage tool, 1.5 compiler ready
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Three-index slices enable efficient sub-slicing without allocation
- **Security:** Stack size guarantee prevents stack overflow attacks
- **Why Introduced:** Slice improvements and tooling enhancements

## Go 1.3
- **Release Date:** June 18, 2014
- **Features:** Stack-based goroutine implementation, contiguous stacks, improved garbage collector, runtime improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Contiguous stacks replaced segmented stacks, reducing memory fragmentation
- **Security:** Stack improvements reduce memory corruption risks
- **Why Introduced:** Major runtime overhaul with contiguous stacks for better memory management

## Go 1.4
- **Release Date:** December 10, 2014
- **Features:** Internal packages, for range x {} loop, Android support, generated code support, improved build, GO38 and GOARM architecture
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Build speed improvements
- **Security:** Internal packages provide access control for sensitive code
- **Why Introduced:** Android support, internal packages for better code organization

## Go 1.5
- **Release Date:** August 19, 2015
- **Features:** Garbage collector redesigned (concurrent), no more C in toolchain, vendoring experiment, GOVENDOREXPERIMENT, internal racerepair, improved testing
- **Deprecated:** GOPATH-based vendoring (experimental)
- **Removed:** C compiler dependency (toolchain now self-hosting)
- **Performance:** Concurrent garbage collector reduced pause times to under 10ms
- **Security:** Self-hosting toolchain eliminates C compiler vulnerabilities
- **Why Introduced:** Major GC overhaul for lower latency, self-hosting toolchain for portability

## Go 1.6
- **Release Date:** February 17, 2016
- **Features:** HTTP/2 support, HTTPS by default in godoc, vendor directory experiment enabled by default, runtime type safety
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** HTTP/2 support for faster web communication
- **Security:** HTTPS by default for documentation, HTTP/2 encryption
- **Why Introduced:** HTTP/2 support for modern web standards, vendor directories for dependency management

## Go 1.7
- **Release Date:** August 15, 2016
- **Features:** Context package in standard library, subtest benchmarks, profile-guided optimization, vendor support standard, binary size reduction
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Profile-guided optimization for runtime performance, binary size reductions
- **Security:** Context propagation for request-scoped security
- **Why Introduced:** Context package for cancellation and deadline propagation, optimization improvements

## Go 1.8
- **Release Date:** February 16, 2017
- **Features:** HTTP/2 server push, grace period for server shutdown, subtests and sub-benchmarks, type assertions for interfaces, sort.Slice, improved defer performance
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Defer 3x faster, subtests improve test parallelism
- **Security:** Graceful shutdown prevents request dropping during deployment
- **Why Introduced:** Server improvements, faster defer for common patterns

## Go 1.9
- **Release Date:** August 24, 2017
- **Features:** Type aliases, Monotonic clock readings, math/bits package, parallel compilation, sync.Map, test helper functions
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Monotonic clocks prevent time manipulation, sync.Map for concurrent map access
- **Security:** Monotonic clock readings prevent timing attacks
- **Why Introduced:** Type aliases for code evolution, monotonic clocks for accurate timing

## Go 1.10
- **Release Date:** February 16, 2018
- **Features:** Automatic caching of test results, default GOPATH, go test caching, pprof labels, HTTP transport improvements, strings.Builder
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Test caching speeds up development cycles
- **Security:** HTTP transport improvements for connection security
- **Why Introduced:** Developer productivity improvements, test caching for faster feedback

## Go 1.11
- **Release Date:** August 24, 2018
- **Features:** Modules (experimental), WebAssembly support, experimental garbage collector tuning, improved error messages
- **Deprecated:** GOPATH mode (modules experimental)
- **Removed:** N/A
- **Performance:** WASM support enables Go in browsers, module system improves dependency management
- **Security:** Modules provide reproducible builds and dependency verification
- **Why Introduced:** Modules for modern dependency management, WebAssembly for browser compilation

## Go 1.12
- **Release Date:** February 25, 2019
- **Features:** Modules improvements (ready for production), improved fmt.Errorf with %w, TLS 1.3 default, improved binary analysis, test -coverpkg improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** TLS 1.3 faster handshake, improved fmt.Errorf performance
- **Security:** TLS 1.3 default provides stronger encryption
- **Why Introduced:** Modules production-ready, TLS 1.3 for modern security

## Go 1.13
- **Release Date:** September 3, 2019
- **Features:** Error wrapping with errors.Is/As/Unwrap, number literal syntax (binary, octal, hex floats), module mirror and checksum database, TLS 1.3 default enabled
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Number parsing improvements, module verification efficiency
- **Security:** Module checksum database prevents supply chain attacks
- **Why Introduced:** Error handling improvements, number syntax, module security

## Go 1.14
- **Release Date:** February 25, 2020
- **Features:** Module support (default), goroutine preemption (asynchronous), interface embedding improvements, os.UserHomeDir, math/rand improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Asynchronous goroutine preemption reduces scheduling latency
- **Security:** Module mode default improves dependency security
- **Why Introduced:** Goroutine preemption for better scheduling, modules as default

## Go 1.15
- **Release Date:** August 11, 2020
- **Features:** Smaller binaries (debug info), faster builds, improved TLS defaults, linker improvements, time/tzdata embedded, improved GC for large heaps
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** 20% faster builds, 30% smaller binaries, improved GC for large heaps
- **Security:** Improved TLS defaults, time/tzdata embedded for accurate time handling
- **Why Introduced:** Build and binary size optimizations, security improvements

## Go 1.16
- **Release Date:** February 16, 2021
- **Features:** go install with version, module-aware mode default, embed package, io/fs, vendor always used if present, darwin arm64 support, retire GOPATH mode
- **Deprecated:** GOPATH mode completely
- **Removed:** GOPATH mode
- **Performance:** Module-aware mode default speeds dependency resolution
- **Security:** Embed package for embedding static assets, vendor always used for reproducibility
- **Why Introduced:** Module mode default, embed for static assets, fs abstraction

## Go 1.17
- **Release Date:** August 16, 2021
- **Features:** Fuzzing support (go test -fuzz), conversion rules for slices, module graph pruning, register-based calling convention (amd64), improved performance
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Register-based calling convention 5-15% faster, module graph pruning
- **Security:** Fuzzing finds security vulnerabilities through random testing
- **Why Introduced:** Fuzzing for better test coverage, register calling convention for speed

## Go 1.18
- **Release Date:** March 15, 2022
- **Features:** Generics (type parameters), workspace mode (go work), fuzzing improvements, any comparable constraints, type inference, GOVERSION environment variable
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Generics reduce allocation through type specialization, workspace mode improves multi-module development
- **Security:** Type safety improvements through generics
- **Why Introduced:** Generics for code reuse, workspace for multi-module development

## Go 1.19
- **Release Date:** August 16, 2022
- **Features:** doc comments (//line), runtime/metrics, GOMEMLIMIT, soft memory limit, sync.Pool improvements, debug/rlimit, atomic.Int64 type
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** GOMEMLIMIT for better memory management, sync.Pool improvements
- **Security:** Runtime metrics for better monitoring, rlimit for resource control
- **Why Introduced:** Memory management improvements, documentation enhancements

## Go 1.20
- **Release Date:** February 1, 2023
- **Features:** Profile-guided optimization (PGO), wrap multiple errors, arena experiment, crypto/ecdh, unsafe.SliceData, unsafe.String
- **Deprecated:** Arena (experimental)
- **Removed:** N/A
- **Performance:** PGO provides 2-7% performance improvement, arena for efficient allocation
- **Security:** crypto/ecdh for elliptic curve Diffie-Hellman, improved crypto
- **Why Introduced:** PGO for production performance, crypto improvements

## Go 1.21
- **Release Date:** August 8, 2023
- **Features:** slog structured logging, min/max built-ins, improved garbage collector, forward toolchain compatibility, slices/maps packages, errors.Join
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Improved garbage collector with reduced latency
- **Security:** Structured logging for better security audit trails
- **Why Introduced:** Standard structured logging, built-in min/max, improved GC

## Go 1.22
- **Release Date:** February 6, 2024
- **Features:** For-range integers, improved HTTP router, goroutine profiling, runtime/trace v2, improved build caching, better error messages
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** HTTP router improvements, goroutine profiling for concurrency analysis
- **Security:** Improved HTTP routing for better request handling security
- **Why Introduced:** Language ergonomics improvements, HTTP routing, profiling enhancements
