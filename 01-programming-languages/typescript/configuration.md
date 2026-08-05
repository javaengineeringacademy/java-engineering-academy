# TypeScript Configuration

## tsconfig.json Structure

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "lib": ["ES2020", "DOM"],
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "resolveJsonModule": true,
    "declaration": true,
    "declarationMap": true,
    "sourceMap": true,
    "incremental": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist"]
}
```

## Strict Mode Options

| Option | Description |
|--------|-------------|
| `strict` | Enables all strict checks |
| `noImplicitAny` | Error on implicit `any` |
| `strictNullChecks` | Null/undefined checking |
| `strictFunctionTypes` | Stricter function type checking |
| `strictBindCallApply` | Stricter bind/call/apply |
| `noImplicitThis` | Error on `this` with implicit `any` |
| `alwaysStrict` | Emit `"use strict"` in all files |

## Module Resolution

```json
{
  "compilerOptions": {
    "moduleResolution": "node16",
    "baseUrl": "./src",
    "paths": {
      "@/*": ["./*"],
      "@models/*": ["models/*"],
      "@utils/*": ["utils/*"]
    }
  }
}
```

## Project References

```json
// Root tsconfig.json
{
  "files": [],
  "references": [
    { "path": "./packages/core" },
    { "path": "./packages/api" },
    { "path": "./packages/web" }
  ]
}

// packages/core/tsconfig.json
{
  "compilerOptions": {
    "composite": true,
    "declaration": true,
    "outDir": "./dist"
  }
}
```

## Environment-Specific Configs

```json
// tsconfig.base.json
{
  "compilerOptions": {
    "strict": true,
    "esModuleInterop": true
  }
}

// tsconfig.json
{
  "extends": "./tsconfig.base.json",
  "compilerOptions": {
    "outDir": "./dist"
  }
}
```

## Compiler Options Reference

### Output Control

- `outDir`: Output directory
- `rootDir`: Root source directory
- `declaration`: Generate .d.ts files
- `declarationMap`: Generate declaration source maps
- `sourceMap`: Generate JavaScript source maps
- `removeComments`: Remove comments from output
- `noEmit`: Type-check only, no output

### Type Checking

- `noUnusedLocals`: Error on unused locals
- `noUnusedParameters`: Error on unused parameters
- `noImplicitReturns`: Error on missing returns
- `noFallthroughCasesInSwitch`: Error on switch fallthrough
- `exactOptionalPropertyTypes`: Strict optional properties

### Interop

- `esModuleInterop`: CommonJS/ES module interop
- `allowSyntheticDefaultImports`: Allow default imports
- `isolatedModules`: Ensure per-file transpilation
- `verbatimModuleSyntax`: Enforce import/export syntax
