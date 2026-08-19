# 02. ClassLoader - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Building plugin architectures | **Must** |
| Developing application servers or containers | **Must** |
| Debugging ClassNotFoundException / NoClassDefFoundError | **Must** |
| Working with OSGi or modular frameworks | **Must** |
| Implementing hot-reload or hot-deploy | **Should** |
| Diagnosing classloader memory leaks | **Should** |
| Understanding Spring Boot DevTools class loading | **Should** |
| Writing custom classloaders for encrypted classes | **Critical** |
| Simple CRUD applications | **Nice to have** |

## When This Knowledge is Essential

- **Plugin systems**: Loading extensions dynamically at runtime requires custom classloaders
- **Application servers**: Web containers like Tomcat isolate web applications using separate classloaders
- **Hot deployment**: Reloading classes without restarting the JVM demands understanding of class unloading
- **Memory leak debugging**: Many production leaks trace back to classloader references not being released
- **Framework internals**: Spring, Hibernate, and other frameworks use classloaders for proxy generation and instrumentation
- **Security**: The parent delegation model prevents untrusted code from replacing core Java classes

## When This Knowledge is Less Critical

- Writing standalone command-line tools
- Simple microservices with flat classpath
- Prototypes and proof-of-concept code
- One-off scripts or utilities

## Key Decision Points

| Decision | ClassLoader Knowledge Impact |
|----------|------------------------------|
| Choosing between classpath and module path | ClassLoader hierarchy determines visibility |
| Selecting a web container | Each container has its own classloader strategy |
| Debugging version conflicts | Different classloaders can load different versions |
| Implementing hot-reload | Must understand class unloading and redefinition |
| Diagnosing production leaks | ClassLoader leaks are a top cause of Metaspace OOM |
