#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TOTAL_ISSUES=0

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}   Repository Quality Check Summary${NC}"
echo -e "${YELLOW}========================================${NC}"

echo -e "\n${YELLOW}--- Markdown Validation ---${NC}"
bash "$SCRIPT_DIR/validate-markdown.sh"
if [ $? -ne 0 ]; then
    TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
fi

echo -e "\n${YELLOW}--- Java Validation ---${NC}"
bash "$SCRIPT_DIR/validate-java.sh"
if [ $? -ne 0 ]; then
    TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
fi

echo -e "\n${YELLOW}--- Python Validation ---${NC}"
bash "$SCRIPT_DIR/validate-python.sh"
if [ $? -ne 0 ]; then
    TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
fi

echo -e "\n${YELLOW}--- Structure Validation ---${NC}"
bash "$SCRIPT_DIR/validate-structure.sh"
if [ $? -ne 0 ]; then
    TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
fi

echo -e "\n${YELLOW}========================================${NC}"
if [ "$TOTAL_ISSUES" -eq 0 ]; then
    echo -e "${GREEN}All checks passed! Repository is clean.${NC}"
    exit 0
else
    echo -e "${RED}$TOTAL_ISSUES validator(s) reported issues.${NC}"
    exit 1
fi
