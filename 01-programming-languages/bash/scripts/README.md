# Bash Scripts

30+ practical bash scripts for file operations, text processing, system administration, DevOps, and automation.

## Script Template

```bash
#!/bin/bash
set -euo pipefail

# Script description
# Usage: ./script.sh [options] <arguments>

# Constants
readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly SCRIPT_NAME="$(basename "$0")"
readonly LOG_FILE="/var/log/${SCRIPT_NAME%.sh}.log"

# Functions
usage() {
    cat << EOF
Usage: $SCRIPT_NAME [OPTIONS] <ARGUMENTS>

Description of what this script does.

Options:
    -h, --help      Show this help message
    -v, --verbose   Enable verbose output
    -d, --dry-run   Dry run (don't make changes)
    -f, --file      Input file
    -o, --output    Output file
EOF
}

log() {
    local level=$1
    shift
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] [$level] $*" | tee -a "$LOG_FILE"
}

error() {
    log "ERROR" "$*" >&2
    exit 1
}

cleanup() {
    log "INFO" "Cleaning up..."
    # Remove temp files
    rm -f "${TMPFILE:-}"
}

# Main
main() {
    trap cleanup EXIT
    parse_args "$@"
    # Script logic here
}

parse_args() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help) usage; exit 0 ;;
            -v|--verbose) VERBOSE=true; shift ;;
            -d|--dry-run) DRY_RUN=true; shift ;;
            -f|--file) FILE="$2"; shift 2 ;;
            -o|--output) OUTPUT="$2"; shift 2 ;;
            *) error "Unknown option: $1" ;;
        esac
    done
}

main "$@"
```

---

## File Operations

### 1. Batch File Renamer

```bash
#!/bin/bash
set -euo pipefail

# Rename files with pattern
# Usage: ./rename.sh <pattern> <replacement> <directory>

pattern=$1
replacement=$2
directory=${3:-.}

for file in "$directory"/*; do
    filename=$(basename "$file")
    if [[ $filename == *"$pattern"* ]]; then
        newname="${filename/$pattern/$replacement}"
        mv "$file" "$directory/$newname"
        echo "Renamed: $filename -> $newname"
    fi
done
```

### 2. Find Duplicate Files

```bash
#!/bin/bash
set -euo pipefail

# Find duplicate files by hash
# Usage: ./find_duplicates.sh <directory>

directory=${1:-.}

echo "Scanning $directory for duplicates..."
echo ""

# Create hash list
declare -A file_hashes

while IFS= read -r -d '' file; do
    hash=$(md5sum "$file" | awk '{print $1}')
    file_hashes[$hash]+="$file"$'\n'
done < <(find "$directory" -type f -print0)

# Display duplicates
found=0
for hash in "${!file_hashes[@]}"; do
    files="${file_hashes[$hash]}"
    count=$(echo -n "$files" | grep -c '^' || true)
    
    if [ "$count" -gt 1 ]; then
        found=1
        echo "Duplicate group (hash: ${hash:0:8}):"
        echo "$files" | while read -r f; do
            [ -n "$f" ] && echo "  $f"
        done
        echo ""
    fi
done

if [ $found -eq 0 ]; then
    echo "No duplicates found."
fi
```

### 3. Backup Script

```bash
#!/bin/bash
set -euo pipefail

# Backup files with rotation
# Usage: ./backup.sh <source> <destination>

source_dir=$1
dest_dir=$2
backup_name="backup_$(date +%Y%m%d_%H%M%S)"
max_backups=5

# Create backup
mkdir -p "$dest_dir"
tar -czf "$dest_dir/$backup_name.tar.gz" "$source_dir"
echo "Created backup: $backup_name"

# Rotate old backups
cd "$dest_dir"
ls -t backup_*.tar.gz 2>/dev/null | tail -n +$((max_backups + 1)) | xargs rm -f
echo "Rotated to keep $max_backups backups"
```

### 4. Sync Directories

```bash
#!/bin/bash
set -euo pipefail

# Sync directories with rsync
# Usage: ./sync.sh <source> <destination> [--dry-run]

source=$1
destination=$2
dry_run=${3:-}

rsync_opts="-av --delete"
[ "$dry_run" = "--dry-run" ] && rsync_opts+=" --dry-run"

rsync $rsync_opts "$source/" "$destination/"
echo "Sync complete: $source -> $destination"
```

