# C# Project Structure

## Standard .NET Solution Layout

```
MySolution/
|-- src/
|   |-- MyApp.Web/              # Web API or MVC
|   |   |-- Controllers/
|   |   |-- Models/
|   |   |-- Views/
|   |   |-- wwwroot/
|   |   |-- Program.cs
|   |   |-- appsettings.json
|   |   |-- MyApp.Web.csproj
|   |
|   |-- MyApp.Core/             # Business logic
|   |   |-- Services/
|   |   |-- Interfaces/
|   |   |-- Models/
|   |   |-- MyApp.Core.csproj
|   |
|   |-- MyApp.Infrastructure/   # Data access, external
|   |   |-- Data/
|   |   |-- Repositories/
|   |   |-- Migrations/
|   |   |-- MyApp.Infrastructure.csproj
|   |
|   |-- MyApp.Shared/           # Common utilities
|       |-- Extensions/
|       |-- Helpers/
|       |-- MyApp.Shared.csproj
|
|-- tests/
|   |-- MyApp.UnitTests/
|   |-- MyApp.IntegrationTests/
|
|-- docs/
|-- MySolution.sln
|-- .gitignore
|-- README.md
```

## Project File Structure

```xml
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net8.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="MediatR" Version="12.2.0" />
  </ItemGroup>

  <ItemGroup>
    <ProjectReference Include="..\MyApp.Core\MyApp.Core.csproj" />
  </ItemGroup>
</Project>
```

## Dependency Flow

```
Web (Presentation)
    |
    v
Core (Business Logic)
    |
    v
Infrastructure (Data/External)
    |
    v
Shared (Utilities)
```

## Folder Naming Conventions

- **Controllers/**: API endpoints or MVC controllers
- **Models/**: DTOs, view models, domain models
- **Services/**: Business logic implementations
- **Repositories/**: Data access implementations
- **Extensions/**: Extension methods
- **Middleware/**: Custom HTTP middleware
- **Filters/**: Action and exception filters

## Key Files

- `Program.cs`: Application entry point and DI configuration
- `appsettings.json`: Configuration values
- `.csproj`: Project dependencies and settings
- `.sln`: Solution file grouping projects
- `global.json`: SDK version pinning
- `Directory.Build.props`: Shared MSBuild properties
