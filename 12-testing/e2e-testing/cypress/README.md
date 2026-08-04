# Cypress - End-to-End Testing

## Overview

Cypress is a modern E2E testing framework built for the web. It runs directly in the browser, providing fast, reliable, and easy-to-debug tests with time travel, automatic waiting, and network traffic control.

## Table of Contents

1. [Setup](#setup)
2. [Core Concepts](#core-concepts)
3. [Selectors](#selectors)
4. [Commands](#commands)
5. [Assertions](#assertions)
6. [Network Interception](#network-interception)
7. [Testing Patterns](#testing-patterns)
8. [CI/CD Integration](#cicd-integration)
9. [Plugins](#plugins)
10. [Best Practices](#best-practices)

---

## Setup

### Installation

```bash
# Install Cypress
npm install cypress --save-dev

# Open Cypress
npx cypress open

# Run headless
npx cypress run
```

### Configuration

```javascript
// cypress.config.js
const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:3000',
    viewportWidth: 1280,
    viewportHeight: 720,
    video: true,
    screenshotOnRunFailure: true,
    defaultCommandTimeout: 10000,
    requestTimeout: 10000,
    responseTimeout: 30000,
    pageLoadTimeout: 60000,
    retries: {
      runMode: 2,
      openMode: 0
    },
    env: {
      apiUrl: 'http://localhost:8080/api',
      user: 'test@example.com',
      password: 'testpass123'
    },
    specPattern: 'cypress/e2e/**/*.cy.{js,jsx,ts,tsx}',
    supportFile: 'cypress/support/e2e.js',
    setupNodeEvents(on, config) {
      on('task', {
        log(message) {
          console.log(message);
          return null;
        },
        table(table) {
          console.table(table);
          return null;
        }
      });
      return config;
    }
  }
});
```

### Project Structure

```
cypress/
  e2e/
    auth/
      login.cy.js
      register.cy.js
    dashboard/
      widgets.cy.js
      charts.cy.js
    api/
      users.cy.js
      orders.cy.js
  fixtures/
    users.json
    orders.json
  support/
    e2e.js
    commands.js
    utils.js
  plugins/
    index.js
cypress.config.js
```

---

## Core Concepts

### Test Structure

```javascript
describe('User Authentication', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login with valid credentials', () => {
    cy.get('[data-cy=username]').type('admin@example.com');
    cy.get('[data-cy=password]').type('password123');
    cy.get('[data-cy=submit]').click();

    cy.url().should('include', '/dashboard');
    cy.get('[data-cy=welcome]').should('contain', 'Welcome');
  });

  it('should show error with invalid credentials', () => {
    cy.get('[data-cy=username]').type('wrong@example.com');
    cy.get('[data-cy=password]').type('wrongpass');
    cy.get('[data-cy=submit]').click();

    cy.get('[data-cy=error]').should('be.visible');
    cy.get('[data-cy=error]').should('contain', 'Invalid credentials');
  });

  it('should validate required fields', () => {
    cy.get('[data-cy=submit]').click();
    cy.get('[data-cy=username-error]').should('be.visible');
    cy.get('[data-cy=password-error]').should('be.visible');
  });
});
```

### Custom Commands

```javascript
// cypress/support/commands.js
Cypress.Commands.add('login', (email, password) => {
  cy.session([email, password], () => {
    cy.visit('/login');
    cy.get('[data-cy=username]').type(email);
    cy.get('[data-cy=password]').type(password);
    cy.get('[data-cy=submit]').click();
    cy.url().should('include', '/dashboard');
  });
});

Cypress.Commands.add('createUser', (user) => {
  cy.request({
    method: 'POST',
    url: `${Cypress.env('apiUrl')}/users`,
    body: user,
    headers: {
      Authorization: `Bearer ${window.localStorage.getItem('token')}`
    }
  });
});

Cypress.Commands.add('getByDataCy', (selector) => {
  cy.get(`[data-cy="${selector}"]`);
});

Cypress.Commands.add('mockAPI', (route, response) => {
  cy.intercept('GET', route, {
    statusCode: 200,
    body: response
  }).as(route.split('/').pop());
});

// cypress/support/e2e.js
import './commands';
```

---

## Selectors

### Best Practices for Selectors

```javascript
// GOOD: Use data-cy attributes
cy.get('[data-cy=submit-button]').click();

// GOOD: Use data-testid
cy.get('[data-testid=login-form]').submit();

// AVOID: Using CSS classes (brittle)
cy.btn-primary').click(); // Bad - will break on style changes

// AVOID: Using text content (hard to maintain)
cy.contains('Submit').click(); // OK for small tests

// Selector strategies
cy.get('[data-cy=user-list]')           // data-cy attribute
cy.get('[data-testid=profile]')         // data-testid attribute
cy.get('#login-form')                   // ID selector
cy.get('form').first()                  // Element + index
cy.contains('Submit')                   // Text content
cy.find('button[type=submit]')          // CSS selector within chain
```

### Dynamic Content Selection

```javascript
// Selecting within a specific context
cy.get('[data-cy=user-card]')
  .contains('John Doe')
  .parent()
  .find('[data-cy=edit-button]')
  .click();

// Using closest() to navigate up
cy.get('[data-cy=delete-icon]')
  .closest('[data-cy=card]')
  .should('have.attr', 'data-user-id');

// Handling dynamic lists
cy.get('[data-cy=item-list]')
  .children()
  .should('have.length', 5);

// Filtering list items
cy.get('[data-cy=item-list] [data-cy=item]')
  .filter(':contains("Active")')
  .should('have.length', 3);
```

---

## Commands

### DOM Commands

```javascript
// Click variations
cy.get('[data-cy=button]').click();
cy.get('[data-cy=button]').dblclick();
cy.get('[data-cy=button]').rightclick();
cy.get('[data-cy=button]').click({ force: true });
cy.get('[data-cy=button]').click({ position: 'topLeft' });

// Type variations
cy.get('[data-cy=input]').type('Hello World');
cy.get('[data-cy=input]').type('{selectall}New Text');
cy.get('[data-cy=input]').type('text{enter}');
cy.get('[data-cy=input]').type('text{delay: 100}');

// Clear
cy.get('[data-cy=input]').clear();

// Check/Uncheck
cy.get('[data-cy=checkbox]').check();
cy.get('[data-cy=checkbox]').uncheck();
cy.get('[data-cy=radio]').check('option1');

// Select dropdown
cy.get('[data-cy=select]').select('option1');
cy.get('[data-cy=select]').select(1);

// File upload
cy.get('[data-cy=file-input]').selectFile('cypress/fixtures/test.pdf');
cy.get('[data-cy=dropzone]').selectFile('test.pdf', { action: 'drag-drop' });
```

### Visibility and State

```javascript
cy.get('[data-cy=element]').should('be.visible');
cy.get('[data-cy=element]').should('not.exist');
cy.get('[data-cy=element]').should('be.disabled');
cy.get('[data-cy=element]').should('be.enabled');
cy.get('[data-cy=element]').should('be.checked');
cy.get('[data-cy=element]').should('have.class', 'active');
cy.get('[data-cy=element]').should('have.attr', 'href', '/home');
cy.get('[data-cy=element]').should('have.css', 'color', 'rgb(0, 0, 0)');

// Waiting for elements
cy.get('[data-cy=loaded-content]').should('be.visible');
cy.get('[data-cy=spinner]').should('not.exist');
cy.get('[data-cy=element]').should('have.length.greaterThan', 0);
```

---

## Assertions

### Chai Assertions

```javascript
// Equality
expect(value).to.equal(42);
expect(value).to.deep.equal({ a: 1 });
expect(value).to.eql({ a: 1 }); // deep equality shorthand

// Truthiness
expect(value).to.be.true;
expect(value).to.be.ok;
expect(value).to.exist;

// Type
expect(value).to.be.a('string');
expect(value).to.be.an.instanceof(Array);

// Numeric
expect(value).to.be.greaterThan(5);
expect(value).to.be.within(1, 10);

// String
expect(value).to.contain('substring');
expect(value).to.match(/regex/);

// Array/Object
expect(value).to.have.length(3);
expect(value).to.have.property('name');
expect(value).to.have.keys('a', 'b');
expect(value).to.include({ a: 1 });

// Function
expect(fn).to.throw(Error);
expect(fn).to.be.calledWith('arg');
```

### Custom Assertions

```javascript
// cypress/support/assertions.js
Cypress.Commands.add('shouldBeAt', (path) => {
  cy.url().should('include', path);
});

Cypress.Commands.add('shouldHaveToast', (message) => {
  cy.get('[data-cy=toast]').should('contain', message);
});

Cypress.Commands.add('shouldHaveTableRows', (count) => {
  cy.get('table tbody tr').should('have.length', count);
});
```

---

## Network Interception

### API Mocking

```javascript
// Mock entire API response
cy.intercept('GET', '/api/users', {
  statusCode: 200,
  body: [
    { id: 1, name: 'John', email: 'john@test.com' },
    { id: 2, name: 'Jane', email: 'jane@test.com' }
  ]
}).as('getUsers');

// Mock with dynamic response
cy.intercept('POST', '/api/users', (req) => {
  req.reply({
    statusCode: 201,
    body: { id: 3, ...req.body }
  });
}).as('createUser');

// Delay response
cy.intercept('GET', '/api/slow', {
  statusCode: 200,
  body: { data: 'slow response' },
  delay: 2000
});

// Mock by route matching
cy.intercept('/api/**', { fixture: 'api-response.json' });
```

### Request Verification

```javascript
// Wait for API call
cy.visit('/users');
cy.wait('@getUsers').then((interception) => {
  expect(interception.request.headers).to.have.property('authorization');
  expect(interception.response.body).to.have.length(2);
});

// Verify request body
cy.get('[data-cy=name]').type('John');
cy.get('[data-cy=submit]').click();
cy.wait('@createUser').its('request.body').should('deep.equal', {
  name: 'John',
  email: 'john@example.com'
});

// Spy without mocking
cy.intercept('GET', '/api/analytics').as('analytics');
cy.visit('/dashboard');
cy.wait('@analytics');
```

### Network Errors

```javascript
// Simulate network error
cy.intercept('GET', '/api/users', {
  statusCode: 500,
  body: { error: 'Internal Server Error' }
});

// Simulate timeout
cy.intercept('GET', '/api/users', {
  forceNetworkError: true
});

// Simulate abort
cy.intercept('GET', '/api/users', (req) => {
  req.destroy();
});
```

---

## Testing Patterns

### Page Object Model

```javascript
// cypress/support/pages/LoginPage.js
class LoginPage {
  elements = {
    username: () => cy.get('[data-cy=username]'),
    password: () => cy.get('[data-cy=password]'),
    submitButton: () => cy.get('[data-cy=submit]'),
    error: () => cy.get('[data-cy=error]')
  };

  visit() {
    cy.visit('/login');
    return this;
  }

  login(username, password) {
    this.elements.username().type(username);
    this.elements.password().type(password);
    this.elements.submitButton().click();
    return this;
  }

  expectError(message) {
    this.elements.error().should('be.visible').and('contain', message);
    return this;
  }
}

// Usage in test
const loginPage = new LoginPage();

it('should login successfully', () => {
  loginPage.visit()
    .login('user@test.com', 'password123');
  cy.url().should('include', '/dashboard');
});
```

### Data-Driven Tests

```javascript
// cypress/fixtures/test-data.json
{
  "loginTests": [
    { "user": "admin@test.com", "pass": "admin123", "expected": "success" },
    { "user": "user@test.com", "pass": "user123", "expected": "success" },
    { "user": "wrong@test.com", "pass": "wrong", "expected": "error" }
  ]
}

// Data-driven test
describe('Login Data-Driven Tests', () => {
  beforeEach(() => {
    cy.fixture('test-data').as('data');
  });

  it('should test login scenarios', function() {
    this.data.loginTests.forEach((test) => {
      cy.visit('/login');
      cy.get('[data-cy=username]').type(test.user);
      cy.get('[data-cy=password]').type(test.pass);
      cy.get('[data-cy=submit]').click();

      if (test.expected === 'success') {
        cy.url().should('include', '/dashboard');
      } else {
        cy.get('[data-cy=error]').should('be.visible');
      }
    });
  });
});
```

### API Testing

```javascript
describe('API Tests', () => {
  it('should create and retrieve user', () => {
    // Create user
    cy.request({
      method: 'POST',
      url: '/api/users',
      body: {
        name: 'Test User',
        email: 'test@example.com'
      }
    }).then((response) => {
      expect(response.status).to.eq(201);
      expect(response.body).to.have.property('id');

      // Retrieve user
      cy.request(`/api/users/${response.body.id}`)
        .then((getResponse) => {
          expect(getResponse.status).to.eq(200);
          expect(getResponse.body.name).to.eq('Test User');
        });
    });
  });

  it('should handle pagination', () => {
    cy.request({
      method: 'GET',
      url: '/api/users',
      qs: { page: 1, limit: 10 }
    }).then((response) => {
      expect(response.body.data).to.have.length(10);
      expect(response.body.pagination).to.have.property('totalPages');
    });
  });
});
```

---

## CI/CD Integration

### GitHub Actions

```yaml
name: E2E Tests
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  cypress:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        browser: [chrome, firefox, edge]
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: 18
      - run: npm ci
      - uses: cypress-io/github-action@v5
        with:
          browser: ${{ matrix.browser }}
          build: npm run build
          start: npm start
          wait-on: 'http://localhost:3000'
      - uses: actions/upload-artifact@v3
        if: failure()
        with:
          name: cypress-screenshots-${{ matrix.browser }}
          path: cypress/screenshots
      - uses: actions/upload-artifact@v3
        if: failure()
        with:
          name: cypress-videos-${{ matrix.browser }}
          path: cypress/videos
```

### Parallelization

```bash
# Run tests in parallel
npx cypress run --record --parallel --key <record_key>

# Group tests
npx cypress run --record --group "E2E Tests" --ci-build-id $BUILD_ID
```

### Docker

```dockerfile
FROM cypress/included:12.0.0
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build
CMD ["npx", "cypress", "run", "--record"]
```

---

## Plugins

### Popular Plugins

```javascript
// cypress.config.js
const plugins = {
  'cypress-axe': 'Accessibility testing',
  'cypress-real-events': 'Native browser events',
  'cypress-file-upload': 'File upload testing',
  'cypress-localstorage': 'Local storage commands',
  'cypress-mochawesome-reporter': 'HTML reports',
  'cypress-grep': 'Test filtering',
  'cypress-split': 'Parallelization',
  'cypress-iframe': 'iframe testing'
};

// Example: cypress-axe
it('should have no accessibility violations', () => {
  cy.visit('/');
  cy.injectAxe();
  cy.checkA11y();
});

// Example: cypress-real-events
it('should handle hover state', () => {
  cy.get('[data-cy=menu-trigger]').realHover();
  cy.get('[data-cy=dropdown]').should('be.visible');
});

// Example: cypress-mochawesome-reporter
// cypress.config.js
module.exports = defineConfig({
  reporter: 'cypress-mochawesome-reporter',
  reporterOptions: {
    charts: true,
    reportPageTitle: 'Test Report',
    embeddedScreenshots: true,
    inlineAssets: true
  }
});
```

---

## Best Practices

### Do's and Don'ts

```
DO:
  Use data-cy attributes for selectors
  Use cy.intercept() for API mocking
  Use cy.session() for login caching
  Write isolated, independent tests
  Use custom commands for repeated logic
  Use fixtures for test data
  Test error states and edge cases

DON'T:
  Use CSS class selectors
  Hard-code waits (cy.wait(5000))
  Test backend logic in E2E
  Share state between tests
  Use cy.get() without specificity
  Ignore test failures
  Over-test simple pages
```

### Performance Tips

```javascript
// Use cy.session() to cache login
beforeEach(() => {
  cy.login('user@test.com', 'password'); // Only logs in once per spec
});

// Minimize API calls with fixtures
cy.intercept('GET', '/api/users', { fixture: 'users.json' });

// Run tests in parallel
// Use --parallel flag with Cypress Dashboard

// Use test isolation
it('test 1', () => { /* ... */ });
it('test 2', () => { /* ... */ }); // Resets between tests
```

### Debugging

```javascript
// Debug in test
cy.get('[data-cy=element]').then(($el) => {
  console.log('Element:', $el);
  debugger; // Pauses here in DevTools
});

// Cypress logs
cy.log('Step completed');
cy.task('log', 'Custom log message');

// Screenshot on failure
// cypress.config.js
screenshotOnRunFailure: true

// Video recording
video: true
```

---

## Related Topics

- [Playwright](../playwright/)
- [Selenium](../selenium/)
- [Cucumber](../cucumber/)
- [E2E Testing Overview](../)
