# Bash Projects

Automation tools, CLI applications, and system utilities to practice bash scripting.

## Project Ideas

### 1. File Management Tool

A command-line tool for advanced file operations.

**Features:**
- Batch rename with regex patterns
- Find duplicate files by hash
- Organize files by type/date
- Monitor directory for changes
- Generate file reports

**Implementation:**
```bash
#!/bin/bash
set -euo pipefail

# Main menu
show_menu() {
    cat << EOF
File Management Tool
====================
1. Rename files
2. Find duplicates
3. Organize by type
4. Monitor directory
5. Generate report
6. Exit
EOF
}

# Rename with pattern
rename_files() {
    local dir=$1 pattern=$2 replacement=$3
    for file in "$dir"/*"$pattern"*; do
        [ -e "$file" ] || continue
        local newname="${file/$pattern/$replacement}"
        mv "$file" "$newname"
        echo "Renamed: $(basename "$file") -> $(basename "$newname")"
    done
}

# Find duplicates
find_duplicates() {
    local dir=$1
    declare -A hashes
    while IFS= read -r -d '' file; do
        local hash=$(md5sum "$file" | awk '{print $1}')
        hashes[$hash]+="$file"$'\n'
    done < <(find "$dir" -type f -print0)
    
    for hash in "${!hashes[@]}"; do
        local count=$(echo -n "${hashes[$hash]}" | grep -c '^')
        [ "$count" -gt 1 ] && echo "Duplicates:" && echo "${hashes[$hash]}"
    done
}
```

---

### 2. System Information Dashboard

Display system information in a formatted dashboard.

**Features:**
- CPU, memory, disk usage
- Top processes
- Network statistics
- Docker container status
- Service health checks

**Example Output:**
```
╔══════════════════════════════════════╗
║       System Information            ║
╠══════════════════════════════════════╣
║ CPU:    ████████░░ 80%              ║
║ Memory: ██████░░░░ 60%              ║
║ Disk:   ████░░░░░░ 40%              ║
╚══════════════════════════════════════╝
```

---

### 3. Log Analyzer and Reporter

Parse and analyze log files for insights.

**Features:**
- Parse Apache/Nginx logs
- Generate traffic reports
- Identify error patterns
- Create visualizations (ASCII charts)
- Export to CSV/JSON

**Usage:**
```bash
./log_analyzer.sh access.log
./log_analyzer.sh --from "2024-01-01" --to "2024-01-31" access.log
./log_analyzer.sh --top-ips 10 access.log
```

---

### 4. Backup and Sync Tool

Automated backup with multiple destinations.

**Features:**
- Local and remote backups
- Incremental backups
- Compression and encryption
- Schedule with cron
- Restore from any backup

**Configuration:**
```yaml
# backup.conf
source: /home/user
destinations:
  - /mnt/backup
  - s3://my-backup-bucket
retention:
  daily: 7
  weekly: 4
  monthly: 12
compress: true
encrypt: true
```

---

### 5. Process Monitor

Monitor and manage system processes.

**Features:**
- View running processes
- Kill runaway processes
- Set resource limits
- Alert on high usage
- Process tree visualization

**Commands:**
```bash
./proc_monitor.sh list
./proc_monitor.sh kill <pattern>
./proc_monitor.sh limit <pid> <cpu> <memory>
./proc_monitor.sh watch <interval>
```

---

### 6. Network Scanner

Scan and report on network devices.

**Features:**
- Port scanning
- Service detection
- Device discovery
- Network mapping
- Vulnerability checks

**Usage:**
```bash
./net_scanner.sh scan 192.168.1.0/24
./net_scanner.sh ports 192.168.1.1 1-1000
./net_scanner.sh services 192.168.1.1
```

---

### 7. Configuration Manager

Manage dotfiles and system configurations.

**Features:**
- Track dotfiles with git
- Deploy to multiple machines
- Backup existing configs
- Symlink management
- Template support

**Structure:**
```
dotfiles/
├── bash/
│   ├── .bashrc
│   └── .bash_aliases
├── vim/
│   └── .vimrc
├── git/
│   └── .gitconfig
├── install.sh
└── uninstall.sh
```

---

### 8. Git Workflow Automation

Automate common git workflows.

**Features:**
- Branch management
- Release automation
- Code review helpers
- Commit message validation
- PR creation

**Commands:**
```bash
./gitflow.sh feature start <name>
./gitflow.sh feature finish <name>
./gitflow.sh release start <version>
./gitflow.sh hotfix start <issue>
```

---

### 9. Docker Environment Manager

Manage Docker development environments.

**Features:**
- Multi-container environments
- Environment snapshots
- Service templates
- Health checks
- Log aggregation

**Example:**
```bash
./docker_mgr.sh create myapp
./docker_mgr.sh start myapp
./docker_mgr.sh logs myapp
./docker_mgr.sh snapshot myapp
./docker_mgr.sh restore myapp <snapshot>
```

---

### 10. CLI Password Manager

Simple password management from the terminal.

**Features:**
- Generate secure passwords
- Store credentials encrypted
- Search and retrieve
- Clipboard integration
- Import/export

**Commands:**
```bash
./passmgr.sh add github
./passmgr.sh get github
./passmgr.sh generate --length 20
./passmgr.sh list
./passmgr.sh export > backup.enc
```

---

## Project Structure Template

```
project/
├── README.md
├── LICENSE
├── bin/
│   └── main.sh
├── lib/
│   ├── common.sh
│   ├── config.sh
│   └── utils.sh
├── config/
│   └── default.conf
├── tests/
│   ├── test_common.sh
│   └── test_utils.sh
├── docs/
│   └── usage.md
└── Makefile
```

## Development Guidelines

### Testing

```bash
#!/bin/bash
# tests/test_utils.sh

source lib/utils.sh

# Test framework
assert_eq() {
    local expected=$1 actual=$2 message=$3
    if [ "$expected" != "$actual" ]; then
        echo "FAIL: $message"
        echo "  Expected: $expected"
        echo "  Actual: $actual"
        return 1
    fi
    echo "PASS: $message"
}

# Tests
test_string_length() {
    assert_eq 5 "$(string_length 'hello')" "String length"
}

test_string_length
```

### Documentation

```bash
#!/bin/bash
# @description Brief description of the script
# @usage script.sh [OPTIONS] <ARGUMENTS>
# @option -h, --help     Show help
# @option -v, --verbose  Enable verbose output
# @example
#   script.sh -v file.txt
```

### Error Handling

```bash
#!/bin/bash
set -euo pipefail

# Error handler
error_handler() {
    local line=$1
    local code=$2
    local command=$3
    echo "Error on line $line: '$command' exited with code $2"
}

trap 'error_handler ${LINENO} $? "$BASH_COMMAND"' ERR
```

---

## Difficulty Levels

### Beginner
- Simple file operations
- Basic text processing
- System information scripts
- Cron job scripts

### Intermediate
- Log analysis tools
- Backup automation
- Process management
- Network utilities

### Advanced
- Full CLI applications
- Configuration management
- DevOps automation
- Testing frameworks

---

## Resources

- [Bash Hackers Wiki](https://wiki.bash-hackers.org/)
- [ShellCheck](https://www.shellcheck.net/)
- [Explainshell](https://explainshell.com/)
- [Commandlinefu](https://www.commandlinefu.com/)
- [Bash One-Liners](https://bashone-liners.com/)
