# WSDL (Web Services Description Language)

## Overview

WSDL is an XML-based language for describing web service interfaces. It defines the operations a service provides, the message formats it accepts and returns, and the protocols it uses for communication.

## Document Structure

A WSDL document contains five main elements: types, messages, portType, binding, and service. Each element serves a specific purpose in describing the complete web service interface.

## Types Definition

The types element defines data structures using XML Schema (XSD). Complex types specify the structure of request and response messages exchanged with the web service.

```xml
<types>
  <schema>
    <complexType name="Customer">
      <sequence>
        <element name="id" type="xsd:long"/>
        <element name="name" type="xsd:string"/>
      </sequence>
    </complexType>
  </schema>
</types>
```

## Messages and Operations

Messages define the input and output parameters for service operations. PortTypes group related operations and define the message exchange pattern (one-way, request-response, solicit-response, notification).

## Bindings

Bindings associate portTypes with specific protocols and data formats. The most common binding is SOAP over HTTP, but WSDL supports JMS, SMTP, and other transport mechanisms.

## Service Definitions

The service element provides the network address (URL) for accessing the web service. Multiple endpoints can be defined for the same portType with different bindings or locations.

## SOAP Integration

WSDL is tightly coupled with SOAP for web service description. SOAP messages follow the message definitions in WSDL, with the binding specifying encoding rules and transport details.

## Modern Usage

REST APIs typically use OpenAPI (Swagger) instead of WSDL. However, WSDL remains essential for SOAP-based enterprise integration, particularly in financial and government systems.
