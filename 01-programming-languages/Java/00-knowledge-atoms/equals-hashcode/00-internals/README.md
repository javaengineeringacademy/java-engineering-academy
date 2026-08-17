# Internals: Equals & HashCode

## Overview
This folder contains internal implementation details of how equals() and hashCode() work in Java collections.

## Files
- `EqualsHashCodeInternals.java` — Demonstrates hash bucket mechanics, collision handling, and contract violations

## What You'll Learn
- How HashMap uses hashCode() to locate buckets
- How equals() resolves hash collisions
- What happens when the equals/hashCode contract is violated
- The impact of poor hashCode() implementations on collection performance

## Prerequisites
- Basic understanding of equals() and hashCode()
- Familiarity with HashMap and HashSet
