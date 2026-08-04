# XML External Entity (XXE)

## Overview

XXE attacks exploit XML parsers that process external entity references, allowing attackers to read files, perform SSRF, or cause DoS.

## Vulnerable Code

```java
// NEVER DO THIS - Default parser configuration
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(inputStream);
```

## Attack Example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE foo [
  <!ENTITY xxe SYSTEM "file:///etc/passwd">
]>
<user>
  <name>&xxe;</name>
</user>
```

## Prevention

### Secure Parser Configuration
```java
@Component
public class SecureXmlParser {
    
    public Document parse(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // Disable DTDs
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        
        // Disable external entities
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        
        // Disable external DTDs
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        
        // Use a no-op entity resolver
        factory.setEntityResolver((publicId, systemId) -> {
            throw new SAXException("External entities not allowed");
        });
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputStream);
    }
}
```

### JAXB Configuration
```java
// Disable external entities in JAXB
JAXBContext context = JAXBContext.newInstance(MyClass.class);
Unmarshaller unmarshaller = context.createUnmarshaller();

// Set secure properties
unmarshaller.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
unmarshaller.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
```

### Jackson XML
```java
// Configure Jackson XML
XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
```

## XInclude Prevention

```java
// Disable XInclude
factory.setXIncludeAware(false);
factory.setExpandEntityReferences(false);
```

## Best Practices

1. Disable DTDs entirely
2. Disable external entities
3. Use secure parser configurations
4. Validate XML input
5. Use JSON instead of XML when possible
6. Implement input validation
7. Update XML libraries regularly
8. Use XML firewalls
