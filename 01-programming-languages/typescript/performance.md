# TypeScript Performance

## Compilation Speed

### Optimization Tips

```json
// tsconfig.json optimizations
{
  "compilerOptions": {
    "incremental": true,
    "tsBuildInfoFile": "./.tsbuildinfo",
    "skipLibCheck": true
  }
}
```

- Use `incremental` for faster rebuilds
- Enable `skipLibCheck` to skip type checking declaration files
- Use project references for large codebases
- Avoid `--noEmit` combined with emit options

### Build Tool Comparison

| Tool | Speed | Features |
|------|-------|----------|
| tsc | Baseline | Full type checking |
| esbuild | 10-100x faster | Bundling, no type checking |
| SWC | 20-70x faster | Near tsc compatibility |
| Vite | Very fast | HMR, dev server |

## Tree Shaking

Remove unused code from bundles.

```typescript
// Good - named exports enable tree shaking
export function formatDate() { /* ... */ }
export function parseDate() { /* ... */ }

// Bad - default export with everything
export default { formatDate, parseDate };
```

### Webpack Configuration

```javascript
// webpack.config.js
module.exports = {
  mode: 'production',
  optimization: {
    usedExports: true,
    sideEffects: false,
  },
};
```

## Declaration Files

Generate `.d.ts` files for library consumers.

```json
{
  "compilerOptions": {
    "declaration": true,
    "declarationDir": "./types",
    "declarationMap": true
  }
}
```

```typescript
// Input
export function add(a: number, b: number): number;

// Generated declaration
export declare function add(a: number, b: number): number;
```

## Runtime Performance

### Avoid Allocations

```typescript
// Bad - creates new array each time
const getItems = () => items.filter(i => i.active).map(i => i.name);

// Bad - creates new object each call
const getConfig = () => ({ debug: true, timeout: 3000 });

// Good - reuse or memoize
const ACTIVE_ITEMS = items.filter(i => i.active);
```

### Use Appropriate Data Structures

```typescript
// Bad - O(n) lookup
const user = users.find(u => u.id === targetId);

// Good - O(1) lookup
const userMap = new Map(users.map(u => [u.id, u]));
const user = userMap.get(targetId);
```

### Lazy Loading

```typescript
// Dynamic imports for code splitting
const module = await import('./heavy-module');

// Lazy initialization
class Service {
  private _instance: HeavyThing | null = null;
  get instance() {
    return (this._instance ??= new HeavyThing());
  }
}
```

## Bundle Size Analysis

```bash
# Analyze bundle
npx source-map-explorer dist/main.js

# webpack-bundle-analyzer
npx webpack-bundle-analyzer stats.json
```

## Profiling

```bash
# Node.js profiling
node --prof app.js
node --prof-process isolate-*.log > processed.txt

# Chrome DevTools
# Enable Performance tab, record, analyze heap snapshots
```
