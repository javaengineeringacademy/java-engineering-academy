# Data Quality

## Table of Contents

- [Overview](#overview)
- [Data Quality Dimensions](#data-quality-dimensions)
- [Data Quality Rules](#data-quality-rules)
- [Data Quality Tools](#data-quality-tools)
- [Data Quality Frameworks](#data-quality-frameworks)
- [Data Quality Monitoring](#data-quality-monitoring)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Data quality refers to the fitness of data for its intended uses in operations,
decision-making, and planning. High-quality data is accurate, complete,
consistent, timely, and valid.

### Key Characteristics

- **Accuracy**: Data correctly represents the real-world entity
- **Completeness**: Data is complete and not missing
- **Consistency**: Data is consistent across systems
- **Timeliness**: Data is available when needed
- **Validity**: Data conforms to defined formats and rules
- **Uniqueness**: Data is unique and not duplicated

### When to Use Data Quality

- Data warehouse and data lake management
- Business intelligence and reporting
- Machine learning and analytics
- Regulatory compliance
- Operational efficiency

### Data Quality vs Data Governance

| Feature | Data Quality | Data Governance |
|---------|-------------|-----------------|
| Focus | Data accuracy and consistency | Policies and standards |
| Scope | Specific datasets | Organization-wide |
| Tools | Quality checks and monitors | Catalogs and policies |
| Metrics | Quality scores and metrics | Compliance and access |

---

## Data Quality Dimensions

### Dimension Definitions

```python
# Data Quality Dimensions
quality_dimensions = {
    "Accuracy": {
        "definition": "Data correctly represents the real-world entity",
        "metrics": [
            "Error rate",
            "Validation pass rate",
            "Comparison with source"
        ],
        "examples": [
            "Customer name matches source system",
            "Amount matches invoice",
            "Date is valid calendar date"
        ]
    },
    "Completeness": {
        "definition": "Data is complete and not missing",
        "metrics": [
            "Null percentage",
            "Missing value count",
            "Record completeness"
        ],
        "examples": [
            "All required fields are populated",
            "No missing transactions",
            "All customers have email addresses"
        ]
    },
    "Consistency": {
        "definition": "Data is consistent across systems",
        "metrics": [
            "Cross-system match rate",
            "Duplicate rate",
            "Format consistency"
        ],
        "examples": [
            "Customer ID matches across systems",
            "Amount is consistent between orders and invoices",
            "Date format is consistent"
        ]
    },
    "Timeliness": {
        "definition": "Data is available when needed",
        "metrics": [
            "Data freshness",
            "Latency",
            "Update frequency"
        ],
        "examples": [
            "Daily reports available by 9 AM",
            "Real-time data within 5 seconds",
            "Monthly data available by 5th of month"
        ]
    },
    "Validity": {
        "definition": "Data conforms to defined formats and rules",
        "metrics": [
            "Format compliance",
            "Range compliance",
            "Rule compliance"
        ],
        "examples": [
            "Email addresses are valid format",
            "Amounts are positive",
            "Dates are within valid range"
        ]
    },
    "Uniqueness": {
        "definition": "Data is unique and not duplicated",
        "metrics": [
            "Duplicate rate",
            "Unique record count",
            "Primary key uniqueness"
        ],
        "examples": [
            "Each customer has unique ID",
            "No duplicate transactions",
            "Each product has unique SKU"
        ]
    }
}
```

### Dimension Scoring

```python
# Data Quality Scoring
def calculate_quality_score(df, rules):
    """Calculate overall data quality score"""
    scores = {}

    # Accuracy score
    accuracy_score = calculate_accuracy_score(df, rules)
    scores["accuracy"] = accuracy_score

    # Completeness score
    completeness_score = calculate_completeness_score(df, rules)
    scores["completeness"] = completeness_score

    # Consistency score
    consistency_score = calculate_consistency_score(df, rules)
    scores["consistency"] = consistency_score

    # Timeliness score
    timeliness_score = calculate_timeliness_score(df, rules)
    scores["timeliness"] = timeliness_score

    # Validity score
    validity_score = calculate_validity_score(df, rules)
    scores["validity"] = validity_score

    # Uniqueness score
    uniqueness_score = calculate_uniqueness_score(df, rules)
    scores["uniqueness"] = uniqueness_score

    # Overall score
    overall_score = sum(scores.values()) / len(scores)
    scores["overall"] = overall_score

    return scores

def calculate_accuracy_score(df, rules):
    """Calculate accuracy score"""
    total_checks = 0
    passed_checks = 0

    # Check accuracy rules
    for rule in rules.get("accuracy", []):
        total_checks += 1
        if validate_accuracy_rule(df, rule):
            passed_checks += 1

    return (passed_checks / total_checks * 100) if total_checks > 0 else 100
```

---

## Data Quality Rules

### Rule Types

```python
# Data Quality Rules
quality_rules = {
    "not_null": {
        "description": "Check for null values",
        "example": "Column 'id' should not have null values",
        "sql": "SELECT COUNT(*) FROM table WHERE column IS NULL"
    },
    "unique": {
        "description": "Check for unique values",
        "example": "Column 'email' should have unique values",
        "sql": "SELECT column, COUNT(*) FROM table GROUP BY column HAVING COUNT(*) > 1"
    },
    "range": {
        "description": "Check for valid range",
        "example": "Column 'age' should be between 0 and 150",
        "sql": "SELECT COUNT(*) FROM table WHERE column < 0 OR column > 150"
    },
    "format": {
        "description": "Check for valid format",
        "example": "Column 'email' should match email format",
        "sql": "SELECT COUNT(*) FROM table WHERE column NOT REGEXP '^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$'"
    },
    "values": {
        "description": "Check for valid values",
        "example": "Column 'status' should be one of: active, inactive, pending",
        "sql": "SELECT COUNT(*) FROM table WHERE column NOT IN ('active', 'inactive', 'pending')"
    },
    "referential": {
        "description": "Check referential integrity",
        "example": "Column 'customer_id' should exist in customers table",
        "sql": "SELECT COUNT(*) FROM orders o LEFT JOIN customers c ON o.customer_id = c.id WHERE c.id IS NULL"
    },
    "statistical": {
        "description": "Check statistical properties",
        "example": "Column 'amount' should have mean between 0 and 10000",
        "sql": "SELECT AVG(column) FROM table"
    },
    "custom": {
        "description": "Custom business rules",
        "example": "Order total should equal sum of line items",
        "sql": "SELECT COUNT(*) FROM orders WHERE ABS(total - (SELECT SUM(amount) FROM order_items WHERE order_id = orders.id)) > 0.01"
    }
}
```

### Rule Implementation

```python
# Data Quality Rule Implementation
class DataQualityRule:
    def __init__(self, name, rule_type, column, params=None):
        self.name = name
        self.rule_type = rule_type
        self.column = column
        self.params = params or {}

    def validate(self, df):
        """Validate rule against dataframe"""
        if self.rule_type == "not_null":
            return self._validate_not_null(df)
        elif self.rule_type == "unique":
            return self._validate_unique(df)
        elif self.rule_type == "range":
            return self._validate_range(df)
        elif self.rule_type == "format":
            return self._validate_format(df)
        elif self.rule_type == "values":
            return self._validate_values(df)
        else:
            raise ValueError(f"Unknown rule type: {self.rule_type}")

    def _validate_not_null(self, df):
        """Validate not null rule"""
        null_count = df.filter(col(self.column).isNull()).count()
        total_count = df.count()
        return {
            "passed": null_count == 0,
            "null_count": null_count,
            "total_count": total_count,
            "null_percentage": (null_count / total_count * 100) if total_count > 0 else 0
        }

    def _validate_unique(self, df):
        """Validate unique rule"""
        duplicate_count = df.groupBy(self.column).count().filter(col("count") > 1).count()
        return {
            "passed": duplicate_count == 0,
            "duplicate_count": duplicate_count,
            "unique_count": df.select(self.column).distinct().count()
        }

    def _validate_range(self, df):
        """Validate range rule"""
        min_val = self.params.get("min")
        max_val = self.params.get("max")

        if min_val is not None and max_val is not None:
            invalid_count = df.filter(
                (col(self.column) < min_val) | (col(self.column) > max_val)
            ).count()
        elif min_val is not None:
            invalid_count = df.filter(col(self.column) < min_val).count()
        elif max_val is not None:
            invalid_count = df.filter(col(self.column) > max_val).count()
        else:
            invalid_count = 0

        return {
            "passed": invalid_count == 0,
            "invalid_count": invalid_count,
            "min_val": min_val,
            "max_val": max_val
        }
```

---

## Data Quality Tools

### Great Expectations

```python
import great_expectations as gx

# Create context
context = gx.get_context()

# Create expectation suite
suite = context.add_expectation_suite("my_suite")

# Add expectations
suite.add_expectation(
    gx.expectations.ExpectColumnValuesToNotBeNull(column="id")
)
suite.add_expectation(
    gx.expectations.ExpectColumnValuesToBeUnique(column="email")
)
suite.add_expectation(
    gx.expectations.ExpectColumnValuesToBeBetween(
        column="age", min_value=0, max_value=150
    )
)

# Validate data
result = context.run_validation_operator(
    "action_list_operator",
    assets_to_validate=[batch],
    run_id="my_run"
)
```

### Apache Griffin

```python
# Apache Griffin configuration
griffin_config = {
    "name": "data_quality_job",
    "process.type": "batch",
    "compute.type": "spark",
    "sink.type": ["log", "hdfs"],
    "metrics": [
        {
            "name": "accuracy",
            "type": "accuracy",
            "rule": "source.id = target.id",
            "source": {
                "type": "hive",
                "config": {
                    "database": "raw",
                    "table": "users"
                }
            },
            "target": {
                "type": "hive",
                "config": {
                    "database": "curated",
                    "table": "users"
                }
            }
        }
    ]
}
```

### Deequ

```python
import deequ
from deequ.checks import Check
from deequ.checks import CheckLevel

# Create Spark session
spark = deequ.SparkSession("data_quality")

# Create check
check = Check(spark, "data_quality_check", CheckLevel.Error) \
    .isComplete("id") \
    .isUnique("id") \
    .isContainedIn("status", ["active", "inactive", "pending"]) \
    .isNonNegative("amount")

# Run check
result = check.run(df)

# Get results
print(result.check_results)
```

### PySpark Data Quality

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("DataQuality").getOrCreate()

def validate_data_quality(df, rules):
    """Validate data quality using PySpark"""
    violations = []

    # Check not null
    for column in rules.get("not_null", []):
        null_count = df.filter(col(column).isNull()).count()
        if null_count > 0:
            violations.append({
                "rule": "not_null",
                "column": column,
                "violations": null_count
            })

    # Check unique
    for column in rules.get("unique", []):
        duplicate_count = df.groupBy(column).count().filter(col("count") > 1).count()
        if duplicate_count > 0:
            violations.append({
                "rule": "unique",
                "column": column,
                "violations": duplicate_count
            })

    # Check range
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

    return violations

# Define rules
rules = {
    "not_null": ["id", "name", "email"],
    "unique": ["id", "email"],
    "range": {
        "age": (0, 150),
        "amount": (0, 1000000)
    }
}

# Validate
violations = validate_data_quality(df, rules)
```

---

## Data Quality Frameworks

### Framework Components

```python
# Data Quality Framework
data_quality_framework = {
    "rules_engine": {
        "description": "Engine for executing quality rules",
        "components": [
            "Rule parser",
            "Rule executor",
            "Result aggregator"
        ]
    },
    "monitoring": {
        "description": "Monitoring and alerting",
        "components": [
            "Quality metrics",
            "Thresholds",
            "Alerts",
            "Dashboards"
        ]
    },
    "remediation": {
        "description": "Issue remediation",
        "components": [
            "Issue tracking",
            "Root cause analysis",
            "Fix implementation",
            "Verification"
        ]
    },
    "reporting": {
        "description": "Reporting and analytics",
        "components": [
            "Quality reports",
            "Trend analysis",
            "Scorecards",
            "Executive dashboards"
        ]
    }
}
```

### Framework Implementation

```python
# Data Quality Framework Implementation
class DataQualityFramework:
    def __init__(self, config):
        self.config = config
        self.rules = []
        self.results = []

    def add_rule(self, rule):
        """Add quality rule"""
        self.rules.append(rule)

    def validate(self, df):
        """Validate data against all rules"""
        results = []
        for rule in self.rules:
            result = rule.validate(df)
            results.append(result)
        return results

    def calculate_score(self, results):
        """Calculate overall quality score"""
        passed = sum(1 for r in results if r["passed"])
        total = len(results)
        return (passed / total * 100) if total > 0 else 100

    def generate_report(self, results):
        """Generate quality report"""
        report = {
            "timestamp": datetime.now().isoformat(),
            "total_rules": len(results),
            "passed_rules": sum(1 for r in results if r["passed"]),
            "failed_rules": sum(1 for r in results if not r["passed"]),
            "score": self.calculate_score(results),
            "details": results
        }
        return report
```

---

## Data Quality Monitoring

### Monitoring Setup

```python
# Data Quality Monitoring
def setup_monitoring(config):
    """Setup data quality monitoring"""
    monitoring = {
        "metrics": {
            "accuracy": {"threshold": 99, "alert": True},
            "completeness": {"threshold": 95, "alert": True},
            "consistency": {"threshold": 98, "alert": True},
            "timeliness": {"threshold": 99, "alert": True},
            "validity": {"threshold": 99, "alert": True},
            "uniqueness": {"threshold": 100, "alert": True}
        },
        "alerts": {
            "email": ["data-team@example.com"],
            "slack": "#data-quality",
            "pagerduty": "data-quality-oncall"
        },
        "schedule": {
            "frequency": "hourly",
            "retention": "90 days"
        }
    }
    return monitoring

def check_quality_and_alert(df, rules, monitoring):
    """Check quality and send alerts"""
    # Validate data
    violations = validate_data_quality(df, rules)

    # Calculate scores
    scores = calculate_quality_scores(df, rules)

    # Check thresholds
    alerts = []
    for dimension, score in scores.items():
        threshold = monitoring["metrics"][dimension]["threshold"]
        if score < threshold:
            alerts.append({
                "dimension": dimension,
                "score": score,
                "threshold": threshold
            })

    # Send alerts
    if alerts:
        send_alerts(alerts, monitoring["alerts"])

    return scores, alerts
```

### Monitoring Dashboard

```python
# Data Quality Dashboard
dashboard = {
    "overview": {
        "overall_score": 98.5,
        "total_datasets": 100,
        "datasets_with_issues": 5,
        "critical_issues": 2
    },
    "by_dimension": {
        "accuracy": 99.2,
        "completeness": 97.8,
        "consistency": 98.5,
        "timeliness": 99.0,
        "validity": 99.1,
        "uniqueness": 100.0
    },
    "trends": {
        "daily": [98.0, 98.2, 98.5, 98.3, 98.5],
        "weekly": [97.5, 98.0, 98.2, 98.5],
        "monthly": [96.0, 97.0, 98.0, 98.5]
    },
    "top_issues": [
        {"dataset": "orders", "dimension": "completeness", "score": 85.0},
        {"dataset": "customers", "dimension": "accuracy", "score": 92.0}
    ]
}
```

---

## Best Practices

### Rule Design

1. **Start simple**: Begin with basic rules (not null, unique)
2. **Be specific**: Define clear, measurable rules
3. **Set realistic thresholds**: Avoid false positives
4. **Regularly review**: Update rules as data evolves

### Monitoring

1. **Automate checks**: Run quality checks automatically
2. **Set up alerts**: Notify when quality drops
3. **Track trends**: Monitor quality over time
4. **Regular reporting**: Generate quality reports

### Remediation

1. **Prioritize issues**: Focus on critical issues first
2. **Root cause analysis**: Understand why issues occur
3. **Prevent recurrence**: Implement preventive measures
4. **Document fixes**: Record issues and solutions

### Communication

1. **Share results**: Communicate quality status
2. **Educate stakeholders**: Raise awareness of quality
3. **Celebrate improvements**: Acknowledge progress
4. **Continuous improvement**: Regularly enhance processes

---

## References

- [Great Expectations](https://greatexpectations.io/)
- [Apache Griffin](https://griffin.apache.org/)
- [Deequ](https://github.com/awslabs/deequ)
- [Data Quality Fundamentals](https://www.oreilly.com/library/view/data-quality-fundamentals/9781098106478/)
- [Data Quality Management](https://www.amazon.com/Data-Quality-Management-Practical-Guide/dp/1492028161)
