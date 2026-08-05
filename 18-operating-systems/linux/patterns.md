# Linux Patterns

## 1. Set -euo pipefail

**Problem:** Scripts silently ignore errors, unset variables, and pipeline failures, leading to unpredictable behavior.

**Solution:** Enable strict error handling at the top of every bash script.

**Implementation:**
```bash
#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

main() {
    local config_file="${1:?Usage: $0 <config-file>}"
    process_config "$config_file"
}

main "$@"
```

`-e` exits on error, `-u` treats unset variables as errors, `-o pipefail` fails on any command in a pipeline.

**When to Use:** Every bash script. Without these flags, scripts silently corrupt data.

**When NOT to Use:** Never. There is no valid reason to omit strict error handling.

---

## 2. Trap Cleanup

**Problem:** Scripts leave temp files, locks, or processes behind when they exit abnormally.

**Solution:** Register a trap handler that cleans up resources on any exit.

**Implementation:**
```bash
#!/usr/bin/env bash
set -euo pipefail

TMPDIR=$(mktemp -d)
LOCKFILE="/var/lock/my-script.lock"

cleanup() {
    rm -rf "$TMPDIR"
    rm -f "$LOCKFILE"
    echo "Cleaned up" >&2
}
trap cleanup EXIT INT TERM

exec 200>"$LOCKFILE"
flock -n 200 || { echo "Already running" >&2; exit 1; }

# Main logic here
process_data > "$TMPDIR/output.json"
```

**When to Use:** Every script that creates temporary resources, acquires locks, or starts background processes.

**When NOT to Use:** Trivial one-liners that create no side effects.

---

## 3. Unix Pipes and Filters

**Problem:** Processing data streams requires writing custom programs for each transformation.

**Solution:** Compose small, single-purpose utilities using pipes.

**Implementation:**
```bash
# Find top 5 processes by memory
ps aux | sort -k4 -rn | head -5

# Extract and count unique error codes from logs
grep "ERROR" /var/log/app.log \
    | awk '{print $5}' \
    | sort | uniq -c | sort -rn

# Monitor log file for specific patterns
tail -f /var/log/app.log \
    | grep --line-buffered "ERROR\|WARN" \
    | while read -r line; do
        echo "$line" | mail -s "Alert" admin@example.com
    done
```

**When to Use:** Ad-hoc data processing, monitoring, log analysis, and text transformation.

**When NOT to Use:** When the pipeline requires complex state management or when performance on large datasets is critical (use dedicated tools).

---

## 4. Daemonization Pattern

**Problem:** Long-running processes need to survive terminal disconnection and run in the background.

**Solution:** Fork, detach from terminal, and optionally use systemd for lifecycle management.

**Implementation:**
```bash
# Simple daemonization
daemonize() {
    if [ -n "${2:-}" ]; then
        "$@" >> "$2" 2>&1 &
        echo $! > "$3"
    else
        "$@" &
        echo $!
    fi
}

# Preferred: systemd unit
# /etc/systemd/system/myapp.service
cat <<'EOF' > /etc/systemd/system/myapp.service
[Unit]
Description=My Application
After=network.target

[Service]
Type=simple
User=appuser
ExecStart=/usr/local/bin/myapp --config /etc/myapp/config.yaml
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable --now myapp
```

**When to Use:** Any long-running service that must survive terminal disconnects and system reboots.

**When NOT to Use:** Batch jobs (use cron or systemd timers instead).

---

## 5. Cron Scheduling

**Problem:** Recurring tasks must run automatically without manual intervention.

**Solution:** Use cron or systemd timers for scheduled execution.

**Implementation:**
```bash
# Cron approach
# m h dom mon dow command
0 2 * * * /usr/local/bin/backup-database.sh >> /var/log/backup.log 2>&1
*/5 * * * * /usr/local/bin/check-health.sh

# Systemd timer approach (preferred for modern systems)
# /etc/systemd/system/backup.timer
[Unit]
Description=Daily backup timer

[Timer]
OnCalendar=*-*-* 02:00:00
Persistent=true

[Install]
WantedBy=timers.target

# /etc/systemd/system/backup.service
[Unit]
Description=Database backup

[Service]
Type=oneshot
ExecStart=/usr/local/bin/backup-database.sh
User=backup
```

**When to Use:** Periodic maintenance, backups, log rotation, and health checks.

**When NOT to Use:** Event-driven tasks or tasks that need complex scheduling (use a job scheduler like Airflow).

---

## 6. Logrotate Configuration

**Problem:** Log files grow unbounded, consuming disk space and degrading performance.

**Solution:** Rotate, compress, and archive logs based on size or time.

