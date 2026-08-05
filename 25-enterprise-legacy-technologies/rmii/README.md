# RMI and RMI-IIOP

## Overview

Remote Method Invocation (RMI) enables Java objects to invoke methods on objects running in different Java Virtual Machines. RMI-IIOP extends RMI to work with CORBA-compliant non-Java systems using the IIOP protocol.

## RMI Architecture

RMI uses a layered architecture with the Remote Reference Layer, Transport Layer, and Skeleton/Stub Layer. Each layer handles specific aspects of remote communication and object management.

## Remote Interfaces

RMI remote interfaces extend java.rmi.Remote and declare methods that can be invoked remotely. All method signatures must throw RemoteException to handle communication failures.

```java
public interface CustomerService extends Remote {
    Customer getCustomer(long id) throws RemoteException;
    void saveCustomer(Customer customer) throws RemoteException;
}
```

## RMI-IIOP Integration

RMI-IIOP allows Java RMI objects to communicate with CORBA systems. It compiles RMI interfaces into IIOP-compatible stubs using the rmic compiler with the -iiop flag.

## Naming and Registration

RMI uses the Java RMI Registry or JNDI for object lookup. Servers bind remote objects to names, and clients look up these objects to obtain references for invocation.

## Security Considerations

RMI communicates using Java serialization, which has known security vulnerabilities. Organizations should implement authentication, authorization, and network-level security for RMI communications.

## Modern Alternatives

RESTful web services, gRPC, and Spring Remoting have replaced RMI for most new development. These alternatives provide better interoperability, performance, and security characteristics.

## Migration Paths

Existing RMI applications can be modernized by implementing REST APIs, migrating to messaging systems, or wrapping RMI interfaces with web service endpoints for broader accessibility.