### 5. File Watcher

```bash
#!/bin/bash
set -euo pipefail

# Watch for file changes
# Usage: ./watch.sh <directory> <command>

directory=$1
shift
command="$@"

echo "Watching $directory for changes..."

while IFS= read -r -d '' file; do
    echo "Changed: $file"
    eval "$command"
done < <(find "$directory" -type f -print0 | inotifywait -m -r -e modify,create,delete --format '%w%f' -)
```

---

## Text Processing

### 6. Log Analyzer

```bash
#!/bin/bash
set -euo pipefail

# Analyze log file
# Usage: ./log_analyzer.sh <logfile>

logfile=$1

echo "=== Log Analysis for $logfile ==="
echo ""

echo "Total lines: $(wc -l < "$logfile")"
echo ""

echo "=== Status Code Distribution ==="
grep -oP 'HTTP/\d\.\d" \K[0-9]+' "$logfile" | sort | uniq -c | sort -rn
echo ""

echo "=== Top 10 IP Addresses ==="
grep -oP '\d+\.\d+\.\d+\.\d+' "$logfile" | sort | uniq -c | sort -rn | head -10
echo ""

echo "=== Request Methods ==="
grep -oP '(GET|POST|PUT|DELETE|PATCH) ' "$logfile" | sort | uniq -c | sort -rn
echo ""

echo "=== Error Count ==="
grep -c ' 5[0-9][0-9] ' "$logfile" || echo "0 errors found"
```

### 7. CSV Processor

```bash
#!/bin/bash
set -euo pipefail

# Process CSV files
# Usage: ./csv_processor.sh <file> <column> <operation>

file=$1
column=$2
operation=$3

case $operation in
    sum)
        awk -F',' -v col=$column 'NR>1 {sum+=$col} END {print "Sum:", sum}'
        ;;
    avg)
        awk -F',' -v col=$column 'NR>1 {sum+=$col; count++} END {print "Average:", sum/count}'
        ;;
    min)
        awk -F',' -v col=$column 'NR>1 {if(min==""||$col<min) min=$col} END {print "Min:", min}'
        ;;
    max)
        awk -F',' -v col=$column 'NR>1 {if(max==""||$col>max) max=$col} END {print "Max:", max}'
        ;;
    count)
        awk -F',' 'NR>1 {count++} END {print "Count:", count}'
        ;;
    *)
        echo "Unknown operation: $operation"
        echo "Available: sum, avg, min, max, count"
        exit 1
        ;;
esac < "$file"
```

### 8. Text Substitution

```bash
#!/bin/bash
set -euo pipefail

# Replace text in files
# Usage: ./replace.sh <pattern> <replacement> <files...>

pattern=$1
replacement=$2
shift 2

for file in "$@"; do
    if [ -f "$file" ]; then
        sed -i.bak "s|$pattern|$replacement|g" "$file"
        rm -f "$file.bak"
        echo "Updated: $file"
    fi
done
```

### 9. Word Frequency Counter

```bash
#!/bin/bash
set -euo pipefail

# Count word frequency in text
# Usage: ./word_freq.sh <file> [top_n]

file=${1:?'Usage: word_freq.sh <file> [top_n]'}
top_n=${2:-20}

echo "Top $top_n words in $file:"
echo ""

tr -s '[:space:]' '\n' < "$file" |
    tr -d '[:punct:]' |
    tr '[:upper:]' '[:lower:]' |
    grep -v '^$' |
    sort |
    uniq -c |
    sort -rn |
    head -n "$top_n"
```

### 10. Markdown to HTML

```bash
#!/bin/bash
set -euo pipefail

# Convert markdown to HTML
# Usage: ./md2html.sh <input.md> [output.html]

input=${1:?'Usage: md2html.sh <input.md> [output.html]'}
output=${2:-${input%.md}.html}

if ! command -v pandoc &> /dev/null; then
    echo "pandoc not found. Installing..."
    brew install pandoc  # or apt-get install pandoc
fi

pandoc "$input" -o "$output" --standalone
echo "Converted: $input -> $output"
```

