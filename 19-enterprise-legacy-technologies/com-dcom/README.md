# COM/DCOM

## Overview

Component Object Model (COM) is Microsoft's platform-independent, object-oriented programming model for creating interoperable software components. Distributed COM (DCOM) extends COM for network communication between components.

## COM Fundamentals

COM defines a binary standard for component interaction regardless of programming language. Components expose functionality through interfaces with vtables, enabling runtime method resolution and memory management.

## Interface Definition

COM interfaces are defined using Interface Definition Language (IDL). Each interface has a unique identifier (IID) and specifies method signatures that implementing classes must provide.

```idl
[object, uuid(12345678-1234-1234-1234-123456789ABC)]
interface ICustomerService : IUnknown {
    HRESULT GetCustomer([in] long id, [out, retval] BSTR* name);
    HRESULT SaveCustomer([in] long id, [in] BSTR name);
};
```

## DCOM Network Communication

DCOM enables COM components to communicate across network boundaries. It handles marshaling of method calls, object references, and return values between processes and machines.

## Component Services

COM+ (Component Services) provides transaction management, object pooling, queued components, and security enforcement. It simplifies deployment and management of COM components in enterprise environments.

## Registration and Discovery

COM components must be registered in the Windows registry. The registry stores class identifiers, interface mappings, and in-process server locations for component discovery.

## Modern Relevance

While COM remains part of Windows architecture, new development typically uses .NET assemblies or REST APIs. Legacy COM components can be wrapped using COM interop for continued use in modern applications.

## Migration Strategies

Modernizing COM-based applications involves identifying component dependencies, evaluating .NET replacements, and implementing incremental migration using COM interop wrappers during transition periods.
