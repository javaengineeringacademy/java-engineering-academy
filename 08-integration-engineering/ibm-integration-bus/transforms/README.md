# IBM Integration Bus / App Connect - Transforms

## Overview

Message transformation in IIB/ACE converts messages between different formats using ESQL, XSLT, and graphical mapping.

## Table of Contents

1. [Transformation Types](#transformation-types)
2. [ESQL Transformations](#esql-transformations)
3. [XSLT Transformations](#xslt-transformations)
4. [Graphical Mapping](#graphical-mapping)
5. [Data Format Conversion](#data-format-conversion)

## Transformation Types

### Transformation Approaches

| Approach | Description |
|----------|-------------|
| ESQL | Procedural transformation |
| XSLT | Declarative transformation |
| Mapping | Graphical transformation |
| Compute | Custom transformation |

## ESQL Transformations

### ESQL in Compute Node

```sql
-- Basic transformation
DECLARE outref REFERENCE TO OutputRoot;
SET outref.XMLNSC = InputRoot.XMLNSC;

-- Field mapping
SET outref.XMLNSC.order.id = InputRoot.XMLNSC.orderId;
SET outref.XMLNSC.order.status = 'PROCESSED';
SET outref.XMLNSC.order.timestamp = CURRENT_TIMESTAMP;

-- Loop through array
DECLARE i INTEGER;
SET i = 1;
WHILE i <= CARDINALITY(InputRoot.XMLNSC.order.items[]) DO
    SET outref.XMLNSC.order.items[i].processed = TRUE;
    SET i = i + 1;
END WHILE;
```

### ESQL Functions

```sql
-- Custom function
CREATE FUNCTION transformOrder(OLDORDER ABCOrderType)
RETURNS ABCOrderReturnType
BEGIN
    DECLARE newOrder ABCOrderReturnType;
    SET newOrder.id = OLDORDER.id;
    SET newOrder.status = 'TRANSFORMED';
    RETURN newOrder;
END;
```

## XSLT Transformations

### XSLT Stylesheet

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <order>
            <id><xsl:value-of select="order/orderId"/></id>
            <status>PROCESSED</status>
            <timestamp><xsl:value-of select="current-dateTime()"/></timestamp>
        </order>
    </xsl:template>
</xsl:stylesheet>
```

### XSLT in Message Flow

```
Input ──> XSLT Node ──> Output
```

## Graphical Mapping

### Mapping Editor

- Visual transformation design
- Drag-and-drop mapping
- Built-in functions
- Testing capabilities

### Mapping Functions

| Function | Description |
|----------|-------------|
| String | String manipulation |
| Number | Numeric operations |
| Date | Date functions |
| Aggregate | Collection operations |

## Data Format Conversion

### XML to JSON

```
XML Input ──> Compute ──> JSON Output
```

### JSON to XML

```
JSON Input ──> Compute ──> XML Output
```

### CSV to XML

```
CSV Input ──> Compute ──> XML Output
```

## Best Practices

1. **Use appropriate transformation**: Match approach to complexity
2. **Keep transformations simple**: Single responsibility
3. **Test transformations**: Verify output
4. **Document mappings**: Document transformation logic
5. **Handle errors**: Configure error handling
6. **Performance**: Consider transformation performance
7. **Reusability**: Create reusable transformations
8. **Monitoring**: Track transformation metrics

## References

- [IIB ESQL](https://www.ibm.com/docs/en/iib)
- [IIB XSLT](https://www.ibm.com/docs/en/iib)
- [IIB Mapping](https://www.ibm.com/docs/en/iib)
