# References

## Official Documentation

### Synthetic Monitoring Tools

| Tool | Type | URL | Description |
|------|------|-----|-------------|
| Grafana k6 | Open Source | https://grafana.com/docs/k6/latest/ | Load testing and synthetic monitoring |
| Grafana Synthetic Monitoring | SaaS/On-Prem | https://grafana.com/docs/grafana-cloud/testing/synthetic-monitoring/ | Full synthetic monitoring platform |
| Playwright | Open Source | https://playwright.dev/docs/intro | Browser automation for synthetic checks |
| Selenium | Open Source | https://www.selenium.dev/documentation/ | Cross-browser testing framework |
| UptimeRobot | SaaS | https://uptimerobot.com/api/ | Uptime monitoring with API |
| Pingdom | SaaS | https://documentation.pingdom.com/ | Website monitoring service |
| Checkly | SaaS | https://checklyhq.com/docs/ | API and browser monitoring |
| ThousandEyes | Enterprise | https://docs.thousandeyes.com/ | Network and application monitoring |

### Observability Platforms

| Platform | URL | Relevance |
|----------|-----|-----------|
| Prometheus | https://prometheus.io/docs/ | Metrics collection and alerting |
| Grafana | https://grafana.com/docs/ | Visualization and dashboards |
| Jaeger | https://www.jaegertracing.io/docs/ | Distributed tracing |
| OpenTelemetry | https://opentelemetry.io/docs/ | Vendor-neutral observability framework |
| Datadog | https://docs.datadoghq.com/ | Full observability platform |
| New Relic | https://docs.newrelic.com/ | Application performance monitoring |

---

## Tutorials and Guides

### Getting Started

1. **Grafana k6 Getting Started**
   - https://grafana.com/docs/k6/latest/get-started/
   - Learn basics of load testing and synthetic monitoring

2. **Playwright for Monitoring**
   - https://playwright.dev/docs/api/class-page
   - Browser-based synthetic monitoring setup

3. **OpenTelemetry Synthetic Monitoring**
   - https://opentelemetry.io/docs/specs/semconv/http/http-metrics/
   - Standardized metrics for HTTP monitoring

### Advanced Topics

4. **SLA/SLO Monitoring**
   - https://sre.google/sre-book/practical-alerting/
   - Google SRE book chapter on SLI/SLO/SLA

5. **Chaos Engineering**
   - https://principlesofchaos.org/
   - Principles of Chaos Engineering

6. **Canary Deployments**
   - https://docs.flagger.app/
   - Progressive delivery with Flagger

---

## Tools and Libraries

### Python Libraries

```bash
# HTTP monitoring
pip install requests aiohttp httpx

# DNS monitoring
pip install dnspython

# SSL monitoring
pip install pyopenssl

# Metrics export
pip install prometheus-client

# Browser automation
pip install playwright
playwright install
```

### JavaScript Libraries

```bash
# Load testing
npm install k6 --save-dev

# Browser automation
npm install playwright puppeteer

# HTTP client
npm install axios got

# Metrics
npm install prom-client
```

### Go Libraries

```bash
# HTTP client
go get github.com/hashicorp/go-retryablehttp

# DNS
go get github.com/miekg/dns

# Metrics
go get github.com/prometheus/client_golang
```

---

## Architecture Patterns

### Synthetic Monitoring Architecture

```
┌─────────────────────────────────────────────────────┐
│              Synthetic Monitoring Architecture        │
├─────────────────────────────────────────────────────┤
│                                                       │
│  ┌─────────────┐    ┌─────────────┐                  │
│  │  Scheduler  │───▶│   Runner    │                  │
│  └─────────────┘    └──────┬──────┘                  │
│                            │                          │
│              ┌─────────────┼─────────────┐           │
│              │             │             │            │
│        ┌─────▼─────┐ ┌────▼────┐ ┌─────▼─────┐     │
│        │   HTTP    │ │ Browser │ │  Network  │     │
│        │  Checks   │ │ Checks  │ │  Checks   │     │
│        └─────┬─────┘ └────┬────┘ └─────┬─────┘     │
│              │             │             │            │
│              └─────────────┼─────────────┘           │
│                            │                          │
│                    ┌───────▼───────┐                  │
│                    │   Collector   │                  │
│                    └───────┬───────┘                  │
│                            │                          │
│              ┌─────────────┼─────────────┐           │
│              │             │             │            │
│        ┌─────▼─────┐ ┌────▼────┐ ┌─────▼─────┐     │
│        │Prometheus │ │  Logs   │ │  Alerts   │     │
│        └───────────┘ └─────────┘ └───────────┘     │
│                                                       │
└─────────────────────────────────────────────────────┘
```

### Real User Monitoring Architecture

