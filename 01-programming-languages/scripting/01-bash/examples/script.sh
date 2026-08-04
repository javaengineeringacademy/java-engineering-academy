#!/bin/bash

# Variables
NAME="Software Engineering Academy"
VERSION="1.0"
echo "Welcome to $NAME v$VERSION"

# Conditionals
if [ -f "/etc/os-release" ]; then
    echo "OS detected:"
    cat /etc/os-release | head -5
fi

# Loops
echo "Languages:"
for lang in Java Python Go Rust TypeScript; do
    echo "  - $lang"
done

# Functions
greet() {
    local name=$1
    echo "Hello, $name!"
}
greet "Developer"

# Arrays
LANGUAGES=("Java" "Python" "Go" "Rust")
echo "First language: ${LANGUAGES[0]}"

# String operations
TEXT="Hello World"
echo "Uppercase: ${TEXT^^}"
echo "Lowercase: ${TEXT,,}"
echo "Length: ${#TEXT}"

# File operations
if [ -d "logs" ]; then
    echo "Logs directory exists"
else
    mkdir -p logs
    echo "Created logs directory"
fi

# Error handling
set -e  # Exit on error
trap 'echo "Error on line $LINENO"' ERR

# Process management
echo "Current PID: $$"
echo "User: $(whoami)"
echo "Date: $(date)"
