# Integration Protocols - SOAP

## Overview

SOAP (Simple Object Access Protocol) is a XML-based protocol for exchanging structured information in web services.

## Table of Contents

1. [SOAP Basics](#soap-basics)
2. [SOAP Message Structure](#soap-message-structure)
3. [WSDL](#wsdl)
4. [SOAP Bindings](#soap-bindings)
5. [WS-* Standards](#ws-standards)

## SOAP Basics

### SOAP Envelope

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ord="http://example.com/orders">
    <soap:Header/>
    <soap:Body>
        <ord:GetOrderRequest>
            <ord:orderId>12345</ord:orderId>
        </ord:GetOrderRequest>
    </soap:Body>
</soap:Envelope>
```

### SOAP Response

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ord="http://example.com/orders">
    <soap:Body>
        <ord:GetOrderResponse>
            <ord:order>
                <ord:id>12345</ord:id>
                <ord:status>PROCESSED</ord:status>
                <ord:total>99.99</ord:total>
            </ord:order>
        </ord:GetOrderResponse>
    </soap:Body>
</soap:Envelope>
```

## SOAP Message Structure

### Structure

```
┌─────────────────────────────────────┐
│           SOAP Envelope             │
├─────────────────────────────────────┤
│  Header (optional)                  │
│  - Security                        │
│  - Transaction                     │
│  - Reliability                      │
├─────────────────────────────────────┤
│  Body                               │
│  - Request/Response                 │
│  - Fault (error)                    │
└─────────────────────────────────────┘
```

### SOAP Header

```xml
<soap:Header>
    <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/...">
        <wsse:UsernameToken>
            <wsse:Username>user</wsse:Username>
            <wsse:Password>password</wsse:Password>
        </wsse:UsernameToken>
    </wsse:Security>
</soap:Header>
```

### SOAP Fault

```xml
<soap:Fault>
    <soap:Faultcode>soap:Server</soap:Faultcode>
    <soap:Faultstring>Order not found</soap:Faultstring>
    <detail>
        <ord:OrderNotFound>
            <ord:orderId>12345</ord:orderId>
        </ord:OrderNotFound>
    </detail>
</soap:Fault>
```

## WSDL

### WSDL Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions name="OrderService"
             xmlns="http://schemas.xmlsoap.org/wsdl/"
             xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
             xmlns:tns="http://example.com/orders"
             xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    
    <types>
        <schema targetNamespace="http://example.com/orders">
            <element name="GetOrderRequest">
                <complexType>
                    <sequence>
                        <element name="orderId" type="xsd:string"/>
                    </sequence>
                </complexType>
            </element>
            <element name="GetOrderResponse">
                <complexType>
                    <sequence>
                        <element name="order" type="tns:Order"/>
                    </sequence>
                </complexType>
            </element>
        </schema>
    </types>
    
    <message name="GetOrderInput">
        <part name="parameters" element="tns:GetOrderRequest"/>
    </message>
    
    <message name="GetOrderOutput">
        <part name="parameters" element="tns:GetOrderResponse"/>
    </message>
    
    <portType name="OrderServicePortType">
        <operation name="GetOrder">
            <input message="tns:GetOrderInput"/>
            <output message="tns:GetOrderOutput"/>
        </operation>
    </portType>
    
    <binding name="OrderServiceBinding" type="tns:OrderServicePortType">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
        <operation name="GetOrder">
            <soap:operation soapAction="http://example.com/GetOrder"/>
            <input><soap:body use="literal"/></input>
            <output><soap:body use="literal"/></output>
        </operation>
    </binding>
    
    <service name="OrderService">
        <port name="OrderServicePort" binding="tns:OrderServiceBinding">
            <soap:address location="http://localhost:8080/services/orders"/>
        </port>
    </service>
</definitions>
```

## SOAP Bindings

### HTTP Binding

```xml
<binding name="OrderServiceBinding" type="tns:OrderServicePortType">
    <soap:binding style="document" 
                  transport="http://schemas.xmlsoap.org/soap/http"/>
</binding>
```

### HTTPS Binding

```xml
<binding name="OrderServiceBinding" type="tns:OrderServicePortType">
    <soap:binding style="document" 
                  transport="https://schemas.xmlsoap.org/soap/http"/>
</binding>
```

## WS-* Standards

### WS-Security

```xml
<soap:Header>
    <wsse:Security>
        <wsse:UsernameToken>
            <wsse:Username>user</wsse:Username>
            <wsse:Password Type="PasswordDigest">digest</wsse:Password>
            <wsse:Nonce>nonce</wsse:Nonce>
            <wsu:Created>2024-01-15T10:30:00Z</wsu:Created>
        </wsse:UsernameToken>
    </wsse:Security>
</soap:Header>
```

### WS-ReliableMessaging

```xml
<soap:Header>
    <wsrm:Sequence>
        <wsrm:Identifier>uuid</wsrm:Identifier>
        <wsrm:MessageNumber>1</wsrm:MessageNumber>
    </wsrm:Sequence>
</soap:Header>
```

## Best Practices

1. **Use document/literal**: Prefer document/literal style
2. **Version WSDL**: Version your WSDL contracts
3. **Security**: Use WS-Security for security
4. **Error handling**: Use SOAP Faults for errors
5. **Testing**: Test with SOAPUI
6. **Documentation**: Document SOAP operations
7. **Performance**: Consider MTOM for attachments
8. **Monitoring**: Track SOAP message flow

## References

- [SOAP Specification](https://www.w3.org/TR/soap12/)
- [WSDL Specification](https://www.w3.org/TR/wsdl/)
