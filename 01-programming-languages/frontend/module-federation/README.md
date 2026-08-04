# Module Federation

Module Federation is a Webpack 5 feature that allows JavaScript applications to dynamically load code from other applications at runtime. It enables micro-frontend architectures with shared dependencies.

## Table of Contents

- [Concept](#concept)
- [Host and Remote Configuration](#host-and-remote-configuration)
- [Shared Dependencies](#shared-dependencies)
- [Runtime Loading](#runtime-loading)
- [Version Management](#version-management)
- [Dynamic Federation](#dynamic-federation)
- [Use Cases](#use-cases)
- [Webpack 5 Configuration](#webpack-5-configuration)
- [Troubleshooting](#troubleshooting)

---

## Concept

Module Federation allows multiple independently deployed applications to share code at runtime:

```
Application A (Host)          Application B (Remote)
     |                              |
     |  Loads at runtime            |
     +---------> RemoteEntry.js <---+
     |                              |
     |  Shared dependencies         |
     +---- React, React DOM --------+
```

### Key Terms

- **Host (Shell)**: Application that consumes remote modules
- **Remote**: Application that exposes modules
- **RemoteEntry**: Entry point file that exposes modules
- **Container**: Module that manages exposes and consumes
- **Scope**: Namespace for a container's modules

---

## Host and Remote Configuration

### Host Configuration

```javascript
// host/webpack.config.js
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
  entry: "./src/index.js",
  mode: "development",
  devServer: {
    port: 3000,
    historyApiFallback: true,
  },
  output: {
    publicPath: "http://localhost:3000/",
    uniqueName: "host",
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "host",
      filename: "remoteEntry.js",
      remotes: {
        app1: "app1@http://localhost:3001/remoteEntry.js",
        app2: "app2@http://localhost:3002/remoteEntry.js",
      },
      shared: {
        react: { singleton: true },
        "react-dom": { singleton: true },
      },
    }),
    new HtmlWebpackPlugin({
      template: "./public/index.html",
    }),
  ],
};
```

### Remote Configuration

```javascript
// remote/webpack.config.js
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
  entry: "./src/index.js",
  mode: "development",
  devServer: {
    port: 3001,
    historyApiFallback: true,
  },
  output: {
    publicPath: "http://localhost:3001/",
    uniqueName: "remoteApp",
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "remoteApp",
      filename: "remoteEntry.js",
      exposes: {
        "./Button": "./src/components/Button",
        "./Header": "./src/components/Header",
        "./utils": "./src/utils/index.js",
      },
      shared: {
        react: { singleton: true },
        "react-dom": { singleton: true },
      },
    }),
    new HtmlWebpackPlugin({
      template: "./public/index.html",
    }),
  ],
};
```

---

## Shared Dependencies

Prevent duplicate dependencies across applications:

```javascript
// Basic shared configuration
shared: {
  react: { singleton: true },
  "react-dom": { singleton: true },
}

// Advanced shared configuration
shared: {
  react: {
    singleton: true, // Only one copy loaded
    requiredVersion: "^18.0.0",
    eager: false, // Load on demand
    strictVersion: false, // Warn on version mismatch
  },
  "react-dom": {
    singleton: true,
    requiredVersion: "^18.0.0",
  },
  lodash: {
    singleton: true,
    requiredVersion: "^4.17.21",
  },
  "styled-components": {
    singleton: true,
    requiredVersion: "^5.3.0",
  },
}

// Library sharing
shared: {
  "@mui/material": {
    singleton: true,
    requiredVersion: "^5.0.0",
  },
  "@emotion/react": {
    singleton: true,
    requiredVersion: "^11.0.0",
  },
}
```

### Shared Dependency Modes

```javascript
// Eager loading (loads immediately)
shared: {
  react: {
    singleton: true,
    eager: true, // Load on host initialization
  },
}

// Lazy loading (loads on demand)
shared: {
  react: {
    singleton: true,
    eager: false, // Default behavior
  },
}

// Required version strategies
shared: {
  react: {
    requiredVersion: "^18.0.0", // Minimum version
    strictVersion: false, // Warn, don't error
  },
}
```

---

## Runtime Loading

### Static Imports

```typescript
// Import remote module at build time
import RemoteButton from "remoteApp/Button";
import RemoteHeader from "remoteApp/Header";

function App() {
  return (
    <div>
      <RemoteHeader />
      <RemoteButton>Click me</RemoteButton>
    </div>
  );
}
```

### Dynamic Imports

```typescript
// Load remote module on demand
const RemoteComponent = React.lazy(() => import("remoteApp/Button"));

function App() {
  return (
    <Suspense fallback="Loading...">
      <RemoteComponent />
    </Suspense>
  );
}
```

### Async Component Loading

```typescript
// Async loading with error handling
async function loadRemoteModule(scope: string, module: string) {
  try {
    // Initialize sharing scope
    await __webpack_init_sharing__("default");

    // Get container
    const container = window[scope];
    if (!container) {
      throw new Error(`Container ${scope} not found`);
    }

    // Initialize container
    await container.init(__webpack_share_scopes__.default);

    // Get module factory
    const factory = await container.get(module);
    return factory();
  } catch (error) {
    console.error(`Failed to load ${scope}/${module}:`, error);
    throw error;
  }
}

// React component wrapper
function RemoteModule({ scope, module, fallback = "Loading..." }: RemoteModuleProps) {
  const [Component, setComponent] = useState<React.ComponentType | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadRemoteModule(scope, module)
      .then((m) => setComponent(() => m.default))
      .catch((err) => setError(err.message));
  }, [scope, module]);

  if (error) return <div>Error: {error}</div>;
  if (!Component) return <div>{fallback}</div>;

  return <Component />;
}

// Usage
<RemoteModule scope="remoteApp" module="./Button" />
```

---

## Version Management

### Package.json Configuration

```json
// Host package.json
{
  "name": "host-app",
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0"
  }
}

// Remote package.json
{
  "name": "remote-app",
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0"
  }
}
```

### Version Compatibility Matrix

```markdown
| Dependency | Host | Remote App 1 | Remote App 2 | Status |
|------------|------|--------------|--------------|--------|
| React | 18.2 | 18.2 | 18.1 | Compatible |
| React DOM | 18.2 | 18.2 | 18.1 | Compatible |
| Redux | 4.2 | 4.1 | 4.2 | Warning |
| Material UI | 5.14 | 5.14 | 5.13 | Compatible |
```

### Version Validation

```javascript
// webpack.config.js
shared: {
  react: {
    singleton: true,
    requiredVersion: "^18.0.0",
    strictVersion: true, // Error on mismatch
    fallback: false, // Don't use alternative version
  },
}

// Runtime version check
function checkVersionCompatibility(scope: string) {
  const container = window[scope];
  if (!container) return false;

  const shared = container._share_scopes?.default;
  if (!shared) return false;

  for (const [name, info] of Object.entries(shared)) {
    if (info.version && !semver.satisfies(info.version, "^18.0.0")) {
      console.warn(`Version mismatch for ${name}: ${info.version}`);
      return false;
    }
  }

  return true;
}
```

---

## Dynamic Federation

Load remotes dynamically at runtime:

```typescript
// Dynamic remotes configuration
const dynamicRemotes = async () => {
  const response = await fetch("/api/remotes");
  const remotes = await response.json();

  return remotes.reduce((acc, remote) => {
    acc[remote.name] = `${remote.name}@${remote.url}/remoteEntry.js`;
    return acc;
  }, {});
};

// Webpack configuration
module.exports = async () => {
  const remotes = await dynamicRemotes();

  return {
    plugins: [
      new ModuleFederationPlugin({
        name: "host",
        remotes,
        shared: {
          react: { singleton: true },
          "react-dom": { singleton: true },
        },
      }),
    ],
  };
};

// Runtime dynamic loading
async function loadRemote(scope: string, module: string) {
  await __webpack_init_sharing__("default");
  const container = window[scope];
  await container.init(__webpack_share_scopes__.default);
  const factory = await container.get(module);
  return factory();
}

// React hook for dynamic remotes
function useRemoteModule(scope: string, module: string) {
  const [Component, setComponent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadRemote(scope, module)
      .then((m) => setComponent(m.default))
      .catch(setError)
      .finally(() => setLoading(false));
  }, [scope, module]);

  return { Component, loading, error };
}
```

---

## Use Cases

### Micro-Frontend E-Commerce Platform

```typescript
// Host application
const remotes = {
  cart: "cart@http://localhost:3001/remoteEntry.js",
  checkout: "checkout@http://localhost:3002/remoteEntry.js",
  admin: "admin@http://localhost:3003/remoteEntry.js",
};

// Cart remote exposes
exposes: {
  "./CartWidget": "./src/components/CartWidget",
  "./CartPage": "./src/pages/CartPage",
}

// Checkout remote exposes
exposes: {
  "./CheckoutForm": "./src/components/CheckoutForm",
  "./PaymentMethods": "./src/components/PaymentMethods",
}
```

### Multi-Team Development

```
Team A: Product Catalog
  - Product list
  - Product detail
  - Search

Team B: User Management
  - Authentication
  - User profile
  - Preferences

Team C: Analytics
  - Dashboard
  - Reports
  - Real-time data
```

### A/B Testing and Feature Flags

```typescript
// Dynamic component loading based on feature flags
function FeatureComponent({ feature, fallback }) {
  const [Component, setComponent] = useState(null);

  useEffect(() => {
    const loadFeature = async () => {
      try {
        const remote = feature.enabled ? feature.remote : "control";
        const module = await loadRemote(remote, feature.module);
        setComponent(() => module.default);
      } catch {
        setComponent(() => fallback);
      }
    };

    loadFeature();
  }, [feature]);

  return Component ? <Component /> : <div>Loading...</div>;
}
```

---

## Webpack 5 Configuration

### Complete Host Configuration

```javascript
// webpack.config.js (Host)
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const path = require("path");

module.exports = {
  entry: "./src/index.js",
  mode: "development",
  target: "web",
  devServer: {
    port: 3000,
    hot: true,
    historyApiFallback: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: "[name].[contenthash].js",
    publicPath: "http://localhost:3000/",
    uniqueName: "host",
    clean: true,
  },
  resolve: {
    extensions: [".js", ".jsx", ".ts", ".tsx", ".json"],
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx|ts|tsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env", "@babel/preset-react", "@babel/preset-typescript"],
          },
        },
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"],
      },
    ],
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "host",
      filename: "remoteEntry.js",
      remotes: {
        app1: "app1@http://localhost:3001/remoteEntry.js",
        app2: "app2@http://localhost:3002/remoteEntry.js",
      },
      shared: {
        react: {
          singleton: true,
          requiredVersion: "^18.0.0",
          eager: false,
        },
        "react-dom": {
          singleton: true,
          requiredVersion: "^18.0.0",
          eager: false,
        },
      },
    }),
    new HtmlWebpackPlugin({
      template: "./public/index.html",
      favicon: "./public/favicon.ico",
    }),
  ],
  optimization: {
    splitChunks: false, // Disable for Module Federation
  },
};
```

### Complete Remote Configuration

```javascript
// webpack.config.js (Remote)
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const path = require("path");

module.exports = {
  entry: "./src/index.js",
  mode: "development",
  target: "web",
  devServer: {
    port: 3001,
    hot: true,
    historyApiFallback: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: "[name].[contenthash].js",
    publicPath: "http://localhost:3001/",
    uniqueName: "remoteApp",
    clean: true,
  },
  resolve: {
    extensions: [".js", ".jsx", ".ts", ".tsx", ".json"],
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx|ts|tsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env", "@babel/preset-react", "@babel/preset-typescript"],
          },
        },
      },
    ],
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "remoteApp",
      filename: "remoteEntry.js",
      exposes: {
        "./Button": "./src/components/Button",
        "./Header": "./src/components/Header",
        "./utils": "./src/utils/index.js",
        "./store": "./src/store/index.js",
      },
      shared: {
        react: {
          singleton: true,
          requiredVersion: "^18.0.0",
        },
        "react-dom": {
          singleton: true,
          requiredVersion: "^18.0.0",
        },
      },
    }),
    new HtmlWebpackPlugin({
      template: "./public/index.html",
    }),
  ],
  optimization: {
    splitChunks: false,
  },
};
```

---

## Troubleshooting

### Common Issues

```markdown
### Shared Dependency Conflicts
Problem: Multiple versions of React loaded
Solution: Use singleton: true and align versions

### CORS Errors
Problem: Cross-origin requests blocked
Solution: Add CORS headers in devServer configuration

### Module Not Found
Problem: Cannot resolve remote module
Solution: Ensure remoteEntry.js is accessible and remotes are correct

### Version Mismatch Warnings
Problem: Different versions of shared dependencies
Solution: Align versions across host and remotes

### Build Performance
Problem: Slow builds with many remotes
Solution: Use eager: false and lazy load remotes
```

### Debug Configuration

```javascript
// Enable debug logging
module.exports = {
  plugins: [
    new ModuleFederationPlugin({
      name: "host",
      debug: true, // Enable debug logging
    }),
  ],
};

// Check shared dependencies at runtime
console.log("__webpack_share_scopes__", __webpack_share_scopes__);
console.log("Window containers:", Object.keys(window).filter(k => k.includes("remote")));
```
