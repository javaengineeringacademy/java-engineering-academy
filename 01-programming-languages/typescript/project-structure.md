# TypeScript Project Structure

## Standard Project Layout

```
my-project/
|-- src/
|   |-- index.ts              # Entry point
|   |-- app.ts                # Application setup
|   |-- config/
|   |   |-- index.ts          # Configuration
|   |   |-- database.ts       # DB config
|   |
|   |-- models/
|   |   |-- user.ts           # Domain models
|   |   |-- product.ts
|   |
|   |-- services/
|   |   |-- user.service.ts   # Business logic
|   |   |-- product.service.ts
|   |
|   |-- controllers/
|   |   |-- user.controller.ts
|   |   |-- product.controller.ts
|   |
|   |-- repositories/
|   |   |-- user.repository.ts
|   |
|   |-- middleware/
|   |   |-- auth.ts
|   |   |-- error-handler.ts
|   |
|   |-- utils/
|   |   |-- helpers.ts
|   |   |-- validators.ts
|   |
|   |-- types/
|       |-- index.ts          # Shared types
|       |-- express.d.ts      # Type augmentations
|
|-- tests/
|   |-- unit/
|   |-- integration/
|
|-- dist/                     # Compiled output
|-- node_modules/
|-- tsconfig.json
|-- package.json
|-- .gitignore
|-- README.md
```

## Monorepo Structure

```
monorepo/
|-- packages/
|   |-- core/                 # Shared core
|   |   |-- src/
|   |   |-- tsconfig.json
|   |   |-- package.json
|   |
|   |-- api/                  # Backend API
|   |   |-- src/
|   |   |-- tsconfig.json
|   |   |-- package.json
|   |
|   |-- web/                  # Frontend
|       |-- src/
|       |-- tsconfig.json
|       |-- package.json
|
|-- tsconfig.json             # Root config with references
|-- package.json              # Workspace config
```

## File Naming Conventions

| Pattern | Usage |
|---------|-------|
| `kebab-case.ts` | General files |
| `user.service.ts` | Service files |
| `user.controller.ts` | Controller files |
| `user.model.ts` | Model files |
| `user.test.ts` | Test files |
| `index.ts` | Barrel exports |

## Barrel Exports

```typescript
// src/models/index.ts
export { User } from './user';
export { Product } from './product';
export type { UserDTO, ProductDTO } from './dtos';

// src/services/index.ts
export { UserService } from './user.service';
export { ProductService } from './product.service';
```

## Configuration Files

| File | Purpose |
|------|---------|
| `tsconfig.json` | TypeScript compiler options |
| `package.json` | Dependencies and scripts |
| `.eslintrc.js` | Linting rules |
| `.prettierrc` | Formatting rules |
| `.env` | Environment variables |
| `.gitignore` | Git ignore patterns |
| `jest.config.js` | Test configuration |

## Entry Point Pattern

```typescript
// src/index.ts
import { createApp } from './app';
import { config } from './config';

const app = createApp();
const port = config.port;

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
```
