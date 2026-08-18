# CompletableFuture

## Overview

`CompletableFuture` enables async composition with fluent APIs. Chain operations, handle errors, and combine results without blocking.

## Key Methods

| Method | Description |
|--------|-------------|
| supplyAsync(supplier) | Async supply |
| thenApply(fn) | Transform result |
| thenCompose(fn) | Flat-map (async chain) |
| thenCombine(other, fn) | Combine two futures |
| allOf(futures) | Wait for all |
| anyOf(futures) | Wait for first |
| exceptionally(fn) | Handle errors |
| handle(fn) | Transform or handle error |
