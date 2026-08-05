# Architecture

Java Engineering Academy is organized as a Maven multi-module repository. The root project owns shared build
configuration, quality gates, and repository governance. Individual learning modules live under `modules/` and can
contain lessons, examples, exercises, and tests.

## Principles

- Keep modules independently understandable.
- Put shared build rules in the parent POM.
- Prefer runnable examples over static snippets.
- Treat tests as part of the learning material.
- Keep documentation close to the code it explains.

## Current Module Layout

```text
modules/
`-- java-fundamentals/
    |-- pom.xml
    |-- README.md
    `-- src/
        |-- main/java/
        `-- test/java/
```

Future modules should follow the same structure unless there is a clear reason to introduce a different layout.

