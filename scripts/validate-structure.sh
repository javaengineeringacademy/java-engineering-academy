#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

ISSUES=0

print_issue() {
    local msg="$1"
    echo -e "${RED}  $msg${NC}"
    ISSUES=$((ISSUES + 1))
}

print_header() {
    echo -e "\n${YELLOW}=== Structure Validation ===${NC}"
}

print_header

# Check for modules (directories with Java files)
for dir in $(find . -name "*.java" -type f -exec dirname {} \; | sort -u); do
    # Check missing README.md
    if [ ! -f "$dir/README.md" ]; then
        print_issue "Missing README.md in $dir"
    fi

    # Check missing quiz.md
    if [ ! -f "$dir/quiz.md" ]; then
        print_issue "Missing quiz.md in $dir"
    fi
done

# Check for empty files
find . -type f -empty | while read -r file; do
    # Skip .git directory
    echo "$file" | grep -q '^\.git' && continue
    print_issue "Empty file: $file"
done

# Check for inconsistent naming (uppercase in paths)
find . -type f -o -type d | grep '[A-Z]' | grep -v '.git/' | while read -r path; do
    # Only flag if it's a directory name issue (not file extensions)
    dir_name=$(basename "$path")
    if echo "$dir_name" | grep -qE '[A-Z]' && [ -d "$path" ]; then
        print_issue "Inconsistent naming (uppercase): $path"
    fi
done

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}Structure is clean!${NC}"
    exit 0
else
    echo -e "\n${RED}$ISSUES issue(s) found.${NC}"
    exit 1
fi
