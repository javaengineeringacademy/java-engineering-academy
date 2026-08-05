# JavaScript Project Structure

## Standard Layout

```
project/
  src/                    # Source code
    index.js              # Entry point
    app.js                # Application setup
    components/           # UI components
      Button.js
      Modal.js
    hooks/                # Custom hooks
      useAuth.js
    services/             # API services
      api.js
    utils/                # Utility functions
      helpers.js
    styles/               # CSS/SCSS files
      global.css
  public/                 # Static assets
    index.html
    images/
  dist/                   # Build output
  tests/                  # Test files
    unit/
    integration/
    e2e/
  docs/                   # Documentation
  scripts/                # Build scripts
  config/                 # Configuration files
  .env                    # Environment variables
  .eslintrc               # ESLint config
  .prettierrc             # Prettier config
  tsconfig.json           # TypeScript config
  webpack.config.js       # Webpack config
  package.json            # Dependencies
  README.md               # Documentation
```

## src/ Directory

Source code organized by feature or layer:

```
src/
  features/               # Feature-based organization
    auth/
      authSlice.js
      authApi.js
      LoginPage.js
    users/
      userSlice.js
      userApi.js
      UserList.js
  shared/                 # Shared components
    components/
    hooks/
    utils/
```

## tests/ Directory

Test organization mirrors source structure:

```
tests/
  unit/                   # Unit tests
    utils/
      helpers.test.js
  integration/            # Integration tests
    api/
      auth.test.js
  e2e/                    # End-to-end tests
    login.test.js
  fixtures/               # Test data
    users.json
  helpers/                # Test utilities
    setup.js
```

## Configuration Files

```
config/
  webpack.dev.js         # Development config
  webpack.prod.js        # Production config
  jest.config.js         # Jest configuration
  babel.config.js        # Babel configuration
```

## Entry Points

- `src/index.js` - Main application entry
- `src/app.js` - Application setup and routing
- `public/index.html` - HTML template
- `scripts/build.js` - Build process
- `scripts/start.js` - Development server

## Module Organization

```javascript
// src/index.js
import app from './app.js';
import { initAuth } from './features/auth/authSlice.js';

initAuth();
app.start();
```

## Best Practices

- Keep entry points minimal
- Group by feature, not type
- Use barrel exports (index.js)
- Separate configuration from code
- Keep tests near source files
- Use consistent naming conventions
