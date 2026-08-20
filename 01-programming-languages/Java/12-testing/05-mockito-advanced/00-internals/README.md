# Advanced Mockito - Internals

## Spy Implementation

Spies are created using subclass generation:

1. ByteBuddy generates a subclass of the real object
2. All methods are intercepted by the proxy
3. Interceptor checks if method is stubbed
4. If stubbed: use mock behavior
5. If not stubbed: delegate to real implementation
6. State is shared between spy and real object

## BDD Stubbing Flow

```
given(mock.method(args))     →  when(mock.method(args))
    ↓                              ↓
Configures stubbing           Returns stubbed value
    ↓                              ↓
willwillReturn(value)         thenReturn(value)
    ↓                              ↓
then(mock).should()           verify(mock).method(args)
```

## Custom Answer Execution

```
when(mock.method(args))
    ↓
thenAnswer(invocation → {
    // Access invocation metadata
    Object arg0 = invocation.getArgument(0);
    Method method = invocation.getMethod();
    // Compute and return result
    return computedValue;
})
```

## Answer Interface

```java
public interface Answer<T> {
    T answer(InvocationOnMock invocation) throws Throwable;
}
```

The answer method receives the invocation, allowing access to:
- Method being called
- Arguments passed
- Mock object
- Real method (for spies)
