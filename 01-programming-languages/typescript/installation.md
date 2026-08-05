# TypeScript Installation Guide

## Installing TypeScript

```bash
# Global installation
npm install -g typescript

# Project-local (recommended)
npm install --save-dev typescript

# Verify installation
tsc --version
```

## Project Setup

```bash
# Initialize project
mkdir my-project && cd my-project
npm init -y

# Install TypeScript
npm install --save-dev typescript @types/node

# Create tsconfig.json
npx tsc --init
```

## Types Version Management

### DefinitelyTyped

```bash
# Install types for libraries
npm install --save-dev @types/node
npm install --save-dev @types/express
npm install --save-dev @types/jest

# Search for available types
npm search @types/express
```

### No Types Needed

Many libraries ship their own TypeScript definitions:
- React (`react`)
- Vue (`vue`)
- Angular (`@angular/core`)
- Lodash (`lodash`)

## Package.json Scripts

```json
{
  "scripts": {
    "build": "tsc",
    "watch": "tsc --watch",
    "typecheck": "tsc --noEmit",
    "lint": "tsc --noEmit && eslint src/"
  },
  "devDependencies": {
    "typescript": "^5.3.0",
    "@types/node": "^20.0.0"
  }
}
```

## IDE Setup

### VS Code

```json
// .vscode/settings.json
{
  "typescript.tsdk": "node_modules/typescript/lib",
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "typescript.enablePromptUseWorkspaceTsdk": true
}
```

### Extensions

```json
{
  "recommendations": [
    "dbaeumer.vscode-eslint",
    "esbenp.prettier-vscode",
    "bradlc.vscode-tailwindcss"
  ]
}
```

## Build Tools

### ts-node (Development)

```bash
# Run TypeScript directly
npm install --save-dev ts-node
npx ts-node src/index.ts

# With watch mode
npx ts-node-dev --respawn src/index.ts
```

### esbuild (Bundling)

```bash
npm install --save-dev esbuild
npx esbuild src/index.ts --bundle --outdir=dist
```

### SWC (Fast Compilation)

```bash
npm install --save-dev @swc/core
npx swc src -d dist
```

## Docker

```dockerfile
FROM node:20-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build
CMD ["node", "dist/index.js"]
```

## Quick Start Template

```json
// package.json
{
  "name": "my-ts-project",
  "version": "1.0.0",
  "scripts": {
    "build": "tsc",
    "dev": "ts-node src/index.ts",
    "start": "node dist/index.js"
  }
}
```
