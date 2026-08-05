# Splunk Patterns

## 1. Universal Forwarder Deployment

**Problem:** Indexing data directly on search heads wastes resources and creates bottlenecks.

**Solution:** Deploy lightweight universal forwarders on data sources that forward to dedicated indexers.

**Implementation:**
```bash
# /opt/splunkforwarder/etc/system/local/outputs.conf
[tcpout:indexers]
server = indexer1:9997,indexer2:9997
compressed = true

[tcpout:indexers:indexer1]
server = indexer1:9997

[tcpout:indexers:indexer2]
server = indexer2:9997

# /opt/splunkforwarder/etc/system/local/inputs.conf
[monitor:///var/log/app/]
sourcetype = app_logs
index = app_prod
disabled = false
```

**When to Use:** Any production deployment with more than one server or more than a few GB of daily data.

**When NOT to Use:** Single-server lab environments where forwarding adds unnecessary complexity.

---

## 2. HTTP Event Collector (HEC)

**Problem:** Application logs require a lightweight, HTTP-based ingestion path without installing forwarders.

**Solution:** Applications send JSON events directly to Splunk via HEC endpoints.

**Implementation:**
```bash
# Enable HEC on Splunk
curl -k -u admin:password \
  https://splunk:8089/services/admin/collected \
  -d mode=enabled

# HEC configuration
# settings > HTTP Event Collector > New Token
```

```python
import requests

HEC_URL = "https://splunk.example.com:8088/services/collector/event"
HEC_TOKEN = "your-hec-token"

def send_to_splunk(event, sourcetype="app_logs", index="app_prod"):
    payload = {
        "event": event,
        "sourcetype": sourcetype,
        "index": index,
        "host": "app-server-1"
    }
    requests.post(HEC_URL, json=payload, headers={
        "Authorization": f"Splunk {HEC_TOKEN}"
    }, verify=True)
```

**When to Use:** Cloud-native applications, containers, or environments where installing a forwarder is impractical.

**When NOT to Use:** High-throughput on-premise environments where forwarders are more efficient.

---

## 3. Index-per-Retention Policy

**Problem:** All data shares one retention policy, forcing compromise between compliance and cost.

**Solution:** Create separate indexes with different retention settings based on data requirements.

**Implementation:**
```bash
# /opt/splunk/etc/apps/search/local/indexes.conf
[app_prod]
homePath.maxDataSizeMB = 20480
coldPath.maxDataSizeMB = 40960
frozenTimePeriodInSecs = 7776000   # 90 days

[security_audit]
homePath.maxDataSizeMB = 5120
coldPath.maxDataSizeMB = 10240
frozenTimePeriodInSecs = 31536000  # 1 year

[compliance_data]
homePath.maxDataSizeMB = 10240
coldPath.maxDataSizeMB = 20480
frozenTimePeriodInSecs = 94608000  # 3 years
```

**When to Use:** When different data types have different compliance, debugging, or cost requirements.

**When NOT to Use:** Small deployments where managing multiple indexes adds overhead without benefit.

---

## 4. Hot/Warm/Cold Tiering

**Problem:** Storing all data on fast storage is expensive; storing everything on slow storage hurts search performance.

**Solution:** Tiered storage moves data through hot (fast SSD), warm (bulk), and cold (archive) based on age.

**Implementation:**
```bash
# /opt/splunk/etc/master-apps/_cluster/local/indexes.conf
[app_prod]
homePath.maxDataSizeMB = 20480
homePath = $SPLUNK_DB/app_prod/db
coldPath = $SPLUNK_DB/app_prod/colddb
coldPath.maxDataSizeMB = 40960
thawedPath = $SPLUNK_DB/app_prod/thaweddb
frozenTimePeriodInSecs = 7776000

# Server class for tiered storage
# /opt/splunk/etc/apps/search/local/serverclass.conf
[serverClass:indexers:appSplunkIndexesAndTransforms]
whitelist = indexer*
```

**When to Use:** Any deployment exceeding 50GB/day where storage cost and search performance must be balanced.

**When NOT to Use:** Small deployments where the operational complexity of tiered storage is not justified.

---

## 5. Summary Indexing

**Problem:** Complex searches over raw data are slow and resource-intensive.

**Solution:** Pre-compute results and store them in summary indexes for fast dashboard queries.

**Implementation:**
```bash
# Create summary index
# indexes.conf
[summary_hourly]
frozenTimePeriodInSecs = 7776000

# Scheduled search to populate summary
# savedsearches.conf
[Hourly Error Summary]
search = index=app_prod sourcetype=app_logs level=ERROR
         | stats count as error_count by host, _time
         | collect index=summary_hourly
schedule = 0 * * * *
```

```spl
# Query summary instead of raw data (much faster)
index=summary_hourly
| timechart span=1h sum(error_count) by host
```

**When to Use:** Dashboards with fixed aggregations that run over large time ranges.

**When NOT to Use:** When real-time accuracy is required or when data volumes are small enough for ad-hoc searches.

---

## 6. Data Models and Acceleration

**Problem:** Pivot tables and dashboards require repeated complex searches that are slow on raw data.

**Solution:** Define data models that Splunk accelerates (pre-computes) for O(1) lookups.

