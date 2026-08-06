# JavaScript Comparisons

Comparing JavaScript features and concepts.

## var vs let vs const

| Feature | var | let | const |
|---------|-----|-----|-------|
| Scope | Function | Block | Block |
| Hoisted | Yes | No (TDZ) | No (TDZ) |
| Redeclare | Yes | No | No |
| Reassign | Yes | Yes | No |

```javascript
// var: Function scoped
function example() {
    if (true) {
        var x = 1;
    }
    console.log(x); // 1
}

// let: Block scoped
function example2() {
    if (true) {
        let y = 1;
    }
    // console.log(y); // ReferenceError
}
```

## Callbacks vs Promises vs async/await

| Feature | Callbacks | Promises | async/await |
|---------|-----------|----------|-------------|
| Readability | Poor | Good | Best |
| Error Handling | Verbose | .catch() | try/catch |
| Debugging | Hard | Better | Best |
| Composability | Difficult | Good | Best |

```javascript
// Callbacks
getData(function(err, data) {
    if (err) handleError(err);
    processData(data, function(err, result) {
        if (err) handleError(err);
        saveResult(result);
    });
});

// Promises
getData()
    .then(data => processData(data))
    .then(result => saveResult(result))
    .catch(err => handleError(err));

// async/await
async function process() {
    try {
        const data = await getData();
        const result = await processData(data);
        await saveResult(result);
    } catch (err) {
        handleError(err);
    }
}
```

## map vs forEach vs for...of

| Feature | map | forEach | for...of |
|---------|-----|---------|----------|
| Returns | New array | undefined | Each value |
| Async support | No | No | Yes |
| Break/continue | No | No | Yes |

## Module Systems

| Feature | ES Modules | CommonJS |
|---------|------------|----------|
| Syntax | import/export | require/module.exports |
| Async | Yes | No |
| Browser | Native | Bundled |
| Node.js | Native (.mjs) | Default |

## Quick Reference

| Concept | Recommendation |
|---------|----------------|
| Variables | Use const by default, let when needed |
| Equality | Always use === |
| Async | Use async/await |
| Array iteration | Use map/filter/reduce |
| Modules | Use ES modules for new projects |
