# Bash Fundamentals

Variables, arrays, conditionals, loops, functions, strings, arithmetic, I/O, and more.

## Table of Contents

- [Shebang](#shebang)
- [Variables](#variables)
- [Arrays](#arrays)
- [Conditionals](#conditionals)
- [Loops](#loops)
- [Functions](#functions)
- [String Operations](#string-operations)
- [Arithmetic](#arithmetic)
- [Input/Output](#inputoutput)
- [Exit Codes](#exit-codes)
- [Permissions](#permissions)

---

## Shebang

The shebang tells the system which interpreter to use.

```bash
#!/bin/bash          # Bash shell
#!/bin/sh            # POSIX sh (portable)
#!/usr/bin/env bash  # Portable Bash (finds bash in PATH)
#!/bin/zsh           # Zsh shell
```

### Script Execution

```bash
# Make script executable
chmod +x script.sh

# Run script
./script.sh

# Run with bash explicitly
bash script.sh

# Run in subshell
(source script.sh)
```

---

## Variables

### Defining Variables

```bash
# No spaces around =
name="John"
age=30
readonly PI=3.14  # Cannot be changed

# Command substitution
current_date=$(date +%Y-%m-%d)
file_count=$(ls -1 | wc -l)
```

### Using Variables

```bash
echo "Name: $name"
echo "Name: ${name}"
echo "Today is $current_date"

# String concatenation
greeting="Hello, ${name}!"
```

### Special Variables

```bash
$0          # Script name
$1, $2, ... # Positional arguments
$#          # Number of arguments
$@          # All arguments (as separate words)
$*          # All arguments (as single string)
$?          # Exit code of last command
$$          # Process ID of current shell
$!          # PID of last background process
```

### Environment Variables

```bash
# Set environment variable
export PATH="/usr/local/bin:$PATH"
export EDITOR="vim"

# Common environment variables
echo "Home: $HOME"
echo "User: $USER"
echo "Shell: $SHELL"
echo "Path: $PATH"
echo "PWD: $PWD"
```

### Variable Scope

```bash
# Global variable
global_var="I'm global"

function my_func() {
    # Local variable (only exists in function)
    local local_var="I'm local"
    echo "$local_var"
}

my_func
echo "$global_var"    # Works
echo "$local_var"     # Empty (not accessible)
```

### Default Values

```bash
# Use default if unset
echo "${name:-Unknown}"       # Use "Unknown" if name is unset
echo "${name:=Unknown}"       # Set name to "Unknown" if unset
echo "${name:+Has value}"     # Use "Has value" if name is set
echo "${name:?Error message}" # Error if name is unset
```

---

## Arrays

### Indexed Arrays

```bash
# Declare array
fruits=("apple" "banana" "cherry" "date")

# Access elements
echo "${fruits[0]}"     # apple (0-indexed)
echo "${fruits[@]}"     # All elements
echo "${#fruits[@]}"    # Length: 4

# Modify element
fruits[1]="blueberry"

# Add element
fruits+=("elderberry")

# Slice
echo "${fruits[@]:1:2}"   # banana cherry (start at 1, length 2)

# Delete element
unset fruits[1]
```

### Associative Arrays (Bash 4+)

```bash
# Declare associative array
declare -A colors
colors[red]="#FF0000"
colors[green]="#00FF00"
colors[blue]="#0000FF"

# Access
echo "${colors[red]}"
echo "${colors[@]}"    # All values
echo "${!colors[@]}"   # All keys

# Add key-value pair
colors[yellow]="#FFFF00"

# Check if key exists
if [[ ${colors[red]+exists} ]]; then
    echo "Red exists"
fi
```

### Array Operations

```bash
arr=(1 2 3 4 5)

# Append
arr+=(6 7)

# Remove by index
unset arr[2]  # Removes 3

# Iterate
for item in "${arr[@]}"; do
    echo "$item"
done

# Sort
sorted=($(echo "${arr[@]}" | tr ' ' '\n' | sort))

# Reverse
reversed=($(echo "${arr[@]}" | tac))
```

---

## Conditionals

### if/elif/else

```bash
if [ "$a" -eq "$b" ]; then
    echo "Equal"
elif [ "$a" -gt "$b" ]; then
    echo "Greater"
else
    echo "Less"
fi
```

### Comparison Operators

```bash
# Numeric comparisons
-eq     # Equal
-ne     # Not equal
-gt     # Greater than
-ge     # Greater than or equal
-lt     # Less than
-le     # Less than or equal

# String comparisons
=       # Equal
!=      # Not equal
-z      # Empty string
-n      # Non-empty string

# File tests
-f      # Regular file exists
-d      # Directory exists
-e      # File/directory exists
-r      # Readable
-w      # Writable
-x      # Executable
-s      # File exists and is not empty
```

### [[ ]] Enhanced Test

```bash
# String pattern matching
if [[ "$name" == J* ]]; then
    echo "Starts with J"
fi

# Regex matching
if [[ "$email" =~ ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$ ]]; then
    echo "Valid email"
fi

# Logical operators
if [[ $a -gt 0 && $b -lt 10 ]]; then
    echo "Both conditions met"
fi

if [[ $a -eq 0 || $b -eq 0 ]]; then
    echo "At least one condition met"
fi
```

### case Statement

```bash
case "$input" in
    start)
        echo "Starting service"
        ;;
    stop)
        echo "Stopping service"
        ;;
    restart)
        echo "Restarting service"
        ;;
    *)
        echo "Unknown command: $input"
        exit 1
        ;;
esac
```

### Ternary-like Patterns

```bash
# Simple ternary
result=$([ $a -gt $b ] && echo "a is bigger" || echo "b is bigger")

# Null coalescing
value=${input:-default}
```

---

## Loops

### for Loop

```bash
# Range
for i in {1..5}; do
    echo "$i"
done

# With step
for i in {0..20..5}; do
    echo "$i"
done

# List
for fruit in apple banana cherry; do
    echo "$fruit"
done

# Array
arr=(one two three)
for item in "${arr[@]}"; do
    echo "$item"
done

# C-style
for ((i=0; i<10; i++)); do
    echo "$i"
done
```

### while Loop

```bash
# Basic while
count=1
while [ $count -le 5 ]; do
    echo "$count"
    ((count++))
done

# Read file line by line
while IFS= read -r line; do
    echo "$line"
done < file.txt

# Read command output
while IFS= read -r line; do
    echo "$line"
done < <(ls -la)
```

### until Loop

```bash
# Runs until condition is true
count=1
until [ $count -gt 5 ]; do
    echo "$count"
    ((count++))
done
```

### Loop Control

```bash
# break - exit loop
for i in {1..10}; do
    if [ $i -eq 5 ]; then
        break
    fi
    echo "$i"
done

# continue - skip iteration
for i in {1..10}; do
    if [ $((i % 2)) -eq 0 ]; then
        continue
    fi
    echo "$i"  # Only odd numbers
done

# break with label (Bash 4+)
for i in {1..5}; do
    for j in {1..5}; do
        if [ $j -eq 3 ]; then
            break 2  # Breaks outer loop
        fi
        echo "$i $j"
    done
done
```

### Infinite Loops

```bash
# while true
while true; do
    read -p "Enter command: " cmd
    [ "$cmd" == "quit" ] && break
done

# until false
until false; do
    sleep 1
done
```

---

## Functions

### Basic Function

```bash
# Syntax 1
function greet() {
    echo "Hello, $1!"
}

# Syntax 2
greet() {
    echo "Hello, $1!"
}

# Call function
greet "World"
```

### Return Values

```bash
# Return string via stdout
get_name() {
    echo "John Doe"
}

name=$(get_name)

# Return via global variable
result=""
calculate() {
    result=$(( $1 + $2 ))
}

calculate 5 3
echo "$result"  # 8

# Return code
is_valid() {
    if [ "$1" -gt 0 ]; then
        return 0  # Success
    else
        return 1  # Failure
    fi
}

if is_valid 5; then
    echo "Valid"
fi
```

### Function Arguments

```bash
process_args() {
    echo "Total args: $#"
    echo "All args: $@"
    echo "First arg: $1"
    echo "Second arg: $2"
}

process_args "hello" "world" "foo"
```

### Recursive Function

```bash
factorial() {
    if [ $1 -le 1 ]; then
        echo 1
    else
        echo $(( $1 * $(factorial $(( $1 - 1 ))) ))
    fi
}

echo $(factorial 5)  # 120
```

### Function Scope

```bash
my_var="global"

my_func() {
    my_var="modified"  # Modifies global variable
    local local_var="local"  # Local to function
}

my_func
echo "$my_var"     # "modified"
echo "$local_var"  # Empty
```

---

## String Operations

### Length

```bash
str="Hello, World!"
echo "${#str}"          # 13
```

### Substring

```bash
str="Hello, World!"

echo "${str:0:5}"       # Hello (start at 0, length 5)
echo "${str:7}"         # World! (from index 7)
echo "${str: -6}"       # World! (6 from end)
```

### Replace

```bash
str="Hello, World!"

echo "${str/World/Bash}"     # Hello, Bash! (first occurrence)
echo "${str//o/0}"           # Hell0, W0rld! (all occurrences)
```

### Case Conversion

```bash
str="Hello, World!"

echo "${str^^}"          # HELLO, WORLD!
echo "${str,,}"          # hello, world!
echo "${str^}"           # Hello, World! (first char upper)
```

### Trim

```bash
str="  Hello, World!  "

echo "${str# }"          # Remove leading space
echo "${str## }"         # Remove all leading spaces
echo "${str% }"          # Remove trailing space
echo "${str%% }"         # Remove all trailing spaces

# Trim whitespace
trimmed=$(echo "$str" | xargs)
```

### String Testing

```bash
str="Hello"

# Check if empty
if [ -z "$str" ]; then
    echo "Empty"
fi

# Check if not empty
if [ -n "$str" ]; then
    echo "Not empty"
fi

# String contains
if [[ "$str" == *"ell"* ]]; then
    echo "Contains 'ell'"
fi

# String starts with
if [[ "$str" == Hello* ]]; then
    echo "Starts with Hello"
fi
```

### String Splitting

```bash
# Split by delimiter
csv="apple,banana,cherry"
IFS=',' read -ra fruits <<< "$csv"
for fruit in "${fruits[@]}"; do
    echo "$fruit"
done

# Split into array
str="one two three"
arr=($str)
echo "${arr[1]}"  # two
```

---

## Arithmetic

### Integer Arithmetic

```bash
a=10
b=3

# Using $(( ))
echo $((a + b))     # 13
echo $((a - b))     # 7
echo $((a * b))     # 30
echo $((a / b))     # 3
echo $((a % b))     # 1

# Increment
((a++))
((a += 5))

# Using let
let "c = a + b"
```

### Floating-Point Arithmetic

```bash
# Using bc
result=$(echo "scale=2; 10 / 3" | bc)
echo "$result"  # 3.33

# Using awk
result=$(awk "BEGIN {printf \"%.2f\", 10/3}")
echo "$result"  # 3.33
```

### Random Numbers

```bash
# Random number (0-32767)
echo $RANDOM

# Random in range (1-100)
echo $(( (RANDOM % 100) + 1 ))

# Random between min and max
min=1
max=100
echo $(( RANDOM % (max - min + 1) + min ))
```

### Math Functions

```bash
# Square root
echo "sqrt(144)" | bc  # 12

# Power
echo "2^10" | bc  # 1024

# Scale for precision
echo "scale=10; 1/3" | bc  # 0.3333333333
```

---

## Input/Output

### Reading Input

```bash
# Basic input
read -p "Enter name: " name

# Silent input (passwords)
read -sp "Enter password: " password

# With default
read -p "Continue? [Y/n]: " choice
choice=${choice:-Y}

# Read into array
read -p "Enter values: " -a values

# Read with timeout
read -t 5 -p "Quick! Enter something: " input
```

### Output

```bash
# Echo
echo "Hello, World!"
echo -n "No newline"        # No trailing newline
echo -e "Tab\there"         # Enable escape sequences

# Printf
printf "Name: %s\n" "$name"
printf "Pi: %.2f\n" 3.14159
printf "%-10s %10s\n" "Name" "Value"
```

### Redirection

```bash
# Output to file (overwrite)
echo "Hello" > file.txt

# Output to file (append)
echo "World" >> file.txt

# Error output
command 2> error.log

# Both output and errors
command > all.log 2>&1

# Discard output
command > /dev/null

# Discard everything
command > /dev/null 2>&1

# Here document
cat << EOF
Line 1
Line 2
EOF

# Here string
grep "pattern" <<< "string to search"
```

### File Descriptors

```bash
# Standard streams
0  # stdin
1  # stdout
2  # stderr

# Custom file descriptor
exec 3> output.txt
echo "Hello" >&3
exec 3>&-

# Read from custom descriptor
exec 3< input.txt
read -u 3 line
exec 3<&-
```

---

## Exit Codes

### Checking Exit Codes

```bash
# Run command and check
if command; then
    echo "Success"
else
    echo "Failed"
fi

# Check exit code
command
echo "Exit code: $?"

# Use in condition
command || echo "Command failed"
command && echo "Command succeeded"
```

### Common Exit Codes

| Code | Meaning |
|------|---------|
| 0 | Success |
| 1 | General error |
| 2 | Misuse of shell command |
| 126 | Permission denied |
| 127 | Command not found |
| 128 | Invalid exit argument |
| 130 | Ctrl+C (SIGINT) |
| 255 | Exit code out of range |

### Custom Exit Codes

```bash
exit_on_error() {
    if [ $? -ne 0 ]; then
        echo "Error occurred in line $1"
        exit 1
    fi
}

# Usage
some_command
exit_on_error $LINENO
```

### trap for Cleanup

```bash
cleanup() {
    echo "Cleaning up..."
    rm -f "$temp_file"
}

trap cleanup EXIT

temp_file=$(mktemp)
# ... do work ...
# cleanup runs automatically on exit
```

---

## Permissions

### File Permissions

```bash
# View permissions
ls -la file.txt
# -rw-r--r-- 1 user group 1234 Jan 1 00:00 file.txt
#  ^^^         owner read/write
#     ^^^      group read
#        ^^^   others read

# Change permissions
chmod 755 script.sh      # rwxr-xr-x
chmod +x script.sh       # Add execute
chmod -w file.txt        # Remove write

# Change owner
chown user:group file.txt
```

### Permission Numbers

```
4 = read (r)
2 = write (w)
1 = execute (x)

7 = rwx (4+2+1)
6 = rw- (4+2+0)
5 = r-x (4+0+1)
4 = r-- (4+0+0)
0 = --- (0+0+0)
```

### Common Permission Patterns

```bash
chmod 755 directory   # rwxr-xr-x (common for directories)
chmod 700 private     # rwx------ (private)
chmod 644 file.txt    # rw-r--r-- (common for files)
chmod 600 secret      # rw------- (sensitive files)
```

### umask

```bash
# Check current umask
umask

# Set umask (default permissions for new files)
umask 022  # Files: 644, Directories: 755
umask 077  # Files: 600, Directories: 700
```

### Special Permissions

```bash
# Setuid (run as file owner)
chmod u+s program
chmod 4755 program

# Setgid (run as group)
chmod g+s directory
chmod 2755 directory

# Sticky bit (only owner can delete files)
chmod +t directory
chmod 1755 directory
```

---

## Best Practices

1. **Always quote variables**: `"$var"` not `$var`
2. **Use `[[ ]]`** instead of `[ ]` for better safety
3. **Use `local`** in functions to avoid global pollution
4. **Check exit codes** after important commands
5. **Use `set -e`** to exit on errors
6. **Use `set -u`** to catch unset variables
7. **Use `set -o pipefail`** to catch pipe failures
8. **Comment your code** for maintainability
9. **Use shellcheck** to lint your scripts
10. **Prefer `$(command)`** over backticks `` `command` ``
