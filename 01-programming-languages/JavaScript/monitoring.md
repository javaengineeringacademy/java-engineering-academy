# JavaScript Monitoring

## Sentry

```javascript
import * as Sentry from '@sentry/browser';

Sentry.init({
    dsn: 'https://your-dsn@sentry.io/project-id',
    integrations: [
        new Sentry.BrowserTracing(),
        new Sentry.Replay()
    ],
    tracesSampleRate: 1.0,
    replaysSessionSampleRate: 0.1,
    replaysOnErrorSampleRate: 1.0
});

// Capture exceptions
try {
    riskyOperation();
} catch (error) {
    Sentry.captureException(error);
}

// Add context
Sentry.setContext('user', {
    id: 123,
    email: 'user@example.com'
});

// Breadcrumbs
Sentry.addBreadcrumb({
    category: 'navigation',
    message: 'Page loaded',
    level: 'info'
});
```

## New Relic Browser

```html
<script src="https://js-agent.newrelic.com/nr-spa.js"></script>
<script>
    NREUM.info = {
        beacon: 'bam.nr-data.net',
        errorBeacon: 'bam.nr-data.net',
        licenseKey: 'your-license-key',
        applicationID: 'your-app-id',
        sa: 1
    };
</script>
```

## Lighthouse

```bash
# Install
npm install -g lighthouse

# Run audit
lighthouse https://example.com --output html --view

# CI integration
lighthouse https://example.com --output json --quiet --chrome-flags="--headless"
```

Key metrics:

- Performance score (0-100)
- First Contentful Paint
- Largest Contentful Paint
- Total Blocking Time
- Cumulative Layout Shift
- Speed Index

## Chrome DevTools Performance

- **Performance tab**: Record and analyze runtime performance
- **Memory tab**: Heap snapshots, allocation instrumentation
- **Network tab**: Request waterfall, timing analysis
- **Lighthouse tab**: Performance audits

## Custom Metrics

```javascript
// Track custom events
function trackEvent(name, data) {
    window.dataLayer = window.dataLayer || [];
    window.dataLayer.push({
        event: name,
        ...data
    });
}

// Track performance
function trackPerformance() {
    const [navigation] = performance.getEntriesByType('navigation');
    trackEvent('page_load', {
        domContentLoaded: navigation.domContentLoadedEventEnd,
        loadComplete: navigation.loadEventEnd
    });
}

// Track errors
window.addEventListener('error', (event) => {
    trackEvent('javascript_error', {
        message: event.message,
        filename: event.filename,
        lineno: event.lineno
    });
});
```

## Logging

```javascript
// Structured logging
function log(level, message, data) {
    const entry = {
        timestamp: new Date().toISOString(),
        level,
        message,
        data,
        url: window.location.href,
        userAgent: navigator.userAgent
    };
    console.log(JSON.stringify(entry));
}

// Error tracking
window.addEventListener('unhandledrejection', (event) => {
    log('error', 'Unhandled Promise Rejection', {
        reason: event.reason?.message || event.reason
    });
});
```
