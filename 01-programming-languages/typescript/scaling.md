# TypeScript Scaling Strategies

## Large Codebases

### Project References

```json
// Root tsconfig.json
{
  "files": [],
  "references": [
    { "path": "./packages/core" },
    { "path": "./packages/api" },
    { "path": "./packages/web" },
    { "path": "./packages/shared" }
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

### Incremental Builds

```json
{
  "compilerOptions": {
    "incremental": true,
    "tsBuildInfoFile": "./.tsbuildinfo"
  }
}
```

## Monorepos

### Workspaces (npm)

```json
// Root package.json
{
  "name": "monorepo",
  "workspaces": ["packages/*"]
}
```

### Turborepo

```json
// turbo.json
{
  "pipeline": {
    "build": { "dependsOn": ["^build"] },
    "test": { "dependsOn": ["build"] },
    "lint": {}
  }
}
```

### Nx

```bash
# Create workspace
npx create-nx-workspace my-workspace

# Add library
nx g @nx/js:lib shared-util

# Add application
nx g @nx/node:app my-api
```

## Module Federation

```javascript
// webpack.config.js (host)
const ModuleFederationPlugin = require('webpack/lib/container/ModuleFederationPlugin');

module.exports = {
  plugins: [
    new ModuleFederationPlugin({
      name: 'host',
      remotes: {
        remote: 'remote@http://localhost:3001/remoteEntry.js',
      },
    }),
  ],
};
```

## Code Splitting

```typescript
// Route-based splitting
const Dashboard = React.lazy(() => import('./pages/Dashboard'));
const Settings = React.lazy(() => import('./pages/Settings'));

// Dynamic imports for heavy modules
async function loadChart(data: Data[]) {
  const { Chart } = await import('./heavy-chart');
  return new Chart(data);
}
```

## Shared Packages

```
packages/
|-- shared/
|   |-- src/
|   |   |-- types/
|   |   |-- utils/
|   |   |-- constants/
|   |   |-- index.ts
|   |-- package.json
|   |-- tsconfig.json
```

```typescript
// Import in other packages
import { formatDate, User } from '@monorepo/shared';
```

## Type Sharing

```typescript
// packages/shared/src/types/index.ts
export interface User {
  id: string;
  email: string;
  role: 'admin' | 'user';
}

export interface ApiResponse<T> {
  data: T;
  status: number;
  message: string;
}

// Import across packages
import type { User, ApiResponse } from '@monorepo/shared';
```

## Performance at Scale

| Strategy | Benefit |
|----------|---------|
| Project references | Faster builds |
| Incremental compilation | Only rebuild changed |
| Bundle splitting | Smaller initial load |
| Lazy loading | On-demand code |
| Tree shaking | Remove unused code |
| Caching | Reuse build output |

## Build Pipeline

```json
{
  "scripts": {
    "build": "turbo run build",
    "test": "turbo run test",
    "lint": "turbo run lint",
    "typecheck": "turbo run typecheck"
  }
}
```
