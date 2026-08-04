# k6 - Performance Testing

## Overview

k6 is a modern load testing tool built for developer productivity. It uses JavaScript for test scripts and provides excellent performance with low resource usage.

## Setup

### Installation

```bash
# macOS
brew install k6

# Linux
sudo apt-get install k6

# Windows
choco install k6
```

### Maven (for Java integration)

```xml
<dependency>
    <groupId>io.gatling</groupId>
    <artifactId>gatling-recorder</artifactId>
    <version>3.9.5</version>
</dependency>
```

## Basic Script

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
};

export default function () {
  const res = http.get('http://localhost:8080/api/users');

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(1);
}
```

## HTTP Requests

### GET

```javascript
import http from 'k6/http';

export default function () {
  const res = http.get('http://localhost:8080/api/users');
  
  console.log(`Status: ${res.status}`);
  console.log(`Body: ${res.body}`);
}
```

### POST

```javascript
export default function () {
  const payload = JSON.stringify({
    name: 'John',
    email: 'john@example.com',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post('http://localhost:8080/api/users', payload, params);
  
  check(res, {
    'status is 201': (r) => r.status === 201,
  });
}
```

### PUT

```javascript
export default function () {
  const payload = JSON.stringify({
    name: 'John Updated',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.put('http://localhost:8080/api/users/1', payload, params);
  
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
}
```

### DELETE

```javascript
export default function () {
  const res = http.del('http://localhost:8080/api/users/1');
  
  check(res, {
    'status is 204': (r) => r.status === 204,
  });
}
```

## Checks

### Status Checks

```javascript
check(res, {
  'status is 200': (r) => r.status === 200,
  'status is 2xx': (r) => r.status >= 200 && r.status < 300,
  'status is not 500': (r) => r.status !== 500,
});
```

### Response Time Checks

```javascript
check(res, {
  'response time < 500ms': (r) => r.timings.duration < 500,
  'response time < 1s': (r) => r.timings.duration < 1000,
});
```

### Body Checks

```javascript
check(res, {
  'body contains "success"': (r) => r.body.includes('success'),
  'body is not empty': (r) => r.body.length > 0,
  'json has id': (r) => JSON.parse(r.body).id !== undefined,
});
```

### Custom Checks

```javascript
const isValidUser = (r) => {
  const body = JSON.parse(r.body);
  return body.name && body.email && body.id;
};

check(res, {
  'is valid user': isValidUser,
});
```

## Options

### Basic Options

```javascript
export const options = {
  vus: 10,                    // Virtual users
  duration: '30s',           // Test duration
  iterations: 100,           // Total iterations
};
```

### Thresholds

```javascript
export const options = {
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95% of requests < 500ms
    http_req_failed: ['rate<0.01'],    // < 1% failure rate
    http_reqs: ['rate>100'],           // > 100 requests/sec
  },
};
```

### Stages

```javascript
export const options = {
  stages: [
    { duration: '1m', target: 10 },   // Ramp up
    { duration: '5m', target: 10 },   // Stay at 10 VUs
    { duration: '1m', target: 20 },   // Ramp to 20 VUs
    { duration: '5m', target: 20 },   // Stay at 20 VUs
    { duration: '1m', target: 0 },    // Ramp down
  ],
};
```

### Scenarios

```javascript
export const options = {
  scenarios: {
    browse: {
      executor: 'constant-vus',
      vus: 10,
      duration: '5m',
    },
    buy: {
      executor: 'ramping-arrival-rate',
      startRate: 10,
      timeUnit: '1s',
      preAllocatedVUs: 100,
      stages: [
        { duration: '2m', target: 20 },
        { duration: '5m', target: 20 },
      ],
    },
  },
};
```

## Data

### JSON Data

```javascript
import { SharedArray } from 'k6/data';

const users = new SharedArray('users', function () {
  return JSON.parse(open('./users.json'));
});

export default function () {
  const user = users[Math.floor(Math.random() * users.length)];
  http.get(`http://localhost:8080/api/users/${user.id}`);
}
```

### CSV Data

```javascript
import { SharedArray } from 'k6/data';

const users = new SharedArray('users', function () {
  return open('./users.csv')
    .split('\n')
    .map((line) => {
      const [id, name, email] = line.split(',');
      return { id, name, email };
    });
});

export default function () {
  const user = users[Math.floor(Math.random() * users.length)];
  http.get(`http://localhost:8080/api/users/${user.id}`);
}
```

### Environment Variables

```javascript
import http from 'k6/http';

export default function () {
  const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';
  const token = __ENV.API_TOKEN;

  const params = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };

  http.get(`${baseUrl}/api/users`, params);
}
```

## Groups

```javascript
import http from 'k6/http';
import { group, check } from 'k6';

export default function () {
  group('User API', function () {
    group('Get Users', function () {
      const res = http.get('http://localhost:8080/api/users');
      check(res, { 'status is 200': (r) => r.status === 200 });
    });

    group('Create User', function () {
      const res = http.post(
        'http://localhost:8080/api/users',
        JSON.stringify({ name: 'John', email: 'john@example.com' }),
        { headers: { 'Content-Type': 'application/json' } }
      );
      check(res, { 'status is 201': (r) => r.status === 201 });
    });
  });
}
```

## Custom Metrics

```javascript
import { Counter, Gauge, Rate, Trend } from 'k6/metrics';

const myCounter = new Counter('my_counter');
const myGauge = new Gauge('my_gauge');
const myRate = new Rate('my_rate');
const myTrend = new Trend('my_trend');

export default function () {
  const res = http.get('http://localhost:8080/api/users');
  
  myCounter.add(1);
  myGauge.add(res.timings.duration);
  myRate.add(res.status === 200);
  myTrend.add(res.timings.duration);
}
```

## Thresholds

```javascript
export const options = {
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    http_req_failed: ['rate<0.01'],
    http_reqs: ['count>1000'],
    'http_req_duration{status:200}': ['p(95)<300'],
    'http_req_duration{status:500}': ['count<5'],
  },
};
```

## Tags

```javascript
export default function () {
  const res = http.get('http://localhost:8080/api/users', {
    tags: {
      name: 'Get Users',
      group: 'User API',
    },
  });
}
```

## Lifecycle Hooks

```javascript
export function setup() {
  // Before test starts
  const token = login();
  return { token };
}

export default function (data) {
  // During test
  const params = {
    headers: {
      Authorization: `Bearer ${data.token}`,
    },
  };
  http.get('http://localhost:8080/api/users', params);
}

export function teardown(data) {
  // After test ends
  console.log('Test complete');
}
```

## Best Practices

### Use Appropriate Load Patterns

```javascript
// Good: Ramp up gradually
export const options = {
  stages: [
    { duration: '2m', target: 100 },
    { duration: '5m', target: 100 },
    { duration: '2m', target: 0 },
  ],
};

// Bad: Spike load
export const options = {
  vus: 100,
  duration: '5m',
};
```

### Set Meaningful Thresholds

```javascript
export const options = {
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95% of requests < 500ms
    http_req_failed: ['rate<0.01'],    // < 1% failure rate
  },
};
```

### Use Checks Effectively

```javascript
check(res, {
  'status is 200': (r) => r.status === 200,
  'response time < 500ms': (r) => r.timings.duration < 500,
  'body has expected structure': (r) => {
    const body = JSON.parse(r.body);
    return body.id && body.name && body.email;
  },
});
```

## Resources

- [k6 Documentation](https://grafana.com/docs/k6/)
- [k6 GitHub](https://github.com/grafana/k6)
- [k6 Examples](https://github.com/grafana/k6/tree/master/examples)
