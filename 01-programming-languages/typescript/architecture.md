# TypeScript Architecture

## TypeScript Compiler (tsc)

The compiler performs lexical analysis, parsing, semantic analysis, and emits JavaScript output. It operates in phases to ensure type correctness before code generation.

### Compilation Pipeline

```
Source Code (.ts)
    |
    v
Lexer (Tokenization)
    |
    v
Parser (AST generation)
    |
    v
Type Checker (semantic analysis)
    |
    v
Emitter (JavaScript output)
```

### Compiler Modes

```bash
# Type checking only (no output)
tsc --noEmit

# Watch mode
tsc --watch

# Project build
tsc --project tsconfig.json
```

## Type Checker

The type checker validates type relationships at compile time without affecting runtime behavior.

### Type Inference

```typescript
// Compiler infers types automatically
let x = 42;           // number
let arr = [1, 2, 3];  // number[]
let fn = (a: number) => a * 2;  // (a: number) => number
```

### Structural Typing

TypeScript uses structural typing instead of nominal typing. Types are compatible if their structures match.

```typescript
interface Point { x: number; y: number; }
interface Coordinate { x: number; y: number; }

const p: Point = { x: 1, y: 2 };
const c: Coordinate = p;  // Compatible - same structure
```

## JavaScript Interop

### Declaration Files

```typescript
// Declare external JavaScript libraries
declare module 'external-lib' {
  export function doSomething(input: string): number;
  export interface Config {
    debug: boolean;
    timeout: number;
  }
}
```

### Mixed Projects

```typescript
// Import JavaScript modules
import utils from './utils.js';  // JS file
import { helper } from './helper.ts';  // TS file

// Use JavaScript with type safety
const result: string = utils.format('test');
```

### CommonJS vs ESM

```typescript
// CommonJS (Node.js traditional)
const express = require('express');
module.exports = { myFunction };

// ES Modules (modern standard)
import express from 'express';
export const myFunction = () => {};
```

## Project References

Enable incremental builds across multiple TypeScript projects.

```json
// tsconfig.json
{
  "references": [
    { "path": "./packages/core" },
    { "path": "./packages/utils" }
  ],
  "compilerOptions": {
    "composite": true,
    "declaration": true
  }
}
```

## Compilation Targets

| Target | Use Case |
|--------|----------|
| ES5 | Legacy browsers |
| ES2015+ | Modern browsers |
| ESNext | Latest features |
| ES2017 | Node.js 8+ |
| ES2020 | Node.js 14+ |

## Type-Only Imports

```typescript
// Import only for type checking, erased at compile time
import type { User } from './models';
import { type Config, loadConfig } from './config';
```
