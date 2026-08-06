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
    echo -e "\n${YELLOW}=== Markdown Validation ===${NC}"
}

print_header

find . -name "*.md" -type f | while read -r file; do
    # Check broken code fences (odd number of ```)
    fence_count=$(grep -c '^```' "$file" 2>/dev/null)
    fence_count=${fence_count:-0}
    if [ "$((fence_count % 2))" -ne 0 ]; then
        print_issue "$file" "0" "Odd number of code fences ($fence_count)"
    fi

    # Check placeholder content
    grep -n 'TODO\|FIXME\|Coming soon\|TBD\|PLACEHOLDER' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        print_issue "$file" "$lineno" "Placeholder: $content"
    done

    # Check file length
    line_count=$(wc -l < "$file" 2>/dev/null | tr -d ' ')
    line_count=${line_count:-0}
    if [ "$line_count" -gt 400 ]; then
        print_issue "$file" "0" "File over 400 lines ($line_count lines)"
    fi

    # Check internal links (markdown links to local files)
    grep -nE '\[.*\]\(([^http][^)]+\.md)\)' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        link=$(echo "$content" | grep -oE '\]\([^)]+\)' | sed 's/](//' | sed 's/)//')
        dir=$(dirname "$file")
        target="$dir/$link"
        if [ -n "$link" ] && [ ! -f "$target" ] && [ ! -d "$target" ]; then
            print_issue "$file" "$lineno" "Broken internal link: $link"
        fi
    done
done

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}All markdown files passed!${NC}"
    exit 0
else
    echo -e "\n${RED}$ISSUES issue(s) found.${NC}"
    exit 1
fi