---

## System Administration

### 11. System Monitor

```bash
#!/bin/bash
set -euo pipefail

# Display system information
# Usage: ./sysinfo.sh

echo "=== System Information ==="
echo "Hostname: $(hostname)"
echo "OS: $(uname -s) $(uname -r)"
echo "Uptime: $(uptime | awk -F'up ' '{print $2}' | awk -F',' '{print $1}')"
echo ""

echo "=== CPU ==="
echo "Model: $(sysctl -n machdep.cpu.brand_string 2>/dev/null || grep 'model name' /proc/cpuinfo | head -1 | cut -d: -f2)"
echo "Cores: $(nproc)"
echo "Load: $(uptime | awk -F'load average:' '{print $2}')"
echo ""

echo "=== Memory ==="
free -h 2>/dev/null || vm_stat 2>/dev/null || echo "Memory info unavailable"
echo ""

echo "=== Disk Usage ==="
df -h / | tail -1 | awk '{print "Used:", $3, "/ Total:", $2, "(", $5, ")"}'
echo ""

echo "=== Top Processes ==="
ps aux --sort=-%cpu 2>/dev/null | head -6 || ps aux | head -6
```

### 12. Disk Usage Reporter

```bash
#!/bin/bash
set -euo pipefail

# Report disk usage by directory
# Usage: ./disk_usage.sh <directory> [depth]

directory=${1:-.}
depth=${2:-2}

echo "Disk usage for $directory (depth: $depth):"
echo ""

du -h --max-depth="$depth" "$directory" 2>/dev/null | sort -hr | head -20

echo ""
echo "Total: $(du -sh "$directory" 2>/dev/null | cut -f1)"
```

### 13. Service Monitor

```bash
#!/bin/bash
set -euo pipefail

# Monitor a service and restart if down
# Usage: ./monitor.sh <service_name> [check_interval]

service=$1
interval=${2:-60}

while true; do
    if ! systemctl is-active "$service" &>/dev/null; then
        echo "[$(date)] Service $service is down. Restarting..."
        sudo systemctl restart "$service"
        echo "[$(date)] Service $service restarted."
    fi
    sleep "$interval"
done
```

### 14. User Management

```bash
#!/bin/bash
set -euo pipefail

# Create user with home directory
# Usage: ./add_user.sh <username> [shell]

username=$1
shell=${2:-/bin/bash}

if id "$username" &>/dev/null; then
    echo "User $username already exists"
    exit 1
fi

sudo useradd -m -s "$shell" "$username"
echo "User $username created"
echo "Set password with: passwd $username"
```

### 15. Log Rotation

```bash
#!/bin/bash
set -euo pipefail

# Rotate and compress log files
# Usage: ./rotate_logs.sh <log_dir> [max_days]

log_dir=${1:?'Usage: rotate_logs.sh <log_dir> [max_days]'}
max_days=${2:-30}

find "$log_dir" -name "*.log" -mtime +$max_days -exec gzip {} \;
find "$log_dir" -name "*.gz" -mtime +$((max_days * 2)) -delete

echo "Log rotation complete in $log_dir"
```

### 16. Backup Database

```bash
#!/bin/bash
set -euo pipefail

# Backup MySQL/PostgreSQL database
# Usage: ./backup_db.sh <database> <backup_dir>

database=$1
backup_dir=${2:-/var/backups/db}
timestamp=$(date +%Y%m%d_%H%M%S)
backup_file="$backup_dir/${database}_${timestamp}.sql.gz"

mkdir -p "$backup_dir"

# Detect database type
if command -v mysqldump &>/dev/null; then
    mysqldump "$database" | gzip > "$backup_file"
elif command -v pg_dump &>/dev/null; then
    pg_dump "$database" | gzip > "$backup_file"
else
    echo "No database dump tool found"
    exit 1
fi

echo "Backup created: $backup_file"
echo "Size: $(du -sh "$backup_file" | cut -f1)"
```

---

## DevOps Automation

### 17. Docker Cleanup

