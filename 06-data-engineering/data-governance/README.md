# Data Governance

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Data Governance Framework](#data-governance-framework)
- [Data Quality Management](#data-quality-management)
- [Data Security](#data-security)
- [Data Privacy](#data-privacy)
- [Data Lineage](#data-lineage)
- [Data Catalog](#data-catalog)
- [Data Stewardship](#data-stewardship)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Data governance is the process of managing the availability, usability,
integrity, and security of data used in an organization. It ensures that
data is consistent, trustworthy, and properly protected.

### Key Characteristics

- **Accountability**: Clear roles and responsibilities
- **Policies**: Defined rules and standards
- **Quality**: Ensured accuracy and consistency
- **Security**: Protected from unauthorized access
- **Compliance**: Meeting regulatory requirements

### When to Use Data Governance

- Regulatory compliance (GDPR, CCPA, HIPAA)
- Data quality improvement
- Risk management
- Data-driven decision making
- Data security and privacy

### Data Governance vs Data Management

| Feature | Data Governance | Data Management |
|---------|----------------|-----------------|
| Focus | Policies and standards | Implementation |
| Scope | Organization-wide | Specific systems |
| People | Stewards and owners | Engineers and analysts |
| Tools | Catalogs and policies | ETL and databases |

---

## Core Concepts

### Data Governance Principles

1. **Data Quality**: Ensure data is accurate, complete, and consistent
2. **Data Security**: Protect data from unauthorized access
3. **Data Privacy**: Comply with privacy regulations
4. **Data Lineage**: Track data movement and transformations
5. **Data Accountability**: Assign ownership and responsibility
6. **Data Standards**: Define consistent data definitions
7. **Data Lifecycle**: Manage data from creation to deletion

### Data Governance Roles

```python
# Data Governance Roles
roles = {
    "Data Owner": {
        "responsibilities": [
            "Defines data strategy",
            "Approves data policies",
            "Accountable for data quality",
            "Authorizes data access"
        ],
        "examples": ["VP of Marketing", "CFO", "CTO"]
    },
    "Data Steward": {
        "responsibilities": [
            "Implements data policies",
            "Monitors data quality",
            "Resolves data issues",
            "Manages data definitions"
        ],
        "examples": ["Marketing Analyst", "Finance Analyst"]
    },
    "Data Custodian": {
        "responsibilities": [
            "Manages data infrastructure",
            "Implements security controls",
            "Backs up and recovers data",
            "Monitors data access"
        ],
        "examples": ["Data Engineer", "DBA"]
    },
    "Data Consumer": {
        "responsibilities": [
            "Uses data responsibly",
            "Reports data issues",
            "Follows data policies",
            "Maintains data confidentiality"
        ],
        "examples": ["Business Analyst", "Data Scientist"]
    }
}
```

---

## Data Governance Framework

### Framework Components

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Data Governance Framework                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Governance Structure                      │   │
│  │  - Data Governance Council                                   │   │
│  │  - Data Owners                                              │   │
│  │  - Data Stewards                                            │   │
│  │  - Data Custodians                                          │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Policies and Standards                    │   │
│  │  - Data Quality Standards                                    │   │
│  │  - Data Security Policies                                    │   │
│  │  - Data Privacy Policies                                     │   │
│  │  - Data Retention Policies                                   │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Processes and Procedures                  │   │
│  │  - Data Quality Management                                   │   │
│  │  - Data Access Management                                    │   │
│  │  - Data Change Management                                    │   │
│  │  - Incident Management                                       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Tools and Technologies                    │   │
│  │  - Data Catalog                                             │   │
│  │  - Data Lineage Tools                                       │   │
│  │  - Data Quality Tools                                       │   │
│  │  - Data Security Tools                                      │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Governance Council

```python
# Data Governance Council
governance_council = {
    "chair": "Chief Data Officer",
    "members": [
        "VP of Marketing",
        "VP of Finance",
        "VP of Engineering",
        "Chief Privacy Officer",
        "Chief Security Officer"
    ],
    "responsibilities": [
        "Defines data strategy",
        "Approves data policies",
        "Resolves data disputes",
        "Monitors governance effectiveness"
    ],
    "meeting_frequency": "Monthly"
}
```

---

## Data Quality Management

### Data Quality Dimensions

```python
# Data Quality Dimensions
quality_dimensions = {
    "Accuracy": {
        "definition": "Data correctly represents the real-world entity",
        "metrics": [
            "Error rate",
            "Validation pass rate",
            "Comparison with source"
        ]
    },
    "Completeness": {
        "definition": "Data is complete and not missing",
        "metrics": [
            "Null percentage",
            "Missing value count",
            "Record completeness"
        ]
    },
    "Consistency": {
        "definition": "Data is consistent across systems",
        "metrics": [
            "Cross-system match rate",
            "Duplicate rate",
            "Format consistency"
        ]
    },
    "Timeliness": {
        "definition": "Data is available when needed",
        "metrics": [
            "Data freshness",
            "Latency",
            "Update frequency"
        ]
    },
    "Validity": {
        "definition": "Data conforms to defined formats and rules",
        "metrics": [
            "Format compliance",
            "Range compliance",
            "Rule compliance"
        ]
    },
    "Uniqueness": {
        "definition": "Data is unique and not duplicated",
        "metrics": [
            "Duplicate rate",
            "Unique record count",
            "Primary key uniqueness"
        ]
    }
}
```

### Data Quality Rules

```python
# Data Quality Rules
def validate_data_quality(df, rules):
    """Validate data against quality rules"""
    violations = []

    # Check for null values
    for column in rules.get("not_null", []):
        null_count = df.filter(col(column).isNull()).count()
        if null_count > 0:
            violations.append({
                "rule": "not_null",
                "column": column,
                "violations": null_count
            })

    # Check for valid ranges
    for column, (min_val, max_val) in rules.get("range", {}).items():
        invalid_count = df.filter(
            (col(column) < min_val) | (col(column) > max_val)
        ).count()
        if invalid_count > 0:
            violations.append({
                "rule": "range",
                "column": column,
                "violations": invalid_count
            })

    # Check for valid values
    for column, valid_values in rules.get("values", {}).items():
        invalid_count = df.filter(~col(column).isin(valid_values)).count()
        if invalid_count > 0:
            violations.append({
                "rule": "values",
                "column": column,
                "violations": invalid_count
            })

    # Check for duplicates
    for columns in rules.get("unique", []):
        duplicate_count = df.groupBy(columns).count().filter(col("count") > 1).count()
        if duplicate_count > 0:
            violations.append({
                "rule": "unique",
                "columns": columns,
                "violations": duplicate_count
            })

    return violations

# Define rules
rules = {
    "not_null": ["id", "name", "email"],
    "range": {
        "age": (0, 150),
        "amount": (0, 1000000)
    },
    "values": {
        "status": ["active", "inactive", "pending"]
    },
    "unique": ["id"]
}

# Validate
violations = validate_data_quality(df, rules)
```

---

## Data Security

### Security Controls

```python
# Data Security Controls
security_controls = {
    "Access Control": {
        "methods": [
            "Role-Based Access Control (RBAC)",
            "Attribute-Based Access Control (ABAC)",
            "Mandatory Access Control (MAC)"
        ],
        "implementation": [
            "Define roles and permissions",
            "Implement authentication",
            "Enforce authorization",
            "Monitor access"
        ]
    },
    "Encryption": {
        "at_rest": [
            "AES-256",
            "AWS KMS",
            "Azure Key Vault"
        ],
        "in_transit": [
            "TLS 1.2+",
            "SSL certificates",
            "VPN"
        ]
    },
    "Data Masking": {
        "methods": [
            "Static masking",
            "Dynamic masking",
            "Tokenization"
        ],
        "use_cases": [
            "Development environments",
            "Testing environments",
            "Reporting"
        ]
    },
    "Audit Logging": {
        "events": [
            "Data access",
            "Data modifications",
            "Schema changes",
            "User management"
        ],
        "retention": "7 years"
    }
}
```

### Access Control Matrix

```python
# Access Control Matrix
access_matrix = {
    "roles": {
        "data-engineer": {
            "read": ["raw", "staging", "curated"],
            "write": ["raw", "staging", "curated"],
            "admin": []
        },
        "data-analyst": {
            "read": ["curated"],
            "write": [],
            "admin": []
        },
        "business-user": {
            "read": ["curated"],
            "write": [],
            "admin": []
        },
        "admin": {
            "read": ["*"],
            "write": ["*"],
            "admin": ["*"]
        }
    }
}
```

---

## Data Privacy

### Privacy Regulations

```python
# Privacy Regulations
regulations = {
    "GDPR": {
        "full_name": "General Data Protection Regulation",
        "scope": "EU residents",
        "requirements": [
            "Consent for data collection",
            "Right to access",
            "Right to rectification",
            "Right to erasure",
            "Data portability",
            "Privacy by design"
        ],
        "penalties": "Up to 4% of annual global turnover"
    },
    "CCPA": {
        "full_name": "California Consumer Privacy Act",
        "scope": "California residents",
        "requirements": [
            "Right to know",
            "Right to delete",
            "Right to opt-out",
            "Non-discrimination"
        ],
        "penalties": "Up to $7,500 per violation"
    },
    "HIPAA": {
        "full_name": "Health Insurance Portability and Accountability Act",
        "scope": "Health information",
        "requirements": [
            "Privacy rule",
            "Security rule",
            "Breach notification rule"
        ],
        "penalties": "Up to $1.5 million per violation"
    }
}
```

### PII Handling

```python
# PII Classification
pii_classification = {
    "Direct Identifiers": [
        "Name",
        "Email",
        "Phone number",
        "Social Security Number",
        "Passport number"
    ],
    "Indirect Identifiers": [
        "IP address",
        "Device ID",
        "Cookie ID",
        "Location data"
    ],
    "Sensitive PII": [
        "Health information",
        "Financial information",
        "Biometric data",
        "Criminal records"
    ]
}

# PII Masking
def mask_pii(df):
    """Mask PII columns"""
    from pyspark.sql.functions import when, concat, lit, substr

    # Mask email
    df = df.withColumn(
        "masked_email",
        when(
            col("email").isNotNull(),
            concat(substr(col("email"), 1, 2), lit("***@"), substring_INDEX(col("email"), "@", -1))
        ).otherwise(None)
    )

    # Mask phone
    df = df.withColumn(
        "masked_phone",
        when(
            col("phone").isNotNull(),
            concat(lit("***-***-"), substr(col("phone"), -4, 4))
        ).otherwise(None)
    )

    # Mask SSN
    df = df.withColumn(
        "masked_ssn",
        when(
            col("ssn").isNotNull(),
            concat(lit("***-**-"), substr(col("ssn"), -4, 4))
        ).otherwise(None)
    )

    return df
```

---

## Data Lineage

### Lineage Tracking

```python
# Data Lineage
lineage = {
    "source": {
        "system": "PostgreSQL",
        "database": "mydb",
        "table": "users",
        "columns": ["id", "name", "email"]
    },
    "transformations": [
        {
            "step": 1,
            "operation": "filter",
            "description": "Remove inactive users",
            "sql": "SELECT * FROM users WHERE status = 'active'"
        },
        {
            "step": 2,
            "operation": "enrich",
            "description": "Add customer segment",
            "sql": "SELECT *, CASE WHEN purchases > 100 THEN 'VIP' ELSE 'Regular' END AS segment FROM filtered_users"
        }
    ],
    "target": {
        "system": "Snowflake",
        "database": "analytics",
        "table": "active_customers",
        "columns": ["id", "name", "email", "segment"]
    }
}
```

### Lineage Tools

```python
# Lineage Tools
lineage_tools = {
    "OpenLineage": {
        "description": "Open standard for data lineage",
        "features": [
            "Vendor neutral",
            "Extensible",
            "Integration with Spark, Airflow, dbt"
        ]
    },
    "Apache Atlas": {
        "description": "Metadata management and data governance",
        "features": [
            "Hadoop ecosystem integration",
            "Type system",
            "Lineage tracking"
        ]
    },
    "DataHub": {
        "description": "Metadata platform for data ecosystems",
        "features": [
            "Real-time metadata",
            "Discovery",
            "Lineage",
            "Governance"
        ]
    },
    "Amundsen": {
        "description": "Data discovery and metadata platform",
        "features": [
            "Data discovery",
            "Lineage",
            "Quality"
        ]
    }
}
```

---

## Data Catalog

### Catalog Components

```python
# Data Catalog
data_catalog = {
    "metadata": {
        "technical": {
            "schema": "Column names, types, constraints",
            "lineage": "Data flow and transformations",
            "quality": "Quality metrics and scores"
        },
        "business": {
            "description": "Business meaning and context",
            "ownership": "Data owner and steward",
            "classification": "Data sensitivity and classification"
        }
    },
    "discovery": {
        "search": "Full-text search across metadata",
        "browse": "Navigate data assets",
        "recommendations": "Suggested datasets"
    },
    "collaboration": {
        "reviews": "Data asset reviews",
        "ratings": "User ratings",
        "comments": "Discussion and feedback"
    }
}
```

### Catalog Implementation

```python
# Data Catalog Implementation
catalog_implementation = {
    "metadata_store": {
        "options": [
            "PostgreSQL",
            "Elasticsearch",
            "Neo4j"
        ],
        "schema": {
            "datasets": {
                "id": "UUID",
                "name": "String",
                "description": "Text",
                "owner": "String",
                "tags": "Array<String>",
                "schema": "JSON",
                "lineage": "JSON",
                "quality": "JSON"
            }
        }
    },
    "api": {
        "endpoints": [
            "GET /datasets",
            "GET /datasets/{id}",
            "POST /datasets",
            "PUT /datasets/{id}",
            "DELETE /datasets/{id}",
            "GET /search",
            "GET /lineage/{id}"
        ]
    },
    "ui": {
        "features": [
            "Search and browse",
            "Dataset details",
            "Lineage visualization",
            "Quality dashboards"
        ]
    }
}
```

---

## Data Stewardship

### Steward Responsibilities

```python
# Data Steward Responsibilities
steward_responsibilities = {
    "Data Quality": [
        "Monitor data quality metrics",
        "Investigate data quality issues",
        "Define data quality rules",
        "Collaborate with data owners on remediation"
    ],
    "Data Definitions": [
        "Maintain business glossary",
        "Define data elements",
        "Document data transformations",
        "Ensure consistent terminology"
    ],
    "Data Access": [
        "Review access requests",
        "Monitor data access",
        "Enforce access policies",
        "Handle access exceptions"
    ],
    "Data Issues": [
        "Triage data issues",
        "Coordinate remediation",
        "Communicate status",
        "Document resolutions"
    ]
}
```

### Stewardship Process

```python
# Data Stewardship Process
stewardship_process = {
    "issue_management": {
        "steps": [
            "Issue identification",
            "Issue triage",
            "Root cause analysis",
            "Remediation planning",
            "Implementation",
            "Verification",
            "Closure"
        ]
    },
    "change_management": {
        "steps": [
            "Change request",
            "Impact analysis",
            "Approval",
            "Implementation",
            "Testing",
            "Deployment"
        ]
    },
    "quality_management": {
        "steps": [
            "Define quality rules",
            "Implement monitoring",
            "Identify issues",
            "Investigate root causes",
            "Implement fixes",
            "Verify results"
        ]
    }
}
```

---

## Best Practices

### Governance Program

1. **Executive sponsorship**: Secure leadership support
2. **Clear roles and responsibilities**: Define data ownership
3. **Policies and standards**: Document and communicate
4. **Training and awareness**: Educate stakeholders

### Data Quality

1. **Define quality metrics**: Establish measurement criteria
2. **Implement monitoring**: Track quality over time
3. **Automate validation**: Use tools for consistency
4. **Continuous improvement**: Regularly review and enhance

### Data Security

1. **Classify data**: Identify sensitivity levels
2. **Implement controls**: Access, encryption, masking
3. **Monitor access**: Track and audit data usage
4. **Respond to incidents**: Have incident response plans

### Compliance

1. **Understand regulations**: Know applicable laws
2. **Implement controls**: Meet regulatory requirements
3. **Document compliance**: Maintain evidence
4. **Regular audits**: Verify compliance

---

## References

- [DAMA-DMBOK](https://www.dama.org/cpages/body-of-knowledge)
- [Data Governance Best Practices](https://www.dataversity.net/data-governance-best-practices/)
- [Data Governance Framework](https://www.gartner.com/en/information-technology/glossary/data-governance)
- [GDPR Compliance](https://gdpr.eu/)
- [Data Governance Tools](https://www.g2.com/categories/data-governance)
