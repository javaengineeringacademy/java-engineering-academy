# PHP Architecture

## Zend Engine

The Zend Engine is the core of PHP, handling parsing, compilation, and execution.

- **Lexer**: Tokenizes PHP source code into tokens
- **Parser**: Converts tokens into an Abstract Syntax Tree (AST)
- **Compiler**: Transforms AST into Zend Opcode (bytecode)
- **Executor**: Executes opcodes using a virtual machine

The engine uses a two-stack architecture: one for operands and one for operators. Each request is processed in isolation with separate memory spaces.

## OPcache

OPcache is a bytecode cache built into PHP since version 5.5.

- **Opcode Cache**: Stores compiled opcodes in shared memory
- **Script Cache**: Caches entire script files
- **Class Cache**: Caches class metadata and function tables
- **Restart Detection**: Automatically invalidates cache on file changes

Key configuration:
- `opcache.enable`: Enable or disable OPcache
- `opcache.memory_consumption`: Memory allocated for bytecode
- `opcache.max_accelerated_files`: Maximum number of scripts to cache
- `opcache.revalidate_freq`: How often to check for file changes

## SAPI (Server API)

SAPI defines how PHP interfaces with the web server.

- **CLI SAPI**: Command-line interface for scripts and cron jobs
- **FPM SAPI**: FastCGI Process Manager for production servers
- **Apache SAPI**: Mod_php for Apache web server
- **CGI SAPI**: Common Gateway Interface (legacy)

Each SAPI has its own lifecycle, configuration handling, and request processing model.

## PHP-FPM

PHP-FPM is a process manager for FastCGI-based PHP deployments.

- **Master Process**: Manages worker processes and configuration
- **Worker Processes**: Handle individual PHP requests
- **Pool Managers**: Manage separate process pools with different configurations

Process management modes:
- **Static**: Fixed number of worker processes
- **Dynamic**: Adjusts worker count based on demand
- **Ondemand**: Creates workers only when requests arrive

## Request Lifecycle

1. SAPI receives request from web server
2. PHP initializes configuration and extensions
3. Lexing and parsing produce AST
4. Compilation generates Zend opcodes
5. Executor runs opcodes and produces output
6. Response sent to web server
7. PHP cleans up resources and resets state

## Extension System

PHP extensions are written in C and load into the engine at runtime.

- **Standard Extensions**: Compiled into PHP by default
- **User Extensions**: Loaded via `extension` directive in php.ini
- **Zend Extensions**: Provide low-level hooks (debuggers, profilers)

Common extensions: `mbstring`, `curl`, `pdo`, `json`, `openssl`, `xml`

## Memory Management

PHP uses reference counting for memory management.

- **Reference Counting**: Tracks how many variables reference a value
- **Copy-on-Write**: Variables share data until modification
- **Garbage Collector**: Cycles reference counting cannot detect
- **Zend Memory Manager**: Per-request allocation with automatic cleanup

Memory is freed at the end of each request, preventing leaks across requests.
