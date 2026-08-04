# Advanced Bash

Signals, traps, job control, process substitution, named pipes, regex, here documents, coprocesses, and debugging.

## Table of Contents

- [Signals and Traps](#signals-and-traps)
- [Job Control](#job-control)
- [Process Substitution](#process-substitution)
- [Named Pipes (FIFOs)](#named-pipes-fifos)
- [Advanced Arrays](#advanced-arrays)
- [Regular Expressions](#regular-expressions)
- [Here Documents and Here Strings](#here-documents-and-here-strings)
- [Coprocesess](#coprocesses)
- [Debugging](#debugging)
- [Concurrency](#concurrency)

---

## Signals and Traps

### Common Signals

| Signal | Number | Description |
|--------|--------|-------------|
| SIGHUP | 1 | Hangup detected |
| SIGINT | 2 | Interrupt (Ctrl+C) |
| SIGQUIT | 3 | Quit (Ctrl+\) |
| SIGKILL | 9 | Kill (cannot be caught) |
| SIGTERM | 15 | Termination |
| SIGUSR1 | 10 | User-defined 1 |
| SIGUSR2 | 12 | User-defined 2 |
| SIGSTOP | 19 | Stop process |
| SIGCONT | 18 | Continue stopped process |
| SIGCHLD | 17 | Child stopped/exited |

### Trap Command

```bash
# Trap signals
trap 'echo "Ctrl+C caught"' INT
trap 'echo "Terminated"' TERM
trap 'echo "SIGHUP received"' HUP

# Ignore signals
trap '' INT TERM    # Ignore Ctrl+C and kill

# Reset signal to default
trap - INT TERM
```

### Cleanup Trap

```bash
#!/bin/bash
temp_file=""
temp_dir=""

cleanup() {
    echo "Cleaning up..."
    [ -n "$temp_file" ] && rm -f "$temp_file"
    [ -n "$temp_dir" ] && rm -rf "$temp_dir"
}

trap cleanup EXIT  # Runs on any exit

temp_file=$(mktemp)
temp_dir=$(mktemp -d)

# ... do work ...
# cleanup runs automatically
```

### Signal Handling Examples

```bash
# Graceful shutdown
running=true
trap 'running=false; echo "Shutting down..."' TERM INT

while $running; do
    echo "Working..."
    sleep 1
done
echo "Stopped cleanly"
```

### trap DEBUG

```bash
# Debug trap - runs before each command
trap 'echo "DEBUG: $BASH_COMMAND"' DEBUG

# Trace execution
trap 'echo "Line $LINENO: $BASH_COMMAND"' DEBUG
```

### trap ERR

```bash
# Run on any error
trap 'echo "Error on line $LINENO (exit code: $?)"' ERR

# Combined trap
trap 'cleanup; exit 1' ERR
```

---

## Job Control

### Background Jobs

```bash
# Run command in background
long_running_command &

# Get background job PID
echo $!  # PID of last background process

# Wait for background job
wait $!

# Wait for all background jobs
wait
```

### Job Management

```bash
# List jobs
jobs -l       # List with PIDs
jobs -r       # Running jobs
jobs -s       # Stopped jobs

# Bring job to foreground
fg %1         # Job number 1

# Send job to background
bg %1

# Suspend current job (Ctrl+Z)
# Then resume in background
bg
```

### Disown

```bash
# Remove job from job table (survives shell exit)
disown %1

# Disown all jobs
disown -a

# Disown specific job
disown -h %1  # Mark to receive HUP on shell exit
```

### Wait with Timeout

```bash
# Wait for process with timeout
timeout 10 long_running_command
if [ $? -eq 124 ]; then
    echo "Command timed out"
fi

# Wait for PID with timeout
wait -n $pid  # Wait for specific PID
```

---

## Process Substitution

### Basic Syntax

```bash
# Compare output of two commands
diff <(ls -la /dir1) <(ls -la /dir2)

# Feed input to command
grep "pattern" <(cat file.txt)

# Multiple process substitutions
paste <(cut -f1 file1) <(cut -f2 file2) > combined.txt
```

### Practical Examples

```bash
# Compare sorted versions of files
diff <(sort file1.txt) <(sort file2.txt)

# Process files in parallel
while read -r line; do
    echo "$line"
done < <(find /path -name "*.txt")

# Read output in loop
while IFS= read -r line; do
    echo "Processing: $line"
done < <(curl -s "https://api.example.com/data")
```

### Named vs Unnamed Process Substitution

```bash
# Unnamed (most common)
diff <(cmd1) <(cmd2)

# Named (when you need the FD)
exec 3< <(cmd1)
while read -r line <&3; do
    echo "$line"
done
exec 3<&-
```

---

## Named Pipes (FIFOs)

### Creating FIFOs

```bash
# Create named pipe
mkfifo /tmp/mypipe

# Check if it's a pipe
ls -la /tmp/mypipe
# prw-r--r-- 1 user user 0 ... /tmp/mypipe
```

### Producer-Consumer Pattern

```bash
# Producer (Terminal 1)
echo "data1" > /tmp/mypipe &
echo "data2" > /tmp/mypipe &
echo "data3" > /tmp/mypipe &

# Consumer (Terminal 2)
while IFS= read -r line; do
    echo "Received: $line"
done < /tmp/mypipe
```

### Inter-Process Communication

```bash
# Writer
mkfifo /tmp/comm_pipe
for i in {1..5}; do
    echo "Message $i" > /tmp/comm_pipe
    sleep 1
done
rm /tmp/comm_pipe

# Reader
while read -r msg; do
    echo "Got: $msg"
done < /tmp/comm_pipe
```

### Named Pipe with Timeout

```bash
mkfifo /tmp/timed_pipe

# Read with timeout
if read -t 5 -r line < /tmp/timed_pipe; then
    echo "Got: $line"
else
    echo "Timed out"
fi

rm /tmp/timed_pipe
```

---

## Advanced Arrays

### Sparse Arrays

```bash
# Bash allows sparse arrays
declare -A sparse
sparse[0]="zero"
sparse[100]="hundred"
sparse[500]="five hundred"

echo "${sparse[@]}"      # All values
echo "${!sparse[@]}"     # All indices
```

### Array Slicing

```bash
arr=(a b c d e f g h)

echo "${arr[@]:2:3}"    # c d e (start at 2, length 3)
echo "${arr[@]:5}"      # f g h (from index 5)
echo "${arr[@]::3}"     # a b c (first 3)
echo "${arr[@]:: -2}"   # All except last 2
```

### Array Manipulation

```bash
arr=(1 2 3 4 5)

# Prepend element
arr=(0 "${arr[@]}")

# Append element
arr+=("6")

# Remove element by index
unset arr[2]

# Remove element by value
arr=("${arr[@]/3}")

# Reverse array
reversed=("${arr[@]: -1}" "${arr[@]:0:${#arr[@]}-1}")

# Shuffle array
shuffled=($(shuf -e "${arr[@]}"))
```

### Array Filtering

```bash
# Filter array elements
arr=(1 2 3 4 5 6 7 8 9 10)

# Keep even numbers
even=($(for i in "${arr[@]}"; do
    [ $((i % 2)) -eq 0 ] && echo "$i"
done))

# Using filter function
filter_array() {
    local -n result=$1
    local filter=$2
    local -a temp=()
    
    for item in "${!result[@]}"; do
        if eval "$filter"; then
            temp+=("${result[$item]}")
        fi
    done
    
    result=("${temp[@]}")
}
```

### Array to String and Back

```bash
# Array to string
arr=(one two three four)
str=$(IFS=', '; echo "${arr[*]}")
echo "$str"  # one, two, three, four

# String to array
IFS=',' read -ra arr <<< "$str"
echo "${arr[1]}"  # two
```

---

## Regular Expressions

### Basic Regex

```bash
# =~ operator in [[ ]]
str="Hello 123 World"
if [[ $str =~ [0-9]+ ]]; then
    echo "Contains number: ${BASH_REMATCH[0]}"
fi

# Capture groups
if [[ $str =~ ([A-Z][a-z]+) ([0-9]+) ([A-Z][a-z]+) ]]; then
    echo "Word 1: ${BASH_REMATCH[1]}"
    echo "Number: ${BASH_REMATCH[2]}"
    echo "Word 2: ${BASH_REMATCH[3]}"
fi
```

### Common Patterns

```bash
# Email validation
email="user@example.com"
if [[ $email =~ ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$ ]]; then
    echo "Valid email"
fi

# Phone number (US)
phone="555-123-4567"
if [[ $phone =~ ^[0-9]{3}-[0-9]{3}-[0-9]{4}$ ]]; then
    echo "Valid phone"
fi

# IP address
ip="192.168.1.100"
if [[ $ip =~ ^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$ ]]; then
    echo "Valid IP format"
fi
```

### grep Regex

```bash
# Basic regex (grep)
grep "pattern" file.txt

# Extended regex (grep -E or egrep)
grep -E "pattern1|pattern2" file.txt
grep -E "[0-9]+" file.txt

# Perl regex (grep -P)
grep -P "\d+" file.txt
```

### sed Regex

```bash
# Basic sed
sed 's/old/new/' file.txt        # First occurrence
sed 's/old/new/g' file.txt       # All occurrences
sed -i 's/old/new/g' file.txt    # In-place edit

# Extended regex
sed -E 's/[0-9]+/NUM/g' file.txt

# Delete lines
sed '/pattern/d' file.txt
```

---

## Here Documents and Here Strings

### Here Documents

```bash
# Basic
cat << EOF
Hello, World!
This is line 2.
EOF

# With variable expansion
name="John"
cat << EOF
Hello, $name!
Today is $(date).
EOF

# Quoted delimiter (no expansion)
cat << 'EOF'
This $variable won't be expanded.
$(date) won't run.
EOF

# Indented (<<- strips leading tabs)
if true; then
	cat <<-EOF
	This is indented.
	EOF
fi

# Here document to file
cat > file.txt << EOF
Line 1
Line 2
EOF

# Here document to command
mysql -u root << EOF
CREATE DATABASE mydb;
USE mydb;
CREATE TABLE users (id INT, name VARCHAR(50));
EOF
```

### Here Strings

```bash
# Basic here string
grep "pattern" <<< "string to search"

# Process here string
rev <<< "hello"
echo "hello" | rev  # Same result

# Multi-word here string
read -r first last <<< "John Doe"
echo "$first"  # John

# Here string with arithmetic
echo $(( 5 + 3 <<< "3" ))  # Doesn't work as expected

# Use with commands
bc <<< "2+2"
awk '{print $2}' <<< "one two three"
```

### Here Document Patterns

```bash
# Generate multiple files
for name in {1..3}; do
    cat > "file_${name}.txt" << EOF
File number: $name
Created: $(date)
Content goes here
EOF
done

# Create config file
cat > /etc/myapp.conf << EOF
[general]
debug = false
log_level = info

[database]
host = localhost
port = 5432
name = myapp
EOF
```

---

## Coprocesses

### Basic Coprocess

```bash
# Start coprocess
coproc mycoproc {
    cat -  # Reads from stdin
}

# Write to coprocess
echo "Hello" >&${mycoproc[1]}

# Read from coprocess
read -r line <&${mycoproc[0]}
echo "$line"  # Hello

# Close coprocess
exec {mycoproc[1]}>&-
exec {mycoproc[0]}<&-
```

### Bidirectional Communication

```bash
# Calculator coprocess
coproc CALC {
    bc -l
}

# Send calculation
echo "2+2" >&${CALC[0]}

# Read result
read -r result <&${CALC[1]}
echo "$result"  # 4

# More complex
echo "scale=10; sqrt(2)" >&${CALC[0]}
read -r result <&${CALC[1]}
echo "$result"  # 1.4142135624
```

### Coprocess with Loop

```bash
coproc PROCESS {
    while IFS= read -r line; do
        echo "Processed: $line"
    done
}

# Send data
for i in {1..5}; do
    echo "Data $i" >&${PROCESS[0]}
done

exec {PROCESS[0]}>&-

# Read all output
while read -r line <&${PROCESS[1]}; do
    echo "$line"
done
```

---

## Debugging

### set Options

```bash
# Exit on error
set -e

# Exit on unset variable
set -u

# Trace execution
set -x

# Exit on pipe failure
set -o pipefail

# Combine
set -euo pipefail
```

### Debug Output

```bash
# Enable debug mode
set -x

# Or for single command
set -x; command; set +x

# Debug with custom prefix
export PS4='+${BASH_SOURCE}:${LINENO}: '
set -x
```

### Debug Trap

```bash
# Print each command before execution
trap 'echo "[DEBUG] $BASH_COMMAND"' DEBUG

# More detailed debug
trap '
    echo "=== Line $LINENO ==="
    echo "Command: $BASH_COMMAND"
    echo "Exit code: $?"
' DEBUG
```

### Bash Debugging Tools

```bash
# Bash syntax check
bash -n script.sh

# Run with xtrace
bash -x script.sh

# Run with verbose
bash -v script.sh
```

### shellcheck

```bash
# Install shellcheck
brew install shellcheck  # macOS
apt install shellcheck   # Ubuntu

# Run shellcheck
shellcheck script.sh

# Common issues shellcheck finds:
# SC2006: Use $(..) instead of legacy `..`
# SC2046: Quote this to prevent word splitting
# SC2086: Double quote to prevent globbing
# SC2155: Declare and assign separately
```

### Common Debug Patterns

```bash
# Debug variable
echo "DEBUG: var='$var'"

# Debug function
debug() {
    echo "DEBUG: $@" >&2
}

# Debug with caller info
debug() {
    echo "DEBUG [${BASH_SOURCE[1]}:${BASH_LINENO[0]}]: $@" >&2
}
```

---

## Concurrency

### Background Processes

```bash
# Run multiple commands in background
for i in {1..5}; do
    sleep $((RANDOM % 5)) &
done

# Wait for all
wait
echo "All done"
```

### Parallel Execution

```bash
# GNU parallel
cat urls.txt | parallel -j 4 wget {}

# xargs parallel
cat files.txt | xargs -P 4 -I {} gzip {}

# Background with limit
max_jobs=4
for file in *.txt; do
    # Wait if too many jobs
    while [ $(jobs -r | wc -l) -ge $max_jobs ]; do
        sleep 0.1
    done
    process_file "$file" &
done
wait
```

### Synchronization with Named Pipes

```bash
# Barrier pattern
mkfifo /tmp/barrier
rm /tmp/barrier

# Worker 1
echo "Worker 1 done" | tee /tmp/worker1 &
# Worker 2
echo "Worker 2 done" | tee /tmp/worker2 &

# Wait for all
wait
```

### Timeout Patterns

```bash
# Timeout a command
timeout 10s long_command

# Custom timeout with kill
(
    long_command &
    pid=$!
    (sleep 10 && kill $pid 2>/dev/null) &
    watch_pid=$!
    wait $pid 2>/dev/null
    kill $watch_pid 2>/dev/null
)
```

---

## Advanced Patterns

### Argument Parsing

```bash
# Parse named arguments
parse_args() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -f|--file)
                FILE="$2"
                shift 2
                ;;
            -v|--verbose)
                VERBOSE=true
                shift
                ;;
            -h|--help)
                show_help
                exit 0
                ;;
            *)
                echo "Unknown option: $1"
                exit 1
                ;;
        esac
    done
}

parse_args "$@"
```

### Retry Pattern

```bash
retry() {
    local max_attempts=$1
    local delay=$2
    shift 2
    
    local attempt=1
    while [ $attempt -le $max_attempts ]; do
        if "$@"; then
            return 0
        fi
        echo "Attempt $attempt failed. Retrying in ${delay}s..."
        sleep $delay
        ((attempt++))
    done
    
    echo "All $max_attempts attempts failed"
    return 1
}

# Usage
retry 3 2 curl "https://api.example.com/data"
```

### Logging

```bash
LOG_LEVEL=${LOG_LEVEL:-INFO}

log() {
    local level=$1
    shift
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] [$level] $*"
}

log DEBUG "Debug message"
log INFO "Info message"
log WARN "Warning message"
log ERROR "Error message"
```

### State Machine

```bash
state="init"

while true; do
    case "$state" in
        init)
            echo "Initializing..."
            state="running"
            ;;
        running)
            echo "Running..."
            sleep 1
            state="checking"
            ;;
        checking)
            echo "Checking..."
            if [ -f /tmp/stop ]; then
                state="stopping"
            else
                state="running"
            fi
            ;;
        stopping)
            echo "Stopping..."
            break
            ;;
    esac
done
```

---

## Best Practices

1. **Use `set -euo pipefail`** at the top of scripts
2. **Trap signals** for cleanup on exit
3. **Use process substitution** instead of temp files
4. **Name pipes** for inter-process communication
5. **Use `[[ ]]`** for regex and pattern matching
6. **Debug with `set -x`** or `bash -x script.sh`
7. **Run shellcheck** to catch common errors
8. **Use named references** for array manipulation
9. **Implement proper argument parsing** for complex scripts
10. **Handle signals gracefully** for long-running scripts
