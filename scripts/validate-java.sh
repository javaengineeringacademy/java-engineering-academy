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
    echo -e "\n${YELLOW}=== Java Validation ===${NC}"
}

print_header

find . -name "*.java" -type f | while read -r file; do
    # Check catch(Exception)
    grep -n 'catch\s*(Exception)' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "Catch(Exception) found"
    done

    # Check printStackTrace()
    grep -n 'printStackTrace()' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "printStackTrace() found"
    done

    # Check raw types (List, Map, Set without generics)
    grep -nE '\b(List|Map|Set|ArrayList|HashMap|HashSet)\s+\w+\s*=' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        if ! echo "$content" | grep -qE '<[^>]+>'; then
            print_issue "$file" "$lineno" "Raw type: $content"
        fi
    done

    # Check System.out in non-demo files
    basename_file=$(basename "$file")
    if ! echo "$basename_file" | grep -qiE 'demo|example|test'; then
        grep -n 'System\.out\.' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
            print_issue "$file" "$lineno" "System.out in non-demo file"
        done
    fi
done

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}All Java files passed!${NC}"
    exit 0
else
    echo -e "\n${RED}$ISSUES issue(s) found.${NC}"
    exit 1
fi