```bash
#!/bin/bash
set -euo pipefail

# Clean up Docker resources
# Usage: ./docker_cleanup.sh [--aggressive]

aggressive=${1:-}

echo "=== Docker Cleanup ==="

echo "Removing stopped containers..."
docker container prune -f

echo "Removing dangling images..."
docker image prune -f

echo "Removing unused volumes..."
docker volume prune -f

echo "Removing unused networks..."
docker network prune -f

if [ "$aggressive" = "--aggressive" ]; then
    echo "Removing ALL unused images..."
    docker image prune -a -f
fi

echo "=== Cleanup Complete ==="
docker system df
```

### 18. Kubernetes Pod Cleaner

```bash
#!/bin/bash
set -euo pipefail

# Clean up failed pods in Kubernetes
# Usage: ./k8s_cleanup.sh [namespace]

namespace=${1:-default}

echo "Cleaning up failed pods in namespace: $namespace"

kubectl get pods -n "$namespace" --field-selector=status.phase=Failed -o name | \
    xargs -r kubectl delete -n "$namespace"

echo "Cleanup complete"
```

### 19. Deploy Script

```bash
#!/bin/bash
set -euo pipefail

# Deploy application
# Usage: ./deploy.sh <environment> <version>

environment=$1
version=$2

echo "Deploying version $version to $environment..."

case $environment in
    staging)
        docker-compose -f docker-compose.staging.yml up -d
        ;;
    production)
        docker-compose -f docker-compose.prod.yml up -d
        ;;
    *)
        echo "Unknown environment: $environment"
        exit 1
        ;;
esac

echo "Deployment complete"
```

### 20. SSL Certificate Checker

```bash
#!/bin/bash
set -euo pipefail

# Check SSL certificate expiration
# Usage: ./ssl_check.sh <domain> [warn_days]

domain=$1
warn_days=${2:-30}

expiry_date=$(echo | openssl s_client -servername "$domain" -connect "$domain":443 2>/dev/null | \
    openssl x509 -noout -enddate 2>/dev/null | cut -d= -f2)

if [ -z "$expiry_date" ]; then
    echo "Could not retrieve certificate for $domain"
    exit 1
fi

expiry_epoch=$(date -d "$expiry_date" +%s 2>/dev/null || date -j -f "%b %d %T %Y %Z" "$expiry_date" +%s)
current_epoch=$(date +%s)
days_left=$(( (expiry_epoch - current_epoch) / 86400 ))

echo "Certificate for $domain:"
echo "  Expires: $expiry_date"
echo "  Days left: $days_left"

if [ $days_left -lt $warn_days ]; then
    echo "  WARNING: Certificate expires in less than $warn_days days!"
    exit 1
fi
```

### 21. Git Repository Cleanup

```bash
#!/bin/bash
set -euo pipefail

# Clean up git repository
# Usage: ./git_cleanup.sh [directory]

directory=${1:-.}

cd "$directory"

echo "=== Git Cleanup ==="

echo "Pruning remote tracking branches..."
git fetch --prune

echo "Cleaning up local branches merged into main..."
git branch --merged main | grep -v 'main\|master' | xargs -r git branch -d

echo "Removing unused git objects..."
git gc --prune=now

echo "=== Cleanup Complete ==="
```

### 22. Infrastructure Provisioner

```bash
#!/bin/bash
set -euo pipefail

# Provision infrastructure using Terraform
# Usage: ./provision.sh <environment> <action>

environment=$1
action=$2

cd "terraform/$environment"

case $action in
    plan)
        terraform plan -out=tfplan
        ;;
    apply)
        terraform apply tfplan
        ;;
    destroy)
        terraform destroy -auto-approve
        ;;
    *)
        echo "Unknown action: $action"
        exit 1
        ;;
esac
```

### 23. Environment Setup

```bash
#!/bin/bash
set -euo pipefail

# Setup development environment
# Usage: ./setup_env.sh

echo "=== Development Environment Setup ==="

# Check dependencies
for cmd in git docker node npm; do
    if ! command -v "$cmd" &>/dev/null; then
        echo "Installing $cmd..."
        case $cmd in
            git) brew install git ;;
            docker) brew install --cask docker ;;
            node) brew install node ;;
            npm) npm install -g npm ;;
        esac
    fi
done

# Clone repositories
if [ ! -d "project" ]; then
    git clone https://github.com/user/project.git
fi

# Setup project
cd project
npm install
cp .env.example .env

echo "=== Setup Complete ==="
```

