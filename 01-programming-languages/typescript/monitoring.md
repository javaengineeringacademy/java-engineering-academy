# TypeScript Monitoring

## Source Maps

Enable debugging in production by uploading source maps.

```json
// tsconfig.json
{
  "compilerOptions": {
    "sourceMap": true,
    "declarationMap": true,
    "inlineSourceMap": false
  }
}
```

### Upload to Error Tracking

```bash
# Sentry
npx @sentry/cli releases files dist/upload-sourcemaps

# Datadog
npx @datadog/datadog-ci sourcemaps upload ./dist
```

## Error Tracking

### Sentry Integration

```typescript
import * as Sentry from '@sentry/node';

Sentry.init({
  dsn: process.env.SENTRY_DSN,
  environment: process.env.NODE_ENV,
  tracesSampleRate: 1.0,
});

// Capture errors
try {
  await riskyOperation();
} catch (error) {
  Sentry.captureException(error);
  throw error;
}
```

### Global Error Handler

```typescript
// Express error middleware
app.use((err: Error, req: Request, res: Response, next: NextFunction) => {
  logger.error({
    message: err.message,
    stack: err.stack,
    path: req.path,
    method: req.method,
  });

  res.status(500).json({ error: 'Internal server error' });
});

// Unhandled rejections
process.on('unhandledRejection', (reason) => {
  logger.error('Unhandled rejection', { reason });
  Sentry.captureException(reason);
});

process.on('uncaughtException', (error) => {
  logger.error('Uncaught exception', { error });
  process.exit(1);
});
```

## Performance Monitoring

### Request Timing

```typescript
app.use((req, res, next) => {
  const start = Date.now();
  res.on('finish', () => {
    const duration = Date.now() - start;
    logger.info({
      method: req.method,
      path: req.path,
      status: res.statusCode,
      duration,
    });
  });
  next();
});
```

### Custom Metrics

```typescript
import { Counter, Histogram } from 'prom-client';

const httpRequestDuration = new Histogram({
  name: 'http_request_duration_seconds',
  help: 'Duration of HTTP requests',
  labelNames: ['method', 'route', 'status'],
});

const apiCalls = new Counter({
  name: 'api_calls_total',
  help: 'Total API calls',
  labelNames: ['endpoint', 'method'],
});

// Usage
httpRequestDuration.labels('GET', '/api/users', '200').observe(duration);
apiCalls.labels('/api/users', 'GET').inc();
```

## Structured Logging

```typescript
import pino from 'pino';

const logger = pino({
  level: process.env.LOG_LEVEL || 'info',
  formatters: {
    level: (label) => ({ level: label }),
  },
  timestamp: pino.stdTimeFunctions.isoTime,
});

// Usage
logger.info({ userId: 123, action: 'login' }, 'User logged in');
logger.error({ err: error, requestId }, 'Processing failed');
```

## Health Checks

```typescript
app.get('/health', (req, res) => {
  const health = {
    uptime: process.uptime(),
    status: 'OK',
    timestamp: Date.now(),
  };
  res.json(health);
});

app.get('/ready', async (req, res) => {
  try {
    await db.ping();
    res.json({ status: 'ready' });
  } catch {
    res.status(503).json({ status: 'not ready' });
  }
});
```

## APM Integration

```typescript
// OpenTelemetry
import { NodeTracerProvider } from '@opentelemetry/sdk-trace-node';
import { JaegerExporter } from '@opentelemetry/exporter-jaeger';

const provider = new NodeTracerProvider({
  serviceName: 'my-app',
});

const exporter = new JaegerExporter();
provider.addSpanProcessor(new BatchSpanProcessor(exporter));
provider.register();
```
