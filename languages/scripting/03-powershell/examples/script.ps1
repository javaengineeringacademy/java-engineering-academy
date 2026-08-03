# PowerShell Scripting Example

# Variables
$Name = "Software Engineering Academy"
$Version = "1.0"
Write-Host "Welcome to $Name v$Version"

# Conditionals
if (Test-Path "C:\Windows") {
    Write-Host "Windows detected"
}

# Loops
$Languages = @("Java", "Python", "Go", "Rust", "TypeScript")
foreach ($Lang in $Languages) {
    Write-Host "Language: $Lang"
}

# Functions
function Get-Greeting {
    param([string]$Name)
    return "Hello, $Name!"
}
Get-Greeting -Name "Developer"

# HashTables
$Config = @{
    Database = "localhost:5432"
    Cache = "localhost:6379"
    Port = 8080
}
$Config.GetEnumerator() | ForEach-Object { Write-Host "$($_.Key): $($_.Value)" }

# Pipelines
Get-Process | Where-Object { $_.CPU -gt 10 } | Select-Object Name, CPU | Format-Table

# Error Handling
try {
    $Result = Get-Content "nonexistent.txt" -ErrorAction Stop
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Modules
Install-Module -Name Az -Scope CurrentUser -Force
Connect-AzAccount