---

## Data Processing

### 24. JSON Processor

```bash
#!/bin/bash
set -euo pipefail

# Process JSON files with jq
# Usage: ./json_processor.sh <file> <jq_expression>

file=$1
expression=$2

if ! command -v jq &>/dev/null; then
    echo "jq not found. Installing..."
    brew install jq
fi

jq "$expression" "$file"
```

### 25. Log Aggregator

```bash
#!/bin/bash
set -euo pipefail

# Aggregate logs from multiple sources
# Usage: ./aggregate_logs.sh <output> <sources...>

output=$1
shift

echo "Aggregating logs..."

for source in "$@"; do
    if [ -f "$source" ]; then
        echo "--- $source ---" >> "$output"
        cat "$source" >> "$output"
    elif [ -d "$source" ]; then
        echo "--- $source ---" >> "$output"
        cat "$source"/*.log >> "$output" 2>/dev/null || true
    fi
done

sort -o "$output" "$output"
echo "Aggregated to: $output"
```

### 26. Data Converter

```bash
#!/bin/bash
set -euo pipefail

# Convert between data formats
# Usage: ./convert.sh <input_format> <output_format> <file>

input_format=$1
output_format=$2
file=$3

case "${input_format}_${output_format}" in
    csv_json)
        head -1 "$file" | awk -F',' '{for(i=1;i<=NF;i++) printf "\"%s\",", $i}' | sed 's/,$/\n/'
        ;;
    json_csv)
        jq -r '.[] | [.key1, .key2] | @csv' "$file"
        ;;
    yaml_json)
        if command -v python3 &>/dev/null; then
            python3 -c "import yaml, json, sys; print(json.dumps(yaml.safe_load(sys.stdin)))" < "$file"
        fi
        ;;
    *)
        echo "Unsupported conversion: ${input_format} -> ${output_format}"
        exit 1
        ;;
esac
```

---

## Monitoring and Alerting

### 27. Website Monitor

```bash
#!/bin/bash
set -euo pipefail

# Monitor website availability
# Usage: ./website_monitor.sh <url> [interval] [timeout]

url=$1
interval=${2:-60}
timeout=${3:-10}

echo "Monitoring $url every $interval seconds..."

while true; do
    status=$(curl -o /dev/null -s -w "%{http_code}" --max-time "$timeout" "$url")
    
    if [ "$status" -ge 200 ] && [ "$status" -lt 400 ]; then
        echo "[$(date)] OK - Status: $status"
    else
        echo "[$(date)] ALERT - Status: $status"
        # Add notification here (email, Slack, etc.)
    fi
    
    sleep "$interval"
done
```

### 28. Performance Monitor

```bash
#!/bin/bash
set -euo pipefail

# Monitor system performance
# Usage: ./perf_monitor.sh [interval] [count]

interval=${1:-5}
count=${2:-60}

echo "timestamp,cpu_percent,mem_percent,disk_io"

for ((i=0; i<count; i++)); do
    cpu=$(top -bn1 | grep "Cpu(s)" | awk '{print $2}')
    mem=$(free | awk 'NR==2{printf "%.2f", $3*100/$2}')
    disk=$(iostat -d 1 1 | awk '/^[a-z]/{print $4}' | tail -1)
    
    echo "$(date +%H:%M:%S),$cpu,$mem,$disk"
    sleep "$interval"
done
```

### 29. Alert Script

```bash
#!/bin/bash
set -euo pipefail

# Send alerts via email/Slack
# Usage: ./alert.sh <severity> <message>

severity=$1
message=$2
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

# Email alert
if [ -n "${ALERT_EMAIL:-}" ]; then
    echo "$timestamp [$severity] $message" | mail -s "Alert: $severity" "$ALERT_EMAIL"
fi

# Slack alert
if [ -n "${SLACK_WEBHOOK:-}" ]; then
    curl -X POST -H 'Content-type: application/json' \
        --data "{\"text\":\"[$severity] $message\"}" \
        "$SLACK_WEBHOOK"
fi

echo "Alert sent: [$severity] $message"
```

