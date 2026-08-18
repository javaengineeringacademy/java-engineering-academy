# CompletableFuture - Decision Guide

## thenApply vs thenCompose

| Criteria | thenApply | thenCompose |
|----------|-----------|-------------|
| Returns | CompletableFuture<T> | CompletableFuture<U> |
| Function type | Function<T, U> | Function<T, CompletableFuture<U>> |
| Analogy | Map | FlatMap |
| Use when | Synchronous transform | Async chain |

## thenCombine vs thenAcceptBoth vs runAfterBoth

| Method | Input | Returns | Use When |
|--------|-------|---------|----------|
| thenCombine | Two futures | Result of both | Combine two independent results |
| thenAcceptBoth | Two futures | void | Consume both results |
| runAfterBoth | Two futures | void | Run after both complete |

## Error Handling Strategy

| Method | Behavior | Returns |
|--------|----------|---------|
| exceptionally | Handle error, return fallback | CompletableFuture<T> |
| handle | Transform result OR handle error | CompletableFuture<U> |
| whenComplete | Side effect on result/error | CompletableFuture<T> |
| compose (in exceptionally) | Chain recovery | CompletableFuture<T> |

## When to Use Each Composition Pattern

| Pattern | Method Chain |
|---------|-------------|
| Transform result | `thenApply(fn)` |
| Chain async calls | `thenCompose(fn)` |
| Combine independent | `thenCombine(other, fn)` |
| Wait for all | `allOf(futures)` |
| Wait for first | `anyOf(futures)` |
| Fallback on error | `exceptionally(fn)` |
| Final action | `whenComplete((r,e) -> ...)` |

## Common Anti-Patterns

| Anti-Pattern | Correct Approach |
|-------------|------------------|
| Calling get() on CompletableFuture | Use thenApply/thenCompose |
| Blocking in async chain | Use thenCompose for async |
| Ignoring exceptions | Use exceptionally or handle |
| Mixing blocking and async | Keep chains fully non-blocking |
