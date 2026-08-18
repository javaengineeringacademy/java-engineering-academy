# Browser Monitoring

## Overview

Browser monitoring uses real browser instances to render pages, execute JavaScript, and validate the complete user experience. It captures Core Web Vitals, detects JavaScript errors, and validates visual elements.

---

## Core Web Vitals

### Metrics Captured

| Metric | Description | Target | Measurement |
|--------|-------------|--------|-------------|
| **LCP** | Largest Contentful Paint | < 2.5s | Time to render largest visible element |
| **FID** | First Input Delay | < 100ms | Time from first interaction to response |
| **CLS** | Cumulative Layout Shift | < 0.1 | Visual stability metric |
| **TTFB** | Time to First Byte | < 800ms | Server response time |
| **FCP** | First Contentful Paint | < 1.8s | Time to first content |

---

## Playwright Browser Monitor

### Basic Implementation

```javascript
const { chromium } = require('playwright');

class BrowserSyntheticMonitor {
  constructor(config = {}) {
    this.config = {
      headless: true,
      viewport: { width: 1920, height: 1080 },
      timeout: 30000,
      ...config
    };
    this.browser = null;
  }

  async init() {
    this.browser = await chromium.launch({ 
      headless: this.config.headless 
    });
  }

  async monitorPage(pageConfig) {
    const context = await this.browser.newContext({
      viewport: this.config.viewport
    });
    const page = await context.newPage();
    
    const metrics = {
      url: pageConfig.url,
      timestamps: {},
      errors: [],
      resources: [],
      webVitals: {}
    };

    // Capture console errors
    page.on('pageerror', error => {
      metrics.errors.push({
        type: 'pageerror',
        message: error.message,
        timestamp: Date.now()
      });
    });

    // Capture failed requests
    page.on('requestfailed', request => {
      metrics.errors.push({
        type: 'requestfailed',
        url: request.url(),
        error: request.failure()?.errorText,
        timestamp: Date.now()
      });
    });

    // Capture resource timing
    page.on('response', response => {
      metrics.resources.push({
        url: response.url(),
        status: response.status(),
        timing: response.request().timing()
      });
    });

    try {
      // Navigate and measure load time
      const navStart = Date.now();
      await page.goto(pageConfig.url, { 
        waitUntil: pageConfig.waitUntil || 'networkidle',
        timeout: this.config.timeout 
      });
      metrics.timestamps.load = Date.now() - navStart;

      // Wait for specific element if specified
      if (pageConfig.waitForSelector) {
        const selStart = Date.now();
        await page.waitForSelector(pageConfig.waitForSelector, { 
          timeout: 10000 
        });
        metrics.timestamps.elementVisible = Date.now() - selStart;
      }

      // Validate content
      if (pageConfig.expectedText) {
        const content = await page.textContent('body');
        metrics.contentValid = content.includes(pageConfig.expectedText);
      }

      // Validate title
      if (pageConfig.expectedTitle) {
        const title = await page.title();
        metrics.titleValid = title.includes(pageConfig.expectedTitle);
      }

      // Collect Core Web Vitals
      metrics.webVitals = await this.collectWebVitals(page);

      // Take screenshot
      if (pageConfig.screenshot) {
        await page.screenshot({ 
          path: `/tmp/synthetic-${Date.now()}.png`,
          fullPage: pageConfig.fullPage || false 
        });
      }

      metrics.success = metrics.errors.length === 0 && 
                        (metrics.contentValid !== false);
                        
    } catch (error) {
      metrics.success = false;
      metrics.error = error.message;
    } finally {
      await context.close();
    }

    return metrics;
  }

  async collectWebVitals(page) {
    return await page.evaluate(() => {
      return new Promise(resolve => {
        const vitals = {
          LCP: 0,
          FID: 0,
          CLS: 0,
          FCP: 0,
          TTFB: 0
        };

        // LCP Observer
        new PerformanceObserver(list => {
          const entries = list.getEntries();
          vitals.LCP = entries[entries.length - 1].startTime;
        }).observe({ type: 'largest-contentful-paint', buffered: true });

        // CLS Observer
        new PerformanceObserver(list => {
          let cls = 0;
          for (const entry of list.getEntries()) {
            if (!entry.hadRecentInput) cls += entry.value;
          }
          vitals.CLS = cls;
        }).observe({ type: 'layout-shift', buffered: true });

        // FCP Observer
        new PerformanceObserver(list => {
          const entries = list.getEntries();
          vitals.FCP = entries[entries.length - 1].startTime;
        }).observe({ type: 'paint', buffered: true });

        // TTFB from Navigation Timing
        const navEntry = performance.getEntriesByType('navigation')[0];
        if (navEntry) {
          vitals.TTFB = navEntry.responseStart;
        }

        setTimeout(() => resolve(vitals), 3000);
      });
    });
  }

  async close() {
    if (this.browser) await this.browser.close();
  }
}
```

