#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

ISSUES=0

print_issue() {
    local file="$1"
    local line="$2"
    local msg="$3"
    echo -e "${RED}  $file:$line${NC}: $msg"
    ISSUES=$((ISSUES + 1))
}

print_header() {
    echo -e "\n${YELLOW}=== Python Validation ===${NC}"
}

print_header

PYTHON_DIR="${1:-.}"

find "$PYTHON_DIR" -name "*.py" -type f | while read -r file; do
    # Check bare except
    grep -n 'except:' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "Bare except: (use specific exception)"
    done

    # Check except Exception (too broad)
    grep -n 'except Exception' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "except Exception (too broad, use specific exception)"
    done

    # Check mutable default arguments
    grep -nE 'def\s+\w+\(.*=\s*\[\]|def\s+\w+\(.*=\s*\{\}' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "Mutable default argument (use None)"
    done

    # Check global keyword usage
    grep -n '^\s*global ' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "Global keyword (consider class or function params)"
    done

    # Check print() in non-educational files (skip exercises, demos, examples, tests)
    basename_file=$(basename "$file")
    dir_path=$(dirname "$file")
    if ! echo "$basename_file" | grep -qiE 'demo|example|test|exercise|__main__|__init__|main'; then
        if ! echo "$dir_path" | grep -qiE 'exercise|demo|example'; then
            grep -n '^\s*print(' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
                print_issue "$file" "$lineno" "print() in non-demo file (use logging)"
            done
        fi
    fi
done

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}All Python files passed!${NC}"
    exit 0
else
    echo -e "\n${RED}$ISSUES issue(s) found.${NC}"
    exit 1
fi
