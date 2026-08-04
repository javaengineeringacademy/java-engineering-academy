# Progressive Web Apps (PWA)

Progressive Web Apps combine the best of web and native apps, delivering fast, reliable, and engaging experiences to users across all devices.

## Table of Contents

- [Service Workers](#service-workers)
- [Caching Strategies](#caching-strategies)
- [Manifest.json](#manifestjson)
- [Add to Homescreen](#add-to-homescreen)
- [Push Notifications](#push-notifications)
- [Offline Support](#offline-support)
- [Workbox](#workbox)
- [Lighthouse](#lighthouse)
- [Testing PWAs](#testing-pwas)

---

## Service Workers

Background scripts that enable offline capabilities and more:

```javascript
// sw.js - Service Worker
const CACHE_NAME = "v1.0.0";
const urlsToCache = [
  "/",
  "/styles/main.css",
  "/scripts/app.js",
  "/offline.html",
];

// Install event
self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      console.log("Opened cache");
      return cache.addAll(urlsToCache);
    })
  );
});

// Activate event
self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames
          .filter((name) => name !== CACHE_NAME)
          .map((name) => caches.delete(name))
      );
    })
  );
});

// Fetch event
self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      if (response) {
        return response;
      }
      return fetch(event.request).then((response) => {
        if (!response || response.status !== 200 || response.type !== "basic") {
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
```

### Register Service Worker

```javascript
// main.js
if ("serviceWorker" in navigator) {
  window.addEventListener("load", () => {
    navigator.serviceWorker
      .register("/sw.js")
      .then((registration) => {
        console.log("SW registered:", registration);
      })
      .catch((error) => {
        console.log("SW registration failed:", error);
      });
  });
}

// Check if service worker is supported
if ("serviceWorker" in navigator) {
  // Register
  const registration = await navigator.serviceWorker.register("/sw.js");

  // Unregister
  await registration.unregister();

  // Update
  await registration.update();

  // Listen for updates
  registration.addEventListener("updatefound", () => {
    const newWorker = registration.installing;
    newWorker.addEventListener("statechange", () => {
      if (newWorker.state === "activated") {
        console.log("New service worker activated");
      }
    });
  });
}
```

---

## Caching Strategies

Different approaches for caching resources:

### Cache First

```javascript
// Cache First - Good for static assets
self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then((cached) => {
      if (cached) {
        return cached;
      }
      return fetch(event.request).then((response) => {
        if (response.ok) {
          const responseClone = response.clone();
          caches.open(CACHE_NAME).then((cache) => {
            cache.put(event.request, responseClone);
          });
        }
        return response;
      });
    })
  );
});
```

### Network First

```javascript
// Network First - Good for API calls
self.addEventListener("fetch", (event) => {
  event.respondWith(
    fetch(event.request)
      .then((response) => {
        if (response.ok) {
          const responseClone = response.clone();
          caches.open(CACHE_NAME).then((cache) => {
            cache.put(event.request, responseClone);
          });
        }
        return response;
      })
      .catch(() => {
        return caches.match(event.request);
      })
  );
});
```

### Stale While Revalidate

```javascript
// Stale While Revalidate - Good for frequently updated content
self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.match(event.request).then((cached) => {
        const networkFetch = fetch(event.request).then((response) => {
          if (response.ok) {
            cache.put(event.request, response.clone());
          }
          return response;
        });

        return cached || networkFetch;
      });
    })
  );
});
```

### Network Only

```javascript
// Network Only - Never cache
self.addEventListener("fetch", (event) => {
  event.respondWith(fetch(event.request));
});
```

### Cache Only

```javascript
// Cache Only - Never fetch from network
self.addEventListener("fetch", (event) => {
  event.respondWith(caches.match(event.request));
});
```

---

## Manifest.json

Web app manifest for installability:

```json
{
  "name": "My Progressive Web App",
  "short_name": "MyPWA",
  "description": "A progressive web app example",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#ffffff",
  "theme_color": "#000000",
  "orientation": "portrait-primary",
  "scope": "/",
  "icons": [
    {
      "src": "/icons/icon-72x72.png",
      "sizes": "72x72",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-96x96.png",
      "sizes": "96x96",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-128x128.png",
      "sizes": "128x128",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-144x144.png",
      "sizes": "144x144",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-152x152.png",
      "sizes": "152x152",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-192x192.png",
      "sizes": "192x192",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-384x384.png",
      "sizes": "384x384",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icons/icon-512x512.png",
      "sizes": "512x512",
      "type": "image/png",
      "purpose": "any maskable"
    }
  ],
  "screenshots": [
    {
      "src": "/screenshots/desktop.png",
      "sizes": "1280x720",
      "type": "image/png",
      "form_factor": "wide"
    },
    {
      "src": "/screenshots/mobile.png",
      "sizes": "750x1334",
      "type": "image/png",
      "form_factor": "narrow"
    }
  ],
  "categories": ["productivity", "utilities"],
  "lang": "en-US",
  "dir": "ltr",
  "prefer_related_applications": false,
  "related_applications": []
}
```

### Display Modes

```json
{
  "display": "standalone"
}
```

| Mode | Description |
|------|-------------|
| `fullscreen` | Full screen, no browser UI |
| `standalone` | Like native app, no browser UI |
| `minimal-ui` | Minimal browser UI |
| `browser` | Standard browser window |

---

## Add to Homescreen

Enable installability on mobile and desktop:

```javascript
// Listen for beforeinstallprompt event
let deferredPrompt;

window.addEventListener("beforeinstallprompt", (e) => {
  e.preventDefault();
  deferredPrompt = e;

  // Show install button
  showInstallButton();
});

function showInstallButton() {
  const installButton = document.getElementById("install-button");
  installButton.style.display = "block";

  installButton.addEventListener("click", () => {
    deferredPrompt.prompt();
    deferredPrompt.userChoice.then((choiceResult) => {
      if (choiceResult.outcome === "accepted") {
        console.log("User accepted the install prompt");
      }
      deferredPrompt = null;
    });
  });
}

// Listen for app installed event
window.addEventListener("appinstalled", () => {
  console.log("PWA was installed");
  deferredPrompt = null;
});

// Check if app is installed
if (window.matchMedia("(display-mode: standalone)").matches) {
  console.log("App is installed");
}

// Check if running in standalone mode
if (navigator.standalone === true) {
  console.log("Running in standalone mode");
}
```

---

## Push Notifications

Send notifications to users:

```javascript
// Request notification permission
async function requestNotificationPermission() {
  const permission = await Notification.requestPermission();

  if (permission === "granted") {
    console.log("Notification permission granted");
    return true;
  }

  return false;
}

// Subscribe to push notifications
async function subscribeToPushNotifications() {
  const registration = await navigator.serviceWorker.ready;

  const subscription = await registration.pushManager.subscribe({
    userVisibleOnly: true,
    applicationServerKey: urlBase64ToUint8Array(
      "YOUR_PUBLIC_VAPID_KEY"
    ),
  });

  console.log("Push subscription:", subscription);

  // Send subscription to server
  await fetch("/api/subscribe", {
    method: "POST",
    body: JSON.stringify(subscription),
    headers: {
      "Content-Type": "application/json",
    },
  });
}

// Convert VAPID key
function urlBase64ToUint8Array(base64String) {
  const padding = "=".repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding).replace(/-/g, "+").replace(/_/g, "/");
  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }

  return outputArray;
}

// Handle push event in service worker
self.addEventListener("push", (event) => {
  const data = event.data.json();

  const options = {
    body: data.body,
    icon: "/icons/icon-192x192.png",
    badge: "/icons/badge-72x72.png",
    vibrate: [100, 50, 100],
    data: {
      dateOfArrival: Date.now(),
      primaryKey: 1,
    },
    actions: [
      {
        action: "explore",
        title: "Explore",
        icon: "/icons/checkmark.png",
      },
      {
        action: "close",
        title: "Close",
        icon: "/icons/xmark.png",
      },
    ],
  };

  event.waitUntil(
    self.registration.showNotification(data.title, options)
  );
});

// Handle notification click
self.addEventListener("notificationclick", (event) => {
  event.notification.close();

  if (event.action === "explore") {
    event.waitUntil(clients.openWindow("/explore"));
  } else if (event.action === "close") {
    // Close notification
  } else {
    event.waitUntil(clients.openWindow("/"));
  }
});
```

---

## Offline Support

Ensure app works without network:

```javascript
// Offline fallback page
const OFFLINE_PAGE = "/offline.html";

self.addEventListener("fetch", (event) => {
  if (event.request.mode === "navigate") {
    event.respondWith(
      fetch(event.request).catch(() => {
        return caches.match(OFFLINE_PAGE);
      })
    );
  }
});

// Cache offline page on install
self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.add(OFFLINE_PAGE);
    })
  );
});
```

### Offline Page

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Offline</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      margin: 0;
      background: #f5f5f5;
    }
    .offline-container {
      text-align: center;
      padding: 2rem;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    h1 {
      color: #333;
      margin-bottom: 1rem;
    }
    p {
      color: #666;
      margin-bottom: 1.5rem;
    }
    button {
      background: #007bff;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background: #0056b3;
    }
  </style>
</head>
<body>
  <div class="offline-container">
    <h1>You're Offline</h1>
    <p>Please check your internet connection and try again.</p>
    <button onclick="window.location.reload()">Retry</button>
  </div>
</body>
</html>
```

---

## Workbox

Google's set of libraries for PWA development:

```javascript
// workbox-config.js
module.exports = {
  globDirectory: "dist",
  globPatterns: ["**/*.{html,js,css,png,jpg,gif,svg,woff,woff2}"],
  swDest: "dist/sw.js",
  runtimeCaching: [
    {
      urlPattern: /^https:\/\/api\.example\.com\/.*/i,
      handler: "NetworkFirst",
      options: {
        cacheName: "api-cache",
        expiration: {
          maxEntries: 100,
          maxAgeSeconds: 60 * 60 * 24, // 24 hours
        },
      },
    },
    {
      urlPattern: /\.(?:png|jpg|jpeg|svg|gif|webp)$/,
      handler: "CacheFirst",
      options: {
        cacheName: "images",
        expiration: {
          maxEntries: 100,
          maxAgeSeconds: 60 * 60 * 24 * 30, // 30 days
        },
      },
    },
  ],
};
```

### Workbox Strategies

```javascript
import { CacheFirst, NetworkFirst, StaleWhileRevalidate } from "workbox-recipes";
import { CacheableResponsePlugin } from "workbox-cacheable-response";
import { ExpirationPlugin } from "workbox-expiration";

// Cache First with expiration
const imageCache = new CacheFirst({
  cacheName: "images",
  plugins: [
    new CacheableResponsePlugin({
      statuses: [0, 200],
    }),
    new ExpirationPlugin({
      maxEntries: 100,
      maxAgeSeconds: 60 * 60 * 24 * 30,
    }),
  ],
});

// Network First with fallback
const apiCache = new NetworkFirst({
  cacheName: "api",
  plugins: [
    new CacheableResponsePlugin({
      statuses: [0, 200],
    }),
  ],
});

// Stale While Revalidate
const staticCache = new StaleWhileRevalidate({
  cacheName: "static",
  plugins: [
    new CacheableResponsePlugin({
      statuses: [0, 200],
    }),
  ],
});
```

---

## Lighthouse

Audit PWA quality and performance:

```bash
# Run Lighthouse audit
npx lighthouse https://example.com --view

# PWA-specific audit
npx lighthouse https://example.com --only-categories=pwa

# Output as JSON
npx lighthouse https://example.com --output=json --output-path=report.json
```

### PWA Checklist

```markdown
### Required
- [ ] Start URL responds with 200 when offline
- [ ] Registered service worker
- [ ] Service worker controls the app
- [ ] Web app manifest meets installability requirements
- [ ] Has a valid HTTPS connection
- [ ] Current page responds with 200 when offline

### Recommended
- [ ] Redirects HTTP to HTTPS
- [ ] Configured for a custom splash screen
- [ ] Sets theme color on the manifest
- [ ] Content sized to viewport
- [ ] Provides a valid apple-touch-icon
- [ ] Maskable icon meets minimum size requirements
- [ ] Has viewport meta tag
- [ ] Has theme-color meta tag

### Additional
- [ ] Page score > 90 in Performance
- [ ] Page score > 90 in Accessibility
- [ ] Page score > 90 in Best Practices
- [ ] Page score > 90 in SEO
```

---

## Testing PWAs

```javascript
// Service Worker testing
describe("Service Worker", () => {
  it("should register successfully", async () => {
    const registration = await navigator.serviceWorker.register("/sw.js");
    expect(registration).toBeDefined();
  });

  it("should cache static assets", async () => {
    const cache = await caches.open("v1");
    await cache.add("/");
    const response = await cache.match("/");
    expect(response).toBeDefined();
  });

  it("should work offline", async () => {
    // Simulate offline
    await navigator.serviceWorker.ready;
    // Test offline functionality
  });
});

// Offline detection
function isOnline() {
  return navigator.onLine;
}

function onOffline(callback) {
  window.addEventListener("offline", callback);
}

function onOnline(callback) {
  window.addEventListener("online", callback);
}

// Use in components
function NetworkStatus() {
  const [isOnline, setIsOnline] = useState(navigator.onLine);

  useEffect(() => {
    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);

    return () => {
      window.removeEventListener("online", handleOnline);
      window.removeEventListener("offline", handleOffline);
    };
  }, []);

  return (
    <div>
      {isOnline ? "Online" : "Offline"}
    </div>
  );
}
```
