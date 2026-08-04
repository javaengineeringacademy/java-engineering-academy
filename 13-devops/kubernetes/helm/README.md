# Kubernetes Helm

## Overview

Helm is a package manager for Kubernetes that simplifies application deployment and management through charts.

## Basic Concepts

### Chart Structure
```
my-chart/
├── Chart.yaml
├── values.yaml
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── ingress.yaml
│   └── _helpers.tpl
└── README.md
```

### Chart.yaml
```yaml
apiVersion: v2
name: my-app
description: A Helm chart for my application
type: application
version: 0.1.0
appVersion: "1.0.0"
dependencies:
- name: postgresql
  version: "12.0.0"
  repository: "https://charts.bitnami.com/bitnami"
  condition: postgresql.enabled
```

### values.yaml
```yaml
replicaCount: 3

image:
  repository: my-app
  pullPolicy: IfNotPresent
  tag: "1.0.0"

service:
  type: ClusterIP
  port: 80

ingress:
  enabled: true
  className: nginx
  hosts:
  - host: myapp.example.com
    paths:
    - path: /
      pathType: Prefix

resources:
  limits:
    cpu: 500m
    memory: 256Mi
  requests:
    cpu: 250m
    memory: 128Mi
```

## Helm Commands

```bash
# Install chart
helm install my-release ./my-chart

# Upgrade release
helm upgrade my-release ./my-chart --set image.tag=2.0

# Rollback release
helm rollback my-release 1

# Uninstall release
helm uninstall my-release

# List releases
helm list

# Search charts
helm search repo bitnami

# Add repository
helm repo add bitnami https://charts.bitnami.com/bitnami

# Update repositories
helm repo update
```

## Templates

### Deployment Template
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "my-chart.fullname" . }}
  labels:
    {{- include "my-chart.labels" . | nindent 4 }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      {{- include "my-chart.selectorLabels" . | nindent 6 }}
  template:
    metadata:
      labels:
        {{- include "my-chart.selectorLabels" . | nindent 8 }}
    spec:
      containers:
      - name: {{ .Chart.Name }}
        image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
        ports:
        - containerPort: {{ .Values.service.port }}
```

### Helpers Template
```yaml
{{/*
Expand the name of the chart.
*/}}
{{- define "my-chart.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "my-chart.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "my-chart.labels" -}}
helm.sh/chart: {{ include "my-chart.chart" . }}
{{ include "my-chart.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
```

## Best Practices

1. **Use semantic versioning** - Follow semver for charts
2. **Template reusable components** - Use helpers and partials
3. **Default values** - Provide sensible defaults
4. **Document charts** - Add README and values documentation
5. **Test charts** - Use helm test
6. **Use chart repositories** - Publish and share charts
7. **Version charts** - Track changes with Chart.yaml
8. **Use dependencies** - Leverage existing charts
9. **Implement hooks** - Use pre/post install hooks
10. **Monitor releases** - Track deployment status
