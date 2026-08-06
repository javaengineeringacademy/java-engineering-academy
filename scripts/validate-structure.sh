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

# ──────────────────────────────────────────────
# Existing checks
# ──────────────────────────────────────────────

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

# ──────────────────────────────────────────────
# NEW CHECK: Duplicate detection – files with
#            identical content
# ──────────────────────────────────────────────
echo -e "${YELLOW}  Checking for duplicate files...${NC}"
find . -type f ! -path './.git/*' ! -name '.DS_Store' -exec md5 -r {} \; 2>/dev/null \
    | sort \
    | awk '{print $1, $2}' \
    | awk '{
        content_hash = $1
        filepath = $2
        if (content_hash in seen) {
            print "DUPLICATE: " filepath " has same content as " seen[content_hash]
        } else {
            seen[content_hash] = filepath
        }
    }' | while read -r line; do
        print_issue "$line"
    done

# ──────────────────────────────────────────────
# NEW CHECK: Learning path integrity – all
#            modules referenced in learning paths
#            must exist as directories
# ──────────────────────────────────────────────
echo -e "${YELLOW}  Checking learning path integrity...${NC}"
for lp_dir in 27-learning-paths/*/; do
    [ -d "$lp_dir" ] || continue
    lp_readme="$lp_dir/README.md"
    [ -f "$lp_readme" ] || continue

    # Extract local markdown links from the learning path README
    # Matches [text](./path) or [text](path) patterns
    grep -oE '\]\(\./[^)]+\)|\]\([^)h][^)]*\)' "$lp_readme" 2>/dev/null | \
        sed 's/](//' | sed 's/)//' | while read -r link; do
        # Skip external URLs and anchors
        echo "$link" | grep -qE '^https?://|^#' && continue

        link="${link#./}"

        # Resolve relative to the learning path directory
        target="$lp_dir$link"
        # Also check from project root
        root_target="./$link"

        if [ ! -d "$target" ] && [ ! -f "$target" ] && [ ! -d "$root_target" ] && [ ! -f "$root_target" ]; then
            print_issue "Learning path $lp_dir references non-existent module: $link"
        fi
    done
done

# ──────────────────────────────────────────────
# NEW CHECK: Every module must have exercises/
#            directory
# ──────────────────────────────────────────────
echo -e "${YELLOW}  Checking modules have exercises/ directory...${NC}"
# A "module" is a directory that contains Java source files
for dir in $(find . -name "*.java" -type f -exec dirname {} \; | sort -u); do
    # Skip build output directories
    echo "$dir" | grep -qE 'target/|build/|\.git' && continue

    # Walk up to find the true module root (stop at top-level numbered dirs)
    module_root="$dir"
    while [ "$module_root" != "." ]; do
        parent=$(dirname "$module_root")
        # Stop if parent is a top-level numbered directory or project root
        basename_parent=$(basename "$parent")
        if echo "$basename_parent" | grep -qE '^[0-9]{2}-' || [ "$parent" = "." ]; then
            break
        fi
        module_root="$parent"
    done

    if [ ! -d "$module_root/exercises" ]; then
        print_issue "Module $module_root is missing exercises/ directory"
    fi
done

# ──────────────────────────────────────────────
# NEW CHECK: Every module must have quiz.md
#            (reinforced check across all module
#            directories, not just Java-only ones)
# ──────────────────────────────────────────────
echo -e "${YELLOW}  Checking modules have quiz.md...${NC}"
find . -mindepth 2 -maxdepth 5 -type d ! -path './.git/*' ! -path '*/target/*' ! -path '*/build/*' | while read -r dir; do
    # A module directory contains Java files or is explicitly a module
    has_java=$(find "$dir" -maxdepth 1 -name "*.java" -type f 2>/dev/null | head -1)
    has_readme=$(find "$dir" -maxdepth 1 -name "README.md" -type f 2>/dev/null | head -1)

    # Only check directories that look like modules (have README or Java files)
    if [ -n "$has_java" ] || [ -n "$has_readme" ]; then
        if [ ! -f "$dir/quiz.md" ]; then
            print_issue "Module $dir is missing quiz.md"
        fi
    fi
done

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}Structure is clean!${NC}"
    exit 0
else
    echo -e "\n${RED}$ISSUES issue(s) found.${NC}"
    exit 1
fi
