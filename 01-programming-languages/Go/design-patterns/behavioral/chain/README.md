# Chain of Responsibility Pattern

## Overview

Chain of Responsibility passes requests along handlers. Go implements this as middleware chains.

## When to Use

- HTTP middleware (auth, logging, CORS)
- Request processing pipelines
- Validation chains

## Go Implementation

```go
type Handler interface {
    Handle(request string) (string, error)
    SetNext(handler Handler) Handler
}

type BaseHandler struct{ next Handler }

func (h *BaseHandler) SetNext(handler Handler) Handler {
    h.next = handler
    return handler
}

type AuthHandler struct{ BaseHandler }

func (a *AuthHandler) Handle(request string) (string, error) {
    if !authenticated(request) { return "", errors.New("unauthorized") }
    if a.next != nil { return a.next.Handle(request) }
    return request, nil
}
```

## Go-Idiomatic Alternative

```go
type HandlerFunc func(string) (string, error)

func Chain(handlers ...HandlerFunc) HandlerFunc {
    return func(req string) (string, error) {
        for _, handler := range handlers {
            resp, err := handler(req)
            if err != nil { return "", err }
            req = resp
        }
        return req, nil
    }
}
```

## Real-World Example

```go
func ChainMiddlewares(handler http.Handler, middlewares ...Middleware) http.Handler {
    for i := len(middlewares) - 1; i >= 0; i-- {
        handler = middlewares[i](handler)
    }
    return handler
}

func Logging(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        log.Printf("%s %s", r.Method, r.URL.Path)
        next.ServeHTTP(w, r)
    })
}
```

## Best Practices

- Keep handlers focused on single responsibility
- Provide a way to break the chain
- Use context for passing data

## Interview Questions

1. How does HTTP middleware relate to Chain?
2. How do you break out of a handler chain?
3. What happens if a handler forgets to call next?
4. How do you test handlers in isolation?
5. Can handlers modify the request?

## References

- "Design Patterns" - GoF Chapter 5
- Go Blog: "How to write middleware"
