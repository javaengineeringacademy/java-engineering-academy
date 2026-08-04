# Metadata Management

## Table of Contents

- [Overview](#overview)
- [Types of Metadata](#types-of-metadata)
- [Metadata Architecture](#metadata-architecture)
- [Metadata Catalog](#metadata-catalog)
- [Metadata Lineage](#metadata-lineage)
- [Metadata Standards](#metadata-standards)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Metadata management is the practice of managing metadata to provide context,
meaning, and structure to data assets. It enables data discovery, governance,
and quality management.

### Key Characteristics

- **Context**: Provides meaning to data
- **Discovery**: Enables finding relevant data
- **Governance**: Supports compliance and security
- **Quality**: Helps ensure data accuracy
- **Integration**: Connects data across systems

### When to Use Metadata Management

- Data discovery and exploration
- Data governance and compliance
- Data quality management
- Data integration and migration
- Impact analysis

### Metadata vs Data

| Feature | Metadata | Data |
|---------|----------|------|
| Purpose | Describes data | Actual information |
| Example | Column names, types | Customer records |
| Storage | Catalogs, repositories | Databases, files |
| Usage | Discovery, governance | Analytics, reporting |

---

## Types of Metadata

### Technical Metadata

```python
# Technical Metadata
technical_metadata = {
    "schema": {
        "tables": [
            {
                "name": "users",
                "columns": [
                    {"name": "id", "type": "INTEGER", "nullable": False},
                    {"name": "name", "type": "VARCHAR(100)", "nullable": False},
                    {"name": "email", "type": "VARCHAR(255)", "nullable": True},
                    {"name": "created_at", "type": "TIMESTAMP", "nullable": False}
                ],
                "primary_key": ["id"],
                "indexes": [
                    {"name": "idx_users_email", "columns": ["email"]}
                ]
            }
        ]
    },
    "lineage": {
        "source": "postgresql://host:5432/db",
        "target": "s3://data-lake/users/",
        "transformations": ["filter", "aggregate", "join"]
    },
    "quality": {
        "null_percentage": {"id": 0, "name": 0.1, "email": 5.2},
        "unique_percentage": {"id": 100, "email": 99.8}
    }
}
```

### Business Metadata

```python
# Business Metadata
business_metadata = {
    "description": "Customer information table",
    "owner": "Marketing Team",
    "steward": "John Smith",
    "classification": "Internal",
    "retention": "7 years",
    "glossary": {
        "customer": "A person or organization that purchases products or services",
        "email": "Electronic mail address used for communication"
    },
    "tags": ["customers", "PII", "marketing"],
    "sla": {
        "freshness": "Daily",
        "availability": "99.9%",
        "support": "Business hours"
    }
}
```

### Operational Metadata

```python
# Operational Metadata
operational_metadata = {
    "execution": {
        "job_name": "daily_etl",
        "start_time": "2024-01-01T00:00:00Z",
        "end_time": "2024-01-01T01:30:00Z",
        "duration": "90 minutes",
        "status": "success",
        "records_processed": 1000000
    },
    "resource_usage": {
        "cpu_hours": 10,
        "memory_gb": 50,
        "storage_gb": 100,
        "cost": "$5.00"
    },
    "errors": {
        "count": 0,
        "details": []
    }
}
```

---

## Metadata Architecture

### Architecture Components

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Metadata Architecture                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Metadata Sources                                                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │Databases │ │  Files   │ │   APIs   │ │  Tools   │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                           │                                          │
│  Metadata Collection      │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │ Crawlers │ │Scanners  │ │  Agents  │ │  APIs    │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Metadata Storage         │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  Graph   │ │ Document │ │ Key-Value│ │  Search  │       │  │
│  │  │   DB     │ │    DB    │ │    DB    │ │  Index   │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Metadata Services        │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  Catalog │ │  Lineage │ │  Search  │ │Governance│       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Metadata Consumers       │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │   BI     │ │   Data   │ │   Data   │ │   Data   │       │  │
│  │  │  Tools   │ │Scientists│ │Engineers │ │ Stewards │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Metadata Flow

```python
# Metadata Flow
metadata_flow = {
    "collection": {
        "sources": [
            "Database schemas",
            "File systems",
            "APIs",
            "ETL jobs",
            "Data quality tools"
        ],
        "methods": [
            "Automated crawling",
            "Manual entry",
            "API integration",
            "Log parsing"
        ]
    },
    "processing": {
        "steps": [
            "Validation",
            "Enrichment",
            "Normalization",
            "Indexing"
        ]
    },
    "storage": {
        "options": [
            "Graph database (Neo4j)",
            "Document database (MongoDB)",
            "Search index (Elasticsearch)",
            "Key-value store (Redis)"
        ]
    },
    "distribution": {
        "methods": [
            "REST APIs",
            "GraphQL",
            "SDKs",
            "Webhooks"
        ]
    }
}
```

---

## Metadata Catalog

### Catalog Features

```python
# Metadata Catalog Features
catalog_features = {
    "discovery": {
        "search": "Full-text search across metadata",
        "browse": "Navigate data assets hierarchically",
        "filter": "Filter by tags, owners, types",
        "recommendations": "Suggested datasets based on usage"
    },
    "documentation": {
        "descriptions": "Business and technical descriptions",
        "glossary": "Business term definitions",
        "schemas": "Column-level documentation",
        "lineage": "Data flow visualization"
    },
    "collaboration": {
        "reviews": "Data asset reviews",
        "ratings": "User ratings and feedback",
        "comments": "Discussion and Q&A",
        "ownership": "Clear ownership assignment"
    },
    "governance": {
        "classification": "Data sensitivity classification",
        "policies": "Data policies and standards",
        "compliance": "Regulatory compliance tracking",
        "audit": "Access and change audit logs"
    }
}
```

### Catalog Schema

```python
# Catalog Schema
catalog_schema = {
    "datasets": {
        "id": "UUID",
        "name": "String",
        "description": "Text",
        "owner": "String",
        "steward": "String",
        "tags": "Array<String>",
        "classification": "String",
        "schema": "JSON",
        "lineage": "JSON",
        "quality": "JSON",
        "created_at": "Timestamp",
        "updated_at": "Timestamp"
    },
    "columns": {
        "id": "UUID",
        "dataset_id": "UUID",
        "name": "String",
        "type": "String",
        "description": "Text",
        "nullable": "Boolean",
        "primary_key": "Boolean",
        "tags": "Array<String>"
    },
    "lineage": {
        "id": "UUID",
        "source_dataset_id": "UUID",
        "target_dataset_id": "UUID",
        "transformations": "JSON",
        "job_name": "String",
        "execution_time": "Timestamp"
    }
}
```

### Catalog Implementation

```python
# DataHub Metadata Catalog
from datahub.emitter.mce_builder import make_dataset_urn
from datahub.emitter.rest_emitter import DatahubRestEmitter
from datahub.metadata.com.linkedin.pegasus2avro.metadata import MetadataChangeProposal

# Initialize emitter
emitter = DatahubRestEmitter(gms_url="http://localhost:8080")

# Create dataset metadata
dataset_urn = make_dataset_urn(platform="spark", name="users")

# Emit metadata
mcp = MetadataChangeProposal(
    entityUrn=dataset_urn,
    aspectName="datasetProperties",
    aspect=DatasetProperties(
        description="Customer information table",
        customProperties={
            "owner": "Marketing Team",
            "retention": "7 years"
        }
    )
)

emitter.emit(mcp)
```

---

## Metadata Lineage

### Lineage Tracking

```python
# Data Lineage
lineage_tracking = {
    "technical_lineage": {
        "description": "Data flow between systems",
        "components": [
            "Source systems",
            "ETL processes",
            "Target systems"
        ],
        "granularity": "Column-level"
    },
    "business_lineage": {
        "description": "Business context of data flow",
        "components": [
            "Business processes",
            "Data products",
            "Reports"
        ],
        "granularity": "Dataset-level"
    },
    "operational_lineage": {
        "description": "Execution details of data processes",
        "components": [
            "Job executions",
            "Data volumes",
            "Quality metrics"
        ],
        "granularity": "Execution-level"
    }
}
```

### Lineage Visualization

```python
# Lineage Graph
lineage_graph = {
    "nodes": [
        {"id": "source_db", "type": "database", "name": "PostgreSQL"},
        {"id": "staging", "type": "table", "name": "staging_users"},
        {"id": "curated", "type": "table", "name": "curated_users"},
        {"id": "mart", "type": "table", "name": "dim_customers"}
    ],
    "edges": [
        {"source": "source_db", "target": "staging", "type": "extract"},
        {"source": "staging", "target": "curated", "type": "transform"},
        {"source": "curated", "target": "mart", "type": "aggregate"}
    ]
}

# Lineage Query
def get_upstream_datasets(dataset_id):
    """Get all upstream datasets"""
    query = """
    MATCH (target {id: $dataset_id})<-[:TRANSFORMS_TO]-(source)
    RETURN source
    """
    return execute_query(query, {"dataset_id": dataset_id})

def get_downstream_datasets(dataset_id):
    """Get all downstream datasets"""
    query = """
    MATCH (source {id: $dataset_id})-[:TRANSFORMS_TO]->(target)
    RETURN target
    """
    return execute_query(query, {"dataset_id": dataset_id})
```

---

## Metadata Standards

### Metadata Schemas

```json
{
    "$schema": "http://json-schema.org/draft-07/schema#",
    "title": "Dataset Metadata",
    "type": "object",
    "properties": {
        "id": {"type": "string", "format": "uuid"},
        "name": {"type": "string"},
        "description": {"type": "string"},
        "owner": {"type": "string"},
        "tags": {"type": "array", "items": {"type": "string"}},
        "schema": {
            "type": "object",
            "properties": {
                "columns": {
                    "type": "array",
                    "items": {
                        "type": "object",
                        "properties": {
                            "name": {"type": "string"},
                            "type": {"type": "string"},
                            "description": {"type": "string"}
                        }
                    }
                }
            }
        }
    },
    "required": ["id", "name", "description", "owner"]
}
```

### Metadata APIs

```python
# Metadata REST API
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class DatasetMetadata(BaseModel):
    id: str
    name: str
    description: str
    owner: str
    tags: list[str]

@app.get("/datasets/{dataset_id}")
async def get_dataset(dataset_id: str):
    return get_dataset_metadata(dataset_id)

@app.post("/datasets")
async def create_dataset(metadata: DatasetMetadata):
    return save_dataset_metadata(metadata)

@app.put("/datasets/{dataset_id}")
async def update_dataset(dataset_id: str, metadata: DatasetMetadata):
    return update_dataset_metadata(dataset_id, metadata)

@app.delete("/datasets/{dataset_id}")
async def delete_dataset(dataset_id: str):
    return delete_dataset_metadata(dataset_id)
```

---

## Best Practices

### Metadata Collection

1. **Automate collection**: Use crawlers and scanners
2. **Capture at source**: Collect metadata as close to source as possible
3. **Standardize formats**: Use consistent schemas and formats
4. **Validate metadata**: Ensure accuracy and completeness

### Metadata Storage

1. **Choose appropriate storage**: Graph, document, or search index
2. **Index for search**: Enable full-text search
3. **Version metadata**: Track changes over time
4. **Backup regularly**: Ensure metadata is recoverable

### Metadata Quality

1. **Validate completeness**: Ensure all required fields are present
2. **Check accuracy**: Verify metadata matches actual data
3. **Monitor freshness**: Keep metadata up-to-date
4. **Enforce standards**: Use validation rules

### Metadata Governance

1. **Assign ownership**: Clear responsibility for metadata
2. **Define standards**: Establish metadata standards
3. **Monitor compliance**: Track adherence to standards
4. **Continuous improvement**: Regularly review and enhance

---

## References

- [Data Management Body of Knowledge (DAMA-DMBOK)](https://www.dama.org/cpages/body-of-knowledge)
- [Metadata Management Best Practices](https://www.dataversity.net/metadata-management-best-practices/)
- [OpenMetadata](https://open-metadata.org/)
- [DataHub](https://datahubproject.io/)
- [Apache Atlas](https://atlas.apache.org/)
- [Amundsen](https://www.amundsen.io/)
