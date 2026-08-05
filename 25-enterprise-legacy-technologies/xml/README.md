# XML (Extensible Markup Language)

## Overview

XML (Extensible Markup Language) is a markup language defining rules for encoding documents in a format that is both human-readable and machine-readable. It provides a flexible way to structure, store, and transport data with self-describing tags and strict syntax rules enforced by schemas.

## History

XML 1.0 became a W3C recommendation in February 1998. XML 1.1 arrived in 2004 with improved character encoding support. XML Schema (XSD) reached recommendation status in 2001, providing strong typing for XML documents. Namespaces in XML (1999) enabled combining XML vocabularies. JSON emerged in 2001 as a simpler alternative, gradually displacing XML for data interchange.

## Why It Is Considered Legacy

XML documents are verbose due to opening and closing tags for every element. Parsing and validation add overhead compared to simpler formats. Schema definitions (XSD) are complex and verbose. The tree-based data model does not map naturally to modern object-oriented code. Namespaces create complexity in document processing and tooling support.

## Key Concepts

- **Elements and Tags**: Enclosed content within `<tag>content</tag>` forming the hierarchical document structure
- **Attributes**: Name-value pairs within start tags providing metadata or simple data values
- **DTD (Document Type Definition)**: Legacy schema language defining document structure and entity references
- **XML Schema (XSD)**: W3C schema language with data types, constraints, and validation rules for XML documents
- **Namespaces**: URI-qualified element names preventing conflicts when combining multiple XML vocabularies
- **Well-formedness vs Validity**: Strict syntax rules (well-formedness) and optional schema conformance (validity)

## When It Was Used

XML was the dominant data interchange format from 1998 to 2010. SOAP web services used XML exclusively for message exchange. Enterprise configuration files (Maven, Ant, Spring) adopted XML extensively. RSS and Atom feeds used XML for syndication. Office documents (Microsoft Office Open XML, ODF) are ZIP archives containing XML content. E-commerce standards (EDI, B2B) migrated to XML-based formats.

## Why It Was Replaced

JSON provides a lighter-weight format with native JavaScript support and less verbose syntax. YAML offers human-readable configuration without angle brackets. REST APIs prefer JSON for request and response bodies. Protocol Buffers and Avro provide efficient binary serialization. Modern web frameworks default to JSON serialization and deserialization.

## Migration Path

Replace XML configuration files with YAML or properties files where possible. Convert XML-based APIs to JSON using serialization libraries (Jackson, Gson). Migrate SOAP services to RESTful endpoints returning JSON. Update build tools from Ant (XML) to Maven or Gradle (XML or DSL). Replace XML data feeds with JSON equivalents for client consumption.

## Modern Alternative

JSON is the dominant data interchange format with native browser support and simpler syntax. YAML provides human-readable configuration for DevOps and deployment. Protocol Buffers and Avro offer efficient binary serialization for high-performance systems. TOML serves as a configuration format for modern tools. GraphQL provides flexible query-based data exchange.

## Interview Questions

1. What are the key differences between XML Schema (XSD) and DTD for document validation?
2. How do XML namespaces work, and what problem do they solve in enterprise integration?
3. What advantages does JSON offer over XML for REST API data exchange?
4. When might XML still be preferred over JSON despite its verbosity?
5. Describe the XML parsing models (DOM, SAX, StAX) and their trade-offs.

## References

- W3C: Extensible Markup Language (XML) Specification
- W3C: XML Schema Part 0-2
- Oracle: Java API for XML Processing (JAXP)
- Baeldung: XML Processing with Java
