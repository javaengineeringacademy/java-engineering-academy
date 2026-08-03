import os
import json
import subprocess
import argparse
from datetime import datetime
from pathlib import Path

def backup_files(source, destination):
    """Backup files from source to destination."""
    source_path = Path(source)
    dest_path = Path(destination) / f"backup_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
    
    if not source_path.exists():
        print(f"Source {source} does not exist")
        return False
    
    dest_path.mkdir(parents=True, exist_ok=True)
    
    for file in source_path.rglob("*"):
        if file.is_file():
            relative = file.relative_to(source_path)
            dest_file = dest_path / relative
            dest_file.parent.mkdir(parents=True, exist_ok=True)
            dest_file.write_bytes(file.read_bytes())
            print(f"Backed up: {relative}")
    
    return True

def run_command(cmd):
    """Run a shell command and return output."""
    try:
        result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
        return result.stdout, result.stderr, result.returncode
    except Exception as e:
        return "", str(e), 1

def main():
    parser = argparse.ArgumentParser(description="Automation Script")
    parser.add_argument("--backup", nargs=2, metavar=("SOURCE", "DEST"), help="Backup files")
    parser.add_argument("--run", help="Run a command")
    args = parser.parse_args()
    
    if args.backup:
        backup_files(args.backup[0], args.backup[1])
    elif args.run:
        stdout, stderr, code = run_command(args.run)
        if stdout:
            print(stdout)
        if stderr:
            print(stderr, flush=True)
        exit(code)

if __name__ == "__main__":
    main()