```
┌─────────────────────────────────────────────────────┐
│             Real User Monitoring Architecture         │
├─────────────────────────────────────────────────────┤
│                                                       │
│  ┌─────────────────────────────────────────────┐     │
│  │                Browser/Client                │     │
│  │  ┌─────────────┐    ┌─────────────┐         │     │
│  │  │   RUM SDK   │    │ Performance │         │     │
│  │  │             │    │    API      │         │     │
│  │  └──────┬──────┘    └──────┬──────┘         │     │
│  │         └──────────────────┘                  │     │
│  └──────────────────────────┬───────────────────┘     │
│                              │                         │
│                    ┌─────────▼─────────┐              │
│                    │   Data Ingestion  │              │
│                    └─────────┬─────────┘              │
│                              │                         │
│              ┌───────────────┼───────────────┐        │
│              │               │               │         │
│        ┌─────▼─────┐ ┌──────▼──────┐ ┌─────▼─────┐  │
│        │ Session   │ │ Performance │ │   Error   │  │
│        │ Tracking  │ │  Metrics    │ │  Tracking │  │
│        └─────┬─────┘ └──────┬──────┘ └─────┬─────┘  │
│              │               │               │         │
│              └───────────────┼───────────────┘        │
│                              │                         │
│                    ┌─────────▼─────────┐              │
│                    │    Analytics      │              │
│                    └─────────┬─────────┘              │
│                              │                         │
│              ┌───────────────┼───────────────┐        │
│              │               │               │         │
│        ┌─────▼─────┐ ┌──────▼──────┐ ┌─────▼─────┐  │
│        │ Dashboards│ │  Alerts     │ │ Reports   │  │
│        └───────────┘ └─────────────┘ └───────────┘  │
│                                                       │
└─────────────────────────────────────────────────────┘
```

---

## Best Practices Resources

### Monitoring Best Practices

1. **Google SRE Book**
   - https://sre.google/sre-book/table-of-contents/
   - Comprehensive guide to site reliability engineering

2. **Observability Engineering**
   - https://www.oreilly.com/library/view/observability-engineering/9781492076438/
   - Advanced observability patterns

3. **Prometheus Best Practices**
   - https://prometheus.io/docs/practices/
   - Metrics collection and alerting best practices

### Alerting Best Practices

4. **Alerting on SLOs**
   - https://sre.google/workbook/alerting-on-slos/
   - How to alert based on service level objectives

5. **PagerDuty Alerting**
   - https://www.pagerduty.com/resources/learn/incident-command-handbook/
   - Incident response best practices

### Performance Monitoring

6. **Web Performance Working Group**
   - https://www.w3.org/webperf/
   - Web performance standards and specifications

7. **Core Web Vitals**
   - https://web.dev/vitals/
   - Google's Core Web Vitals metrics and guidelines

---

## Community Resources

### Blogs and Articles

- **Monitoring and Observability** by Charity Majors
  - https://charity.wtf/
  
- **The Practical Monitoring** by Mike Julian
  - https://www.oreilly.com/library/view/the-practical/9781491957349/

- **Site Reliability Engineering** by Google
  - https://sre.google/sre-book/table-of-contents/

### Open Source Projects

- **k6** - Modern load testing tool
  - https://github.com/grafana/k6

- **Playwright** - Browser automation
  - https://github.com/microsoft/playwright

- **Grafana** - Observability platform
  - https://github.com/grafana/grafana

- **Prometheus** - Monitoring system
  - https://github.com/prometheus/prometheus

### Training and Certifications

- **Grafana Certified Associate**
  - https://grafana.com/launchers/grafana-learning-path/

- **K6 Developer Certification**
  - https://grafana.com/launchers/k6-learning-path/

- **Prometheus Certified Associate**
  - https://training.linuxfoundation.org/certification/

---

## Conference Talks

1. **KubeCon: Synthetic Monitoring at Scale**
   - Recording available on YouTube
   
2. **GrafanaCON: Observability Best Practices**
   - https://grafana.com/events/grafanacon/
   
3. **SREcon: SLI/SLO Deep Dive**
   - https://srecon.org/

---

## Books

| Title | Author | Publisher | Year |
|-------|--------|-----------|------|
| Site Reliability Engineering | Google | O'Reilly | 2016 |
| Observability Engineering | Charity Majors et al. | O'Reilly | 2022 |
| The Practical Monitoring | Mike Julian | O'Reilly | 2017 |
| Monitoring Distributed Systems | Google | O'Reilly | 2017 |
| Prometheus: Up & Running | Brian Brazil | O'Reilly | 2018 |

---

## Standards and Specifications

- **OpenTelemetry Specification**
  - https://opentelemetry.io/docs/specs/
  
- **W3C Performance Timing**
  - https://www.w3.org/TR/navigation-timing/
  
- **W3C Resource Timing**
  - https://www.w3.org/TR/resource-timing/
  
- **Prometheus Remote Write**
  - https://prometheus.io/docs/operating/configuration/#remote_write-receiver
