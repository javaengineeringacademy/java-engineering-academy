# OWASP ZAP

## Overview
OWASP ZAP is a free, open-source web application security scanner that automates finding vulnerabilities in web applications.

## Scan Types
1. **Passive Scan**: Analyzes traffic without sending requests
2. **Active Scan**: Sends crafted requests to find vulnerabilities
3. **Spider**: Discovers application endpoints
4. **Fuzzer**: Tests inputs with malformed data

## Vulnerabilities
- SQL Injection (SQLi)
- Cross-Site Scripting (XSS)
- Cross-Site Request Forgery (CSRF)
- Path Traversal
- Remote Code Execution

## API Client
```java
@Component
public class ZapClient {
    private final String zapApiUrl;
    private final HttpClient client;

    public void startSpider(String targetUrl) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(zapApiUrl + "/JSON/spider/action/scan/?url=" + targetUrl))
            .POST(HttpRequest.BodyPublishers.noBody()).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void startActiveScan(String targetUrl) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(zapApiUrl + "/JSON/ascan/action/scan/?url=" + targetUrl))
            .POST(HttpRequest.BodyPublishers.noBody()).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<Alert> getAlerts() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(zapApiUrl + "/JSON/core/view/alerts/"))
            .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), AlertList.class).getAlerts();
    }
}
```

## CI/CD Integration
```yaml
security-scan:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v3
    - name: Run ZAP Scan
      run: docker run --network host ghcr.io/zaproxy/zaproxy:stable zap-full-scan.py -t http://localhost:8000 -r report.html
    - name: Upload Report
      uses: actions/upload-artifact@v3
      with:
        name: security-report
        path: report.html
```

## Best Practices
1. Run passive scan on every build
2. Run full active scan nightly
3. Focus on high-risk endpoints first
4. Use context files for authenticated scanning
5. Integrate with issue trackers for remediation
