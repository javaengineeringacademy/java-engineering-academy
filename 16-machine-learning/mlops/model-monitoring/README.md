# Model Monitoring

## Overview

Model monitoring tracks deployed model performance, data drift, and system health in production.

## Data Drift Detection

### Implementation

```python
import numpy as np
from scipy import stats
from sklearn.preprocessing import StandardScaler

def detect_drift(reference_data, current_data, threshold=0.05):
    """Detect data drift using KS test"""
    drift_detected = {}
    
    for col in reference_data.columns:
        stat, p_value = stats.ks_2samp(reference_data[col], current_data[col])
        drift_detected[col] = {
            'statistic': stat,
            'p_value': p_value,
            'drifted': p_value < threshold
        }
    
    return drift_detected

# Usage
import pandas as pd

reference = pd.DataFrame({
    'feature1': np.random.normal(0, 1, 1000),
    'feature2': np.random.normal(5, 2, 1000)
})

current = pd.DataFrame({
    'feature1': np.random.normal(0.5, 1.2, 1000),  # Drifted
    'feature2': np.random.normal(5, 2, 1000)  # Not drifted
})

drift_results = detect_drift(reference, current)
for col, result in drift_results.items():
    print(f"{col}: drifted={result['drifted']}, p={result['p_value']:.4f}")
```

---

## Performance Monitoring

### Metrics Tracking

```python
import time
from datetime import datetime
from collections import deque

class PerformanceMonitor:
    def __init__(self, window_size=1000):
        self.predictions = deque(maxlen=window_size)
        self.latencies = deque(maxlen=window_size)
        self.errors = deque(maxlen=window_size)
    
    def log_prediction(self, prediction, true_label=None, latency=None):
        self.predictions.append(prediction)
        if latency:
            self.latencies.append(latency)
        if true_label is not None:
            self.errors.append(int(prediction != true_label))
    
    def get_metrics(self):
        metrics = {
            'avg_latency': np.mean(self.latencies) if self.latencies else 0,
            'p95_latency': np.percentile(self.latencies, 95) if self.latencies else 0,
            'error_rate': np.mean(self.errors) if self.errors else 0,
            'throughput': len(self.predictions) / max(sum(self.latencies), 0.001)
        }
        return metrics

# Usage
monitor = PerformanceMonitor()

# Log predictions
start = time.time()
prediction = model.predict(X)
latency = time.time() - start
monitor.log_prediction(prediction, true_label=y_true, latency=latency)

# Get metrics
metrics = monitor.get_metrics()
print(metrics)
```

---

## Bias Detection

### Implementation

```python
def calculate_fairness_metrics(y_true, y_pred, sensitive_feature):
    """Calculate fairness metrics"""
    groups = sensitive_feature.unique()
    metrics = {}
    
    for group in groups:
        mask = sensitive_feature == group
        group_true = y_true[mask]
        group_pred = y_pred[mask]
        
        metrics[group] = {
            'accuracy': np.mean(group_pred == group_true),
            'positive_rate': np.mean(group_pred == 1),
            'true_positive_rate': np.mean(group_pred[group_true == 1] == 1),
            'false_positive_rate': np.mean(group_pred[group_true == 0] == 1)
        }
    
    # Disparate impact ratio
    if len(groups) == 2:
        group1, group2 = groups
        disparate_impact = (
            metrics[group1]['positive_rate'] / 
            metrics[group2]['positive_rate']
        )
        metrics['disparate_impact'] = disparate_impact
    
    return metrics
```

---

## Alerting System

```python
class AlertSystem:
    def __init__(self):
        self.alerts = []
        self.thresholds = {
            'error_rate': 0.05,
            'p95_latency': 1.0,
            'drift_score': 0.1
        }
    
    def check_alerts(self, metrics):
        new_alerts = []
        
        if metrics.get('error_rate', 0) > self.thresholds['error_rate']:
            new_alerts.append({
                'type': 'HIGH_ERROR_RATE',
                'value': metrics['error_rate'],
                'threshold': self.thresholds['error_rate']
            })
        
        if metrics.get('p95_latency', 0) > self.thresholds['p95_latency']:
            new_alerts.append({
                'type': 'HIGH_LATENCY',
                'value': metrics['p95_latency'],
                'threshold': self.thresholds['p95_latency']
            })
        
        self.alerts.extend(new_alerts)
        return new_alerts
    
    def send_notification(self, alert):
        # Send email, Slack, PagerDuty, etc.
        print(f"ALERT: {alert['type']} - Value: {alert['value']:.4f}")
```

---

## Dashboard

```python
import dash
from dash import dcc, html
import plotly.graph_objs as go

app = dash.Dash(__name__)

app.layout = html.Div([
    html.H1("Model Monitoring Dashboard"),
    dcc.Graph(id='latency-graph'),
    dcc.Graph(id='error-graph'),
    dcc.Interval(id='interval', interval=5000)
])

@app.callback(
    [dash.Output('latency-graph', 'figure'),
     dash.Output('error-graph', 'figure')],
    [dash.Input('interval', 'n_intervals')]
)
def update_graphs(n):
    metrics = monitor.get_metrics()
    
    latency_fig = go.Figure(data=[go.Scatter(
        y=[metrics['avg_latency']],
        mode='markers',
        marker=dict(size=20)
    )])
    
    error_fig = go.Figure(data=[go.Bar(
        x=['Error Rate'],
        y=[metrics['error_rate']]
    )])
    
    return latency_fig, error_fig
```

---

## Best Practices

1. **Monitor inputs**: Track data distributions
2. **Monitor outputs**: Track predictions and latency
3. **Set alerts**: Automated notifications
4. **Retrain triggers**: Automate retraining
5. **A/B testing**: Compare model versions

## Further Reading

- "Machine Learning Engineering" by Andriy Burkov
- Evidently AI documentation
- WhyLabs documentation