**Implementation:**
```bash
# Settings > Data Models > Create
# Name: Application_Data
# Searches: index=app_logs sourcetype=app_logs

# Enable acceleration
# dataui.conf or via UI
[Application_Data]
acceleration = true
acceleration.earliest_time = -60m@m
acceleration.cron_schedule = */5 * * * *
acceleration.max_concurrent = 2

# Use in search
tstats count FROM datamodel=Application_Data WHERE earliest=-24h
| rename "Application_Data.action" as action
| stats count by action
```

**When to Use:** When Pivot or dashboards need to query large datasets with consistent performance.

**When NOT to Use:** When data models are large and storage for accelerated summaries is constrained.

---

## 7. CIM Normalization

**Problem:** Different sourcetypes use different field names, making cross-source searches difficult.

**Solution:** Map fields to Common Information Model (CIM) standards using field aliases and extractions.

**Implementation:**
```bash
# /opt/splunk/etc/apps/cim_app/local/props.conf
[cisco:ios]
FIELDALIAS-actions = action AS action
FIELDALIAS-status = result AS status
EXTRACT-method = (?i)method=(?<method>\w+)

# /opt/splunk/etc/apps/cim_app/local/transforms.conf
[normalize_cisco_fields]
REGEX = method=(?<src_method>\w+)
FORMAT = src_method::$1

# Search against normalized fields
index=firewall OR index=ids
| where action="blocked"
| stats count by src_ip, dest_ip
```

**When to Use:** When correlating data across multiple vendor sources (firewalls, IDS, proxies, endpoints).

**When NOT to Use:** When all data comes from a single source with consistent field naming.

---

## 8. Scheduled Alerts

**Problem:** Operations teams miss critical conditions without proactive monitoring.

**Solution:** Splunk scheduled searches trigger alerts when thresholds are breached.

**Implementation:**
```bash
# savedsearches.conf
[High Error Rate Alert]
search = index=app_prod sourcetype=app_logs level=ERROR
         | stats count as errors by host
         | where errors > 1000
alert.severity = 3
alert.suppress = 5m
alert.suppress.fields = host
alert.track = 1
counttype = number of events
relation = greater than
quantity = 1000
dispatch.earliest_time = -5m
dispatch.latest_time = now
cron_schedule = */5 * * * *
enableSched = 1

# Alert action
action.email.to = ops@example.com
action.email.subject = Alert: High Error Rate on $name$
action.email.message = $result$
```

**When to Use:** Any condition requiring immediate notification (error spikes, security events, SLA breaches).

**When NOT to Use:** When conditions are better handled by APM tools or when alert volume would cause fatigue.

---

## 9. Dashboard Design

**Problem:** Complex dashboards with too many panels are slow and confusing.

**Solution:** Design dashboards with progressive disclosure: overview panels drill into details.

**Implementation:**
```xml
<dashboard version="1.1" theme="dark">
  <title>Application Health</title>

  <row>
    <panel>
      <title>Error Rate (Last Hour)</title>
      <chart>
        <search>
          <query>index=app_prod level=ERROR
                 | timechart span=5m count</query>
          <earliest>-1h</earliest>
          <latest>now</latest>
        </search>
        <option name="charting.chart">line</option>
      </chart>
    </panel>

    <panel>
      <title>Top Errors by Host</title>
      <table>
        <search>
          <query>index=app_prod level=ERROR
                 | stats count by host
                 | sort -count</query>
        </search>
      </table>
    </panel>
  </row>

  <row>
    <panel>
      <title>Error Details</title>
      <table>
        <search>
          <query>index=app_prod level=ERROR
                 | fields _time, host, message
                 | head 100</query>
        </search>
      </table>
    </panel>
  </row>
</dashboard>
```

**When to Use:** When providing operational visibility to teams with different levels of expertise.

**When NOT to Use:** When data is too complex for a dashboard and users need ad-hoc exploration.

---

## 10. Distributed Deployment Architecture

**Problem:** Single-instance Splunk cannot handle high ingestion or search loads.

**Solution:** Deploy indexers in a cluster, search heads in a search head cluster, and forwarders for ingestion.

**Implementation:**
```
Architecture:
  Universal Forwarders (on app servers)
       |
       v
  Indexer Cluster (3+ nodes, replication factor=2)
       |
       v
  Search Head Cluster (3+ nodes, with deployer)
       |
       v
  Deployment Server (manages forwarder configs)
       |
       v
  Splunk Enterprise (management, alerting)
```

```bash
# Indexer cluster master
# server.conf
[clustering]
mode = manager
storage_factor = 2
pass4SymmKey = cluster_key

# Search head cluster member
# server.conf
[clustering]
mode = searchhead
pass4SymmKey = cluster_key
master_uri = https://master:8089
```

**When to Use:** Ingestion exceeds 50GB/day or search performance is degraded on a single instance.

**When NOT to Use:** Small deployments under 10GB/day where a single instance suffices.

---

## Best Practices

- Use CIM-compatible field names for cross-source correlation.
- Keep scheduled search intervals staggered to avoid resource spikes.
- Use tstats for queries against accelerated data models.
- Set up monitoring console (MC) to track cluster health.
- Use role-based access control to limit data access.
- Archive frozen data to S3 or cold storage before deletion.
- Document data sources, sourcetypes, and index mappings.
