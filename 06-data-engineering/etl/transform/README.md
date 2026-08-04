# Data Transformation

Data transformation is the process of converting data from one format or structure to another. This covers transformation patterns, data cleaning, enrichment, and validation.

## Table of Contents

- [Overview](#overview)
- [Transformation Types](#transformation-types)
- [Data Cleaning](#data-cleaning)
- [Data Enrichment](#data-enrichment)
- [Examples](#examples)
- [Best Practices](#best-practices)

## Overview

Transformation is the middle step in ETL/ELT pipelines where raw data is converted into a format suitable for analysis and reporting.

### Transformation Pipeline

```
┌─────────────────────────────────────────────────────────────────┐
│                    TRANSFORMATION PIPELINE                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Raw Data ──> Clean ──> Validate ──> Enrich ──> Aggregate      │
│                                                                 │
│  ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐       │
│  │ Remove  │──>│ Format  │──>│ Add     │──>│ Summarize│       │
│  │ Duplicates│  │ Standard│   │ External│   │ Group    │       │
│  │ Handle  │   │ Normalize│  │ Data    │   │ Calculate│       │
│  │ Nulls   │   │ Type    │   │ Lookup  │   │          │       │
│  └─────────┘   └─────────┘   └─────────┘   └─────────┘       │
└─────────────────────────────────────────────────────────────────┘
```

## Transformation Types

### Data Type Transformation

```python
from typing import Any, Dict
from datetime import datetime

class TypeTransformer:
    @staticmethod
    def to_string(value: Any) -> str:
        return str(value) if value is not None else None

    @staticmethod
    def to_integer(value: Any) -> int:
        try:
            return int(value)
        except (ValueError, TypeError):
            return None

    @staticmethod
    def to_float(value: Any) -> float:
        try:
            return float(value)
        except (ValueError, TypeError):
            return None

    @staticmethod
    def to_boolean(value: Any) -> bool:
        if isinstance(value, bool):
            return value
        if isinstance(value, str):
            return value.lower() in ('true', 'yes', '1', 'on')
        return bool(value)

    @staticmethod
    def to_datetime(value: Any, format: str = None) -> datetime:
        if isinstance(value, datetime):
            return value
        if isinstance(value, str):
            if format:
                return datetime.strptime(value, format)
            return datetime.fromisoformat(value)
        return None

    def transform_record(self, record: Dict, schema: Dict) -> Dict:
        """Transform record according to schema"""
        transformed = {}
        for field, field_type in schema.items():
            if field in record:
                transformer = getattr(self, f'to_{field_type}', None)
                if transformer:
                    transformed[field] = transformer(record[field])
                else:
                    transformed[field] = record[field]
        return transformed
```

### Normalization

```python
from typing import List, Dict

class Normalizer:
    @staticmethod
    def min_max_normalize(values: List[float]) -> List[float]:
        """Normalize values to 0-1 range"""
        if not values:
            return []

        min_val = min(values)
        max_val = max(values)

        if min_val == max_val:
            return [0.5] * len(values)

        return [(v - min_val) / (max_val - min_val) for v in values]

    @staticmethod
    def z_score_normalize(values: List[float]) -> List[float]:
        """Normalize using z-score"""
        if not values:
            return []

        mean = sum(values) / len(values)
        variance = sum((v - mean) ** 2 for v in values) / len(values)
        std_dev = variance ** 0.5

        if std_dev == 0:
            return [0.0] * len(values)

        return [(v - mean) / std_dev for v in values]

    def normalize_record(self, record: Dict, numeric_fields: List[str]) -> Dict:
        """Normalize numeric fields in a record"""
        normalized = record.copy()

        for field in numeric_fields:
            if field in normalized and normalized[field] is not None:
                normalized[f"{field}_normalized"] = self.min_max_normalize([normalized[field]])[0]

        return normalized
```

### Denormalization

```python
from typing import List, Dict

class Denormalizer:
    @staticmethod
    def join_tables(main_table: List[Dict], lookup_table: List[Dict],
                    join_key: str, lookup_fields: List[str]) -> List[Dict]:
        """Join tables for denormalization"""
        # Create lookup index
        lookup_index = {row[join_key]: row for row in lookup_table}

        # Join
        result = []
        for row in main_table:
            denormalized = row.copy()
            lookup_row = lookup_index.get(row[join_key])

            if lookup_row:
                for field in lookup_fields:
                    denormalized[f"lookup_{field}"] = lookup_row.get(field)

            result.append(denormalized)

        return result

    @staticmethod
    def flatten_nested(record: Dict, prefix: str = '') -> Dict:
        """Flatten nested dictionary"""
        flattened = {}

        for key, value in record.items():
            new_key = f"{prefix}{key}" if prefix else key

            if isinstance(value, dict):
                flattened.update(
                    Denormalizer.flatten_nested(value, f"{new_key}_")
                )
            elif isinstance(value, list):
                for i, item in enumerate(value):
                    if isinstance(item, dict):
                        flattened.update(
                            Denormalizer.flatten_nested(item, f"{new_key}_{i}_")
                        )
                    else:
                        flattened[f"{new_key}_{i}"] = item
            else:
                flattened[new_key] = value

        return flattened
```

## Data Cleaning

### Handling Missing Values

```python
from typing import Any, Dict, List
from enum import Enum

class FillStrategy(Enum):
    MEAN = "mean"
    MEDIAN = "median"
    MODE = "mode"
    FORWARD_FILL = "forward_fill"
    BACKWARD_FILL = "backward_fill"
    CONSTANT = "constant"

class MissingValueHandler:
    def __init__(self):
        self.fill_values = {}

    def calculate_fill_values(self, data: List[Dict], strategy: Dict[str, FillStrategy]):
        """Calculate fill values based on strategy"""
        for field, strat in strategy.items():
            values = [r[field] for r in data if field in r and r[field] is not None]

            if not values:
                self.fill_values[field] = None
                continue

            if strat == FillStrategy.MEAN:
                self.fill_values[field] = sum(values) / len(values)
            elif strat == FillStrategy.MEDIAN:
                sorted_vals = sorted(values)
                mid = len(sorted_vals) // 2
                self.fill_values[field] = sorted_vals[mid]
            elif strat == FillStrategy.MODE:
                from collections import Counter
                counter = Counter(values)
                self.fill_values[field] = counter.most_common(1)[0][0]

    def fill_missing(self, record: Dict, strategy: Dict[str, FillStrategy]) -> Dict:
        """Fill missing values in record"""
        filled = record.copy()

        for field, strat in strategy.items():
            if field not in filled or filled[field] is None:
                if strat == FillStrategy.CONSTANT:
                    filled[field] = self.fill_values.get(field, 0)
                elif field in self.fill_values:
                    filled[field] = self.fill_values[field]

        return filled

    def drop_rows_with_missing(self, data: List[Dict], required_fields: List[str]) -> List[Dict]:
        """Drop rows with missing required fields"""
        return [
            row for row in data
            if all(field in row and row[field] is not None for field in required_fields)
        ]
```

### Removing Duplicates

```python
from typing import List, Dict, Hashable

class DuplicateRemover:
    @staticmethod
    def remove_exact_duplicates(data: List[Dict]) -> List[Dict]:
        """Remove exact duplicate records"""
        seen = set()
        unique_data = []

        for record in data:
            record_tuple = tuple(sorted(record.items()))
            if record_tuple not in seen:
                seen.add(record_tuple)
                unique_data.append(record)

        return unique_data

    @staticmethod
    def remove_duplicates_by_key(data: List[Dict], key_fields: List[str]) -> List[Dict]:
        """Remove duplicates by key fields, keeping latest"""
        seen = {}
        for record in data:
            key = tuple(record.get(field) for field in key_fields)
            if key not in seen:
                seen[key] = record
            else:
                # Keep record with latest timestamp if available
                if 'timestamp' in record:
                    if record['timestamp'] > seen[key]['timestamp']:
                        seen[key] = record

        return list(seen.values())

    @staticmethod
    def remove_duplicates_keep_first(data: List[Dict], key_fields: List[str]) -> List[Dict]:
        """Remove duplicates, keeping first occurrence"""
        seen = set()
        unique_data = []

        for record in data:
            key = tuple(record.get(field) for field in key_fields)
            if key not in seen:
                seen.add(key)
                unique_data.append(record)

        return unique_data
```

### Data Validation

```python
from typing import Any, Dict, List, Callable
from dataclasses import dataclass
from enum import Enum

class ValidationSeverity(Enum):
    ERROR = "error"
    WARNING = "warning"
    INFO = "info"

@dataclass
class ValidationResult:
    field: str
    rule: str
    passed: bool
    severity: ValidationSeverity
    message: str = None

class DataValidator:
    def __init__(self):
        self.rules: List[Dict] = []

    def add_rule(self, field: str, rule_type: str, params: Dict = None,
                 severity: ValidationSeverity = ValidationSeverity.ERROR):
        """Add validation rule"""
        self.rules.append({
            'field': field,
            'rule_type': rule_type,
            'params': params or {},
            'severity': severity
        })

    def validate(self, record: Dict) -> List[ValidationResult]:
        """Validate a record"""
        results = []

        for rule in self.rules:
            field = rule['field']
            value = record.get(field)

            result = self.apply_rule(value, rule)
            results.append(result)

        return results

    def apply_rule(self, value: Any, rule: Dict) -> ValidationResult:
        """Apply a single validation rule"""
        rule_type = rule['rule_type']
        params = rule['params']

        if rule_type == 'not_null':
            passed = value is not None
            return ValidationResult(
                field=rule['field'],
                rule=rule_type,
                passed=passed,
                severity=rule['severity'],
                message=f"Field {rule['field']} is null" if not passed else None
            )

        elif rule_type == 'min_length':
            min_len = params.get('min', 0)
            passed = value is not None and len(str(value)) >= min_len
            return ValidationResult(
                field=rule['field'],
                rule=rule_type,
                passed=passed,
                severity=rule['severity'],
                message=f"Field {rule['field']} too short" if not passed else None
            )

        elif rule_type == 'max_value':
            max_val = params.get('max', float('inf'))
            passed = value is not None and value <= max_val
            return ValidationResult(
                field=rule['field'],
                rule=rule_type,
                passed=passed,
                severity=rule['severity'],
                message=f"Field {rule['field']} exceeds max" if not passed else None
            )

        elif rule_type == 'regex':
            import re
            pattern = params.get('pattern', '')
            passed = value is not None and bool(re.match(pattern, str(value)))
            return ValidationResult(
                field=rule['field'],
                rule=rule_type,
                passed=passed,
                severity=rule['severity'],
                message=f"Field {rule['field']} doesn't match pattern" if not passed else None
            )

        return ValidationResult(
            field=rule['field'],
            rule=rule_type,
            passed=True,
            severity=rule['severity']
        )
```

## Data Enrichment

### External Data Enrichment

```python
from typing import Dict, List
from datetime import datetime

class DataEnricher:
    def __init__(self):
        self.enrichment_sources = {}

    def add_enrichment_source(self, name: str, source):
        """Add an enrichment source"""
        self.enrichment_sources[name] = source

    def enrich_record(self, record: Dict, enrichments: List[str]) -> Dict:
        """Enrich a record with external data"""
        enriched = record.copy()

        for enrichment_name in enrichments:
            if enrichment_name in self.enrichment_sources:
                source = self.enrichment_sources[enrichment_name]
                additional_data = source.lookup(record)
                enriched.update(additional_data)

        return enriched

class GeoEnrichment:
    """Enrich with geolocation data"""
    def __init__(self, geo_service):
        self.geo_service = geo_service

    def lookup(self, record: Dict) -> Dict:
        """Lookup geolocation data"""
        if 'ip_address' in record:
            geo_data = self.geo_service.lookup_ip(record['ip_address'])
            return {
                'country': geo_data.get('country'),
                'city': geo_data.get('city'),
                'latitude': geo_data.get('latitude'),
                'longitude': geo_data.get('longitude')
            }
        return {}

class UserEnrichment:
    """Enrich with user data"""
    def __init__(self, user_service):
        self.user_service = user_service

    def lookup(self, record: Dict) -> Dict:
        """Lookup user data"""
        if 'user_id' in record:
            user_data = self.user_service.get_user(record['user_id'])
            return {
                'user_name': user_data.get('name'),
                'user_email': user_data.get('email'),
                'user_segment': user_data.get('segment')
            }
        return {}
```

### Derived Field Generation

```python
from typing import Dict, Callable
from datetime import datetime

class DerivedFieldGenerator:
    def __init__(self):
        self.derived_fields: Dict[str, Callable] = {}

    def add_derived_field(self, name: str, func: Callable):
        """Add a derived field"""
        self.derived_fields[name] = func

    def generate(self, record: Dict) -> Dict:
        """Generate all derived fields"""
        result = record.copy()

        for field_name, func in self.derived_fields.items():
            try:
                result[field_name] = func(record)
            except Exception as e:
                result[field_name] = None

        return result

# Example derived fields
def create_derived_fields():
    generator = DerivedFieldGenerator()

    # Calculate age from birth date
    generator.add_derived_field(
        'age',
        lambda r: (datetime.now() - r['birth_date']).days // 365 if 'birth_date' in r else None
    )

    # Full name from first and last
    generator.add_derived_field(
        'full_name',
        lambda r: f"{r.get('first_name', '')} {r.get('last_name', '')}".strip()
    )

    # Email domain
    generator.add_derived_field(
        'email_domain',
        lambda r: r['email'].split('@')[1] if 'email' in r and '@' in r.get('email', '') else None
    )

    # Price with tax
    generator.add_derived_field(
        'price_with_tax',
        lambda r: r['price'] * 1.08 if 'price' in r else None
    )

    return generator
```

## Examples

### Customer Data Pipeline

```python
from typing import Dict, List
from datetime import datetime

class CustomerDataPipeline:
    def __init__(self):
        self.type_transformer = TypeTransformer()
        self.duplicate_remover = DuplicateRemover()
        self.missing_handler = MissingValueHandler()
        self.validator = DataValidator()
        self.enricher = DataEnricher()

    def process(self, raw_data: List[Dict]) -> Dict:
        """Complete transformation pipeline"""
        # Step 1: Remove exact duplicates
        data = self.duplicate_remover.remove_exact_duplicates(raw_data)

        # Step 2: Handle missing values
        fill_strategies = {
            'age': FillStrategy.MEAN,
            'city': FillStrategy.CONSTANT
        }
        self.missing_handler.calculate_fill_values(data, fill_strategies)
        data = [self.missing_handler.fill_missing(r, fill_strategies) for r in data]

        # Step 3: Transform types
        schema = {
            'customer_id': 'string',
            'age': 'integer',
            'email': 'string',
            'is_active': 'boolean'
        }
        data = [self.type_transformer.transform_record(r, schema) for r in data]

        # Step 4: Validate
        self.validator.add_rule('customer_id', 'not_null')
        self.validator.add_rule('email', 'not_null')
        self.validator.add_rule('age', 'max_value', {'max': 150})

        valid_data = []
        invalid_data = []
        for record in data:
            results = self.validator.validate(record)
            if all(r.passed for r in results):
                valid_data.append(record)
            else:
                invalid_data.append({'record': record, 'errors': results})

        # Step 5: Enrich
        self.enricher.add_enrichment_source('geo', GeoEnrichment(geo_service))
        enriched_data = [
            self.enricher.enrich_record(r, ['geo'])
            for r in valid_data
        ]

        return {
            'valid_data': enriched_data,
            'invalid_data': invalid_data,
            'metrics': {
                'input_count': len(raw_data),
                'output_count': len(enriched_data),
                'invalid_count': len(invalid_data)
            }
        }
```

### Transaction Enrichment

```python
from datetime import datetime
from typing import Dict, List

class TransactionEnricher:
    def __init__(self):
        self.category_rules = []
        self.risk_rules = []

    def add_category_rule(self, merchant_pattern: str, category: str):
        """Add merchant to category mapping"""
        self.category_rules.append({
            'pattern': merchant_pattern,
            'category': category
        })

    def add_risk_rule(self, condition: callable, risk_level: str):
        """Add risk assessment rule"""
        self.risk_rules.append({
            'condition': condition,
            'risk_level': risk_level
        })

    def enrich_transaction(self, transaction: Dict) -> Dict:
        """Enrich transaction with category and risk"""
        enriched = transaction.copy()

        # Add category
        enriched['category'] = self.categorize(transaction)

        # Add risk level
        enriched['risk_level'] = self.assess_risk(transaction)

        # Add derived fields
        enriched['hour'] = transaction['timestamp'].hour
        enriched['day_of_week'] = transaction['timestamp'].strftime('%A')
        enriched['is_weekend'] = transaction['timestamp'].weekday() >= 5

        return enriched

    def categorize(self, transaction: Dict) -> str:
        """Categorize transaction based on merchant"""
        merchant = transaction.get('merchant', '')

        for rule in self.category_rules:
            if rule['pattern'].lower() in merchant.lower():
                return rule['category']

        return 'uncategorized'

    def assess_risk(self, transaction: Dict) -> str:
        """Assess transaction risk"""
        for rule in self.risk_rules:
            if rule['condition'](transaction):
                return rule['risk_level']

        return 'low'

# Usage
enricher = TransactionEnricher()
enricher.add_category_rule('uber', 'transportation')
enricher.add_category_rule('amazon', 'shopping')
enricher.add_category_rule('netflix', 'entertainment')

enricher.add_risk_rule(
    lambda t: t.get('amount', 0) > 10000,
    'high'
)
enricher.add_risk_rule(
    lambda t: t.get('country') != 'US',
    'medium'
)
```

## Best Practices

### 1. Idempotent Transformations

```python
class IdempotentTransformer:
    def __init__(self):
        self.processed_ids = set()

    def transform(self, record: Dict) -> Dict:
        """Transform idempotently"""
        record_id = record.get('id')

        if record_id in self.processed_ids:
            return record

        transformed = self.apply_transformation(record)
        self.processed_ids.add(record_id)

        return transformed

    def apply_transformation(self, record: Dict) -> Dict:
        """Apply transformation logic"""
        return record
```

### 2. Schema Evolution Handling

```python
class SchemaEvolutionHandler:
    def __init__(self):
        self.schema_versions = {}

    def register_schema(self, version: int, schema: Dict):
        """Register schema version"""
        self.schema_versions[version] = schema

    def transform_with_schema(self, record: Dict, version: int) -> Dict:
        """Transform record using specific schema version"""
        schema = self.schema_versions.get(version, {})
        transformed = {}

        for field, field_type in schema.items():
            if field in record:
                transformed[field] = self.cast_value(record[field], field_type)
            elif 'default' in schema[field]:
                transformed[field] = schema[field]['default']

        return transformed

    def cast_value(self, value, target_type):
        """Cast value to target type"""
        try:
            if target_type == 'string':
                return str(value)
            elif target_type == 'integer':
                return int(value)
            elif target_type == 'float':
                return float(value)
            elif target_type == 'boolean':
                return bool(value)
        except (ValueError, TypeError):
            return None
```

### 3. Performance Optimization

```python
from concurrent.futures import ThreadPoolExecutor, as_completed
from typing import List, Dict

class ParallelTransformer:
    def __init__(self, max_workers: int = 4):
        self.max_workers = max_workers

    def transform_parallel(self, data: List[Dict], transform_func) -> List[Dict]:
        """Transform data in parallel"""
        with ThreadPoolExecutor(max_workers=self.max_workers) as executor:
            futures = {executor.submit(transform_func, record): record for record in data}
            results = []

            for future in as_completed(futures):
                try:
                    result = future.result()
                    results.append(result)
                except Exception as e:
                    print(f"Transformation error: {e}")
                    results.append(None)

        return [r for r in results if r is not None]

    def transform_batch(self, data: List[Dict], transform_func, batch_size: int = 1000) -> List[Dict]:
        """Transform data in batches"""
        results = []
        for i in range(0, len(data), batch_size):
            batch = data[i:i + batch_size]
            transformed_batch = [transform_func(record) for record in batch]
            results.extend(transformed_batch)
        return results
```

### 4. Data Lineage

```python
from datetime import datetime
from typing import Dict, List

class LineageTracker:
    def __init__(self):
        self.lineage_records = []

    def track_transformation(self, input_record: Dict, output_record: Dict,
                            transformation_name: str):
        """Track data lineage"""
        self.lineage_records.append({
            'input_hash': hash(str(sorted(input_record.items()))),
            'output_hash': hash(str(sorted(output_record.items()))),
            'transformation': transformation_name,
            'timestamp': datetime.now(),
            'input_fields': list(input_record.keys()),
            'output_fields': list(output_record.keys())
        })

    def get_lineage(self, output_hash: int) -> List[Dict]:
        """Get lineage for a record"""
        return [
            r for r in self.lineage_records
            if r['output_hash'] == output_hash
        ]
```

## Further Reading

- [ETL Extract](../extract/) - Data extraction patterns
- [ETL Load](../load/) - Loading strategies
- [ETL Tools](../tools/) - ETL tools comparison
- [Data Quality](../../data-lakes/governance/) - Data governance
