# C# Installation Guide

## .NET SDK

The SDK includes the runtime, compilers, and development tools.

### Installation

```bash
# macOS
brew install dotnet

# Windows
winget install Microsoft.DotNet.SDK.8

# Linux (Ubuntu)
sudo apt-get install dotnet-sdk-8.0
```

### Verify Installation

```bash
dotnet --version
dotnet --list-sdks
dotnet --list-runtimes
```

## Visual Studio

Full-featured IDE for Windows and Mac.

### Recommended Extensions

- ReSharper or Roslynator
- GitHub Copilot
- CodeMaid
- NuGet Package Manager

### Workloads to Install

- .NET desktop development
- ASP.NET and web development
- Data storage and processing

## Visual Studio Code

Lightweight, cross-platform editor.

### Essential Extensions

```json
{
  "recommendations": [
    "ms-dotnettools.csharp",
    "ms-dotnettools.csdevkit",
    "ms-dotnettools.cssnippet",
    "formulahendry.dotnet-test-explorer"
  ]
}
```

### Key Bindings

| Action | Windows | macOS |
|--------|---------|-------|
| Build | Ctrl+Shift+B | Cmd+Shift+B |
| Run | F5 | F5 |
| Format | Ctrl+K,D | Cmd+K,D |

## JetBrains Rider

Cross-platform .NET IDE.

### Installation

```bash
# macOS
brew install --cask rider

# Windows
winget install JetBrains.Rider
```

## Project Templates

```bash
# List templates
dotnet new list

# Create projects
dotnet new console -n MyApp
dotnet new webapi -n MyApi
dotnet new blazor -n MyBlazor
dotnet new classlib -n MyLibrary

# Install additional templates
dotnet new install Microsoft.DotNet.Common.ProjectTemplates.8.0
```

## Global Tools

```bash
# Install tools
dotnet tool install -g dotnet-outdated
dotnet tool install -g dotnet-ef
dotnet tool install -g dotnet-format

# List installed tools
dotnet tool list -g
```

## Docker

```dockerfile
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src
COPY . .
RUN dotnet publish -c Release -o /app

FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /app
COPY --from=build /app .
ENTRYPOINT ["dotnet", "MyApp.dll"]
```
