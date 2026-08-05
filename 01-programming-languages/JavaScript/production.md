# JavaScript Production

## Minification

```javascript
// webpack.config.js
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
    optimization: {
        minimizer: [
            new TerserPlugin({
                terserOptions: {
                    compress: {
                        drop_console: true,
                        drop_debugger: true
                    },
                    output: {
                        comments: false
                    }
                }
            })
        ]
    }
};
```

## Tree Shaking

```javascript
// package.json
{
    "sideEffects": false
}

// Import only what you need
import { debounce } from 'lodash-es';
import { map, filter } from 'lodash-es';

// webpack.config.js
module.exports = {
    optimization: {
        usedExports: true
    }
};
```

## CDN Configuration

```html
<!-- Use CDN for popular libraries -->
<script src="https://cdn.jsdelivr.net/npm/react@18/umd/react.production.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/react-dom@18/umd/react-dom.production.min.js"></script>

<!-- Subresource Integrity -->
<script src="https://cdn.example.com/lib.js"
        integrity="sha384-oqVuAfXRKap7fdgcCY5uykM6+R9GqQ8K/uxy9rx7HNQlGYl1kPzQho1wx4JwY8wC"
        crossorigin="anonymous"></script>
```

## Service Workers

```javascript
// service-worker.js
const CACHE_NAME = 'v1';
const urlsToCache = ['/', '/styles.css', '/app.js'];

self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll(urlsToCache))
    );
});

self.addEventListener('fetch', (event) => {
    event.respondWith(
        caches.match(event.request)
            .then(response => response || fetch(event.request))
    );
});

// Register service worker
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/service-worker.js');
}
```

## Caching Strategies

```javascript
// Cache-first
async function cacheFirst(request) {
    const cached = await caches.match(request);
    return cached || fetch(request);
}

// Network-first
async function networkFirst(request) {
    try {
        const response = await fetch(request);
        const cache = await caches.open('v1');
        cache.put(request, response.clone());
        return response;
    } catch (error) {
        return caches.match(request);
    }
}

// Stale-while-revalidate
async function staleWhileRevalidate(request) {
    const cache = await caches.open('v1');
    const cached = await cache.match(request);
    const fetchPromise = fetch(request).then(response => {
        cache.put(request, response.clone());
        return response;
    });
    return cached || fetchPromise;
}
```

## Compression

```javascript
// gzip compression
const compression = require('compression');
app.use(compression());

// Brotli compression
const brotli = require('brotli');
const compressed = brotli.compress(Buffer.from(html));
```

## Environment Variables

```bash
# .env.production
NODE_ENV=production
API_URL=https://api.production.com
SENTRY_DSN=https://your-dsn@sentry.io

# Build with environment
REACT_APP_API_URL=https://api.production.com npm run build
```

## Error Tracking

```javascript
// Global error handler
window.onerror = function(message, source, lineno, colno, error) {
    console.error('Error:', { message, source, lineno, colno, error });
};

window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled rejection:', event.reason);
});
```