---

## Backup and Recovery

### 30. Automated Backup

```bash
#!/bin/bash
set -euo pipefail

# Automated backup with retention
# Usage: ./auto_backup.sh <config_file>

config=${1:?'Usage: auto_backup.sh <config_file>'}

# Source config
source "$config"

# Backup function
do_backup() {
    local source=$1
    local dest=$2
    local name=$3
    
    timestamp=$(date +%Y%m%d_%H%M%S)
    backup_file="$dest/${name}_${timestamp}.tar.gz"
    
    mkdir -p "$dest"
    tar -czf "$backup_file" "$source"
    
    echo "Created: $backup_file"
}

# Cleanup old backups
cleanup_backups() {
    local dir=$1
    local keep=$2
    
    find "$dir" -name "*.tar.gz" -mtime +$keep -delete
}

# Main
for backup in "${BACKUPS[@]}"; do
    IFS='|' read -r source dest name retain <<< "$backup"
    do_backup "$source" "$dest" "$name"
    cleanup_backups "$dest" "$retain"
done
```

### 31. Disaster Recovery Script

```bash
#!/bin/bash
set -euo pipefail

# Restore from backup
# Usage: ./restore.sh <backup_file> <restore_location>

backup_file=$1
restore_location=$2

if [ ! -f "$backup_file" ]; then
    echo "Backup file not found: $backup_file"
    exit 1
fi

echo "Restoring from $backup_file..."
mkdir -p "$restore_location"
tar -xzf "$backup_file" -C "$restore_location"
echo "Restored to: $restore_location"
```

### 32. Database Migration Runner

```bash
#!/bin/bash
set -euo pipefail

# Run database migrations
# Usage: ./migrate.sh <direction> [steps]

direction=${1:?'Usage: migrate.sh <up|down> [steps]'}
steps=${2:-1}

migrations_dir="migrations"

case $direction in
    up)
        for ((i=1; i<=steps; i++)); do
            migration=$(ls "$migrations_dir"/*.up.sql 2>/dev/null | head -1)
            if [ -z "$migration" ]; then
                echo "No more migrations to apply"
                break
            fi
            echo "Applying: $(basename "$migration")"
            psql -f "$migration"
            mv "$migration" "${migration%.up.sql}.applied"
        done
        ;;
    down)
        for ((i=1; i<=steps; i++)); do
            migration=$(ls "$migrations_dir"/*.applied 2>/dev/null | tail -1)
            if [ -z "$migration" ]; then
                echo "No more migrations to rollback"
                break
            fi
            echo "Rolling back: $(basename "$migration")"
            rollback="${migration%.applied}.down.sql"
            psql -f "$rollback"
            mv "$migration" "${migration%.applied}.up.sql"
        done
        ;;
    *)
        echo "Unknown direction: $direction"
        exit 1
        ;;
esac

echo "Migration complete"
```

---

## Quick Reference

| Category | Script | Purpose |
|----------|--------|---------|
| File Ops | rename.sh | Batch rename files |
| File Ops | find_duplicates.sh | Find duplicate files |
| File Ops | backup.sh | Backup with rotation |
| Text | log_analyzer.sh | Analyze log files |
| Text | csv_processor.sh | Process CSV data |
| System | sysinfo.sh | System information |
| System | monitor.sh | Monitor services |
| DevOps | docker_cleanup.sh | Clean Docker resources |
| DevOps | deploy.sh | Deploy applications |
| Data | json_processor.sh | Process JSON |
| Monitor | website_monitor.sh | Monitor websites |
| Backup | auto_backup.sh | Automated backups |

---

## Best Practices

1. **Always use `set -euo pipefail`** at the top
2. **Quote all variables** to prevent word splitting
3. **Use functions** to organize code
4. **Add usage/help** messages
5. **Log important actions** for debugging
6. **Handle errors gracefully** with traps
7. **Validate input** before processing
8. **Use meaningful variable names**
9. **Comment complex logic**
10. **Test scripts** with `shellcheck`
