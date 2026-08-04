# Bash

Bash (Bourne Again SHell) is a Unix shell and command language for system administration, automation, and scripting.

## Contents

| Section | Description |
|---------|-------------|
| [Fundamentals](fundamentals/README.md) | Variables, arrays, loops, functions, I/O |
| [Advanced](advanced/README.md) | Signals, traps, job control, coprocesses |
| [Scripts](scripts/README.md) | 30+ practical bash scripts |
| [Projects](projects/README.md) | Automation tools and CLI applications |

## What is Bash?

- Default shell on most Linux/macOS systems
- Combines sh, csh, and ksh features
- Scripting language for automation
- Interactive command-line interface

## Quick Start

```bash
#!/bin/bash
# First script
echo "Hello, World!"
```

```bash
# Make executable and run
chmod +x hello.sh
./hello.sh
```

## Core Features

```
Shell Scripting
├── Variables & Arrays
├── Control Flow (if/else, loops)
├── Functions
├── I/O & Redirection
├── Process Management
├── String Manipulation
└── Error Handling
```

## Common Patterns

```bash
# Variables
name="World"
echo "Hello, $name!"

# Arrays
fruits=("apple" "banana" "cherry")
echo "${fruits[0]}"

# Conditionals
if [ "$name" == "World" ]; then
    echo "Yes"
fi

# Loops
for i in {1..5}; do
    echo "$i"
done
```

## Shebang Reference

| Shebang | Shell |
|---------|-------|
| `#!/bin/bash` | Bash |
| `#!/bin/sh` | POSIX sh |
| `#!/usr/bin/env bash` | Portable Bash |
| `#!/bin/zsh` | Zsh |

## Learning Path

1. Start with [Fundamentals](fundamentals/README.md)
2. Explore [Advanced](advanced/README.md) topics
3. Study [Practical Scripts](scripts/README.md)
4. Build [Projects](projects/README.md)
