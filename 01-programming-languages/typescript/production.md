# TypeScript Production Deployment

## Build Optimization

### Production tsconfig

```json
{
  "extends": "./tsconfig.json",
  "compilerOptions": {
    "sourceMap": false,
    "declaration": false,
    "removeComments": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noImplicitReturns": true,
    "noFallthroughCasesInSwitch": true
  }
}
```

### Build Scripts

```json
{
  "scripts": {
    "build": "tsc",
    "build:prod": "tsc --project tsconfig.prod.json",
    "prebuild": "rimraf dist",
    "postbuild": "node scripts/optimize.js"
  }
}
```

## Bundling

### Webpack

```javascript
// webpack.config.js
module.exports = {
  entry: './src/index.ts',
  output: { filename: 'bundle.js', path: __dirname + '/dist' },
  resolve: { extensions: ['.ts', '.js'] },
  module: {
    rules: [{ test: /\.ts$/, use: 'ts-loader', exclude: /node_modules/ }],
  },
  optimization: {
    splitChunks: { chunks: 'all' },
    minimize: true,
  },
};
```

### esbuild

```bash
# Fast bundling
npx esbuild src/index.ts --bundle --minify --outdir=dist

# With code splitting
npx esbuild src/index.ts --bundle --splitting --outdir=dist
```

## Docker Deployment

```dockerfile
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM node:20-alpine
WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
COPY package.json ./
EXPOSE 3000
CMD ["node", "dist/index.js"]
```

## Environment Configuration

```typescript
import { config as dotenvConfig } from 'dotenv';
dotenvConfig();

export const config = {
  port: parseInt(process.env.PORT || '3000'),
  nodeEnv: process.env.NODE_ENV || 'development',
  dbUrl: process.env.DATABASE_URL!,
  jwtSecret: process.env.JWT_SECRET!,
};

// Validate required env vars
const required = ['DATABASE_URL', 'JWT_SECRET'];
for (const key of required) {
  if (!process.env[key]) {
    throw new Error(`Missing required env var: ${key}`);
  }
}
```

## Graceful Shutdown

```typescript
const server = app.listen(config.port, () => {
  console.log(`Server running on port ${config.port}`);
});

process.on('SIGTERM', async () => {
  console.log('SIGTERM received, shutting down gracefully');
  server.close(async () => {
    await db.disconnect();
    process.exit(0);
  });
});
```

## Health Checks

```typescript
app.get('/health/live', (req, res) => res.json({ status: 'alive' }));

app.get('/health/ready', async (req, res) => {
  const checks = {
    database: await checkDatabase(),
    cache: await checkCache(),
    queue: await checkQueue(),
  };
  const healthy = Object.values(checks).every(c => c === 'ok');
  res.status(healthy ? 200 : 503).json({ status: healthy ? 'ready' : 'not ready', checks });
});
```

## CI/CD

```yaml
# GitHub Actions
name: Deploy
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20' }
      - run: npm ci
      - run: npm run typecheck
      - run: npm test
      - run: npm run build
```
