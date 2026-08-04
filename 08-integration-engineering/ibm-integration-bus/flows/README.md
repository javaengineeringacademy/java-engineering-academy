# IBM Integration Bus / App Connect - Flows

## Overview

Message flows define the processing pipeline in IIB/ACE. They connect input nodes through processing steps to output nodes.

## Table of Contents

1. [Flow Components](#flow-components)
2. [Input Nodes](#input-nodes)
3. [Processing Nodes](#processing-nodes)
4. [Output Nodes](#output-nodes)
5. [Flow Patterns](#flow-patterns)

## Flow Components

### Flow Structure

```
┌─────────────────────────────────────────┐
│            Message Flow                 │
├─────────────────────────────────────────┤
│  Input ──> Transform ──> Route ──> Output│
│    │           │           │          │  │
│    ▼           ▼           ▼          ▼  │
│  [Node]     [Node]     [Node]     [Node]│
└─────────────────────────────────────────┘
```

## Input Nodes

### MQ Input

```
MQ Input Node
├── Queue Manager: QM1
├── Queue: INPUT.QUEUE
└── Input Message: XML
```

### HTTP Input

```
HTTP Input Node
├── URL: /api/orders
├── Method: POST
└── Content Type: application/json
```

### File Input

```
File Input Node
├── Directory: /input
├── File Pattern: *.xml
└── Poll Interval: 5 seconds
```

## Processing Nodes

### Compute Node

```
Compute Node
├── ESQL Code
├── Message Tree Manipulation
└── Custom Logic
```

### Filter Node

```
Filter Node
├── Condition: TRUE/FALSE
├── True Terminal ──> Next Node
└── False Terminal ──> Error Handler
```

### Database Node

```
Database Node
├── Data Source: DB2
├── SQL Statement
└── Input/Output Mapping
```

## Output Nodes

### MQ Output

```
MQ Output Node
├── Queue Manager: QM1
├── Queue: OUTPUT.QUEUE
└── Output Message: XML
```

### HTTP Reply

```
HTTP Reply Node
├── Status Code: 200
├── Content Type: application/json
└── Response Body
```

## Flow Patterns

### Request/Reply Pattern

```
HTTP Input ──> Compute ──> HTTP Reply
```

### One-Way Pattern

```
MQ Input ──> Compute ──> MQ Output
```

### Request/Reply with Database

```
HTTP Input ──> Compute ──> Database ──> HTTP Reply
```

## Best Practices

1. **Use subflows**: Promote reuse
2. **Error handling**: Configure error nodes
3. **Logging**: Add logging nodes
4. **Testing**: Test flows thoroughly
5. **Documentation**: Document flow logic
6. **Monitoring**: Track flow metrics
7. **Security**: Secure endpoints
8. **Performance**: Optimize processing

## References

- [IIB Message Flows](https://www.ibm.com/docs/en/iib)
- [ACE Message Flows](https://www.ibm.com/docs/en/app-connect/enterprise)