### Multi-Page Journey Monitor

```javascript
class UserJourneyMonitor extends BrowserSyntheticMonitor {
  async monitorJourney(journey) {
    const context = await this.browser.newContext();
    const page = await context.newPage();
    const results = {
      name: journey.name,
      steps: [],
      totalDuration: 0,
      success: true
    };

    try {
      for (const step of journey.steps) {
        const stepStart = Date.now();
        
        await this.executeStep(page, step);
        
        const stepDuration = Date.now() - stepStart;
        results.steps.push({
          name: step.name,
          duration: stepDuration,
          success: true
        });

        // Take screenshot at each step if configured
        if (step.screenshot) {
          await page.screenshot({ 
            path: `/tmp/journey-${journey.name}-step-${step.name}.png` 
          });
        }
      }
    } catch (error) {
      results.success = false;
      results.error = error.message;
    } finally {
      await context.close();
    }

    results.totalDuration = results.steps.reduce(
      (sum, step) => sum + step.duration, 0
    );
    
    return results;
  }

  async executeStep(page, step) {
    switch (step.action) {
      case 'navigate':
        await page.goto(step.url, { waitUntil: 'networkidle' });
        break;
        
      case 'click':
        await page.click(step.selector);
        break;
        
      case 'fill':
        await page.fill(step.selector, step.value);
        break;
        
      case 'wait':
        await page.waitForSelector(step.selector, { timeout: 10000 });
        break;
        
      case 'screenshot':
        await page.screenshot({ path: step.path });
        break;
        
      case 'evaluate':
        await page.evaluate(step.script);
        break;
        
      default:
        throw new Error(`Unknown action: ${step.action}`);
    }
  }
}
```

---

## Performance Monitoring

### Resource Analysis

```javascript
class ResourceAnalyzer {
  static async analyze(page) {
    const resources = await page.evaluate(() => {
      return performance.getEntriesByType('resource').map(entry => ({
        name: entry.name,
        type: entry.initiatorType,
        duration: entry.duration,
        size: entry.transferSize,
        protocol: entry.nextHopProtocol
      }));
    });

    // Group by type
    const byType = resources.reduce((acc, res) => {
      acc[res.type] = acc[res.type] || [];
      acc[res.type].push(res);
      return acc;
    }, {});

    // Calculate totals
    const totals = {
      totalResources: resources.length,
      totalSize: resources.reduce((sum, r) => sum + r.size, 0),
      totalDuration: resources.reduce((sum, r) => sum + r.duration, 0),
      byProtocol: resources.reduce((acc, r) => {
        acc[r.protocol] = (acc[r.protocol] || 0) + 1;
        return acc;
      }, {})
    };

    return { resources, byType, totals };
  }
}
```

### JavaScript Error Tracking

