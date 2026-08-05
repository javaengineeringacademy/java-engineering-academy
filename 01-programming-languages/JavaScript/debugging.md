# JavaScript Debugging

## Chrome DevTools

```javascript
// Open DevTools
// Windows/Linux: Ctrl+Shift+I or F12
// Mac: Cmd+Option+I

// Console tab: Interactive debugging
// Sources tab: Set breakpoints, step through code
// Network tab: Monitor HTTP requests
// Performance tab: Record and analyze performance
// Memory tab: Heap snapshots, allocation tracking
```

## Console Methods

```javascript
console.log('Basic logging');
console.info('Informational message');
console.warn('Warning message');
console.error('Error message');

// Structured logging
console.table([{ name: 'Alice', age: 30 }, { name: 'Bob', age: 25 }]);

// Grouping
console.group('Section');
console.log('Item 1');
console.log('Item 2');
console.groupEnd();

// Timing
console.time('timer');
heavyOperation();
console.timeEnd('timer');

// Counting
console.count('label');

// Formatting
console.log('%c Styled text', 'color: red; font-size: 20px');
```

## Debugger Statement

```javascript
function problematicFunction() {
    debugger; // Execution pauses here when DevTools is open
    const result = calculateSomething();
    return result;
}
```

## Breakpoints

```javascript
// Source breakpoints
// Click line number in Sources tab

// Conditional breakpoints
// Right-click line number > Add conditional breakpoint

// DOM breakpoints
// Right-click element > Break on > subtree modifications

// XHR breakpoints
// Sources tab > XHR Breakpoints > Add URL pattern

// Event listener breakpoints
// Sources tab > Event Listener Breakpoints
```

## Network Debugging

```javascript
// Monitor requests
// Network tab > Filter by type

// Modify request headers
// Right-click request > Edit and Resend

// Simulate network conditions
// Network tab > Throttling dropdown

// Capture response data
// Network tab > Click request > Preview/Response
```

## Memory Debugging

```javascript
// Take heap snapshot
// Memory tab > Take heap snapshot

// Record allocation timeline
// Memory tab > Allocation instrumentation on timeline

// Compare snapshots
// Take snapshot > Take another > Compare

// Find memory leaks
// Look for Detached elements
// Check for growing arrays/objects
```

## Error Handling

```javascript
// Global error handler
window.addEventListener('error', (event) => {
    console.error('Uncaught error:', event.error);
});

// Unhandled promise rejection
window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled rejection:', event.reason);
});

// Try-catch for async code
async function safeAsync() {
    try {
        await riskyOperation();
    } catch (error) {
        console.error('Async error:', error);
    }
}
```

## Remote Debugging

```bash
# Node.js debugging
node --inspect app.js
node --inspect-brk app.js

# Connect Chrome DevTools to Node.js
# Open chrome://inspect in Chrome
```

## Performance Profiling

```javascript
// Record performance
performance.mark('start');
heavyOperation();
performance.mark('end');
performance.measure('operation', 'start', 'end');

// User Timing API
const measures = performance.getEntriesByType('measure');
console.log(measures);
```
