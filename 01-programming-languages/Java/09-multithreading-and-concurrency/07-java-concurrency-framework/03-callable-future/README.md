# Callable and Future

## Overview

`Callable` is like `Runnable` but returns a value and can throw checked exceptions. `Future` represents the result of an asynchronous computation.

## Key Methods

| Future Method | Description |
|---------------|-------------|
| get() | Block until result available |
| get(timeout, unit) | Block with timeout |
| isDone() | Check if completed |
| cancel(boolean) | Attempt to cancel |
| isCancelled() | Check if cancelled |
