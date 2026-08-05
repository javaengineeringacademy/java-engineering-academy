# TypeScript Troubleshooting

## Build Errors

### Module Not Found

```bash
# Check path
ls node_modules/package-name

# Reinstall
rm -rf node_modules package-lock.json
npm install

# Check tsconfig paths
npx tsc --showConfig
```

### Type Errors

```bash
# Show all errors
npx tsc --noEmit 2>&1 | head -50

# Skip library checks
npx tsc --skipLibCheck

# Check specific file
npx tsc --noEmit src/file.ts
```

### Version Mismatch

```bash
# Check TypeScript version
npx tsc --version

# Update types
npm update @types/node
npm update @types/express

# Force specific version
npm install typescript@5.3.0
```

## Runtime Errors

### Cannot Find Module

```typescript
// Check import path
import { helper } from './utils/helper';  // Missing .ts extension?

// CommonJS vs ESM
import helper = require('./helper');  // CommonJS
import helper from './helper.js';     // ESM
```

### Property Does Not Exist

```typescript
// Type assertion (use carefully)
const value = (obj as any).property;

// Type guard
if ('property' in obj) {
  console.log(obj.property);
}

// Optional chaining
const value = obj?.property;
```

### Null Reference

```typescript
// Enable strict null checks
// tsconfig.json
{
  "compilerOptions": {
    "strictNullChecks": true
  }
}

// Handle null
const value = data?.nested?.property ?? 'default';
```

## IDE Issues

### VS Code Not Recognizing Types

```json
// .vscode/settings.json
{
  "typescript.tsdk": "node_modules/typescript/lib"
}
```

### Restart TypeScript Server

```
Cmd+Shift+P > TypeScript: Restart TS Server
```

## Package Issues

### Peer Dependency Conflicts

```bash
# Force install
npm install --legacy-peer-deps

# Or use --force
npm install --force
```

### Missing Types

```bash
# Install types
npm install --save-dev @types/package-name

# If no types exist, create declaration file
// src/types/package-name.d.ts
declare module 'package-name' {
  export function doSomething(): void;
}
```

## Common Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| TS2304: Cannot find name | Missing import/type | Add import or declaration |
| TS2339: Property does not exist | Wrong type | Type assertion or guard |
| TS2345: Argument not assignable | Type mismatch | Check types align |
| TS2531: Object is possibly null | Null access | Add null check |
| TS7006: Parameter has implicit any | Missing type | Add type annotation |

## Performance Issues

### Slow Compilation

```json
{
  "compilerOptions": {
    "incremental": true,
    "skipLibCheck": true,
    "tsBuildInfoFile": "./.tsbuildinfo"
  }
}
```

### Large Bundle Size

```bash
# Analyze bundle
npx source-map-explorer dist/main.js

# Check for unused imports
npx ts-prune | head -20
```