**Implementation:**
```bash
# /etc/logrotate.d/myapp
/var/log/myapp/*.log {
    daily
    rotate 14
    compress
    delaycompress
    missingok
    notifempty
    create 0640 appuser appgroup
    sharedscripts
    postrotate
        systemctl reload myapp
    endscript
}
```

**When to Use:** Every service that generates log files.

**When NOT to Use:** When using a centralized logging system that handles retention (e.g., CloudWatch, ELK).

---

## 7. Least Privilege with Sudo

**Problem:** Running everything as root creates catastrophic security risk.

**Solution:** Use sudo with granular permissions and drop privileges for service execution.

**Implementation:**
```bash
# /etc/sudoers.d/appuser
# Allow specific commands without password
appuser ALL=(root) NOPASSWD: /usr/bin/systemctl reload myapp
appuser ALL=(root) NOPASSWD: /usr/bin/journalctl -u myapp
appuser ALL=(backup) NOPASSWD: /usr/local/bin/backup-*.sh

# Service runs as dedicated user
# /etc/systemd/system/myapp.service
[Service]
User=appuser
Group=appgroup
NoNewPrivileges=true
ProtectSystem=strict
ReadWritePaths=/var/lib/myapp /var/log/myapp
PrivateTmp=true
```

**When to Use:** Every service and user account. Default to least privilege.

**When NOT to Use:** Never. Root access should be an exception, never the default.

---

## 8. Idempotent Scripts

**Problem:** Scripts that fail midway leave the system in an inconsistent state and cannot be safely re-run.

**Solution:** Write scripts that check state before making changes and produce the same result on repeated runs.

**Implementation:**
```bash
#!/usr/bin/env bash
set -euo pipefail

# Idempotent user creation
ensure_user() {
    local user="$1"
    if id "$user" &>/dev/null; then
        echo "User $user already exists"
    else
        useradd --system --shell /bin/false "$user"
        echo "Created user $user"
    fi
}

# Idempotent directory creation
ensure_dir() {
    local dir="$1" owner="$2"
    mkdir -p "$dir"
    chown "$owner" "$dir"
}

# Idempotent service setup
ensure_service() {
    local name="$1"
    if systemctl is-enabled "$name" &>/dev/null; then
        echo "Service $name already enabled"
    else
        systemctl enable --now "$name"
        echo "Enabled service $name"
    fi
}

ensure_user "appuser"
ensure_dir "/var/lib/myapp" "appuser:appgroup"
ensure_service "myapp"
```

**When to Use:** Every deployment script, provisioning script, and configuration management task.

**When NOT to Use:** When scripts are truly one-shot and state is managed externally.

---

## 9. FHS Directory Layout

**Problem:** Custom directory structures make backups, packaging, and administration unpredictable.

**Solution:** Follow the Filesystem Hierarchy Standard for predictable file placement.

**Implementation:**
```
/etc/myapp/           # Configuration files
  config.yaml
  ssl/
/var/lib/myapp/       # Mutable state, databases
  data/
/var/log/myapp/       # Log files
  app.log
  error.log
/tmp/myapp/           # Temporary files (cleared on reboot)
/run/myapp/           # Runtime PID files, sockets
/usr/local/bin/myapp  # Application binaries
/usr/share/doc/myapp  # Documentation
```

**When to Use:** Every application deployed on Linux. Consistent layouts simplify operations.

**When NOT to Use:** Container images where FHS is irrelevant and everything is in /app.

---

## 10. Signal Handling

**Problem:** Scripts do not respond gracefully to SIGTERM, leading to abrupt termination and resource leaks.

**Solution:** Trap signals and perform graceful shutdown.

**Implementation:**
```bash
#!/usr/bin/env bash
set -euo pipefail

RUNNING=true

graceful_shutdown() {
    echo "Received signal, shutting down..." >&2
    RUNNING=false
    # Wait for current operation to finish
    wait
    # Clean up
    rm -f /run/myapp.pid
    exit 0
}

trap graceful_shutdown SIGTERM SIGINT SIGHUP

while $RUNNING; do
    process_next_item
done
```

**When to Use:** Every daemon and long-running script.

**When NOT to Use:** Short-lived scripts that complete in seconds and have no external dependencies.

---

## Best Practices

- Always start scripts with `#!/usr/bin/env bash` and `set -euo pipefail`.
- Use `trap cleanup EXIT` to guarantee resource cleanup.
- Write idempotent scripts that can be safely re-run.
- Log to stdout/stderr and let the supervisor handle routing.
- Avoid parsing `ls` output; use globs or `find` instead.
- Quote all variables to prevent word splitting and glob expansion.
- Use `mktemp` for temporary files, never predict file names.
