# Frontend Performance

Performance optimization ensures fast, responsive user experiences. Poor performance leads to user abandonment, lower conversions, and reduced engagement.

## Table of Contents

- [Core Web Vitals](#core-web-vitals)
- [Bundle Analysis](#bundle-analysis)
- [Code Splitting](#code-splitting)
- [Lazy Loading](#lazy-loading)
- [Tree Shaking](#tree-shaking)
- [Image Optimization](#image-optimization)
- [Caching Strategies](#caching-strategies)
- [Service Workers](#service-workers)
- [CDN](#cdn)

---

## Core Web Vitals

Google's metrics for measuring real-world user experience:

### Largest Contentful Paint (LCP)

Measures loading performance - time for largest content element to render:

```typescript
// Measure LCP
const observer = new PerformanceObserver((list) => {
  const entries = list.getEntries();
  const lastEntry = entries[entries.length - 1];
  console.log("LCP:", lastEntry.startTime);
});

observer.observe({ type: "largest-contentful-paint", buffered: true });

// Good LCP: < 2.5 seconds
// Needs improvement: 2.5 - 4 seconds
// Poor: > 4 seconds
```

**Optimizations:**
```html
<!-- Preload critical resources -->
<link rel="preload" href="/fonts/main.woff2" as="font" crossorigin>
<link rel="preload" href="/hero-image.webp" as="image">

<!-- Use modern image formats -->
<picture>
  <source srcset="/hero.webp" type="image/webp">
  <img src="/hero.jpg" alt="Hero image" loading="eager">
</picture>

<!-- Inline critical CSS -->
<style>
  /* Critical above-the-fold styles */
  .header { display: flex; }
  .hero { height: 100vh; }
</style>

<!-- Defer non-critical CSS -->
<link rel="preload" href="/styles.css" as="style" onload="this.onload=null;this.rel='stylesheet'">
```

### First Input Delay (FID)

Measures interactivity - time from first user interaction to response:

```typescript
// Measure FID
const observer = new PerformanceObserver((list) => {
  for (const entry of list.getEntries()) {
    console.log("FID:", entry.processingStart - entry.startTime);
  }
});

observer.observe({ type: "first-input", buffered: true });

// Good FID: < 100 milliseconds
// Needs improvement: 100 - 300 milliseconds
// Poor: > 300 milliseconds
```

**Optimizations:**
```javascript
// Break up long tasks
function processLargeDataset(data) {
  const chunkSize = 1000;
  let index = 0;

  function processChunk(deadline) {
    while (index < data.length && deadline.timeRemaining() > 0) {
      // Process item
      processItem(data[index]);
      index++;
    }

    if (index < data.length) {
      requestIdleCallback(processChunk);
    }
  }

  requestIdleCallback(processChunk);
}

// Use web workers for heavy computation
const worker = new Worker("/worker.js");
worker.postMessage({ data: largeDataset });
worker.onmessage = (e) => {
  updateUI(e.data);
};
```

### Cumulative Layout Shift (CLS)

Measures visual stability - unexpected layout shifts:

```typescript
// Measure CLS
let clsValue = 0;
let clsEntries = [];

const observer = new PerformanceObserver((list) => {
  for (const entry of list.getEntries()) {
    if (!entry.hadRecentInput) {
      clsValue += entry.value;
      clsEntries.push(entry);
    }
  }
});

observer.observe({ type: "layout-shift", buffered: true });

// Good CLS: < 0.1
// Needs improvement: 0.1 - 0.25
// Poor: > 0.25
```

**Optimizations:**
```html
<!-- Set dimensions for images and videos -->
<img src="photo.jpg" width="800" height="600" alt="Photo">

<!-- Reserve space for dynamic content -->
<div style="min-height: 200px">
  <!-- Ad or dynamic content -->
</div>

<!-- Use CSS aspect-ratio -->
<style>
  .video-container {
    aspect-ratio: 16 / 9;
    width: 100%;
  }
</style>

<!-- Avoid inserting content above existing content -->
<style>
  .ad-slot {
    min-height: 250px;
  }
</style>
```

---

## Bundle Analysis

Analyze bundle size to identify optimization opportunities:

```bash
# Webpack Bundle Analyzer
npm install webpack-bundle-analyzer --save-dev

# In webpack.config.js
const { BundleAnalyzerPlugin } = require("webpack-bundle-analyzer");

module.exports = {
  plugins: [
    new BundleAnalyzerPlugin({
      analyzerMode: "static",
      reportFilename: "bundle-report.html",
    }),
  ],
};

# Vite bundle analysis
npm install rollup-plugin-visualizer --save-dev

# In vite.config.ts
import { visualizer } from "rollup-plugin-visualizer";

export default {
  plugins: [
    visualizer({
      open: true,
      filename: "bundle-stats.html",
    }),
  ],
};

# Next.js bundle analysis
npm install @next/bundle-analyzer --save-dev

# next.config.js
const withBundleAnalyzer = require("@next/bundle-analyzer")({
  enabled: process.env.ANALYZE === "true",
});

module.exports = withBundleAnalyzer({});
```

### Bundle Analysis Checklist

```markdown
- [ ] Identify largest dependencies
- [ ] Check for duplicate packages
- [ ] Remove unused dependencies
- [ ] Replace heavy libraries with lighter alternatives
- [ ] Ensure tree shaking is working
- [ ] Verify code splitting is effective
- [ ] Check for accidental full imports (e.g., lodash vs lodash-es)
```

---

## Code Splitting

Split code into smaller chunks for faster loading:

### Route-Based Splitting

```typescript
// React with React.lazy
import { lazy, Suspense } from "react";

const Home = lazy(() => import("./pages/Home"));
const About = lazy(() => import("./pages/About"));
const Dashboard = lazy(() => import("./pages/Dashboard"));

function App() {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </Suspense>
  );
}

// Next.js dynamic imports
import dynamic from "next/dynamic";

const DynamicComponent = dynamic(() => import("./HeavyComponent"), {
  loading: () => <p>Loading...</p>,
  ssr: false,
});

// With named exports
const DynamicComponent = dynamic(
  () => import("./Module").then((mod) => mod.NamedExport),
  { loading: () => <p>Loading...</p> }
);
```

### Component-Based Splitting

```typescript
// Split by feature
const HeavyChart = lazy(() => import("./features/charts/HeavyChart"));
const DataGrid = lazy(() => import("./features/data-grid/DataGrid"));

// Split by condition
function AdminPanel({ isAdmin }) {
  const [AdminSettings, setAdminSettings] = useState(null);

  useEffect(() => {
    if (isAdmin) {
      import("./AdminSettings").then((mod) => {
        setAdminSettings(() => mod.default);
      });
    }
  }, [isAdmin]);

  return isAdmin && AdminSettings ? <AdminSettings /> : null;
}
```

### Webpack Configuration

```javascript
// webpack.config.js
module.exports = {
  optimization: {
    splitChunks: {
      chunks: "all",
      maxInitialRequests: 25,
      minSize: 20000,
      cacheGroups: {
        vendor: {
          test: /[\\/]node_modules[\\/]/,
          name(module) {
            const packageName = module.context.match(
              /[\\/]node_modules[\\/](.*?)([\\/]|$)/
            )[1];
            return `vendor.${packageName.replace("@", "")}`;
          },
        },
        common: {
          minChunks: 2,
          priority: -10,
          reuseExistingChunk: true,
        },
      },
    },
  },
};
```

---

## Lazy Loading

Defer loading of non-critical resources:

### Images

```html
<!-- Native lazy loading -->
<img src="image.jpg" alt="Description" loading="lazy" />

<!-- Intersection Observer for custom lazy loading -->
<div data-src="image.jpg" class="lazy-image">Loading...</div>

<script>
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const img = entry.target;
        img.src = img.dataset.src;
        observer.unobserve(img);
      }
    });
  });

  document.querySelectorAll(".lazy-image").forEach((img) => {
    observer.observe(img);
  });
</script>

<!-- React lazy image component -->
function LazyImage({ src, alt, ...props }) {
  const [imageSrc, setImageSrc] = useState(null);
  const imgRef = useRef();

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setImageSrc(src);
          observer.unobserve(entry.target);
        }
      },
      { rootMargin: "100px" }
    );

    observer.observe(imgRef.current);

    return () => observer.disconnect();
  }, [src]);

  return <img ref={imgRef} src={imageSrc} alt={alt} {...props} />;
}
```

### Components

```typescript
// React.lazy for component splitting
const Modal = lazy(() => import("./Modal"));
const DatePicker = lazy(() => import("./DatePicker"));

function Form() {
  const [showModal, setShowModal] = useState(false);

  return (
    <div>
      <button onClick={() => setShowModal(true)}>Open Modal</button>
      {showModal && (
        <Suspense fallback={<div>Loading modal...</div>}>
          <Modal onClose={() => setShowModal(false)} />
        </Suspense>
      )}
    </div>
  );
}
```

### Data

```typescript
// Prefetch on hover
function Link({ href, children }) {
  const [prefetched, setPrefetched] = useState(false);

  const prefetch = () => {
    if (!prefetched) {
      const link = document.createElement("link");
      link.rel = "prefetch";
      link.href = href;
      document.head.appendChild(link);
      setPrefetched(true);
    }
  };

  return (
    <a href={href} onMouseEnter={prefetch}>
      {children}
    </a>
  );
}

// Dynamic import with preload
const preloadChart = () => import("./HeavyChart");

function ChartContainer() {
  const [Chart, setChart] = useState(null);

  useEffect(() => {
    // Preload during idle time
    if ("requestIdleCallback" in window) {
      requestIdleCallback(() => {
        preloadChart().then((mod) => setChart(() => mod.default));
      });
    } else {
      preloadChart().then((mod) => setChart(() => mod.default));
    }
  }, []);

  return Chart ? <Chart /> : <div>Loading chart...</div>;
}
```

---

## Tree Shaking

Remove unused code from bundles:

```javascript
// Good: Named exports (tree-shakeable)
// utils.js
export const formatDate = (date) => {...};
export const parseJSON = (str) => {...};
export const debounce = (fn, delay) => {...};

// Import only what you use
import { formatDate } from "./utils"; // Only formatDate is bundled

// Bad: Default export with everything
// utils.js
export default {
  formatDate,
  parseJSON,
  debounce,
};

// All exports are bundled
import utils from "./utils"; // Entire object is bundled

// Package.json sideEffects flag
{
  "name": "my-package",
  "sideEffects": false
}

// Or specify which files have side effects
{
  "sideEffects": ["*.css", "*.scss"]
}
```

### Tree Shaking Configuration

```javascript
// Webpack
module.exports = {
  mode: "production",
  optimization: {
    usedExports: true,
    sideEffects: true,
  },
};

// Vite (uses Rollup)
export default {
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ["react", "react-dom"],
        },
      },
    },
  },
};
```

---

## Image Optimization

Optimize images for better performance:

```html
<!-- Modern formats -->
<picture>
  <source srcset="/image.avif" type="image/avif">
  <source srcset="/image.webp" type="image/webp">
  <img src="/image.jpg" alt="Description">
</picture>

<!-- Responsive images -->
<img
  srcset="/image-400.jpg 400w, /image-800.jpg 800w, /image-1200.jpg 1200w"
  sizes="(max-width: 600px) 100vw, 50vw"
  src="/image-800.jpg"
  alt="Description"
>

<!-- CSS responsive images -->
<style>
  .hero-image {
    background-image: url("/hero-small.jpg");
    background-size: cover;
  }

  @media (min-width: 768px) {
    .hero-image {
      background-image: url("/hero-large.jpg");
    }
  }
</style>
```

### Image Optimization Tools

```bash
# Sharp (Node.js)
npm install sharp

const sharp = require("sharp");

await sharp("input.jpg")
  .resize(800, 600)
  .webp({ quality: 80 })
  .toFile("output.webp");

# Next.js Image Component
import Image from "next/image";

<Image
  src="/photo.jpg"
  width={800}
  height={600}
  alt="Description"
  placeholder="blur"
  blurDataURL="data:image/jpeg;base64,..."
/>
```

---

## Caching Strategies

Implement effective caching for faster repeat visits:

```typescript
// Service Worker caching
const CACHE_NAME = "v1";
const urlsToCache = ["/", "/styles.css", "/app.js"];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(urlsToCache))
  );
});

self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      if (response) {
        return response;
      }
      return fetch(event.request).then((response) => {
        if (!response || response.status !== 200) {
          return response;
        }
        const responseToCache = response.clone();
        caches.open(CACHE_NAME).then((cache) => {
          cache.put(event.request, responseToCache);
        });
        return response;
      });
    })
  );
});

// HTTP caching headers
// Server-side (Express example)
app.use(express.static("public", {
  maxAge: "1d",
  etag: true,
  lastModified: true,
}));

// Cache-Control headers
res.setHeader("Cache-Control", "public, max-age=31536000, immutable");

// React Query caching
import { useQuery } from "@tanstack/react-query";

function Users() {
  const { data } = useQuery({
    queryKey: ["users"],
    queryFn: fetchUsers,
    staleTime: 5 * 60 * 1000, // 5 minutes
    cacheTime: 30 * 60 * 1000, // 30 minutes
  });
}
```

---

## Service Workers

Enable offline support and background features:

```typescript
// Register service worker
if ("serviceWorker" in navigator) {
  window.addEventListener("load", () => {
    navigator.serviceWorker.register("/sw.js").then((registration) => {
      console.log("SW registered:", registration);
    });
  });
}

// sw.js
const CACHE_NAME = "v1";
const ASSETS = ["/", "/styles.css", "/app.js", "/offline.html"];

// Install
self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS))
  );
});

// Activate
self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(
        keys.filter((key) => key !== CACHE_NAME).map((key) => caches.delete(key))
      )
    )
  );
});

// Fetch
self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then((cached) => {
      if (cached) {
        return cached;
      }
      return fetch(event.request).catch(() => {
        if (event.request.mode === "navigate") {
          return caches.match("/offline.html");
        }
      });
    })
  );
});

// Background sync
self.addEventListener("sync", (event) => {
  if (event.tag === "sync-posts") {
    event.waitUntil(syncPosts());
  }
});

// Push notifications
self.addEventListener("push", (event) => {
  const data = event.data.json();
  event.waitUntil(
    self.registration.showNotification(data.title, {
      body: data.body,
      icon: "/icon.png",
      badge: "/badge.png",
    })
  );
});
```

---

## CDN

Content Delivery Networks for faster global delivery:

```html
<!-- Use CDN for static assets -->
<script src="https://cdn.jsdelivr.net/npm/react@18/umd/react.production.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/css/bootstrap.min.css">

<!-- Self-hosted with CDN fallback -->
<script>
  window.React || document.write('<script src="/react.production.min.js"><\/script>');
</script>

<!-- CDN configuration headers -->
<!-- CloudFlare -->
Cache-Control: public, max-age=31536000, immutable
Access-Control-Allow-Origin: *

<!-- AWS CloudFront -->
Cache-Control: public, max-age=86400
Content-Encoding: gzip
```

### CDN Optimization Tips

```markdown
- Use a CDN for all static assets
- Enable Brotli/Gzip compression
- Set appropriate cache headers
- Use HTTP/2 or HTTP/3
- Implement edge caching
- Use CDN for API responses when possible
- Monitor CDN performance metrics
```
