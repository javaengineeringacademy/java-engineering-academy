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

# ──────────────────────────────────────────────
# Existing checks
# ──────────────────────────────────────────────

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

    # ──────────────────────────────────────────────
    # NEW CHECK: Cross-link validity – all local
    #            links must point to existing files
    # ──────────────────────────────────────────────
    grep -nE '\[.*\]\([^)]+\)' "$file" 2>/dev/null | while IFS=: read -r lineno content; do
        # Extract all links from the line
        echo "$content" | grep -oE '\]\([^)]+\)' | sed 's/](//' | sed 's/)//' | while read -r link; do
            # Skip external URLs, anchors-only, and images referenced by path
            echo "$link" | grep -qE '^https?://|^#' && continue

            # Strip leading ./ if present
            link="${link#./}"

            dir=$(dirname "$file")

            # Resolve relative path from the file's directory
            target="$dir/$link"

            # Also check from project root
            root_target="./$link"

            if [ ! -f "$target" ] && [ ! -d "$target" ] && [ ! -f "$root_target" ] && [ ! -d "$root_target" ]; then
                print_issue "$file" "$lineno" "Broken link: $link (target not found)"
            fi
        done
    done

    # ──────────────────────────────────────────────
    # NEW CHECK: README format consistency –
    #            required sections must be present
    # ──────────────────────────────────────────────
    basename_file=$(basename "$file")
    if [ "$basename_file" = "README.md" ]; then
        required_sections=("Overview" "Why" "Internal Working" "Examples" "Performance" "Pitfalls" "Interview Questions" "References")
        for section in "${required_sections[@]}"; do
            # Match ## heading containing the section name (case-insensitive)
            if ! grep -qiE "^##\s+.*${section}" "$file" 2>/dev/null; then
                print_issue "$file" "0" "README missing required section: $section"
            fi
        done
    fi

    # ──────────────────────────────────────────────
    # NEW CHECK: Quiz completeness – minimum 10
    #            questions, must have code examples
    # ──────────────────────────────────────────────
    if [ "$basename_file" = "quiz.md" ]; then
        # Count questions (## Question N pattern)
        question_count=$(grep -ciE '^##\s+question\s+[0-9]+' "$file" 2>/dev/null)
        question_count=${question_count:-0}
        if [ "$question_count" -lt 10 ]; then
            print_issue "$file" "0" "Quiz has only $question_count question(s), minimum is 10"
        fi

        # Check for code examples in questions (``` blocks)
        code_block_count=$(grep -c '^```' "$file" 2>/dev/null)
        code_block_count=${code_block_count:-0}
        code_blocks=$((code_block_count / 2))
        if [ "$code_blocks" -eq 0 ]; then
            print_issue "$file" "0" "Quiz has no code examples (expected code blocks)"
        fi
    fi

    # ──────────────────────────────────────────────
    # NEW CHECK: Naming conventions – directories
    #            must be lowercase-hyphenated
    # ──────────────────────────────────────────────
    file_dir=$(dirname "$file")
    # Skip root and .git
    if [ "$file_dir" != "." ] && echo "$file_dir" | grep -qvE '^\.'; then
        # Check each directory component
        echo "$file_dir" | tr '/' '\n' | while read -r component; do
            [ -z "$component" ] && continue
            # Must match: lowercase letters, digits, hyphens only
            if ! echo "$component" | grep -qE '^[a-z0-9]+(-[a-z0-9]+)*$'; then
                print_issue "$file" "0" "Directory naming violation: '$component' (must be lowercase-hyphenated)"
                break
            fi
        done
    fi
done

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}All markdown files passed!${NC}"
    exit 0
else
    echo -e "\n${RED}$ISSUES issue(s) found.${NC}"
    exit 1
fi