```javascript
class JSErrorTracker {
  constructor() {
    this.errors = [];
    this.warnings = [];
  }

  attachToPage(page) {
    // Capture uncaught errors
    page.on('pageerror', error => {
      this.errors.push({
        type: 'uncaught',
        message: error.message,
        stack: error.stack,
        timestamp: Date.now()
      });
    });

    // Capture console errors
    page.on('console', msg => {
      if (msg.type() === 'error') {
        this.errors.push({
          type: 'console.error',
          text: msg.text(),
          timestamp: Date.now()
        });
      } else if (msg.type() === 'warning') {
        this.warnings.push({
          text: msg.text(),
          timestamp: Date.now()
        });
      }
    });

    // Capture failed requests
    page.on('requestfailed', request => {
      this.errors.push({
        type: 'network',
        url: request.url(),
        error: request.failure()?.errorText,
        timestamp: Date.now()
      });
    });
  }

  getSummary() {
    return {
      errorCount: this.errors.length,
      warningCount: this.warnings.length,
      errors: this.errors,
      warnings: this.warnings
    };
  }
}
```

---

## Visual Regression Testing

### Screenshot Comparison

```javascript
const { compare } = require('resemblejs');

class VisualRegressionMonitor {
  static async compareScreenshots(baselinePath, currentPath) {
    return new Promise((resolve, reject) => {
      compare(
        baselinePath,
        currentPath,
        {
          output: {
            errorColor: {
              red: 255,
              green: 0,
              blue: 255
            },
            errorType: 'movement',
            transparency: 0.3
          },
          scaleToSameSize: true
        },
        (err, data) => {
          if (err) reject(err);
          resolve({
            match: data.rawMisMatchPercentage < 0.1,
            mismatch: data.rawMisMatchPercentage,
            diffImage: data.getBuffer()
          });
        }
      );
    });
  }

  static async monitorVisualRegression(page, baselinePath, testName) {
    const currentPath = `/tmp/visual-${testName}-${Date.now()}.png`;
    
    await page.screenshot({ path: currentPath, fullPage: true });
    
    const result = await this.compareScreenshots(baselinePath, currentPath);
    
    return {
      testName,
      baseline: baselinePath,
      current: currentPath,
      match: result.match,
      mismatchPercentage: result.mismatch
    };
  }
}
```

---

## Alert Configuration

### Browser Monitoring Alerts

```yaml
groups:
  - name: browser_monitoring
    rules:
      # Page load time
      - alert: BrowserPageLoadSlow
        expr: browser_page_load_duration_seconds > 5
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Slow page load detected"
          description: "{{ $labels.url }} took {{ $value }}s to load"
      
      # JavaScript errors
      - alert: BrowserJSErrors
        expr: rate(browser_js_errors_total[5m]) > 0.1
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "High JavaScript error rate"
          description: "Error rate: {{ $value }}/second"
      
      # Core Web Vitals
      - alert: PoorLCP
        expr: browser_lcp_seconds > 2.5
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Poor Largest Contentful Paint"
          description: "LCP is {{ $value }}s"
      
      - alert: HighCLS
        expr: browser_cls > 0.1
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High Cumulative Layout Shift"
          description: "CLS is {{ $value }}"
```

---

## Best Practices

1. **Use Real Browsers:** Simulators don't capture real-world browser behavior
2. **Test Multiple Browsers:** Chrome, Firefox, Safari may behave differently
3. **Monitor Core Web Vitals:** These metrics directly impact user experience
4. **Capture Screenshots:** Visual evidence helps debug issues
5. **Track JavaScript Errors:** Uncaught errors break user workflows
6. **Analyze Resource Loading:** Identify slow or blocking resources
7. **Test on Real Devices:** Mobile and desktop have different constraints
8. **Set Realistic Timeouts:** Balance between detection speed and false positives

---

## Next Steps

- [Load Testing](../04-load-testing/README.md) - Load testing as monitoring
- [Chaos Engineering](../05-chaos-engineering/README.md) - Chaos engineering integration
- [Dashboard Integration](../07-dashboard-integration/README.md) - Creating dashboards
